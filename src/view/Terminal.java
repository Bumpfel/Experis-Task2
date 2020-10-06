package view;

import java.io.IOException;
import java.text.DecimalFormat;
import models.FileService;
import models.Logger;

public class Terminal {

  private static String resetColour = "\u001B[0m";
  private static String errorColour = "\u001B[31m";
  private static String successColour = "\u001B[32m";
  private static String systemColour = "\u001B[36m";

  private enum MENU_OPTIONS { 
    EXIT("Exit"),
    LIST_FILES("List files"),
    LIST_FILES_BY_EXT("List files by extension"),
    RENAME_FILE("Rename file"),
    GET_FILE_SIZE("Get text file size"),
    GET_LINES("Get nr of lines"),
    SEARCH_WORD("Search for word"),
    COUNT_WORD("Count words"),
    ;

    public String title;

    private MENU_OPTIONS(String title) {
      this.title = title;
    }
  };

  Logger logger = new Logger();
  FileService fileService = new FileService();
  String fileFolder = "assets";
  
  public void run() {
    while(true) {
      MENU_OPTIONS input = null;
      
      while(input == null) {
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        System.out.println("--------------");
        System.out.println("---  Menu  ---");
        System.out.println("--------------");
        for (int i = 1; i < MENU_OPTIONS.values().length; i++) {
          System.out.println(i + ". " + MENU_OPTIONS.values()[i].title);
        }
        System.out.println("0. " + MENU_OPTIONS.values()[0].title);

        input = selectedOption(promptedInput(""));
      }
      System.out.println();

      if(input == MENU_OPTIONS.EXIT) {
        printSystemMsg("Goodbye!");
        System.exit(0);
      
      } else if(input == MENU_OPTIONS.LIST_FILES) {
        printSystemMsg("Listing files");
        String files = listFiles("");
        System.out.println(files);
      
      } else if(input == MENU_OPTIONS.LIST_FILES_BY_EXT) {
        String extensionInput = promptedInput("Enter extension");
        if(extensionInput.isBlank()) {
          continue;
        }
        printSystemMsg("Listing files with extension \"" + extensionInput + "\"");
        String files = listFiles(extensionInput);
        System.out.println(files);

      } else if(input == MENU_OPTIONS.RENAME_FILE) {
        String fileToRename = fileInputPrompt("Enter file you want to rename");
        if(fileToRename == null) {
          continue;
        }
        String newFileName = promptedInput("Enter new file name");
        
        logger.start();
        boolean renameStatus = fileService.rename(fileFolder + "/" + fileToRename, fileFolder + "/" + newFileName);
        String msg = "File \"" + fileToRename + "\"" + (renameStatus ? " were successfully renamed to \"" + newFileName + "\"" : " could not be renamed");
        logger.stop(msg);
        printFeedbackMsg(msg, renameStatus ? false : true);

      } else if(input == MENU_OPTIONS.GET_FILE_SIZE) {
        String fileInput = fileInputPrompt(null);
        if(fileInput == null) {
          continue;
        }
        logger.start();
        long size = fileService.getFileSize(fileFolder + "/" + fileInput);
        String msg = "The size of \"" + fileInput + "\" is " + formatSize(size);
        logger.stop(msg);
        printSystemMsg(msg);
        
      } else if(input == MENU_OPTIONS.GET_LINES) {
        String fileInput = fileInputPrompt(null);
        if(fileInput == null) {
          continue;
        }
        logger.start();
        int lines = fileService.getNrOfLines(fileFolder + "/" + fileInput);
        String msg = lines > 0 ? "The file has " + lines + " lines" : "The file has 0 lines or is not a text file";
        logger.stop(msg);
        printSystemMsg(msg);
        
      } else if(input == MENU_OPTIONS.SEARCH_WORD) {
        String fileInput = fileInputPrompt(null);
        if(fileInput == null) {
          continue;
        }
        String word = promptedInput("Search for a word");
        if(word.isBlank()) {
          continue;
        }
        logger.start();
        String msg = "The word \"" + word + "\" " + (fileService.getWordOccurences(fileFolder + "/" + fileInput, word) > 0 ? "exists" : "does not exist") + " in the file \"" + fileInput + "\"";
        logger.stop(msg);
        printSystemMsg(msg);

      } else if(input == MENU_OPTIONS.COUNT_WORD) {
        Integer lines = 0;
        String fileInput = null;
        fileInput = fileInputPrompt(null);
        if(fileInput == null) {
          continue;
        }
        lines = fileService.getNrOfLines(fileFolder + "/" + fileInput);
        if(lines == 0) {
          printFeedbackMsg("This file is not searchable since it doesn't contain any lines", true);
        } else {
          String word = promptedInput("Search for a word");
          if(word.isBlank()) {
            continue;
          }
          
          logger.start();
          int occurrences = fileService.getWordOccurences(fileFolder + "/" + fileInput, word);
          String msg = "The word \"" + word + "\"" + (occurrences > 0 ? " exists " + occurrences + " times" : " does not exist") + " in the file";
          logger.stop(msg);
          printSystemMsg(msg);
        }
      }

      printSystemMsg("\nPress enter key to continue...");
      try {
        System.in.read();
      } catch(IOException e) {}
    }
  }

