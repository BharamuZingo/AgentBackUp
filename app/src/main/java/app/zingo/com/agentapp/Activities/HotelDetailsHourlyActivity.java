package app.zingo.com.agentapp.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import app.zingo.com.agentapp.Adapter.AutocompleteCustomArrayAdapter;
import app.zingo.com.agentapp.Adapter.CategorisedRoomListAdapter;
import app.zingo.com.agentapp.Adapter.HotelDetailsAdapter;
import app.zingo.com.agentapp.Adapter.ImageAdapter;
import app.zingo.com.agentapp.Adapter.ListItemAdapter;
import app.zingo.com.agentapp.Adapter.ViewPagerAdapter;
import app.zingo.com.agentapp.CustomViews.CustomAutoCompleteView;
import app.zingo.com.agentapp.CustomViews.CustomFontTextView;
import app.zingo.com.agentapp.CustomViews.CustomGridView;
import app.zingo.com.agentapp.MainActivity;
import app.zingo.com.agentapp.Model.AgentHotel;
import app.zingo.com.agentapp.Model.Bookings1;
import app.zingo.com.agentapp.Model.HotelDetails;
import app.zingo.com.agentapp.Model.HotelNotification;
import app.zingo.com.agentapp.Model.HotelService;
import app.zingo.com.agentapp.Model.NotificationManager;
import app.zingo.com.agentapp.Model.Payment;
import app.zingo.com.agentapp.Model.Traveller;
import app.zingo.com.agentapp.Model.TravellerAgentProfiles;
import app.zingo.com.agentapp.R;
import app.zingo.com.agentapp.Utils.Constants;
import app.zingo.com.agentapp.Utils.PreferenceHandler;
import app.zingo.com.agentapp.Utils.ThreadExecuter;
import app.zingo.com.agentapp.Utils.Util;
import app.zingo.com.agentapp.WebApi.BookingApi;
import app.zingo.com.agentapp.WebApi.HotelOperations;
import app.zingo.com.agentapp.WebApi.NotificationApi;
import app.zingo.com.agentapp.WebApi.PaymentApi;
import app.zingo.com.agentapp.WebApi.TravellerApi;
import at.blogc.android.views.ExpandableTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
//activity_hotel_details_hourly

public class HotelDetailsHourlyActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener , PaymentResultListener {

    ViewPager viewPager,mHotelDetailViewPager;
    CustomGridView gridView;
    CustomFontTextView mHotelName,mHotelLocality,mHotelPolicy,mHotelDescription,
            mPolicyMore,mDescriptionMore,mGuestUserName;
    ImageView ReadPolicy;
    ExpandableTextView mHotelDetailsMoreInfo;
    TextView mToggleExpand,mSelectedRooms,mNoOfGuest;
    TabLayout mTabLayout;
    private GoogleMap mMap;
    MapView mapView;
    TextView mReadMoreHotelPolicy,mCheckInDate,mCheckOutDate,
            mSelectRoomBtn,mHotelNameTitle;
    LinearLayout mCheckOutDateSelect,mCheckInDateSelect,mSelectNoGuestAndRooms,
            mGoToHotelLocation,mGotoPhotos,mGotoFacilities,mGuestLayout;
    // Toolbar mBackBtn;
    Toolbar mAnimToolBar;
    RecyclerView mHotelCategoryList;


    ArrayList<Integer> hotelImagesArraylist;
    ArrayList<HotelService> hotelFacilitiesArraylist;
    public int mYear,mMonth,mDay;
    HotelDetails details;
    AgentHotel agentHotel;

    //String checkInDate,checkoutDate;
    int hotelId,displayprice,sellprice;
    String checkInDate,checkoutDate,room,CheckInTime,CheckOutTime;
    //int hotelId;
    SimpleDateFormat simpleDateFormat;

    //LinearLayout dots;
    //ImageView[] dot;

    //New Design
    TextView  mVerifyMobile,mResendOTP,mSubmitOtp;
    private RadioButton mMale,mFemale,mTransgender,mBussiness,mPersonal;
    LinearLayout mCompany,mGST,mGSTCompanyParent,mOTPParent;
    CustomAutoCompleteView mGuestName;
    EditText mGuestMobile,mGuestEmail,mGuestCompany,mGuestGST,mOTP;
    LinearLayout mAboutHotel,mPolicy;

    int travellerIid;
    Traveller traveller;
    Bookings1 bookings;
    int hotelid;
    long commissionAmt;
    AgentHotel hotelDetailseResponse;

    int walletAmount,usedAmount;

    boolean book = true,guestLay = false;
    TravellerAgentProfiles profiles;
    double amount;
    ProgressDialog dialog;

    ArrayList<Traveller> tlist;
    ArrayAdapter<Traveller> searchAdapter;
    TextView mHotelTotalCharges;


    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";

    // [START declare_auth]
    private FirebaseAuth mAuth;

    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String type  = null;


    //PaymentGateway
    private static final String TAG = HotelDetailsHourlyActivity.class.getSimpleName();


