package com.target.barrenland;

import java.awt.Point;
import java.util.List;
import java.util.Scanner;

/**
 *
 * BarrenLandAnalysis. Main functionality for the command line program.
 * For logic, see Farm.java first.
 *
 * @author Alexis Johnson
 * @version 1.0
 * @since 2020-4-12
 */
public class BarrenLandAnalysis {

    // hard code these, feel free to change
    // and recompile if desired
    private static final int WIDTH = 400;
    private static final int LENGTH = 600;

    private static final String instructions = "The farm field is " + WIDTH + " x " + LENGTH +
            ". Please enter squares of barren land.\n" +
            "Squares should be formatted as four integers separated by a space. " +
            "The first two integers should be the coordinates of the bottom left corner in the given rectangle,\n" +
            "and the last two integers should be the coordinates of the top right corner. Keep in mind " +
            "that valid x values range from 0 to " + (WIDTH-1) + " and valid y values range from 0\n" +
            "to " + (LENGTH -1) + ". Once you've entered a barren land square, press ENTER. Continue entering " +
            "rectangles until you've added all barren land. Then, press ENTER to continue.\n";

    private static final String enterBarrenLand = "To add barren land, input 4 integers. Otherwise, press ENTER to see results.";

    /**
     * Main function. Continually asks for barren land input. Validates each input and
     * lets the user know if the input was added to the land matrix successfully.
     * When the user presses ENTER with no input, it runs a fertile land analysis,
     * printing a list of fertile areas in ascending order to the terminal.
     *
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("WELCOME TO THE BARREN LAND ANALYSIS");
        System.out.println(instructions);
        try {
            Farm farm = new Farm(WIDTH, LENGTH);
            Scanner in = new Scanner(System.in);
            boolean inputReceived = false;
            while (!inputReceived) {
                inputReceived = setBarrenLand(in, farm);
            }
            System.out.println("Calculating . . . \n");
            printAreas(farm.getFertileLand());

        } catch (InvalidBoundaryException e) {
            System.out.println("Hardcoded WIDTH and LENGTH are invalid. Ending program.");
        }
    }

    /**
     * Get one set of barren land coordinates from the user.
     * Validate input for
     * 1. 4 inputs
     * 2. all integers
     * 3. integers are IN matrix
     * 4. integers represent the bottom left and top right corner of a rectangle
     *
     * @param in Scanner for STDIN
     * @param farm the current farm beind edited
     * @return boolean indicating whether the user is
     * done entering input
     */
    private static boolean setBarrenLand(Scanner in, Farm farm) {
        System.out.println(enterBarrenLand);
        Point bottom = new Point();
        Point top = new Point();
        if (in.hasNextLine()) {
            String nextline = in.nextLine().trim();
            if (nextline.isEmpty()) {
                return true; // user is done entering barren land
            }
            String[] tokens = nextline.split("\\s+");
            // need 4 input values
            if (tokens.length != 4) {
                System.out.println("Valid input is 4 integers. You entered " + tokens.length + " token(s). Please try again.\n");
                return false;
            }
            // all inputs must be integers
            try {
                bottom.setLocation(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]));
                top.setLocation(Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]));
            } catch (NumberFormatException e) {
                System.out.println("At least one of your inputs was not an integer. Valid input is 4 integers.\n");
                return false;
            }
            // integers must represent the bottom left and top right corner of a rectangle
            if (bottom.x > top.x || bottom.y > top.y) {
                System.out.println("Your bottom corner is either above or to the right of your top corner.\n" +
                        "The first 2 coordinates should represent the bottom left corner. The second 2 coordinates should" +
                        " represent the bottom right corner.\n");
                return false;
            }
            try {
                farm.makeBarren(bottom, top);
                System.out.println("Barren land was successfully added to the farm field.\n");
            } catch (InvalidBoundaryException e) {
                System.out.println("Your coordinates did not fall within the boundary of the farm field. X coordinates should" +
                        " be from 0 to " + (WIDTH-1) + ". Y coordinates should be from 0 to " + (LENGTH-1) + ".\n");
            }
            return false;
        }
        return true; // if no input, we are done setting the barren land
    }

    /**
     * Print the result of the program to STDOUT
     *
     * @param areas A list of fertile land areas
     */
    private static void printAreas(List<Integer> areas) {
        for (int a : areas) {
            System.out.print(a + " ");
        }
        System.out.println();
    }
}
