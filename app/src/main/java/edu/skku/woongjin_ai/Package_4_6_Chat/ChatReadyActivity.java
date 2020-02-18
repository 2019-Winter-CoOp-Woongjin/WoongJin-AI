package edu.skku.woongjin_ai_winter.Package_4_6_Chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.skku.woongjin_ai_winter.GlobalApplication;
import edu.skku.woongjin_ai_winter.R;

public class ChatReadyActivity extends AppCompatActivity {

    private EditText user_name, user_chatname;
    private Button entering_chat;
    private ListView chat_list;

    private ListView m_oListView = null;
    int nDatCnt=0;


    private DatabaseReference mPostReference;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private DatabaseReference mPostReference2 = FirebaseDatabase.getInstance().getReference();


    String mynickname, myid;
    String friend, friend_nick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_ready);

        user_name = (EditText) findViewById(R.id.user_Name);
        user_chatname = (EditText) findViewById(R.id.user_chatName);
        entering_chat = (Button) findViewById(R.id.entering_chat);
        chat_list = (ListView) findViewById(R.id.chat_list);

        mynickname = GlobalApplication.getInstance().getGlobalNickname();
        myid = GlobalApplication.getInstance().getGlobalID();

        mPostReference = firebaseDatabase.getReference().child("user_list");


        entering_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFirebaseDatabaseChat();
            }
        });


        showChatList();

    }

    public void getFirebaseDatabaseChat() {
        try {
            final ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    DataSnapshot snapshot=dataSnapshot.child("user_list/"+ myid +"/my_friend_list");
                    long temp = snapshot.getChildrenCount();
                    int flag = safeLongToInt(temp);
                    for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                        friend = snapshot1.getKey();   // my friend list 속 모든 id key.
                        String friend_nick = snapshot1.child("nickname").getValue().toString(); // friend와 매치되는 닉네임


                        // user_name edittext 입력값과 내 닉네임 같을 경우
                        if(user_name.getText().toString().equals(mynickname)) startToast("본인입니다.");
                        else if(user_name.getText().toString().equals(friend_nick)){
                            int chatflag = 0;
                            //  user_name edittext 입력값과 친구 닉네임 일치 할 경우
                            // 1. 나에게 채팅방 이름(일치), 상대방 이름(불일치)가 존재 하는지 체크.
                            // 2. 상대에게 채팅방 이름(일치), 상대방 이름(==내 이름. 불일치)가 존재하는지 체크.

                            DataSnapshot checkmysnapshot = dataSnapshot.child("user_list/"+myid+"/ChatInfo");

                            for(DataSnapshot check : checkmysnapshot.getChildren())
                            {
                                if(check.getKey().equals("CHAT " + user_chatname.getText().toString()))
                                {
                                    if(check.child(myid).getValue().equals(user_name.getText().toString()) &&
                                            check.child(friend).getValue().equals(mynickname))
                                    {
                                        chatflag=0;
                                        break;
                                    }
                                    else
                                    {
                                        startToast("동일한 이름의 다른 친구와의 채팅방이 존재합니다!\n다른 채팅방 이름을 사용해 주세요");
                                        chatflag=1;
                                        break;
                                    }
                                }
                            }
                            //완료

                            DataSnapshot tempgetId = dataSnapshot.child("user_list");
                            for (DataSnapshot tempget : tempgetId.getChildren())
                            {
                                System.out.println("tempget"+friend);
                                if(tempget.child("nickname").getValue().toString().equals(user_name.getText().toString()))
                                {
                                    friend=tempget.child("id").getValue().toString();
                                    System.out.println("아이디우효"+friend);
                                    //여기 까지 정상적으로 들어감.

                                    DataSnapshot checkyoursnapshot = dataSnapshot.child("user_list/"+friend+"/ChatInfo");
                                    for(DataSnapshot check : checkyoursnapshot.getChildren())
                                    {
                                        System.out.println("Checkmysnapshot value"+check.getValue());   // 값 두개가 리스트
                                        System.out.println("Checkmysnapshot key"+check.getKey());   // 챗 네임 정상으로 들어옴

                                        if(check.getKey().equals("CHAT " + user_chatname.getText().toString()))
                                        {
                                            System.out.println("Checkmysnapshot key boolean "+check.getKey().equals("CHAT " + user_chatname.getText().toString()));   // 챗 네임 정상으로 들어옴
                                            System.out.println("아이디우효"+friend);

                                            if(check.child(myid).exists() && check.child(friend).exists() &&
                                                    check.child(myid).getValue().equals(user_name.getText().toString()) &&   //여기서 아마도 euqls가 없어서 그런듯???모르겟다시발 이거만 고치자
                                                    check.child(friend).getValue().equals(mynickname) &&check.child(myid).exists()) //이거 건들기. 사실 위에 상대 아이디만 채우면 밑에는 맨뒤 조건만 해도 될듯 ?
                                            {
                                                chatflag=0;
                                                break;
                                            }
                                            else
                                            {
                                                startToast("동일한 이름의 다른 친구의 채팅방이 존재합니다!\n다른 채팅방 이름을 사용해 주세요");
                                                chatflag=1;
                                                break;
                                            }
                                        }
                                    }
                                    break;
                                }
                            }

                            /*
                            DataSnapshot tempgetId = dataSnapshot.child("user_list");
                            for (DataSnapshot tempget : tempgetId.getChildren())
                            {
                                System.out.println("tempget"+friend);
                                if(tempget.child("nickname").getValue().toString().equals(user_name.getText().toString()))
                                {
                                    friend=tempget.child("id").getValue().toString();
                                    System.out.println("아이디우효"+friend);
                                    //여기 까지 정상적으로 들어감.

                                    DataSnapshot checkyoursnapshot = dataSnapshot.child("user_list/"+friend+"ChatInfo");
                                    for(DataSnapshot check : checkyoursnapshot.getChildren())
                                    {
                                        System.out.println("Checkmysnapshot"+check.getValue());

                                        if(check.getKey().equals("CHAT " + user_chatname.getText().toString()))
                                        {
                                            if(check.child(myid).getValue().equals(user_name.getText().toString()) &&   //여기서 아마도 euqls가 없어서 그런듯???모르겟다시발 이거만 고치자
                                                    check.child(friend).getValue().equals(mynickname) &&check.child(myid).exists()) //이거 건들기. 사실 위에 상대 아이디만 채우면 밑에는 맨뒤 조건만 해도 될듯 ?
                                            {
                                                chatflag=0;
                                                break;
                                            }
                                            else
                                            {
                                                startToast("동일한 이름의 다른 친구의 채팅방이 존재합니다!\n다른 채팅방 이름을 사용해 주세요");
                                                chatflag=1;
                                                break;
                                            }
                                        }
                                    }
                                    break;
                                }
                            }
                             */

                            if(chatflag==0)
                            {


                                databaseReference.child("user_list/" + myid + "/ChatInfo/").child("CHAT " + user_chatname.getText().toString()).child(myid).setValue(friend_nick);
                                databaseReference.child("user_list/" + myid + "/ChatInfo/").child("CHAT " + user_chatname.getText().toString()).child(friend).setValue(mynickname);
                                // user_list/내아이디/chatinfo에 채팅방 접속 위한 정보 등록
                                databaseReference.child("user_list/" + friend + "/ChatInfo/").child("CHAT " + user_chatname.getText().toString()).child(myid).setValue(friend_nick);
                                databaseReference.child("user_list/" + friend + "/ChatInfo/").child("CHAT " + user_chatname.getText().toString()).child(friend).setValue(mynickname);
                                // user_list/친구아이디/chatinfo에 채팅방 접속 위한 정보 등록


                                startChatActivity(friend, friend_nick);
                            }
                            //}
                        } else{
                            flag--;
                            if(flag==0) startToast("그러한 유저가 존재 하지 않습니다.");
                        }

                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            };
            mPostReference2.addListenerForSingleValueEvent(postListener);

        } catch (NullPointerException e) {

        }
    }



    private void showChatList() {
        // 리스트 어댑터 생성 및 세팅
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.activity_chat_item, R.id.text1);
        chat_list.setAdapter(adapter);

        // 데이터 받아오기 및 어댑터 데이터 추가 및 삭제 등..리스너 관리
        databaseReference.child("user_list/"+ myid +"/myChatlist/"+user_name.getText().toString()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for(DataSnapshot chat : dataSnapshot.getChildren()){
                    //String chatparent = dataSnapshot.getKey();
                    //String fr = chat.getKey();
                    //Log.e("LOG", "dataSnapshotchild.getKey() : " + fr + chatparent);
                    System.out.println("방이름은은요 "+chat.getKey());
                    adapter.add(chat.getKey());
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) { }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) { }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });


        chat_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected_item = (String) parent.getItemAtPosition(position);  // 현재 채팅방 이름 그대로 읽어옴
                selected_item = selected_item.replace("CHAT ", "");

                databaseReference.child("user_list/"+ myid +"/ChatInfo/").child("CHAT "+selected_item+"/"+myid)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {

                                    //System.out.println("eeeee"+dataSnapshot.getValue().toString());
                                    user_name.setText(dataSnapshot.getValue().toString());
                                }else startToast("채팅방이 존재하지 않습니다.\n 친구와 채팅방을 다시 만들고 메시지를 보내주세요");
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) { }
                        });

                //디비에 key2가 들어가면 읽어와서 인텐트 가능.
                user_chatname.setText(selected_item);

                //바로 채팅안으로 인텐트 시킬수 있을까?
            }
        });

        /*
        chat_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected_item = (String) parent.getItemAtPosition(position);  // 현재 채팅방 이름 그대로 읽어옴
                selected_item = selected_item.replace("CHAT ", "");


                //디비에 key2가 들어가면 읽어와서 인텐트 가능.
                user_chatname.setText(selected_item);


                //user_name.setText();

                //바로 채팅안으로 인텐트 시킬수 있을까?

            }
        });
*/

    }


    private void startChatActivity(String user2id, String user2nickname){
        Intent intent = new Intent(this, ChatIn.class);
        intent.putExtra("user2id", user2id);
        intent.putExtra("user2nickname",user2nickname);
        intent.putExtra("chatName", "CHAT "+user_chatname.getText().toString());
        intent.putExtra("chatPlayoneName", mynickname);
        intent.putExtra("chatPlaytwoName", user_name.getText().toString());

        startActivity(intent);
    }

    public void startToast(String msg){ Toast.makeText(this, msg, Toast.LENGTH_SHORT).show(); }

    public static int safeLongToInt(long l) {
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new IllegalArgumentException
                    (l + " cannot be cast to int without changing its value.");
        }
        return (int) l;
    }

}