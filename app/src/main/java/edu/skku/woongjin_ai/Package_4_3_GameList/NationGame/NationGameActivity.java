package edu.skku.woongjin_ai.Package_4_3_GameList.NationGame;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.LinkObject;
import com.kakao.message.template.TextTemplate;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.util.helper.log.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.skku.woongjin_ai.Package_4_3_GameList.FirebasePost_list;
import edu.skku.woongjin_ai.Package_3_Main.MainActivity;
import edu.skku.woongjin_ai.Package_4_5_ShowFriend.ShowFriendListAdapter;
import edu.skku.woongjin_ai.Package_4_5_ShowFriend.ShowFriendListItem;
import edu.skku.woongjin_ai.R;
import edu.skku.woongjin_ai.UserInfo;

public class NationGameActivity extends AppCompatActivity {
    private DatabaseReference mPostReference, sPostReference, gPostReference;
    ListView friend_list, script_list;
    ArrayList<ShowFriendListItem> data;
    ArrayList<String> scriptArrayList;
    ShowFriendListAdapter arrayAdapter;
    edu.skku.woongjin_ai.Package_4_3_GameList.NationGame.ScriptListAdapter ScriptListAdapter;

    String id_key, nickname_key;
    String friend_nickname, script_name;
    ImageButton invitefriend, imageButtonHome;

    long Solved;
    String text_roomname;
    EditText editText_roomname;
    Button create;

