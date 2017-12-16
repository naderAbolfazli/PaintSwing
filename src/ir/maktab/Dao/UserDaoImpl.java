package ir.maktab.Dao;

import ir.maktab.Model.User;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nader on 11/19/2017.
 */
public class UserDaoImpl implements UserDao {
    protected String TABLE_NAME;


    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/paint";

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "";

    protected static Connection conn = null;
    protected static Statement stmt = null;
    protected static ResultSet rs;

    protected void init() {
        //STEP 2: Register JDBC driver
        try {
            Class.forName(JDBC_DRIVER);

            //STEP 3: Open a connection
            //System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            //STEP 4: Execute a query
            //System.out.println("Creating statement...");
            stmt = conn.createStatement();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void finalize(ResultSet rs, Statement stmt, Connection conn) {
        try {
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void finalize(Statement stmt, Connection conn) {
        try {
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean add(User user, Map<Shape, Color> shapeColorMap) throws SQLException {
        init();
        String sql;
        sql = shapeColorMap == null ? "INSERT INTO user_shapes(username, password) VALUES (?, ?)"
                : "INSERT INTO user_shapes(username, password, shapes) VALUES (?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, user.getUserName());
        pstmt.setString(2, user.getPassword());
        if (shapeColorMap != null)
            pstmt.setObject(3, shapeColorMap);
        int res = pstmt.executeUpdate();

        pstmt.close();
        conn.close();

        return res > 0;
    }

    @Override
    public Map<Shape, Color> load(User user) throws SQLException, IOException, ClassNotFoundException {
        init();
        String sql;
        sql = "SELECT shapes FROM user_shapes WHERE username = '" + user.getUserName() + "'";
        ResultSet rs = stmt.executeQuery(sql);
        if (! rs.next())
            return null;

        //Object object = rs.getObject(1);
        byte[] buf = rs.getBytes(1);
        ObjectInputStream objectIn;
        if (buf != null)
            objectIn = new ObjectInputStream(new ByteArrayInputStream(buf));
        else return null;

        Object deSerializedObject = objectIn.readObject();

        finalize(rs, stmt, conn);
        objectIn.close();
        return (Map<Shape, Color>) deSerializedObject;
    }

    @Override
    public boolean isMember(User user) throws SQLException, IOException, ClassNotFoundException {
        init();
        String sql;
        sql = "SELECT * FROM user_shapes WHERE username = '" + user.getUserName() + "' AND password = '" + user.getPassword() + "'";
        ResultSet rs = stmt.executeQuery(sql);
        boolean res = rs.next();

        finalize(rs, stmt, conn);
        return res;
    }

    @Override
    public boolean update(User user, Map<Shape, Color> shapeColorMap) throws SQLException {
        init();
        String sql;
        sql = "UPDATE user_shapes SET shapes = ? WHERE username ='" + user.getUserName() + "'";
        PreparedStatement pstmt = conn.prepareStatement(sql);

        pstmt.setObject(1, shapeColorMap);
        int res = pstmt.executeUpdate();

        pstmt.close();
        conn.close();

        return res > 0;
    }

    @Override
    public boolean delete(User user) {
        return false;
    }

    public boolean delete(int id) throws SQLException {
        init();
        String sql;
        sql = "DELETE from " + TABLE_NAME + " where id=" + id;
        int status = stmt.executeUpdate(sql);
        finalize(stmt, conn);

        return status > 0;
    }

    public boolean delete(String name) throws SQLException {
        init();
        String sql;
        sql = "DELETE from " + TABLE_NAME + " where username='" + name + "'";
        int status = stmt.executeUpdate(sql);
        finalize(stmt, conn);

        return status > 0;
    }

    public int deleteAll() throws SQLException {
        init();
        String sql = String.format("delete from %s", TABLE_NAME);
        int status = stmt.executeUpdate(sql);
        finalize(stmt, conn);

        return status;
    }

    public int searchByName(String name) throws SQLException {
        init();
        String sql = "select * from " + TABLE_NAME + " where username ='" + name + "'";
        ResultSet rs = stmt.executeQuery(sql);

        if (rs.next()) {
            int res = rs.getInt("id");
            finalize(rs, stmt, conn);
            return res;
        }

        finalize(rs, stmt, conn);
        return 0;
    }

    public boolean exist(int id) {
        init();
        try {
            String sql;
            sql = "SELECT * FROM " + TABLE_NAME + " WHERE id =" + id;
            rs = stmt.executeQuery(sql);

            if (rs.next())
                return true;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            finalize(rs, stmt, conn);
        }
        return false;
    }
}