package app.zingo.com.agentapp.Activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import app.zingo.com.agentapp.Adapter.BookingHistoryViewPagerAdapter;
import app.zingo.com.agentapp.DemoActivity;
import app.zingo.com.agentapp.ProfileDetails;
import app.zingo.com.agentapp.R;

public class BookingHistoryActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    TabLayout tabLayout;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_history);

        Toolbar toolbar = (Toolbar) findViewById(R.id.booking_history_toolbar);
        setSupportActionBar(toolbar);
       /* getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);*/

        setTitle("Bookings");

        tabLayout = (TabLayout) findViewById(R.id.booking_list_tabs);
        tabLayout.setTabGravity(TabLayout.MODE_FIXED);
        viewPager = (ViewPager) findViewById(R.id.booking_list_view_pager);

        BookingHistoryViewPagerAdapter adapter = new BookingHistoryViewPagerAdapter(getSupportFragmentManager());

        //Adding adapter to pager
        viewPager.setAdapter(adapter);

        //Adding onTabSelectedListener to swipe views
        tabLayout.setOnTabSelectedListener(this);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id)
        {
            case android.R.id.home:
                //BookingHistoryActivity.this.finish();
                goback();
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goback();
    }

    public void goback()
    {
        Intent main = new Intent(BookingHistoryActivity.this, DemoActivity.class);
        main.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(main);
        this.finish();
    }
}
