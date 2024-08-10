package org.example.courzelo.dto.requests.Groups;

import java.util.List;

public class GroupsREQ {
    private String id;
    private String name;
    private String creator;
    private String members;

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void setMembers(String members) {
        this.members = members;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMembers() {
        return members;
    }

}