    Intent intent;
    int check_choose, check_script, flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nationgame);


        check_choose = 0;
        check_script = 0;
        flag = 0;

        invitefriend = (ImageButton) findViewById(R.id.invitefriend);
        imageButtonHome = (ImageButton) findViewById(R.id.home);

        editText_roomname = (EditText) findViewById(R.id.edit_roomname);
        create = (Button) findViewById(R.id.create);

        friend_list = findViewById(R.id.friend_list);
        script_list = findViewById(R.id.script_list);
        data = new ArrayList<>();
        scriptArrayList = new ArrayList<String>();

        intent = getIntent();
        id_key = intent.getStringExtra("id");
        nickname_key = intent.getStringExtra("nickname");

        arrayAdapter = new ShowFriendListAdapter();
        ScriptListAdapter = new ScriptListAdapter();


        friend_list.setAdapter(arrayAdapter);

        mPostReference = FirebaseDatabase.getInstance().getReference().child("user_list").child(id_key).child("my_friend_list");
        sPostReference = FirebaseDatabase.getInstance().getReference().child("script_list");
        gPostReference = FirebaseDatabase.getInstance().getReference().child("gameroom_list");

        imageButtonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentHome = new Intent(NationGameActivity.this, MainActivity.class);
                intentHome.putExtra("id", id_key);
                intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentHome);
            }
        });

        invitefriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextTemplate params = TextTemplate.newBuilder(
                        "친구와 함께 책을 읽고 퀴즈 폭탄을 던지세요!",
                        LinkObject.newBuilder()
                                .setWebUrl("https://skku.edu")
                                .setMobileWebUrl("https://skku.edu")
                                .build()
                )
                        .setButtonTitle("친구야 같이 하자!")
                        .build();
                KakaoLinkService.getInstance().sendDefault(NationGameActivity.this, params, serverCallbackArgs, new ResponseCallback<KakaoLinkResponse>() {
                    @Override
                    public void onFailure(ErrorResult errorResult) {
                        Logger.e(errorResult.toString());
                    }
                    @Override
                    public void onSuccess(KakaoLinkResponse result) {
                    }
                });
            }
        });

        friend_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) { //custom listview에서 data 받아오기
                ShowFriendListItem temp_show = (ShowFriendListItem) parent.getItemAtPosition(position);
                friend_nickname = temp_show.getNameFriend();
                check_choose = 1;
            }
        });

        script_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long i) {
                ScriptListItem temp_show = (ScriptListItem) parent.getItemAtPosition(position);
                script_name = temp_show.getTitle();
                check_script = 1;
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text_roomname = editText_roomname.getText().toString();
                if (check_choose == 1 && check_script == 1) {
                    if (friend_nickname.length() > 0 && spaceCheck(text_roomname) == false && text_roomname.length() > 0) { //create chat room
                        final ValueEventListener checkRoomRegister = new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                                    String user1 = postSnapshot.child("user1").getValue().toString();
                                    String user2 = postSnapshot.child("user2").getValue().toString();
                                    String state = postSnapshot.child("state").getValue().toString();
                                    Log.d("_user1", user1);
                                    Log.d("_user2", user2);
                                    if (!(state.equals("win") || state.equals("win1") || state.equals("win2")) &&
                                            editText_roomname.getText().toString().length() > 0 &&
                                            ((user1.equals(nickname_key) && user2.equals(friend_nickname)) || (user2.equals(nickname_key) && user1.equals(friend_nickname)))) { //있으면
                                        Toast.makeText(getApplicationContext(), "이미 " + friend_nickname + " 와 게임에 참여중입니다.\n 진행중인 request를 먼저 완료해주세요", Toast.LENGTH_SHORT).show();
                                        flag = 1;
                                    }
                                }
                                if (editText_roomname.getText().toString().length() > 0 && flag == 0) { //채팅방이 처음 만들어질 경우
                                    postListDatabase(true);
                                    editText_roomname.setText("");
                                    Toast.makeText(getApplicationContext(), friend_nickname + "와의 게임방이 생성되었습니다.", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                                flag = 0;
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) { }
                        };
                        gPostReference.addValueEventListener(checkRoomRegister);
                    }
                    else if (friend_nickname.length() == 0 || spaceCheck(text_roomname) == true || text_roomname.length() == 0) {
                        editText_roomname.setText("");
                        Toast.makeText(NationGameActivity.this, "채팅방 이름을 바르게 입력해주세요", Toast.LENGTH_SHORT).show();
                    }
                }
                else if (check_choose == 0){
                    Toast.makeText(NationGameActivity.this, "채팅할 친구를 골라주세요", Toast.LENGTH_SHORT).show();
                }
                else if (check_script == 0){
                    Toast.makeText(NationGameActivity.this, "채팅할 지문을 골라주세요", Toast.LENGTH_SHORT).show();
                }
                else if (check_choose == 0 && check_script == 0){
                    Toast.makeText(NationGameActivity.this, "채팅할 친구와 게임할 지문을 골라주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        callback = new ResponseCallback<KakaoLinkResponse>() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                Toast.makeText(getApplicationContext(), errorResult.getErrorMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(KakaoLinkResponse result) {
                Toast.makeText(getApplicationContext(), "Successfully sent KakaoLink v2 message.", Toast.LENGTH_LONG).show();
            }
        };
        getFirebaseDatabase();
        getFirebaseDatabaseScriptList();
    }

    private Map<String, String> getServerCallbackArgs() {
        Map<String, String> callbackParameters = new HashMap<>();
        return callbackParameters;
    }

    private ResponseCallback<KakaoLinkResponse> callback;
    private Map<String, String> serverCallbackArgs = getServerCallbackArgs();


    public void getFirebaseDatabase() {
        try {
            final ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ShowFriendListAdapter showFriendListAdapter = new ShowFriendListAdapter();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        UserInfo friend = snapshot.getValue(UserInfo.class);
                        showFriendListAdapter.addItem(friend.profile, friend.nickname, friend.grade, friend.school, friend.id, friend.nickname, false, true, friend.onoffline);
                    }
                    friend_list.setAdapter(showFriendListAdapter);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            };
            mPostReference.addValueEventListener(postListener);

        } catch (java.lang.NullPointerException e) {

        }
    }

    public void getFirebaseDatabaseScriptList() {
        sPostReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                scriptArrayList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String key = snapshot.getKey();
                    String bookname = snapshot.child("book_name").getValue().toString();
                    scriptArrayList.add(key);
                    ScriptListAdapter.addItem(key, bookname);
                }
                script_list.setAdapter(ScriptListAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        });
    }

    public void postListDatabase(boolean add) {
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        Long time = System.currentTimeMillis() / 1000;
        String ts = time.toString();
        if(add) {
            FirebasePost_list post = new FirebasePost_list(editText_roomname.getText().toString(), nickname_key, friend_nickname, script_name,"gaming0" );
            postValues = post.toMap();
        }
        childUpdates.put(ts, postValues);
        gPostReference.updateChildren(childUpdates);
    }

    public boolean spaceCheck(String spaceCheck) {
        for (int i = 0 ; i < spaceCheck.length() ; i++) {
            if (spaceCheck.charAt(i) == ' ')
                continue;
            else if (spaceCheck.charAt(i) != ' ') {
                return false;
            }
        }
        return true;
    }

}