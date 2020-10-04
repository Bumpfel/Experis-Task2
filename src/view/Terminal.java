package view;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Scanner;
import models.FileService;
import models.Logger;

public class Terminal {

  private static String resetColour = "\u001B[0m";
  private static String errorColour = "\u001B[31m";
  private static String successColour = "\u001B[32m";

  Logger logger = new Logger();
  FileService fileService = new FileService();
  String fileFolder = "assets";
  
  public void run() {
    
    try(Scanner scanner = new Scanner(System.in)) {
      while(true) {
        Integer input = null;
        
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        System.out.println("---------------------");
        System.out.println("--  Program Start  --");
        System.out.println("---------------------");
        System.out.println("1. List files");
        System.out.println("2. List files by extension");
        System.out.println("3. Rename file");
        System.out.println("4. Get text file size");
        System.out.println("5. Get nr of lines");
        System.out.println("6. Search for word");
        System.out.println("7. Count words");
        System.out.println("0. Exit");
        
        while(input == null) {
          System.out.print(": ");
          input = selectedOption(scanner.next(), 7);
        }
        
        System.out.println();

        if(input == 0) {
          // Exit app
          System.out.println("Goodbye!");
          System.exit(0);
       
        } else if(input == 1) {
          // List all files
          listFiles("");
          pressToContinue();
        
        } else if(input == 2) {
          // List specific files
          listFiles(promptedInput("Enter extension", false));
          pressToContinue();

        } else if(input == 3) {
          // Rename file
          String fileToRename = null;
          while(fileToRename == null) {
            System.out.print("Enter file you want to rename: ");
            fileToRename = validateFileExist(scanner.next());
          }
          
          System.out.print("Enter new file name: ");
          String stringInput2 = scanner.next();
          
          logger.start();
          boolean renameStatus = fileService.rename(fileFolder + "/" + fileToRename, fileFolder + "/" + stringInput2);
          printMsg("File " + (renameStatus ? "successfully renamed" : "rename failed"), renameStatus ? false : true);
          logger.stop();
          pressToContinue();

        } else if(input == 4) {
          // Get file size          
          String file = null;
          while(file == null) {
            System.out.print("Enter file name: ");
            file = validateFileExist(scanner.next());
          }
          System.out.println(formatSize(fileService.getFileSize(fileFolder + "/" + file)));
          pressToContinue();
          
        } else if(input == 5) {
          // Get lines
          String file = null;
          while(file == null) {
            System.out.print("Enter file name: ");
            file = validateFileExist(scanner.next());
          }
          System.out.println(fileService.getNrOfLines(file));
          pressToContinue();
          
        } else if(input == 6) {
          // Search for word
          String file = null;
          while(file == null) {
            System.out.print("Enter file name: ");
            file = validateFileExist(scanner.next());
          }
          System.out.print("Search for a word: ");
          String word = scanner.next();
          System.out.println("The word " + word + " " + (fileService.getWordOccurences(fileFolder + "/" + file, word) > 0 ? "exists" : "does not exist") + " in the file");
          pressToContinue();
          
        } else if(input == 7) {
          // Count words
          String file = null;
          while(file == null) {
            System.out.print("Enter file name: ");
            file = validateFileExist(scanner.next());
          }
          System.out.print("Search for a word: ");
          String word = scanner.next();
          int occurrences = fileService.getWordOccurences(fileFolder + "/" + file, word);
          System.out.println("The word " + word + (occurrences > 0 ? " exists " + occurrences + " times" : " does not exist") + " in the file");
          pressToContinue();
          
        }
      }
    }
  }

  private String promptedInput(String msg, boolean validateFile) {
     try(Scanner scanner = new Scanner(System.in)) {
       String input = null;
        while(input == null) {
          System.out.print(msg + ": ");
          input = scanner.next();
          if(validateFile) {
            input = validateFileExist(input);
          }
          if(input != null) {
            return input;
          }
        }
     }
     return null;
  }

  private String validateFileExist(String input) {
    if(!fileService.fileExists(fileFolder + "/" + input)) {
      printMsg("The file does not exist", true);
      return null;
    }
    return input;
  }
    
  private void printMsg(String msg, boolean erroneous) {
    System.out.println((erroneous ? errorColour : successColour) + msg + resetColour);
  }

  
  private void pressToContinue() {
    System.out.print("\nPress enter key to continue...");
    try {
      System.in.read();
    } catch(IOException e) {
    }
  }

  private Integer selectedOption(String input, int lastOption) {
    try {
      int option = Integer.parseInt(input);
      if(option >= 0 && option <= lastOption) {
        return option;
      }
    } catch(Exception e) {}
    printMsg("Erroneous input", true);
    return null;
  }

  private void listFiles(String extension) {
    logger.start();
    System.out.println("Listing files" + (extension.length() > 0 ? " with extension \"" + extension + "\"" : ""));
    
    String[] files = fileService.getFiles(fileFolder, extension);
    for (String file : files) {
      System.out.println(file);
    }
    logger.stop();
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
