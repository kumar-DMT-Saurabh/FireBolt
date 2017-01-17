package com.skull.red.firebolt;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by Kumar Saurabh on 1/17/2017.
 */

public class MyCustomAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<Repository> repos;

    private class ViewHolder
    {
        TextView nameView, descriptionView, loginView;
    }
    public MyCustomAdapter (Context context, ArrayList<Repository>repos)
    {
        inflater=LayoutInflater.from(context);
        this.repos=repos;
    }

    public int getCount()
    {
        return repos.size();
    }

    public Repository getItem(int position)
    {
        return repos.get(position);
    }
    public long getItemId(int position)
    {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder=null;

        if(convertView==null)
            {
                holder=new ViewHolder();
                convertView= inflater.inflate(R.layout.activity_main,null);
                holder.nameView=(TextView) convertView.findViewById(R.id.name);
                holder.descriptionView=(TextView) convertView.findViewById(R.id.desc);
                holder.loginView=(TextView) convertView.findViewById(R.id.login);
                convertView.setTag(holder);
            }
        else
            {
                holder=(ViewHolder)convertView.getTag();
            }
        holder.nameView.setText(repos.get(position).getName());
        holder.loginView.setText(repos.get(position).getLogin());
        holder.descriptionView.setText(repos.get(position).getDescription());
        if(repos.get(position).isFork())
        {
            holder.nameView.setBackgroundColor(Color.WHITE);
            holder.loginView.setBackgroundColor(Color.WHITE);
            holder.descriptionView.setBackgroundColor(Color.WHITE);
        }
        else
        {
            holder.nameView.setBackgroundColor(Color.GREEN);
            holder.loginView.setBackgroundColor(Color.GREEN);
            holder.descriptionView.setBackgroundColor(Color.GREEN);
        }
        return convertView;
    }

}
