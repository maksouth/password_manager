package com.example.maksouth.passwordmanager.entities;

/**
 * Created by maksouth on 12.02.17.
 */

public interface ItemCredentials extends Credentials {
    String getLogin();
    void setLogin(String login);
    String getName();
    void setName(String name);
    long getId();

    /**
     * Do not modify it by yourself!
     * This field is for database communication only!
     * Id is auto generating by {@link com.example.maksouth.passwordmanager.interfaces.Repository}
     * @param id
     */
    void setId(long id);
    void setAddress(String address);
    String getAddress();
}
