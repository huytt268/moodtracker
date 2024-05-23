package com.hfad.uitime;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.hfad.uitime.R;
import com.hfad.uitime.views.TodoListActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Chart extends AppCompatActivity {
    private LineChart lineChart;
    private List<String> xValues;
    private EditText moodentry;
    private Button btn_donerecord;
    private ImageView back_arrow;
    private SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart);

        lineChart = findViewById(R.id.chart);

        Description description = new Description();
        description.setText("Mood Flow");
        description.setPosition(150f, 15f);
        lineChart.setDescription(description);
        back_arrow = findViewById(R.id.back_mood);
        moodentry = findViewById(R.id.moodentry);
        btn_donerecord = findViewById(R.id.btn_donerecord);
        db = TodoListActivity.getDatabase();

        btn_donerecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String note = moodentry.getText().toString().trim();
                int idUser = 2;
                int idDay = 4;
                ContentValues insertDay = new ContentValues();
                //insertDay.put("idDay", idDay);
                insertDay.put("idUser", idUser);
                insertDay.put("date", 23);
                insertDay.put("overallEmo", 5);
                insertDay.put("note", note);
                insertDay.putNull("image");

                String result = "";
                if(db.insert("Day", null, insertDay) == -1) {
                    result = "Fail";
                } else {
                    result = "Success";
                }
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
            }
        });
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        xValues = Arrays.asList("1/5","11/5","21/5","31/5");

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xValues));
        xAxis.setLabelCount(4);
        xAxis.setGranularity(1f);

        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(180f);
        yAxis.setAxisLineWidth(2f);
        yAxis.setAxisLineColor(android.R.color.holo_green_light);
        yAxis.setLabelCount(5);

        List<Entry> entries1 = new ArrayList<>();
        entries1.add(new Entry(0,10f));
        entries1.add(new Entry(1,10f));
        entries1.add(new Entry(2,15f));
        entries1.add(new Entry(3,45f));

        LineDataSet dataSet1 = new LineDataSet(entries1, "Good");
        dataSet1.setColor(android.R.color.holo_green_light);

        LineData lineData = new LineData(dataSet1);
        lineChart.setData(lineData);
        lineChart.invalidate();

    }
}
