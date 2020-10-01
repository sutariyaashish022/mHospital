package com.app.health;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by apple on 03/04/18.
 */

public class LazyAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<String> name;
    private ArrayList<String> email;
    private static LayoutInflater inflater=null;

    public LazyAdapter(Activity a, ArrayList<String> n,ArrayList<String> e) {
        activity = a;
        name = n;
        email  = e;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return name.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.listrow, null);

        TextView title = (TextView)vi.findViewById(R.id.title); // title
        TextView artist = (TextView)vi.findViewById(R.id.artist); // artist name// thumb image



        // Setting all values in listview
        title.setText(name.get(position));
        artist.setText(email.get(position));

        return vi;
    }
}