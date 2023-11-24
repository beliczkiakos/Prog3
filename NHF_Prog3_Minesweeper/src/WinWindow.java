import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class WinWindow extends JFrame implements ActionListener {
    private Game game;
    private JTextField field;
    private int counter;
    private String difficulty;
    public WinWindow(Game game) {
        setTitle("Win");
        setPreferredSize(new Dimension(400,200));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setBackground(Color.GRAY);
        setResizable(false);
        game.getTimer().stop();
        this.game = game;
        this.counter = game.getCounter();
        this.difficulty = game.getDifficulty();

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = GridBagConstraints.CENTER; gbc.gridy = 0;
        JLabel label1 = new JLabel("Congratulations, you won!", SwingConstants.CENTER);
        JLabel label2 = new JLabel("Please enter your name to be placed onto the Glorylist!", SwingConstants.CENTER);
        field = new JTextField();
        field.setPreferredSize(new Dimension(150, 20));
        JButton button = new JButton("Add and return to Main Menu");
        button.setMargin(new Insets(0,0,0,0));
        button.addActionListener(this);

        panel.add(label1, gbc);
        gbc.gridy = 1; gbc.insets = new Insets(7,0,0,0);
        panel.add(label2, gbc);
        gbc.gridy = 2;
        panel.add(field, gbc);
        gbc.gridy = 3;
        panel.add(button, gbc);
        add(panel);
        pack();
        setVisible(true);

    }

    public void addToGloryList() {
        String filename = "glorylist.txt";
        String newLineglorylist = field.getText() + ";" + counter + ";" + difficulty;
        File file = new File(filename);
        try (FileWriter fileWriter = new FileWriter(filename, true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            if(file.length() != 0) {
                bufferedWriter.newLine();
            }
            bufferedWriter.write(newLineglorylist);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        addToGloryList();
        game.setVisible(false);
        setVisible(false);
        new MainMenu();
    }
}
