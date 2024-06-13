package graphing;

import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Window extends JFrame {

    JPanel bdlJP = new JPanel(new BorderLayout()); // BorderLayout JPanel

    Canvas cnv = new Canvas() { public void paint(Graphics g) {draw(g); } };

    DefaultListModel<String> inputsLi = new DefaultListModel<>(); // LOAD //// SAFE
    JList<String> inputsJL = new JList<>(inputsLi);

    JPanel btnsJP = new JPanel(new GridLayout());
    JButton addJB = new JButton("ADD");
    JButton removeJB = new JButton("REMOVE");
    JButton editJB = new JButton("RENAME");

    JPanel loadASafeJP = new JPanel(new GridLayout());
    JButton loadJB = new JButton("LOAD");
    JButton saveJB = new JButton("SAVE");

    JTextArea notesTA = new JTextArea("This is a beautiful note :D"); // LOAD //// SAFE


    PopUp popUp = new PopUp("add", this);


    public Window(int width, int height) {
        // -- WINDOW - START
        //setMinimumSize();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(width, height);


        // -- Canvas / Inputs / Buttons / Load And Save
        //cnv.setBackground(new Color(235, 235, 235));
        cnv.setBackground(Color.RED); // Replace with the line above !!!!!!!!!!!!!!!!!!!!!!!!


        inputsLi.addElement(Graphing.inputNaming("4*x", PopUp.typeCE.FUNCTION.ordinal()));
        inputsLi.addElement(Graphing.inputNaming("2; 4", PopUp.typeCE.POINT.ordinal()));


        addJB.addActionListener(e -> {
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


        loadJB.addActionListener(e -> { loadFile(); });
        saveJB.addActionListener(e -> { saveFile(null, true); });

        loadASafeJP.add(loadJB, BorderLayout.LINE_START);
        loadASafeJP.add(saveJB, BorderLayout.LINE_END);

        // -- JPanel
        bdlJP.add(cnv, BorderLayout.CENTER);                // Center
        bdlJP.add(inputsJL, BorderLayout.LINE_START);       // Left
        bdlJP.add(notesTA, BorderLayout.LINE_END);          // Right
        bdlJP.add(loadASafeJP, BorderLayout.PAGE_START);
        bdlJP.add(btnsJP, BorderLayout.PAGE_END);           // Bottom


        // -- WINDOW - END
        add(bdlJP);
        /* pack();
        setSize(width, height); */

    }



    public void draw(Graphics g)
    {
        if(notesTA.getText().isEmpty()) notesTA.setText("---------------");
        //repaint();
    }


    // LOAD AND SAVE
    JFileChooser fileJFC = new JFileChooser();

    public void loadFile() {
        saveFile("Auto-BackUp-" + System.currentTimeMillis() + ".pgraph", false);
        if (fileJFC.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            Scanner scan = null;
            try {
                scan = new Scanner(fileJFC.getSelectedFile());
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            enum scanModeE {
                INPUTS,
                NOTES
            }
            notesTA.setText(notesTA.getText() + "\n\n");
            int scanMode = -1;
            while (scan.hasNextLine()) { // maybe delete the "continue" statements
                String data = scan.nextLine();
                if (data.startsWith("- ")) {
                    data = data.substring("- ".length());
                    if (data.equalsIgnoreCase("inputs")) {
                        scanMode = scanModeE.INPUTS.ordinal();
                    } else if (data.equalsIgnoreCase("notes")) {
                        scanMode = scanModeE.NOTES.ordinal();
                    }
                } else if (scanMode != -1) {
                    if (scanMode == scanModeE.INPUTS.ordinal()) {
                        data = data.substring("-- ".length());
                        int t_type = Graphing.detectType(data);
                        if (t_type == -1) continue;
                        String t_input = Graphing.inputNaming(data, t_type);
                        if (!t_input.equalsIgnoreCase("ERROR")) inputsLi.addElement(t_input);
                    } else if (scanMode == scanModeE.NOTES.ordinal()) { // "else if" instead of "else" for future (if more modes are getting added)
                        notesTA.setText(notesTA.getText() + "\n" + data);
                    }
                }
            }
            notesTA.setText(notesTA.getText() + "\n\n");
        }
    }
    public void saveFile(@Nullable String fileName, boolean dialog) {
        if (!dialog || fileJFC.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            if (fileName == null) fileName = fileJFC.getSelectedFile().getAbsolutePath();
            if (fileName == null) fileName = "NoName.pgraph";

            File t_file = new File(fileName);
            try {
                t_file.createNewFile();
                try {
                    FileWriter t_fileW = new FileWriter(t_file);
                    t_fileW.write("- inputs\n");

                    for(Object t_str : inputsLi.toArray())
                        t_fileW.write("-- " + t_str + "\n");

                    t_fileW.write("- notes\n");
                    t_fileW.write(notesTA.getText());

                    t_fileW.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

}
