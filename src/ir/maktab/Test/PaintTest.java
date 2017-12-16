package ir.maktab.Test;

import static org.junit.Assert.*;

import ir.maktab.Dao.UserDao;
import ir.maktab.Dao.UserDaoImpl;
import ir.maktab.Model.User;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by nader on 12/16/2017.
 */
public class PaintTest {
    UserDao userDao;

    @Before
    public void beforeTest() {
        userDao = new UserDaoImpl();
    }

    @Test
    public void testLogin() {
        try {
            assertFalse(userDao.isMember(new User("nima", "nima")));
            assertTrue(userDao.isMember(new User("nader", "1993")));

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSignup() {
        try {
            assertFalse(userDao.isMember(new User("nima", "nima")));
            assertTrue(userDao.add(new User("nima", "nima"), null));
            assertTrue(userDao.isMember(new User("nima", "nima")));

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoad() throws SQLException, IOException, ClassNotFoundException {

        assertTrue(userDao.load(new User("nader", "1993"))!=null);
        try {
            userDao.load(new User()).entrySet();
            fail("An error expected");
        }catch (SQLException e){
            e.printStackTrace();
        }catch (NullPointerException e1){
            System.out.println(e1.getMessage());
        }
    }
}
