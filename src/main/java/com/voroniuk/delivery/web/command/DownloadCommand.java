package com.voroniuk.delivery.web.command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class DownloadCommand extends Command {
    private final int ARBITARY__SIZE = 1048;
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        String sessionId = req.getSession().getId();
        String reportName = sessionId + ".xls";

        resp.setContentType("application/vnd.ms-excel");
        resp.setHeader("Content-disposition", "attachment; filename=report.xls");

        File file = new File("src\\main\\webapp\\reports\\"+reportName);

        try(InputStream in = new FileInputStream(file);
            OutputStream out = resp.getOutputStream()) {

            byte[]buffer = new byte[ARBITARY__SIZE];

            int numBytesRead;
            while ((numBytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, numBytesRead);
            }
        }

        return null;
    }
}
