package BurnsKyle_LiangTony_Maze;

import java.util.*;

public class Maze {

   private char[][] maze;
   private boolean[][] tfmaze;

   private int size;
   private int arrayLength;
   private Random myRandGen;

	public Maze(int size, int seed) {
		//Created constructor
      this.size = size;
      myRandGen=new java.util.Random(seed);
      arrayLength = 2 * size + 1;
      maze = new char[arrayLength][arrayLength];
	}
   //==========================================================================================================================
	// Getters and setters
   //==========================================================================================================================
   public char[][] getMaze()
   {
      return maze;
   }

   public int getArrayLength() {
      return arrayLength;
   }
   public int getSize() {
      return size;
   }

   public boolean[][] gettfmaze(){
      return tfmaze;
   }
   //==========================================================================================================================
   // Public Methods
   //==========================================================================================================================
   public void generateRoom() {
		//System.out.println(maze[0].length);
	      for (int i = 0; i < arrayLength; i += 2) {
	         for (int j = 0; j < arrayLength; j++) {
	            if (j % 2 == 0) {
	               maze[i][j] = '+';
	            } else {
	               maze[i][j] = '-';
	            }
	         }
	      }
	      for (int i = 1; i < arrayLength; i += 2) {
	         for (int j = 0; j < arrayLength; j++) {
	            if (j % 2 == 0) {
	               maze[i][j] = '|';
	            } else {
	               maze[i][j] = ' ';
	            }
	         }
	      }
	      maze[0][1] = ' ';
	      maze[arrayLength - 1][arrayLength - 2] = ' ';
	}
//=============================================================================================================================
	public void removeWalls() {
      initializeTfmaze();
		Stack<Cell> cellStack = new Stack<>();
		int totalCells = size*size;
		Cell currentCell = new Cell(1,1);
		int visitedCells = 1;
		tfmaze[1][1] = true;
      while(visitedCells<totalCells) {

         ArrayList<Cell> neighbors = findNeighbors(currentCell);
         if (neighbors.size() > 0) {
            Cell randomNeighbor = neighbors.get((int) (myrandom() * neighbors.size()));
            tfmaze[randomNeighbor.getY()][randomNeighbor.getX()] = true;
            int wallx = (randomNeighbor.getX() + currentCell.getX()) / 2;
            int wally = (randomNeighbor.getY() + currentCell.getY()) / 2;
            maze[wally][wallx] = ' ';
            cellStack.push(currentCell);
            currentCell = randomNeighbor;
            visitedCells += 1;
         } else {
            currentCell = cellStack.pop();
         }

      }
      //printMaze(maze);
	}
   //==========================================================================================================================
   public void bfsTraversing () {
	   char[][] mazeCopy = copy2dArray(maze);
	   char[][] routeMaze = copy2dArray(maze);
	   Cell end = new Cell(arrayLength-2, arrayLength-2);
      initializeTfmaze();
      Queue<Cell> queue = new ArrayDeque<>();
      Cell head = new Cell(1,1);
      queue.offer(head);
      int counter = 0;
      while (!queue.isEmpty()) {
         Cell cur = queue.poll();
         int curX = cur.getX();
         int curY = cur.getY();
         mazeCopy[cur.getY()][cur.getX()] = (char) (counter % 10 + 48);


         if (curX == arrayLength - 2 && curY == arrayLength - 2) {
            end = cur;
            break;
         }
         counter ++;
         ArrayList<Cell> visitableNeighbors = new ArrayList<>();
         tfmaze[curY][curX] = true;
         //look up
         if (curY - 2 > 0 && mazeCopy[curY - 1][curX] == ' ' && !tfmaze[curY - 2][curX]) {
            Cell nextCell = new Cell(cur.getX(), cur.getY() - 2);
            nextCell.setParent(cur);
            visitableNeighbors.add(nextCell);
         }
         //look left
         if (curX - 2 > 0 && mazeCopy[curY][curX - 1] == ' ' && !tfmaze[curY][curX - 2]) {
            Cell nextCell = new Cell(cur.getX() - 2, cur.getY());
            nextCell.setParent(cur);

            visitableNeighbors.add(nextCell);
         }
         //look down
         if (curY + 2 < arrayLength && mazeCopy[curY + 1][curX] == ' ' && !tfmaze[curY + 2][curX]) {
            Cell nextCell = new Cell(cur.getX(), cur.getY() + 2);
            nextCell.setParent(cur);
            visitableNeighbors.add(nextCell);
         }
         //look right
         if (curX + 2 < arrayLength && mazeCopy[curY][curX+ 1] == ' ' && !tfmaze[curY][curX + 2]) {
            Cell nextCell = new Cell(cur.getX() + 2, cur.getY());
            nextCell.setParent(cur);
            visitableNeighbors.add(nextCell);
         }
         for (Cell c: visitableNeighbors) {
            queue.offer(c);
         }
      }
      System.out.println("traversal using BFS: ");
      printMaze(mazeCopy);
      fillInHashTag(end,routeMaze);
      System.out.println("\n"+"the shortest route generated by BFS: ");
      printMaze(routeMaze);
   }

//   public void dfsTraversing () {
//      char[][] mazeCopy = copy2dArray(maze);
//      char[][] routeMaze = copy2dArray(maze);
//      Cell end = new Cell(arrayLength-2, arrayLength-2);
//      initializeTfmaze();
//      //Queue<Cell> queue = new ArrayDeque<>();
//      Cell head = new Cell(1,1);
//      queue.offer(head);
//      int counter = 0;
//
//      System.out.println("traversal using BFS: ");
//      printMaze(mazeCopy);
//      fillInHashTag(end,routeMaze);
//      System.out.println("\n"+"the shortest route generated by BFS: ");
//      printMaze(routeMaze);
//   }



