package ir.maktab.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nader on 12/13/2017.
 */

public class RectDraw extends JFrame {
    private Color selectedColor = Color.BLACK;
    private String selectedShape = "line";

    public RectDraw() {
        initUI();
    }

    private void initUI() {

        getContentPane().setBackground(new Color(186, 186, 186));
        getContentPane().setLayout(null);
        setResizable(false);

        setSize(650, 550);
        setTitle("Paint");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        /*DrawPanel dpnl = new DrawPanel();
        dpnl.setOpaque(true);
        dpnl.setBackground(new Color(255, 255, 255));
        dpnl.setBounds(5, 5, 505, 505);
        add(dpnl);*/

        PaintSurface paintS = new PaintSurface();
        //paintS.setOpaque(true);
        paintS.setBackground(new Color(255, 255, 255));
        paintS.setBounds(5, 5, 505, 505);
        add(paintS);

        Button line = new Button("خط");
        line.setBounds(525, 25, 100, 25);
        line.setActionCommand("line");
        line.addActionListener(e -> ActionPerformed(e));
        add(line);

        Button oval = new Button("دایره");
        oval.setBounds(525, 75, 100, 25);
        oval.setActionCommand("oval");
        oval.addActionListener(e -> ActionPerformed(e));
        add(oval);

        Button rectangle = new Button("مستطیل");
        rectangle.setBounds(525, 125, 100, 25);
        rectangle.setActionCommand("rectangle");
        rectangle.addActionListener(e -> ActionPerformed(e));
        add(rectangle);

        Label label = new Label("انتخاب رنگ");
        label.setBounds(525, 175, 100, 25);
        label.setAlignment(Label.CENTER);
        add(label);

        JRadioButton black = new JRadioButton("مشکی");
        black.setBounds(525, 225, 100, 25);
        black.setActionCommand("black");
        black.addActionListener(e -> ActionPerformed(e));
        black.setHorizontalAlignment(SwingConstants.CENTER);
        black.setSelected(true);
        add(black);

        JRadioButton red = new JRadioButton("قرمز");
        red.setBounds(525, 275, 100, 25);
        red.setActionCommand("red");
        red.addActionListener(e -> ActionPerformed(e));
        red.setHorizontalAlignment(SwingConstants.CENTER);
        add(red);

        JRadioButton green = new JRadioButton("سبز");
        green.setBounds(525, 325, 100, 25);
        green.setActionCommand("green");
        green.addActionListener(e -> ActionPerformed(e));
        green.setHorizontalAlignment(SwingConstants.CENTER);
        add(green);

        JRadioButton blue = new JRadioButton("آبی");
        blue.setBounds(525, 375, 100, 25);
        blue.setActionCommand("blue");
        blue.addActionListener(e -> ActionPerformed(e));
        blue.setHorizontalAlignment(SwingConstants.CENTER);
        add(blue);

        ButtonGroup group = new ButtonGroup();
        group.add(black);
        group.add(red);
        group.add(green);
        group.add(blue);

        Button logOut = new Button("خروج");
        logOut.setBounds(525, 450, 100, 25);
        add(logOut);

    }

    private void ActionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "black":
                selectedColor = Color.BLACK;
                break;
            case "red":
                selectedColor = Color.RED;
                break;
            case "green":
                selectedColor = Color.GREEN;
                break;
            case "blue":
                selectedColor = Color.BLUE;
                break;
            case "line":
                selectedShape = "line";
                break;
            case "oval":
                selectedShape = "oval";
                break;
            case "rectangle":
                selectedShape = "rectangle";
                break;
        }
    }

    private class PaintSurface extends JPanel {
        Map<Shape, Color> shapes = new HashMap<>();

        Point startDrag, endDrag;

        public PaintSurface() {
            this.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    startDrag = new Point(e.getX(), e.getY());
                    endDrag = startDrag;
                    repaint();
                }

                public void mouseReleased(MouseEvent e) {
                    Shape r = makeRectangle(startDrag.x, startDrag.y, e.getX(), e.getY());
                    shapes.put(r, selectedColor);
                    startDrag = null;
                    endDrag = null;
                    repaint();
                }
            });

            this.addMouseMotionListener(new MouseMotionAdapter() {
                public void mouseDragged(MouseEvent e) {
                    endDrag = new Point(e.getX(), e.getY());
                    repaint();
                }
            });
        }

        private void paintBackground(Graphics2D g2) {
            g2.setPaint(selectedColor);
            for (int i = 0; i < getSize().width; i += 10) {
                Shape line = new Line2D.Float(i, 0, i, getSize().height);
                g2.draw(line);
            }

            for (int i = 0; i < getSize().height; i += 10) {
                Shape line = new Line2D.Float(0, i, getSize().width, i);
                g2.draw(line);
            }

        }

        public void paint(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            for (Map.Entry<Shape, Color> s : shapes.entrySet()) {
                g2.setPaint(s.getValue());
                g2.draw(s.getKey());
            }

            if (startDrag != null && endDrag != null) {
                g2.setPaint(selectedColor);
                Shape r = makeRectangle(startDrag.x, startDrag.y, endDrag.x, endDrag.y);
                g2.draw(r);
            }
        }

        private Rectangle2D.Float makeRectangle(int x1, int y1, int x2, int y2) {
            return new Rectangle2D.Float(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
        }
    }

    class DrawPanel extends JPanel {

        private void doDrawing(Graphics g) {

            Graphics2D g2d = (Graphics2D) g;

            g2d.setColor(new Color(19, 19, 19));
            g2d.drawRect(0, 0, 500, 500);
            g2d.drawOval(300, 300, 20, 20);

            g2d.setColor(new Color(125, 167, 116));
            g2d.fillRect(10, 15, 90, 60);

        }

        @Override
        public void paintComponent(Graphics g) {

            super.paintComponent(g);
            doDrawing(g);
        }
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> new RectDraw());
    }
}