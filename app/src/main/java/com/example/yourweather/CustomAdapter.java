package com.example.yourweather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    Context context;
    ArrayList<ThoiTiet> arrayList;

    public CustomAdapter(Context context, ArrayList<ThoiTiet> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.dong_listview,null);

        ThoiTiet thoiTiet = arrayList.get(position);
        TextView txtDay = view.findViewById(R.id.textviewNgay);
        TextView txtStatus = view.findViewById(R.id.textviewTranghai);
        TextView txtMaxTemp = view.findViewById(R.id.textviewMaxTemp);
        TextView txtMinTemp = view.findViewById(R.id.textviewMinTemp);
        ImageView ImgStatus = view.findViewById(R.id.imageviewBack);

        txtDay.setText(thoiTiet.day);
        txtStatus.setText(thoiTiet.status);
        txtMaxTemp.setText(thoiTiet.maxTemp+"C");
        txtMinTemp.setText(thoiTiet.minTemp+"C");

        Picasso.with(context).load("http://openweathermap.org/img/w/" + thoiTiet.image + ".png").into(ImgStatus);
        return view;
    }
}