   //==========================================================================================================================
   // Utility method
   //==========================================================================================================================
   public static void printMaze(char[][] maze) {
      for (int i = 0; i < maze.length; i++) {
         for (int j = 0; j < maze.length; j++) {
            System.out.print(maze[i][j]);
         }
         System.out.println();
      }
   }
   //==========================================================================================================================
   // Private Methods
   //==========================================================================================================================
   private double myrandom() {
      return myRandGen.nextDouble(); //random in 0-1
   }

   //==========================================================================================================================
   private void initializeTfmaze () {
      tfmaze = new boolean[arrayLength][arrayLength];
      for(int y = 1; y < arrayLength; y += 2) {
         for (int x = 1; x < arrayLength; x +=2) {
            tfmaze[y][x] = false;
         }
      }
   }
   //==========================================================================================================================
   private ArrayList<Cell> findNeighbors (Cell currentCell) {
      ArrayList<Cell> neighbors = new ArrayList<>();
      int curx = currentCell.getX();
      int cury = currentCell.getY();
      //look left
      if(  curx - 2 > 0  && !tfmaze[cury][curx-2] ) {
         Cell leftNeighbor = new Cell(curx-2, cury);
         neighbors.add(leftNeighbor);
      }
      //look right
      if( curx + 2 < arrayLength  && !tfmaze[cury][curx+2]) {
         Cell rightNeighbor = new Cell(curx+2, cury);
         neighbors.add(rightNeighbor);
      }
      //look up
      if(cury - 2 > 0  && !tfmaze[cury - 2][curx]) {
         Cell upNeighbor = new Cell(curx, cury - 2);
         neighbors.add(upNeighbor);
      }
      //loop down
      if( cury + 2 < arrayLength && !tfmaze[cury + 2][curx]) {
         Cell downNeighbor = new Cell(curx, cury + 2);
         neighbors.add(downNeighbor);
      }
      return neighbors;
   }
   //==========================================================================================================================

   //==========================================================================================================================
   private char[][] copy2dArray(char[][] copyThis) {
      char[][] newMaze = new char[arrayLength][];
      for(int i=0;i<arrayLength;i++) {
         newMaze[i] = maze[i].clone();
      }
      return newMaze;
   }
   //==========================================================================================================================
   private void fillInHashTag (Cell end, char[][]routeMaze) {
      Cell pointer = end;
      while (pointer != null) {
         routeMaze[pointer.getY()][pointer.getX()] = '#';

         pointer = pointer.getParent();
      }
   }
   //==========================================================================================================================

}
