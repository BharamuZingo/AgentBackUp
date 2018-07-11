package app.zingo.com.agentapp.Activities;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import app.zingo.com.agentapp.Adapter.PlaceArrayAdapter;
import app.zingo.com.agentapp.DemoActivity;
import app.zingo.com.agentapp.R;
import app.zingo.com.agentapp.Services.SharedPrefManager;
import app.zingo.com.agentapp.Utils.PreferenceHandler;

import static android.text.TextUtils.isEmpty;

public class HourlyChargesActivty extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks  {

    //Ordinary Views
    Toolbar toolbar;
    private TextView showSearch;

    //Searchview and google place api from local adapter
    private SearchView mSearchView;
    private SearchView.SearchAutoComplete   mSearchAutoComplete;


    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private  int GOOGLE_API_CLIENT_ID = 0;
    private static final String LOG_TAG = "Hourly_Search";
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(12.971599, 77.594563), new LatLng(12.971599, 77.994563));


    //Intent values  to pass
    double latitude,longitude;
    String ocity,city,localty,duration,fds,tds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hourly_charges_activty);

        toolbar = findViewById(R.id.hr_activity_toolbar);
        setSupportActionBar(toolbar);
        setTitle("Hourly Charges");

        showSearch= (TextView) findViewById(R.id.search_editText_hourly);

        mSearchView = (SearchView) findViewById(R.id.search_view_hourly);
        mSearchAutoComplete = (SearchView.SearchAutoComplete) mSearchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);


        mPlaceArrayAdapter = new PlaceArrayAdapter(HourlyChargesActivty.this, android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null);
        mSearchAutoComplete.setAdapter(mPlaceArrayAdapter);


        showSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSearchView.setIconified(false);
            }
        });

        mSearchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
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
        });

    }


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
            System.out.println("Places=="+place);
            CharSequence attributions = places.getAttributions();
            LatLng latLang = place.getLatLng();
            latitude  = latLang.latitude;
            longitude  = latLang.longitude;

            try {
                Geocoder geocoder = new Geocoder(HourlyChargesActivty.this);
                List<Address> addresses = geocoder.getFromLocation(latitude,longitude,1);
                //localty = String.valueOf(place.getName());
                System.out.println("Rating = "+place.getRating());
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

        String  location = showSearch.getText().toString();

        if(location == null || location.isEmpty())
        {
            showToast("Please enter location");
        }else{



            String token = SharedPrefManager.getInstance(HourlyChargesActivty.this).getDeviceToken();
            System.out.println("token"+token);

            int profileId= PreferenceHandler.getInstance(HourlyChargesActivty.this).getUserId();



                Intent search = new Intent(HourlyChargesActivty.this,HotelListActivity.class);
                System.out.println("CheckInTime"+"12:00 pm"+" "+"CheckOutTime"+"12:00 pm");
                search.putExtra("Locality",localty);
                search.putExtra("City",city);
                search.putExtra("OriginalCity",ocity);
                search.putExtra("Latitude",latitude);
                search.putExtra("Longitude",longitude);
                search.putExtra("GuestDetails","");
                startActivity(search);





        }


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
