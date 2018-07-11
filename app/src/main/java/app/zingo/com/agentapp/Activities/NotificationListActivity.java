package app.zingo.com.agentapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import app.zingo.com.agentapp.Adapter.NotificationManagerAdapter;
import app.zingo.com.agentapp.DemoActivity;
import app.zingo.com.agentapp.MainActivity;
import app.zingo.com.agentapp.Model.NotificationManager;
import app.zingo.com.agentapp.R;
import app.zingo.com.agentapp.Utils.PreferenceHandler;
import app.zingo.com.agentapp.Utils.ThreadExecuter;
import app.zingo.com.agentapp.Utils.Util;
import app.zingo.com.agentapp.WebApi.NotificationApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationListActivity extends AppCompatActivity {

    private RecyclerView notification_list;
    private NotificationManagerAdapter adapter;
    private ArrayList<NotificationManager> list;
    private ProgressDialog progressDialog;
    View empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Notifications");
        notification_list = (RecyclerView) findViewById(R.id.notification_list);
        empty = (View)findViewById(R.id.empty);
        // getHotels();
        getNotification();
    }
    private void getNotification(){
        final ProgressDialog progressDialog = new ProgressDialog(NotificationListActivity.this);
        progressDialog.setTitle("please wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        PreferenceHandler ph = PreferenceHandler.getInstance(this);
        final int hotelId = ph.getUserId();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                NotificationApi apiService =
                        Util.getClient().create(NotificationApi.class);
                String authenticationString = Util.getToken(NotificationListActivity.this);
                Call<ArrayList<NotificationManager>> call = apiService.getNotification(authenticationString)/*getRooms()*/;

                call.enqueue(new Callback<ArrayList<NotificationManager>>() {
                    @Override
                    public void onResponse(Call<ArrayList<NotificationManager>> call, Response<ArrayList<NotificationManager>> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                        int statusCode = response.code();
                        if (progressDialog!=null) {
                            progressDialog.dismiss();
                        }
                        if (statusCode == 200) {

                            list =  response.body();
                            ArrayList<NotificationManager> nfm = new ArrayList<>();

//                            docList = list.get(1).getProfileList();

                            if(list != null && list.size() != 0)
                            {

                                for(int i=0;i<list.size();i++){
                                    if(list.get(i).getTravellerAgentId()==hotelId){
                                        nfm.add(list.get(i));
                                    }
                                }

                                if(nfm!=null && nfm.size()!=0){
                                    Collections.reverse(nfm);
                                    adapter = new NotificationManagerAdapter(NotificationListActivity.this, nfm);
                                    notification_list.setAdapter(adapter);
                                    empty.setVisibility(View.GONE);
                                }else{

                                }


                            }
                            else
                            {
                                /*Intent intent = new Intent(NotificationListActivity.this,AddRoomsActivity.class);
                                startActivity(intent);
                                NotificationListActivity.this.finish();*/
                            }
//                            Object dto = response.body();
//                            listCities.add(dto);



                        }else {
                            empty.setVisibility(View.VISIBLE);
                            Toast.makeText(NotificationListActivity.this, " failed due to status code:"+statusCode, Toast.LENGTH_SHORT).show();
                        }
//                callGetStartEnd();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<NotificationManager>> call, Throwable t) {
                        // Log error here since request failed
                        if (progressDialog!=null)
                            progressDialog.dismiss();
                        Log.e("TAG", t.toString());
                    }
                });
            }


        });
    }
    //777774
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id)
        {
            case android.R.id.home:
                goback();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void goback()
    {
        Intent main = new Intent(NotificationListActivity.this,DemoActivity.class);
        main.putExtra("ARG_PAGE",4);
        startActivity(main);
        NotificationListActivity.this.finish();
    }

}
