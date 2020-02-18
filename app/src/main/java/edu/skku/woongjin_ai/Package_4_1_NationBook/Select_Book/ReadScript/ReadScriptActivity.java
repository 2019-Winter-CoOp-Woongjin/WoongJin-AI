package edu.skku.woongjin_ai_winter.Package_4_1_NationBook.Select_Book.ReadScript;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import edu.skku.woongjin_ai_winter.DrawerDialog;
import edu.skku.woongjin_ai_winter.Package_4_1_NationBook.Select_Book.ReadScript.SelectType.NewHoonjangFragment;
import edu.skku.woongjin_ai_winter.Package_3_Main.MainActivity;
import edu.skku.woongjin_ai_winter.Package_4_5_ShowFriend.ShowFriendActivity;
import edu.skku.woongjin_ai_winter.Package_4_5_ShowFriend.ShowFriendListAdapter;
import edu.skku.woongjin_ai_winter.Package_4_6_Chat.ChatReadyActivity;
import edu.skku.woongjin_ai_winter.R;
import edu.skku.woongjin_ai_winter.Package_4_1_NationBook.Select_Book.ReadScript.SelectType.SelectTypeActivity;
import edu.skku.woongjin_ai_winter.UserInfo;

/*
from SelectBookActivity
지문 공부하기 - 소리내어 읽기(미구현) / 표시하며 읽기
 */

public class ReadScriptActivity extends AppCompatActivity
        implements SelectStudyTypeFragment.OnFragmentInteractionListener, NewHoonjangFragment.OnFragmentInteractionListener {
    public DatabaseReference mPostReference;
    Intent intent, intentHome, intentMakeQuiz;
    String id, scriptnm, backgroundID, script, studyType = "", nickname, thisWeek;
    TextView textview_title, textview_script_1, textview_script_2;
    ImageButton goHome;
    TextView goMakeQuiz;
//    FirebaseStorage storage;
//    private StorageReference storageReference, dataReference;
    Fragment selectStudyTypeFragment;
    NewHoonjangFragment hoonjangFragment;


    //////
    Button messaging_btn,adding;
    DatabaseReference mPostReference2;
    ListView friend_list;
    ArrayList<String> myFriendList;
    //ReadScriptActivity m_oReadScriptActivity = null;

    Button menu_draw;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readscript);

        intent = getIntent();
        id= intent.getStringExtra("id");
        scriptnm = intent.getStringExtra("scriptnm");
        backgroundID = intent.getStringExtra("background");
        nickname = intent.getStringExtra("nickname");
        thisWeek = intent.getStringExtra("thisWeek");

        selectStudyTypeFragment = new SelectStudyTypeFragment();

        textview_title = (TextView) findViewById(R.id.textview_title);
        textview_script_1 = (TextView) findViewById(R.id.textview_script_1);
        textview_script_2 = (TextView) findViewById(R.id.textview_script_2);
        goHome = (ImageButton) findViewById(R.id.home);
        goMakeQuiz = (TextView) findViewById(R.id.makeQuiz);

        menu_draw = (Button) findViewById(R.id.menu_drawer);

        textview_title.setText(scriptnm);
        textview_script_1.setMovementMethod(new ScrollingMovementMethod());
        textview_script_2.setMovementMethod(new ScrollingMovementMethod());

        mPostReference = FirebaseDatabase.getInstance().getReference();

        // 데이터베이스 단어장에 임시 단어 저장
        mPostReference.child("user_list/" + id + "/my_script_list/" + scriptnm + "/time").setValue("2m");
        mPostReference.child("user_list/" + id + "/my_script_list/" + scriptnm + "/word_list/test1/ex").setValue("test1Ex");
        mPostReference.child("user_list/" + id + "/my_script_list/" + scriptnm + "/word_list/test1/meaning").setValue("test1Meaning");
        mPostReference.child("user_list/" + id + "/my_script_list/" + scriptnm + "/word_list/test2/ex").setValue("test2Ex");
        mPostReference.child("user_list/" + id + "/my_script_list/" + scriptnm + "/word_list/test2/meaning").setValue("test2Meaning");
        mPostReference.child("user_list/" + id + "/my_script_list/" + scriptnm + "/word_list/test3/ex").setValue("test3Ex");
        mPostReference.child("user_list/" + id + "/my_script_list/" + scriptnm + "/word_list/test3/meaning").setValue("test3Meaning");

        // 배경 이미지
//        storage = FirebaseStorage.getInstance();
//        storageReference = storage.getInstance().getReference();
//        dataReference = storageReference.child("/scripts_background/" + backgroundID);
//        dataReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                Picasso.with(ReadScriptActivity.this)
//                        .load(uri)
//                        .placeholder(R.drawable.bot)
//                        .error(R.drawable.btn_x)
//                        .into(backgroundImage);
//                backgroundImage.setAlpha(0.5f);
//            }
//        });

        // 데이터베이스에서 지문 가져오기
        mPostReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                script = dataSnapshot.child("script_list/" + scriptnm + "/text").getValue().toString();
                String[] array=script.split("###");
                textview_script_1.setText(array[0]);
                textview_script_2.setText(array[1]);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        });

        // 소리내어 읽기 / 표시하며 읽기 선택
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.contentReadScript, selectStudyTypeFragment);
//        transaction.commit();

        //draw
        menu_draw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (!drawer.isDrawerOpen(Gravity.LEFT)) drawer.openDrawer(Gravity.LEFT);
            }
        });

        // 메인페이지 버튼 이벤트
        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentHome = new Intent(ReadScriptActivity.this, MainActivity.class);
                intentHome.putExtra("id", id);
                intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentHome);
            }
        });

        // 질문 만들기 버튼 이벤트
        goMakeQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFirebaseUserCoinInfo();
                intentMakeQuiz = new Intent(ReadScriptActivity.this, SelectTypeActivity.class);
                intentMakeQuiz.putExtra("id", id);
                intentMakeQuiz.putExtra("scriptnm", scriptnm);
                intentMakeQuiz.putExtra("background", backgroundID);
                intentMakeQuiz.putExtra("nickname", nickname);
                intentMakeQuiz.putExtra("thisWeek", thisWeek);
                startActivity(intentMakeQuiz);
            }
        });

        // 지문 단어 공부하기 버튼 이벤트
//        goStudyWord.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            Intent intentStudyWord = new Intent(ReadScriptActivity.this, WordListActivity.class);
//            intentStudyWord.putExtra("scriptnm",scriptnm);
//            intentStudyWord.putExtra("id", id);
//            intentStudyWord.putExtra("background", backgroundID);
//            startActivity(intentStudyWord);
//        }
//    });


//////

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

        //m_oReadScriptActivity = this;

    }

    // 코인 수여, 데이터베이스에 저장
    private void uploadFirebaseUserCoinInfo(){
        mPostReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                String today = new SimpleDateFormat("yyMMddHHmm").format(date);
                mPostReference.child("user_list/" + id + "/my_coin_list/" + today + "/get").setValue("20");
                mPostReference.child("user_list/" + id + "/my_coin_list/" + today + "/why").setValue("지문 [" + scriptnm + "]을(를) 읽었어요.");

                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    String key=dataSnapshot1.getKey();
                    if(key.equals("user_list")){
                        String mycoin=dataSnapshot1.child(id).child("coin").getValue().toString();
                        int coin = Integer.parseInt(mycoin) + 10;
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

    public void onStudyTypeInfoSet(String type) {
        studyType = type;

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

}