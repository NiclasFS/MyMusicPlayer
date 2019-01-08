package sample;

import javafx.collections.FXCollections;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.media.*;
import javafx.scene.control.Button;
import javafx.collections.ObservableList;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import java.io.*;
import java.net.*;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private MediaView mediaV;
    @FXML
    private Button bPlay, bPause, bStop, bAddSongsToPlaylist, bAddPlaylist, bEditPlaylist, bDeletePlaylist;
    @FXML
    private Label lbSongs, lbPlaylist, lbSongTitle;
    @FXML
    private ListView lvPlaylist, lvSongList;
    @FXML
    private MediaPlayer mp;
    private Media me;






    /**
     * This method is invoked automatically in the beginning. Used for initializing, loading data etc.
     *
     * @param location
     * @param resources
     */
    public void initialize(URL location, ResourceBundle resources){
        // Build the path to the location of the media file
        //String path = new File("src/sample/media/SampleAudio_0.4mb.mp3").getAbsolutePath();
        String path = new File("src/sample/media/KieLoBot_Alles_Leer.mp3").getAbsolutePath();
        // Create new Media object (the actual media content)
        me = new Media(new File(path).toURI().toString());
        // Create new MediaPlayer and attach the media to be played
        mp = new MediaPlayer(me);
        //
        mediaV.setMediaPlayer(mp);
        // mp.setAutoPlay(true);
        // If autoplay is turned of the method play(), stop(), pause() etc controls how/when medias are played
        mp.setAutoPlay(false);

        Playlist myPlaylist = new Playlist();
        Songs song1 = new Songs(1);
        myPlaylist.addSongToPlaylist(song1);
        //song1.printValues();
        Songs song2 = new Songs(2);
        myPlaylist.addSongToPlaylist(song2);
        song2.printValues();
        System.out.println(myPlaylist.getSongList());

        //Adding all song objects from myPlayList to items as String
        ObservableList<String> items = FXCollections.observableArrayList();
        for (int i = 0; i < myPlaylist.getSongList().size(); i++) {
            items.add(myPlaylist.songList.get(i).printValues());
        }
        lvSongList.setItems(items);



    }

    @FXML
    /**
     * Handler for the play button
     */
    private void handlePlay()
    {
        // Play the mediaPlayer with the attached media
        mp.play();

    }
    @FXML
    private void handlePause()
    {
        mp.pause();
    }
    @FXML
    private void handleStop()
    {
        mp.stop();
    }

    //populate listview
    private void populate(){

    }
}
