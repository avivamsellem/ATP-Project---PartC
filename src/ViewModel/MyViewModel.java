package ViewModel;

import Model.IModel;
import algorithms.search.AState;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.input.KeyCode;

import java.io.File;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer
{
    private IModel model;

    private int characterPositionRowIndex;
    private int characterPositionColumnIndex;

    public StringProperty characterPositionRow = new SimpleStringProperty("1"); //For Binding
    public StringProperty characterPositionColumn = new SimpleStringProperty("1"); //For Binding


    public MyViewModel(IModel model)
    {
        this.model = model;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o==model)
        {
            setChanged();
            notifyObservers();
        }
    }

    //Getter
    public int getCharacterPositionRow() {
        return this.model.GetCharacterPositionRow();
    }

    public int getCharacterPositionColumn() {
        return this.model.GetCharacterPositionColumn();
    }

    public void generateBoard(int heigth, int width)
    {
        model.GenerateBoard(heigth, width);
    }


    public void moveCharacter(KeyCode movement)
    {
        model.MoveCharacter(movement);
    }

    public int[][] GetBoard()
    {
        return this.model.GetBoard();
    }

    public int[] GetGoalPosition() {
        return this.model.GetGoalPosition();
    }


    public void SaveFile(File file)
    {
        this.model.saveBoard(file);
    }

    public void SetBoard(File file)
    {
        this.model.SetBoard(file);
    }

    public int[] GetBoardSize() {
        return this.model.GetBoardSize();
    }

    public ArrayList<AState> getSolution()
    {
        return this.model.solve(this.model.GetCharacterPositionRow(), this.model.GetCharacterPositionColumn());
    }
}
