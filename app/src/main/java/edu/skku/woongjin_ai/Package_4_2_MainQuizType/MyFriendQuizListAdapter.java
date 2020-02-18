package edu.skku.woongjin_ai_winter.Package_4_2_MainQuizType;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import edu.skku.woongjin_ai_winter.R;

/*
in ShowFriendQuizActivity
 */

public class MyFriendQuizListAdapter extends BaseAdapter {

    private ArrayList<MyFriendQuizListItem> myFriendQuizListItems = new ArrayList<MyFriendQuizListItem>();

    FirebaseStorage storage;
    private StorageReference storageReference, dataReference;

    @Override
    public int getCount() {
        return myFriendQuizListItems.size();
    }

    @Override
    public MyFriendQuizListItem getItem(int position) {
        return myFriendQuizListItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listviewcustom_myfriendquizlist, parent, false);
        }

        ImageView profile = (ImageView) convertView.findViewById(R.id.profile);
        TextView user = (TextView) convertView.findViewById(R.id.user);
        ImageView star2 = (ImageView) convertView.findViewById(R.id.star2);
        ImageView star3 = (ImageView) convertView.findViewById(R.id.star3);
        ImageView star4 = (ImageView) convertView.findViewById(R.id.star4);
        ImageView star5 = (ImageView) convertView.findViewById(R.id.star5);
        TextView bookName = (TextView) convertView.findViewById(R.id.bookName);
        TextView scriptName = (TextView) convertView.findViewById(R.id.scriptName);
        TextView question = (TextView) convertView.findViewById(R.id.question);

        user.setMovementMethod(new ScrollingMovementMethod());
        bookName.setMovementMethod(new ScrollingMovementMethod());
        scriptName.setMovementMethod(new ScrollingMovementMethod());
        question.setMovementMethod(new ScrollingMovementMethod());

        MyFriendQuizListItem myFriendQuizListItem = getItem(position);

        // 유저 프로필 사진 가져오기
        if(!myFriendQuizListItem.getProfile().equals("noimage")) {
            storage = FirebaseStorage.getInstance();
            storageReference = storage.getReference();
            dataReference = storageReference.child("/profile/" + myFriendQuizListItem.getProfile());
            dataReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.with(context)
                            .load(uri)
                            .error(R.drawable.btn_x)
                            .into(profile);
                }
            });
        }

        user.setText(myFriendQuizListItem.getUser()+"\n친구가 만들었어요!");
        star2.setImageDrawable(myFriendQuizListItem.getStar2());
        star3.setImageDrawable(myFriendQuizListItem.getStar3());
        star4.setImageDrawable(myFriendQuizListItem.getStar4());
        star5.setImageDrawable(myFriendQuizListItem.getStar5());
        bookName.setText(myFriendQuizListItem.getBookName());
        scriptName.setText(myFriendQuizListItem.getScriptName());
        question.setText(myFriendQuizListItem.getQuestion());

        return convertView;
    }

    public void addItem(String profile, String user, Drawable star2, Drawable star3, Drawable star4, Drawable star5, String bookName, String scriptName, String question) {
        MyFriendQuizListItem myFriendQuizListItem = new MyFriendQuizListItem();
        myFriendQuizListItem.setProfile(profile);
        myFriendQuizListItem.setUser(user);
        myFriendQuizListItem.setStar2(star2);
        myFriendQuizListItem.setStar3(star3);
        myFriendQuizListItem.setStar4(star4);
        myFriendQuizListItem.setStar5(star5);
        myFriendQuizListItem.setBookName(bookName);
        myFriendQuizListItem.setScriptName(scriptName);
        myFriendQuizListItem.setQuestion(question);
        myFriendQuizListItems.add(myFriendQuizListItem);
    }
}
