package com.hfad.uitime;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hfad.uitime.views.TodoListActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    //khai báo biến xml
    private Button btn_openChange_Nickname, btn_logout;
    private TextView txt_username, txt_email, txt_totaldays, txt_totalpics;
    //khai báo biến lưu mã code
    private static final int UPDATE_USERNAME_REQUEST = 26;
    private static final int UPDATE_USERNAME_SUCCESS = 200;

    private SQLiteDatabase db;
    private int idUser;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        View profileview = inflater.inflate(R.layout.fragment_profile, container, false);
        db = TodoListActivity.getDatabase();
        idUser = this.getArguments().getInt("idUser");
        //kết nối với xml
        btn_openChange_Nickname = (Button) profileview.findViewById(R.id.btn_openChange_Nickname);
        btn_logout = (Button) profileview.findViewById(R.id.btn_logout);
        txt_username = (TextView) profileview.findViewById(R.id.txt_username);
        txt_email = (TextView) profileview.findViewById(R.id.txt_email);
        txt_totaldays = (TextView) profileview.findViewById(R.id.txt_totaldays);
        txt_totalpics = (TextView) profileview.findViewById(R.id.txt_totalpics);
        //hiển thi thông tin user
        Cursor getUser = db.query("User", null,  "idUser = ?", new String[]{String.valueOf(idUser)},
                null, null, null);
        if (getUser != null) {
            getUser.moveToFirst();
        }
        txt_username.setText(getUser.getString(1));
        txt_email.setText(getUser.getString(2));
        //Hiển thị tổng ngày đã viết nhật kí
        Cursor countDays = db.rawQuery("SELECT COUNT(*) FROM Day WHERE idUser = '" + idUser + "'",null);
        countDays.moveToFirst();
        txt_totaldays.setText(String.valueOf(countDays.getInt(0)));
        countDays.close();

        //Event listener
        btn_openChange_Nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleChangeNickname();
            }
        });


        return profileview;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPDATE_USERNAME_REQUEST && resultCode == UPDATE_USERNAME_SUCCESS) {
            String newNickname = data.getStringExtra("newNickname");
            txt_username.setText(newNickname);
        }
    }

    private void handleChangeNickname() {
        Intent intent = new Intent(getActivity(),ChangeNicknameActivity.class);
        intent.putExtra("idUser", idUser);
        intent.putExtra("username", txt_username.getText());
        startActivityForResult(intent, UPDATE_USERNAME_REQUEST);
    }
}