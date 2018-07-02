package app.zingo.com.agentapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import app.zingo.com.agentapp.Activities.BiddingBookingActivity;
import app.zingo.com.agentapp.Activities.BiddingRequestReply;
import app.zingo.com.agentapp.Model.Bookings1;
import app.zingo.com.agentapp.Model.HotelDetails;
import app.zingo.com.agentapp.Model.NotificationManager;
import app.zingo.com.agentapp.Model.Traveller;
import app.zingo.com.agentapp.R;
import app.zingo.com.agentapp.Utils.PreferenceHandler;
import app.zingo.com.agentapp.Utils.ThreadExecuter;
import app.zingo.com.agentapp.Utils.Util;
import app.zingo.com.agentapp.WebApi.BookingApi;
import app.zingo.com.agentapp.WebApi.HotelOperations;
import app.zingo.com.agentapp.WebApi.TravellerApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ZingoHotels Tech on 10-05-2018.
 */

public class NotificationManagerAdapter extends RecyclerView.Adapter<NotificationManagerAdapter.ViewHolder>{

    private Context context;
    private ArrayList<NotificationManager> list;
    HotelDetails dtos;
    public NotificationManagerAdapter(Context context, ArrayList<NotificationManager> list) {

        this.context = context;
        this.list = list;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_notification_manager, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final NotificationManager dto = list.get(position);

        if(dto.getNotificationText().equalsIgnoreCase("Bidding Request Accepted")){
            holder.mTitle.setText(""+dto.getNotificationText());
            //holder.mRoom.setVisibility(View.GONE);
            holder.mBidding.setVisibility(View.GONE);
            holder.mOther.setVisibility(View.GONE);

            String message = dto.getNotificationFor();
            String bookingNumber;

            if(message.contains(",")){
                String bookin[] = message.split(",");
                System.out.println("Length=="+bookin.length);
                if(bookin.length==9){
                    String hotelid = bookin[0];
                    String fds = bookin[1];
                    String tds = bookin[2];
                    String price = bookin[3];
                    String  room= bookin[4];
                    String  adult = bookin[5];
                    String child = bookin[6];
                    String  cit = bookin[7];
                    String cot = bookin[8];

                    //profileId = Integer.parseInt(agentId);
                    getAgentDetails(Integer.parseInt(hotelid),holder.mHotelName);
                    holder.mBookingStatus.setText("Bidding Price is ₹ "+price);
                    holder.mRooms.setText(room+" Room(s)");
                    //mGuestCount.setText(adult+" Adult(s) "+child+" child");
                    holder.mCIT.setText(""+cit);
                    holder.mCOT.setText(""+cot);
                    //getAgentDetails(Integer.parseInt(agentId));
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
                        holder.mCID.setText(simpleDateFormat1.format(CIDate));
                        holder.mCOD.setText(simpleDateFormat1.format(CODate));


                    }
                    catch (ParseException ex)
                    {
                        ex.printStackTrace();
                    }


                }



            }else{

            }
        }else if(dto.getNotificationText().equalsIgnoreCase("Bidding Request Rejected")){

            holder.mTitle.setText(""+dto.getNotificationText());
            //holder.mRoom.setVisibility(View.GONE);
            holder.mBidding.setVisibility(View.GONE);
            holder.mOther.setVisibility(View.GONE);

            String message = dto.getNotificationFor();
            String bookingNumber;

            if(message.contains(",")){
                String bookin[] = message.split(",");
                System.out.println("Length=="+bookin.length);
                if(bookin.length==9){
                    String hotelid = bookin[0];
                    String fds = bookin[1];
                    String tds = bookin[2];
                    String price = bookin[3];
                    String  room= bookin[4];
                    String  adult = bookin[5];
                    String child = bookin[6];
                    String  cit = bookin[7];
                    String cot = bookin[8];

                    //profileId = Integer.parseInt(agentId);
                    getAgentDetails(Integer.parseInt(hotelid),holder.mHotelName);
                    holder.mBookingStatus.setText("Bidding Price is ₹ "+price);
                    holder.mRooms.setText(room+" Room(s)");
                    //mGuestCount.setText(adult+" Adult(s) "+child+" child");
                    holder.mCIT.setText(""+cit);
                    holder.mCOT.setText(""+cot);
                    //getAgentDetails(Integer.parseInt(agentId));
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
                        holder.mCID.setText(simpleDateFormat1.format(CIDate));
                        holder.mCOD.setText(simpleDateFormat1.format(CODate));


                    }
                    catch (ParseException ex)
                    {
                        ex.printStackTrace();
                    }


                }



            }else{

            }

        }else if(dto.getNotificationText().equalsIgnoreCase("Bidding Request Reply")){
            holder.mTitle.setText(""+dto.getNotificationText());
            //holder.mRoom.setVisibility(View.GONE);
            holder.mBookingStatus.setVisibility(View.GONE);
            holder.mBidding.setVisibility(View.VISIBLE);
            holder.mOther.setVisibility(View.GONE);

            String message = dto.getNotificationFor();
            String bookingNumber;

            if(message.contains(",")){
                String bookin[] = message.split(",");
                System.out.println("Length=="+bookin.length);
                if(bookin.length==11){
                    String hotelid = bookin[0];
                    String fds = bookin[1];
                    String tds = bookin[2];
                    String price = bookin[3];
                    String  room= bookin[4];
                    String  adult = bookin[5];
                    String child = bookin[6];
                    String  cit = bookin[7];
                    String cot = bookin[8];
                    String bid = bookin[9];
                    String reason = bookin[10];

                    //profileId = Integer.parseInt(agentId);
                    getAgentDetails(Integer.parseInt(hotelid),holder.mHotelName);
                    holder.mHBiddingPrice.setText("₹ "+bid);
                    holder.mYBiddingPrice.setText("₹ "+price);
                   // holder.mBookingStatus.setText("Bidding Price is ₹ "+price);
                    holder.mRooms.setText(room+" Room(s)");
                    //mGuestCount.setText(adult+" Adult(s) "+child+" child");
                    holder.mCIT.setText(""+cit);
                    holder.mCOT.setText(""+cot);
                    //getAgentDetails(Integer.parseInt(agentId));
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
                        holder.mCID.setText(simpleDateFormat1.format(CIDate));
                        holder.mCOD.setText(simpleDateFormat1.format(CODate));


                    }
                    catch (ParseException ex)
                    {
                        ex.printStackTrace();
                    }


                }



            }else{

            }
        }

        holder.mNotificationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(dto.getNotificationText().equalsIgnoreCase("Bidding Request Accepted")) {
                    Intent intent = new Intent(context, BiddingBookingActivity.class);
                    intent.putExtra("Title",dto.getNotificationText());
                    intent.putExtra("Message",dto.getNotificationFor());
                    context.startActivity(intent);

                }else if(dto.getNotificationText().equalsIgnoreCase("Bidding Request Rejected")){
                    Intent intent = new Intent(context, BiddingBookingActivity.class);
                    intent.putExtra("Title",dto.getNotificationText());
                    intent.putExtra("Message",dto.getNotificationFor());
                    context.startActivity(intent);
                }else if(dto.getNotificationText().equalsIgnoreCase("Bidding Request Reply")){
                    Intent intent = new Intent(context, BiddingRequestReply.class);
                    intent.putExtra("Title",dto.getNotificationText());
                    intent.putExtra("Message",dto.getNotificationFor());
                    context.startActivity(intent);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder /*implements View.OnClickListener*/ {

        public TextView mTitle,mHotelName,mBookingStatus,mCID,mCOD,mNights,mTotal,mBookingNumber,
                mCIT,mCOT,mRooms,mYBiddingPrice,mHBiddingPrice;
        CardView mNotificationLayout;
        LinearLayout mTime,mRoom,mOther,mBidding;
//

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setClickable(true);

            mNotificationLayout = (CardView) itemView.findViewById(R.id.notification_layout);
            mTitle = (TextView) itemView.findViewById(R.id.title);
            mHotelName = (TextView) itemView.findViewById(R.id.brief_detail_traveller_name);
            mBookingStatus = (TextView) itemView.findViewById(R.id.amount);
            mCID = (TextView) itemView.findViewById(R.id.brief_detail_check_in_date);
            mCOD = (TextView) itemView.findViewById(R.id.brief_detail_check_out_date);
            mNights = (TextView) itemView.findViewById(R.id.nights);
            mTotal = (TextView) itemView.findViewById(R.id.total_amount_info);
            mBookingNumber = (TextView) itemView.findViewById(R.id.booking_number);
            mYBiddingPrice = (TextView) itemView.findViewById(R.id.your_amount);
            mHBiddingPrice = (TextView) itemView.findViewById(R.id.hotelier_amount);
            mCIT= (TextView) itemView.findViewById(R.id.brief_detail_check_in_time);
            mCOT = (TextView) itemView.findViewById(R.id.brief_detail_check_out_time);
            mRooms = (TextView) itemView.findViewById(R.id.num_rooms);
            mTime = (LinearLayout) itemView.findViewById(R.id.time);
            mRoom = (LinearLayout) itemView.findViewById(R.id.layout_room);
            mOther = (LinearLayout) itemView.findViewById(R.id.layout_amount);
            mBidding = (LinearLayout) itemView.findViewById(R.id.bidding);


        }
    }
   /* public void getDetails(final String bookingsnum,final TextView tv1,final TextView tv2,final TextView tv3,final TextView tv4,final TextView tv5,final TextView tv6)
    {



        //  System.out.println("TravelleR id="+ PreferenceHandler.getInstance(RoomBookingNotifyActivity.this).getTravelerID());

        System.out.println("Hotels id = "+ PreferenceHandler.getInstance(context).getHotelID());
        System.out.println("bookkkkk"+bookingsnum);
        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                String authenticationString = Util.getToken(context);
                BookingApi bookingApi = Util.getClient().create(BookingApi.class);
                Call<ArrayList<Bookings1>> getBookingsApiResponse = bookingApi.getBookingByNum(authenticationString,bookingsnum);

                getBookingsApiResponse.enqueue(new Callback<ArrayList<Bookings1>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Bookings1>> call, Response<ArrayList<Bookings1>> response) {
                        int code = response.code();

                        if(code == 200)
                        {

                            // System.out.println("Nou"+response.body().get(0).getBookingNumber());

                            String cit   = response.body().get(0).getCheckInDate();
                            String cot = response.body().get(0).getCheckOutDate();
                            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("MMM dd yyyy");
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
                            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
                            //mCID.setText(simpleDateFormat1.format(cit));
                            // mCOD.setText(simpleDateFormat1.format(cot));
                            if(cit != null && cot != null)
                            {
                                try {
                                    Date CIDate = null,CODate= null;
                                    if(cit.contains("-"))
                                    {
                                        CIDate  =  simpleDateFormat2.parse(cit);
                                        CODate  =  simpleDateFormat2.parse(cot);
                                    }
                                    else
                                    {
                                        CIDate  =  simpleDateFormat.parse(cit);
                                        CODate  =  simpleDateFormat.parse(cot);
                                    }
                                    tv1.setText(""+simpleDateFormat1.format(CIDate));
                                    tv2.setText(""+simpleDateFormat1.format(CODate));
                                    *//*mCIT.setText(booked.getCheckInTime());
                                    mCOT.setText(booked.getCheckOutTime());*//*

                                }
                                catch (ParseException ex)
                                {
                                    ex.printStackTrace();
                                }
                            }

                            tv4.setText("₹ "+response.body().get(0).getTotalAmount());
                            tv5.setText(""+response.body().get(0).getDurationOfStay());
                            tv6.setText(""+response.body().get(0).getBookingStatus());

                            getTravellerDetails(response.body().get(0).getTravellerId(),tv3);

                        }
                        else
                        {
                            Toast.makeText(context,response.message(),Toast.LENGTH_SHORT).show();
                            System.out.println(response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Bookings1>> call, Throwable t) {

                        System.out.println(t.getMessage());
                        Toast.makeText(context,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }*/

    private void getTravellerDetails(final int travellerId,final TextView tv) {



        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                String auth_string = Util.getToken(context);//"Basic " +  Base64.encodeToString(authentication.getBytes(), Base64.NO_WRAP);
                TravellerApi travellerApi = Util.getClient().create(TravellerApi.class);
                Call<Traveller> getTravellerDetails = travellerApi.getTravellerDetails(auth_string,travellerId);

                getTravellerDetails.enqueue(new Callback<Traveller>() {
                    @Override
                    public void onResponse(Call<Traveller> call, Response<Traveller> response) {

                        if(response.code() == 200)
                        {
                            tv.setText(""+response.body().getFirstName());
                        }
                        else
                        {
                            System.out.println("traveller = "+response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<Traveller> call, Throwable t) {

                    }
                });
            }
        });
    }


    private void getAgentDetails(final int hotelid,final TextView tv) {



        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                String auth_string = Util.getToken(context);//"Basic " +  Base64.encodeToString(authentication.getBytes(), Base64.NO_WRAP);
                HotelOperations travellerApi = Util.getClient().create(HotelOperations.class);
                Call<HotelDetails> getTravellerDetails = travellerApi.getHotelByHotelId(auth_string,hotelid);

                getTravellerDetails.enqueue(new Callback<HotelDetails>() {
                    @Override
                    public void onResponse(Call<HotelDetails> call, Response<HotelDetails> response) {

                        if(response.code() == 200)
                        {
                            if(response.body()!=null){
                                tv.setText(response.body().getHotelName());
                                dtos =response.body();                            }

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


}
