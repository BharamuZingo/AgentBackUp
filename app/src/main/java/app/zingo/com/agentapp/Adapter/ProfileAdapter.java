package app.zingo.com.agentapp.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import app.zingo.com.agentapp.Model.Profile1;
import app.zingo.com.agentapp.Model.Traveller;
import app.zingo.com.agentapp.R;

/**
 * Created by ZingoHotels.com on 5/30/2018.
 */

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder>{

    private Context context;
    private ArrayList<Traveller> list;
    ProgressDialog dialog;

    public ProfileAdapter(Context context, ArrayList<Traveller> list) {

        this.context = context;
        this.list = list;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hotels_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Traveller dto = list.get(position);
        holder.mHotelName.setText(dto.getFirstName());
        //holder.mHotelPlace.setText(String.valueOf(dto.getHotelStreetAddress()));
//        holder.long_description.setText(dto.getLongDescription());

        /*holder.hotelOverviewlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
//                final City city = citiesList.get(pos);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Do you want to update hotel details");
                builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("UpdateHotel",dto);
                        Intent intent = new Intent(context,AddHotelActivity.class);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                });
                builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //dialogInterface.dismiss();
                        deleteCity(dto);
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });*/
//
//        holder.check_in_date.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context,"Go for check inn....",Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder /*implements View.OnClickListener*/ {

        TextView mHotelName,mHotelPlace;
        LinearLayout hotelOverviewlayout;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setClickable(true);
//
            mHotelName = (TextView)  itemView.findViewById(R.id.added_hotel_name);
            mHotelPlace = (TextView)  itemView.findViewById(R.id.added_hotel_place);
            hotelOverviewlayout = (LinearLayout)  itemView.findViewById(R.id.hotel_overview);//

        }
    }

}
