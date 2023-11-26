package minesweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameMainMenu extends JFrame implements ActionListener {
    JComboBox<String> dcb;
    JButton start;
    JButton back;

    /**
     * GameMainMenu konstruktora. Létrehozza és beállítja a framet és a rajta lévő komponenseket.
     */
    public GameMainMenu() {
        setTitle("Game Main Menu");
        setPreferredSize(new Dimension(600, 400));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setBackground(Color.GRAY);
        setResizable(false);

        JLabel titlelabel = new JLabel("Game Main Menu", SwingConstants.CENTER);
        titlelabel.setFont(new Font("Arial", Font.BOLD, 40));

        start = new JButton("Start");
        start.setPreferredSize(new Dimension(150, 40));
        start.setBackground(Color.LIGHT_GRAY);
        start.addActionListener(this);

        back = new JButton("Back");
        back.setPreferredSize(new Dimension(150, 40));
        back.setBackground(Color.LIGHT_GRAY);
        back.addActionListener(this);

        String[] difficulty = {"Beginner", "Advanced", "Expert"};
        dcb = new JComboBox<>(difficulty);
        dcb.setPreferredSize(new Dimension(150, 40));
        dcb.setBackground(Color.LIGHT_GRAY);

        JPanel optionspanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.insets = new Insets(5,5,5,5);
        optionspanel.add(dcb, gbc);
        gbc.gridx = 1;
        optionspanel.add(start, gbc);

        add(titlelabel, BorderLayout.NORTH);
        add(optionspanel, BorderLayout.CENTER);
        add(back, BorderLayout.SOUTH);
        pack();
        setVisible(true);
    }

    /**
     * Kezeli a különböző tevékenységeket az ablakon.
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == back) {
            setVisible(false);
            new MainMenu();
        }
        if(e.getSource() == start) {
            setVisible(false);
            new Game(this);
        }
    }
}
