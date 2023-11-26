package minesweeper;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;
import java.io.Serializable;

public class Cell extends JButton implements Serializable {
    @Serial
    private static final long serialVersionUID = 123456789;
    private int row;
    private int col;
    private int surroundingMines;
    private boolean isMine;
    private boolean isFlag;
    private boolean isRevealed;
    private boolean isHole;
    private boolean isHeart;

    /**
     * Megkapja az aktuális pályát és beállítja minden olyan mezőre, amelyen nincsen bomba, hogy hány bomba van körülötte.
     * @param cells
     */
    public void setSurroundingMines(Cell[][] cells) {
        for(int i = -1; i <= 1; i++) {
            for(int j = -1; j <= 1; j++) {
                if(i == 0 && j == 0) {
                    continue;
                }
                if(isValidCell(row+i, col+j, cells.length, cells[0].length) && cells[row + i][col + j].isMine) {
                    surroundingMines++;
                }
            }
        }
    }

    /**
     * Beállítja a mezőkre tett számok színét.
     */
    public void setColorOfNumbers() {
        switch(surroundingMines) {
            case 1: setForeground(new Color(64,0,255)); break;
            case 2: setForeground(new Color(5,109,14)); break;
            case 3: setForeground(new Color(255,0,0)); break;
            case 4: setForeground(new Color(32,0,127)); break;
            case 5: setForeground(new Color(99,78,4)); break;
            case 6: setForeground(new Color(23,170,168)); break;
            case 7: setForeground(new Color(0,0,0)); break;
            case 8: setForeground(new Color(153,153,153)); break;
            default: break;
        }
    }

    /**
     * Eldönti, hogy az adott cella érvényes cella-e.
     * @param row
     * @param col
     * @param numRows
     * @param numCols
     * @return
     */
    public boolean isValidCell(int row, int col, int numRows, int numCols) {
        return row >= 0 && row < numRows && col >= 0 && col < numCols;
    }
    /**
     * Visszaadja, hogy az adott mező bomba-e.
     * @return
     */
    public boolean getIsMine() {return isMine;}

    /**
     * Beállítja, hogy az adott mező bomba-e vagy sem.
     * @param m
     */
    public void setIsMine(boolean m) {isMine = m;}

    /**
     * Visszaadja, hogy az adott mező zászló-e.
     * @return
     */
    public boolean getIsFlag() {return isFlag;}

    /**
     * Beállítja, hogy az adott mező zászló-e vagy sem.
     * @param f
     */
    public void setIsFlag(boolean f) {isFlag = f;}

    /**
     * Visszaadja, hogy az adott mező fel van-e fedve.
     * @return
     */
    public boolean getIsRevealed() {return isRevealed;}

    /**
     * Beállítja, hogy az adott mező fel van-e fedve vagy sem.
     * @param r
     */
    public void setIsRevealed(boolean r) {isRevealed = r;}

    /**
     * Visszaadja, hogy az adott mező körül mennyi bomba van.
     * @return
     */
    public int getSurroundingMines() {return surroundingMines;}

    /**
     * Beállítja, hogy az adott mező sorát.
     * @param r
     */
    public void setRow(int r) {row = r;}

    /**
     * Beállítja, hogy az adott mező oszlopát.
     * @param c
     */
    public void setCol(int c) {col = c;}

    /**
     * Visszaadja, hogy az adott mező gödör-e.
     * @return
     */
    public boolean getIsHole() {return isHole;}

    /**
     * Beállítja, hogy az adott mező gödör-e vagy sem.
     * @param h
     */
    public void setIsHole(boolean h) {isHole = h;}

    /**
     * Visszaadja, hogy az adott mező extra élet mező-e.
     * @return
     */
    public boolean getIsHeart() {return isHeart;}

    /**
     * Beállítja, hogy az adott mező extra élet mező-e vagy sem.
     * @param h
     */
    public void setIsHeart(boolean h) {isHeart = h;}
}
