package edu.skku.woongjin_ai_winter.Package_4_3_GameList.BattleClass.MakingRoom;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

import edu.skku.woongjin_ai_winter.GlobalApplication;
import edu.skku.woongjin_ai_winter.Package_4_3_GameList.BattleClass.GamePlayer;
import edu.skku.woongjin_ai_winter.Package_4_3_GameList.BattleClass.MainPage;
import edu.skku.woongjin_ai_winter.R;


public class MakingRoomActivity extends AppCompatActivity implements MakingRoomAdapter.OnNoteListener {

    //책제목 적는 에딧 텍스트랑 취소버튼 선언
    private EditText roomNameEdit;
    private Button cancleButton;
    private Button createButton;

    //파베 사용하기위해
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(); //파이어베이스 데이터베이스 연동
    private DatabaseReference databaseReference = firebaseDatabase.getReference(); //DB 테이블 연결

    //리사이클러뷰 사용하기위해
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<BookName> arrayList; //MakingRoomActivity랑 어뎁터랑 어레이 리스트로 왓다갓다 할꺼기때문에 어레이리스트는 통신매개체

    //리사이클러뷰 눌렀을때 책이름 알기위해
    private String mName = null;

    //인텐트
    private String id_key;
    private Intent intent;

    //
    private String script_text;

    /*
    //리스트뷰 해볼려고 해보기.. NationGameActivity 참고
    private DatabaseReference sPostReference;
    ListView script_list;
    ArrayList<String> scriptArrayList;
    edu.skku.woongjin_ai.Package_4_3_GameList.NationGame.ScriptListAdapter ScriptListAdapter;
    String script_name;

    long Solved;
    String text_roomname;

    Intent intent;
    int check_script, flag;
    */

    GlobalApplication gv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gv = (GlobalApplication) getApplication();

        //상태바 제거(전체화면 모드)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_battlemakingroom);

        //책제목 적는 에딧 텍스트랑 취소,생성버튼 xml연결
        roomNameEdit = findViewById(R.id.roomNameEditText);

        cancleButton = findViewById(R.id.cancleButton);
        createButton = findViewById(R.id.createButton);

        //리싸이클러뷰와 어뎁터 관련 연결
        recyclerView = findViewById(R.id.recyclerView); // first xml에 있는 리싸이클러뷰 연결
        recyclerView.setHasFixedSize(true); //리싸이클러뷰 성능강화
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>(); // BookName 객체를 담을 어레이 리스트(어뎁터쪽으로 날림)

