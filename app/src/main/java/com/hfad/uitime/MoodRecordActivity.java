package com.hfad.uitime;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.hfad.uitime.views.TodoListActivity;

public class MoodRecordActivity extends AppCompatActivity {
    private ImageView back_arrow;
    private TextView txt_dayrecord;
    private EditText et_note;
    private Button btn_donerecord;
    private SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mood_record);
        back_arrow = findViewById(R.id.back_mood);
        txt_dayrecord = findViewById(R.id.txt_dayrecord);
        et_note = findViewById(R.id.et_note);
        btn_donerecord = findViewById(R.id.btn_donerecord);
        db = TodoListActivity.getDatabase();

        btn_donerecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String note = et_note.getText().toString().trim();
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

        //xử lí sự kiện nút back góc trên bên trái
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


}