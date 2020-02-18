package edu.skku.woongjin_ai_winter.Package_4_5_ShowFriend;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import edu.skku.woongjin_ai_winter.R;

public class ShowFriendListAdapter extends BaseAdapter {

    FirebaseStorage storage;
    private StorageReference storageReference, dataReference;
    private DatabaseReference mPostReference;
    private FirebaseDatabase database;

    SharedPreferences WhoAmI;

    String myid, myschool, mygrade, mynickname, myname, myprofile, whatcase, mystatus;
    //Boolean status, mystatus;

    private ArrayList<ShowFriendListItem> showFriendListItems = new ArrayList<ShowFriendListItem>();

    @Override
    public int getCount() {
        return showFriendListItems.size();
    }

    @Override
    public ShowFriendListItem getItem(int position) {
        return showFriendListItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();

        WhoAmI=context.getSharedPreferences("myinfo", Context.MODE_PRIVATE);

        mPostReference = FirebaseDatabase.getInstance().getReference().child("user_list");

        myid=WhoAmI.getString("myid", "nil");
        mynickname=WhoAmI.getString("mynick", "nil");
        mygrade=WhoAmI.getString("mygrade", "nil");
        myprofile=WhoAmI.getString("myprofile", "nil");
        myname=WhoAmI.getString("myname", "nil");
        myschool=WhoAmI.getString("myschool", "nil");
        //mystatus=WhoAmI.getBoolean("onoffline",true);

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listviewcustom_showfriendlist, parent, false);
        }

        ImageView imageViewFace = (ImageView) convertView.findViewById(R.id.friendFace);
        TextView textViewName = (TextView) convertView.findViewById(R.id.friendName);
        TextView textViewGrade = (TextView) convertView.findViewById(R.id.friendGrade);
        TextView textViewSchool = (TextView) convertView.findViewById(R.id.friendSchool);
        Button addFriend = (Button) convertView.findViewById(R.id.addrecommendfriend);
        //Button msg = (Button) convertView.findViewById(R.id.messaging_btn);

        ImageView image = (ImageView) convertView.findViewById(R.id.status_check);



//안바뀐다. 리스트의 각 아이템을 나타내는걸 찾기.
        //채팅 따로



        ShowFriendListItem showFriendListItem = getItem(position);


        if(showFriendListItem.getFriendid()!=null) {
            mPostReference.child(showFriendListItem.getFriendid()).child("onoffline").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    Boolean status = dataSnapshot.getValue(Boolean.class);  //boolean 타입으로 디비 읽기.
                    if (status.equals(true)) {
                        image.setImageResource(R.drawable.on_status);
                        //Log.e("스테이터스on", status.toString() + showFriendListItem.getFriendid());
                    } else {
                        image.setImageResource(R.drawable.off_status);
                        //Log.e("스테이터스off", status.toString()+showFriendListItem.getFriendid());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        // status 바뀔때 마다 친구목록 사진이 갱신되는 문제 발생. 아직 별로 안중요.


        if(showFriendListItem.getVisible_and_clickable()){
            addFriend.setClickable(true);
            addFriend.setVisibility(View.VISIBLE);
        }
        /* 리스트 뷰 아이템에 메시지가 있었을때.
        if(showFriendListItem.getmsgv_c()){
            msg.setClickable(true);
            msg.setVisibility(View.VISIBLE);
        }
        */
        if (!showFriendListItem.getFaceFriend().equals("noimage")) {
            storage = FirebaseStorage.getInstance();
            storageReference = storage.getInstance().getReference();
            dataReference = storageReference.child("profile/" + showFriendListItem.getFaceFriend());
            dataReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.with(context)
                            .load(uri)
                            .error(R.drawable.btn_x)
                            .into(imageViewFace);
                }
            });
        }
        textViewName.setText(showFriendListItem.getFriendnick());
        textViewGrade.setText(showFriendListItem.getGradeFriend()+"학년");
        textViewSchool.setText(showFriendListItem.getSchoolFriend());

        addFriend.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mPostReference.child(myid+"/my_friend_list/").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int flag =0;

                            for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                flag =0;
                                if(snapshot.getKey().equals(showFriendListItem.getFriendid()))
                                {
                                    Toast.makeText(context, "이미 친구입니다.", Toast.LENGTH_SHORT).show();
                                    flag=1;
                                    break;
                                }
                            }

                            if(flag==0){

                                //친구추가 파베
                                mPostReference.child(myid+"/my_friend_list/"+showFriendListItem.getFriendid()+"/grade").setValue(showFriendListItem.getGradeFriend());
                                mPostReference.child(myid+"/my_friend_list/"+showFriendListItem.getFriendid()+"/name").setValue(showFriendListItem.getNameFriend());
                                mPostReference.child(myid+"/my_friend_list/"+showFriendListItem.getFriendid()+"/nickname").setValue(showFriendListItem.getFriendnick());
                                mPostReference.child(myid+"/my_friend_list/"+showFriendListItem.getFriendid()+"/profile").setValue(showFriendListItem.getFaceFriend());
                                mPostReference.child(myid+"/my_friend_list/"+showFriendListItem.getFriendid()+"/school").setValue(showFriendListItem.getSchoolFriend());
                                //mPostReference.child(myid+"/my_friend_list/"+showFriendListItem.getFriendid()+"/onoffline").setValue(showFriendListItem.getOnoffline());


                                mPostReference.child(showFriendListItem.getFriendid()+"/my_friend_list/"+myid+"/grade").setValue(mygrade);
                                mPostReference.child(showFriendListItem.getFriendid()+"/my_friend_list/"+myid+"/name").setValue(myname);
                                mPostReference.child(showFriendListItem.getFriendid()+"/my_friend_list/"+myid+"/nickname").setValue(mynickname);
                                mPostReference.child(showFriendListItem.getFriendid()+"/my_friend_list/"+myid+"/profile").setValue(myprofile);
                                mPostReference.child(showFriendListItem.getFriendid()+"/my_friend_list/"+myid+"/school").setValue(myschool);
                                //mPostReference.child(showFriendListItem.getFriendid()+"/my_friend_list/"+myid+"/onoffline").setValue(true);


                                //토스트 띄우기
                                Toast.makeText(context, "친구 추가 되었습니다.", Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

        });
/*
        msg.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "메시지.", Toast.LENGTH_SHORT).show();

            }
            //인텐트 후 채팅창으로 넘어가기.

        });
*/
        return convertView;

    }



    public void addItem(String faceFriend, String nameFriend, String gradeFriend, String schoolFriend, String friendid, String friendnick, Boolean v_and_c, Boolean msgv_c, Boolean Status) {
        ShowFriendListItem showFriendListItem = new ShowFriendListItem();

        showFriendListItem.setFaceFriend(faceFriend);
        showFriendListItem.setNameFriend(nameFriend);
        showFriendListItem.setGradeFriend(gradeFriend);
        showFriendListItem.setSchoolFriend(schoolFriend);
        showFriendListItem.setFriendid(friendid);
        showFriendListItem.setFriendnick(friendnick);
        showFriendListItem.setVisible_and_clickable(v_and_c);
        showFriendListItem.setMsgv_c(msgv_c);
        showFriendListItem.setOnoffline(Status);
        showFriendListItems.add(showFriendListItem);
    }


}

