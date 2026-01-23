package nl.tudelft.jpacman.npc.ghost;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.sprite.Sprite;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author Hannah Potter
 * 
 */
public class Experimenter extends Ghost {

	/**
	 * The amount of cells Clyde wants to stay away from Pac Man.
	 */
	private static final int SHYNESS = 5;

	/**
	 * The variation in intervals, this makes the ghosts look more dynamic and
	 * less predictable.
	 */
	private static final int INTERVAL_VARIATION = 40;

	/**
	 * The base movement interval.
	 */
	private static final int MOVE_INTERVAL = 200;

	/**
	 * A map of opposite directions.
	 */
	private static final Map<Direction, Direction> OPPOSITES = new EnumMap<>(
			Direction.class);
	static {
		OPPOSITES.put(Direction.NORTH, Direction.SOUTH);
		OPPOSITES.put(Direction.SOUTH, Direction.NORTH);
		OPPOSITES.put(Direction.WEST, Direction.EAST);
		OPPOSITES.put(Direction.EAST, Direction.WEST);
	}

	/**
	 * Creates a new "Clyde", a.k.a. "Pokey".
	 *
	 * @param spriteMap
	 *            The sprites for this ghost.
	 */
	public Experimenter(Map<Direction, Sprite> spriteMap) {
		super(spriteMap);
	}
	
	@Override
	public long getInterval() {
		return MOVE_INTERVAL + new Random().nextInt(INTERVAL_VARIATION);
	}
	

	/**
	 * {@inheritDoc}
	 * 
	 * <p>
	 * Pokey has two basic AIs, one for when he's far from Pac-Man, and one for
	 * when he is near to Pac-Man. When the ghosts are not patrolling their home
	 * corners, and Pokey is far away from Pac-Man (beyond eight grid spaces),
	 * Pokey behaves very much like Blinky, trying to move to Pac-Man's exact
	 * location. However, when Pokey gets within eight grid spaces of Pac-Man,
	 * he automatically changes his behavior and goes to patrol his home corner
	 * in the bottom-left section of the maze.
	 * </p>
	 * <p>
	 * <b>Implementation:</b> Lacking a patrol function so far, Clyde will just
	 * move in the opposite direction when he gets within 8 cells of Pac-Man.
	 * </p>
	 */
	@Override
	public Direction nextMove() {
		Square target = Navigation.findNearest(Player.class, getSquare())
				.getSquare();
		if (target == null) {
			return randomMove();
		}

		List<Direction> path = Navigation.shortestPath(getSquare(), target,
				this);
		if (path != null && !path.isEmpty()) {
			Direction d = path.get(0);
			if (path.size() <= SHYNESS) {
				Direction oppositeDir = OPPOSITES.get(d);
				return oppositeDir;
			}
			return d;
		}
		Direction d = randomMove();
		return d;
	}
}
