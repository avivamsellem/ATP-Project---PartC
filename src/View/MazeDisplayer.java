package View;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MazeDisplayer extends Canvas {

    private int[][] maze;
    private int goalPositionRow = 1;
    private int goalPositionColumn = 1;

    CharacterDisplayer charDisp;

    //region Properties
    private StringProperty ImageFileNameWall = new SimpleStringProperty();
    private StringProperty ImageFileNameGoal = new SimpleStringProperty();
    private StringProperty ImageFileNameField = new SimpleStringProperty();

    //Getters
    public String getImageFileNameWall()
    {
        return ImageFileNameWall.get();
    }


    public String getImageFileNameGoal()
    {
        return ImageFileNameGoal.get();
    }

    public String getImageFileNameField()
    {
        return ImageFileNameField.get();
    }


    //Setter
    public void setImageFileNameWall(String imageFileNameWall)
    {
        this.ImageFileNameWall.set(imageFileNameWall);
    }

    public void setImageFileNameGoal(String imageFileNameCharacter)
    {
        this.ImageFileNameGoal.set(imageFileNameCharacter);
    }

    public void setImageFileNameField(String imageFileNameField)
    {
        this.ImageFileNameField.set(imageFileNameField);
    }



    public void setBoard(int[][] maze) {
        this.maze = maze;

        redraw();
    }


    public void setGoalPosition(int[] pos)
    {
        if(pos.length == 2) {
            this.goalPositionRow = pos[0];
            this.goalPositionColumn = pos[1];
        }
    }


    public void redraw() {
        if (maze != null) {
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            double cellHeight = canvasHeight / maze.length;
            double cellWidth = canvasWidth / maze[0].length;

            try {
                Image wallImage = new Image(new FileInputStream(ImageFileNameWall.get()));
                Image goalImage = new Image(new FileInputStream(ImageFileNameGoal.get()));
                Image fieldImage = new Image(new FileInputStream(ImageFileNameField.get()));

                GraphicsContext gc = getGraphicsContext2D();
                gc.clearRect(0, 0, getWidth(), getHeight());

                //Draw Maze
                for (int i = 0; i < maze.length; i++) {
                    for (int j = 0; j < maze[i].length; j++) {
                        if (maze[i][j] == 1) {
                            //gc.fillRect(i * cellHeight, j * cellWidth, cellHeight, cellWidth);
                            gc.drawImage(wallImage, j * cellWidth, i * cellHeight, cellWidth, cellHeight);
                        }
                        else
                        {
                            if(i != goalPositionRow || j != goalPositionColumn)
                            {
                                    gc.drawImage(fieldImage, j * cellWidth, i * cellHeight, cellWidth, cellHeight);
                            }
                        }
                    }
                }
                gc.drawImage(fieldImage, goalPositionColumn * cellWidth, goalPositionRow * cellHeight, cellWidth, cellHeight);
                gc.drawImage(goalImage, goalPositionColumn * cellWidth, goalPositionRow * cellHeight, cellWidth, cellHeight);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    //endregion

}
