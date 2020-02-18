package edu.skku.woongjin_ai.Package_4_2_MainQuizType;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import edu.skku.woongjin_ai.Package_4_2_MainQuizType.NationQuiz.NationQuizActivity;
import edu.skku.woongjin_ai.Package_3_Main.MainActivity;
import edu.skku.woongjin_ai.R;

/*
from MainActivity
메인페이지에서 퀴즈나라 선택 후 질문 만들기/친구 지문 보기 선택하기
 */

public class MainQuizTypeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    Button meButton, friendButton;
    Intent intentMakeQuiz, intentFriendQuiz;
    String id, nickname, thisWeek;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;


    private MainQuizTypeFragment.OnFragmentInteractionListener mListener;

    public MainQuizTypeFragment() {

    }

    public static MainQuizTypeFragment newInstance(String param1, String param2) {
        MainQuizTypeFragment fragment = new MainQuizTypeFragment();
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
        final View view = inflater.inflate(R.layout.fragment_mainquiztype, container, false);
        final Context context = container.getContext();

        id = getArguments().getString("id");
        nickname = getArguments().getString("nickname");
        thisWeek = getArguments().getString("thisWeek");

        meButton = (Button) view.findViewById(R.id.me);
        friendButton = (Button) view.findViewById(R.id.friend);

        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        // 문제 만들기 버튼 이벤트
        meButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //질문 만들기로 ㄱㄱ
                intentMakeQuiz = new Intent(getActivity(), NationQuizActivity.class);
                intentMakeQuiz.putExtra("id", id);
                intentMakeQuiz.putExtra("nickname", nickname);
                intentMakeQuiz.putExtra("thisWeek", thisWeek);

                fragmentTransaction.remove(((MainActivity)getActivity()).mainQuizTypeFragment);
                fragmentTransaction.commit();
                startActivity(intentMakeQuiz);
            }
        });

        // 친구 문제 풀기 버튼 이벤트
        friendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentFriendQuiz = new Intent(getActivity(), ShowFriendQuizActivity.class);
                intentFriendQuiz.putExtra("id", id);
                intentFriendQuiz.putExtra("nickname", nickname);
                intentFriendQuiz.putExtra("thisWeek", thisWeek);

                fragmentTransaction.remove(((MainActivity)getActivity()).mainQuizTypeFragment);
                fragmentTransaction.commit();
                startActivity(intentFriendQuiz);
            }
        });

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
        if (context instanceof MainQuizTypeFragment.OnFragmentInteractionListener) {
            mListener = (MainQuizTypeFragment.OnFragmentInteractionListener) context;
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
