package com.example.myapplication66;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.myapplication66.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import javax.xml.transform.Result;

public class SignupActivity extends AppCompatActivity {

    private static final int PICK_FROM_ALBUM = 10;
    private EditText email;
    private EditText name;
    private EditText password;
    private Button signup;
    private ImageView profile;
    private Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        profile = (ImageView)findViewById(R.id.SignupActivity_imageview_profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_ALBUM);
            }
        });

        email = (EditText)findViewById(R.id.SignupActivity_edittext_email);
        name = (EditText)findViewById(R.id.SignupActivity_edittext_name);
        password = (EditText)findViewById(R.id.SignupActivity_edittext_password);
        signup = (Button)findViewById(R.id.LoginActivity_button_signup);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email.getText().toString() == null || name.getText().toString() == null || password.getText().toString() == null || imageUri == null){
                    return;
                }
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                final String uid = task.getResult().getUser().getUid();
                                FirebaseStorage.getInstance().getReference().child("userImages").child(uid).putFile(imageUri)
                                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                            @Override

                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                                FirebaseStorage.getInstance().getReference().child("userImages").child(uid).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                                                    @Override

                                                    public void onSuccess(Uri uri) {

                                                        String imageUrl=uri.toString();

                                                        UserModel userModel=new UserModel();

                                                        userModel.userName=name.getText().toString();

                                                        userModel.profileImageUrl=imageUrl;

                                                        userModel.uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                                        FirebaseDatabase.getInstance().getReference().child("users").child(uid).setValue(userModel);

                                                        FirebaseDatabase.getInstance().getReference().child("users").child(uid).setValue(userModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                SignupActivity.this.finish();
                                                            }
                                                        });
                                                    }

                                                });

                                            }

                                        });


                            }
                        });
            }
        });
    }


        /*

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(email.getText().toString() == null || name.getText().toString() == null || password.getText().toString() == null || imageUri == null){
                    return;
                }

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                final String uid = task.getResult().getUser().getUid();
                                FirebaseStorage.getInstance().getReference().child("userImages").child(uid).putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                        String imageUrl = task.getResult().getUploadSessionUri().toString();

                                        UserModel userModel = new UserModel();
                                        userModel.userName = name.getText().toString();
                                        userModel.userPassword = password.getText().toString();
                                        userModel.profileImageUrl = imageUrl;
                                        userModel.uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                        Task<Void> users = FirebaseDatabase.getInstance().getReference().child("users").child(uid).setValue(userModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                SignupActivity.this.finish();
                                            }
                                        });

                                    }
                                });

                            }
                        });
            }
        });
    }

         */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == PICK_FROM_ALBUM && resultCode == RESULT_OK) {
            profile.setImageURI(data.getData()); // 가운데 뷰를 바꿈
            imageUri = data.getData(); // 이미지 경로 원본
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
