package app.zingo.com.agentapp.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import app.zingo.com.agentapp.Adapter.NavigationListAdapter;
import app.zingo.com.agentapp.DemoActivity;
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

public class NavbarListActivity extends AppCompatActivity {

    CircleImageView mProfilePhoto;
    TextView mProfileName,mWishField,mReferColde;
    LinearLayout mNavbarHeader;
    ListView navBarListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navbar_list);

        mProfileName = (TextView)findViewById(R.id.main_user_name);
        mReferColde = (TextView)findViewById(R.id.referal_code);
        mWishField = (TextView)findViewById(R.id.main_greetings);
        mProfilePhoto = (CircleImageView) findViewById(R.id.profile_photo);
        mNavbarHeader = (LinearLayout) findViewById(R.id.main_user_profile);

        navBarListView = (ListView) findViewById(R.id.navbar_list);

        mReferColde.setText("ZINGO"+ PreferenceHandler.getInstance(NavbarListActivity.this).getUserId());
        mProfileName.setText(""+ PreferenceHandler.getInstance(NavbarListActivity.this).getFullName());

        setUpNavigationDrawer();
        getTimeFromAndroid();
        getProfileById();

    }

    private void setUpNavigationDrawer() {

        TypedArray icons = null;
        String[] title  = null;
        if(PreferenceHandler.getInstance(NavbarListActivity.this).getUserRoleUniqueID().equalsIgnoreCase("Luci-Agent"))
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
               // drawer.closeDrawer(START);
                displayView(finalTitle[position]);
            }
        });
    }


    private void displayView(String position) {
        //System.out.println("position = "+position);
        switch (position)
        {
            case "My profile":
                // Toast.makeText(this, "Need to Add", Toast.LENGTH_SHORT).show();
                Intent userProfileIntent = new Intent(NavbarListActivity.this,DemoActivity.class);
                userProfileIntent.putExtra("ARG_PAGE",3);
                startActivity(userProfileIntent);
                break;



            case "Earnings":
                Intent earnings = new Intent(NavbarListActivity.this,EarningDetailsActivity.class);
                startActivity(earnings);
                break;
            case "Booking History":
                Intent bookingHistory = new Intent(NavbarListActivity.this,DemoActivity.class);
                bookingHistory.putExtra("ARG_PAGE",2);
                startActivity(bookingHistory);
                break;
            case "Invite & Earn":

              /*  Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Hello this is Zingo Hotels Agent App. Join the Zingo Hotels referral programme, and earn money for every new referral.\n Open the Zingo Hotels App and visit the invite & earn section, and find out your referral code.  It’s an alpha-numeric code like: ZINGO"+ PreferenceHandler.getInstance(MainActivity.this).getUserId()+"\n Keep Sharing & Earning.\nTo Download the app click here: https://play.google.com/store/apps/details?id=app.zingo.com.agentapp");
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent,"Zingo Agent" ));*/
                Intent rc = new Intent(NavbarListActivity.this,ReferalCodeActivity.class);
                startActivity(rc);
                break;

            case "Notifications":

              /*  Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Hello this is Zingo Hotels Agent App. Join the Zingo Hotels referral programme, and earn money for every new referral.\n Open the Zingo Hotels App and visit the invite & earn section, and find out your referral code.  It’s an alpha-numeric code like: ZINGO"+ PreferenceHandler.getInstance(MainActivity.this).getUserId()+"\n Keep Sharing & Earning.\nTo Download the app click here: https://play.google.com/store/apps/details?id=app.zingo.com.agentapp");
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent,"Zingo Agent" ));*/
                Intent nfm = new Intent(NavbarListActivity.this,NotificationListActivity.class);
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


        AlertDialog.Builder builder = new AlertDialog.Builder(NavbarListActivity.this);
        builder.setTitle("Do you want to Logout?");
        //builder.setCancelable(false);
        builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                PreferenceHandler.getInstance(NavbarListActivity.this).clear();
                Toast.makeText(NavbarListActivity.this,"Logout done successfully",Toast.LENGTH_SHORT).show();
                PreferenceHandler.getInstance(NavbarListActivity.this).clear();
                Intent log = new Intent(NavbarListActivity.this, LoginActivity.class);
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


        AlertDialog.Builder builder = new AlertDialog.Builder(NavbarListActivity.this);
        builder.setTitle("Do you want to Call our Customer Care Number +91-7065651651?");
        //builder.setCancelable(false);
        builder.setPositiveButton("Call", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri number = Uri.parse("tel:" + "7065651651");
                Intent dial = new Intent(Intent.ACTION_CALL, number);
                if (ActivityCompat.checkSelfPermission(NavbarListActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

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


    private void getProfileById() {

        final ProgressDialog dialog = new ProgressDialog(NavbarListActivity.this);
        dialog.setTitle("Loading");
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                LoginApi profileApi = Util.getClient().create(LoginApi.class);
                String authenticationString = Util.getToken(NavbarListActivity.this);
                Call<TravellerAgentProfiles> getProfile = profileApi.getProfileByID(authenticationString,
                        PreferenceHandler.getInstance(NavbarListActivity.this).getUserId());
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
                                    if(dto.getProfilePhoto() == null|| dto.getProfilePhoto().equalsIgnoreCase("")
                                            ||dto.getProfilePhoto().equalsIgnoreCase("test")){
                                        mProfilePhoto.setImageResource(R.drawable.icons_profile);
                                    }else{
                                        System.out.println("Bit = "+ Util.convertToBitMap(dto.getProfilePhoto()));
                                        mProfilePhoto.setImageBitmap(Util.convertToBitMap(dto.getProfilePhoto()));
                                    }

                                    // mUserProfileImage.setEnabled(false);
                                }else {
                                    mProfilePhoto.setImageResource(R.drawable.icons_profile);
                                }

                                mProfileName.setText(""+dto.getFirstName());

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

    private void getTimeFromAndroid() {
        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        int hours = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);

        if(hours>=1 && hours<=12){
            //Toast.makeText(this, "Good Morning "+PreferenceHandler.getInstance(HotelListActivity.this).getUserFullName(), Toast.LENGTH_SHORT).show();
            mWishField.setText("Good Morning");
        }else if(hours>=12 && hours<=16){
            //Toast.makeText(this, "Good Afternoon "+PreferenceHandler.getInstance(HotelListActivity.this).getUserFullName(), Toast.LENGTH_SHORT).show();
            mWishField.setText("Good Afternoon");
        }else if(hours>=16 && hours<=20){
            mWishField.setText("Good Evening");
            //Toast.makeText(this, "Good Evening "+PreferenceHandler.getInstance(HotelListActivity.this).getUserFullName(), Toast.LENGTH_SHORT).show();
        }else if(hours>=20 && hours<=24){
            mWishField.setText("Good Night");
            //Toast.makeText(this, "Good Night "+PreferenceHandler.getInstance(HotelListActivity.this).getUserFullName(), Toast.LENGTH_SHORT).show();
        }
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
        goback();
    }

    public void goback()
    {
        Intent main = new Intent(NavbarListActivity.this, DemoActivity.class);
        main.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(main);
        NavbarListActivity.this.finish();
    }
}
