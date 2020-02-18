package edu.skku.woongjin_ai_winter.Package_4_1_NationBook.Select_Book.ReadScript.SelectType;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

import edu.skku.woongjin_ai_winter.Package_3_Main.MainActivity;
import edu.skku.woongjin_ai_winter.Package_4_1_NationBook.Select_Book.ReadScript.SelectType.QuizType.QuizChoiceTypeInfo;
import edu.skku.woongjin_ai_winter.Package_4_1_NationBook.Select_Book.ReadScript.SelectType.QuizType.QuizOXShortwordTypeInfo;
import edu.skku.woongjin_ai_winter.R;

/*
from SelectTypeActivity
질문 타입별 예시 보기 페이지
 */

public class TemplateActivity extends AppCompatActivity {

    Intent intent, intentHome, intentSelectType;
    String id, scriptnm, backgroundID, thisWeek, nickname;
    TextView oxT, choiceT, shortwordT;
    ImageView imageHome;
    Button buttonGoBack;
    public DatabaseReference mPostReference;
    ArrayList<QuizOXShortwordTypeInfo> quizListOX, quizListShortword;
    ArrayList<QuizChoiceTypeInfo> quizListChoice;
    int minLikeQuiz1 = 0, cnt1 = 0, minLikeQuiz2 = 0, cnt2 = 0, minLikeQuiz3 = 0, cnt3 = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template);

        //TODO 디자인

        imageHome = (ImageView) findViewById(R.id.home);
        oxT = (TextView) findViewById(R.id.quiz_ox_template);
        choiceT = (TextView) findViewById(R.id.quiz_choice_template);
        shortwordT = (TextView) findViewById(R.id.quiz_shortword_template);
        buttonGoBack = (Button) findViewById(R.id.goBack);

        quizListOX = new ArrayList<QuizOXShortwordTypeInfo>();
        quizListChoice = new ArrayList<QuizChoiceTypeInfo>();
        quizListShortword = new ArrayList<QuizOXShortwordTypeInfo>();

        quizListOX.clear();
        quizListChoice.clear();
        quizListShortword.clear();

        intent = getIntent();
        id = intent.getStringExtra("id");
        scriptnm = intent.getStringExtra("scriptnm");
        backgroundID = intent.getStringExtra("background");
        thisWeek = intent.getStringExtra("thisWeek");
        nickname = intent.getStringExtra("nickname");

        mPostReference = FirebaseDatabase.getInstance().getReference();

        final Random generator = new Random();

        // 질문 타입 고르기 페이지로 돌아가기 버튼 이벤트
        buttonGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentSelectType = new Intent(TemplateActivity.this, SelectTypeActivity.class);
                intentSelectType.putExtra("id", id);
                intentSelectType.putExtra("scriptnm", scriptnm);
                intentSelectType.putExtra("background", backgroundID);
                intentSelectType.putExtra("thisWeek", thisWeek);
                intentSelectType.putExtra("nickname", nickname);
                startActivity(intentSelectType);
            }
        });

        // 메인 페이지 버튼 이벤트
        imageHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentHome = new Intent(TemplateActivity.this, MainActivity.class);
                intentHome.putExtra("id", id);
                startActivity(intentHome);
                intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
            }
        });

        // 보여줄 타입별 예시 질문 고르기
        mPostReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.child("quiz_list/" + scriptnm).getChildren()) {
                    String type = snapshot.child("type").getValue().toString();
                    if(type.equals("1")) {
                        QuizOXShortwordTypeInfo getNew = snapshot.getValue(QuizOXShortwordTypeInfo.class);

                        if(cnt1 < 3) {
                            quizListOX.add(getNew);
                            for(QuizOXShortwordTypeInfo findMinLike : quizListOX) {
                                int findMin = Integer.parseInt(findMinLike.like);
                                int minLike = Integer.parseInt(quizListOX.get(minLikeQuiz1).like);
                                if(findMin < minLike) minLikeQuiz1 = quizListOX.indexOf(findMinLike);
                            }
                            cnt1++;
                        } else {
                            String getLike = getNew.like;
                            int minLike = Integer.parseInt(quizListOX.get(minLikeQuiz1).like);
                            if(minLike < Integer.parseInt(getLike)) {
                                quizListOX.remove(minLikeQuiz1);
                                quizListOX.add(minLikeQuiz1, getNew);
                                for(QuizOXShortwordTypeInfo findMinLike : quizListOX) {
                                    int findMin = Integer.parseInt(findMinLike.like);
                                    int minLikeNew = Integer.parseInt(quizListOX.get(minLikeQuiz1).like);
                                    if(findMin < minLikeNew) minLikeQuiz1 = quizListOX.indexOf(findMinLike);
                                }
                            }
                        }
                    } else if(type.equals("2")) {
                        QuizChoiceTypeInfo getNew = snapshot.getValue(QuizChoiceTypeInfo.class);

                        if(cnt2 < 3) {
                            quizListChoice.add(getNew);
                            for(QuizChoiceTypeInfo findMinLike : quizListChoice) {
                                int findMin = Integer.parseInt(findMinLike.like);
                                int minLike = Integer.parseInt(quizListChoice.get(minLikeQuiz2).like);
                                if(findMin < minLike) minLikeQuiz2 = quizListChoice.indexOf(findMinLike);
                            }
                            cnt2++;
                        } else {
                            String getLike = getNew.like;
                            int minLike = Integer.parseInt(quizListChoice.get(minLikeQuiz2).like);
                            if(minLike < Integer.parseInt(getLike)) {
                                quizListChoice.remove(minLikeQuiz2);
                                quizListChoice.add(minLikeQuiz2, getNew);
                                for(QuizChoiceTypeInfo findMinLike : quizListChoice) {
                                    int findMin = Integer.parseInt(findMinLike.like);
                                    int minLikeNew = Integer.parseInt(quizListChoice.get(minLikeQuiz2).like);
                                    if(findMin < minLikeNew) minLikeQuiz2 = quizListChoice.indexOf(findMinLike);
                                }
                            }
                        }
                    } else if(type.equals("3")) {
                        QuizOXShortwordTypeInfo getNew = snapshot.getValue(QuizOXShortwordTypeInfo.class);

                        if(cnt3 < 3) {
                            quizListShortword.add(getNew);
                            for(QuizOXShortwordTypeInfo findMinLike : quizListShortword) {
                                int findMin = Integer.parseInt(findMinLike.like);
                                int minLike = Integer.parseInt(quizListShortword.get(minLikeQuiz3).like);
                                if(findMin < minLike) minLikeQuiz3 = quizListShortword.indexOf(findMinLike);
                            }
                            cnt3++;
                        } else {
                            String getLike = getNew.like;
                            int minLike = Integer.parseInt(quizListShortword.get(minLikeQuiz3).like);
                            if(minLike < Integer.parseInt(getLike)) {
                                quizListShortword.remove(minLikeQuiz3);
                                quizListShortword.add(minLikeQuiz3, getNew);
                                for(QuizOXShortwordTypeInfo findMinLike : quizListShortword) {
                                    int findMin = Integer.parseInt(findMinLike.like);
                                    int minLikeNew = Integer.parseInt(quizListShortword.get(minLikeQuiz3).like);
                                    if(findMin < minLikeNew) minLikeQuiz3 = quizListShortword.indexOf(findMinLike);
                                }
                            }
                        }
                    }
                }

                int bound, rand;
                String post;
                if(cnt1 == 0) {
                    post = "데이터 없음";
                    oxT.setText(post);
                } else {
                    bound = 3;
                    if(cnt1 < 3) bound = cnt1;
                    rand = generator.nextInt(bound);
                    QuizOXShortwordTypeInfo post1 = quizListOX.get(rand);
                    post = "Q. " + post1.question + "\nA. " + post1.answer + "\n힌트: " + post1.desc + "\n레벨: " + post1.star;
                    oxT.setText(post);
                }

                if(cnt2 == 0) {
                    post = "데이터 없음";
                    choiceT.setText(post);
                } else {
                    bound = 3;
                    if(cnt2 < 3) bound = cnt2;
                    rand = generator.nextInt(bound);
                    QuizChoiceTypeInfo post2 = quizListChoice.get(rand);
                    post = "Q. " + post2.question + "\nA1. " + post2.answer1 + " A2. " + post2.answer2 + " A3. " + post2.answer3 + " A4. " + post2.answer4 + "\nA. " + post2.answer + "\n힌트: " + post2.desc + "\n레벨: " + post2.star;
                    choiceT.setText(post);
                }

                if(cnt3 == 0) {
                    post = "데이터 없음";
                    shortwordT.setText(post);
                } else {
                    bound = 3;
                    if(cnt3 < 3) bound = cnt3;
                    rand = generator.nextInt(bound);
                    QuizOXShortwordTypeInfo post3 = quizListShortword.get(rand);
                    post = "Q. " + post3.question + "\nA. " + post3.answer + "\n힌트: " + post3.desc + "\n레벨: " + post3.star;
                    shortwordT.setText(post);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        });
    }
}
