package app.zingo.com.agentapp.Activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import app.zingo.com.agentapp.MainActivity;
import app.zingo.com.agentapp.Model.HotelNotification;
import app.zingo.com.agentapp.R;
import app.zingo.com.agentapp.Services.SharedPrefManager;
import app.zingo.com.agentapp.Services.TrackGPS;
import app.zingo.com.agentapp.Utils.Constants;
import app.zingo.com.agentapp.Utils.LocationHelper;
import app.zingo.com.agentapp.Utils.PreferenceHandler;
import app.zingo.com.agentapp.Utils.ThreadExecuter;
import app.zingo.com.agentapp.Utils.Util;
import app.zingo.com.agentapp.WebApi.NotificationApi;
import retrofit2.Call;
import retrofit2.Callback;

import static android.text.TextUtils.isEmpty;

public class BidingActivity extends AppCompatActivity {

    EditText search_editText,budget_et;

    ScrollView scrollView_app_home;
    ImageView image_city_background;
    TextView cin_date_tv,cout_date_tv,cin_time_tv,cout_time_tv,cin_day_tv,cout_day_tv;
    ImageButton location_button;
    Button search_button;
    Spinner madult,mchild,mRoomCount;

    DatePickerDialog datePickerDialog;
    String ocity,city,localty,duration;
    long cin,cout;
    SimpleDateFormat dateFormatter;

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

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
    public static final int MY_PERMISSIONS_REQUEST_RESULT = 1;

