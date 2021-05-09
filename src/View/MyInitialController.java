package View;

import ViewModel.MyViewModel;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.Observable;
import java.util.Observer;
import java.util.Optional;


public class MyInitialController implements IView, Observer {

    private MyViewModel myViewModel;

    @FXML
    public TextField initialRowSize;
    public TextField initialColumnSize;
    public Button generateButton;

    private MyViewController myViewController;

    public void setViewModel(MyViewModel MyviewModel)
    {
        this.myViewModel = MyviewModel;
    }


    public void generate()
    {
        try {
            this.generateButton.setDisable(true);
            Stage stage = new Stage();
            stage.setTitle("maze");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("Game.fxml").openStream());
            Scene scene = new Scene(root, 600, 600);

            scene.getStylesheets().add(getClass().getResource("ViewStyle.css").toExternalForm()); //* * * CSS is the same as for the initialView * * *
            stage.setScene(scene);
            //--------------
            MyViewController gameView = fxmlLoader.getController();
            gameView.setResizeEvent(scene);
            gameView.setViewModel(this.myViewModel, Integer.valueOf(this.initialRowSize.getText()), Integer.valueOf(this.initialColumnSize.getText())); // * * * need new myViewModel?
            this.myViewModel.addObserver(gameView);
            //--------------

            SetStageCloseEvent(stage);
            stage.show();
            stage.setMinWidth(stage.getWidth());
            stage.setMinHeight(stage.getHeight());

           // stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
            stage.show();
            this.generateButton.setDisable(true);
            gameView.generate();

            this.generateButton.setDisable(false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public void UpdateApplication(int[][] maze)
    {

    }

    @Override
    public void update(Observable o, Object arg)
    {

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
}
