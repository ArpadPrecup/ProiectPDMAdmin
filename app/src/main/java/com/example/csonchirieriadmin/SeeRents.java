package com.example.csonchirieriadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SeeRents extends AppCompatActivity {
    date selectedDate;
    LinearLayout lLayout, lLayoutSintetic, lLayoutSala, lLayoutTenis;
    public static final String OPTION_TENIS = "tenis";
    public static final String OPTION_SINTETIC = "sintetic";
    public static final String OPTION_SALA = "sala";
    DatabaseReference databaseReference , reffDel;
    LinkedHashMap<String, List<Button>> rents = new LinkedHashMap<String, List<Button>>();
    List<Button> buttons = new ArrayList<>();
    public static Integer SINTETIC_CODE = 1;
    public static Integer SALA_CODE = 2;
    public static Integer TENIS_CODE = 3;
    TextView txtViewShowSelectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_rents);
        Intent intent = getIntent();
        selectedDate = new date(intent.getStringExtra("EXTRA_YEAR"),intent.getStringExtra("EXTRA_MONTH"), intent.getStringExtra("EXTRA_DAY"));
        Log.i("Data", selectedDate.getYear() + selectedDate.getMonth() + selectedDate.getDay());
        lLayout = (LinearLayout)findViewById(R.id.lLayoutShow);
        lLayoutSintetic = (LinearLayout)findViewById(R.id.sintetic);
        lLayoutSala = (LinearLayout)findViewById(R.id.sala);
        lLayoutTenis = (LinearLayout)findViewById(R.id.tenis);
        txtViewShowSelectedDate = (TextView)findViewById(R.id.txtViewSelectedDate);
        txtViewShowSelectedDate.setText("Data: " + beautify(selectedDate.getDay()) + "." + beautify(selectedDate.getMonth())+ "." + beautify(selectedDate.getYear()));
        readData();

    }

    public void readData(){
        retrieveData(OPTION_SINTETIC);
        retrieveData(OPTION_SALA);
        retrieveData(OPTION_TENIS);
    }

    public void retrieveData(final String option) {
        Resources res = getResources();
        int id = res.getIdentifier(option, "id", SeeRents.this.getPackageName());
        final LinearLayout llayoutToWrite;
        llayoutToWrite = (LinearLayout)findViewById(id);
        databaseReference = FirebaseDatabase.getInstance().getReference().
                child(option).child(selectedDate.getYear()).child(selectedDate.getMonth()).
                child(selectedDate.getDay());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                buttons.clear();
                llayoutToWrite.removeAllViews();
                for (DataSnapshot s: snapshot.getChildren()){
                    if (option.equals(OPTION_TENIS))
                    {
                        for (DataSnapshot ds: s.getChildren()){
                            Log.i("IN AL DOILEA FOR", ds.getKey() + " si valoarea " + ds.getValue());
                            Button rent = createButton(option, selectedDate.getYear(),selectedDate.getMonth(), selectedDate.getDay(), s.getKey(), ds.getKey(), ds.child("nume").getValue().toString(), ds.child("telefon").getValue().toString());
                            buttons.add(rent);
                        }
                    }
                    else
                    {
                        Button rent = createButton(option, selectedDate.getYear(), selectedDate.getMonth(), selectedDate.getDay(), s.getKey(), s.child("nume").getValue().toString(), s.child("telefon").getValue().toString());
                        buttons.add(rent);
                    }
                }
                rents.put(option, buttons);
                for (Button b: buttons){
                    llayoutToWrite.addView(b);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public Button createButton(String option, String year, String month, String day, String hour, String name, String phone)
    {

        Button btn = new Button(this);
        btn.setText( "Închiriat de la " + beautify(hour) + ":00" + " de: " + name + ", Telefon " + phone);
        btn.setTag(option.substring(0,4) + beautify(hour));
        btn.setAllCaps(false);
        btn.setTypeface(ResourcesCompat.getFont(this, R.font.raleway_semibold));
        btn.setTextSize(20);
        btn.setBackgroundResource(R.color.CSOyellow);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(20,10,20,10);
        btn.setLayoutParams(params);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenu(view);
            }
        });
        return btn;

    }

    public Button createButton(String option, String year, String month, String day, String hour, String court, String name, String phone) //Tenis
    {
        Button btn = new Button(this);
        btn.setText( "Închiriat de la " + beautify(hour) + ":00" + ", terenul nr. " + court + " de: " + name + ", Telefon " + phone);
        btn.setTag(option.substring(0,4) + beautify(hour) + court);
        btn.setAllCaps(false);
        btn.setTypeface(ResourcesCompat.getFont(this, R.font.raleway_semibold));
        btn.setTextSize(20);
        btn.setBackgroundResource(R.color.CSOyellow);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(20,10,20,10);
        btn.setLayoutParams(params);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenu(view);
            }
        });
        return btn;
    }

    public String beautify(String s){
        Integer n = Integer.parseInt(s);
        if (n < 10)
        {
            s = "0" + s;
        }
        return s;
    }

    public void showMenu(final View view)
    {
        PopupMenu popup = new PopupMenu(this, view);
        popup.inflate(R.menu.button_menu);
        popup.show();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.remove:
                        deleteRent(view);
                        return true;
                    default:
                        return false;

                }
            }
        });
    }

    public void deleteRent(View view){
        String hour = Integer.toString(Integer.parseInt(view.getTag().toString().substring(4,6)));
        String court = view.getTag().toString().substring(6);

        switch (((View)view.getParent()).getId()){
            case R.id.sintetic:
                Log.i("E SINTETIC", "Da");
                reffDel = FirebaseDatabase.getInstance().getReference().
                        child("sintetic").child(selectedDate.getYear()).child(selectedDate.getMonth()).
                        child(selectedDate.getDay()).child(hour);
                reffDel.removeValue();
                break;
            case R.id.sala:
                Log.i("E SALA","Da");
                reffDel = FirebaseDatabase.getInstance().getReference().
                        child("sala").child(selectedDate.getYear()).child(selectedDate.getMonth()).
                        child(selectedDate.getDay()).child(hour);
                reffDel.removeValue();
                break;
            case R.id.tenis:
                reffDel = FirebaseDatabase.getInstance().getReference().
                        child("tenis").child(selectedDate.getYear()).child(selectedDate.getMonth()).
                        child(selectedDate.getDay()).child(hour).child(court);
                reffDel.removeValue();
                break;

        }
    }
}