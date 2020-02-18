package edu.skku.woongjin_ai_winter.Package_4_2_MainQuizType;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import edu.skku.woongjin_ai_winter.DrawerDialog;
import edu.skku.woongjin_ai_winter.Package_4_1_NationBook.Select_Book.ReadScript.SelectType.NewHoonjangFragment;
import edu.skku.woongjin_ai_winter.Package_3_Main.MainActivity;
import edu.skku.woongjin_ai_winter.Package_4_2_MainQuizType.Correct_Wrong_Quiz.CorrectFriendQuizFragment;
import edu.skku.woongjin_ai_winter.Package_4_2_MainQuizType.Correct_Wrong_Quiz.WrongFriendQuizFragment;
import edu.skku.woongjin_ai_winter.Package_4_2_MainQuizType.QuizType.FriendChoiceQuizFragment;
import edu.skku.woongjin_ai_winter.Package_4_2_MainQuizType.QuizType.FriendOXQuizFragment;
import edu.skku.woongjin_ai_winter.Package_4_2_MainQuizType.QuizType.FriendShortwordQuizFragment;
import edu.skku.woongjin_ai_winter.Package_4_4_MyPage.MyPageActivity;
import edu.skku.woongjin_ai_winter.Package_4_4_MyPage.LikeQuiz.LikeQuizListAdapter;
import edu.skku.woongjin_ai_winter.Package_4_1_NationBook.Select_Book.ReadScript.SelectType.QuizType.QuizChoiceTypeInfo;
import edu.skku.woongjin_ai_winter.Package_4_1_NationBook.Select_Book.ReadScript.SelectType.QuizType.QuizOXShortwordTypeInfo;
import edu.skku.woongjin_ai_winter.Package_4_5_ShowFriend.ShowFriendActivity;
import edu.skku.woongjin_ai_winter.Package_4_5_ShowFriend.ShowFriendListAdapter;
import edu.skku.woongjin_ai_winter.Package_4_6_Chat.ChatReadyActivity;
import edu.skku.woongjin_ai_winter.R;
import edu.skku.woongjin_ai_winter.UserInfo;

/*
from MainQuizTypeFragment
친구 질문 풀기 - 내 친구 문제 풀기(default) / 추천수 높은 문제 풀기
 */

//4-4 MyPage Quiz LikeQuiz하고 관련있음