    //Booking intent and data
    int price;
    String hotelRate,hotelGst;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_hotel_details_hourly);


        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        simpleDateFormat = new SimpleDateFormat("dd MMM");

        // mBackBtn = (Toolbar) findViewById(R.id.hotel_details_back_btn);
        //  dots = (LinearLayout) findViewById(R.id.dots_layout);

        //mReadMoreHotelPolicy = (TextView) findViewById(R.id.read_more_about_hotel);
        mAnimToolBar = (Toolbar) findViewById(R.id.anim_toolbar);
        setSupportActionBar(mAnimToolBar);
        mCheckInDate = (TextView) findViewById(R.id.hotel_details_check_in_date);
        mHotelNameTitle = (TextView) findViewById(R.id.hotel_name_title);
        mHotelName = (CustomFontTextView) findViewById(R.id.hotel_details_hotel_address);
        mHotelLocality = (CustomFontTextView) findViewById(R.id.hotel_locality);
        mHotelPolicy = (CustomFontTextView) findViewById(R.id.hotel_policy_hourly);
        mHotelDescription = (CustomFontTextView) findViewById(R.id.hotel_about_description_hourly);
        mPolicyMore = (CustomFontTextView) findViewById(R.id.read_more_policy_hourly);
        mDescriptionMore = (CustomFontTextView) findViewById(R.id.read_more_description_hourly);
        mGuestUserName = (CustomFontTextView) findViewById(R.id.guest_user_name);
        ReadPolicy = (ImageView) findViewById(R.id.right_about_hotel);
        mCheckOutDate = (TextView) findViewById(R.id.hotel_details_check_out_date);
        mSelectedRooms = (TextView) findViewById(R.id.hotel_details_no_of_selected_rooms);
        mNoOfGuest = (TextView) findViewById(R.id.hotel_details_no_of_guests);
        //mSelectRoomBtn = (TextView) findViewById(R.id.hotel_details_select_room_btn);
        mCheckInDateSelect = (LinearLayout) findViewById(R.id.check_in_date_linearlayout);
        mCheckOutDateSelect = (LinearLayout) findViewById(R.id.check_out_date_linearlayout);
        mSelectNoGuestAndRooms = (LinearLayout) findViewById(R.id.no_of_guests_and_rooms_linearlayout);

        mHotelCategoryList = (RecyclerView) findViewById(R.id.hotel_category_list);
        mHotelCategoryList.setNestedScrollingEnabled(false);
        //mHotelCategoryList.setVisibility(View.GONE);

        viewPager = (ViewPager) findViewById(R.id.hotel_images_viewpager);
        viewPager.setClipToPadding(false);
        viewPager.setPageMargin(12);
        mGoToHotelLocation = (LinearLayout) findViewById(R.id.go_to_hotel_location);
        mGotoPhotos = (LinearLayout) findViewById(R.id.display_hotel_images);
        mGotoFacilities = (LinearLayout) findViewById(R.id.show_hotel_facilities);
        mGuestLayout = (LinearLayout) findViewById(R.id.guest_layout);


        //New design-----------
        mGuestName = (CustomAutoCompleteView)findViewById(R.id.full_name_guest);
        mGuestName.setThreshold(1);
        mGuestName.setAdapter(searchAdapter);

        mGuestMobile = (EditText) findViewById(R.id.mobile_guest);
        mGuestEmail = (EditText)findViewById(R.id.email_guest);
        mGuestGST = (EditText)findViewById(R.id.gst_guest);
        mGuestCompany = (EditText)findViewById(R.id.company_guest);
        mOTP = (EditText)findViewById(R.id.mobile_otp);
        mCompany = (LinearLayout) findViewById(R.id.company_layout);
        mGST = (LinearLayout)findViewById(R.id.gst_layout);
        mGSTCompanyParent = (LinearLayout)findViewById(R.id.gst_company_parent);
        mOTPParent = (LinearLayout)findViewById(R.id.otp_parent);
        mGSTCompanyParent.setVisibility(View.GONE);
        mOTPParent.setVisibility(View.GONE);
        mVerifyMobile = (TextView)findViewById(R.id.verify_mobile_number);
        mResendOTP = (TextView)findViewById(R.id.resend_otp);
        mSubmitOtp = (TextView)findViewById(R.id.submit_otp);
        mAboutHotel = (LinearLayout) findViewById(R.id.linear_about_hotel);
        mPolicy = (LinearLayout) findViewById(R.id.linear_policy);


        mMale = (RadioButton) findViewById(R.id.booking_male);
        mFemale = (RadioButton) findViewById(R.id.booking_female);
        mPersonal = (RadioButton) findViewById(R.id.booking_personal);
        mBussiness = (RadioButton) findViewById(R.id.booking_company);
        mPersonal.setChecked(true);
        //mTransgender = (RadioButton) findViewById(R.id.booking_transgender);



        //createDot(0);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
        {
            hotelId = bundle.getInt(Constants.HOTEL_ID);
            checkInDate = bundle.getString("CheckinDate");
            checkoutDate = bundle.getString("CheckoutDate");
            CheckInTime = bundle.getString("CheckInTime");
            CheckOutTime = bundle.getString("CheckOutTime");
            displayprice = bundle.getInt("DisplayPrice");
            sellprice = bundle.getInt("SellRate");
            room = bundle.getString("Room");
            System.out.println("CheckInTime"+CheckInTime+" "+"CheckOutTime"+CheckOutTime);
            mCheckInDate.setText(getFormatedDate(checkInDate));
            mCheckOutDate.setText(getFormatedDate(checkoutDate));

            if(room != null)
            {
                String[] s = room.split(",");
                mSelectedRooms.setText(s[0]+" Rooms");
                mNoOfGuest.setText((Integer.parseInt(s[1]))+" Guest");
            }

        }
        getHotelDetails(hotelId);

        if(checkInDate != null && checkoutDate != null)
        {
            CategorisedRoomListAdapter adapter = new CategorisedRoomListAdapter(HotelDetailsHourlyActivity.this,checkInDate,checkoutDate,displayprice,sellprice,room);
            //CategorisedRoomListAdapter adapter = new CategorisedRoomListAdapter(HotelDetailsHourlyActivity.this,checkInDate,checkoutDate,room);
            mHotelCategoryList.setAdapter(adapter);
        }

        mGoToHotelLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(agentHotel.getMaps().size()!=0){
                    Intent intent = new Intent(HotelDetailsHourlyActivity.this,HotelMapActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("HotelName",agentHotel.getHotelName());
                    bundle.putSerializable("Map",agentHotel.getMaps());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else{
                    Toast.makeText(HotelDetailsHourlyActivity.this, "No Map Location for this hotel", Toast.LENGTH_SHORT).show();
                }

            }
        });

        mGotoPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HotelDetailsHourlyActivity.this,ImageFull.class);
                intent.putExtra("HotelId",agentHotel.getHotelId());
                startActivity(intent);


            }
        });

        /*viewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HotelDetailsHourlyActivity.this,HotelImagesList.class);
                intent.putExtra("HotelId",agentHotel.getHotelId());
                startActivity(intent);
            }
        });*/

        mGotoFacilities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlert(agentHotel);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            CollapsingToolbarLayout.LayoutParams lp = (CollapsingToolbarLayout.LayoutParams) mAnimToolBar.getLayoutParams();
            lp.setMargins(0,24,0,0);
            mAnimToolBar.setLayoutParams(lp);
        }

       /* ReadPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(agentHotel.getPolicies().size()!=0){
                    showAlertPolicy(agentHotel);
                }else{
                    Toast.makeText(HotelDetailsHourlyActivity.this, "This hotel do not have any policies", Toast.LENGTH_SHORT).show();
                }

            }
        });*/

        mDescriptionMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //showAlertPolicy(agentHotel);


            }
        });



        mPolicyMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showAlertPolicy(agentHotel);


            }
        });



       /* mTabLayout = (TabLayout) findViewById(R.id.hotel_details_tabLayout);
        mTabLayout.setTabGravity(TabLayout.MODE_FIXED);*/
        //mHotelDetailViewPager = (ViewPager) findViewById(R.id.hotel_details_pager);
        //mapView = (MapView) findViewById(R.id.hotel_details_hotel_location);

        /*mapView.onCreate(savedInstanceState);
        mapView.onResume();

        try {
            MapsInitializer.initialize(HotelDetailsHourlyActivity.this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;


                if (ActivityCompat.checkSelfPermission(HotelDetailsHourlyActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(HotelDetailsHourlyActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mMap.getUiSettings().setZoomControlsEnabled(true);
                mMap.setMyLocationEnabled(true);
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                mMap.getProjection().getVisibleRegion().latLngBounds.getCenter();

                *//*if(trackGPS.canGetLocation())
                {
                    mLatitude = trackGPS.getLatitude();
                    mLongitude = trackGPS.getLongitude();
                }

                LatLng latLng = new LatLng(mLatitude,mLongitude);
                lat.setText(mLatitude+"");
                lng.setText(mLongitude+"");
                final CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(17).build();
                marker = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.
                        defaultMarker(BitmapDescriptorFactory.HUE_RED))
                        .position(latLng));

                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        lat.setText(latLng.latitude+"");
                        lng.setText(latLng.longitude+"");

                        mMap.clear();
                        marker = mMap.addMarker(new MarkerOptions()
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                                .position(latLng));
                        CameraPosition cameraPosition1 = new CameraPosition.Builder().target(latLng).zoom(17).build();
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition1));
                    }
                });*//*

            }
        });*/

        HotelDetailsAdapter hotelDetailsAdapter = new HotelDetailsAdapter(getSupportFragmentManager());

        //Adding adapter to pager
        //mHotelDetailViewPager.setAdapter(hotelDetailsAdapter);

        //Adding onTabSelectedListener to swipe views
       /* mTabLayout.setOnTabSelectedListener(this);
        mTabLayout.setupWithViewPager(mHotelDetailViewPager);*/
        /*gridView = (CustomGridView) findViewById(R.id.hotel_details_amenities);
        mHotelDetailsMoreInfo = (ExpandableTextView) findViewById(R.id.hotel_details_expand_text);
        mToggleExpand = (TextView) findViewById(R.id.collapse_text);
        mToggleExpand.setText(R.string.expand_string);

        mHotelDetailsMoreInfo.setAnimationDuration(750L);
        mHotelDetailsMoreInfo.setInterpolator(new OvershootInterpolator());

// or set them separately
        mHotelDetailsMoreInfo.setExpandInterpolator(new OvershootInterpolator());
        mHotelDetailsMoreInfo.setCollapseInterpolator(new OvershootInterpolator());
        // toggle the ExpandableTextView
        mToggleExpand.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View v)
            {
                mToggleExpand.setText(mHotelDetailsMoreInfo.isExpanded() ? R.string.expand_string : R.string.expand_string);
                mHotelDetailsMoreInfo.toggle();
            }
        });

// but, you can also do the checks yourself
        mToggleExpand.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View v)
            {
                if (mHotelDetailsMoreInfo.isExpanded())
                {
                    mHotelDetailsMoreInfo.collapse();
                    mToggleExpand.setText(R.string.expand_string);
                }
                else
                {
                    mHotelDetailsMoreInfo.expand();
                    mToggleExpand.setText(R.string.collapse_string);
                }
            }
        });*/

