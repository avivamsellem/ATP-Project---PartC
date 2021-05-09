package Model;

import algorithms.search.AState;
import javafx.scene.input.KeyCode;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public interface IModel{

    void GenerateBoard(int width, int height);
    void MoveCharacter(KeyCode movement);
    int GetCharacterPositionRow();
    int GetCharacterPositionColumn();
    void Start();
    int[][] GetBoard();
    int[] GetGoalPosition();
    void Stop();

    void saveBoard(File file);
    void SetBoard(File file);

    int[] GetBoardSize();

    ArrayList<AState> solve(int characterPositionRowIndex, int characterPositionColumnIndex);
}
