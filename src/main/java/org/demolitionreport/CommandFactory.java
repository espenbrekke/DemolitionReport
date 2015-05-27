package org.demolitionreport;

import org.demolitionreport.commands.ExecuteCommand;
import org.demolitionreport.commands.MemoryCommand;
import org.demolitionreport.commands.ThreadCommand;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

public class CommandFactory{


    public static Command make(String commandName,String[] commandDef){
        Command command= MemoryCommand.getMemoryCommand(commandName);
        if(command!=null) return command;
        command= ExecuteCommand.getExecuteCommand(commandName, commandDef);
        if(command!=null) return command;
        command= ThreadCommand.getMemoryCommand(commandName);
        if(command!=null) return command;

        return null;
    };


}
