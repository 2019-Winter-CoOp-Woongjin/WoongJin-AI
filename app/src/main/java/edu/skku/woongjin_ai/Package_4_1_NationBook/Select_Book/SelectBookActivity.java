package edu.skku.woongjin_ai_winter.Package_4_1_NationBook.Select_Book;

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

import edu.skku.woongjin_ai_winter.DrawerDialog;
import edu.skku.woongjin_ai_winter.Package_3_Main.MainActivity;
import edu.skku.woongjin_ai_winter.Package_4_5_ShowFriend.ShowFriendActivity;
import edu.skku.woongjin_ai_winter.Package_4_5_ShowFriend.ShowFriendListAdapter;
import edu.skku.woongjin_ai_winter.Package_4_6_Chat.ChatReadyActivity;
import edu.skku.woongjin_ai_winter.R;
import edu.skku.woongjin_ai_winter.Package_4_1_NationBook.Select_Book.ReadScript.ReadScriptActivity;
import edu.skku.woongjin_ai_winter.UserInfo;

/*
from NationBookActivity
지문 선택하기
 */

public class SelectBookActivity extends AppCompatActivity {

    Intent intent, intentHome, intentReadScript;
    String id, bookType, nickname, thisWeek;
    ImageButton homeButton;
    TextView textView;
    public DatabaseReference mPostReference;
    ListView bookListView;
    ArrayList<String> bookArrayList, backgroundArrayList, studiedBookArrayList;
    SelectBookListAdapter selectBookListAdapter;

////// for navigation drawer

    Button messaging_btn,adding,menu_draw;
    DatabaseReference mPostReference2;
    ListView friend_list;
    ArrayList<String> myFriendList;

   // SelectBookActivity m_oSelectBookActivity = null;
    //////
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectbook);

        intent = getIntent();
        id = intent.getStringExtra("id");
        bookType = intent.getStringExtra("bookType");
        nickname = intent.getStringExtra("nickname");
        thisWeek = intent.getStringExtra("thisWeek");

        intentReadScript = new Intent(SelectBookActivity.this, ReadScriptActivity.class);
        intentReadScript.putExtra("id", id);
        intentReadScript.putExtra("bookType", bookType);
        intentReadScript.putExtra("nickname", nickname);
        intentReadScript.putExtra("thisWeek", thisWeek);

        mPostReference = FirebaseDatabase.getInstance().getReference();

        homeButton = (ImageButton) findViewById(R.id.home);
        bookListView = (ListView) findViewById(R.id.bookList);
        textView = (TextView) findViewById(R.id.selectBook);

        menu_draw = (Button) findViewById(R.id.menu_drawer);

        bookArrayList = new ArrayList<String>();
        backgroundArrayList = new ArrayList<String>();
        studiedBookArrayList = new ArrayList<String>();
        selectBookListAdapter = new SelectBookListAdapter();

        getFirebaseDatabaseBookList();

        if(bookType.equals("science")) {
            textView.setText("과학을 선택했구나!\n아래 목록에서 읽고싶은 지문을 선택해줘~");
        } else if(bookType.equals("history")){
            textView.setText("역사를 선택했구나!\n아래 목록에서 읽고싶은 지문을 선택해줘~");
        }else if(bookType.equals("news")){
            textView.setText("어린이 시사를 선택했구나!\n아래 목록에서 읽고싶은 지문을 선택해줘~");
        }else if(bookType.equals("morality")){
            textView.setText("도덕을 선택했구나!\n아래 목록에서 읽고싶은 지문을 선택해줘~");
        }else if(bookType.equals("mistery")){
            textView.setText("미스터리 소설을 선택했구나!\n아래 목록에서 읽고싶은 지문을 선택해줘~");
        }else if(bookType.equals("comics")){
            textView.setText("웃긴 이야기를 선택했구나!\n아래 목록에서 읽고싶은 지문을 선택해줘~");
        }else if(bookType.equals("oldstory")){
            textView.setText("전래동화를 선택했구나!\n아래 목록에서 읽고싶은 지문을 선택해줘~");
        }else if(bookType.equals("greatman")){
            textView.setText("위인전을 선택했구나!\n아래 목록에서 읽고싶은 지문을 선택해줘~");
        }

        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intentReadScript.putExtra("scriptnm", bookArrayList.get(position));
                intentReadScript.putExtra("background", backgroundArrayList.get(position));
                startActivity(intentReadScript);
            }
        });

        // 메인페이지 버튼 이벤트
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentHome = new Intent(SelectBookActivity.this, MainActivity.class);
                intentHome.putExtra("id", id);
                intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentHome);
            }
        });

        //draw
        menu_draw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (!drawer.isDrawerOpen(Gravity.LEFT)) drawer.openDrawer(Gravity.LEFT);
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
        myFriendList = new ArrayList<String>();

        getFirebaseDatabase();

        final DrawerLayout draw = (DrawerLayout) findViewById(R.id.drawer_layout);
        draw.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                return gdt.onTouchEvent(event);
            }
        });

       // m_oSelectBookActivity = this;

//////
    }


    // 데이터베이스에서 카테고리에 맞는 지문 불러오기
    private void getFirebaseDatabaseBookList(){
        mPostReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                studiedBookArrayList.clear();
                bookArrayList.clear();
                backgroundArrayList.clear();


                for(DataSnapshot snapshot : dataSnapshot.child("script_list").getChildren()){
                    String type = snapshot.child("type").getValue().toString();
                    if(type.equals(bookType)) {
                        String key = snapshot.getKey();
                        int flag = 0;
                        for(String script : studiedBookArrayList) {
                            if(key.equals(script)) {
                                flag = 1;
                                break;
                            }
                        }
                        if(flag == 0) {
                            String key1 = snapshot.getKey();
                            String background = snapshot.child("background").getValue().toString();
                            String bookname = snapshot.child("book_name").getValue().toString();
                            bookArrayList.add(key1);
                            backgroundArrayList.add(background);
                            selectBookListAdapter.addItem(key1, bookname);
                        }
                    }
                }
                bookListView.setAdapter(selectBookListAdapter);
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
                    myFriendList.clear();

                    DataSnapshot snapshot=dataSnapshot.child("user_list/"+id+"/my_friend_list");
                    for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                        String key2 = snapshot1.getKey();
                        myFriendList.add(key2); //myFriendList 속에 내 친구들 id 저장
                    }

                    ShowFriendListAdapter showFriendListAdapter = new ShowFriendListAdapter();
                    DataSnapshot snapshot1=dataSnapshot.child("user_list");
                    for(DataSnapshot snapshot2: snapshot1.getChildren()){
                        String keys=snapshot2.getKey();
                        for(String UID: myFriendList){
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
