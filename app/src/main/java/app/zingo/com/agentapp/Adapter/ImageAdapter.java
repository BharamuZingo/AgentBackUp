package app.zingo.com.agentapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

import app.zingo.com.agentapp.Activities.HotelDetailsActivity;
import app.zingo.com.agentapp.Activities.HotelImagesList;
import app.zingo.com.agentapp.Activities.ImageFull;
import app.zingo.com.agentapp.Model.HotelImage;
import app.zingo.com.agentapp.R;
import app.zingo.com.agentapp.Utils.Util;

/**
 * Created by ZingoHotels Tech on 25-04-2018.
 */

public class ImageAdapter extends PagerAdapter {

    Context context;
    ArrayList<HotelImage> hotelImagesList;
    int hotelId;
    String activity;


    public ImageAdapter(Context context, ArrayList<HotelImage> hotelImagesList,int hotelId, String activity)
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        try{
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.custom_view_pager_layout,container,false);
            ImageView hotelImage = (ImageView) view.findViewById(R.id.hotel_image);
            /*hotelImage.setVisibility(View.GONE);
            ImageView hotelImage1 = (ImageView) view.findViewById(R.id.hotel_image2);*/

            /*Bitmap bitmap = Util.decodeBase64(hotelImagesList.get(position).getImages());
            if(bitmap != null)
            {
                hotelImage.setImageBitmap(bitmap);
                //textView.setText(hotelImage.getCaption());
                //textView.setEnabled(false);
            }*/
            getImageFromServer(hotelImagesList.get(position).getImages(),hotelImage);
            //hotelImage.setImageResource(hotelImagesList.get(position));
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void getImageFromServer(String s, final ImageView im) {
        /*Picasso.with(ImageUploadActivity.this).load(s).placeholder(R.drawable.user).
                error(R.drawable.warning).into(uploadedimage);*/
        //System.out.println("Started = "+System.currentTimeMillis());
        /*AnimationDrawable animPlaceholder = (AnimationDrawable)context.getDrawable(R.drawable.loading);
        animPlaceholder.start();*/
        Glide.with(context)
                .load(s)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        /*im.setVisibility(View.GONE);
                        im2.setVisibility(View.VISIBLE);
                        System.out.println("onException = "+System.currentTimeMillis());*/
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        System.out.println("onResourceReady = "+System.currentTimeMillis());
                        /*im.setVisibility(View.VISIBLE);
                        im2.setVisibility(View.GONE);*/
                        return false;
                    }
                })
                .into(im);
    }
}
