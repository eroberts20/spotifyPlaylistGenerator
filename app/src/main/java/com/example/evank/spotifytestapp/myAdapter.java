package com.example.evank.spotifytestapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import kaaes.spotify.webapi.android.models.Track;

import java.util.ArrayList;
import java.util.Map;

public class myAdapter extends BaseAdapter {
    private final ArrayList mData;

    public myAdapter(Map<Track, Integer> map) {
        mData = new ArrayList();
        mData.addAll(map.entrySet());
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Map.Entry<Track, Integer> getItem(int position) {
        return (Map.Entry) mData.get(position);
    }

    @Override
    public long getItemId(int position) { // TODO implement you own logic with ID
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View result;

        if (convertView == null) {
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_listview, parent, false);
        } else {
            result = convertView;
        }

        Map.Entry<Track, Integer> item = getItem(position);

        // TODO replace findViewById by ViewHolder
        ((TextView) result.findViewById(android.R.id.text1)).setText(item.getKey().name);
        ((TextView) result.findViewById(android.R.id.text2)).setText(item.getValue());

        return result;
    }

}
