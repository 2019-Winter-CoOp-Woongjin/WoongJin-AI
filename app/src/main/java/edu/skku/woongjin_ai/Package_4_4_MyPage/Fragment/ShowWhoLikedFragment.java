package edu.skku.woongjin_ai_winter.Package_4_4_MyPage.Fragment;

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
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import edu.skku.woongjin_ai_winter.Package_4_4_MyPage.MyQuiz.MyQuizActivity;
import edu.skku.woongjin_ai_winter.Package_4_5_ShowFriend.ShowFriendListAdapter;
import edu.skku.woongjin_ai_winter.R;
import edu.skku.woongjin_ai_winter.UserInfo;

public class ShowWhoLikedFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    ListView likedfriends;
    ArrayList<UserInfo> Uinfos;

    DatabaseReference mPostReference;

    MyQuizActivity activity;

    ShowFriendListAdapter showFriendListAdapter;

    ImageButton close;

    private ShowWhoLikedFragment.OnFragmentInteractionListener mListener;

    public ShowWhoLikedFragment(){

    }

    public static ShowWhoLikedFragment newInstance(String param1, String param2) {
        ShowWhoLikedFragment fragment = new ShowWhoLikedFragment();
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
        final View view = inflater.inflate(R.layout.fragment_wholiked, container, false);
        final Context context = container.getContext();

        //int backType=getArguments().getInt("type");
        //String key=getArguments().getString("quizKey");
        //showFriendListAdapter=activity.getFirebaseDatabaseUserList(key);
        //likedfriends=(ListView)view.findViewById(R.id.wholiked_list);
        //Uinfos=new ArrayList<UserInfo>();
        close=(ImageButton)view.findViewById(R.id.close);




        //getFirebaseDatabaseUserInfo();
        mPostReference=FirebaseDatabase.getInstance().getReference();

        //likedfriends.setAdapter(showFriendListAdapter);


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.remove(((MyQuizActivity)getActivity()).showWhoLikedFragment);
                fragmentTransaction.commit();
                //activity.onFragmentChange(backType, "noKey", 0);
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
        activity=(MyQuizActivity)getActivity();
        if (context instanceof ShowWhoLikedFragment.OnFragmentInteractionListener) {
            mListener = (ShowWhoLikedFragment.OnFragmentInteractionListener) context;
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

    /*
    private void getFirebaseDatabaseUserInfo() {
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
    }*/

}
