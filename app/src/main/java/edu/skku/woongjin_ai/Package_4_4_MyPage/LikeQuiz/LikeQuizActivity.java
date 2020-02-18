package edu.skku.woongjin_ai.Package_4_4_MyPage.LikeQuiz;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.skku.woongjin_ai.Package_3_Main.MainActivity;
import edu.skku.woongjin_ai.Package_4_4_MyPage.Fragment.SeeChoiceQuizFragment;
import edu.skku.woongjin_ai.Package_4_4_MyPage.Fragment.SeeOXQuizFragment;
import edu.skku.woongjin_ai.Package_4_4_MyPage.Fragment.SeeShortQuizFragment;
import edu.skku.woongjin_ai.Package_4_1_NationBook.Select_Book.ReadScript.SelectType.QuizType.QuizChoiceTypeInfo;
import edu.skku.woongjin_ai.Package_4_1_NationBook.Select_Book.ReadScript.SelectType.QuizType.QuizOXShortwordTypeInfo;
import edu.skku.woongjin_ai.R;
import edu.skku.woongjin_ai.Package_4_2_MainQuizType.ShowScriptFragment;

import static android.media.CamcorderProfile.get;

public class LikeQuizActivity extends AppCompatActivity implements SeeOXQuizFragment.OnFragmentInteractionListener, SeeChoiceQuizFragment.OnFragmentInteractionListener, SeeShortQuizFragment.OnFragmentInteractionListener, ShowScriptFragment.OnFragmentInteractionListener {

