package sample;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

public class Controller implements Initializable {
    @FXML
    private MediaView mediaV;
    @FXML
    private Button bPlay, bPause, bStop, bAddSongtoPlaylist, bAddPlaylist, bEditPlaylist, bDeletePlaylist;
    @FXML
    private Button bContinue, bPlaylistAddOK, bCreatePlaylist, bAddSongOK,bDeleteSongFromPlaylist, bSearch;
    @FXML
    private Label lbSongs, lbPlaylist, lbSongTitle;
    @FXML
    private Pane pAddPlaylist, pAddSongToPlaylist;
    @FXML
    private TextField tfPlaylistName, tfAddSongtoPlaylist, tfSearchForSong;
    @FXML
    private ListView<Playlist> lvPlaylist;
    @FXML
    private ListView<Songs> lvSongList;
    private MediaPlayer mp;
    private Media me;

    Songs selectedSong;
    Playlist selectedPlaylist = new Playlist("SelectedPlaylist");

    // Declaration: ArrayLists
    ArrayList<Playlist> playlistsList = new ArrayList<>(); //arraylist containing all playlist
    // Declaration: Playlists

    Playlist searchResult = new Playlist("Search results");

    Playlist allSongsPlaylist = new Playlist("temp");






    // Declaration Songs
    Songs song1 = new Songs(1);
    Songs song2 = new Songs(2);
    Songs song3 = new Songs(3);
    Songs song4 = new Songs(4);
    Songs song5 = new Songs(5);
    Songs song6 = new Songs(6);


    // Declaration: ObservableList
    ObservableList<Songs> items = FXCollections.observableArrayList();
    ObservableList<Playlist> playlistItems = FXCollections.observableArrayList();

    // Declaration: boolean
    private boolean isPaused = false;

