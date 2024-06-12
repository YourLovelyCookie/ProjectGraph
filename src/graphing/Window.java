package graphing;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {

    JPanel bdlJP = new JPanel(new BorderLayout()); // BorderLayout JPanel

    Canvas cnv = new Canvas() { public void paint(Graphics g) {draw(g); } };

    DefaultListModel<String> inputsLi = new DefaultListModel<>();
    JList<String> inputsJL = new JList<>(inputsLi);

    JPanel btnsJP = new JPanel(new GridLayout());
    JButton addJB = new JButton("ADD");
    JButton removeJB = new JButton("REMOVE");
    JButton editJB = new JButton("RENAME");

    JTextArea notesTA = new JTextArea("This is a beautiful note :D");


    PopUp popUp = new PopUp("add", this);


    public Window(int width, int height) {
        // -- WINDOW - START
        //setMinimumSize();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(width, height);


        // -- Canvas / Inputs / buttons
        //cnv.setBackground(new Color(235, 235, 235));
        cnv.setBackground(Color.RED); // Replace with the line above !!!!!!!!!!!!!!!!!!!!!!!!


        inputsLi.addElement("f(x) = a*x");
        inputsLi.addElement("a = 2");


        addJB.addActionListener(e -> {
            /*JDialog t_jd = new JDialog();

            t_jd.add(new JComboBox<>(inputsLi.toArray()));
            t_jd.add(new JButton("yep"));
            t_jd.add(new JButton("noo"));
            t_jd.setSize(100, 100);
            t_jd.setModal(true);
            t_jd.setVisible(true);*/
            popUp.setLocationRelativeTo(null);
            popUp.display("add");
            popUp.setVisible(true);
        });
        removeJB.addActionListener(e -> {
            if (inputsJL.getSelectedIndex() > -1) inputsLi.remove(inputsJL.getSelectedIndex());
        });
        editJB.addActionListener(e -> {
            if (inputsJL.getSelectedIndex() > -1) {
                popUp.setLocationRelativeTo(null);
                popUp.display("edit");
                popUp.setVisible(true);
            }
        });

        btnsJP.add(addJB);
        btnsJP.add(removeJB);
        btnsJP.add(editJB);

        // -- JPanel
        bdlJP.add(cnv, BorderLayout.CENTER);                // Center
        bdlJP.add(inputsJL, BorderLayout.LINE_START);       // Left
        bdlJP.add(btnsJP, BorderLayout.PAGE_END);           // Bottom
        bdlJP.add(notesTA, BorderLayout.LINE_END);          // Right


        // -- WINDOW - END
        add(bdlJP);
        /* pack();
        setSize(width, height); */

    }



    public void draw(Graphics g)
    {
        System.out.println("yep");
        //repaint();
    }

}
