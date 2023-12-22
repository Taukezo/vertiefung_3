package org.aulich.wbh.vertiefung_3.report;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.*;

public class ReportTest {
    @Test
    public void test(){
        Report report = new Report();
        report.getReportModel().setClassName(this.getClass().getSimpleName());
        report.getReportModel().setDate(new Date());
        report.getReportModel().setDurationStandardDeviation(new BigDecimal("5.4"));
        assertTrue(report.save());
    }

}