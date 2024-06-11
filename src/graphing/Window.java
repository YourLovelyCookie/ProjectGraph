package graphing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Window extends JFrame {

    JPanel bdlJP = new JPanel(new BorderLayout()); // BorderLayout JPanel

    Canvas cnv = new Canvas() { public void paint(Graphics g) {draw(g); } };

    DefaultListModel<String> inputsLi = new DefaultListModel<>();
    JList<String> inputsJL = new JList<>(inputsLi);

    JPanel btnsJP = new JPanel(new GridLayout());
    JButton addJB = new JButton("ADD");
    JButton removeJB = new JButton("REMOVE");
    JButton renameJB = new JButton("RENAME");

    JTextArea notesTA = new JTextArea("This is a beautiful note :D");



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


        addJB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog t_jd = new JDialog();

                t_jd.add(new JComboBox<>(inputsLi.toArray()));
                t_jd.add(new JButton("yep"));
                t_jd.add(new JButton("noo"));
                t_jd.setSize(100, 100);
                t_jd.setModal(true);
                t_jd.setVisible(true);
            }
        });
        removeJB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (inputsJL.getSelectedIndex() > -1) inputsLi.remove(inputsJL.getSelectedIndex());
            }
        });

        btnsJP.add(addJB);
        btnsJP.add(removeJB);
        btnsJP.add(renameJB);

        // -- JPanel
        bdlJP.add(cnv, BorderLayout.CENTER);                // Center
        bdlJP.add(inputsJL, BorderLayout.LINE_START);       // Left
        bdlJP.add(btnsJP, BorderLayout.PAGE_END);           // Bottom
        bdlJP.add(notesTA, BorderLayout.LINE_END);          // Right


        // -- WINDOW - END
        getContentPane().add(bdlJP);
        /* pack();
        setSize(width, height); */

    }



    public void draw(Graphics g)
    {
        toFront();
        System.out.println("yep");
        //repaint();
    }

}
