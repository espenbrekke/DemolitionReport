package org.demolitionreport.commands;

import org.demolitionreport.Command;
import org.demolitionreport.Report;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by espen on 28.05.15.
 */
public class UtilsCommand implements Command {
    public static Command getUtilCommand(String query){
        try {
            if (query.startsWith("util."))
                return new UtilsCommand(query.replaceFirst("util.", ""));
        } catch (IllegalArgumentException e){

        }
        return null;
    }

    final String query;
    UtilsCommand(String query){
        if(!("time".equals(query))){
            throw new IllegalArgumentException("Unknown thread property "+query);
        }
        this.query=query;
    }

    public String execute(Report report){
        ThreadMXBean bean= ManagementFactory.getThreadMXBean();
        if("time".equals(query)) {
            Date now=new Date();
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yy:HH:mm:ss");
            return format.format(now);
        }
        return "";
    }
}