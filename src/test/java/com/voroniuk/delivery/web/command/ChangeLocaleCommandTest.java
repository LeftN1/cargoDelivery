package com.voroniuk.delivery.web.command;

import com.voroniuk.delivery.Path;
import com.voroniuk.delivery.db.entity.SiteLocale;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Locale;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ChangeLocaleCommandTest {

    private static ChangeLocaleCommand changeLocaleCommand = new ChangeLocaleCommand();

    private  HttpSession session;
    private  HttpServletRequest request;
    private  HttpServletResponse response;

    private static Locale en = SiteLocale.EN.getLocale();

    @Before
    public void init(){
        session = mock(HttpSession.class);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("choosenLang")).thenReturn("EN");
        when(request.getParameter("from")).thenReturn("main");
    }

    @Test
    public void shouldChangeLocale() throws IOException, ServletException {
        changeLocaleCommand.execute(request, response);
        verify(session).setAttribute("locale", en);
    }

    @Test
    public void shouldReturnBack() throws IOException, ServletException {
        String forward = changeLocaleCommand.execute(request, response);
        assertEquals(Path.COMMAND__MAIN, forward);
    }

}