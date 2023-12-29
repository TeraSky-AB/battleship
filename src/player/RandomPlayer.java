package player;

import game.Battleship;
import java.util.Random;

public class RandomPlayer 
{
    public RandomPlayer(Battleship game)
    {
        this.game = game;
        this.random = new Random();
    }

    public boolean doShoot() // Shoot at a random location
    {
        int x, y;
        do {
            x = this.random.nextInt(10);
            y = this.random.nextInt(10);
        } while(!this.game.isShootValid(0, x, y));
        return this.game.shoot(0, x, y);   
    }

    public void doPlaceBoat() // Place all boats.
    {
        int x, y, dir;
        int[] sizes = new int[]{2, 3, 3, 4, 5};
        for(int i = 0; i < 5; i++)
        {
            do {
                x = this.random.nextInt(10);
                y = this.random.nextInt(10);
                dir = this.random.nextInt(2);  
            } while(!this.game. isBoatValid(1, x, y, dir, sizes[this.game.getBoatIndex()]));
            this.game.putBoat(1, x, y, dir, sizes[this.game.getBoatIndex()], this.game.getBoatIndex());
        }
    }

    protected Battleship game;
    protected Random random;


}
