package app.zingo.com.agentapp;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import app.zingo.com.agentapp.Activities.BookingHistoryActivity;
import app.zingo.com.agentapp.Activities.HourlyChargesActivty;
import app.zingo.com.agentapp.Activities.NavbarListActivity;

public class DemoActivity extends TabActivity implements TabHost.OnTabChangeListener {

    TabHost tabHost;
    View tabIndicatorHome,tabIndicatorBidding,tabIndicatorBookingList,tabIndicatorProfile,tabIndicatorOpen;

    public static String HOME_TAB = "Home Tab";
    public static String BID_TAB = "Bid Tab";
    public static String BOOKING_TAB = "Booking Tab";
    public static String PROFILE_TAB = "Profile Tab";
    public static String OPEN = "Open Tab";

    TextView labelHome, labelBid, labelBooking, labelProfile,lableOpen;
    ImageView imgHome, imgBid, imgBooking, imgProfile,imageOpen;

    /*LinearLayout mNavBar;
    private Button btnOpen;
    private Button btnClose;
    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout mDrawerLayout;
    public static ListView mDrawerList;
    Toolbar mToolBar;
*/
    int defaultValue = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            setContentView(R.layout.activity_demo);

            tabHost = (TabHost) findViewById(android.R.id.tabhost);

            //mNavBar = (LinearLayout) findViewById(R.id.nav_bar);


            tabIndicatorHome = LayoutInflater.from(this).inflate(R.layout.tab_indicator, null);
            tabIndicatorBidding= LayoutInflater.from(this).inflate(R.layout.tab_indicator, null);
            tabIndicatorBookingList = LayoutInflater.from(this).inflate(R.layout.tab_indicator, null);
            tabIndicatorProfile = LayoutInflater.from(this).inflate(R.layout.tab_indicator, null);
            tabIndicatorOpen = LayoutInflater.from(this).inflate(R.layout.tab_indicator, null);

            TabHost.TabSpec tabHome = tabHost.newTabSpec(HOME_TAB);
            TabHost.TabSpec tabBid = tabHost.newTabSpec(BID_TAB);
            TabHost.TabSpec tabBooking = tabHost.newTabSpec(BOOKING_TAB);
            TabHost.TabSpec tabProfile = tabHost.newTabSpec(PROFILE_TAB);
            TabHost.TabSpec tabOpen = tabHost.newTabSpec(OPEN);

            labelHome = (TextView) tabIndicatorHome.findViewById(R.id.tab_label);
            imgHome = (ImageView) tabIndicatorHome.findViewById(R.id.tab_image);

            labelBid = (TextView) tabIndicatorBidding.findViewById(R.id.tab_label);
            imgBid = (ImageView) tabIndicatorBidding.findViewById(R.id.tab_image);

            labelBooking = (TextView) tabIndicatorBookingList.findViewById(R.id.tab_label);
            imgBooking = (ImageView) tabIndicatorBookingList.findViewById(R.id.tab_image);

            labelProfile = (TextView) tabIndicatorProfile.findViewById(R.id.tab_label);
            imgProfile = (ImageView) tabIndicatorProfile.findViewById(R.id.tab_image);

            lableOpen = (TextView) tabIndicatorOpen.findViewById(R.id.tab_label);
            imageOpen = (ImageView) tabIndicatorOpen.findViewById(R.id.tab_image);

            labelHome.setText(getResources().getString(R.string.home));
            imgHome.setImageResource(R.drawable.home_icon);
            tabHome.setIndicator(tabIndicatorHome);
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            tabHome.setContent(intent);

            /*labelBid.setText(getResources().getString(R.string.bid));
            imgBid.setImageResource(R.drawable.bidding_icon);
            tabBid.setIndicator(tabIndicatorBidding);
            tabBid.setContent(new Intent(this, BidingActivity.class));*/
            labelBid.setText(getResources().getString(R.string.Hourly_charges));
            imgBid.setImageResource(R.drawable.hr_icon);
            tabBid.setIndicator(tabIndicatorBidding);
            tabBid.setContent(new Intent(this, HourlyChargesActivty.class));

            labelBooking.setText(getResources().getString(R.string.booking));
            imgBooking.setImageResource(R.drawable.booking_history_icon);
            tabBooking.setIndicator(tabIndicatorBookingList);
            tabBooking.setContent(new Intent(this, BookingHistoryActivity.class));

            labelProfile.setText(getResources().getString(R.string.Profile));
            imgProfile.setImageResource(R.drawable.profile_icon);
            tabProfile.setIndicator(tabIndicatorProfile);
            tabProfile.setContent(new Intent(this, ProfileDetails.class));

            lableOpen.setText(getResources().getString(R.string.Menu));
            imageOpen.setImageResource(R.drawable.menu);
            tabOpen.setIndicator(tabIndicatorOpen);
            tabOpen.setContent(new Intent(this, NavbarListActivity.class));

            tabHost.setOnTabChangedListener(this);





            /** Add the tabs to the TabHost to display. */
            tabHost.addTab(tabHome);
            tabHost.addTab(tabBid);
            tabHost.addTab(tabBooking);
            tabHost.addTab(tabProfile);
            tabHost.addTab(tabOpen);

            int page = getIntent().getIntExtra("ARG_PAGE", defaultValue);



            tabHost.setCurrentTab(page);




        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private class SlideMenuClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // display view for selected nav drawer item
            /*displayView(position);*/
        }
    }
    /*private void setUpNavigationDrawer() {

        TypedArray icons = null;
        String[] title  = null;
        if(PreferenceHandler.getInstance(DemoActivity.this).getUserRoleUniqueID().equalsIgnoreCase("Luci-Agent"))
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
        mDrawerList.setAdapter(adapter);

        final String[] finalTitle = title;
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //mDrawerLayout.closeDrawer(STA);
                //displayView(finalTitle[position]);
            }
        });
    }*/

    @Override
    public void onTabChanged(String tabId) {
        labelHome.setTextColor(Color.parseColor("#363636"));
        labelBid.setTextColor(Color.parseColor("#363636"));
        labelBooking.setTextColor(Color.parseColor("#363636"));
        labelProfile.setTextColor(Color.parseColor("#363636"));
        lableOpen.setTextColor(Color.parseColor("#363636"));

        chnageColor(tabId);

    }

    private void chnageColor(String tabId) {
        if(tabId.equalsIgnoreCase(HOME_TAB))
        {
            labelHome.setTextColor(Color.parseColor("#E53935"));
        }
        else if(tabId.equalsIgnoreCase(BID_TAB))
        {
            labelBid.setTextColor(Color.parseColor("#E53935"));
        }
        else if(tabId.equalsIgnoreCase(BOOKING_TAB))
        {
            labelBooking.setTextColor(Color.parseColor("#E53935"));
        }
        else if(tabId.equalsIgnoreCase(PROFILE_TAB))
        {
            labelProfile.setTextColor(Color.parseColor("#E53935"));
        }
        else if(tabId.equalsIgnoreCase(OPEN))
        {
            lableOpen.setTextColor(Color.parseColor("#E53935"));
        }

    }

    private void openCloseNavBar() {

    }
}
