package com.skull.red.firebolt;

import android.widget.TextView;

/**
 * Created by Kumar Saurabh on 1/17/2017.
 */

public class Repository {

    private String name, description, login, url, OwnerUrl;
    private boolean fork;


    public String getOwnerUrl() {
        return OwnerUrl;
    }

    public String getUrl() {
        return url;
    }

    public boolean isFork() {
        return fork;
    }

    public String getDescription() {
        return description;
    }

    public String getLogin() {
        return login;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFork(boolean fork) {
        this.fork = fork;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setOwnerUrl(String ownerUrl) {
        OwnerUrl = ownerUrl;
    }
}
