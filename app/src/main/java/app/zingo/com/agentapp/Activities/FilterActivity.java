package app.zingo.com.agentapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.TreeSet;

import app.zingo.com.agentapp.Adapter.AmenityListAdapter;
import app.zingo.com.agentapp.Adapter.FilterAdapter;
import app.zingo.com.agentapp.Adapter.ListItemAdapter;
import app.zingo.com.agentapp.Adapter.LocalityFilterAdapter;
import app.zingo.com.agentapp.HotelList;
import app.zingo.com.agentapp.MainActivity;
import app.zingo.com.agentapp.Model.DataModel;
import app.zingo.com.agentapp.Model.PaidAmenitiesCategory;
import app.zingo.com.agentapp.R;
import app.zingo.com.agentapp.Utils.ThreadExecuter;
import app.zingo.com.agentapp.Utils.Util;
import app.zingo.com.agentapp.WebApi.PaidAmenitiesCategoryOperation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilterActivity extends AppCompatActivity {

    TextView mLocation,mAmenity,mPrice;
    TextInputEditText mLowPrice,mHighPrice;
    RecyclerView mLocationList;
    ListView mAmenityList;
    LinearLayout mPriceLayout;
    Button mFilter;
    public LocalityFilterAdapter adapter;

    String locality,checkInDate,checkOutDate,room,checkInTime,checkOutTime,price;
    Double latitude,longitude;

    ArrayList<String> hotelLocality;
    ArrayList<PaidAmenitiesCategory> amenityList;
    ArrayList<String> filter;
    public ArrayList<DataModel> featureList;
    public ArrayList<String> selectedFeature = new ArrayList<>();
    String[] hotelTypes;
    String startPrice,endPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            setContentView(R.layout.activity_filter);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            setTitle("Filter Options");


            Intent intent = getIntent();
            if(intent!=null){
                hotelLocality = intent.getStringArrayListExtra("LocalityName");

                locality = intent.getStringExtra("Locality");
                checkInDate = intent.getStringExtra("CheckinDate");
                checkOutDate = intent.getStringExtra("CheckOutDate");
                checkInTime = intent.getStringExtra("CheckInTime");
                checkOutTime = intent.getStringExtra("CheckOutTime");
                price = intent.getStringExtra("Price");
                latitude = intent.getDoubleExtra("Latitude",0.0);
                longitude = intent.getDoubleExtra("Longitude",0.0);

                System.out.println("Looong=="+longitude+" lattt="+latitude);
                System.out.println("CheckInTime"+checkInTime+" "+"CheckOutTime"+checkOutTime);

                room = intent.getStringExtra("Room");
                System.out.println("Pass=="+room);
            }


            mLocation = (TextView)findViewById(R.id.location_text);
            mPrice = (TextView)findViewById(R.id.price_text);
            mLowPrice = (TextInputEditText) findViewById(R.id.min_price);
            mHighPrice = (TextInputEditText) findViewById(R.id.max_price);
            mAmenity = (TextView)findViewById(R.id.amenity_text);
            mLocationList = (RecyclerView)findViewById(R.id.hotel_list_locality);
            mAmenityList = (ListView)findViewById(R.id.hotel_list_amenity);
            mPriceLayout = (LinearLayout) findViewById(R.id.price_layout);
            mFilter = (Button) findViewById(R.id.filter_btn);

            hotelTypes = getResources().getStringArray(R.array.hotel_type_filter);
            if(hotelTypes.length!=0){
                featureList = new ArrayList();
                for(int i=0;i<hotelTypes.length;i++){
                    featureList.add(new DataModel(hotelTypes[i],false));
                }
            }

            mPrice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPriceLayout.setVisibility(View.VISIBLE);
                    mLocationList.setVisibility(View.GONE);
                    mLocation.setBackgroundColor(Color.parseColor("#dadada"));
                    mAmenityList.setVisibility(View.GONE);
                    mAmenity.setBackgroundColor(Color.parseColor("#dadada"));
                    mPrice.setBackgroundColor(Color.WHITE);

                }
            });

            mLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mLocationList.setVisibility(View.VISIBLE);
                    mLocation.setBackgroundColor(Color.WHITE);
                    mAmenityList.setVisibility(View.GONE);
                    mAmenity.setBackgroundColor(Color.parseColor("#dadada"));
                    mPrice.setBackgroundColor(Color.parseColor("#dadada"));
                    mPriceLayout.setVisibility(View.GONE);
                    Util.hideKeyboard(FilterActivity.this);
                }
            });

            mAmenity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Util.hideKeyboard(FilterActivity.this);
                    mLocationList.setVisibility(View.GONE);
                    mLocation.setBackgroundColor(Color.parseColor("#dadada"));
                    mAmenityList.setVisibility(View.VISIBLE);
                    mAmenity.setBackgroundColor(Color.WHITE);
                    mPrice.setBackgroundColor(Color.parseColor("#dadada"));
                    mPriceLayout.setVisibility(View.GONE);

                    mAmenityList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    final FilterAdapter adapter = new FilterAdapter(FilterActivity.this,featureList);
                    mAmenityList.setAdapter(adapter);

                    adapter.notifyDataSetInvalidated();
                    mAmenityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            System.out.println("position = "+position);


                            DataModel dataModel= featureList.get(position);
                            dataModel.checked = !dataModel.checked;
                            if(dataModel.checked)
                            {
                                selectedFeature.add(featureList.get(position).name);

                            }
                            else
                            {
                                if (selectedFeature.contains(featureList.get(position).name))
                                {
                                    selectedFeature.remove(featureList.get(position).name);
                                }
                                //mSelectedFloorLevels.setText(floorsList.get(position).name.replace(floorsList.get(position).name,""));
                            }

                            adapter.notifyDataSetChanged();
                            System.out.println("Feature Size=="+selectedFeature.size());


                        }
                    });



                    //getPaidAmenities();
                }
            });

            mFilter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {



                    if(endPrice==null&&startPrice==null&&selectedFeature==null&&endPrice.isEmpty()&&startPrice.isEmpty()){

                        Toast.makeText(FilterActivity.this, "Choose any filter", Toast.LENGTH_SHORT).show();

                    }else{

                        startPrice = mLowPrice.getText().toString();
                        endPrice = mHighPrice.getText().toString();
                        if(startPrice==null||startPrice.isEmpty()){
                            startPrice = "0";
                        }
                        if(endPrice==null||endPrice.isEmpty()){
                            endPrice="0";
                        }
                        SparseBooleanArray sparseBooleanArray = mAmenityList.getCheckedItemPositions();


                        int no = mAmenityList.getChildCount();


                        if(!selectedFeature.isEmpty())
                        {
                            Intent filter = new Intent(FilterActivity.this, HotelListActivity.class);
                            //filter.putStringArrayListExtra("LocalityName",hotelLocality);
                            filter.putExtra("Locality",locality);
                            filter.putExtra("Latitude",latitude);
                            filter.putExtra("Longitude",longitude);
                            filter.putExtra("CheckinDate",checkInDate);
                            filter.putExtra("CheckOutDate",checkOutDate);
                            filter.putExtra("CheckInTime",checkInTime);
                            filter.putExtra("CheckOutTime",checkOutTime);
                            filter.putExtra("Room",room);
                            filter.putExtra("GuestDetails","");
                            filter.putExtra("FeatureList",selectedFeature);
                            filter.putExtra("StartPrice",startPrice);
                            filter.putExtra("EndPrice",endPrice);
                            filter.putExtra("Activity","Filter");
                            System.out.println("Feature Size=="+selectedFeature.size());
                            startActivity(filter);
                        }
                        else
                        {

                            Intent filter = new Intent(FilterActivity.this, HotelListActivity.class);
                            //filter.putStringArrayListExtra("LocalityName",hotelLocality);
                            filter.putExtra("Locality",locality);
                            filter.putExtra("Latitude",latitude);
                            filter.putExtra("Longitude",longitude);
                            filter.putExtra("CheckinDate",checkInDate);
                            filter.putExtra("CheckOutDate",checkOutDate);
                            filter.putExtra("CheckInTime",checkInTime);
                            filter.putExtra("CheckOutTime",checkOutTime);
                            filter.putExtra("StartPrice",startPrice);
                            filter.putExtra("EndPrice",endPrice);
                            filter.putExtra("Activity","Filter");
                            filter.putExtra("Room",room);
                            filter.putExtra("GuestDetails","");
                            startActivity(filter);
                        }
                    }

                }
            });

            if(hotelLocality!=null&&hotelLocality.size()!=0){
                for(int i =0;i<hotelLocality.size();i++){
                    System.out.println("Locality = "+hotelLocality.get(i).toString());
                }
            }

            adapter = new LocalityFilterAdapter(FilterActivity.this, hotelLocality);
            mLocationList.setAdapter(adapter);
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id)
        {
            case android.R.id.home:
                try{
                    Intent filter = new Intent(FilterActivity.this, HotelListActivity.class);
                    //filter.putStringArrayListExtra("LocalityName",hotelLocality);
                    filter.putExtra("Locality",locality);
                    filter.putExtra("Latitude",latitude);
                    filter.putExtra("Longitude",longitude);
                    filter.putExtra("CheckinDate",checkInDate);
                    filter.putExtra("CheckOutDate",checkOutDate);
                    filter.putExtra("CheckInTime",checkInTime);
                    filter.putExtra("CheckOutTime",checkOutTime);
                    filter.putExtra("Room",room);
                    filter.putExtra("GuestDetails","");
                    startActivity(filter);
                    break;

                }catch (Exception e){
                    e.printStackTrace();
                }



        }

        return super.onOptionsItemSelected(item);
    }

    public void getPaidAmenities()
    {
        final ProgressDialog dialog = new ProgressDialog(FilterActivity.this);
        dialog.setMessage("Loading");
        dialog.setCancelable(false);
        dialog.show();
        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                String auth_string = Util.getToken(FilterActivity.this);//"Basic " +  Base64.encodeToString(authentication.getBytes(), Base64.NO_WRAP);
                PaidAmenitiesCategoryOperation operation = Util.getClient().create(PaidAmenitiesCategoryOperation.class);
                Call<ArrayList<PaidAmenitiesCategory>> response = operation.getPaidAmenitiesCategories(auth_string);

                response.enqueue(new Callback<ArrayList<PaidAmenitiesCategory>>() {
                    @Override
                    public void onResponse(Call<ArrayList<PaidAmenitiesCategory>> call, Response<ArrayList<PaidAmenitiesCategory>> response) {
                        System.out.println("PaidAmenitiesCategory = "+response.code());
                        amenityList = response.body();
                        if(dialog != null && dialog.isShowing())
                        {
                            dialog.dismiss();
                        }

                        try{
                            if(response.code() == 200)
                            {
                                if(amenityList != null && amenityList.size() != 0)
                                {
                                /*AmenityListAdapter adapter = new AmenityListAdapter(FilterActivity.this,amenityList);
                                mAmenityList.setAdapter(adapter);*/


                                }
                                else
                                {
                                    Toast.makeText(FilterActivity.this,"No paid amenities categories added",Toast.LENGTH_SHORT).show();
                                    if(dialog != null && dialog.isShowing())
                                    {
                                        dialog.dismiss();
                                    }
                                }
                            }
                            else
                            {
                                Toast.makeText(FilterActivity.this,"Check your internet connection or please try after some time",
                                        Toast.LENGTH_LONG).show();
                                if(dialog != null && dialog.isShowing())
                                {
                                    dialog.dismiss();
                                }
                                return;
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }




                    }

                    @Override
                    public void onFailure(Call<ArrayList<PaidAmenitiesCategory>> call, Throwable t) {
                        System.out.println("Failed");

                        if(dialog != null && dialog.isShowing())
                        {
                            dialog.dismiss();
                        }
                        Toast.makeText(FilterActivity.this,"Check your internet connection or please try after some time",
                                Toast.LENGTH_LONG).show();
                        return;

                    }
                });

            }
        });

    }

}
