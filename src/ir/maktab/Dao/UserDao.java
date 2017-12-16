package ir.maktab.Dao;

import ir.maktab.Model.User;

import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by nader on 12/15/2017.
 */
public interface UserDao {

    boolean add(User user, Map<Shape, Color> shapeColorMap) throws SQLException;

    Map<Shape, Color> load(User user) throws SQLException, IOException, ClassNotFoundException;

    boolean update(User user, Map<Shape, Color> shapeColorMap) throws SQLException;

    boolean delete(User user);

    boolean isMember(User user) throws SQLException, IOException, ClassNotFoundException;

    int deleteAll() throws SQLException;
}
