package sample;

import java.util.ArrayList;

public class Playlist {

    private String playlistName;
    private String sequence;

    ArrayList<Songs> songList = new ArrayList<>();

    public Playlist(String playlistName){
        this.playlistName = playlistName;
    }


    // Adds a song object to the song list
    public void addSongToPlaylist(Songs song)
    {
        //Getting the values from the database and storing it in the song object
        int counter = 1;
        DB.selectSQL("Select * from tblSongs where fldSongID = '"+song.getSongID()+"'");
        do{
            String data = DB.getData();
            if (data.equals(DB.NOMOREDATA)){
                break;
            }else{
                if(counter == 2){
                    song.setPath(data);
                }
                if(counter == 3){
                    song.setArtistName(data);
                }
                if(counter == 4){
                    song.setTrackName(data);
                }
            }
            counter ++;
        } while(true);

        //Add this song to the songList ArrayList
        songList.add(song);

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

    public ArrayList<Songs> getSongList() {
        return songList;
    }
}
