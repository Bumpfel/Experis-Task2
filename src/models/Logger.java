package models;

import java.io.File;
import java.io.FileWriter;
import java.util.Date;

public class Logger {
  
  private long startTime;
  private boolean isStarted = false;
  private FileWriter fileWriter;
  
  public Logger() {
    File logFile = new File("logs/executed-commands.txt");
    try {
      if(!logFile.exists()) {
        logFile.createNewFile();
      }

      fileWriter = new FileWriter(logFile, true);
    } catch(Exception e) {
    }
  }

  public void start() {
    startTime = System.currentTimeMillis();
    isStarted = true;
  }
  
  /**
   * Stops the logger if the logger is started and prints msg to log file
   * @param msg The message that is printed to the log file in addition to the timestamp and execution tim
   * @return time between start() and stop() method calls
   * @throws RuntimeException if an attempt is made to stop the logger when it isn't running. 
   */
  public long stop(String msg) throws RuntimeException {
    if(isStarted) {
      long timestamp = System.currentTimeMillis();
      long timeTaken = timestamp - startTime;
      isStarted = false;
      try {
        fileWriter.append(new Date(timestamp) + ": " + msg + ". The function executed in " + timeTaken + " ms\n");
        fileWriter.flush();
      } catch(Exception e) {
      }
      return timeTaken;
    }
    throw new RuntimeException("A stop() logger call was made, but the logger wasn't started");
  }


}
