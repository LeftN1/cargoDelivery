package com.voroniuk.delivery.db.dao;

import com.voroniuk.delivery.db.entity.Role;
import com.voroniuk.delivery.db.entity.User;
import org.junit.*;

import java.util.List;

import static org.junit.Assert.*;

public class UserDAOTest {
    static UserDAO userDAO;
    static User user;
    static String login = "login";
    static String password = "password";
    static Role role = Role.USER;

    @BeforeClass
    public static void init() {
        userDAO = new UserDAO();
        user = new User();
        user.setLogin(login);
        user.setPassword(password);
        user.setRole(role);
        userDAO.saveUser(user);
    }

    @Before
    public  void before(){

    }

    @AfterClass
    public static void delete(){
        userDAO.deleteUser(user);
    }

    @Test
    public void ShouldChangeId() {
        assertTrue(user.getId() > 0);
    }

    @Test
    public void findUserByLogin() {
        User newUser = new User();
        newUser = userDAO.findUserByLogin(login);
        assertEquals(user.getId(), newUser.getId());
    }

    @Test
    public void findUserById() {
        int id = user.getId();
        User newUser = new User();
        newUser = userDAO.findUserById(id);
        assertEquals(user.getLogin(), newUser.getLogin());
    }

//    @Test
//    public void findAllUsers() {
//        List<User> userList = userDAO.findAllUsers();
//
//        assertTrue(userList.size()>0);
//
//        User founded = new User();
//
//        for (User u:userList){
//            if(u.getId() == user.getId()){
//                founded = u;
//            }
//        }
//
//        assertEquals(user.getLogin(), founded.getLogin());
//
//    }

    @Test
    public void deleteUser() {
        User newUser = new User();
        newUser.setLogin("_");
        newUser.setPassword("_");
        newUser.setRole(role);
        userDAO.saveUser(newUser);
        userDAO.deleteUser(newUser);
        assertTrue(userDAO.findUserById(newUser.getId()) == null);
    }

}