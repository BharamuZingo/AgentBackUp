package app.zingo.com.agentapp.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import app.zingo.com.agentapp.R;

public class SelectRoomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_select_room);
        setContentView(R.layout.rooms_list_adapter_layout);
    }
}
