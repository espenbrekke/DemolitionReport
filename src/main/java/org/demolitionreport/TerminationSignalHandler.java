package org.demolitionreport;

import sun.misc.Signal;
import sun.misc.SignalHandler;
import java.lang.reflect.*;
import java.util.Map;
import java.util.Set;

/**
 * Created by espen on 27.05.15.
 */
public class TerminationSignalHandler implements SignalHandler {

    private static SignalHandler other=null;
    private static Signal signal=null;

    public static void setup(){
        try {
            Class<?> signalClass=TerminationSignalHandler.class.getClassLoader().loadClass("sun.misc.Signal");
            Field signalHandlers=signalClass.getDeclaredField("handlers");
            signalHandlers.setAccessible(true);
            Map<Signal, SignalHandler> currentValue=(Map<Signal, SignalHandler>)signalHandlers.get(null);

            Set<java.util.Map.Entry<Signal, SignalHandler>> handlerEntries= currentValue.entrySet();

            for (java.util.Map.Entry<Signal, SignalHandler> entry:handlerEntries){
                SignalHandler other=entry.getValue();
                if(other.getClass().getName().contains("Terminator") ){
                    currentValue.put(entry.getKey(), new TerminationSignalHandler(other));
                }
            }

            int a=0;
        } catch (Throwable e){
            e.printStackTrace();
        }
    }

    TerminationSignalHandler(SignalHandler other){
        this.other=other;
    }

    public static int getExitCode(){
        if(signal==null) return 0;
        return (signal.getNumber() + 0200);
    }

    public void handle(Signal sig) {
        signal=sig;
        if(other!=null) other.handle(sig);
    }
}
