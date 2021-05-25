package com.sachin.covid_19tracker;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sachin.covid_19tracker.api.CountryData;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.microedition.khronos.opengles.GL;

public class countryAdapter  extends RecyclerView.Adapter<countryAdapter.CountryViewHolder> {

    private Context context;
    private List<CountryData> list;

    public countryAdapter(Context context, List<CountryData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public CountryViewHolder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.country_item_layout, parent, false);

        return new CountryViewHolder(view);
    }

    //this method is to filter the list or search for specific country

    public void filterList(List<CountryData> filterList ){

        list = filterList;
        notifyDataSetChanged();

    }

    @Override
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull countryAdapter.CountryViewHolder holder, int position) {

        CountryData data = list.get(position);

        holder.countryCases.setText(NumberFormat.getInstance().format(Integer.parseInt(data.getCases())));
        holder.countryName.setText(data.getCountry());
        holder.sno.setText(String.valueOf(position+1));

        //to get image of flag we will create map data

        Map<String , String > img = data.getCountryInfo();
        Glide.with(context).load(img.get("flag")).into(holder.imgFlag);


        //this is to show the data of specific country in main activity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("country", data.getCountry());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CountryViewHolder extends RecyclerView.ViewHolder{

        TextView sno, countryName, countryCases;
        ImageView imgFlag;

        public CountryViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
            super(itemView);

            sno = itemView.findViewById(R.id.sno);
            countryName = itemView.findViewById(R.id.countryName);
            countryCases = itemView.findViewById(R.id.countryCases);
            imgFlag = itemView.findViewById(R.id.flags);

        }
    }
}
