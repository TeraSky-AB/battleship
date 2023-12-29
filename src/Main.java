import game.Battleship;
import player.RandomPlayer;
import display.Window;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main
{
    public static void main(String[] args) { // Main menu
        Scanner scanner = new Scanner(System.in);
        int entry = -1, isSolo = -1;
        do {
            System.out.println("Voulez vous jouer sur interface graphique(0) ou en lignes de commandes(1) ?");
            try{
                entry = scanner.nextInt();
            } catch(InputMismatchException e) {
                scanner.nextLine();
                continue;
            }
        } while((entry != 0 && entry != 1));
        do {
            System.out.println("Voulez vous jouer contre un bot(0) ou contre un autre joueur(1) ?");
            try{
                isSolo = scanner.nextInt();
            } catch(InputMismatchException e) {
                scanner.nextLine();
                continue;
            }
        } while(isSolo != 0 && isSolo != 1);
        if(entry == 0)
        {
            mainGUI(isSolo == 0 ? true : false);
        } else {
            mainCommand(scanner, isSolo == 0 ? true : false);
            scanner.close();
        }
    }

    public static void mainGUI(boolean isSolo) { // GUI Initialization
        Battleship game = new Battleship();
        Window win = new Window(game, isSolo);
        win.setVisible(true);
    }

    public static void mainCommand(Scanner scanner, boolean isSolo) { // Command line play
        Battleship game = new Battleship();
        RandomPlayer randPlayer = new RandomPlayer(game);
        int[] sizes = {2, 3, 3, 4, 5}, entryMove = {-1,-2}, entryPosition = {-1,-2};
        int entryDirection = -1;
        String pos = "Z9";
        int boatCount;
        for(int a=0; a<2; a++){
            if(a==1 && isSolo)
            {
                randPlayer.doPlaceBoat();
                game.updateState();
                break;
            }
            System.out.println("Au tour du joueur " + game.getCurrentPlayer());
            for(int i=0; i<5; i++){
                printGrid(game.getCurrentPlayer(), true, game);
                do
                {
                    do
                    {
                        System.out.println("Choisissez la direction du bateau de taille " + sizes[game.getBoatIndex()] + " (0 = horizontal et 1 = vertical)");
                        try{
                            entryDirection = scanner.nextInt();
                        } catch(InputMismatchException e) {
                            continue;
                        }
                    } while(entryDirection!=0 && entryDirection!=1);
                    scanner.nextLine();
                    System.out.println("Choisissez une case du plateau (ex: A9): ");
                    try{
                        pos = scanner.nextLine();
                        entryPosition = chooseMove(pos.length()==2 ? pos:"Z9");
                    } catch(InputMismatchException e) {
                        continue;
                    }
                } while(!game.isBoatValid(game.getCurrentPlayer(), entryPosition[0], entryPosition[1], entryDirection, sizes[game.getBoatIndex()]));
                game.putBoat(game.getCurrentPlayer(), entryPosition[0], entryPosition[1], entryDirection, sizes[game.getBoatIndex()], game.getBoatIndex());
            }
        }
        game.updateState();
        do
        {   
            int currentPlayer = game.getCurrentPlayer(), otherPlayer = (currentPlayer+1)%2;
            if(currentPlayer == 1 && isSolo)
            {
                System.out.println("Au tour du joueur BOT");
                boatCount = game.countBoats(otherPlayer);
                if(randPlayer.doShoot()){
                    System.out.println("Touché");
                    if(boatCount - game.countBoats(otherPlayer) == 1) {
                        System.out.println("Coulé !");
                    }
                } else{
                    System.out.println("Loupé");
                }
                printGrid(0, false, game);
                continue;
            }
            System.out.println("Au tour du joueur: " + (currentPlayer+1) + " de tirer.");
            do{
                System.out.println("Appuyez sur Entrée");
                scanner.nextLine();
                System.out.println("Choisissez une case du plateau (ex: A9): ");
                try{
                    pos = scanner.nextLine();
                    entryMove = chooseMove(pos.length()==2 ? pos:"Z9");
                } catch(InputMismatchException e) {
                    continue;
                }
            } while(!game.isShootValid(otherPlayer, entryMove[0], entryMove[1]));
            boatCount = game.countBoats(otherPlayer);
            if(game.shoot((game.getCurrentPlayer()+1)%2, entryMove[0], entryMove[1])){
                System.out.println("Touché");
                if(boatCount - game.countBoats(otherPlayer) == 1) {
                    System.out.println("Coulé !");
                }
            } else{
                System.out.println("Loupé");
            }
            printGrid(otherPlayer, false, game);
        } while(!(game.countBoats(game.getCurrentPlayer())==0));
        System.out.println(" " + ((game.getCurrentPlayer()+1)%2+1) + " a gagné la partie.");
    }

    public static void  printGrid(int player, boolean safe, Battleship game){
        char[] numbers = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        int[][] board;
        String gameBoard = " \tA\tB\tC\tD\tE\tF\tG\tH\tI\tJ\n";
        board = game.getBoard(player);
        for(int i=0; i<10; i++){
            gameBoard += numbers[i] + "\t";
            for(int j=0; j<10; j++){
                if(board[j][i] == 2){
                    gameBoard += "X\t";
                } else if(board[j][i] == 3){
                    gameBoard += "!\t";
                } else if(board[j][i] == 0){
                    gameBoard += ".\t";
                } else if(board[j][i] == 1 && safe) {
                    gameBoard += "B\t";
                } else {
                    gameBoard += ".\t";
                }
            }
            gameBoard += "\n";     
        } 
        System.out.println(gameBoard);
    }

    public static int [] chooseMove(String chaine){
        int a = ((int) chaine.charAt(0))-65;
        int b = -1;
        try{
            b = ((int) chaine.charAt(1))-48;
        } catch(InputMismatchException e) {}
        return new int[]{a,b};
    }
}
