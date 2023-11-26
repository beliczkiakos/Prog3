package minesweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.Objects;
import java.util.Random;

public class Game extends JFrame implements ActionListener, Serializable {
    @Serial
    private static final long serialVersionUID = 7654321L;
    private final Cell[][] cells;
    private int sizex;
    private int sizey;
    private int numberOfMines;
    private int numberOfFlags;
    private int numberOfHoles;
    private int numberofRevealedCells;
    private String difficulty;

    private int counter = 0;
    private boolean lost = false;
    private boolean extralife = false;
    private final Timer timer = new Timer(1000, this);
    private JButton smile;
    private JButton save;
    private JButton load;
    private JPanel statusbarpanel = new JPanel(new GridBagLayout());
    private JLabel timertext;
    private JLabel nof;
    private final GameMainMenu gmm;
    private final Random sor = new Random();
    private final Random oszlop = new Random();

    /**
     * Game konstruktora. Létrehozza és beállítja a framet és a rajta lévő komponenseket.
     * @param gmm
     */
    public Game(GameMainMenu gmm) {
        super("Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        this.gmm = gmm;
        setFieldSize(gmm);
        cells = new Cell[sizex][sizey];

        JPanel wholepanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        JPanel gamepanel = new JPanel(new GridLayout(sizex, sizey));
        setCells(gamepanel);
        placeMines();
        setAllSurroundingMines();
        placeHoles();
        placeHeart();
        setStatusBar(statusbarpanel);
        gbc.gridx = 0; gbc.gridy = 0;
        wholepanel.add(statusbarpanel, gbc);
        gbc.gridy = 1;
        wholepanel.add(gamepanel, gbc);
        add(wholepanel);
        pack();
        timer.start();
        setVisible(true);
    }

    public class CellClickListener implements ActionListener, Serializable {
        @Serial
        private static final long serialVersionUID = 76543210L;
        private final int row;
        private final int col;
        private final Game game;

        /**
         * CellClickListener konstruktora. Beállítja, hogy melyik cellára kattintott a felhasználó és az aktuális játékot.
         * @param row
         * @param col
         * @param game
         */
        public CellClickListener(int row, int col, Game game) {
            this.row = row;
            this.col = col;
            this.game = game;
        }

        /**
         * Kezeli a különböző tevékenységeket a játék ablakon.
         * @param e the event to be processed
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if(cells[row][col].getIsMine() && !cells[row][col].getIsFlag() && !extralife) {
                lost = true;
                ImageIcon sadsmileIcon = new ImageIcon("smile2_icon.jpg");
                smile.setIcon(sadsmileIcon);
                revealAllCell();
                setEnabled(false);
                new GameOverWindow(game);
            }
            if(!cells[row][col].getIsFlag()) {
                revealCell(row, col);
            }
            if(numberofRevealedCells == sizex*sizey-numberOfMines && !lost) {
                setEnabled(false);
                new WinWindow(game);
            }
        }
    }

    private class ButtonMouseListener extends MouseAdapter implements Serializable {
        @Serial
        private static final long serialVersionUID = 87654321L;
        private final int row;
        private final int col;

        /**
         * ButtonMouseListener konstruktora. Beállítja, hogy melyik cellára kattintott a felhasználó.
         * @param row
         * @param col
         */
        ButtonMouseListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        /**
         * Kezeli a különböző egértevékenységeket a játék ablakon.
         * @param e the event to be processed
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            if (SwingUtilities.isRightMouseButton(e)) {
                if((numberOfMines-numberOfFlags) >= 0) {
                    toggleFlag(row, col);
                    nof.setText("" + (numberOfMines - numberOfFlags));
                }
                if((numberOfMines-numberOfFlags) == -1) {
                    toggleFlag(row, col);
                }
            }
        }
    }

    /**
     * Megkap egy sort és egy oszlopot. Ha az adott mező már fel van fedve vagy zászló van rajta, akkor rögtön visszatér.
     * Ha viszont semelyik az előzőek közül, akkor felfedi a mezőt és beállítja az adott mezőre a tulajdonságokat.
     * Például, ha a mező egy bomba melletti mező, akkor beállítja a számot rajta, ami megmutatja, hogy mennyi bomba van körülötte.
     * @param row
     * @param col
     */
    public void revealCell(int row, int col) {
        Cell currentcell = cells[row][col];
        if(currentcell.getIsRevealed() || currentcell.getIsFlag()) {
            return;
        }
        currentcell.setIsRevealed(true);
        numberofRevealedCells++;
        if(currentcell.getIsHole()) {
            currentcell.setBackground(new Color(95, 52, 9));
            return;
        }
        if(currentcell.getIsHeart()) {
            ImageIcon heartIcon = new ImageIcon("heart_icon.jpg");
            currentcell.setIcon(heartIcon);
            extralife = true;
        }
        if(currentcell.getIsMine()) {
            ImageIcon bombIcon = new ImageIcon("bomb_icon.jpg");
            currentcell.setIcon(bombIcon);
            extralife = false;
        } else {
            if(currentcell.getSurroundingMines() != 0) {
                currentcell.setText(String.valueOf(currentcell.getSurroundingMines()));
                currentcell.setFont(new Font("Arial", Font.BOLD, 25));
                currentcell.setBackground(new Color(118,118,118));
                currentcell.setColorOfNumbers();
            } else {
                int[][] directions = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};
                for(int[] direction : directions) {
                    currentcell.setBackground(new Color(118,118,118));
                    int newRow = row + direction[0];
                    int newCol = col + direction[1];
                    if(newRow >= 0 && newRow < cells.length && newCol >= 0 && newCol < cells[0].length) {
                        revealCell(newRow, newCol);
                    }
                }
            }
        }
    }

    /**
     * Végigmegy az összes mezőn és felfedi őket.
     */
    public void revealAllCell() {
        for(int i = 0; i < sizex; i++) {
            for(int j = 0; j < sizey; j++) {
                revealCell(i, j);
            }
        }
    }

    /**
     * Letesz egy zászlót az adott mezőre.
     * @param row
     * @param col
     */
    public void toggleFlag(int row, int col) {
        Cell currentcell = cells[row][col];
        if(currentcell.getIsRevealed()) {
            return;
        }
        currentcell.setIsFlag(!currentcell.getIsFlag());
        if(currentcell.getIsFlag()) {
            ImageIcon flagIcon = new ImageIcon("flag_icon.jpg");
            currentcell.setIcon(flagIcon);
            numberOfFlags++;
        } else {
            currentcell.setIcon(null);
            numberOfFlags--;
        }
    }

    /**
     * Beállítja a pálya méretét, bombaszámát és gödörszámát a paraméterül kapott GameMainMenu alapján.
     * @param gmm
     */
    public void setFieldSize(GameMainMenu gmm) {
        String selected = (String) gmm.dcb.getSelectedItem();
        difficulty = selected;
        numberOfFlags = 0;
        numberofRevealedCells = 0;
        switch(Objects.requireNonNull(selected)) {
            case "Beginner":
                sizex = 10;
                sizey = 10;
                numberOfMines = 20;
                numberOfHoles = 3;
                break;
            case "Advanced":
                sizex = 17;
                sizey = 17;
                numberOfMines = 58;
                numberOfHoles = 9;
                break;
            case "Expert":
                sizex = 24;
                sizey = 24;
                numberOfMines = 116;
                numberOfHoles = 18;
                break;
            default:
                return;
        }
    }

    /**
     * Beállítja egy cella tulajdonságait és hozzáadja a paraméterül kapott panelhez.
     * @param gamepanel
     */
    public void setCells(JPanel gamepanel) {
        for (int i = 0; i < sizex; i++) {
            for (int j = 0; j < sizey; j++) {
                cells[i][j] = new Cell();
                cells[i][j].setRow(i);
                cells[i][j].setCol(j);
                cells[i][j].addActionListener(new CellClickListener(i, j, this));
                cells[i][j].addMouseListener(new ButtonMouseListener(i, j));
                cells[i][j].setPreferredSize(new Dimension(40, 40));
                cells[i][j].setBackground(Color.LIGHT_GRAY);
                cells[i][j].setMargin(new Insets(0,0,0,0));
                gamepanel.add(cells[i][j]);
            }
        }
    }

    /**
     * Elhelyezi a bombákat a pályán.
     */
    public void placeMines() {
        int nom = numberOfMines;
        while(nom != 0) {
            int s = sor.nextInt(sizex-1);
            int o = oszlop.nextInt(sizey-1);
            if(!cells[s][o].getIsMine()) {
                cells[s][o].setIsMine(true);
                nom--;
            }
        }
    }

    /**
     * Elhelyezi a gödröket a pályán.
     */
    public void placeHoles() {
        int noh = numberOfHoles;
        while(noh != 0) {
            int s = sor.nextInt(sizex-1);
            int o = oszlop.nextInt(sizey-1);
            if(!cells[s][o].getIsMine() && cells[s][o].getSurroundingMines() == 0 && !cells[s][o].getIsHole()) {
                cells[s][o].setIsHole(true);
                noh--;
            }
        }
    }

    /**
     * Elhelyezi az extra élet mezőt a pályán.
     */
    public void placeHeart() {
        int noh = 1;
        while(noh != 0) {
            int s = sor.nextInt(sizex-1);
            int o = oszlop.nextInt(sizey-1);
            if(!cells[s][o].getIsMine() && cells[s][o].getSurroundingMines() == 0 && !cells[s][o].getIsHole()) {
                cells[s][o].setIsHeart(true);
                noh--;
            }
        }
    }

    /**
     * Végigmegy az összes cellán és beállítja, hogy mennyi bomba van az adott mező körül.
     */
    public void setAllSurroundingMines() {
        for(int i = 0; i < sizex; i++) {
            for(int j = 0; j < sizey; j++) {
                cells[i][j].setSurroundingMines(cells);
            }
        }
    }

    /**
     * Beállítja az állaptsor komponenseit és hozzáadja a paraméterül kapott panelhez.
     * @param statusbarpanel
     */
    public void setStatusBar(JPanel statusbarpanel) {
        JLabel flagtext = new JLabel("Rest flags:");
        nof = new JLabel("" + numberOfMines);
        load = new JButton("Load");
        load.setMargin(new Insets(0,0,0,0));
        load.setPreferredSize(new Dimension(50, 20));
        load.addActionListener(this);

        smile = new JButton();
        smile.setPreferredSize(new Dimension(35,35));
        ImageIcon smileIcon = new ImageIcon("smile_icon.jpg");
        smile.setIcon(smileIcon);
        smile.setBackground(new Color(248,248,248));
        smile.addActionListener(this);

        save = new JButton("Save");
        save.setMargin(new Insets(0,0,0,0));
        save.setPreferredSize(new Dimension(50, 20));
        save.addActionListener(this);

        timertext = new JLabel("Time: 0");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = GridBagConstraints.CENTER;
        JPanel leftpanel = new JPanel(new FlowLayout());
        leftpanel.add(flagtext);
        leftpanel.add(nof);
        leftpanel.add(load);
        statusbarpanel.add(leftpanel, gbc);

        gbc.gridx = GridBagConstraints.EAST;
        JPanel centerpanel = new JPanel(new FlowLayout());
        centerpanel.add(smile);
        statusbarpanel.add(centerpanel, gbc);

        gbc.gridx = GridBagConstraints.WEST;
        JPanel rightpanel = new JPanel(new FlowLayout());
        rightpanel.add(save);
        rightpanel.add(timertext);
        statusbarpanel.add(rightpanel, gbc);
    }

    /**
     * Elmenti az aktuális játékot egy fájlba.
     * @throws FileNotFoundException
     */
    public void saveGame() throws FileNotFoundException {
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("save.txt"))) {
            oos.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Betölti az utoljára elmentett játékot.
     */
    public void loadGame() {
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream("save.txt"))) {
            Game game = (Game) ois.readObject();
            game.getTimer().start();
            game.setVisible(true);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Visszaadja a játék Timer objektumát.
     * @return
     */
    public Timer getTimer() {return timer;}

    /**
     * Visszaadja a játék számlálóját.
     * @return
     */
    public int getCounter() {return counter;}

    /**
     * Visszaadja a nehézségi szintet.
     * @return
     */
    public String getDifficulty() {return difficulty;}

    /**
     * Kezeli a különböző tevékenységeket az ablakon.
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == smile) {
            setVisible(false);
            new Game(gmm);
        }
        if(e.getSource() == save) {
            try {
                saveGame();
                save.setBackground(new Color(0,255,0));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if(e.getSource() == load) {
            setVisible(false);
            loadGame();
        }
        if(e.getSource() == timer) {
            timertext.setText("Time: " + counter);
            counter++;
        }
    }
}
