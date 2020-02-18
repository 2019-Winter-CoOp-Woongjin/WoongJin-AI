package edu.skku.woongjin_ai.Package_4_3_GameList;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import edu.skku.woongjin_ai.Package_4_3_GameList.NationGame.NationGameActivity;
import edu.skku.woongjin_ai.Package_4_1_NationBook.Select_Book.ReadScript.SelectType.NewHoonjangFragment;
import edu.skku.woongjin_ai.Package_3_Main.MainActivity;
import edu.skku.woongjin_ai.Package_4_3_GameList.MakeBombType.MakeBombTypeActivity;
import edu.skku.woongjin_ai.Package_4_3_GameList.SolveBombType.SolveBombChoiceActivity;
import edu.skku.woongjin_ai.Package_4_3_GameList.SolveBombType.SolveBombOXActivity;
import edu.skku.woongjin_ai.Package_4_3_GameList.SolveBombType.SolveBombShortwordActivity;
import edu.skku.woongjin_ai.Package_4_5_ShowFriend.ShowFriendActivity;
import edu.skku.woongjin_ai.Package_4_5_ShowFriend.ShowFriendListAdapter;
import edu.skku.woongjin_ai.Package_4_6_Chat.ChatReadyActivity;
import edu.skku.woongjin_ai.R;
import edu.skku.woongjin_ai.UserInfo;