    public DatabaseReference mPostReference;
    Intent intent, intentHome, intentUpdate;
    String id;
    TextView instruction;
    ListView quizlist;
    Button goback;
    ArrayList<QuizChoiceTypeInfo> myChoiceList;
    ArrayList<QuizOXShortwordTypeInfo> myOXList, myShortList;
    ArrayList<String> LikedKey;
    //MyFriendQuizListAdapter myQuizListAdapter;
    LikeQuizListAdapter likeQuizListAdapter;
    int flag=0;
    public SeeOXQuizFragment OXFragment;
    public SeeChoiceQuizFragment ChoiceFragment;
    public SeeShortQuizFragment ShortFragment;
    public ShowScriptFragment showScriptFragment;
    int cntOX, cntChoice, cntShort;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seequestion_likeormine);

        ImageView imageHome = (ImageView) findViewById(R.id.home);
        quizlist = (ListView) findViewById(R.id.quizlist);
        instruction=(TextView)findViewById(R.id.instruction);
        goback=(Button)findViewById(R.id.goback);


        intent = getIntent();
        id = intent.getStringExtra("id");

        OXFragment=new SeeOXQuizFragment();
        ChoiceFragment=new SeeChoiceQuizFragment();
        ShortFragment=new SeeShortQuizFragment();

        showScriptFragment=new ShowScriptFragment();

        mPostReference = FirebaseDatabase.getInstance().getReference();

        //myQuizListAdapter = new MyFriendQuizListAdapter();
        likeQuizListAdapter=new LikeQuizListAdapter();

        myChoiceList=new ArrayList<QuizChoiceTypeInfo>();
        myOXList=new ArrayList<QuizOXShortwordTypeInfo>();
        myShortList=new ArrayList<QuizOXShortwordTypeInfo>();

        LikedKey=new ArrayList<String>();

        intentUpdate = new Intent(LikeQuizActivity.this, LikeQuizActivity.class);
        intentUpdate.putExtra("id", id);
        ////

        instruction.setText("좋아요를 누른 문제들이야!\n문제를 클릭하면 자세히 볼 수 있어");

        getFirebaseDatabaseMyQuizList();

        imageHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentHome = new Intent(LikeQuizActivity.this, MainActivity.class);
                intentHome.putExtra("id", id);
                intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentHome);
                finish();
            }
        });

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        quizlist.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long i) {
                if(flag == 1) {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.remove(OXFragment);
                    fragmentTransaction.commit();
                    OXFragment = new SeeOXQuizFragment();
                } else if(flag == 2) {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.remove(ChoiceFragment);
                    fragmentTransaction.commit();
                    ChoiceFragment = new SeeChoiceQuizFragment();
                } else if(flag == 3) {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.remove(ShortFragment);
                    fragmentTransaction.commit();
                    ShortFragment = new SeeShortQuizFragment();
                }

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                if(position < cntOX) {
                    flag = 1;
                    QuizOXShortwordTypeInfo quiz = myOXList.get(position);

                    transaction.replace(R.id.seequiz_fragment, OXFragment);
                    Bundle bundle = new Bundle(11);
                    bundle.putString("id", id);
                    bundle.putString("mine_or_like", "1");
                    bundle.putString("scriptnm", quiz.scriptnm);
                    bundle.putString("question", quiz.question);
                    bundle.putString("answer", quiz.answer);
                    bundle.putString("uid", quiz.uid);
                    bundle.putString("star", quiz.star);
                    bundle.putString("like", quiz.like);
                    bundle.putString("desc", quiz.desc);
                    bundle.putString("key", quiz.key);
                    bundle.putInt("cnt", quiz.cnt);
                    OXFragment.setArguments(bundle);
                    transaction.commit();
                } else {
                    position -= cntOX;
                    if(position < cntChoice) {
                        flag = 2;
                        QuizChoiceTypeInfo quiz = myChoiceList.get(position);

                        transaction.replace(R.id.seequiz_fragment, ChoiceFragment);
                        Bundle bundle = new Bundle(15);
                        bundle.putString("id", id);
                        bundle.putString("mine_or_like", "1");
                        bundle.putString("scriptnm", quiz.scriptnm);
                        bundle.putString("question", quiz.question);
                        bundle.putString("answer", quiz.answer);
                        bundle.putString("answer1", quiz.answer1);
                        bundle.putString("answer2", quiz.answer2);
                        bundle.putString("answer3", quiz.answer3);
                        bundle.putString("answer4", quiz.answer4);
                        bundle.putString("uid", quiz.uid);
                        bundle.putString("star", quiz.star);
                        bundle.putString("like", quiz.like);
                        bundle.putString("desc", quiz.desc);
                        bundle.putString("key", quiz.key);
                        bundle.putInt("cnt", quiz.cnt);
                        ChoiceFragment.setArguments(bundle);
                        transaction.commit();
                    } else {
                        position -= cntChoice;
                        flag = 3;
                        QuizOXShortwordTypeInfo quiz = myShortList.get(position);

                        transaction.replace(R.id.seequiz_fragment, ShortFragment);
                        Bundle bundle = new Bundle(11);
                        bundle.putString("id", id);
                        bundle.putString("mine_or_like", "1");
                        bundle.putString("scriptnm", quiz.scriptnm);
                        bundle.putString("question", quiz.question);
                        bundle.putString("answer", quiz.answer);
                        bundle.putString("uid", quiz.uid);
                        bundle.putString("star", quiz.star);
                        bundle.putString("like", quiz.like);
                        bundle.putString("desc", quiz.desc);
                        bundle.putString("key", quiz.key);
                        bundle.putInt("cnt", quiz.cnt);
                        ShortFragment.setArguments(bundle);
                        transaction.commit();
                    }
                }
            }
        });

    }

    private void getFirebaseDatabaseMyQuizList() {
        mPostReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myOXList.clear();
                myChoiceList.clear();
                myShortList.clear();
                LikedKey.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    String key=snapshot.getKey();
                    if(key.equals("user_list")){
                        DataSnapshot snapshot1 = snapshot.child(id).child("my_script_list");
                        for(DataSnapshot snapshot2:snapshot1.getChildren()){ //지문
                            DataSnapshot snapshot3 = snapshot2.child("liked_list");
                            for(DataSnapshot snapshot4: snapshot3.getChildren())
                                LikedKey.add(snapshot4.getKey());
                        }

                    }
                }

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String key = snapshot.getKey();
                    if (key.equals("quiz_list")) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) { //script
                            String scriptTitle = snapshot1.getKey();
                            for (DataSnapshot snapshot2 : snapshot1.getChildren()) { // inside-script
                                String question_key = snapshot2.getKey();
                                for(String liked_key:LikedKey) {
                                    if (question_key.equals(liked_key)) {
                                        String type = snapshot2.child("type").getValue().toString();
                                        if (type.equals("1")) {
                                            QuizOXShortwordTypeInfo quiz = snapshot2.getValue(QuizOXShortwordTypeInfo.class);
                                            quiz.scriptnm = scriptTitle;
                                            myOXList.add(quiz);
                                        } else if (type.equals("2")) {
                                            QuizChoiceTypeInfo quiz = snapshot2.getValue(QuizChoiceTypeInfo.class);
                                            quiz.scriptnm = scriptTitle;
                                            myChoiceList.add(quiz);
                                        } else {
                                            QuizOXShortwordTypeInfo quiz = snapshot2.getValue(QuizOXShortwordTypeInfo.class);
                                            quiz.scriptnm = scriptTitle;
                                            myShortList.add(quiz);
                                        }
                                        //

                                    }
                                }
                            }
                        }

                        cntOX = myOXList.size();
                        cntChoice = myChoiceList.size();
                        cntShort = myShortList.size();
                        //(String likeCnt, String uid, Drawable star2, Drawable star3, Drawable star4, Drawable star5,
                        // String bookName, String scriptName, String question)
                        for(int i=0; i<myOXList.size(); i++) {
                            QuizOXShortwordTypeInfo quizinfo=myOXList.get(i);
                            float stars=Float.parseFloat(quizinfo.star);
                            if(stars < 1.5)
                                likeQuizListAdapter.addItem(quizinfo.like, quizinfo.uid, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), quizinfo.book_name, quizinfo.scriptnm, quizinfo.question);
                            else if (stars >= 1.5 && stars < 2.5)
                                likeQuizListAdapter.addItem(quizinfo.like, quizinfo.uid, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), quizinfo.book_name, quizinfo.scriptnm, quizinfo.question);
                            else if (stars >= 2.5 && stars < 3.5)
                                likeQuizListAdapter.addItem(quizinfo.like, quizinfo.uid, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), quizinfo.book_name, quizinfo.scriptnm, quizinfo.question);
                            else if (stars >= 3.5 && stars < 4.5)
                                likeQuizListAdapter.addItem(quizinfo.like, quizinfo.uid, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), quizinfo.book_name, quizinfo.scriptnm, quizinfo.question);
                            else
                                likeQuizListAdapter.addItem(quizinfo.like, quizinfo.uid, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), quizinfo.book_name, quizinfo.scriptnm, quizinfo.question);
                        }
                        for(int i=0; i<myChoiceList.size(); i++){
                            QuizChoiceTypeInfo quizinfo=myChoiceList.get(i);
                            float stars=Float.parseFloat(quizinfo.star);
                            if(stars < 1.5)
                                likeQuizListAdapter.addItem(quizinfo.like, quizinfo.uid, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), quizinfo.book_name, quizinfo.scriptnm, quizinfo.question);
                            else if (stars >= 1.5 && stars < 2.5)
                                likeQuizListAdapter.addItem(quizinfo.like, quizinfo.uid, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), quizinfo.book_name, quizinfo.scriptnm, quizinfo.question);
                            else if (stars >= 2.5 && stars < 3.5)
                                likeQuizListAdapter.addItem(quizinfo.like, quizinfo.uid, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), quizinfo.book_name, quizinfo.scriptnm, quizinfo.question);
                            else if (stars >= 3.5 && stars < 4.5)
                                likeQuizListAdapter.addItem(quizinfo.like, quizinfo.uid, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), quizinfo.book_name, quizinfo.scriptnm, quizinfo.question);
                            else
                                likeQuizListAdapter.addItem(quizinfo.like, quizinfo.uid, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), quizinfo.book_name, quizinfo.scriptnm, quizinfo.question);

                        }
                        for(int i=0; i<myShortList.size(); i++){
                            QuizOXShortwordTypeInfo quizinfo=myShortList.get(i);
                            float stars=Float.parseFloat(quizinfo.star);
                            if(stars < 1.5)
                                likeQuizListAdapter.addItem(quizinfo.like, quizinfo.uid, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), quizinfo.book_name, quizinfo.scriptnm, quizinfo.question);
                            else if (stars >= 1.5 && stars < 2.5)
                                likeQuizListAdapter.addItem(quizinfo.like, quizinfo.uid, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), quizinfo.book_name, quizinfo.scriptnm, quizinfo.question);
                            else if (stars >= 2.5 && stars < 3.5)
                                likeQuizListAdapter.addItem(quizinfo.like, quizinfo.uid, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), quizinfo.book_name, quizinfo.scriptnm, quizinfo.question);
                            else if (stars >= 3.5 && stars < 4.5)
                                likeQuizListAdapter.addItem(quizinfo.like, quizinfo.uid, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_empty), quizinfo.book_name, quizinfo.scriptnm, quizinfo.question);
                            else
                                likeQuizListAdapter.addItem(quizinfo.like, quizinfo.uid, ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_full), quizinfo.book_name, quizinfo.scriptnm, quizinfo.question);
                        }
                        quizlist.setAdapter(likeQuizListAdapter);
                    }
                    //final ValueEventListener quiz_list = mPostReference.child("quiz_list").addValueEventListener(postListener);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}