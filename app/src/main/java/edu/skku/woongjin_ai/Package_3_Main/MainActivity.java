package edu.skku.woongjin_ai_winter.Package_3_Main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import edu.skku.woongjin_ai_winter.DrawerDialog;
import edu.skku.woongjin_ai_winter.GlobalApplication;
import edu.skku.woongjin_ai_winter.Package_4_2_MainQuizType.MainQuizTypeFragment;
import edu.skku.woongjin_ai_winter.Package_4_1_NationBook.Select_Book.ReadScript.SelectType.NewHoonjangFragment;
import edu.skku.woongjin_ai_winter.Package_4_3_GameList.GameSelectActivity;
import edu.skku.woongjin_ai_winter.Package_4_6_Chat.ChatReadyActivity;
import edu.skku.woongjin_ai_winter.Package_4_4_MyPage.MyPageActivity;
import edu.skku.woongjin_ai_winter.Package_4_1_NationBook.NationBookActivity;
import edu.skku.woongjin_ai_winter.Package_4_5_ShowFriend.ShowFriendActivity;
import edu.skku.woongjin_ai_winter.Package_4_5_ShowFriend.ShowFriendListAdapter;
import edu.skku.woongjin_ai_winter.R;
import edu.skku.woongjin_ai_winter.UserInfo;

/*
from LoginActivity
메인 페이지
 */

public class MainActivity extends AppCompatActivity implements NewHoonjangFragment.OnFragmentInteractionListener, MainQuizTypeFragment.OnFragmentInteractionListener {

    public DatabaseReference mPostReference, mPostReference2;
    Intent intent, intentBook, intentGameSelect, intentMyPage;
    String id, nickname="";
    String school, mygrade, profile, myname;
    LinearLayout bookButton, quizButton, gameButton;
    Button myPageButton;
    TextView userNickname;
    public MainQuizTypeFragment mainQuizTypeFragment;
    UserInfo me;
    public NewHoonjangFragment hoonjangFragment_attend, hoonjangFragment_read;

    SharedPreferences setting;
    SharedPreferences.Editor editor;
    String nomore_atd, nomore_read;
    int thisWeek;

    //Intent intent;

    //////
//////
    String identify, friend = "";
    ListView friend_list;
    ArrayList<String> myFriendList;
    ArrayList<UserInfo> recommendList, recommendFinalList;

    //MainActivity m_oMainActivity = null;
    //ArrayAdapter<String> randomFriendAdapter;
    //private DatabaseReference mPostReference;
//////

    //////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setting = getSharedPreferences("nomore", MODE_PRIVATE);
        nomore_atd = setting.getString("main_attend", "keepgoing");
        nomore_read = setting.getString("main_read", "keepgoing");

        findViewById(R.id.ReadActivity).setOnClickListener(onClickListener);
        findViewById(R.id.QuizActivity).setOnClickListener(onClickListener);
        findViewById(R.id.GameActivity).setOnClickListener(onClickListener);
        findViewById(R.id.myPage).setOnClickListener(onClickListener);
        userNickname = (TextView) findViewById(R.id.main);
        mainQuizTypeFragment = new MainQuizTypeFragment();


        intent = getIntent();
        id = intent.getStringExtra("id");

        recommendList = new ArrayList<UserInfo>();

        mPostReference = FirebaseDatabase.getInstance().getReference();
        mPostReference2 = FirebaseDatabase.getInstance().getReference();

        getFirebaseDatabaseUserInfo();
        postFirebaseDatabaseAttend();

        intent = getIntent();
        identify = intent.getStringExtra("id");


//////
        findViewById(R.id.messaging_btn).setOnClickListener(onClickListener);
        findViewById(R.id.adding).setOnClickListener(onClickListener);
        friend_list = (ListView) findViewById(R.id.friend_list);
        myFriendList = new ArrayList<String>();

        getFirebaseDatabase();

