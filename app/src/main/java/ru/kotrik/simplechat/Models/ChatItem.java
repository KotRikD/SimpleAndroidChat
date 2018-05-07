package ru.kotrik.simplechat.Models;

public class ChatItem {

    public int type;
    public String nickname;
    public String time;
    public String message;

    public ChatItem(int type, String nickname, String time, String message){
        this.type = type;
        this.nickname = nickname;
        this.time = time;
        this.message = message;
    }

}
