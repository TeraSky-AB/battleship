package game;


public class Battleship 
{
    public Battleship(){}

    // INFO ON BOARD:
    // Coords with nothing on it is 0
    //   "    with a boat is 1
    //   "    with a hit is 2
    //   "    with a miss is 3

    // DIRECTION ARGUMENT:
    // 0 is for horizontal boats
    // 1 is for vertical boats

    public void putBoat(int player, int x, int y, int isHorizontal, int size, int boatNb) // To place a boat
    {
        int xPos = x, yPos = y;
        int[] v = getVector(isHorizontal);
        for(int i = 0; i < size; i++)
        {
            this.boards[player][xPos][yPos]++;
            xPos += v[0];
            yPos += v[1];
        }
        this.boatsPos[player][boatNb][0] = x;
        this.boatsPos[player][boatNb][1] = y;
        this.boatsPos[player][boatNb][2] = isHorizontal;
        this.boatIndex = (this.boatIndex+1)%5;
        if(this.boatIndex == 0)
            this.currentPlayer = (this.currentPlayer+1)%2;
    }

    public boolean isBoatValid(int player, int x, int y, int isHorizontal, int size){ // Check if a boat is at a valid place
        int[] v = getVector(isHorizontal);
        int err = 0, xPos = x, yPos = y;
        for(int i=0; i<size; i++){
            if(xPos >= 10 || yPos >= 10)
                return false; 
            err += this.boards[player][xPos][yPos] == 1 ? 1 : 0;
            xPos += v[0];
            yPos += v[1];
        }
        return err == 0;
    }

    private int[] getVector(int direction){return  new int[]{direction==0 ? 1: 0, direction==0 ? 0 : 1};} // To get the vector of directions

    public boolean checkBoat(int player, int[] boat, int size, int boatIndex) // Return true if the boat is hit and sunk
    {
        int err = 0, x = boat[0], y = boat[1];
        int[] v = getVector(boat[2]);
        for(int i = 0; i < size; i++)
        {
            err += this.boards[player][x][y] == 2 ? 1 : 0;
            x += v[0];
            y += v[1];
        }
        return err == size;
    }

    public int getBoatIndex(){return this.boatIndex;}

    public int countBoats(int player) // Count how many boats are still in place
    {
        int cnt = 5;
        for(int i = 0; i < 5; i++)
        {
            cnt -= checkBoat(player, this.boatsPos[player][i], i == 1 || i == 2 ? 3 : (i == 0 ? 2 : i+1), i) ? 1 : 0;
        }
        return cnt;
    }

    public boolean shoot(int player, int x, int y) // To shoot on a grid
    {
        this.boards[player][x][y] += this.boards[player][x][y] == 1 ? 1 : (this.boards[player][x][y] == 2 ? 0 : 3);
        this.currentPlayer = (this.currentPlayer+1)%2;
        return this.boards[player][x][y] == 2;
    }

    public boolean isShootValid(int player, int x, int y) // Check if shoot is valid
    {
        if(x < 10 && x >= 0 && y < 10 && y >= 0){
            return this.boards[player][x][y] == 0 || this.boards[player][x][y] == 1;
        }
        return false;
    }

    public boolean isTouched(int player, int x, int y){return this.boards[player][x][y] == 1;}

    public int getCurrentPlayer(){return this.currentPlayer;}

    public int[][] getBoard(int player){return this.boards[player];}

    public boolean isPlacing(){return this.placing;}

    public void updateState(){this.placing = false;} // Change the game state from placing mode to play mode.

    protected int currentPlayer = 0;
    protected int boatIndex = 0;
    protected boolean placing = true;
    protected int[][][] boards = new int[2][10][10];
    protected int[][][] boatsPos = new int[2][5][3];
}
