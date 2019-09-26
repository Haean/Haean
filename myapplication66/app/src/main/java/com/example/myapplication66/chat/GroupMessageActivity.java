package com.example.myapplication66.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication66.R;
import com.example.myapplication66.model.ChatModel;
import com.example.myapplication66.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class GroupMessageActivity extends AppCompatActivity {

    Map<String, UserModel> users = new HashMap<>(); // 유저에 대한 정보를 담을 맵
    String destinationRoom;
    String uid;
    EditText editText;

    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;

    private RecyclerView recyclerView;

    List<ChatModel.Comment> comments = new ArrayList<>();

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_message);

        destinationRoom = getIntent().getStringExtra("destinationRoom");
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        editText = (EditText)findViewById(R.id.groupMessageActivity_editText);

        FirebaseDatabase.getInstance().getReference().child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot item : dataSnapshot.getChildren()){   // 여러개의 데이터를 배열로 넘어와서 for문을 이용해 하나의 키값을 hashmap에 저장하고 value값을 UserModels에 저장
                    users.put(item.getKey(),item.getValue(UserModel.class));
                }
                init();
                recyclerView = (RecyclerView)findViewById(R.id.groupMessageActivity_recyclerview);
                recyclerView.setAdapter(new GroupMessageRecyclerviewAdapter());
                recyclerView.setLayoutManager(new LinearLayoutManager(GroupMessageActivity.this));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    void init(){
        Button button = (Button)findViewById(R.id.groupMessageActivity_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatModel.Comment comment = new ChatModel.Comment();
                comment.uid = uid;
                comment.message = editText.getText().toString();
                comment.timestamp = ServerValue.TIMESTAMP;
                FirebaseDatabase.getInstance().getReference().child("chatrooms").child(destinationRoom).child("comments")
                        .push().setValue(comment).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        editText.setText("");
                    }
                });
            }
        });

    }
    class GroupMessageRecyclerviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        public GroupMessageRecyclerviewAdapter(){
            getMessageList();
        }

        void getMessageList(){

            FirebaseDatabase.getInstance().getReference().child("chatrooms").child(destinationRoom).child("comments").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    comments.clear();

                    for(DataSnapshot item : dataSnapshot.getChildren()){
                        comments.add(item.getValue(ChatModel.Comment.class));
                    }
                    notifyDataSetChanged();  // 리스트를 새로 갱신 해주는 역할

                    recyclerView.scrollToPosition(comments.size()-1); // 맨 마지막  부분으로 이동
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message,parent,false);
            
            return new GroupMessageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            GroupMessageViewHolder messageViewHoler = ((GroupMessageViewHolder)holder);

            //내가 보낸 메세지
            if(comments.get(position).uid.equals(uid)){
                messageViewHoler.textView_message.setText(comments.get(position).message);
                messageViewHoler.textView_message.setBackgroundResource(R.drawable.right_bubble);
                messageViewHoler.linearLayout_destination.setVisibility(View.INVISIBLE);
                messageViewHoler.textView_message.setTextSize(20);
                messageViewHoler.linearLayout_main.setGravity(Gravity.RIGHT);   // 메세지 오른쪽 정렬

                // 상대방이 보낸 메세지
            }else{

                Glide.with(holder.itemView.getContext()).load(users.get(comments.get(position).uid).profileImageUrl).apply(new RequestOptions().circleCrop())
                        .into(messageViewHoler.imageView_profile);

                messageViewHoler.textView_name.setText(users.get(comments.get(position).uid).userName);
                messageViewHoler.linearLayout_destination.setVisibility(View.VISIBLE);
                messageViewHoler.textView_message.setBackgroundResource(R.drawable.left_bubble);
                messageViewHoler.textView_message.setText(comments.get(position).message);
                messageViewHoler.textView_message.setTextSize(20);
                messageViewHoler.linearLayout_main.setGravity(Gravity.LEFT);  // 메세지 왼쪽 정렬
            }
            long unixTime = (long) comments.get(position).timestamp;
            Date date = new Date(unixTime);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
            String time = simpleDateFormat.format(date);
            messageViewHoler.getTextView_timestamp.setText(time);

        }

        @Override
        public int getItemCount() {
            return comments.size();
        }

        private class GroupMessageViewHolder extends RecyclerView.ViewHolder {

            public TextView textView_message;
            public TextView textView_name;
            public ImageView imageView_profile;
            public LinearLayout linearLayout_destination;
            public LinearLayout linearLayout_main;
            public TextView getTextView_timestamp;

            public GroupMessageViewHolder(View view) {
                super(view);
                textView_message = (TextView)view.findViewById(R.id.messageItem_textview_message);
                textView_name = (TextView)view.findViewById(R.id.messageItem_textview_name);
                imageView_profile = (ImageView)view.findViewById(R.id.messageItem_imageview_profile);
                linearLayout_destination = (LinearLayout)view.findViewById(R.id.messageItem_linerlayout_destination);
                linearLayout_main = (LinearLayout)view.findViewById(R.id.messageItem_linerlayout_main);  //자기 대화내용 왼쪽 오른쪽 정렬 가능
                getTextView_timestamp = (TextView)view.findViewById(R.id.messageItem_textview_timestamp);

            }
        }
    }
}
