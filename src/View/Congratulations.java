package View;

import Model.MyModel;
import ViewModel.MyViewModel;
import com.sun.glass.ui.View;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.awt.event.KeyListener;
import java.io.File;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import java.util.ResourceBundle;

import static javafx.scene.media.MediaPlayer.INDEFINITE;

public class Congratulations {

    @FXML
    private MediaView mediaView;
    @FXML
    private MediaPlayer mediaPlayer;
    @FXML
    private  Media media;


    private String openingPath; // Opening video path

    public void play(){

        // initial not valid level size
        this.openingPath =  GetResource("Videos/endGame.mp4");



        //Play Opening Video
        PlayVideo(this.openingPath, false);

        mediaPlayer.seek(Duration.ZERO);
        mediaPlayer.play();

        mediaPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {

                Stage stage = (Stage) mediaView.getScene().getWindow();
                stage.close();
            }
        });
    }

    private String GetResource(String path) {
        return getClass().getClassLoader().getResource(path).getPath();
    }

    // Play the video o the specify path
    private void PlayVideo(String videoPath, boolean flag) {
        videoPath = new File(videoPath).getAbsolutePath();
        this.media = new Media(new File(videoPath).toURI().toString());
        this.mediaPlayer = new MediaPlayer(this.media);
        this.mediaView.setMediaPlayer(this.mediaPlayer);
        if(flag) {
            this.mediaPlayer.setCycleCount(INDEFINITE);
        }
        this.mediaPlayer.setAutoPlay(true);
    }

}
