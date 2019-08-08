package edu.skku.woongjin_ai;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ChoiceTypeActivity extends AppCompatActivity {

    DatabaseReference mPostReference;
    ImageView imageScript, imageCheck, imageStar1, imageStar2, imageStar3, imageStar4, imageStar5;
    EditText editQuiz, editAns, editAns1, editAns2, editAns3, editAns4, editDesc;
    Intent intent, intentHome;
    String id, scriptnm;
    String quiz = "", ans = "", ans1 = "", ans2 = "", ans3 = "", ans4 = "", desc = "";
    int star = 0;
    int flagS1 = 0, flagS2 = 0, flagS3 = 0, flagS4 = 0, flagS5 = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choicetype);

        ImageView imageHome = (ImageView) findViewById(R.id.home);
        imageScript = (ImageView) findViewById(R.id.script);
        imageCheck = (ImageView) findViewById(R.id.check);
        imageStar1 = (ImageView) findViewById(R.id.star1);
        imageStar2 = (ImageView) findViewById(R.id.star2);
        imageStar3 = (ImageView) findViewById(R.id.star3);
        imageStar4 = (ImageView) findViewById(R.id.star4);
        imageStar5 = (ImageView) findViewById(R.id.star5);
        editQuiz = (EditText) findViewById(R.id.quiz);
        editAns = (EditText) findViewById(R.id.ans);
        editAns1 = (EditText) findViewById(R.id.ans1);
        editAns2 = (EditText) findViewById(R.id.ans2);
        editAns3 = (EditText) findViewById(R.id.ans3);
        editAns4 = (EditText) findViewById(R.id.ans4);
        editDesc = (EditText) findViewById(R.id.desc);
        TextView title = (TextView) findViewById(R.id.title);

        intent = getIntent();
        id = intent.getStringExtra("id");
        scriptnm = intent.getStringExtra("scriptnm");

        title.setText("지문 제목: " + scriptnm);

        mPostReference = FirebaseDatabase.getInstance().getReference();

        imageCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quiz = editQuiz.getText().toString();
                ans = editAns.getText().toString();
                ans1 = editAns1.getText().toString();
                ans2 = editAns2.getText().toString();
                ans3 = editAns3.getText().toString();
                ans4 = editAns4.getText().toString();
                desc = editDesc.getText().toString();
                if(quiz.length() == 0 || ans.length() == 0 || ans1.length() == 0 || ans2.length() == 0 || ans3.length() == 0 || ans4.length() == 0 || desc.length() == 0 || star < 1) {
                    Toast.makeText(ChoiceTypeActivity.this, "Fill all blanks", Toast.LENGTH_SHORT).show();
                } else {
                    postFirebaseDatabaseQuizChoice();
                }
            }
        });

        imageHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentHome = new Intent(ChoiceTypeActivity.this, MainActivity.class);
                intentHome.putExtra("id", id);
                startActivity(intentHome);
                finish();
            }
        });

        imageStar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagS1 == 0) {
                    star++;
                    imageStar1.setImageResource(R.drawable.checked_circle_white);
                    flagS1 = 1;
                } else {
                    star--;
                    imageStar1.setImageResource(R.drawable.unchecked_circle_white);
                    flagS1 = 0;
                }
            }
        });

        imageStar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagS2 == 0) {
                    star++;
                    imageStar2.setImageResource(R.drawable.checked_circle_white);
                    flagS2 = 1;
                } else {
                    star--;
                    imageStar2.setImageResource(R.drawable.unchecked_circle_white);
                    flagS2 = 0;
                }
            }
        });

        imageStar3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagS3 == 0) {
                    star++;
                    imageStar3.setImageResource(R.drawable.checked_circle_white);
                    flagS3 = 1;
                } else {
                    star--;
                    imageStar3.setImageResource(R.drawable.unchecked_circle_white);
                    flagS3 = 0;
                }
            }
        });

        imageStar4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagS4 == 0) {
                    star++;
                    imageStar4.setImageResource(R.drawable.checked_circle_white);
                    flagS4 = 1;
                } else {
                    star--;
                    imageStar4.setImageResource(R.drawable.unchecked_circle_white);
                    flagS4 = 0;
                }
            }
        });

        imageStar5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagS5 == 0) {
                    star++;
                    imageStar5.setImageResource(R.drawable.checked_circle_white);
                    flagS5 = 1;
                } else {
                    star--;
                    imageStar5.setImageResource(R.drawable.unchecked_circle_white);
                    flagS5 = 0;
                }
            }
        });
    }

    private void postFirebaseDatabaseQuizChoice() {
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        QuizChoiceTypeInfo post = new QuizChoiceTypeInfo(id, quiz, ans, ans1, ans2, ans3, ans4, Integer.toString(star), desc, "0");
        postValues = post.toMap();
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        ts = ts + id;
        childUpdates.put("/quiz_list/" + scriptnm + "/type2/" + ts + "/", postValues);
        mPostReference.updateChildren(childUpdates);
        editQuiz.setText("");
        editAns.setText("");
        editAns1.setText("");
        editAns2.setText("");
        editAns3.setText("");
        editAns4.setText("");
        editDesc.setText("");
    }
}