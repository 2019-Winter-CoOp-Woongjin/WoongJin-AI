package edu.skku.woongjin_ai.Package_4_3_GameList.BattleClass.Game.ProblemSolving;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.skku.woongjin_ai.GlobalApplication;
import edu.skku.woongjin_ai.Package_4_3_GameList.BattleClass.Game.Problem;
import edu.skku.woongjin_ai.R;

public class SolveShort extends Activity {

    private ImageButton submit, giveup;
    private TextView problem, solution;
    private EditText ans;
    public String answer;

    //전역변수
    GlobalApplication gv;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();

    //intent 되고난 후 activity 지우기 위한 설정
    public static SolveShort activity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        super.onCreate(savedInstanceState);

        //타이틀바 없앰
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.activity_battlesolveshort);

        submit = findViewById(R.id.answer_submit);
        giveup = findViewById(R.id.answer_giveup);

        problem = findViewById(R.id.textProb);
        problem.setMovementMethod(new ScrollingMovementMethod());

        ans = findViewById(R.id.editAns);
        solution = findViewById(R.id.solution);

        //전역변수
        gv = (GlobalApplication) getApplication();

        //db에서 정답 받아오기
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener(){
            int enemyGameID = (gv.getGameID() + 1) % 2;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Problem prob = dataSnapshot.child("gameroom_list2").child(""+gv.getRoomnum()).child("problem"+enemyGameID).getValue(Problem.class);
                problem.setText(prob.problem);
                answer = prob.answer;
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

        ans.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    submit.requestFocus();
                    return true;
                }
                return false;
            }
        });

        //제출 버튼 클릭 시 문제 정답 비교 후 결과 넘김
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ans.getText().toString().equals(""))
                    Toast.makeText(SolveShort.this, "정답을 입력 해주세요.", Toast.LENGTH_SHORT).show();

                    //정답일때 OK 반환
                else {
                    if (answer.equals(ans.getText().toString())) {
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
        Toast.makeText(this, "문제를 푸십시오.", Toast.LENGTH_SHORT).show();
    }
}
