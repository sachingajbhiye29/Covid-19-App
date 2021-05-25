package com.sachin.covid_19tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sachin.covid_19tracker.api.ApiInterface;
import com.sachin.covid_19tracker.api.ApiUtilities;
import com.sachin.covid_19tracker.api.CountryData;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextView totalConfirm, totalActive, totalDeaths, totalRecovered, totalTests, cname;
    private TextView todayConfirm, todayRecovered, todayDeaths;
    private TextView date;
    private PieChart pieChart;
    private String country = "India"
;

    private List<CountryData> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = new ArrayList<>();

        //check the intent data;
        if(getIntent().getStringExtra("country")!= null){
            country = getIntent().getStringExtra("country");
        }

        init();

        cname = findViewById(R.id.cname);
        cname.setText(country);
        cname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CountryActivity.class));
            }
        });



        ApiUtilities.getInterface().getCountyData().enqueue(new Callback<List<CountryData>>() {
            @Override
            public void onResponse(Call<List<CountryData>> call, Response<List<CountryData>> response) {
                //this will store all the data coming from api inside the list
                list.addAll(response.body());

                //now loop to check for the specific country
                for(int i =0; i<list.size(); i++){
                    if(list.get(i).getCountry().equals(country)){

                        int confirm = Integer.parseInt(list.get(i).getCases());
                        int recovered = Integer.parseInt(list.get(i).getRecovered());
                        int active = Integer.parseInt(list.get(i).getActive());
                        int deaths = Integer.parseInt(list.get(i).getDeaths());

                        //NumberFormat will put the , in between the numbers --> 1,99,920
                        totalConfirm.setText(NumberFormat.getInstance().format(confirm));
                        totalRecovered.setText(NumberFormat.getInstance().format(recovered));
                        totalActive.setText(NumberFormat.getInstance().format(active));
                        totalDeaths.setText(NumberFormat.getInstance().format(deaths));
                        totalTests.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTests())));

                        todayConfirm.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayCases())));
                        todayRecovered.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayRecovered())));
                        todayDeaths.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayDeaths())));

                        setText(list.get(i).getUpdated());

                        pieChart.addPieSlice(new PieModel("Confirm", confirm, getResources().getColor(R.color.yellow)));
                        pieChart.addPieSlice(new PieModel("Recovered", recovered, getResources().getColor(R.color.blue_pie)));
                        pieChart.addPieSlice(new PieModel("Active", active, getResources().getColor(R.color.green_pie)));
                        pieChart.addPieSlice(new PieModel("Deaths", deaths, getResources().getColor(R.color.red_pie)));
                        pieChart.startAnimation();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<CountryData>> call, Throwable t) {
                Toast.makeText(MainActivity.this,t.getLocalizedMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private  void setText(String updated) {

        DateFormat format = new SimpleDateFormat("MMM, dd ,yyyy");

        long milliseconds = Long.parseLong(updated);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);

        date.setText("Updated at "+format.format(calendar.getTime()));

    }

    public void init(){
        totalConfirm = findViewById(R.id.totalConfirm);
        totalActive = findViewById(R.id.totalActive);
        totalDeaths = findViewById(R.id.totalDeaths);
        totalRecovered = findViewById(R.id.totalRecovered);
        totalTests = findViewById(R.id.totalTests);


        todayConfirm = findViewById(R.id.todayConfirm);
        todayDeaths = findViewById(R.id.todayDeaths);
        todayRecovered = findViewById(R.id.todayRecovered);

        date = findViewById(R.id.date);

        pieChart = findViewById(R.id.pieChart);
    }
}