package com.pos.leaders.leaderspossystem.Tools;

/**
 * Created by LeadPos on 20/10/19.
 */

public enum ServerUrl {
    BO_SERVER_URL("http://7aac69e4f0e5.ngrok.io/");

    private final String url;

    ServerUrl(final String url){
        this.url=url;
    }

    public String getItem(){
        return url;
    }
}