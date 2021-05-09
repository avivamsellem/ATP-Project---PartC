package Model;

import Client.*;
import IO.*;
import Server.*;
import algorithms.mazeGenerators.*;
import algorithms.search.*;
import javafx.scene.input.KeyCode;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Observable;

public class MyModel extends Observable implements IModel
{
    private Maze maze;
    private int [][] board;
    private Server mazeGeneratingServer;
    private Server solveSearchProblemServer;
    private int currentPosition_Row;
    private int currentPosition_Col;
        private int goalPositionRow;
        private int goalPositionColumn;
    private ArrayList<AState> solution;

    public MyModel(){
        this.mazeGeneratingServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        this.solveSearchProblemServer = new Server(5401, 1000, new ServerStrategySolveSearchProblem());
    }

    @Override
    public void Start() {
        this.solveSearchProblemServer.start();
        this.mazeGeneratingServer.start();
    }


    @Override
    public void GenerateBoard(int rows, int columns)
    {
        GenerateMaze(rows, columns);

        setChanged();
//        notifyObservers();
    }


    public void GenerateMaze(int width, int height)
    {
        Client client;
        try {
            client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {

                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{width, height };
                        toServer.writeObject(mazeDimensions);
                        toServer.flush();
                        byte[] compressedMaze1 = new byte[width * height + 24];
                        byte b = 0;
                        int i = 0;
                        while (true)
                        {
                            try{
                                b = fromServer.readByte();
                                compressedMaze1[i] = b;
                                i++;
                            }
                            catch (Exception e) {
                                break;
                            }
                        }

                        byte[] compressedMaze = new byte[i];

                        for(i = 0 ; i < compressedMaze.length ; i++)
                        {
                            compressedMaze[i] = compressedMaze1[i];
                        }

                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[width * height + 24];
                        is.read(decompressedMaze);
                        Maze returnedMaze = new Maze(decompressedMaze);

                        maze = returnedMaze;
                        board = returnedMaze.getArray();
                        currentPosition_Col = maze.getStartPosition().getColumnIndex();
                        currentPosition_Row = maze.getStartPosition().getRowIndex();
                        goalPositionRow = maze.getGoalPosition().getRowIndex();
                        goalPositionColumn = maze.getGoalPosition().getColumnIndex();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<AState> solve(int row, int col)
    {
        this.solution = null;
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                      //  MyMazeGenerator mg = new MyMazeGenerator();
                      //  Maze maze = mg.generate(50, 50);
                      //  maze.print();
                        maze.setStartPosition(currentPosition_Row,currentPosition_Col);
                        toServer.writeObject(maze); //send maze to server
                        toServer.flush();
                        Solution mazeSolution = (Solution) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server

                        //Print Maze Solution retrieved from the server
                     //   System.out.println(String.format("Solution steps: %s", mazeSolution));
                        ArrayList<AState> mazeSolutionSteps = mazeSolution.getSolutionPath();
//                        for (int i = 0; i < mazeSolutionSteps.size(); i++) {
//                            System.out.println(String.format("%s. %s", i, mazeSolutionSteps.get(i).toString()));
//                        }
                        solution = mazeSolutionSteps;

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        if(this.solution == null)
        {
            System.out.println("solve method at MyModel returned null solution");
            return this.solution;
        }

        return this.solution;
    }

    @Override
    public void MoveCharacter(KeyCode movement)
    {
        if(movement==KeyCode.UP || movement==KeyCode.NUMPAD8|| movement==KeyCode.DIGIT8){
            if(CheckMovement(this.currentPosition_Row - 1,this.currentPosition_Col)) {
                currentPosition_Row--;
            }
        }
        else if(movement==KeyCode.DOWN || movement==KeyCode.NUMPAD2 || movement==KeyCode.DIGIT2){
            if(CheckMovement(this.currentPosition_Row + 1,this.currentPosition_Col)) {
                currentPosition_Row++;
            }
        }
        else if(movement==KeyCode.RIGHT || movement==KeyCode.NUMPAD6|| movement==KeyCode.DIGIT6){
            if(CheckMovement(this.currentPosition_Row,this.currentPosition_Col + 1))
            {
                currentPosition_Col++;
            }
        }
        else if(movement==KeyCode.LEFT || movement==KeyCode.NUMPAD4|| movement==KeyCode.DIGIT4){
            if(CheckMovement(this.currentPosition_Row,this.currentPosition_Col - 1))
            {
                currentPosition_Col--;
            }
        }
        else if(movement==KeyCode.NUMPAD9|| movement==KeyCode.DIGIT9){
            if(CheckMovement(this.currentPosition_Row - 1,this.currentPosition_Col + 1))
            {
                currentPosition_Col++;
                currentPosition_Row--;
            }
        }
        else if(movement==KeyCode.NUMPAD3|| movement==KeyCode.DIGIT3){
        if(CheckMovement(this.currentPosition_Row + 1,this.currentPosition_Col + 1))
        {
            currentPosition_Col++;
            currentPosition_Row++;
        }
        }
        else if(movement==KeyCode.NUMPAD1|| movement==KeyCode.DIGIT1){
            if(CheckMovement(this.currentPosition_Row + 1,this.currentPosition_Col - 1))
            {
                currentPosition_Col--;
                currentPosition_Row++;
            }
        }
        else if(movement==KeyCode.NUMPAD7|| movement==KeyCode.DIGIT7) {
            if(CheckMovement(this.currentPosition_Row - 1,this.currentPosition_Col - 1))
            {
                currentPosition_Col--;
                currentPosition_Row--;
            }
        }

        System.out.println("row pos: " + currentPosition_Row);
        System.out.println("col pos: " + currentPosition_Col);

        setChanged();
        notifyObservers();
    }

    /**
     * this method checks if the movement on the board is llegal.
     * @param row
     * @param col
     * @return
     */
    private boolean CheckMovement (int row, int col)
    {

        if(this.maze.checkValidPosition(row,col) && this.board[row][col] == 0)
        {
            return true;
        }
        return false;
    }

    @Override
    public int[][] GetBoard() {
        return board;
    }

    @Override
    public int[] GetGoalPosition()
    {
        return new int[]{this.maze.getGoalPosition().getRowIndex(), this.maze.getGoalPosition().getColumnIndex()};
    }



    @Override
    public int GetCharacterPositionRow() {
        return this.currentPosition_Row;
    }

    @Override
    public int GetCharacterPositionColumn() {
        return this.currentPosition_Col;
    }


    @Override
    public void saveBoard(File file) {
        if (file != null) {
            try {
                FileOutputStream fos = new FileOutputStream(file);
                ObjectOutputStream oos = new ObjectOutputStream(fos);

                this.maze.setStartPosition(this.currentPosition_Row, this.currentPosition_Col);
                oos.writeObject(this.maze);
                oos.flush();

                oos.close();
                fos.close();

            } catch (IOException e) {
                e.getStackTrace();
            }
        }
    }

    @Override
    public void SetBoard(File file)
    {
        try {
            InputStream in = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(in);

            this.maze = (Maze)ois.readObject();

            this.currentPosition_Row = this.maze.getStartPosition().getRowIndex();
            this.currentPosition_Col = this.maze.getStartPosition().getColumnIndex();

            this.board = this.maze.getArray();

            ois.close();
            in.close();

            setChanged();
            notifyObservers();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int[] GetBoardSize() {
        return new int[]{this.board.length, this.board[0].length};
    }



    public void Stop() {
        this.solveSearchProblemServer.stop();
        this.mazeGeneratingServer.stop();
    }
}
