package org.demolitionreport.commands;

import org.demolitionreport.Command;
import org.demolitionreport.Report;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.lang.management.ThreadMXBean;

public class ThreadCommand implements Command {
    public static Command getThreadCommand(String query){
        try {
        if(query.startsWith("thread."))
            return new ThreadCommand(query.replaceFirst("thread.",""));
        } catch (IllegalArgumentException e){

        }
        return null;
    }

    final String query;
    ThreadCommand(String query){
        if(!("count".equals(query)||"deamon".equals(query)||"peak".equals(query)||"started".equals(query))){
            throw new IllegalArgumentException("Unknown thread property "+query);
        }
        this.query=query;
    }

    public String execute(Report report){
        ThreadMXBean bean=ManagementFactory.getThreadMXBean();
        if("count".equals(query)) return ""+bean.getThreadCount();
        if("deamon".equals(query)) return ""+bean.getDaemonThreadCount();
        if("peak".equals(query)) return ""+bean.getPeakThreadCount();
        if("started".equals(query)) return ""+bean.getTotalStartedThreadCount();

        return "";
    }
}