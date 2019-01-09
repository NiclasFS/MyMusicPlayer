package sample;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
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
    private Button bPlay, bPause, bStop, bAddSongsToPlaylist, bAddPlaylist, bEditPlaylist, bDeletePlaylist, bContinue;
    @FXML
    private Label lbSongs, lbPlaylist, lbSongTitle;
    @FXML
    private ListView lvPlaylist;
    @FXML
    private ListView<Songs> lvSongList;
    @FXML
    private MediaPlayer mp;
    private Media me;

    Playlist myPlaylist = new Playlist();
    Songs song1 = new Songs(1);
    Songs song2 = new Songs(2);
    Songs song3 = new Songs(3);

    private boolean isPaused = false;

    //ArrayList<Songs> allSongsList = null;

    /**
     * This method is invoked automatically in the beginning. Used for initializing, loading data etc.
     *
     * @param location
     * @param resources
     */
    public void initialize(URL location, ResourceBundle resources){



        myPlaylist.addSongToPlaylist(song1);
        //song1.printValues();
        myPlaylist.addSongToPlaylist(song2);
        myPlaylist.addSongToPlaylist(song3);
        //song2.printValues();
        //allSongsList = myPlaylist.getSongList();
        System.out.println(myPlaylist.getSongList());

        //Adding all song objects from myPlayList to items as Songs
        ObservableList<Songs> items = FXCollections.observableArrayList();
        for (int i = 0; i < myPlaylist.getSongList().size(); i++) {
            //adding to ArrayList in playlist object
            items.add(myPlaylist.songList.get(i));
        }

        //Setting the items/objects in the listview
        lvSongList.setItems(items);

        lvSongList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

    }
    @FXML
    /**
     * Handler for the play button
     */
    private void handlePlay()
    {
        // Play the mediaPlayer with the attached media

        //Storing the selected
        Songs selectedSong = lvSongList.getSelectionModel().getSelectedItem();

        System.out.println(selectedSong.getPath());

        lbSongTitle.setText(selectedSong.getArtistName() +" - " + selectedSong.getTrackName ());
        //if(!isPaused){
            // Build the path to the location of the media file
            //String path = new File("src/sample/media/SampleAudio_0.4mb.mp3").getAbsolutePath();
            String path = new File("src/sample/media/"+selectedSong.getPath()).getAbsolutePath();
            // Create new Media object (the actual media content)
            me = new Media(new File(path).toURI().toString());
            // Create new MediaPlayer and attach the media to be played
            mp = new MediaPlayer(me);
            //
            mediaV.setMediaPlayer(mp);
            // mp.setAutoPlay(true);
            // If autoplay is turned of the method play(), stop(), pause() etc controls how/when medias are played
            mp.setAutoPlay(false);
        //}

        mp.play();


    }


    @FXML
    private void handlePause()
    {
        mp.pause();
        isPaused = true;
    }
    @FXML
    private void handleStop()
    {
        mp.stop();
    }

    @FXML
    /**
     *
     */
    public void handleContinue () {
        Songs selectedSong = lvSongList.getSelectionModel().getSelectedItem();
        System.out.println(selectedSong.getPath());

        if(!isPaused){
        String path = new File("src/sample/media/"+selectedSong.getPath()).getAbsolutePath();
        // Create new Media object (the actual media content)
        me = new Media(new File(path).toURI().toString());
        // Create new MediaPlayer and attach the media to be played
        mp = new MediaPlayer(me);
        mediaV.setMediaPlayer(mp);
        mp.setAutoPlay(false);
        }
        mp.play();

    }

    //populate listview
    private void populate(){

    }
}
