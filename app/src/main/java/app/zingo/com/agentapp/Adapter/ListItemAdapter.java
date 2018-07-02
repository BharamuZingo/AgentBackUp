package app.zingo.com.agentapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

import app.zingo.com.agentapp.Model.FreeAmenities;
import app.zingo.com.agentapp.R;

/**
 * Created by ZingoHotels.com on 4/19/2018.
 */

public class ListItemAdapter extends BaseAdapter {

    Context context;
    ArrayList<FreeAmenities> freeAmenitiesArrayList;
    CheckBox item;

    public ListItemAdapter(Context context, ArrayList<FreeAmenities> freeAmenitiesArrayList)
    {
        this.context = context;
        this.freeAmenitiesArrayList = freeAmenitiesArrayList;
    }
    @Override
    public int getCount() {
        return freeAmenitiesArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return freeAmenitiesArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {


        final Holder holder;
        /*if(view == null)
        {*/
        System.out.println("view null");
        holder = new Holder();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.amenities_list_item_layout,viewGroup,false);


        holder.textview = (TextView) view.findViewById(R.id.amenity_name);

        FreeAmenities freeAmenities = freeAmenitiesArrayList.get(i);
        if(freeAmenities != null)
        {
            holder.textview.setText(freeAmenities.getAmenitiesName());
        }


        return view;
    }

    public class Holder {
        public TextView textview;
        public CheckBox checkBox;

    }
}
