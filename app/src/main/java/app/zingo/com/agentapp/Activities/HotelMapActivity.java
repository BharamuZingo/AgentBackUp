package app.zingo.com.agentapp.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import app.zingo.com.agentapp.CustomViews.CustomFontTextView;
import app.zingo.com.agentapp.Model.Maps;
import app.zingo.com.agentapp.R;
import app.zingo.com.agentapp.Services.TrackGPS;

public class HotelMapActivity extends AppCompatActivity {

    CustomFontTextView mHotelName,mAddress;

    TrackGPS gps;
    GoogleApiClient mGoogleApiClient;
    private static final int GOOGLE_API_CLIENT_ID = 0;
    MapView mMapView;
    Bundle extra;

    GoogleMap mGoogleMap;
    double latitude,longitude;
    double petLatitude,petLongitude;
    Marker marker ;
   // TextView address;
    LatLng userLatLang,hotelLatLang;
    ArrayList<Maps> maps;
    String hotelName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_map);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        setTitle("Location");

        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
        {
            hotelName =  bundle.getString("HotelName");
            maps = (ArrayList<Maps>) bundle.getSerializable("Map");

        }
        mMapView = (MapView) findViewById(R.id.map);
        mAddress = (CustomFontTextView) findViewById(R.id.hotel_address);
        mHotelName = (CustomFontTextView) findViewById(R.id.adress_tag);

        if (hotelName!=null&&!hotelName.isEmpty()){
            mHotelName.setText(hotelName);
        }

        if (maps.size()!=0){
            longitude = Double.parseDouble(maps.get(0).getLogitude());
            latitude = Double.parseDouble(maps.get(0).getLatitude());
            mAddress.setText(maps.get(0).getLocation());
        }

        gps = new TrackGPS(HotelMapActivity.this);
        mMapView.onCreate(savedInstanceState);


        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(GoogleMap mMap) {

                mGoogleMap = mMap;
               //getLocationFromAddress(address.getText().toString());
                // For showing a move to my location button

                // mGoogleMap.setMyLocationEnabled(true);
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


                // For dropping a marker at a point on the Map
                /*LatLng sydney = new LatLng(-34, 151);*/

              /*  if(gps.canGetLocation())
                {

                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();
                    Log.d("Latitude = "+latitude,"Longitude = "+longitude);
                    LatLng location = new LatLng(latitude,longitude);
                    mGoogleMap.addMarker(new MarkerOptions().position(location).title("Demo Hotel"));//.snippet("Marker Description"));

                    // For zooming automatically to the location of the marker

                    CameraPosition cameraPosition = new CameraPosition.Builder().target(location).zoom(14).build();
                    mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
                else
                {
                    gps.showSettingsAlert();
                }*/


                LatLng location = new LatLng(latitude,longitude);
                mGoogleMap.addMarker(new MarkerOptions().position(location).title(hotelName));//.snippet("Marker Description"));

                // For zooming automatically to the location of the marker

                CameraPosition cameraPosition = new CameraPosition.Builder().target(location).zoom(14).build();
                mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id)
        {
            case android.R.id.home:
                HotelMapActivity.this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
