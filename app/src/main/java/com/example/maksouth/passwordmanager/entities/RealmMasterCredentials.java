package com.example.maksouth.passwordmanager.entities;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by maksouth on 23.02.17.
 */

public class RealmMasterCredentials extends RealmObject implements Credentials{

    @PrimaryKey
    private String password;

    public RealmMasterCredentials(){}

    public RealmMasterCredentials(String password){
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

        RealmMasterCredentials that = (RealmMasterCredentials) o;

        return password.equals(that.password);

    }

    @Override
    public int hashCode() {
        return password.hashCode();
    }
}
