package org.demolitionreport.commands;

import org.demolitionreport.Command;
import org.demolitionreport.Report;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.ThreadMXBean;


public class OsCommand implements Command {

    public static Command getOsCommand(String query){
        if(query.startsWith("os."))
            return new OsCommand(query.replaceFirst("os.",""));

        return null;
    }

    final String query;
    OsCommand(String query){
        if(!("architecture".equals(query)||"name".equals(query)||"version".equals(query)
                ||"available_processors".equals(query)||"system_load".equals(query))){
            throw new IllegalArgumentException("Unknown os property "+query);
        }
        this.query=query;
    }

    public String execute(Report report){
        OperatingSystemMXBean bean=ManagementFactory.getOperatingSystemMXBean();
        if("architecture".equals(query)) return ""+bean.getArch();
        if("name".equals(query)) return ""+bean.getName();
        if("version".equals(query)) return ""+bean.getVersion();
        if("available_processors".equals(query)) return ""+bean.getAvailableProcessors();
        if("system_load".equals(query)) return ""+bean.getSystemLoadAverage();

        return "";
    }
}
