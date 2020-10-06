# Experis-Task2
Task description can be found in Instructions.pdf

## Program Description
This is a terminal program that handles files (in the assets folder). The commands used are logged to logs/executed-commands.txt along with timestamp and command execution time.

## Program usage

The main menu presents the options available. The options are selected by inputting the number associated with the command<br />
Some commands requests additional user input (like which file to manipulate). The user can at all times go back to the main menu by providing an empty input (by just pressing the enter key). If the input is a file input, the application will provide new input until a file matching the user input is found.<br />
![menu](https://github.com/Bumpfel/Experis-Task2/blob/main/menu.png?raw=true)

- **List files:** Lists all the files in the assets folder
- **List files by extension:** Requests a second user input. List all the files in the folder that ends with the second user input.
- **Rename file:** Request a second user input for which file to rename (requests new input if the file is not found). Subsequently, the program requests a second user input for the new file name. The function will fail if the new file name already exists.
- **Get file size:** Requests user input for file. Returns file size
- **Get nr of lines:** Requests user input for file. If file is a text file, the number of lines (line breaks) is printed.
- **Search for word:** Requests user input for file and search word. The search function only works for single words.
- **Count words:** Does the same thing as the previous function, but displays a count of the words.
- **Exit:** Terminates the program.

## Compile/run program
The first line shows how to compile the project using the java compile command. The second shows how to run it.
![compile/run](https://github.com/Bumpfel/Experis-Task2/blob/main/compile.png?raw=true)

The first line shows how to package the compiled class files into a jar, and the second how to run the program from the jar file.
![compile/run](https://github.com/Bumpfel/Experis-Task2/blob/main/package-jar.png?raw=true)
