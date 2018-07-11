package app.zingo.com.agentapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import app.zingo.com.agentapp.DemoActivity;
import app.zingo.com.agentapp.MainActivity;
import app.zingo.com.agentapp.Model.Bookings1;
import app.zingo.com.agentapp.R;
import app.zingo.com.agentapp.Utils.PreferenceHandler;
import app.zingo.com.agentapp.Utils.ThreadExecuter;
import app.zingo.com.agentapp.Utils.Util;
import app.zingo.com.agentapp.WebApi.BookingApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EarningDetailsActivity extends AppCompatActivity {

    TextView mCommisionDeal,mTotalEarnings,mTotalBookingCount,mTotalBookingAmount,mConfirmedBookingCount,mConfirmedBookingAmount,
    mUpcomingBookingCount,mUpcomingBookingAmount,mCancelledBookingCount,mCancelledBookingAmount;
    long commissionAmount,commisionPercentage;

    ArrayList<Bookings1> confirmedbookings,cancelledbookings,upcomingbookings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earning_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setTitle("Earnings");

        commissionAmount = PreferenceHandler.getInstance(EarningDetailsActivity.this).getCommissionAmount();
        commisionPercentage = PreferenceHandler.getInstance(EarningDetailsActivity.this).getCommissionPercentage();

        //System.out.println("COmmmm=="+commisionPercentage+"CoomAmm=="+commissionAmount);


        mCommisionDeal = (TextView)findViewById(R.id.commission_amount);
        mTotalEarnings = (TextView)findViewById(R.id.total_earnings);
        mTotalBookingCount = (TextView)findViewById(R.id.total_bookings_count);
        mTotalBookingAmount = (TextView)findViewById(R.id.total_bookings_amount);
        mConfirmedBookingCount = (TextView)findViewById(R.id.confirmed_bookings_count);
        mConfirmedBookingAmount = (TextView)findViewById(R.id.confirmed_bookings_amount);
        mUpcomingBookingCount = (TextView)findViewById(R.id.upcoming_bookings_count);
        mUpcomingBookingAmount = (TextView)findViewById(R.id.upcoming_bookings_amount);
        mCancelledBookingCount = (TextView)findViewById(R.id.cancelled_bookings_count);
        mCancelledBookingAmount = (TextView)findViewById(R.id.cancelled_bookings_amount);

        getBookings();

        System.out.println("Cp=="+commisionPercentage);
        System.out.println("Ca=="+commissionAmount);

        if(commissionAmount==0&&commisionPercentage==0){
            mCommisionDeal.setText("₹ 0 per Booking");
        }else if(commisionPercentage==0&&commissionAmount!=0){
            mCommisionDeal.setText("₹ "+commissionAmount+" per Booking");
        }else if(commissionAmount==0&&commisionPercentage!=0){
            mCommisionDeal.setText(commisionPercentage+" % per Booking");
        }else if(commissionAmount!=0&&commisionPercentage!=0){
            mCommisionDeal.setText("₹ "+commissionAmount+" per Booking");
        }
      //

    }



    private void getBookings() {
        final ProgressDialog dialog = new ProgressDialog(EarningDetailsActivity.this);
        dialog.setTitle(R.string.loader_message);
        dialog.setCancelable(false);
        dialog.show();


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                BookingApi bookingApi = Util.getClient().create(BookingApi.class);
                String authenticationString = Util.getToken(EarningDetailsActivity.this);
                final Call<ArrayList<Bookings1>> getAllBookings = bookingApi.
                        getBookingsByProfileId(authenticationString, PreferenceHandler.getInstance(EarningDetailsActivity.this).getUserId());

                getAllBookings.enqueue(new Callback<ArrayList<Bookings1>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Bookings1>> call, Response<ArrayList<Bookings1>> response) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        if(response.code() == 200)
                        {
                            if(response.body() != null)
                            {
                                confirmedbookings = new ArrayList<>();
                                upcomingbookings = new ArrayList<>();
                                cancelledbookings = new ArrayList<>();
                                System.out.println("Room Booking=="+response.body().size());
                                double total=0,confirmed = 0,upcoming = 0,cancelled = 0;



                                for(int i=0;i<response.body().size();i++){
                                    total = total+response.body().get(i).getCommissionAmount();

                                    if(response.body().get(i).getBookingStatus().equalsIgnoreCase("completed") )
                                    {
                                        confirmed = confirmed+response.body().get(i).getCommissionAmount();
                                        confirmedbookings.add(response.body().get(i));
                                    }
                                    else if(response.body().get(i).getBookingStatus().equalsIgnoreCase("quick") )
                                    {
                                        upcoming = upcoming+response.body().get(i).getCommissionAmount();
                                        upcomingbookings.add(response.body().get(i));
                                    }
                                    else if(response.body().get(i).getBookingStatus().equalsIgnoreCase("cancelled") )
                                    {
                                        cancelled = cancelled+response.body().get(i).getCommissionAmount();
                                        cancelledbookings.add(response.body().get(i));
                                    }
                                }

                                setToFields(response.body(),confirmedbookings,upcomingbookings,cancelledbookings,total,confirmed,upcoming,cancelled);
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Bookings1>> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                    }
                });
            }
        });


    }

    private void setToFields(ArrayList<Bookings1> totalbookings,ArrayList<Bookings1> confirmedbookings,
                             ArrayList<Bookings1> upcomingbookings, ArrayList<Bookings1> cancelledbookings,
                             double total, double confirmed, double upcoming, double cancelled) {

        mTotalBookingCount.setText(totalbookings.size()+"");
        mTotalBookingAmount.setText("₹ "+total+"");
        mTotalEarnings.setText("₹ "+total+"");
        mConfirmedBookingCount.setText(confirmedbookings.size()+"");
        mConfirmedBookingAmount.setText("₹ "+confirmed+"");
        mUpcomingBookingCount.setText(upcomingbookings.size()+"");
        mUpcomingBookingAmount.setText("₹ "+upcoming+"");
        mCancelledBookingCount.setText(cancelledbookings.size()+"");
        mCancelledBookingAmount.setText("₹ "+cancelled+"");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id)
        {
            case android.R.id.home:
                goback();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goback();
    }

    private void goback()
    {
        Intent main = new Intent(EarningDetailsActivity.this, DemoActivity.class);
        main.putExtra("ARG_PAGE",4);
        startActivity(main);
        this.finish();
    }
}
