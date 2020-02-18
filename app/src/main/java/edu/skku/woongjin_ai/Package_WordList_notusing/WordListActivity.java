package edu.skku.woongjin_ai.Package_WordList_notusing;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import edu.skku.woongjin_ai.Package_3_Main.MainActivity;
import edu.skku.woongjin_ai.R;
import edu.skku.woongjin_ai.Package_4_1_NationBook.Select_Book.ReadScript.SelectType.SelectTypeActivity;

/*
from ReadScriptActivity
단어장, 단어 공부하기 페이지(구현중)
 */

public class WordListActivity extends AppCompatActivity{
    //implements ShowScriptFragment.OnFragmentInteractionListener

    DatabaseReference mPostReference;
    Intent intent, intentHome, intentQuiz;
    String id, scriptnm, backgroundID;
    ImageView backgroundImage;
    ImageButton scriptButton;
    Button makeQuizButton;
    FirebaseStorage storage;
    ListView wordListView;
    ArrayList<String> wordArrayList;
    WordListAdapter wordListAdapter;
    //Fragment showScriptFragment;

    private StorageReference storageReference, dataReference;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wordlist);

        intent = getIntent();
        id = intent.getStringExtra("id");
        scriptnm = intent.getStringExtra("scriptnm");
        backgroundID = intent.getStringExtra("background");

        wordListView = (ListView)findViewById(R.id.wordlist);
        ImageView imageHome = (ImageView) findViewById(R.id.home);
        TextView title = (TextView) findViewById(R.id.title);
        backgroundImage = (ImageView) findViewById(R.id.background);
        scriptButton = (ImageButton) findViewById(R.id.script);
        makeQuizButton = (Button) findViewById(R.id.makeQuiz);

        wordArrayList = new ArrayList<String>();
        wordListAdapter = new WordListAdapter();

        title.setText("지문 제목: " + scriptnm);
        //showScriptFragment = new ShowScriptFragment();

        mPostReference = FirebaseDatabase.getInstance().getReference();
        getFirebaseDatabaseWordList();
        //dataSetting();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getInstance().getReference();
        dataReference = storageReference.child("/scripts_background/" + backgroundID);
        dataReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(WordListActivity.this)
                        .load(uri)
                        .placeholder(R.drawable.bot)
                        .error(R.drawable.btn_x)
                        .into(backgroundImage);
                backgroundImage.setAlpha(0.5f);
            }
        });

        makeQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentQuiz = new Intent(WordListActivity.this, SelectTypeActivity.class);
                intentQuiz.putExtra("id", id);
                intentQuiz.putExtra("scriptnm", scriptnm);
                intentQuiz.putExtra("background", backgroundID);
                startActivity(intentQuiz);
            }
        });

//        scriptButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.wordlistFragment, showScriptFragment);
//                Bundle bundle = new Bundle(2);
//                bundle.putString("scriptnm", scriptnm);
//                showScriptFragment.setArguments(bundle);
//                transaction.addToBackStack(null);
//                transaction.commit();
//            }
//        });

        imageHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentHome = new Intent(WordListActivity.this, MainActivity.class);
                intentHome.putExtra("id", id);
                startActivity(intentHome);
            }
        });
    }

    private void getFirebaseDatabaseWordList(){
        mPostReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                wordArrayList.clear();

                for(DataSnapshot snapshot : dataSnapshot.child("user_list/" + id + "/my_script_list/" + scriptnm + "/word_list").getChildren()) {
                    String key = snapshot.getKey();
                    wordArrayList.add(key);
                }

                for(int i=0; i<wordArrayList.size(); i++) {
                    wordListAdapter.addItem(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_icons_pen_hand), wordArrayList.get(i), ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_icons_learn), ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_icons_script));
                }
                wordListView.setAdapter(wordListAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        });
    }
/*
    private void dataSetting() {

        WordListAdapter wAdapter = new WordListAdapter();
        for(int i = 0 ; i < wordArraylist.size(); i++) {
            Log.d("요기요8", wordArraylist.get(0));
            wAdapter.addItem(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_icons_pen_hand), wordArraylist.get(i), ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_icons_learn), ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_icons_script));
        }
        mListView.setAdapter(wAdapter);
    }
    */
/*
    public void onFragmentInteraction(Uri uri) {

    }
*/
}