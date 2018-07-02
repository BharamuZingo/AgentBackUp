package app.zingo.com.agentapp.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import app.zingo.com.agentapp.CustomViews.CustomFontTextView;
import app.zingo.com.agentapp.MainActivity;
import app.zingo.com.agentapp.Model.HotelDetails;
import app.zingo.com.agentapp.Model.HotelNotification;
import app.zingo.com.agentapp.R;
import app.zingo.com.agentapp.Utils.Constants;
import app.zingo.com.agentapp.Utils.PreferenceHandler;
import app.zingo.com.agentapp.Utils.ThreadExecuter;
import app.zingo.com.agentapp.Utils.Util;
import app.zingo.com.agentapp.WebApi.HotelOperations;
import app.zingo.com.agentapp.WebApi.NotificationApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BiddingRequestReply extends AppCompatActivity {

    Button mClose,mAccept,mReject,mSuggest;
    TextView mTitle,mHotelName;
    CustomFontTextView mBiddingPrice,mGuestCount,mRoomCount,mCID,mCOD,mBid,mReason,mCIT,mCOT;

    String title,message,fds,tds,price,rooms,adult,child,cit,cot,bid,reason;
    //String bookingNumber;
    LinearLayout mButton,mRejected,mSelect;
    private int profileId,bookingId,hotelId;
    String preBookingSelectedRoomNumber="";

    HotelDetails dto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bidding_request_reply);

        profileId = PreferenceHandler.getInstance(BiddingRequestReply.this).getUserId();

        title = getIntent().getExtras().getString("Title");
        message = getIntent().getExtras().getString("Message");

        mTitle = (TextView) findViewById(R.id.title_message);

        mButton = (LinearLayout)findViewById(R.id.buttons);
       /* mRejected = (LinearLayout)findViewById(R.id.rejected_layout);
        mSelect = (LinearLayout)findViewById(R.id.select_room_lay);*/

        mHotelName = (TextView) findViewById(R.id.agent_name);
        mClose = (Button)findViewById(R.id.btnClose);
        mAccept = (Button)findViewById(R.id.btnaccept);
        mReject = (Button)findViewById(R.id.btnReject);
        mSuggest = (Button)findViewById(R.id.btnSuggest);
        mGuestCount = (CustomFontTextView) findViewById(R.id.brief_detail_pax_details);
        mBid = (CustomFontTextView) findViewById(R.id.bidding_amount_hotel);
        mReason = (CustomFontTextView) findViewById(R.id.reason);
        mRoomCount = (CustomFontTextView) findViewById(R.id.total_rooms);
        mBiddingPrice = (CustomFontTextView) findViewById(R.id.bidding_amount);
        //  mBookingNumber = (CustomFontTextView) findViewById(R.id.brief_detail_booking_number);
        mCID = (CustomFontTextView) findViewById(R.id.brief_detail_check_in_date);
        mCOD = (CustomFontTextView) findViewById(R.id.brief_detail_check_out_date);
        mCIT = (CustomFontTextView) findViewById(R.id.brief_detail_check_in_time);
        mCOT = (CustomFontTextView) findViewById(R.id.brief_detail_check_out_time);

        if(title!=null){
            if(title.equalsIgnoreCase("Bidding Request Rejected")){
                //mRejected.setVisibility(View.VISIBLE);
                mButton.setVisibility(View.GONE);
                //mSelect.setVisibility(View.GONE);
            }else{
                mButton.setVisibility(View.VISIBLE);
            }
        }

        if(message!=null){
            if(message.contains(",")){
                String bookin[] = message.split(",");
                System.out.println("Length=="+bookin.length);
                if(bookin.length==11){
                    String hotelid = bookin[0];
                    fds = bookin[1];
                    tds = bookin[2];
                    price = bookin[3];
                    rooms = bookin[4];
                    adult = bookin[5];
                    child = bookin[6];
                    cit = bookin[7];
                    cot = bookin[8];
                    bid = bookin[9];
                    reason = bookin[10];

                    mBid.setText("Hotelier bidding amount ₹ "+bid);
                    mReason.setText("Notes "+reason);

                    hotelId = Integer.parseInt(hotelid);
                    System.out.println("Hotel id =="+hotelId);

                    mBiddingPrice.setText("Bidding Price is ₹ "+price);
                    mRoomCount.setText(rooms+" Room(s)");
                    mGuestCount.setText(adult+" Adult(s) "+child+" child");
                    getAgentDetails(Integer.parseInt(hotelid));
                    SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("MMM dd yyyy");
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
                    SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");

                    try {
                        Date CIDate = null,CODate= null;
                        if(fds.contains("-"))
                        {
                            CIDate  =  simpleDateFormat2.parse(fds);
                            CODate  =  simpleDateFormat2.parse(tds);
                        }
                        else
                        {
                            CIDate  =  simpleDateFormat.parse(fds);
                            CODate  =  simpleDateFormat.parse(tds);
                        }
                        mCID.setText(simpleDateFormat1.format(CIDate));
                        mCOD.setText(simpleDateFormat1.format(CODate));
                        mCIT.setText(cit);
                        mCOT.setText(cot);


                    }
                    catch (ParseException ex)
                    {
                        ex.printStackTrace();
                    }


                }



            }else{

            }
        }


        mAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("MMM dd yyyy");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");

                try {
                    Date CIDate = null,CODate= null;
                    if(fds.contains("-"))
                    {
                        CIDate  =  simpleDateFormat2.parse(fds);
                        CODate  =  simpleDateFormat2.parse(tds);
                    }
                    else
                    {
                        CIDate  =  simpleDateFormat.parse(fds);
                        CODate  =  simpleDateFormat.parse(tds);
                    }
                    mCID.setText(simpleDateFormat1.format(CIDate));
                    mCOD.setText(simpleDateFormat1.format(CODate));

                    Intent intent = new Intent(BiddingRequestReply.this, HotelDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constants.HOTEL_ID, dto.getHotelId());
                    //bundle.putString("CheckinDate", simpleDateFormat1.format(CIDate));
                    bundle.putString("CheckinDate", simpleDateFormat.format(CIDate));
                    bundle.putString("CheckInTime", cit);
                    bundle.putString("CheckOutTime", cot);
                    String room = rooms+","+adult+","+child;
                    bundle.putString("Room",room);
                    /*bundle.putInt("DisplayPrice",dto.getRooms().get(0).getDisplayRate());
                    bundle.putInt("SellRate",dto.getRooms().get(0).getSellRate());*/
                    bundle.putInt("DisplayPrice", Integer.parseInt(bid));
                    bundle.putInt("SellRate", Integer.parseInt(bid));
                    //bundle.putString("CheckoutDate", simpleDateFormat1.format(CODate));
                    bundle.putString("CheckoutDate", simpleDateFormat.format(CODate));
                    intent.putExtras(bundle);
                    startActivity(intent);


                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }



            }
        });

        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BiddingRequestReply.this.finish();
            }
        });

        mReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(BiddingBookingActivity.this,"Reject",Toast.LENGTH_SHORT).show();
                HotelNotification fm = new HotelNotification();
                fm.setSenderId(Constants.senderId);
                fm.setServerId(Constants.serverId);
                fm.setHotelId(hotelId);
                //fm.setDeviceId(deviceId);
                fm.setTitle("Bidding Request Rejected");
                String pass = rooms+","+adult+","+child;
                fm.setMessage(profileId+","+fds+","+tds+","+price+","+pass+","+cit+","+cot);
                //registerTokenInDB(fm);
                sendNotification(fm);
                // sendNotificationByDevice(fm);
                // sendNotification(fm);

            }
        });

       /* mSuggest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });*/

    }


    private void getAgentDetails(final int hotelid) {



        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                String auth_string = Util.getToken(BiddingRequestReply.this);//"Basic " +  Base64.encodeToString(authentication.getBytes(), Base64.NO_WRAP);
                HotelOperations travellerApi = Util.getClient().create(HotelOperations.class);
                Call<HotelDetails> getTravellerDetails = travellerApi.getHotelByHotelId(auth_string,hotelid);

                getTravellerDetails.enqueue(new Callback<HotelDetails>() {
                    @Override
                    public void onResponse(Call<HotelDetails> call, Response<HotelDetails> response) {

                        if(response.code() == 200)
                        {
                            if(response.body()!=null){
                                mHotelName.setText(response.body().getHotelName());
                                dto =response.body();                            }

                        }
                        else
                        {
                            System.out.println("traveller = "+response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<HotelDetails> call, Throwable t) {

                    }
                });
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id)
        {
            case android.R.id.home:
                BiddingRequestReply.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void sendNotification(final HotelNotification fireBaseModel) {


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                String auth_string = Util.getToken(BiddingRequestReply.this);
                NotificationApi apiService =
                        Util.getClient().create(NotificationApi.class);


                System.out.println("Nodel" + fireBaseModel.toString());
                Call<ArrayList<String>> call = apiService.sendnotificationToHotel(auth_string, fireBaseModel)/*getString()*/;

                call.enqueue(new Callback<ArrayList<String>>() {
                    @Override
                    public void onResponse(Call<ArrayList<String>> call, retrofit2.Response<ArrayList<String>> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                        int statusCode = response.code();

                        if (statusCode == 200) {

                            ArrayList<String> list = response.body();

                            Toast.makeText(BiddingRequestReply.this, "Notification Send Successfully", Toast.LENGTH_SHORT).show();
                            //sendEmailattache();
                            Intent search = new Intent(getApplicationContext(),MainActivity.class);


                            startActivity(search);


                        } else {

                            Toast.makeText(BiddingRequestReply.this, " failed due to status code:" + statusCode, Toast.LENGTH_SHORT).show();
                        }
//                callGetStartEnd();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<String>> call, Throwable t) {
                        // Log error here since request failed

                        Log.e("TAG", t.toString());
                    }
                });
            }


        });
    }
   /* public void dateCal(){
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date fd =null,td =null;
        try {
             fd = sdf.parse(fds);
            //System.out.println("Text=="+book_from_date.getText().toString()+" Date"+fd);
             td = sdf.parse(tds);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            long diff = td.getTime() - fd.getTime();
            long diffDays = diff / (24 * 60 * 60 * 1000);
            System.out.println("Diff===" + diffDays);
        }catch(Exception e){
            e.printStackTrace();
        }
    }*/


}