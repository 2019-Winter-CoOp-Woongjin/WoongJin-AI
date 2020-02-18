package edu.skku.woongjin_ai_winter.Package_WordList_notusing;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.skku.woongjin_ai_winter.R;

/*
in WordListAdapter
 */

public class WordListAdapter extends BaseAdapter {

    private ArrayList<WordListItem> wordListItems = new ArrayList<WordListItem>();

    @Override
    public int getCount() {
        return wordListItems.size();
    }

    @Override
    public WordListItem getItem(int position) {
        return wordListItems.get(position);
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
            convertView = inflater.inflate(R.layout.listviewcustom_wordlist, parent, false);
        }

        ImageView imageViewIcon = (ImageView) convertView.findViewById(R.id.iconWordList);
        TextView textViewWord = (TextView) convertView.findViewById(R.id.wordWordList);
        ImageButton imageButtonLearn = (ImageButton) convertView.findViewById(R.id.learnWordList);
        ImageButton imageButtonScript = (ImageButton) convertView.findViewById(R.id.scriptWordList);

        WordListItem wordListItem = getItem(position);

        imageViewIcon.setImageDrawable(wordListItem.getIcon());
        textViewWord.setText(wordListItem.getWord());
        imageButtonLearn.setImageDrawable(wordListItem.getImgLearn());
        imageButtonScript.setImageDrawable(wordListItem.getImgScript());

        return convertView;
    }

    public void addItem(Drawable icon, String word, Drawable imgLearn, Drawable imgScript) {
        WordListItem myItem = new WordListItem();
        myItem.setIcon(icon);
        myItem.setWord(word);
        myItem.setImgLearn(imgLearn);
        myItem.setImgScript(imgScript);
        wordListItems.add(myItem);
    }
}
