package com.paril.mlaclientapp.model;
import java.io.Serializable;
public class MLAGroupDetails1 implements Serializable
{

        public String groupName;
        public String idGroup;
        public int userId;
        public String groupKey;
        public String status;
        public String idStudent;
        public String publicKey;

        public String getIdStudent() {
            return idStudent;
        }
        public void setIdStudent(String idStudent) {
            this.idStudent = idStudent;
        }

        public String getGroupName() {
            return groupName;
        }
        public void setGroupName(String groupName) { this.groupName = groupName; }

        public String getIdGroup() {
            return idGroup;
        }
        public void setIdGroup(String idGroup) {
            this.idGroup = idGroup;
        }

        public String getGroupKey() {
            return groupKey;
        }
        public void setGroupKey(String groupKey) {
            this.groupKey = groupKey;
        }

        public String getStatus() {
            return status;
        }
        public void setStatus(String status) {
            this.status = status;
        }

        public int getUserId() {
            return userId;
        }
        public void setUserId(int userId) { this.userId = userId; }

      public String getPublicKey() {
        return publicKey;
    }
    public void setPublicKey(String publicKey) { this.publicKey = publicKey; }

    }