    private TrackGPS gps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_bidding_fragemnet);

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
        search_button = (Button)findViewById(R.id.search_button_api);

        /*locationHelper=new LocationHelper(BidingActivity.this);
        locationHelper.checkpermission();
        checkPermission();*/
        gps = new TrackGPS(BidingActivity.this);

        setTitle("Bid Your Stay");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // gps = new TrackGPS(BidingActivity.this);

        long date = System.currentTimeMillis();


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


        //Google Place Auto Complete

        search_editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                    .build(BidingActivity.this);
                    BidingActivity.this.startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);

                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });



        location_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Add code access for gps
                /*mLastLocation=locationHelper.getLocation();

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
        }*/


        //Search and send request for hotel
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateFields();


            }
        });
    }

    public void getAddress()
    {
        /*Address locationAddress;

        locationAddress=locationHelper.getAddress(latitude,longitude);

        System.out.println("Lattt=="+latitude+" long=="+longitude);

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
            geocoder = new Geocoder(BidingActivity.this, Locale.getDefault());

            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            localty = addresses.get(0).getSubLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();


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
        Toast.makeText(BidingActivity.this,message,Toast.LENGTH_SHORT).show();
    }

    public void validateFields(){
        String fromDate = cin_date_tv.getText().toString();
        String toDate = cout_date_tv.getText().toString();
        String location = search_editText.getText().toString();
        fromTime = cin_time_tv.getText().toString();
        String price = budget_et.getText().toString();
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
        }else if(price == null || price.isEmpty())
        {
            budget_et.setError("Price date not selected");
            budget_et.requestFocus();
        }else if(Integer.parseInt(price)<200)
        {
            budget_et.setError("Price should be above 200");
            budget_et.requestFocus();
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


           /* int roomstobeselected = rooms*2;
            if(adults > roomstobeselected )
            {
                *//*System.out.println(adults +" == "+rooms+" == "+roomstobeselected);
                System.out.println("selected more");*//*
                Toast.makeText(BidingActivity.this,"",Toast.LENGTH_SHORT).show();

            }
            else
            {*/

                String token = SharedPrefManager.getInstance(BidingActivity.this).getDeviceToken();
                System.out.println("token"+token);

                int profileId= PreferenceHandler.getInstance(BidingActivity.this).getUserId();

                HotelNotification fm = new HotelNotification();
                fm.setSenderId(Constants.senderId);
                fm.setServerId(Constants.serverId);
                //fm.setTravellerId(travellerId);
                fm.setTitle("Agent Booking Request");
                price = budget_et.getText().toString();
                String cit = cin_time_tv.getText().toString()+","+cout_time_tv.getText().toString();
                fm.setMessage(profileId+","+fds+","+tds+","+price+","+pass+","+cit);
                //registerTokenInDB(fm);
                //sendNotification(fm);

                Intent search = new Intent(BidingActivity.this,HotelListActivity.class);

                //Passing Localty value for next Activty get List of Hotel
                search.putExtra("Locality",localty);
                search.putExtra("City",city);
                search.putExtra("OriginalCity",ocity);
                search.putExtra("Latitude",latitude);
                search.putExtra("Longitude",longitude);
                search.putExtra("CheckinDate",fds);
                search.putExtra("CheckOutDate",tds);
                search.putExtra("CheckInTime",fromTime);
                search.putExtra("CheckOutTime",toTime);
                search.putExtra("Price",price);
                System.out.println("Pass="+pass);
                search.putExtra("Room",pass);
                search.putExtra("GuestDetails","");

          /*  search.putExtra("Locality",localty);

            search.putExtra("CheckinDate",fds);
            search.putExtra("CheckOutDate",tds);
            search.putExtra("CIT",cit);
            System.out.println("Pass="+pass);
            search.putExtra("Room",pass);


            search.putExtra("GuestDetails","");*/
                startActivity(search);
                //BidingActivity.this.finish();
            //}





            /*String token = SharedPrefManager.getInstance(BidingActivity.this).getDeviceToken();
            System.out.println("token"+token);

            int profileId= PreferenceHandler.getInstance(BidingActivity.this).getUserId();

            HotelNotification fm = new HotelNotification();
            fm.setSenderId(Constants.senderId);
            fm.setServerId(Constants.serverId);
            //fm.setTravellerId(travellerId);
            fm.setTitle("Agent Booking Request");
            price = budget_et.getText().toString();
            String cit = cin_time_tv.getText().toString()+","+cout_time_tv.getText().toString();
            fm.setMessage(profileId+","+fds+","+tds+","+price+","+pass+","+cit);
            //registerTokenInDB(fm);
            //sendNotification(fm);

            Intent search = new Intent(BidingActivity.this,HotelListActivity.class);

            //Passing Localty value for next Activty get List of Hotel
            search.putExtra("Locality",localty);
            search.putExtra("City",city);
            search.putExtra("OriginalCity",ocity);
            search.putExtra("Latitude",latitude);
            search.putExtra("Longitude",longitude);
            search.putExtra("CheckinDate",fds);
            search.putExtra("CheckOutDate",tds);
            search.putExtra("CheckInTime",fromTime);
            search.putExtra("CheckOutTime",toTime);
            search.putExtra("Price",price);
            System.out.println("Pass="+pass);
            search.putExtra("Room",pass);
            search.putExtra("GuestDetails","");

          *//*  search.putExtra("Locality",localty);

            search.putExtra("CheckinDate",fds);
            search.putExtra("CheckOutDate",tds);
            search.putExtra("CIT",cit);
            System.out.println("Pass="+pass);
            search.putExtra("Room",pass);


            search.putExtra("GuestDetails","");*//*
            startActivity(search);
            //BidingActivity.this.finish();*/
        }


    }

    public void sendNotification(final HotelNotification fireBaseModel) {


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                String auth_string = Util.getToken(BidingActivity.this);
                NotificationApi apiService =
                        Util.getClient().create(NotificationApi.class);


                System.out.println("Nodel" + fireBaseModel.toString());
                Call<ArrayList<String>> call = apiService.sendnotificationToAll(auth_string, fireBaseModel)/*getString()*/;

                call.enqueue(new Callback<ArrayList<String>>() {
                    @Override
                    public void onResponse(Call<ArrayList<String>> call, retrofit2.Response<ArrayList<String>> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                        int statusCode = response.code();

                        if (statusCode == 200) {

                            ArrayList<String> list = response.body();

                            Toast.makeText(BidingActivity.this, "Notification Send Successfully", Toast.LENGTH_SHORT).show();
                            //sendEmailattache();
                            Intent search = new Intent(BidingActivity.this,HotelListActivity.class);

                            //Passing Localty value for next Activty get List of Hotel
                            search.putExtra("Locality",localty);
                            search.putExtra("City",city);
                            search.putExtra("OriginalCity",ocity);
                            search.putExtra("CheckinDate",fds);
                            search.putExtra("CheckOutDate",tds);
                            search.putExtra("CheckInTime",fromTime);
                            search.putExtra("CheckOutTime",toTime);
                            System.out.println("Pass="+pass);
                            search.putExtra("Room",pass);
                            search.putExtra("GuestDetails","");

                            search.putExtra("Locality",localty);
                            search.putExtra("CheckinDate",fds);
                            search.putExtra("CheckOutDate",tds);
                            System.out.println("Pass="+pass);
                            search.putExtra("Room",pass);


                            search.putExtra("GuestDetails","");
                            startActivity(search);


                        } else {

                            Toast.makeText(BidingActivity.this, " failed due to status code:" + statusCode, Toast.LENGTH_SHORT).show();
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


    public void setDate(final TextView textView)
    {
        Calendar newCalendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(BidingActivity.this, new DatePickerDialog.OnDateSetListener() {
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
        mTimePicker = new TimePickerDialog(BidingActivity.this, new TimePickerDialog.OnTimeSetListener() {
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

    public boolean checkPermission() {
        if ((ContextCompat.checkSelfPermission(BidingActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                && (ContextCompat.checkSelfPermission(BidingActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)&&
                (ContextCompat.checkSelfPermission(BidingActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                && (ContextCompat.checkSelfPermission(BidingActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)) {
            if ((ActivityCompat.shouldShowRequestPermissionRationale(BidingActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION))
                    && (ActivityCompat.shouldShowRequestPermissionRationale(BidingActivity.this, android.Manifest.permission.CALL_PHONE))
                    && (ActivityCompat.shouldShowRequestPermissionRationale(BidingActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE))
                    && (ActivityCompat.shouldShowRequestPermissionRationale(BidingActivity.this, android.Manifest.permission.CAMERA))) {

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(BidingActivity.this,
                        new String[]{
                                android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.CALL_PHONE,
                                android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_RESULT);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(BidingActivity.this,
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
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == BidingActivity.this.RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(BidingActivity.this, data);
                LatLng latLang = place.getLatLng();
                latitude  = latLang.latitude;
                longitude  = latLang.longitude;

                System.out.println("Lat Au="+latitude+" Lon Au="+longitude);

                try {
                    /*Geocoder geocoder = new Geocoder(BidingActivity.this);
                    List<Address> addresses = geocoder.getFromLocation(latitude,longitude,1);
                    localty = String.valueOf(place.getName());
                    search_editText.setText(place.getName()+","+addresses.get(0).getAdminArea()+","+addresses.get(0).getCountryName());*/
                    Geocoder geocoder = new Geocoder(BidingActivity.this);
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
                        ocity = addresses.get(0).getLocality();
                    }
                    else if(place.getName().toString().equalsIgnoreCase(addresses.get(0).getLocality()))
                    {
                        localty = "abc";
                        city = String.valueOf(place.getName());
                        ocity = addresses.get(0).getLocality();
                    }
                }
                catch (IOException ex)
                {
                    ex.printStackTrace();
                }
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(BidingActivity.this, data);

            } else if (resultCode == BidingActivity.this.RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id)
        {
            case  android.R.id.home:
                goBack();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    public void goBack()
    {
        Intent intent = new Intent(BidingActivity.this,MainActivity.class);
        startActivity(intent);
        BidingActivity.this.finish();
    }
}
