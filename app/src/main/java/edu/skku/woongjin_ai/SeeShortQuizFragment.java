package edu.skku.woongjin_ai;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SeeShortQuizFragment extends Fragment implements  ShowWhoLikedFragment.OnFragmentInteractionListener{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private SeeShortQuizFragment.OnFragmentInteractionListener mListener;

    String id, scriptnm, question, answer, uid, star, like, hint, key, ans = "";
    String mine_or_like;
    int cnt;
    float starFloat;
    TextView Title, QuizContent, Answer;
    ImageView imageO, imageX, imageViewS2, imageViewS3, imageViewS4, imageViewS5;
    ImageButton imageButtonScript, imageButtonHint;
    Button wholike;
    ShowWhoLikedFragment showWhoLikedFragment;

    MyQuizActivity activity;

    public SeeShortQuizFragment() {

    }

    public static FriendOXQuizFragment newInstance(String param1, String param2) {
        FriendOXQuizFragment fragment = new FriendOXQuizFragment();
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
        final View view = inflater.inflate(R.layout.fragment_seequiz_shortword, container, false);
        final Context context = container.getContext();

        id = getArguments().getString("id");
        scriptnm = getArguments().getString("scriptnm");
        question = getArguments().getString("question");
        answer = getArguments().getString("answer");
        uid = getArguments().getString("uid");
        star = getArguments().getString("star");
        like = getArguments().getString("like");
        hint = getArguments().getString("desc");
        key = getArguments().getString("key");
        cnt = getArguments().getInt("cnt");
        mine_or_like = getArguments().getString("mine_or_like");
        showWhoLikedFragment=new ShowWhoLikedFragment();

        wholike=view.findViewById(R.id.fakeBT_likecnt_or_friendname);

        if(mine_or_like.equals("0"))//내문제보기
            wholike.setText(like+"명이 좋아했어요!");
        else//좋아요한문제보기
            wholike.setText(uid+"친구가 만든 문제!\n"+like+"명이 좋아했어요!");

        Title = view.findViewById(R.id.title);
        Answer=view.findViewById(R.id.answerContent);
        QuizContent = view.findViewById(R.id.quizContent);
        imageViewS2 = (ImageView) view.findViewById(R.id.star2);
        imageViewS3 = (ImageView) view.findViewById(R.id.star3);
        imageViewS4 = (ImageView) view.findViewById(R.id.star4);
        imageViewS5 = (ImageView) view.findViewById(R.id.star5);
        imageButtonScript = (ImageButton) view.findViewById(R.id.see_script);

        starFloat = Float.parseFloat(star);

        Title.setText(scriptnm);
        QuizContent.setText(question);
        Answer.setText(answer);

        //wholike눌렀을때 fragment..
        wholike.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager=getFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                if(mine_or_like.equals("0")){
                    activity.onFragmentChange(0, key, 1);
/*
                    fragmentTransaction.replace(R.id.contentFriendShortwordQuiz, ((MyQuizActivity)getActivity()).showWhoLikedFragment);
                    Bundle bundle=new Bundle(1);
                    bundle.putString("quizKey", key);
                    showWhoLikedFragment.setArguments(bundle);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    //이 코드 choice, short에도 적용하고 showwholikedfragment.java에서 파베에서 키값 이용해서 누가 좋아햇는지 어레이리스트에 넣어놧다가 커스텀 리스트뷰에 띄우기
                */
                }
            }
        });



        imageButtonScript.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                if(mine_or_like.equals("0")) {
                    fragmentTransaction.replace(R.id.contentShowScript, ((MyQuizActivity) getActivity()).showScriptFragment);

                    Bundle bundle = new Bundle(2);
                    bundle.putString("scriptnm", scriptnm);
                    bundle.putString("type", "myquiz");
                    ((MyQuizActivity) getActivity()).showScriptFragment.setArguments(bundle);
                    fragmentTransaction.commit();
                }
                else{
                    fragmentTransaction.replace(R.id.contentShowScript, ((LikeQuizActivity) getActivity()).showScriptFragment);

                    Bundle bundle = new Bundle(2);
                    bundle.putString("scriptnm", scriptnm);
                    bundle.putString("type", "likequiz");
                    ((LikeQuizActivity) getActivity()).showScriptFragment.setArguments(bundle);
                    fragmentTransaction.commit();
                }
            }
        });

        if(starFloat >= 1.5) {
            imageViewS2.setImageResource(R.drawable.ic_icons_difficulty_star_full);
            if(starFloat >= 2.5) {
                imageViewS3.setImageResource(R.drawable.ic_icons_difficulty_star_full);
                if(starFloat >= 3.5) {
                    imageViewS4.setImageResource(R.drawable.ic_icons_difficulty_star_full);
                    if(starFloat >= 4.5) {
                        imageViewS5.setImageResource(R.drawable.ic_icons_difficulty_star_full);
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
        activity=(MyQuizActivity) getActivity();
        if (context instanceof SeeShortQuizFragment.OnFragmentInteractionListener) {
            mListener = (SeeShortQuizFragment.OnFragmentInteractionListener) context;
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
