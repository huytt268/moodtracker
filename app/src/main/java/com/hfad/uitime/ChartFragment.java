package com.hfad.uitime;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.hfad.uitime.views.MainActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChartFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private LineChart lineChart;
    private List<String> xValues;
    private EditText moodentry;
    private Button btn_donerecord;
    private ImageView back_arrow;
    private SQLiteDatabase db;

    public ChartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PomodoroFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChartFragment newInstance(String param1, String param2) {
        ChartFragment fragment = new ChartFragment();
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
        View view = inflater.inflate(R.layout.fragment_chart, container, false);

        lineChart = view.findViewById(R.id.chart);

        Description description = new Description();
        description.setText("Mood Flow");
        description.setPosition(150f, 15f);
        lineChart.setDescription(description);
        back_arrow = view.findViewById(R.id.back_mood);
        moodentry = view.findViewById(R.id.moodentry);
        btn_donerecord = view.findViewById(R.id.btn_donerecord);
        db = MainActivity.getDatabase();

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
                if (db.insert("Day", null, insertDay) == -1) {
                    result = "Fail";
                } else {
                    result = "Success";
                }
                Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
            }
        });
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        xValues = Arrays.asList("1/5", "11/5", "21/5", "31/5");

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
        entries1.add(new Entry(0, 10f));
        entries1.add(new Entry(1, 10f));
        entries1.add(new Entry(2, 15f));
        entries1.add(new Entry(3, 45f));

        LineDataSet dataSet1 = new LineDataSet(entries1, "Good");
        dataSet1.setColor(android.R.color.holo_green_light);

        LineData lineData = new LineData(dataSet1);
        lineChart.setData(lineData);
        lineChart.invalidate();
        return view;
    }
}