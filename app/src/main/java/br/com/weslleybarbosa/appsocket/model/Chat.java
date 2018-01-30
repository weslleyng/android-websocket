package br.com.weslleybarbosa.appsocket.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Criado por Weslley Barbosa em 26/01/2018.
 */

public class Chat {

    public Chat() {
        super();

    }

    public Chat(String name) {
        super();
        this.name = name;
    }

    private String id = UUID.randomUUID().toString();

    private Set<Message> messages;

    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }

    public List<Message> messageList(){
        return new ArrayList<>(getMessages());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
