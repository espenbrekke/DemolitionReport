package org.demolitionreport.commands;

import org.demolitionreport.Command;
import org.demolitionreport.Report;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

public class ExecuteCommand implements Command {

    public static Command getExecuteCommand(String commandName, String[] command){
        if("execute".equals(commandName)){
            final String[] strippedCommand= Arrays.copyOfRange(command, 1, command.length - 1);
            return new ExecuteCommand(strippedCommand);
        }
        return null;
    }

    final String[] strippedCommand;
    ExecuteCommand(String[] strippedCommand){
        this.strippedCommand=strippedCommand;
    }

    public String execute(Report report){
        try {
            ProcessBuilder builder = new ProcessBuilder(strippedCommand);
            builder.redirectErrorStream(true);
            Process process = builder.start();

            InputStream out=process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(out));

            String line;
            while ((line = reader.readLine ()) != null) {
                report.report(line);
            }
            reader.close();

        } catch (Exception e){
            return e.getMessage();
        }
        String retVal="executed";
        for(String part:strippedCommand){
            retVal+=" "+part;
        }
        return "executed "+strippedCommand;
    }
}
