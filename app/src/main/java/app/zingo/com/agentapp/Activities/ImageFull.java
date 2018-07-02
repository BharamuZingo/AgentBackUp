package app.zingo.com.agentapp.Activities;

import android.app.ProgressDialog;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import app.zingo.com.agentapp.Adapter.GridViewAdapter;
import app.zingo.com.agentapp.Adapter.ImageAdapter;
import app.zingo.com.agentapp.Adapter.ViewPagerAdapter;
import app.zingo.com.agentapp.CustomViews.CustomGridView;
import app.zingo.com.agentapp.Model.HotelImage;
import app.zingo.com.agentapp.Model.HotelService;
import app.zingo.com.agentapp.R;
import app.zingo.com.agentapp.Utils.ThreadExecuter;
import app.zingo.com.agentapp.Utils.Util;
import app.zingo.com.agentapp.WebApi.UploadHotelImagesApi;
import at.blogc.android.views.ExpandableTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ImageFull extends AppCompatActivity {

    ViewPager viewPager;
    Button mClose;

    ArrayList<HotelImage> list;
    int hotelId;



    ArrayList<Integer> hotelImagesArraylist;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        try{
            hotelId = getIntent().getIntExtra("HotelId",0);

            setContentView(R.layout.activity_image_full);


            viewPager = (ViewPager) findViewById(R.id.pager_image);
            mClose = (Button) findViewById(R.id.btnClose);
            mClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageFull.this.finish();
                }
            });


            TypedArray hotelImages = getResources().obtainTypedArray(R.array.hotel_images);


            hotelImagesArraylist = new ArrayList<>();

            for (int i=0;i<hotelImages.length();i++)
            {
                hotelImagesArraylist.add(hotelImages.getResourceId(i,-1));
            }

            getHotelImages();


            viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                /*addBottomDots(position);*/
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }



    }

    public void getHotelImages() {
        final ProgressDialog dialog = new ProgressDialog(ImageFull.this);
        dialog.setMessage(getResources().getString(R.string.loader_message));
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                UploadHotelImagesApi api = Util.getClient().create(UploadHotelImagesApi.class);
                String auth = Util.getToken(ImageFull.this);
                final Call<ArrayList<HotelImage>> HotelImagereaponse = api.getHotelImages(auth, hotelId);

                HotelImagereaponse.enqueue(new Callback<ArrayList<HotelImage>>() {
                    @Override
                    public void onResponse(Call<ArrayList<HotelImage>> call, Response<ArrayList<HotelImage>> response) {
                        if (dialog != null) {
                            dialog.dismiss();
                        }

                        try{
                            if (response.code() == 200 || response.code() == 201) {
                                if (response.body() != null && response.body().size() != 0) {
                                    list =response.body();
                                    ImageAdapter hotelImagesadapter = new ImageAdapter(ImageFull.this,list,hotelId,"ImageFull");
                                    viewPager.setAdapter(hotelImagesadapter);

                                } else {

                                    ViewPagerAdapter hotelImagesadapter = new ViewPagerAdapter(ImageFull.this,hotelImagesArraylist,hotelId,"ImageFull");
                                    viewPager.setAdapter(hotelImagesadapter);
                                }
                            } else {


                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(Call<ArrayList<HotelImage>> call, Throwable t) {
                        System.out.println(t.getMessage());
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        Toast.makeText(ImageFull.this, "Please Check your data connection", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
