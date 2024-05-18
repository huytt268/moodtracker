package com.hfad.uitime.views;

import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hfad.uitime.CalendarFragment;
import com.hfad.uitime.HomeFragment;
import com.hfad.uitime.PomodoroFragment;
import com.hfad.uitime.ProfileFragment;
import com.hfad.uitime.R;

public class TodoListActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_todo_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.todo_list_view), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        frameLayout = findViewById(R.id.framelayout);

        //xử lí sự kiện nhấn vào các nút trong bottom bar
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();

                if(itemId == R.id.navigation_home) {
                    loadFragment(new HomeFragment(), false);
                } else if (itemId == R.id.navigation_calendar) {
                    loadFragment(new CalendarFragment(), false);
                } else if (itemId == R.id.navigation_pomodoro) {
                    loadFragment(new PomodoroFragment(), false);
                } else { //nav Profile
                    loadFragment(new ProfileFragment(), false);
                }

                return true;
            }
        });

        loadFragment(new HomeFragment(), true);
    }
    //hàm khởi tạo fragment tương ứng
    private void loadFragment (Fragment fragment, boolean isAppInitialized) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if(isAppInitialized) {
            fragmentTransaction.add(R.id.framelayout, fragment);
        } else {
            fragmentTransaction.replace(R.id.framelayout, fragment);
        }

        fragmentTransaction.commit();
    }
}