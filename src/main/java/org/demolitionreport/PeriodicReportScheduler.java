package org.demolitionreport;

import java.io.File;
import java.util.List;

class PeriodicReportScheduler extends Thread{
    private final long interval;
    private final Report report;
    public PeriodicReportScheduler(long interval, Report report){
        this.report=report;
        this.interval=interval;
    }

    public void init(){
        report.init();
        this.start();
    }

    @Override
    public void run(){
        try {
            while (true) {
                report.run();
                this.sleep(interval);
            }
        } catch (InterruptedException e){
            //OK. we will exit
            report.report("Thread interupted");
        }
    }
}
