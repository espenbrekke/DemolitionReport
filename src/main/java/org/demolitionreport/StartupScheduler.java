package org.demolitionreport;

public class StartupScheduler  extends Thread{
    private final Report report;
    public StartupScheduler(Report report){
        this.report=report;
    }

    public void init(){
        report.init();
        this.start();
    }

    @Override
    public void run(){
        try {
            report.run();
        } catch (Exception e){
            //OK. we will exit
            report.report(e.getMessage());
        }
    }
}