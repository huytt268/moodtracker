package com.hfad.uitime;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hfad.uitime.views.MainActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder>
{
    private final ArrayList<String> daysOfMonth;
    private final OnItemListener onItemListener;

    private final SQLiteDatabase db = MainActivity.getDatabase();

    public CalendarAdapter(ArrayList<String> daysOfMonth, OnItemListener onItemListener)
    {
        this.daysOfMonth = daysOfMonth;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * 0.166666666);
        return new CalendarViewHolder(view, onItemListener);
    }

    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        String dayText = daysOfMonth.get(position);
        holder.dayOfMonth.setText(dayText);
        if (!dayText.equals("")) {
            holder.dayIcon.setVisibility(View.VISIBLE);
            long date = holder.getDate();
            Map<Long, Integer> datesAndEmotions = getAllDatesAndEmotions();
            if (datesAndEmotions.containsKey(date)) {
                int emotion = datesAndEmotions.get(date);
                int iconResId = getIconResId(emotion);
                holder.dayIcon.setImageResource(iconResId);
            } else {
                holder.dayIcon.setImageResource(R.drawable.ic_add_item);
            }

            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);

            Calendar cellCalendar = Calendar.getInstance();
            cellCalendar.setTimeInMillis(date);
            int cellDay = cellCalendar.get(Calendar.DAY_OF_MONTH);
            int cellMonth = cellCalendar.get(Calendar.MONTH);
            int cellYear = cellCalendar.get(Calendar.YEAR);

            if (dayText.equals(String.valueOf(day)) && month == cellMonth && year == cellYear) {
                holder.itemView.setBackgroundResource(R.drawable.circle_btn);
            } else {
                holder.itemView.setBackgroundResource(0);
            }
        } else {
            holder.dayIcon.setVisibility(View.GONE);
        }
    }


    public Map<Long, Integer> getAllDatesAndEmotions() {
        Map<Long, Integer> datesAndEmotions = new HashMap<>();
        SQLiteDatabase db = MainActivity.getDatabase();
        Cursor cursor = db.rawQuery("SELECT date, overallEmo FROM Day", null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    int dateIndex = cursor.getColumnIndex("date");
                    int emotionIndex = cursor.getColumnIndex("overallEmo");

                    if (dateIndex != -1 && emotionIndex != -1) {
                        long date = cursor.getLong(dateIndex);
                        int emotion = cursor.getInt(emotionIndex);
                        datesAndEmotions.put(date, emotion);
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d("DatabaseAccess", "Error while trying to get dates and emotions from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return datesAndEmotions;
    }

    private int getIconResId(int emotion) {
        // Replace this with your own logic to map emotions to icons
        switch (emotion) {
            case 1:
                return R.drawable.mood_1_checked;
            case 2:
                return R.drawable.mood_2_checked;
            case 3:
                return R.drawable.mood_3_checked;
            case 4:
                return R.drawable.mood_4_checked;
            case 5:
                return R.drawable.mood_5_checked;
            default:
                return R.drawable.ic_add_item;
        }
    }

    @Override
    public int getItemCount()
    {
        return daysOfMonth.size();
    }

    public interface  OnItemListener
    {
        void onItemClick(int position, String dayText, Drawable dayIcon);
    }
}