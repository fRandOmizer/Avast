package cz.richardzan.avast2;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.ClientCredentials;
import com.wrapper.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@EnableAsync
@RestController
@EnableAutoConfiguration
public class App {
    
    private static final String clientId = "d180335677de4f7281a10d454732fe0d";
    private static final String clientSecret = "7435df072fbc4a5f817a07902fd2bdd8";

    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setClientId(clientId)
            .setClientSecret(clientSecret)
            .build();
    
    private static final ClientCredentialsRequest clientCredentialsRequest = 
            spotifyApi.clientCredentials()
            .build();
    
    @Autowired
    private SongService songService;
    
    @Bean(name = "threadPoolExecutor")
    public Executor getAsyncExecutor() {
            ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
            executor.setCorePoolSize(1);
            executor.setMaxPoolSize(3);
            executor.setQueueCapacity(100);
            executor.setThreadNamePrefix("Async-");
            executor.initialize();
            return executor;
    }
    
    @RequestMapping(value="/")
    public String home() {
        
        String text = "Hello, welcome at my first project with Spotify API <br>" +
                "This api has defined one endpoint accesible via POST reqests: <br>  <br>" + 
                "&nbsp;&nbsp;&nbsp;2. http://localhost:8080/songsCount <br> " +
                "&nbsp;&nbsp;&nbsp;&nbsp; request JSON: { \"songName\" : \"bohemian rhapsody\"} <br>" +
                "&nbsp;&nbsp;&nbsp;&nbsp; response JSON: { \"tracksCount\" : \"Count of tracks with given name: number\" } <br> <br>"; 
                
        return text + "Check accessibility: " + clientCredentials_Sync();
    }
    
    @RequestMapping(value="/songsCount", method = RequestMethod.POST)
    @ResponseBody
    public SongResponse song(@RequestBody SongReguest input) throws Exception {
        
        final ClientCredentials clientCredentials = clientCredentialsRequest.execute();

        // Set access token for further "spotifyApi" object usage
        spotifyApi.setAccessToken(clientCredentials.getAccessToken());
        
        CompletableFuture<String> resultFuture = songService
                .getTrackCount(clientId, spotifyApi);
        
        // Wait until they are all done
        CompletableFuture.allOf(resultFuture).join();
        
        SongResponse response = new SongResponse();
        response.setTracksCount(resultFuture.get());
        
        return response;
    }
    
    public static String clientCredentials_Sync() {
        try {
          final ClientCredentials clientCredentials = clientCredentialsRequest.execute();

          // Set access token for further "spotifyApi" object usage
          spotifyApi.setAccessToken(clientCredentials.getAccessToken());

          return ("Still valid clientId credentials");
        } catch (IOException | SpotifyWebApiException e) {
          return ("Error: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
