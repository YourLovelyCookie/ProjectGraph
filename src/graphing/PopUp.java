package graphing;

import javax.swing.*;
import java.awt.*;
import java.sql.Array;
import java.util.ArrayList;

public class PopUp extends JFrame {
    CardLayout typeCL = new CardLayout();

    public enum typeCE{ FUNCTION, POINT };
    public static String[] typeC = { "Function", "Point" };

    public PopUp(String disType, Window wind) {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(400, 200);

        setLayout(typeCL);

        // -- ADD
        JPanel addJP = new JPanel(new BorderLayout());
        JTextField valJTF = new JTextField();

        JComboBox typeJCB = new JComboBox(typeC);

        JPanel btnsAddJP = new JPanel();
        JButton addJB = new JButton("ADD");
        addJB.addActionListener(e -> {
            String t_input = Graphing.inputNaming(valJTF.getText(), typeJCB.getSelectedIndex());
            if (!t_input.equals("ERROR"))
                wind.inputsLi.addElement(t_input);
            setVisible(false);
        });
        JButton addCancelJB = new JButton("CANCEL");
        addCancelJB.addActionListener(e -> {
            setVisible(false);
        });
        btnsAddJP.add(addCancelJB);
        btnsAddJP.add(addJB);



        addJP.add(valJTF, BorderLayout.CENTER);
        addJP.add(typeJCB, BorderLayout.PAGE_START);
        addJP.add(btnsAddJP, BorderLayout.PAGE_END);


        // -- Edit
        JPanel editJP = new JPanel(new BorderLayout());
        JTextField newValJTF = new JTextField();

        JComboBox editTypeJCB = new JComboBox(typeC);

        JPanel btnsEditJP = new JPanel();
        JButton editJB = new JButton("EDIT");
        editJB.addActionListener(e -> {
            if (wind.inputsJL.getSelectedIndex() > -1) {
                String t_input = Graphing.inputNaming(newValJTF.getText(), editTypeJCB.getSelectedIndex());
                if (!t_input.equals("ERROR"))
                    wind.inputsLi.setElementAt(t_input, wind.inputsJL.getSelectedIndex());
                setVisible(false);
            }
        });
        JButton editCancelJB = new JButton("CANCEL");
        editCancelJB.addActionListener(e -> {
            setVisible(false);

        });
        btnsEditJP.add(editCancelJB);
        btnsEditJP.add(editJB);


        editJP.add(newValJTF, BorderLayout.CENTER);
        editJP.add(editTypeJCB, BorderLayout.PAGE_START);
        editJP.add(btnsEditJP, BorderLayout.PAGE_END);


        // ----
        add("add", addJP);
        add("edit", editJP);

        display(disType);
    }

    public void display(String disType) {
        typeCL.show(getContentPane(), disType);
    }

}
