package app.zingo.com.agentapp.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.fido.u2f.api.common.RequestParams;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import app.zingo.com.agentapp.Activities.LoginActivity;
import app.zingo.com.agentapp.Model.TravellerAgentProfiles;
import app.zingo.com.agentapp.Model.UserRole;
import app.zingo.com.agentapp.R;
import app.zingo.com.agentapp.Utils.Constants;
import app.zingo.com.agentapp.Utils.PreferenceHandler;
import app.zingo.com.agentapp.Utils.RealPathUtil;
import app.zingo.com.agentapp.Utils.ThreadExecuter;
import app.zingo.com.agentapp.Utils.Util;
import app.zingo.com.agentapp.WebApi.LoginApi;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



/**
 * Created by CSC on 1/5/2018.
 */

public class ProfileInfoFragment extends Fragment {

    TextInputEditText mName,mEmail,mMobile,mAddress,mCountry,mState,mCity,mPinCode,mNationality;
    CircleImageView mProfilePhoto;
    Button mUpdate;
    TravellerAgentProfiles dto;

    private static final int REQUEST_CAMERA = 1,REQUEST_GALLERY = 2;

    public ProfileInfoFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstance){

        View v = inflater.inflate(R.layout.enter_profile_layout, container, false);
        mName = (TextInputEditText)v.findViewById(R.id.add_lead_first_name);
        mEmail = (TextInputEditText)v.findViewById(R.id.email_id);
        mMobile = (TextInputEditText)v.findViewById(R.id.mobile_number);
        mAddress = (TextInputEditText)v.findViewById(R.id.address);
        mPinCode = (TextInputEditText)v.findViewById(R.id.pincode);
        mProfilePhoto = (CircleImageView) v.findViewById(R.id.profile_photo);
        mUpdate = (Button)v.findViewById(R.id.submit);

        mUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });

        mProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        getProfileById();
        return v;
    }

    public void validate(){
        String name = mName.getText().toString();
        String email = mEmail.getText().toString();
        String mobile = mMobile.getText().toString();
        String address = mAddress.getText().toString();
        String pincode = mPinCode.getText().toString();

        if(name.isEmpty()){
            Toast.makeText(getActivity(), "Field should not be empty", Toast.LENGTH_SHORT).show();
        }else if(email.isEmpty()){
            Toast.makeText(getActivity(), "Field should not be empty", Toast.LENGTH_SHORT).show();
        }else if(mobile.isEmpty()){
            Toast.makeText(getActivity(), "Field should not be empty", Toast.LENGTH_SHORT).show();
        }else if(address.isEmpty()){
            Toast.makeText(getActivity(), "Field should not be empty", Toast.LENGTH_SHORT).show();
        }else if(pincode.isEmpty()){
            Toast.makeText(getActivity(), "Field should not be empty", Toast.LENGTH_SHORT).show();
        }else{
            dto.setFirstName(name);
            dto.setLastName("");
            dto.setMiddleName("");
            dto.setEmail(email);
            dto.setPhoneNumber(mobile);
            dto.setPinCode(pincode);
            dto.setAddress(address);
            updatePhoto(dto);
        }
    }

    private void getProfileById() {

        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Loading");
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                LoginApi profileApi = Util.getClient().create(LoginApi.class);
                String authenticationString = Util.getToken(getActivity());
                Call<TravellerAgentProfiles> getProfile = profileApi.getProfileByID(authenticationString, PreferenceHandler.getInstance(getActivity()).getUserId());
                //System.out.println("hotelid = "+hotelid);
                System.out.println();

                getProfile.enqueue(new Callback<TravellerAgentProfiles>() {
                    @Override
                    public void onResponse(Call<TravellerAgentProfiles> call, Response<TravellerAgentProfiles> response) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        if(response.code() == 200)
                        {
                             dto = response.body();

                            if(dto != null)
                            {
                               if(dto.getMiddleName()==null||dto.getLastName()==null){
                                   mName.setText(dto.getFirstName());
                               }else{
                                   mName.setText(dto.getFirstName()+" "+dto.getLastName());
                               }
                               mEmail.setText(dto.getEmail());
                               mMobile.setText(""+dto.getPhoneNumber());

                                if(dto.getPinCode()==null){
                                    mAddress.setText("");
                                }else{
                                    mAddress.setText(dto.getAddress());
                                }
                               if(dto.getPinCode()==null){
                                   mPinCode.setText("");
                               }else{
                                   mPinCode.setText(""+dto.getPinCode());
                               }

                                if(dto.getProfilePhoto() != null && !dto.getProfilePhoto().isEmpty())
                                {
                                    if(dto.getProfilePhoto().equalsIgnoreCase("test")){
                                        mProfilePhoto.setImageResource(R.drawable.icons_profile);
                                    }else{
                                        mProfilePhoto.setImageBitmap(Util.convertToBitMap(dto.getProfilePhoto()));
                                    }
                                   // mUserProfileImage.setEnabled(false);
                                }else{
                                    mProfilePhoto.setImageResource(R.drawable.icons_profile);
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<TravellerAgentProfiles> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                    }
                });
            }
        });
    }

    public void selectImage()
    {
        final String[] imageSelectionArray = {"Gallery","Take Photo","Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select Photo");
        builder.setCancelable(false);
        builder.setItems(imageSelectionArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(imageSelectionArray[which].equals("Gallery"))
                {
                    boolean result=Util.checkPermissionOfCamera(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE
                            ,"This App Needs Storage Permission");
                    if(result)
                        gotoGallery();
                }
                else if(imageSelectionArray[which].equals("Take Photo"))
                {
                    boolean result=Util.checkPermissionOfCamera(getActivity(),Manifest.permission.CAMERA,
                            "This Application Needs Camera Permission");
                    if(result)
                        gotoCamera();
                }
                else
                {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void gotoCamera() {

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent,REQUEST_CAMERA);
    }

    private void gotoGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"SELECT_FILE"),REQUEST_GALLERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK)
        {
            if(requestCode == REQUEST_CAMERA)
            {
                onImageCaptureResult(data);
            }
            else if(requestCode == REQUEST_GALLERY)
            {
                onSelectImageFromGalleryResult(data);
            }
        }
    }

    private void onImageCaptureResult(Intent data) {
        if(data != null)
        {
            Uri selectedImageUri = data.getData( );
            String picturePath = getPath( getActivity( ).getApplicationContext( ), selectedImageUri );
            System.out.println("Picture=="+picturePath);




            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,60,byteArrayOutputStream);
            if(bitmap != null)
            {
                bitmap = Util.getResizedBitmap(bitmap, 400);
                dto.setProfilePhoto(Util.convertToBase64String(bitmap));
                updatePhoto(dto);
                mProfilePhoto.setImageBitmap(bitmap);
            }
        }
    }

    private void onSelectImageFromGalleryResult(Intent data) {

        if(data != null)
        {
            Uri selectedImageUri = data.getData( );
            String picturePath = getPath( getActivity( ).getApplicationContext( ), selectedImageUri );
            System.out.println("Picture Gal=="+picturePath);

            String realPath = RealPathUtil.getRealPathFromURI_API19(getActivity(), data.getData());
            System.out.println("Picture=="+realPath);
            System.out.println("Picture=="+data.getData().getPath());

            Bitmap bm = null;
            try {
                bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),data.getData());
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG,60,byteArrayOutputStream);
                if(bm != null)
                {
                    bm = Util.getResizedBitmap(bm, 400);
                    dto.setProfilePhoto(Util.convertToBase64String(bm));
                    updatePhoto(dto);
                    mProfilePhoto.setImageBitmap(bm);
                }
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }

        }
    }




    private void updatePhoto(TravellerAgentProfiles up) {

        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getResources().getString(R.string.loader_message));
        dialog.setCancelable(true);
        dialog.show();

        String auth_string = Util.getToken(getActivity());

        LoginApi profileApi = Util.getClient().create(LoginApi.class);
        Call<String> res = profileApi.updateProfileById(auth_string,PreferenceHandler.getInstance(getActivity()).getUserId(),up);
        res.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if(dialog != null)
                {
                    dialog.dismiss();
                }
                if(response.code() == 204 ||response.code() == 200 ||response.code() == 201)
                {
                    Toast.makeText(getActivity(), "Updated Successfully", Toast.LENGTH_SHORT).show();
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    SharedPreferences.Editor spe = sp.edit();
                    spe.putInt(Constants.USER_ID, dto.getTravellerAgentProfileId());
                    PreferenceHandler.getInstance(getActivity()).setUserId(dto.getTravellerAgentProfileId());

                    if(dto.getMiddleName()==null||dto.getLastName()==null){
                        PreferenceHandler.getInstance(getActivity()).setFullName(dto.getFirstName());
                    }else{
                        PreferenceHandler.getInstance(getActivity()).setFullName(dto.getFirstName()+" "+dto.getLastName());
                    }

                    PreferenceHandler.getInstance(getActivity()).setPhoneNumber(dto.getPhoneNumber());
                    PreferenceHandler.getInstance(getActivity()).setUserName(dto.getUserName());
                    spe.putString("FirstName", dto.getFirstName());
                    spe.putString("MiddleName", dto.getMiddleName());
                    spe.putString("LastName", dto.getLastName());
                    spe.putString("UserName", dto.getUserName());
                    spe.putString("Password", dto.getPassword());

                    spe.putString("PlaceName", dto.getPlaceName());
                    spe.putString("Email", dto.getEmail());
                    spe.putString("PhoneNumber", dto.getPhoneNumber());
                    spe.putString("Address", dto.getAddress());
                    spe.putString("PinCode", dto.getPinCode());
                    spe.putInt("UserRoleId", dto.getUserRoleId());
                    spe.apply();


                    UserRole userRole = dto.get_userRole();
                    if(userRole != null)
                    {
                        System.out.println("Unique id = "+userRole.getUserRoleUniqueId());
                        PreferenceHandler.getInstance(getActivity()).setUserRoleUniqueID(userRole.getUserRoleUniqueId());
                    }
                    // mUserProfileImage.setEnabled(false);
                }
                else
                {
                    Toast.makeText(getActivity(),response.message(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                if(dialog != null)
                {
                    dialog.dismiss();
                }
                Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    public static String getPath(Context context, Uri uri ) {
        String result = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver( ).query( uri, proj, null, null, null );
        if(cursor != null){
            if ( cursor.moveToFirst( ) ) {
                int column_index = cursor.getColumnIndexOrThrow( proj[0] );
                result = cursor.getString( column_index );
            }
            cursor.close( );
        }
        if(result == null) {
            result = "Not found";
        }
        return result;
    }




}
