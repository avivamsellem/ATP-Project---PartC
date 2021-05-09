package View;

import ViewModel.MyViewModel;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
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

import java.io.File;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import java.util.ResourceBundle;

import static javafx.scene.media.MediaPlayer.INDEFINITE;

public class Controller implements Observer, Initializable {

    @FXML
    private MediaView mediaView;
    @FXML
    private MediaPlayer mediaPlayer;
    @FXML
    private  Media media;
    @FXML
    private Button btn_play;
    @FXML
    private Button btn_select;
    @FXML
    private StackPane stackPane;

    private MyViewModel myViewModel;

    // Indicate of Game Level
    private int level;

    private String openingPath; // Opening video path
    private String crossPath; // Cross video path
    private String levelPath; // Cross video path

    private MediaPlayer mediaPlayerMusic; // video

    private String musicPath;
    private Media sound;


    public void SetViewModel(MyViewModel viewModel){
        this.myViewModel = viewModel;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // initial not valid level size
        this.openingPath =  GetResource("Videos/Opening.mp4");
        this.crossPath = GetResource("Videos/cross.mp4");
        this.levelPath = GetResource("Videos/amature.mp4");

        this.musicPath = GetResource("music/themeSong.mp3");

        this.level = 3;


        //hide play game button
        this.btn_play.setVisible(false);
        this.btn_select.setVisible(false);

        this.btn_play.setDisable(true);

        //set timer to make button visible
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(6000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                btn_play.setVisible(true);

                try {
                    Thread.sleep(200);


                    btn_play.setVisible(false);


                    Thread.sleep(200);


                    btn_play.setVisible(true);

                    Thread.sleep(200);


                    btn_play.setVisible(false);

                    Thread.sleep(200);


                    btn_play.setVisible(true);


                    Thread.sleep(3500);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                btn_play.setDisable(false);
            }
        }).start();

        StartMusic(this.musicPath);

        //Play Opening Video
        PlayVideo(this.openingPath, false);

        //resize in fix ratio
        DoubleProperty width = this.mediaView.fitWidthProperty();
        DoubleProperty height = this.mediaView.fitHeightProperty();
        width.bind(Bindings.selectDouble(this.mediaView.sceneProperty(), "width"));
        height.bind(Bindings.selectDouble(this.mediaView.sceneProperty(), "height"));

        mediaPlayer.seek(Duration.ZERO);
        mediaPlayer.play();

        // Play cross meanwhile
        mediaPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {

                PlayVideo(crossPath, true);
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

    // Play Music
    private void StartMusic(String musicPath) {
        sound = new Media(new File(musicPath).toURI().toString());
        mediaPlayerMusic = new MediaPlayer(sound);
        mediaPlayerMusic.setVolume(0.09);
        startTune();
    }

    public void showLevelMenu(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                btn_play.setVisible(false);
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                btn_select.setVisible(true);
            }
        }).start();

        this.levelPath = GetResource("Videos/Level.mp4");

        PlayVideo(this.levelPath, false);

        mediaPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {

                //Play cross
                String amaturePath = GetResource("Videos/amature.mp4");
                mediaPlayer.stop();
                PlayVideo(amaturePath, false);

                level = 0;

                mediaView.requestFocus();
            }
        });
    }

    private void startTune(){
        mediaPlayerMusic.setCycleCount(INDEFINITE);
        mediaPlayerMusic.play();
    }

    public void initialGame(MouseEvent mouseEvent) {

        try {
            this.btn_select.setDisable(true);

            Stage stage = new Stage();
            stage.setTitle("Maze");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("Game.fxml").openStream());
            Scene scene = new Scene(root, 600, 629);

            scene.getStylesheets().add(getClass().getResource("ViewStyle.css").toExternalForm()); //* * * CSS is the same as for the initialView * * *
            stage.setScene(scene);
            //--------------
            MyViewController gameView = fxmlLoader.getController();
            gameView.setResizeEvent(scene);
            int boardSize = GetBoarSize();
            gameView.setViewModel(this.myViewModel, boardSize,boardSize);
            MyViewController view = fxmlLoader.getController();
            view.setResizeEvent(scene);
            this.myViewModel.addObserver(gameView);

            SetStageCloseEvent(stage);
            stage.show();
            stage.setMinWidth(stage.getWidth());
            stage.setMinHeight(stage.getHeight());

            // stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
            stage.show();
            gameView.generate();

            this.btn_select.setDisable(false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int GetBoarSize() {
        if(this.level == 0){
            return 14;
        }
        else if(this.level == 1) {
            return 61;
        }
        else {
            return 91;
        }
    }

    public void onKeyPress(KeyEvent keyEvent){
        if(this.level < 3) {
            KeyCode movement = keyEvent.getCode();

            if (movement == KeyCode.UP || movement == KeyCode.NUMPAD8 || movement == KeyCode.DIGIT8) {
                this.level = (((this.level - 1) < 0)? 2:(this.level - 1));
                updateMenu();
            } else if (movement == KeyCode.DOWN || movement == KeyCode.NUMPAD2 || movement == KeyCode.DIGIT2) {
                this.level = ((this.level + 1) % 3);
                updateMenu();
            }
        }

        keyEvent.consume();
    }

    private void updateMenu() {
        if(this.level == 0){
            String menu1 = GetResource("Videos/amature.mp4");
            PlayVideo(menu1, false);
        }
        else if(this.level == 1){
            String menu1 = GetResource("Videos/professional.mp4");
            PlayVideo(menu1, false);
        }
        else if(this.level == 2){
            String menu1 = GetResource("Videos/playa.mp4");
            PlayVideo(menu1, false);
        }
    }


    public void mouseClick(MouseEvent mouseEvent)
    {
        this.mediaView.requestFocus();
    }

    private void SetStageCloseEvent(Stage primaryStage) {
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent windowEvent) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    // ... user chose OK
                    // Close program
                } else {
                    // ... user chose CANCEL or closed the dialog
                    windowEvent.consume();
                }
            }
        });
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
