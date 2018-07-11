package app.zingo.com.agentapp;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.vision.text.Line;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import app.zingo.com.agentapp.Activities.BidingActivity;
import app.zingo.com.agentapp.Activities.BookingDetailsActivity;
import app.zingo.com.agentapp.Activities.BookingHistoryActivity;
import app.zingo.com.agentapp.Activities.ComingSoonActivity;
import app.zingo.com.agentapp.Activities.EarningDetailsActivity;
import app.zingo.com.agentapp.Activities.HotelListActivity;
import app.zingo.com.agentapp.Activities.LoginActivity;
import app.zingo.com.agentapp.Activities.NotificationListActivity;
import app.zingo.com.agentapp.Activities.ReferalCodeActivity;
import app.zingo.com.agentapp.Activities.SplitActivity;
import app.zingo.com.agentapp.Adapter.NavigationListAdapter;
import app.zingo.com.agentapp.Model.Bookings1;
import app.zingo.com.agentapp.Model.NavBarItems;
import app.zingo.com.agentapp.Model.TravellerAgentProfiles;
import app.zingo.com.agentapp.Services.SharedPrefManager;
import app.zingo.com.agentapp.Services.TrackGPS;
import app.zingo.com.agentapp.Utils.LocationHelper;
import app.zingo.com.agentapp.Utils.PreferenceHandler;
import app.zingo.com.agentapp.Utils.ThreadExecuter;
import app.zingo.com.agentapp.Utils.Util;
import app.zingo.com.agentapp.WebApi.LoginApi;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.v4.view.GravityCompat.START;
import static android.text.TextUtils.isEmpty;

public class MainActivity extends AppCompatActivity  {

    ListView navBarListView;
    DrawerLayout drawer;

    EditText search_editText,budget_et;
    ScrollView scrollView_app_home;
    ImageView image_city_background;
    TextView cin_date_tv,cout_date_tv,cin_time_tv,cout_time_tv,cin_day_tv,mSearchHotels,mBid,cout_day_tv;
    ImageButton location_button;
    Button search_button;
    Spinner madult,mchild,mRoomCount;
    LinearLayout mNavbarHeader;

    DatePickerDialog datePickerDialog;
    String ocity,city,localty,duration;
    long cin,cout;
    SimpleDateFormat dateFormatter;

    CircleImageView mProfilePhoto;
    TextView mProfileName,mWishField,mReferColde;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1,MY_PERMISSIONS_REQUEST_RESULT = 1;

    //Location access
    private Location mLastLocation;
    double latitude;
    double longitude;

    LocationHelper locationHelper;
    private ProgressDialog progressDialog;

    private long diffDays,diffHours,diffMinutes;
    private long fd;
    private long td;
    private String ft,tt;
    private String fds,tds,pass,fromTime,toTime;
    TrackGPS gps;

    int referalProfileId=0;
    double referAmountOther = 0,walletAmount = 0;
    TravellerAgentProfiles profiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            setContentView(R.layout.activity_main);
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
            getWindow().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
            getWindow().addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
            getWindow().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            getWindow().addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        /*getWindow().addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            setTitle("Search");

            checkPermission();
            gps = new TrackGPS(MainActivity.this);

        /*navBarListView = (ListView) findViewById(R.id.navbar_list);

        scrollView_app_home = (ScrollView)findViewById(R.id.scrollview_app_home);
        image_city_background = (ImageView)findViewById(R.id.image_city_background);

        search_editText = (EditText)findViewById(R.id.search_editText);
        budget_et = (EditText)findViewById(R.id.budget_et);

        madult = (Spinner)findViewById(R.id.adult_count);
        mchild = (Spinner)findViewById(R.id.child_count);
        mRoomCount = (Spinner)findViewById(R.id.room_count);
        cin_date_tv = (TextView)findViewById(R.id.cin_date_tv);
        cout_date_tv = (TextView)findViewById(R.id.cout_date_tv);
        cin_time_tv = (TextView)findViewById(R.id.cin_time_tv);
        cout_time_tv = (TextView)findViewById(R.id.cout_time_tv);
        cin_day_tv = (TextView)findViewById(R.id.cin_day_tv);
        cout_day_tv = (TextView)findViewById(R.id.cout_day_tv);
        location_button = (ImageButton)findViewById(R.id.location_button);
        search_button = (Button)findViewById(R.id.search_button_api);*/
            search_editText = (EditText)findViewById(R.id.search_editText);
            //navBarListView = (ListView) findViewById(R.id.navbar_list);

