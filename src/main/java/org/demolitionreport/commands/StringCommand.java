package org.demolitionreport.commands;

import org.demolitionreport.Command;
import org.demolitionreport.Report;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;

public class StringCommand implements Command {
    public static Command getStringCommand(String value){
        return new StringCommand(value);
    }

    final String value;
    StringCommand(String value){
        this.value=value;
    }

    public String execute(Report report){
        return value;
    }
}
