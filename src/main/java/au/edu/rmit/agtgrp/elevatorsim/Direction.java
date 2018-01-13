package au.edu.rmit.agtgrp.elevatorsim;

/**
 * Enum representing a direction of elevator travel.
 * @author Joshua Beale
 * @author Sebastian Sardina
 */
public enum Direction {
    // Elevators can travel upwards/downwards
	UP("up"), DOWN("down"), UNKNOWN("unknown");

    // String representation of the chosen direction
	public final String name;

	/**
	 * Constructor.
	 * @param name Direction string
	 */
	Direction(String name) {
		this.name = name;
	}

	/**
	 * Get a direction instance from the specified direction
	 * string.
	 * @param name Direction string representation
	 * @return Direction instance
	 */
	public static Direction getDirection(String name) {
		switch (name) {
	        case "down": 
	        	return DOWN;
	        case "up":
				return UP;
	        default:
	        	return UNKNOWN;
		}
	}

	/**
	 * Flip current direction (up to down, down to up)
	 * @return Switched direction
	 */
	public Direction switchDirection() {
		switch (this) {
        case UP: 
        	return DOWN;
        case DOWN:
			return UP;
        default:
        	return UNKNOWN;
		}
	}
}
