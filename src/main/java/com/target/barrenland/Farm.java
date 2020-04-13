package com.target.barrenland;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * Used to model a farm with a land matrix of size width x length.
 * Land is initialized to land[i][j] = 0 for all i in (0,width-1) and
 * j in (0,length-1).
 *
 * Barren portions can be added to the matrix by calling makeBarren()
 * with the corners of the barren rectangle as arguments. Barren portions
 * are indicated by -1 in the matrix.
 *
 * The key functionality is in getFertileLand(), which returns the
 * mathematical area of every connected fertile land space in the matrix.
 * The function iterates through each square in the graph until it
 * finds a coordinate (i,j) where land[i][j] == 0. When a square's value is
 * 0, this means it is fertile land that has not been searched yet.
 * Beginning at land[i][j], the function uses a flood fill algorithm
 * with DFS to flood all connected fertile area with 1's. Each time
 * it visits a coordinate, it increments a counter. When the DFS
 * completes, the counter equals the total area of the fertile section.
 *
 * When the flood fill is finished, the function increments the flood fill value from 1
 * to 2. It continues iterating through the matrix until it finds another 0,
 * which it flood fills with 2s. Again, the flood fill value is incremented.
 * This continues until each coordinate in the matrix has been visited, in which case all
 * fertile areas have been found. The function sorts the list of fertile areas from least
 * to greatest and returns it.
 *
 * @author Alexis Johnson
 * @version 1.0
 * @since 2020-4-12
 */
public class Farm {
    private int[][] land; // the matrix representing the farm field
    private final int width;
    private final int length;

    // -1 -> barren
    // 0 -> not checked
    // >0 -> land

    /**
     * Construct Farm with the given length and width.
     * Throws an InvalidBoundaryException if the length
     * or width are negative.
     *
     * @param length: Candidate name
     * @param width: Candidate rank within a party
     */
    public Farm(int width, int length) throws InvalidBoundaryException {
        // array representing each coordinate in the farm
        // Java initializes all ints to 0
        if (width <= 0 || length <= 0) {
            throw new InvalidBoundaryException("Length and width must be greater than 0.");
        }
        this.width = width;
        this.length = length;
        land = new int[width][length];
    }

    /**
     * Resets land to all 0's. Used in test cases.
     */
    public void resetLand() {
        land = new int[width][length];
    }

    /**
     * Makes the given rectangle "barren" (all -1's)
     * in the land matrix. The spec requires that
     * one point be the bottom left corner and
     * the other be the top right. However, that is
     * enforced in BarrenLandAnalysis. This function
     * is more flexible and calculates a rectangle
     * given any 2 corners, in any order.
     *
     * The function throws an InvalidBoundaryException
     * if the given corners are out of the bounds
     * of the land matrix.
     *
     * @param a: One corner of the barren land, represented with java.awt.Point
     * @param b: The other corner of the barren land
     */
    public void makeBarren(Point a, Point b) throws InvalidBoundaryException {
        if (outOfBounds(a) || outOfBounds(b)) {
            throw new InvalidBoundaryException("Barren land coordinates must be in land matrix.");
        }
        // change all coordinates in the rectangle to -1
        for (int i=Math.min(a.x,b.x); i<=Math.max(a.x,b.x); i++) {
            for (int j=Math.min(a.y,b.y); j<=Math.max(a.y,b.y); j++) {
                land[i][j] = -1;
            }
        }
    }

    /**
     * Returns a boolean indicating whether the given
     * point lies on the land matrix.
     *
     * @param p: A point with coordinates x and y
     */
    private boolean outOfBounds(Point p) {
        return p.x < 0 || p.y < 0 || p.x >= width || p.y >= length;
    }

    public List<Integer> getFertileLand() {
        List<Integer> areas = new ArrayList<>();
        int nextFill = 1;
        for (int i=0; i<width; i++) {
            for (int j=0; j<length; j++) {
                if (land[i][j] == 0) {
                    areas.add(floodFill(new Point(i,j), nextFill));
                    nextFill++;
                }
            }
        }
        Collections.sort(areas);
        return areas;
    }

