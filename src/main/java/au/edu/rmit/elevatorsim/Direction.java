package au.edu.rmit.elevatorsim;

/**
 * Enum representing a direction of elevator travel.
 * @author Joshua Beale
 */
public enum Direction {
    // Elevators can travel upwards/downwards
	UP("up"), DOWN("down");

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
		if (name.equals("down")) {
			return DOWN;
		} else {
			return UP;
		}
	}

	/**
	 * Flip current direction (up to down, down to up)
	 * @return Switched direction
	 */
	public Direction switchDirection() {
		if (this == UP) {
			return DOWN;
		} else {
			return UP;
		}
	}
}
