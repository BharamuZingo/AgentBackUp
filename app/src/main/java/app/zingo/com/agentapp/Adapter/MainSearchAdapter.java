package app.zingo.com.agentapp.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import app.zingo.com.agentapp.Activities.BiddingBookingActivity;
import app.zingo.com.agentapp.Fragments.ActiveBookingFragment;
import app.zingo.com.agentapp.Fragments.BiddingFragemnet;
import app.zingo.com.agentapp.Fragments.HotelSearchFragments;

/**
 * Created by ZingoHotels Tech on 07-05-2018.
 */

public class MainSearchAdapter extends FragmentStatePagerAdapter {

    //String[] tabTitles = {"New Booking","Upcoming","All","Cancelled","Blocked Rooms"};
    String[] tabTitles = {"Search Hotel","Bid Your Stay"};

    /*public BookingListTabAdapter(FragmentManager fm)
    {
        super(fm);

    }*/

    public MainSearchAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {

            case 0:
                HotelSearchFragments active = new HotelSearchFragments();
                return active;
            case 1:
                BiddingFragemnet all = new BiddingFragemnet();
                return all;


            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }
}
