package app.zingo.com.agentapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import app.zingo.com.agentapp.Model.NavBarItems;
import app.zingo.com.agentapp.R;

/**
 * Created by Benayah on 9/5/2017.
 */

public class NavigationListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<NavBarItems> mList = new ArrayList<>();

    public NavigationListAdapter(Context context, ArrayList<NavBarItems> mList)
    {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int pos) {
        return mList.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int pos, View view, ViewGroup viewGroup) {

        if(view == null)
        {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = mInflater.inflate(R.layout.navbar_list_item_layout,viewGroup,false);
        }

        TextView mTitle = (TextView) view.findViewById(R.id.title);
        ImageView mIcon = (ImageView) view.findViewById(R.id.icon);

        mTitle.setText(mList.get(pos).getTitle().toString());
        mIcon.setImageResource(mList.get(pos).getIcon());
        return view;
    }
}