        //리싸이클러뷰 구분선 추가
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));

        //받아오기
        intent = getIntent();
        id_key = intent.getExtras().getString("id");

        /*//리스트뷰
        check_script = 0;
        flag = 0;
        script_list = findViewById(R.id.script_list);
        scriptArrayList = new ArrayList<String>();
        ScriptListAdapter = new ScriptListAdapter();

        sPostReference = FirebaseDatabase.getInstance().getReference().child("script_list");

        getFirebaseDatabase();
        getFirebaseDatabaseScriptList();

        script_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ScriptListItem temp_show = (ScriptListItem) parent.getItemAtPosition(position);
                script_name = temp_show.getTitle();
                check_script = 1;
            }
        });

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
        }*/

        //파이어베이스 데이터 읽기
        databaseReference.child("script_list2").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //실제적으로 파이어베이스 데이터베이스의 데이터를 받아오는 곳

                arrayList.clear(); //BookName객체를 담을 어레이리스트인데 기존 배열 리스트가 존재하지않게 초기화

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){ //반복문으로 데이터 리스트 추출

                    BookName bookname = snapshot.getValue(BookName.class);

                   arrayList.add(bookname); //어레이 리스트에 담음, 담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비 끝
                }

                adapter.notifyDataSetChanged(); //리스트 저장 및 새로고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //디비를 가져오던 중 에러 발생 시
                Log.e("MakingRoomActivity", String.valueOf(databaseError.toException())); //에러문 출력
            }
        });

        //이제 어뎁터를 생성, 이제 어레이리스트에 context 넘겨줄게
        adapter = new MakingRoomAdapter(arrayList, this, this);
        recyclerView.setAdapter(adapter); //리싸이클러뷰에 어뎁터 연결



    }



    @Override
    protected void onStart() {
        super.onStart();

        //생성버튼 눌렀을때
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //방 제목쓴거 스트링으로 변환
                String roomName = roomNameEdit.getText().toString();


                //방 텍스트 안썻을떄
                if(!roomName.equals(""))
                {
                    if(mName == null){ //책 클릭안했을떄
                        Toast.makeText(getApplicationContext(), "책을 선택해주세요.", Toast.LENGTH_SHORT).show();
                    }
                    else{ //방제목쓰고 책 클릭 했을때
                        //파이어베이스 데이터 읽기
                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.child("user_list").child(gv.getGlobalID()).child("my_script_list").child(mName).exists()) {
                                    //실제적으로 파이어베이스 데이터베이스의 데이터를 받아오는 곳
                                    int ranRoomNum;
                                    Random random = new Random();


                                    script_text = dataSnapshot.child("script_list").child("" + mName).child("text").getValue(String.class);

                                    gv.setScript(script_text);



                                    //do while문으로 처음 한번은 실행되도록.
                                    do {
                                        ranRoomNum = random.nextInt(30); //다시 랜덤하기

                                        if (!dataSnapshot.child("gameroom_list2").child("" + ranRoomNum).exists()) { //방넘버가 존재하지 않을경우 방생성하기


                                            //방번호 전역변수로 올리기
                                            gv.setRoomnum(ranRoomNum);

                                            //게임만든사람은 player 0로 전역변수 ㄱㄱ
                                            gv.setGameID(0);

                                            //기타 게임에 필요한 정보 파베에 올리기
                                            databaseReference.child("gameroom_list2").child("" + gv.getRoomnum()).child("player" + gv.getGameID()).setValue(new GamePlayer(gv.getGlobalName(), gv.getGameID(), gv.getGlobalID(), 0, 0, 0, 3));

                                            //방 제목쓴거 스트링으로 변환
                                            String roomName = roomNameEdit.getText().toString();

                                            //파베에 방 제목 올리기
                                            databaseReference.child("gameroom_list2").child("" + gv.getRoomnum()).child("roomName").setValue(roomName);

                                            //파베에 방 번호 올리기
                                            databaseReference.child("gameroom_list2").child("" + gv.getRoomnum()).child("roomNum").setValue("" + gv.getRoomnum());

                                            //게임화면으로 넘어가기
                                            Intent intent = new Intent(MakingRoomActivity.this, MainPage.class);
                                            intent.putExtra("id", id_key);
                                            startActivity(intent);


                                            //DB에 방만들떄 리사이클러뷰 누를때 방제목이 파베에 저장되도록
                                            databaseReference.child("gameroom_list2").child("" + gv.getRoomnum()).child("bookName").setValue(mName);


                                            //방을 만들엇으므로 personNum 1로 DB에 저장, 이때 스트링형태로 읽기때문에 스트링으로 저장!
                                            String personNum = "1";
                                            databaseReference.child("gameroom_list2").child("" + gv.getRoomnum()).child("personNum").setValue(personNum);

                                            break;


                                        }

                                    }
                                    while (dataSnapshot.child("" + ranRoomNum).exists()); //방번호 데이터베이스에 존재하는지 확인

                                    //이중 for문 이용해서 생각해보기, 위의 do while문으로 해결함.
                                /*
                                if (dataSnapshot.child("" + ranRoomNum).exists()) {
                                    //ranRoomNum이 존재할때
                                    //랜덤으로 돌리기

                                    ranRoomNum = random.nextInt(15) ;

                                } else{

                                    //전역변수 위해
                                    GlobalApplication gv = (GlobalApplication) getApplication();


                                    //방번호 전역변수로 올리기
                                    gv.setRoomnum(ranRoomNum);

                                    //게임만든사람은 player 0로 전역변수 ㄱㄱ
                                    gv.setGameID(0);

                                    //기타 게임에 필요한 정보 파베에 올리기
                                    databaseReference.child("gameroom_list2").child(""+gv.getRoomnum()).child("player"+gv.getGameID()).setValue(new GamePlayer(gv.getGlobalName(), gv.getGameID(),gv.getGlobalID(), 0, 0, 0, 3));

                                    //방 제목쓴거 스트링으로 변환
                                    String roomName = roomNameEdit.getText().toString();

                                    //파베에 방 제목 올리기
                                    databaseReference.child("gameroom_list2").child(""+gv.getRoomnum()).child("roomName").setValue(roomName);

                                    //파베에 방 번호 올리기
                                    databaseReference.child("gameroom_list2").child(""+gv.getRoomnum()).child("roomNum").setValue(""+gv.getRoomnum());

                                    //게임화면으로 넘어가기
                                    Intent intent = new Intent(MakingRoomActivity.this, MainPage.class);
                                    intent.putExtra("id", id_key);
                                    startActivity(intent);



                                    //DB에 방만들떄 리사이클러뷰 누를때 방제목이 파베에 저장되도록
                                    databaseReference.child("gameroom_list2").child(""+gv.getRoomnum()).child("bookName").setValue(mBookName);


                                    //방을 만들엇으므로 personNum 1로 DB에 저장, 이때 스트링형태로 읽기때문에 스트링으로 저장!
                                    String personNum = "1";
                                    databaseReference.child("gameroom_list2").child(""+gv.getRoomnum()).child("personNum").setValue(personNum);

                                }*/

                                }
                                else {
                                    Toast.makeText(MakingRoomActivity.this, "책을 먼저 읽고 와야해요!", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                //디비를 가져오던 중 에러 발생 시
                                Log.e("MakingRoomActivity", String.valueOf(databaseError.toException())); //에러문 출력
                            }
                        });


                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "방제목을 써주세요.", Toast.LENGTH_SHORT).show();
                }


            }
        });

        //취소버튼 눌렀을때
        cancleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //뒤로가기
                finish();
            }
        });

    }

    // 액티비티에서 클릭이벤트
    @Override
    public void onNoteClick(int position) {


        BookName dict = arrayList.get(position);

        mName = dict.getName();


        /*
        Roomnum ++;
        //책제목클릭하면 roomnum이 전역변수로 ㄱㄱ
        gv.setRoomnum(Roomnum);*/

        /*
        //DB에 방만들떄 리사이클러뷰 누를때 방제목이 파베에 저장되도록
        databaseReference.child("game").child("room").child(""+gv.getRoomnum()).child("bookName").setValue(dict.getBookName());
         */

        //Toast.makeText(MakingRoomActivity.this,"Position"+position,Toast.LENGTH_SHORT).show();
        //System.out.println("책이름" +dict.getBookName());

        //이제 책 제목 넣어야함, 책 제목을 클릭시 뽑아낼수 있도록 해야함. 그담 파이어베이스로 보내야함
        //그담 확인 눌르면 게임액티비티로 가지게 해야함

       /* arrayList.get(position);
        Intent intent = new Intent(this, GameRoomActivity.java);
        startActivity(intent);*/
    }

}


