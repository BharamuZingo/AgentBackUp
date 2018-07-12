package app.zingo.com.agentapp.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import app.zingo.com.agentapp.Activities.HotelDetailsActivity;
import app.zingo.com.agentapp.Activities.HotelDetailsHourlyActivity;
import app.zingo.com.agentapp.Activities.ReviewHotelDetailsActivity;
import app.zingo.com.agentapp.Model.Traveller;
import app.zingo.com.agentapp.R;

/**
 * Created by ZingoHotels.com on 5/30/2018.
 */

public class AutocompleteCustomArrayAdapter extends ArrayAdapter<Traveller> {

    final String TAG = "AutocompleteCustomArrayAdapter.java";

    Context mContext;
    int layoutResourceId;
    String activity;
    ArrayList<Traveller> data = null;

    public AutocompleteCustomArrayAdapter(Context mContext, int layoutResourceId, ArrayList<Traveller> data,String activity) {

        super(mContext, layoutResourceId, data);

        this.layoutResourceId = layoutResourceId;
        this.activity = activity;
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        try{

            /*
             * The convertView argument is essentially a "ScrapView" as described is Lucas post
             * http://lucasr.org/2012/04/05/performance-tips-for-androids-listview/
             * It will have a non-null value when ListView is asking you recycle the row layout.
             * So, when convertView is not null, you should simply update its contents instead of inflating a new row layout.
             */
            if(convertView==null){
                // inflate the layout
                if(activity!=null&&activity.equalsIgnoreCase("HotelDetailsActivity")){
                    LayoutInflater inflater = ((HotelDetailsActivity) mContext).getLayoutInflater();
                    convertView = inflater.inflate(layoutResourceId, parent, false);
                }else if(activity!=null&&activity.equalsIgnoreCase("HotelDetailsHourlyActivity")){
                    LayoutInflater inflater = ((HotelDetailsHourlyActivity) mContext).getLayoutInflater();
                    convertView = inflater.inflate(layoutResourceId, parent, false);
                }else{
                    LayoutInflater inflater = ((ReviewHotelDetailsActivity) mContext).getLayoutInflater();
                    convertView = inflater.inflate(layoutResourceId, parent, false);
                }

            }

            // object item based on the position
            Traveller objectItem = data.get(position);

            // get the TextView and then set the text (item name) and tag (item ID) values
            TextView textViewItem = (TextView) convertView.findViewById(R.id.added_hotel_name);
            textViewItem.setText(objectItem.getFirstName());

            // in case you want to add some style, you can do something like:
            textViewItem.setBackgroundColor(Color.CYAN);

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;

    }
}
