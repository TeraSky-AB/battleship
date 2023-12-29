package display;

import javax.swing.*;
import java.awt.*;

import game.Battleship;


public class GamePanel extends JPanel
{

    public GamePanel(Battleship game, boolean safe, boolean bot)
    {
        super();
        this.safe = safe;
        this.game = game;
        this.bot = bot;
    }

    public void paintComponent(Graphics g) // Draw the game board
    {
        int[][] gameBoard;
        Color[] colors = new Color[]{new Color(255, 255, 255), this.safe ? new Color(33, 33, 33): new Color(255, 255, 255), new Color(255,0,0), new Color(0, 0, 125)};
        int x, y;
        int currentPlayer = this.game.getCurrentPlayer();
        gameBoard = this.game.getBoard(this.safe ? currentPlayer : (currentPlayer+1)%2);
        for(int i=0; i < 10; i++)
        {
            for(int j=0; j < 10; j++)
            {
                x = i*(this.caseSide+this.margin)+30;
                y = j*(this.caseSide+this.margin)+30;
                g.setColor(colors[gameBoard[i][j]]);
                g.fillOval(x,y,this.caseSide,this.caseSide);
            }
        }
        String[][] overlays = {{" ", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"}, {" ", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J"}};
        int[] offset;
        g.setColor(new Color(0, 0, 0));
        for(int nb = 0; nb < 2; nb++) 
        {
            for(int i = 0; i <= 10; i++)
            {
                g.drawString(overlays[nb][i], (nb == 1 ? i*(this.caseSide+this.margin) : 10) , (nb == 0 ? 5+i*(this.caseSide+this.margin) : 20));
            }
            offset = nb == 0 ? new int[]{25, 0, 25, 470} : new int[]{0, 25, 470, 25};
            g.drawLine(offset[0], offset[1], offset[2], offset[3]);
        }
    }

    public void changeState()
    {
        this.safe = false;
    }

    protected Battleship game;
    protected boolean safe, bot;
    private static final long serialVersionUID = 1L;
    protected int margin = 5, caseSide = 40;
}
