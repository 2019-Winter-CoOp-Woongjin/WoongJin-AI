package edu.skku.woongjin_ai;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class TemplateActivity extends AppCompatActivity {

    Intent intent, intentHome;
    String id, scriptnm;
    TextView oxT, choiceT, shortwordT, scriptnmT;
    ImageView imageHome;
    public DatabaseReference mPostReference;
    ArrayList<QuizOXShortwordTypeInfo> quizListOX, quizListShortword;
    ArrayList<QuizChoiceTypeInfo> quizListChoice;
    int minLikeQuiz1 = 0, cnt1 = 0, minLikeQuiz2 = 0, cnt2 = 0, minLikeQuiz3 = 0, cnt3 = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template);

        imageHome = (ImageView) findViewById(R.id.home);
        oxT = (TextView) findViewById(R.id.quiz_ox_template);
        choiceT = (TextView) findViewById(R.id.quiz_choice_template);
        shortwordT = (TextView) findViewById(R.id.quiz_shortword_template);
        scriptnmT = (TextView) findViewById(R.id.scriptnm);

        quizListOX = new ArrayList<QuizOXShortwordTypeInfo>();
        quizListChoice = new ArrayList<QuizChoiceTypeInfo>();
        quizListShortword = new ArrayList<QuizOXShortwordTypeInfo>();

        quizListOX.clear();
        quizListChoice.clear();
        quizListShortword.clear();

        intent = getIntent();
        id = intent.getStringExtra("id");
        scriptnm = intent.getStringExtra("scriptnm");
        scriptnmT.setText("제목:  " + scriptnm);

        mPostReference = FirebaseDatabase.getInstance().getReference();

        final Random generator = new Random();

        imageHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentHome = new Intent(TemplateActivity.this, MainActivity.class);
                intentHome.putExtra("id", id);
                startActivity(intentHome);
                finish();
            }
        });

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

//                for(DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
//                    for(DataSnapshot snapshot2 : snapshot1.getChildren()) {
//                        QuizOXShortwordTypeInfo getNew = snapshot2.getValue(QuizOXShortwordTypeInfo.class);
//
//                        if(cnt1 < 3) {
//                            quizListOX.add(getNew);
//                            for(QuizOXShortwordTypeInfo findMinLike : quizListOX) {
//                                int findMin = Integer.parseInt(findMinLike.like);
//                                int minLike = Integer.parseInt(quizListOX.get(minLikeQuiz1).like);
//                                if(findMin < minLike) minLikeQuiz1 = quizListOX.indexOf(findMinLike);
//                            }
//                            cnt1++;
//                        } else {
//                            String getLike = getNew.like;
//                            int minLike = Integer.parseInt(quizListOX.get(minLikeQuiz1).like);
//                            if(minLike < Integer.parseInt(getLike)) {
//                                quizListOX.remove(minLikeQuiz1);
//                                quizListOX.add(minLikeQuiz1, getNew);
//                                for(QuizOXShortwordTypeInfo findMinLike : quizListOX) {
//                                    int findMin = Integer.parseInt(findMinLike.like);
//                                    int minLikeNew = Integer.parseInt(quizListOX.get(minLikeQuiz1).like);
//                                    if(findMin < minLikeNew) minLikeQuiz1 = quizListOX.indexOf(findMinLike);
//                                }
//                            }
//                        }
//                    }
//
//                    for(DataSnapshot snapshot2 : snapshot1.getChildren()) {
//                        QuizChoiceTypeInfo getNew = snapshot2.getValue(QuizChoiceTypeInfo.class);
//
//                        if(cnt2 < 3) {
//                            quizListChoice.add(getNew);
//                            for(QuizChoiceTypeInfo findMinLike : quizListChoice) {
//                                int findMin = Integer.parseInt(findMinLike.like);
//                                int minLike = Integer.parseInt(quizListChoice.get(minLikeQuiz2).like);
//                                if(findMin < minLike) minLikeQuiz2 = quizListChoice.indexOf(findMinLike);
//                            }
//                            cnt2++;
//                        } else {
//                            String getLike = getNew.like;
//                            int minLike = Integer.parseInt(quizListChoice.get(minLikeQuiz2).like);
//                            if(minLike < Integer.parseInt(getLike)) {
//                                quizListChoice.remove(minLikeQuiz2);
//                                quizListChoice.add(minLikeQuiz2, getNew);
//                                for(QuizChoiceTypeInfo findMinLike : quizListChoice) {
//                                    int findMin = Integer.parseInt(findMinLike.like);
//                                    int minLikeNew = Integer.parseInt(quizListChoice.get(minLikeQuiz2).like);
//                                    if(findMin < minLikeNew) minLikeQuiz2 = quizListChoice.indexOf(findMinLike);
//                                }
//                            }
//                        }
//                    }
//
//                    for(DataSnapshot snapshot2 : snapshot1.getChildren()) {
//                        QuizOXShortwordTypeInfo getNew = snapshot2.getValue(QuizOXShortwordTypeInfo.class);
//
//                        if(cnt3 < 3) {
//                            quizListShortword.add(getNew);
//                            for(QuizOXShortwordTypeInfo findMinLike : quizListShortword) {
//                                int findMin = Integer.parseInt(findMinLike.like);
//                                int minLike = Integer.parseInt(quizListShortword.get(minLikeQuiz3).like);
//                                if(findMin < minLike) minLikeQuiz3 = quizListShortword.indexOf(findMinLike);
//                            }
//                            cnt3++;
//                        } else {
//                            String getLike = getNew.like;
//                            int minLike = Integer.parseInt(quizListShortword.get(minLikeQuiz3).like);
//                            if(minLike < Integer.parseInt(getLike)) {
//                                quizListShortword.remove(minLikeQuiz3);
//                                quizListShortword.add(minLikeQuiz3, getNew);
//                                for(QuizOXShortwordTypeInfo findMinLike : quizListShortword) {
//                                    int findMin = Integer.parseInt(findMinLike.like);
//                                    int minLikeNew = Integer.parseInt(quizListShortword.get(minLikeQuiz3).like);
//                                    if(findMin < minLikeNew) minLikeQuiz3 = quizListShortword.indexOf(findMinLike);
//                                }
//                            }
//                        }
//                    }
//
//                }

                int rand = generator.nextInt(3);
                QuizOXShortwordTypeInfo post1 = quizListOX.get(rand);
                String postS1 = " Q. " + post1.question + "\n A. " + post1.answer + "\n Desc: " + post1.desc + "\n Star: " + post1.star;
                oxT.setText(postS1);

                rand = generator.nextInt(3);
                QuizChoiceTypeInfo post2 = quizListChoice.get(rand);
                String postS2 = " Q. " + post2.question + "\n A1. " + post2.answer1 + " A2. " + post2.answer2 + " A3. " + post2.answer3 + " A4. " + post2.answer4 + "\n A. " + post2.answer + "\n Desc: " + post2.desc + "\n Star: " + post2.star;
                choiceT.setText(postS2);

                rand = generator.nextInt(3);
                QuizOXShortwordTypeInfo post3 = quizListShortword.get(rand);
                String postS3 = " Q. " + post3.question + "\n A. " + post3.answer + "\n Desc: " + post3.desc + "\n Star: " + post3.star;
                shortwordT.setText(postS3);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        });
    }
}
