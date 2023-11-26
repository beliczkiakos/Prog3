package minesweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class GloryList extends JFrame implements ActionListener {
    private final JButton back = new JButton("Back");
    private final JPanel names = new JPanel();
    private final JPanel times = new JPanel();
    private final JPanel difficulties = new JPanel();

    /**
     * GloryList konstruktora. Létrehozza és beállítja a framet és a rajta lévő komponenseket.
     * @throws IOException
     */
    public GloryList() throws IOException {
        setTitle("Glorylist");
        setPreferredSize(new Dimension(600, 400));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setBackground(Color.GRAY);
        setResizable(false);

        JLabel glorylistlabel = new JLabel("Glorylist", SwingConstants.CENTER);
        glorylistlabel.setFont(new Font("Arial", Font.BOLD, 40));

        JPanel panel = new JPanel(new GridLayout(1,3));
        names.setLayout(new BoxLayout(names, BoxLayout.PAGE_AXIS));
        times.setLayout(new BoxLayout(times, BoxLayout.PAGE_AXIS));
        difficulties.setLayout(new BoxLayout(difficulties, BoxLayout.PAGE_AXIS));

        JLabel label1 = new JLabel("Name");
        label1.setFont(new Font("Arial", Font.BOLD, 25));
        label1.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel label2 = new JLabel("Time");
        label2.setFont(new Font("Arial", Font.BOLD, 25));
        label2.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel label3 = new JLabel("Difficulty");
        label3.setFont(new Font("Arial", Font.BOLD, 25));
        label3.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(glorylistlabel, BorderLayout.NORTH);
        names.add(label1);
        times.add(label2);
        difficulties.add(label3);
        ArrayList<String[]> list = loadglorylist();
        printglorylist(list);
        panel.add(names);
        panel.add(times);
        panel.add(difficulties);

        back.setPreferredSize(new Dimension(150, 40));
        back.setBackground(Color.LIGHT_GRAY);
        back.addActionListener(this);

        add(panel);
        add(back, BorderLayout.SOUTH);
        pack();
        setVisible(true);
    }

    /**
     * Betölti fájlból a dicsőséglistát.
     * @return
     * @throws IOException
     */
    public ArrayList<String[]> loadglorylist() throws IOException {
        ArrayList<String[]> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("glorylist.txt"))) {
            String line;
            while((line = br.readLine()) != null) {
                String[] array = line.split(";");
                list.add(array);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Kiírja az ablakra a dicsőséglistát megfelelően formázva.
     * @param glorylist
     */
    public void printglorylist(ArrayList<String[]> glorylist) {
        Collections.sort(glorylist, Comparator.comparing(array -> Integer.parseInt(array[1])));
        for(int i = 0; i < glorylist.size(); i++) {
            if(i == 12) break;
            JLabel label1 = new JLabel(glorylist.get(i)[0]);
            label1.setFont(new Font("Arial", Font.BOLD, 15));
            label1.setAlignmentX(Component.CENTER_ALIGNMENT);
            JLabel label2 = new JLabel(glorylist.get(i)[1]);
            label2.setFont(new Font("Arial", Font.BOLD, 15));
            label2.setAlignmentX(Component.CENTER_ALIGNMENT);
            JLabel label3 = new JLabel(glorylist.get(i)[2]);
            label3.setFont(new Font("Arial", Font.BOLD, 15));
            label3.setAlignmentX(Component.CENTER_ALIGNMENT);
            names.add(label1);
            times.add(label2);
            difficulties.add(label3);
        }
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
    }
}
