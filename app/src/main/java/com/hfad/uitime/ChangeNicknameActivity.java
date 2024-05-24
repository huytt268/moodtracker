package com.hfad.uitime;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.hfad.uitime.views.MainActivity;

public class ChangeNicknameActivity extends AppCompatActivity {
    private Button changeNickname;
    private EditText et_nickname;
    private ImageView back_arrow;

    private SQLiteDatabase db;
    private int idUser;
    private String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_nickname);

        db = MainActivity.getDatabase();

        String username = "";
        et_nickname = findViewById(R.id.et_nickname);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            if(bundle.getString("username") != null) {
                username = bundle.getString("username");
                idUser = bundle.getInt("idUser", 0);
                et_nickname.setText(username);
            }
        }

        changeNickname = findViewById(R.id.btn_changeNickname);
        back_arrow = findViewById(R.id.back);

        //xử lí sự kiện thay đổi nickname
        changeNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newNickname = et_nickname.getText().toString().trim();
                if(TextUtils.isEmpty(newNickname)) {
                    Toast.makeText(ChangeNicknameActivity.this, "Please enter a new nickname", Toast.LENGTH_SHORT).show();
                    return;
                }

                ContentValues updateNickname = new ContentValues();
                updateNickname.put("username", newNickname);
                int n = db.update("User", updateNickname , "idUser = ?", new String[]{String.valueOf(idUser)});
                if(n == 0) {
                    Toast.makeText(ChangeNicknameActivity.this, "Some thing wrong", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ChangeNicknameActivity.this, "Nickname updated successfully!", Toast.LENGTH_SHORT).show();
                    Cursor cursor = db.query("User", null,  "idUser = ?", new String[]{String.valueOf(idUser)},
                            null, null, null);
                    if (cursor != null) {
                        cursor.moveToFirst();
                        newNickname = cursor.getString(1);
                    }
                    Intent resutIntent = new Intent();
                    resutIntent.putExtra("newNickname", newNickname);
                    setResult(200, resutIntent);
                    finish();
                }
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