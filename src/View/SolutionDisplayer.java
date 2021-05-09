package View;

import algorithms.search.AState;
import algorithms.search.MazeState;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class SolutionDisplayer extends Canvas {
    private ArrayList<AState> solutionPath;
    private int mazeHeight;
    private int mazeWeidth;
    private int characterRow;
    private int chracterColumn;


    //region Properties
    private StringProperty ImageFileNameSolution = new SimpleStringProperty();
    private StringProperty ImageFileNameWall = new SimpleStringProperty();

    //Getters
    public String getImageFileNameSolution() {
        return ImageFileNameSolution.get();
    }

    public String getImageFileNameWall() {
        return ImageFileNameSolution.get();
    }

    //Setter
    public void setSolutionAsNull()
    {
        this.solutionPath = null;

        redraw();
    }

    public void setImageFileNameSolution(String imageFileNameSolution) {
        this.ImageFileNameSolution.set(imageFileNameSolution);
    }

    public void setImageFileNameWall(String imageFileNameWall) {
        this.ImageFileNameWall.set(imageFileNameWall);
    }


    public void checkIfCharcterIsOnSolutionPath() {

    }

    public void setSolution(ArrayList<AState> solutionPath, int mazeHeight, int mazeWeidth, int charRow, int charColumn) {
        if (solutionPath != null) {
            this.solutionPath = solutionPath;
            this.mazeHeight = mazeHeight;
            this.mazeWeidth = mazeWeidth;
            this.characterRow = charRow;
            this.chracterColumn = charColumn;

            redraw();
        }
    }

    public void redraw() {
        double canvasHeight = getHeight();
        double canvasWidth = getWidth();
        double cellHeight = canvasHeight / mazeHeight;
        double cellWidth = canvasWidth / mazeWeidth;


        Image solutionImage = null;
        try {
            solutionImage = new Image(new FileInputStream(ImageFileNameSolution.get()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());

        int lastIndex = this.solutionPath.size() - 1;

        int counter = 0;
        for (AState state : this.solutionPath)
        {


            if (counter == 0 || counter == this.solutionPath.size() - 1) {
                counter++;
                continue;
            }
            counter++;
            try {

                int rowSolution = ((MazeState) state).getPositionFromState().getRowIndex();
                int columnSolution = ((MazeState) state).getPositionFromState().getColumnIndex();

                rowSolution = ((MazeState) state).getPositionFromState().getRowIndex();
                columnSolution = ((MazeState) state).getPositionFromState().getColumnIndex();

                //Draw solution
                gc.drawImage(solutionImage, columnSolution * cellWidth, rowSolution * cellHeight, cellWidth, cellHeight);
            } catch (
                    Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void CheckIfCharcterOnSolution(ArrayList<AState> currentSolutionPath, int heigth, int width, int characterPositionRow, int characterPositionColumn)
    {
        double canvasHeight = getHeight();
        double canvasWidth = getWidth();
        double cellHeight = canvasHeight / mazeHeight;
        double cellWidth = canvasWidth / mazeWeidth;

        Image wallImage = null;
        try {
            wallImage = new Image(new FileInputStream(ImageFileNameWall.get()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());


        int rowGoalState = ((MazeState)(currentSolutionPath.get(0))).getPositionFromState().getRowIndex();
        int columnGoalState = ((MazeState)(currentSolutionPath.get(0))).getPositionFromState().getColumnIndex();

        if(!checkIfCurrentCharacterStateInSolution(currentSolutionPath, characterPositionRow, characterPositionColumn))
        {
            for(AState state: currentSolutionPath) {

                try {
                    int rowSolution = ((MazeState) state).getPositionFromState().getRowIndex();
                    int columnSolution = ((MazeState) state).getPositionFromState().getColumnIndex();

                    if(characterPositionRow == rowSolution && characterPositionColumn == rowSolution)
                    {
                        continue;
                    }
                    if(rowSolution == rowGoalState || columnGoalState == columnGoalState)
                    {
                        continue;
                    }


                    //Draw walls
                    gc.drawImage(wallImage, columnSolution * cellHeight, rowSolution * cellWidth, cellHeight, cellWidth);
                } catch (
                        Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean checkIfCurrentCharacterStateInSolution(ArrayList<AState> currentSolutionPath, int characterPositionRow, int characterPositionColumn)
    {
        for(AState state: currentSolutionPath)
        {
            int currentRowSolution = ((MazeState) state).getPositionFromState().getRowIndex();
            int currentColumnSolution = ((MazeState) state).getPositionFromState().getColumnIndex();

            if(currentRowSolution == characterPositionRow && currentColumnSolution == characterPositionColumn)
            {
                return true;
            }
        }
        return false;
    }
}






