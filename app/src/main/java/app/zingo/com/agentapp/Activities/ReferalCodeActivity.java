package app.zingo.com.agentapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import app.zingo.com.agentapp.Adapter.NotificationManagerAdapter;
import app.zingo.com.agentapp.Adapter.ReferalCodeAdapter;
import app.zingo.com.agentapp.CustomViews.CustomFontTextView;
import app.zingo.com.agentapp.DemoActivity;
import app.zingo.com.agentapp.MainActivity;
import app.zingo.com.agentapp.Model.Bookings1;
import app.zingo.com.agentapp.Model.ReferCodeModel;
import app.zingo.com.agentapp.Model.TravellerAgentProfiles;
import app.zingo.com.agentapp.R;
import app.zingo.com.agentapp.Utils.Constants;
import app.zingo.com.agentapp.Utils.PreferenceHandler;
import app.zingo.com.agentapp.Utils.ThreadExecuter;
import app.zingo.com.agentapp.Utils.Util;
import app.zingo.com.agentapp.WebApi.BookingApi;
import app.zingo.com.agentapp.WebApi.LoginApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReferalCodeActivity extends AppCompatActivity {

    TextView mCommision,mTotal,mReferalCode;
    CustomFontTextView mRedeem,mWallet,mUsed;
    private RecyclerView referal_list;
    private ReferalCodeAdapter adapter;
    ArrayList<TravellerAgentProfiles> profiles;
    long commissionAmount;
    int walletAmount,usedAmount;
    View empty;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            setContentView(R.layout.activity_referal_code);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            setTitle("Referals");

            commissionAmount = PreferenceHandler.getInstance(ReferalCodeActivity.this).getReferalAmount();
            //  walletAmount = PreferenceHandler.getInstance(ReferalCodeActivity.this).getReferedAmount();



            mCommision = (TextView)findViewById(R.id.commission_amount);
            mRedeem = (CustomFontTextView)findViewById(R.id.redeem);
            mWallet = (CustomFontTextView)findViewById(R.id.wallet_amount);
            mUsed = (CustomFontTextView)findViewById(R.id.used_amount);
            empty = (View)findViewById(R.id.empty);
            mTotal = (TextView)findViewById(R.id.total_earnings);
            mReferalCode = (TextView)findViewById(R.id.refer_code);
            referal_list = (RecyclerView) findViewById(R.id.referal_list);

            mCommision.setText("₹ "+commissionAmount+" per new signup user");
            mReferalCode.setText("ZINGO"+PreferenceHandler.getInstance(ReferalCodeActivity.this).getUserId());


            getProfileById();

            mRedeem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String total = mWallet.getText().toString();
                    if(total.contains("₹ ")){
                        String array[] = total.split(" ");
                        double amount = Double.parseDouble(array[1]);
                        if(amount>=1000){
                            sendEmailattache();
                        }else{
                            Toast.makeText(ReferalCodeActivity.this, "Your earnings should be more than ₹ 1000 for redeem", Toast.LENGTH_LONG).show();
                        }
                    }

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void checkReferalCode(){


        final ReferCodeModel p1 = new ReferCodeModel();

        p1.setReferralCodeUsed("ZINGO"+PreferenceHandler.getInstance(ReferalCodeActivity.this).getUserId());

//        new ThreadExecuter().execute(new Runnable() {
//            @Override
//            public void run() {
        String authenticationString = Util.getToken(ReferalCodeActivity.this);

        LoginApi apiService =
                Util.getClient().create(LoginApi.class);
        Call<ArrayList<TravellerAgentProfiles>> call = apiService.getProfilesByUsedReferCode(Constants.auth_string,p1);

        call.enqueue(new Callback<ArrayList<TravellerAgentProfiles>>() {
            @Override
            public void onResponse(Call<ArrayList<TravellerAgentProfiles>> call, Response<ArrayList<TravellerAgentProfiles>> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                int statusCode = response.code();
                try{
                    if (statusCode == 200 || statusCode == 201) {

                        ArrayList<TravellerAgentProfiles> s = response.body();

                        if (s!=null&&s.size()!=0){

                            // commissionAmount = (s.size()*PreferenceHandler.getInstance(ReferalCodeActivity.this).getReferalAmount());
                            //mTotal.setText("₹ "+(commissionAmount+walletAmount+usedAmount));
                            //mWallet.setText("₹ "+(commissionAmount+walletAmount-usedAmount));
                            mTotal.setText("₹ "+(walletAmount+usedAmount));
                            mWallet.setText("₹ "+(walletAmount));


                            adapter = new ReferalCodeAdapter(ReferalCodeActivity.this, s);
                            referal_list.setAdapter(adapter);
                            empty.setVisibility(View.GONE);

                        }else {
                            //empty.setVisibility(View.VISIBLE);
                            mTotal.setText("₹ "+(walletAmount+usedAmount));
                            mWallet.setText("₹ "+(walletAmount-usedAmount));
                        }

                    }else {
                        referal_list.setVisibility(View.GONE);
                        empty.setVisibility(View.VISIBLE);
                        // Toast.makeText(ReferalCodeActivity.this, "checking failed due to status code:"+statusCode, Toast.LENGTH_SHORT).show();
                    }
//                callGetStartEnd();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ArrayList<TravellerAgentProfiles>> call, Throwable t) {
                // Log error here since request failed

                Log.e("TAG", t.toString());
            }
        });
//            }


//        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        try{
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.submit_menu, menu);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }



    private void sendEmailattache() {

        try{
            String[] mailto = {"hello@zingohotels.com"};

            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("text/plain");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, mailto);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Request for Redeem my Earnings");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Dear Zingo Team,\n" +
                    "I want to redeem my earnings.Would you please process my request?. Please find below my details\n" +
                    "\n"+"Full name "+PreferenceHandler.getInstance(ReferalCodeActivity.this).getFullName()+"\n"
                    +"Total Earnings "+mTotal.getText().toString()+"\n"+
                    "Thank you");

            startActivity(Intent.createChooser(emailIntent, "Pick an Email provider"));
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void getProfileById() {

        final ProgressDialog dialog = new ProgressDialog(ReferalCodeActivity.this);
        dialog.setTitle("Loading");
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                LoginApi profileApi = Util.getClient().create(LoginApi.class);
                String authenticationString = Util.getToken(ReferalCodeActivity.this);
                Call<TravellerAgentProfiles> getProfile = profileApi.getProfileByID(authenticationString,
                        PreferenceHandler.getInstance(ReferalCodeActivity.this).getUserId());
                //System.out.println("hotelid = "+hotelid);
                System.out.println();

                getProfile.enqueue(new Callback<TravellerAgentProfiles>() {
                    @Override
                    public void onResponse(Call<TravellerAgentProfiles> call, Response<TravellerAgentProfiles> response) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        try{
                            if(response.code() == 200)
                            {
                                TravellerAgentProfiles dto = response.body();

                                if(dto != null)
                                {

                                    walletAmount = dto.getWalletBalance();
                                    usedAmount = dto.getUsedAmount();
                                    mUsed.setText("₹ "+usedAmount);
                                    checkReferalCode();

                                }
                            }
                        }catch (Exception e){
                            e.printStackTrace();
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        try{
            int id = item.getItemId();
            switch (id) {

                case android.R.id.home:
                    goback();
                    break;

                case R.id.action_share:
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "Hello this is Zingo Hotels Agent App. Join the Zingo Hotels referral programme, and earn money for every new referral.\n Open the Zingo Hotels App and visit the invite & earn section, and find out your referral code.  It’s an alpha-numeric code like: ZINGO"+ PreferenceHandler.getInstance(ReferalCodeActivity.this).getUserId()+"\n Keep Sharing & Earning.\nTo Download the app click here: https://play.google.com/store/apps/details?id=app.zingo.com.agentapp");
                    sendIntent.setType("text/plain");
                    startActivity(Intent.createChooser(sendIntent,"Zingo Agent" ));
                    break;



            }
            return super.onOptionsItemSelected(item);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goback();
    }

    private void goback()
    {
        Intent main = new Intent(ReferalCodeActivity.this,DemoActivity.class);
        main.putExtra("ARG_PAGE",4);
        startActivity(main);
        ReferalCodeActivity.this.finish();
    }

}