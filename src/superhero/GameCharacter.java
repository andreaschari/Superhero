package superhero;

import java.util.Arrays;
import java.util.HashSet;

public final class GameCharacter implements Comparable<GameCharacter> {
	private final String name;
	private final int cost;
	private final HashSet<Power> powers;

	public GameCharacter(String name, int cost, Power... powers) {
		this.name = name;
		this.cost = cost;
		this.powers = new HashSet<>(Arrays.asList(powers));
	}

	public String getName() {
		return this.name;
	}

	public int getCost() {
		return this.cost;
	}

	public HashSet<Power> getPowers() {
		return new HashSet<>(powers);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + cost;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((powers == null) ? 0 : powers.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GameCharacter other = (GameCharacter) obj;
		if (cost != other.cost)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (powers == null) {
			if (other.powers != null)
				return false;
		} else if (!powers.equals(other.powers))
			return false;
		return true;
	}

	public String toString() {
		return name + " (" + cost + ") : [" + powers + "]";
	}

	public int compareTo(GameCharacter otherGc) {
		return Integer.compare(this.cost, otherGc.getCost());
	}
}
