package ir.maktab.UI;

import ir.maktab.Dao.UserDao;
import ir.maktab.Dao.UserDaoImpl;
import ir.maktab.Model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nader on 12/13/2017.
 */

public class Paint extends JFrame {
    private Color selectedColor = Color.BLACK;
    private String selectedShape = "line";
    private User user;
    private UserDao userDao;
    Map<Shape, Color> shapes;

    public Paint(User user) {
        try {
            userDao = new UserDaoImpl();
            shapes = userDao.load(user);
            this.user = user;
            initUI();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initUI() {

        getContentPane().setBackground(new Color(186, 186, 186));
        getContentPane().setLayout(null);
        setResizable(false);
        Font myFont = new Font("BZar", Font.BOLD, 14);


        setSize(650, 550);
        setTitle("Paint");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        PaintSurface paintS = new PaintSurface();
        //paintS.setOpaque(true);
        paintS.setBackground(new Color(255, 255, 255));
        paintS.setBounds(5, 5, 505, 505);
        add(paintS);

        Button line = new Button("خط");
        line.setFont(myFont);
        line.setBounds(525, 25, 100, 25);
        line.setActionCommand("line");
        line.addActionListener(e -> ActionPerformed(e));
        add(line);

        Button oval = new Button("دایره");
        oval.setFont(myFont);
        oval.setBounds(525, 75, 100, 25);
        oval.setActionCommand("oval");
        oval.addActionListener(e -> ActionPerformed(e));
        add(oval);

        Button rectangle = new Button("مستطیل");
        rectangle.setFont(myFont);
        rectangle.setBounds(525, 125, 100, 25);
        rectangle.setActionCommand("rectangle");
        rectangle.addActionListener(e -> ActionPerformed(e));
        add(rectangle);

        Label label = new Label("انتخاب رنگ");
        label.setFont(myFont);
        label.setBounds(525, 175, 100, 25);
        label.setAlignment(Label.CENTER);
        add(label);

        JRadioButton black = new JRadioButton("مشکی");
        black.setFont(myFont);
        black.setBounds(525, 225, 100, 25);
        black.setBackground(Color.GRAY);
        black.setActionCommand("black");
        black.addActionListener(e -> ActionPerformed(e));
        black.setHorizontalAlignment(SwingConstants.CENTER);
        black.setSelected(true);
        add(black);

        JRadioButton red = new JRadioButton("قرمز");
        red.setFont(myFont);
        red.setBounds(525, 275, 100, 25);
        red.setBackground(Color.RED);
        red.setActionCommand("red");
        red.addActionListener(e -> ActionPerformed(e));
        red.setHorizontalAlignment(SwingConstants.CENTER);
        add(red);

        JRadioButton green = new JRadioButton("سبز");
        green.setFont(myFont);
        green.setBounds(525, 325, 100, 25);
        green.setBackground(Color.GREEN);
        green.setActionCommand("green");
        green.addActionListener(e -> ActionPerformed(e));
        green.setHorizontalAlignment(SwingConstants.CENTER);
        add(green);

        JRadioButton blue = new JRadioButton("آبی");
        blue.setFont(myFont);
        blue.setBounds(525, 375, 100, 25);
        blue.setBackground(Color.BLUE);
        blue.setActionCommand("blue");
        blue.addActionListener(e -> ActionPerformed(e));
        blue.setHorizontalAlignment(SwingConstants.CENTER);
        add(blue);

        ButtonGroup group = new ButtonGroup();
        group.add(black);
        group.add(red);
        group.add(green);
        group.add(blue);

        Label username = new Label(user.getUserName() + " خوش آمدید");
        username.setFont(myFont);
        username.setBounds(525, 450, 100, 25);
        username.setAlignment(Label.CENTER);
        add(username);

        Button logOut = new Button("خروج");
        logOut.setFont(myFont);
        logOut.setActionCommand("logOut");
        logOut.addActionListener(e -> ActionPerformed(e));
        logOut.setBounds(525, 480, 100, 25);
        add(logOut);

    }

    private void ActionPerformed(ActionEvent e) {
        try {
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
                case "logOut":
                    userDao.update(user, shapes);
                    new Login();
                    dispose();
                    break;
            }

        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    private class PaintSurface extends JPanel {
        {
            if (shapes==null)
                shapes = new HashMap<>();
        }

        Point startDrag, endDrag;

        public PaintSurface() {
            this.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    startDrag = new Point(e.getX(), e.getY());
                    endDrag = startDrag;
                    repaint();
                }

                public void mouseReleased(MouseEvent e) {
                    Shape shape = selectedShape.equals("rectangle") ? makeRectangle(startDrag.x, startDrag.y, endDrag.x, endDrag.y)
                            : selectedShape.equals("line") ? makeLine(startDrag.x, startDrag.y, endDrag.x, endDrag.y)
                            : makeOval(startDrag.x, startDrag.y, endDrag.x, endDrag.y);
                    shapes.put(shape, selectedColor);
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

        @Override
        public void paint(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(4));

            for (Map.Entry<Shape, Color> s : shapes.entrySet()) {
                g2.setPaint(s.getValue());
                g2.draw(s.getKey());
            }

            if (startDrag != null && endDrag != null) {
                g2.setPaint(selectedColor);

                Shape shape = selectedShape.equals("rectangle") ? makeRectangle(startDrag.x, startDrag.y, endDrag.x, endDrag.y)
                        : selectedShape.equals("line") ? makeLine(startDrag.x, startDrag.y, endDrag.x, endDrag.y)
                        : makeOval(startDrag.x, startDrag.y, endDrag.x, endDrag.y);
                g2.draw(shape);
            }
        }

        private Shape makeOval(int x1, int y1, int x2, int y2) {
            return new Ellipse2D.Float(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2)*2, Math.abs(y1 - y2)*2);
        }

        private Shape makeLine(int x, int y, int x1, int y1) {
            return new Line2D.Float(x, y, x1, y1);
        }

        private Rectangle2D.Float makeRectangle(int x1, int y1, int x2, int y2) {
            return new Rectangle2D.Float(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
        }
    }

}