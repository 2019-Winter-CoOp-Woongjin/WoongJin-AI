package edu.skku.woongjin_ai.Package_4_2_MainQuizType.QuizType;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.skku.woongjin_ai.Package_4_2_MainQuizType.ShowFriendQuizActivity;
import edu.skku.woongjin_ai.R;
import edu.skku.woongjin_ai.Package_4_2_MainQuizType.ShowVideoHintActivity;

/*
from ShowFriendQuizActivity
질문나라 - 객관식 문제 풀기
 */

public class FriendChoiceQuizFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private FriendChoiceQuizFragment.OnFragmentInteractionListener mListener;

    String id, scriptnm, question, answer, answer1, answer2, answer3, answer4, uid, star, like, hint, key, ans = "", background, nickname, url;
    int cnt, flagA1 = 0, flagA2 = 0, flagA3 = 0, flagA4 = 0;
    float starFloat;
    Intent videoIntent;
    ImageView imageViewS2, imageViewS3, imageViewS4, imageViewS5;
    Button imageButtonScript, imageButtonHint, imageButtonCheck;
    TextView textViewAns1, textViewAns2, textViewAns3, textViewAns4;
    DatabaseReference mPostReference;

    public FriendChoiceQuizFragment() {

    }

    public static FriendChoiceQuizFragment newInstance(String param1, String param2) {
        FriendChoiceQuizFragment fragment = new FriendChoiceQuizFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_friendchoicequiz, container, false);
        final Context context = container.getContext();

        id = getArguments().getString("id");
        scriptnm = getArguments().getString("scriptnm");
        question = getArguments().getString("question");
        answer = getArguments().getString("answer");
        answer1 = getArguments().getString("answer1");
        answer2 = getArguments().getString("answer2");
        answer3 = getArguments().getString("answer3");
        answer4 = getArguments().getString("answer4");
        uid = getArguments().getString("uid");
        star = getArguments().getString("star");
        like = getArguments().getString("like");
        hint = getArguments().getString("desc");
        key = getArguments().getString("key");
        cnt = getArguments().getInt("cnt");
        background = getArguments().getString("background");
        nickname = getArguments().getString("nickname");
        url = getArguments().getString("url");
        TextView textViewUid = (TextView) view.findViewById(R.id.uidFriendChoice);
        TextView textViewName = (TextView) view.findViewById(R.id.nameFriendChoice);
        TextView textViewQuestion = (TextView) view.findViewById(R.id.questionFriendChoice);
        imageViewS2 = (ImageView) view.findViewById(R.id.star2);
        imageViewS3 = (ImageView) view.findViewById(R.id.star3);
        imageViewS4 = (ImageView) view.findViewById(R.id.star4);
        imageViewS5 = (ImageView) view.findViewById(R.id.star5);
        imageButtonScript = (Button) view.findViewById(R.id.scriptFriendChoice);
        imageButtonHint = (Button) view.findViewById(R.id.hintFriendChoice);
        imageButtonCheck = (Button) view.findViewById(R.id.checkFriendChoice);
        textViewAns1 = (TextView) view.findViewById(R.id.ans1);
        textViewAns2 = (TextView) view.findViewById(R.id.ans2);
        textViewAns3 = (TextView) view.findViewById(R.id.ans3);
        textViewAns4 = (TextView) view.findViewById(R.id.ans4);
        ConstraintLayout backgroundLayout = (ConstraintLayout) view.findViewById(R.id.backgroundchoice);

        mPostReference = FirebaseDatabase.getInstance().getReference();

        starFloat = Float.parseFloat(star);

        mPostReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String unickname = dataSnapshot.child("user_list/" + uid + "/nickname").getValue().toString();
                textViewUid.setText(unickname + " 친구가 낸 질문");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        });

        textViewName.setText(scriptnm);
        textViewQuestion.setText(question);
        textViewAns1.setText(answer1);
        textViewAns2.setText(answer2);
        textViewAns3.setText(answer3);
        textViewAns4.setText(answer4);

        if(background.equals("blue")) backgroundLayout.setBackgroundColor(Color.rgb(51, 153, 204));
        else if(background.equals("red")) backgroundLayout.setBackgroundColor(Color.rgb(255, 102, 102));

        textViewAns1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagA1 == 0) {
                    ans = answer1;
                    textViewAns1.setBackgroundColor(Color.rgb(255, 153, 0));
                    textViewAns2.setBackgroundColor(Color.rgb(255, 255, 255));
                    textViewAns3.setBackgroundColor(Color.rgb(255, 255, 255));
                    textViewAns4.setBackgroundColor(Color.rgb(255, 255, 255));
                    flagA1 = 1;
                    flagA2 = flagA3 = flagA4 = 0;
                } else {
                    ans = "";
                    textViewAns1.setBackgroundColor(Color.rgb(255, 255, 255));
                    flagA1 = 0;
                }
            }
        });

        textViewAns2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagA2 == 0){
                    ans = answer2;
                    textViewAns2.setBackgroundColor(Color.rgb(255, 153, 0));
                    textViewAns1.setBackgroundColor(Color.rgb(255, 255, 255));
                    textViewAns3.setBackgroundColor(Color.rgb(255, 255, 255));
                    textViewAns4.setBackgroundColor(Color.rgb(255, 255, 255));
                    flagA2 = 1;
                    flagA1 = flagA3 = flagA4 = 0;
                } else {
                    ans = "";
                    textViewAns2.setBackgroundColor(Color.rgb(255, 255, 255));
                    flagA2 = 0;
                }
            }
        });

        textViewAns3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagA3 == 0){
                    ans = answer3;
                    textViewAns3.setBackgroundColor(Color.rgb(255, 153, 0));
                    textViewAns2.setBackgroundColor(Color.rgb(255, 255, 255));
                    textViewAns1.setBackgroundColor(Color.rgb(255, 255, 255));
                    textViewAns4.setBackgroundColor(Color.rgb(255, 255, 255));
                    flagA3 = 1;
                    flagA2 = flagA1 = flagA4 = 0;
                } else {
                    ans = "";
                    textViewAns3.setBackgroundColor(Color.rgb(255, 255, 255));
                    flagA3 = 0;
                }
            }
        });

        textViewAns4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagA4 == 0){
                    ans = answer4;
                    textViewAns4.setBackgroundColor(Color.rgb(255, 153, 0));
                    textViewAns2.setBackgroundColor(Color.rgb(255, 255, 255));
                    textViewAns3.setBackgroundColor(Color.rgb(255, 255, 255));
                    textViewAns1.setBackgroundColor(Color.rgb(255, 255, 255));
                    flagA4 = 1;
                    flagA2 = flagA3 = flagA1 = 0;
                } else {
                    ans = "";
                    textViewAns4.setBackgroundColor(Color.rgb(255, 255, 255));
                    flagA4 = 0;
                }
            }
        });

        // 답 제출 버튼 이벤트
        imageButtonCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ans.equals("")) {
                    Toast.makeText(context, "정답을 선택하세요.", Toast.LENGTH_SHORT).show();
                } else { // 정답
                    if(ans.equals(answer)) {
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.contentShowScript, ((ShowFriendQuizActivity)getActivity()).correctFriendQuizFragment);
                        Bundle bundle = new Bundle(8);
                        bundle.putString("id", id);
                        bundle.putString("scriptnm", scriptnm);
                        bundle.putString("uid", uid);
                        bundle.putString("star", star);
                        bundle.putString("like", like);
                        bundle.putString("key", key);
                        bundle.putInt("cnt", cnt);
                        bundle.putString("nickname", nickname);
                        ((ShowFriendQuizActivity)getActivity()).correctFriendQuizFragment.setArguments(bundle);
                        fragmentTransaction.commit();
                    } else { // 틀린 답
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.contentShowScript, ((ShowFriendQuizActivity)getActivity()).wrongFriendQuizFragment);
                        Bundle bundle = new Bundle(1);
                        bundle.putString("nickname", nickname);
                        ((ShowFriendQuizActivity)getActivity()).wrongFriendQuizFragment.setArguments(bundle);
                        fragmentTransaction.commit();
                    }
                }
            }
        });

        // 힌트 보기 버튼 이벤트
        imageButtonHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                if (hint.equals("video")) { // 영상 힌트
                    videoIntent = new Intent(getActivity(), ShowVideoHintActivity.class);//fragment는 context타입이 아니기 떄문에 this쓸수 없기 떄문에 FriendShortwordQuizFragment.class하면 안됨
                    videoIntent.putExtra("url", url);
                    videoIntent.putExtra("scriptnm", scriptnm);
                    videoIntent.putExtra("key", key);
                    startActivity(videoIntent);
                } else { // 글 힌트 / 힌트 없음
                    fragmentTransaction.replace(R.id.contentShowHint, ((ShowFriendQuizActivity) getActivity()).showHintFragment);
                    Bundle bundle = new Bundle(1);
                    bundle.putString("hint", hint);
                    ((ShowFriendQuizActivity) getActivity()).showHintFragment.setArguments(bundle);
                    fragmentTransaction.commit();
                }
            }
        });

        // 지문 보기 버튼 이벤트
        imageButtonScript.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.contentShowScript, ((ShowFriendQuizActivity)getActivity()).showScriptFragment);
                Bundle bundle = new Bundle(2);
                bundle.putString("scriptnm", scriptnm);
                bundle.putString("type", "friend");
                ((ShowFriendQuizActivity)getActivity()).showScriptFragment.setArguments(bundle);
                fragmentTransaction.commit();
            }
        });

        // 난이도 표시
        if(starFloat >= 1.5) {
            imageViewS2.setImageResource(R.drawable.star_full);
            if(starFloat >= 2.5) {
                imageViewS3.setImageResource(R.drawable.star_full);
                if(starFloat >= 3.5) {
                    imageViewS4.setImageResource(R.drawable.star_full);
                    if(starFloat >= 4.5) {
                        imageViewS5.setImageResource(R.drawable.star_full);
                    }
                }
            }
        }

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FriendChoiceQuizFragment.OnFragmentInteractionListener) {
            mListener = (FriendChoiceQuizFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
