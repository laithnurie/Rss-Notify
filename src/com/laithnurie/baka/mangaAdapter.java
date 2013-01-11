package com.laithnurie.baka;

import java.util.ArrayList;

import com.laithnurie.baka.Manga;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class mangaAdapter extends BaseAdapter {
    private static ArrayList<Manga> mangaList;
    private LayoutInflater mInflater;
 
    public mangaAdapter(Context context, ArrayList<Manga> results) {
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
            holder.txtManga = (TextView) convertView.findViewById(R.id.manga);
            holder.txtChapter = (TextView) convertView.findViewById(R.id.chapter);
            holder.txtDesc = (TextView) convertView.findViewById(R.id.desc);
            holder.txtDate = (TextView) convertView.findViewById(R.id.pubDate);  
 
            convertView.setTag(holder);
        } 
        else {
            holder = (ViewHolder) convertView.getTag();
        }
 
        holder.txtManga.setText(mangaList.get(position).getManga());
        holder.txtChapter.setText(mangaList.get(position).getChapter());
        holder.txtDesc.setText(mangaList.get(position).getDesc());
        holder.txtDate.setText(mangaList.get(position).getDate());

        return convertView;
    }
 
    static class ViewHolder {
        TextView txtManga;
        TextView txtChapter;
        TextView txtDesc;
        TextView txtDate;
    }
}
