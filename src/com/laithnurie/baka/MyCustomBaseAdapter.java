package com.laithnurie.baka;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyCustomBaseAdapter extends BaseAdapter {
    private static ArrayList<Manga> mangaList;
 
    private LayoutInflater mInflater;
 
    public MyCustomBaseAdapter(Context context, ArrayList<Manga> results) {
        mangaList = results;
        mInflater = LayoutInflater.from(context);
    }
 
    public int getCount() {
        return mangaList.size();
    }
 
    public Object getItem(int position) {
        return mangaList.get(position);
    }
 
    public long getItemId(int position) {
        return position;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.custom_row_view, null);
            holder = new ViewHolder();
            holder.txtName = (TextView) convertView.findViewById(R.id.manga);
            holder.txtCityState = (TextView) convertView
                    .findViewById(R.id.chapter);
            holder.txtPhone = (TextView) convertView.findViewById(R.id.desc);
 
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
 
        holder.txtName.setText(mangaList.get(position).getManga());
        holder.txtCityState.setText(mangaList.get(position)
                .getChapter());
        holder.txtPhone.setText(mangaList.get(position).getDesc());
 
        return convertView;
    }
 
    static class ViewHolder {
        TextView txtName;
        TextView txtCityState;
        TextView txtPhone;
    }
}
