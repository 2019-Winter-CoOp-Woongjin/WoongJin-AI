package edu.skku.woongjin_ai_winter.Package_4_2_MainQuizType.Correct_Wrong_Quiz;

import android.content.Context;
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
import android.widget.TextView;

import edu.skku.woongjin_ai_winter.Package_4_2_MainQuizType.ShowFriendQuizActivity;
import edu.skku.woongjin_ai_winter.R;

/*
from ShowFriendQuizActivity
친구 문제 풀기 틀렸을 경우
 */

public class WrongFriendQuizFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private WrongFriendQuizFragment.OnFragmentInteractionListener mListener;

    String nickname;
    Button imageButtonClose;

    public WrongFriendQuizFragment() {

    }

    public static WrongFriendQuizFragment newInstance(String param1, String param2) {
        WrongFriendQuizFragment fragment = new WrongFriendQuizFragment();
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
        final View view = inflater.inflate(R.layout.fragment_wrongfriendquiz, container, false);
        final Context context = container.getContext();

        nickname = getArguments().getString("nickname");

        TextView textViewWrong = (TextView) view.findViewById(R.id.textWrong);
        imageButtonClose = (Button) view.findViewById(R.id.close);

        textViewWrong.setText("다시 한 번 생각해보자~\n" + nickname + ", 할 수 있어!");

        // 닫기 버튼 이벤트
        imageButtonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.remove(((ShowFriendQuizActivity)getActivity()).wrongFriendQuizFragment);
                fragmentTransaction.commit();
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
        if (context instanceof WrongFriendQuizFragment.OnFragmentInteractionListener) {
            mListener = (WrongFriendQuizFragment.OnFragmentInteractionListener) context;
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
