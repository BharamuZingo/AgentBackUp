package app.zingo.com.agentapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import app.zingo.com.agentapp.CustomViews.CustomEditextTTF;
import app.zingo.com.agentapp.CustomViews.CustomFontTextView;
import app.zingo.com.agentapp.MainActivity;
import app.zingo.com.agentapp.Model.City;
import app.zingo.com.agentapp.Model.Profile1;
import app.zingo.com.agentapp.Model.ReferCodeModel;
import app.zingo.com.agentapp.Model.TravellerAgentProfiles;
import app.zingo.com.agentapp.Model.UserRole;
import app.zingo.com.agentapp.R;
import app.zingo.com.agentapp.Services.TrackGPS;
import app.zingo.com.agentapp.Utils.Constants;
import app.zingo.com.agentapp.Utils.PreferenceHandler;
import app.zingo.com.agentapp.Utils.ThreadExecuter;
import app.zingo.com.agentapp.Utils.Util;
import app.zingo.com.agentapp.WebApi.LoginApi;
import app.zingo.com.agentapp.WebApi.UserRoleApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    CustomEditextTTF first_name,email_id,phone_no,user_name,password,confirm_password,refercode;//
    //private TextView city;
    CustomFontTextView mGuest,mAgent;
    private RadioButton mProfileMale,mProfileFemale,mProfileOthers;
    private CardView password_card,confirm_password_card;
    private CustomFontTextView submit;
    private TravellerAgentProfiles p;
   // private List<City> listCities;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private String uniqueId;
    private int roleId = 5;
    double referedAmount = 0;

   // String enc;

    //Intent values
   String mPhoneNumber,type;
    TrackGPS gps;
    int referProfileId=0;
    double referAmountOtherProfile = 0;
    double walletAmountOther = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mPhoneNumber = getIntent().getStringExtra("PhoneNumber");


        gps = new TrackGPS(SignUpActivity.this);
      //  listCities = new ArrayList<>();
        //getCities();
        //mPhoneNumber = getIntent().getStringExtra("PhoneNumber");

        first_name = (CustomEditextTTF) findViewById(R.id.first_name);
        //  middle_name = (EditText) findViewById(R.id.middle_name);
        //last_name = (EditText) findViewById(R.id.last_name);
        email_id = (CustomEditextTTF) findViewById(R.id.email_id_profile);
        phone_no = (CustomEditextTTF) findViewById(R.id.phone_no);
        refercode = (CustomEditextTTF) findViewById(R.id.refer_code);
       phone_no.setText(mPhoneNumber);
       phone_no.setEnabled(false);

        user_name = (CustomEditextTTF) findViewById(R.id.user_name_profile);
        password = (CustomEditextTTF) findViewById(R.id.password);
        confirm_password = (CustomEditextTTF) findViewById(R.id.confirm_password);

        submit = (CustomFontTextView) findViewById(R.id.submit_profile);
        mProfileFemale = (RadioButton) findViewById(R.id.profile_female);
        mProfileMale = (RadioButton) findViewById(R.id.profile_male);
        mProfileOthers = (RadioButton) findViewById(R.id.profile_transgender);

        mGuest = (CustomFontTextView) findViewById(R.id.login_guest);
        mGuest.setVisibility(View.GONE);
        mAgent = (CustomFontTextView) findViewById(R.id.login_agent);
        mAgent.setVisibility(View.GONE);

        //mUserGender = (EditText) findViewById(R.id.user_gender);

        type = getIntent().getStringExtra("Type");

        Intent in = getIntent();
        boolean isFb = in.getBooleanExtra("ISFB",false);
        phone_no.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        //mHotelMobileNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        first_name.setText(in.getStringExtra("FIRST_NAME"));
        //  middle_name.setText(in.getStringExtra("MIDDLE_NAME"));
        // last_name.setText(in.getStringExtra("LAST_NAME"));
        uniqueId = in.getStringExtra("FB_ID");
        email_id.setText(in.getStringExtra("EMAIL"));

//        ArrayAdapter<String> adapter = new ArrayAdapter<String>
//                (this, android.R.layout.simple_list_item_1, PLACES);