  /**
   * Prints msg and prompts for user input
   * Allows detection of empty line breaks (user pressing enter) which can be used to go back in the menu
   */
  private String promptedInput(String msg) {
    System.out.print(msg + ": ");

    try {
      System.in.read(new byte[System.in.available()]); // consume potential leftovers in stdin
      StringBuilder str = new StringBuilder();
      int data;
      do {
        data = System.in.read();
        str.append((char) data);
      } while(data != 10); // if newline, end
      return str.toString().trim();
    } catch(IOException e) {
      return null;
    }
  }

  private MENU_OPTIONS selectedOption(String input) {
    try {
      int option = Integer.parseInt(input);
      if(option >= 0 && option <= MENU_OPTIONS.values().length) {
        return MENU_OPTIONS.values()[option];
      }
    } catch(Exception e) {}
    if(!input.isBlank()) {
      printFeedbackMsg("Erroneous input", true);
    }
    return null; // user did not enter any input
  }

  /**
   * Prompts for input until a valid file name has been given (or user sent empty input)
   * @param customMsg Provide a custom msg if desired, otherwise if null, a default one will be used
   * @return
   */
  private String fileInputPrompt(String customMsg) {
    String input = null;
    do {
      input = promptedInput(customMsg != null ? customMsg : "Enter file name");
      if(input.trim().isEmpty()) {
        return null;
      }
    } while(!validateFileExist(input));
    return input.trim();
  }

  private boolean validateFileExist(String input) {    
    if(!fileService.fileExists(fileFolder + "/" + input)) {
      printFeedbackMsg("The file does not exist", true);
      return false;
    }
    return true;
  }

  private void printFeedbackMsg(String msg, boolean erroneous) {
    System.out.println((erroneous ? errorColour : successColour) + msg + resetColour);
  }

  private void printSystemMsg(String msg) {
    System.out.println(systemColour + msg + resetColour);
  }

  private String listFiles(String extension) {
   StringBuilder fileList = new StringBuilder();
    String[] files = fileService.getFiles(fileFolder, extension);
    if(files == null) {
      printFeedbackMsg("Asset folder does not exist", true);
    }

    for (String file : files) {
      fileList.append(file + "\n");
    }
    return fileList.substring(0, fileList.length() -1);
  }

  private String formatSize(long bytes) {
    var units = new String[]{ "B", "kB", "MB", "GB" };
    int divisions = 0;
    double size = bytes;

    while(size / 1024.0 > 1) {
      size /= 1024.0;
      divisions ++;
    }   

    return new DecimalFormat("#.#").format(size) + " " + units[divisions];
  }
}
