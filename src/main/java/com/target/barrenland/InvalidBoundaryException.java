package com.target.barrenland;

/**
 *
 * InvalidBoundaryException. Indicates when coordinates
 * are outside of the land matrix in the BarrenLandAnalysis
 *
 * @author Alexis Johnson
 * @version 1.0
 * @since 2020-4-12
 */
public class InvalidBoundaryException extends Exception {
    public InvalidBoundaryException(String message) {
        super(message);
    }
}
