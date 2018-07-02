package app.zingo.com.agentapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import app.zingo.com.agentapp.Model.HotelService;
import app.zingo.com.agentapp.R;


/**
 * Created by User on 23-09-2017.
 */

public class GridViewAdapter extends BaseAdapter {
    Context context;
    ArrayList<HotelService> serviceList;

    public GridViewAdapter(Context context, ArrayList<HotelService> serviceList)
    {
        this.context = context;
        this.serviceList = serviceList;
    }
    @Override
    public int getCount() {
        return serviceList.size();
    }

    @Override
    public Object getItem(int position) {
        return serviceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.services_layout,parent,false);
        }

        ImageView serviceImage = (ImageView) convertView.findViewById(R.id.service_image);
        TextView serviceName = (TextView) convertView.findViewById(R.id.service_name);

        serviceImage.setImageResource(serviceList.get(position).getServiceImage());
        serviceName.setText(serviceList.get(position).getServiceName());

        return convertView;
    }
}
