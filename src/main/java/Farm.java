import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class Farm {
    private int[][] land;
    private final int width;
    private final int length;

    // -1 -> barren
    // 0 -> not checked
    // >0 -> land

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

    public void resetBarren() {
        land = new int[width][length];
    }

    public void makeBarren(Point a, Point b) throws InvalidBoundaryException {
        if (outOfBounds(a) || outOfBounds(b)) {
            throw new InvalidBoundaryException("Barren land coordinates must be in land matrix.");
        }
        for (int i=Math.min(a.x,b.x); i<=Math.max(a.x,b.x); i++) {
            for (int j=Math.min(a.y,b.y); j<=Math.max(a.y,b.y); j++) {
                land[i][j] = -1;
            }
        }
    }

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

    private int floodFill(Point start, int fill) {
        int prev = land[start.x][start.y];
        if (prev == fill) {
            return 0;
        }
        // DFS
        int area = 0;
        land[start.x][start.y] = fill;
        Stack<Point> stack = new Stack<>();
        stack.push(start);
        while(!stack.isEmpty()) {
            Point curr = stack.pop();
            pushNeighbors(stack, curr, start, fill, prev);
            area++;
        }
        return area;
    }

    private void pushNeighbors(Stack<Point> stack, Point curr, Point start, int fill, int prev) {
        // -x direction
        if (needsFill(curr.x-1, curr.y, start, prev)) {
            land[curr.x-1][curr.y] = fill;
            stack.push(new Point(curr.x-1,curr.y));
        }
        // +x direction
        if (needsFill(curr.x+1, curr.y, start, prev)) {
            land[curr.x+1][curr.y] = fill;
            stack.push(new Point(curr.x+1,curr.y));
        }
        // +y direction
        if (needsFill(curr.x, curr.y+1, start, prev)) {
            land[curr.x][curr.y+1] = fill;
            stack.push(new Point(curr.x,curr.y+1));
        }
        // -y direction
        if (needsFill(curr.x, curr.y-1, start, prev)) {
            land[curr.x][curr.y-1] = fill;
            stack.push(new Point(curr.x,curr.y-1));
        }
    }

    private boolean needsFill(int x, int y, Point start, int prev) {
        return x >= start.x && y >= start.y && x < width && y < length && land[x][y] == prev;
    }

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

    public int getWidth() {
        return width;
    }

    public int getLength() {
        return length;
    }

    public int[][] getLand() {
        return land;
    }

    public static void main(String[] args) throws InvalidBoundaryException {
        Farm farm = new Farm(400, 600);
        farm.makeBarren(new Point(48,192), new Point(351,207));
        farm.makeBarren(new Point(120,52), new Point(135,547));
        farm.makeBarren(new Point(48,392), new Point(351,407));
        farm.makeBarren(new Point(260,52), new Point(275,547));
        List<Integer> list = farm.getFertileLand();
        for (int i=0; i<list.size(); i++) {
            System.out.print(list.get(i) + " ");
        }
        System.out.println();
        //farm.print();
    }
}
