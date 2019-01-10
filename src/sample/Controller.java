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

    // Declaration: ArrayLists
    ArrayList<Playlist> playlistsList = new ArrayList<>(); //arraylist containing all playlist
    // Declaration: Playlists
    Playlist myPlaylist = new Playlist("All Songs"); //The playlist containing all songs
    Playlist deleteTester = new Playlist("Delete tester");
    // Declaration Songs
    Songs song1 = new Songs(1);
    Songs song2 = new Songs(2);
    Songs song3 = new Songs(3);
    Songs song4 = new Songs(4);
    Songs song5 = new Songs(5);
    // Declaration: ObservableList
    ObservableList<Songs> items = FXCollections.observableArrayList();
    ObservableList<Playlist> playlistItems = FXCollections.observableArrayList();
    // Declaration: boolean
    private boolean isPaused = false;

    //ArrayList<Songs> allSongsList = null;

    /**
     * This method is invoked automatically in the beginning. Used for initializing, loading data etc.
     *
     * @param location
     * @param resources
     */
    public void initialize(URL location, ResourceBundle resources) {

        playlistsList.add(myPlaylist); //temporary for testing

        //playlistsList.add(tester);     //temporary for testing
        //tester.setPlaylistName("TEST#1");
        // Adding songs
        myPlaylist.addSongToPlaylist(song1);
        //song1.printValues();
        myPlaylist.addSongToPlaylist(song2);
        myPlaylist.addSongToPlaylist(song3);
        myPlaylist.addSongToPlaylist(song4);
        myPlaylist.addSongToPlaylist(song5);

        //song2.printValues();
        //allSongsList = myPlaylist.getSongList();

        // System.out.println(myPlaylist.getSongList());

        //Adding all song objects from myPlayList to items as Songs
        for (int i = 0; i < myPlaylist.getSongList().size(); i++) {
            //adding to ArrayList in playlist object
            items.add(myPlaylist.songList.get(i));
        }
        // Calling updateSonglist(); to get the list of all songs on application start
        updateSonglist(myPlaylist);
        //Setting the items/objects in the listview
        //lvSongList.setItems(items);
        // Using CellFactory to make the ListViewer show the names of the songs not the object
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
        // Setting the selection mode for the lvSongList so that we can actually select songs (set as single selection)
        lvSongList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        //setting Playlists in listview
        updatePlaylists(playlistItems);
    }

    @FXML
    /**
     * Handler for the play button
     */
    private void handlePlay() {

        // Play the mediaPlayer with the attached media

        //Storing the selected song
        Songs selectedSong = lvSongList.getSelectionModel().getSelectedItem();

        //System.out.println(selectedSong.getPath());

        // Setting the label lbSongTitle to display the currently playing artist and which song.
        lbSongTitle.setText(selectedSong.getArtistName() + " - " + selectedSong.getTrackName());
        //if(!isPaused){

        // Building the path to the location of the media file
        String path = new File("src/sample/media/" + selectedSong.getPath()).getAbsolutePath();
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
    private void handlePause() {
        mp.pause();
        isPaused = true;
    }

    @FXML
    private void handleStop() {
        mp.stop();
    }

    @FXML
    /**
     *
     */
    public void handleContinue() {
        Songs selectedSong = lvSongList.getSelectionModel().getSelectedItem();
        System.out.println(selectedSong.getPath());

        if (!isPaused) {
            String path = new File("src/sample/media/" + selectedSong.getPath()).getAbsolutePath();
            // Create new Media object (the actual media content)
            me = new Media(new File(path).toURI().toString());
            // Create new MediaPlayer and attach the media to be played
            mp = new MediaPlayer(me);
            mediaV.setMediaPlayer(mp);
            mp.setAutoPlay(false);
        }
        mp.play();

    }


    /**
     * This method updates the ListView with songs to the current content that needs to be there.
     *
     * @param selectedPlaylist is used to get a list of all songs currently in the application.
     */
    public void updateSonglist(Playlist selectedPlaylist) {
        items.setAll(selectedPlaylist.songList);
        lvSongList.setItems(items);
    }
        // Creates a pop up window for the user input

        @FXML
        public void handleCreatePlaylist ()
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
            for (Playlist element : playlistsList) {
                if (element.getPlaylistName().equals(playlistName)) {
                    temp = element;
                }
            }


            // Stores the playlist in the database
            DB.insertSQL("insert into tblPlaylist values ('" + temp.getPlaylistName() + "','" + temp.getSequence() + "');");

            //Hides the pop up window
            pAddPlaylist.setDisable(true);
            pAddPlaylist.setOpacity(0.0);

        }

        // Deletes the selected playlist
        public void handleDeletePlaylist ()
        {
            Playlist selectedPlaylist = lvPlaylist.getSelectionModel().getSelectedItem();

            DB.deleteSQL("Delete from tblPlaylist where fldPlaylistName = '" + selectedPlaylist.getPlaylistName() + "';");
            //DB.deleteSQL("Delete from tblPlaylist where fldSequence = 'null';");
            //DB.deleteSQL("delete from tblPlaylist values ('"+selectedSong+"');");
            // System.out.println(selectedPlaylist.getPlaylistName());


            for (int i = 0; i < playlistItems.size(); i++) {

                playlistsList.remove(playlistItems.get(i));
            }


            lvPlaylist.getItems().remove(selectedPlaylist);
            updatePlaylists(playlistItems);


        }


        public void updatePlaylists (ObservableList < Playlist > playlistItems) {
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
        @FXML
        /**
         * This method updates the ListView of songs to the corresponding playlist
         */
        private void getPlaylistInformation () {
            // New object that gets the songs(items) from the selected playlist.
            Playlist selectedPlaylist = lvPlaylist.getSelectionModel().getSelectedItem();
            // Updates the ListView to show the songs(items) in the selected playlist.
            updateSonglist(selectedPlaylist);

            //System.out.println(selectedPlaylist.getSongList().size());
            //System.out.println(selectedPlaylist.getPlaylistName());
            //System.out.println(selectedPlaylist.getSongList().toString());
        }
    }
