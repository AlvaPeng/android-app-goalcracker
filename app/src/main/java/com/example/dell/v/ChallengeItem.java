package com.example.dell.v;

public class ChallengeItem {
    private String challengeName;
    private String[] challengeContent;
    private int duration;
    private int iconId;
    private int challengeBG;

    //精简版
    public ChallengeItem(String challengeName, int duration, int iconId, int challengeBG){
        this.challengeName = challengeName;
        this.duration = duration;
        this.iconId = iconId;
        this.challengeBG = challengeBG;
    }
    public String getChallengeName() {
        return challengeName;
    }

    public void setChallengeName(String challengeName) {
        this.challengeName = challengeName;
    }

    public String[] getChallengeContent() {
        return challengeContent;
    }

    public void setChallengeContent(String[] challengeContent) {
        this.challengeContent = challengeContent;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public int getIconId() {
        return iconId;
    }

    public void setChallengeBG(int challengeBG) {
        this.challengeBG = challengeBG;
    }

    public int getChallengeBG() {
        return challengeBG;
    }
}
