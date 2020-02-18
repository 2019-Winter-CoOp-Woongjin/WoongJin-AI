package edu.skku.woongjin_ai.Package_4_5_ShowFriend;

public class ShowFriendListItem {
    private String faceFriend;
    private String nameFriend;
    private String gradeFriend;
    private String schoolFriend;
    private String friendid;
    private String friendnick;
    private Boolean onoffline;
    private Boolean visible_and_clickable;
    private Boolean msgv_c;

    public ShowFriendListItem() {}

    public ShowFriendListItem(String faceFriend, String nameFriend, String gradeFriend, String schoolFriend, String friendid, String friendnick, Boolean visible_and_clickable, Boolean v_c) {
        this.faceFriend = faceFriend;
        this.nameFriend = nameFriend;
        this.gradeFriend = gradeFriend;
        this.schoolFriend = schoolFriend;
        this.friendid=friendid;
        this.friendnick=friendnick;
        this.visible_and_clickable=visible_and_clickable;
        this.msgv_c = v_c;
    }


    public String getFaceFriend() {
        return faceFriend;
    }

    public void setFaceFriend(String faceFriend) {
        this.faceFriend = faceFriend;
    }

    public String getNameFriend() {
        return nameFriend;
    }

    public void setNameFriend(String nameFriend) {
        this.nameFriend = nameFriend;
    }

    public String getGradeFriend() {
        return gradeFriend;
    }

    public void setGradeFriend(String gradeFriend) {
        this.gradeFriend = gradeFriend;
    }

    public String getSchoolFriend() {
        return schoolFriend;
    }

    public void setSchoolFriend(String schoolFriend) {
        this.schoolFriend = schoolFriend;
    }

    public Boolean getOnoffline() {
        return onoffline;
    }

    public void setOnoffline(Boolean status) {
        this.onoffline = status;
    }

    public String getFriendid() {
        return friendid;
    }

    public void setFriendid(String friendid) {
        this.friendid = friendid;
    }

    public String getFriendnick() {
        return friendnick;
    }

    public void setFriendnick(String friendnick) {
        this.friendnick = friendnick;
    }

    public Boolean getVisible_and_clickable() {
        return visible_and_clickable;
    }

    public void setVisible_and_clickable(Boolean visible_and_clickable) {
        this.visible_and_clickable = visible_and_clickable;
    }

    public Boolean getmsgv_c() {
        return msgv_c; }

    public void setMsgv_c(Boolean visible_and_clickable) {
        this.msgv_c = visible_and_clickable; }

    /*

    public String getMyid() {
        return myid;
    }

    public void setMyid(String id){
        this.myid=id;
    }

    */
}
