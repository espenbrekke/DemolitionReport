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
        char[] spaceArray=new char[1024];
        for(int i=0;i<spaceArray.length;i++) spaceArray[i]=' ';
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
            logString=logString+","+command.execute(this);
        }
        report(logString);
    }

    private final ByteBuffer readBuffer=ByteBuffer.allocate(1024);
    public void report(String what){
        byte[] whatBytes=(what+"\n").getBytes();
        ByteBuffer whatBuffer=ByteBuffer.wrap(whatBytes);
        try{
            FileChannel fc = FileChannel.open(workingFile.toPath(),StandardOpenOption.DSYNC,  StandardOpenOption.READ, StandardOpenOption.WRITE);
            
            if(position +whatBytes.length>workingSize){
                fc.position(position);
                ByteBuffer nullBuffer=ByteBuffer.allocate((int)(workingSize- position));
                fc.write(nullBuffer);
                position =0;
            }
            fc.position(position);
            fc.write(whatBuffer);
            position = position +whatBytes.length;
            
            fc.read(readBuffer);
            String readString=new String(readBuffer.array());
            int nextLine=readString.indexOf('\n');
            if(nextLine>0){
                fc.position(position);
                byte[] spaceArray=new byte[nextLine-1];
                for(int i=0;i<spaceArray.length;i++) spaceArray[i]=32;
                ByteBuffer spaceBuffer=ByteBuffer.wrap(spaceArray);
                fc.write(spaceBuffer);
            }
            fc.close();
        } catch (IOException e){
            // Nowhere to log this.
        }
    }
}
