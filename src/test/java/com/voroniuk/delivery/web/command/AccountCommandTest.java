package com.voroniuk.delivery.web.command;

import com.voroniuk.delivery.Path;
import com.voroniuk.delivery.db.entity.Role;
import com.voroniuk.delivery.db.entity.User;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AccountCommandTest {

    private static AccountCommand accountCommand = new AccountCommand();

    private static User user;
    private static User manager;

    private static String uAcc = Path.COMMAND__USER_ACCOUNT;
    private static String mAcc = Path.COMMAND__MANAGER_ACCOUNT;
    private static String errorPage = Path.PAGE__ERROR_PAGE;

    private static HttpSession session = mock(HttpSession.class);

    private static HttpServletRequest request = mock(HttpServletRequest.class);
    private static HttpServletResponse response = mock(HttpServletResponse.class);

    @BeforeClass
    public static void init() {
        user = new User();
        user.setRole(Role.USER);

        manager = new User();
        manager.setRole(Role.MANAGER);

        when(request.getSession()).thenReturn(session);
    }

    @Test
    public void shouldReturnUserAccountWhenRoleUser() throws IOException, ServletException {
        when(session.getAttribute("user")).thenReturn(user);
        String forward = accountCommand.execute(request, response);
        assertEquals(uAcc, forward);
    }

    @Test
    public void shouldReturnManagerAccountWhenRoleManager() throws IOException, ServletException {
        when(session.getAttribute("user")).thenReturn(manager);
        String forward = accountCommand.execute(request, response);
        assertEquals(mAcc, forward);
    }

    @Test
    public void shouldReturnErrorPageWhenUserNull() throws IOException, ServletException {
        when(session.getAttribute("user")).thenReturn(null);
        String forward = accountCommand.execute(request, response);
        assertEquals(errorPage, forward);
    }

}