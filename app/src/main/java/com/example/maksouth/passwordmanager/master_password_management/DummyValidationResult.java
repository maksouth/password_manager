package com.example.maksouth.passwordmanager.master_password_management;

/**
 * Created by maksouth on 12.02.17.
 */

public class DummyValidationResult implements ValidationResult {

    private int statusCode;

    public DummyValidationResult(int statusCode){
        this.statusCode = statusCode;
    }

    @Override
    public int getStatusCode() {
        return statusCode;
    }
}
