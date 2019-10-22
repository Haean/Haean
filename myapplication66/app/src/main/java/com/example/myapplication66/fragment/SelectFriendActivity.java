
package com.example.myapplication66.fragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication66.R;
import com.example.myapplication66.chat.GroupMessageActivity;
import com.example.myapplication66.chat.MessageActivity;
import com.example.myapplication66.model.ChatModel;
import com.example.myapplication66.model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SelectFriendActivity extends AppCompatActivity {

    ChatModel chatModel = new ChatModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_friend);

        RecyclerView recyclerView = findViewById(R.id.selectFriendAcitivity_recyclerview);
        recyclerView.setAdapter(new SelectFriendRecyclerViewAdapter());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Button button = (Button)findViewById(R.id.selectFriendAcitivity_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                chatModel.users.put(myUid,true);

                FirebaseDatabase.getInstance().getReference().child("chatrooms").push().setValue(chatModel);
            }
        });
    }

    class SelectFriendRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        List<UserModel> userModels;
        public SelectFriendRecyclerViewAdapter(){
            userModels = new ArrayList<>();
            final String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) { //친구목록 불러오기
                    userModels.clear();
                    for(DataSnapshot snapshot :dataSnapshot.getChildren()){
                        UserModel userModel = snapshot.getValue(UserModel.class);
                        if (userModel.uid.equals(myUid)|| userModel.uid.equals("chatbot")){
                            continue;
                        }
                        userModels.add(userModel);
                    }
                    notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend_select,parent,false);

            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

            Glide.with(holder.itemView.getContext()).load(userModels.get(position).profileImageUrl)
                    .apply(new RequestOptions().circleCrop()).into(((CustomViewHolder)holder).imageView);  // 친구 목록에 이미지 넣기

            ((CustomViewHolder)holder).textView.setText(userModels.get(position).userName);  // 친구 목록에 친구이름 넣기

            holder.itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), MessageActivity.class);
                    intent.putExtra("destinationUid",userModels.get(position).uid); // 상대방 uid를 담아주는 역할
                    startActivity(intent);
                }
            });

            ((CustomViewHolder)holder).checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    // 체크가 된 상태
                    if(b){
                        chatModel.users.put(userModels.get(position).uid,true);

                        // 체크가 취소 된 상태
                    }else{
                        chatModel.users.remove(userModels.get(position));
                    }
                }
            });
        }

        @Override
        public int getItemCount() {

            return userModels.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder {

            public ImageView imageView;
            public TextView textView;
            public CheckBox checkBox;

            public CustomViewHolder(View view) {
                super(view);
                imageView = (ImageView)view.findViewById(R.id.frienditem_imageview1);
                textView = (TextView)view.findViewById(R.id.frienditem_textview);
                checkBox = (CheckBox)view.findViewById(R.id.friendItem_checkbox);
            }
        }
    }
}
