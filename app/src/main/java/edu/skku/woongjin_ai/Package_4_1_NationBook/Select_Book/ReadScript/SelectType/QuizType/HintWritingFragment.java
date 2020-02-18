package edu.skku.woongjin_ai_winter.Package_4_1_NationBook.Select_Book.ReadScript.SelectType.QuizType;

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
import android.widget.EditText;

import edu.skku.woongjin_ai_winter.R;

/*
from OXTypeActivity, ChoiceTypeActivity, ShortwordTypeActivity
글 힌트 주기
 */

public class HintWritingFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private HintWritingFragment.OnFragmentInteractionListener mListener;

    public EditText editTextHint;
    public String desc = "", type;

    public HintWritingFragment() {

    }

    public static HintWritingFragment newInstance(String param1, String param2) {
        HintWritingFragment fragment = new HintWritingFragment();
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
        final View view = inflater.inflate(R.layout.fragment_hintwriting, container, false);
        final Context context = container.getContext();

        type = getArguments().getString("type");

        editTextHint = (EditText) view.findViewById(R.id.hint);
        Button goBackButton = (Button) view.findViewById(R.id.goBack);

        // 다른 힌트 선택하기 버튼 이벤트
        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                if(type.equals("ox")) {
                    ((OXTypeActivity)getActivity()).flagD = 0;
                    fragmentTransaction.remove(((OXTypeActivity)getActivity()).hintWritingFragment);
                    fragmentTransaction.commit();
                } else if(type.equals("choice")) {
                    ((ChoiceTypeActivity)getActivity()).flagD = 0;
                    fragmentTransaction.remove(((ChoiceTypeActivity)getActivity()).hintWritingFragment);
                    fragmentTransaction.commit();
                } else if(type.equals("shortword")) {
                    ((ShortwordTypeActivity)getActivity()).flagD = 0;
                    fragmentTransaction.remove(((ShortwordTypeActivity)getActivity()).hintWritingFragment);
                    fragmentTransaction.commit();
                }
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
        if (context instanceof HintWritingFragment.OnFragmentInteractionListener) {
            mListener = (HintWritingFragment.OnFragmentInteractionListener) context;
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
