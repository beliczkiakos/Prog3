import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CaesarFrame extends JPanel {
    private JTextField textfield1 = textfield(true);
    private JTextField textfield2 = textfield(false);
    private JButton button = button(new OkButtonActionListener());
    private JComboBox combobox = comboBox(abc());
    private JLabel label;

    private JTextField textfield(boolean editable) {
        JTextField textfield = new JTextField(20);
        textfield.setEditable(editable);
        return textfield;
    }
    private JButton button(ActionListener action){
        JButton button = new JButton("Code!");
        button.addActionListener(action);
        return button;
    }
    private JComboBox<Object> comboBox(Object[] object){
        return new JComboBox<>(object);
    }
    private Character[] abc() {
        Character[] abc = new Character[26];
        for(int i = 0; i < abc.length; i++) {
            abc[i] = (char)('A' + i);
        }
        return abc;
    }
    public CaesarFrame() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Swinglab");
        frame.setPreferredSize(new Dimension(400, 110));
        frame.setResizable(true);
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        JPanel panel2 = new JPanel();

        panel.add(combobox);
        panel.add(textfield1);
        panel.add(button);
        panel2.setLayout(new BorderLayout());
        JLabel label = new JLabel("Output:");
        panel2.add(label, BorderLayout.WEST);
        panel2.add(textfield2);
        frame.add(panel, BorderLayout.NORTH);
        frame.add(panel2, BorderLayout.SOUTH);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private class OkButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String inputText = textfield1.getText();
            char offset = (char) combobox.getSelectedItem();
            String encodedText = caesarCode(inputText, offset);
            textfield2.setText(encodedText);
        }
    }
    private String caesarCode(String input, char offset) {
        String nagyinput = input.toUpperCase();
        StringBuilder output = new StringBuilder();
        int eltolas = offset - 'A';
        for(int i = 0; i < nagyinput.length(); i++) {
            char c = nagyinput.charAt(i);
            c += eltolas;
            if(c > 'Z') {
                c -= 26;
            }
            output.append(c);
        }
        return output.toString();
    }
}
