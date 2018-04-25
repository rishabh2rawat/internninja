package com.rishabhrawat.internninja;

import android.content.Intent;
import android.graphics.Color;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class PlotActivity extends AppCompatActivity {

    private static final String TAG = "PlotActivity";
    PieChart pieChart;
    EditText min, max;
    Button btnrefresh;
    int min1, max1;
    String m, x;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plot);

        /*----------------------------------connecting the layouts--------------------------------------------*/
        pieChart = (PieChart) findViewById(R.id.pie);
        min = (EditText) findViewById(R.id.min);
        max = (EditText) findViewById(R.id.max);
        btnrefresh = (Button) findViewById(R.id.refreshbtn);

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(true);
        pieChart.setExtraOffsets(5, 10, 5, 5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);


        final ArrayList<String> ar1 = getIntent().getExtras().getStringArrayList("Description");
        final ArrayList<Integer> ar2 = getIntent().getExtras().getIntegerArrayList("Expence");


        ArrayList<PieEntry> yValues = new ArrayList<>();
        for (int i = 0; i < ar2.size(); i++) {
            String n = ar1.get(i).toString();

            float z = ar2.get(i).floatValue();
            Log.d("Rishabh", "onCreate: ++++++++++++++++++++++++++++++++++++++++++++++++++++++" + z);
            yValues.add(new PieEntry(z, n));
        }


        /*****checking user session*****************/
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(PlotActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };


        /*seting up pie chart*/

        PieDataSet dataSet = new PieDataSet(yValues, "Expences");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        PieData data = new PieData((dataSet));
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.YELLOW);

        pieChart.setData(data);

        btnrefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m = min.getText().toString();
                x = max.getText().toString();

                if (m.equals("") || x.equals("")) {
                    Toast.makeText(PlotActivity.this, "enter both values", Toast.LENGTH_SHORT).show();
                } else {

                    min1 = Integer.parseInt(m);
                    max1 = Integer.parseInt(x);
                    if (min1 < max1) {
                        ArrayList<PieEntry> yValues = new ArrayList<>();
                        for (int i = 0; i < ar2.size(); i++) {
                            String n = ar1.get(i).toString();

                            float z = ar2.get(i).floatValue();
                            Log.d("Rishabh", "onCreate: ++++++++++++++++++++++++++++++++++++++++++++++++++++++" + z);
                            if (z > min1 && z < max1) {
                                yValues.add(new PieEntry(z, n));
                                Log.d("Rishabh", "onCreate: ---------------------------------------------------+" + z);
                            }

                            PieDataSet dataSet = new PieDataSet(yValues, "Expences");
                            dataSet.setSliceSpace(3f);
                            dataSet.setSelectionShift(5f);
                            dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

                            PieData data = new PieData((dataSet));
                            data.setValueTextSize(10f);
                            data.setValueTextColor(Color.YELLOW);

                            pieChart.setData(data);
                        }
                    } else {
                        Toast.makeText(PlotActivity.this, "set the correct ramge", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }

    @Override
    public void onBackPressed() {

        Intent moveback =
                new Intent(PlotActivity.this, HomeActivity.class);
        startActivity(moveback);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    // menue item in the top corner
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options, menu);

        return super.onCreateOptionsMenu(menu);


    }

    //action for the options button ie logout about and notifiacations
    public void action(MenuItem mi) {
        switch (mi.getItemId()) {
            case R.id.logout:
                mAuth.signOut();

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
}
