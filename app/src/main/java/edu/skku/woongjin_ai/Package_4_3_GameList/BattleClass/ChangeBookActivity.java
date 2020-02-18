package edu.skku.woongjin_ai.Package_4_3_GameList.BattleClass;


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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.skku.woongjin_ai.GlobalApplication;
import edu.skku.woongjin_ai.R;

public class ChangeBookActivity extends AppCompatActivity implements ChangeBookAdapter.OnNoteListener {

    private Button cancleButton;
    private Button changeButton;

    //파베 사용하기위해
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(); //파이어베이스 데이터베이스 연동
    private DatabaseReference databaseReference = firebaseDatabase.getReference(); //DB 테이블 연결

    //리사이클러뷰 사용하기위해
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<ChangeBookName> arrayList;

    //리사이클러뷰 눌렀을때 책이름 알기위해
    private String mName2 = null;

    private String script_text;

    GlobalApplication gv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //상태바 제거(전체화면 모드)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_battlechangebook);

        //취소 변경 이벤트
        cancleButton = findViewById(R.id.cancleButton);
        changeButton = findViewById(R.id.changeButton);

        //리싸이클러뷰와 어뎁터 관련 연결
        recyclerView = findViewById(R.id.recyclerView2); // first xml에 있는 리싸이클러뷰 연결
        recyclerView.setHasFixedSize(true); //리싸이클러뷰 성능강화
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();

        //리싸이클러뷰 구분선 추가
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));

        gv = (GlobalApplication) getApplication();

        //파이어베이스 데이터 읽기
        databaseReference.child("script_list2").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //실제적으로 파이어베이스 데이터베이스의 데이터를 받아오는 곳

                arrayList.clear(); //BookName객체를 담을 어레이리스트인데 기존 배열 리스트가 존재하지않게 초기화

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){ //반복문으로 데이터 리스트 추출

                    ChangeBookName changeBookName = snapshot.getValue(ChangeBookName.class);

                    arrayList.add(changeBookName); //어레이 리스트에 담음, 담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비 끝
                }

                adapter.notifyDataSetChanged(); //리스트 저장 및 새로고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //디비를 가져오던 중 에러 발생 시
                Log.e("ChangeBookActivity", String.valueOf(databaseError.toException())); //에러문 출력
            }
        });

        //이제 어뎁터를 생성, 이제 어레이리스트에 context 넘겨줄게
        adapter = new ChangeBookAdapter(arrayList, this, this);
        recyclerView.setAdapter(adapter); //리싸이클러뷰에 어뎁터 연결


        //취소버튼 눌렀을때
        cancleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //뒤로가기
                finish();
            }
        });

        //변경버튼 눌렀을때
        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        //방장이 읽은책으로만
                        if (dataSnapshot.child("user_list").child(gv.getGlobalID()).child("my_script_list").child(mName2).exists())
                        {

                            //책제목파베 덮여쓰기
                            databaseReference.child("gameroom_list2").child(""+gv.getRoomnum()).child("bookName").setValue(mName2);
                            Toast.makeText(ChangeBookActivity.this, "책이 변경되었어요!", Toast.LENGTH_SHORT).show();

                        }

                        else {
                            Toast.makeText(ChangeBookActivity.this, "책이 변경되지 않았어요!", Toast.LENGTH_SHORT).show();

                        }


                }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




                finish();




            }
        });

    }


    // 액티비티에서 클릭이벤트
    @Override
    public void onNoteClick(int position) {

        ChangeBookName dict = arrayList.get(position);

        mName2 = dict.getName();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child("user_list").child(gv.getGlobalID()).child("my_script_list").child(mName2).exists())
                {
                    Toast.makeText(ChangeBookActivity.this, "읽은 책이에요!", Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(ChangeBookActivity.this, "안 읽은 책이에요!", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
