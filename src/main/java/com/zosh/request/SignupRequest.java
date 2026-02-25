package com.zosh.request;

public class SignupRequest {
    private String fullName;
    private String email;
    private String otp;

    public SignupRequest() {
    }

    public SignupRequest(String fullName, String email, String otp) {
        this.fullName = fullName;
        this.email = email;
        this.otp = otp;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
