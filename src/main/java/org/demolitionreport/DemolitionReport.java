package org.demolitionreport;


import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class DemolitionReport {
    public static void main(String[] args){
        try {
        } catch (Exception e ){

        }
    }

    static {
        try {
            File config = new File("demolitionReport.config");
            if (config.canRead()) {
                InputStream fis = new FileInputStream(config);
                InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
                BufferedReader br = new BufferedReader(isr);
                Stream<String> lines=br.lines();
                final LinkedList<String> configList=new LinkedList<String>();
                lines.forEach(new Consumer<String>() {
                    @Override
                    public void accept(String s) {

                        configList.add(replaceVariables(s));
                    }
                });
                br.close();
                isr.close();
                fis.close();
                setUp(configList);

                TerminationSignalHandler.setup();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

//    private static Pattern variablePattern = Pattern.compile("\\$\\{([^\\}]*)\\}");
    private static String replaceVariables(String in){
        Pattern variablePattern = Pattern.compile("\\$\\{([^\\}]*)\\}");
        Matcher matcher=variablePattern.matcher(in);
        String retVal=in;
        while(matcher.find()) {
            String matchedString=matcher.group(1);
            String[] inArray=matchedString.split("\\s+");
            Command command= CommandFactory.make(inArray[0], inArray, matchedString);

            String result=command.execute(Report.nullReport);
            retVal=retVal.replaceAll("\\$\\{"+matchedString+"\\}",result);
        }
        return retVal;
    }

    static void setUp(Queue<String> config){
        while(!config.isEmpty()){
            String[] command=config.remove().trim().split("\\s+");
            if(command.length>0 && command[0].startsWith("Report") ){
                setUpReport(command, config);
            }
        }
    }

    static void setUpReport(String[] reportDef, Queue<String> config){
        List<Command> commands=new ArrayList<Command>();
        while(!config.isEmpty()){
            String commandSource=config.peek();
            String[] commandDef=commandSource.trim().split("\\s+");

            //Stop eating lines if defining new report.
            if(commandDef.length>0 && commandDef[0].startsWith("Report") )
                break;

            // Remove the line and create command
            config.remove();
            if(commandDef.length>0 ){
                try {
                    Command command=CommandFactory.make(commandDef[0],commandDef, commandSource);
                    commands.add(command);
                } catch (Throwable e){
                    // Nonexistent. Skip this.
                }
            }
        }

        Class typeOfReport=Object.class;

        // Default interval is 5 min
        long interval=parseInterval("5m");

        // Default logfile is demolitionReport.log
        String fileName="demolitionReport.log";

        // Default size of logfile is 1m
        long fileSize=parseSize("1m");

        int i=1;
        while(i<reportDef.length){
            String defPart=reportDef[i];
            if("onShutdown".equals(defPart)){
                typeOfReport=ShutdownReportScheduler.class;
            } else if("onStartup".equals(defPart)){
                typeOfReport=StartupScheduler.class;
            } else if("every".equals(defPart)){
                typeOfReport=PeriodicReportScheduler.class;
                if(i+1<reportDef.length){
                    i++;
                    interval=parseInterval(reportDef[i]);
                }
            } else if("file".equals(defPart)){
                if(i+1<reportDef.length){
                    i++;
                    fileName=reportDef[i];
                }
            } else if("size".equals(defPart)){
                if(i+1<reportDef.length){
                    i++;
                    fileSize=parseSize(reportDef[i]);
                }
            }
            i++;
        }

        Report report=new Report(new File(fileName), fileSize, commands);
        if(typeOfReport==ShutdownReportScheduler.class){
            ShutdownReportScheduler scheduler=new ShutdownReportScheduler(report);
            scheduler.init();
        } else if(typeOfReport==PeriodicReportScheduler.class){
            PeriodicReportScheduler scheduler=new PeriodicReportScheduler(interval, report);
            scheduler.init();
        } else if(typeOfReport==StartupScheduler.class){
            StartupScheduler scheduler=new StartupScheduler(report);
            scheduler.init();
        }

    }


    private static long parseInterval(String interval){
        final Pattern _intervalExtractor = Pattern.compile("([0-9]+)(s|m|h)");
        Matcher match=_intervalExtractor.matcher(interval);
        if (match.find()) {
            long number =Integer.parseInt(match.group(1));
            String timeUnit=match.group(2);

            if("s".equals(timeUnit)) return 1000*number;
            if("m".equals(timeUnit)) return 60*1000*number;
            if("h".equals(timeUnit)) return 60*60*1000*number;
        }
        return -1;
    }


    private static long parseSize(String interval){
        final Pattern _sizeExtractor = Pattern.compile("([0-9]*)(k|m|g)");
        Matcher match=_sizeExtractor.matcher(interval);
        if (match.find()) {
            long number =Integer.parseInt(match.group(1));
            String timeUnit=match.group(2);

            if("k".equals(timeUnit)) return 1024*number;
            if("m".equals(timeUnit)) return 1024*1024*number;
            if("g".equals(timeUnit)) return 1024*1024*1024*number;
        }
        return -1;
    }
}
