package View;

import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.Optional;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        //initialize Model
        MyModel model = new MyModel();
        model.Start();
        //initialize ViewModel
        MyViewModel viewModel = new MyViewModel(model);
        model.addObserver(viewModel);
        //--------------
        primaryStage.setTitle(" -~-  | WH4T TH3 H3LL |  -~-");
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("Opening.fxml").openStream());

        Scene scene = new Scene(root, 960, 540);


        scene.getStylesheets().add(getClass().getResource("Controller.css").toExternalForm()); //CSS
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);


        Controller view = fxmlLoader.getController();
        //  view.setResizeEvent(scene);
        view.SetViewModel(viewModel);
        viewModel.addObserver(view);


        SetStageCloseEvent(primaryStage);

//        primaryStage.setMinWidth(scene.getWidth());
//        primaryStage.setMinHeight(scene.getHeight());
//
//        primaryStage.setMaxWidth(scene.getWidth());
//        primaryStage.setMaxHeight(scene.getHeight());

//        DoubleBinding w = primaryStage.heightProperty().divide(0.5855);
//        primaryStage.minWidthProperty().bind(w);
//        primaryStage.maxWidthProperty().bind(w);

        primaryStage.show();

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


    public static void main(String[] args) {
        launch(args);
    }
}
