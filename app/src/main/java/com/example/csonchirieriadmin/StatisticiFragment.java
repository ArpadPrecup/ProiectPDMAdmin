package com.example.csonchirieriadmin;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatisticiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatisticiFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    BarChart chart;

    DatabaseReference countReference, statisticsReference;

    public StatisticiFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StatisticiFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StatisticiFragment newInstance(String param1, String param2) {
        StatisticiFragment fragment = new StatisticiFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView =  inflater.inflate(R.layout.fragment_statistici, container, false);
        chart = (BarChart) fragmentView.findViewById(R.id.chart);
        getDataSet();
        return fragmentView;
    }

    private void getDataSet() {

        /*ArrayList dataSets = null;
        ArrayList valueSet1 = new ArrayList();
        BarEntry v1e1 = new BarEntry(5.000f, 0); // Jan
        valueSet1.add(v1e1);
        BarEntry v1e2 = new BarEntry(3.000f, 1); // Feb
        valueSet1.add(v1e2);
        BarEntry v1e3 = new BarEntry(2.000f, 2); // Mar
        valueSet1.add(v1e3);

        ArrayList valueSet2 = new ArrayList();
        BarEntry v2e1 = new BarEntry(14.000f, 0); // Jan
        valueSet2.add(v2e1);
        BarEntry v2e2 = new BarEntry(13.000f, 1); // Feb
        valueSet2.add(v2e2);
        BarEntry v2e3 = new BarEntry(15.000f, 2); // Mar
        valueSet2.add(v2e3);


        ArrayList valueSet3 = new ArrayList();
        BarEntry v3e1 = new BarEntry(20.000f, 0); // Jan
        valueSet3.add(v3e1);
        BarEntry v3e2 = new BarEntry(10.000f, 1); // Feb
        valueSet3.add(v3e2);
        BarEntry v3e3 = new BarEntry(15.000f, 2); // Mar
        valueSet3.add(v3e3);


        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Sintetic");
        barDataSet1.setColor(Color.rgb(0, 155, 0));
        BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Tenis");
        barDataSet2.setColors(new int[]{Color.rgb(255, 165, 0)});
        BarDataSet barDataSet3 = new BarDataSet(valueSet3, "Sala");
        barDataSet3.setColor(Color.rgb(56, 172, 237));


        System.out.println(valueSet1 + " e " + valueSet2 + " e " +  valueSet3);

        dataSets = new ArrayList();
        dataSets.add(barDataSet1);
        dataSets.add(barDataSet2);
        dataSets.add(barDataSet3);

        dataSets = new ArrayList();
                    dataSets.add(barDataSet1);
                    dataSets.add(barDataSet2);
                    dataSets.add(barDataSet3);
                    System.out.println(dataSets);

        BarData data = new BarData(getXAxisValues(), dataSets);
                    chart.setData(data);
                    //chart.setDescription((Description) "My Chart");
                    chart.animateXY(2000, 2000);
                    chart.invalidate();*/



        LocalDate currentDate = LocalDate.now();
        for (int i = currentDate.getMonthValue() - 2; i <= currentDate.getMonthValue(); i++)
        {
            setCount("sintetic", String.valueOf(currentDate.getYear()), String.valueOf(i));
            setCount("sala", String.valueOf(currentDate.getYear()), String.valueOf(i));
            setCount("tenis", String.valueOf(currentDate.getYear()), String.valueOf(i));
        }


    }

    private void setCount(String tip, String year, String month)
    {

        countReference = FirebaseDatabase.
                getInstance().
                getReference().
                child(tip).
                child(year)
                .child(month)
                .child("count");
        LocalDate currentDate = LocalDate.now();
        String getMonthDBName;
        if (month.equals(String.valueOf(currentDate.getMonthValue())))
            getMonthDBName = "currMonth";
        else if (month.equals(String.valueOf(currentDate.getMonthValue() - 1)))
            getMonthDBName = "lastMonth";
        else
            getMonthDBName = "lastLastMonth";
        DatabaseReference statisticiRef = FirebaseDatabase.
                getInstance().
                getReference().
                child("statistics").
                child(getMonthDBName).
                child(tip);

        countReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() == null)
                    statisticiRef.setValue(0);
                else
                    statisticiRef.setValue(snapshot.getValue());
                statisticsReference = FirebaseDatabase.getInstance().getReference().child("statistics");
                statisticsReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList valueSet1 = new ArrayList();
                        ArrayList valueSet2 = new ArrayList();
                        ArrayList valueSet3 = new ArrayList();
                        Map<String, Map<String, Integer>> stats= (Map<String, Map<String, Integer>>) snapshot.getValue();
                        for (Map.Entry<String, Map<String, Integer>> stat: stats.entrySet())
                        {



                            int index;
                            if (stat.getKey().equals("currMonth"))
                                index = 2;
                            else if (stat.getKey().equals("lastMonth"))
                                index = 1;
                            else{
                                index = 0;
                            }
                            for (Map.Entry<String, Integer> nrFieldsRented : stat.getValue().entrySet())
                            {
                                //System.out.println("In al doilea for " + nrFieldsRented.getKey() + " " + nrFieldsRented.getValue());

                                BarEntry barEntry = new BarEntry(Float.parseFloat(String.valueOf(nrFieldsRented.getValue())), index);
                                if (nrFieldsRented.getKey().equals("sintetic"))
                                {
                                    valueSet1.add(barEntry);
                                }
                                else if (nrFieldsRented.getKey().equals("tenis")){
                                    valueSet2.add(barEntry);
                                }
                                else
                                {
                                    valueSet3.add(barEntry);
                                }


                            }
                            System.out.println(valueSet1 + " e " + valueSet2 + " e " +  valueSet3);


                        }
                        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Sintetic");
                        barDataSet1.setColor(Color.rgb(0, 155, 0));
                        BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Tenis");
                        barDataSet2.setColors(new int[]{Color.rgb(255, 165, 0)});
                        BarDataSet barDataSet3 = new BarDataSet(valueSet3, "Sala");
                        barDataSet3.setColor(Color.rgb(56, 172, 237));

                        ArrayList dataSets = null;
                        dataSets = new ArrayList();
                        dataSets.add(barDataSet1);
                        dataSets.add(barDataSet2);
                        dataSets.add(barDataSet3);
                        // System.out.println(dataSets);



                        BarData data = new BarData(getXAxisValues(), dataSets);
                        chart.setData(data);
                        //chart.setDescription((Description) "My Chart");
                        chart.animateXY(2000, 2000);
                        chart.invalidate();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private ArrayList getXAxisValues() {
        ArrayList xAxis = new ArrayList();
        ArrayList months = new ArrayList<>();

        months.add("IAN");
        months.add("FEB");
        months.add("MAR");
        months.add("APR");
        months.add("MAI");
        months.add("IUN");
        months.add("IUL");
        months.add("AUG");
        months.add("SEP");
        months.add("OCT");
        months.add("NOI");
        months.add("DEC");
        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonthValue();
        for (int i = currentMonth-3; i < currentMonth; i++)
        {
            xAxis.add(months.get(i));
        }
        return xAxis;
    }
}