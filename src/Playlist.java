import java.util.ArrayList;

public class Playlist {

    private String playlistName;
    private String sequence;
    ArrayList<Songs> songlist = new ArrayList<>();


    // Adds a song object to the song list
    private void addSongToPlaylist()
    {


    }

    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }
}
