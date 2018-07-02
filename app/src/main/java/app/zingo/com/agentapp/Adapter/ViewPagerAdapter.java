package app.zingo.com.agentapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import app.zingo.com.agentapp.Activities.ImageFull;
import app.zingo.com.agentapp.R;


/**
 * Created by User on 31-07-2017.
 */

public class ViewPagerAdapter extends PagerAdapter {

    Context context;
    ArrayList<Integer> hotelImagesList;
    int hotelId;
    String activity;


    public ViewPagerAdapter(Context context, ArrayList<Integer> hotelImagesList,int hotelId, String activity)
    {
        this.context = context;
        this.hotelImagesList = hotelImagesList;
        this.hotelId = hotelId;
        this.activity = activity;

        /*options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .resetViewBeforeLoading(true).cacheOnDisk(true)
                .considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565)
                .build();*/
    }

    @Override
    public int getCount() {
        return hotelImagesList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View)object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View removableView = (View) object;
        container.removeView(removableView);

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        try{
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.custom_view_pager_layout,container,false);
            ImageView hotelImage = (ImageView) view.findViewById(R.id.hotel_image);
            hotelImage.setImageResource(hotelImagesList.get(position));
            //ImageLoader.getInstance().displayImage("file://" + selectedImageList.get(position), selectedImage, options);

            container.addView(view);
            hotelImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(activity.equalsIgnoreCase("HotelDetails")){
                        Intent intent = new Intent(context,ImageFull.class);
                        intent.putExtra("HotelId",hotelId);
                        context.startActivity(intent);
                    }else{

                    }

                }
            });
            return view;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }



    }
}