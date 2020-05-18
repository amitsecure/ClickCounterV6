package com.example.clickcounterv6.model;

public class Counter {
    private String strCount;
    private String strTopic;
    private int flag;

    public Counter(String Topic,String Count) {
        strCount = Count;
        strTopic = Topic;
    }
    public Counter(String Topic,String Count,int n) {
        strCount = Count;
        strTopic = Topic;
        flag=n;
    }

    public String getStrCount() {
        return strCount;
    }
    public void setStrCount(String str) {
        strCount = str;
    }
    public String getStrTopic() {
        return strTopic;
    }
    public void setStrTopic(String str) {
        strTopic = str;
    }
    public int getFlag() {
        return flag;
    }
    public void setFlag(int n) {
        flag = n;
    }
}
