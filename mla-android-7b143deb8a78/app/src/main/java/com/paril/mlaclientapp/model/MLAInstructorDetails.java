package com.paril.mlaclientapp.model;

import java.io.Serializable;

public class MLAInstructorDetails implements Serializable
    {
        public String idInstructor;

        public String userName;
        public String firstName;
        public String lastName;
        public String emailId;
        public String telephone;
        public int userId;
        public String aliasMailId;
        public String address;
        public String skypeId;

        public String publicKey;


        public String getPublicKey() { return publicKey; }

        public void setPublicKey(String publicKey) {
            this.publicKey = publicKey;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String password;

        public String getIdInstructor() {
            return idInstructor;
        }

        public void setIdInstructor(String idInstructor) {
            this.idInstructor = idInstructor;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getEmailId() {
            return emailId;
        }

        public void setEmailId(String emailId) {
            this.emailId = emailId;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getAliasMailId() {
            return aliasMailId;
        }

        public void setAliasMailId(String aliasMailId) {
            this.aliasMailId = aliasMailId;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getSkypeId() {
            return skypeId;
        }

        public void setSkypeId(String skypeId) {
            this.skypeId = skypeId;
        }


    }