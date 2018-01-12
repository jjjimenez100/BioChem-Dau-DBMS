package ph.biochem.prototypes;

import ph.biochem.modules.ConfigManagement;

import java.awt.*;
import java.io.File;
import java.io.FilenameFilter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;

public class test {
    public static void main(String[] args) throws Exception{
        //2018-01-03
        DateFormat fromFormat = new SimpleDateFormat("yyyy-MM-dd");
        fromFormat.setLenient(false);
        DateFormat toFormat = new SimpleDateFormat("M/d/yyyy");
        toFormat.setLenient(false);
        String dateStr = "2011-07-09";
        toFormat.format(fromFormat.parse(dateStr));
        Date date = fromFormat.parse(dateStr);
        System.out.println(toFormat.format(date));
    }
}
