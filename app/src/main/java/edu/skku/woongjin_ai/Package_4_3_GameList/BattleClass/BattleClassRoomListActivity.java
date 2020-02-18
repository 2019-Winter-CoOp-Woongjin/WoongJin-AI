package edu.skku.woongjin_ai.Package_4_3_GameList.BattleClass;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.skku.woongjin_ai.GlobalApplication;
import edu.skku.woongjin_ai.Package_3_Main.MainActivity;
import edu.skku.woongjin_ai.Package_4_3_GameList.BattleClass.MakingRoom.MakingRoomActivity;
import edu.skku.woongjin_ai.Package_4_5_ShowFriend.ShowFriendActivity;
import edu.skku.woongjin_ai.Package_4_5_ShowFriend.ShowFriendListAdapter;
import edu.skku.woongjin_ai.Package_4_6_Chat.ChatReadyActivity;
import edu.skku.woongjin_ai.R;
import edu.skku.woongjin_ai.UserInfo;

public class BattleClassRoomListActivity extends AppCompatActivity
        implements BattleClassRoomListAdapter.OnNoteListener {
    //방만들기 버튼
    private Button makingRoom;

    //파베 사용하기위해
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(); //파이어베이스 데이터베이스 연동
    private DatabaseReference databaseReference = firebaseDatabase.getReference(); //DB 테이블 연결

    //리사이클러뷰 사용하기위해
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<RoomInfo> arrayList;

    //리싸이클러뷰 눌렀을때 방번호알기위해
    private String mRoomnum = null;

    //홈버튼, 도움말버튼
    private ImageView imageHome, help;


    private String id_key, nickname_key;
    private Intent intent;

    //입장할때 스크립트 전역변수에 넣을려고
    private String script_text;
    private String mName = null;

    //유저 코인, 이름 정보들
    private TextView userName2, userGrade2, userCoin2;
    private UserInfo me;

    GlobalApplication gv;

////// for navigation drawer

    Button messaging_btn,adding;
    DatabaseReference mPostReference2;
    ListView friend_list;
    ArrayList<String> myFriendLists;
    //ShowFriendQuizActivity m_oShowFriendQuizActivity = null;

    Button menu_drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battleclassroomlist);


        gv = (GlobalApplication) getApplication();


        //방만드는 버튼 xml 연결
        makingRoom = findViewById(R.id.makingRoomButton);

        //리싸이클러뷰와 어뎁터 관련 연결
        recyclerView = findViewById(R.id.gameroomList); // activity_watingroom xml에 있는 리싸이클러뷰 연결
        recyclerView.setHasFixedSize(true); //리싸이클러뷰 성능강화
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>(); // RoomInfo 객체를 담을 어레이 리스트(어뎁터쪽으로 날림)

        //리싸이클러뷰 구분선 추가
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));

        //도움말, 홈버튼
        help = (ImageView) findViewById(R.id.helpbtn2);

        imageHome = (ImageView) findViewById(R.id.battlehome);

        menu_drawer = (Button) findViewById(R.id.menu_drawer);

        //유저 코인정보들
        userName2 = (TextView) findViewById(R.id.userName2);
        userGrade2 = (TextView) findViewById(R.id.userGrade2);
        userCoin2 = (TextView) findViewById(R.id.userCoin2);


       // System.out.println("ab"+gv.getRoomnum());

        //.child("game").child("room").child(""+gv.getRoomnum())

        //받아오기
        intent = getIntent();
        id_key = intent.getExtras().getString("id");
        nickname_key = intent.getExtras().getString("nickname");

        menu_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (!drawer.isDrawerOpen(Gravity.LEFT)) drawer.openDrawer(Gravity.LEFT);
            }
        });

        //헬프버튼 눌럿을떄
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentHelp = new Intent(BattleClassRoomListActivity.this, BattleClassRoomList_help.class);
                intentHelp.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentHelp);

            }
        });


        //홈버튼 눌럿을떄
        imageHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentHome = new Intent(BattleClassRoomListActivity.this, MainActivity.class);
                intentHome.putExtra("id", id_key);
                intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentHome);
                finish();
            }
        });

        //코인정보 파이어베이스 데이터 읽기
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                DataSnapshot dataSnapshot1=dataSnapshot.child("user_list/"+id_key+"/my_week_list");

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String key = snapshot.getKey();
                    if (key.equals("user_list")) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            String key1 = snapshot1.getKey();
                            if (key1.equals(id_key)) {
                                me = snapshot1.getValue(UserInfo.class);
                                userName2.setText(me.name + " 학생");
                                userGrade2.setText(me.grade + "학년");
                                userCoin2.setText(me.coin + " 코인");
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //파이어베이스 데이터 읽기
        databaseReference.child("gameroom_list2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //실제적으로 파이어베이스 데이터베이스의 데이터를 받아오는 곳

                arrayList.clear(); //RoomInfo객체를 담을 어레이리스트인데 기존 배열 리스트가 존재하지않게 초기화

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){ //반복문으로 데이터 리스트 추출

                    RoomInfo roomInfo = new RoomInfo(); //파이어베이스로 읽은 것들을 RoomInfoclass에 박아주는것. 그 이후 어레이리스트에 담아서 어뎁터로 쏘는것
                    roomInfo.bookname = snapshot.child("bookName").getValue(String.class);
                    roomInfo.hostname = snapshot.child("player0").child("name").getValue(String.class);
                    roomInfo.roomname = snapshot.child("roomName").getValue(String.class);
                    roomInfo.personnum = snapshot.child("personNum").getValue(String.class);
                    roomInfo.roomnum = snapshot.child("roomNum").getValue(String.class);

                    roomInfo.bookname1 = "책 이름 :";
                    roomInfo.hostname1 =  "방장 이름 :";
                    roomInfo.roomname1 =  "방 이름 :";
                    roomInfo.personnum1 =  "사람 수 :";
                    roomInfo.roomnum1 =  "방 번호 :";

                    //RoomInfo roomInfo = snapshot.getValue(RoomInfo.class);

                    arrayList.add(roomInfo); //어레이 리스트에 담음, 담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비 끝



                }

                adapter.notifyDataSetChanged(); //리스트 저장 및 새로고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //디비를 가져오던 중 에러 발생 시
            }
        });

        //이제 어뎁터를 생성, 이제 어레이리스트에 context 넘겨줄게
        adapter = new BattleClassRoomListAdapter(arrayList, this, this);
        recyclerView.setAdapter(adapter); //리싸이클러뷰에 어뎁터 연결


        //방만들기 버튼 눌렀을때
        makingRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(BattleClassRoomListActivity.this, MakingRoomActivity.class);
                intent.putExtra("id", id_key);
                startActivity(intent);
            }
        });


