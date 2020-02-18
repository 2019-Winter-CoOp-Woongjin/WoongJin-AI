package edu.skku.woongjin_ai.Package_4_3_GameList.SolveBombType;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.skku.woongjin_ai.Package_4_3_GameList.SolveBombType.Fragment.CorrectBombFragment;
import edu.skku.woongjin_ai.Package_4_3_GameList.SolveBombType.Fragment.EndBombFragment;
import edu.skku.woongjin_ai.Package_4_3_GameList.SolveBombType.Fragment.WrongBombFragment;
import edu.skku.woongjin_ai.R;
import edu.skku.woongjin_ai.Package_4_2_MainQuizType.ShowScriptFragment;

public class SolveBombShortwordActivity extends AppCompatActivity implements ShowScriptFragment.OnFragmentInteractionListener {
    DatabaseReference mPostReference, wPostReference;
    Intent intent, intent_correct, intent_wrong, intent_end;
    String timestamp_key, id_key, nickname_key, user1_key, user2_key, roomname_key, script_key, state_key, question_key, answer_key;
    char bomb_cnt;
    TextView timer, gamers, question;
    EditText answer_edit;
    ImageButton imageButtonScript;
    Button imageButtonCheck;
    String user_answer;
    public Fragment showScriptFragment;

    ImageView bomb_animate;

    int second = 60;
    int correct_end = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solvebombshortword);

        timer = (TextView) findViewById(R.id.timer);
        mHandler.sendEmptyMessage(0);
        gamers = (TextView) findViewById(R.id.gamers);
        question = (TextView) findViewById(R.id.question);
        answer_edit = (EditText) findViewById(R.id.answer_edit);
        imageButtonScript = (ImageButton) findViewById(R.id.script);
        imageButtonCheck = (Button)findViewById(R.id.check);

        bomb_animate = findViewById(R.id.bomb_animate);
        final Animation wave = AnimationUtils.loadAnimation(this, R.anim.wave);
        bomb_animate.startAnimation(wave);

        intent = getIntent();
        intent_wrong = new Intent(SolveBombShortwordActivity.this, WrongBombFragment.class);
        intent_end = new Intent(SolveBombShortwordActivity.this, EndBombFragment.class);
        intent_correct = new Intent(SolveBombShortwordActivity.this, CorrectBombFragment.class);
        timestamp_key = intent.getStringExtra("timestamp");
        id_key = intent.getStringExtra("id");
        nickname_key = intent.getStringExtra("nickname");
        script_key = intent.getStringExtra("scriptnm");
        user1_key = intent.getStringExtra("user1");
        user2_key = intent.getStringExtra("user2");
        roomname_key = intent.getStringExtra("roomname");
        state_key = intent.getStringExtra("state");
        question_key = intent.getStringExtra("question");
        answer_key = intent.getStringExtra("answer");

        bomb_cnt = state_key.charAt(6);
        mPostReference = FirebaseDatabase.getInstance().getReference().child("gameroom_list").child(timestamp_key).child("quiz_list");
        wPostReference  = FirebaseDatabase.getInstance().getReference().child("gameroom_list").child(timestamp_key);

        gamers.setText(user1_key + " vs " + user2_key);
        question.setText(question_key);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        imageButtonCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_answer = answer_edit.getText().toString();
                if (user_answer.equals("")) {
                    Toast.makeText(SolveBombShortwordActivity.this, "정답을 입력하세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (user_answer.equals(answer_key)) {
                        final ValueEventListener check = new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    String quiznum = postSnapshot.getKey();
                                    if (quiznum.equals("quiz" + bomb_cnt)) {
                                        mPostReference.child(quiznum).child("solve").setValue(nickname_key);
                                        break;
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) { }
                        };
                        mPostReference.addListenerForSingleValueEvent(check);
                        correct_end = 1;
                        //if (bomb_cnt == '6') {
                        if (bomb_cnt == '2') {
                            wPostReference.child("state").setValue("win");
                            intent_end.putExtra("id", id_key);
                            intent_end.putExtra("nickname", nickname_key);
                            intent_end.putExtra("user1", user1_key);
                            intent_end.putExtra("user2", user2_key);
                            startActivity(intent_end);
                            finish();
                        }
                        //else if (bomb_cnt != '6') {
                        else if (bomb_cnt != '2') {
                            intent_correct.putExtra("timestamp", timestamp_key);
                            intent_correct.putExtra("id", id_key);
                            intent_correct.putExtra("nickname", nickname_key);
                            intent_correct.putExtra("user1", user1_key);
                            intent_correct.putExtra("user2", user2_key);
                            intent_correct.putExtra("roomname", roomname_key);
                            intent_correct.putExtra("scriptnm", script_key);
                            intent_correct.putExtra("state", state_key);
                            startActivity(intent_correct);
                            finish();
                        }
                    }
                    else if (!user_answer.equals(answer_key)) {
                        Toast.makeText(SolveBombShortwordActivity.this, "다시 시도해보세요.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        imageButtonScript.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showScriptFragment = new ShowScriptFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.contents, showScriptFragment);
                Bundle bundle = new Bundle(2);
                bundle.putString("scriptnm", script_key);
                bundle.putString("type", "solvebombshortword");
                showScriptFragment.setArguments(bundle);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            second--;
            timer.setText("00 :  " + second);

            // 메세지를 처리하고 또다시 핸들러에 메세지 전달 (1000ms 지연)
            mHandler.sendEmptyMessageDelayed(0,1000);

            if (second == 0 && correct_end == 0) {
                if (nickname_key.equals(user1_key)) {
                    wPostReference.child("state").setValue("win2");
                }
                else if (nickname_key.equals(user2_key)) {
                    wPostReference.child("state").setValue("win1");
                }
                intent_wrong.putExtra("id", id_key);
                intent_wrong.putExtra("nickname", nickname_key);
                intent_wrong.putExtra("user1", user1_key);
                intent_wrong.putExtra("user2", user2_key);
                startActivity(intent_wrong);
                finish();
            }
            if (correct_end == 1) {} //답을 맞췄을 때
        }
         //답을 맞췄을 때
    };

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
