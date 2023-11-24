import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.Random;

public class Game extends JFrame implements ActionListener, Serializable {
    private static final long serialVersionUID = 7654321L;
    private Cell[][] cells;
    private int sizex;
    private int sizey;
    private int numberOfMines;
    private int numberOfFlags;
    private int numberofRevealedCells;
    private String difficulty;

    private int counter = 0;
    private boolean lost = false;
    private Timer timer = new Timer(1000, this);
    private JButton smile;
    private JButton save;
    private JButton load;
    private JPanel statusbarpanel = new JPanel(new GridBagLayout());
    private JLabel timertext;
    private JLabel nof;
    private GameMainMenu gmm;
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
        private static final long serialVersionUID = 76543210L;
        private int row;
        private int col;
        private Game game;
        public CellClickListener(int row, int col, Game game) {
            this.row = row;
            this.col = col;
            this.game = game;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            if(!cells[row][col].getIsFlag()) {
                revealCell(row, col);
            }
            if(cells[row][col].getIsMine() && !cells[row][col].getIsFlag()) {
                lost = true;
                ImageIcon sadsmileIcon = new ImageIcon("smile2_icon.jpg");
                smile.setIcon(sadsmileIcon);
                revealAllCell();
                setEnabled(false);
                new GameOverWindow(game);
            }
            if(numberofRevealedCells == sizex*sizey-numberOfMines && !lost) {
                setEnabled(false);
                new WinWindow(game);
            }
        }
    }

    private class ButtonMouseListener extends MouseAdapter implements Serializable {
        private static final long serialVersionUID = 87654321L;
        private int row;
        private int col;

        ButtonMouseListener(int row, int col) {
            this.row = row;
            this.col = col;
        }
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
    public void revealCell(int row, int col) {
        Cell currentcell = cells[row][col];
        if(currentcell.getIsRevealed()) {
            return;
        }
        if(currentcell.getIsFlag()) {
            return;
        }
        currentcell.setIsRevealed(true);
        numberofRevealedCells++;
        if(currentcell.getIsMine()) {
            ImageIcon bombIcon = new ImageIcon("bomb_icon.jpg");
            currentcell.setIcon(bombIcon);
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

    public void revealAllCell() {
        for(int i = 0; i < sizex; i++) {
            for(int j = 0; j < sizey; j++) {
                revealCell(i, j);
            }
        }
    }

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

    public void setFieldSize(GameMainMenu gmm) {
        String selected = (String) gmm.dcb.getSelectedItem();
        difficulty = selected;
        numberOfFlags = 0;
        numberofRevealedCells = 0;
        switch(selected) {
            case "Beginner":
                sizex = 10;
                sizey = 10;
                numberOfMines = 20;
                break;
            case "Advanced":
                sizex = 17;
                sizey = 17;
                numberOfMines = 58;
                break;
            case "Expert":
                sizex = 24;
                sizey = 24;
                numberOfMines = 116;
                break;
            case "Load":

            default:
                return;
        }
    }

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

    public void placeMines() {
        Random sor = new Random();
        Random oszlop = new Random();
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

    public void setAllSurroundingMines() {
        for(int i = 0; i < sizex; i++) {
            for(int j = 0; j < sizey; j++) {
                cells[i][j].setSurroundingMines(cells);
            }
        }
    }

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

    public void saveGame() throws FileNotFoundException {
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("save.txt"))) {
            oos.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadGame() {
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream("save.txt"))) {
            Game game = (Game) ois.readObject();
            game.getTimer().start();
            System.out.println(game.getTimer().isRunning());
            game.setVisible(true);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Timer getTimer() {return timer;}
    public int getCounter() {return counter;}
    public String getDifficulty() {return difficulty;}

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
