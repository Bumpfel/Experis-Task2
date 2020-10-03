package controllers;

import java.util.Date;

public class Logger {
  
  private long time;
  private boolean isStarted = false;

  private static String loggerColour = "\u001B[33m";
  private static String resetColour = "\u001B[0m";

  public void start() {
    time = System.currentTimeMillis();
    System.out.print(loggerColour + new Date(time) + ": " + resetColour);
    isStarted = true;
  }
  
  public void stop() {
    if(isStarted) {
      long timeTaken = System.currentTimeMillis() - time;
      System.out.println(loggerColour + "The function executed in " + timeTaken + " ms" + resetColour);
      isStarted = false;
    }
  }
}
