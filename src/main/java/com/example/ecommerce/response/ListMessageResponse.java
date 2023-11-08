package com.example.ecommerce.response;

import java.util.Date;

public class ListMessageResponse {
    private Integer idUser;
    private String name;
    private String avatar;
    private String lastMessage;
    private Date date;

    private String formatDate;

    private Boolean fromYou;
    private Integer numberUnreadMessage;

    public ListMessageResponse(Integer idUser, String name, String avatar, String lastMessage, String formatDate, Boolean fromYou, Integer numberUnreadMessage, Date date) {
        this.idUser = idUser;
        this.name = name;
        this.avatar = avatar;
        this.lastMessage = lastMessage;
        this.formatDate = formatDate;
        this.fromYou = fromYou;
        this.numberUnreadMessage = numberUnreadMessage;
        this.date = date;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getFormatDate() {
        return formatDate;
    }

    public void setFormatDate(String formatDate) {
        this.formatDate = formatDate;
    }

    public Boolean getFromYou() {
        return fromYou;
    }

    public void setFromYou(Boolean fromYou) {
        this.fromYou = fromYou;
    }

    public Integer getNumberUnreadMessage() {
        return numberUnreadMessage;
    }

    public void setNumberUnreadMessage(Integer numberUnreadMessage) {
        this.numberUnreadMessage = numberUnreadMessage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