public class ShowFriendQuizActivity extends AppCompatActivity
        implements FriendOXQuizFragment.OnFragmentInteractionListener, FriendChoiceQuizFragment.OnFragmentInteractionListener, FriendShortwordQuizFragment.OnFragmentInteractionListener, ShowScriptFragment.OnFragmentInteractionListener, ShowHintFragment.OnFragmentInteractionListener, CorrectFriendQuizFragment.OnFragmentInteractionListener, WrongFriendQuizFragment.OnFragmentInteractionListener {

    public Intent intent, intentHome, intentUpdate, intentMyPage;
    String id, background, nickname;
    DatabaseReference mPostReference;
    ListView quizListView;
    ArrayList<String> likeQuizList, myFriendList, solvedQuizList, studiedScriptList;
    ArrayList<QuizOXShortwordTypeInfo> myFriendOXQuizList, myFriendShortQuizList, myFriendOXQuizListR, myFriendShortQuizListR;
    ArrayList<QuizOXShortwordTypeInfo> likeOXQuizList, likeShortQuizList, likeOXQuizListR, likeShortQuizListR;
    ArrayList<QuizChoiceTypeInfo> myFriendChoiceQuizList, myFriendChoiceQuizListR;
    ArrayList<QuizChoiceTypeInfo> likeChoiceQuizList, likeChoiceQuizListR;
    MyFriendQuizListAdapter myFriendQuizListAdapter;
    LikeQuizListAdapter likeQuizListAdapter;
    public FriendOXQuizFragment friendOXQuizFragment;
    public FriendChoiceQuizFragment friendChoiceQuizFragment;
    public FriendShortwordQuizFragment friendShortwordQuizFragment;
    public ShowScriptFragment showScriptFragment;
    public ShowHintFragment showHintFragment;
    public CorrectFriendQuizFragment correctFriendQuizFragment;
    public WrongFriendQuizFragment wrongFriendQuizFragment;
    TextView textView;
    int cntOX, cntChoice, cntShort, cntOXL, cntChoiceL, cntShortL, flag = 0;
    UserInfo me;
    Button friendQuizButton, likeQuizButton;
    boolean isFriendQuiz = true;
    public NewHoonjangFragment hoonjangFragment;
    SharedPreferences setting;
    SharedPreferences.Editor editor;
    String nomore;
    FirebaseStorage storage;
    private StorageReference storageReference, dataReference;



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
        setContentView(R.layout.activity_showfriendquiz);

        intent = getIntent();
        id = intent.getStringExtra("id");
        background = intent.getStringExtra("background");
        nickname = intent.getStringExtra("nickname");

        mPostReference = FirebaseDatabase.getInstance().getReference();

        friendOXQuizFragment = new FriendOXQuizFragment();
        friendChoiceQuizFragment = new FriendChoiceQuizFragment();
        friendShortwordQuizFragment = new FriendShortwordQuizFragment();
        showScriptFragment = new ShowScriptFragment();
        showHintFragment = new ShowHintFragment();
        correctFriendQuizFragment = new CorrectFriendQuizFragment();
        wrongFriendQuizFragment = new WrongFriendQuizFragment();

        quizListView = (ListView) findViewById(R.id.quizList);
        textView = (TextView) findViewById(R.id.textShowFriendQuiz);
        ImageButton homeButton = (ImageButton) findViewById(R.id.home);
        ImageButton myPageButton = (ImageButton) findViewById(R.id.myPage);
        friendQuizButton = (Button) findViewById(R.id.friendQuiz);
        likeQuizButton = (Button) findViewById(R.id.likeQuiz);

        menu_drawer = (Button) findViewById(R.id.menu_drawer);

        likeQuizList = new ArrayList<String>();
        myFriendList = new ArrayList<String>();
        solvedQuizList = new ArrayList<String>();
        studiedScriptList = new ArrayList<String>();
        myFriendOXQuizList = new ArrayList<QuizOXShortwordTypeInfo>();
        myFriendOXQuizListR = new ArrayList<QuizOXShortwordTypeInfo>();
        myFriendShortQuizList = new ArrayList<QuizOXShortwordTypeInfo>();
        myFriendShortQuizListR = new ArrayList<QuizOXShortwordTypeInfo>();
        myFriendChoiceQuizList = new ArrayList<QuizChoiceTypeInfo>();
        myFriendChoiceQuizListR = new ArrayList<QuizChoiceTypeInfo>();
        likeOXQuizList = new ArrayList<QuizOXShortwordTypeInfo>();
        likeOXQuizListR = new ArrayList<QuizOXShortwordTypeInfo>();
        likeChoiceQuizList = new ArrayList<QuizChoiceTypeInfo>();
        likeChoiceQuizListR = new ArrayList<QuizChoiceTypeInfo>();
        likeShortQuizList = new ArrayList<QuizOXShortwordTypeInfo>();
        likeShortQuizListR = new ArrayList<QuizOXShortwordTypeInfo>();

        myFriendQuizListAdapter = new MyFriendQuizListAdapter();
        likeQuizListAdapter = new LikeQuizListAdapter();

        setting = getSharedPreferences("nomore", MODE_PRIVATE);
        nomore = setting.getString("showfriendquiz", "keepgoing");

        getFirebaseDatabaseUserInfo();
        getFirebaseDatabaseMyFriendQuiz();
        getFirebaseDatabaseLikeQuiz();
        getFirebaseDatabaseHoonjangInfo();

        intentUpdate = new Intent(ShowFriendQuizActivity.this, ShowFriendQuizActivity.class);
        intentUpdate.putExtra("id", id);
        intentUpdate.putExtra("nickname", nickname);
        intentUpdate.putExtra("background", background);

        menu_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (!drawer.isDrawerOpen(Gravity.LEFT)) drawer.openDrawer(Gravity.LEFT);
            }
        });

        // 내 친구 문제 풀기 버튼 이벤트
        friendQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFriendQuiz) {
                    isFriendQuiz = true;
                    quizListView.setAdapter(myFriendQuizListAdapter);
                    textView.setText(me.nickname + "의 친구가 낸 문제야!");

                    if(flag == 1) {
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.remove(friendOXQuizFragment);
                        fragmentTransaction.commit();
                        friendOXQuizFragment = new FriendOXQuizFragment();
                    } else if(flag == 2) {
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.remove(friendChoiceQuizFragment);
                        fragmentTransaction.commit();
                        friendChoiceQuizFragment = new FriendChoiceQuizFragment();
                    } else if(flag == 3) {
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.remove(friendShortwordQuizFragment);
                        fragmentTransaction.commit();
                        friendShortwordQuizFragment = new FriendShortwordQuizFragment();
                    }
                    flag = 0;
                }
            }
        });

        // 추천수 높은 문제 풀기 버튼 이벤트
        likeQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFriendQuiz) {
                    isFriendQuiz = false;
                    quizListView.setAdapter(likeQuizListAdapter);
                    textView.setText("추천수가 높은 문제야!");

                    if(flag == 1) {
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.remove(friendOXQuizFragment);
                        fragmentTransaction.commit();
                        friendOXQuizFragment = new FriendOXQuizFragment();
                    } else if(flag == 2) {
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.remove(friendChoiceQuizFragment);
                        fragmentTransaction.commit();
                        friendChoiceQuizFragment = new FriendChoiceQuizFragment();
                    } else if(flag == 3) {
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.remove(friendShortwordQuizFragment);
                        fragmentTransaction.commit();
                        friendShortwordQuizFragment = new FriendShortwordQuizFragment();
                    }
                    flag = 0;
                }
            }
        });

        // 메인페이지 버튼 이벤트
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentHome = new Intent(ShowFriendQuizActivity.this, MainActivity.class);
                intentHome.putExtra("id", id);
                intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentHome);
            }
        });

        // 마이페이지 버튼 이벤트
        myPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentMyPage = new Intent(ShowFriendQuizActivity.this, MyPageActivity.class);
                intentMyPage.putExtra("id", id);
                startActivity(intentMyPage);
            }
        });

        // 문제 리스트뷰 클릭 이벤트
        quizListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long i) {
                if(flag == 1) {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.remove(friendOXQuizFragment);
                    fragmentTransaction.commit();
                    friendOXQuizFragment = new FriendOXQuizFragment();
                } else if(flag == 2) {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.remove(friendChoiceQuizFragment);
                    fragmentTransaction.commit();
                    friendChoiceQuizFragment = new FriendChoiceQuizFragment();
                } else if(flag == 3) {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.remove(friendShortwordQuizFragment);
                    fragmentTransaction.commit();
                    friendShortwordQuizFragment = new FriendShortwordQuizFragment();
                }

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                int cnt1, cnt2;
                if(isFriendQuiz) {
                    cnt1 = cntOX;
                    cnt2 = cntChoice;
                } else {
                    cnt1 = cntOXL;
                    cnt2 = cntChoiceL;
                }
                if(position < cnt1) { // OX 문제
                    flag = 1;
                    QuizOXShortwordTypeInfo quiz;
                    if(isFriendQuiz) quiz = myFriendOXQuizListR.get(position);
                    else quiz = likeOXQuizListR.get(position);

                    transaction.replace(R.id.contentShowFriendQuiz, friendOXQuizFragment);
                    Bundle bundle = new Bundle(12);
                    bundle.putString("id", id);
                    bundle.putString("question", quiz.question);
                    bundle.putString("answer", quiz.answer);
                    bundle.putString("uid", quiz.uid);
                    bundle.putString("star", quiz.star);
                    bundle.putString("like", quiz.like);
                    bundle.putString("desc", quiz.desc);
                    bundle.putString("key", quiz.key);
                    bundle.putInt("cnt", quiz.cnt);
                    bundle.putString("scriptnm", quiz.scriptnm);
                    bundle.putString("nickname", nickname);
                    if(isFriendQuiz) bundle.putString("background", "blue");
                    else bundle.putString("background", "red");
                    friendOXQuizFragment.setArguments(bundle);
                    transaction.commit();
                } else {
                    position -= cnt1;
                    if(position < cnt2) { // 객관식 문제
                        flag = 2;
                        QuizChoiceTypeInfo quiz;
                        if(isFriendQuiz) quiz = myFriendChoiceQuizListR.get(position);
                        else quiz = likeChoiceQuizListR.get(position);

                        transaction.replace(R.id.contentShowFriendQuiz, friendChoiceQuizFragment);
                        Bundle bundle = new Bundle(16);
                        bundle.putString("id", id);
                        bundle.putString("question", quiz.question);
                        bundle.putString("answer", quiz.answer);
                        bundle.putString("answer1", quiz.answer1);
                        bundle.putString("answer2", quiz.answer2);
                        bundle.putString("answer3", quiz.answer3);
                        bundle.putString("answer4", quiz.answer4);
                        bundle.putString("uid", quiz.uid);
                        bundle.putString("star", quiz.star);
                        bundle.putString("like", quiz.like);
                        bundle.putString("desc", quiz.desc);
                        bundle.putString("key", quiz.key);
                        bundle.putInt("cnt", quiz.cnt);
                        bundle.putString("scriptnm", quiz.scriptnm);
                        bundle.putString("nickname", nickname);
                        if(isFriendQuiz) bundle.putString("background", "blue");
                        else bundle.putString("background", "red");
                        friendChoiceQuizFragment.setArguments(bundle);
                        transaction.commit();
                    } else { // 주관식 문제
                        position -= cnt2;
                        flag = 3;
                        QuizOXShortwordTypeInfo quiz;
                        if(isFriendQuiz) quiz = myFriendShortQuizListR.get(position);
                        else quiz = likeShortQuizListR.get(position);

                        transaction.replace(R.id.contentShowFriendQuiz, friendShortwordQuizFragment);
                        Bundle bundle = new Bundle(12);
                        bundle.putString("id", id);
                        bundle.putString("question", quiz.question);
                        bundle.putString("answer", quiz.answer);
                        bundle.putString("uid", quiz.uid);
                        bundle.putString("star", quiz.star);
                        bundle.putString("like", quiz.like);
                        bundle.putString("desc", quiz.desc);
                        bundle.putString("key", quiz.key);
                        bundle.putInt("cnt", quiz.cnt);
                        bundle.putString("scriptnm", quiz.scriptnm);
                        bundle.putString("nickname", nickname);

                        if(isFriendQuiz) bundle.putString("background", "blue");
                        else bundle.putString("background", "red");
                        friendShortwordQuizFragment.setArguments(bundle);
                        transaction.commit();
                    }
                }
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

        //m_oShowFriendQuizActivity = this;
//////
    }

    // 데이터베이스에서 내 친구 문제 가져오기
    // 아직 풀지 않은 내 친구의 문제들 중 각 유형당 랜덤으로 5개 이하 보여준다.
    private void getFirebaseDatabaseMyFriendQuiz() {
        mPostReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myFriendList.clear();
                myFriendOXQuizList.clear();
                myFriendChoiceQuizList.clear();
                myFriendShortQuizList.clear();
                myFriendOXQuizListR.clear();
                myFriendChoiceQuizListR.clear();
                myFriendShortQuizListR.clear();

                for(DataSnapshot snapshot : dataSnapshot.child("user_list/" + id + "/my_friend_list").getChildren()) {
                    String key = snapshot.getKey();
                    myFriendList.add(key);
                }

                for(DataSnapshot snapshot : dataSnapshot.child("quiz_list/").getChildren()) {
                    String script = snapshot.getKey();
                    boolean isStuied = false;
                    for(String studied : studiedScriptList) {
                        if(script.equals(studied)) {
                            isStuied = true;
                            break;
                        }
                    }

                    if(isStuied) {
                        for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                            String type = snapshot1.child("type").getValue().toString();
                            String uid = snapshot1.child("uid").getValue().toString();
                            for(String friend : myFriendList) {
                                if(friend.equals(uid)) {
                                    boolean isSolved = false;
                                    for(String solved : solvedQuizList) {
                                        String key = snapshot1.getKey();
                                        if(solved.equals(key)) {
                                            isSolved = true;
                                            break;
                                        }
                                    }
                                    if(!isSolved) {
                                        if(type.equals("1")) {
                                            QuizOXShortwordTypeInfo quiz = snapshot1.getValue(QuizOXShortwordTypeInfo.class);
                                            myFriendOXQuizList.add(quiz);
                                        } else if(type.equals("2")) {
                                            QuizChoiceTypeInfo quiz = snapshot1.getValue(QuizChoiceTypeInfo.class);
                                            myFriendChoiceQuizList.add(quiz);
                                        } else if(type.equals("3")) {
                                            QuizOXShortwordTypeInfo quiz = snapshot1.getValue(QuizOXShortwordTypeInfo.class);
                                            myFriendShortQuizList.add(quiz);
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }

                Random generator = new Random();
                cntOX = myFriendOXQuizList.size();
                cntChoice = myFriendChoiceQuizList.size();
                cntShort = myFriendShortQuizList.size();
                int[] randList = new int[5];

                for(int i=0; i<5; i++) randList[i] = -1;
                int cnt = 5;
                if(cnt > cntOX) cnt = cntOX;
                for(int i=0; i<cnt; i++) {
                    randList[i] = generator.nextInt(cntOX);
                    for(int j = 0; j < i; j++) {
                        if(randList[i] == randList[j]) {
                            i--;
                            break;
                        }
                    }
                }
                for(int i : randList) {
                    if(i == -1) break;
                    float star = Float.parseFloat(myFriendOXQuizList.get(i).star);
                    UserInfo userInfo = dataSnapshot.child("user_list/" + myFriendOXQuizList.get(i).uid).getValue(UserInfo.class);
                    String user = userInfo.nickname + "\n" + userInfo.grade + "학년\n" + userInfo.school;

                    if(star < 1.5)
                        myFriendQuizListAdapter.addItem(userInfo.profile, user, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), myFriendOXQuizList.get(i).book_name, myFriendOXQuizList.get(i).scriptnm, myFriendOXQuizList.get(i).question);
                    else if (star >= 1.5 && star < 2.5)
                        myFriendQuizListAdapter.addItem(userInfo.profile, user, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), myFriendOXQuizList.get(i).book_name, myFriendOXQuizList.get(i).scriptnm, myFriendOXQuizList.get(i).question);
                    else if (star >= 2.5 && star < 3.5)
                        myFriendQuizListAdapter.addItem(userInfo.profile, user, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), myFriendOXQuizList.get(i).book_name, myFriendOXQuizList.get(i).scriptnm, myFriendOXQuizList.get(i).question);
                    else if (star >= 3.5 && star < 4.5)
                        myFriendQuizListAdapter.addItem(userInfo.profile, user, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), myFriendOXQuizList.get(i).book_name, myFriendOXQuizList.get(i).scriptnm, myFriendOXQuizList.get(i).question);
                    else
                        myFriendQuizListAdapter.addItem(userInfo.profile, user, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), myFriendOXQuizList.get(i).book_name, myFriendOXQuizList.get(i).scriptnm, myFriendOXQuizList.get(i).question);

                    myFriendOXQuizListR.add(myFriendOXQuizList.get(i));
                }

                for(int i=0; i<5; i++) randList[i] = -1;
                cnt = 5;
                if(cnt > cntChoice) cnt = cntChoice;
                for(int i=0; i<cnt; i++) {
                    randList[i] = generator.nextInt(cntChoice);
                    for(int j = 0; j < i; j++) {
                        if(randList[i] == randList[j]) {
                            i--;
                            break;
                        }
                    }
                }
                for(int i : randList) {
                    if(i == -1) break;
                    float star = Float.parseFloat(myFriendChoiceQuizList.get(i).star);
                    UserInfo userInfo = dataSnapshot.child("user_list/" + myFriendChoiceQuizList.get(i).uid).getValue(UserInfo.class);
                    String user = userInfo.nickname + "\n" + userInfo.grade + "학년\n" + userInfo.school;

                    if(star < 1.5)
                        myFriendQuizListAdapter.addItem(userInfo.profile, user, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), myFriendChoiceQuizList.get(i).book_name, myFriendChoiceQuizList.get(i).scriptnm, myFriendChoiceQuizList.get(i).question);
                    else if (star >= 1.5 && star < 2.5)
                        myFriendQuizListAdapter.addItem(userInfo.profile, user, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), myFriendChoiceQuizList.get(i).book_name, myFriendChoiceQuizList.get(i).scriptnm, myFriendChoiceQuizList.get(i).question);
                    else if (star >= 2.5 && star < 3.5)
                        myFriendQuizListAdapter.addItem(userInfo.profile, user, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), myFriendChoiceQuizList.get(i).book_name, myFriendChoiceQuizList.get(i).scriptnm, myFriendChoiceQuizList.get(i).question);
                    else if (star >= 3.5 && star < 4.5)
                        myFriendQuizListAdapter.addItem(userInfo.profile, user, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), myFriendChoiceQuizList.get(i).book_name, myFriendChoiceQuizList.get(i).scriptnm, myFriendChoiceQuizList.get(i).question);
                    else
                        myFriendQuizListAdapter.addItem(userInfo.profile, user, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), myFriendChoiceQuizList.get(i).book_name, myFriendChoiceQuizList.get(i).scriptnm, myFriendChoiceQuizList.get(i).question);

                    myFriendChoiceQuizListR.add(myFriendChoiceQuizList.get(i));
                }

                for(int i=0; i<5; i++) randList[i] = -1;
                cnt = 5;
                if(cnt > cntShort) cnt = cntShort;
                for(int i=0; i<cnt; i++) {
                    randList[i] = generator.nextInt(cntShort);
                    for(int j = 0; j < i; j++) {
                        if(randList[i] == randList[j]) {
                            i--;
                            break;
                        }
                    }
                }
                for(int i : randList) {
                    if(i == -1) break;
                    float star = Float.parseFloat(myFriendShortQuizList.get(i).star);
                    UserInfo userInfo = dataSnapshot.child("user_list/" + myFriendShortQuizList.get(i).uid).getValue(UserInfo.class);
                    String user = userInfo.nickname + "\n" + userInfo.grade + "학년\n" + userInfo.school;

                    if(star < 1.5)
                        myFriendQuizListAdapter.addItem(userInfo.profile, user, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), myFriendShortQuizList.get(i).book_name, myFriendShortQuizList.get(i).scriptnm, myFriendShortQuizList.get(i).question);
                    else if (star >= 1.5 && star < 2.5)
                        myFriendQuizListAdapter.addItem(userInfo.profile, user, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), myFriendShortQuizList.get(i).book_name, myFriendShortQuizList.get(i).scriptnm, myFriendShortQuizList.get(i).question);
                    else if (star >= 2.5 && star < 3.5)
                        myFriendQuizListAdapter.addItem(userInfo.profile, user, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), myFriendShortQuizList.get(i).book_name, myFriendShortQuizList.get(i).scriptnm, myFriendShortQuizList.get(i).question);
                    else if (star >= 3.5 && star < 4.5)
                        myFriendQuizListAdapter.addItem(userInfo.profile, user, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), myFriendShortQuizList.get(i).book_name, myFriendShortQuizList.get(i).scriptnm, myFriendShortQuizList.get(i).question);
                    else
                        myFriendQuizListAdapter.addItem(userInfo.profile, user, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), myFriendShortQuizList.get(i).book_name, myFriendShortQuizList.get(i).scriptnm, myFriendShortQuizList.get(i).question);

                    myFriendShortQuizListR.add(myFriendShortQuizList.get(i));
                }

                cntOX = myFriendOXQuizListR.size();
                cntChoice = myFriendChoiceQuizListR.size();
                cntShort = myFriendShortQuizListR.size();

                quizListView.setAdapter(myFriendQuizListAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        });
    }

    // 데이터베이스에서 추천수 높은 문제 가져오기
    // 각 유형별로 추천수 제일 높은 문제 3개 가져와서 그 중 랜덤으로 2개 보여주기
    private void getFirebaseDatabaseLikeQuiz() {
        mPostReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                likeOXQuizList.clear();
                likeOXQuizListR.clear();
                likeChoiceQuizList.clear();
                likeChoiceQuizListR.clear();
                likeShortQuizList.clear();
                likeShortQuizListR.clear();
                for(DataSnapshot snapshot : dataSnapshot.child("quiz_list").getChildren()) {
                    String script = snapshot.getKey();
                    boolean isStuied = false;
                    for(String studied : studiedScriptList) {
                        if(script.equals(studied)) {
                            isStuied = true;
                            break;
                        }
                    }

                    if(isStuied) {
                        for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                            boolean isSolved = false;
                            for(String solved : solvedQuizList) {
                                String key = snapshot1.getKey();
                                if(solved.equals(key)) {
                                    isSolved = true;
                                    break;
                                }
                            }
                            if(!isSolved) {
                                String type = snapshot1.child("type").getValue().toString();
                                if (type.equals("1")) {
                                    QuizOXShortwordTypeInfo quiz = snapshot1.getValue(QuizOXShortwordTypeInfo.class);
                                    if(!quiz.uid.equals(id)) likeOXQuizList.add(quiz);
                                } else if (type.equals("2")) {
                                    QuizChoiceTypeInfo quiz = snapshot1.getValue(QuizChoiceTypeInfo.class);
                                    if(!quiz.uid.equals(id)) likeChoiceQuizList.add(quiz);
                                } else if (type.equals("3")) {
                                    QuizOXShortwordTypeInfo quiz = snapshot1.getValue(QuizOXShortwordTypeInfo.class);
                                    if(!quiz.uid.equals(id)) likeShortQuizList.add(quiz);
                                }
                            }
                        }
                    }
                }

                int j;
                for(int i=1; i<likeOXQuizList.size(); i++) {
                    QuizOXShortwordTypeInfo tmp = likeOXQuizList.get(i);
                    for(j=i-1; j>=0; j--) {
                        if(Integer.parseInt(tmp.like) < Integer.parseInt(likeOXQuizList.get(j).like))
                            likeOXQuizList.set(j+1, likeOXQuizList.get(j));
                        else break;
                    }
                    likeOXQuizList.set(j+1, tmp);
                }
                for(int i=1; i<likeChoiceQuizList.size(); i++) {
                    QuizChoiceTypeInfo tmp = likeChoiceQuizList.get(i);
                    for(j=i-1; j>=0; j--) {
                        if(Integer.parseInt(tmp.like) < Integer.parseInt(likeChoiceQuizList.get(j).like))
                            likeChoiceQuizList.set(j+1, likeChoiceQuizList.get(j));
                        else break;
                    }
                    likeChoiceQuizList.set(j+1, tmp);
                }
                for(int i=1; i<likeShortQuizList.size(); i++) {
                    QuizOXShortwordTypeInfo tmp = likeShortQuizList.get(i);
                    for(j=i-1; j>=0; j--) {
                        if(Integer.parseInt(tmp.like) < Integer.parseInt(likeShortQuizList.get(j).like))
                            likeShortQuizList.set(j+1, likeShortQuizList.get(j));
                        else break;
                    }
                    likeShortQuizList.set(j+1, tmp);
                }

                while(likeOXQuizList.size() > 3) likeOXQuizList.remove(0);
                while(likeChoiceQuizList.size() > 3) likeChoiceQuizList.remove(0);
                while(likeShortQuizList.size() > 3) likeShortQuizList.remove(0);


                cntOXL = likeOXQuizList.size();
                cntChoiceL = likeChoiceQuizList.size();
                cntShortL = likeShortQuizList.size();

                Random generator = new Random();
                int[] randList = new int[2];

                for(int i=0; i<2; i++) randList[i] = -1;
                int cnt = 2;
                if(cnt > cntOXL) cnt = cntOXL;
                for(int i=0; i<cnt; i++) {
                    randList[i] = generator.nextInt(cntOXL);
                    for(j = 0; j < i; j++) {
                        if(randList[i] == randList[j]) {
                            i--;
                            break;
                        }
                    }
                }
                for(int i : randList) {
                    if(i == -1) break;
                    float star = Float.parseFloat(likeOXQuizList.get(i).star);
                    if(star < 1.5)
                        likeQuizListAdapter.addItem(likeOXQuizList.get(i).like, likeOXQuizList.get(i).uid, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), likeOXQuizList.get(i).book_name, likeOXQuizList.get(i).scriptnm, likeOXQuizList.get(i).question);
                    else if (star >= 1.5 && star < 2.5)
                        likeQuizListAdapter.addItem(likeOXQuizList.get(i).like, likeOXQuizList.get(i).uid, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), likeOXQuizList.get(i).book_name, likeOXQuizList.get(i).scriptnm, likeOXQuizList.get(i).question);
                    else if (star >= 2.5 && star < 3.5)
                        likeQuizListAdapter.addItem(likeOXQuizList.get(i).like, likeOXQuizList.get(i).uid, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), likeOXQuizList.get(i).book_name, likeOXQuizList.get(i).scriptnm, likeOXQuizList.get(i).question);
                    else if (star >= 3.5 && star < 4.5)
                        likeQuizListAdapter.addItem(likeOXQuizList.get(i).like, likeOXQuizList.get(i).uid, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), likeOXQuizList.get(i).book_name, likeOXQuizList.get(i).scriptnm, likeOXQuizList.get(i).question);
                    else
                        likeQuizListAdapter.addItem(likeOXQuizList.get(i).like, likeOXQuizList.get(i).uid, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), likeOXQuizList.get(i).book_name, likeOXQuizList.get(i).scriptnm, likeOXQuizList.get(i).question);

                    likeOXQuizListR.add(likeOXQuizList.get(i));
                }

                for(int i=0; i<2; i++) randList[i] = -1;
                cnt = 2;
                if(cnt > cntChoiceL) cnt = cntChoiceL;
                for(int i=0; i<cnt; i++) {
                    randList[i] = generator.nextInt(cntChoiceL);
                    for(j = 0; j < i; j++) {
                        if(randList[i] == randList[j]) {
                            i--;
                            break;
                        }
                    }
                }
                for(int i : randList) {
                    if(i == -1) break;
                    float star = Float.parseFloat(likeChoiceQuizList.get(i).star);
                    if(star < 1.5)
                        likeQuizListAdapter.addItem(likeChoiceQuizList.get(i).like, likeChoiceQuizList.get(i).uid, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), likeChoiceQuizList.get(i).book_name, likeChoiceQuizList.get(i).scriptnm, likeChoiceQuizList.get(i).question);
                    else if (star >= 1.5 && star < 2.5)
                        likeQuizListAdapter.addItem(likeChoiceQuizList.get(i).like, likeChoiceQuizList.get(i).uid, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty),likeChoiceQuizList.get(i).book_name, likeChoiceQuizList.get(i).scriptnm, likeChoiceQuizList.get(i).question);
                    else if (star >= 2.5 && star < 3.5)
                        likeQuizListAdapter.addItem(likeChoiceQuizList.get(i).like, likeChoiceQuizList.get(i).uid, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), likeChoiceQuizList.get(i).book_name, likeChoiceQuizList.get(i).scriptnm, likeChoiceQuizList.get(i).question);
                    else if (star >= 3.5 && star < 4.5)
                        likeQuizListAdapter.addItem(likeChoiceQuizList.get(i).like, likeChoiceQuizList.get(i).uid, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), likeChoiceQuizList.get(i).book_name, likeChoiceQuizList.get(i).scriptnm, likeChoiceQuizList.get(i).question);
                    else
                        likeQuizListAdapter.addItem(likeChoiceQuizList.get(i).like, likeChoiceQuizList.get(i).uid, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), likeChoiceQuizList.get(i).book_name, likeChoiceQuizList.get(i).scriptnm, likeChoiceQuizList.get(i).question);

                    likeChoiceQuizListR.add(likeChoiceQuizList.get(i));
                }

                for(int i=0; i<2; i++) randList[i] = -1;
                cnt = 2;
                if(cnt > cntShortL) cnt = cntShortL;
                for(int i=0; i<cnt; i++) {
                    randList[i] = generator.nextInt(cntShortL);
                    for(j = 0; j < i; j++) {
                        if(randList[i] == randList[j]) {
                            i--;
                            break;
                        }
                    }
                }
                for(int i : randList) {
                    if(i == -1) break;
                    float star = Float.parseFloat(likeShortQuizList.get(i).star);
                    if(star < 1.5)
                        likeQuizListAdapter.addItem(likeShortQuizList.get(i).like, likeShortQuizList.get(i).uid, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), likeShortQuizList.get(i).book_name, likeShortQuizList.get(i).scriptnm, likeShortQuizList.get(i).question);
                    else if (star >= 1.5 && star < 2.5)
                        likeQuizListAdapter.addItem(likeShortQuizList.get(i).like, likeShortQuizList.get(i).uid, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), likeShortQuizList.get(i).book_name, likeShortQuizList.get(i).scriptnm, likeShortQuizList.get(i).question);
                    else if (star >= 2.5 && star < 3.5)
                        likeQuizListAdapter.addItem(likeShortQuizList.get(i).like, likeShortQuizList.get(i).uid, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), likeShortQuizList.get(i).book_name, likeShortQuizList.get(i).scriptnm, likeShortQuizList.get(i).question);
                    else if (star >= 3.5 && star < 4.5)
                        likeQuizListAdapter.addItem(likeShortQuizList.get(i).like, likeShortQuizList.get(i).uid, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), likeShortQuizList.get(i).book_name, likeShortQuizList.get(i).scriptnm, likeShortQuizList.get(i).question);
                    else
                        likeQuizListAdapter.addItem(likeShortQuizList.get(i).like, likeShortQuizList.get(i).uid, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), likeShortQuizList.get(i).book_name, likeShortQuizList.get(i).scriptnm, likeShortQuizList.get(i).question);

                    likeShortQuizListR.add(likeShortQuizList.get(i));
                }

                cntOXL = likeOXQuizListR.size();
                cntChoiceL = likeChoiceQuizListR.size();
                cntShortL = likeShortQuizListR.size();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        });
    }

    // 훈장 수여 자격 확인 및 수여
    private void getFirebaseDatabaseHoonjangInfo() {
        mPostReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int SolvedCount=0;
                DataSnapshot dataSnapshot1=dataSnapshot.child("user_list/"+id+"my_week_list");
                for(DataSnapshot dataSnapshot2: dataSnapshot1.getChildren()){ //week 껍데기
                    SolvedCount+=Integer.parseInt(dataSnapshot2.child("correct").getValue().toString());
                }
                Calendar calendar = Calendar.getInstance();
                Date dateS = calendar.getTime();
                String MedalUpdate = new SimpleDateFormat("yyyy-MM-dd").format(dateS);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                hoonjangFragment=new NewHoonjangFragment();
                if(SolvedCount==150 && nomore.equals("stop3")) {
                    uploadFirebaseUserCoinInfo_H("문제사냥꾼", 3);
                    mPostReference.child("user_list/" + id + "/my_medal_list/문제사냥꾼").setValue("Lev3##"+MedalUpdate);
                    SharedPreferences sf = getSharedPreferences("nomore", MODE_PRIVATE);
                    editor=sf.edit();
                    editor.putString("showfriendquiz", "stop3");
                    editor.commit();
                    transaction.replace(R.id.friendquizFrame, hoonjangFragment);
                    Bundle bundle = new Bundle(3);
                    bundle.putString("what", "quizhunter");
                    bundle.putString("from", "showfriendquiz");
                    bundle.putInt("level", 3);
                    hoonjangFragment.setArguments(bundle);
                    transaction.commit();
                }else if(SolvedCount==100 && nomore.equals("stop2")){
                    uploadFirebaseUserCoinInfo_H("문제사냥꾼", 2);
                    mPostReference.child("user_list/" + id + "/my_medal_list/문제사냥꾼").setValue("Lev2##"+MedalUpdate);
                    SharedPreferences sf = getSharedPreferences("nomore", MODE_PRIVATE);
                    editor=sf.edit();
                    editor.putString("showfriendquiz", "stop2");
                    editor.commit();
                    transaction.replace(R.id.friendquizFrame, hoonjangFragment);
                    Bundle bundle = new Bundle(3);
                    bundle.putString("what", "quizhunter");
                    bundle.putString("from", "showfriendquiz");
                    bundle.putInt("level", 2);
                    hoonjangFragment.setArguments(bundle);
                    transaction.commit();
                }else if(SolvedCount==50 && nomore.equals("stop1")){
                    uploadFirebaseUserCoinInfo_H("문제사냥꾼", 1);
                    mPostReference.child("user_list/" + id + "/my_medal_list/문제사냥꾼").setValue("Lev1##"+MedalUpdate);
                    SharedPreferences sf = getSharedPreferences("nomore", MODE_PRIVATE);
                    editor=sf.edit();
                    editor.putString("showfriendquiz", "stop1");
                    editor.commit();
                    transaction.replace(R.id.friendquizFrame, hoonjangFragment);
                    Bundle bundle = new Bundle(3);
                    bundle.putString("what", "quizhunter");
                    bundle.putString("from", "showfriendquiz");
                    bundle.putInt("level", 1);
                    hoonjangFragment.setArguments(bundle);
                    transaction.commit();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        });
    }

    // 코인 증정, 데이터베이스에 저장
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

    // 데이터베이스에서 유저 정보 가져오기
    private void getFirebaseDatabaseUserInfo() {
        mPostReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                solvedQuizList.clear();
                studiedScriptList.clear();

                me = dataSnapshot.child("user_list/" + id).getValue(UserInfo.class);
                textView.setText(me.nickname + "의 친구가 낸 문제야!");

                for(DataSnapshot snapshot : dataSnapshot.child("user_list/" + id + "/my_script_list").getChildren()) {
                    String key1 = snapshot.getKey();
                    studiedScriptList.add(key1);
                    for(DataSnapshot snapshot1 : snapshot.child("solved_list").getChildren()) {
                        String key = snapshot1.getKey();
                        solvedQuizList.add(key);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        });
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