            madult = (Spinner)findViewById(R.id.adult_count);
            mchild = (Spinner)findViewById(R.id.child_count);
            mRoomCount = (Spinner)findViewById(R.id.room_count);
            cin_date_tv = (TextView)findViewById(R.id.cin_date_tv);
            cout_date_tv = (TextView)findViewById(R.id.cout_date_tv);
            cin_time_tv = (TextView)findViewById(R.id.cin_time_tv);
            cout_time_tv = (TextView)findViewById(R.id.cout_time_tv);
            cin_day_tv = (TextView)findViewById(R.id.cin_day_tv);
            cout_day_tv = (TextView)findViewById(R.id.cout_day_tv);
            //mSearchHotels = (TextView)findViewById(R.id.search_hotels);
            //mBid = (TextView)findViewById(R.id.bid_to_hotels);

            //mProfileName = (TextView)findViewById(R.id.main_user_name);
            //mReferColde = (TextView)findViewById(R.id.referal_code);
            //mWishField = (TextView)findViewById(R.id.main_greetings);
            /*mProfilePhoto = (CircleImageView) findViewById(R.id.profile_photo);
            mNavbarHeader = (LinearLayout) findViewById(R.id.main_user_profile);*/

            //mReferColde.setText("ZINGO"+PreferenceHandler.getInstance(MainActivity.this).getUserId());

            location_button = (ImageButton)findViewById(R.id.location_button);
            search_button = (Button)findViewById(R.id.search_button_api);

        /*locationHelper=new LocationHelper(this);
        locationHelper.checkpermission();*/


            //mProfileName.setText(""+PreferenceHandler.getInstance(MainActivity.this).getFullName());

            /*drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();*/

            //setUpNavigationDrawer();
            getTimeFromAndroid();

            //Get current date,day,time and set


            long date = System.currentTimeMillis();


           /* mBid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, BidingActivity.class);
                    startActivity(intent);
                }
            });*/

            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

