package app.zingo.com.agentapp.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.security.Key;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import app.zingo.com.agentapp.CustomViews.CustomFontTextView;
import app.zingo.com.agentapp.DemoActivity;
import app.zingo.com.agentapp.MainActivity;
import app.zingo.com.agentapp.Model.DeviceMapping;
import app.zingo.com.agentapp.Model.HotelDetails;
import app.zingo.com.agentapp.Model.TravellerAgentProfiles;
import app.zingo.com.agentapp.Model.UserRole;
import app.zingo.com.agentapp.R;
import app.zingo.com.agentapp.Services.SharedPrefManager;
import app.zingo.com.agentapp.Services.TrackGPS;
import app.zingo.com.agentapp.SplashActivity;
import app.zingo.com.agentapp.Utils.Constants;
import app.zingo.com.agentapp.Utils.LocationHelper;
import app.zingo.com.agentapp.Utils.PreferenceHandler;
import app.zingo.com.agentapp.Utils.ThreadExecuter;
import app.zingo.com.agentapp.Utils.Util;
import app.zingo.com.agentapp.WebApi.DeviceMappingApi;
import app.zingo.com.agentapp.WebApi.LoginApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText mUserName,mUserPwd;
    CustomFontTextView mLogin,mSignUp;

    LocationHelper locationHelper;
    public static final int MY_PERMISSIONS_REQUEST_RESULT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try
        {
            setContentView(R.layout.activity_login);

        /*getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN|
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);*/

            TrackGPS gps = new TrackGPS(LoginActivity.this);
            if(gps.canGetLocation())
            {
                gps.getLongitude();
                gps.getLatitude();
            }
            //checkPermission();

            mUserName = (EditText)findViewById(R.id.login_username);
            mUserPwd = (EditText)findViewById(R.id.login_password);

            mLogin = (CustomFontTextView)findViewById(R.id.loginBtn);
            mSignUp = (CustomFontTextView)findViewById(R.id.signBtn);


        /*locationHelper=new LocationHelper(LoginActivity.this);
        locationHelper.checkpermission();*/


            mSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                /*Intent signUp = new Intent(LoginActivity.this,PhoneNumberVerficationActivity.class);
                startActivity(signUp);*/
                    try {
                        showAlertBox();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            mLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try
                    {

                        Util.hideKeyboard(LoginActivity.this);
                        if (!mUserName.getText().toString().trim().isEmpty() &&
                                !mUserPwd.getText().toString().trim().isEmpty()) {

                            encrypt(mUserPwd.getText().toString());
                        }
                        else
                            Toast.makeText(LoginActivity.this, "Fields should not be empty", Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }


                }
            });
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }


    }

    public void showAlertBox() throws Exception
    {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LoginActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.alert_box_layout,null);
        CustomFontTextView traveller = view.findViewById(R.id.traveller_btn);
        CustomFontTextView agent = view.findViewById(R.id.agent_btn);
        dialogBuilder.setView(view);
        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();
        traveller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    if(dialog != null)
                    {
                        dialog.dismiss();
                    }
                    Intent signUp = new Intent(LoginActivity.this,PhoneNumberVerficationActivity.class);
                    signUp.putExtra("Type","Traveller");
                    startActivity(signUp);
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        });
        agent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try
                {
                    if(dialog != null)
                    {
                        dialog.dismiss();
                    }
                    Intent signUp = new Intent(LoginActivity.this,PhoneNumberVerficationActivity.class);
                    signUp.putExtra("Type","Agent");
                    startActivity(signUp);
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        });


    }


    public boolean checkPermission() {
        if ((ContextCompat.checkSelfPermission(LoginActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                && (ContextCompat.checkSelfPermission(LoginActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)&&
                (ContextCompat.checkSelfPermission(LoginActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                && (ContextCompat.checkSelfPermission(LoginActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)) {
            if ((ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION))
                    && (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this, android.Manifest.permission.CALL_PHONE))
                    && (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE))
                    && (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this, android.Manifest.permission.CAMERA))) {

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(LoginActivity.this,
                        new String[]{
                                android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.CALL_PHONE,
                                android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_RESULT);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(LoginActivity.this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.CALL_PHONE,
                                android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_RESULT);


            }
            return false;
        } else {
            return true;
        }
    }

    public void encrypt(String pwd){
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
            String enc = sb.toString();
            System.out.println("encrypted:" + enc);

            login(mUserName.getText().toString(), enc,"Encrypted");



        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    private void login( final String username, final String password,final String type) throws Exception{

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("please wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        final TravellerAgentProfiles p = new TravellerAgentProfiles();
        p.setUserName(username);
        p.setPassword(password);

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                try
                {
                    LoginApi apiService =
                            Util.getClient().create(LoginApi.class);

                    Call<ArrayList<TravellerAgentProfiles>> call = apiService.loginApiByUsernamePassword(Constants.auth_string,p);

                    call.enqueue(new Callback<ArrayList<TravellerAgentProfiles>>() {
                        @Override
                        public void onResponse(Call<ArrayList<TravellerAgentProfiles>> call, Response<ArrayList<TravellerAgentProfiles>> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                           try
                           {
                               int statusCode = response.code();
                               if (progressDialog != null)
                                   progressDialog.dismiss();
                               if (statusCode == 200 || statusCode == 201) {

                                   ArrayList<TravellerAgentProfiles> dto1 = response.body();//-------------------should not be list------------
                                   if (dto1!=null && dto1.size()!=0) {
                                       TravellerAgentProfiles dto = dto1.get(0);

                                       if(dto.getStatus() != null && dto.getStatus().equalsIgnoreCase("enabled"))
                                       {

                                           SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                                           SharedPreferences.Editor spe = sp.edit();
                                           spe.putInt(Constants.USER_ID, dto.getTravellerAgentProfileId());
                                           PreferenceHandler.getInstance(LoginActivity.this).setUserId(dto.getTravellerAgentProfileId());
                                           PreferenceHandler.getInstance(LoginActivity.this).setCommissionAmount(dto.getCommissionAmount());
                                           PreferenceHandler.getInstance(LoginActivity.this).setCommissionPercentage(dto.getCommissionPercentage());
                                           PreferenceHandler.getInstance(LoginActivity.this).setReferalAmount(dto.getReferralAmountForOtherProfile());
                                           PreferenceHandler.getInstance(LoginActivity.this).setReferedAmount(dto.getWalletBalance());
                                           System.out.println("CA"+PreferenceHandler.getInstance(LoginActivity.this).getCommissionAmount());
                                           System.out.println("CP"+PreferenceHandler.getInstance(LoginActivity.this).getCommissionPercentage());

                                           if(dto.getMiddleName()==null||dto.getLastName()==null){
                                               PreferenceHandler.getInstance(LoginActivity.this).setFullName(dto.getFirstName());
                                           }else{
                                               PreferenceHandler.getInstance(LoginActivity.this).setFullName(dto.getFirstName()+" "+dto.getLastName());
                                           }

                                           PreferenceHandler.getInstance(LoginActivity.this).setPhoneNumber(dto.getPhoneNumber());
                                           PreferenceHandler.getInstance(LoginActivity.this).setUserName(dto.getUserName());
                                    /*spe.putString("FirstName", dto.getFirstName());
                                    spe.putString("MiddleName", dto.getMiddleName());
                                    spe.putString("LastName", dto.getLastName());
                                    spe.putString("UserName", dto.getUserName());
                                    spe.putString("Password", dto.getPassword());
                                    spe.putString("UniqueId", dto.getUniqueId());
                                    spe.putInt("CityId", dto.getCityId());
                                    spe.putString("PlaceName", dto.getPlaceName());
                                    spe.putString("Email", dto.getEmail());
                                    spe.putString("PhoneNumber", dto.getPhoneNumber());
                                    spe.putString("Address", dto.getAddress());
                                    spe.putString("PinCode", dto.getPinCode());
                                    spe.putInt("UserRoleId", dto.getUserRoleId());
                                    spe.apply();*/


                                           UserRole userRole = dto.get_userRole();
                                           if(userRole != null)
                                           {
                                               System.out.println("Unique id = "+userRole.getUserRoleUniqueId());
                                               PreferenceHandler.getInstance(LoginActivity.this).setUserRoleUniqueID(userRole.getUserRoleUniqueId());
                                           }

                                    /*if(userRole.getUserRoleUniqueId().equalsIgnoreCase("Luci-Agent"))
                                    {*/

                                           String token = SharedPrefManager.getInstance(LoginActivity.this).getDeviceToken();
                                           System.out.println("token"+token);

                                           if(token!=null){
                                               DeviceMapping hm = new DeviceMapping();
                                               hm.setProfileId(dto.getTravellerAgentProfileId());
                                               hm.setDeviceId(token);
                                               addDeviceId(hm);
                                           }else{

                                           }

                                    /*Intent i = new Intent(LoginActivity.this, ComingSoonActivity.class);
                                    startActivity(i);
                                    finish();*/
                                    /*}
                                    else
                                    {
                                        Toast.makeText(LoginActivity.this,"Sorry agents can only login",Toast.LENGTH_SHORT).show();
                                    }*/

                                       }
                                       else
                                       {
                                           SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                                           SharedPreferences.Editor spe = sp.edit();
                                           spe.putInt(Constants.USER_ID, dto.getTravellerAgentProfileId());
                                           PreferenceHandler.getInstance(LoginActivity.this).setUserId(dto.getTravellerAgentProfileId());
                                           PreferenceHandler.getInstance(LoginActivity.this).setCommissionAmount(dto.getCommissionAmount());
                                           PreferenceHandler.getInstance(LoginActivity.this).setCommissionPercentage(dto.getCommissionPercentage());
                                           PreferenceHandler.getInstance(LoginActivity.this).setReferalAmount(dto.getReferralAmountForOtherProfile());
                                           PreferenceHandler.getInstance(LoginActivity.this).setReferedAmount(dto.getWalletBalance());
                                           System.out.println("CA"+PreferenceHandler.getInstance(LoginActivity.this).getCommissionAmount());
                                           System.out.println("CP"+PreferenceHandler.getInstance(LoginActivity.this).getCommissionPercentage());

                                           if(dto.getMiddleName()==null||dto.getLastName()==null){
                                               PreferenceHandler.getInstance(LoginActivity.this).setFullName(dto.getFirstName());
                                           }else{
                                               PreferenceHandler.getInstance(LoginActivity.this).setFullName(dto.getFirstName()+" "+dto.getLastName());
                                           }

                                           PreferenceHandler.getInstance(LoginActivity.this).setPhoneNumber(dto.getPhoneNumber());
                                           PreferenceHandler.getInstance(LoginActivity.this).setUserName(dto.getUserName());
                                           UserRole userRole = dto.get_userRole();

                                           if(userRole != null)
                                           {
                                               PreferenceHandler.getInstance(LoginActivity.this).setUserRoleUniqueID(userRole.getUserRoleUniqueId());

                                               String token = SharedPrefManager.getInstance(LoginActivity.this).getDeviceToken();
                                               System.out.println("token"+token);

                                               if(token!=null){
                                                   DeviceMapping hm = new DeviceMapping();
                                                   hm.setProfileId(dto.getTravellerAgentProfileId());
                                                   hm.setDeviceId(token);
                                                   addDeviceId(hm);
                                               }else{

                                               }
                                              /* if(userRole.getUserRoleUniqueId().equalsIgnoreCase("Luci-Agent"))
                                               {
                                                   Intent i = new Intent(LoginActivity.this, ComingSoonActivity.class);
                                                   startActivity(i);
                                                   finish();
                                               }
                                               else
                                               {
                                                   Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                                   startActivity(i);
                                                   finish();
                                               }*/
                                           }

                                       }

                                   }else{
//
                                       if(type.equalsIgnoreCase("Encrypted")){
                                           login(mUserName.getText().toString(), mUserPwd.getText().toString(),"Normal");
                                       }else{
                                           Toast.makeText(LoginActivity.this, "Login credentials are wrong..", Toast.LENGTH_SHORT).show();
                                       }


                                   }
                               }else {
                                   if (progressDialog!=null)
                                       progressDialog.dismiss();
                                   Toast.makeText(LoginActivity.this, "Login failed due to status code:"+statusCode, Toast.LENGTH_SHORT).show();
                               }
                           }
                           catch (Exception ex)
                           {
                               ex.printStackTrace();
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
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        });
    }

    public void addDeviceId(final DeviceMapping hm) throws Exception
    {
        final ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
        dialog.setMessage(getResources().getString(R.string.loader_message));
        dialog.setCancelable(false);
        dialog.show();
        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {


                try {
                    String authenticationString = Util.getToken(LoginActivity.this);//"Basic " +  Base64.encodeToString(authentication.getBytes(), Base64.NO_WRAP);
                    DeviceMappingApi hotelOperation = Util.getClient().create(DeviceMappingApi.class);
                    Call<DeviceMapping> response = hotelOperation.addProfileMap(hm);

                    response.enqueue(new Callback<DeviceMapping>() {
                        @Override
                        public void onResponse(Call<DeviceMapping> call, Response<DeviceMapping> response) {

                            try
                            {
                                if(dialog != null && dialog.isShowing())
                                {
                                    dialog.dismiss();
                                }
                                if(response.code() == 200||response.code() == 201||response.code() == 202||response.code() == 204)
                                {
                                    DeviceMapping hotelDetailseResponse = response.body();

                                    if(hotelDetailseResponse != null)
                                    {
                                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(LoginActivity.this, DemoActivity.class);
                                        startActivity(i);
                                        finish();
                                    }



                                }else if(response.code() == 404){
                                    if(response.body()==null){
                                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(i);
                                        finish();
                                    }
                                }
                                else
                                {

                                    Toast.makeText(LoginActivity.this,"Check your internet connection or please try after some time",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                            catch (Exception ex)
                            {
                                ex.printStackTrace();
                            }


                        }

                        @Override
                        public void onFailure(Call<DeviceMapping> call, Throwable t) {
                            System.out.println("Failed");
                            System.out.println(" Exception = "+t.getMessage());
                            if(dialog != null && dialog.isShowing())
                            {
                                dialog.dismiss();
                            }
                            Toast.makeText(LoginActivity.this,"Check your internet connection or please try after some time",
                                    Toast.LENGTH_LONG).show();

                        }
                    });
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        });
    }


}
