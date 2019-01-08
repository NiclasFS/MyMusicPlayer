package sample;

public class Songs {

private String trackName;
private String artistName;
private String path;
private int songID;

    public Songs(int songID){
        this.songID = songID;
    }


    public String printValues(){
        return "trackname: " + trackName + "\n artistname:" + artistName + "\n path: " + path + "\n";
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getSongID() {
        return songID;
    }

    public void setSongID(int songID) {
        this.songID = songID;
    }
}
