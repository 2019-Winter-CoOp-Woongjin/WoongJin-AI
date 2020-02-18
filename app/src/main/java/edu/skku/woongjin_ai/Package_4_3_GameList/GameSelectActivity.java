package edu.skku.woongjin_ai_winter.Package_4_3_GameList;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.skku.woongjin_ai_winter.DrawerDialog;
import edu.skku.woongjin_ai_winter.GlobalApplication;
import edu.skku.woongjin_ai_winter.Package_3_Main.MainActivity;
import edu.skku.woongjin_ai_winter.Package_4_3_GameList.BattleClass.BattleClassRoomListActivity;
import edu.skku.woongjin_ai_winter.Package_4_5_ShowFriend.ShowFriendActivity;
import edu.skku.woongjin_ai_winter.Package_4_5_ShowFriend.ShowFriendListAdapter;
import edu.skku.woongjin_ai_winter.Package_4_6_Chat.ChatReadyActivity;
import edu.skku.woongjin_ai_winter.R;
import edu.skku.woongjin_ai_winter.UserInfo;

public class GameSelectActivity extends AppCompatActivity implements Fragment_game_help.OnFragmentInteractionListener{

    //intent 정보
    String id_key, nickname_key;

    //버튼 추가
    Button gotoBombGame, gotoBattleClass;

    //도움말 1,2, home
    ImageView imageHome, help1, help2;

    //intent
    Intent intent, intentHome, intentBombGame, intentBattleClass;

    //칠판 text뷰
    TextView textView;

    //Fragment
    Fragment_game_help show_game_help;

    String id;

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
        setContentView(R.layout.activity_gameselect);

        id = GlobalApplication.getInstance().getGlobalID();

        //xml파일 연결
        imageHome = findViewById(R.id.home);
        gotoBombGame = findViewById(R.id.gotoBombGame);
        gotoBattleClass = findViewById(R.id.gotoBattleClass);
        help1 = findViewById(R.id.help1);
        textView = findViewById(R.id.title);

        intent = getIntent();
        id_key = intent.getExtras().getString("id");
        nickname_key = intent.getExtras().getString("nickname");

        menu_drawer = (Button) findViewById(R.id.menu_drawer);

        //textView 칠판
        textView.setText(nickname_key + "(이)가 할 수 있는 게임들이야~ \n 무슨 게임인지 궁금하면 도움말을 눌러봐~");

        // 홈 버튼 이벤트
        imageHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentHome = new Intent(GameSelectActivity.this, MainActivity.class);
                intentHome.putExtra("id", id_key);
                intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentHome);
            }
        });

        //도움말 버튼 이벤트 꾸미기
        show_game_help = new Fragment_game_help();
        help1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
                fragmentTransaction.commit();
                show_game_help = new Fragment_game_help();

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.gameselect, show_game_help);
                transaction.commit();

            }
        });

        menu_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (!drawer.isDrawerOpen(Gravity.LEFT)) drawer.openDrawer(Gravity.LEFT);
            }
        });

        //폭탄게임 버튼 이벤트
        gotoBombGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentBombGame = new Intent(GameSelectActivity.this, GameListActivity.class);
                intentBombGame.putExtra("id", id_key);
                intentBombGame.putExtra("nickname", nickname_key);
                startActivity(intentBombGame);
            }
        });

        //배틀게임 버튼 이벤트
        gotoBattleClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentBattleClass = new Intent(GameSelectActivity.this, BattleClassRoomListActivity.class);
                intentBattleClass.putExtra("id", id_key);
                intentBattleClass.putExtra("nickname", nickname_key);
                startActivity(intentBattleClass);
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

       // m_oGameSelectActivity = this;
//////



    }

    @Override
    public void onFragmentInteraction(Uri uri) {

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
