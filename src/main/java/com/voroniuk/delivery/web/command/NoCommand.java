package com.voroniuk.delivery.web.command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class NoCommand extends Command{
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        return null;
    }
}
