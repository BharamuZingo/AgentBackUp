package app.zingo.com.agentapp.Activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import app.zingo.com.agentapp.Adapter.PlaceArrayAdapter;
import app.zingo.com.agentapp.DemoActivity;
import app.zingo.com.agentapp.MainActivity;
import app.zingo.com.agentapp.R;
import app.zingo.com.agentapp.Services.SharedPrefManager;
import app.zingo.com.agentapp.Utils.PreferenceHandler;

import static android.text.TextUtils.isEmpty;

public class HourlyChargesActivty extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks  {

    //Ordinary Views
    Toolbar toolbar;
    private TextView mSearchButton;
    AutoCompleteTextView showSearch;
    Spinner madult,mchild,mRoomCount;
    TextInputEditText  mCID,mCOD,mCIT,mCOT;

    //Searchview and google place api from local adapter
 /*   private SearchView mSearchView;
    private SearchView.SearchAutoComplete   mSearchAutoComplete;*/



    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private  int GOOGLE_API_CLIENT_ID = 0;
    private static final String LOG_TAG = "Hourly_Search";
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(12.9356033224, 77.6124714687),new LatLng(12.978483303, 77.5631597266));
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1,MY_PERMISSIONS_REQUEST_RESULT = 1;


    //Intent values  to pass
    double latitude,longitude;
    String ocity,city,localty,duration,fds,tds,pass,fromTime,toTime;
    String placeName;



    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hourly_charges_activty);

        toolbar = findViewById(R.id.hr_activity_toolbar);
        setSupportActionBar(toolbar);
        setTitle("Hourly Charges");

        showSearch= (AutoCompleteTextView) findViewById(R.id.search_editText_hourly);
        mSearchButton= (TextView) findViewById(R.id.search_button_hourly);
        madult = (Spinner)findViewById(R.id.adult_count_hourly);
        mchild = (Spinner)findViewById(R.id.child_count_hourly);
        mRoomCount = (Spinner)findViewById(R.id.room_count_hourly);
        mCID = (TextInputEditText)findViewById(R.id.check_in_date_hourly);
        mCOD = (TextInputEditText)findViewById(R.id.check_out_date_hourly);
        mCIT = (TextInputEditText)findViewById(R.id.check_in_time_hourly);
        mCOT = (TextInputEditText)findViewById(R.id.check_out_time_hourly);

      //  mSearchView = (SearchView) findViewById(R.id.search_view_hourly);
        //mSearchAutoComplete = (SearchView.SearchAutoComplete) mSearchView.findViewById(R.id.search_src_text);

       // mSearchAutoComplete.setDropDownBackgroundResource(R.drawable.background_white);
      /*  mSearchAutoComplete.setDropDownAnchor(R.id.search_view_hourly);
        mSearchAutoComplete.setThreshold(0);

        mPlaceArrayAdapter = new PlaceArrayAdapter(HourlyChargesActivty.this, android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null);
        mSearchAutoComplete.setAdapter(mPlaceArrayAdapter);*/

        showSearch.setThreshold(1);
        showSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSearch.requestFocus();
            }
        });
        showSearch.setOnItemClickListener(mAutocompleteClickListener);
        mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null);
        showSearch.setAdapter(mPlaceArrayAdapter);


        /*showSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSearchView.setIconified(false);
            }
        });*/

       /* mSearchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int i) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {

                try{
                    final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
                    final String placeId = String.valueOf(item.placeId);
                    Log.i(LOG_TAG, "Selected: " + item.description);
                    PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                            .getPlaceById(mGoogleApiClient, placeId);
                    placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
                    Log.i(LOG_TAG, "Fetching details for ID: " + item.placeId);

                    mSearchView.onActionViewCollapsed();


                    return true;
                }catch (Exception e){
                    e.printStackTrace();
                    return  false;
                }

            }
        });*/


        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM");
        SimpleDateFormat sdfs = new SimpleDateFormat("MM/dd/yyyy");


        Calendar cal = Calendar.getInstance();
        mCID.setText(""+sdf.format(cal.getTime()));
        fds = sdfs.format(cal.getTime());

        cal.add(Calendar.DAY_OF_YEAR,1);
        Date checkout = cal.getTime();
        mCOD.setText(""+sdf.format(checkout));
        tds = sdfs.format(checkout);

        mCIT.setText("12:00 pm");
        mCOT.setText("11:00 am");

        mCID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate(mCID);
            }
        });

        mCOD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate(mCOD);
            }
        });

        mCIT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTimePicker(mCIT);
            }
        });

        mCOT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTimePicker(mCOT);
            }
        });

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateFields();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == -1) {
                Place place = PlaceAutocomplete.getPlace(HourlyChargesActivty.this, data);
                LatLng latLang = place.getLatLng();
                latitude  = latLang.latitude;
                longitude  = latLang.longitude;

                try {
                    Geocoder geocoder = new Geocoder(HourlyChargesActivty.this);
                    List<Address> addresses = geocoder.getFromLocation(latitude,longitude,1);

                    System.out.println("address = "+addresses.get(0));
                    System.out.println("address = "+addresses.get(0).getSubLocality()+" = "+addresses.get(0).getLocality());
                    if(place.getName().toString().equalsIgnoreCase(addresses.get(0).getLocality()))
                    {

                            showSearch.setText(place.getName()+","+addresses.get(0).getAdminArea());


                    }
                    else
                    {

                            showSearch.setText(place.getName()+","+addresses.get(0).getLocality()+","+addresses.get(0).getAdminArea());


                    }
                    if(place.getName().toString().equalsIgnoreCase(addresses.get(0).getSubLocality()))
                    {
                        localty = String.valueOf(place.getName());
                        System.out.println("Locality=="+localty);
                        city = "abc";
                        System.out.println("City abc=="+city);
                        ocity = addresses.get(0).getLocality();
                        System.out.println("City abc=="+ocity);
                    }
                    else if(place.getName().toString().equalsIgnoreCase(addresses.get(0).getLocality()))
                    {
                        localty = "abc";
                        System.out.println("Locality=="+localty);
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
                Status status = PlaceAutocomplete.getStatus(HourlyChargesActivty.this, data);

            } else if (resultCode == 0) {
                // The user canceled the operation.
            }
        }
    }

    public void setDate(final TextView textView)
    {
        Calendar newCalendar = Calendar.getInstance();

        final SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, dd MMM");
        final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

        DatePickerDialog datePickerDialog = new DatePickerDialog(HourlyChargesActivty.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                Calendar cal = Calendar.getInstance();
                cal.set(year,month,dayOfMonth);
                Date date = cal.getTime();
                cal.add(Calendar.DAY_OF_YEAR,1);
                Date date2 = cal.getTime();

                textView.setText(dateFormatter.format(date));

                if(textView == mCID)
                {
                    fds = sdf.format(date);
                    tds = sdf.format(date2);
                    mCOD.setText(dateFormatter.format(date2));


                }
                else if(textView == mCID)
                {

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
        mTimePicker = new TimePickerDialog(HourlyChargesActivty.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                //tv.setText( selectedHour + ":" + selectedMinute);



                System.out.println("time==="+mcurrentTime);
                if (tv.equals(mCIT)){
                    fromTime = String.format("%02d:%02d:00",hourOfDay,minute);
                    boolean isPM =(hourOfDay >= 12);
                    mCIT.setText( String.format("%02d:%02d %s ", (hourOfDay == 12 || hourOfDay == 0) ? 12 : hourOfDay % 12, minute, isPM ? "PM" : "AM"));
                }else if (tv.equals(mCOT)) {
                    toTime = String.format("%02d:%02d:00",hourOfDay,minute);
                    boolean isPM =(hourOfDay >= 12);
                    mCOT.setText( String.format("%02d:%02d %s", (hourOfDay == 12 || hourOfDay == 0) ? 12 : hourOfDay % 12, minute, isPM ? "PM" : "AM"));

                }
            }
        }, hour, minute, false);//Yes 24 hour time
        if(tv.equals(mCIT)){
            mTimePicker.setTitle("Check-In Time");
        }else if(tv.equals(mCOT)){
            mTimePicker.setTitle("Check-Out Time");
        }

        mTimePicker.show();
    }


    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i(LOG_TAG, "Selected: " + item.description);
            placeName = ""+item.description;
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            Log.i(LOG_TAG, "Fetching details for ID: " + item.placeId);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(LOG_TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
            System.out.println("Places=="+place.getName());
            CharSequence attributions = places.getAttributions();
            LatLng latLang = place.getLatLng();
            latitude  = latLang.latitude;
            longitude  = latLang.longitude;

            try {
                Geocoder geocoder = new Geocoder(HourlyChargesActivty.this);
                List<Address> addresses = geocoder.getFromLocation(latitude,longitude,1);
                //localty = String.valueOf(place.getName());
                System.out.println("Rating = "+place.getRating());
                System.out.println("Place Name = "+place.getName());
                System.out.println("address = "+addresses.get(0).getSubLocality()+" = "+addresses.get(0).getLocality()+" = "+addresses.get(0).getAdminArea()+" = "+addresses.get(0).getSubAdminArea());
                System.out.println("Address Details:"+addresses.toString());
                System.out.println("Address DetailsA:"+addresses.toArray());
                System.out.println("Address DetailsAD:"+addresses.toArray());
               /* if(place.getName().toString().equalsIgnoreCase(addresses.get(0).getLocality()))
                {

                        showSearch.setText(place.getName()+","+addresses.get(0).getAdminArea());
                        showSearch.clearFocus();

                }
                else
                {

                        showSearch.setText(place.getName()+","+addresses.get(0).getLocality()+","+addresses.get(0).getAdminArea());
                        showSearch.clearFocus();


                }*/
                showSearch.setText(""+placeName);
                showSearch.clearFocus();
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
                }else{
                    localty = addresses.get(0).getSubLocality();
                    city = "abc";
                    System.out.println("City =="+city);
                    ocity = addresses.get(0).getLocality();
                }


                   /* Intent search = new Intent(HourlyChargesActivty.this,HotelListActivity.class);
                    System.out.println("CheckInTime"+"12:00 pm"+" "+"CheckOutTime"+"12:00 pm");
                    search.putExtra("Locality",localty);
                    search.putExtra("City",city);
                    search.putExtra("OriginalCity",ocity);
                    search.putExtra("Latitude",latitude);
                    search.putExtra("Longitude",longitude);
                    search.putExtra("GuestDetails","");
                    startActivity(search);
                    System.out.println("Locality=="+localty);*/



            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }

        }
    };


    public void getAddress()
    {

        try
        {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(HourlyChargesActivty.this, Locale.ENGLISH);

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



            System.out.println("address = "+address+" City & locality"+city+" "+localty);

            String currentLocation;

            if(!isEmpty(address))
            {
                currentLocation=address;

                if (!isEmpty(address))
                    currentLocation+="\n"+address;


                    showSearch.setText(currentLocation);
                    showSearch.setVisibility(View.VISIBLE);




                validateFields();


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
        Toast.makeText(HourlyChargesActivty.this,message,Toast.LENGTH_SHORT).show();
    }

    public void validateFields(){

        String fromDate = mCID.getText().toString();
        String toDate = mCOD.getText().toString();
        fromTime = mCIT.getText().toString();
        toTime = mCOT.getText().toString();
        String  location = showSearch.getText().toString();
        String child = mchild.getSelectedItem().toString();
        String room = mRoomCount.getSelectedItem().toString();
        String adult = madult.getSelectedItem().toString();

        if(location == null || location.isEmpty())
        {
            showToast("Please enter location");
        }else if(fromDate == null || fromDate.isEmpty())
        {
            showToast("Please enter Check-In Date");
        }else if(toDate == null || toDate.isEmpty())
        {
            showToast("Please enter Check-Out Date");
        }else if(fromTime == null || fromTime.isEmpty())
        {
            showToast("Please enter Check-In Time");
        }else if(toTime == null || toTime.isEmpty())
        {
            showToast("Please enter Check-Out Time");
        }
        else{




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

            String token = SharedPrefManager.getInstance(HourlyChargesActivty.this).getDeviceToken();
            System.out.println("token"+token);

            int profileId= PreferenceHandler.getInstance(HourlyChargesActivty.this).getUserId();



            Intent search = new Intent(HourlyChargesActivty.this,HourlyChargesHotelList.class);
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
            search.putExtra("Hours",hourCalculation());
            System.out.println("Pass="+pass);
            search.putExtra("Room",pass);
            search.putExtra("GuestDetails","");
            startActivity(search);




        }


    }

    //hour calculation
    public String hourCalculation(){

        SimpleDateFormat sdfs = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        Date fd = null,td = null;
        long diffDays=0,diffMinutes=0,diffHours=0 ;
        long durations = 0 ;
        try {
            fd = sdfs.parse(fds+" "+fromTime);
            td = sdfs.parse(tds+" "+toTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        try {
            long diff =td.getTime() - fd.getTime();
            diffMinutes = diff / (60 * 1000) % 60;
            diffHours = diff / (60 * 60 * 1000) % 24;
            diffDays = diff / (24 * 60 * 60 * 1000);
            System.out.println("Diff===" + diffDays);
        }catch(Exception e){
            e.printStackTrace();
        }
        if(diffDays!=0){
            durations =  diffDays*24;
        }else{
            durations = durations;
        }
        if(diffHours!=0){
            durations =  durations+diffHours;
        }else{
            durations = durations;
        }
       /* if(diffMinutes!=0){
            duration =  diffMinutes;
        }*/

        return durations+"";
    }

    //Google API CLient
    @Override
    public void onResume() {
        super.onResume();
        System.out.println("OnResume");
        try{
            mGoogleApiClient = new GoogleApiClient.Builder(HourlyChargesActivty.this)
                    .addApi(Places.GEO_DATA_API)
                    .enableAutoManage(HourlyChargesActivty.this, GOOGLE_API_CLIENT_ID, this)
                    .addConnectionCallbacks(this)
                    .build();

            System.out.println("OncreateView api");

        }catch (Exception e ){
            e.printStackTrace();
        }

    }

    @Override
    public void onConnected(Bundle bundle) {

        try{

            mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
            Log.i(LOG_TAG, "Google Places API connected.");
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        try{

            Log.e(LOG_TAG, "Google Places API connection failed with error code: "
                    + connectionResult.getErrorCode());

            Toast.makeText(HourlyChargesActivty.this,
                    "Google Places API connection failed with error code:" +
                            connectionResult.getErrorCode(),
                    Toast.LENGTH_LONG).show();

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

        try{

            mPlaceArrayAdapter.setGoogleApiClient(null);
            Log.e(LOG_TAG, "Google Places API connection suspended.");
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onPause() {
        super.onPause();

        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage( HourlyChargesActivty.this);
            mGoogleApiClient.disconnect();
            System.out.println("OnPause");
        }
        System.out.println("OnPause");

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case android.R.id.home:
                goback();
                return true;
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
        Intent intent = new Intent(HourlyChargesActivty.this, DemoActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        this.finish();
    }
}
