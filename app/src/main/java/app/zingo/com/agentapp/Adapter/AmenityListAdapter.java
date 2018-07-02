package app.zingo.com.agentapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

import app.zingo.com.agentapp.Model.PaidAmenities;
import app.zingo.com.agentapp.Model.PaidAmenitiesCategory;
import app.zingo.com.agentapp.R;

/**
 * Created by ZingoHotels Tech on 19-04-2018.
 */

public class AmenityListAdapter extends RecyclerView.Adapter<AmenityListAdapter.ViewHolder> {
    private Context context;
    ArrayList<PaidAmenitiesCategory> amenityList;
    String[] featureList;
    CheckBox item;

    public AmenityListAdapter(Context context, String[] featureList) {

        this.context = context;
        this.featureList = featureList;

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
       // PaidAmenitiesCategory dto = amenityList.get(position);
        String feature = featureList[position];


        holder.mAmenityName.setText(feature);
        item = holder.mCheckBox;
        /*if(item != null)
        {
            item.setChecked(!item.isChecked());
        }*/
        holder.mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.mCheckBox.setSelected(!holder.mCheckBox.isChecked());
            }
        });

    }

    @Override
    public int getItemCount() {
        return featureList.length;
    }


    class ViewHolder extends RecyclerView.ViewHolder /*implements View.OnClickListener*/ {

        public TextView mAmenityName;
        public CheckBox mCheckBox;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setClickable(true);

            mAmenityName = (TextView) itemView.findViewById(R.id.txtName);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.check_box);


        }
    }
}