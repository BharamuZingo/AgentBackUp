package app.zingo.com.agentapp.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import app.zingo.com.agentapp.Fragments.AmenitiesFragment;
import app.zingo.com.agentapp.Fragments.HotelOverviewFragment;

/**
 * Created by ZingoHotels.com on 15-01-2018.
 */

public class HotelDetailsAdapter extends FragmentStatePagerAdapter {



        //String[] tabTitles = {"New Booking","Upcoming","All","Cancelled","Blocked Rooms"};
        String[] tabTitles = {"Hotel Overview","Amenities"};

        public HotelDetailsAdapter(FragmentManager fm)
        {
            super(fm);

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    HotelOverviewFragment newBooking = new HotelOverviewFragment();
                    return newBooking;
            /*case 1:
                AllBookingsFragment all = new AllBookingsFragment();
                return all;
            case 2:
                UpcomingFragment upcoming = new UpcomingFragment();
                return upcoming;
            case 3:
                CancelledBookingFragment cancelled = new CancelledBookingFragment();
                return cancelled;*/
            /*case 4:
                PayAtHotelFragment payAtHotel = new PayAtHotelFragment();
                return payAtHotel;*/

                case 1:
                    AmenitiesFragment block = new AmenitiesFragment();
                    return block;
            /*case 1:
                AmenitiesFragment policyFragment = new AmenitiesFragment();
                return policyFragment;
            case 2:
                AmenitiesFragment aboutUs1 = new AmenitiesFragment();
                return aboutUs1;
            case 3:
                AmenitiesFragment policyFragment1 = new AmenitiesFragment();
                return policyFragment1;
            case 4:
                AmenitiesFragment policyFragment2 = new AmenitiesFragment();
                return policyFragment2;
            case 5:
                AmenitiesFragment aboutUs2 = new AmenitiesFragment();
                return aboutUs2;
            case 6:
                AmenitiesFragment aboutUs3 = new AmenitiesFragment();
                return aboutUs3;
            case 7:
                AmenitiesFragment aboutUs4 = new AmenitiesFragment();
                return aboutUs4;
            case 8:
                AmenitiesFragment aboutUs5 = new AmenitiesFragment();
                return aboutUs5;
            case 9:
                AmenitiesFragment aboutUs6 = new AmenitiesFragment();
                return aboutUs6;
            case 10:
                PaidAmenitiesFragment aboutUs7 = new PaidAmenitiesFragment();
                return aboutUs7;*/
                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }


}
