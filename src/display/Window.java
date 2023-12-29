package display;

import java.awt.*;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.event.MouseInputListener;

import game.Battleship;
import player.RandomPlayer;

public class Window extends JFrame implements MouseInputListener{
    
    public Window(Battleship game, boolean isSolo)
    {
        int width = 780, height = 540;
        if(isSolo)
        {
            width = 1250;
        }
        this.game = game;
        this.isSolo = isSolo;
        this.randPlayer = new RandomPlayer(this.game);
        this.setSize(width, height);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(false);
        Container contentPane = this.getContentPane();
        contentPane.setLayout(null);

        //Initialization of game panels
        this.gamePanel = new GamePanel(this.game, this.safe, false);
        this.gamePanel.setBounds(0,0,550,550);
        if(this.isSolo)
        {
            this.secondGamePanel = new GamePanel(this.game, true, true);
            this.secondGamePanel.setBounds(730,0,550,550);
        }
        //Text update
        int[] sizes = {2, 3, 3, 4, 5};
        this.currentPlayerText.setText("Au tour du joueur "+(this.game.getCurrentPlayer()+1)+ ":");
        this.actionText.setText(this.game.isPlacing() ? "Placement des bateaux:" : "Tirez !");
        this.secondActionText.setText("Bateau de taille "+sizes[this.game.getBoatIndex()]);
        this.descriptionText.setText("<html>Clique gauche: bateau vertical,<br/>clique droit: bateau horizontal</html>");

        //Définition des limites des componants
        this.currentPlayerText.setBounds(540, 100, 250, 30);
        this.actionText.setBounds(540, 125, 300, 30);
        this.secondActionText.setBounds(540, 140, 350, 30);
        this.descriptionText.setBounds(540, 160, 350, 60);

        //Listener adding
        this.gamePanel.addMouseListener(this);

        //Adding to content pane
        contentPane.add(gamePanel);
        contentPane.add(this.currentPlayerText);
        contentPane.add(this.actionText);
        contentPane.add(this.secondActionText);
        contentPane.add(this.descriptionText);
        if(isSolo)
            contentPane.add(this.secondGamePanel);
    }

    public void changeState()
    {
        this.game.updateState();
        this.safe = false;
        this.descriptionText.setText("Clique gauche pour tirer");
        this.gamePanel.changeState();
    }

    private void refresh()
    {
        this.invalidate();
        this.repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) // Event management : mouse click
    {
        int currentPlayer = this.game.getCurrentPlayer(), otherPlayer = (currentPlayer+1)%2;
        if(!this.finished) // While party isn't over
        {
            int x = (e.getX()-30)/45%10, y = (e.getY()-30)/45%10;
            if(this.game.isPlacing())
            {
                int[] sizes = {2, 3, 3, 4, 5};
                int boatIndx = this.game.getBoatIndex();
                if(this.game.isBoatValid(currentPlayer, x, y, e.getButton() == MouseEvent.BUTTON3 ? 0 : 1, sizes[boatIndx]))
                { 
                    this.game.putBoat(currentPlayer, x, y, e.getButton() == MouseEvent.BUTTON3 ? 0 : 1, sizes[boatIndx], boatIndx);
                    if(this.game.getBoatIndex() == 0 && this.game.getCurrentPlayer() == 1)
                    {
                        this.randPlayer.doPlaceBoat();
                        this.changeState();
                    }
                    this.currentPlayerText.setText("Au tour du joueur "+(currentPlayer+1)+ ":");
                    this.secondActionText.setText("Bateau de taille "+sizes[this.game.getBoatIndex()]);
                    if(boatIndx == 0 && currentPlayer == 1 && !this.isSolo)
                        this.doUpdate = true;
                    if(this.doUpdate && boatIndx == 4 && !this.isSolo) {
                        this.changeState();
                    }
                    this.actionText.setText(this.game.isPlacing() ? "Placement des bateaux:" : "Tirez !");
                    this.refresh();
                }
            } else {
                if(this.game.isShootValid(otherPlayer, x, y))
                {
                    int count = this.game.countBoats(otherPlayer);
                    if(this.game.shoot(otherPlayer, x, y))
                    {
                        this.secondActionText.setText(((count - this.game.countBoats(otherPlayer)) == 1) ? "Coulé !" : "Touché !");
                    } else {
                        this.secondActionText.setText("Raté.");
                    }
                    if(this.isSolo){
                        this.randPlayer.doShoot();
                        this.isOver();
                    }
                    this.currentPlayerText.setText("Au tour du joueur "+(currentPlayer+1)+ ":");
                    this.refresh();
                }
                this.isOver();
            }
        }         
    }

    public void isOver(){
        for(int i = 0; i< 2; i++){
            if(this.game.countBoats((i+1)%2)==0) 
            {
                this.finished = true;
                this.actionText.setText("Jeu terminé:");
                this.secondActionText.setText("Le joueur "+(i+1)+" a gagné !");
                this.descriptionText.setText("");
                this.currentPlayerText.setText("");
                this.refresh();
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {}

    protected Battleship game;
    protected GamePanel gamePanel, secondGamePanel;
    protected RandomPlayer randPlayer;
    protected boolean safe = true, doUpdate = false, finished = false, isSolo=false;
    protected JLabel currentPlayerText = new JLabel("Au tour du joueur 1:"), actionText = new JLabel("Placement des bateaux"), secondActionText = new JLabel("<html>Clique gauche: bateau vertical,<br/>clique droit: bateau horizontal</html>");
    protected JLabel descriptionText = new JLabel();
}
