package app.zingo.com.agentapp.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import app.zingo.com.agentapp.Adapter.MainSearchAdapter;
import app.zingo.com.agentapp.Adapter.NavigationListAdapter;
import app.zingo.com.agentapp.MainActivity;
import app.zingo.com.agentapp.Model.NavBarItems;
import app.zingo.com.agentapp.Model.TravellerAgentProfiles;
import app.zingo.com.agentapp.ProfileDetails;
import app.zingo.com.agentapp.R;
import app.zingo.com.agentapp.Utils.PreferenceHandler;
import app.zingo.com.agentapp.Utils.ThreadExecuter;
import app.zingo.com.agentapp.Utils.Util;
import app.zingo.com.agentapp.WebApi.LoginApi;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.v4.view.GravityCompat.START;

public class SplitActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,ActivityCompat.OnRequestPermissionsResultCallback  {

    TabLayout tabLayout;
    ViewPager viewPager;
    MainSearchAdapter adapter;

    ListView navBarListView;
    DrawerLayout drawer;

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    CircleImageView mProfilePhoto;
    TextView mProfileName,mWishField,mReferColde;

    public static final int MY_PERMISSIONS_REQUEST_RESULT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_split);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Search");

        navBarListView = (ListView) findViewById(R.id.navbar_list);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        tabLayout = (TabLayout) findViewById(R.id.booking_list_tabLayout);
        tabLayout.setTabGravity(TabLayout.MODE_FIXED);
        viewPager = (ViewPager) findViewById(R.id.booking_list_pager);
        mProfileName = (TextView)findViewById(R.id.main_user_name);
        mReferColde = (TextView)findViewById(R.id.referal_code);
        mWishField = (TextView)findViewById(R.id.main_greetings);
        mProfilePhoto = (CircleImageView) findViewById(R.id.profile_photo);

        mProfileName.setText(PreferenceHandler.getInstance(SplitActivity.this).getFullName());
        mReferColde.setText("Your Code: ZINGO"+PreferenceHandler.getInstance(SplitActivity.this).getUserId());

        adapter = new MainSearchAdapter(getSupportFragmentManager());

        //Adding adapter to pager
        viewPager.setAdapter(adapter);

        //Adding onTabSelectedListener to swipe views
        tabLayout.setOnTabSelectedListener(this);
        tabLayout.setupWithViewPager(viewPager);
        setUpNavigationDrawer();

        getProfileById();





    }

    private void getProfileById() {

        final ProgressDialog dialog = new ProgressDialog(SplitActivity.this);
        dialog.setTitle("Loading");
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                LoginApi profileApi = Util.getClient().create(LoginApi.class);
                String authenticationString = Util.getToken(SplitActivity.this);
                Call<TravellerAgentProfiles> getProfile = profileApi.getProfileByID(authenticationString, PreferenceHandler.getInstance(SplitActivity.this).getUserId());
                //System.out.println("hotelid = "+hotelid);
                System.out.println();

                getProfile.enqueue(new Callback<TravellerAgentProfiles>() {
                    @Override
                    public void onResponse(Call<TravellerAgentProfiles> call, Response<TravellerAgentProfiles> response) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        if(response.code() == 200)
                        {
                            TravellerAgentProfiles dto = response.body();

                            if(dto != null)
                            {

                                if(dto.getProfilePhoto() != null && !dto.getProfilePhoto().isEmpty())
                                {
                                    if(dto.getProfilePhoto().equalsIgnoreCase("test")){
                                        mProfilePhoto.setImageResource(R.drawable.icons_profile);
                                    }else{
                                        mProfilePhoto.setImageBitmap(Util.convertToBitMap(dto.getProfilePhoto()));
                                    }

                                    // mUserProfileImage.setEnabled(false);
                                }else {
                                    mProfilePhoto.setImageResource(R.drawable.icons_profile);
                                }

                            }
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

    private void setUpNavigationDrawer() {

        /*TypedArray icons = getResources().obtainTypedArray(R.array.navbar_images);
        String[] title  = getResources().getStringArray(R.array.navbar_items_title);*/
        TypedArray icons = null;
        String[] title  = null;
        if(PreferenceHandler.getInstance(SplitActivity.this).getUserRoleUniqueID().equalsIgnoreCase("Luci-Agent"))
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

        navBarListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                drawer.closeDrawer(START);
                displayView(position);
            }
        });
    }

    private void displayView(int position) {
        //System.out.println("position = "+position);
        switch (position)
        {
            case 0:
                // Toast.makeText(this, "Need to Add", Toast.LENGTH_SHORT).show();
                Intent userProfileIntent = new Intent(SplitActivity.this,ProfileDetails.class);
                startActivity(userProfileIntent);
                break;



            case 1:
                Intent earnings = new Intent(SplitActivity.this,EarningDetailsActivity.class);
                startActivity(earnings);
                break;
            case 2:
                Intent bookingHistory = new Intent(SplitActivity.this,BookingHistoryActivity.class);
                startActivity(bookingHistory);
                break;
            case 3:

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Hello this is Zingo Hotels Agent App. Join the Zingo Hotels referral programme, and earn money for every new referral.\n Open the Zingo Hotels App and visit the invite & earn section, and find out your referral code.  It’s an alpha-numeric code like: ZINGO"+ PreferenceHandler.getInstance(SplitActivity.this).getUserId()+"\n Keep Sharing & Earning.\nTo Download the app click here: https://play.google.com/store/apps/details?id=app.zingo.com.agentapp");
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent,"Zingo Agent" ));
                break;
            case 4:
                /*Intent hotel = new Intent(MainActivity.this,HotelListActivity.class);
                startActivity(hotel);*/
                call();
                break;
            case 5:
                //Toast.makeText(this, "Need to Add", Toast.LENGTH_SHORT).show();
               logout();
                break;



        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        setTitle("Search");
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());


    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    public void call()
    {


        AlertDialog.Builder builder = new AlertDialog.Builder(SplitActivity.this);
        builder.setTitle("Do you want to Call our Customer Care Number +91-7065651651?");
        //builder.setCancelable(false);
        builder.setPositiveButton("Call", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri number = Uri.parse("tel:" + "7065651651");
                Intent dial = new Intent(Intent.ACTION_CALL, number);
                if (ActivityCompat.checkSelfPermission(SplitActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

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

    public void logout()
    {


        AlertDialog.Builder builder = new AlertDialog.Builder(SplitActivity.this);
        builder.setTitle("Do you want to Logout?");
        //builder.setCancelable(false);
        builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                PreferenceHandler.getInstance(SplitActivity.this).clear();
                Toast.makeText(SplitActivity.this,"Logout done successfully",Toast.LENGTH_SHORT).show();
                PreferenceHandler.getInstance(SplitActivity.this).clear();
                Intent log = new Intent(SplitActivity.this, LoginActivity.class);
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


    public boolean checkPermission() {
        if ((ContextCompat.checkSelfPermission(SplitActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                && (ContextCompat.checkSelfPermission(SplitActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)&&
                (ContextCompat.checkSelfPermission(SplitActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                && (ContextCompat.checkSelfPermission(SplitActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)) {
            if ((ActivityCompat.shouldShowRequestPermissionRationale(SplitActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION))
                    && (ActivityCompat.shouldShowRequestPermissionRationale(SplitActivity.this, android.Manifest.permission.CALL_PHONE))
                    && (ActivityCompat.shouldShowRequestPermissionRationale(SplitActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE))
                    && (ActivityCompat.shouldShowRequestPermissionRationale(SplitActivity.this, android.Manifest.permission.CAMERA))) {

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(SplitActivity.this,
                        new String[]{
                                android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.CALL_PHONE,
                                android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_RESULT);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(SplitActivity.this,
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
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                LatLng latLang = place.getLatLng();
                double lat  = latLang.latitude;
                double longi  = latLang.longitude;

                try {
                    Geocoder geocoder = new Geocoder(this);
                    List<Address> addresses = geocoder.getFromLocation(lat,longi,1);

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
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