// listen for expand / collapse events
        /*mHotelDetailsMoreInfo.setOnE(new ExpandableTextView.OnExpandListener()
        {
            @Override
            public void onExpand(final ExpandableTextView view)
            {
                Log.d("Activity", "ExpandableTextView expanded");
            }

            @Override
            public void onCollapse(final ExpandableTextView view)
            {
                Log.d("Activity", "ExpandableTextView collapsed");
            }
        });*/


        TypedArray hotelImages = getResources().obtainTypedArray(R.array.hotel_images);

        // TypedArray hotelFacilityImages = getResources().obtainTypedArray(R.array.hotel_facily_images);
        // String[] hotelFacilityName = getResources().getStringArray(R.array.hotel_facility_name);

        hotelImagesArraylist = new ArrayList<>();

        for (int i=0;i<hotelImages.length();i++)
        {
            hotelImagesArraylist.add(hotelImages.getResourceId(i,-1));
        }




        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //createDot(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        TypedArray hotelFacilityImages = getResources().obtainTypedArray(R.array.hotel_facily_images);
        String[] hotelFacilityName = getResources().getStringArray(R.array.hotel_facility_name);

        hotelFacilitiesArraylist = new ArrayList<>();

        for (int i=0;i<hotelFacilityName.length;i++)
        {
            hotelFacilitiesArraylist.add(new HotelService(hotelFacilityImages.getResourceId(i,-1),hotelFacilityName[i]));
        }

        /*ViewPagerAdapter hotelImagesadapter = new ViewPagerAdapter(MoreHotelDetailsHourlyActivity.this,hotelImagesArraylist);
        viewPager.setAdapter(hotelImagesadapter);*/

        /*GridViewAdapter facilityAdapter = new GridViewAdapter(HotelDetailsHourlyActivity.this,hotelFacilitiesArraylist);
        gridView.setAdapter(facilityAdapter);*/

       /* mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HotelDetailsHourlyActivity.this.finish();
            }
        });*/
       /* mReadMoreHotelPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHotelPolicy();
            }
        });*/
        SimpleDateFormat dateFormate = new SimpleDateFormat("dd MMM yy");
        //mCheckInDate.setText(dateFormate.format(new Date()));


        Calendar checkout = Calendar.getInstance();
        checkout.setTime(new Date());
        checkout.add(Calendar.DATE, 1);
        System.out.println("checkIn = "+dateFormate.format(checkout.getTime()));
        //mCheckOutDate.setText(dateFormate.format(checkout.getTime()));

        mCheckInDateSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker(mCheckInDateSelect,mCheckInDate);
            }
        });

        mCheckOutDateSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker(mCheckOutDateSelect,mCheckOutDate);
            }
        });

        mSelectNoGuestAndRooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(HotelDetailsHourlyActivity.this,SelectRoomActivity.class);
                startActivity(intent);*/
            }
        });

        /*mSelectRoomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HotelDetailsHourlyActivity.this,SelectRoomBasedOnCategoryActivity.class);
                startActivity(intent);
            }
        });*/


        //New Design------
        mGuestName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Traveller ci = (Traveller)parent.getItemAtPosition(position);
                mGuestName.setText(ci.getFirstName());
                if(ci.getGender().equalsIgnoreCase("Male"))
                {
                    mMale.setChecked(true);
                    mMale.setEnabled(false);
                    mFemale.setEnabled(false);

                }
                else if(ci.getGender().equalsIgnoreCase("Female"))
                {
                    mFemale.setChecked(true);
                    mMale.setEnabled(false);
                    mFemale.setEnabled(false);
                }
                //mGuestMobile.setEnabled(false);
                mGuestEmail.setText(ci.getEmail());
                //mGuestEmail.setEnabled(false);
                mGuestCompany.setText(ci.getCompany());
                //mGuestCompany.setEnabled(false);
                mGuestGST.setText(ci.getCustomerGST());
                //mGuestGST.setEnabled(false);

                travellerIid = ci.getTravellerId();
            }
        });

        mResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile = mGuestMobile.getText().toString();
                if(mobile == null || mobile.isEmpty())
                {
                    mGuestMobile.setError("Please enter valid mobile number");
                }
                else
                {
                    dialog = new ProgressDialog(HotelDetailsHourlyActivity.this);
                    dialog.setMessage(getResources().getString(R.string.loader_message));
                    dialog.setCancelable(false);
                    dialog.show();
                    //verifyPhoneNumberWithCode(mVerificationId, code);
                    /*if(type.equalsIgnoreCase("resend")){
                        resendVerificationCode(country+mobile, mResendToken);
                    }else{*/
                    //startPhoneNumberVerification(country+mobile);
                    //startPhoneNumberVerification(mobile);
                    resendVerificationCode(mobile, mResendToken);
                }
            }
        });

        mVerifyMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile = mGuestMobile.getText().toString();
                if(mobile == null || mobile.isEmpty())
                {
                    mGuestMobile.setError("Please enter valid mobile number");
                }
                else
                {
                    dialog = new ProgressDialog(HotelDetailsHourlyActivity.this);
                    dialog.setMessage(getResources().getString(R.string.loader_message));
                    dialog.setCancelable(false);
                    dialog.show();
                    //verifyPhoneNumberWithCode(mVerificationId, code);
                    /*if(type.equalsIgnoreCase("resend")){
                        resendVerificationCode(country+mobile, mResendToken);
                    }else{*/
                    //startPhoneNumberVerification(country+mobile);
                    startPhoneNumberVerification(mobile);
                }
                //}
            }
        });


        mSubmitOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = mOTP.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    mOTP.setError("Cannot be empty.");
                    return;
                }
                else if(mVerificationId==null||mVerificationId.isEmpty()){
                    Toast.makeText(HotelDetailsHourlyActivity.this, "OTP is not matching", Toast.LENGTH_SHORT).show();
                }else{
                    dialog = new ProgressDialog(HotelDetailsHourlyActivity.this);
                    dialog.setMessage(getResources().getString(R.string.loader_message));
                    dialog.setCancelable(false);
                    dialog.show();
                    verifyPhoneNumberWithCode(mVerificationId, code);
                }
            }
        });

        mPersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGST.setVisibility(View.GONE);
                mCompany.setVisibility(View.GONE);
            }
        });

        mCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGST.setVisibility(View.VISIBLE);
                mCompany.setVisibility(View.VISIBLE);
            }
        });


        mPersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPersonal.isChecked())
                {
                    mPersonal.setChecked(true);
                    mBussiness.setChecked(false);
                    mGSTCompanyParent.setVisibility(View.GONE);
                }
                else
                {
                    mPersonal.setChecked(true);
                    mBussiness.setChecked(false);
                    mGSTCompanyParent.setVisibility(View.GONE);
                }
            }
        });

        mBussiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBussiness.isChecked())
                {
                    mBussiness.setChecked(true);
                    mPersonal.setChecked(false);
                    mGSTCompanyParent.setVisibility(View.VISIBLE);
                }
                else
                {
                    mBussiness.setChecked(true);
                    mPersonal.setChecked(false);
                    mGSTCompanyParent.setVisibility(View.VISIBLE);
                }
            }
        });

        mGuestMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                isValidEmail();

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (start == 9){

                    getTravelerByPhone(mGuestMobile.getText().toString());

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {

                Log.d(TAG, "onVerificationCompleted:" + credential);
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;

                String code = credential.getSmsCode();
                mOTP.setText(code);
                dialog = new ProgressDialog(HotelDetailsHourlyActivity.this);
                dialog.setMessage(getResources().getString(R.string.loader_message));
                dialog.setCancelable(false);
                dialog.show();

                signInWithPhoneAuthCredential(credential);//<---hard code---->

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {


                Log.w(TAG, "onVerificationFailed", e);
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;
                // [END_EXCLUDE]

                if (e instanceof FirebaseAuthInvalidCredentialsException) {

                    mGuestMobile.setError("Invalid phone number.");

                } else if (e instanceof FirebaseTooManyRequestsException) {

                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {

                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;


            }
        };

        if(PreferenceHandler.getInstance(HotelDetailsHourlyActivity.this).getUserId()!=0&&PreferenceHandler.getInstance(HotelDetailsHourlyActivity.this).getFullName()!=null&&!PreferenceHandler.getInstance(HotelDetailsHourlyActivity.this).getFullName().isEmpty()){
            mGuestUserName.setText(PreferenceHandler.getInstance(HotelDetailsHourlyActivity.this).getFullName());
            mGuestName.setText(PreferenceHandler.getInstance(HotelDetailsHourlyActivity.this).getFullName());
            if(PreferenceHandler.getInstance(HotelDetailsHourlyActivity.this).getPhoneNumber()!=null){
                mGuestMobile.setText(PreferenceHandler.getInstance(HotelDetailsHourlyActivity.this).getPhoneNumber());
            }


            if(PreferenceHandler.getInstance(HotelDetailsHourlyActivity.this).getEmailId()!=null){
                mGuestEmail.setText(PreferenceHandler.getInstance(HotelDetailsHourlyActivity.this).getEmailId());
            }

        }else{
            mGuestUserName.setText("");
            mGuestMobile.setText("");
            mGuestEmail.setText("");
        }


        mGuestUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(guestLay){
                    mGuestLayout.setVisibility(View.GONE);
                    guestLay = false;
                }else{
                    mGuestLayout.setVisibility(View.VISIBLE);
                    guestLay = true;
                }

            }
        });


    }

    private String getFormatedDate(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd MMM yy");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd MMM yyyy");

        try {
            Date date1 = null;
            if(date.contains("/"))
            {
                date1 = simpleDateFormat.parse(date);
            }
            else
            {
                date1 = simpleDateFormat2.parse(date);
            }

            return  simpleDateFormat1.format(date1);

        }
        catch (Exception ex)
        {
            return null;
        }
    }

    private void getHotelDetails(final int hotelId) {

        final ProgressDialog dialog = new ProgressDialog(HotelDetailsHourlyActivity.this);
        dialog.setMessage(getResources().getString(R.string.loader_message));
        dialog.setCancelable(false);
        dialog.show();
        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                String auth_string = Util.getToken(HotelDetailsHourlyActivity.this);//"Basic " +  Base64.encodeToString(authentication.getBytes(), Base64.NO_WRAP);
                HotelOperations hotelOperation = Util.getClient().create(HotelOperations.class);
                Call<AgentHotel> response = hotelOperation.getAgentHotelByHotelId(auth_string,hotelId);

                response.enqueue(new Callback<AgentHotel>() {
                    @Override
                    public void onResponse(Call<AgentHotel> call, Response<AgentHotel> response) {
                        System.out.println("GetHotelByProfileId = "+response.code());
                        hotelDetailseResponse = response.body();
                        if(dialog != null && dialog.isShowing())
                        {
                            dialog.dismiss();
                        }
                        if(response.code() == 200)
                        {
                            AgentHotel hotelDetailseResponse = response.body();
                            if(hotelDetailseResponse != null )
                            {
                                agentHotel = hotelDetailseResponse;


                                if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                                setSupportActionBar(mAnimToolBar);


                                mHotelNameTitle.setText(agentHotel.getHotelName());
                                mHotelName.setText(agentHotel.getHotelName());
                                if(agentHotel.getMaps()!=null&&agentHotel.getMaps().size()!=0){
                                    mHotelLocality.setText(agentHotel.getMaps().get(0).getLocation());
                                }else{
                                    mHotelLocality.setText(agentHotel.getHotelStreetAddress()+","+agentHotel.getLocalty());
                                }


                                if(agentHotel.getPolicies()!=null&&agentHotel.getPolicies().size()!=0){
                                    mHotelPolicy.setText("Hotel Policy: \n"+agentHotel.getPolicies().get(0).getHotelPolicy()+"\n\n\nCancellation Policy:\n\n"
                                            +agentHotel.getPolicies().get(0).getStandardCancellationPolicy()+"\n\n\nService Policy:\n\n"
                                            +agentHotel.getPolicies().get(0).getHotelServices());
                                }else{
                                    mHotelPolicy.setText("Sorry! This hotel do not have any policies");
                                }




                                if(agentHotel.getHotelImage().size()!=0){
                                    ImageAdapter hotelImagesadapter = new ImageAdapter(HotelDetailsHourlyActivity.this,agentHotel.getHotelImage(),agentHotel.getHotelId(),"HotelDetails");
                                    viewPager.setAdapter(hotelImagesadapter);

                                }else{
                                    ViewPagerAdapter hotelImagesadapter = new ViewPagerAdapter(HotelDetailsHourlyActivity.this,hotelImagesArraylist,agentHotel.getHotelId(),"HotelDetails");
                                    viewPager.setAdapter(hotelImagesadapter);

                                }


                                //}
                                /*details = hotelDetailseResponse.get(0);
                                /*//*System.out.println("Hotel name = "+details.getHotelName());
                                System.out.println("Hotel id = "+details.getHotelId());/*//*
                                System.out.println("Hotel id = "+details.getHotelId());
                                mHotelName.setText(details.getHotelName());
                                mHotelDisplayName.setText(details.getHotelDisplayName());
                                for (int i=0;i<hotelTypes.length;i++)
                                {
                                    if(hotelTypes[i].equals(details.getHotelType()))
                                    {
                                        mHotelTypeSpinner.setSelection(i);
                                        break;
                                    }
                                }
                                if(chainsList != null)
                                {
                                    for (int i=0;i<chainsList.size();i++)
                                    {
                                        if(chainsList.get(i).getChainName().equals(details.getChainName()))
                                        {
                                            mChainNameSpinner.setSelection(i);
                                            break;
                                        }
                                    }
                                }
                                mStarRating.setText(details.getStarRating());
                                mBuiltYear.setText(details.getHotelBuiltYear());
                                mNOofRestuarents.setText(details.getNoofRestuarentsInHotel());
                                mNoOfRooms.setText(details.getNoOfRoomsInHotel());
                                mNoofFloors.setText(details.getNoOfFloorsInHotel());
                                mCurrency.setText(details.getCurrencyAccepted());
                                mVccCurrency.setText(details.getVccCurrencyAccepted());
                                mTimeZone.setText(details.getHotelTimeZone());
                                mStreetAddress.setText(details.getHotelStreetAddress());
                                mCheckInTime.setText(details.getStandardCheckInTime());
                                mCheckOutTime.setText(details.getStandardCheckOutTime());
                                //mState.setText(details.get());
                                m24HrCheckIn.setChecked(details.getIs24HoursCheckIn());*/
                                /*mBuiltYear.setText(details.getHotelBuiltYear());
                                mBuiltYear.setText(details.getHotelBuiltYear());
                                mBuiltYear.setText(details.getHotelBuiltYear());
                                mBuiltYear.setText(details.getHotelBuiltYear());
                                mBuiltYear.setText(details.getHotelBuiltYear());
                                mBuiltYear.setText(details.getHotelBuiltYear());*/
                                /*mHotelPlace.setText(details.getHotelStreetAddress());
                                hotelOverviewlayout.setVisibility(View.VISIBLE);*/
                                /*PreferenceHandler.getInstance(getActivity()).setHotelId(details.getHotelId());
                                PreferenceHandler.getInstance(getActivity()).setHotelName(details.getHotelName());
                                mAddHotelBtn.setVisibility(View.GONE);*/


                            }
                            else
                            {
                            }
                        }
                        else
                        {
                            Toast.makeText(HotelDetailsHourlyActivity.this,"Check your internet connection or please try after some time",
                                    Toast.LENGTH_LONG).show();
                        }


                    }

                    @Override
                    public void onFailure(Call<AgentHotel> call, Throwable t) {
                        System.out.println("Failed");

                        if(dialog != null && dialog.isShowing())
                        {
                            dialog.dismiss();
                        }
                        Toast.makeText(HotelDetailsHourlyActivity.this,"Check your internet connection or please try after some time",
                                Toast.LENGTH_LONG).show();

                    }
                });
            }
        });
    }

    private void showAlert(AgentHotel details) {

        if(details != null && details.getAminetiesList() != null && details.getAminetiesList().size() != 0)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(HotelDetailsHourlyActivity.this);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = (View) inflater.inflate(R.layout.facilities_alertbox_layout,null);

            ListView itemsList = (ListView) v.findViewById(R.id.facilities_list);
            itemsList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);


            final ListItemAdapter adapter = new ListItemAdapter(HotelDetailsHourlyActivity.this,agentHotel.getAminetiesList());

            itemsList.setAdapter(adapter);


            builder.setView(v);
            // builder.setTitle(title);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    dialogInterface.dismiss();


                }
            });

       /* builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });*/


            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else
        {
            Toast.makeText(HotelDetailsHourlyActivity.this,"No amenities are there",Toast.LENGTH_SHORT).show();
        }

    }

    private void showAlertPolicy(AgentHotel details) {

        if(details != null )
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(HotelDetailsHourlyActivity.this);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = (View) inflater.inflate(R.layout.alert_box_policy,null);

            TextView policyDetails = (TextView) v.findViewById(R.id.policy_details);

            if(details.getPolicies()!=null&&details.getPolicies().size()!=0){
                policyDetails.setText("Hotel Policy: \n\n"+details.getPolicies().get(0).getHotelPolicy()+"\n\n\nCancellation Policy:\n\n"
                        +details.getPolicies().get(0).getStandardCancellationPolicy()+"\n\n\nService Policy:\n\n"
                        +details.getPolicies().get(0).getHotelServices());
            }else{
                policyDetails.setText("Sorry! This hotel do not have any policies");
            }


//This hotel do not have any policies

            builder.setView(v);
            // builder.setTitle(title);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    dialogInterface.dismiss();


                }
            });



            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else
        {
            Toast.makeText(HotelDetailsHourlyActivity.this,"No amenities are there",Toast.LENGTH_SHORT).show();
        }

    }

    private void showalertboxPay() throws Exception{

        final android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(HotelDetailsHourlyActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.alert_payment_mode,null);

        mHotelTotalCharges = (TextView)view.findViewById(R.id.hotel_total_charges);
        TextView mHotelRate = (TextView)view.findViewById(R.id.hotel_charges);
        TextView mHotelGst = (TextView)view.findViewById(R.id.hotel_gst_charges);
        TextView mAgentErnings = (TextView)view.findViewById(R.id.agent_earnings);

        TextView mPayLater = (TextView)view.findViewById(R.id.pay_later_btn);
        TextView mPayNow = (TextView)view.findViewById(R.id.pay_now_btn);
        dialogBuilder.setView(view);
        final android.app.AlertDialog dialog = dialogBuilder.create();
        dialog.show();




        //int days = getDays();


        int totalbaseprice = 0;
        int totalprice = 0;
        int GST =0,GST1 = 0,roomPrice = 0;
        String[] s = room.split(",");

        if(s[0] != null && !s[0].isEmpty()) {
            totalbaseprice = sellprice;
            GST = getGstPrice(sellprice)*Integer.parseInt(s[0]);
            roomPrice = sellprice*Integer.parseInt(s[0]);
            price = totalbaseprice;
            GST1 = getGstPrice(price);
            totalprice = (totalbaseprice+GST1)*Integer.parseInt(s[0]);
        }
        else
        {
            totalbaseprice = sellprice;
            roomPrice = sellprice;
            price = totalbaseprice;
            GST = getGstPrice(price);
            totalprice = totalbaseprice+GST;
        }
        //int totalbaseprice = price*days*rooms;




        mHotelGst.setText("₹ "+GST);
        mHotelRate.setText("₹ "+sellprice);
        mHotelTotalCharges.setText("₹ "+(totalprice));

        hotelRate = mHotelRate.getText().toString();
        hotelGst = mHotelGst.getText().toString();





        mPayLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    if(dialog != null)
                    {
                        dialog.dismiss();
                    }

                    String guestName = mGuestName.getText().toString();
                    String guestMobile = mGuestMobile.getText().toString();
                    String guestEmail = mGuestEmail.getText().toString();
                    String total = mHotelTotalCharges.getText().toString();
                    String hotelName = mHotelName.getText().toString();
                    String gst = mGuestGST.getText().toString();
                    String company = mGuestCompany.getText().toString();

                    if(guestName==null||guestName.isEmpty()){
                        Toast.makeText(HotelDetailsHourlyActivity.this, "Field should not be empty", Toast.LENGTH_SHORT).show();
                    }else if(guestMobile==null||guestMobile.isEmpty()){
                        Toast.makeText(HotelDetailsHourlyActivity.this, "Field should not be empty", Toast.LENGTH_SHORT).show();
                    }else if(guestEmail==null||guestEmail.isEmpty()){
                        Toast.makeText(HotelDetailsHourlyActivity.this, "Field should not be empty", Toast.LENGTH_SHORT).show();
                    }else if(total==null||total.isEmpty()){
                        Toast.makeText(HotelDetailsHourlyActivity.this, "Field should not be empty", Toast.LENGTH_SHORT).show();
                    }else if(hotelName==null||hotelName.isEmpty()){
                        Toast.makeText(HotelDetailsHourlyActivity.this, "Field should not be empty", Toast.LENGTH_SHORT).show();
                    } else if(!mMale.isChecked() && !mFemale.isChecked() )//&& !mTransgender.isChecked())
                    {
                        Toast.makeText(HotelDetailsHourlyActivity.this,"Please select gender",Toast.LENGTH_SHORT).show();
                    }else if(mBussiness.isChecked()  )//&& !mTransgender.isChecked())
                    {
                        if(gst==null||gst.isEmpty()){
                            mGuestGST.setError("Field should not be empty");
                            mGuestGST.requestFocus();
                        }
                        else if(gst.length() != 15)
                        {
                            mGuestGST.setError("Please enter valid gst number");
                            mGuestGST.requestFocus();
                        }
                        else if(company==null||company.isEmpty()){
                            mGuestCompany.setError("Field should not be empty");
                            mGuestCompany.requestFocus();
                        }else{
                            if(travellerIid==0){
                                addTraveler("paylater");
                                //System.out.println("Not exist");
                            }else if(tlist != null && !isexist(tlist)){
                                addTraveler("paylater");
                                //updateTraveller("paylater");
                                //System.out.println("Not exist "+isexist(tlist));
                            }
                            else
                            {
                                booking("paylater");
                            }
                        }
                    }else{
                        if(travellerIid==0){
                            addTraveler("paylater");
                            //System.out.println("Not exist "+isexist(tlist));
                        }else if(tlist != null && !isexist(tlist)){
                            addTraveler("paylater");
                            //updateTraveller("paylater");
                            //System.out.println("Not exist "+isexist(tlist));
                        }
                        else
                        {
                            booking("paylater");
                        }
                    /*Intent book = new Intent(HotelDetailsHourlyActivity.this,BookingDetailsActivity.class);
                    startActivity(book);*/
                    }


                    //booking("paylater");



                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        });
        mPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try
                {
                    if(dialog != null)
                    {
                        dialog.dismiss();
                    }

                    String guestName = mGuestName.getText().toString();
                    String guestMobile = mGuestMobile.getText().toString();
                    String guestEmail = mGuestEmail.getText().toString();
                    String total = mHotelTotalCharges.getText().toString();
                    String hotelName = mHotelName.getText().toString();

                    if(guestName==null||guestName.isEmpty()){
                        Toast.makeText(HotelDetailsHourlyActivity.this, "Field should not be empty", Toast.LENGTH_SHORT).show();
                    }else if(guestMobile==null||guestMobile.isEmpty()){
                        Toast.makeText(HotelDetailsHourlyActivity.this, "Field should not be empty", Toast.LENGTH_SHORT).show();
                    }else if(guestEmail==null||guestEmail.isEmpty()){
                        Toast.makeText(HotelDetailsHourlyActivity.this, "Field should not be empty", Toast.LENGTH_SHORT).show();
                    }else if(total==null||total.isEmpty()){
                        Toast.makeText(HotelDetailsHourlyActivity.this, "Field should not be empty", Toast.LENGTH_SHORT).show();
                    }else if(hotelName==null||hotelName.isEmpty()){
                        Toast.makeText(HotelDetailsHourlyActivity.this, "Field should not be empty", Toast.LENGTH_SHORT).show();
                    } else if(!mMale.isChecked() && !mFemale.isChecked() )//&& !mTransgender.isChecked())
                    {
                        Toast.makeText(HotelDetailsHourlyActivity.this,"Please select gender",Toast.LENGTH_SHORT).show();
                    }else{
                        startPayment();
                    /*Intent book = new Intent(HotelDetailsHourlyActivity.this,BookingDetailsActivity.class);
                    startActivity(book);*/
                    }




                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        });
    }
    public void openDatePicker(final LinearLayout lv, final TextView tv) {
        // Get Current Date

        final Calendar c = Calendar.getInstance();
        mYear  = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay   = c.get(Calendar.DAY_OF_MONTH);
        //launch datepicker modal
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Log.d("Date", "DATE SELECTED "+dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year,monthOfYear,dayOfMonth);

                        String date1 = (monthOfYear + 1)  + "/" + dayOfMonth + "/" + year;

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


                        if (lv.equals(mCheckInDateSelect)){
                            // from = date1;
                            try {

                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");

                                Date fdate = simpleDateFormat.parse(date1);
                                System.out.println(simpleDateFormat.format(simpleDateFormat.parse(date1)));
                                checkInDate = simpleDateFormat.format(simpleDateFormat.parse(date1));

                                if(new Date().getTime() > fdate.getTime())
                                {
                                    Toast.makeText(HotelDetailsHourlyActivity.this,"Please select future date",Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    String from = new SimpleDateFormat("dd MMM yy").format(fdate);
                                    System.out.println("To = "+from);
                                    tv.setText(from);
                                }

                                //fd = sdf.parse(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
                                //cd = sdf.parse(sdf.format(new Date()));
                                /*if(cd.compareTo(fd)>0){
                                    Toast.makeText(BookRoomActivity.super.getApplicationContext(),"Choose valid date",Toast.LENGTH_LONG).show();
                                    tv.setText("");
                                }else{

                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
                                    try {
                                        Date fdate = simpleDateFormat.parse(date1);

                                        from = simpleDateFormat.format(fdate);
                                        System.out.println("To = "+from);
                                        tv.setText(from);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    //
                                }*/
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }


                        }else if (lv.equals(mCheckOutDateSelect)) {



                            try {
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
                                Date fdate = simpleDateFormat.parse(date1);
                                String cdate = mCheckInDate.getText().toString();
                                Date cDate = new SimpleDateFormat("dd MMM yy").parse(cdate);
                                System.out.println(simpleDateFormat.format(simpleDateFormat.parse(date1)));

                                checkoutDate = simpleDateFormat.format(simpleDateFormat.parse(date1));
                                if(cDate.getTime() >= fdate.getTime())
                                {
                                    Toast.makeText(HotelDetailsHourlyActivity.this,"Please select future date",Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    String to = new SimpleDateFormat("dd MMM yy").format(fdate);
                                    System.out.println("To = "+to);
                                    tv.setText(to);

                                }


                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            //to = date1;
                            /*try {
                                td = sdf.parse(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
                                cd = sdf.parse(sdf.format(new Date()));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }*/
                            /*if(cd.compareTo(td)>0){
                                Toast.makeText(BookRoomActivity.super.getApplicationContext(),"Choose valid date",Toast.LENGTH_LONG).show();
                                tv.setText("");
                            }else if(fd.compareTo(td) >= 0){
                                Toast.makeText(BookRoomActivity.super.getApplicationContext(),"Check out date should not be before to Check in date",Toast.LENGTH_LONG).show();
                                tv.setText("");
                            }else{
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
                                try {
                                    Date tdate = simpleDateFormat.parse(date1);

                                    *//*from = simpleDateFormat.format(fdate);

                                    tv.setText(from);*//*
                                    to = simpleDateFormat.format(tdate);
                                    System.out.println("To = "+to);
                                    tv.setText(to);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }*/
                        }
                        else
                        {
                            /*dateOfBirth = date1;
                            dob_traveler.setText(dateOfBirth);*/
                        }


                    }
                }, mYear, mMonth, mDay);


        datePickerDialog.show();

    }



   /* public void showHotelPolicy()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(HotelDetailsHourlyActivity.this);
        builder.setTitle("Hotel Policy");
        builder.setPositiveButton("GOT IT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }*/

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        //mHotelDetailViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    public class CategorisedRoomListAdapter extends RecyclerView.Adapter<CategorisedRoomListAdapter.ViewHolder> {

        private Context context;
        private String checkindate,checkoutdate,room;
        private int displayprice,sellprice;
        public CategorisedRoomListAdapter(Context context,String checkindate,String checkoutdate,int displayprice,int sellprice,String room)
        {
            this.context = context;
            this.checkindate = checkInDate;
            this.checkoutdate = checkoutDate;
            this.displayprice = displayprice;
            this.sellprice = sellprice;
            this.room = room;
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_room_list_adapter_layout,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {


            holder.mDisplaprice.setText("₹ "+displayprice);
            holder.mSellPrice.setText("₹ "+sellprice);
            if(displayprice!= 0 && sellprice != 0)
            {
                double diff = displayprice - sellprice;
                //System.out.println("diff = "+diff);
                double div = diff/displayprice;
                //System.out.println("div = "+div);
                double dis = div*100;
                //System.out.println("dis = "+dis);
                DecimalFormat numberFormat = new DecimalFormat("#.##");
               // holder.mDiscount.setText(" "+numberFormat.format(dis)+"% Discount");
                //holder.mDiscount.setText(" "+dis+"% Discount");
            }
            else
            {
                holder.mDiscount.setText(" "+0+"% Discount");
            }

            holder.mSelectedBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                  /*  Intent intent = new Intent(context, ReviewHotelDetailsHourlyActivity.class);
                    intent.putExtra("HotelId",hotelId   );
                    intent.putExtra("Price",sellprice);
                    intent.putExtra("displayprice",displayprice);
                    intent.putExtra("CheckinDate",checkInDate);
                    intent.putExtra("CheckoutDate",checkoutDate);
                    intent.putExtra("CheckInTime",CheckInTime);
                    intent.putExtra("CheckoutTime",CheckOutTime);
                    intent.putExtra("Room",room);

                    context.startActivity(intent);*/
                    try{
                        showalertboxPay();
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            });

            holder.mDisplayPolicy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(agentHotel.getPolicies().size()!=0){
                        showAlertPolicy(agentHotel);
                    }else{
                        Toast.makeText(HotelDetailsHourlyActivity.this, "This hotel do not have any policies", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            holder.mRoomOverView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showOverView(holder.mRoomCategory.getText().toString());
                }
            });
        }

        private void showOverView(String s) {


            AlertDialog.Builder builder = new AlertDialog.Builder(HotelDetailsHourlyActivity.this);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = (View) inflater.inflate(R.layout.alert_box_policy,null);

            TextView policyDetails = (TextView) v.findViewById(R.id.policy_details);
            TextView title = (TextView) v.findViewById(R.id.alert_title);

            title.setText("Room Overview");
            policyDetails.setText(" "+s);
            //itemsList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);


            /*final ListItemAdapter adapter = new ListItemAdapter(HotelDetailsHourlyActivity.this,details.getAminetiesList());

            itemsList.setAdapter(adapter);*/


            builder.setView(v);
            // builder.setTitle(title);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    dialogInterface.dismiss();


                }
            });

       /* builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });*/


            AlertDialog dialog = builder.create();
            dialog.show();
        }

        @Override
        public int getItemCount() {
            return 1;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView mSelectedBtn,mDisplaprice,mSellPrice,mDiscount,mDisplayPolicy,mRoomOverView,mRoomCategory;

            View itemView;
            public ViewHolder(View itemView) {
                super(itemView);
                this.itemView = itemView;
                mSelectedBtn = (TextView) itemView.findViewById(R.id.selected_room_btn);
                mRoomCategory = (TextView) itemView.findViewById(R.id.select_room_category_type);
                mDisplaprice = (TextView) itemView.findViewById(R.id.select_room_display_rate);
                mSellPrice = (TextView) itemView.findViewById(R.id.select_room_sell_rate);
                mDiscount = (TextView) itemView.findViewById(R.id.select_room_discount);
                mDiscount.setVisibility(View.GONE);
                mDisplaprice.setVisibility(View.GONE);
                mDisplayPolicy = (TextView) itemView.findViewById(R.id.display_policies);
                mRoomOverView = (TextView) itemView.findViewById(R.id.show_room_overview);
                //mRoomCategory = (TextView) itemView.findViewById(R.id.select_room_category_type);
            }
        }
    }

    /*private void createDot(int current){
        if(dots != null){
            dots.removeAllViews();
        }
        dot = new ImageView[agentHotel.getHotelImage().size()];
        for (int i =0;i<agentHotel.getHotelImage().size();i++){
            dot[i] = new ImageView(this);
            if(i==current){
                dot[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.active_dots));
            }else {
                dot[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.inactive_dots));
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(4,0,4,0);
            dots.addView(dot[i],params);
        }
    }*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        try{
            switch (itemId)
            {


                case android.R.id.home:
                    // app icon action bar is clicked; go to parent activity


                    this.finish();
                    return true;

                default:
                    return super.onOptionsItemSelected(item);
            }

        }catch (Exception e ){
            e.printStackTrace();
            return super.onOptionsItemSelected(item);
        }



    }

    //New Design-------
    private void getTravelerByPhone(final String dto){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("please wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                String auth_string = Util.getToken(HotelDetailsHourlyActivity.this);//"Basic " +  Base64.encodeToString(authentication.getBytes(), Base64.NO_WRAP);
                TravellerApi apiService = Util.getClient().create(TravellerApi.class);
                Call<ArrayList<Traveller>> call = apiService.fetchTravelerByPhone(auth_string,dto);

                call.enqueue(new Callback<ArrayList<Traveller>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Traveller>> call, Response<ArrayList<Traveller>> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                        int statusCode = response.code();
                        if (statusCode == 200 || statusCode == 201 || statusCode == 203 || statusCode == 204) {

                            if (progressDialog != null)
                                progressDialog.dismiss();


                            if (response.body().size() != 0) {
                                tlist = response.body();

                                AutocompleteCustomArrayAdapter autocompleteCustomArrayAdapter =
                                        new AutocompleteCustomArrayAdapter(HotelDetailsHourlyActivity.this,R.layout.hotels_row,tlist,"HotelDetailsHourlyActivity");
                                mGuestName.setThreshold(1);
                                mGuestName.setAdapter(autocompleteCustomArrayAdapter);


                            }else{
                                if(tlist != null)
                                {
                                    tlist.clear();
                                }
                                travellerIid = 0;
                                mGuestName.setEnabled(true);
                                mGuestMobile.setEnabled(true);
                                mGuestEmail.setEnabled(true);
                                mGuestCompany.setEnabled(true);
                                mGuestGST.setEnabled(true);

                              /*  mVerifyMobile.setVisibility(View.VISIBLE);
                                mOTPParent.setVisibility(View.VISIBLE);*/
                               /* mOtherParent.setVisibility(View.GONE);
                                mPayNow.setEnabled(false);
                                mPayLater.setEnabled(false);*/
                            }
                        }else {
                            if (progressDialog != null)
                                progressDialog.dismiss();
                            Toast.makeText(HotelDetailsHourlyActivity.this, " failed due to : " + response.message(), Toast.LENGTH_SHORT).show();
                        }
