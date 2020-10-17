import com.voroniuk.delivery.db.dao.*;
import com.voroniuk.delivery.db.entity.Delivery;
import com.voroniuk.delivery.db.entity.DeliveryStatus;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class Test {
    public static void main(String[] args) {

        CityDAO cityDAO = new CityDAO();
        UserDAO userDAO = new UserDAO();
        ResourceDAO resourceDAO = new ResourceDAO();
        OrderDAO orderDAO = new OrderDAO();

        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");

//        Date d1, d2, d3, d4;
//
//        d1 = d2 = d3 = d4 = new Date(0);
//
//        try {
//            d1 = format.parse("10.10.2020");
//            d2 = format.parse("11.10.2020");
//            d3 = format.parse("12.10.2020");
//            d4 = format.parse("08.10.2020");
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println(d1.getTime()+10860000);
//        System.out.println(d2.getTime()+10860000);
//        System.out.println(d3.getTime()+10860000);
//        System.out.println(d4.getTime()+10860000);

        final int ARBITARY__SIZE = 1048;

        File file = new File("src\\main\\webapp\\reports\\"+"a.xls");
        try {
            InputStream inputStream = new FileInputStream(file);


        OutputStream out = System.out;

        byte[]buffer = new byte[ARBITARY__SIZE];

        int numBytesRead;
        while ((numBytesRead = inputStream.read(buffer)) > 0) {
            out.write(buffer, 0, numBytesRead);
        }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Branch master. test message");
        System.out.println("Branch master. another test message");

    }
}
