package app.zingo.com.agentapp.Activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import app.zingo.com.agentapp.Adapter.CategorisedRoomListAdapter;
import app.zingo.com.agentapp.Adapter.HotelDetailsAdapter;
import app.zingo.com.agentapp.Adapter.ImageAdapter;
import app.zingo.com.agentapp.Adapter.ListItemAdapter;
import app.zingo.com.agentapp.Adapter.ViewPagerAdapter;
import app.zingo.com.agentapp.CustomViews.CustomFontTextView;
import app.zingo.com.agentapp.CustomViews.CustomGridView;
import app.zingo.com.agentapp.Model.AgentHotel;
import app.zingo.com.agentapp.Model.HotelDetails;
import app.zingo.com.agentapp.Model.HotelService;
import app.zingo.com.agentapp.R;
import app.zingo.com.agentapp.Utils.Constants;
import app.zingo.com.agentapp.Utils.ThreadExecuter;
import app.zingo.com.agentapp.Utils.Util;
import app.zingo.com.agentapp.WebApi.HotelOperations;
import at.blogc.android.views.ExpandableTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ZingoHotels.com on 15-01-2018.
 */

public class HotelDetailsActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener{

    ViewPager viewPager,mHotelDetailViewPager;
    CustomGridView gridView;
    CustomFontTextView mHotelName,mHotelLocality,mHotelPolicy,ReadPolicy;
    ExpandableTextView mHotelDetailsMoreInfo;
    TextView mToggleExpand,mSelectedRooms,mNoOfGuest;
    TabLayout mTabLayout;
    private GoogleMap mMap;
    MapView mapView;
    TextView mReadMoreHotelPolicy,mCheckInDate,mCheckOutDate,
            mSelectRoomBtn,mHotelNameTitle;
    LinearLayout mCheckOutDateSelect,mCheckInDateSelect,mSelectNoGuestAndRooms,mGoToHotelLocation,mGotoPhotos,mGotoFacilities;
    Toolbar mBackBtn;
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.hotel_details_activity_layout);


        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        simpleDateFormat = new SimpleDateFormat("dd MMM");

        mBackBtn = (Toolbar) findViewById(R.id.hotel_details_back_btn);
      //  dots = (LinearLayout) findViewById(R.id.dots_layout);

        //mReadMoreHotelPolicy = (TextView) findViewById(R.id.read_more_about_hotel);
        mCheckInDate = (TextView) findViewById(R.id.hotel_details_check_in_date);
        mHotelNameTitle = (TextView) findViewById(R.id.hotel_name_title);
        mHotelName = (CustomFontTextView) findViewById(R.id.hotel_details_hotel_address);
        mHotelLocality = (CustomFontTextView) findViewById(R.id.hotel_locality);
        mHotelPolicy = (CustomFontTextView) findViewById(R.id.hotel_policy);
        ReadPolicy = (CustomFontTextView) findViewById(R.id.read_hotel_policy_more);
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


        if(checkInDate != null && checkoutDate != null)
        {
            CategorisedRoomListAdapter adapter = new CategorisedRoomListAdapter(HotelDetailsActivity.this,checkInDate,checkoutDate,displayprice,sellprice,room);
            //CategorisedRoomListAdapter adapter = new CategorisedRoomListAdapter(HotelDetailsActivity.this,checkInDate,checkoutDate,room);
            mHotelCategoryList.setAdapter(adapter);
        }

        mGoToHotelLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(agentHotel.getMaps().size()!=0){
                    Intent intent = new Intent(HotelDetailsActivity.this,HotelMapActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("HotelName",agentHotel.getHotelName());
                    bundle.putSerializable("Map",agentHotel.getMaps());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else{
                    Toast.makeText(HotelDetailsActivity.this, "No Map Location for this hotel", Toast.LENGTH_SHORT).show();
                }

            }
        });

        mGotoPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HotelDetailsActivity.this,HotelImagesList.class);
                intent.putExtra("HotelId",agentHotel.getHotelId());
                startActivity(intent);
            }
        });

        /*viewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HotelDetailsActivity.this,HotelImagesList.class);
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

        ReadPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(agentHotel.getPolicies().size()!=0){
                    showAlertPolicy(agentHotel);
                }else{
                    Toast.makeText(HotelDetailsActivity.this, "This hotel do not have any policies", Toast.LENGTH_SHORT).show();
                }

            }
        });

        getHotelDetails(hotelId);
       /* mTabLayout = (TabLayout) findViewById(R.id.hotel_details_tabLayout);
        mTabLayout.setTabGravity(TabLayout.MODE_FIXED);*/
        //mHotelDetailViewPager = (ViewPager) findViewById(R.id.hotel_details_pager);
        //mapView = (MapView) findViewById(R.id.hotel_details_hotel_location);

        /*mapView.onCreate(savedInstanceState);
        mapView.onResume();

        try {
            MapsInitializer.initialize(HotelDetailsActivity.this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;


                if (ActivityCompat.checkSelfPermission(HotelDetailsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(HotelDetailsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

        /*ViewPagerAdapter hotelImagesadapter = new ViewPagerAdapter(MoreHotelDetailsActivity.this,hotelImagesArraylist);
        viewPager.setAdapter(hotelImagesadapter);*/

        /*GridViewAdapter facilityAdapter = new GridViewAdapter(HotelDetailsActivity.this,hotelFacilitiesArraylist);
        gridView.setAdapter(facilityAdapter);*/

        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HotelDetailsActivity.this.finish();
            }
        });
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
                /*Intent intent = new Intent(HotelDetailsActivity.this,SelectRoomActivity.class);
                startActivity(intent);*/
            }
        });

        /*mSelectRoomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HotelDetailsActivity.this,SelectRoomBasedOnCategoryActivity.class);
                startActivity(intent);
            }
        });*/


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

        final ProgressDialog dialog = new ProgressDialog(HotelDetailsActivity.this);
        dialog.setMessage(getResources().getString(R.string.loader_message));
        dialog.setCancelable(false);
        dialog.show();
        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                String auth_string = Util.getToken(HotelDetailsActivity.this);//"Basic " +  Base64.encodeToString(authentication.getBytes(), Base64.NO_WRAP);
                HotelOperations hotelOperation = Util.getClient().create(HotelOperations.class);
                Call<AgentHotel> response = hotelOperation.getAgentHotelByHotelId(auth_string,hotelId);

                response.enqueue(new Callback<AgentHotel>() {
                    @Override
                    public void onResponse(Call<AgentHotel> call, Response<AgentHotel> response) {
                        System.out.println("GetHotelByProfileId = "+response.code());
                        AgentHotel hotelDetailseResponse = response.body();
                        if(dialog != null && dialog.isShowing())
                        {
                            dialog.dismiss();
                        }
                        if(response.code() == 200)
                        {
                            if(hotelDetailseResponse != null )
                            {
                                agentHotel = hotelDetailseResponse;



                                mHotelNameTitle.setText(agentHotel.getHotelName());
                                mHotelName.setText(agentHotel.getHotelName());
                                if(agentHotel.getMaps()!=null&&agentHotel.getMaps().size()!=0){
                                    mHotelLocality.setText(agentHotel.getMaps().get(0).getLocation());
                                }else{
                                    mHotelLocality.setText(agentHotel.getHotelStreetAddress()+","+agentHotel.getLocalty());
                                }

                                if(agentHotel.getPolicies().size()!=0){
                                    String[] s = agentHotel.getPolicies().get(0).getHotelPolicy().split(",");
                                    if(s.length > 1)
                                    {
                                        mHotelPolicy.setText(s[0]+"\n"+s[1]);
                                    }
                                    else
                                    {
                                        mHotelPolicy.setText(s[0]);
                                    }
                                }



                                    if(agentHotel.getHotelImage().size()!=0){
                                        ImageAdapter hotelImagesadapter = new ImageAdapter(HotelDetailsActivity.this,agentHotel.getHotelImage(),agentHotel.getHotelId(),"HotelDetails");
                                        viewPager.setAdapter(hotelImagesadapter);

                                    }else{
                                        ViewPagerAdapter hotelImagesadapter = new ViewPagerAdapter(HotelDetailsActivity.this,hotelImagesArraylist,agentHotel.getHotelId(),"HotelDetails");
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
                            Toast.makeText(HotelDetailsActivity.this,"Check your internet connection or please try after some time",
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
                        Toast.makeText(HotelDetailsActivity.this,"Check your internet connection or please try after some time",
                                Toast.LENGTH_LONG).show();

                    }
                });
            }
        });
    }

    private void showAlert(AgentHotel details) {

        if(details != null && details.getAminetiesList() != null && details.getAminetiesList().size() != 0)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(HotelDetailsActivity.this);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = (View) inflater.inflate(R.layout.facilities_alertbox_layout,null);

            ListView itemsList = (ListView) v.findViewById(R.id.facilities_list);
            itemsList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);


            final ListItemAdapter adapter = new ListItemAdapter(HotelDetailsActivity.this,agentHotel.getAminetiesList());

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
            Toast.makeText(HotelDetailsActivity.this,"No amenities are there",Toast.LENGTH_SHORT).show();
        }

    }

    private void showAlertPolicy(AgentHotel details) {

        if(details != null && details.getAminetiesList() != null && details.getAminetiesList().size() != 0)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(HotelDetailsActivity.this);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = (View) inflater.inflate(R.layout.alert_box_policy,null);

            TextView policyDetails = (TextView) v.findViewById(R.id.policy_details);

            policyDetails.setText("Hotel Policy: \n\n"+agentHotel.getPolicies().get(0).getHotelPolicy()+"\n\n\nCancellation Policy:\n\n"
                    +agentHotel.getPolicies().get(0).getStandardCancellationPolicy()+"\n\n\nService Policy:\n\n"
                    +agentHotel.getPolicies().get(0).getHotelServices());
            //itemsList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);


            /*final ListItemAdapter adapter = new ListItemAdapter(HotelDetailsActivity.this,details.getAminetiesList());

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
        else
        {
            Toast.makeText(HotelDetailsActivity.this,"No amenities are there",Toast.LENGTH_SHORT).show();
        }

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
                                        Toast.makeText(HotelDetailsActivity.this,"Please select future date",Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(HotelDetailsActivity.this,"Please select future date",Toast.LENGTH_SHORT).show();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(HotelDetailsActivity.this);
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
                holder.mDiscount.setText(" "+numberFormat.format(dis)+"% Discount");
                //holder.mDiscount.setText(" "+dis+"% Discount");
            }
            else
            {
                holder.mDiscount.setText(" "+0+"% Discount");
            }

            holder.mSelectedBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent intent = new Intent(context, ReviewHotelDetailsActivity.class);
                    intent.putExtra("HotelId",hotelId   );
                    intent.putExtra("Price",sellprice);
                    intent.putExtra("displayprice",displayprice);
                    intent.putExtra("CheckinDate",checkInDate);
                    intent.putExtra("CheckoutDate",checkoutDate);
                    intent.putExtra("CheckInTime",CheckInTime);
                    intent.putExtra("CheckoutTime",CheckOutTime);
                    intent.putExtra("Room",room);

                    context.startActivity(intent);
                }
            });

            holder.mDisplayPolicy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(agentHotel.getPolicies().size()!=0){
                        showAlertPolicy(agentHotel);
                    }else{
                        Toast.makeText(HotelDetailsActivity.this, "This hotel do not have any policies", Toast.LENGTH_SHORT).show();
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


            AlertDialog.Builder builder = new AlertDialog.Builder(HotelDetailsActivity.this);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = (View) inflater.inflate(R.layout.alert_box_policy,null);

            TextView policyDetails = (TextView) v.findViewById(R.id.policy_details);
            TextView title = (TextView) v.findViewById(R.id.alert_title);

            title.setText("Room Overview");
            policyDetails.setText(" "+s);
            //itemsList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);


            /*final ListItemAdapter adapter = new ListItemAdapter(HotelDetailsActivity.this,details.getAminetiesList());

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
}
