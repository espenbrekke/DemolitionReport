package org.demolitionreport;

import org.demolitionreport.commands.*;

public class CommandFactory{
    public static Command make(String commandName,String[] commandDef, String def){
        if(commandName.startsWith("//")) return null;

        Command command= MemoryCommand.getMemoryCommand(commandName);
        if(command!=null) return command;

        command= ExecuteCommand.getExecuteCommand(commandName, commandDef);
        if(command!=null) return command;

        command= ThreadCommand.getThreadCommand(commandName);
        if(command!=null) return command;

        command= OsCommand.getOsCommand(commandName);
        if(command!=null) return command;

        command= RuntimeCommand.getRuntimeCommand(commandName);
        if(command!=null) return command;

        command= StringCommand.getStringCommand(def);
        if(command!=null) return command;

        return null;
    };
}
