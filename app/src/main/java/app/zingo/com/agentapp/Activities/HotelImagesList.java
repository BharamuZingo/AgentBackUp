package app.zingo.com.agentapp.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

/*import app.zingo.com.hotelmanagement.Model.HotelImage;
import app.zingo.com.hotelmanagement.R;
import app.zingo.com.hotelmanagement.Util.PreferenceHandler;
import app.zingo.com.hotelmanagement.Util.ThreadExecuter;
import app.zingo.com.hotelmanagement.Util.Util;
import app.zingo.com.hotelmanagement.WebApi.UploadHotelImagesApi;*/

import app.zingo.com.agentapp.Model.HotelImage;
import app.zingo.com.agentapp.R;
import app.zingo.com.agentapp.Utils.ThreadExecuter;
import app.zingo.com.agentapp.Utils.Util;
import app.zingo.com.agentapp.WebApi.UploadHotelImagesApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HotelImagesList extends AppCompatActivity {

    LinearLayout mParent;
    ImageView mNoImages;
    FloatingActionButton mAddImagesBtn;


    ArrayList<HotelImage> list;
    int hotelId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_images_list);


        setTitle("Hotel Imges");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        hotelId = getIntent().getIntExtra("HotelId",0);

        mParent = (LinearLayout) findViewById(R.id.added_hotel_images_parent);
        mNoImages = (ImageView)findViewById(R.id.no_images_added);
        mAddImagesBtn = (FloatingActionButton) findViewById(R.id.add_hotel_images);

        //getHotelImages();

        /*mAddImagesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HotelImagesList.this,HotelImageUpload.class);
                startActivity(intent);
            }
        });*/
    }

    public void addView(HotelImage hotelImage)
    {
        LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.gallery_layout, null);
        ImageView imageView =(ImageView) v.findViewById(R.id.hotel_images);
        //EditText textView = (EditText)v.findViewById(R.id.hotel_image_caption);

        if(hotelImage !=null)
        {

            if(hotelImage.getImages() != null && !hotelImage.getImages().isEmpty())
            {
                Bitmap bitmap = Util.decodeBase64(hotelImage.getImages());
                if(bitmap != null)
                {
                    imageView.setImageBitmap(bitmap);
                    //textView.setText(hotelImage.getCaption());
                    //textView.setEnabled(false);
                }
            }
            mParent.addView(v);

        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        getHotelImages();
    }

    public void getHotelImages()
    {
        final ProgressDialog dialog = new ProgressDialog(HotelImagesList.this);
        dialog.setMessage(getResources().getString(R.string.loader_message));
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                UploadHotelImagesApi api = Util.getClient().create(UploadHotelImagesApi.class);
                String auth = Util.getToken(HotelImagesList.this);
                final Call<ArrayList<HotelImage>> HotelImagereaponse = api.getHotelImages(auth, hotelId);

                HotelImagereaponse.enqueue(new Callback<ArrayList<HotelImage>>() {
                    @Override
                    public void onResponse(Call<ArrayList<HotelImage>> call, Response<ArrayList<HotelImage>> response) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        if(response.code() == 200 || response.code() == 201)
                        {
                            if(response.body() != null && response.body().size() != 0)
                            {
                                mNoImages.setVisibility(View.GONE);
                                mParent.setVisibility(View.VISIBLE);

                                for (int i=0;i<response.body().size();i++)
                                {
                                    addView(response.body().get(i));
                                }
                            }
                            else
                            {

                                mNoImages.setVisibility(View.VISIBLE);
                                mParent.setVisibility(View.GONE);
                            }
                        }
                        else
                        {


                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<HotelImage>> call, Throwable t) {
                        System.out.println(t.getMessage());
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        Toast.makeText(HotelImagesList.this,"Please Check your data connection", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id)
        {
            case android.R.id.home:
                HotelImagesList.this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