//        first_name.setText(in.getStringExtra(""));



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(type !=null)
                {
                    validate();
                }

            }
        });

        /*mAgent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mGuest.isSelected())
                {
                    mGuest.setSelected(false);
                    mAgent.setSelected(true);
                }
                else
                {
                    mAgent.setSelected(true);
                }
            }
        });

        mGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAgent.isSelected())
                {
                    mAgent.setSelected(false);
                    mGuest.setSelected(true);
                }
                else
                {
                    mGuest.setSelected(true);
                }
            }
        });*/


       /* city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY*//*MODE_FULLSCREEN*//*)
                                    .build(SignUpActivity.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                    e.printStackTrace();
                }
            }
        });*/

    }

    private String TAG = "PlaceSeach";
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);

                // city.setText(place.getName());
                //address.setText(place.getAddress());
                Log.i(TAG, "Place: " + place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    public void validate()
    {
        /*String male = mProfileMale.getText().toString();
        String female = mProfileFemale.getText().toString();
        String others = mProfileOthers.getText().toString();*/
        // String lName = last_name.getText().toString();
        String fName = first_name.getText().toString();
        String uName = user_name.getText().toString();
        String email = email_id.getText().toString();
        String phoneNumber = phone_no.getText().toString();
        // String pPlace = city.getText().toString();
        // String pAddress = address.getText().toString();
        String pPassword = password.getText().toString();
        //String pPincode = pin_code.getText().toString();
        String pEnterPassword = confirm_password.getText().toString();


        if(fName == null || fName.isEmpty())
        {
            first_name.setError("Please Enter Name");
            first_name.requestFocus();
        }
       /* else if(lName == null || lName.isEmpty())
        {
            last_name.setError(getResources().getString(R.string.last_name_validation_message));
            last_name.requestFocus();
        }*/
        else if(!mProfileMale.isChecked() && !mProfileFemale.isChecked() && !mProfileOthers.isChecked())
        {
            Toast.makeText(SignUpActivity.this,getResources().getString(R.string.gender_validation_message),Toast.LENGTH_SHORT).show();
        }
        else if(uName == null || uName.isEmpty())
        {
            user_name.setError(getResources().getString(R.string.unique_name_validation_message));
            user_name.requestFocus();
        }
        else if(email == null || email.isEmpty() )
        {
            email_id.setError(getResources().getString(R.string.email_validation_message));
            email_id.requestFocus();
        }
        else if(!isValidEmail(email))
        {
            email_id.setError(getResources().getString(R.string.email_validation_message));
            email_id.requestFocus();
        }
        else if(phoneNumber == null || phoneNumber.isEmpty() || phoneNumber.length() != 10)
        {
            phone_no.setError(getResources().getString(R.string.phone_number_validation_message));
            phone_no.requestFocus();
        }
       /* else if(pPlace == null || pPlace.isEmpty())
        {
            city.setError("Please Enter Your Place");
            city.requestFocus();
        }
        else if(pAddress == null || pAddress.isEmpty())
        {
            address.setError("Please Enter Your Address");
            address.requestFocus();
        }
        else if(pPincode == null || pPincode.isEmpty())
        {
            pin_code.setError("Please Enter Pin Code");
            pin_code.requestFocus();
        }*/
        else if(pPassword == null || pPassword.isEmpty())
        {
            password.setError("Please Enter Password");
            password.requestFocus();
        }
        else if(pEnterPassword == null || pEnterPassword.isEmpty())
        {
            confirm_password.setError("Please Enter Confirm Password");
            confirm_password.requestFocus();
        }
        else if(!pPassword.equals(pEnterPassword))
        {
            confirm_password.setError("Password and Confirm Password Should be Same");
            confirm_password.requestFocus();
        }
        else
        {
            //encrypt(pPassword);
            register();
        }


    }

    private ProgressDialog progressDialog;

    public void register() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("please wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();


        if (isValidEmail(email_id.getText().toString())&& isValidUserName(user_name.getText().toString()) &&
                isValidPhone(phone_no.getText().toString())
                && isValidPassword(password.getText().toString())) {
            if (isValidConfirmPass(password.getText().toString(),confirm_password.getText().toString())) {
//                checkEmailAvailablity();
//                checkPhoneAvailablity();
//                loginThreadCall();
//                if (!){
                boolean res1 = checkEmailAvailablity();
//                }else Toast.makeText(SignUpActivity.this,"Email already available.",Toast.LENGTH_SHORT).show();

            }
            else {
                progressDialog.dismiss();
                Toast.makeText(SignUpActivity.this,"Password dose not match.",Toast.LENGTH_SHORT).show();
            }
        }else {
            progressDialog.dismiss();
            Toast.makeText(SignUpActivity.this,"Please Enter valid credentials.",Toast.LENGTH_SHORT).show();
        }

    }

    /*public void encrypt(String pwd){
        try
        {
            // String text = "Hello World";
            String key = "Bar12345Bar12345"; // 128 bit key
            // Create key and cipher
            Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            // encrypt the text
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            byte[] encrypted = cipher.doFinal(pwd.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b: encrypted) {
                sb.append((char)b);
            }

            // the encrypted String
            enc = sb.toString();
            System.out.println("encrypted:" + enc);

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }*/

  /*  private void getCities(){
        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                String authenticationString = Util.getToken(SignUpActivity.this);
                IRegistrasionService apiService =
                        Util.getClient().create(IRegistrasionService.class);
                Call<ArrayList<City>> call = apiService.fetchCities(authenticationString);

                call.enqueue(new Callback<ArrayList<City>>() {
                    @Override
                    public void onResponse(Call<ArrayList<City>> call, Response<ArrayList<City>> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                        int statusCode = response.code();
                        if (statusCode == 200) {

                            listCities = response.body();
//                            Object dto = response.body();
//                            listCities.add(dto);
                            if (progressDialog!=null)
                                progressDialog.dismiss();


                        }else {
                            if (progressDialog!=null)
                                progressDialog.dismiss();
                            Toast.makeText(SignUpActivity.this, "Login failed due to status code:"+statusCode, Toast.LENGTH_SHORT).show();
                        }
//                callGetStartEnd();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<City>> call, Throwable t) {
                        // Log error here since request failed
                        if (progressDialog!=null)
                            progressDialog.dismiss();
                        Log.e("TAG", t.toString());
                    }
                });
            }


        });
    }*/

    private void loginThreadCall(){

        p = new TravellerAgentProfiles();

        p.setFirstName(first_name.getText().toString());
        // p.setMiddleName(middle_name.getText().toString());
        // p.setLastName(last_name.getText().toString());
        p.setEmail(email_id.getText().toString());
        p.setPhoneNumber(phone_no.getText().toString());
        if(mProfileMale.isChecked())
        {
            p.setUserGender("Male");
        }
        else if(mProfileFemale.isChecked())
        {
            p.setUserGender("Female");
        }
        else if(mProfileOthers.isChecked())
        {
            p.setUserGender("Others");
        }
//        p.set_city(city.getText().toString());
//        p.setPinCode(pin_code.getText().toString());
          p.setAddress("");
            p.setPinCode("");
        // String cN = city.getText().toString();
        //p.setPlaceName(cN);
        //c.setCityName(cN);

        /*int cityId = 0;
        for(City d : listCities){
            if(d.getCityName() != null && d.getCityName().contains(cN)){
                cityId = d.getCityId();
            }
            //something here
        }
        c.setCityId(cityId);
        p.setCityId(1);*/
        if(type.equalsIgnoreCase("agent"))
        {
            p.setUserRoleId(roleId);
        }
        else if(type.equalsIgnoreCase("traveller"))
        {
            p.setUserRoleId(6);
        }

        //-----------------------------hard coded-------------------
        //p.setPassword(enc);
        p.setPassword(password.getText().toString());
        p.setUserName(user_name.getText().toString());
        if(refercode.getText().toString()!=null && !refercode.getText().toString().isEmpty()){
            p.setReferralCodeUsed(refercode.getText().toString());
            if(type.equalsIgnoreCase("agent"))
            {
                p.setWalletBalance((int)referedAmount);
                p.setReferralAmount((int)referedAmount);
            }
            else if(type.equalsIgnoreCase("traveller"))
            {
                p.setWalletBalance(50);
                p.setReferralAmountForOtherProfile(50);
                p.setReferralAmount(50);

            }

            System.out.println("Refer= "+referedAmount);
        }

        // p.setProfilePhoto("test");
        // p.setFrontSidePhoto("test");
        // p.setBackSidePhoto("test");
        p.setStatus("Disabled");
        p.setPlans("Basic");




//          gson = new Gson();
//        final String j = gson.toJson(p);

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {


                LoginApi apiService =
                        Util.getClient().create(LoginApi.class);

                String authenticationString = Util.getToken(SignUpActivity.this);
                Call<TravellerAgentProfiles> call = apiService.loginApi(Constants.auth_string,p);

                call.enqueue(new Callback<TravellerAgentProfiles>() {
                    @Override
                    public void onResponse(Call<TravellerAgentProfiles> call, Response<TravellerAgentProfiles> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                        if (progressDialog!=null)
                            progressDialog.dismiss();
                        int statusCode = response.code();
                        if (statusCode == 200 || statusCode == 201) {


                            TravellerAgentProfiles dto = response.body();
                            SharedPreferences sp  = PreferenceManager.getDefaultSharedPreferences(SignUpActivity.this);
                            SharedPreferences.Editor spe = sp.edit();
                            spe.putInt(Constants.USER_ID,dto.getTravellerAgentProfileId());
                            PreferenceHandler.getInstance(SignUpActivity.this).setUserId(dto.getTravellerAgentProfileId());
                            PreferenceHandler.getInstance(SignUpActivity.this).setPhoneNumber(dto.getPhoneNumber());
                            PreferenceHandler.getInstance(SignUpActivity.this).setReferedAmount((int)referedAmount);

                            if(dto.getMiddleName()==null||dto.getLastName()==null){
                                PreferenceHandler.getInstance(SignUpActivity.this).setFullName(dto.getFirstName());
                            }else{
                                PreferenceHandler.getInstance(SignUpActivity.this).setFullName(dto.getFirstName()+" "+dto.getLastName());
                            }
                            //PreferenceHandler.getInstance(SignUpActivity.this).setUserFullName(dto.getFirstName()+" "+dto.getLastName());
                            PreferenceHandler.getInstance(SignUpActivity.this).setUserName(dto.getUserName());
                           // PreferenceHandler.getInstance(SignUpActivity.this).setUserPlans(dto.getPlans());
                           // PreferenceHandler.getInstance(SignUpActivity.this).setUserStatus(dto.getStatus());
                            spe.putString("FirstName",dto.getFirstName());
                            spe.putString("MiddleName",dto.getMiddleName());
                            spe.putString("LastName",dto.getLastName());
                            spe.putString("UserName",dto.getUserName());
                            spe.putString("Password",dto.getPassword());
                            spe.putString("PlaceName",dto.getPlaceName());
                            spe.putString("Email",dto.getEmail());
                            spe.putString("PhoneNumber",dto.getPhoneNumber());
                            spe.putString("Address",dto.getAddress());
                            spe.putString("PinCode",dto.getPinCode());
                            spe.putInt("UserRoleId",dto.getUserRoleId());
                            spe.apply();

                            updateProfile("ZINGO"+dto.getTravellerAgentProfileId(),dto);
//                            String status = dto.getAddress();
                            UserRole userRole = dto.get_userRole();
                            if(userRole != null)
                            {
                                PreferenceHandler.getInstance(SignUpActivity.this).setUserRoleUniqueID(userRole.getUserRoleUniqueId());
                                if(userRole.getUserRoleUniqueId().equalsIgnoreCase("Luci-Agent"))
                                {
                                    System.out.println("Unique id = "+userRole.getUserRoleUniqueId());
                                    PreferenceHandler.getInstance(SignUpActivity.this).setUserRoleUniqueID(userRole.getUserRoleUniqueId());
                                    Toast.makeText(SignUpActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(SignUpActivity.this, ComingSoonActivity.class);
                                    i.putExtra("OtherProfileId",referProfileId);
                                    i.putExtra("ReferAmountOther",referAmountOtherProfile);
                                    i.putExtra("WalletAmountOther",walletAmountOther);
                                    startActivity(i);
                                    finish();
                                }
                                else
                                {
                                    System.out.println("Unique id = "+userRole.getUserRoleUniqueId());
                                    PreferenceHandler.getInstance(SignUpActivity.this).setUserRoleUniqueID(userRole.getUserRoleUniqueId());
                                    Toast.makeText(SignUpActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(SignUpActivity.this, MainActivity.class);
                                    i.putExtra("OtherProfileId",referProfileId);
                                    i.putExtra("ReferAmountOther",referAmountOtherProfile);
                                    i.putExtra("WalletAmountOther",walletAmountOther);
                                    startActivity(i);
                                    finish();
                                }
                            }
                            else {
                                getUserRole(dto.getUserRoleId());
                            }



                            /*Intent i = new Intent(SignUpActivity.this, ComingSoonActivity.class);
                            startActivity(i);
                            finish();*/
                            /*Toast.makeText(SignUpActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                            */
                        }else {

                            Toast.makeText(SignUpActivity.this, "Registration failed due to status code:"+statusCode, Toast.LENGTH_SHORT).show();
                        }
//                callGetStartEnd();
                    }

                    @Override
                    public void onFailure(Call<TravellerAgentProfiles> call, Throwable t) {
                        // Log error here since request failed
                        if (progressDialog!=null)
                            progressDialog.dismiss();
                        Log.e("TAG", t.toString());
                    }
                });
            }
        });
    }

    private void updateProfile(final String s, final TravellerAgentProfiles dto) {

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                new ThreadExecuter().execute(new Runnable() {
                    @Override
                    public void run() {


                        LoginApi apiService =
                                Util.getClient().create(LoginApi.class);
                        TravellerAgentProfiles profiles = dto;
                        profiles.setReferralCodeToUseForOtherProfile(s);
                        String authenticationString = Util.getToken(SignUpActivity.this);
                        Call<String> call = apiService.updateProfileById(Constants.auth_string,dto.getTravellerAgentProfileId(),profiles);

                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                                if(response.code()== 200 || response.code() == 201 || response.code() == 204)
                                {

                                }
//                callGetStartEnd();
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                // Log error here since request failed
                                if (progressDialog!=null)
                                    progressDialog.dismiss();
                                Log.e("TAG", t.toString());
                            }
                        });
                    }
                });
            }
        });

    }

    private void getUserRole(final int i) {

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle(getResources().getString(R.string.loader_message));
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {


                UserRoleApi apiService =
                        Util.getClient().create(UserRoleApi.class);

                String authenticationString = Util.getToken(SignUpActivity.this);//"Basic " +  Base64.encodeToString(authentication.getBytes(), Base64.NO_WRAP);
                Call<UserRole> call = apiService.apiGetRolesById(authenticationString,i);

                call.enqueue(new Callback<UserRole>() {
                    @Override
                    public void onResponse(Call<UserRole> call, Response<UserRole> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                        if (dialog!=null)
                            dialog.dismiss();
                        int statusCode = response.code();
                        if (statusCode == 200 || statusCode == 201) {



//                            String status = dto.getAddress();
                            UserRole userRole = response.body();
                            if(userRole != null)
                            {
                                if(type != null && type.equalsIgnoreCase("agent"))
                                {
                                    System.out.println("Unique id = "+userRole.getUserRoleUniqueId());
                                    PreferenceHandler.getInstance(SignUpActivity.this).setUserRoleUniqueID(userRole.getUserRoleUniqueId());
                                    Toast.makeText(SignUpActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(SignUpActivity.this, ComingSoonActivity.class);
                                    i.putExtra("OtherProfileId",referProfileId);
                                    i.putExtra("ReferAmountOther",referAmountOtherProfile);
                                    i.putExtra("WalletAmountOther",walletAmountOther);
                                    startActivity(i);
                                    finish();
                                }
                                else
                                {
                                    System.out.println("Unique id = "+userRole.getUserRoleUniqueId());
                                    PreferenceHandler.getInstance(SignUpActivity.this).setUserRoleUniqueID(userRole.getUserRoleUniqueId());
                                    Toast.makeText(SignUpActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(SignUpActivity.this, MainActivity.class);
                                    i.putExtra("OtherProfileId",referProfileId);
                                    i.putExtra("ReferAmountOther",referAmountOtherProfile);
                                    i.putExtra("WalletAmountOther",walletAmountOther);
                                    startActivity(i);
                                    finish();
                                }
                            }
                            else
                            {

                            }



                        }else {
                            /*if (progressDialog!=null)
                                progressDialog.dismiss();*/
                            Toast.makeText(SignUpActivity.this, "Registration failed due to status code:"+statusCode, Toast.LENGTH_SHORT).show();
                        }
//                callGetStartEnd();
                    }

                    @Override
                    public void onFailure(Call<UserRole> call, Throwable t) {
                        // Log error here since request failed
                        if (dialog!=null)
                            dialog.dismiss();
                        Log.e("TAG", t.toString());
                    }
                });
            }
        });


    }


    public final static boolean isValidEmail(CharSequence target)
    {
        if (TextUtils.isEmpty(target))
        {
            return false;
        } else {
            System.out.println(Patterns.EMAIL_ADDRESS.matcher(target).matches());
            return Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public final static boolean isValidName(String target)
    {
        if (TextUtils.isEmpty(target))
        {
            return false;
        } else {
            return target.matches("[A-Za-z][^&*:;<>.,/]*");
        }
    }

    public final static boolean isValidPhone(String target)
    {
        if (TextUtils.isEmpty(target))
        {
            return false;
        } else if (target.length()==10){
            if (Patterns.PHONE.matcher(target).matches()){
                return target.matches("[0-9][^-. ]*");
            }
            return false;
        }else return false;
    }
    public final static boolean isValidPin(String target)
    {
        if (TextUtils.isEmpty(target))
        {
            return false;
        } else if (target.length()==6){
            return target.matches("^[0-9]*$");
        }else return false;
    }

    public final static boolean isValidUserName(String target)
    {
        if (TextUtils.isEmpty(target))
        {
            return false;
        } else {
            return target.matches("^[a-zA-Z][^/ ]*$");
        }//else return false;
    }

    public final static boolean isValidPassword(String target)
    {
        if (TextUtils.isEmpty(target))
        {
            return false;
        } else return true;
    }

    public final static boolean isValidConfirmPass(String target,String target2)
    {
        if (TextUtils.isEmpty(target) && TextUtils.isEmpty(target2))
        {
            return false;
        } else if (target.equals(target2)){
            return true;
        }else return false;
    }

    public boolean validate(EditText _emailText, EditText _passwordText ) {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
//          _emailText.requestFocus();
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    @Override
    protected void onResume() {
        super.onResume();

        email_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

//                isValidEmail();

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                /*if(!isValidEmail(email_id.getText().toString()))
                {
                    email_id.requestFocus();
                    email_id.setError(getResources().getString(R.string.email_validation_message));
                }*/
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        first_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                isValidEmail();

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!isValidName(first_name.getText().toString()))
                {
                    first_name.requestFocus();
                    first_name.setError("Enter Correct Name ..!!");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        phone_no.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                isValidEmail();

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!isValidPhone(phone_no.getText().toString()))
                {
                    phone_no.requestFocus();
                    phone_no.setError("Enter Correct Phone ..!!");


                }else{
                    // getTravelerByPhone(phone_traveler.getText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

       /* pin_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                isValidEmail();

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!isValidPin(pin_code.getText().toString()))
                {
                    pin_code.requestFocus();
                    pin_code.setError("Enter Correct Pin Code ..!!");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/

        user_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                isValidEmail();

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!isValidUserName(user_name.getText().toString()))
                {
                    user_name.requestFocus();
                    user_name.setError("Enter valid user name ..!!");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                isValidEmail();

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!isValidPassword(password.getText().toString()))
                {
                    password.requestFocus();
                    password.setError("Should not be empty ..!!");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        confirm_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                isValidEmail();

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!isValidConfirmPass(password.getText().toString(),confirm_password.getText().toString()))
                {
                    confirm_password.requestFocus();
                    confirm_password.setError("Password Does Not Match");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    boolean response_str ;

    private boolean checkEmailAvailablity(){


        final TravellerAgentProfiles p1 = new TravellerAgentProfiles();

        p1.setEmail(email_id.getText().toString());

//        new ThreadExecuter().execute(new Runnable() {
//            @Override
//            public void run() {
        String authenticationString = Util.getToken(SignUpActivity.this);

        LoginApi apiService =
                Util.getClient().create(LoginApi.class);
        Call<String> call = apiService.getProfileByEmail(Constants.auth_string,p1);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                int statusCode = response.code();
                if (statusCode == 200) {

                    String  s = response.body();

                    if (s.equals("Profile Exist")){
                        email_id.requestFocus();
                        email_id.setError("Email Already exist..!!");
                        response_str = true;
                        if (progressDialog!=null)
                            progressDialog.dismiss();
                    }else {
                        if(refercode.getText().toString()==null||refercode.getText().toString().isEmpty()){
                            //boolean res2 = checkPhoneAvailablity();
                            boolean res3 = checkUserNameAvailability();

                        }else{
                            boolean res2 = checkReferalCode();
                        }
                       // boolean res2 = checkPhoneAvailablity();
                        response_str = false;
                    }
//                            Object listCities = response.body();
//                            Object dto = response.body();
//                            listCities.add(dto);



                }else {
                    if (progressDialog!=null)
                        progressDialog.dismiss();
                    Toast.makeText(SignUpActivity.this, "checking failed due to status code:"+statusCode, Toast.LENGTH_SHORT).show();
                }
//                callGetStartEnd();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Log error here since request failed
                if (progressDialog!=null)
                    progressDialog.dismiss();
                Log.e("TAG", t.toString());
            }
        });
//            }


//        });


        return response_str;
    }


    private boolean checkReferalCode(){


        final ReferCodeModel p1 = new ReferCodeModel();

        p1.setReferralCode(refercode.getText().toString());

//        new ThreadExecuter().execute(new Runnable() {
//            @Override
//            public void run() {
        String authenticationString = Util.getToken(SignUpActivity.this);

        LoginApi apiService =
                Util.getClient().create(LoginApi.class);
        Call<ArrayList<TravellerAgentProfiles>> call = apiService.getProfileByReferCode(Constants.auth_string,p1);

        call.enqueue(new Callback<ArrayList<TravellerAgentProfiles>>() {
            @Override
            public void onResponse(Call<ArrayList<TravellerAgentProfiles>> call, Response<ArrayList<TravellerAgentProfiles>> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();

                int statusCode = response.code();
                if (statusCode == 200) {

                    ArrayList<TravellerAgentProfiles> s = response.body();

                    if (s!=null && s.size() != 0) {
                       // boolean res2 = checkPhoneAvailablity();
                        boolean res3 = checkUserNameAvailability();
                        referedAmount = s.get(0).getReferralAmount();
                        referProfileId = s.get(0).getTravellerAgentProfileId();
                        referAmountOtherProfile = s.get(0).getReferralAmountForOtherProfile();
                        walletAmountOther = s.get(0).getWalletBalance();
                        System.out.println("Refered Amount=="+s.get(0).getReferralAmount());
                        response_str = false;
                    }
                    else
                    {
                        //boolean res2 = checkPhoneAvailablity();
                        if (progressDialog!=null)
                            progressDialog.dismiss();
                        response_str = true;
                        Toast.makeText(SignUpActivity.this, "Invalid referral code entered", Toast.LENGTH_SHORT).show();
                    }
//                            Object listCities = response.body();
//                            Object dto = response.body();
//                            listCities.add(dto);



                }else {
                    /*if (progressDialog!=null)
                        progressDialog.dismiss();*/
                    //boolean res2 = checkPhoneAvailablity();
                    boolean res3 = checkUserNameAvailability();
                    response_str = true;
                    //Toast.makeText(SignUpActivity.this, "checking failed due to status code:"+statusCode, Toast.LENGTH_SHORT).show();
                }
//                callGetStartEnd();
            }

            @Override
            public void onFailure(Call<ArrayList<TravellerAgentProfiles>> call, Throwable t) {
                // Log error here since request failed
                if (progressDialog!=null)
                    progressDialog.dismiss();
                Log.e("TAG", t.toString());
            }
        });
