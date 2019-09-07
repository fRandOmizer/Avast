package cz.richardzan.avast2;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.Artist;
import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.requests.data.search.simplified.SearchTracksRequest;
import com.wrapper.spotify.requests.data.tracks.GetTrackRequest;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class SongService {
    
    public SongService() {
        
    }
    
    @Async("threadPoolExecutor")
    public CompletableFuture <String> getTrackCount(String song, SpotifyApi spotifyApi) throws InterruptedException, ExecutionException, IOException, SpotifyWebApiException {

        SearchTracksRequest searchTrackRequest = spotifyApi.searchTracks(song).build();

        Paging<Track> tracks =  searchTrackRequest.execute();

        return CompletableFuture.completedFuture("Count of tracks with given name: " + tracks.getTotal());
    }
}
