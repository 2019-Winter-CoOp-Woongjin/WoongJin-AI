package edu.skku.woongjin_ai;

import android.Manifest;
import android.content.Context;
import android.graphics.Camera;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import java.io.IOException;

public class HintVideoFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    String type;
    ImageButton recordButton, watchButton;
    Camera camera;

    private HintVideoFragment.OnFragmentInteractionListener mListener;

    public HintVideoFragment() {

    }

    public static HintVideoFragment newInstance(String param1, String param2) {
        HintVideoFragment fragment = new HintVideoFragment();
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
        final View view = inflater.inflate(R.layout.fragment_hintvideo, container, false);
        final Context context = container.getContext();

        type = getArguments().getString("type");

        recordButton = (ImageButton) view.findViewById(R.id.record);
        watchButton = (ImageButton) view.findViewById(R.id.watchVideo);
        ImageButton goBackButton = (ImageButton) view.findViewById(R.id.goBack);
        camera = new Camera();

        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        watchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                if(type.equals("ox")) {
                    ((OXTypeActivity)getActivity()).checkButton.setImageResource(R.drawable.ic_icons_quiz_complete_inactivate);
                    ((OXTypeActivity)getActivity()).flagD = 0;
                    fragmentTransaction.remove(((OXTypeActivity)getActivity()).hintVideoFragment);
                    fragmentTransaction.commit();
                } else if(type.equals("choice")) {
                    ((ChoiceTypeActivity)getActivity()).checkButton.setImageResource(R.drawable.ic_icons_quiz_complete_inactivate);
                    ((ChoiceTypeActivity)getActivity()).flagD = 0;
                    fragmentTransaction.remove(((ChoiceTypeActivity)getActivity()).hintVideoFragment);
                    fragmentTransaction.commit();
                } else if(type.equals("shortword")) {
                    ((ShortwordTypeActivity)getActivity()).checkButton.setImageResource(R.drawable.ic_icons_quiz_complete_inactivate);
                    ((ShortwordTypeActivity)getActivity()).flagD = 0;
                    fragmentTransaction.remove(((ShortwordTypeActivity)getActivity()).hintVideoFragment);
                    fragmentTransaction.commit();
                }
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HintVideoFragment.OnFragmentInteractionListener) {
            mListener = (HintVideoFragment.OnFragmentInteractionListener) context;
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
