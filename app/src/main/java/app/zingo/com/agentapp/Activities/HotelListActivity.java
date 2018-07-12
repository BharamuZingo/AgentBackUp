package app.zingo.com.agentapp.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeSet;
import java.util.stream.Collectors;


import app.zingo.com.agentapp.Adapter.HotelListImageAdapter;
import app.zingo.com.agentapp.Adapter.ImageAdapter;
import app.zingo.com.agentapp.Adapter.ViewPagerAdapter;
import app.zingo.com.agentapp.CustomViews.RecyclerViewAnimator;
import app.zingo.com.agentapp.DemoActivity;
import app.zingo.com.agentapp.MainActivity;
import app.zingo.com.agentapp.Model.DataModel;
import app.zingo.com.agentapp.Model.HotelAvailablityResponse;
import app.zingo.com.agentapp.Model.HotelDetails;
import app.zingo.com.agentapp.Model.HotelNotification;
import app.zingo.com.agentapp.Model.HotelRoomAvailablity;
import app.zingo.com.agentapp.Model.HotelWithDistance;
import app.zingo.com.agentapp.Model.Maps;
import app.zingo.com.agentapp.Model.NotificationManager;
import app.zingo.com.agentapp.R;
import app.zingo.com.agentapp.Utils.Constants;
import app.zingo.com.agentapp.Utils.PreferenceHandler;
import app.zingo.com.agentapp.Utils.RecyclerToutchListener;
import app.zingo.com.agentapp.Utils.ThreadExecuter;
import app.zingo.com.agentapp.Utils.Util;
import app.zingo.com.agentapp.WebApi.BookingApi;
import app.zingo.com.agentapp.WebApi.HotelApi;
import app.zingo.com.agentapp.WebApi.MapApi;
import app.zingo.com.agentapp.WebApi.NotificationApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HotelListActivity extends AppCompatActivity {

    public LinearLayout mPreloaderList,mProgress;
    public RecyclerView mLocalHotelList,mAddedAllHotels;
    public HotelListAdapter adapter;
    TextView mDates,mGuestDetails,mModify,mNoHotels,mAllHotelsTitle;
    ArrayList<HotelDetails> hotelDetailseResponse,allHotelResponse,approvedHotels,allApprovedHotels,fillterHotels;
    public LinearLayout mSortParent,mFilterParent,mSortAndFilterParent,mNoResult;
    ScrollView mScrollView;



    String ocity,city,locality,checkInDate,checkOutDate,room,checkInTime,checkOutTime,price,activity,lowPrice,highPrice;
    Double latitude,longitude;
    ArrayList<String> hotelLocality;
    ArrayList<String> featureList;
    ArrayList<HotelWithDistance> allApprovedHotelsDistance,approvedHotelsDistance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_hotel_list2);

        /*getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);*/
        /*setTitle("Hotel List");*/

            Toolbar toolbar = (Toolbar) findViewById(R.id.hotel_list_back_btn);
            //setSupportActionBar(toolbar);
        /*getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
*/
            final Intent intent = getIntent();
            if (intent != null) {
                locality = intent.getStringExtra("Locality");
                System.out.println("Locality hotel list=="+locality);
                city = intent.getStringExtra("City");
                System.out.println("city hotel list=="+city);
                ocity = intent.getStringExtra("OriginalCity");
                checkInDate = intent.getStringExtra("CheckinDate");
                checkOutDate = intent.getStringExtra("CheckOutDate");
                checkInTime = intent.getStringExtra("CheckInTime");
                checkOutTime = intent.getStringExtra("CheckOutTime");
                price = intent.getStringExtra("Price");
                latitude = intent.getDoubleExtra("Latitude", 0.0);
                longitude = intent.getDoubleExtra("Longitude", 0.0);

                System.out.println("Looong==" + longitude + " lattt=" + latitude);
                System.out.println("CheckInTime" + checkInTime + " " + "CheckOutTime" + checkOutTime);

                room = intent.getStringExtra("Room");
                System.out.println("Pass==" + room);
                featureList = intent.getStringArrayListExtra("FeatureList");
                activity = intent.getStringExtra("Activity");
                lowPrice = intent.getStringExtra("StartPrice");
                highPrice = intent.getStringExtra("EndPrice");
            }


            //mPreloaderList = (LinearLayout) findViewById(R.id.preloader_hotel_list);
            //mProgress = (LinearLayout) findViewById(R.id.preloader_layout);
            mDates = (TextView) findViewById(R.id.guest_dates);
            mGuestDetails = (TextView) findViewById(R.id.guest_details);
            mNoHotels = (TextView) findViewById(R.id.no_hotels);
            mModify = (TextView) findViewById(R.id.modify_details);
            mAllHotelsTitle = (TextView) findViewById(R.id.all_hotels_title);
            mAllHotelsTitle.setVisibility(View.GONE);
            //mProgress = (LinearLayout) findViewById(R.id.preloader_layout);
            mLocalHotelList =  (RecyclerView)findViewById(R.id.local_hotel_list_loader);
            mLocalHotelList.setNestedScrollingEnabled(false);

            mAddedAllHotels = (RecyclerView) findViewById(R.id.jhb_local_all_hotels);
            mAddedAllHotels.setNestedScrollingEnabled(false);

            mSortParent = (LinearLayout) findViewById(R.id.sort_parent);
            mFilterParent = (LinearLayout) findViewById(R.id.filter_parent);
            mNoResult = (LinearLayout) findViewById(R.id.no_result);
            mScrollView = (ScrollView) findViewById(R.id.scrollview);
            mSortAndFilterParent = (LinearLayout) findViewById(R.id.sort_and_filter_parent);
            mSortAndFilterParent.setVisibility(View.VISIBLE);

            if (checkInDate != null && checkOutDate != null) {
                try {
                    String cid = new SimpleDateFormat("dd MMM").format(new SimpleDateFormat("MM/dd/yyyy").parse(checkInDate));
                    String cod = new SimpleDateFormat("dd MMM").format(new SimpleDateFormat("MM/dd/yyyy").parse(checkOutDate));
                    mDates.setText(cid + " to " + cod);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            if (room != null) {
                String[] s = room.split(",");
                mGuestDetails.setText(s[0] + " Rooms, " + s[1] + " Adult, " + s[2] + " Child");
            }
            //getHotels();
            /*if (activity != null) {
//                System.out.println("Feature Size=="+featureList.size());
                //filterFeature(featureList);
                getHotels(activity);

            } else {*/
                if (locality != null) {
                    if (activity != null) {
//                System.out.println("Feature Size=="+featureList.size());
                        //filterFeature(featureList);
                        getHotelsByLocalty(locality,city,activity);

                    }else{
                        getHotelsByLocalty(locality,city,"normal");
                    }

                } else {
                   // getHotels("normal");
                    Toast.makeText(HotelListActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }

          //  }


            mFilterParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //showalert();
                    Intent filter = new Intent(HotelListActivity.this, FilterActivity.class);
                    //filter.putStringArrayListExtra("LocalityName",hotelLocality);
                    filter.putExtra("Locality", locality);
                    filter.putExtra("City", city);
                    filter.putExtra("Latitude", latitude);
                    filter.putExtra("Longitude", longitude);
                    filter.putExtra("CheckinDate", checkInDate);
                    filter.putExtra("CheckOutDate", checkOutDate);
                    filter.putExtra("CheckInTime", checkInTime);
                    filter.putExtra("CheckOutTime", checkOutTime);
                    filter.putExtra("Room", room);
                    filter.putExtra("GuestDetails", "");
                    startActivity(filter);
                }
            });

            mSortParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showbottomdialog();
                }
            });

            mModify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                Intent intent1 = new Intent(HotelListActivity.this,MainActivity.class);
                startActivity(intent1);
                    HotelListActivity.this.finish();
                }
            });

            mLocalHotelList.addOnItemTouchListener(new RecyclerToutchListener(this, new RecyclerToutchListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {

                    try {
                        LinearLayout linearLayout = (LinearLayout) mLocalHotelList.getChildAt(position);
                        CardView cardView = (CardView) linearLayout.getChildAt(0);
                        //LinearLayout linearLayout1 = (LinearLayout) cardView.getChildAt(0);
                        FrameLayout frameLayout1 = (FrameLayout) cardView.getChildAt(0);
                        //LinearLayout linearLayout2 = (LinearLayout) frameLayout1.getChildAt(1);
                       // LinearLayout linearLayout3 = (LinearLayout) linearLayout2.getChildAt(1);
                        TextView textView = (TextView) frameLayout1.getChildAt(3);

                        //System.out.println(textView.getText().toString());
                        if (textView.getText().toString().equalsIgnoreCase("Sold out")) {
                            Toast.makeText(HotelListActivity.this, "Sorry! No rooms available", Toast.LENGTH_SHORT).show();
                        } else {
                            if (hotelDetailseResponse != null) {
                                final HotelDetails dto = approvedHotels.get(position);

                                System.out.println("Hotel LIst id==" + dto.getHotelId());

                                Intent intent = new Intent(HotelListActivity.this, HotelDetailsActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putInt(Constants.HOTEL_ID, dto.getHotelId());
                                bundle.putString("CheckinDate", checkInDate);
                                bundle.putString("CheckInTime", checkInTime);
                                bundle.putString("CheckOutTime", checkOutTime);
                                bundle.putString("Room", room);
                                bundle.putInt("DisplayPrice", dto.getRooms().get(0).getDisplayRate());
                                bundle.putInt("SellRate", dto.getRooms().get(0).getSellRate());
                                bundle.putString("CheckoutDate", checkOutDate);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    //System.out.println(mHotelList.getChildAt(position));
                    /*if(hotelDetailseResponse != null) {
                        final HotelDetails dto = approvedHotels.get(position);

                        System.out.println("Hotel LIst id=="+dto.getHotelId());

                        Intent intent = new Intent(HotelListActivity.this, HotelDetailsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt(Constants.HOTEL_ID, dto.getHotelId());
                        bundle.putString("CheckinDate", checkInDate);
                        bundle.putString("CheckInTime", checkInTime);
                        bundle.putString("CheckOutTime", checkOutTime);
                        bundle.putString("Room",room);
                        bundle.putInt("DisplayPrice",dto.getRooms().get(0).getDisplayRate());
                        bundle.putInt("SellRate",dto.getRooms().get(0).getSellRate());
                        bundle.putString("CheckoutDate", checkOutDate);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }*/
                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));

           mLocalHotelList.addOnScrollListener(new RecyclerView.OnScrollListener() {
               @Override
               public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                   super.onScrollStateChanged(recyclerView, newState);
                   System.out.println("Scroll Changed");
                   slideUp(mLocalHotelList);
               }

               @Override
               public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                   super.onScrolled(recyclerView, dx, dy);
                   System.out.println("Scrolling");
                   slideUp(mLocalHotelList);
               }
           });



            toolbar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                Intent intent1 = new Intent(HotelListActivity.this,MainActivity.class);
                startActivity(intent1);
                    //HotelListActivity.super.onBackPressed();
                     HotelListActivity.this.finish();
                }
            });

            mAddedAllHotels.addOnItemTouchListener(new RecyclerToutchListener(this, new RecyclerToutchListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {


                    try {
                        LinearLayout linearLayout = (LinearLayout) mAddedAllHotels.getChildAt(position);
                        CardView cardView = (CardView) linearLayout.getChildAt(0);
                        LinearLayout linearLayout1 = (LinearLayout) cardView.getChildAt(0);
                        LinearLayout linearLayout2 = (LinearLayout) linearLayout1.getChildAt(1);
                        LinearLayout linearLayout3 = (LinearLayout) linearLayout2.getChildAt(1);
                        TextView textView = (TextView) linearLayout3.getChildAt(2);

                        //System.out.println(textView.getText().toString());
                        if (textView.getText().toString().equalsIgnoreCase("Sold out")) {
                            Toast.makeText(HotelListActivity.this, "Sorry! No rooms available", Toast.LENGTH_SHORT).show();
                        } else {

                            if (allHotelResponse != null) {
                                final HotelDetails dto;

                                if (fillterHotels != null && fillterHotels.size() != 0) {
                                    dto = fillterHotels.get(position);
                                } else {
                                    dto = allApprovedHotels.get(position);
                                }

                                System.out.println("Hotel LIst id==" + dto.getHotelId());

                                Intent intent = new Intent(HotelListActivity.this, HotelDetailsActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putInt(Constants.HOTEL_ID, dto.getHotelId());
                                bundle.putString("CheckinDate", checkInDate);
                                bundle.putString("CheckoutDate", checkOutDate);
                                bundle.putString("CheckInTime", checkInTime);
                                bundle.putString("CheckOutTime", checkOutTime);
                                bundle.putInt("DisplayPrice", dto.getRooms().get(0).getDisplayRate());
                                bundle.putInt("SellRate", dto.getRooms().get(0).getSellRate());
                                bundle.putString("Room", room);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));


        }catch (Exception e){
            e.printStackTrace();
        }



    }



    public void showbottomdialog()
    {
        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(HotelListActivity.this);
        View sheetView = this.getLayoutInflater().inflate(R.layout.sort_layout, null);
        TextView mLowToHigh = (TextView)sheetView.findViewById(R.id.low_to_high);
        TextView mHighToLow = (TextView)sheetView.findViewById(R.id.high_to_low);
        TextView mDistance = (TextView)sheetView.findViewById(R.id.sort_distance);
        mBottomSheetDialog.setContentView(sheetView);
        mBottomSheetDialog.show();

        mLowToHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBottomSheetDialog != null)
                {
                    mBottomSheetDialog.dismiss();
                }

                sortByPrice("Low");
            }
        });

        mHighToLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBottomSheetDialog != null)
                {
                    mBottomSheetDialog.dismiss();
                }
                sortByPrice("High");

            }
        });

        mDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBottomSheetDialog != null)
                {
                    mBottomSheetDialog.dismiss();
                }

                sortByDistance();
            }
        });



    }



    private void getHotels(final String filterType){
        final ProgressDialog progressDialog;
        if(filterType.equalsIgnoreCase("Filter")){
             progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("please wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        }else {
            progressDialog = null;
        }

        /*final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("please wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();*/

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                String auth_string = Util.getToken(HotelListActivity.this);
                HotelApi apiService =
                        Util.getClient().create(HotelApi.class);
                Call<ArrayList<HotelDetails>> call = apiService.getHotel(auth_string);/*getRooms()*/;

                call.enqueue(new Callback<ArrayList<HotelDetails>>() {
                    @Override
                    public void onResponse(Call<ArrayList<HotelDetails>> call, Response<ArrayList<HotelDetails>> response) {
                        int statusCode = response.code();

                        if (progressDialog!=null)
                            progressDialog.dismiss();

                        try
                        {
                            if (statusCode == 200) {

                                allHotelResponse =  response.body();
                                hotelLocality = new ArrayList<>();
                                allApprovedHotelsDistance = new ArrayList<>();


                                allApprovedHotels = new ArrayList<>();
                                if(allHotelResponse != null && allHotelResponse.size() != 0)
                                {
                                    mAllHotelsTitle.setVisibility(View.VISIBLE);
                                    mSortAndFilterParent.setVisibility(View.VISIBLE);

                                    for(int i=0;i<allHotelResponse.size();i++){
                                        hotelLocality.add(allHotelResponse.get(i).getLocalty().toUpperCase());
                                        if(allHotelResponse.get(i).getApproved() )
                                        {
                                            //System.out.println("City = "+allHotelResponse.get(i).getCity());
                                            //System.out.println("City = "+allHotelResponse.get(i).getHotelId());
                                            allApprovedHotels.add(allHotelResponse.get(i));

                                        }
                                        //else if(allHotelResponse.get(i).getApproved() && (allApprovedHotels.get(i).getCity().equalsIgnoreCase(ocity))

                                    }

                                    //allApprovedHotels.removeAll(approvedHotels);

                                    if(filterType.equalsIgnoreCase("filter")){

                                        fillterHotels = new ArrayList<>();
                                        int minPrice = Integer.parseInt(lowPrice);
                                        int maxPrice = Integer.parseInt(highPrice);
                                        System.out.println("LP=="+lowPrice+" HP=="+highPrice);

                                        if(allApprovedHotels != null && allApprovedHotels.size() != 0 )
                                        {

                                            if(featureList!=null&&featureList.size()!=0){
                                                for(int i=0;i<allApprovedHotels.size();i++){

                                                    for(int j=0;j<featureList.size();j++){

                                                        if(allApprovedHotels.get(i).getHotelType().contains(featureList.get(j))){
                                                            fillterHotels.add(allApprovedHotels.get(i));
                                                            break;
                                                        }


                                                       /* if(featureList.get(j).contains(allApprovedHotels.get(i).getHotelType())){
                                                            fillterHotels.add(allApprovedHotels.get(i));
                                                            break;
                                                        }*/

                                                    }


                                                }

                                                if(fillterHotels!=null&&fillterHotels.size()!=0){

                                                    ArrayList<HotelDetails> priceHotels = new ArrayList<>();
                                                    if(minPrice==0&&maxPrice==0){
                                                        if(allApprovedHotels != null)
                                                        {
                                                            allApprovedHotels.clear();
                                                        }
                                                        allApprovedHotels = fillterHotels;
                                                        adapter = new HotelListAdapter(HotelListActivity.this, allApprovedHotels,checkInDate,checkOutDate,latitude,longitude,checkInTime,checkOutTime,room,price,"all");
                                                        mAddedAllHotels.setAdapter(adapter);
                                                    }else{

                                                        for(int i=0;i<fillterHotels.size();i++){
                                                            if(minPrice!=0&&maxPrice==0){
                                                                if(fillterHotels.get(i).getRooms().get(0).getSellRate()>=minPrice){
                                                                    priceHotels.add(fillterHotels.get(i));
                                                                    break;
                                                                }

                                                            }else if(minPrice==0&&maxPrice!=0){

                                                                if(fillterHotels.get(i).getRooms().get(0).getSellRate()<=maxPrice){
                                                                    priceHotels.add(fillterHotels.get(i));
                                                                    break;
                                                                }

                                                            }else if(minPrice!=0&&maxPrice!=0){
                                                                if(fillterHotels.get(i).getRooms().get(0).getSellRate()>=minPrice&&fillterHotels.get(i).getRooms().get(0).getSellRate()<=maxPrice){
                                                                    priceHotels.add(fillterHotels.get(i));
                                                                    break;
                                                                }
                                                            }
                                                        }

                                                        if(priceHotels!=null&&priceHotels.size()!=0){
                                                            if(allApprovedHotels != null)
                                                            {
                                                                allApprovedHotels.clear();
                                                            }
                                                            allApprovedHotels = priceHotels;
                                                            adapter = new HotelListAdapter(HotelListActivity.this, allApprovedHotels,checkInDate,checkOutDate,latitude,longitude,checkInTime,checkOutTime,room,price,"all");
                                                            mAddedAllHotels.setAdapter(adapter);
                                                        }else{

                                                            mNoResult.setVisibility(View.VISIBLE);
                                                            mScrollView.setVisibility(View.GONE);
                                                        }

                                                    }


                                                }else{

                                                    mNoResult.setVisibility(View.VISIBLE);
                                                    mScrollView.setVisibility(View.GONE);
                                                }

                                            }else{

                                                if(allApprovedHotels!=null&&allApprovedHotels.size()!=0){

                                                    ArrayList<HotelDetails> priceHotels = new ArrayList<>();
                                                    if(minPrice==0&&maxPrice==0){
                                                        adapter = new HotelListAdapter(HotelListActivity.this, allApprovedHotels,checkInDate,checkOutDate,latitude,longitude,checkInTime,checkOutTime,room,price,"all");
                                                        mAddedAllHotels.setAdapter(adapter);
                                                    }else{

                                                        for(int i=0;i<allApprovedHotels.size();i++){
                                                            if(allApprovedHotels.get(i).getRooms()!=null&&allApprovedHotels.get(i).getRooms().size()!=0){

                                                                if(minPrice!=0&&maxPrice==0){
                                                                    if(allApprovedHotels.get(i).getRooms().get(0).getSellRate()>=minPrice){
                                                                        priceHotels.add(allApprovedHotels.get(i));
                                                                    }

                                                                }else if(minPrice==0&&maxPrice!=0){

                                                                    if(allApprovedHotels.get(i).getRooms().get(0).getSellRate()<=maxPrice){
                                                                        priceHotels.add(allApprovedHotels.get(i));
                                                                    }

                                                                }else if(minPrice!=0&&maxPrice!=0){
                                                                    if(allApprovedHotels.get(i).getRooms().get(0).getSellRate()>=minPrice&&allApprovedHotels.get(i).getRooms().get(0).getSellRate()<=maxPrice){
                                                                        priceHotels.add(allApprovedHotels.get(i));
                                                                    }
                                                                }

                                                            }

                                                        }

                                                        if(priceHotels!=null&&priceHotels.size()!=0){
                                                            System.out.println("Price hotels=="+priceHotels.size());
                                                            if(allApprovedHotels != null)
                                                            {
                                                                allApprovedHotels.clear();
                                                            }
                                                            allApprovedHotels = priceHotels;
                                                            adapter = new HotelListAdapter(HotelListActivity.this, allApprovedHotels,checkInDate,checkOutDate,latitude,longitude,checkInTime,checkOutTime,room,price,"all");
                                                            mAddedAllHotels.setAdapter(adapter);
                                                        }else{

                                                            mNoResult.setVisibility(View.VISIBLE);
                                                            mScrollView.setVisibility(View.GONE);
                                                        }

                                                    }


                                                }

                                            }



                                        }

                                    }else{

                                        if(allApprovedHotels != null && allApprovedHotels.size() != 0 )
                                        {
                                            Collections.sort(allApprovedHotels, new Comparator<HotelDetails>() {
                                                @Override
                                                public int compare(HotelDetails o1, HotelDetails o2) {
                                                    return o1.getCity().compareTo(o2.getCity());
                                                }
                                            });
                                            adapter = new HotelListAdapter(HotelListActivity.this, allApprovedHotels,checkInDate,checkOutDate,latitude,longitude,checkInTime,checkOutTime,room,price,"all");
                                            mAddedAllHotels.setAdapter(adapter);
                                        }
                                        //mPreloaderList.setVisibility(View.GONE);
                                        //mProgress.setVisibility(View.GONE);
                                    }

                                   /* if(allApprovedHotels != null && allApprovedHotels.size() != 0 )
                                    {
                                        Collections.sort(allApprovedHotels, new Comparator<HotelDetails>() {
                                            @Override
                                            public int compare(HotelDetails o1, HotelDetails o2) {
                                                return o1.getCity().compareTo(o2.getCity());
                                            }
                                        });
                                        adapter = new HotelListAdapter(HotelListActivity.this, allApprovedHotels,checkInDate,checkOutDate,latitude,longitude,checkInTime,checkOutTime,room,price,"all");
                                        mAllHotels.setAdapter(adapter);
                                    }
                                    mPreloaderList.setVisibility(View.GONE);
                                    mProgress.setVisibility(View.GONE);*/
                                }
                                else
                                {
                                    //Toast.makeText(HotelListActivity.this, "No Hotels", Toast.LENGTH_SHORT).show();
                                    mNoResult.setVisibility(View.VISIBLE);
                                    mScrollView.setVisibility(View.GONE);
                                }



                            }else {
                                //mPreloaderList.setVisibility(View.GONE);
                                //mProgress.setVisibility(View.GONE);
                            if (progressDialog!=null)
                                progressDialog.dismiss();

                                mNoResult.setVisibility(View.VISIBLE);
                                mScrollView.setVisibility(View.GONE);
                                //Toast.makeText(HotelListActivity.this, " failed due to status code:"+statusCode, Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception ex)
                        {
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<HotelDetails>> call, Throwable t) {
                        // Log error here since request failed
                        /*if (progressDialog!=null)
                            progressDialog.dismiss();*/
                        //mPreloaderList.setVisibility(View.GONE);
                        //mProgress.setVisibility(View.GONE);
                        Log.e("TAG", t.toString());
                    }
                });
            }


        });
    }





    private void getHotelsByLocalty(final String locality, final String city,final String filterType){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("please wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                String auth_string = Util.getToken(HotelListActivity.this);
                HotelApi apiService =
                        Util.getClient().create(HotelApi.class);
                //Call<ArrayList<HotelDetails>> call = apiService.getHotelByLocalty(auth_string, HotelListActivity.this.locality);
                Call<ArrayList<HotelDetails>> call = apiService.getHotelByLocaltyOrCity(auth_string,locality,city);

                call.enqueue(new Callback<ArrayList<HotelDetails>>() {
                    @Override
                    public void onResponse(Call<ArrayList<HotelDetails>> call, Response<ArrayList<HotelDetails>> response) {
                        int statusCode = response.code();
                        if (progressDialog!=null)
                            progressDialog.dismiss();
                        try{
                            if (statusCode == 200) {

                                hotelDetailseResponse =  response.body();

                                approvedHotels = new ArrayList<>();
                                approvedHotelsDistance = new ArrayList<>();

                                if(hotelDetailseResponse != null && hotelDetailseResponse.size() != 0)
                                {
                                    mSortAndFilterParent.setVisibility(View.VISIBLE);


                                    for(int i=0;i<hotelDetailseResponse.size();i++)
                                    {
                                        if(hotelDetailseResponse.get(i).getApproved())
                                        {
                                            approvedHotels.add(hotelDetailseResponse.get(i));
                                        }
                                    }

                                    if(approvedHotels != null && approvedHotels.size() != 0)
                                    {

                                        if(filterType.equalsIgnoreCase("filter")){

                                            fillterHotels = new ArrayList<>();
                                            int minPrice = Integer.parseInt(lowPrice);
                                            int maxPrice = Integer.parseInt(highPrice);
                                            System.out.println("LP=="+lowPrice+" HP=="+highPrice);

                                            if(approvedHotels != null && approvedHotels.size() != 0 )
                                            {

                                                if(featureList!=null&&featureList.size()!=0){
                                                    for(int i=0;i<approvedHotels.size();i++){

                                                        for(int j=0;j<featureList.size();j++){

                                                            if(approvedHotels.get(i).getHotelType().contains(featureList.get(j))){
                                                                fillterHotels.add(approvedHotels.get(i));
                                                                break;
                                                            }


                                                       /* if(featureList.get(j).contains(allApprovedHotels.get(i).getHotelType())){
                                                            fillterHotels.add(allApprovedHotels.get(i));
                                                            break;
                                                        }*/

                                                        }


                                                    }

                                                    if(fillterHotels!=null&&fillterHotels.size()!=0){

                                                        ArrayList<HotelDetails> priceHotels = new ArrayList<>();
                                                        if(minPrice==0&&maxPrice==0){
                                                            if(approvedHotels != null)
                                                            {
                                                                approvedHotels.clear();
                                                            }
                                                            approvedHotels = fillterHotels;
                                                            adapter = new HotelListAdapter(HotelListActivity.this, approvedHotels,checkInDate,
                                                                    checkOutDate,latitude,longitude,checkInTime,checkOutTime,room,price,"filter");
                                                            mLocalHotelList.setAdapter(adapter);
                                                            recyclerAnimation();
                                                        }else{

                                                            for(int i=0;i<fillterHotels.size();i++){
                                                                if(minPrice!=0&&maxPrice==0){
                                                                    if(fillterHotels.get(i).getRooms().get(0).getSellRate()>=minPrice){
                                                                        priceHotels.add(fillterHotels.get(i));
                                                                        break;
                                                                    }

                                                                }else if(minPrice==0&&maxPrice!=0){

                                                                    if(fillterHotels.get(i).getRooms().get(0).getSellRate()<=maxPrice){
                                                                        priceHotels.add(fillterHotels.get(i));
                                                                        break;
                                                                    }

                                                                }else if(minPrice!=0&&maxPrice!=0){
                                                                    if(fillterHotels.get(i).getRooms().get(0).getSellRate()>=minPrice&&fillterHotels.get(i).getRooms().get(0).getSellRate()<=maxPrice){
                                                                        priceHotels.add(fillterHotels.get(i));
                                                                        break;
                                                                    }
                                                                }
                                                            }

                                                            if(priceHotels!=null&&priceHotels.size()!=0){
                                                                if(approvedHotels != null)
                                                                {
                                                                    approvedHotels.clear();
                                                                }
                                                                approvedHotels = priceHotels;
                                                                adapter = new HotelListAdapter(HotelListActivity.this, approvedHotels,checkInDate,checkOutDate,latitude,longitude,checkInTime,checkOutTime,room,price,"filter");
                                                                mLocalHotelList.setAdapter(adapter);
                                                                recyclerAnimation();
                                                            }else{

                                                                mNoResult.setVisibility(View.VISIBLE);
                                                                mScrollView.setVisibility(View.GONE);
                                                            }

                                                        }


                                                    }else{

                                                        mNoResult.setVisibility(View.VISIBLE);
                                                        mScrollView.setVisibility(View.GONE);
                                                    }

                                                }else{

                                                    if(approvedHotels!=null&&approvedHotels.size()!=0){

                                                        ArrayList<HotelDetails> priceHotels = new ArrayList<>();
                                                        if(minPrice==0&&maxPrice==0){
                                                            adapter = new HotelListAdapter(HotelListActivity.this, approvedHotels,checkInDate,checkOutDate,latitude,longitude,checkInTime,checkOutTime,room,price,"filter");
                                                            mLocalHotelList.setAdapter(adapter);
                                                            recyclerAnimation();
                                                        }else{

                                                            for(int i=0;i<approvedHotels.size();i++){
                                                                if(approvedHotels.get(i).getRooms()!=null&&approvedHotels.get(i).getRooms().size()!=0){

                                                                    if(minPrice!=0&&maxPrice==0){
                                                                        if(approvedHotels.get(i).getRooms().get(0).getSellRate()>=minPrice){
                                                                            priceHotels.add(approvedHotels.get(i));
                                                                        }

                                                                    }else if(minPrice==0&&maxPrice!=0){

                                                                        if(approvedHotels.get(i).getRooms().get(0).getSellRate()<=maxPrice){
                                                                            priceHotels.add(approvedHotels.get(i));
                                                                        }

                                                                    }else if(minPrice!=0&&maxPrice!=0){
                                                                        if(approvedHotels.get(i).getRooms().get(0).getSellRate()>=minPrice&&approvedHotels.get(i).getRooms().get(0).getSellRate()<=maxPrice){
                                                                            priceHotels.add(approvedHotels.get(i));
                                                                        }
                                                                    }

                                                                }

                                                            }

                                                            if(priceHotels!=null&&priceHotels.size()!=0){
                                                                System.out.println("Price hotels=="+priceHotels.size());
                                                                if(approvedHotels != null)
                                                                {
                                                                    approvedHotels.clear();
                                                                }
                                                                approvedHotels = priceHotels;
                                                                adapter = new HotelListAdapter(HotelListActivity.this, approvedHotels,checkInDate,checkOutDate,latitude,longitude,checkInTime,checkOutTime,room,price,"all");
                                                                mLocalHotelList.setAdapter(adapter);
                                                                recyclerAnimation();
                                                            }else{

                                                                mNoResult.setVisibility(View.VISIBLE);
                                                                mScrollView.setVisibility(View.GONE);
                                                            }

                                                        }


                                                    }

                                                }



                                            }

                                        }else {
                                            adapter = new HotelListAdapter(HotelListActivity.this, approvedHotels,checkInDate,
                                                    checkOutDate,latitude,longitude,checkInTime,checkOutTime,room,price,"filter");
                                            mLocalHotelList.setAdapter(adapter);
                                            recyclerAnimation();
                                        }


                                    }else{

                                        mNoHotels.setVisibility(View.VISIBLE);

                                        mNoHotels.setText("Currently, There is No hotels in this city but we are coming soon with some exciting hotels for this city");
                                    }
                                /*mPreloaderList.setVisibility(View.GONE);
                                mProgress.setVisibility(View.GONE);*/
                                   // getHotels("normal");
                                }
                                else
                                {
                                    //Toast.makeText(HotelListActivity.this, "No Hotels in your search location", Toast.LENGTH_SHORT).show();
                                   // getHotels("normal");
                                    //mPreloaderList.setVisibility(View.GONE);
                                    //mProgress.setVisibility(View.GONE);
                                    mNoHotels.setVisibility(View.VISIBLE);

                                    mNoHotels.setText("We will be coming soon");
                                }



                            }else {
                            if (progressDialog!=null)
                                progressDialog.dismiss();
                                //mPreloaderList.setVisibility(View.GONE);
                                //mProgress.setVisibility(View.GONE);
                                Toast.makeText(HotelListActivity.this, " failed due to status code:"+statusCode, Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(Call<ArrayList<HotelDetails>> call, Throwable t) {
                        // Log error here since request failed
                        if (progressDialog!=null)
                            progressDialog.dismiss();
                        //mPreloaderList.setVisibility(View.GONE);
                        //mProgress.setVisibility(View.GONE);
                        Log.e("TAG", t.toString());
                    }
                });
            }


        });
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id)
        {
            case android.R.id.home:
                Intent main = new Intent(HotelListActivity.this, MainActivity.class);
                startActivity(main);
                this.finish();
                break;

            *//*case R.id.action_filter:
                Object[] st = hotelLocality.toArray();
                for (Object s : st) {
                    if (hotelLocality.indexOf(s) != hotelLocality.lastIndexOf(s)) {
                        hotelLocality.remove(hotelLocality.lastIndexOf(s));
                    }
                }
                Intent filter = new Intent(HotelListActivity.this, FilterActivity.class);
                filter.putStringArrayListExtra("LocalityName",hotelLocality);
                startActivity(filter);
                break;*//*
        }

        return super.onOptionsItemSelected(item);
    }
*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.modify,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        try{
            switch (itemId)
            {
                case R.id.action_modify:
                    goback();
                    return true;

                case android.R.id.home:
                    // app icon action bar is clicked; go to parent activity

                    Intent main = new Intent(HotelListActivity.this, MainActivity.class);
                    startActivity(main);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goback();
    }

    private void goback()
    {
        Intent intent = new Intent(HotelListActivity.this,DemoActivity.class);
        startActivity(intent);
        HotelListActivity.this.finish();
    }

    public void sortByPrice(String type)
    {
        if(type.equalsIgnoreCase("low"))
        {
            if(approvedHotels != null && approvedHotels.size() != 0)
            {
                Collections.sort(approvedHotels, new Comparator<HotelDetails>() {
                    @Override
                    public int compare(HotelDetails o1, HotelDetails o2) {
                        if(o1.getRooms() != null && o1.getRooms().size() != 0 && o2.getRooms() != null && o2.getRooms().size() != 0)
                        {
                            return o1.getRooms().get(0).getSellRate() - o2.getRooms().get(0).getSellRate();
                        }
                        else
                        {
                            return 1;
                        }
                    }
                });

               /* if(approvedHotelsDistance != null)
                {
                    approvedHotelsDistance.clear();
                }*/
                adapter = new HotelListAdapter(HotelListActivity.this, approvedHotels,checkInDate,checkOutDate,
                        latitude,longitude,checkInTime,checkOutTime,room,price);
                mLocalHotelList.setAdapter(adapter);

               /* if(approvedHotels != null && approvedHotels.size() != 0)
                {
                    Collections.sort(approvedHotels, new Comparator<HotelDetails>() {
                        @Override
                        public int compare(HotelDetails o1, HotelDetails o2) {
                            if(o1.getRooms() != null && o1.getRooms().size() != 0 && o2.getRooms() != null && o2.getRooms().size() != 0)
                            {
                                return o1.getRooms().get(0).getSellRate() - o2.getRooms().get(0).getSellRate();
                            }
                            else
                            {
                                return 1;
                            }
                        }
                    });
                    *//*if(approvedHotelsDistance != null)
                    {
                        approvedHotelsDistance.clear();
                    }*//*
                    adapter = new HotelListAdapter(HotelListActivity.this, approvedHotels,checkInDate,
                            checkOutDate,latitude,longitude,checkInTime,checkOutTime,room,price);
                    mAddedAllHotels.setAdapter(adapter);
                }*/
            }
            /*else if(approvedHotels != null && approvedHotels.size() ==0)
            {
                if(approvedHotels != null && approvedHotels.size() != 0)
                {
                    Collections.sort(approvedHotels, new Comparator<HotelDetails>() {
                        @Override
                        public int compare(HotelDetails o1, HotelDetails o2) {
                            if(o1.getRooms() != null && o1.getRooms().size() != 0 && o2.getRooms() != null && o2.getRooms().size() != 0)
                            {
                                return o1.getRooms().get(0).getSellRate() - o2.getRooms().get(0).getSellRate();
                            }
                            else
                            {
                                return 1;
                            }
                        }
                    });
                    *//*if(approvedHotelsDistance != null)
                    {
                        approvedHotelsDistance.clear();
                    }*//*
                    adapter = new HotelListAdapter(HotelListActivity.this, approvedHotels,checkInDate,
                            checkOutDate,latitude,longitude,checkInTime,checkOutTime,room,price);
                    mAddedAllHotels.setAdapter(adapter);
                }
            }*/
            /*else if(approvedHotels == null && (approvedHotels !=null && approvedHotels.size() != 0))
            {
                Collections.sort(approvedHotels, new Comparator<HotelDetails>() {
                    @Override
                    public int compare(HotelDetails o1, HotelDetails o2) {
                        if(o1.getRooms() != null && o1.getRooms().size() != 0 && o2.getRooms() != null && o2.getRooms().size() != 0)
                        {
                            return o1.getRooms().get(0).getSellRate() - o2.getRooms().get(0).getSellRate();
                        }
                        else
                        {
                            return 1;
                        }
                    }
                });
                *//*if(approvedHotelsDistance != null)
                {
                    approvedHotelsDistance.clear();
                }*//*
                adapter = new HotelListAdapter(HotelListActivity.this, approvedHotels,checkInDate,
                        checkOutDate,latitude,longitude,checkInTime,checkOutTime,room,price);
                mAddedAllHotels.setAdapter(adapter);
            }*/
        }

        else if(type.equalsIgnoreCase("high"))
        {
            if(approvedHotels != null && approvedHotels.size() != 0)
            {
                Collections.sort(approvedHotels, new Comparator<HotelDetails>() {
                    @Override
                    public int compare(HotelDetails o1, HotelDetails o2) {
                        if(o1.getRooms() != null && o1.getRooms().size() != 0 && o2.getRooms() != null && o2.getRooms().size() != 0)
                        {
                            return o2.getRooms().get(0).getSellRate() - o1.getRooms().get(0).getSellRate();
                        }
                        else
                        {
                            return 1;
                        }
                    }
                });
                /*if(approvedHotelsDistance != null)
                {
                    approvedHotelsDistance.clear();
                }*/

                adapter = new HotelListAdapter(HotelListActivity.this, approvedHotels,checkInDate,
                        checkOutDate,latitude,longitude,checkInTime,checkOutTime,room,price);
                mLocalHotelList.setAdapter(adapter);

                /*if(approvedHotels != null && approvedHotels.size() != 0)
                {
                    Collections.sort(approvedHotels, new Comparator<HotelDetails>() {
                        @Override
                        public int compare(HotelDetails o1, HotelDetails o2) {
                            if(o1.getRooms() != null && o1.getRooms().size() != 0 && o2.getRooms() != null && o2.getRooms().size() != 0)
                            {
                                return o2.getRooms().get(0).getSellRate() - o1.getRooms().get(0).getSellRate();
                            }
                            else
                            {
                                return 1;
                            }
                        }
                    });
                    *//*if(approvedHotelsDistance != null)
                    {
                        approvedHotelsDistance.clear();
                    }*//*
                    adapter = new HotelListAdapter(HotelListActivity.this, approvedHotels,checkInDate,
                            checkOutDate,latitude,longitude,checkInTime,checkOutTime,room,price);
                    mAddedAllHotels.setAdapter(adapter);
                }*/
            }
            /*else if(approvedHotels != null && approvedHotels.size() == 0)
            {
                if(approvedHotels != null && approvedHotels.size() != 0)
                {
                    Collections.sort(approvedHotels, new Comparator<HotelDetails>() {
                        @Override
                        public int compare(HotelDetails o1, HotelDetails o2) {
                            if(o1.getRooms() != null && o1.getRooms().size() != 0 && o2.getRooms() != null && o2.getRooms().size() != 0)
                            {
                                return o2.getRooms().get(0).getSellRate() - o1.getRooms().get(0).getSellRate();
                            }
                            else
                            {
                                return 1;
                            }
                        }
                    });
                    *//*if(approvedHotelsDistance != null)
                    {
                        approvedHotelsDistance.clear();
                    }*//*
                    adapter = new HotelListAdapter(HotelListActivity.this, approvedHotels,checkInDate,
                            checkOutDate,latitude,longitude,checkInTime,checkOutTime,room,price);
                    mAddedAllHotels.setAdapter(adapter);
                }
            }
            else if(approvedHotels == null && (approvedHotels !=null && approvedHotels.size() != 0))
            {
                Collections.sort(approvedHotels, new Comparator<HotelDetails>() {
                    @Override
                    public int compare(HotelDetails o1, HotelDetails o2) {
                        if(o1.getRooms() != null && o1.getRooms().size() != 0 && o2.getRooms() != null && o2.getRooms().size() != 0)
                        {
                            return o2.getRooms().get(0).getSellRate() - o1.getRooms().get(0).getSellRate();
                        }
                        else
                        {
                            return 1;
                        }
                    }
                });
                *//*if(approvedHotelsDistance != null)
                {
                    approvedHotelsDistance.clear();
                }*//*
                adapter = new HotelListAdapter(HotelListActivity.this, approvedHotels,checkInDate,
                        checkOutDate,latitude,longitude,checkInTime,checkOutTime,room,price);
                mAddedAllHotels.setAdapter(adapter);
            }*/

        }
    }

    private void sortByDistance() {

        if (approvedHotelsDistance != null && approvedHotelsDistance.size() != 0) {
            Collections.sort(approvedHotelsDistance, new Comparator<HotelWithDistance>() {
                @Override
                public int compare(HotelWithDistance o1, HotelWithDistance o2) {
                    if (o1.getDistance() > o2.getDistance()) {
                        return 1;
                    } else if (o1.getDistance() < o2.getDistance()) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            });
            approvedHotels.clear();
            for(int i=0;i<approvedHotelsDistance.size();i++)
            {
                approvedHotels.add(approvedHotelsDistance.get(i).getHotelDetails());
            }

            adapter = new HotelListAdapter(HotelListActivity.this, approvedHotels,checkInDate,checkOutDate,
                    latitude,longitude,checkInTime,checkOutTime,room,price);
            mLocalHotelList.setAdapter(adapter);

           /* if (approvedHotelsDistance != null && approvedHotelsDistance.size() != 0) {
                Collections.sort(approvedHotelsDistance, new Comparator<HotelWithDistance>() {
                    @Override
                    public int compare(HotelWithDistance o1, HotelWithDistance o2) {
                        if (o1.getDistance() > o2.getDistance()) {
                            return 1;
                        } else if (o1.getDistance() < o2.getDistance()) {
                            return -1;
                        } else {
                            return 0;
                        }
                    }
                });

                approvedHotels.clear();
                for(int i=0;i<approvedHotelsDistance.size();i++)
                {
                    approvedHotels.add(approvedHotelsDistance.get(i).getHotelDetails());
                }
                adapter = new HotelListAdapter(HotelListActivity.this, approvedHotels,checkInDate,checkOutDate,
                        latitude,longitude,checkInTime,checkOutTime,room,price);
                mAddedAllHotels.setAdapter(adapter);
            }*/
        }

        /*else if (approvedHotelsDistance != null && approvedHotelsDistance.size() == 0) {
            if (approvedHotelsDistance != null && approvedHotelsDistance.size() != 0) {
                Collections.sort(approvedHotelsDistance, new Comparator<HotelWithDistance>() {
                    @Override
                    public int compare(HotelWithDistance o1, HotelWithDistance o2) {
                        if (o1.getDistance() > o2.getDistance()) {
                            return 1;
                        } else if (o1.getDistance() < o2.getDistance()) {
                            return -1;
                        } else {
                            return 0;
                        }
                    }
                });

                approvedHotels.clear();
                for(int i=0;i<approvedHotelsDistance.size();i++)
                {
                    approvedHotels.add(approvedHotelsDistance.get(i).getHotelDetails());
                }
                adapter = new HotelListAdapter(HotelListActivity.this, approvedHotels,checkInDate,checkOutDate,
                        latitude,longitude,checkInTime,checkOutTime,room,price);
                mAddedAllHotels.setAdapter(adapter);
            }
        }
        else if(approvedHotelsDistance == null &&(approvedHotelsDistance != null && approvedHotelsDistance.size() != 0))
        {
            Collections.sort(approvedHotelsDistance, new Comparator<HotelWithDistance>() {
                @Override
                public int compare(HotelWithDistance o1, HotelWithDistance o2) {
                    if (o1.getDistance() > o2.getDistance()) {
                        return 1;
                    } else if (o1.getDistance() < o2.getDistance()) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            });

            approvedHotels.clear();
            for(int i=0;i<approvedHotelsDistance.size();i++)
            {
                approvedHotels.add(approvedHotelsDistance.get(i).getHotelDetails());
            }
            adapter = new HotelListAdapter(HotelListActivity.this, approvedHotels,checkInDate,checkOutDate,
                    latitude,longitude,checkInTime,checkOutTime,room,price);
            mAddedAllHotels.setAdapter(adapter);
        }*/
    }





   //adapter for loading hotels
    public class HotelListAdapter extends RecyclerView.Adapter<HotelListAdapter.ViewHolder> {
       private Context context;
       private ArrayList<Integer> hotelImagesArraylist;
       private ArrayList<HotelDetails> list;
       private  String cid,cod,cit,cot,room,price;
       final private double curLang,curLat;
       private String adtype;
       private int mLastPosition=0;
       private RecyclerViewAnimator mAnimator;

       public HotelListAdapter(Context context, ArrayList<HotelDetails> list,String cid,String cod,
                               double curLat,double curLang,String cit,String cot,String room,String price,String adtype) {

           this.context = context;
           this.list = list;
           this.cid = cid;
           this.cod = cod;
           this.cit = cit;
           this.cot = cot;
           this.room= room;
           this.price = price;
           this.curLat = curLat;
           this.curLang = curLang;
           this.adtype = adtype;
           if(approvedHotelsDistance != null)
           {
               approvedHotelsDistance.clear();
           }
           if(approvedHotelsDistance != null)
           {
               approvedHotelsDistance.clear();
           }

           mAnimator = new RecyclerViewAnimator(mLocalHotelList);
       }

       public HotelListAdapter(Context context, ArrayList<HotelDetails> list,String cid,String cod,
                               double curLat,double curLang,String cit,String cot,String room,String price) {

           this.context = context;
           this.list = list;
           this.cid = cid;
           this.cod = cod;
           this.cit = cit;
           this.cot = cot;
           this.room= room;
           this.price = price;
           this.curLat = curLat;
           this.curLang = curLang;
           mAnimator = new RecyclerViewAnimator(mLocalHotelList);



       }

    /*   @Override
       public void onViewDetachedFromWindow(ViewHolder holder) {
           super.onViewDetachedFromWindow(holder);
           holder.itemView.clearAnimation();
       }*/

       @Override
       public HotelListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

           try{
               View v = LayoutInflater.from(parent.getContext())
                       .inflate(R.layout.adapter_hotel_list, parent, false);
               HotelListAdapter.ViewHolder viewHolder = new HotelListAdapter.ViewHolder(v);
               mAnimator.onCreateViewHolder(v);
               return viewHolder;
           }catch (Exception e){
               e.printStackTrace();
               return null;
           }

       }

       @Override
       public void onBindViewHolder(final HotelListAdapter.ViewHolder holder, int position) {

           try{

               HotelDetails dto = list.get(position);

               TypedArray hotelImages = context.getResources().obtainTypedArray(R.array.hotel_images);

               hotelImagesArraylist = new ArrayList<>();

               for (int i=0;i<hotelImages.length();i++)
               {
                   hotelImagesArraylist.add(hotelImages.getResourceId(i,-1));
               }

               if(dto.getHotelType().contains("Zingo Verified"))
               {
                   holder.mVerificationImage.setImageResource(R.drawable.zingo_verified);
               }
               else if(dto.getHotelType().contains("Zingo Partner"))
               {
                   holder.mVerificationImage.setImageResource(R.drawable.zingo_partner);

               }
               else if(dto.getHotelType().contains("Zingo Owned"))
               {
                  // holder.mVerificationImage.setImageResource(R.drawable.call);
               }
               else{
                   //holder.mVerificationImage.setImageResource(R.drawable.call);
               }


               //holder.mHotelName.setText(dto.getHotelDisplayName());
               holder.mHotelName.setText(dto.getHotelDisplayName());
               holder.mHotelLocality.setText(dto.getLocalty());
               getMapDetails(dto,dto.getHotelId(),holder.mHotelDustance,adtype);

               if(dto.getHotelImage() != null && dto.getHotelImage().size()!=0){
                   HotelListImageAdapter hotelImagesadapter = new HotelListImageAdapter(context,dto.getHotelImage(),dto.getHotelId(),"HotelList");
                   holder.viewPager.setAdapter(hotelImagesadapter);

               }else{
                   ViewPagerAdapter hotelImagesadapter = new ViewPagerAdapter(context,hotelImagesArraylist,dto.getHotelId(),"HotelList");
                   holder.viewPager.setAdapter(hotelImagesadapter);

               }
            /*if(dto.getHotelImage() != null && dto.getHotelImage().size()!=0)
            {
                //holder.mHotelImage.setImageResource(R.drawable.hotel1);
                holder.mHotelImage.setImageBitmap(Util.convertToBitMap(dto.getHotelImage().get(0).getImages()));
            }else{
                holder.mHotelImage.setImageResource(R.drawable.hotel1);
            }*/

               if(dto.getRooms() != null && dto.getRooms().size()!=0)
               {
                   getAvailablity(dto.getHotelId(),holder.mRoomsLeft,holder.mItemParentView);
                   System.out.println("₹ "+dto.getRooms().get(0).getDisplayRate()+"");
                   holder.mDisplayPrice.setText("₹ "+dto.getRooms().get(0).getDisplayRate()+"");
                   holder.mSellPrice.setText("₹ "+dto.getRooms().get(0).getSellRate()+"");

                   if(dto.getRooms().get(0).getDisplayRate() != 0 && dto.getRooms().get(0).getSellRate() != 0)
                   {
                       double diff = dto.getRooms().get(0).getDisplayRate() - dto.getRooms().get(0).getSellRate();
                       //System.out.println("diff = "+diff);
                       double div = diff/dto.getRooms().get(0).getDisplayRate();
                       //System.out.println("div = "+div);
                       double dis = div*100;
                       System.out.println("dis = "+Math.round(dis));
                       DecimalFormat numberFormat = new DecimalFormat("#.##");
                       holder.mDiscount.setText(" "+Math.round(dis)+"% Discount");
                   }
                   else
                   {
                       holder.mDiscount.setText(" "+0+"% Discount");
                   }


               }else{
                   holder.mItemParentView.setEnabled(false);
                   //mParent.setEnabled(false);
                   holder.mRoomsLeft.setText("Sold out");
               }

               // holder.mHotelName.setText(dto.getHotelName());
               //holder.mHotelName.setText(dto.getHotelName());

        /*holder.mHotelImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                context.startActivity(new Intent(context,ImageFull.class));
            }
        });*/

        if(position!=mLastPosition){
            mAnimator.onBindViewHolder(holder.itemView, position);
            mLastPosition = position;
        }



           }catch (Exception e){
               e.printStackTrace();
           }

       }
       public void setFadeAnimation(View view) {
           AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
           anim.setDuration(500);
           view.startAnimation(anim);
       }


       @Override
       public int getItemCount() {
           return list.size();
       }


       class ViewHolder extends RecyclerView.ViewHolder /*implements View.OnClickListener*/ {

           public ImageView mHotelImage,mVerificationImage;
           public TextView mHotelName,mHotelDustance,mHotelLocality,mDisplayPrice,mSellPrice,mRoomsLeft,mDiscount;
           public LinearLayout mItemParentView;

           ViewPager viewPager;

           public ViewHolder(View itemView) {
               super(itemView);
               itemView.setClickable(true);

               try{
                   mHotelImage = (ImageView) itemView.findViewById(R.id.hotel_preview_imageview);
                   mVerificationImage = (ImageView) itemView.findViewById(R.id.verification_image);
                   //mVerificationImage.setVisibility(View.GONE);
                   viewPager = (ViewPager) itemView.findViewById(R.id.hotel_images_viewpager);
                   viewPager.setClipToPadding(false);
                   viewPager.setPageMargin(12);
                   mHotelName = (TextView) itemView.findViewById(R.id.hotel_name_textview);
                   mHotelDustance = (TextView) itemView.findViewById(R.id.hotel_distance);
                   mHotelLocality = (TextView) itemView.findViewById(R.id.hotel_address_textview);
                   mDisplayPrice = (TextView) itemView.findViewById(R.id.hotel_display_price);
                   mSellPrice = (TextView) itemView.findViewById(R.id.hotel_sell_price);
                   mRoomsLeft = (TextView) itemView.findViewById(R.id.txt_room_left);
                   mDiscount = (TextView) itemView.findViewById(R.id.activity_discount);
                   mItemParentView = (LinearLayout) itemView.findViewById(R.id.item_parent_view);
               }catch (Exception e){
                   e.printStackTrace();
               }



           }
       }

       private void getAvailablity(final int hotelId, final TextView tv, final LinearLayout mParent) {
           /*final ProgressDialog dialog = new ProgressDialog(context);
           dialog.setTitle(R.string.loader_message);
           dialog.setCancelable(false);
           dialog.show();*/
           final HotelRoomAvailablity search = new HotelRoomAvailablity();
           search.setHotelId(hotelId);
           search.setFromdate(cid);
           search.setToDate(cod);

           System.out.println("Hotel id1=="+search.getHotelId());

           new ThreadExecuter().execute(new Runnable() {
               @Override
               public void run() {
                   BookingApi bookingApi = Util.getClient().create(BookingApi.class);
                   String authenticationString = Util.getToken(context);
                   final Call<HotelAvailablityResponse> getAllBookings = bookingApi.
                           getHotelRoomAvailablitys(authenticationString,search);

                   getAllBookings.enqueue(new Callback<HotelAvailablityResponse>() {
                       @Override
                       public void onResponse(Call<HotelAvailablityResponse> call, Response<HotelAvailablityResponse> response) {
                           /*if(dialog != null)
                           {
                               dialog.dismiss();
                           }*/
                           try{
                               if(response.code() == 200)
                               {
                                   HotelAvailablityResponse book = new HotelAvailablityResponse();
                                   if(response.body() != null)
                                   {

                                       //System.out.println("Rooms left "+response.body().getTotalRoomAvailable());
                                       if(response.body().getTotalRoomAvailable() > 0)
                                       {
                                           tv.setText(response.body().getTotalRoomAvailable()+" room(s) left");
                                       }
                                       else
                                       {
                                           mParent.setEnabled(false);
                                           mParent.setClickable(false);
                                           tv.setText("Sold out");
                                           //tv.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                       }

                                   }
                                   //getTavellers(bookings1ArrayList);
                                   //return
                               }
                           }catch (Exception e){
                               e.printStackTrace();
                           }

                       }
                       @Override
                       public void onFailure(Call<HotelAvailablityResponse> call, Throwable t) {

                           Toast.makeText(context,"Fail",Toast.LENGTH_LONG);
                           System.out.println("Rooms left fail ");
                           /*if(dialog != null)
                           {
                               dialog.dismiss();
                           }*/
                       }
                   });
               }
           });


       }

       public void getMapDetails(final HotelDetails hotelDetails, final int id, final TextView tv , final String adaptertype)
       {


           new ThreadExecuter().execute(new Runnable() {
               @Override
               public void run() {
                   String auth_string = Util.getToken(context);//"Basic " +  Base64.encodeToString(authentication.getBytes(), Base64.NO_WRAP);
                   MapApi mapApi = Util.getClient().create(MapApi.class);
                   Call<ArrayList<Maps>> getMapDetails = mapApi.getMapDetails(auth_string,id);

                   getMapDetails.enqueue(new Callback<ArrayList<Maps>>() {
                       @Override
                       public void onResponse(Call<ArrayList<Maps>> call, Response<ArrayList<Maps>> response) {

                           try{
                               if(response.code() == 200 && response.body() != null && response.body().size() != 0)
                               {


                                   DecimalFormat dc = new DecimalFormat("#.##");
                                   System.out.println("Hotel id="+id);
                                   double distance = distance(curLat,curLang,Double.parseDouble(response.body().get(0).getLatitude()),Double.parseDouble(response.body().get(0).getLogitude()));
                                   if(adaptertype != null)
                                   {
                                       if(adaptertype.equalsIgnoreCase("all"))
                                       {
                                           approvedHotelsDistance.add(new HotelWithDistance(hotelDetails,distance));
                                       }
                                       else if(adaptertype.equalsIgnoreCase("filter"))
                                       {
                                           approvedHotelsDistance.add(new HotelWithDistance(hotelDetails,distance));
                                       }
                                   }
                                   if(distance<=10&&price!=null){
                                       HotelNotification fm = new HotelNotification();
                                       fm.setSenderId(Constants.senderId);
                                       fm.setServerId(Constants.serverId);
                                       //fm.setTravellerId(travellerId);
                                       fm.setHotelId(id);
                                       fm.setTitle("Agent Booking Request");
                                       int profileId = PreferenceHandler.getInstance(context).getUserId();
                                       // price = budget_et.getText().toString();
                                       fm.setMessage(profileId+","+cid+","+cod+","+price+","+room+","+cit+","+cot);
                                       //registerTokenInDB(fm);
                                       //sendNotification(fm);
                                       sendfirebaseNotification(fm);
                                   }
                                   tv.setText(dc.format(distance)+" Km");
                               }
                               else
                               {
                                   if(adaptertype != null)
                                   {
                                       if(adaptertype.equalsIgnoreCase("all"))
                                       {
                                           approvedHotelsDistance.add(new HotelWithDistance(hotelDetails,0.0));
                                       }
                                       else
                                       {
                                           approvedHotelsDistance.add(new HotelWithDistance(hotelDetails,0.0));
                                       }
                                   }
                                   //Toast.makeText(context,response.message(),Toast.LENGTH_SHORT).show();
                                   //tv.setText("No Location found for this hotel");
                                   tv.setVisibility(View.GONE);
                               }
                           }catch (Exception e){
                               e.printStackTrace();
                           }


                       }

                       @Override
                       public void onFailure(Call<ArrayList<Maps>> call, Throwable t) {

                           Toast.makeText(context,t.getMessage(),Toast.LENGTH_SHORT).show();
                           System.out.println(t.getMessage());
                       }
                   });

               }
           });


       }


       public double distance(double curLat,double curLang,double hotelLat,double hotelLang){

           try{
               System.out.println(" long=="+curLang+" lat=="+curLat);
               double theta = curLang - hotelLang;
               double dist = Math.sin(deg2rad(curLat))
                       * Math.sin(deg2rad(hotelLat))
                       + Math.cos(deg2rad(curLat))
                       * Math.cos(deg2rad(hotelLat))
                       * Math.cos(deg2rad(theta));
               dist = Math.acos(dist);
               dist = rad2deg(dist);
               dist = dist * 60 * 1.1515 * 1.60;
               System.out.println("Distance =="+dist);

               return dist;

           }catch (Exception e){
               e.printStackTrace();
               return 0;
           }


       }

       private double deg2rad(double deg) {
           return (deg * Math.PI / 180.0);
       }

       private double rad2deg(double rad) {
           return (rad * 180.0 / Math.PI);
       }

       private void sendfirebaseNotification(final HotelNotification notification) {


           new ThreadExecuter().execute(new Runnable() {
               @Override
               public void run() {

                   String auth_string = Util.getToken(context);//"Basic " +  Base64.encodeToString(authentication.getBytes(), Base64.NO_WRAP);
                   //System.out.println("Hotel id = "+notification.getHotelId());
                   NotificationApi travellerApi = Util.getClient().create(NotificationApi.class);
                   Call<ArrayList<String>> response = travellerApi.sendnotificationToHotel(auth_string,notification);

                   response.enqueue(new Callback<ArrayList<String>>() {
                       @Override
                       public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {


                           System.out.println(response.code());
                           try{
                               if(response.code() == 200)
                               {
                                   if(response.body() != null)
                                   {

                                       NotificationManager nf = new NotificationManager();
                                       nf.setNotificationText(notification.getTitle());
                                       nf.setNotificationFor(notification.getMessage());
                                       nf.setHotelId(notification.getHotelId());
                                       savenotification(nf);


                                   }
                               }
                           }catch (Exception e){
                               e.printStackTrace();
                           }

                       }

                       @Override
                       public void onFailure(Call<ArrayList<String>> call, Throwable t) {

                       }
                   });
               }
           });
       }

       private void savenotification(final NotificationManager notification) {



           new ThreadExecuter().execute(new Runnable() {
               @Override
               public void run() {
                   System.out.println("Hotel id = "+notification.getHotelId());
                   String auth_string = Util.getToken(context);
                   NotificationApi travellerApi = Util.getClient().create(NotificationApi.class);
                   Call<NotificationManager> response = travellerApi.saveNotification(auth_string,notification);

                   response.enqueue(new Callback<NotificationManager>() {
                       @Override
                       public void onResponse(Call<NotificationManager> call, Response<NotificationManager> response) {


                           System.out.println(response.code());
                           try{
                               if(response.code() == 200)
                               {
                                   if(response.body() != null)
                                   {



                                   }
                               }
                           }catch (Exception e){
                               e.printStackTrace();
                           }

                       }

                       @Override
                       public void onFailure(Call<NotificationManager> call, Throwable t) {

                       }
                   });
               }
           });
       }


   }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = HotelListActivity.this.getMenuInflater();
        inflater.inflate(R.menu.filter_option, menu);
        return true;
    }*/

    public void showAlertBox(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        try{
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.activity_filter,null);

            TextView mLocation = (TextView)view.findViewById(R.id.location_text);
            TextView mPrice = (TextView)view.findViewById(R.id.price_text);
            TextView mAmenity = (TextView)view.findViewById(R.id.amenity_text);
            RecyclerView mLocationList = (RecyclerView)view.findViewById(R.id.hotel_list_locality);
            RecyclerView mAmenityList = (RecyclerView)view.findViewById(R.id.hotel_list_amenity);
            LinearLayout mPriceLayout = (LinearLayout)view. findViewById(R.id.price_layout);

            builder.setView(view);
            builder.setTitle("Filter");
            final AlertDialog dialog = builder.create();
            //getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.show();
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);


        }catch (Exception e){
            e.printStackTrace();
        }



    }

    private void filterFeature(ArrayList<String> features) {

        try{
            if(features!=null&&features.size()!=0){
                getHotels(activity);
            }



        }catch (Exception e){
            e.printStackTrace();
        }



    }

    public void slideUp(View view){

        Animation animate = AnimationUtils.loadAnimation(this,R.anim.up_scroll);

        animate.setDuration(400);
        animate.setFillAfter(false);
        view.startAnimation(animate);

    }


    public void slideDown(View view){
        Animation animate = AnimationUtils.loadAnimation(this,R.anim.down_scroll);
        animate.setDuration(400);
        animate.setFillAfter(true);
        view.startAnimation(animate);

    }

    public void recyclerAnimation(){
        mLocalHotelList.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {

                    @Override
                    public boolean onPreDraw() {
                        mLocalHotelList.getViewTreeObserver().removeOnPreDrawListener(this);

                        for (int i = 0; i < mLocalHotelList.getChildCount(); i++) {
                            View v = mLocalHotelList.getChildAt(i);
                            v.setAlpha(0.0f);
                            v.animate().alpha(1.0f)
                                    .setDuration(300)
                                    .setStartDelay(i * 50)
                                    .start();
                        }

                        return true;
                    }
                });
    }
}
