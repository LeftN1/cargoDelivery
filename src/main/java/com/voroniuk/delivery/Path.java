package com.voroniuk.delivery;

public class Path {

    //pages
    public static final String PAGE__MAIN = "/main.jsp";
    public static final String PAGE__REGISTER = "/WEB-INF/jsp/register.jsp";
    public static final String PAGE__USER_ACCOUNT = "/WEB-INF/jsp/user_account.jsp";
    public static final String PAGE__MANAGER_ACCOUNT = "/WEB-INF/jsp/manager_account.jsp";
    public static final String PAGE__ERROR_PAGE = "/WEB-INF/jsp/error_page.jsp";

    //commands
    public static final String COMMAND__USER_ACCOUNT = "/controller?command=user_account";
    public static final String COMMAND__MANAGER_ACCOUNT = "/controller?command=manager_account";
}
