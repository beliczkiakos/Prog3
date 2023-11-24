import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class Cell extends JButton implements Serializable {
    private static final long serialVersionUID = 123456789;
    private int row;
    private int col;
    private int surroundingMines;
    private boolean isMine;
    private boolean isFlag;
    private boolean isRevealed;
    public void setSurroundingMines(Cell[][] cells) {
        for(int i = -1; i <= 1; i++) {
            for(int j = -1; j <= 1; j++) {
                if(i == 0 && j == 0) {
                    continue;
                }
                if(isValidCell(row+i, col+j, cells.length, cells[0].length)) {
                    if (cells[row + i][col + j].isMine) {
                        surroundingMines++;
                    }
                }
            }
        }
    }

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

    public boolean isValidCell(int row, int col, int numRows, int numCols) {
        return row >= 0 && row < numRows && col >= 0 && col < numCols;
    }

    public boolean getIsMine() {return isMine;}
    public void setIsMine(boolean m) {isMine = m;}
    public boolean getIsFlag() {return isFlag;}
    public void setIsFlag(boolean f) {isFlag = f;}
    public boolean getIsRevealed() {return isRevealed;}
    public void setIsRevealed(boolean r) {isRevealed = r;}
    public int getSurroundingMines() {return surroundingMines;}
    public void setRow(int r) {row = r;}
    public void setCol(int c) {col = c;}
}
