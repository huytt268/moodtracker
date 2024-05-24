
package com.hfad.uitime;


import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.Date;

public class CalendarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public final TextView dayOfMonth;
    public final ImageButton dayIcon;
    public final Date currentDate = new Date();
    private final CalendarAdapter.OnItemListener onItemListener;
    public CalendarViewHolder(@NonNull View itemView, CalendarAdapter.OnItemListener onItemListener)
    {
        super(itemView);
        dayOfMonth = itemView.findViewById(R.id.cellDayText);
        dayIcon = itemView.findViewById(R.id.cellDayBtn);
        this.onItemListener = onItemListener;
        itemView.setOnClickListener(this);
    }

    long getDate()
    {
        // Get the day of the month from the text view
        int day = Integer.parseInt(dayOfMonth.getText().toString());

        // Get the current year and month
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);

        // Set the calendar to the specific date
        calendar.set(year, month, day);

        // Return the Date object
        return calendar.getTime().getTime();
    }

    @Override
    public void onClick(View view)
    {
        onItemListener.onItemClick(getAdapterPosition(),
                (String) dayOfMonth.getText(),
                dayIcon.getDrawable());
        Intent intent = new Intent(view.getContext(), MoodRecordActivity.class);

        intent.putExtra("selected_day", getDate());

        view.getContext().startActivity(intent);
    }
}
