package com.example.myproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {

    private ArrayList<Data_Upload> dataList;
    private Context context;
    LayoutInflater inflater;

    public MyAdapter(ArrayList<Data_Upload> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void searchDataList(ArrayList<Data_Upload> searchList) {
        dataList = searchList;
        notifyDataSetChanged(); // This line notifies the adapter of the data change.
    }


    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"SetTextI18n", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.grid_item, null);
            holder = new ViewHolder();
            holder.gridImage = convertView.findViewById(R.id.gridImage);
            holder.gridCaption = convertView.findViewById(R.id.gridTitle);
            holder.gridPrice = convertView.findViewById(R.id.gridPrice);
            holder.gridDesc = convertView.findViewById(R.id.gridDesc);
            holder.gridCard = convertView.findViewById(R.id.gridCard);
            holder.gridLocation = convertView.findViewById(R.id.gridLocation);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Data_Upload dataUpload = dataList.get(position);

        // placholder
        holder.gridImage.setImageResource(R.drawable.pawpet_match_cp);

        // ito ung dinagdag ko hehe
        if (dataUpload.getuImageUrl() != null && !dataUpload.getuImageUrl().isEmpty()) {
            Glide.with(context).load(dataUpload.getuImageUrl()).into(holder.gridImage);
        }

        holder.gridPrice.setText("Price: " + dataList.get(position).getuPrice());
        holder.gridDesc.setText(dataUpload.getuDescription());
        holder.gridCaption.setText("Title: " + dataUpload.getuTitle());
        holder.gridLocation.setText("Location: " + dataUpload.getuLocation());


        holder.gridCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, activity_detail.class);
                intent.putExtra("Image", dataUpload.getuImageUrl());
                intent.putExtra("Description", "Description: " + dataUpload.getuDescription());
                intent.putExtra("Title", "Title: " + dataUpload.getuTitle());
                intent.putExtra("Location", "Location: " +  dataUpload.getuLocation());
                //step 3 delete button
                intent.putExtra("Key", dataUpload.getKey());
                intent.putExtra("Price","Price: " + dataUpload.getuPrice().toString());
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    static class ViewHolder {
        ImageView gridImage;
        TextView gridCaption;
        TextView gridPrice;

        TextView gridLocation;
        TextView gridDesc;
        CardView gridCard;
    }
}