////// for navigation drawer

        messaging_btn = (Button)findViewById(R.id.messaging_btn);
        messaging_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChatActivity();
            }
        });
        adding = (Button)findViewById(R.id.adding);
        adding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startShowFriendActivity();

            }
        });

        friend_list = (ListView) findViewById(R.id.friend_list);
        mPostReference2 = FirebaseDatabase.getInstance().getReference();
        myFriendLists = new ArrayList<String>();

        getFirebaseDatabase();

        final DrawerLayout draw = (DrawerLayout) findViewById(R.id.drawer_layout);
        draw.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                return gdt.onTouchEvent(event);
            }
        });

        //m_oBattleClassRoomAcitivity=this;
//////

    }


    //리사이클러뷰 리스트 눌렀을때
    @Override
    public void onNoteClick(int position) {

        //입장하는 사람이 리사이클러뷰 클릭했을 때 방번호를 알기 위해
        RoomInfo dict = arrayList.get(position);
        mRoomnum = dict.getRoomnum();
        mName = dict.getBookname();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (dataSnapshot.child("user_list").child(gv.getGlobalID()).child("my_script_list").child(mName).exists()) {
                    String personnum = dataSnapshot.child("gameroom_list2").child("" + mRoomnum).child("personNum").getValue(String.class);


                    //방인원수가 2가 아닌경우 String이므로 equals로 비교
                    if (!personnum.equals("2")) {

                        //전역변수 위해
                        gv.setRoomnum(Integer.parseInt(mRoomnum));

                        //게임들어간사람은 player 1로 전역변수 ㄱㄱ
                        gv.setGameID(1);

                        //게임들어가면 playerNum은 2로 DB에 넣기ㄱㄱ
                        String personNum = "2";
                        databaseReference.child("gameroom_list2").child("" + mRoomnum).child("personNum").setValue(personNum);

                        //게임을 위해 player1에 정보 넣기
                        databaseReference.child("gameroom_list2").child("" + mRoomnum).child("player" + gv.getGameID()).setValue(new GamePlayer(gv.getGlobalName(), gv.getGameID(), gv.getGlobalID(), 0, 0, 0, 3));


                        Intent intent = new Intent(BattleClassRoomListActivity.this, MainPage.class);
                        intent.putExtra("id", id_key);
                        startActivity(intent);

                        //책 script 전역변수에 넣기
                        script_text = dataSnapshot.child("script_list").child("" + mName).child("text").getValue(String.class);
                        gv.setScript(script_text);


                    } else if (personnum.equals("2")) { //방인원수가 2인경우, string이므로 equals로 비교
                        Toast.makeText(BattleClassRoomListActivity.this, "현재 클릭하신 방 인원이 꽉찼습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(BattleClassRoomListActivity.this, "잠시 오류가 있습니다.", Toast.LENGTH_SHORT).show();
                    }
                }

                else {
                    Toast.makeText(BattleClassRoomListActivity.this, "책을 먼저 읽고 와야해요!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    ////// for navigation drawer
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    // swipe 구현
    private final GestureDetector gdt = new GestureDetector(new GestureListener());

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private final int SWIPE_MIN_DISTANCE = 250;
        private final int SWIPE_THRESHOLD_VELOCITY = 50;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if(e1.getX() - e2.getX() > (SWIPE_MIN_DISTANCE-100) && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(Gravity.LEFT))  onBackPressed();
                return true;
            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE &&     Math.abs(velocityX) >  SWIPE_THRESHOLD_VELOCITY) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (!drawer.isDrawerOpen(Gravity.LEFT)) drawer.openDrawer(Gravity.LEFT);
                //Log.i(logTag, "sucessssss!");
                return true;
            }
            if(e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) >     SWIPE_THRESHOLD_VELOCITY) {
                // Bottom to top, your code here
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(Gravity.LEFT))  onBackPressed();
                return true;
            } else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE &&    Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                // Top to bottom, your code here
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(Gravity.LEFT))  onBackPressed();
                return true;
            }
            return false;
        }
    }

    public void getFirebaseDatabase() {
        try {
            final ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    myFriendLists.clear();

                    DataSnapshot snapshot=dataSnapshot.child("user_list/"+id_key+"/my_friend_list");
                    for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                        String key2 = snapshot1.getKey();
                        myFriendLists.add(key2); //myFriendList 속에 내 친구들 id 저장
                    }

                    ShowFriendListAdapter showFriendListAdapter = new ShowFriendListAdapter();
                    DataSnapshot snapshot1=dataSnapshot.child("user_list");
                    for(DataSnapshot snapshot2: snapshot1.getChildren()){
                        String keys=snapshot2.getKey();
                        for(String UID: myFriendLists){
                            if(UID.equals(keys)){
                                UserInfo friend=snapshot2.getValue(UserInfo.class);
                                showFriendListAdapter.addItem(friend.profile, friend.name, friend.grade, friend.school, friend.id, friend.nickname, false, true, friend.onoffline);
                            }
                        }
                    }
                    friend_list.setAdapter(showFriendListAdapter);
                    friend_list.clearChoices();
                    showFriendListAdapter.notifyDataSetChanged();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            };
            mPostReference2.addValueEventListener(postListener);

        } catch (java.lang.NullPointerException e) {

        }
    }




    private void startShowFriendActivity(){
        Intent intent = new Intent(this, ShowFriendActivity.class);
        // 액티비티 기록 지움으로써 뒤로가기 버튼으로 무한 츠쿠요미 벗어나기.
        // 메인화면에서 뒤로가기 하면 꺼지도록 함.
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void startChatActivity(){
        Intent intent = new Intent(this, ChatReadyActivity.class);
        // 액티비티 기록 지움으로써 뒤로가기 버튼으로 무한 츠쿠요미 벗어나기.
        // 메인화면에서 뒤로가기 하면 꺼지도록 함.
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    /*
    public void showDialog(View _view)
    {
        DrawerDialog oDialog = new DrawerDialog(this);
        oDialog.setCancelable(false);
        oDialog.show();
    }

     */
//////

}
