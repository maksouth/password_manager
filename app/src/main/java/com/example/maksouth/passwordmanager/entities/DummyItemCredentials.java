package com.example.maksouth.passwordmanager.entities;

/**
 * Created by maksouth on 12.02.17.
 */

public class DummyItemCredentials implements ItemCredentials {

    private String login;
    private String password;
    private String name;
    private long id;
    private String address;

    @Override
    public String getLogin() {
        return login;
    }

    @Override
    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Item: [" + getId() +
                ", " + getName() +
                ", " + getLogin() +
                ", " + getPassword() + "]";
    }
}
