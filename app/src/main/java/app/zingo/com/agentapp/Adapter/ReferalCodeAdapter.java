package app.zingo.com.agentapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import app.zingo.com.agentapp.Activities.ReviewHotelDetailsActivity;
import app.zingo.com.agentapp.Model.NotificationManager;
import app.zingo.com.agentapp.Model.TravellerAgentProfiles;
import app.zingo.com.agentapp.R;

/**
 * Created by ZingoHotels Tech on 10-05-2018.
 */

public class ReferalCodeAdapter extends RecyclerView.Adapter<ReferalCodeAdapter.ViewHolder> {

    private Context context;
    private ArrayList<TravellerAgentProfiles> profiles;

    public ReferalCodeAdapter(Context context,ArrayList<TravellerAgentProfiles> profiles)
    {
        this.context = context;
        this.profiles = profiles;

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_referal_profiles,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final TravellerAgentProfiles dto = profiles.get(position);
        holder.mProfileName.setText(""+dto.getFirstName());


    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mProfileName;

        View itemView;
        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            mProfileName = (TextView) itemView.findViewById(R.id.referal_name);

        }
    }
}
