package View;

import Server.Configurations;
import ViewModel.MyViewModel;
import algorithms.search.AState;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;


public class MyViewController implements IView, Observer , Initializable {

    public CharacterDisplayer characterDisplayer;
    @FXML
    private MyViewModel myViewModel;
    public MazeDisplayer mazeDisplayer;
    public MenuItem generateButton;
    public MenuItem solveButton;
    public TextField currentRowLabel;
    public TextField currentColumnLabel;
    public SolutionDisplayer solutionDisplayer;
    @FXML
    private MenuBar menuBar;

    private int initialRowSize;
    private int initialColumnSize;
    private int currentRow;
    private int currentColumn;
    private ArrayList<AState> solutionPath;
    private boolean isSolvePathDisplayer;

    public void setViewModel(MyViewModel MyviewModel, int initialRowSize, int initialColumnSize) {
        this.initialRowSize = initialRowSize;
        this.initialColumnSize = initialColumnSize;
        this.myViewModel = MyviewModel;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == myViewModel) {
            UpdateApplication(myViewModel.GetBoard());
            generateButton.setDisable(false);
        }

        if(this.myViewModel.getCharacterPositionColumn() == this.myViewModel.GetGoalPosition()[1] && this.myViewModel.getCharacterPositionRow() == this.myViewModel.GetGoalPosition()[0])
        {
            try {
                Stage stage = new Stage();
                stage.setTitle("Congratulations");
                FXMLLoader fxmlLoader = new FXMLLoader();
                Parent root = fxmlLoader.load(getClass().getResource("Congratulations.fxml").openStream());
                Scene scene = new Scene(root, 950, 530);


                Congratulations Congratulations = fxmlLoader.getController();
                Congratulations.play();
                stage.setScene(scene);
                //--------------

                stage.setResizable(false);
                stage.showAndWait();
//                stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
//                stage.show();

                Stage currStage = (Stage) menuBar.getScene().getWindow();

                currStage.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void UpdateApplication(int[][] board) {
        int characterPositionRow = myViewModel.getCharacterPositionRow();
        int characterPositionColumn = myViewModel.getCharacterPositionColumn();
        characterDisplayer.UpdateCharacterPosition(characterPositionRow, characterPositionColumn);

        int heigth = this.initialRowSize;
        int width = this.initialColumnSize;

        this.solutionPath = this.myViewModel.getSolution();
        solutionDisplayer.CheckIfCharcterOnSolution(this.solutionPath, heigth, width, characterPositionRow, characterPositionColumn);
        this.isSolvePathDisplayer = false;
    }

    public void generate() {
        try {
            generateButton.setDisable(true);

            this.isSolvePathDisplayer = false;

            int heigth = this.initialRowSize;
            int width = this.initialColumnSize;

            myViewModel.generateBoard(heigth, width);

            int[] p = this.myViewModel.GetGoalPosition();
            this.mazeDisplayer.setGoalPosition(p);
            this.mazeDisplayer.setBoard(myViewModel.GetBoard());

            int charRow = this.myViewModel.getCharacterPositionRow();
            int charColumn = this.myViewModel.getCharacterPositionColumn();
            this.characterDisplayer.setCharacter(charRow, charColumn, heigth, width);

            solveButton.setDisable(false);
            generateButton.setDisable(false);
            //yatden - i Added here
            this.isSolvePathDisplayer = false;

            this.mazeDisplayer.requestFocus();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void setResizeEvent(Scene scene) {

        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                mazeDisplayer.setWidth(newSceneWidth.doubleValue());
                mazeDisplayer.redraw();
                characterDisplayer.setWidth(newSceneWidth.doubleValue());
                characterDisplayer.redraw();
                solutionDisplayer.setWidth(newSceneWidth.doubleValue());
                if (isSolvePathDisplayer) {
                    solutionDisplayer.redraw();
                }
            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {

                mazeDisplayer.setHeight(newSceneHeight.doubleValue() - 29);
                mazeDisplayer.redraw();
                characterDisplayer.setHeight(newSceneHeight.doubleValue() - 29);
                characterDisplayer.redraw();
                solutionDisplayer.setHeight(newSceneHeight.doubleValue() - 29);
                if (isSolvePathDisplayer) {
                    solutionDisplayer.redraw();
                }
            }
        });
    }

    //shows the about window
    public void viewAbout(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            stage.setTitle("About");
            Pane pane = new Pane();

            String imagePath = getClass().getClassLoader().getResource("Images/about.jpg").getPath();
            Image image = new Image((new FileInputStream(imagePath)));
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(500);
            imageView.setFitWidth(650);
            pane.getChildren().add(imageView);

            Scene scene = new Scene(pane,650,500);

            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void KeyPressed(KeyEvent keyEvent) {
        this.myViewModel.moveCharacter(keyEvent.getCode());
        keyEvent.consume();

    }


    public void solve() {
        int heigth = this.initialRowSize;
        int width = this.initialColumnSize;

        int charRow = this.myViewModel.getCharacterPositionRow();
        int charColumn = this.myViewModel.getCharacterPositionColumn();

        this.solutionPath = this.myViewModel.getSolution();
        this.solutionDisplayer.setSolution(solutionPath, heigth, width, charRow, charColumn);
        this.isSolvePathDisplayer = true;
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        this.mazeDisplayer.requestFocus();
    }

    public void SaveBoard(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();

        //Set extension filter for text files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("mazeFile", "*.*");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            this.myViewModel.SaveFile(file);
        }
    }

    public void LoadBoard(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            this.myViewModel.SetBoard(file);
        }

        int[] boardSize = this.myViewModel.GetBoardSize();
        int heigth = boardSize[0];
        int width = boardSize[1];

        this.mazeDisplayer.setGoalPosition(this.myViewModel.GetGoalPosition());
        this.mazeDisplayer.setBoard(this.myViewModel.GetBoard());

        int charRow = this.myViewModel.getCharacterPositionRow();
        int charColumn = this.myViewModel.getCharacterPositionColumn();
        this.characterDisplayer.setCharacter(charRow, charColumn, heigth, width);
        this.characterDisplayer.UpdateCharacterPosition(charRow, charColumn);
    }

    public void handleScroll(ScrollEvent scrollEvent) {
        try {
            AnimatedZoomOperator zoomOperatorMaze = new AnimatedZoomOperator();
            AnimatedZoomOperator zoomOperatorChar = new AnimatedZoomOperator();
            AnimatedZoomOperator zoomOperatorSol = new AnimatedZoomOperator();

            double zoomFactor;
            if (scrollEvent.isControlDown()) {
                zoomFactor = 1.5;
                double deltaY = scrollEvent.getDeltaY();
                if (deltaY < 0) {
                    zoomFactor = 1 / zoomFactor;
                }
                zoomOperatorMaze.zoom(mazeDisplayer, zoomFactor, scrollEvent.getSceneX(), scrollEvent.getSceneY());
                zoomOperatorChar.zoom(characterDisplayer, zoomFactor, scrollEvent.getSceneX(), scrollEvent.getSceneY());
                zoomOperatorSol.zoom(solutionDisplayer, zoomFactor, scrollEvent.getSceneX(), scrollEvent.getSceneY());

                scrollEvent.consume();
            }
        } catch (NullPointerException e) {
            scrollEvent.consume();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setBestFS(ActionEvent actionEvent) {
        Configurations.setASearchingAlgorithm("BFS");
    }

    public void setBFS(ActionEvent actionEvent) {
        Configurations.setASearchingAlgorithm("Breadth First Search");
    }

    public void setDFS(ActionEvent actionEvent) {
        Configurations.setASearchingAlgorithm("DFS");
    }

    public void setSimpleGenerator(ActionEvent actionEvent) {
        Configurations.setAMazeGenerator("SimpleMazeGenerator");
    }

    public void setMyGenerator(ActionEvent actionEvent) {
        Configurations.setAMazeGenerator("MyMazeGenerator");
    }
}
