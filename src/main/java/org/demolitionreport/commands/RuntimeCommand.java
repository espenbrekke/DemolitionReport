package org.demolitionreport.commands;

import org.demolitionreport.Command;
import org.demolitionreport.Report;
import org.demolitionreport.TerminationSignalHandler;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;

public class RuntimeCommand implements Command {

    public static Command getRuntimeCommand(String query){
        if(query.startsWith("runtime."))
            return new RuntimeCommand(query.replaceFirst("runtime.",""));

        return null;
    }

    final String query;
    RuntimeCommand(String query){
        this.query=query;
    }

    public String execute(Report report){
        RuntimeMXBean bean= ManagementFactory.getRuntimeMXBean();
        if("boot_classpath".equals(query)) return ""+bean.getBootClassPath();
        if("classpath".equals(query)) return ""+bean.getClassPath();
        if("inputarguments".equals(query)) return ""+bean.getInputArguments();
        if("librarypath".equals(query)) return ""+bean.getLibraryPath();
        if("managentspeck_version".equals(query)) return ""+bean.getManagementSpecVersion();
        if("name".equals(query)) return ""+bean.getName();
        if("spec.vendor".equals(query)) return ""+bean.getSpecVendor();
        if("spec.version".equals(query)) return ""+bean.getSpecVersion();
        if("vm.name".equals(query)) return ""+bean.getVmName();
        if("vm.version".equals(query)) return ""+bean.getVmVersion();
        if("starttime".equals(query)) return ""+bean.getStartTime();
        if("systemproperties".equals(query)) return ""+bean.getSystemProperties();
        if("uptime".equals(query)) return ""+bean.getUptime();

        if("killcode".equals(query)) return ""+ TerminationSignalHandler.getExitCode();

        return "";
    }
}
