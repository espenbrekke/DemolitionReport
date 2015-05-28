package org.demolitionreport.commands;

import org.demolitionreport.Command;
import org.demolitionreport.Report;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

public class ExecuteCommand implements Command {

    public static Command getExecuteCommand(String commandName, String[] command){
        try {
            if("execute_withStatus".equals(commandName)){
                final String[] strippedCommand= Arrays.copyOfRange(command, 1, command.length);
                return new ExecuteCommand(strippedCommand,true);
            }
            if("execute".equals(commandName)){
                final String[] strippedCommand= Arrays.copyOfRange(command, 1, command.length);
                return new ExecuteCommand(strippedCommand, false);
            }
        } catch (IllegalArgumentException e){
        }
        return null;
    }

    final String[] strippedCommand;
    final boolean outputStatus;
    ExecuteCommand(String[] strippedCommand,boolean outputStatus){
        this.strippedCommand=strippedCommand;
        this.outputStatus=outputStatus;
    }

    public String execute(Report report){
        int status=-1;
        try {
            ProcessBuilder builder = new ProcessBuilder(strippedCommand);
            builder.redirectErrorStream(true);
            Process process = builder.start();

            InputStream out=process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(out));

            boolean running=true;
            while (running) {
                String line = reader.readLine ();
                if(line!=null) report.report(line);
                else Thread.sleep(20);
                if(line==null && !process.isAlive()) running=false;
            }
            reader.close();
            status=process.exitValue();

        } catch (Exception e){
            return e.getMessage();
        }

        if(outputStatus){
            return ""+status;
        }else {
            return "";
        }
    }
}
