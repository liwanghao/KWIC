// -*- Java -*-
/*
 * <copyright>
 *
 *  Copyright (c) 2002
 *  Institute for Information Processing and Computer Supported New Media (IICM),
 *  Graz University of Technology, Austria.
 *
 * </copyright>
 *
 * <file>
 *
 *  Name:    KWIC.java
 *
 *  Purpose: The Master Control class
 *
 *  Created: 05 Nov 2002
 *
 *  $Id$
 *
 *  Description:
 *    The Master Control class
 * </file>
*/

package kwic.es;

import java.io.IOException;
import java.util.HashMap;

/*
 * $Log$
*/

/**
 *  The KWIC system implemented by means of event-based architecture consists of the
 *  following modules:
 *  <ul>
 *  <li>Master Control module, which is responsible for controling all other modules
 *  in the system
 *  <li>Input module, which is responsible for reading the input file, parsing it
 *  and adding lines to Original Line Storage module
 *  <li>Original Line Storage module, which stores original lines
 *  <li>Circular Shifter module, which produces all circular shifts from original lines
 *  <li>Shifted Line Storage module, which stores circular shifts
 *  <li>Alphabetizer module, which sorts shifted lines
 *  <li>Output module, which prints out the sorted shifts in a nice format.
 *  </ul>
 *  The system works as follows. Input module reads the input file, parses it
 *  and adds lines to Original Line Storage module. Any change (e.g. a new line
 *  was added) to Original Line Storage module causes an event (message) to be
 *  sent to all modules which declare interest in tracking these changes. In
 *  the current system, Circular Shifter module declares such interest. Thus,
 *  upon receiving a notification event stating that the new line was added to
 *  Original Line Storage module, Circular Shifter module processes the line and
 *  creates circular shifts for that line. The newly created circular shifts are
 *  stored in Shifted Line Storage module. Similarly to changes in Original Line
 *  Storage module, any change (e.g. a new circular shift was added) to Shifted
 *  Line Storage module causes an event (message) to be sent to all modules which
 *  declare interest in tracking these changes. Obviously, in the current system
 *  Alphabetizer module declares such interest. Thus, upon receiving a notification
 *  event stating that the new circular shift was added to Shifted Line Storage module,
 *  Alphabetizer module sorts this circular shift. The sorted shifts are kept in
 *  Shifted Line Storage as well. Once when Input module has finished with parsing
 *  procedure, which implies that all circular shifts have been created and sorted
 *  (because of notification mechanism) Outpur module prints out the sorted shifts.
 *  In the current implementation an object of the KWIC class creates and initializes
 *  all other objects from the event-based KWIC implementation to achieve the desired
 *  functionality. Thus, KWIC instance creates the following instances of other classes:
 *  <ul>
 *  <li>An instance of LineStorageWrapper class that holds the original lines
 *  <li>An instance of LineStorageWrapper class that holds the circular shifts
 *  <li>An instance of Input class which parses the input file and adds lines
 *  to the line storage for original lines
 *  <li>An instance of CircularShift class which declares its interest in
 *  changes to the line storage for original lines. Thus, whenever it receives a
 *  message that a new line has been added it produces circular shifts for that line
 *  and stores them into the line storage for circular shifts
 *  <li>An instance of Alphabetizer class which declares its interest in
 *  changes to the line storage for circular shifts. Thus, whenever it receives a
 *  message that a new circular shift has been added it sorts the line storage for
 *  circular shifts
 *  <li>An instance of Output class which prints all shifts from the line
 *  storage for circular shifts.
 *  Further, the KWIC class provides also the main method which checks the command line
 *  arguments.
 *  @author  dhelic
 *  @version $Id$
*/

