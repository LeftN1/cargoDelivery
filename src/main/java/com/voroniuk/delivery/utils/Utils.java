package com.voroniuk.delivery.utils;

import javax.servlet.http.HttpServletRequest;

public class Utils {

    public static int getPageNoFromRequest(HttpServletRequest req,String paramName, int totalPages){
        int pageNo=1;
        try {
            pageNo = Integer.parseInt(req.getParameter(paramName));
        } catch (NumberFormatException e) {
            pageNo = 1;
        }

        if (pageNo < 1) {
            pageNo = 1;
        }
        if (pageNo > totalPages) {
            pageNo = totalPages;
        }
        return pageNo;
    }

}
