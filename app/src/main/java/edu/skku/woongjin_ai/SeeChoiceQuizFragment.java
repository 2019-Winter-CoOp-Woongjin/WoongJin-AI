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

public class SeeChoiceQuizFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private SeeChoiceQuizFragment.OnFragmentInteractionListener mListener;

    String id, scriptnm, question, answer, answer1, answer2, answer3, answer4, uid, star, like, hint, key, ans = "";
    String mine_or_like;
    int cnt, flagAO = 0, flagAX = 0;
    float starFloat;
    TextView Title, QuizContent;
    ImageView imageO, imageX, imageViewS2, imageViewS3, imageViewS4, imageViewS5;
    ImageButton imageButtonScript, imageButtonHint;
    Button imageButtonCheck, wholike;
    TextView textViewAns1, textViewAns2, textViewAns3, textViewAns4;

    public SeeChoiceQuizFragment() {

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
        final View view = inflater.inflate(R.layout.fragment_seequiz_choice, container, false);
        final Context context = container.getContext();

        id = getArguments().getString("id");
        mine_or_like = getArguments().getString("mine_or_like");
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


        wholike=view.findViewById(R.id.fakeBT_likecnt_or_friendname);

        if(mine_or_like.equals("0"))//내문제보기
            wholike.setText(like+"명이 좋아했어요!");
        else//좋아요한문제보기
            wholike.setText(uid+"친구가 만든 문제!\n"+like+"명이 좋아했어요!");

        Title = view.findViewById(R.id.title);
        QuizContent = view.findViewById(R.id.quizContent);
        imageViewS2 = (ImageView) view.findViewById(R.id.star2);
        imageViewS3 = (ImageView) view.findViewById(R.id.star3);
        imageViewS4 = (ImageView) view.findViewById(R.id.star4);
        imageViewS5 = (ImageView) view.findViewById(R.id.star5);
        imageButtonScript = (ImageButton) view.findViewById(R.id.see_script);
        textViewAns1 = (TextView) view.findViewById(R.id.first);
        textViewAns2 = (TextView) view.findViewById(R.id.second);
        textViewAns3 = (TextView) view.findViewById(R.id.third);
        textViewAns4 = (TextView) view.findViewById(R.id.fourth);

        textViewAns1.setText(answer1);
        textViewAns2.setText(answer2);
        textViewAns3.setText(answer3);
        textViewAns4.setText(answer4);

        starFloat = Float.parseFloat(star);

        Title.setText(scriptnm);
        QuizContent.setText(question);

        //wholike눌렀을때 fragment..


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




        if(answer.equals(answer1))
            textViewAns1.setBackgroundResource(R.drawable.ic_icons_selector_correct);
        else if(answer.equals(answer2))
            textViewAns2.setBackgroundResource(R.drawable.ic_icons_selector_correct);
        else if(answer.equals(answer3))
            textViewAns3.setBackgroundResource(R.drawable.ic_icons_selector_correct);
        else
            textViewAns4.setBackgroundResource(R.drawable.ic_icons_selector_correct);


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
        if (context instanceof SeeChoiceQuizFragment.OnFragmentInteractionListener) {
            mListener = (SeeChoiceQuizFragment.OnFragmentInteractionListener) context;
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
