package app.zingo.com.agentapp.Fragments;

import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import app.zingo.com.agentapp.Adapter.GridViewAdapter;
import app.zingo.com.agentapp.CustomViews.CustomGridView;
import app.zingo.com.agentapp.Model.HotelService;
import app.zingo.com.agentapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link //AmenitiesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link //AmenitiesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AmenitiesFragment extends Fragment {

    ArrayList<HotelService> hotelFacilitiesArraylist;

    CustomGridView gridView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_amenities, container, false);

        gridView = (CustomGridView) view.findViewById(R.id.hotel_details_amenities);

        TypedArray hotelFacilityImages = getResources().obtainTypedArray(R.array.hotel_facily_images);
        String[] hotelFacilityName = getResources().getStringArray(R.array.hotel_facility_name);

        hotelFacilitiesArraylist = new ArrayList<>();

        for (int i=0;i<hotelFacilityName.length;i++)
        {
            hotelFacilitiesArraylist.add(new HotelService(hotelFacilityImages.getResourceId(i,-1),hotelFacilityName[i]));
        }

        GridViewAdapter facilityAdapter = new GridViewAdapter(getActivity(),hotelFacilitiesArraylist);
         gridView.setAdapter(facilityAdapter);
        return view;
    }


}
