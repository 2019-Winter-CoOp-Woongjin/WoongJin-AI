package edu.skku.woongjin_ai.Package_4_4_MyPage.LikeQuiz;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.skku.woongjin_ai.R;

/*
in ShowFriendQuizActivity
 */

public class LikeQuizListAdapter extends BaseAdapter {

    private ArrayList<LikeQuizListItem> likeQuizListItems = new ArrayList<LikeQuizListItem>();

    @Override
    public int getCount() {
        return likeQuizListItems.size();
    }

    @Override
    public LikeQuizListItem getItem(int position) {
        return likeQuizListItems.get(position);
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
            convertView = inflater.inflate(R.layout.listviewcustom_likequizlist, parent, false);
        }

        TextView likeCnt = (TextView) convertView.findViewById(R.id.like);
        TextView uid = (TextView) convertView.findViewById(R.id.uid);
        ImageView star2 = (ImageView) convertView.findViewById(R.id.star2);
        ImageView star3 = (ImageView) convertView.findViewById(R.id.star3);
        ImageView star4 = (ImageView) convertView.findViewById(R.id.star4);
        ImageView star5 = (ImageView) convertView.findViewById(R.id.star5);
        TextView bookName = (TextView) convertView.findViewById(R.id.bookName);
        TextView scriptName = (TextView) convertView.findViewById(R.id.scriptName);
        TextView question = (TextView) convertView.findViewById(R.id.question);

        uid.setMovementMethod(new ScrollingMovementMethod());
        bookName.setMovementMethod(new ScrollingMovementMethod());
        scriptName.setMovementMethod(new ScrollingMovementMethod());
        question.setMovementMethod(new ScrollingMovementMethod());

        LikeQuizListItem likeQuizListItem = getItem(position);

        likeCnt.setText(likeQuizListItem.getLikeCnt());
        uid.setText(likeQuizListItem.getUid()+"\n친구가 만들었어요!");
        star2.setImageDrawable(likeQuizListItem.getStar2());
        star3.setImageDrawable(likeQuizListItem.getStar3());
        star4.setImageDrawable(likeQuizListItem.getStar4());
        star5.setImageDrawable(likeQuizListItem.getStar5());
        bookName.setText(likeQuizListItem.getBookName());
        scriptName.setText(likeQuizListItem.getScriptName());
        question.setText(likeQuizListItem.getQuestion());

        return convertView;
    }

    public void addItem(String likeCnt, String uid, Drawable star2, Drawable star3, Drawable star4, Drawable star5, String bookName, String scriptName, String question) {
        LikeQuizListItem likeQuizListItem = new LikeQuizListItem();
        likeQuizListItem.setLikeCnt(likeCnt);
        likeQuizListItem.setUid(uid);
        likeQuizListItem.setStar2(star2);
        likeQuizListItem.setStar3(star3);
        likeQuizListItem.setStar4(star4);
        likeQuizListItem.setStar5(star5);
        likeQuizListItem.setBookName(bookName);
        likeQuizListItem.setScriptName(scriptName);
        likeQuizListItem.setQuestion(question);
        likeQuizListItems.add(likeQuizListItem);
    }
}