public class GameListActivity extends AppCompatActivity
        implements edu.skku.woongjin_ai.Package_4_3_GameList.Fragment_help.OnFragmentInteractionListener, NewHoonjangFragment.OnFragmentInteractionListener {
    private DatabaseReference mPostReference, qaPostReference, nPostReference;
    public DatabaseReference m2PostReference;
    ArrayList<String> data;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<GameRoomInfo> gameRoomInfos=new ArrayList<GameRoomInfo>();

    long Solved;

    String id_key, nickname_key;
    String roomname_key, friend_nickname;
    String timestamp_key, user1_key, user2_key, script_key, state_key;
    char bomb_cnt;
    String last, solve;
    String type_key, question_key, answer_key, ans1_key, ans2_key, ans3_key, ans4_key;
    int find, check_gamelist;
    UserInfo me;

    ListView gamelist;
    Button create;
    ImageView makebomb, solvebomb;
    ImageView imageHome, help;
    TextView userName1, userGrade1, userCoin;

    Intent intent, intent_makebombtype;
    public Fragment Fragment_help;

    public NewHoonjangFragment hoonjangFragment_bombmaster;

    SharedPreferences setting;
    SharedPreferences.Editor editor;
    String nomore;


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
        setContentView(R.layout.activity_gamelist);

        setting = getSharedPreferences("nomore", MODE_PRIVATE);
        nomore=setting.getString("bombmaster", "keepgoing");


        gamelist = findViewById(R.id.gamelist);
        create = findViewById(R.id.create);
        help = findViewById(R.id.helpbtn);
        makebomb = findViewById(R.id.makebomb);
        solvebomb = findViewById(R.id.solvebomb);
        imageHome = (ImageView) findViewById(R.id.home);
        userName1 = (TextView) findViewById(R.id.userName1);
        userGrade1 = (TextView) findViewById(R.id.userGrade1);
        userCoin = (TextView) findViewById(R.id.userCoin);

        menu_drawer = (Button) findViewById(R.id.menu_drawer);


        m2PostReference = FirebaseDatabase.getInstance().getReference();

        data = new ArrayList<String>();
        mPostReference = FirebaseDatabase.getInstance().getReference().child("gameroom_list");
        nPostReference=FirebaseDatabase.getInstance().getReference();
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        find = 0;
        check_gamelist = 0;


        intent = getIntent();
        id_key = intent.getExtras().getString("id");
        nickname_key = intent.getExtras().getString("nickname");


        getFirebaseDatabaseUserInfo();

        menu_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (!drawer.isDrawerOpen(Gravity.LEFT)) drawer.openDrawer(Gravity.LEFT);
            }
        });

        imageHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentHome = new Intent(GameListActivity.this, MainActivity.class);
                intentHome.putExtra("id", id_key);
                intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentHome);
                finish();
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent_nationgame = new Intent(GameListActivity.this, NationGameActivity.class);
                intent_nationgame.putExtra("id", id_key);
                intent_nationgame.putExtra("nickname", nickname_key);
                startActivity(intent_nationgame);
            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment_help = new Fragment_help();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.comment, Fragment_help);
                Bundle bundle = new Bundle(1);
                bundle.putString("gamelist", "gamelist");
                Fragment_help.setArguments(bundle);
                transaction.commit();
            }
        });

        gamelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                check_gamelist = 1;
                GameRoomInfo listItem=gameRoomInfos.get(position);
                roomname_key=listItem.Roomname;
                friend_nickname=listItem.WithWhom;
                //roomname_key = gamelist.getItemAtPosition(position).toString().split("-")[0];
                //friend_nickname = gamelist.getItemAtPosition(position).toString().split("-")[1];
                final ValueEventListener checkRoom = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            String temp_timestamp = postSnapshot.getKey();
                            String temp_roomname = postSnapshot.child("roomname").getValue().toString();
                            String temp_user1 = postSnapshot.child("user1").getValue().toString();
                            String temp_user2 = postSnapshot.child("user2").getValue().toString();
                            String temp_script = postSnapshot.child("script").getValue().toString();
                            String temp_state = postSnapshot.child("state").getValue().toString();
                            if ((temp_roomname.equals(roomname_key) && temp_user1.equals(nickname_key) && temp_user2.equals(friend_nickname))) {
                                if (temp_state.equals("win")) {
                                    //Toast.makeText(GameListActivity.this, "Both win!", Toast.LENGTH_SHORT).show();
                                    find = 0;
                                    break;
                                }
                                else if (temp_state.equals("win1")) {
                                    if (temp_user1.equals(nickname_key)) { //user1이 이겼고, user1이 본인일 때
                                        //Toast.makeText(GameListActivity.this, "You win!", Toast.LENGTH_SHORT).show();
                                        find = 0;
                                        break;
                                    }
                                    else if (!temp_user1.equals(nickname_key)) { //user1이 이겼는데, user1이 본인이 아니라 상대방일 때
                                        //Toast.makeText(GameListActivity.this, "You lose...", Toast.LENGTH_SHORT).show();
                                        find = 0;
                                        break;
                                    }
                                }
                                else if (temp_state.equals("win2")) {
                                    if (temp_user2.equals(nickname_key)) { //user2이 이겼고, user2이 본인일 때
                                        //Toast.makeText(GameListActivity.this, "You win!", Toast.LENGTH_SHORT).show();
                                        find = 0;
                                        break;
                                    }
                                    else if (!temp_user2.equals(nickname_key)) { //user2이 이겼는데, user2이 본인이 아니라 상대방일 때
                                        //Toast.makeText(GameListActivity.this, "You lose...", Toast.LENGTH_SHORT).show();
                                        find = 0;
                                        break;
                                    }
                                }
                                else if (temp_state.equals("gaming0")) {
                                    timestamp_key = temp_timestamp;
                                    user1_key = temp_user1;
                                    user2_key = temp_user2;
                                    roomname_key = temp_roomname;
                                    script_key = temp_script;
                                    state_key = temp_state;
                                    last = "none";
                                    solve = "none";
                                    bomb_cnt = state_key.charAt(6);
                                    find = 1;
                                    break;
                                }
                                /*else if (temp_state.equals("gaming1") || temp_state.equals("gaming2") || temp_state.equals("gaming3") || temp_state.equals("gaming4")
                                        || temp_state.equals("gaming5") || temp_state.equals("gaming6")) {*/
                                else if (temp_state.equals("gaming1") || temp_state.equals("gaming2")) {
                                    timestamp_key = temp_timestamp;
                                    user1_key = temp_user1;
                                    user2_key = temp_user2;
                                    roomname_key = temp_roomname;
                                    script_key = temp_script;
                                    state_key = temp_state;
                                    bomb_cnt = state_key.charAt(6);
                                    last = postSnapshot.child("quiz_list").child("quiz"+bomb_cnt).child("last").getValue().toString();
                                    solve = postSnapshot.child("quiz_list").child("quiz"+bomb_cnt).child("solve").getValue().toString();
                                    find = 1;
                                    break;
                                }
                            }
                            else if (temp_roomname.equals(roomname_key) && temp_user1.equals(friend_nickname) && temp_user2.equals(nickname_key)) {
                                if (temp_state.equals("win")) {
                                    //Toast.makeText(GameListActivity.this, "Both win!", Toast.LENGTH_SHORT).show();
                                    find = 0;
                                    break;
                                }
                                else if (temp_state.equals("win1")) {
                                    if (temp_user1.equals(nickname_key)) { //user1이 이겼고, user1이 본인일 때
                                        //Toast.makeText(GameListActivity.this, "You win!", Toast.LENGTH_SHORT).show();
                                        find = 0;
                                        break;
                                    }
                                    else if (!temp_user1.equals(nickname_key)) { //user1이 이겼는데, user1이 본인이 아니라 상대방일 때
                                        //Toast.makeText(GameListActivity.this, "You lose...", Toast.LENGTH_SHORT).show();
                                        find = 0;
                                        break;
                                    }
                                }
                                else if (temp_state.equals("win2")) {
                                    if (temp_user2.equals(nickname_key)) { //user2이 이겼고, user2이 본인일 때
                                        //Toast.makeText(GameListActivity.this, "You win!", Toast.LENGTH_SHORT).show();
                                        find = 0;
                                        break;
                                    }
                                    else if (!temp_user2.equals(nickname_key)) { //user2이 이겼는데, user2이 본인이 아니라 상대방일 때
                                        //Toast.makeText(GameListActivity.this, "You lose...", Toast.LENGTH_SHORT).show();
                                        find = 0;
                                        break;
                                    }
                                }
                                else if (temp_state.equals("gaming0")) {
                                    timestamp_key = temp_timestamp;
                                    user1_key = temp_user1;
                                    user2_key = temp_user2;
                                    roomname_key = temp_roomname;
                                    script_key = temp_script;
                                    state_key = temp_state;
                                    last = "none";
                                    solve = "none";
                                    bomb_cnt = state_key.charAt(6);
                                    find = 1;
                                    break;
                                }
                                /*else if (temp_state.equals("gaming1") || temp_state.equals("gaming2") || temp_state.equals("gaming3") || temp_state.equals("gaming4")
                                        || temp_state.equals("gaming5") || temp_state.equals("gaming6")) {*/
                                else if (temp_state.equals("gaming1") || temp_state.equals("gaming2")) {
                                    timestamp_key = temp_timestamp;
                                    user1_key = temp_user1;
                                    user2_key = temp_user2;
                                    roomname_key = temp_roomname;
                                    script_key = temp_script;
                                    state_key = temp_state;
                                    bomb_cnt = state_key.charAt(6);
                                    last = postSnapshot.child("quiz_list").child("quiz"+bomb_cnt).child("last").getValue().toString();
                                    solve = postSnapshot.child("quiz_list").child("quiz"+bomb_cnt).child("solve").getValue().toString();
                                    find = 1;
                                    break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                };
                mPostReference.addValueEventListener(checkRoom);
            }
        });


        makebomb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (check_gamelist == 1) {
                    if (find == 0) {
                        Toast.makeText(GameListActivity.this, "이미 끝난 게임입니다.", Toast.LENGTH_SHORT).show();
                    }
                    //else if (find == 1 && bomb_cnt-'0' == 6) {
                    else if (find == 1 && bomb_cnt-'0' == 2) {
                        Toast.makeText(GameListActivity.this, "문제를 더 이상 만들 수 없습니다!", Toast.LENGTH_SHORT).show();
                    }
                    else if (find == 1 && last.equals(nickname_key) && solve.equals("none")) {
                        Toast.makeText(GameListActivity.this, "상대방이 아직 문제를 풀지 않았습니다.", Toast.LENGTH_SHORT).show();
                    }
                    else if (find == 1 && !last.equals(nickname_key) && solve.equals("none") && !last.equals("none")) {
                        Toast.makeText(GameListActivity.this, "문제를 먼저 풀어주세요.", Toast.LENGTH_SHORT).show();
                    }
                    else if (find == 1 && last.equals("none") && solve.equals("none")) {
                        Toast.makeText(GameListActivity.this, "상대방에게 문제를 내러 가보자!", Toast.LENGTH_SHORT).show();
                        intent_makebombtype = new Intent(GameListActivity.this, MakeBombTypeActivity.class);
                        intent_makebombtype.putExtra("timestamp", timestamp_key);
                        intent_makebombtype.putExtra("id", id_key);
                        intent_makebombtype.putExtra("nickname", nickname_key);
                        intent_makebombtype.putExtra("scriptnm", script_key);
                        intent_makebombtype.putExtra("user1", user1_key);
                        intent_makebombtype.putExtra("user2", user2_key);
                        intent_makebombtype.putExtra("roomname", roomname_key);
                        intent_makebombtype.putExtra("state", state_key);
                        startActivity(intent_makebombtype);
                    }
                    //else if (find == 1 && !last.equals(nickname_key) && (!solve.equals("none") || solve.equals(nickname_key)) && bomb_cnt-'0' != 6) {
                    else if (find == 1 && !last.equals(nickname_key) && (!solve.equals("none") || solve.equals(nickname_key)) && bomb_cnt-'0' != 2) {
                        Toast.makeText(GameListActivity.this, "상대방에게 다시 폭탄을 돌려주자!", Toast.LENGTH_SHORT).show();
                        intent_makebombtype = new Intent(GameListActivity.this, MakeBombTypeActivity.class);
                        intent_makebombtype.putExtra("timestamp", timestamp_key);
                        intent_makebombtype.putExtra("id", id_key);
                        intent_makebombtype.putExtra("nickname", nickname_key);
                        intent_makebombtype.putExtra("scriptnm", script_key);
                        intent_makebombtype.putExtra("user1", user1_key);
                        intent_makebombtype.putExtra("user2", user2_key);
                        intent_makebombtype.putExtra("roomname", roomname_key);
                        intent_makebombtype.putExtra("state", state_key);
                        startActivity(intent_makebombtype);
                    }
                }
                else if (check_gamelist == 0) {
                    Toast.makeText(GameListActivity.this, "게임방을 선택해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        solvebomb.setOnClickListener(new View.OnClickListener() { //OX, choice, shortword type -> need to modify!!!
            public void onClick(View view) {
                if (check_gamelist == 1) {
                    if (find == 0) {
                        Toast.makeText(GameListActivity.this, "이미 끝난 게임입니다.", Toast.LENGTH_SHORT).show();
                    }
                    else if (find == 1 && last.equals(nickname_key) && !solve.equals("none")) {
                        Toast.makeText(GameListActivity.this, "상대방이 아직 문제를 제출하지 않았습니다.", Toast.LENGTH_SHORT).show();
                    }
                    else if (find == 1 && last.equals("none") && solve.equals("none")) {
                        Toast.makeText(GameListActivity.this, "문제를 먼저 만들어주세요.", Toast.LENGTH_SHORT).show();
                    }
                    else if (find == 1 && !last.equals(nickname_key) && solve.equals(nickname_key)) { //마지막으로 상대방이 문제를 내고, 내가 풀었는데, 문제를 다시 toss 하지 않았을 때
                        Toast.makeText(GameListActivity.this, "문제를 낼 차례입니다.", Toast.LENGTH_SHORT).show();
                    }
                    else if (find == 1 && !last.equals(nickname_key) && solve.equals("none")) {
                        Toast.makeText(GameListActivity.this, "상대방이 보낸 폭탄이 도착했어!", Toast.LENGTH_SHORT).show();
                        qaPostReference = FirebaseDatabase.getInstance().getReference().child("gameroom_list").child(timestamp_key).child("quiz_list");
                        final ValueEventListener findQna = new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    String key = postSnapshot.getKey();
                                    type_key = postSnapshot.child("type").getValue().toString();
                                    question_key = postSnapshot.child("question").getValue().toString();
                                    if (key.equals("quiz" + bomb_cnt)) {
                                        if (type_key.equals("ox")) {
                                            answer_key = postSnapshot.child("answer").getValue().toString();
                                            ans1_key = "";
                                            ans2_key = "";
                                            ans3_key = "";
                                            ans4_key = "";
                                            Intent intent_solvebombox = new Intent(GameListActivity.this, SolveBombOXActivity.class);
                                            intent_solvebombox.putExtra("timestamp", timestamp_key);
                                            intent_solvebombox.putExtra("id", id_key);
                                            intent_solvebombox.putExtra("nickname", nickname_key);
                                            intent_solvebombox.putExtra("scriptnm", script_key);
                                            intent_solvebombox.putExtra("user1", user1_key);
                                            intent_solvebombox.putExtra("user2", user2_key);
                                            intent_solvebombox.putExtra("roomname", roomname_key);
                                            intent_solvebombox.putExtra("state", state_key);
                                            intent_solvebombox.putExtra("question", question_key);
                                            intent_solvebombox.putExtra("answer", answer_key);
                                            startActivity(intent_solvebombox);
                                            break;

                                        }
                                        else if (type_key.equals("shortword")) {
                                            answer_key = postSnapshot.child("answer").getValue().toString();
                                            ans1_key = "";
                                            ans2_key = "";
                                            ans3_key = "";
                                            ans4_key = "";
                                            Intent intent_solvebombshortword = new Intent(GameListActivity.this, SolveBombShortwordActivity.class);
                                            intent_solvebombshortword.putExtra("timestamp", timestamp_key);
                                            intent_solvebombshortword.putExtra("id", id_key);
                                            intent_solvebombshortword.putExtra("nickname", nickname_key);
                                            intent_solvebombshortword.putExtra("scriptnm", script_key);
                                            intent_solvebombshortword.putExtra("user1", user1_key);
                                            intent_solvebombshortword.putExtra("user2", user2_key);
                                            intent_solvebombshortword.putExtra("roomname", roomname_key);
                                            intent_solvebombshortword.putExtra("state", state_key);
                                            intent_solvebombshortword.putExtra("question", question_key);
                                            intent_solvebombshortword.putExtra("answer", answer_key);
                                            startActivity(intent_solvebombshortword);
                                            break;

                                        }
                                        else if (type_key.equals("choice")) {
                                            answer_key = postSnapshot.child("answer").getValue().toString();
                                            ans1_key = postSnapshot.child("answer1").getValue().toString();
                                            ans2_key = postSnapshot.child("answer2").getValue().toString();
                                            ans3_key = postSnapshot.child("answer3").getValue().toString();
                                            ans4_key = postSnapshot.child("answer4").getValue().toString();
                                            Intent intent_solvebombchoice = new Intent(GameListActivity.this, SolveBombChoiceActivity.class);
                                            intent_solvebombchoice.putExtra("timestamp", timestamp_key);
                                            intent_solvebombchoice.putExtra("id", id_key);
                                            intent_solvebombchoice.putExtra("nickname", nickname_key);
                                            intent_solvebombchoice.putExtra("scriptnm", script_key);
                                            intent_solvebombchoice.putExtra("user1", user1_key);
                                            intent_solvebombchoice.putExtra("user2", user2_key);
                                            intent_solvebombchoice.putExtra("roomname", roomname_key);
                                            intent_solvebombchoice.putExtra("state", state_key);
                                            intent_solvebombchoice.putExtra("question", question_key);
                                            intent_solvebombchoice.putExtra("answer", answer_key);
                                            intent_solvebombchoice.putExtra("answer1", ans1_key);
                                            intent_solvebombchoice.putExtra("answer2", ans2_key);
                                            intent_solvebombchoice.putExtra("answer3", ans3_key);
                                            intent_solvebombchoice.putExtra("answer4", ans4_key);
                                            startActivity(intent_solvebombchoice);
                                            break;
                                        }
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        };
                        qaPostReference.addListenerForSingleValueEvent(findQna);
                    }
                }
                else if(check_gamelist == 0) {
                    Toast.makeText(GameListActivity.this, "게임방을 선택해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
        gameRoomInfos.clear();
        getFirebaseDatabase();



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

        getFirebaseDatabases();

        final DrawerLayout draw = (DrawerLayout) findViewById(R.id.drawer_layout);
        draw.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                return gdt.onTouchEvent(event);
            }
        });

        //m_oGameBombAcitivity = this;
//////


    }

    public void getFirebaseDatabase() {
        try {
            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    GameListAdapter gameListAdapter=new GameListAdapter();
                    //String roomname, String withwhom, String status
                    String roomname="", withwhom="", status="";
                    String lastperson="", solveperson="";
                    gameRoomInfos.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        String user1 = postSnapshot.child("user1").getValue().toString();
                        String user2 = postSnapshot.child("user2").getValue().toString();
                        String temp_status = postSnapshot.child("state").getValue().toString();
                        char bombcount;
                        if(!temp_status.equals("win") && !temp_status.equals("win1") && !temp_status.equals("win2")){ //gaming0~6
                            bombcount = temp_status.charAt(6);
                        }
                        else { //win, win1, win2
                            bombcount='x';
                        }
                        if(bombcount=='0'){
                            lastperson="none";
                            solveperson="none";
                        }
                        else if(bombcount!='x'){
                            lastperson = postSnapshot.child("quiz_list").child("quiz" + bombcount).child("last").getValue().toString();
                            solveperson = postSnapshot.child("quiz_list").child("quiz" + bombcount).child("solve").getValue().toString();
                        }

                        if (user1.equals(nickname_key)) { //내가 user1
                            FirebasePost_list get = postSnapshot.getValue(FirebasePost_list.class);
                            //data.add(get.roomname + "-" + get.user2);
                            withwhom=get.user2;
                            roomname=get.roomname;

                            if (bombcount=='x' && temp_status.equals("win")){ //ok
                                status="end";
                            }
                            else if (bombcount=='x' && temp_status.equals("win1")){ //ok
                                status="iwin";
                            }
                            else if (bombcount=='x' && temp_status.equals("win2")){ //ok
                                status="youwin";
                            }
                            else if (bombcount=='0') {
                                status="makeplease";
                            }
                            else if (!lastperson.equals(nickname_key) && solveperson.equals(nickname_key)) {
                                status="myturn"; //내가 폭탄 만들 차례
                            }
                            else if (!lastperson.equals(nickname_key) && solveperson.equals("none")) { //ok
                                status="newbomb"; //새 폭탄이 도착했어요 (내가 폭탄을 풀 차례)
                            }
                            else if (lastperson.equals(nickname_key) && solveperson.equals("none")) { //ok
                                status="elsenotsolve"; //친구가 폭탄을 아직 풀지 않음
                            }
                            else if (lastperson.equals(nickname_key) && !solveperson.equals("none")) { //ok
                                status = "elsenotmake"; //친구가 폭탄을 아직 만들지 않음
                            }
                            else{
                                Log.d("폭탄에러났슈user1", "에러유");
                            }
                            GameRoomInfo roomInfo=new GameRoomInfo(roomname, withwhom, status);
                            gameRoomInfos.add(roomInfo);
                            gameListAdapter.addItem(roomname, withwhom, status);
                        }
                        else if (user2.equals(nickname_key)) { //내가 user2
                            FirebasePost_list get = postSnapshot.getValue(FirebasePost_list.class);
                            //data.add(get.roomname + "-" + get.user1);
                            withwhom=get.user1;
                            roomname=get.roomname;

                            if (bombcount=='x' && temp_status.equals("win")){ //ok
                                status="end";
                            }
                            else if (bombcount=='x' && temp_status.equals("win1")){ //ok
                                status="youwin";
                            }
                            else if (bombcount=='x' && temp_status.equals("win2")){ //ok
                                status="iwin";
                            }
                            else if (bombcount=='0') {
                                status="makeplease";
                            }
                            else if (!lastperson.equals(nickname_key) && solveperson.equals(nickname_key)) {
                                status="myturn"; //내가 폭탄 만들 차례
                            }
                            else if (!lastperson.equals(nickname_key) && solveperson.equals("none")) { //ok
                                status="newbomb"; //새 폭탄이 도착했어요 (내가 폭탄을 풀 차례)
                            }
                            else if (lastperson.equals(nickname_key) && solveperson.equals("none")) { //ok
                                status="elsenotsolve"; //친구가 폭탄을 아직 풀지 않음
                            }
                            else if (lastperson.equals(nickname_key) && !solveperson.equals("none")) { //ok
                                status="elsenotmake"; //친구가 폭탄을 아직 만들지 않음
                            }
                            else{
                                Log.d("폭탄에러났슈user2", "에러유");
                            }
                            GameRoomInfo roomInfo=new GameRoomInfo(roomname, withwhom, status);
                            gameRoomInfos.add(roomInfo);
                            gameListAdapter.addItem(roomname, withwhom, status);
                        }
                    }
                    gamelist.setAdapter(gameListAdapter);
                    gamelist.clearChoices();
                    gameListAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };
            mPostReference.addValueEventListener(postListener);
        } catch (java.lang.NullPointerException e) {
        }
    }

    private void getFirebaseDatabaseUserInfo() {
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Solved=0;
                DataSnapshot dataSnapshot1=dataSnapshot.child("user_list/"+id_key+"/my_week_list");
                for(DataSnapshot dataSnapshot2: dataSnapshot1.getChildren()){ //week 껍데기
                    Solved+=Long.parseLong(dataSnapshot2.child("solvebomb").getValue().toString());
                }
                Calendar calendar = Calendar.getInstance();
                Date dateS = calendar.getTime();
                String MedalUpdate = new SimpleDateFormat("yyyy-MM-dd").format(dateS);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                hoonjangFragment_bombmaster=new NewHoonjangFragment();

                if(Solved==365 && nomore.equals("stop2")) {
                    uploadFirebaseUserCoinInfo_H("폭탄마스터", 3);
                    nPostReference.child("user_list/" + id_key + "/my_medal_list/폭탄마스터").setValue("Lev3##"+MedalUpdate);
                    SharedPreferences sf = getSharedPreferences("nomore", MODE_PRIVATE);
                    editor=sf.edit();
                    editor.putString("bombmaster", "stop3");
                    editor.commit();
                    transaction.replace(R.id.gamelist_main, hoonjangFragment_bombmaster);
                    Bundle bundle = new Bundle(3);
                    bundle.putString("what", "bombmaster");
                    bundle.putString("from", "gamelist");
                    bundle.putInt("level", 3);
                    hoonjangFragment_bombmaster.setArguments(bundle);
                    transaction.commit();
                }else if(Solved==100 && nomore.equals("stop1")){
                    uploadFirebaseUserCoinInfo_H("폭탄마스터", 2);
                    nPostReference.child("user_list/" + id_key + "/my_medal_list/폭탄마스터").setValue("Lev2##"+MedalUpdate);
                    SharedPreferences sf = getSharedPreferences("nomore", MODE_PRIVATE);
                    editor=sf.edit();
                    editor.putString("bombmaster", "stop2");
                    editor.commit();
                    transaction.replace(R.id.gamelist_main, hoonjangFragment_bombmaster);
                    Bundle bundle = new Bundle(3);
                    bundle.putString("what", "bombmaster");
                    bundle.putString("from", "gamelist");
                    bundle.putInt("level", 2);
                    hoonjangFragment_bombmaster.setArguments(bundle);
                    transaction.commit();
                }else if(Solved==30 && nomore.equals("keepgoing")){
                    uploadFirebaseUserCoinInfo_H("폭탄마스터", 1);
                    nPostReference.child("user_list/" + id_key + "/my_medal_list/폭탄마스터").setValue("Lev1##"+MedalUpdate);
                    SharedPreferences sf = getSharedPreferences("nomore", MODE_PRIVATE);
                    editor=sf.edit();
                    editor.putString("bombmaster", "stop1");
                    editor.commit();
                    transaction.replace(R.id.gamelist_main, hoonjangFragment_bombmaster);
                    Bundle bundle = new Bundle(3);
                    bundle.putString("what", "bombmaster");
                    bundle.putString("from", "gamelist");
                    bundle.putInt("level", 1);
                    hoonjangFragment_bombmaster.setArguments(bundle);
                    transaction.commit();
                }
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String key = snapshot.getKey();
                    if (key.equals("user_list")) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            String key1 = snapshot1.getKey();
                            if (key1.equals(id_key)) {
                                me = snapshot1.getValue(UserInfo.class);
                                userName1.setText(me.name + " 학생");
                                userGrade1.setText(me.grade + "학년");
                                userCoin.setText(me.coin + " 코인");
                            }
                        }
                    }
                }
            }
            @Override
            public void onCancelled (@NonNull DatabaseError databaseError){
            }
        };
        m2PostReference.addValueEventListener(postListener);
    }

    private void uploadFirebaseUserCoinInfo_H(String hoonjangname, int level){
        nPostReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                String today = new SimpleDateFormat("yyMMddHHmm").format(date);
                nPostReference.child("user_list/" + id_key + "/my_coin_list/" + today + "/get").setValue(Integer.toString(level*100));
                nPostReference.child("user_list/" + id_key + "/my_coin_list/" + today + "/why").setValue(hoonjangname+" 레벨 "+Integer.toString(level)+"달성!");

                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    String key=dataSnapshot1.getKey();
                    if(key.equals("user_list")){
                        String mycoin=dataSnapshot1.child(id_key).child("coin").getValue().toString();
                        int coin = Integer.parseInt(mycoin) + level*100;
                        String coin_convert = Integer.toString(coin);
                        nPostReference.child("user_list/" + id_key).child("coin").setValue(coin_convert);
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

    public void getFirebaseDatabases() {
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
    }*/
//////

}
