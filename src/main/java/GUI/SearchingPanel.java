package GUI;

import API.OSMDataPuller;
import org.openstreetmap.gui.jmapviewer.Coordinate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class SearchingPanel extends JPanel {
    private final Map map;
    private final SpringLayout layout;
    private final OSMDataPuller osmDataPuller;
    private final JTextField toFind;

    SearchingPanel(Map map) {
        this.map = map;
        osmDataPuller = new OSMDataPuller();
        layout = new SpringLayout();

        setLayout(layout);
        setPreferredSize(new Dimension(300, 0));
        setBackground(Color.white);
        setVisible(true);

        Label findLabel = new Label("Find:");
        toFind = new JTextField(20);

        toFind.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    showResults(toFind.getText());
                }
            }
        });

        add(toFind);
        add(findLabel);
        layout.putConstraint(SpringLayout.WEST, toFind, 10, SpringLayout.EAST, findLabel);
    }

    private void showResults(String toFindStr) {
        ArrayList<String> data = osmDataPuller.getData(toFindStr);
        String[] result = new String[data.size()];
        result = data.toArray(result);

        JList<String> list = new JList<>(result);
        list.setLayoutOrientation(JList.VERTICAL);

        JScrollPane results = new JScrollPane(list);
        results.setPreferredSize(new Dimension(300, 500));
        results.setVisible(true);

        list.addListSelectionListener(e -> {
            String elementAt = list.getModel().getElementAt(list.getSelectedIndex());
            Coordinate coordinate = osmDataPuller.getCoordinates(elementAt);
            map.setToPoint(coordinate);
        });

        add(results);
        layout.putConstraint(SpringLayout.NORTH, results, 10, SpringLayout.SOUTH, toFind);
    }
}
