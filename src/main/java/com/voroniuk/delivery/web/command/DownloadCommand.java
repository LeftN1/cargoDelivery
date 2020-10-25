package com.voroniuk.delivery.web.command;

import com.voroniuk.delivery.db.entity.Delivery;
import com.voroniuk.delivery.db.entity.Total;
import com.voroniuk.delivery.utils.Utils;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Creates xls document using Utils.createXLS() and provides downloading
 *
 * @author M. Voroniuk
 */

public class DownloadCommand extends Command {
    private static final Logger LOG = Logger.getLogger(DownloadCommand.class);
    private final int ARBITARY__SIZE = 1048;

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        String sessionId = req.getSession().getId();
        String reportName = sessionId + ".xls";

        Map<String, List<Delivery>> report = new LinkedHashMap<>();
        Map<String, Total> totals = new LinkedHashMap<>();

        report = (Map<String, List<Delivery>>) req.getSession().getAttribute("report");
        totals = (Map<String, Total>) req.getSession().getAttribute("totals");

        Locale locale = (Locale) req.getSession().getAttribute("locale");
        if(locale == null){
            locale = Locale.getDefault();
        }

        Utils.createXLS(report, totals, locale, reportName);
        LOG.debug("File created");

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
        LOG.debug("File downloaded");



        LOG.debug("Trying to delete file " + reportName);
        if(file.delete()){
            LOG.debug("report file deleted. name: " + reportName);
        }

        return null;
    }
}
