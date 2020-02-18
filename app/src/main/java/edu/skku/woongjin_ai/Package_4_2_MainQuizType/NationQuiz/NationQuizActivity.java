package edu.skku.woongjin_ai.Package_4_2_MainQuizType.NationQuiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.skku.woongjin_ai.Package_3_Main.MainActivity;
import edu.skku.woongjin_ai.Package_4_1_NationBook.Select_Book.ReadScript.SelectType.SelectTypeActivity;
import edu.skku.woongjin_ai.Package_4_1_NationBook.Select_Book.SelectBookListAdapter;
import edu.skku.woongjin_ai.Package_4_5_ShowFriend.ShowFriendActivity;
import edu.skku.woongjin_ai.Package_4_5_ShowFriend.ShowFriendListAdapter;
import edu.skku.woongjin_ai.Package_4_6_Chat.ChatReadyActivity;
import edu.skku.woongjin_ai.R;
import edu.skku.woongjin_ai.UserInfo;

/*
from MainQuizTypeFragment
질문나라 - 문제 낼 지문 선택하기
 */
public class NationQuizActivity extends AppCompatActivity {

    Intent intent, intentHome, intentMakeQuiz, intentFriendQuiz;
    String id, nickname, thisWeek;
    TextView textView;
    ImageButton homeButton;
    public DatabaseReference mPostReference;
    ListView studiedBookListView;
    SelectBookListAdapter selectBookListAdapter;
    ArrayList<String> studiedBookArrayList, backgroundArrayList;


////// for navigation drawer

    Button messaging_btn,adding;
    DatabaseReference mPostReference2;
    ListView friend_list;
    ArrayList<String> myFriendLists;
    //ShowFriendQuizActivity m_oShowFriendQuizActivity = null;

    Button menu_drawer;
//////

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nationquiz);

        intent = getIntent();
        id = intent.getStringExtra("id");
        nickname = intent.getStringExtra("nickname");
        thisWeek = intent.getStringExtra("thisWeek");

        mPostReference = FirebaseDatabase.getInstance().getReference();

        textView = (TextView) findViewById(R.id.nationQuiz);
        homeButton = (ImageButton) findViewById(R.id.home);
        studiedBookListView = (ListView) findViewById(R.id.studiedBookList);

        menu_drawer = (Button) findViewById(R.id.menu_drawer);

        studiedBookArrayList = new ArrayList<String>();
        backgroundArrayList = new ArrayList<String>();
        selectBookListAdapter = new SelectBookListAdapter();

        getFirebaseDatabaseStudiedBookList();

        textView.setText(nickname + "(이)가 읽은 책 목록이야~\n문제를 내고 싶은 책을 클릭하면 문제를 만들 수 있어!");

        menu_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (!drawer.isDrawerOpen(Gravity.LEFT)) drawer.openDrawer(Gravity.LEFT);
            }
        });

        studiedBookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long i) {
                intentMakeQuiz = new Intent(NationQuizActivity.this, SelectTypeActivity.class);
                intentMakeQuiz.putExtra("id", id);
                intentMakeQuiz.putExtra("scriptnm", studiedBookArrayList.get(position));
                intentMakeQuiz.putExtra("background", backgroundArrayList.get(position));
                intentMakeQuiz.putExtra("nickname", nickname);
                intentMakeQuiz.putExtra("thisWeek", thisWeek);
                startActivity(intentMakeQuiz);
                finish();
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentHome = new Intent(NationQuizActivity.this, MainActivity.class);
                intentHome.putExtra("id", id);
                intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentHome);
                finish();
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

        //m_oNationQuizActivity = this;
//////

    }

    private void getFirebaseDatabaseStudiedBookList() {
        mPostReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                studiedBookArrayList.clear();
                backgroundArrayList.clear();

                for(DataSnapshot snapshot : dataSnapshot.child("user_list/" + id + "/my_script_list").getChildren()) {
                    String key = snapshot.getKey();
                    String bookname = dataSnapshot.child("script_list/" + key + "/book_name").getValue().toString();
                    studiedBookArrayList.add(key);
                    selectBookListAdapter.addItem(key, bookname);
                }
                studiedBookListView.setAdapter(selectBookListAdapter);

                for(DataSnapshot snapshot : dataSnapshot.child("script_list").getChildren()) {
                    String key = snapshot.getKey();
                    for(String script : studiedBookArrayList) {
                        if(key.equals(script)) {
                            String background = snapshot.child("background").getValue().toString();
                            backgroundArrayList.add(background);
                            break;
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
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

                    DataSnapshot snapshot=dataSnapshot.child("user_list/"+id+"/my_friend_list");
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
    }*/
//////

}
