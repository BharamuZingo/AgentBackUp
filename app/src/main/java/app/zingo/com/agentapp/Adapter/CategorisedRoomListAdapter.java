package app.zingo.com.agentapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import app.zingo.com.agentapp.Activities.ReviewHotelDetailsActivity;
import app.zingo.com.agentapp.R;

/**
 * Created by ZingoHotels.com on 16-01-2018.
 */

public class CategorisedRoomListAdapter extends RecyclerView.Adapter<CategorisedRoomListAdapter.ViewHolder> {

    private Context context;
    private String checkindate,checkoutdate,room;
    private int displayprice,sellprice;
    public CategorisedRoomListAdapter(Context context,String checkindate,String checkoutdate,int displayprice,int sellprice,String room)
    {
        this.context = context;
        this.checkindate = checkindate;
        this.checkoutdate = checkoutdate;
        this.displayprice = displayprice;
        this.sellprice = sellprice;
        this.room = room;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_room_list_adapter_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        holder.mDisplaprice.setText("₹ "+displayprice);
        holder.mSellPrice.setText("₹ "+sellprice);
        if(displayprice!= 0 && sellprice != 0)
        {
            double diff = displayprice - sellprice;
            //System.out.println("diff = "+diff);
            double div = diff/displayprice;
            //System.out.println("div = "+div);
            double dis = div*100;
            //System.out.println("dis = "+dis);
            holder.mDiscount.setText(" "+dis+"% Discount");
        }
        else
        {
            holder.mDiscount.setText(" "+0+"% Discount");
        }

        holder.mSelectedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(context, ReviewHotelDetailsActivity.class);
                intent.putExtra("HotelId",1);
                intent.putExtra("Price",sellprice);

                intent.putExtra("CheckinDate",checkindate);
                intent.putExtra("CheckoutDate",checkoutdate);
                intent.putExtra("Room",room);

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mSelectedBtn,mDisplaprice,mSellPrice,mDiscount;

        View itemView;
        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            mSelectedBtn = (TextView) itemView.findViewById(R.id.selected_room_btn);
            mDisplaprice = (TextView) itemView.findViewById(R.id.select_room_display_rate);
            mSellPrice = (TextView) itemView.findViewById(R.id.select_room_sell_rate);
            mDiscount = (TextView) itemView.findViewById(R.id.select_room_discount);
        }
    }
}
