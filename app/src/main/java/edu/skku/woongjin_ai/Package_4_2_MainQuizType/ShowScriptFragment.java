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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.skku.woongjin_ai.Package_4_1_NationBook.Select_Book.ReadScript.SelectType.QuizType.ChoiceTypeActivity;
import edu.skku.woongjin_ai.Package_4_1_NationBook.Select_Book.ReadScript.SelectType.QuizType.OXTypeActivity;
import edu.skku.woongjin_ai.Package_4_1_NationBook.Select_Book.ReadScript.SelectType.QuizType.ShortwordTypeActivity;
import edu.skku.woongjin_ai.Package_4_3_GameList.MakeBombType.MakeBombChoiceActivity;
import edu.skku.woongjin_ai.Package_4_3_GameList.MakeBombType.MakeBombOXActivity;
import edu.skku.woongjin_ai.Package_4_3_GameList.MakeBombType.MakeBombShortwordActivity;
import edu.skku.woongjin_ai.Package_4_3_GameList.SolveBombType.SolveBombChoiceActivity;
import edu.skku.woongjin_ai.Package_4_3_GameList.SolveBombType.SolveBombOXActivity;
import edu.skku.woongjin_ai.Package_4_3_GameList.SolveBombType.SolveBombShortwordActivity;
import edu.skku.woongjin_ai.Package_4_4_MyPage.MyQuiz.MyQuizActivity;
import edu.skku.woongjin_ai.Package_4_4_MyPage.LikeQuiz.LikeQuizActivity;
import edu.skku.woongjin_ai.R;

/*
from OXTypeActivity, SelectTypeActivity, ShortwordActivity, ShowFriendQuizActivity
지문 보여주기
 */

public class ShowScriptFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    TextView textViewScript1, textViewScript2;
    String scriptnm, script, type;
    DatabaseReference mPostReference;

    private ShowScriptFragment.OnFragmentInteractionListener mListener;

    public ShowScriptFragment() {

    }

    public static ShowScriptFragment newInstance(String param1, String param2) {
        ShowScriptFragment fragment = new ShowScriptFragment();
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
        final View view = inflater.inflate(R.layout.fragment_showscript, container, false);
        final Context context = container.getContext();

        scriptnm = getArguments().getString("scriptnm");
        type = getArguments().getString("type");

        textViewScript1 = (TextView) view.findViewById(R.id.script1);
        textViewScript2 = (TextView) view.findViewById(R.id.script2);
        Button buttonClose = (Button) view.findViewById(R.id.close);

        mPostReference = FirebaseDatabase.getInstance().getReference();

        textViewScript1.setMovementMethod(new ScrollingMovementMethod());
        textViewScript2.setMovementMethod(new ScrollingMovementMethod());

        // 닫기 버튼 이벤트
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                if(type.equals("ox")) {
                    fragmentTransaction.remove(((OXTypeActivity)getActivity()).showScriptFragment);
                    fragmentTransaction.commit();
                } else if(type.equals("choice")) {
                    fragmentTransaction.remove(((ChoiceTypeActivity)getActivity()).showScriptFragment);
                    fragmentTransaction.commit();
                } else if(type.equals("shortword")) {
                    fragmentTransaction.remove(((ShortwordTypeActivity)getActivity()).showScriptFragment);
                    fragmentTransaction.commit();
                } else if(type.equals("friend")) {
                    fragmentTransaction.remove(((ShowFriendQuizActivity)getActivity()).showScriptFragment);
                    fragmentTransaction.commit();
                } else if(type.equals("myquiz")) {
                    fragmentTransaction.remove(((MyQuizActivity)getActivity()).showScriptFragment);
                    fragmentTransaction.commit();
                } else if(type.equals("likequiz")) {
                    fragmentTransaction.remove(((LikeQuizActivity)getActivity()).showScriptFragment);
                    fragmentTransaction.commit();
                }
                else if(type.equals("solvebombchoice")) {
                    fragmentTransaction.remove(((SolveBombChoiceActivity)getActivity()).showScriptFragment);
                    fragmentTransaction.commit();
                }
                else if(type.equals("solvebombox")) {
                    fragmentTransaction.remove(((SolveBombOXActivity)getActivity()).showScriptFragment);
                    fragmentTransaction.commit();
                }
                else if(type.equals("solvebombshortword")) {
                    fragmentTransaction.remove(((SolveBombShortwordActivity)getActivity()).showScriptFragment);
                    fragmentTransaction.commit();
                }
                else if(type.equals("makebombchoice")) {
                    fragmentTransaction.remove(((MakeBombChoiceActivity)getActivity()).showScriptFragment);
                    fragmentTransaction.commit();
                }
                else if(type.equals("makebombox")) {
                    fragmentTransaction.remove(((MakeBombOXActivity)getActivity()).showScriptFragment);
                    fragmentTransaction.commit();
                }
                else if(type.equals("makebombshortword")) {
                    fragmentTransaction.remove(((MakeBombShortwordActivity)getActivity()).showScriptFragment);
                    fragmentTransaction.commit();
                }

            }
        });

        // 데이터베이스에서 지문 가져오기
        mPostReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                script = dataSnapshot.child("script_list/" + scriptnm + "/text").getValue().toString();
                String[] array=script.split("###");
                textViewScript1.setText(array[0]);
                textViewScript2.setText(array[1]);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
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
        if (context instanceof ShowScriptFragment.OnFragmentInteractionListener) {
            mListener = (ShowScriptFragment.OnFragmentInteractionListener) context;
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