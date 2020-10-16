package com.voroniuk.delivery.web.command;

import com.sun.deploy.net.HttpRequest;
import com.sun.deploy.net.HttpResponse;
import com.voroniuk.delivery.Path;
import com.voroniuk.delivery.db.dao.UserDAO;
import com.voroniuk.delivery.db.entity.User;
import org.junit.Before;
import org.junit.Test;

import javax.jws.soap.SOAPBinding;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class LoginCommandTest {


        UserDAO userDAO = new UserDAO();
        LoginCommand loginCommand = new LoginCommand();

        String ok = Path.COMMAND__ACCOUNT;
        String error = Path.PAGE__ERROR_PAGE;
        String main = Path.COMMAND__MAIN;

        HttpSession session = mock(HttpSession.class);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        String login = "aaa";
        String correctPassword ="aaa";
        String incorrectPassword="---";




    @Test
    public void shouldLoginIfCorrectLoginPass() throws IOException, ServletException {

        when(request.getParameter("login")).thenReturn(login);
        when(request.getParameter("password")).thenReturn(correctPassword);
        when(request.getSession()).thenReturn(session);
        assertEquals(ok, loginCommand.execute(request,response));
    }

    @Test
    public void shouldNotLoginIfCorrectLoginPass() throws IOException, ServletException {

        when(request.getParameter("login")).thenReturn(login);
        when(request.getParameter("password")).thenReturn(incorrectPassword);
        when(request.getSession()).thenReturn(session);
        assertEquals(main, loginCommand.execute(request,response));
    }


}