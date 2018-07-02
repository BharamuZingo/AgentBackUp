package app.zingo.com.agentapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import app.zingo.com.agentapp.Activities.ComingSoonActivity;
import app.zingo.com.agentapp.Activities.HotelDetailsActivity;
import app.zingo.com.agentapp.Activities.LoginActivity;
import app.zingo.com.agentapp.Model.TravellerAgentProfiles;
import app.zingo.com.agentapp.Services.TrackGPS;
import app.zingo.com.agentapp.Utils.LocationHelper;
import app.zingo.com.agentapp.Utils.PreferenceHandler;
import app.zingo.com.agentapp.Utils.ThreadExecuter;
import app.zingo.com.agentapp.Utils.Util;
import app.zingo.com.agentapp.WebApi.LoginApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SplashActivity extends AppCompatActivity {

    private ImageView app_logo;
    private TextView text,mVersionName;

    LocationHelper locationHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        app_logo = (ImageView)findViewById(R.id.app_log);
        text = (TextView) findViewById(R.id.text);
        //mVersionName = (TextView) findViewById(R.id.version_name);
        Animation logo_anim = AnimationUtils.loadAnimation(this,R.anim.transition);
        app_logo.startAnimation(logo_anim);
        text.startAnimation(logo_anim);

        TrackGPS gps = new TrackGPS(SplashActivity.this);
        locationHelper=new LocationHelper(SplashActivity.this);
        //locationHelper.checkpermission();

        new Handler().postDelayed(new Runnable() {
            public void run(){

                int userId = PreferenceHandler.getInstance(SplashActivity.this).getUserId();

                if(userId!=0){

                    //getProfileById(userId);
                    if(PreferenceHandler.getInstance(SplashActivity.this).getUserRoleUniqueID().equalsIgnoreCase("Luci-Agent"))
                    {
                        if(PreferenceHandler.getInstance(SplashActivity.this).getProfileStatus().equalsIgnoreCase("enabled"))
                        {
                            Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            getProfileById(userId);
                        }
                    }
                    else
                    {
                        Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                        finish();
                    }

                }else{
                    Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                    finish();
                }

            }
        }, 3000);
    }

    private void getProfileById(final int id) {


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                LoginApi profileApi = Util.getClient().create(LoginApi.class);
                String authenticationString = Util.getToken(SplashActivity.this);
                Call<TravellerAgentProfiles> getProfile = profileApi.getProfileByID(authenticationString, id);
                //System.out.println("hotelid = "+hotelid);
                System.out.println();

                getProfile.enqueue(new Callback<TravellerAgentProfiles>() {
                    @Override
                    public void onResponse(Call<TravellerAgentProfiles> call, Response<TravellerAgentProfiles> response) {

                        if(response.code() == 200)
                        {
                            TravellerAgentProfiles dto = response.body();

                            if(dto != null)
                            {

                                if(dto.getStatus().equalsIgnoreCase("enabled")){
                                    Intent intent = new Intent(SplashActivity.this,MainActivity.class);

                                    PreferenceHandler.getInstance(SplashActivity.this).setCommissionAmount(dto.getCommissionAmount());
                                    PreferenceHandler.getInstance(SplashActivity.this).setProfileStatus(dto.getStatus());
                                    PreferenceHandler.getInstance(SplashActivity.this).setCommissionPercentage(dto.getCommissionPercentage());
                                    PreferenceHandler.getInstance(SplashActivity.this).setReferalAmount(dto.getReferralAmountForOtherProfile());
                                    PreferenceHandler.getInstance(SplashActivity.this).setReferedAmount(dto.getWalletBalance());
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                    startActivity(intent);
                                    finish();
                                }else{
                                    Intent intent = new Intent(SplashActivity.this,ComingSoonActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                    startActivity(intent);
                                    finish();
                                }

                            }
                        }else{
                            Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<TravellerAgentProfiles> call, Throwable t) {
                        Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
    }
}