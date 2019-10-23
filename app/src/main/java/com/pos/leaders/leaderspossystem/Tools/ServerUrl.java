package com.pos.leaders.leaderspossystem.Tools;

/**
 * Created by LeadPos on 20/10/19.
 */

public enum ServerUrl {
    BO_SERVER_URL("http://apiv4.leadpos.net/"),
    BO_ALNAJAH_SERVER_URL("http://wbapiv4.leadpos.net/"),BO_LOCAL_SERVER_URL("http://10.0.0.23:8000/");

    private final String url;

    ServerUrl(final String url){
        this.url=url;
    }

    public String getItem(){
        return url;
    }
}