//                callGetStartEnd();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Traveller>> call, Throwable t) {
                        // Log error here since request failed
                        if (progressDialog!=null)
                            progressDialog.dismiss();
                        Log.e("TAG", t.toString());
                    }
                });
            }


        });
    }

    private boolean isexist(ArrayList<Traveller> travellers) {

        if(travellers.size() != 0)
        {
            String name = mGuestName.getText().toString().toLowerCase().trim();
            String mobile = mGuestMobile.getText().toString().trim();
            String email = mGuestEmail.getText().toString().trim();
            for (int i=0;i<travellers.size();i++)
            {
                Traveller traveller = travellers.get(i);
                if(traveller.getFirstName() != null && traveller.getPhoneNumber() != null && traveller.getEmail() != null)
                {

                    if(traveller.getFirstName().equalsIgnoreCase(name) && traveller.getPhoneNumber().equalsIgnoreCase(mobile)
                            && traveller.getEmail().equalsIgnoreCase(email))
                    {
                        travellerIid = traveller.getTravellerId();

                        return true;
                        //break;
                    }
                }
            }
        }
        return false;
    }





    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                75,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]

        mVerificationInProgress = true;
        /*mOtpLayout.setVisibility(View.VISIBLE);
        mSubmit.setVisibility(View.GONE);
        mResend.setVisibility(View.VISIBLE);*/
        if(dialog != null && dialog.isShowing())
        {
            dialog.dismiss();
        }
        Toast.makeText(HotelDetailsHourlyActivity.this, "OTP sent successfully", Toast.LENGTH_SHORT).show();

    }


    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);
        // ForceResendingToken from callbacks
        if(dialog != null && dialog.isShowing())
        {
            dialog.dismiss();
        }

        Toast.makeText(HotelDetailsHourlyActivity.this, "OTP resent successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, mVerificationInProgress);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mVerificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(dialog != null && dialog.isShowing())
                        {
                            dialog.dismiss();
                        }
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            Toast.makeText(HotelDetailsHourlyActivity.this, "Sucess verification", Toast.LENGTH_SHORT).show();
                            // [START_EXCLUDE]



                            //PhoneNumberVerficationActivity.this.finish();
                            mGuestName.setText("");
                            mGuestEmail.setText("");
                            mGuestCompany.setText("");
                            mGuestGST.setText("");





                            // [END_EXCLUDE]
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {

                                mOTP.setError("Invalid code.");
                                // [END_EXCLUDE]
                            }

                        }
                    }
                });
    }




    public int getGstPrice(int sellRate)
    {
        double gstamount = 0;
        if(sellRate <= 999.99)
        {
            //System.out.println("0%");

            gstamount = (sellRate * 0)/100;

        }
        else if(sellRate >= 1000 && sellRate <= 2499.99)
        {

            gstamount = (sellRate * 12)/100;

            //System.out.println("12%");
        }
        else if(sellRate >= 2500 && sellRate <= 7499.99)
        {

            gstamount = (sellRate * 18)/100;

            //System.out.println("18%");
        }
        else if(sellRate >= 7500)
        {

            gstamount = (sellRate * 28)/100;

            //System.out.println("28%");
        }

        return (int)Math.round(gstamount);
    }

    private int getDays() {
        //System.out.println("Text=="+checkinDate+" Date"+checkoutDate);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date fd = null,td = null;
        try {
            fd = sdf.parse(checkInDate);
            //System.out.println("Text=="+book_from_date.getText().toString()+" Date"+fd);
            td = sdf.parse(checkoutDate);
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


    private void addTraveler(final String type){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("please wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        final Traveller dto = new Traveller();
        dto.setFirstName(mGuestName.getText().toString());
        dto.setPhoneNumber(mGuestMobile.getText().toString());
        dto.setEmail(mGuestEmail.getText().toString());

        if(mGuestCompany.getText().toString()==null||mGuestCompany.getText().toString().isEmpty()){

            dto.setCompany("");

        }else{
            dto.setCompany(""+mGuestCompany.getText().toString());
        }

        if(mGuestGST.getText().toString()==null||mGuestGST.getText().toString().isEmpty()){
            dto.setCustomerGST("");

        }else{
            dto.setCustomerGST(""+mGuestGST.getText().toString());
        }


        if(mMale.isChecked())
        {
            dto.setGender("Male");
        }
        else if(mFemale.isChecked())
        {
            dto.setGender("Female");
        }
        /*else if(mTransgender.isChecked())
        {
            dto.setGender("Transgender");
        }*/
        dto.setUserRoleId(6);

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                String auth_string = Util.getToken(HotelDetailsHourlyActivity.this);
                TravellerApi apiService = Util.getClient().create(TravellerApi.class);
                Call<Traveller> call = apiService.addTraveler(auth_string,dto);

                call.enqueue(new Callback<Traveller>() {
                    @Override
                    public void onResponse(Call<Traveller> call, Response<Traveller> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                        int statusCode = response.code();
                        if (statusCode == 200 || statusCode == 201 || statusCode == 203 || statusCode == 204) {

                            if (progressDialog!=null)
                                progressDialog.dismiss();

                            Traveller dto = response.body();
                            System.out.println("Response Traveller==="+response.body());

                            if (dto != null) {

                                travellerIid = dto.getTravellerId();
                                booking(type);

                            }



                        }else {
                            if (progressDialog!=null)
                                progressDialog.dismiss();
                            Toast.makeText(HotelDetailsHourlyActivity.this, " failed due to : "+response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Traveller> call, Throwable t) {
                        // Log error here since request failed
                        if (progressDialog!=null)
                            progressDialog.dismiss();
                        Log.e("TAG", t.toString());
                    }
                });
            }


        });
    }


    public void booking(String type){
        bookings = new Bookings1();
        String adults="";
        String childs="";
        String roomCount="";

        try {
            String bookingnumber = randomByDate();
            bookings.setBookingNumber(bookingnumber);

            SimpleDateFormat sdfs = new SimpleDateFormat("E,d MMMM yyyy");
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");



            if(room != null)
            {
                String[] s = room.split(",");
                adults = (s[1]);
                childs = (s[2]);
                roomCount = (s[0]);
                //mNoOfGuest.setText((Integer.parseInt(s[1]))+" Guest");
            }


            bookings.setTravellerId(travellerIid);
            bookings.setCheckInDate(checkInDate);
            bookings.setOptCheckInDate(checkInDate);
            bookings.setCheckOutDate(checkoutDate);
            bookings.setOptCheckOutDate(checkoutDate);
            bookings.setCheckInTime(CheckInTime);
            bookings.setCheckOutTime(CheckOutTime);
            bookings.setHotelId(hotelDetailseResponse.getHotelId());
            if(PreferenceHandler.getInstance(HotelDetailsHourlyActivity.this).getUserRoleUniqueID().equalsIgnoreCase("Luci-Agent"))
            {
                bookings.setCommissionAmount((int)commissionAmt);
            }
            else
            {
                bookings.setCommissionAmount(0);
            }

           /* double diff = displayprice - price;
            double div = diff/displayprice;
            double disc = div*100;*/

            DecimalFormat numberFormat = new DecimalFormat("#.##");



            if(adults!=null&&!adults.isEmpty()){
                if(adults.contains(" ")){
                    String adult[] = adults.split(" ");
                    bookings.setNoOfAdults(Integer.parseInt(adult[0]));
                }else{
                    bookings.setNoOfAdults(Integer.parseInt(adults));
                }
            }

            if(childs!=null&&!childs.isEmpty()){
                if(childs.contains(" ")){
                    String child[] = childs.split(" ");
                    bookings.setNoOfChild(Integer.parseInt(child[0]));
                }else{
                    bookings.setNoOfChild(Integer.parseInt(childs));
                }
            }

            if(roomCount!=null&&!roomCount.isEmpty()){
                if(roomCount.contains(" ")){
                    String rooms[] = roomCount.split(" ");
                    bookings.setNoOfRooms(Integer.parseInt(rooms[0]));
                }else{
                    bookings.setNoOfRooms(Integer.parseInt(roomCount));
                }
            }else{
                bookings.setNoOfRooms(1);
            }

            /*if(mRoomType.getText().toString()!=null&&!mRoomType.getText().toString().isEmpty()){
                bookings.setRoomCategory(mRoomType.getText().toString());
            }*/

            bookings.setRoomCategory("Deluxe");
            bookings.setDurationOfStay(getDays());
            bookings.setBookingStatus("Quick");
            bookings.setDeclaredRate(displayprice);

            if(price <= 999.99)
            {
                //System.out.println("0%");
                bookings.setGst(0);
                //gstamount = (sellRate * 0)/100;

            }
            else if(price >= 1000 && price <= 2499.99)
            {

                //gstamount = (sellRate * 12)/100;
                bookings.setGst(12);
                //System.out.println("12%");
            }
            else if(price >= 2500 && price <= 7499.99)
            {

                //gstamount = (sellRate * 18)/100;
                bookings.setGst(18);
                //System.out.println("18%");
            }
            else if(price >= 7500) {
                bookings.setGst(28);
                //gstamount = (sellRate * 28)/100;
            }
            //


            if(hotelRate.contains(" ")){
                String sell[] = hotelRate.split(" ");
                bookings.setSellRate((int) Double.parseDouble(sell[1]));
                // bookings.setDeclaredRate((int) Double.parseDouble(sell[1]));
            }else{
                bookings.setSellRate((int) Double.parseDouble(hotelRate));
                //bookings.setDeclaredRate((int) Double.parseDouble(mHotelRate.getText().toString()));
            }

            if(hotelGst.contains(" ")){
                String gst[] = hotelGst.split(" ");
                bookings.setGstAmount((int) Double.parseDouble(gst[1]));
            }else{
                bookings.setGstAmount((int) Double.parseDouble(hotelGst));
            }


       /* String [] arrOfStr = gst.split("%", 2);
        int value = Integer.parseInt(arrOfStr[0]);
        bookings.setGst(value);*/
            if(mHotelTotalCharges.getText().toString().contains(" ")){
                String total[] = mHotelTotalCharges.getText().toString().split(" ");
                bookings.setTotalAmount((int) Double.parseDouble(total[1]));
                if(type.equalsIgnoreCase("paylater")){
                    bookings.setBalanceAmount((int) Double.parseDouble(total[1]));
                }else{
                    bookings.setBalanceAmount(0);
                }
            }else{
                bookings.setTotalAmount((int) Double.parseDouble(mHotelTotalCharges.getText().toString()));
                if(type.equalsIgnoreCase("paylater")){
                    bookings.setBalanceAmount((int) Double.parseDouble(mHotelTotalCharges.getText().toString()));
                }else{
                    bookings.setBalanceAmount(0);
                }
            }

            // bookings.setBalanceAmount((int) Double.parseDouble(total));
            //bookings.setBookingPlan(mBookingPlan.getSelectedItem().toString());

            //Current time and date for booking

            long date = System.currentTimeMillis();

            //  SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            String bookingDate = sdf.format(date);
            System.out.println("Booking Date===" + bookingDate);
            bookings.setBookingDate(bookingDate);

            SimpleDateFormat sdft = new SimpleDateFormat("hh:mm a");
            Date d = new Date();
            String time = sdft.format(d);

            bookings.setBookingTime(time);
            bookings.setTravellerAgentId(PreferenceHandler.getInstance(HotelDetailsHourlyActivity.this).getUserId());
            if(book){
                updateRoomBooking(bookings,type);
                book = false;
            }else{
                Toast.makeText(HotelDetailsHourlyActivity.this,"Booking done successfully",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HotelDetailsHourlyActivity.this,BookingDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Booking",bookings);
                intent.putExtras(bundle);
                startActivity(intent);
            }

        }catch (Exception e){
            e.printStackTrace();
            System.out.println(" Error: "+e.getMessage());
        }
    }


    public void updateRoomBooking(final Bookings1 booking,final String type){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("please wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();

//        travellersList.add(dto);
        // bookings = new Bookings1();
        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                BookingApi bookingApi = Util.getClient().create(BookingApi.class);
                String auth = Util.getToken(HotelDetailsHourlyActivity.this);
                Call<Bookings1> bookingresponse = bookingApi.postBooking(auth,booking);

                bookingresponse.enqueue(new Callback<Bookings1>() {
                    @Override
                    public void onResponse(Call<Bookings1> call, Response<Bookings1> response) {

                        if(response.code() == 200 || response.code() == 201)
                        {
                            if(progressDialog != null)
                            {
                                progressDialog.dismiss();
                            }

                            if(response.body() != null)
                            {
                                bookings = response.body();

                                if(type.equalsIgnoreCase("paylater")){
                                    HotelNotification notify = new HotelNotification();
                                    notify.setHotelId(bookings.getHotelId());
                                    //System.out.println("roomids = "+roomids);
                                    // notify.setMessage(bookings.getProfileId()+","+bookings.getCheckInDate()+","+bookings.getCheckOutDate()+","+bookings.getTotalAmount()+","+room);
                                    notify.setMessage("Congrats! "+hotelDetailseResponse.getHotelDisplayName()+" got one new booking for "+getDays() +" nights from "+bookings.getCheckInDate()+
                                            " to "+bookings.getCheckOutDate()+"\nBooking Number:"+bookings.getBookingNumber()+","+bookings.getProfileId());
                                    notify.setTitle("New Booking from Zingo Hotels");
                                    notify.setSenderId(Constants.senderId);
                                    notify.setServerId(Constants.serverId);
                                    sendfirebaseNotification(notify,bookings);

                                    HotelNotification notifyprofile = new HotelNotification();
                                    notifyprofile.setProfileId(107);
                                    //System.out.println("roomids = "+roomids);
                                    // notify.setMessage(bookings.getProfileId()+","+bookings.getCheckInDate()+","+bookings.getCheckOutDate()+","+bookings.getTotalAmount()+","+room);
                                    notifyprofile.setMessage("Congrats! "+hotelDetailseResponse.getHotelDisplayName()+" got one new booking for "+getDays() +" nights from "+bookings.getCheckInDate()+
                                            " to "+bookings.getCheckOutDate()+"\nBooking Number:"+bookings.getBookingNumber()+","+bookings.getProfileId());
                                    notifyprofile.setTitle("New Booking from Zingo Hotels");
                                    notifyprofile.setSenderId(Constants.bill_senderId);
                                    notifyprofile.setServerId(Constants.bill_serverID);
                                    sendNotificationByProfileId(notifyprofile,bookings);

                                }else{
                                    addPayment();
                                }






                            }
                        }
                        else
                        {
                            if(progressDialog != null)
                            {
                                progressDialog.dismiss();
                            }

                            Toast.makeText(HotelDetailsHourlyActivity.this,"Please try after some time",Toast.LENGTH_SHORT).show();
                        }
                    }


                    @Override
                    public void onFailure(Call<Bookings1> call, Throwable throwable) {
                        if(progressDialog != null)
                        {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(HotelDetailsHourlyActivity.this,"Please try after some time",Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });



    }


    public void addPayment(){
       /* String paymentName = "Room Payment";
     
      
        String paymentPrice = mAmount.getText().toString();
        String paymentMode = "Online";
        
        */
        final Payment payment = new Payment();
        if(mHotelTotalCharges.getText().toString().contains(" ")){
            String total[] = mHotelTotalCharges.getText().toString().split(" ");
            payment.setAmount((int) Double.parseDouble(total[1]));

        }else{
            payment.setAmount((int) Double.parseDouble(mHotelTotalCharges.getText().toString()));

        }
        payment.setPaymentName("Room Payment");
        payment.setPaymentType("Online");


        payment.setBookingId(bookings.getBookingId());
        SimpleDateFormat date = new SimpleDateFormat("MM/dd/yyyy");
        payment.setPaymentDate(date.format(new Date()));
        final ProgressDialog dialog = new ProgressDialog(HotelDetailsHourlyActivity.this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                String auth_string = Util.getToken(HotelDetailsHourlyActivity.this);//"Basic " +  Base64.encodeToString(authentication.getBytes(), Base64.NO_WRAP);
                PaymentApi paymentApi = Util.getClient().create(PaymentApi.class);

                Call<Payment> response = paymentApi.addPayment(auth_string,payment);
                response.enqueue(new Callback<Payment>() {
                    @Override
                    public void onResponse(Call<Payment> call, Response<Payment> response) {

                        if(dialog != null && dialog.isShowing())
                        {
                            dialog.dismiss();
                        }
                        Payment serviceResponse = response.body();

                        if(response.code() == 200 && serviceResponse != null)
                        {

                            //Toast.makeText(HotelDetailsHourlyActivity.this,"Payment added successfully",Toast.LENGTH_LONG).show();
                            HotelNotification notify = new HotelNotification();
                            notify.setHotelId(bookings.getHotelId());
                            //System.out.println("roomids = "+roomids);
                            // notify.setMessage(bookings.getProfileId()+","+bookings.getCheckInDate()+","+bookings.getCheckOutDate()+","+bookings.getTotalAmount()+","+room);
                            notify.setMessage("Congrats! "+hotelDetailseResponse.getHotelDisplayName()+" got one new booking for "+getDays() +" nights from "+bookings.getCheckInDate()+
                                    " to "+bookings.getCheckOutDate()+"\nBooking Number:"+bookings.getBookingNumber()+","+bookings.getProfileId());
                            notify.setTitle("New Booking from Zingo Hotels");
                            notify.setSenderId(Constants.senderId);
                            notify.setServerId(Constants.serverId);
                            sendfirebaseNotification(notify,bookings);

                            HotelNotification notifyprofile = new HotelNotification();
                            notifyprofile.setProfileId(107);
                            //System.out.println("roomids = "+roomids);
                            // notify.setMessage(bookings.getProfileId()+","+bookings.getCheckInDate()+","+bookings.getCheckOutDate()+","+bookings.getTotalAmount()+","+room);
                            notifyprofile.setMessage("Congrats! "+hotelDetailseResponse.getHotelDisplayName()+" got one new booking for "+getDays() +" nights from "+bookings.getCheckInDate()+
                                    " to "+bookings.getCheckOutDate()+"\nBooking Number:"+bookings.getBookingNumber()+","+bookings.getProfileId());
                            notifyprofile.setTitle("New Booking from Zingo Hotels");
                            notifyprofile.setSenderId(Constants.bill_senderId);
                            notifyprofile.setServerId(Constants.bill_serverID);
                            sendNotificationByProfileId(notifyprofile,bookings);

                        }
                        else {

                            Toast.makeText(HotelDetailsHourlyActivity.this,"Please try after some time",Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<Payment> call, Throwable t) {
                        System.out.println("onFailure");
                        Toast.makeText(HotelDetailsHourlyActivity.this,"Please try after some time",Toast.LENGTH_SHORT).show();
                        if(dialog != null && dialog.isShowing())
                        {
                            dialog.dismiss();
                        }
                    }
                });

            }
        });



    }

    private void sendNotificationByProfileId(final HotelNotification notification, final Bookings1 bookings1) {

        /*final ProgressDialog dialog = new ProgressDialog(HotelDetailsHourlyActivity.this);
        dialog.setMessage(getResources().getString(R.string.loader_message));
        dialog.setCancelable(false);
        dialog.show();*/

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("Hotel id = "+notification.getHotelId());
                String auth_string = Util.getToken(HotelDetailsHourlyActivity.this);//"Basic " +  Base64.encodeToString(authentication.getBytes(), Base64.NO_WRAP);
                NotificationApi travellerApi = Util.getClient().create(NotificationApi.class);
                Call<ArrayList<String>> response = travellerApi.sendnotificationToProfile(auth_string,notification);

                response.enqueue(new Callback<ArrayList<String>>() {
                    @Override
                    public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {

                        /*if(dialog != null)
                        {
                            dialog.dismiss();
                        }*/
                        System.out.println(response.code());
                        if(response.code() == 200)
                        {
                            if(response.body() != null)
                            {
                                /*Toast.makeText(HotelDetailsHourlyActivity.this,"Thank you for selecting room. Your request has been sent to hotel. " +
                                        "Please wait for there reply.",Toast.LENGTH_SHORT).show();*/
                                //SelectRoom.this.finish();
                                /*Toast.makeText(HotelDetailsHourlyActivity.this,"Booking done successfully",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(HotelDetailsHourlyActivity.this,BookingDetailsActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("Booking",bookings1);
                                intent.putExtras(bundle);
                                startActivity(intent);*/

                                /*NotificationManager nf = new NotificationManager();
                                nf.setNotificationText(notification.getTitle());
                                nf.setNotificationFor(notification.getMessage());
                                nf.setHotelId(notification.getHotelId());
                                savenotification(nf,bookings1);*/
                                Log.d("Notification","Sent to bill");


                            }
                        }
                        else
                        {
                            Log.d("Notification","not Sent to bill");
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<String>> call, Throwable t) {
                        /*if(dialog != null)
                        {
                            dialog.dismiss();
                        }*/
                        Toast.makeText(HotelDetailsHourlyActivity.this, "Notification sent failed due to "+t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    private void sendfirebaseNotification(final HotelNotification notification, final Bookings1 bookings1) {

        final ProgressDialog dialog = new ProgressDialog(HotelDetailsHourlyActivity.this);
        dialog.setMessage(getResources().getString(R.string.loader_message));
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("Hotel id = "+notification.getHotelId());
                String auth_string = Util.getToken(HotelDetailsHourlyActivity.this);//"Basic " +  Base64.encodeToString(authentication.getBytes(), Base64.NO_WRAP);
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
                                /*Toast.makeText(HotelDetailsHourlyActivity.this,"Thank you for selecting room. Your request has been sent to hotel. " +
                                        "Please wait for there reply.",Toast.LENGTH_SHORT).show();*/
                                //SelectRoom.this.finish();
                                /*Toast.makeText(HotelDetailsHourlyActivity.this,"Booking done successfully",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(HotelDetailsHourlyActivity.this,BookingDetailsActivity.class);
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
                    }

                    @Override
                    public void onFailure(Call<ArrayList<String>> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        Toast.makeText(HotelDetailsHourlyActivity.this, "Notification sent failed due to "+t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    private void savenotification(final NotificationManager notification, final Bookings1 bookings1) {

        final ProgressDialog dialog = new ProgressDialog(HotelDetailsHourlyActivity.this);
        dialog.setMessage(getResources().getString(R.string.loader_message));
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("Hotel id = "+notification.getHotelId());
                String auth_string = Util.getToken(HotelDetailsHourlyActivity.this);
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
                                /*Toast.makeText(HotelDetailsHourlyActivity.this,"Thank you for selecting room. Your request has been sent to hotel. " +
                                        "Please wait for there reply.",Toast.LENGTH_SHORT).show();*/
                                //SelectRoom.this.finish();

                               /* if(mZingoCash.isChecked()){
                                    updateProfileOther(amount,bookings1);
                                }else {

                                }*/


                                Toast.makeText(HotelDetailsHourlyActivity.this,"Booking done successfully",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(HotelDetailsHourlyActivity.this,BookingDetailsActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("Booking",bookings1);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                //Toast.makeText(HotelDetailsHourlyActivity.this, "Save Notification", Toast.LENGTH_SHORT).show();




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


    //Generate BookingNumber
    public String randomByDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date random = new Date();
        String bookNumber = dateFormat.format(random);
        System.out.println(""+bookNumber);
        return ""+bookNumber;
    }


    public void startPayment() {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();
        double amount=0;

        try {
            JSONObject options = new JSONObject();
            if(mHotelTotalCharges.getText().toString().contains(" ")){
                String total[] = mHotelTotalCharges.getText().toString().split(" ");
                amount = Double.parseDouble(total[1]);
            }else{
                amount = Double.parseDouble(mHotelTotalCharges.getText().toString());
            }
            //int amount = Integer.parseInt(mHotelTotalCharges.getText().toString());
            options.put("name", mHotelName.getText().toString() );
            options.put("description", "For  "+mGuestName.getText().toString());
            //You can omit the image option to fetch the image from dashboard
            //options.put("image", R.drawable.app_logo);
            //options.put("currency", "INR");
            options.put("amount",amount * 100);
            //options.put("amount",1 * 100);

            JSONObject preFill = new JSONObject();
            preFill.put("email", mGuestEmail.getText().toString());
            preFill.put("contact",mGuestMobile.getText().toString() );

            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }



    @SuppressWarnings("unused")
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {

            //addPayment();
            Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
            /*if(travellerIid==0){
                addTraveler("paynow");
            }else{
                updateTraveller("paynow");
            }*/

            if(travellerIid==0){
                addTraveler("paynow");
                //System.out.println("Not exist "+isexist(tlist));
            }else if(tlist != null && !isexist(tlist)){
                //updateTraveller("paynow");
                //System.out.println("Not exist "+isexist(tlist));
                addTraveler("paylater");
            }
            else
            {
                booking("paynow");
            }

            //addTraveler();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentSuccess", e);
        }
    }


    @SuppressWarnings("unused")
    @Override
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(this, "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
        }
    }


}
