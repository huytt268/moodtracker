package com.hfad.uitime;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
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
import com.hfad.uitime.views.TodoListActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PomodoroFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PomodoroFragment extends Fragment {

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

    private int idUser = this.getArguments().getInt("idUser");

    public PomodoroFragment() {
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
    public static PomodoroFragment newInstance(String param1, String param2) {
        PomodoroFragment fragment = new PomodoroFragment();
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
        View chartview = inflater.inflate(R.layout.fragment_pomodoro, container, false);
        lineChart = chartview.findViewById(R.id.chart);

        Description description = new Description();
        description.setText("Mood Flow");
        description.setPosition(150f, 15f);
        lineChart.setDescription(description);

        moodentry = chartview.findViewById(R.id.moodentry);

        // Get database
        SQLiteDatabase db = TodoListActivity.getDatabase();

        // Get idUser
        int idUser = getArguments().getInt("idUser");

        // Query database
        Cursor cursor = db.rawQuery("SELECT date, overallEmo FROM YourTableName WHERE idUser = ?", new String[]{String.valueOf(idUser)});
        ArrayList<String> xValues = new ArrayList<>();
        ArrayList<Entry> entries = new ArrayList<>();
        if (cursor != null) {
            int index = 0;
            while (cursor.moveToNext()) {
                int dateIndex = cursor.getColumnIndex("date");
                int overallEmoIndex = cursor.getColumnIndex("overallEmo");

                if (dateIndex != -1 && overallEmoIndex != -1) {
                    String date = cursor.getString(dateIndex);
                    int overallEmo = cursor.getInt(overallEmoIndex);
                    xValues.add(date);
                    entries.add(new Entry(index, overallEmo)); // Assuming overallEmo is float or int
                    index++;
                }
            }
            cursor.close();
        }

        // Setup XAxis
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xValues));
        xAxis.setLabelCount(xValues.size());
        xAxis.setGranularity(1f);

        // Setup YAxis
        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(180f); // Adjust as per your requirements
        yAxis.setAxisLineWidth(2f);
        yAxis.setAxisLineColor(ContextCompat.getColor(getContext(), android.R.color.holo_green_light));
        yAxis.setLabelCount(5);

        // Create dataset
        LineDataSet dataSet1 = new LineDataSet(entries, "Overall Emotion");
        dataSet1.setColor(ContextCompat.getColor(getContext(), android.R.color.holo_green_light));

        // Set data to chart
        LineData lineData = new LineData(dataSet1);
        lineChart.setData(lineData);
        lineChart.invalidate();

        return chartview;
    }


}