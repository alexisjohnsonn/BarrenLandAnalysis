import java.awt.Point;
import java.util.List;
import java.util.Scanner;

public class BarrenLandAnalysis {

    private static final int WIDTH = 400;
    private static final int LENGTH = 600;

    private static final String instructions = "The farm field is " + WIDTH + " x " + LENGTH +
            ". Please enter squares of barren land. " +
            "Squares should be formatted as four integers separated by a space. " +
            "The first two integers should be the coordinates of the bottom left corner in the given rectangle, " +
            "and the last two integers should be the coordinates of the top right corner. Keep in mind " +
            "that valid x values range from 0 to " + (WIDTH-1) + " and valid y values range from 0 " +
            "to " + (LENGTH -1) + ". Once you've entered a barren land square, press ENTER. Continue entering " +
            "rectangles until you've added all barren land. Then, press ENTER to continue.\n";

    private static final String enterBarrenLand = "To add barren land, input 4 integers. Otherwise, press ENTER to see results.";

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
            printAreas(farm.getFertileLand());

        } catch (InvalidBoundaryException e) {
            System.out.println("Hardcoded WIDTH and LENGTH are invalid. Ending program.");
        }
    }

    private static boolean setBarrenLand(Scanner in, Farm farm) {
        System.out.println(enterBarrenLand);
        Point bottom = new Point();
        Point top = new Point();
        if (in.hasNextLine()) {
            String nextline = in.nextLine();
            if (nextline.isEmpty()) {
                return true; // user is done entering barren land
            }
            String[] tokens = nextline.split("\\s+");
            if (tokens.length != 4) {
                System.out.println("Valid input is 4 integers. You entered " + tokens.length + " token(s). Please try again.\n");
                return false;
            }
            try {
                bottom.setLocation(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]));
                top.setLocation(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
            } catch (NumberFormatException e) {
                System.out.println("Valid input is 4 integers. At least one of your inputs was not an integer.\n");
                return false;
            }
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

    private static void printAreas(List<Integer> areas) {
        for (int a : areas) {
            System.out.print(a + " ");
        }
        System.out.println();
    }
}
