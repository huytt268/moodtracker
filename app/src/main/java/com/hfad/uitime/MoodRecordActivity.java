package com.hfad.uitime;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.hfad.uitime.views.MainActivity;

import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MoodRecordActivity extends AppCompatActivity {
    private ImageView back_arrow, iv_todayimage;
    private TextView txt_dayrecord;
    private EditText et_note;
    private Button btn_donerecord;
    private ImageButton btn_addimage;
    private RadioGroup moodGroup;
    private RadioButton mood_1, mood_2, mood_3, mood_4, mood_5;
    private ActivityResultLauncher<Intent> resultLauncher;
    private SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mood_record);
        back_arrow = findViewById(R.id.back_mood);
        iv_todayimage = findViewById(R.id.iv_todayimage);
        txt_dayrecord = findViewById(R.id.txt_dayrecord);

        long value = getIntent().getLongExtra("selected_day", 0);
        Date currentDate = (value == 0? new Date() : new Date(value));
        SimpleDateFormat format = new SimpleDateFormat("MMMM dd'th' yyyy", Locale.US);
        String formattedDate = format.format(currentDate);
        txt_dayrecord.setText(formattedDate);

        et_note = findViewById(R.id.et_note);
        btn_donerecord = findViewById(R.id.btn_donerecord);
        btn_addimage = findViewById(R.id.btn_addimage);
        moodGroup = findViewById(R.id.mood_group);
        mood_1 = findViewById(R.id.mood_1);
        mood_2 = findViewById(R.id.mood_2);
        mood_3 = findViewById(R.id.mood_3);
        mood_4 = findViewById(R.id.mood_4);
        mood_5 = findViewById(R.id.mood_5);
        db = MainActivity.getDatabase();

        registerResult();

        btn_addimage.setOnClickListener(v -> pickImage());

        btn_donerecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String note = et_note.getText().toString().trim();
                int idUser = 2;
                int overallEmo;
                if(mood_1.isChecked()) overallEmo = 1;
                else if (mood_2.isChecked()) overallEmo = 2;
                else if (mood_3.isChecked()) overallEmo = 3;
                else if (mood_4.isChecked()) overallEmo = 4;
                else overallEmo = 5;
                ContentValues insertDay = new ContentValues();
                insertDay.put("idUser", idUser);
                insertDay.put("date", 24);
                insertDay.put("overallEmo", overallEmo);
                insertDay.put("note", note);
                insertDay.putNull("image");

                String result = "";
                if(db.insert("Day", null, insertDay) == -1) {
                    result = "Some thing wrong";
                } else {
                    result = "Recorded succesfully!";
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

    //Hàm thêm hình

    private void pickImage() {
        Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
        resultLauncher.launch(intent);
    }
    //Cái này cũnug để thêm hình
    private void registerResult() {
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        try {
                            Uri imageUri = result.getData().getData();
                            iv_todayimage.setImageURI(imageUri);
                        } catch (Exception e) {
                            Toast.makeText(MoodRecordActivity.this, "No image selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

}