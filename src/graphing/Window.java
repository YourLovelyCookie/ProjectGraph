package graphing;

import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Window extends JFrame {

    JPanel bdlJP = new JPanel(new BorderLayout()); // BorderLayout JPanel

    JPanel cnv = new JPanel() { public void paintComponent(Graphics g) {paintComponentCnv(g); } };

    // Editable settings for canvas
    double zoom = 1;
    double zoomStrength = 0.2;
    float lineWidth = 5;
    float levelOfDetail = 0.05f;
    float minX = -1000;
    float maxX = 1000;


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
        /*System.out.println(Graphing.calculate(Graphing.inputNaming("-4*(5+(-3-2)/8)+4", 0), 5));
        System.out.println();
        System.out.println(Graphing.calculate(Graphing.inputNaming("-3*-2/5^(3*x)/4+5", 0), 5));*/

        // -- Canvas / Inputs / Buttons / Load And Save

        inputsLi.addElement(Graphing.inputNaming("0.005*x^2", PopUp.typeCE.FUNCTION.ordinal())); // 4x
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


        loadJB.addActionListener(e -> loadFile());
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

        setMinimumSize(new Dimension(800, 400));
        setResizable(false);
        addMouseWheelListener(e -> {
            if (e.isControlDown()) {
                if (e.getWheelRotation() < 0) {
                    setSize(getWidth()-200, getWidth()/2);
                    setSize(getWidth(), getWidth()/2);
                } else {
                    setSize(getWidth()+200, getWidth()/2);
                    setSize(getWidth(), getWidth()/2);
                }
            } else {
                if (e.getWheelRotation() < 0) {
                    zoom += zoomStrength;
                } else if(zoom-zoomStrength > 0) {
                    zoom -= zoomStrength;
                }
                repaint();
            }

        });
    }

    public void paintComponentCnv(Graphics g)
    {
        if(notesTA.getText().isEmpty()) notesTA.setText("---------------");

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(new Color(235, 235, 235));
        g2d.fillRect(0, 0, cnv.getWidth(), cnv.getHeight());
        /*g2d.translate(0, getHeight() - 1);
        g2d.scale(1, -1);

        g2d.drawLine(0, 0, 100, 100);*/
        int width = getWidth();
        int height = getHeight();

        g2d.translate(width / 2, height / 2);

        // Now you can draw shapes relative to the center
        // For example, draw a line from the center to the top right corner
        g2d.scale(1*zoom, -1*zoom);
        g2d.setColor(Color.BLACK);

        g2d.setStroke(new BasicStroke(3));
        /*for(Object str : inputsLi.toArray()) {
            for(float i = 0; i < maxX; i+=levelOfDetail)
                Graphing.calculate(str.toString(), i);
        }
        for(Object str : inputsLi.toArray()) {
            for(float i = 0; i > minX; i-=levelOfDetail)
                Graphing.calculate(str.toString(), i);
        }*/
        /**/Random rand = new Random();
        for (Object str : inputsLi.toArray()) {
            rand.setSeed(str.hashCode());
            g2d.setColor(new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat()));
            if (Graphing.detectType(str.toString()) == PopUp.typeCE.FUNCTION.ordinal()) {
                ArrayList<Float> xFA = new ArrayList<Float>();
                ArrayList<Float> yFA = new ArrayList<Float>();
                for (float i = 0; i <= maxX; i+=levelOfDetail) {
                    xFA.add(i);
                    yFA.add(Graphing.calculate(str.toString(), i));
                }
                for (int i = 0; i < xFA.size()-1; i++) {
                    g2d.drawLine(Math.round(xFA.get(i)), Math.round(yFA.get(i)), Math.round(xFA.get(i+1)), Math.round(yFA.get(i+1)));
                }
                xFA.clear();
                yFA.clear();
                for (float i = 0; i > minX; i-=levelOfDetail) {
                    xFA.add(i);
                    yFA.add(Graphing.calculate(str.toString(), i));
                }
                for (int i = 0; i < xFA.size()-1; i++) {
                    g2d.drawLine(Math.round(xFA.get(i)), Math.round(yFA.get(i)), Math.round(xFA.get(i+1)), Math.round(yFA.get(i+1)));
                }
            }
        }
        //float j = Graphing.calculate(Graphing.inputNaming("-12+12", PopUp.typeCE.FUNCTION.ordinal()), 0);             ////        TEST
        //float j = Graphing.calculate(Graphing.funcPrefix + "1x^2", 2);
        //System.out.println(j);/**/

        //repaint();
    }


    // LOAD AND SAVE
    JFileChooser fileJFC = new JFileChooser();

    public void loadFile() {
        //saveFile("Auto-BackUp-" + System.currentTimeMillis() + ".pgraph", false);          //// AutoBackUp
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
