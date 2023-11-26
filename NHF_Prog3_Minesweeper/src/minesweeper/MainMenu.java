package minesweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class MainMenu extends JFrame implements ActionListener {
    private final JButton startgame;
    private final JButton glorylist;
    public MainMenu() {
        setTitle("Main Menu");
        setPreferredSize(new Dimension(600, 400));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setBackground(Color.GRAY);
        setResizable(false);

        JLabel titlelabel = new JLabel("Minesweeper Main Menu", SwingConstants.CENTER);
        titlelabel.setFont(new Font("Arial", Font.BOLD, 40));
        add(titlelabel, BorderLayout.NORTH);

        startgame = new JButton("Start Game");
        startgame.setPreferredSize(new Dimension(150, 40));
        startgame.setBackground(Color.LIGHT_GRAY);
        startgame.addActionListener(this);

        glorylist = new JButton("Glory List");
        glorylist.setPreferredSize(new Dimension(150, 40));
        glorylist.setBackground(Color.LIGHT_GRAY);
        glorylist.addActionListener(this);

        JPanel buttonpanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.insets = new Insets(0,0,10,0);
        buttonpanel.add(startgame, gbc);
        gbc.gridy = 1;
        buttonpanel.add(glorylist, gbc);

        add(buttonpanel, BorderLayout.CENTER);
        pack();
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == startgame) {
            setVisible(false);
            new GameMainMenu();
        } else if(e.getSource() == glorylist) {
            setVisible(false);
            try {
                new GloryList();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