//            }


//        });


        return response_str;
    }

    private boolean checkPhoneAvailablity(){
 
         final TravellerAgentProfiles p1 = new TravellerAgentProfiles();
 
         p1.setPhoneNumber(phone_no.getText().toString());
 
 //        new ThreadExecuter().execute(new Runnable() {
 //            @Override
 //            public void run() {
         LoginApi apiService =
                 Util.getClient().create(LoginApi.class);
         Call<ArrayList<TravellerAgentProfiles>> call = apiService.getProfileByPhone(Constants.auth_string,p1);
 
         call.enqueue(new Callback<ArrayList<TravellerAgentProfiles>>() {
             @Override
             public void onResponse(Call<ArrayList<TravellerAgentProfiles>> call, Response<ArrayList<TravellerAgentProfiles>> response) {
 //                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                 int statusCode = response.code();
                 if (statusCode == 200) {
                     //String  s = response.body();
 
                     if (response.body()!=null){
                         //if (s.equals("Profile Exist")){
                         if (progressDialog!=null)
                             progressDialog.dismiss();

                         phone_no.requestFocus();
                         phone_no.setError("Phone Number Already exist..!!");
                         response_str = true;
                     }
                     /*else {
                         boolean res3 = checkUserNameAvailability();
                         response_str = false;
                     }*/
 //                            Object listCities = response.body();
 //                            Object dto = response.body();
 //                            listCities.add(dto);

 
 
                 }else if (statusCode == 404){
                     boolean res3 = checkUserNameAvailability();
                     response_str = false;
                 } else{
                     if (progressDialog!=null)
                         progressDialog.dismiss();
                     Toast.makeText(SignUpActivity.this, "checking failed due to status code:"+statusCode, Toast.LENGTH_SHORT).show();
                 }
 //                callGetStartEnd();
             }
 
             @Override
             public void onFailure(Call<ArrayList<TravellerAgentProfiles>> call, Throwable t) {
                 // Log error here since request failed
                 if (progressDialog!=null)
                     progressDialog.dismiss();
                 Log.e("TAG", t.toString());
             }
         });
 //            }
 
 
 //        });
 
 
         return response_str;
     }

    private boolean checkUserNameAvailability(){


        final TravellerAgentProfiles p1 = new TravellerAgentProfiles();

        p1.setUserName(user_name.getText().toString());

//        new ThreadExecuter().execute(new Runnable() {
//            @Override
//            public void run() {
        LoginApi apiService =
                Util.getClient().create(LoginApi.class);
        String authenticationString = Util.getToken(SignUpActivity.this);
        Call<String> call = apiService.getProfileByUserName(Constants.auth_string,user_name.getText().toString());

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                int statusCode = response.code();
                if (statusCode == 200 || statusCode == 201) {

                    String  s = response.body();

                    if (s.equals("Profile Exist")){
                        if (progressDialog!=null)
                            progressDialog.dismiss();

                        user_name.requestFocus();
                        user_name.setError("User Already exist..!!");
                        response_str = true;
                    }
                    else
                        {
                        response_str = false;
                        loginThreadCall();
                    }
//                            Object listCities = response.body();
//                            Object dto = response.body();
//                            listCities.add(dto);



                }else {
                    if (progressDialog!=null)
                        progressDialog.dismiss();
                    Toast.makeText(SignUpActivity.this, "checking failed due to status code:"+statusCode, Toast.LENGTH_SHORT).show();
                }
//                callGetStartEnd();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Log error here since request failed
                if (progressDialog!=null)
                    progressDialog.dismiss();
                Log.e("TAG", t.toString());
            }
        });
//            }


//        });


        return response_str;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id)
        {
            case android.R.id.home:
                this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /*button_resetPassword.setOnClickListener(new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            CharSequence temp_emilID=username.getText().toString();//here username is the your edittext object...
            if(!isValidEmail(temp_emilID))
            {
                username.requestFocus();
                username.setError("Enter Correct Mail_ID ..!!");
                     or
                Toast.makeText(getApplicationContext(), "Enter Correct Mail_ID", Toast.LENGTH_SHORT).show();

            }
            else
           {
              correctMail..
             //Your action...

           }

         });*/



}