    /**
     * Uses a Flood Fill with DFS beginning at start.
     * Searches for each neighboring point that has the same
     * value as start (0). It floods any points that equal 0
     * with the given "fill" value and pushes them onto a stack.
     * It repeats this process with any values popped from
     * the stack until the stack is empty.
     *
     * @param start: The starting point for the flood fill
     * @param fill: The integer that the area will be flooded with
     *
     * @return returns the # of coordinates that were flooded,
     * which corresponds to the area of the fertile region
     */
    private int floodFill(Point start, int fill) {
        // function will flood all neighboring
        // values that also have the value "prev"
        // (in this case, prev will always be 0)
        int prev = land[start.x][start.y];
        // no need to continue searching if
        // the starting value doesn't need to
        // be flooded
        if (prev == fill) {
            return 0;
        }
        int area = 0; // keep track of # of coordinates visited
        land[start.x][start.y] = fill;
        Stack<Point> stack = new Stack<>();
        stack.push(start);
        // DFS
        while(!stack.isEmpty()) {
            Point curr = stack.pop(); // pop next value from stack
            // push all neighbors that have "prev" as their value.
            // immediately change their value to "fill" so they
            // won't be visited again.
            pushNeighbors(stack, curr, fill, prev);
            area++;
        }
        // return area of this fertile land space
        return area;
    }

    /**
     * Helper function that pushes all neighbors that need to
     * be flooded onto the stack. A neighbor is any node directly
     * above, below, left, or right of the current node.
     *
     * A neighbor should be pushed on the stack if it is
     * 1. a set of valid coordinates in the land matrix
     * 2. has a value of prev, indicating it needs to be filled
     *
     * Prior to being pushed on the stack, the neighbor's value
     * is changed to "fill" so it will not be visited again.
     *
     * @param stack: The stack of coordinates used for DFS
     * @param curr: The current point. We need to check this
     *            point's neighbors.
     * @param fill: the number we will flood fill valid neighbors with
     * @param prev: the previous value of this region. A neighbor
     *            must initially have this value to be flood-filled.
     */
    private void pushNeighbors(Stack<Point> stack, Point curr, int fill, int prev) {
        // -x direction
        if (needsFill(curr.x-1, curr.y, prev)) {
            land[curr.x-1][curr.y] = fill;
            stack.push(new Point(curr.x-1,curr.y));
        }
        // +x direction
        if (needsFill(curr.x+1, curr.y, prev)) {
            land[curr.x+1][curr.y] = fill;
            stack.push(new Point(curr.x+1,curr.y));
        }
        // +y direction
        if (needsFill(curr.x, curr.y+1, prev)) {
            land[curr.x][curr.y+1] = fill;
            stack.push(new Point(curr.x,curr.y+1));
        }
        // -y direction
        if (needsFill(curr.x, curr.y-1, prev)) {
            land[curr.x][curr.y-1] = fill;
            stack.push(new Point(curr.x,curr.y-1));
        }
    }

    /**
     *
     * Returns a boolean indicating whether a coordinate
     * needs to be filled.
     *
     * @param x : the x coordinate in the matrix
     * @param y : the y coordinate in the matrix
     * @param prev : the previous value of this region.
     *             land[x][y] must have this value to be filled
     * @return boolean indicator
     */
    private boolean needsFill(int x, int y, int prev) {
        return x >= 0 && y >= 0 && x < width && y < length && land[x][y] == prev;
    }

    // used for testing
    // NOTE: avoid using this for the full sized 400 x 600 matrix. There are
    // too many values to gain any useful insight
    public void print() {
        System.out.printf("%4s", "");
        for (int j=0;j<length; j++) {
            System.out.printf("%4d", j);
        }
        System.out.println();
        for (int i=0; i<width; i++) {
            // use StringBuilder for efficiency
            // since our cols are relatively long
            System.out.printf("%4d", i);
            for (int j=0;j<length; j++) {
                System.out.printf("%4d", land[i][j]);
            }
            System.out.println();
        }
    }

    /**
     * get the width of the matrix - used for testing
     *
     * @return width
     */
    public int getWidth() {
        return width;
    }

    /**
     * get the length of the matrix - used for testing
     *
     * @return length
     */
    public int getLength() {
        return length;
    }

    /**
     * get the land matrix - used for testing
     *
     * @return land matrix
     */
    public int[][] getLand() {
        return land;
    }

    /* UNCOMMENT TO TEST FARM FUNCTIONALITY
    public static void main(String[] args) throws InvalidBoundaryException {
        Farm farm = new com.target.barrenland.Farm(400, 600);
        farm.makeBarren(new Point(48,192), new Point(351,207));
        farm.makeBarren(new Point(120,52), new Point(135,547));
        farm.makeBarren(new Point(48,392), new Point(351,407));
        farm.makeBarren(new Point(260,52), new Point(275,547));
        List<Integer> list = farm.getFertileLand();
        for (int i=0; i<list.size(); i++) {
            System.out.print(list.get(i) + " ");
        }
        System.out.println();
    } */
}
