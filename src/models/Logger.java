package models;

import java.util.Date;

public class Logger {
  
  private long time;
  private boolean isStarted = false;

  private static String loggerColour = "\u001B[33m";
  private static String resetColour = "\u001B[0m";

  public String start() {
    time = System.currentTimeMillis();
    isStarted = true;
    return loggerColour + new Date(time) + ": " + resetColour;
  }
  
  public String stop() {
    if(isStarted) {
      long timeTaken = System.currentTimeMillis() - time;
      isStarted = false;
      return loggerColour + "The function executed in " + timeTaken + " ms" + resetColour;
    }
    return "A stop() logger call was made, but the logger wasn't started";
  }
}
