package app.zingo.com.agentapp.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import app.zingo.com.agentapp.CustomViews.CustomFontTextView;
import app.zingo.com.agentapp.MainActivity;
import app.zingo.com.agentapp.Model.Bookings1;
import app.zingo.com.agentapp.Model.ContactDetails;
import app.zingo.com.agentapp.Model.GSTDetails;
import app.zingo.com.agentapp.Model.HotelDetails;
import app.zingo.com.agentapp.Model.HotelNotification;
import app.zingo.com.agentapp.Model.NotificationManager;
import app.zingo.com.agentapp.Model.Rooms;
import app.zingo.com.agentapp.Model.Traveller;
import app.zingo.com.agentapp.R;
import app.zingo.com.agentapp.Utils.Constants;
import app.zingo.com.agentapp.Utils.PreferenceHandler;
import app.zingo.com.agentapp.Utils.ThreadExecuter;
import app.zingo.com.agentapp.Utils.Util;
import app.zingo.com.agentapp.WebApi.BookingApi;
import app.zingo.com.agentapp.WebApi.ContactOperations;
import app.zingo.com.agentapp.WebApi.GST;
import app.zingo.com.agentapp.WebApi.HotelOperations;
import app.zingo.com.agentapp.WebApi.NotificationApi;
import app.zingo.com.agentapp.WebApi.RoomApi;
import app.zingo.com.agentapp.WebApi.TravellerApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BookingDetailsActivity extends AppCompatActivity {

    CustomFontTextView mCancelBtn,mInvoiceBtn,mHomeBtn,mBookingCheckInTime,mBookedHotelName,mCustomerName,mGuestInfo,mTotalPrice,
    mCheckInDate,mCheckOutDate,mNoOfNights,mBookedHotelAdd,mCustomeName,mBookingMessageText,mBookingNumber,mTotalAmount,mDiscountAmount,
            mTotalPayAmount,mCall;
    ImageView mSuccess,mHotelImage;

    Bookings1 bookings1;

    //PDF Variable
    private BaseFont bfBold;
    private BaseFont bf;
    private BaseFont rf;
    private int pageNumber = 0;
    String pdfFilename = "";
    String csvFile;
    String pdfFile,serFile,roomFile;
    int pdfMinLen;

    private String mGSTNumber;
    private String duration;
    String hotelMob,hotelPhone,hotelEmail,hoteAddress,roomNumber,hotelName;
    int hotelId;
    private long diffDays;
    private double totalRoomPaid,totalRoomBalance;
    Traveller traveller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);

        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        //setTitle("Booking Details");
        Toolbar toolbar = (Toolbar) findViewById(R.id.hotel_list_back_btn);
        
        mCancelBtn = (CustomFontTextView)findViewById(R.id.cancel_booking);
        mCancelBtn.setVisibility(View.GONE);
        mInvoiceBtn = (CustomFontTextView)findViewById(R.id.invoice_booking);
       // mCancel.setVisibility(View.GONE);
        mHomeBtn = (CustomFontTextView)findViewById(R.id.go_to_home_booking);

        mBookingCheckInTime = (CustomFontTextView)findViewById(R.id.guest_check_in_time);
        mBookedHotelName = (CustomFontTextView)findViewById(R.id.booked_hotel_name);
        mCustomerName = (CustomFontTextView)findViewById(R.id.customer_name);
        mGuestInfo = (CustomFontTextView)findViewById(R.id.booked_guest_info);
        mTotalPrice = (CustomFontTextView)findViewById(R.id.booked_total_price);
        mCheckInDate = (CustomFontTextView)findViewById(R.id.booked_check_in_date);
        mCheckOutDate = (CustomFontTextView)findViewById(R.id.booked_check_out_date);
        mNoOfNights = (CustomFontTextView)findViewById(R.id.booked_no_of_nights);
        mBookedHotelAdd = (CustomFontTextView)findViewById(R.id.booked_hotel_address);
        mCustomeName = (CustomFontTextView)findViewById(R.id.customer_name);
        mBookingMessageText = (CustomFontTextView)findViewById(R.id.booking_message_text);
        mBookingNumber = (CustomFontTextView)findViewById(R.id.booking_number);
        mTotalAmount = (CustomFontTextView)findViewById(R.id.total_room_tariff);
        mDiscountAmount = (CustomFontTextView)findViewById(R.id.total_room_tariff_discount);
        mTotalPayAmount = (CustomFontTextView)findViewById(R.id.total_room_tariff_amount_payable);
        mCall = (CustomFontTextView)findViewById(R.id.call_booked_hotel);

        mSuccess = (ImageView) findViewById(R.id.success_icon);
        mHotelImage = (ImageView) findViewById(R.id.booked_hotel_image);


        Bundle bundle = getIntent().getExtras();
        if(bundle !=null)
        {
            bookings1 = (Bookings1) bundle.getSerializable("Booking");
            if(bookings1 != null)
            {
                if(bookings1.getBookingStatus().equalsIgnoreCase("Cancelled"))
                {
                    mSuccess.setVisibility(View.GONE);
                    mBookingMessageText.setText("Sorry! This booking is cancelled");
                }
                else if(bookings1.getBookingStatus().equalsIgnoreCase("quick"))
                {
                    mCancelBtn.setVisibility(View.VISIBLE);
                }
                setFields(bookings1);
            }
        }

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main = new Intent(BookingDetailsActivity.this, MainActivity.class);
                main.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK );
                startActivity(main);
                BookingDetailsActivity.this.finish();
            }
        });

        mCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call();
            }
        });

        mInvoiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(BookingDetailsActivity.this, "Your Booking is cancelled Successfully", Toast.LENGTH_SHORT).show();
