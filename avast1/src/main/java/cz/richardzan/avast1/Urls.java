package cz.richardzan.avast1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Urls implements Serializable {
    private List<String> urls;

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public List<String> getUrls() {
        return urls;
    }
    
    public Urls() {
        urls = new ArrayList<String>();
    }
}
