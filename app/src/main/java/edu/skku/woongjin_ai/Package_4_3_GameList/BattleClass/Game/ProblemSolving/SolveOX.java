package edu.skku.woongjin_ai_winter.Package_4_3_GameList.BattleClass.Game.ProblemSolving;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.skku.woongjin_ai_winter.GlobalApplication;
import edu.skku.woongjin_ai_winter.Package_4_3_GameList.BattleClass.Game.Problem;
import edu.skku.woongjin_ai_winter.R;

public class SolveOX extends Activity {

    private ImageView imageO, imageX;
    private ImageButton submit, giveup;
    private TextView problem, solution;
    private String answer;

    //전역 변수
    GlobalApplication gv;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();

    //intent 되고난 후 activity 지우기 위한 설정
    public static SolveOX activity = null;

    private int pushO, pushX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        super.onCreate(savedInstanceState);

        //타이틀바 없앰
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.activity_battlesolveox);

        submit = findViewById(R.id.answer_submit);
        giveup = findViewById(R.id.answer_giveup);
        imageO  = findViewById(R.id.o);
        imageX = findViewById(R.id.x);
        solution = findViewById(R.id.solution);

        problem = findViewById(R.id.textProb);
        problem.setMovementMethod(new ScrollingMovementMethod());

        pushO = 0; pushX = 0;

        //전역변수
        gv = (GlobalApplication) getApplication();

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

        imageO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pushO == 1) {
                    pushO = 0;
                    imageO.setImageResource(R.drawable.o_orange);
                }
                else if (pushX == 1) {
                    pushO = 1;
                    pushX = 0;
                    imageO.setImageResource(R.drawable.ic_icons_blue_o);
                    imageX.setImageResource(R.drawable.x_orange);
                }
                else {
                    pushO = 1;
                    imageO.setImageResource(R.drawable.ic_icons_blue_o);
                }
            }
        });

        imageX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pushX == 1) {
                    pushX = 0;
                    imageX.setImageResource(R.drawable.x_orange);
                }
                else if (pushO == 1) {
                    pushX = 1;
                    pushO = 0;
                    imageX.setImageResource(R.drawable.ic_icons_red_x);
                    imageO.setImageResource(R.drawable.o_orange);
                }
                else {
                    pushX = 1;
                    imageX.setImageResource(R.drawable.ic_icons_red_x);
                }
            }
        });

        //제출 버튼 클릭 시 문제 정답 비교 후 결과 넘김
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((pushO + pushX) == 0)
                    Toast.makeText(SolveOX.this, "정답을 선택 해주세요.", Toast.LENGTH_SHORT).show();
                else {
                    if (pushO == 1) {
                        //정답일 때 OK 반환
                        if (answer.equals("O")) {
                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            finish();
                        }

                        //오답일 때 CANCELED 반환
                        else {
                            Intent intent = new Intent();
                            setResult(RESULT_CANCELED, intent);
                            finish();
                        }
                    }
                    else {
                        //정답일 때 OK 반환
                        if (answer.equals("X")) {
                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            finish();

                        }

                        //오답일 때 CANCELED 반환
                        else {
                            Intent intent = new Intent();
                            setResult(RESULT_CANCELED, intent);
                            finish();
                        }
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