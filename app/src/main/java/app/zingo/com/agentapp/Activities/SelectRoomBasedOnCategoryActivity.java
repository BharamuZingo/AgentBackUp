package app.zingo.com.agentapp.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import app.zingo.com.agentapp.Adapter.CategorisedRoomListAdapter;
import app.zingo.com.agentapp.R;

public class SelectRoomBasedOnCategoryActivity extends AppCompatActivity {

    RecyclerView mCategorisedRoomList;
    Toolbar mSelectRoomBackBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_room_based_on_category);

        mCategorisedRoomList = (RecyclerView) findViewById(R.id.select_room_categorised_room_list);
        mSelectRoomBackBtn = (Toolbar) findViewById(R.id.select_room_back_btn);

        //mCategorisedRoomList.setAdapter(new CategorisedRoomListAdapter(SelectRoomBasedOnCategoryActivity.this,"",""));

        mSelectRoomBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectRoomBasedOnCategoryActivity.this.finish();
            }
        });
    }
}
