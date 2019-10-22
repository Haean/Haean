package com.example.myapplication66;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private Button login;
    private EditText id;
    private EditText password;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private TextView Button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        id = (EditText)findViewById(R.id.login_id);
        password = (EditText)findViewById(R.id.login_password);
        login = (Button)findViewById(R.id.login);
        firebaseAuth.signOut();

        login.setOnClickListener(new View.OnClickListener() {  // 로그인
            @Override
            public void onClick(View view) {
                loginEvent();
            }
        });

        Button=(TextView)findViewById(R.id.button2);  // 회원가입
        Button.setOnClickListener(new View.OnClickListener(){  // intent를 이용해서 버튼을 눌렀을 경우 회원가입 창으로 이동
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.button2) {
                    Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                    startActivity(intent);

                }
            }
        });

        //로그인 인터페이스 리스너
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser(); // FirebaseAuth에서 정보를 확인해주는 것
                if(user != null){
                    //로그인
                    Intent intent  = new Intent(LoginActivity.this,friend_list.class);
                    startActivity(intent);
                    finish();

                }else{
                    //로그아웃
                }
            }
        };
    }

    //로그인 확인
    void loginEvent(){
        firebaseAuth.signInWithEmailAndPassword(id.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    //로그인 실패
                    Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    protected void onStart() { //로그인 할 때 자동으로 생성
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }
}
