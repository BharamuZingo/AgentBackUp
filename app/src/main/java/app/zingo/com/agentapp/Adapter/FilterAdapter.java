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

import app.zingo.com.agentapp.Model.DataModel;
import app.zingo.com.agentapp.Model.FreeAmenities;
import app.zingo.com.agentapp.R;

/**
 * Created by ZingoHotels Tech on 24-05-2018.
 */

public class FilterAdapter extends BaseAdapter {

    Context context;
    ArrayList<DataModel> items;
    CheckBox item;

    public FilterAdapter(Context context, ArrayList<DataModel> items)
    {
        this.context = context;
        this.items = items;
    }
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        final Holder holder;

        System.out.println("view null");
        holder = new Holder();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.adapter_locality_filter,viewGroup,false);

        holder.checkBox = (CheckBox) view.findViewById(R.id.check_box);
        holder.textview = (TextView) view.findViewById(R.id.txtName);



        holder.checkBox.setChecked(items.get(i).checked);
        holder.textview.setText(items.get(i).name);

        //holder.roomNumerTextView.setChecked(items.get(i).getIsSelected());
        item = holder.checkBox;
        if(item != null)
        {
            item.setChecked(items.get(i).checked);
        }

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //floorsList.get(i).checked = isChecked;
            }
        });



        return view;
    }

    public class Holder {
        public TextView textview;
        public CheckBox checkBox;

    }

}
