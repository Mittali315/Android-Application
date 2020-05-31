package com.paril.mlaclientapp.model;
import java.io.Serializable;
public class MLAFacebookDetails implements Serializable
{
   // public String idPost;
    public String startTime;
    public String postmsg;
    public int userId;
    public String idGroup;
    public String sessionKey;
    public String digitalSignature;


    public String getPostmsg() {
        return postmsg;
    }

    public void setPostmsg(String postmsg) {
        this.postmsg = postmsg;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) { this.userId = userId; }

    public String getIdGroup() {
        return idGroup;
    }
    public void setIdGroup(String idGroup) {
        this.idGroup = idGroup;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getDigitalSignature() {
        return digitalSignature;
    }

    public void setDigitalSignature(String digitalSignature) {
        this.digitalSignature = digitalSignature;
    }




}