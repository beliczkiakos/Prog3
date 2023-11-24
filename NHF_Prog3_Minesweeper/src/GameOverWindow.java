import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameOverWindow extends JFrame implements ActionListener {
    private JButton newgame;
    private Game game;
    public GameOverWindow(Game game) {
        setTitle("Game Over");
        setPreferredSize(new Dimension(400, 200));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setBackground(Color.GRAY);
        setResizable(false);
        this.game = game;
        game.getTimer().stop();

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = GridBagConstraints.CENTER; gbc.gridy = 0;
        JLabel label1 = new JLabel("Unfortunately, you lost!", SwingConstants.CENTER);
        JLabel label2 = new JLabel("If you want to start a new game, please click the button.", SwingConstants.CENTER);
        newgame = new JButton("New Game");
        newgame.addActionListener(this);
        panel.add(label1, gbc);
        gbc.gridy = 1;
        panel.add(label2, gbc);
        gbc.gridy = 2;
        gbc.insets = new Insets(20,0,0,0);
        panel.add(newgame, gbc);
        add(panel);
        pack();
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == newgame) {
            game.setVisible(false);
            setVisible(false);
            new GameMainMenu();
        }
    }
}
