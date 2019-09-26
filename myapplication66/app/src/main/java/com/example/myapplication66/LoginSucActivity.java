package com.example.myapplication66;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.myapplication66.fragment.ChatFragment;
import com.example.myapplication66.fragment.PeopleFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

public class LoginSucActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_suc);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.LoginSucActivity_bottomnavigationview);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_people:
                        getFragmentManager().beginTransaction().replace(R.id.LoginSucActivity_framelayout, new PeopleFragment()).commit();
                        return true;

                    case R.id.action_chat:
                        getFragmentManager().beginTransaction().replace(R.id.LoginSucActivity_framelayout, new ChatFragment()).commit();
                        return true;
                }

                return false;
            }
        });


    }

}
