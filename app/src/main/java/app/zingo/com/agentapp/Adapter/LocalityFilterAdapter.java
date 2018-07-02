package app.zingo.com.agentapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import app.zingo.com.agentapp.Activities.ImageFull;
import app.zingo.com.agentapp.Model.DataModel;
import app.zingo.com.agentapp.Model.HotelDetails;
import app.zingo.com.agentapp.Model.NavBarItems;
import app.zingo.com.agentapp.R;

/**
 * Created by ZingoHotels Tech on 19-04-2018.
 */

public class LocalityFilterAdapter extends RecyclerView.Adapter<LocalityFilterAdapter.ViewHolder> {
    private Context context;
    ArrayList<String> hotelLocalityList;


    public LocalityFilterAdapter(Context context, ArrayList<String> hotelLocalityList) {

        this.context = context;
        this.hotelLocalityList = hotelLocalityList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_locality_filter, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        String dto = hotelLocalityList.get(position);


        holder.mLocalityName.setText(dto.toString());
        holder.mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.mCheckBox.setSelected(!holder.mCheckBox.isChecked());
            }
        });


    }

    @Override
    public int getItemCount() {
        return hotelLocalityList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder /*implements View.OnClickListener*/ {

        public TextView mLocalityName;
        public CheckBox mCheckBox;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setClickable(true);

            mLocalityName = (TextView) itemView.findViewById(R.id.txtName);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.check_box);

        }
    }
}