package org.demolitionreport.commands;

import com.sun.management.HotSpotDiagnosticMXBean;
import org.demolitionreport.Command;
import org.demolitionreport.Report;

import javax.management.MBeanServer;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UtilsCommand implements Command {
    public static Command getUtilCommand(String query, String[] command){
        try {
            if (query.startsWith("util."))
                return new UtilsCommand(query.replaceFirst("util.", ""), command);
        } catch (IllegalArgumentException e){

        }
        return null;
    }

    final String query;
    final String[] command;
    UtilsCommand(String query, String[] command){
        if(!( "time".equals(query) || "dump".equals(query) )){
            throw new IllegalArgumentException("Unknown thread property "+query);
        }
        this.query=query;
        this.command=command;
    }

    public String execute(Report report){
        if("time".equals(query)) {
            Date now=new Date();
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yy:HH:mm:ss");
            if(command.length>1){
                format = new SimpleDateFormat(command[1]);
            }
            return format.format(now);
        }
        if("dump".equals(query)) {
            String dumpFile="demolitionReport.hprof";
            if(command.length>1){
                dumpFile=command[1];
            }
            try {
                final String HOTSPOT_BEAN_NAME =
                        "com.sun.management:type=HotSpotDiagnostic";
                MBeanServer server = ManagementFactory.getPlatformMBeanServer();
                HotSpotDiagnosticMXBean bean =
                        ManagementFactory.newPlatformMXBeanProxy(server,
                                HOTSPOT_BEAN_NAME, HotSpotDiagnosticMXBean.class);
                bean.dumpHeap(dumpFile, false);
                return "requested heap dump";
            } catch (Exception e){
                return "Error: "+e.getMessage()+" while requesting heap dump.";
            }
        }
        return "";
    }
}