package edu.skku.woongjin_ai_winter.Package_WordList_notusing;

import android.graphics.drawable.Drawable;

/*
in WordListAdapter
 */

public class WordListItem {
    private Drawable icon;
    private String word;
    private Drawable imgLearn;
    private Drawable imgScript;

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Drawable getImgLearn() {
        return imgLearn;
    }

    public void setImgLearn(Drawable imgLearn) {
        this.imgLearn = imgLearn;
    }

    public Drawable getImgScript() {
        return imgScript;
    }

    public void setImgScript(Drawable imgScript) {
        this.imgScript = imgScript;
    }
}
