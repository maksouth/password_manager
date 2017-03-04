package com.example.maksouth.passwordmanager.entities;

/**
 * Created by maksouth on 11.02.17.
 */

public class MasterCredentials implements Credentials {

    private String password;

    public MasterCredentials(String password){
        setPassword(password);
    }

    public void setPassword(String password){
        this.password = password;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MasterCredentials that = (MasterCredentials) o;

        return password.equals(that.password);

    }

    @Override
    public int hashCode() {
        return password.hashCode();
    }
}
