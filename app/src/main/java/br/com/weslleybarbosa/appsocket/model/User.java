package br.com.weslleybarbosa.appsocket.model;

import java.util.UUID;

/**
 * Criado por Weslley Barbosa em 26/01/2018.
 */

public class User {
    private String name;
    private String password;
    private String email;
    private String id = UUID.randomUUID().toString();

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

}
