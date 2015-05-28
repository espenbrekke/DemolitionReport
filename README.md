# DemolitionReport
DemolitionReport is a java library designed for logging while the jvm is in the process of shutting down.

Usage:
1. Add the DemolitionReport jar to the classpath of the application.
2. Make shure the class org.demolitionreport.DemolitionReport is staticaly initialized. This can be done 
  by executing the main method. In the future I will add more methods of doing this.

3. Create a file named demolitionReport.config in the current dir of the application.
  This files spesifies the logging strategies.
  
4. Start the application.

demolitionReport.config example:
-- Start of file--
Report onStartup file startup.log size 20k
{ "heap.max":"
	memory.heap.max
", "heap.used":"
	memory.heap.used
", "heap.free":"
	memory.heap.free
"}
execute bash runThisOnStartup.sh
Report every 3s file running.log size 20k
{ "heap.max":"
	memory.heap.max
", "heap.used":"
	memory.heap.used
", "heap.free":"
	memory.heap.free
", "native.max":"
	memory.native.max
", "native.used":"
	memory.native.used
", "native.free":"
	memory.native.free
"}
	execute bash runThisRegularly.sh
Report onShutdown file shutdown.log size 20k
{ "heap.max":"
	memory.heap.max
", "heap.used":"
	memory.heap.used
", "heap.free":"
	memory.heap.free
"}
	execute bash runThisOnShutdown.sh
-- End of file --

Logging commands:

execute <command> <parameters>
  Executes the shell command specified. 
  The output of this logging command is written to the report buffer.
  
execute_withStatus <command> <parameters>
  Executes the shell command specified. 
  The output of this logging command is written to the report buffer. 
  The status value of the command is also written to the report buffer
  
memory.heap.[max | used | free]<br>
  Logs how mutch heap memory is used by the jvm. The options are max, used and free.

memory.native.[max | used | free]<br>
  Logs how mutch native memory is used by the jvm. The options are max, used and free. 
  
os.architecture<br>
  Logs the architecture of the operating system.
  
os.name<br>
  Logs the name of the operating system.

os.version<br>
  Logs the version of the operating system.

os.available_processors<br>
  Logs the number of available processors.
  
os.system_load<br>
  Logs the system load.
  
thread.count<br>
  Logs the number of threads.

thread.deamon<br>
  Logs the number of deamon threads.

thread.peak<br>
  Logs the peak number of threads.
  
thread.started<br>
  Logs the number of threads started.
  
runtime.boot_classpath<br>
  Logs the boot classpath.

runtime.classpath<br>
  Logs the classpath.
  
runtime.inputarguments<br>
  Logs the inputarguments.
  
runtime.librarypath<br>
  Logs the librarypath.
  
runtime.managentspeck_version<br>
  Logs the managentspeck_version.

runtime.name<br>
  Logs the name.
  
runtime.spec.vendor<br>
  Logs the spec.vendor.
  
runtime.spec.version<br>
  Logs the spec.version.

runtime.vm.name<br>
  Logs the vm.name.

runtime.vm.version<br>
  Logs the vm.version.
  
runtime.starttime<br>
  Logs the starttime.
  
runtime.systemproperties<br>
  Logs the systemproperties.
  
runtime.uptime<br>
  Logs the uptime.
 
runtime.killcode<br>
  If the process is being killed this vil log the expected exit value.