        final DrawerLayout draw = (DrawerLayout) findViewById(R.id.drawer_layout);
        draw.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                return gdt.onTouchEvent(event);
            }
        });

        //m_oMainActivity = this;

        findViewById(R.id.menu_drawer).setOnClickListener(onClickListener);

    }

    //button click event!
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                //4-1
                case R.id.ReadActivity:
                    intentBook = new Intent(MainActivity.this, NationBookActivity.class);
                    intentBook.putExtra("id", id);
                    intentBook.putExtra("nickname", nickname);
                    intentBook.putExtra("thisWeek", Integer.toString(thisWeek));
                    startActivity(intentBook);

                    break;


                //4-2
                case R.id.QuizActivity:
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.mainQuizSelect, mainQuizTypeFragment);
                    Bundle bundle = new Bundle(3);
                    bundle.putString("id", id);
                    bundle.putString("nickname", nickname);
                    bundle.putString("thisWeek", Integer.toString(thisWeek));
                    mainQuizTypeFragment.setArguments(bundle);
                    transaction.addToBackStack(null);
                    transaction.commit();

                    break;


                //4-3 게임나라눌렀을때 게임select으로
                case R.id.GameActivity:

                    intentGameSelect = new Intent(MainActivity.this, GameSelectActivity.class);
                    intentGameSelect.putExtra("id", id);
                    intentGameSelect.putExtra("nickname", nickname);
                    startActivity(intentGameSelect);

                    break;

                //4-4
                case R.id.myPage:
                    intentMyPage = new Intent(MainActivity.this, MyPageActivity.class);
                    intentMyPage.putExtra("id", id);
                    intentMyPage.putExtra("nickname", nickname);
                    intentMyPage.putExtra("grade", mygrade);
                    intentMyPage.putExtra("profile", profile);
                    intentMyPage.putExtra("school", school);
                    intentMyPage.putExtra("name", myname );
                    startActivity(intentMyPage);

                    break;

                //4-5
                case R.id.messaging_btn:
                    startChatActivity();

                    break;

                //4-6
                case R.id.adding:
                    startShowFriendActivity();

                    break;

                case R.id.menu_drawer:
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    if (!drawer.isDrawerOpen(Gravity.LEFT)) drawer.openDrawer(Gravity.LEFT);
                    break;

            }
        }
    };

    // 출석 체크
    private void postFirebaseDatabaseAttend() {
        mPostReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int weekNum = 0;
                for(DataSnapshot snapshot : dataSnapshot.child("user_list/" + id + "/my_week_list").getChildren()) weekNum++;

                Calendar calendar = Calendar.getInstance();
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                String dayOfWeekS = "";
                switch(dayOfWeek) {
                    case 1:
                        dayOfWeekS = "일"; calendar.add(Calendar.DAY_OF_MONTH, -6); break;
                    case 2:
                        dayOfWeekS = "월"; break;
                    case 3:
                        dayOfWeekS = "화"; calendar.add(Calendar.DAY_OF_MONTH, -1); break;
                    case 4:
                        dayOfWeekS = "수"; calendar.add(Calendar.DAY_OF_MONTH, -2); break;
                    case 5:
                        dayOfWeekS = "목"; calendar.add(Calendar.DAY_OF_MONTH, -3); break;
                    case 6:
                        dayOfWeekS = "금"; calendar.add(Calendar.DAY_OF_MONTH, -4); break;
                    case 7:
                        dayOfWeekS = "토"; calendar.add(Calendar.DAY_OF_MONTH, -5); break;
                }

                String startDate = "";
                Date dateS = calendar.getTime();

                if(weekNum == 0) {
                    weekNum++;
                    String startDate2 = new SimpleDateFormat("yyyy-MM-dd").format(dateS);
/*
//this is origin
                    mPostReference.child("user_list/" + id + "/my_week_list/week" + weekNum + "/cnt").setValue(0);
                    mPostReference.child("user_list/" + id + "/my_week_list/week" + weekNum + "/correct").setValue(0);
                    mPostReference.child("user_list/" + id + "/my_week_list/week" + weekNum + "/level").setValue(0);
                    mPostReference.child("user_list/" + id + "/my_week_list/week" + weekNum + "/like").setValue(0);
                    mPostReference.child("user_list/" + id + "/my_week_list/week" + weekNum + "/made").setValue(0);
                    mPostReference.child("user_list/" + id + "/my_week_list/week" + weekNum + "/solvebomb").setValue(0);
                    mPostReference.child("user_list/" + id + "/my_week_list/week" + weekNum + "/firstDateOfWeek").setValue(startDate2);
*/
                    mPostReference.child(id + "/my_week_list/week" + weekNum + "/cnt").setValue(0);
                    mPostReference.child(id + "/my_week_list/week" + weekNum + "/correct").setValue(0);
                    mPostReference.child(id + "/my_week_list/week" + weekNum + "/level").setValue(0);
                    mPostReference.child(id + "/my_week_list/week" + weekNum + "/like").setValue(0);
                    mPostReference.child(id + "/my_week_list/week" + weekNum + "/made").setValue(0);
                    mPostReference.child(id + "/my_week_list/week" + weekNum + "/solvebomb").setValue(0);
                    mPostReference.child(id + "/my_week_list/week" + weekNum + "/firstDateOfWeek").setValue(startDate2);
                } else startDate = dataSnapshot.child("user_list/" + id + "/my_week_list/week" + weekNum + "/firstDateOfWeek").getValue().toString();
                //startDate = dataSnapshot.child("user_list/" + id + "/my_week_list/week" + weekNum + "/firstDateOfWeek").getValue().toString();

                long now = System.currentTimeMillis();
                Date date = new Date(now);
                String today = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                String todayDate = new SimpleDateFormat("yyyy-MM-dd").format(date);

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date date1 = new Date();
                Date date2 = new Date();
                try {
                    date1 = format.parse(todayDate);
                    date2 = format.parse(startDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if(Math.abs((date1.getTime() - date2.getTime()) / (24*60*60*1000)) > 6) {
                    weekNum++;
                    String startDate2 = new SimpleDateFormat("yyyy-MM-dd").format(dateS);
                    //user_list안에 새로운 user_list생성시키길래 바꿈. 에물레이터에서만 그럴지도?
                    // 만약 에뮬레이터에서만 이런 오류 발생할 경우,
                    // this is origin

                    mPostReference.child("user_list/" + id + "/my_week_list/week" + weekNum + "/cnt").setValue(0);
                    mPostReference.child("user_list/" + id + "/my_week_list/week" + weekNum + "/correct").setValue(0);
                    mPostReference.child("user_list/" + id + "/my_week_list/week" + weekNum + "/level").setValue(0);
                    mPostReference.child("user_list/" + id + "/my_week_list/week" + weekNum + "/like").setValue(0);
                    mPostReference.child("user_list/" + id + "/my_week_list/week" + weekNum + "/made").setValue(0);
                    mPostReference.child("user_list/" + id + "/my_week_list/week" + weekNum + "/solvebomb").setValue(0);
                    mPostReference.child("user_list/" + id + "/my_week_list/week" + weekNum + "/firstDateOfWeek").setValue(startDate2);
                    mPostReference.child("user_list/" + id + "/my_week_list/week" + weekNum + "/attend_list/" + dayOfWeekS).setValue(today);

                } else {
                    if(!dataSnapshot.child("user_list/" + id + "/my_week_list/week" + weekNum + "/attend_list/" + dayOfWeekS).exists()) {
                        mPostReference.child("user_list/" + id + "/my_week_list/week" + weekNum + "/attend_list/" + dayOfWeekS).setValue(today);
                        //mPostReference.child(id + "/my_week_list/week" + weekNum + "/attend_list/" + dayOfWeekS).setValue(today);

                    }
                }

                thisWeek = weekNum;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        });
    }

    // 데이터베이스에서 유저 정보 가져오기
    private void getFirebaseDatabaseUserInfo() {
        mPostReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                me = dataSnapshot.child("user_list/" + id).getValue(UserInfo.class);
                nickname = me.nickname;
                mygrade=me.grade;
                myname=me.name;
                school=me.school;
                profile=me.profile;
                GlobalApplication.getInstance().setGlobalStr(id, profile, mygrade, school, nickname, myname);

                userNickname.setText("안녕 " + nickname + "!\n여행하고 싶은 나라를 골라보자!");
                int AttendCount=0;
                long ReadCount=0;
                ReadCount=dataSnapshot.child("user_list/"+id+"/my_script_list").getChildrenCount();
                DataSnapshot dataSnapshot1=dataSnapshot.child("user_list/"+id+"/my_week_list");
                for(DataSnapshot dataSnapshot2: dataSnapshot1.getChildren()){ //week 껍데기
                    AttendCount+=dataSnapshot2.child("attend_list").getChildrenCount();
                }
                Calendar calendar = Calendar.getInstance();
                Date dateS = calendar.getTime();
                String MedalUpdate = new SimpleDateFormat("yyyy-MM-dd").format(dateS);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                hoonjangFragment_attend=new NewHoonjangFragment();
                hoonjangFragment_read=new NewHoonjangFragment();

                Log.d("nomore", nomore_atd);
                String atdcnt=Integer.toString(AttendCount);
                Log.d("AttendCount", atdcnt);

                if(AttendCount==365 && nomore_atd.equals("stop2")) {
                    uploadFirebaseUserCoinInfo_H("출석왕", 3);
                    mPostReference.child("user_list/" + id + "/my_medal_list/출석왕").setValue("Lev3##"+MedalUpdate);
                    SharedPreferences sf = getSharedPreferences("nomore", MODE_PRIVATE);
                    editor=sf.edit();
                    editor.putString("main_attend", "stop3");
                    editor.commit();
                    transaction.replace(R.id.Mainframe, hoonjangFragment_attend);
                    Bundle bundle = new Bundle(3);
                    bundle.putString("what", "attend");
                    bundle.putString("from", "main_attend");
                    bundle.putInt("level", 3);
                    hoonjangFragment_attend.setArguments(bundle);
                    transaction.commit();
                }else if(AttendCount==100 && nomore_atd.equals("stop1")){
                    uploadFirebaseUserCoinInfo_H("출석왕", 2);
                    mPostReference.child("user_list/" + id + "/my_medal_list/출석왕").setValue("Lev2##"+MedalUpdate);
                    SharedPreferences sf = getSharedPreferences("nomore", MODE_PRIVATE);
                    editor=sf.edit();
                    editor.putString("main_attend", "stop2");
                    editor.commit();
                    transaction.replace(R.id.Mainframe, hoonjangFragment_attend);
                    Bundle bundle = new Bundle(3);
                    bundle.putString("what", "attend");
                    bundle.putString("from", "main_attend");
                    bundle.putInt("level", 2);
                    hoonjangFragment_attend.setArguments(bundle);
                    transaction.commit();
                }else if(AttendCount==30 && nomore_atd.equals("keepgoing")){
                    uploadFirebaseUserCoinInfo_H("출석왕", 1);
                    mPostReference.child("user_list/" + id + "/my_medal_list/출석왕").setValue("Lev1##"+MedalUpdate);
                    SharedPreferences sf = getSharedPreferences("nomore", MODE_PRIVATE);
                    editor=sf.edit();
                    editor.putString("main_attend", "stop1");
                    editor.commit();
                    transaction.replace(R.id.Mainframe, hoonjangFragment_attend);
                    Bundle bundle = new Bundle(3);
                    bundle.putString("what", "attend");
                    bundle.putString("from", "main_attend");
                    bundle.putInt("level", 1);
                    hoonjangFragment_attend.setArguments(bundle);
                    transaction.commit();
                }
                if(ReadCount==150 && nomore_read.equals("stop2")) {
                    uploadFirebaseUserCoinInfo_H("다독왕", 3);
                    mPostReference.child("user_list/" + id + "/my_medal_list/다독왕").setValue("Lev3##"+MedalUpdate);
                    SharedPreferences sf = getSharedPreferences("nomore", MODE_PRIVATE);
                    editor=sf.edit();
                    editor.putString("main_read", "stop3");
                    editor.commit();
                    transaction.replace(R.id.Mainframe, hoonjangFragment_read);
                    Bundle bundle = new Bundle(3);
                    bundle.putString("what", "read");
                    bundle.putString("from", "main_read");
                    bundle.putInt("level", 3);
                    hoonjangFragment_read.setArguments(bundle);
                    transaction.commit();
                }else if(ReadCount==100 && nomore_read.equals("stop1")){
                    uploadFirebaseUserCoinInfo_H("다독왕", 2);
                    mPostReference.child("user_list/" + id + "/my_medal_list/다독왕").setValue("Lev2##"+MedalUpdate);
                    SharedPreferences sf = getSharedPreferences("nomore", MODE_PRIVATE);
                    editor=sf.edit();
                    editor.putString("main_read", "stop2");
                    editor.commit();
                    transaction.replace(R.id.Mainframe, hoonjangFragment_read);
                    Bundle bundle = new Bundle(3);
                    bundle.putString("what", "attend");
                    bundle.putString("from", "main_read");
                    bundle.putInt("level", 2);
                    hoonjangFragment_read.setArguments(bundle);
                    transaction.commit();
                }else if(ReadCount==50 && nomore_read.equals("keepgoing")){
                    uploadFirebaseUserCoinInfo_H("다독왕", 1);
                    mPostReference.child("user_list/" + id + "/my_medal_list/다독왕").setValue("Lev1##"+MedalUpdate);
                    SharedPreferences sf = getSharedPreferences("nomore", MODE_PRIVATE);
                    editor=sf.edit();
                    editor.putString("main_read", "stop1");
                    editor.commit();
                    transaction.replace(R.id.Mainframe, hoonjangFragment_read);
                    Bundle bundle = new Bundle(3);
                    bundle.putString("what", "attend");
                    bundle.putString("from", "main_read");
                    bundle.putInt("level", 1);
                    hoonjangFragment_read.setArguments(bundle);
                    transaction.commit();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        });
    }

    // 훈장 수여 자격 여부 확인 및 수여, 데이터베이스에 저장
    private void uploadFirebaseUserCoinInfo_H(String hoonjangname, int level){
        mPostReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                String today = new SimpleDateFormat("yyMMddHHmm").format(date);
                mPostReference.child("user_list/" + id + "/my_coin_list/" + today + "/get").setValue(Integer.toString(level*100));
                mPostReference.child("user_list/" + id + "/my_coin_list/" + today + "/why").setValue(hoonjangname+" 레벨 "+Integer.toString(level)+"달성!");

                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    String key=dataSnapshot1.getKey();
                    if(key.equals("user_list")){
                        String mycoin=dataSnapshot1.child(id).child("coin").getValue().toString();
                        int coin = Integer.parseInt(mycoin) + level*100;
                        String coin_convert = Integer.toString(coin);
                        mPostReference.child("user_list/" + id).child("coin").setValue(coin_convert);
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

//////
//////

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
    private final GestureDetector gdt = new GestureDetector(new     GestureListener());

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

    public void showDialog(View _view)
    {
        DrawerDialog oDialog = new DrawerDialog(this);
        oDialog.setCancelable(false);
        oDialog.show();
    }

}


