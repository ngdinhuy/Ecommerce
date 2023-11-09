package com.example.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "tbl_message")
public class MessageModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 3000)
    private String message;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;


    private Integer readStatus;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "id_sender")
    private User sender;

    @Transient
    private Boolean fromYou;

    @Transient
    private String formatDate;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "id_receiver")
    private User reciever;

    public MessageModel(String message, Date date, User sender, User reciever, Integer readStatus) {
        this.message = message;
        this.date = date;
        this.sender = sender;
        this.reciever = reciever;
        this.readStatus = readStatus;
    }

    public MessageModel() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReciever() {
        return reciever;
    }

    public void setReciever(User reciever) {
        this.reciever = reciever;
    }

    public Integer getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(Integer readStatus) {
        this.readStatus = readStatus;
    }

    public Boolean getFromYou() {
        return fromYou;
    }

    public void setFromYou(Boolean fromYou) {
        this.fromYou = fromYou;
    }

    public String getFormatDate() {
        return formatDate;
    }

    public void setFormatDate(String responseDate) {
        this.formatDate = responseDate;
    }
}

