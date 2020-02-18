package edu.skku.woongjin_ai.Package_4_1_NationBook.Select_Book.ReadScript;

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
import android.widget.ImageView;

import edu.skku.woongjin_ai.R;

/*
from ReadScriptActivity
공부 타입 고르기 - 소리내어 읽기/표시하며 읽기
 */

public class SelectStudyTypeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    private SelectStudyTypeFragment.OnFragmentInteractionListener mListener;

    public SelectStudyTypeFragment() {

    }

    public static SelectStudyTypeFragment newInstance(String param1, String param2) {
        SelectStudyTypeFragment fragment = new SelectStudyTypeFragment();
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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_selectstudytype, container, false);
        final Context context = container.getContext();

        ImageView imageSpeak = (ImageView) view.findViewById(R.id.speak);
        ImageView imageWrite = (ImageView) view.findViewById(R.id.write);

//        String id = getArguments().getString("userID");
//        String scriptnm = getArguments().getString("scriptnm");
//        String backgroundID = getArguments().getString("backgroundID");
//        Intent intentReadScript = new Intent(context, SelectStudyTypeFragment.class);

        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        imageSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onStudyTypeInfoSet("speak");
                fragmentTransaction.remove(((ReadScriptActivity)getActivity()).selectStudyTypeFragment);
                fragmentTransaction.commit();
            }
        });

        imageWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onStudyTypeInfoSet("write");
                fragmentTransaction.remove(((ReadScriptActivity)getActivity()).selectStudyTypeFragment);
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
        if (context instanceof SelectStudyTypeFragment.OnFragmentInteractionListener) {
            mListener = (SelectStudyTypeFragment.OnFragmentInteractionListener) context;
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
        void onStudyTypeInfoSet(String type);
    }
}
