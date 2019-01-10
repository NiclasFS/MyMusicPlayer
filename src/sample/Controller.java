package sample;

import javafx.collections.FXCollections;

import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

import javafx.scene.media.*;
import javafx.collections.ObservableList;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.io.*;
import java.net.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

public class Controller implements Initializable {
    @FXML
    private MediaView mediaV;
    @FXML
    private Button bPlay, bPause, bStop, bAddSongsToPlaylist, bAddPlaylist, bEditPlaylist, bDeletePlaylist, bContinue;
    @FXML
    private Label lbSongs, lbPlaylist, lbSongTitle;
    @FXML
    private ListView<Playlist> lvPlaylist;
    @FXML
    private ListView<Songs> lvSongList;
    @FXML
    private MediaPlayer mp;
    private Media me;

    Songs selectedSong;
    ArrayList<Playlist> playlistsList = new ArrayList<>(); //arraylist containing all playlists
    //Playlist myPlaylist = new Playlist("All Songs"); //The playlist containing all songs

    Songs song1 = new Songs(1);
    Songs song2 = new Songs(2);
    Songs song3 = new Songs(3);
    Songs song4 = new Songs(4);
    Songs song5 = new Songs(5);
    Songs song6 = new Songs(6);
    Songs song7 = new Songs(6);
    Songs song8 = new Songs(6);



    private boolean isPaused = false;

    //ArrayList<Songs> allSongsList = null;

    /**
     * This method is invoked automatically in the beginning. Used for initializing, loading data etc.
     *
     * @param location
     * @param resources
     */
    public void initialize(URL location, ResourceBundle resources){

        loadPlaylistsFromDB();

        /*playlistsList.add(myPlaylist); //temporary for testing

        myPlaylist.setPlaylistName("All Songs");

        myPlaylist.addSongToPlaylist(song1);
        //song1.printValues();
        myPlaylist.addSongToPlaylist(song2);
        myPlaylist.addSongToPlaylist(song3);
        myPlaylist.addSongToPlaylist(song4);
        myPlaylist.addSongToPlaylist(song5);
        myPlaylist.addSongToPlaylist(song6);
        myPlaylist.addSongToPlaylist(song7);
        myPlaylist.addSongToPlaylist(song8);*/

        //song2.printValues();
        //allSongsList = myPlaylist.getSongList();
        //System.out.println(myPlaylist.getSongList());



        //Finding the playlist called "All Songs" and storing it as allSongsPlaylist
        Playlist allSongsPlaylist = new Playlist("temp");
        for (Playlist element:playlistsList) {
            if(element.getPlaylistName().equals("All Songs")){
                allSongsPlaylist = element;
            }
        }
        //Adding all song objects from "All Songs" playlist to items as Songs
        ObservableList<Songs> items = FXCollections.observableArrayList();
        for (int i = 0; i < allSongsPlaylist.getSongList().size(); i++) {
            //adding to ArrayList in playlist object
            items.add(allSongsPlaylist.songList.get(i));
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
        ObservableList<Playlist> playlistItems = FXCollections.observableArrayList();
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
        selectedSong = lvSongList.getSelectionModel().getSelectedItem();

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

        mp.setOnEndOfMedia(() -> {
            selectNextSong();
            handlePlay();
        });

        //getNextSongID(selectedSong, myPlaylist);

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

    //updates all playlists
    //***To be used when the user makes changes to a playlist***
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

        //setting the sequence for all playlists
        for (Playlist element:playlistsList) {
            setPlaylistSequence(element);
        }


    }


    //creates a sequence for a playlist by looking at the songlist of that playlist, and stores it in tblPlaylist
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
        //System.out.println(playlist.getSequence());
        //updating the database with the new sequence
        DB.updateSQL("update tblPlaylist set fldSequence = '"+sequence+"' where fldPlaylistName = '"+playlist.getPlaylistName()+"'");
    }

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
            //splitting the string
            StringTokenizer IDtokens = new StringTokenizer(sequence, ";");
            //adding ID's from sequence to IDArray as individual elements
            while (IDtokens.hasMoreTokens()){
                //System.out.println(IDtokens.nextToken());
                IDList.add(Integer.parseInt(IDtokens.nextToken()));
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

    /*
    //converting sequence from a playlist into list of ID's
    public void convertSequenceToIDs(Playlist playlist){

        ArrayList<Integer> IDList = new ArrayList<>();

        //splitting the string
        StringTokenizer IDtokens = new StringTokenizer(playlist.getSequence(), ";");
        //adding ID's from sequence to IDArray as individual elements
        while (IDtokens.hasMoreTokens()){
            //System.out.println(IDtokens.nextToken());
            IDList.add(Integer.parseInt(IDtokens.nextToken()));
        }
    }*/

    public void selectNextSong(){
        //check where in the sequence the selectedSong is
        int songIndex = lvSongList.getSelectionModel().getSelectedIndex();
        songIndex++;
        lvSongList.getSelectionModel().select(songIndex);
    }



}
