package com.voroniuk.delivery.web.command;

import com.voroniuk.delivery.db.entity.SiteLocale;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.Locale;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ChangeLocaleCommandTest {

    ChangeLocaleCommand changeLocaleCommand = new ChangeLocaleCommand();

    private static HttpSession session = mock(HttpSession.class);
    private static HttpServletRequest request = mock(HttpServletRequest.class);
    private static HttpServletResponse response = mock(HttpServletResponse.class);

    private static Locale ru = SiteLocale.RU.getLocale();
    private static Locale en = SiteLocale.EN.getLocale();

    @BeforeClass
    public static void init(){
        Locale siteLocale = ru;
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("locale")).thenReturn(siteLocale);
    }

    @Test
    public static void shouldChangeLocale(){

    }

}