package sample;

import javafx.collections.FXCollections;

import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

import javafx.scene.layout.Pane;
import javafx.scene.media.*;
import javafx.collections.ObservableList;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private MediaView mediaV;
    @FXML
    private Button bPlay, bPause, bStop, bAddSongsToPlaylist, bAddPlaylist, bEditPlaylist, bDeletePlaylist, bContinue, bPlaylistAddOK, bCreatePlaylist;
    @FXML
    private Label lbSongs, lbPlaylist, lbSongTitle;
    @FXML
    private Pane pAddPlaylist;
    @FXML
    private TextField tfPlaylistName;
    @FXML
    private ListView<Playlist> lvPlaylist;
    @FXML
    private ListView<Songs> lvSongList;
    @FXML
    private MediaPlayer mp;
    private Media me;

    ArrayList<Playlist> playlistsList = new ArrayList<>(); //arraylist containing all playlists
    Playlist myPlaylist = new Playlist("All Songs"); //The playlist containing all songs


    Songs song1 = new Songs(1);
    Songs song2 = new Songs(2);
    Songs song3 = new Songs(3);
    Songs song4 = new Songs(4);
    Songs song5 = new Songs(5);

    ObservableList<Songs> items = FXCollections.observableArrayList();

    ObservableList<Playlist> playlistItems = FXCollections.observableArrayList();

    private boolean isPaused = false;

    //ArrayList<Songs> allSongsList = null;

    /**
     * This method is invoked automatically in the beginning. Used for initializing, loading data etc.
     *
     * @param location
     * @param resources
     */
    public void initialize(URL location, ResourceBundle resources){

        playlistsList.add(myPlaylist); //temporary for testing



        myPlaylist.addSongToPlaylist(song1);
        //song1.printValues();
        myPlaylist.addSongToPlaylist(song2);
        myPlaylist.addSongToPlaylist(song3);
        myPlaylist.addSongToPlaylist(song4);
        myPlaylist.addSongToPlaylist(song5);
        //song2.printValues();
        //allSongsList = myPlaylist.getSongList();
        System.out.println(myPlaylist.getSongList());

        //Adding all song objects from myPlayList to items as Songs

        for (int i = 0; i < myPlaylist.getSongList().size(); i++) {
            //adding to ArrayList in playlist object
            items.add(myPlaylist.songList.get(i));
        }
        //Setting the items/objects in the listview
        lvSongList.setItems(items);

        lvSongList.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Songs item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null || item.getTrackName() == null) {
                    setText(null);
                } else {
                    setText(item.getTrackName());
                }
            }
        });


        lvSongList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        //setting Playlists in listview

        updatePlaylists(playlistItems);



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

    // Creates a pop up window for the user input
    @FXML
    public void handleCreatePlaylist()
    {
        pAddPlaylist.setDisable(false);
        pAddPlaylist.setOpacity(1.0);
    }

    //Gets user input and creates a playlist with the name specified by the user
    @FXML
    public void handleAddPlaylist ()
    {

        String playlistName = tfPlaylistName.getText();

        for (int i = 0; i < playlistItems.size(); i++) {
            //adding to ArrayList in playlist object
            playlistsList.remove(playlistItems.get(i));
        }

        playlistsList.add(new Playlist(playlistName));
        updatePlaylists(playlistItems);

        //Creates a temporary object which is set to the playlist in playlislist that matches the name from the user input
        Playlist temp = new Playlist("temp");
        for (Playlist element:playlistsList) {
            if (element.getPlaylistName().equals(playlistName)){
                temp = element;
            }
        }


        // Stores the playlist in the database
        DB.insertSQL("insert into tblPlaylist values ('"+temp.getPlaylistName()+"','"+temp.getSequence()+"');");

        //Hides the pop up window
        pAddPlaylist.setDisable(true);
        pAddPlaylist.setOpacity(0.0);

    }

    // Deletes the selected playlist
    public void handleDeletePlaylist ()
    {
        Playlist selectedSong = lvPlaylist.getSelectionModel().getSelectedItem();

        DB.deleteSQL("Delete from tblPlaylist where fldPlaylistName = '"+selectedSong.getPlaylistName()+"';");
        //DB.deleteSQL("Delete from tblPlaylist where fldSequence = 'null';");
        //DB.deleteSQL("delete from tblPlaylist values ('"+selectedSong+"');");
        System.out.println(selectedSong.getPlaylistName());

        for (int i = 0; i < playlistItems.size(); i++) {
            //adding to ArrayList in playlist object
            playlistsList.remove(playlistItems.get(i));
        }

        lvPlaylist.getItems().remove(selectedSong);
        updatePlaylists(playlistItems);

    }


    public void updatePlaylists(ObservableList<Playlist> playlistItems){
        //adding playlists to observablelist
        for (int i = 0; i < playlistsList.size(); i++) {
            //adding to ArrayList in playlist object
            playlistItems.add(playlistsList.get(i));
        }
        //Setting the items/objects in the listview
        lvPlaylist.setItems(playlistItems);
        lvPlaylist.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        //setting name as text in listview
        lvPlaylist.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Playlist item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null || item.getPlaylistName() == null) {
                    setText(null);
                } else {
                    setText(item.getPlaylistName());
                }
            }
        });
    }



}
