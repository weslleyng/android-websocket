package br.com.weslleybarbosa.appsocket.model;

import java.util.Date;
import java.util.UUID;

/**
 * Criado por Weslley Barbosa em 26/01/2018.
 */

public class Message {

    private String content;

    private String id = UUID.randomUUID().toString();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private User user;
    private Date date;


    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

}
