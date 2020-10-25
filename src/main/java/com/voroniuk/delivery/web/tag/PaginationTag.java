package com.voroniuk.delivery.web.tag;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Class for implement custom jstl tag that provides pagination of database items
 *
 * @author M. Voroniuk
 */
public class PaginationTag extends TagSupport {

    private static final Logger LOG = Logger.getLogger(PaginationTag.class);

    private int pageno;
    private int total;
    private String current;

    @Override
    public int doStartTag() throws JspException {
        JspWriter out = pageContext.getOut();
        Locale locale = (Locale) pageContext.getSession().getAttribute("locale");

        ResourceBundle rb = ResourceBundle.getBundle("resources", locale);

        try {
            out.print("<ul class=\"pagination\">");
            if (pageno > 2) {
                out.print("<li class=\"page-item\">");
                out.print(String.format("<a class=\"page-link\" href=\"%s&page=1\">%s</a>", current, rb.getString("all.href.first")));
                out.print("</li>");
            }

            int start = pageno - 2 > 1 ? pageno - 2 : 1;
            int end = pageno + 2 < total ? pageno + 2 : total;

            for (int i = start; i <= end; i++) {

                if (i == pageno) {
                    out.print("<li class=\"page-item active\" aria-current=\"page\">");
                    out.print(String.format("<a class=\"page-link\" href=\"%s&page=%d\">%d <span class=\"sr-only\">(current)</span></a>", current, i, i));
                } else {
                    out.print("<li class=\"page-item\">");
                    out.print(String.format("<a class=\"page-link\" href=\"%s&page=%d\">%d</a>", current, i, i));
                }
                out.print("</li>");

            }

            if (total - pageno > 2) {
                out.print("<li class=\"page-item\">");
                out.print(String.format("<a class=\"page-link\" href=\"%s&page=%d\">%s</a>", current, total, rb.getString("all.href.last")));
                out.print("</li>");
            }

            out.print("</ul>");

        } catch (IOException e) {
            LOG.error(e);
        }

        return SKIP_BODY;
    }

    public int getPageno() {
        return pageno;
    }

    public void setPageno(int pageno) {
        this.pageno = pageno;
    }

    public int getTotal() {
        return total;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public void setTotal(int total) {
        this.total = total;


    }
}
