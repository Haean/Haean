package com.example.myapplication66.fragment;

import android.app.ActivityOptions;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

public class ChatFragment extends Fragment {

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm"); // 사람이 알아볼 수 있도록 설정
    private Uri imageUri;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat,container,false);

        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.chatfragment_recyclerview);
        recyclerView.setAdapter(new ChatRecyclerViewAdapter());
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));

        return view;
    }

    class ChatRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        private List<ChatModel> chatModels = new ArrayList<>();
        private List<String> keys = new ArrayList<>(); // 채팅 방에 대한 키
        private String uid;
        private ArrayList<String> destinationUers = new ArrayList<>();  // 대화하는 사람들의 데이터가 담기는 부분


        public ChatRecyclerViewAdapter(){  // 채팅목록 가져오는 역할
            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("users/"+uid).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    chatModels.clear();;
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        chatModels.add(snapshot.getValue(ChatModel.class));
                        keys.add(snapshot.getKey());
                    }
                    notifyDataSetChanged(); //새로 고침
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat,parent,false);

            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

            final CustomViewHolder customViewHolder = (CustomViewHolder)holder;
            String destinationUid = null;     //destinationUid : 내가 대화할 상대의 uid

            //하나하나 챗방에 있는 유저를 체크
            for(String user: chatModels.get(position).users.keySet()){
                if (!user.equals(uid)) {   // 내가 아닌 사람을 뽑을 수 있다.
                    destinationUid = user;
                    destinationUers.add(destinationUid);
                }
            }
            FirebaseDatabase.getInstance().getReference().child("users").child(destinationUid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    UserModel userModel = dataSnapshot.getValue(UserModel.class);
                    Glide.with(customViewHolder.itemView.getContext()).load(userModel.profileImageUrl).apply(new RequestOptions().circleCrop())
                            .into(customViewHolder.imageView);

                    customViewHolder.textView_title.setText(userModel.userName);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            //메세지를 내림차순으로 정렬 후 마지막 메세지의 키값을 가져옴
            Map<String,ChatModel.Comment> commentMap = new TreeMap<>(Collections.reverseOrder());
            commentMap.putAll(chatModels.get(position).comments); // 채팅에 대한 모델
            if(commentMap.keySet().toArray().length>0) {  // 채팅방에 메세지가 있을 경우만 마지막 메세지의 키값을 받아옴
                String lastMessageKey = (String) commentMap.keySet().toArray()[0]; //내림차순으로 정렬했으므로 첫번째 메세지 = 마지막으로 보낸 메세지
                customViewHolder.textView_lastmessage.setText(chatModels.get(position).comments.get(lastMessageKey).message);

                //TimeStamp
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));

                long unixTime = (long) chatModels.get(position).comments.get(lastMessageKey).timestamp;

                Date date = new Date(unixTime);
                customViewHolder.TextView_timestamp.setText(simpleDateFormat.format(date));

            }
            customViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = null;
                    if(chatModels.get(position).users.size() > 2){
                        intent = new Intent(view.getContext(), GroupMessageActivity.class);
                        intent.putExtra("destinationRoom",keys.get(position));

                    }else{
                        intent = new Intent(view.getContext(), MessageActivity.class); // MessageActivity: 채팅방 띄워주는 액티비티
                        intent.putExtra("destinationUid", destinationUers.get(position));
                    }
                    startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return chatModels.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder {

            public ImageView imageView;
            public TextView textView_title;
            public TextView textView_lastmessage;
            public TextView TextView_timestamp;

            public CustomViewHolder(View view) {
                super(view);

                imageView = (ImageView)view.findViewById(R.id.chatitem_imageview);
                textView_title = (TextView)view.findViewById(R.id.chatitem_textview_title);
                textView_lastmessage = (TextView)view.findViewById(R.id.chatitem_textview_lastMessage);
                TextView_timestamp = (TextView)view.findViewById(R.id.chatitem_textview_timestamp);

            }
        }
    }
}
