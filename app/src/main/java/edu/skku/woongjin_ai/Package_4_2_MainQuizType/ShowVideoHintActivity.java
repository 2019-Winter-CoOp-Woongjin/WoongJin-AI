package edu.skku.woongjin_ai_winter.Package_4_2_MainQuizType;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import edu.skku.woongjin_ai_winter.R;

public class ShowVideoHintActivity extends AppCompatActivity {

    public DatabaseReference mPostReference;
    private StorageReference storageReference, dataReference;
    Intent intent;
    Button closeButton;
    String url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showvideohint);

        closeButton = (Button)findViewById(R.id.close);
        intent = getIntent();
        url = intent.getStringExtra("url"); //비디오 이름 받기
        VideoView video = (VideoView)findViewById(R.id.showVideoHint);
        MediaController mediaController = new MediaController(this); //미디어 플레리어를 제어

        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getInstance().getReference();
        dataReference = storageReference.child("/video/" + url);
        dataReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                video.setVideoURI(uri);
                mediaController.setAnchorView(video);
                //mediaController.setPadding(0, 0, 0, 80); //상위 레이어의 바닥에서 패딩을 줌
                video.setMediaController(mediaController);
                video.requestFocus();
                video.start();
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}