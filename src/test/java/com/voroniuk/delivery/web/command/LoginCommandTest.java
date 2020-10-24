package com.voroniuk.delivery.web.command;


import com.voroniuk.delivery.Path;

import org.junit.Test;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class LoginCommandTest {


    private LoginCommand loginCommand = new LoginCommand();

    private String ok = Path.COMMAND__ACCOUNT;
    private String error = Path.PAGE__ERROR_PAGE;
    private String main = Path.COMMAND__MAIN;

    private HttpSession session = mock(HttpSession.class);

    private HttpServletRequest request = mock(HttpServletRequest.class);
    private HttpServletResponse response = mock(HttpServletResponse.class);

    private String login = "aaa";
    private String correctPassword = "aaa";
    private String incorrectPassword = "---";


    @Test
    public void shouldLoginIfCorrectLoginPass() throws IOException, ServletException {

        when(request.getParameter("login")).thenReturn(login);
        when(request.getParameter("password")).thenReturn(correctPassword);
        when(request.getSession()).thenReturn(session);
        assertEquals(ok, loginCommand.execute(request, response));
    }

    @Test
    public void shouldNotLoginIfCorrectLoginPass() throws IOException, ServletException {

        when(request.getParameter("login")).thenReturn(login);
        when(request.getParameter("password")).thenReturn(incorrectPassword);
        when(request.getSession()).thenReturn(session);
        assertEquals(main, loginCommand.execute(request, response));
    }


}