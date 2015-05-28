package org.demolitionreport;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class Report{
    final File workingFile;
    final long workingSize;
    final List<Command> logCommands;
    private long position =0;

    public Report(File workingFile, long workingSize, List<Command> logCommands){
        this.workingFile=workingFile;
        this.workingSize=workingSize;
        this.logCommands=logCommands;
    }

    //Initialize the file, since the disk might get full
    public void init(){
        workingFile.getParentFile().mkdirs();
        char[] spaceArray=new char[1024];
        for(int i=0;i<spaceArray.length;i++) spaceArray[i]='\n';
        try{
            workingFile.delete();
            PrintWriter writer=new PrintWriter(workingFile);

            long pos=0;
            while(pos<workingSize){
                writer.write(spaceArray);
                pos=pos+1024;
            }
            writer.close();
        } catch (IOException e){
            // Nowhere to log this.
            e.printStackTrace();
        }
    }

    public void run(){
        String logString="";
        for(Command command:logCommands){
            logString=logString+command.execute(this);
        }
        report(logString);
    }

    private final ByteBuffer readBuffer=ByteBuffer.allocate(1024);
    private final String seperator=
            "--------------/\\----------------\n"+
            "-------------/||\\---------------\n"+
            "--------------||----------------\n"+
            "---------- New data ------------\n"+
            "--------------------------------\n"+
            "---------- Old data-------------\n"+
            "--------------||----------------\n"+
            "-------------\\||/---------------\n"+
            "--------------\\/----------------\n";

    public void report(String what){
        String whatWithNewline=(what+"\n");
        int whatLength=whatWithNewline.length();
        byte[] whatBytes=(whatWithNewline+seperator).getBytes();
        ByteBuffer whatBuffer=ByteBuffer.wrap(whatBytes);
        try{
            FileChannel fc = FileChannel.open(workingFile.toPath(),StandardOpenOption.DSYNC,  StandardOpenOption.READ, StandardOpenOption.WRITE);
            
            if(position +whatBytes.length>workingSize){
                position =0;
            }

            fc.position(position);
            fc.write(whatBuffer);
            position = position +whatLength;

            fc.close();

        } catch (IOException e){
            // Nowhere to log this.
        }
    }
}
