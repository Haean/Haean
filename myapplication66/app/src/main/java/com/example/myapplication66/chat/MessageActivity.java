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
import com.example.myapplication66.model.TestModel;
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
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import kr.co.shineware.nlp.komoran.model.Token;

public class MessageActivity extends AppCompatActivity {

    private String destinationUid;
    private Button button;
    private EditText editText;

    private String uid;
    private String chatRoomUid;

    private RecyclerView recyclerView;

    private String output;
    int func = 0;   //0:없음 1:메뉴

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
                    Check(comment.message);
                    comment.uid = "oXK4YFZxceV5yKduSmgfcAg5ehg1";
                    comment.message = output;
                    FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoomUid).child("comments")
                            .push().setValue(comment);

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

    //챗봇기능 구현
    public void Check(String a) {
        ArrayList<String> A = new ArrayList();
        Komoran komoran = new Komoran(DEFAULT_MODEL.FULL);

        KomoranResult analyzeResultList = komoran.analyze(a);

        List<Token> tokenList = analyzeResultList.getTokenList();
        for (Token token : tokenList) {
            if (token.getPos().equals("NNG") || token.getPos().equals("NNP") || token.getPos().equals("VV")) {
                A.add(token.getMorph());
            }
        }

        for (String noun : A) {
            System.out.println(noun);
        }

        if (A.contains("해안")) {
            A.remove("해안");

            if (A.contains("선택") || A.contains("고르")) {
                if (A.contains("메뉴") || A.contains("추천") || A.contains("아침") || A.contains("점심") || A.contains("저녁") || A.contains("밥") || A.contains("아침밥") || A.contains("점심밥") || A.contains("저녁밥")) {
                    //메뉴추천
                    Menu(A);
                }
                //선택
            } else if (A.contains("메뉴") || A.contains("추천") || A.contains("아침") || A.contains("점심") || A.contains("저녁") || A.contains("밥") || A.contains("아침밥") || A.contains("점심밥") || A.contains("저녁밥")) {
                //메뉴추천
                Menu(A);
            } else if (A.contains("날씨")) {
                //날씨
            } else if (A.contains("위치")) {
                //위치
            } else if (A.contains("문제") || A.contains("퀴즈")) {
                //퀴즈
            } else if (A.contains("가위바위보")) {
                //가위바위보
            } else if (A.contains("교통")) {
                //교통정보
            } else if (A.contains("운세")) {
                //운세
            } else {
                //사용법,메뉴얼 보여주기
            }
        }

    }

    public void Menu(ArrayList A) {
        ArrayList<String> Han = new ArrayList<String>(Arrays.asList("김치찌개", "부대찌개", "된장찌개", "비빔밥", "제육덮밥", "뼈해장국", "수육국밥"));
        ArrayList<String> Bun = new ArrayList<String>(Arrays.asList("떡볶이", "라볶이", "라면", "만두", "김밥", "순대", "튀김", "우동", "돈까스", "쫄면"));
        ArrayList<String> Jung = new ArrayList<String>(Arrays.asList("짜장면", "짬뽕", "간짜장", "우동", "울면", "탕수육", "볶음밥", "잡채밥"));
        ArrayList<String> Il = new ArrayList<String>(Arrays.asList("우동", "초밥", "돈까스", "덮밥", "회", "라멘", "소바", "오니기리"));
        ArrayList<String> Asia = new ArrayList<String>(Arrays.asList("훠궈", "쌀국수", "마라탕", "팟타이", "분짜", "나시고렝"));
        ArrayList<String> Fast = new ArrayList<String>(Arrays.asList("피자", "치킨", "햄버거"));
        ArrayList<String> Ki = new ArrayList<String>(Arrays.asList("냉면", "고기", "곱창", "닭발", "족발", "보쌈"));

        Random rand = new Random();
        int a = 0;

        if (A.contains("한식")) {
            a = Han.size();
            a = rand.nextInt(a);
            output = Han.get(a);
        } else if (A.contains("분식")) {
            a = Han.size();
            a = rand.nextInt(a);
            output = Bun.get(a);
        } else if (A.contains("중식")) {
            a = Han.size();
            a = rand.nextInt(a);
            output = Jung.get(a);
        } else if (A.contains("일식")) {
            a = Han.size();
            a = rand.nextInt(a);
            output = Il.get(a);
        } else if (A.contains("아시아")) {
            a = Han.size();
            a = rand.nextInt(a);
            output = Asia.get(a);
        } else if (A.contains("패스트푸드")) {
            a = Han.size();
            a = rand.nextInt(a);
            output = Fast.get(a);
        } else if (A.contains("기타")) {
            a = Han.size();
            a = rand.nextInt(a);
            output = Ki.get(a);
        } else {
            output = "한식/분식/중식/일식/아시아/패스트푸드/기타 중 어떤 음식을 먹고 싶으신가요?";
        }

        A.clear();
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


            //내가 보낸 메세지
            if (comments.get(position).uid.equals(uid)) {
                messageViewHoler.textView_message.setText(comments.get(position).message);
                messageViewHoler.textView_message.setBackgroundResource(R.drawable.right_bubble);
                messageViewHoler.linearLayout_destination.setVisibility(View.INVISIBLE);
                messageViewHoler.textView_message.setTextSize(20);
                messageViewHoler.linearLayout_main.setGravity(Gravity.RIGHT);   // 메세지 오른쪽 정렬


                // 상대방이 보낸 메세지
            } else if (comments.get(position).uid.equals("oXK4YFZxceV5yKduSmgfcAg5ehg1")) {
                System.out.println("찾음");

                String c = "챗봇";
                String d = "https://firebasestorage.googleapis.com/v0/b/my-application66.appspot.com/o/userImages%2FoXK4YFZxceV5yKduSmgfcAg5ehg1?alt=media&token=bc6820e5-d35e-441a-8658-b9cf9dc34604";

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
