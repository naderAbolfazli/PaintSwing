package ir.maktab.UI;

import ir.maktab.Dao.UserDao;
import ir.maktab.Dao.UserDaoImpl;
import ir.maktab.Model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by nader on 12/16/2017.
 */
public class Login extends JFrame {
    private HintTextField userName;
    private HintTextField passWord;
    private UserDao userDao;
    private Button signUp;

    public Login() throws HeadlessException {
        userDao = new UserDaoImpl();

        Container contentPane = getContentPane();
        contentPane.setLayout(null);
        setResizable(false);
        Font myFont = new Font(" BZar", Font.BOLD, 14);

        setSize(200, 250);
        setTitle("Paint");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        /////////// username TEXT ///////////////
        userName = new HintTextField("UserName");
        userName.setBounds(50, 50, 100, 25);
        contentPane.add(userName);

        /////////// password TEXT /////////////
        passWord = new HintTextField("Password");
        passWord.setBounds(50, 100, 100, 25);
        contentPane.add(passWord);

        /////////// login BUTTON //////////////
        Button login = new Button("ورود");
        login.setBounds(50, 160,100, 25);
        contentPane.add(login);
        login.addActionListener(e -> ActionPerformed(e));

/////////// login BUTTON //////////////
        signUp = new Button("ایجاد اکانت");
        signUp.setBounds(50, 210,100, 25);
        contentPane.add(signUp);
        signUp.addActionListener(e -> ActionPerformed(e));

        contentPane.setPreferredSize(new Dimension(200, 250));
        pack();
        setLocationRelativeTo(getOwner());
    }

    private void ActionPerformed(ActionEvent e) {
        try {
            if(userName.getText().equals("")||passWord.getText().equals("")) {
                JOptionPane.showMessageDialog(this, "empty Field!");
                return;
            }
            User user = new User(userName.getText(), passWord.getText());

            if (e.getSource() == signUp){
                if (userDao.isMember(user)){
                    JOptionPane.showMessageDialog(this, "User Already Exist!");
                    return;
                }
                userDao.add(user, null);
                return;
            }
            if (userDao.isMember(user)) {
                new Paint(user);
                dispose();
            }
            else {
                JOptionPane.showMessageDialog(this, "Wrong User Or Password");
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (SQLException e1) {
            e1.printStackTrace();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }
    }
}
