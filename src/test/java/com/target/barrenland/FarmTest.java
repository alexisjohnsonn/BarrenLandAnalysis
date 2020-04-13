package com.target.barrenland;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class FarmTest {

    Farm smallFarm;
    Farm bigFarm;
    Farm squareFarm;

    @Before
    public void setup() throws InvalidBoundaryException {
        // small Farm for quicker iterating
        smallFarm = new Farm(15, 10);
        // big Farm to represent size used in BarrenLandAnalysis
        bigFarm = new Farm(400, 600);
        // one extra Farm for testing
        squareFarm = new Farm(30,30);
    }

    @Test
    public void ConstructorTest() {
        // assert that length and width are properly set
        assert(smallFarm.getLength() == 10);
        assert(smallFarm.getWidth() == 15);
        // assert that the land matrix is initialized to 0
        for (int i=0; i<smallFarm.getWidth(); i++) {
            for (int j=0; j<smallFarm.getLength(); j++) {
                assert(smallFarm.getLand()[i][j] == 0);
            }
        }
        assert(bigFarm.getWidth() == 400);
        assert(bigFarm.getLength() == 600);
    }

    // reference: https://www.baeldung.com/junit-assert-exception.
    /* Ensure the constructor throws an InvalidBoundaryException when an invalid
     length or width is given as an argument.
     NOTE: for now, we allow a Farm to be as large as Integer.MAX_VALUE. */
    @Test
    public void ConstructorInvalidBoundariesTest() {
        // length cannot be 0
        Exception exception1 = assertThrows(InvalidBoundaryException.class, () -> new Farm(20, 0));

        String expectedMessage = "Length and width must be greater than 0.";
        String actualMessage = exception1.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

        // width cannot be negative
        Exception exception2 = assertThrows(InvalidBoundaryException.class, () -> {
            new Farm(-5, 15);
        });

        actualMessage = exception2.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /*
       makeBarren() throws an InvalidBoundaryException
       if the input falls outside the "land" matrix.
     */
    @Test
    public void MakeBarrenInvalidBoundariesTest() {
        // x value < 0
        Exception exception1 = assertThrows(InvalidBoundaryException.class, () -> {
            smallFarm.makeBarren(new Point(-1,3), new Point(4,5));
        });

        String expectedMessage = "Barren land coordinates must be in land matrix.";
        String actualMessage = exception1.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

        // y value == length (will get OutOfBoundsException when indexing into matrix)
        Exception exception2 = assertThrows(InvalidBoundaryException.class, () -> {
            smallFarm.makeBarren(new Point(0,3), new Point(4,10));
        });

        actualMessage = exception2.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    // test 1 successful call to makeBarren()
    @Test
    public void MakeBarrenTest() throws InvalidBoundaryException {
        // make entire matrix barren
        smallFarm.makeBarren(new Point(0,0), new Point(14,9));
        // entire matrix should be -1
        for (int i=0; i<smallFarm.getWidth(); i++) {
            for (int j=0; j<smallFarm.getLength(); j++) {
                assert(smallFarm.getLand()[i][j] == -1);
            }
        }
        // single point barren
        bigFarm.makeBarren(new Point(1,1), new Point(1,1));
        // (1,1) should be barren
        assert(bigFarm.getLand()[1][1] == -1);
        // all remaining points should be fertile
        for (int i=0; i<smallFarm.getWidth(); i++) {
            for (int j=0; j<smallFarm.getLength(); j++) {
                if (j!=1 || i!= 1) {
                    assert (smallFarm.getLand()[i][j] == -1);
                }
            }
        }
        // large area (3 <= x <= 8 and 4 <= y <= 29)
        squareFarm.makeBarren(new Point(3,4), new Point(8,29));
        for (int i=0; i<squareFarm.getWidth(); i++) {
            for (int j=0; j<squareFarm.getLength(); j++) {
                // if point is in barren area, it should be -1
                if (3<=i && i <=8 && j>=4) {
                    assert (squareFarm.getLand()[i][j] == -1);
                } else {
                    assert (squareFarm.getLand()[i][j] == 0);
                }
            }
        }
    }

    // test multiple calls to makeBarren() on the same Farm
    @Test
    public void MakeBarrenMultipleCallsTest() throws InvalidBoundaryException {
        // area 1
        squareFarm.makeBarren(new Point(3,4), new Point(8,29));
        // area 2 (overlaps area 1)
        squareFarm.makeBarren(new Point(1,25), new Point(16,16));
        // area 3 (no overlap
        squareFarm.makeBarren(new Point(28,2), new Point(20,7));
        for (int i=0; i<squareFarm.getWidth(); i++) {
            for (int j=0; j<squareFarm.getLength(); j++) {
                // separating these for clarity
                boolean barrenArea1 = 3<=i && i <=8 && j>=4;
                boolean barrenArea2 = 1<=i && i <=16 && 16<=j && j<= 25;
                boolean barrenArea3 = 20<= i && i <= 28 && 2<=j && j<= 7;
                // if the point is in a barren area, it should be -1
                if (barrenArea1 || barrenArea2 || barrenArea3) {
                    assert (squareFarm.getLand()[i][j] == -1);
                } else {
                    assert (squareFarm.getLand()[i][j] == 0);
                }
            }
        }
    }

    // test GetFertileLand() when no parts of the matrix are barren
    // entire matrix should be fertile before calls to makeBarren()
    @Test
    public void GetFertileLandNoBarrenTest() {
        // fertile area is length * width
        assertEquals(smallFarm.getFertileLand(), Arrays.asList(smallFarm.getLength() * smallFarm.getWidth()));
        // land should be all 1's
        for (int i=0; i<smallFarm.getWidth(); i++) {
            for (int j=0; j<smallFarm.getLength(); j++) {
                assert(smallFarm.getLand()[i][j] == 1);
            }
        }
        // fertile area is length * width
        assertEquals(bigFarm.getFertileLand(), Arrays.asList(bigFarm.getLength() * bigFarm.getWidth()));
        // land should be all 1's
        for (int i=0; i<bigFarm.getWidth(); i++) {
            for (int j=0; j<bigFarm.getLength(); j++) {
                assert(bigFarm.getLand()[i][j] == 1);
            }
        }
    }

    // test GetFertileLand() when there is just 1 fertile area (and the
    // remaining area is barren)
    @Test
    public void GetFertileLandOneFertileAreaTest() throws InvalidBoundaryException {
        // area 1
        squareFarm.makeBarren(new Point(3,4), new Point(8,29));
        // area 2 (overlaps area 1)
        squareFarm.makeBarren(new Point(1,25), new Point(16,16));
        // area 3 (no overlap)
        squareFarm.makeBarren(new Point(28,2), new Point(20,7));

        // Fertile area = 590 (only 1 fertile area)
        assertEquals(squareFarm.getFertileLand(), Arrays.asList(590));
        // Fertile points should be 1. Barren should be -1
        for (int i=0; i<squareFarm.getWidth(); i++) {
            for (int j = 0; j < squareFarm.getLength(); j++) {
                // separating these for clarity
                boolean barrenArea1 = 3 <= i && i <= 8 && j >= 4;
                boolean barrenArea2 = 1 <= i && i <= 16 && 16 <= j && j <= 25;
                boolean barrenArea3 = 20 <= i && i <= 28 && 2 <= j && j <= 7;
                if (barrenArea1 || barrenArea2 || barrenArea3) {
                    assert (squareFarm.getLand()[i][j] == -1);
                } else {
                    assert (squareFarm.getLand()[i][j] == 1);
                }
            }
        }
    }

    // test GetFertileLand() when there are 3 separate fertile areas
    @Test
    public void GetFertileLandMultipleFertileAreasTest() throws InvalidBoundaryException {
        // barren area 1
        squareFarm.makeBarren(new Point(3,4), new Point(8,29));
        // barren area 2
        squareFarm.makeBarren(new Point(0,25), new Point(16,16));
        // barren area 3
        squareFarm.makeBarren(new Point(28,2), new Point(20,7));
        // barren area 4
        squareFarm.makeBarren(new Point(29,6), new Point(7,10));

        // three fertile areas, sorted in ascending order
        assertEquals(squareFarm.getFertileLand(), Arrays.asList(12, 162, 319));

        // DFS flood fill algorithm gives each fertile area its own number, iterating up from 1
        // It begins searching at (0,0), going left to right. It searches rows top to bottom.
        for (int i=0; i<squareFarm.getWidth(); i++) {
            for (int j = 0; j < squareFarm.getLength(); j++) {
                // separating this logic for clarity
                boolean barrenArea1 = 3 <= i && i <= 8 && j >= 4;
                boolean barrenArea2 = i <= 16 && 16 <= j && j <= 25;
                boolean barrenArea3 = 20 <= i && i <= 28 && 2 <= j && j <= 7;
                boolean barrenArea4 = 7 <= i && i <= 29 && 6 <= j && j <= 10;
                // if barren -> -1
                if (barrenArea1 || barrenArea2 || barrenArea3 || barrenArea4) {
                    assert (squareFarm.getLand()[i][j] == -1);
                } else {
                    // fertile area 1
                    if (j<=10 || (j<=15 && i<=2)) {
                        assert (squareFarm.getLand()[i][j] == 1);
                    }
                    // fertile area 2
                    else if (j>=26 && i<=2) {
                        assert (squareFarm.getLand()[i][j] == 2);
                    }
                    // fertile area 3
                    else {
                        assert (squareFarm.getLand()[i][j] == 3);
                    }
                }
            }
        }
    }

    // GetFertileLand() should return an empty list if all the land
    // is barren.
    @Test
    public void GetFertileLandAllBarrenTest() throws InvalidBoundaryException {
        smallFarm.makeBarren(new Point(0,0), new Point(14,9));
        assertEquals(smallFarm.getFertileLand(), new ArrayList<>());
    }

    // These are the provided tests from the prompt.
    @Test
    public void ProvidedCaseTest() throws InvalidBoundaryException {
        // provided test 1
        // {“0 292 399 307”}
        // 116800 116800
        bigFarm.makeBarren(new Point(0,292), new Point(399, 307));
        assertEquals(bigFarm.getFertileLand(), Arrays.asList(116800, 116800));
        bigFarm.resetLand();
        // provided test 2
        // {“48 192 351 207”, “48 392 351 407”, “120 52 135 547”, “260 52 275 547”}
        // result = 22816 192608
        bigFarm.makeBarren(new Point(48,192), new Point(351, 207));
        bigFarm.makeBarren(new Point(48,392), new Point(351, 407));
        bigFarm.makeBarren(new Point(120,52), new Point(135, 547));
        bigFarm.makeBarren(new Point(260,52), new Point(275, 547));
        assertEquals(bigFarm.getFertileLand(), Arrays.asList(22816, 192608));
    }
}