public class KWIC{

//----------------------------------------------------------------------
/**
 * Fields
 *
 */


//----------------------------------------------------------------------

//----------------------------------------------------------------------
/**
 * Constructors
 *
 */
//----------------------------------------------------------------------

//----------------------------------------------------------------------
/**
 * Methods
 *
 */
//----------------------------------------------------------------------

//----------------------------------------------------------------------
/**
 * Parses the data, makes shifts and sorts them. At the end prints the
 * sorted shifts.
 * @param file name of the input file
 * @return void
 */

  public void execute(String file){

        // initialize all variables

        // storage for original lines
    LineStorageWrapper lines = new LineStorageWrapper();

        // storage for circular shifts
    LineStorageWrapper shifts = new LineStorageWrapper();

        // input reader
    Input input = new Input();

        // circular shifter
    CircularShifter shifter = new CircularShifter(shifts);
        // declare interest in tracking changes
    lines.addObserver(shifter);

        // alphabetizer
    Alphabetizer alphabetizer = new Alphabetizer();
        // declare interest in tracking changes
    shifts.addObserver(alphabetizer);

        // line printer
    Output output = new Output();

        // read and parse the input file
    input.parse(file, lines);

        // print sorted shifts
    output.print(shifts);
  }

//----------------------------------------------------------------------
/**
 * Main function checks the command line arguments. The program expects
 * exactly one command line argument specifying the name of the file
 * that contains the data. If the program has not been started with
 * proper command line arguments, main function exits
 * with an error message. Otherwise, a KWIC instance is created and program
 * control is passed to it.
 * @param args command line arguments
 * @return void
 */

  public static void main(String[] args){

    KWIC kwic = new KWIC();
    switch (args.length) {
        case 0:
            kwic.execute();
            break;
        case 1:
            kwic.execute(args[0]);
            break;
        default:
            System.err.println("Error args.");
            System.exit(1);

    }
  }

private void execute() {


        // initialize all variables

        HashMap<String, Integer> indexes = new HashMap<>();
        // storage for original lines
        LineStorageWrapper lines = new LineStorageWrapper();

        // storage for circular shifts
        LineStorageWrapper shifts = new LineStorageWrapper();

        // input reader
        Input input = new Input();

        // circular shifter
        CircularShifter shifter = new CircularShifter(shifts);

        // alphabetizer
        Alphabetizer alphabetizer = new Alphabetizer();
        // line printer
        Output output = new Output();

        WordIndexer wordindexer = new WordIndexer(indexes);

        // declare interest in tracking changes
        lines.addObserver(shifter);
        // declare interest in tracking changes
        shifts.addObserver(alphabetizer);

        lines.addObserver(wordindexer);

    while (true) {
        System.out.print("Add, Delete, Print, Index, Quit:");
        try {
            String line = input.readLine();
            switch (line) {
                case "a":
                    line = input.readLine();
                    String[] words=line.split(" ");
                    lines.addLine(words);
                    continue;
                case "d":
                    line = input.readLine();
                    int line_index=getIndexOfLine(line,lines);
                    if(line_index==-1){
                        System.out.println("error");
                    }else{
                        lines.deleteLine(line_index);
                    }
                    continue;
                case "p":
                    // print sorted shifts
                    output.print(shifts);
                    continue;
                case "i":
                    output.print(indexes);
                    continue;
                case "q":
                    System.exit(0);
                    break;
                default:
                    System.out.println("Wrong input, please try again.");
                    continue;

            }
        } catch (IOException e) {
            System.err.println("KWIC Error: Could not read a line.");
            e.printStackTrace();
        }
    }
}

/**
 * @param line 要查找的行
 * @param lines 行存储
 * @return 该行所在的index
 */
private int getIndexOfLine(String line, LineStorageWrapper lines) {
    int index=-1;
    String current_line;
    for(int i=0;i<lines.getLineCount();i++){
        current_line=lines.getLineAsString(i);
        if(current_line.equals(line)){
            index=i;
            break;
        }

    }
    return index;
}

//----------------------------------------------------------------------
/**
 * Inner classes
 *
 */
//----------------------------------------------------------------------

}
