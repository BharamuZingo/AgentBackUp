package app.zingo.com.agentapp.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import app.zingo.com.agentapp.R;
import at.blogc.android.views.ExpandableTextView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link //HotelOverviewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link //HotelOverviewFragment#newInstance} factory method to
 * create an instance of this fragment.*/

public class HotelOverviewFragment extends Fragment {

    ExpandableTextView mHotelDetailsMoreInfo;
    TextView mToggleExpand;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_hotel_overview, container, false);


        mHotelDetailsMoreInfo = (ExpandableTextView) view.findViewById(R.id.hotel_details_expand_text);
        mToggleExpand = (TextView) view.findViewById(R.id.collapse_text);
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
        });
        return view;
    }


}
