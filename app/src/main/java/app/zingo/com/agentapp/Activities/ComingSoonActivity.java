package app.zingo.com.agentapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import app.zingo.com.agentapp.MainActivity;
import app.zingo.com.agentapp.Model.TravellerAgentProfiles;
import app.zingo.com.agentapp.R;
import app.zingo.com.agentapp.Utils.PreferenceHandler;
import app.zingo.com.agentapp.Utils.ThreadExecuter;
import app.zingo.com.agentapp.Utils.Util;
import app.zingo.com.agentapp.WebApi.LoginApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ComingSoonActivity extends AppCompatActivity {

    boolean isFirstTimePressed = false;
    int referalProfileId=0;
    double referAmountOther = 0,walletAmount = 0;
    TravellerAgentProfiles profiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            setContentView(R.layout.activity_coming_soon);

            referAmountOther =getIntent().getDoubleExtra("ReferAmountOther",0);
            walletAmount =getIntent().getDoubleExtra("WalletAmountOther",0);
            referalProfileId = getIntent().getIntExtra("OtherProfileId",0);

            if(referalProfileId!=0){
                getProfileOther();
            }else{
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(getSupportFragmentManager().getBackStackEntryCount() == 0)
        {
            new CountDownTimer(2000, 2000) {
                @Override
                public void onTick(long l) {
                    if(!isFirstTimePressed)
                    {
                        //System.out.println("isFirstTimePressed = "+isFirstTimePressed);
                        Toast.makeText(ComingSoonActivity.this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
                        isFirstTimePressed = true;
                    }
                    else
                    {
                        //System.out.println("isFirstTimePressed = "+isFirstTimePressed);
                        isFirstTimePressed = false;
                        finish();
                    }
                }

                @Override
                public void onFinish() {
                    isFirstTimePressed = false;
                }
            }.start();
        }
        else
        {
            getSupportFragmentManager().popBackStack();
        }
    }

    private void getProfileOther() {

       /* final ProgressDialog dialog = new ProgressDialog(ComingSoonActivity.this);
        dialog.setTitle("Loading");
        dialog.setCancelable(false);
        dialog.show();
*/
        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                LoginApi profileApi = Util.getClient().create(LoginApi.class);
                String authenticationString = Util.getToken(ComingSoonActivity.this);
                Call<TravellerAgentProfiles> getProfile = profileApi.getProfileByID(authenticationString, referalProfileId);
                //System.out.println("hotelid = "+hotelid);
                System.out.println();

                getProfile.enqueue(new Callback<TravellerAgentProfiles>() {
                    @Override
                    public void onResponse(Call<TravellerAgentProfiles> call, Response<TravellerAgentProfiles> response) {
                       /* if(dialog != null)
                        {
                            dialog.dismiss();
                        }*/
                        if(response.code() == 200)
                        {
                            profiles = response.body();

                            if(profiles != null)
                            {
                                profiles.setWalletBalance((int) (walletAmount+referAmountOther));
                                updateProfileOther(profiles);

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<TravellerAgentProfiles> call, Throwable t) {
                        /*if(dialog != null)
                        {
                            dialog.dismiss();
                        }*/
                    }
                });
            }
        });
    }

    private void updateProfileOther(TravellerAgentProfiles up) {

        /*final ProgressDialog dialog = new ProgressDialog(ComingSoonActivity.this);
        dialog.setMessage(getResources().getString(R.string.loader_message));
        dialog.setCancelable(true);
        dialog.show();*/

        String auth_string = Util.getToken(ComingSoonActivity.this);

        LoginApi profileApi = Util.getClient().create(LoginApi.class);
        Call<String> res = profileApi.updateProfileById(auth_string,referalProfileId,up);
        res.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

               /* if(dialog != null)
                {
                    dialog.dismiss();
                }*/
                if(response.code() == 204 ||response.code() == 200 ||response.code() == 201)
                {
                }else{
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
               /* if(dialog != null)
                {
                    dialog.dismiss();
                }*/
                //Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }
}
