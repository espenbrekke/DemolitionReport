package org.demolitionreport;

import java.io.File;
import java.util.List;

class ShutdownReportScheduler extends Thread {
    private final Report report;
    public ShutdownReportScheduler(Report report){
        this.report=report;
    }

    public void init(){
        report.init();
        Runtime.getRuntime().addShutdownHook(this);
    }

    @Override
    public void run(){
        report.run();
    }

}
