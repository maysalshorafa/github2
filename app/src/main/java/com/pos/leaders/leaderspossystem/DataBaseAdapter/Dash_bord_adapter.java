package com.pos.leaders.leaderspossystem.DataBaseAdapter;

/**
 * Created by Win8.1 on 6/11/2017.
 */
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pos.leaders.leaderspossystem.R;


public class Dash_bord_adapter  extends BaseAdapter{

    private Context mContext;
    private final String[] dash_bord_text;
    private final int[] Imageid;
    public Dash_bord_adapter(Context c,String[] web,int[] Imageid ) {
        mContext = c;
        this.Imageid = Imageid;
        this.dash_bord_text = web;
    }

    @Override
    public int getCount() {
        return dash_bord_text.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View grid=view;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (grid == null) {

            grid = inflater.inflate(R.layout.dash_grid_layout_item, null);
            TextView textView = (TextView) grid.findViewById(R.id.grid_text);
            ImageView imageView = (ImageView)grid.findViewById(R.id.grid_image);
            textView.setText(dash_bord_text[i]);
            imageView.setImageResource(Imageid[i]);
        //    grid.setBackgroundColor(Color.parseColor(color));
        } else {
            grid = view;
        }


        return grid;
    }

}
