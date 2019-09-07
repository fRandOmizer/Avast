package cz.richardzan.avast1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UrlsResponse implements Serializable {
    private List<String> urlsResponses;

    public List<String> getUrlsResponses() {
        return urlsResponses;
    }

    public void setUrlsResponses(List<String> urlsResponses) {
        this.urlsResponses = urlsResponses;
    }
    
    public UrlsResponse(){
        urlsResponses = new ArrayList<String>();
    }
}
