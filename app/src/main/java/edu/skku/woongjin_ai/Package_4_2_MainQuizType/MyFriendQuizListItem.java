package edu.skku.woongjin_ai.Package_4_2_MainQuizType;

import android.graphics.drawable.Drawable;

/*
in MyFriendQuizListAdapter
 */

public class MyFriendQuizListItem {
    private String profile;
    private String user;
    private Drawable star2;
    private Drawable star3;
    private Drawable star4;
    private Drawable star5;
    private String bookName;
    private String scriptName;
    private String question;

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Drawable getStar2() {
        return star2;
    }

    public void setStar2(Drawable star2) {
        this.star2 = star2;
    }

    public Drawable getStar3() {
        return star3;
    }

    public void setStar3(Drawable star3) {
        this.star3 = star3;
    }

    public Drawable getStar4() {
        return star4;
    }

    public void setStar4(Drawable star4) {
        this.star4 = star4;
    }

    public Drawable getStar5() {
        return star5;
    }

    public void setStar5(Drawable star5) {
        this.star5 = star5;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getScriptName() {
        return scriptName;
    }

    public void setScriptName(String scriptName) {
        this.scriptName = scriptName;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
