package com.example.myapplication66.chat;


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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.example.myapplication66.chat.*;


public class MessageActivity extends AppCompatActivity {

    private String destinationUid;
    private Button button;
    private EditText editText;

    private String uid;
    private String chatRoomUid;

    private RecyclerView recyclerView;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid(); // 채팅을 요구하는 아이디 즉 단말기에 로그인 된 Uid
        destinationUid = getIntent().getStringExtra("destinationUid"); // 채팅을 당하는 아이디
        button = (Button) findViewById(R.id.MessageActivity_button);
        editText = (EditText) findViewById(R.id.MessageActivity_editText);

        recyclerView = (RecyclerView) findViewById(R.id.MessageActivity_recycleview);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatModel chatModel = new ChatModel();
                chatModel.users.put(uid, true);  // 현재 대화하는 사람들 전부(로그인한 uid)
                chatModel.users.put(destinationUid, true); // 현재 대화하는 사람들 전부 (패팅을 당한 사람(들))


                if (chatRoomUid == null) {
                    button.setEnabled(false);
                    FirebaseDatabase.getInstance().getReference().child("chatrooms").push().setValue(chatModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            checkChatRoom();
                        }
                    });
                } else {
                    ChatModel.Comment comment = new ChatModel.Comment();
                    comment.uid = uid;
                    comment.message = editText.getText().toString();
                    comment.timestamp = ServerValue.TIMESTAMP;
                    FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoomUid).child("comments")
                            .push().setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {  // 데이터베이스에 chatrooms에 채팅방 이름(chatRoomUid)를 넣고 그 안에 comments를 넣어줌
                        @Override
                        public void onSuccess(Void aVoid) {
                            editText.setText(null);  // 콜백을 걸어서 전송버튼 눌렀을 때 메세지보내는 창 초기화
                        }
                    });
                    ChatBot.Check(comment.message);
                    comment.uid = "chatbot";
                    comment.message = ChatBot.output;
                    if(ChatBot.output!=null) {
                        FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoomUid).child("comments")
                                .push().setValue(comment);
                    }

                }
            }
        });
        checkChatRoom();
        //checkTest();
    }

    void checkChatRoom() {
        FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("users/" + uid).equalTo(true)
                .addListenerForSingleValueEvent(new ValueEventListener() { // 데이터베이스에 내용을 저장
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot item : dataSnapshot.getChildren()) {
                            ChatModel chatModel = item.getValue(ChatModel.class);  // 데이터 베이스에 users에 저장횐 것
                            if (chatModel.users.containsKey(destinationUid) && chatModel.users.size() == 2) {
                                chatRoomUid = item.getKey(); //  item.getKey() : 채팅 방 아이디 가져옴
                                button.setEnabled(true);
                                recyclerView.setLayoutManager(new LinearLayoutManager(MessageActivity.this));
                                recyclerView.setAdapter(new RecyclerViewAdapter());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        List<ChatModel.Comment> comments;
        UserModel userModel;

        public RecyclerViewAdapter() {
            comments = new ArrayList<>();

            FirebaseDatabase.getInstance().getReference().child("users").child(destinationUid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    userModel = dataSnapshot.getValue(UserModel.class);
                    getMessageList();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        void getMessageList() {

            FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoomUid).child("comments").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    comments.clear();

                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        comments.add(item.getValue(ChatModel.Comment.class));
                    }
                    notifyDataSetChanged();  // 리스트를 새로 갱신 해주는 역할

                    recyclerView.scrollToPosition(comments.size() - 1); // 맨 마지막  부분으로 이동
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);  // xml파일을 넣어줌
            // View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat,parent,false);

            return new MessageViewHoler(view); // ViewHolder는 view를 재사용할 때 쓰이는 클래스
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            MessageViewHoler messageViewHoler = ((MessageViewHoler) holder);

            System.out.println(comments.get(position).uid);

            //내가 보낸 메세지
            if (comments.get(position).uid.equals(uid)) {
                messageViewHoler.textView_message.setText(comments.get(position).message);
                messageViewHoler.textView_message.setBackgroundResource(R.drawable.right_bubble);
                messageViewHoler.linearLayout_destination.setVisibility(View.INVISIBLE);
                messageViewHoler.textView_message.setTextSize(20);
                messageViewHoler.linearLayout_main.setGravity(Gravity.RIGHT);   // 메세지 오른쪽 정렬


                // 상대방이 보낸 메세지
            } else if (comments.get(position).uid.equals("chatbot")) {

                String c = "해안";
                String d = "https://firebasestorage.googleapis.com/v0/b/my-application66.appspot.com/o/userImages%2F3.png?alt=media&token=e7f57cfb-89fb-48a7-9229-bbe4cb04208b";

                Glide.with(holder.itemView.getContext()).load(d).apply(new RequestOptions().circleCrop())
                        .into(messageViewHoler.imageView_profile);
                messageViewHoler.textView_name.setText(c);

                messageViewHoler.linearLayout_destination.setVisibility(View.VISIBLE);
                messageViewHoler.textView_message.setBackgroundResource(R.drawable.left_bubble);
                messageViewHoler.textView_message.setText(comments.get(position).message);
                messageViewHoler.textView_message.setTextSize(20);
                messageViewHoler.linearLayout_main.setGravity(Gravity.LEFT);  // 메세지 왼쪽 정렬

            } else {


                Glide.with(holder.itemView.getContext()).load(userModel.profileImageUrl).apply(new RequestOptions().circleCrop())
                        .into(messageViewHoler.imageView_profile);
                messageViewHoler.textView_name.setText(userModel.userName);


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

        private class MessageViewHoler extends RecyclerView.ViewHolder {
            public TextView textView_message;
            public TextView textView_name;
            public ImageView imageView_profile;
            public LinearLayout linearLayout_destination;
            public LinearLayout linearLayout_main;
            public TextView getTextView_timestamp;

            public MessageViewHoler(View view) {
                super(view);
                textView_message = (TextView) view.findViewById(R.id.messageItem_textview_message);
                textView_name = (TextView) view.findViewById(R.id.messageItem_textview_name);
                imageView_profile = (ImageView) view.findViewById(R.id.messageItem_imageview_profile);
                linearLayout_destination = (LinearLayout) view.findViewById(R.id.messageItem_linerlayout_destination);
                linearLayout_main = (LinearLayout) view.findViewById(R.id.messageItem_linerlayout_main);  //자기 대화내용 왼쪽 오른쪽 정렬 가능
                getTextView_timestamp = (TextView) view.findViewById(R.id.messageItem_textview_timestamp);

            }
        }

    }
}
