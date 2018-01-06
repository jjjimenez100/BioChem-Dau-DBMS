package ph.biochem.prototypes;

import ph.biochem.modules.ConfigManagement;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class test {
    public static void main(String[] args) {
        String date = "3/5/1999";
        String date2 = "12/2/2018";
        DateTimeFormatter format = DateTimeFormatter.ofPattern("M/d/yyyy");
        LocalDate newDate = LocalDate.parse(date, format);
        LocalDate newDate2 = LocalDate.parse(date2, format);
        System.out.println(Period.between(newDate, newDate2).getYears());
        System.out.println(Period.between(newDate, LocalDate.now()).getYears());
    }
}
