package cz.richardzan.avast2;

public class SongResponse {
    
    private String tracksCount;

    public String getTracksCount() {
        return tracksCount;
    }

    public void setTracksCount(String tracksCount) {
        this.tracksCount = tracksCount;
    }

    public SongResponse() {
        this.tracksCount = "";
    }
}