//                Intent main = new Intent(BookingDetailsActivity.this, MainActivity.class);
//                main.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK );
//                startActivity(main);
//                BookingDetailsActivity.this.finish();

                try {
                    boolean iscreated = createPDF();

                    if(iscreated)
                    {
                        Intent emailIntent = new Intent(Intent.ACTION_SEND);
                        emailIntent.setType("application/pdf");
                        /*emailIntent.putExtra(Intent.EXTRA_EMAIL, mailto);*/
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT,"Zingo Invoice");
                        /*emailIntent.putExtra(Intent.EXTRA_TEXT, "Dear "+guest+",\n" +
                                "Thank you for Staying with us at "+ PreferenceHandler.getInstance(ViewPDF.this).getHotelName() +","+address+
                                ".\n" +
                                "Please find your Invoice attached to this email.\n\n"+"Wishing you a Happy Journey !\n\n\n"+"Thanks\n All rights reserved by "+ PreferenceHandler.getInstance(ViewPDF.this).getHotelName() +"\n This is a Computer Generated Invoice.\n This hotel is powered by Zingo Hotels.");*/
                        File root = Environment.getExternalStorageDirectory();
                        String pathToMyAttachedFile = "/Agent App/Pdf/Invoice/"+csvFile;
                        File file = new File(root, pathToMyAttachedFile);
                        if (!file.exists() || !file.canRead()) {
                            return;
                        }
                        Uri uri = Uri.fromFile(file);
                        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
                        startActivity(Intent.createChooser(emailIntent, "Pick an Email provider"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent main = new Intent(BookingDetailsActivity.this, MainActivity.class);
                main.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK );
                startActivity(main);
                BookingDetailsActivity.this.finish();
            }
        });

        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    if(bookings1 != null)
                    {

                        showalertbox(bookings1);
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        });




    }


    public void call()
    {


        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(BookingDetailsActivity.this);
        builder.setTitle("Do you want to Call our Customer Care Number +91-7065651651?");
        //builder.setCancelable(false);
        builder.setPositiveButton("Call", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri number = Uri.parse("tel:" + "7065651651");
                Intent dial = new Intent(Intent.ACTION_CALL, number);
                if (ActivityCompat.checkSelfPermission(BookingDetailsActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                startActivity(dial);

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, 500);
    }


    private void showalertbox(final Bookings1 bookings1) throws Exception{



        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(BookingDetailsActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.cancel_booking_alert_box_layout,null);
        TextView dontCancelBtn = (TextView)view.findViewById(R.id.cancel_booking_dont_btn);
        TextView cancelBtn = (TextView)view.findViewById(R.id.cancel_booking_btn);
        dialogBuilder.setView(view);
        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();
        dontCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    if(dialog != null)
                    {
                        dialog.dismiss();
                    }

                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try
                {
                    if(dialog != null)
                    {
                        dialog.dismiss();
                    }
                    cancelBooking(bookings1);

                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        });
    }


    private void cancelBooking(final Bookings1 booked) {
        final ProgressDialog dialog = new ProgressDialog(BookingDetailsActivity.this);
        dialog.setTitle(getResources().getString(R.string.loader_message));
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                BookingApi bookingApi = Util.getClient().create(BookingApi.class);
                final Bookings1 dBook = booked;
                dBook.setBookingStatus("Cancelled");
                /*dBook.setRefundAmount((dBook.getTotalAmount() - dBook.getBalanceAmount())+"");
                dBook.setRefundsMode("Cash");*/

                String authenticationString = Util.getToken(BookingDetailsActivity.this);
                Call<String> checkout = bookingApi.updateBookingStatus(authenticationString,dBook.getBookingId(),dBook);
                checkout.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {


                        try
                        {
                            if(response.code() == 204)
                            {
                                //updateRoom(dialog,dBook);
                                Toast.makeText(BookingDetailsActivity.this,"Booking cancelled successfully",Toast.LENGTH_SHORT).show();
                                HotelNotification notify = new HotelNotification();
                                notify.setHotelId(dBook.getHotelId());
                                //System.out.println("roomids = "+roomids);
                                // notify.setMessage(bookings.getProfileId()+","+bookings.getCheckInDate()+","+bookings.getCheckOutDate()+","+bookings.getTotalAmount()+","+room);
                                notify.setMessage("Sorry! "+"This booking cancelled "+dBook.getCheckInDate()+
                                        " to "+dBook.getCheckOutDate()+"\nBooking Number:"+dBook.getBookingNumber()+","+dBook.getProfileId());
                                notify.setTitle("Cancelled Booking");
                                notify.setSenderId(Constants.senderId);
                                notify.setServerId(Constants.serverId);
                                sendfirebaseNotification(notify,dBook);
                            }
                            else
                            {
                                if(dialog != null)
                                {
                                    dialog.dismiss();
                                }
                                Toast.makeText(BookingDetailsActivity.this,"Please try after some time",Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception ex)
                        {
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                    }
                });
            }
        });
    }



    private void sendfirebaseNotification(final HotelNotification notification, final Bookings1 bookings1) {

        final ProgressDialog dialog = new ProgressDialog(BookingDetailsActivity.this);
        dialog.setMessage(getResources().getString(R.string.loader_message));
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("Hotel id = "+notification.getHotelId());
                String auth_string = Util.getToken(BookingDetailsActivity.this);//"Basic " +  Base64.encodeToString(authentication.getBytes(), Base64.NO_WRAP);
                NotificationApi travellerApi = Util.getClient().create(NotificationApi.class);
                Call<ArrayList<String>> response = travellerApi.sendnotificationToHotel(auth_string,notification);

                response.enqueue(new Callback<ArrayList<String>>() {
                    @Override
                    public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {

                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        System.out.println(response.code());
                        if(response.code() == 200)
                        {
                            if(response.body() != null)
                            {
                                /*Toast.makeText(ReviewHotelDetailsActivity.this,"Thank you for selecting room. Your request has been sent to hotel. " +
                                        "Please wait for there reply.",Toast.LENGTH_SHORT).show();*/
                                //SelectRoom.this.finish();
                                /*Toast.makeText(ReviewHotelDetailsActivity.this,"Booking done successfully",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ReviewHotelDetailsActivity.this,BookingDetailsActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("Booking",bookings1);
                                intent.putExtras(bundle);
                                startActivity(intent);*/

                                NotificationManager nf = new NotificationManager();
                                nf.setNotificationText(notification.getTitle());
                                nf.setNotificationFor(notification.getMessage());
                                nf.setHotelId(notification.getHotelId());
                                savenotification(nf,bookings1);


                            }
                        }
                        else
                        {
                            Toast.makeText(BookingDetailsActivity.this, "Sending notification failed due to "+response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<String>> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        Toast.makeText(BookingDetailsActivity.this, "Sending notification failed due to "+t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void savenotification(final NotificationManager notification, final Bookings1 bookings1) {

        final ProgressDialog dialog = new ProgressDialog(BookingDetailsActivity.this);
        dialog.setMessage(getResources().getString(R.string.loader_message));
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("Hotel id = "+notification.getHotelId());
                String auth_string = Util.getToken(BookingDetailsActivity.this);
                NotificationApi travellerApi = Util.getClient().create(NotificationApi.class);
                Call<NotificationManager> response = travellerApi.saveNotification(auth_string,notification);

                response.enqueue(new Callback<NotificationManager>() {
                    @Override
                    public void onResponse(Call<NotificationManager> call, Response<NotificationManager> response) {

                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        System.out.println(response.code());
                        if(response.code() == 200||response.code() == 201)
                        {
                            if(response.body() != null)
                            {
                                /*Toast.makeText(ReviewHotelDetailsActivity.this,"Thank you for selecting room. Your request has been sent to hotel. " +
                                        "Please wait for there reply.",Toast.LENGTH_SHORT).show();*/
                                //SelectRoom.this.finish();
                                /*Toast.makeText(BookingHistoryDetailsActivity.this,"Booking done successfully",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(BookingHistoryDetailsActivity.this,BookingDetailsActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("Booking",bookings1);
                                intent.putExtras(bundle);
                                startActivity(intent);*/
                                //BookingDetailsActivity.this.finish();
                                Intent main = new Intent(BookingDetailsActivity.this, MainActivity.class);
                                main.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK );
                                startActivity(main);
                                BookingDetailsActivity.this.finish();

                                //Toast.makeText(ReviewHotelDetailsActivity.this, "Save Notification", Toast.LENGTH_SHORT).show();




                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<NotificationManager> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                    }
                });
            }
        });
    }

    private void setFields(Bookings1 bookings1) {

        try {
            /*if(bookings1.getCheckInTime() == null)
            {
                mBookingCheckInTime.setText("Check in time: 12:00 onwards");
            }
            else
            {
                mBookingCheckInTime.setText("Check in time: "+bookings1.getCheckInTime()+" onwards");
            }*/

            duration(bookings1.getCheckInDate(),bookings1.getCheckOutDate());
            mGuestInfo.setText(bookings1.getNoOfAdults()+" Adults, "+bookings1.getNoOfChild()+" Child(s)\n"+bookings1.getNoOfRooms()+" Rooms");
            mTotalPrice.setText("₹ "+bookings1.getTotalAmount());
            mBookingNumber.setText("Booking id: #"+bookings1.getBookingNumber());

            if(bookings1.getRoomId() != 0)
            {
                getRoomDetails(bookings1.getRoomId());
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("EE, dd MMM");

            Date fdate = simpleDateFormat.parse(bookings1.getCheckInDate());
            Date tdate = simpleDateFormat.parse(bookings1.getCheckOutDate());
            mCheckInDate.setText(simpleDateFormat1.format(fdate));
            mCheckOutDate.setText(simpleDateFormat1.format(tdate));
            getHotel(bookings1.getHotelId());
            getContactByHotelId(bookings1.getHotelId());
            getTravellerName(bookings1.getTravellerId());


            int days = getDays(bookings1);
            mNoOfNights.setText(days+"N");
            mTotalAmount.setText("₹ "+bookings1.getDeclaredRate()*days*bookings1.getNoOfRooms()+"");
            mDiscountAmount.setText("₹ "+(bookings1.getDeclaredRate() - bookings1.getSellRate())*days*bookings1.getNoOfRooms()+"");
            mTotalPayAmount.setText("₹ "+bookings1.getTotalAmount()+"");

            System.out.println("₹ "+bookings1.getTotalAmount());

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }


    }


    public  void getTravellerName(final int i)
    {
        String auth_string = Util.getToken(BookingDetailsActivity.this);//"Basic " +  Base64.encodeToString(authentication.getBytes(), Base64.NO_WRAP);
        TravellerApi api = Util.getClient().create(TravellerApi.class);
        Call<Traveller> getTrav = api.getTravellerDetails(auth_string,i);

        getTrav.enqueue(new Callback<Traveller>() {
            @Override
            public void onResponse(Call<Traveller> call, Response<Traveller> response) {
                if(response.code() == 200)
                {
                    if(response.body() != null)
                    {
                        traveller =response.body();
                        mCustomeName.setText("For: "+response.body().getFirstName());
                    }

                }
                else
                {
                    System.out.println(response.message());
                }
            }

            @Override
            public void onFailure(Call<Traveller> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    private void getHotel(final int id){

        final ProgressDialog progressDialog = new ProgressDialog(BookingDetailsActivity.this);
        progressDialog.setTitle("please wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                String auth_string = Util.getToken(BookingDetailsActivity.this);//"Basic " +  Base64.encodeToString(authentication.getBytes(), Base64.NO_WRAP);
                HotelOperations apiService =
                        Util.getClient().create(HotelOperations.class);
                Call<HotelDetails> call = apiService.getHotelByHotelId(auth_string,id);

                call.enqueue(new Callback<HotelDetails>() {
                    @Override
                    public void onResponse(Call<HotelDetails> call, Response<HotelDetails> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                        int statusCode = response.code();
                        if (statusCode == 200) {

                            if (progressDialog!=null)
                                progressDialog.dismiss();
                            HotelDetails list1 =  response.body();

                            if(list1 != null)
                            {
                                mBookedHotelName.setText(list1.getHotelDisplayName());
                                mBookedHotelAdd.setText(list1.getHotelStreetAddress());
                                getGSTNumber(list1.getHotelId());
                                hotelName = list1.getHotelDisplayName();
                                mBookingCheckInTime.setText("Check in time: "+list1.getStandardCheckInTime()+" onwards");
                                if(list1.getHotelImage() != null && list1.getHotelImage().size() != 0)
                                {
                                    mHotelImage.setImageBitmap(Util.convertToBitMap(list1.getHotelImage().get(0).getImages()));
                                }
                            }
                            //else
                            /*{
                                Intent intent = new Intent(AddBankDetailsActivity.this,AddBankDetailsActivity.class);
                                startActivity(intent);
                            }*/
//                            Object dto = response.body();
//                            listCities.add(dto);



                        }else {
                            if (progressDialog!=null)
                                progressDialog.dismiss();
                            Toast.makeText(BookingDetailsActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                        }
//                callGetStartEnd();
                    }

                    @Override
                    public void onFailure(Call<HotelDetails> call, Throwable t) {
                        // Log error here since request failed
                        if (progressDialog!=null)
                            progressDialog.dismiss();
                        Log.e("TAG", t.toString());
                    }
                });
            }


        });
    }

    private int getDays(Bookings1 bookings1) {
        //System.out.println("Text=="+checkinDate+" Date"+checkoutDate);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date fd = null,td = null;
        try {
            fd = sdf.parse(bookings1.getCheckInDate());
            //System.out.println("Text=="+book_from_date.getText().toString()+" Date"+fd);
            td = sdf.parse(bookings1.getCheckOutDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        System.out.println("From==="+fd+" To===="+td);
        try {
            long diff = td.getTime() - fd.getTime();
            int diffDays = (int) diff / (24 * 60 * 60 * 1000);
            System.out.println("Diff===" + diffDays);
            return diffDays;

        }catch(Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    private boolean createPDF () throws Exception{

        Document doc = new Document();
        PdfWriter docWriter = null;
        initializeFonts();

        try {
            File sd = Environment.getExternalStorageDirectory();
            csvFile = System.currentTimeMillis() + ".pdf";

            File directory = new File(sd.getAbsolutePath()+"/Agent App/Pdf/Invoice/");
            //create directory if not exist
            if (!directory.exists() && !directory.isDirectory()) {
                directory.mkdirs();
            }

            pdfFile = sd.getAbsolutePath()+"/Agent App/Pdf/Invoice/"+csvFile;

            File file = new File(directory, csvFile);
            String path = "docs/" + pdfFilename;
            docWriter = PdfWriter.getInstance(doc , new FileOutputStream(file));
            doc.addAuthor("Lucida Hospitality Pvt Ltd");
            doc.addCreationDate();
            doc.addProducer();
            doc.addCreator("zingohotels.com");
            doc.addTitle("Invoice");
            doc.setPageSize(PageSize.LETTER);

            doc.open();
            PdfContentByte cb = docWriter.getDirectContent();

            boolean beginPage = true;
            int y = 0;


            if(beginPage){
                // beginPage = false;
                generateLayout(doc, cb);
                generateHeader(doc, cb);

                if(bookings1.getDiscount()==0&&bookings1.getDeclaredRate()==0){
                    generateDetailZero(doc, cb);
                }else{
                    generateDetail(doc, cb);
                }

                y = 615;
            }


            // createTables(cb);
            //printPageNumber(cb);
            return true;

        }
        catch (DocumentException dex)
        {
            dex.printStackTrace();
            return false;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return false;
        }
        finally
        {
            if (doc != null)
            {
                doc.close();
            }
            if (docWriter != null)
            {
                docWriter.close();
            }
        }
    }


    private void generateLayout(Document doc, PdfContentByte cb)  {

        try {

            cb.setLineWidth(1f);

            // Invoice Header box layout
            cb.rectangle(400,660,185,100);
            cb.moveTo(400,680);
            cb.lineTo(585,680);
            cb.moveTo(400,720);
            cb.lineTo(585,720);
            cb.moveTo(400,740);
            cb.lineTo(585,740);
            cb.moveTo(400,700);
            cb.lineTo(585,700);
            cb.moveTo(480,660);
            cb.lineTo(480,760);
            cb.stroke();

            // Invoice Header box Text Headings
            createHeadings(cb,402,743,"GST#");
            createHeadings(cb,402,723,"Room No");
            createHeadings(cb,402,703,"CheckIn Date");
            createHeadings(cb,402,683,"CheckOut Date");
            createHeadings(cb,402,663,"Customer Support");

            //Total
            createHeadings(cb,422,300,"Total");
            createHeadings(cb,422,280,"Amount Paid");
            createHeadings(cb,422,260,"Balance Due");

            // Invoice Detail box layout
            cb.rectangle(110,340,395,200);
            cb.moveTo(110,560);
            cb.lineTo(505,560);
            cb.moveTo(110,340);
            cb.lineTo(110,560);
            cb.moveTo(240,340);
            cb.lineTo(240,560);
            cb.moveTo(340,340);
            cb.lineTo(340,560);
            cb.moveTo(430,340);
            cb.lineTo(430,560);
            cb.moveTo(505,340);
            cb.lineTo(505,560);
            cb.stroke();

            // Invoice Detail box Text Headings
            // createHeadings(cb,42,633,"Item");
            createHeadings(cb,135,543,"Particulars");
            createHeadings(cb,270,543,"Tariff");
            createHeadings(cb,370,543,"Quantity");
            createHeadings(cb,460,543,"Amount");

            cb.rectangle(420,256,150,60);
            cb.moveTo(420,276);
            cb.lineTo(570,276);
            cb.moveTo(420,296);
            cb.lineTo(570,296);
            cb.moveTo(480,256);
            cb.lineTo(480,316);
            cb.stroke();


            Drawable d = getResources ().getDrawable (R.drawable.logo_zingo);
            Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
            ByteArrayOutputStream streams = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, streams);
            byte[] bitmapData = streams.toByteArray();

            Image companyLogo = Image.getInstance(bitmapData);
            // Image companyLogo = Image.getInstance("logo.png");
            companyLogo.setAbsolutePosition(25,700);
            companyLogo.scalePercent(75);
            doc.add(companyLogo);



        }

       /* catch (DocumentException dex){
            dex.printStackTrace();
        }*/
        catch (Exception ex){
            ex.printStackTrace();
        }

    }


    private void generateHeader(Document doc, PdfContentByte cb)  {

        try {

            createHeadingsTitle(cb,200,750, hotelName+" Invoice");

            //Hotel Information
            createHeadings(cb,100,730,"Hotel Inforamation");
            createHeadings(cb,100,715,"Hotel Name: "+hotelName);
            createHeadings(cb,100,700,"Hotel E-mail: "+hotelEmail);
            createHeadings(cb,100,685,"Hotel Phone Number: "+hotelPhone+" | "+hotelMob);
            createHeadings(cb,100,670,"Address "+hoteAddress);

            //Guest Iniformation
            createHeadings(cb,100,640,"Guest Inforamation");
            createHeadings(cb,100,625,"Guest Name: "+traveller.getFirstName());
            createHeadings(cb,100,610,"E-mail: "+traveller.getEmail());
            createHeadings(cb,100,595,"Phone Number: "+traveller.getPhoneNumber());
            createHeadings(cb,100,580,"Booking Number: "+bookings1.getBookingNumber());
            createHeadings(cb, 402, 580, "Invoice Number: " + bookings1.getBookingNumber() + "BN");
            // createHeadings(cb,200,690,"Country");

            long date = System.currentTimeMillis();

            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            String invoiceDate = sdf.format(date);

            createHeadings(cb,482,743,mGSTNumber);//Booking Number
            if( roomNumber!= null && !roomNumber.isEmpty())
            {
                createHeadings(cb,482,723,roomNumber);
            }
            else
            {
                createHeadings(cb,482,723,"No Room");
            }//Room Number
            createHeadings(cb,482,703,bookings1.getCheckInDate());//Date today
            createHeadings(cb,482,683,bookings1.getCheckOutDate());//Date today
            createHeadings(cb,482,663,"hello@zingohotels.com");//Date today

            createHeadings(cb,482,300,""+bookings1.getTotalAmount());

            if(bookings1.getPaymentList().size()!=0){

                totalRoomPaid = 0;
                for(int i=0;i<bookings1.getPaymentList().size();i++){
                    totalRoomPaid = totalRoomPaid+bookings1.getPaymentList().get(i).getAmount();
                }

            }else{
                totalRoomPaid=0;
            }
            totalRoomBalance =  bookings1.getTotalAmount() - totalRoomPaid;
            createHeadings(cb,482,280,""+totalRoomPaid);
            createHeadings(cb,482,260,""+totalRoomBalance);

        }

        catch (Exception ex){
            ex.printStackTrace();
        }

    }

    private void generateDetail(Document doc, PdfContentByte cb)  {
        DecimalFormat df = new DecimalFormat("##.###");

        try {

            // createContent(cb,48,615,"Room Charges",PdfContentByte.ALIGN_CENTER);
            createContent(cb,135,525, "Room Charges",PdfContentByte.ALIGN_LEFT);
            createContent(cb,300,525, ""+bookings1.getDeclaredRate() ,PdfContentByte.ALIGN_LEFT);
            createContent(cb,380,525, duration,PdfContentByte.ALIGN_CENTER);
            createContent(cb,500,525, String.valueOf(diffDays * bookings1.getDeclaredRate()),PdfContentByte.ALIGN_RIGHT);

            // createContent(cb,48,600,"Extra Charges",PdfContentByte.ALIGN_CENTER);
            createContent(cb,135,510, "Extra Charges",PdfContentByte.ALIGN_LEFT);
            createContent(cb,300,510, ""+bookings1.getExtraCharges(),PdfContentByte.ALIGN_LEFT);
            createContent(cb,380,510, "-",PdfContentByte.ALIGN_CENTER);
            createContent(cb,500,510, ""+bookings1.getExtraCharges(),PdfContentByte.ALIGN_RIGHT);

            // createContent(cb,48,585,"Discount",PdfContentByte.ALIGN_CENTER);
            createContent(cb,135,495, "Discount",PdfContentByte.ALIGN_LEFT);
            createContent(cb,300,495, ""+bookings1.getDiscount(),PdfContentByte.ALIGN_LEFT);
            createContent(cb,380,495, duration,PdfContentByte.ALIGN_CENTER);
            long sell = diffDays * bookings1.getDiscountAmount();
            createContent(cb,500,495, "-"+String.valueOf(sell),PdfContentByte.ALIGN_RIGHT);

            createContent(cb,135,480, "",PdfContentByte.ALIGN_LEFT);
            createContent(cb,300,480, "",PdfContentByte.ALIGN_LEFT);
            createContent(cb,380,480, "",PdfContentByte.ALIGN_CENTER);
            createContent(cb,500,480, "",PdfContentByte.ALIGN_RIGHT);

            //  createContent(cb,48,570,"GST%",PdfContentByte.ALIGN_CENTER);
            createContent(cb,135,465, "GST Percentage",PdfContentByte.ALIGN_LEFT);
            createContent(cb,300,465, ""+bookings1.getGst(),PdfContentByte.ALIGN_LEFT);
            createContent(cb,380,465, "-",PdfContentByte.ALIGN_CENTER);
            createContent(cb,500,465, "",PdfContentByte.ALIGN_RIGHT);

            // createContent(cb,48,555,"GST Amount",PdfContentByte.ALIGN_CENTER);
            double gst = Double.parseDouble(""+bookings1.getGstAmount());
            double value = gst / 2;
            //double values = duration * value;
            createContent(cb,135,430, "CGST",PdfContentByte.ALIGN_LEFT);
            createContent(cb,300,430,df.format(value),PdfContentByte.ALIGN_LEFT);
            createContent(cb,380,430, duration,PdfContentByte.ALIGN_CENTER);

            createContent(cb,500,430, df.format(diffDays*value),PdfContentByte.ALIGN_RIGHT);

            createContent(cb,135,415, "SGST",PdfContentByte.ALIGN_LEFT);
            createContent(cb,300,415,df.format(value),PdfContentByte.ALIGN_LEFT);
            createContent(cb,380,415, duration,PdfContentByte.ALIGN_CENTER);
            createContent(cb,500,415, df.format(diffDays*value),PdfContentByte.ALIGN_RIGHT);

            createContent(cb,135,400, "",PdfContentByte.ALIGN_LEFT);
            createContent(cb,300,400, "",PdfContentByte.ALIGN_LEFT);
            createContent(cb,380,400, "",PdfContentByte.ALIGN_CENTER);
            createContent(cb,500,400, "",PdfContentByte.ALIGN_RIGHT);

            double serviceAmount = 0;

           /* if(bookings1.getServicesList().size()!=0){

                // totalRoomPaid = 0;
                for(int i=0;i<bookings1.getServicesList().size();i++){
                    serviceAmount = serviceAmount+bookings1.getServicesList().get(i).getAmount();
                }

            }else{
                serviceAmount=0;
            }*/

           /* createContent(cb,135,385, "Service Amount(Inc.GST)",PdfContentByte.ALIGN_LEFT);
            createContent(cb,300,385,""+serviceAmount,PdfContentByte.ALIGN_LEFT);
            createContent(cb,380,385, "-",PdfContentByte.ALIGN_CENTER);
            createContent(cb,500,385, ""+serviceAmount,PdfContentByte.ALIGN_RIGHT);

            createContent(cb,135,370, "",PdfContentByte.ALIGN_LEFT);
            createContent(cb,300,370, "",PdfContentByte.ALIGN_LEFT);
            createContent(cb,380,370, "",PdfContentByte.ALIGN_CENTER);
            createContent(cb,500,370, "",PdfContentByte.ALIGN_RIGHT);*/

            // createContent(cb,48,540,"Total Amount",PdfContentByte.ALIGN_CENTER);
            createContent(cb,135,355, "Total Amount",PdfContentByte.ALIGN_LEFT);
            createContent(cb,300,355, mTotalAmount.getText().toString(),PdfContentByte.ALIGN_LEFT);
            createContent(cb,380,355, "-",PdfContentByte.ALIGN_CENTER);
            createContent(cb,500,355, mTotalAmount.getText().toString(),PdfContentByte.ALIGN_RIGHT);

            createContent(cb,48,240,"Additional Notes",PdfContentByte.ALIGN_LEFT);

            createContent(cb,48,220,"This is computer generated Invoice.",PdfContentByte.ALIGN_LEFT);
            createContent(cb,48,200,"All rights reserved by "+hotelName,PdfContentByte.ALIGN_LEFT);
            createContent(cb,48,180,"This Hotel is powered by ZingoHotels.",PdfContentByte.ALIGN_LEFT);

            createContent(cb,300,150, "ZingoHotels.com",PdfContentByte.ALIGN_CENTER);
            createContent(cb,300,130, "#88, 1st Floor, Koramangala Industrial Layout 5th Block, Near JNC College, Bangalore-560095",PdfContentByte.ALIGN_CENTER);
            createContent(cb,300,110, "www.Zingohotels.com",PdfContentByte.ALIGN_CENTER);



        }

        catch (Exception ex){
            ex.printStackTrace();
        }

    }

    private void generateDetailZero(Document doc, PdfContentByte cb)  {
        DecimalFormat df = new DecimalFormat("##.###");

        try {

            // createContent(cb,48,615,"Room Charges",PdfContentByte.ALIGN_CENTER);
            createContent(cb,135,525, "Room Charges",PdfContentByte.ALIGN_LEFT);
            createContent(cb,300,525, ""+bookings1.getSellRate() ,PdfContentByte.ALIGN_LEFT);
            createContent(cb,380,525, duration,PdfContentByte.ALIGN_CENTER);
            createContent(cb,500,525, String.valueOf(diffDays * bookings1.getSellRate()),PdfContentByte.ALIGN_RIGHT);

            // createContent(cb,48,600,"Extra Charges",PdfContentByte.ALIGN_CENTER);
            createContent(cb,135,510, "Extra Charges",PdfContentByte.ALIGN_LEFT);
            createContent(cb,300,510, ""+bookings1.getExtraCharges(),PdfContentByte.ALIGN_LEFT);
            createContent(cb,380,510, "-",PdfContentByte.ALIGN_CENTER);
            createContent(cb,500,510, ""+bookings1.getExtraCharges(),PdfContentByte.ALIGN_RIGHT);

            // createContent(cb,48,585,"Discount",PdfContentByte.ALIGN_CENTER);
          /*  createContent(cb,135,495, "Discount",PdfContentByte.ALIGN_LEFT);
            createContent(cb,300,495, mBookedDiscount.getText().toString(),PdfContentByte.ALIGN_LEFT);
            createContent(cb,380,495, duration,PdfContentByte.ALIGN_CENTER);
            long sell = diffDays * booked.getDiscountAmount();
            createContent(cb,500,495, "-"+String.valueOf(sell),PdfContentByte.ALIGN_RIGHT);*/

            createContent(cb,135,480, "",PdfContentByte.ALIGN_LEFT);
            createContent(cb,300,480, "",PdfContentByte.ALIGN_LEFT);
            createContent(cb,380,480, "",PdfContentByte.ALIGN_CENTER);
            createContent(cb,500,480, "",PdfContentByte.ALIGN_RIGHT);

            //  createContent(cb,48,570,"GST%",PdfContentByte.ALIGN_CENTER);
            createContent(cb,135,465, "GST Percentage",PdfContentByte.ALIGN_LEFT);
            createContent(cb,300,465, ""+bookings1.getGst(),PdfContentByte.ALIGN_LEFT);
            createContent(cb,380,465, "-",PdfContentByte.ALIGN_CENTER);
            createContent(cb,500,465, "",PdfContentByte.ALIGN_RIGHT);

            // createContent(cb,48,555,"GST Amount",PdfContentByte.ALIGN_CENTER);
            double gst = Double.parseDouble(""+bookings1.getGstAmount());
            double value = gst / 2;
            //double values = duration * value;
            createContent(cb,135,430, "CGST",PdfContentByte.ALIGN_LEFT);
            createContent(cb,300,430,df.format(value),PdfContentByte.ALIGN_LEFT);
            createContent(cb,380,430, duration,PdfContentByte.ALIGN_CENTER);

            createContent(cb,500,430, df.format(diffDays*value),PdfContentByte.ALIGN_RIGHT);

            createContent(cb,135,415, "SGST",PdfContentByte.ALIGN_LEFT);
            createContent(cb,300,415,df.format(value),PdfContentByte.ALIGN_LEFT);
            createContent(cb,380,415, duration,PdfContentByte.ALIGN_CENTER);
            createContent(cb,500,415, df.format(diffDays*value),PdfContentByte.ALIGN_RIGHT);

            createContent(cb,135,400, "",PdfContentByte.ALIGN_LEFT);
            createContent(cb,300,400, "",PdfContentByte.ALIGN_LEFT);
            createContent(cb,380,400, "",PdfContentByte.ALIGN_CENTER);
            createContent(cb,500,400, "",PdfContentByte.ALIGN_RIGHT);

            double serviceAmount = 0;

           /* if(bookings1.getServicesList().size()!=0){

                // totalRoomPaid = 0;
                for(int i=0;i<bookings1.getServicesList().size();i++){
                    serviceAmount = serviceAmount+bookings1.getServicesList().get(i).getAmount();
                }

            }else{
                serviceAmount=0;
            }*/

          /*  createContent(cb,135,385, "Service Amount(Inc.GST)",PdfContentByte.ALIGN_LEFT);
            createContent(cb,300,385,""+serviceAmount,PdfContentByte.ALIGN_LEFT);
            createContent(cb,380,385, "-",PdfContentByte.ALIGN_CENTER);
            createContent(cb,500,385, ""+serviceAmount,PdfContentByte.ALIGN_RIGHT);
            createContent(cb,135,370, "",PdfContentByte.ALIGN_LEFT);
            createContent(cb,300,370, "",PdfContentByte.ALIGN_LEFT);
            createContent(cb,380,370, "",PdfContentByte.ALIGN_CENTER);
            createContent(cb,500,370, "",PdfContentByte.ALIGN_RIGHT);*/

            // createContent(cb,48,540,"Total Amount",PdfContentByte.ALIGN_CENTER);
            createContent(cb,135,355, "Total Amount",PdfContentByte.ALIGN_LEFT);
            createContent(cb,300,355, mTotalAmount.getText().toString(),PdfContentByte.ALIGN_LEFT);
            createContent(cb,380,355, "-",PdfContentByte.ALIGN_CENTER);
            createContent(cb,500,355, mTotalAmount.getText().toString(),PdfContentByte.ALIGN_RIGHT);

            createContent(cb,48,240,"Additional Notes",PdfContentByte.ALIGN_LEFT);

            createContent(cb,48,220,"This is computer generated Invoice.",PdfContentByte.ALIGN_LEFT);
            createContent(cb,48,200,"All rights reserved by "+hotelName,PdfContentByte.ALIGN_LEFT);
            createContent(cb,48,180,"This Hotel is powered by ZingoHotels.",PdfContentByte.ALIGN_LEFT);

            createContent(cb,300,150, "ZingoHotels.com",PdfContentByte.ALIGN_CENTER);
            createContent(cb,300,130, "#88, 1st Floor, Koramangala Industrial Layout 5th Block, Near JNC College, Bangalore-560095",PdfContentByte.ALIGN_CENTER);
            createContent(cb,300,110, "www.Zingohotels.com",PdfContentByte.ALIGN_CENTER);





        }

        catch (Exception ex){
            ex.printStackTrace();
        }

    }

    private void createHeadings(PdfContentByte cb, float x, float y, String text){


        cb.beginText();
        cb.setFontAndSize(bfBold, 8);
        cb.setTextMatrix(x,y);
        cb.showText(text.trim());
        cb.endText();

    }

    private void createHeadingsTitle(PdfContentByte cb, float x, float y, String text){


        cb.beginText();
        cb.setFontAndSize(bfBold, 15);
        cb.setTextMatrix(x,y);
        cb.showText(text.trim());
        cb.endText();

    }

    private void printPageNumber(PdfContentByte cb){


        cb.beginText();
        cb.setFontAndSize(bfBold, 8);
        cb.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Page No. " + (pageNumber+1), 570 , 10, 0);
        cb.endText();

       // pageNumber++;

    }

    private void createContent(PdfContentByte cb, float x, float y, String text, int align){


        cb.beginText();
        cb.setFontAndSize(bf, 8);
        cb.showTextAligned(align, text.trim(), x , y, 0);
        cb.endText();

    }

    private void createFooter(PdfContentByte cb, float x, float y, String text, int align){


        cb.beginText();
        cb.setFontAndSize(rf, 8);
        cb.showTextAligned(align, text.trim(), x , y, 0);
        cb.endText();

    }

    private void initializeFonts(){


        try {
            bfBold = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void getGSTNumber(int HotelId)
    {
        String auth_string = Util.getToken(BookingDetailsActivity.this);//"Basic " +  Base64.encodeToString(authentication.getBytes(), Base64.NO_WRAP);
        GST api = Util.getClient().create(GST.class);
        Call<ArrayList<GSTDetails>> getRoom = api.fetchGST(auth_string,HotelId);

        getRoom.enqueue(new Callback<ArrayList<GSTDetails>>() {
            @Override
            public void onResponse(Call<ArrayList<GSTDetails>> call, Response<ArrayList<GSTDetails>> response) {
                if(response.code() == 200)
                {

                    ArrayList<GSTDetails> list = response.body();

                    if(list.size()==0){
                        mGSTNumber="No GSTIN";
                    }else{
                        mGSTNumber = response.body().get((list.size()-1)).getGSTNumber();
                    }



                }
                else
                {
                    System.out.println(response.message());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<GSTDetails>> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    /*private void gethotelDetails(){



        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                String auth_string = Util.getToken(BookingDetailsActivity.this);//"Basic " +  Base64.encodeToString(authentication.getBytes(), Base64.NO_WRAP);
                HotelOperations apiService =
                        Util.getClient().create(HotelOperations.class);
                Call<HotelDetails> call = apiService.getHotelByHotelId(auth_string,PreferenceHandler.getInstance(BookingDetailsActivity.this).getHotelID());

                call.enqueue(new Callback<HotelDetails>() {
                    @Override
                    public void onResponse(Call<HotelDetails> call, Response<HotelDetails> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                        int statusCode = response.code();
                        if (statusCode == 200) {


                            hotelName = response.body().getHotelName();
                            hoteAddress = response.body().getLocalty()+","+response.body().getCity()+","+response.body().getState();
                            System.out.println("Address"+hoteAddress);

                        }else {

                            Toast.makeText(BookingDetailsActivity.this, "Login failed due to status code: "+response.message(), Toast.LENGTH_SHORT).show();
                        }
//                callGetStartEnd();
                    }

                    @Override
                    public void onFailure(Call<HotelDetails> call, Throwable t) {
                        // Log error here since request failed

                        Log.e("TAG", t.toString());
                    }
                });
            }


        });
    }*/

    public void getContactByHotelId(final int i)
    {
        final ProgressDialog dialog = new ProgressDialog(BookingDetailsActivity.this);
        dialog.setMessage(getResources().getString(R.string.loader_message));
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                String authenticationString = Util.getToken(BookingDetailsActivity.this);
                ContactOperations operations = Util.getClient().create(ContactOperations.class);
                Call<ArrayList<ContactDetails>> getContactResponse = operations.getContactByHotelId(authenticationString,i);

                getContactResponse.enqueue(new Callback<ArrayList<ContactDetails>>() {
                    @Override
                    public void onResponse(Call<ArrayList<ContactDetails>> call, Response<ArrayList<ContactDetails>> response) {
                        System.out.println("Code  = "+response.code());

                        ArrayList<ContactDetails> contactResponse = response.body();
                        if(dialog != null && dialog.isShowing())
                        {
                            dialog.dismiss();
                        }
                        if(response.code() == 200 && contactResponse != null && contactResponse.size() != 0)
                        {
                            final ContactDetails contactInfo = contactResponse.get(contactResponse.size()-1);


                            hotelMob =  contactInfo.getHotelMobile().toString();
                            hotelPhone =  contactInfo.getHotelPhone().toString();
                            hotelEmail =  contactInfo.getHotelEmail().toString();



                        }else{
                            hotelMob =  "No Mobile";
                            hotelPhone =  "No Phone";
                            hotelEmail =  "No Email";
                        }



                    }

                    @Override
                    public void onFailure(Call<ArrayList<ContactDetails>> call, Throwable t) {
                        System.out.println("Contact failed");
                        if(dialog != null && dialog.isShowing())
                        {
                            dialog.dismiss();
                        }
                    }
                });
            }
        });
    }

    public void getRoomDetails(final int i)
    {
        String auth_string = Util.getToken(BookingDetailsActivity.this);//"Basic " +  Base64.encodeToString(authentication.getBytes(), Base64.NO_WRAP);
        RoomApi api = Util.getClient().create(RoomApi.class);
        Call<Rooms> getRoom = api.getRoom(auth_string,i);

        getRoom.enqueue(new Callback<Rooms>() {
            @Override
            public void onResponse(Call<Rooms> call, Response<Rooms> response) {
                if(response.code() == 200)
                {
                    if(response.body() != null)
                    {
                        /*pendingroom = response.body();
                        change_rooms = response.body();*/
                        roomNumber = response.body().getRoomNo();
                        //System.out.println("Chanfe"+change_rooms);
                        /*mBookedRoomnumber.setText(response.body().getRoomNo()+"");
                        mBookedRoomFloor.setText(response.body().getFloor()+" Floor");*/
                    }

                }
                else
                {
                    System.out.println(response.message());
                }
            }

            @Override
            public void onFailure(Call<Rooms> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    public void duration(String fromm,String too){

        String from = fromm+" 00:00:00";
        String to = too+" 00:00:00";

        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date d1 = null;
        Date d2 = null;
        try {

            if(from.contains("-"))
            {
                d1 = format1.parse(from);
                d2 = format1.parse(to);
            }
            else
            {
                d1 = format.parse(from);
                d2 = format.parse(to);
            }
            long diff = d2.getTime() - d1.getTime();
            diffDays = diff / (24 * 60 * 60 * 1000);
            duration = String.valueOf(diffDays);
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id)
        {
            case android.R.id.home:
                Intent main = new Intent(BookingDetailsActivity.this, MainActivity.class);
                main.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK );
                startActivity(main);
                BookingDetailsActivity.this.finish();
                break;
        }


        return super.onOptionsItemSelected(item);
    }
}