    /**
     * This method is invoked automatically in the beginning. Used for initializing, loading data etc.
     *
     * @param location
     * @param resources
     */
    public void initialize(URL location, ResourceBundle resources) {

        loadPlaylistsFromDB();

        lvPlaylist.getSelectionModel().select(allSongsPlaylist);
        //Finding the playlist called "All Songs" and storing it as allSongsPlaylist

        for (Playlist element:playlistsList) {
            if(element.getPlaylistName().equals("All Songs")){
                allSongsPlaylist = element;
            }
        }
        //Adding all song objects from "All Songs" playlist to items as Songs
        ObservableList<Songs> items = FXCollections.observableArrayList();


        //Adding all song objects from myPlayList to items as Songs
        for (int i = 0; i < allSongsPlaylist.getSongList().size(); i++) {
            //adding to ArrayList in playlist object
            items.add(allSongsPlaylist.songList.get(i));
        }

        // Calling updateSonglist(); to get the list of all songs on application start
        updateSonglist(allSongsPlaylist);

        // Using CellFactory to make the ListViewer show the names of the songs not the object
        lvSongList.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Songs item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null || item.getTrackName() == null) {
                    setText(null);
                } else {
                    setText( item.getArtistName()+ " - " +item.getTrackName());
                }
            }
        });
        // Setting the selection mode for the lvSongList so that we can actually select songs (set as single selection)
        lvSongList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        //setting Playlists in listview
        updatePlaylists(playlistItems);

        //prevents two songs playing at the same time if stop hasn't been pressed yet.
        lvSongList.getSelectionModel().select(0);
        handlePlay();
        handleStop();
    }


    @FXML
    /**
     * Handler for the play button
     */
    private void handlePlay() {

        //Storing the selected song
        selectedSong = lvSongList.getSelectionModel().getSelectedItem();


        // Setting the label lbSongTitle to display the currently playing artist and which song.
        lbSongTitle.setText(selectedSong.getArtistName() + " - " + selectedSong.getTrackName());


        // Building the path to the location of the media file
        String path = new File("src/sample/media/" + selectedSong.getPath()).getAbsolutePath();
        // Create new Media object (the actual media content)
        me = new Media(new File(path).toURI().toString());
        // Create new MediaPlayer and attach the media to be played
        mp = new MediaPlayer(me);
        mediaV.setMediaPlayer(mp);
        // mp.setAutoPlay(true);
        // If autoplay is turned of the method play(), stop(), pause() etc controls how/when medias are played
        mp.setAutoPlay(false);

        //getNextSongID(selectedSong, myPlaylist);
        mp.setOnEndOfMedia(() -> {
            selectNextSong();
            handlePlay();
        });


        mp.play();

    }

    /**
     *Handler for the pause button
     */
    @FXML
    private void handlePause() {
        mp.pause();
        isPaused = true;
    }

    /**
     * Handler for the stop button
     */
    @FXML
    private void handleStop() {
        mp.stop();
    }


    @FXML
    /**
     * This method handles the continue button. It continues playing the song from where it was paused.
     */
    public void handleContinue() {
        Songs selectedSong = lvSongList.getSelectionModel().getSelectedItem();
        // Continues the song from where it was paused, but only if it has been paused.
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
     * @param selectedPlaylist is used to get a list of all songs currently in the application.
     */
    public void updateSonglist(Playlist selectedPlaylist) {
        // Sets the items to all the items in the selected playlist.
        items.setAll(selectedPlaylist.songList);
        // Sets the ListView to the new list of items.
        lvSongList.setItems(items);
    }

    /**
     * This methods creates a pop-up window for the user input
     */
    @FXML
    public void handleCreatePlaylist ()
    {
        pAddPlaylist.setDisable(false);
        pAddPlaylist.setOpacity(1.0);
    }

    /**
     * This method gets the user input and creates a playlist with the name specified by the user
     */
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

        // Clears the input field
        tfPlaylistName.setText("");
    }

    /**
     * This method deletes the selected playlist from the listview and the database
     */
    public void handleDeletePlaylist ()
    {
        Playlist selectedPlaylist = lvPlaylist.getSelectionModel().getSelectedItem();

        if (!selectedPlaylist.getPlaylistName().equals("All Songs")) {


            DB.deleteSQL("Delete from tblPlaylist where fldPlaylistName = '" + selectedPlaylist.getPlaylistName() + "';");


            for (int i = 0; i < playlistItems.size(); i++) {

                playlistsList.remove(playlistItems.get(i));
            }

            lvPlaylist.getItems().remove(selectedPlaylist);
            updatePlaylists(playlistItems);
        }
    }

    /**
     * Creates a sequence for a playlist by looking at the songlist of that playlist, and stores it in tblPlaylist
     * @param playlist The playlist for which the sequence should be set
     */
    public void setPlaylistSequence(Playlist playlist){
        String sequence = "";
        for (int i = 0; i < playlist.getSongList().size(); i++) {
            //add the song ID to the sequence String
            sequence += playlist.getSongList().get(i).getSongID();
            //add delimiter character to sequence
            sequence += ";";
        }
        //updating the object with the new sequence
        playlist.setSequence(sequence);

        //updating the database with the new sequence
        DB.updateSQL("update tblPlaylist set fldSequence = '"+sequence+"' where fldPlaylistName = '"+playlist.getPlaylistName()+"'");
    }

    /**
     * Gets information of all playlists from DB and creates playlist objects with it.
     * PlaylistName is stored in list to be used for the creation of the object.
     * Sequence is split into individual ID's that are used to identify the correct songs to be added to the playlist.
     */
    public void loadPlaylistsFromDB(){
        ArrayList<String> nameList = new ArrayList<>();
        //Get all name Strings from tblPlaylist
        DB.selectSQL("Select fldPlaylistName from tblPlaylist");
        do{
            String data = DB.getData();
            if (data.equals(DB.NOMOREDATA)){
                break;
            }else{
                nameList.add(data);
            }
        } while(true);

        //for each name string
        for (String nameElement:nameList) {
            //add new Playlist object to playlistsList
            playlistsList.add(new Playlist(nameElement));
            DB.selectSQL("Select fldSequence from tblPlaylist where fldPlaylistName = '"+nameElement+"'");
            String sequence = DB.getData();
            //List to hold ID's from from sequence
            ArrayList<Integer> IDList = new ArrayList<>();

            //wrapped in if to prevent crash if you load an empty playlist
            if(sequence.contains(";")){
                //splitting the string
                StringTokenizer IDtokens = new StringTokenizer(sequence, ";");
                //adding ID's from sequence to IDArray as individual elements
                while (IDtokens.hasMoreTokens()){
                    //System.out.println(IDtokens.nextToken());
                    IDList.add(Integer.parseInt(IDtokens.nextToken()));
                }
            }

            //setting sequence of this playlist and adding the songs to it
            for (int i = 0; i < playlistsList.size(); i++) {
                //finding the right playlist
                if(playlistsList.get(i).getPlaylistName().equals(nameElement)){
                    //setting the sequence variable of the playlist
                    playlistsList.get(i).setSequence(sequence);
                    //adding the songs to the playlist
                    for (Integer IDelement:IDList) {
                        playlistsList.get(i).addSongToPlaylist(new Songs(IDelement));
                    }
                }
            }

        }
    }


    /**
     * Moving the listview selection to the next item in the list
     */
    public void selectNextSong(){
        //storing the currently selected song
        int songIndex = lvSongList.getSelectionModel().getSelectedIndex();
        //setting selection to current +1
        songIndex++;
        lvSongList.getSelectionModel().select(songIndex);
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
        selectedPlaylist = lvPlaylist.getSelectionModel().getSelectedItem();

        // Updates the ListView to show the songs(items) in the selected playlist.
        updateSonglist(selectedPlaylist);

    }

    /**
     * This method removes the selected song from the selected playlist
     */
    @FXML
    public void handleDeleteSongFromPlaylist () {

        Songs selectedSong = lvSongList.getSelectionModel().getSelectedItem();

        for (int i = 0; i <selectedPlaylist.getSongList().size() ; i++) {

            if (selectedSong.getSongID()== selectedPlaylist.getSongList().get(i).getSongID()){
                selectedPlaylist.getSongList().remove(i);
            }

        }
        setPlaylistSequence(selectedPlaylist);
        updateSonglist(selectedPlaylist);
    }



    @FXML
    /**
     * This method creates a pop up window for the user input.
     */
    public void handleAddToPlaylistPopup() {
        pAddSongToPlaylist.setDisable(false);
        pAddSongToPlaylist.setOpacity(1.0);
    }

    /**
     * This method checks the user input for existing playlist, and adds the selected song to the corresponding playlist
     *
     */
    @FXML
    public void handleSongAddOK ()
    {
        String playlistName = tfAddSongtoPlaylist.getText();

        selectedSong = lvSongList.getSelectionModel().getSelectedItem();


        //Creates a temporary object which is set to the playlist in playlislist that matches the name from the user input
        for (int i = 0; i < playlistsList.size(); i++) {
            if(playlistsList.get(i).getPlaylistName().equals(playlistName)){
                playlistsList.get(i).addSongToPlaylist(selectedSong);
                setPlaylistSequence(playlistsList.get(i));
            }
        }

        //Hides the pop up window
        pAddSongToPlaylist.setDisable(true);
        pAddSongToPlaylist.setOpacity(0.0);


    }
    @FXML
    /**
     * This method enables the user to press ENTER/RETURN key on the keyboard to add a song to a playlist.
     * It's a substitute for pressing the 'OK' button
     * @param ke is the KeyEvent for the ENTER/Return key.
     */
    public void handleKeyEnterAddSong (KeyEvent ke) {
        // An if statement to check if the pressed key is ENTER, if it is it will run the handleSongOK(); method
        if (ke.getCode().equals(KeyCode.ENTER)) {
            handleSongAddOK();
        }
    }
    @FXML
    /**
     * This method enables the user to press ENTER/RETURN key on the keyboard add a new playlist with the inputted name.
     * It's a substitute for pressing the 'OK' button
     * @param ke is the KeyEvent for the ENTER/Return key.
     */
    public void handleKeyEnterAddPlaylist (KeyEvent ke) {
        // An if statement to check if the pressed key is ENTER, if it is it will run the handleAddPlaylist(); method
        if (ke.getCode().equals(KeyCode.ENTER)) {
            handleAddPlaylist();
        }
    }
    @FXML
    /**
     * This method enables the user to press ENTER/RETURN key on the keyboard to search for songs.
     * It's a substitute for pressing the 'search' button
     * @param ke is the KeyEvent for the ENTER/Return key.
     */
    public void handleKeyEnterSearch (KeyEvent ke) {
        // An if statement to check if the pressed key is ENTER, if it is it will run the searchForSongs(); method
        if (ke.getCode().equals(KeyCode.ENTER)) {
            searchForSongs();
        }
    }
    @FXML
    public void searchForSongs()
    {

        //delete all songs from searchresult
        for (int i = searchResult.getSongList().size()-1; i >= 0; i--) {
            searchResult.getSongList().remove(i);
        }
        //update songlist listview
        updateSonglist(searchResult);

        //store user input
        String userInput = tfSearchForSong.getText();

        //does trackname/artist contain userinput
        for (Songs element: allSongsPlaylist.getSongList()) {
            if (element.getTrackName().toLowerCase().contains(userInput.toLowerCase()) || element.getArtistName().toLowerCase().contains(userInput.toLowerCase()))
            {

                //add song to searchresult
                searchResult.addSongToPlaylist(element);
            }
        }

        //update listview
        updateSonglist(searchResult);

        // Clears the input field
        tfSearchForSong.setText("");

    }

}
