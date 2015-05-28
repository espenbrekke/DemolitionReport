package org.demolitionreport.commands;

import org.demolitionreport.Command;
import org.demolitionreport.Report;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

public class MemoryCommand implements Command {
    public static Command getMemoryCommand(String query){
        try {
            if(query.startsWith("memory.heap."))
                return new MemoryCommand("heap", query.replaceFirst("memory.heap.",""));
            else if(query.startsWith("memory.native"))
                return new MemoryCommand("native", query.replaceFirst("memory.native.",""));
        } catch (IllegalArgumentException e){
        }
        return null;
    }

    final String heapOrNative;
    final String query;
    MemoryCommand(String heapOrNative, String query){
        if(!("max".equals(query)||"used".equals(query)||"free".equals(query))){
            throw new IllegalArgumentException("Unknown memory property "+query);
        }
        this.heapOrNative=heapOrNative;
        this.query=query;
    }

    public String execute(Report report){
        MemoryUsage memUse=null;
        if("heap".equals(heapOrNative))
            memUse=ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
        else memUse=ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage();

        if("max".equals(query)) return toMb(memUse.getMax());
        else if("used".equals(query)) return toMb(memUse.getUsed());
        else if("free".equals(query)) return toMb(memUse.getMax()-memUse.getUsed());

        return "";
    }

    private String toMb(long bytes){
        return ""+(bytes/(1024*1024))+"mb";
    }
}
