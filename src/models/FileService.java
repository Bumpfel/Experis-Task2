package models;

import java.io.File;
import java.io.FilenameFilter;
import java.text.DecimalFormat;
import java.util.Scanner;

public class FileService {
  
  public String[] getFiles(String directory, String fileExtension) {
    try {
      File searchDirectory = new File(directory);
      var filter = new ExtensionFilter(fileExtension);

      String[] foundDirectories = searchDirectory.list(filter);

      return foundDirectories;
    } catch(NullPointerException e) {
      return null;
    }
  }

  public boolean fileExists(String fileName) {
    try {
      var file = new File(fileName);
      return file.exists();
    } catch(Exception e) {
      return false;
    }
  }

  public boolean rename(String oldName, String newName) {
    try {
      var oldFile = new File(oldName);
      return oldFile.renameTo(new File(newName));
    } catch(Exception e) {
      return false;
    }
  }
  
  public Long getFileSize(String fileName) {
    try {
      var file = new File(fileName);
      return file.length();
    } catch(Exception e) {
      return null;
    }
  }

  public Integer getNrOfLines(String fileName) {
    try(Scanner scanner = new Scanner(new File(fileName))) {
      int lines = 0;

      while(scanner.hasNextLine()) {
        scanner.nextLine();
        lines ++;
      }
     
      return lines;
    } catch(Exception e) {
      return null;
    }
  }
  
  public boolean doesWordExist(String fileName, String word) {
    return getWordOccurences(fileName, word) > 0;
  }

  public Integer getWordOccurences(String fileName, String searchWord) {
    try(Scanner scanner = new Scanner(new File(fileName))) { 
      int occurrences = 0;
      while(scanner.hasNext()) {
        String word = scanner.next();
        occurrences += word.equalsIgnoreCase(searchWord) ? 1 : 0;
      }
      return occurrences;
    } catch(Exception e) {
      return null;
    }
  }
}  

class ExtensionFilter implements FilenameFilter {
  String extension;

  public ExtensionFilter(String extension) {
    this.extension = extension;
  }

  public boolean accept(File directory, String fileName) {
    return fileName.endsWith(extension);
  }
}