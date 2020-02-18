package edu.skku.woongjin_ai.Package_4_2_MainQuizType;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import edu.skku.woongjin_ai.R;

/*
from ShowFriendQuizActivity
친구 문제 풀기 & 추천수 높은 문제 풀기에서 힌트 보기 선택시, 퀴즈 힌트가 글 힌트일 경우
*/
public class ShowHintFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private ShowHintFragment.OnFragmentInteractionListener mListener;

    String hint;

    public ShowHintFragment() {

    }

    public static ShowHintFragment newInstance(String param1, String param2) {
        ShowHintFragment fragment = new ShowHintFragment();
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
        final View view = inflater.inflate(R.layout.fragment_showhint, container, false);
        final Context context = container.getContext();

        hint = getArguments().getString("hint");

        TextView textViewHint = (TextView) view.findViewById(R.id.hintShowHint);
        Button buttonClose = (Button) view.findViewById(R.id.close);

        textViewHint.setMovementMethod(new ScrollingMovementMethod());
        textViewHint.setText(hint);

        // 닫기 버튼 이벤트
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.remove(((ShowFriendQuizActivity)getActivity()).showHintFragment);
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
        if (context instanceof ShowHintFragment.OnFragmentInteractionListener) {
            mListener = (ShowHintFragment.OnFragmentInteractionListener) context;
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
