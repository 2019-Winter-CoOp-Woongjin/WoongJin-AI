package edu.skku.woongjin_ai.Package_4_4_MyPage.MyCoinRecord;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.skku.woongjin_ai.Package_3_Main.MainActivity;
import edu.skku.woongjin_ai.R;

public class MyCoinRecordActivity extends AppCompatActivity {

    public DatabaseReference mPostReference;
    Button goback;
    Intent intent, intentGoHome;
    ListView coinlist;
    String id, uname, ugrade, ucoin;
    TextView username, usergrade, usercoin;
    CoinRecordListAdapter coinRecordListAdapter=new CoinRecordListAdapter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycoinrecord);

        intent = getIntent();
        id = intent.getStringExtra("id");
        uname = intent.getStringExtra("name");
        ugrade = intent.getStringExtra("grade");
        ucoin = intent.getStringExtra("coin");

        mPostReference = FirebaseDatabase.getInstance().getReference();

        ImageButton homeButton = (ImageButton) findViewById(R.id.home);
        coinlist=(ListView)findViewById(R.id.coinlist);
        username=(TextView)findViewById(R.id.userName1);
        usergrade=(TextView)findViewById(R.id.userGrade1);
        usercoin=(TextView)findViewById(R.id.userCoin);
        goback=(Button)findViewById(R.id.goback);
        username.setText(uname);
        usergrade.setText(ugrade);
        usercoin.setText(ucoin);

        getFirebaseDatabaseMyCoinList();

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentGoHome = new Intent(MyCoinRecordActivity.this, MainActivity.class);
                intentGoHome.putExtra("id", id);
                intentGoHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentGoHome);
            }
        });

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void getFirebaseDatabaseMyCoinList() {
        mPostReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String howmany, why, date;
                DataSnapshot snapshot1=dataSnapshot.child("user_list/"+id+"/my_coin_list");
                for(DataSnapshot snapshot2:snapshot1.getChildren()) {
                    date = snapshot2.getKey();
                    howmany = snapshot2.child("get").getValue().toString();
                    why = snapshot2.child("why").getValue().toString();
                    coinRecordListAdapter.addItem(howmany, date, why);
                }
                coinlist.setAdapter(coinRecordListAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
