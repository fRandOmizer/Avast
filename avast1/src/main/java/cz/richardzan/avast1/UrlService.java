package cz.richardzan.avast1;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class UrlService {
    
    public UrlService() {
        
    }
    
    @Async("threadPoolExecutor")
    public CompletableFuture <String> getStatus(String url) {
        
        String result = "";
        try {
            URL siteURL = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) siteURL.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(300000);
            connection.connect();

            int code = connection.getResponseCode();
            if (code / 100 == 2) {
                    result = "Passed -> Return Code: " + code;
            } else {
                    result = "Warning -> Return Code: " + code;
            }
        } catch (Exception e) {
            result = "ERROR: Wrong domain -> Return Code:" + e.getMessage();
        }
        
        return CompletableFuture.completedFuture(url + " : " + result);
    }
}
