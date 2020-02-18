package edu.skku.woongjin_ai.Package_4_3_GameList.BattleClass.Game.ProblemSolving;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import android.support.annotation.NonNull;

import edu.skku.woongjin_ai.GlobalApplication;
import edu.skku.woongjin_ai.Package_4_3_GameList.BattleClass.Game.Problem;
import edu.skku.woongjin_ai.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SolveMulti extends Activity {

    private ImageButton submit, giveup;
    private TextView problem, answer1, answer2, answer3, answer4, solution;

    //전역변수
    GlobalApplication gv;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();

    //intent 되고난 후 activity 지우기 위한 설정
    public static SolveMulti activity = null;

    private int ans, ans1, ans2, ans3, ans4, ansNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        super.onCreate(savedInstanceState);

        //타이틀바 없앰
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.activity_battlesolvemulti);

        submit = findViewById(R.id.answer_submit);
        giveup = findViewById(R.id.answer_giveup);

        problem = findViewById(R.id.textProb);
        problem.setMovementMethod(new ScrollingMovementMethod());

        answer1 = findViewById(R.id.ans1);
        answer2 = findViewById(R.id.ans2);
        answer3 = findViewById(R.id.ans3);
        answer4 = findViewById(R.id.ans4);
        solution = findViewById(R.id.solution);

        //전역변수
        gv = (GlobalApplication) getApplication();

        //고른 정답 번호
        ans = 0;

        //db에서 정답 받아오기
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener(){
            int enemyGameID = (gv.getGameID() + 1) % 2;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Problem prob = dataSnapshot.child("gameroom_list2").child(""+gv.getRoomnum()).child("problem"+enemyGameID).getValue(Problem.class);
                problem.setText(prob.problem);
                answer1.setText(prob.ans1);
                answer2.setText(prob.ans2);
                answer3.setText(prob.ans3);
                answer4.setText(prob.ans4);
                ansNum = prob.answerNum;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //바깥 눌러도 안꺼지게 설정
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return true;
    }

    protected void onStart() {
        super.onStart();

        //정답 1 눌렀을 때
        answer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //이미 눌러져 있을 때
                if (ans1 == 1){
                    ans1 = 0; ans = 0;
                    answer1.setBackgroundResource(R.drawable.rounded_white_orange_border);
                }
                //안눌려있을 때
                else {
                    ans1 = 1; ans = 1;
                    ans2 = 0; ans3 = 0; ans4 = 0;
                    answer1.setBackgroundResource(R.drawable.rounded_orange);
                    answer2.setBackgroundResource(R.drawable.rounded_white_orange_border);
                    answer3.setBackgroundResource(R.drawable.rounded_white_orange_border);
                    answer4.setBackgroundResource(R.drawable.rounded_white_orange_border);
                }
            }
        });

        //정답 2 눌렀을 때
        answer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //이미 눌러져 있을 때
                if (ans2 == 1){
                    ans2 = 0; ans = 0;
                    answer2.setBackgroundResource(R.drawable.rounded_white_orange_border);
                }
                //안눌려있을 때
                else {
                    ans2 = 1; ans = 2;
                    ans1 = 0; ans3 = 0; ans4 = 0;
                    answer2.setBackgroundResource(R.drawable.rounded_orange);
                    answer1.setBackgroundResource(R.drawable.rounded_white_orange_border);
                    answer3.setBackgroundResource(R.drawable.rounded_white_orange_border);
                    answer4.setBackgroundResource(R.drawable.rounded_white_orange_border);
                }
            }
        });

        //정답 3 눌렀을 때
        answer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //이미 눌러져 있을 때
                if (ans3 == 1){
                    ans3 = 0; ans = 0;
                    answer3.setBackgroundResource(R.drawable.rounded_white_orange_border);
                }
                //안눌려있을 때
                else {
                    ans3 = 1; ans = 3;
                    ans1 = 0; ans2 = 0; ans4 = 0;
                    answer3.setBackgroundResource(R.drawable.rounded_orange);
                    answer1.setBackgroundResource(R.drawable.rounded_white_orange_border);
                    answer2.setBackgroundResource(R.drawable.rounded_white_orange_border);
                    answer4.setBackgroundResource(R.drawable.rounded_white_orange_border);
                }
            }
        });

        //정답 4 눌렀을 때
        answer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //이미 눌러져 있을 때
                if (ans4 == 1){
                    ans4 = 0; ans = 0;
                    answer4.setBackgroundResource(R.drawable.rounded_white_orange_border);
                }
                //안눌려있을 때
                else {
                    ans4 = 1; ans = 4;
                    ans1 = 0; ans2 = 0; ans3 = 0;
                    answer4.setBackgroundResource(R.drawable.rounded_orange);
                    answer1.setBackgroundResource(R.drawable.rounded_white_orange_border);
                    answer2.setBackgroundResource(R.drawable.rounded_white_orange_border);
                    answer3.setBackgroundResource(R.drawable.rounded_white_orange_border);
                }
            }
        });

        //제출 버튼 클릭 시 문제 정답 비교 후 결과 넘김
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ans == 0)
                    Toast.makeText(SolveMulti.this, "정답을 선택 해주세요.", Toast.LENGTH_SHORT).show();

                    //정답일때 OK 반환
                else {
                    if (ansNum == ans) {
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                    }

                    //오답일때 CANCELED 반환
                    else {
                        Intent intent = new Intent();
                        setResult(RESULT_CANCELED, intent);
                        finish();
                    }

                }
            }
        });

        giveup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "문제를 풀어보아요!.", Toast.LENGTH_SHORT).show();
    }
}
