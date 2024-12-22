package com.example.drycleaning.dtos.edit;

import com.example.drycleaning.models.Role;


import java.io.Serializable;
import java.util.List;

public class UserRoleDtoEdit implements Serializable {


    private List<Role> role;

    public UserRoleDtoEdit(List<Role> role) {
        this.role = role;
    }

    public List<Role> getRole() {
        return role;
    }

    public void setRole(List<Role> role) {
        this.role = role;
    }
}
