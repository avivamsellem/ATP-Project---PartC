package View;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;


import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class CharacterDisplayer extends Canvas {

    private int characterPositionRow = 1;
    private int characterPositionColumn = 1;

    private int mazeHeight;
    private int mazeWeidth;



    private boolean isStand = true;
    private boolean prevChar = true;

    private int isMoveRight = 0;

    private StringProperty ImageFileNameCharacter = new SimpleStringProperty();
    private StringProperty ImageFileNameSurface = new SimpleStringProperty();
    private StringProperty ImageCharacterStandLookLeft = new SimpleStringProperty();
    private StringProperty ImageCharacterStandLookRight = new SimpleStringProperty();
    private StringProperty ImageCharacterWalkLookRight = new SimpleStringProperty();
    private StringProperty ImageCharacterWalkLookLeft = new SimpleStringProperty();


    //Getters
    public String getImageFileNameCharacter()
    {
        return ImageFileNameCharacter.get();
    }
    public String getImageFileNameSurface()
    {
        return ImageFileNameSurface.get();
    }
    public String getImageCharacterStandLookLeft()
    {
        return ImageCharacterStandLookLeft.get();
    }
    public String getImageCharacterStandLookRight()
    {
        return ImageCharacterStandLookRight.get();
    }
    public String getImageCharacterWalkLookRight()
    {
        return ImageCharacterWalkLookRight.get();
    }
    public String getImageCharacterWalkLookLeft()
    {
        return ImageCharacterWalkLookLeft.get();
    }


    //Setters
    public void setImageFileNameCharacter(String imageFileNameCharacter)
    {
        this.ImageFileNameCharacter.set(imageFileNameCharacter);
    }

    public void setImageFileNameSurface(String imageFileNameSurface)
    {
        this.ImageFileNameSurface.set(imageFileNameSurface);
    }

    public void setImageCharacterStandLookLeft(String imageCharacterStandLookLeft)
    {
        this.ImageCharacterStandLookLeft.set(imageCharacterStandLookLeft);
    }
    public void setImageCharacterStandLookRight(String imageCharacterStandLookRight)
    {
        this.ImageCharacterStandLookRight.set(imageCharacterStandLookRight);
    }
    public void setImageCharacterWalkLookRight(String imageCharacterWalkLookRight)
    {
        this.ImageCharacterWalkLookRight.set(imageCharacterWalkLookRight);
    }
    public void setImageCharacterWalkLookLeft(String imageCharacterWalkLookLeft)
    {
        this.ImageCharacterWalkLookLeft.set(imageCharacterWalkLookLeft);
    }

    public void setCharacter(int chararcterRow, int characterColumn, int mazeHeight, int mazeWeidth)
    {
        this.characterPositionRow = chararcterRow;
        this.characterPositionColumn = characterColumn;
        this.mazeHeight = mazeHeight;
        this.mazeWeidth = mazeWeidth;

        redraw();
    }

    public void UpdateCharacterPosition(int row, int column)
    {
        isMoveRight =  column - this.characterPositionColumn; // if 1 - moved right, -1 - left, else (=0) - nothing
        this.characterPositionRow = row;
        this.characterPositionColumn = column;


        redraw();
    }

    //Yarden i changed here to public
    public void redraw() {

        double canvasHeight = getHeight();
        double canvasWidth = getWidth();
        double cellHeight = canvasHeight / mazeHeight;
        double cellWidth = canvasWidth / mazeWeidth;

        try {
            Image characterImage = new Image(new FileInputStream(ImageCharacterWalkLookRight.get()));

            if(isMoveRight == 1 || (isMoveRight == 0 && prevChar))
            {
                if(isStand)
                {
                    this.isStand = false;
                    characterImage = new Image(new FileInputStream(ImageCharacterWalkLookRight.get()));
                    this.prevChar = true; // true - moved right last time, flase - moved left last time.
                }
                else {
                    this.isStand = true;
                    characterImage = new Image(new FileInputStream(ImageCharacterStandLookRight.get()));
                    this.prevChar = true; // true - moved right last time, flase - moved left last time.
                }
            }
            else
            {
                if(isStand)
                {
                    this.isStand = false;
                    characterImage = new Image(new FileInputStream(ImageCharacterWalkLookLeft.get()));
                    this.prevChar = false; // true - moved right last time, flase - moved left last time.
                }
                else
                {
                    this.isStand = true;
                    characterImage = new Image(new FileInputStream(ImageCharacterStandLookLeft.get()));
                    this.prevChar = false; // true - moved right last time, flase - moved left last time.
                }
            }
          // Image characterImage = new Image(new FileInputStream(ImageFileNameCharacter.get()));
            Image surfaceImage = new Image(new FileInputStream(ImageFileNameSurface.get()));

            GraphicsContext gc = getGraphicsContext2D();
            gc.clearRect(0, 0, getWidth(), getHeight());

            //Draw surface
            gc.drawImage(surfaceImage, characterPositionColumn * cellWidth, characterPositionRow * cellHeight, cellWidth, cellHeight);

            //Draw charecter
            gc.drawImage(characterImage, characterPositionColumn * cellWidth, characterPositionRow * cellHeight, cellWidth, cellHeight);

        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

}