            dateFormatter = new SimpleDateFormat("dd MMM");
            String dateString = dateFormatter.format(date);
            cin_date_tv.setText(dateString);
            fds = sdf.format(date);

            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_YEAR,1);
            Date checkout = cal.getTime();

            String coutString = dateFormatter.format(checkout);
            cout_date_tv.setText(coutString);
            tds = sdf.format(checkout);



            SimpleDateFormat sdfw = new SimpleDateFormat("EEEE");
            SimpleDateFormat sdft = new SimpleDateFormat("hh:mm a");

            Date d = new Date();
            String dayOfTheWeek = sdfw.format(d);
            String time = sdft.format(d);
            String nextDay = sdfw.format(checkout);
            cin_day_tv.setText(dayOfTheWeek);
            cout_day_tv.setText(nextDay);
            cin_time_tv.setText("12:00 pm");
            cout_time_tv.setText("11:00 am");



            //check-in date
            cin_date_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setDate(cin_date_tv);
                }
            });


            //check-out date
            cout_date_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setDate(cout_date_tv);
                }
            });



            //check-in time
            cin_time_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openTimePicker(cin_time_tv);

                }
            });


            //check-out time
            cout_time_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openTimePicker(cout_time_tv);

                }
            });




            search_editText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent =
                                new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                        .build(MainActivity.this);
                        startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                    } catch (GooglePlayServicesRepairableException e) {
                        // TODO: Handle the error.
                        e.printStackTrace();
                    } catch (GooglePlayServicesNotAvailableException e) {
                        // TODO: Handle the error.
                        e.printStackTrace();
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                }
            });


            /*mNavbarHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(drawer != null && drawer.isDrawerOpen(START))
                    {
                        drawer.closeDrawer(START);
                    }
                }
            });*/

            location_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Add code access for gps
                /*mLastLocation=locationHelper.getLocation();
                System.out.println("Locaion = "+mLastLocation);

                if (mLastLocation != null) {
                    latitude = mLastLocation.getLatitude();
                    longitude = mLastLocation.getLongitude();
                    getAddress();

                } else {

                    if(search_button.isEnabled())
                        search_button.setEnabled(false);

                    showToast("Couldn't get the location. Make sure location is enabled on the device");
                }*/

                    if(gps.canGetLocation())
                    {
                        System.out.println(gps.getLatitude()+" = "+gps.getLongitude());
                        latitude = gps.getLatitude();
                        longitude = gps.getLongitude();
                        getAddress();
                    }
                    else
                    {
                        showToast("Couldn't get the location. Make sure location is enabled on the device");
                    }

                }
            });


        /*if (locationHelper.checkPlayServices()) {

            // Building the GoogleApi client
            locationHelper.buildGoogleApiClient();
        }
*/

            //Search and send request for hotel
            search_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    validateFields();
                /*final Intent intent=new Intent();
                intent.setAction("app.zingo.com.hotelmanagement");
                intent.putExtra("KeyName","code1id");
                intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                intent.setComponent(
                        new ComponentName("app.zingo.com.agentapp","app.zingo.com.agentapp.MainActivity"));
                sendBroadcast(intent);*/

                }
            });

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

    //@TargetApi()
    public boolean checkPermission() {
        if ((ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                && (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)&&
                (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                && (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)) {
            if ((ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION))
                    && (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.CALL_PHONE))
                    && (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE))
                    && (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.CAMERA))) {

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{
                                android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.CALL_PHONE,
                                android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_RESULT);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.CALL_PHONE,
                                android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_RESULT);


            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(grantResults.length > 0)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                gps = new TrackGPS(MainActivity.this);
            }
        }
    }

    public void getAddress()
    {
        /*Address locationAddress;

        locationAddress=locationHelper.getAddress(latitude,longitude);

        if(locationAddress!=null)
        {

            String address = locationAddress.getAddressLine(0);
            String address1 = locationAddress.getAddressLine(1);
            localty = locationAddress.getSubLocality();
            String city = locationAddress.getLocality();
            String state = locationAddress.getAdminArea();
            String country = locationAddress.getCountryName();
            String postalCode = locationAddress.getPostalCode();


            String currentLocation;

            if(!isEmpty(address))
            {
                currentLocation=address;

                if (!isEmpty(address1))
                    currentLocation+="\n"+address1;


                search_editText.setText(currentLocation);
                search_editText.setVisibility(View.VISIBLE);

                if(!search_button.isEnabled())
                    search_button.setEnabled(true);
            }

        }
        else
            showToast("Something went wrong");*/
        try
        {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(MainActivity.this, Locale.ENGLISH);

            System.out.println("Latlang  = "+latitude+" == "+longitude);
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            localty = addresses.get(0).getSubLocality();
           // city = addresses.get(0).getLocality();
            city = "abc";
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();



            System.out.println("address = "+address);

            String currentLocation;

            if(!isEmpty(address))
            {
                currentLocation=address;

                if (!isEmpty(address))
                    currentLocation+="\n"+address;


                search_editText.setText(currentLocation);
                search_editText.setVisibility(View.VISIBLE);

                if(!search_button.isEnabled())
                    search_button.setEnabled(true);
            }
            else
                showToast("Something went wrong");


        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void showToast(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    /*public void validateFields(){
        String fromDate = cin_date_tv.getText().toString();
        String toDate = cout_date_tv.getText().toString();
        String location = search_editText.getText().toString();
        String price = budget_et.getText().toString();
        String fromTime = cin_time_tv.getText().toString();
        String toTime = cout_time_tv.getText().toString();

        if(fromDate == null || fromDate.isEmpty())
        {
            cin_date_tv.setError("Check-in date not selected");
            cin_date_tv.requestFocus();
        }else if(toDate == null || toDate.isEmpty())
        {
            cout_date_tv.setError("Check-out date not selected");
            cout_date_tv.requestFocus();
        }else if(fromTime == null || fromTime.isEmpty())
        {
            cin_time_tv.setError("Check-in time not selected");
            cin_time_tv.requestFocus();
        }else if(toTime == null || toTime.isEmpty())
        {
            cout_time_tv.setError("Check-out time not selected");
            cout_time_tv.requestFocus();
        }else if(location == null || location.isEmpty())
        {
            search_editText.setError("Location not selected");
            search_editText.requestFocus();
        }else if(price == null || price.isEmpty())
        {
            budget_et.setError("Price date not selected");
            budget_et.requestFocus();
        }else if(Integer.parseInt(price)<200)
        {
            budget_et.setError("Price should be above 200");
            budget_et.requestFocus();
        }else{

            System.out.println("Location==="+location);

          *//*  postSearch();
            dateCal();
            Intent intent = new Intent(getApplicationContext(),HotelListActivity.class);

            //Passing Localty value for next Activty get List of Hotel
            intent.putExtra("Localty",localty);
            intent.putExtra("Duration",duration);
            System.out.println("Duration==="+duration);
            startActivity(intent);*//*

            Intent intent = new Intent(getApplicationContext(),HotelList.class);
            startActivity(intent);

        }


    }*/
    public void validateFields(){
        String fromDate = cin_date_tv.getText().toString();
        String toDate = cout_date_tv.getText().toString();
        String location = search_editText.getText().toString();
        fromTime = cin_time_tv.getText().toString();
        toTime = cout_time_tv.getText().toString();
        String child = mchild.getSelectedItem().toString();
        String room = mRoomCount.getSelectedItem().toString();
        String adult = madult.getSelectedItem().toString();

        if(fromDate == null || fromDate.isEmpty())
        {
            cin_date_tv.setError("Check-in date not selected");
            cin_date_tv.requestFocus();
        }else if(toDate == null || toDate.isEmpty())
        {
            cout_date_tv.setError("Check-out date not selected");
            cout_date_tv.requestFocus();
        }else if(fromTime == null || fromTime.isEmpty())
        {
            cin_time_tv.setError("Check-in time not selected");
            cin_time_tv.requestFocus();
        }else if(toTime == null || toTime.isEmpty())
        {
            cout_time_tv.setError("Check-out time not selected");
            cout_time_tv.requestFocus();
        }else if(location == null || location.isEmpty())
        {
            search_editText.setError("Location not selected");
            search_editText.requestFocus();
        }else{

            System.out.println("Location==="+room +" , "+child+" , "+adult);




            int adults = 0,rooms = 0;

            if(room.contains(" ")){
                String split[] = room.split(" ");
                pass = split[0];
                rooms = Integer.parseInt(split[0]);

            } if(adult.contains(" ")){
                String split[] = adult.split(" ");
                pass = pass+","+split[0];
                adults = Integer.parseInt(split[0]);
            } if(child.contains(" ")){
                String split[] = child.split(" ");
                pass = pass+","+split[0];
            }

            /*int roomstobeselected = rooms*2;
            if(adults > roomstobeselected )
            {
                *//*System.out.println(adults +" == "+rooms+" == "+roomstobeselected);
                System.out.println("selected more");*//*
                Toast.makeText(MainActivity.this,"",Toast.LENGTH_SHORT).show();

            }
            else
            {*/
                /*System.out.println(adults +" == "+rooms+" == "+roomstobeselected);
                System.out.println("selected less");*/
                String token = SharedPrefManager.getInstance(MainActivity.this).getDeviceToken();
                System.out.println("token"+token);

                int profileId= PreferenceHandler.getInstance(MainActivity.this).getUserId();


                Intent search = new Intent(MainActivity.this,HotelListActivity.class);

                //Passing Localty value for next Activty get List of Hotel

                System.out.println("CheckInTime"+fromTime+" "+"CheckOutTime"+toTime);
                search.putExtra("Locality",localty);
                search.putExtra("City",city);
                search.putExtra("OriginalCity",ocity);
                search.putExtra("Latitude",latitude);
                search.putExtra("Longitude",longitude);
                search.putExtra("CheckinDate",fds);
                search.putExtra("CheckOutDate",tds);
                search.putExtra("CheckInTime",fromTime);
                search.putExtra("CheckOutTime",toTime);
                System.out.println("Pass="+pass);
                search.putExtra("Room",pass);


                search.putExtra("GuestDetails","");
                startActivity(search);
            //}


        }


    }

    public void setDate(final TextView textView)
    {
        Calendar newCalendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                Calendar cal = Calendar.getInstance();
                cal.set(year,month,dayOfMonth);
                Date date = cal.getTime();

                textView.setText(dateFormatter.format(date));
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                String dateStr = simpleDateFormat.format(date);
                System.out.println("Date Format==="+dateStr);
                String day = null;
                try {
                    day = new SimpleDateFormat("EEEE").format(simpleDateFormat.parse(dateStr));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(textView == cin_date_tv)
                {
                    if(day != null)
                    {
                        cin_day_tv.setText(day);
                    }

                    fds = sdf.format(date);


                }
                else if(textView == cout_date_tv)
                {
                    if(day != null)
                    {
                        cout_day_tv.setText(day);
                    }
                    tds = sdf.format(date);
                }
            }
        },newCalendar.get(Calendar.YEAR),newCalendar.get(Calendar.MONTH),newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }


    public void openTimePicker(final TextView tv){

        final Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                //tv.setText( selectedHour + ":" + selectedMinute);



                System.out.println("time==="+mcurrentTime);
                if (tv.equals(cin_time_tv)){
                    ft = String.format("%02d:%02d:00",hourOfDay,minute);
                    boolean isPM =(hourOfDay >= 12);
                    cin_time_tv.setText( String.format("%02d:%02d %s ", (hourOfDay == 12 || hourOfDay == 0) ? 12 : hourOfDay % 12, minute, isPM ? "PM" : "AM"));
                }else if (tv.equals(cout_time_tv)) {
                    tt = String.format("%02d:%02d:00",hourOfDay,minute);
                    boolean isPM =(hourOfDay >= 12);
                    cout_time_tv.setText( String.format("%02d:%02d %s", (hourOfDay == 12 || hourOfDay == 0) ? 12 : hourOfDay % 12, minute, isPM ? "PM" : "AM"));

                }
            }
        }, hour, minute, false);//Yes 24 hour time
        if(tv.equals(cin_time_tv)){
            mTimePicker.setTitle("Check-In Time");
        }else if(tv.equals(cout_time_tv)){
            mTimePicker.setTitle("Check-Out Time");
        }

        mTimePicker.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                LatLng latLang = place.getLatLng();
               latitude  = latLang.latitude;
                longitude  = latLang.longitude;

                try {
                    Geocoder geocoder = new Geocoder(MainActivity.this);
                    List<Address> addresses = geocoder.getFromLocation(latitude,longitude,1);
                    //localty = String.valueOf(place.getName());
                    System.out.println("address = "+addresses.get(0));
                    System.out.println("address = "+addresses.get(0).getSubLocality()+" = "+addresses.get(0).getLocality());
                    if(place.getName().toString().equalsIgnoreCase(addresses.get(0).getLocality()))
                    {
                        search_editText.setText(place.getName()+","+addresses.get(0).getAdminArea());
                    }
                    else
                    {
                        search_editText.setText(place.getName()+","+addresses.get(0).getLocality()+","+addresses.get(0).getAdminArea());
                    }
                    if(place.getName().toString().equalsIgnoreCase(addresses.get(0).getSubLocality()))
                    {
                        localty = String.valueOf(place.getName());
                        city = "abc";
                        System.out.println("City abc=="+city);
                        ocity = addresses.get(0).getLocality();
                        System.out.println("City abc=="+ocity);
                    }
                    else if(place.getName().toString().equalsIgnoreCase(addresses.get(0).getLocality()))
                    {
                        localty = "abc";
                        city = String.valueOf(place.getName());
                        System.out.println("City =="+city);
                        ocity = addresses.get(0).getLocality();
                    }
                }
                catch (IOException ex)
                {
                    ex.printStackTrace();
                }
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }


    
    @Override
    public void onBackPressed() {
        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        /*if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {*/
            //super.onBackPressed();
            try {
                showalertbox();
            } catch (Exception e) {
                e.printStackTrace();
            }
        //}
    }


    private void showalertbox() throws Exception{



        final android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.exit_alert_layout,null);
        TextView dontCancelBtn = (TextView)view.findViewById(R.id.no_btn);
        TextView cancelBtn = (TextView)view.findViewById(R.id.exit_app_btn);
        dialogBuilder.setView(view);
        final android.app.AlertDialog dialog = dialogBuilder.create();
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

                    finish();

                    System.exit(0);

                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*private void setUpNavigationDrawer() {

        TypedArray icons = null;
        String[] title  = null;
        if(PreferenceHandler.getInstance(MainActivity.this).getUserRoleUniqueID().equalsIgnoreCase("Luci-Agent"))
        {
             icons = getResources().obtainTypedArray(R.array.agent_navbar_images);
             title  = getResources().getStringArray(R.array.agent_navbar_items_title);
        }
        else
        {
            icons = getResources().obtainTypedArray(R.array.traveller_navbar_images);
            title  = getResources().getStringArray(R.array.traveller_navbar_items_title);
        }

        ArrayList<NavBarItems> navBarItemsList = new ArrayList<>();

        for (int i=0;i<title.length;i++)
        {
            NavBarItems navbarItem = new NavBarItems(title[i],icons.getResourceId(i, -1));
            navBarItemsList.add(navbarItem);
        }

        NavigationListAdapter adapter = new NavigationListAdapter(getApplicationContext(),navBarItemsList);
        navBarListView.setAdapter(adapter);

        final String[] finalTitle = title;
        navBarListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                drawer.closeDrawer(START);
                displayView(finalTitle[position]);
            }
        });
    }*/

    /*private void displayView(int position) {
        //System.out.println("position = "+position);
        switch (position)
        {
            case 0:
                // Toast.makeText(this, "Need to Add", Toast.LENGTH_SHORT).show();
                Intent userProfileIntent = new Intent(MainActivity.this,ProfileDetails.class);
                startActivity(userProfileIntent);
                break;



            case 1:
                Toast.makeText(this, "Need to Add", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(this, "Need to Add", Toast.LENGTH_SHORT).show();
                break;
            case 3:

                break;
            case 4:
                Toast.makeText(this, "Need to Add", Toast.LENGTH_SHORT).show();
                break;
            case 5:
                Toast.makeText(this, "Need to Add", Toast.LENGTH_SHORT).show();
                break;
            case 6:
                Toast.makeText(this, "Need to Add", Toast.LENGTH_SHORT).show();
                break;
            case 7:
                Toast.makeText(this, "Need to Add", Toast.LENGTH_SHORT).show();
                break;

        }
    }*/

    private void displayView(String position) {
        //System.out.println("position = "+position);
        switch (position)
        {
            case "My profile":
                // Toast.makeText(this, "Need to Add", Toast.LENGTH_SHORT).show();
                Intent userProfileIntent = new Intent(MainActivity.this,ProfileDetails.class);
                startActivity(userProfileIntent);
                break;



            case "Earnings":
                Intent earnings = new Intent(MainActivity.this,EarningDetailsActivity.class);
                startActivity(earnings);
                break;
            case "Booking History":
                Intent bookingHistory = new Intent(MainActivity.this,BookingHistoryActivity.class);
                startActivity(bookingHistory);
                break;
            case "Invite & Earn":

              /*  Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Hello this is Zingo Hotels Agent App. Join the Zingo Hotels referral programme, and earn money for every new referral.\n Open the Zingo Hotels App and visit the invite & earn section, and find out your referral code.  It’s an alpha-numeric code like: ZINGO"+ PreferenceHandler.getInstance(MainActivity.this).getUserId()+"\n Keep Sharing & Earning.\nTo Download the app click here: https://play.google.com/store/apps/details?id=app.zingo.com.agentapp");
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent,"Zingo Agent" ));*/
                Intent rc = new Intent(MainActivity.this,ReferalCodeActivity.class);
                startActivity(rc);
                break;

            case "Notifications":

              /*  Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Hello this is Zingo Hotels Agent App. Join the Zingo Hotels referral programme, and earn money for every new referral.\n Open the Zingo Hotels App and visit the invite & earn section, and find out your referral code.  It’s an alpha-numeric code like: ZINGO"+ PreferenceHandler.getInstance(MainActivity.this).getUserId()+"\n Keep Sharing & Earning.\nTo Download the app click here: https://play.google.com/store/apps/details?id=app.zingo.com.agentapp");
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent,"Zingo Agent" ));*/
                Intent nfm = new Intent(MainActivity.this,NotificationListActivity.class);
                startActivity(nfm);
                break;
            case "Call Back":

                /*Intent hotel = new Intent(MainActivity.this,HotelListActivity.class);
                startActivity(hotel);*/

                call();
                break;
            case "Logout":
                //Toast.makeText(this, "Need to Add", Toast.LENGTH_SHORT).show();
                logout();
                break;



        }
    }

    public void logout()
    {


        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Do you want to Logout?");
        //builder.setCancelable(false);
        builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                PreferenceHandler.getInstance(MainActivity.this).clear();
                Toast.makeText(MainActivity.this,"Logout done successfully",Toast.LENGTH_SHORT).show();
                PreferenceHandler.getInstance(MainActivity.this).clear();
                Intent log = new Intent(MainActivity.this, LoginActivity.class);
                log.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                log.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(log);

                finish();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void call()
    {


        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Do you want to Call our Customer Care Number +91-7065651651?");
        //builder.setCancelable(false);
        builder.setPositiveButton("Call", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri number = Uri.parse("tel:" + "7065651651");
                Intent dial = new Intent(Intent.ACTION_CALL, number);
                if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

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

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, 500);
    }


    /*private void getProfileById() {

        *//*final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
        dialog.setTitle("Loading");
        dialog.setCancelable(false);
        dialog.show();*//*

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                LoginApi profileApi = Util.getClient().create(LoginApi.class);
                String authenticationString = Util.getToken(MainActivity.this);
                Call<TravellerAgentProfiles> getProfile = profileApi.getProfileByID(authenticationString,
                        PreferenceHandler.getInstance(MainActivity.this).getUserId());
                //System.out.println("hotelid = "+hotelid);
                System.out.println();

                getProfile.enqueue(new Callback<TravellerAgentProfiles>() {
                    @Override
                    public void onResponse(Call<TravellerAgentProfiles> call, Response<TravellerAgentProfiles> response) {
                        *//*if(dialog != null)
                        {
                            dialog.dismiss();
                        }*//*
                        if(response.code() == 200)
                        {
                            TravellerAgentProfiles dto = response.body();

                            if(dto != null)
                            {

                                if(dto.getProfilePhoto() != null && !dto.getProfilePhoto().isEmpty())
                                {
                                    if(dto.getProfilePhoto() == null|| dto.getProfilePhoto().equalsIgnoreCase("")
                                            ||dto.getProfilePhoto().equalsIgnoreCase("test")){
                                        mProfilePhoto.setImageResource(R.drawable.icons_profile);
                                    }else{
                                        System.out.println("Bit = "+Util.convertToBitMap(dto.getProfilePhoto()));
                                        mProfilePhoto.setImageBitmap(Util.convertToBitMap(dto.getProfilePhoto()));
                                    }

                                    // mUserProfileImage.setEnabled(false);
                                }else {
                                    mProfilePhoto.setImageResource(R.drawable.icons_profile);
                                }

                                //mProfileName.setText(""+dto.getFirstName());

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<TravellerAgentProfiles> call, Throwable t) {
                        *//*if(dialog != null)
                        {
                            dialog.dismiss();
                        }*//*
                    }
                });
            }
        });
    }*/

    private void getTimeFromAndroid() {
        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        int hours = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);

        if(hours>=1 && hours<=12){
            //Toast.makeText(this, "Good Morning "+PreferenceHandler.getInstance(HotelListActivity.this).getUserFullName(), Toast.LENGTH_SHORT).show();
            //mWishField.setText("Good Morning");
        }else if(hours>=12 && hours<=16){
            //Toast.makeText(this, "Good Afternoon "+PreferenceHandler.getInstance(HotelListActivity.this).getUserFullName(), Toast.LENGTH_SHORT).show();
            //mWishField.setText("Good Afternoon");
        }else if(hours>=16 && hours<=20){
            //mWishField.setText("Good Evening");
            //Toast.makeText(this, "Good Evening "+PreferenceHandler.getInstance(HotelListActivity.this).getUserFullName(), Toast.LENGTH_SHORT).show();
        }else if(hours>=20 && hours<=24){
            //mWishField.setText("Good Night");
            //Toast.makeText(this, "Good Night "+PreferenceHandler.getInstance(HotelListActivity.this).getUserFullName(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        //getProfileById();
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
                String authenticationString = Util.getToken(MainActivity.this);
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

        String auth_string = Util.getToken(MainActivity.this);

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
