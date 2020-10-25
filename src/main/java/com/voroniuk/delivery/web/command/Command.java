package com.voroniuk.delivery.web.command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * Main interface for the Command pattern implementation.
 *
 *
 */
public abstract class Command implements Serializable {

    /**
     * Execution method for command.
     * @return Address to go once the command is executed.
     */
    public abstract String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException;

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
