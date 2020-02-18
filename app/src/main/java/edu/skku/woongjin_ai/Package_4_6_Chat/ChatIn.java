package edu.skku.woongjin_ai_winter.Package_4_6_Chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import edu.skku.woongjin_ai_winter.GlobalApplication;
import edu.skku.woongjin_ai_winter.Package_3_Main.MainActivity;
import edu.skku.woongjin_ai_winter.R;

public class ChatIn extends AppCompatActivity {

    private String chatname, CHAT_NAME;
    private String USER_NAME, USER2_NAME;
    String mynickname, myid, yourid, yournickname;

    private TextView chatinInfo;
    private RecyclerView chatting_view;
    private EditText chat_edit;
    private Button chat_send, goback, gohome;

    private Intent intentGoHome, intentGoBack;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    private DatabaseReference myRef;
    private RecyclerView mRecyclerView;
    public  RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<ChatDTO> chatList;
    private EditText EditText_chat;
    private Button Button_send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatin);

        // 위젯 ID 참조
        chatting_view = (RecyclerView) findViewById(R.id.chatting_view);

        chat_edit = (EditText) findViewById(R.id.chat_edit);
        chat_send = (Button) findViewById(R.id.chat_sent);
        goback = (Button) findViewById(R.id.go_back);
        gohome = (Button) findViewById(R.id.go_home);
        chatinInfo = (TextView) findViewById(R.id.chatinInfo);

        mynickname = GlobalApplication.getInstance().getGlobalNickname();
        myid = GlobalApplication.getInstance().getGlobalID();

        // 로그인 화면에서 받아온 채팅방 이름, 유저 이름 저장
        Intent intent = getIntent();
        yourid = intent.getStringExtra("user2id");
        yournickname = intent.getStringExtra("user2nickname");
        //chatname = intent.getStringExtra("chatName");
        CHAT_NAME = intent.getStringExtra("chatName");
        USER_NAME = intent.getStringExtra("chatPlayoneName");
        USER2_NAME = intent.getStringExtra("chatPlaytwoName");
        System.out.println("EOOROR"+USER_NAME +" "+USER2_NAME);
        chatinInfo.setText(CHAT_NAME+" - "+mynickname+"님 그리고 "+yournickname+"님");

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentGoBack = new Intent(ChatIn.this, ChatReadyActivity.class);
                intentGoBack.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //홈으로 버튼 누르면 액티비티 정보삭제
                startActivity(intentGoBack);
            }
        });

        gohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentGoHome = new Intent(ChatIn.this, MainActivity.class);
                intentGoHome.putExtra("id", myid);
                intentGoHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //홈으로 버튼 누르면 액티비티 정보삭제
                startActivity(intentGoHome);
            }
        });

        // 채팅 방 입장
        //openChat(CHAT_NAME);

        // 메시지 전송 버튼에 대한 클릭 리스너 지정
        /*
        chat_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chat_edit.getText().toString().equals(""))
                    return;

                //ChatDTO chat = new ChatDTO(USER_NAME, chat_edit.getText().toString()); //ChatDTO를 이용하여 데이터를 묶는다.
                ChatDTO chat = new ChatDTO(mynickname, chat_edit.getText().toString()); //ChatDTO를 이용하여 데이터를 묶는다.

                databaseReference.child("user_list/"+ myid +"/myChatlist/"+ yourid ).child(CHAT_NAME).push().setValue(chat); // 데이터 푸쉬
                databaseReference.child("user_list/"+ yourid +"/myChatlist/"+ myid ).child(CHAT_NAME).push().setValue(chat); // 데이터 푸쉬

                chat_edit.setText(""); //입력창 초기화

            }
        });


         */

        Button_send = findViewById(R.id.chat_sent);
        EditText_chat = findViewById(R.id.chat_edit);

        Button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = EditText_chat.getText().toString(); //msg

                if(msg != null) {
                    ChatDTO chat = new ChatDTO();
                    chat.setUserName(mynickname);
                    chat.setMessage(msg);
                    databaseReference.child("user_list/"+ myid +"/myChatlist/"+ yourid ).child(CHAT_NAME).push().setValue(chat); // 데이터 푸쉬
                    databaseReference.child("user_list/"+ yourid +"/myChatlist/"+ myid ).child(CHAT_NAME).push().setValue(chat); // 데이터 푸쉬

                    chat_edit.setText(""); //입력창 초기화

                }

            }
        });

        mRecyclerView = findViewById(R.id.chatting_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        chatList = new ArrayList<>();
        mAdapter = new ChatAdapter(chatList, ChatIn.this, mynickname);

        mRecyclerView.setAdapter(mAdapter);

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        //int totalItemCount = mLayoutManager.getItemCount();
        //mRecyclerView.smoothScrollToPosition(totalItemCount);
        //System.out.println("호오 "+ totalItemCount);
        //마지막 리사이클러뷰에 포커스, 이거만 수정하기
        // recycler view last item focus 알아내기



        databaseReference.child("user_list/"+ myid +"/myChatlist/"+ yourid).child(CHAT_NAME).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("CHATCHAT", dataSnapshot.getValue().toString());
                ChatDTO chat = dataSnapshot.getValue(ChatDTO.class);
                ((ChatAdapter) mAdapter).addChat(chat);
                mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

        //1. recyclerView - 반복
        //2. 디비 내용을 넣는다
        //3. 상대방폰에 채팅 내용이 보임 - get

        //1-1. recyclerview - chat data
        //1. message, nickname - Data Transfer Object



    }


/*
    private void addMessage(DataSnapshot dataSnapshot, ArrayAdapter<String> adapter) {
        ChatDTO chatDTO = dataSnapshot.getValue(ChatDTO.class);

        //if(chatDTO.getUserName().equals(myid))
        //{
        //    adapter.add(chatDTO.getUserName() + " : " + chatDTO.getMessage());
        //}
        //else
        adapter.add(chatDTO.getUserName() + " : " + chatDTO.getMessage());
    }

    private void removeMessage(DataSnapshot dataSnapshot, ArrayAdapter<String> adapter) {
        ChatDTO chatDTO = dataSnapshot.getValue(ChatDTO.class);
        adapter.remove(chatDTO.getUserName() + " : " + chatDTO.getMessage());
    }

    private void openChat(String chatName) {
        // 리스트 어댑터 생성 및 세팅
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.activity_chat_list_item, R.id.text2);
        chatting_view.setAdapter(adapter);

        // 데이터 받아오기 및 어댑터 데이터 추가 및 삭제 등..리스너 관리
        databaseReference.child("user_list/"+ myid +"/myChatlist/"+ yourid).child(chatName).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                addMessage(dataSnapshot, adapter);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                removeMessage(dataSnapshot, adapter);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

 */

    public void startToast(String msg){ Toast.makeText(this, msg, Toast.LENGTH_SHORT).show(); }

}