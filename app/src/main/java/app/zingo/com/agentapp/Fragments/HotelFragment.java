package app.zingo.com.agentapp.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

import app.zingo.com.agentapp.Activities.HotelListActivity;
import app.zingo.com.agentapp.Model.HotelDetails;
import app.zingo.com.agentapp.R;
import app.zingo.com.agentapp.Utils.ThreadExecuter;
import app.zingo.com.agentapp.Utils.Util;
import app.zingo.com.agentapp.WebApi.HotelApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by CSC on 1/6/2018.
 */

public class HotelFragment  extends Fragment {

    public LinearLayout mPreloaderList,mProgress;
    public RecyclerView mHotelList;
    public HotelListActivity.HotelListAdapter adapter;


    public HotelFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.allhotels_fragment, container, false);

        mPreloaderList = (LinearLayout) v.findViewById(R.id.preloader_hotel_list);
        mProgress = (LinearLayout) v.findViewById(R.id.preloader_layout);
       // mHotelList = (RecyclerView) v.findViewById(R.id.local_hotel_list_loader);


        getCategories();
        return  v;
    }

    private void getCategories(){



        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                String auth_string = Util.getToken(getActivity());
                HotelApi apiService =
                        Util.getClient().create(HotelApi.class);
                Call<ArrayList<HotelDetails>> call = apiService.getHotel(auth_string);/*getRooms()*/;

                call.enqueue(new Callback<ArrayList<HotelDetails>>() {
                    @Override
                    public void onResponse(Call<ArrayList<HotelDetails>> call, Response<ArrayList<HotelDetails>> response) {
                        int statusCode = response.code();

                        if (statusCode == 200) {

                            ArrayList<HotelDetails> list =  response.body();


                            if(list != null && list.size() != 0)
                            {
                               /* adapter = new HotelListAdapter(getContext(), list);
                                mHotelList.setAdapter(adapter);
                                mPreloaderList.setVisibility(View.GONE);
                                mProgress.setVisibility(View.GONE);*/
                            }
                            else
                            {
                                Toast.makeText(getActivity(), "No Hotels", Toast.LENGTH_SHORT).show();
                            }



                        }else {
                            Toast.makeText(getActivity(), " failed due to status code:"+statusCode, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<HotelDetails>> call, Throwable t) {
                        // Log error here since request failed

                        Log.e("TAG", t.toString());
                    }
                });
            }


        });
    }
}