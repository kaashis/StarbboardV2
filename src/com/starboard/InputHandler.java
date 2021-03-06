package com.starboard;

import com.starboard.util.*;

import java.io.Console;
import java.util.Scanner;

class InputHandler {

    public static String[] input(Room room) {
        //check scanner from file
        Scanner sc = new Scanner(System.in);
        Parser parser = new Parser(room);

        do {
            ConsoleColors.changeTo(ConsoleColors.BLACK_BOLD);
            System.out.print(ConsoleColors.WHITE_BACKGROUND  + "Please type in your command.\n> "+ ConsoleColors.RESET);
            ConsoleColors.reset();
            String input = sc.nextLine();
            if (input.equals("")) {
                Prompt.showCommands();
            } else {
                parser.parse(input);
            }
        } while (!parser.getParseStatus());

        String[] command = new String[2];
        command[0] = parser.getFirstCommand();
        command[1] = parser.getSecondCommand();

        return command;
    }
}