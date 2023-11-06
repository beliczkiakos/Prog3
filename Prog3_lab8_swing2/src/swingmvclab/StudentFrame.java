package swingmvclab;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import javax.swing.*;

/*
 * A megjelenítendõ ablakunk osztálya.
 */
public class StudentFrame extends JFrame {
    
    /*
     * Ebben az objektumban vannak a hallgatói adatok.
     * A program indulás után betölti az adatokat fájlból, bezáráskor pedig kimenti oda.
     * 
     * NE MÓDOSÍTSD!
     */
    private StudentData data;
    
    /*
     * Itt hozzuk létre és adjuk hozzá az ablakunkhoz a különbözõ komponenseket
     * (táblázat, beviteli mezõ, gomb).
     */
    private void initComponents() {
        this.setLayout(new BorderLayout());
        JTable table = new JTable();
        table.setModel(data);
        table.setFillsViewportHeight(true);

        JScrollPane scrollpane = new JScrollPane(table);
        this.add(scrollpane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        JLabel label1 = new JLabel("Név:");
        JTextField field1 = new JTextField(20);
        JLabel label2 = new JLabel("Neptun-kód:");
        JTextField field2 = new JTextField(6);
        JButton button = new JButton("Felvesz");

        panel.add(label1);
        panel.add(field1);
        panel.add(label2);
        panel.add(field2);
        panel.add(button);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = field1.getText();
                String neptun = field2.getText();
                data.addStudent(name, neptun);
                field1.setText("");
                field2.setText("");
            }
        });

        this.add(panel, BorderLayout.SOUTH);
        this.pack();
        // ...
    }

    /*
     * Az ablak konstruktora.
     * 
     * NE MÓDOSÍTSD!
     */
    @SuppressWarnings("unchecked")
    public StudentFrame() {
        super("Hallgatói nyilvántartás");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        // Induláskor betöltjük az adatokat
        try {
            data = new StudentData();
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("students.dat"));
            data.students = (List<Student>)ois.readObject();
            ois.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        
        // Bezáráskor mentjük az adatokat
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("students.dat"));
                    oos.writeObject(data.students);
                    oos.close();
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Felépítjük az ablakot
        setMinimumSize(new Dimension(500, 200));
        initComponents();
    }

    /*
     * A program belépési pontja.
     * 
     * NE MÓDOSÍTSD!
     */
    public static void main(String[] args) {
        // Megjelenítjük az ablakot
        StudentFrame sf = new StudentFrame();
        sf.setVisible(true);
    }
}
