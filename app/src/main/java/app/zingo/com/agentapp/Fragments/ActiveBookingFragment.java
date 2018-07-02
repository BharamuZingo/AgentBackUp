package app.zingo.com.agentapp.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import app.zingo.com.agentapp.Adapter.BookingRecyclerViewAdapter;
import app.zingo.com.agentapp.Model.Bookings1;
import app.zingo.com.agentapp.Model.Traveller;
import app.zingo.com.agentapp.R;
import app.zingo.com.agentapp.Utils.PreferenceHandler;
import app.zingo.com.agentapp.Utils.ThreadExecuter;
import app.zingo.com.agentapp.Utils.Util;
import app.zingo.com.agentapp.WebApi.BookingApi;
import app.zingo.com.agentapp.WebApi.TravellerApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ActiveBookingFragment extends Fragment {


    RecyclerView recyclerView;
    View empty;


    ArrayList<Traveller> travellerArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_active_booking, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.active_bookings_list);
        empty = (View)view.findViewById(R.id.empty);

        getBookings();

        return view;
    }

    private void getBookings() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setTitle(R.string.loader_message);
        dialog.setCancelable(false);
        dialog.show();


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                BookingApi bookingApi = Util.getClient().create(BookingApi.class);
                String authenticationString = Util.getToken(getActivity());
                final Call<ArrayList<Bookings1>> getAllBookings = bookingApi.
                        getBookingsByProfileId(authenticationString, PreferenceHandler.getInstance(getActivity()).getUserId());

                getAllBookings.enqueue(new Callback<ArrayList<Bookings1>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Bookings1>> call, Response<ArrayList<Bookings1>> response) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        if(response.code() == 200)
                        {
                            if(response.body() != null)
                            {
                                    /*bookings1ArrayList = response.body();
                                    getTavellers(response.body());*/
                                ArrayList<Bookings1> bookings1ArrayList = response.body();
                                //System.out.println(response.body().size());

                                //ArrayList<Bookings1> body = response.body();
                                ArrayList<Bookings1> arrayList = new ArrayList<>();
                                for (int i=0;i<bookings1ArrayList.size();i++)
                                {
                                    if(bookings1ArrayList.get(i).getBookingStatus() != null &&
                                            bookings1ArrayList.get(i).getBookingStatus().equals("Active"))
                                    {
                                        Bookings1 bookings1 = bookings1ArrayList.get(i);
                                        arrayList.add(bookings1);
                                    }
                                }



                                if(arrayList.size() != 0)
                                {
                                    Collections.sort(bookings1ArrayList, new Comparator<Bookings1>() {
                                        @Override
                                        public int compare(Bookings1 o1, Bookings1 o2) {
                                            return o2.getCheckInDate().compareTo(o1.getCheckInDate());
                                        }
                                    });
                                    getTavellers(arrayList);
                                }
                                else
                                {
                                    //Toast.makeText(getActivity(),"No Booking found",Toast.LENGTH_SHORT).show();
                                    empty.setVisibility(View.VISIBLE);
                                }
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Bookings1>> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                    }
                });
            }
        });


    }


    private void getTavellers(final ArrayList<Bookings1> body) {
        travellerArrayList = new ArrayList<>();
        if(body.size() != 0)
        {
            final ProgressDialog dialog = new ProgressDialog(getActivity());
            dialog.setTitle(R.string.loader_message);
            dialog.setCancelable(false);
            dialog.show();
            for (int i = 0;i<body.size();i++)
            {
                String auth_string = Util.getToken(getActivity());//"Basic " +  Base64.encodeToString(authentication.getBytes(), Base64.NO_WRAP);
                TravellerApi travellerApi = Util.getClient().create(TravellerApi.class);
                Call<Traveller> getTraveller = travellerApi.getTravellerDetails(auth_string,body.get(i).getTravellerId());

                getTraveller.enqueue(new Callback<Traveller>() {
                    @Override
                    public void onResponse(Call<Traveller> call, Response<Traveller> response) {
                        if(response.code() == 200)
                        {
                            if(response.body() != null)
                            {
                                travellerArrayList.add(response.body());
                                if(body.size() == travellerArrayList.size())
                                {
                                    System.out.println("same");
                                    BookingRecyclerViewAdapter bookingRecyclerViewAdapter =
                                            new BookingRecyclerViewAdapter(getActivity(),body,travellerArrayList);
                                    recyclerView.setAdapter(bookingRecyclerViewAdapter);
                                }
                            }
                        }
                        else
                        {
                            Toast.makeText(getActivity(),"Please try after some time",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Traveller> call, Throwable t) {

                    }
                });
            }

            if(dialog != null)
            {
                dialog.dismiss();
            }
        }
        else
        {
            /*BookingRecyclerViewAdapter bookingRecyclerViewAdapter =
                    new BookingRecyclerViewAdapter(getActivity(),body,travellerArrayList);
            mCancelledBookings.setAdapter(bookingRecyclerViewAdapter);*/
        }


    }


}
