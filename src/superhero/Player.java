package superhero;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Player {
	private int coins;
	private HashSet<GameCharacter> characters;

	public Player(int coins, Set<GameCharacter> characters) {
		this.coins = coins;
		this.characters = new HashSet<>(characters);
	}

	public int getCoins() {
		return this.coins;
	}

	public Set<GameCharacter> getCharacters() {
		return new HashSet<>(this.characters);
	}

	public void buy(GameCharacter gc) throws IllegalArgumentException {
		if (characters.contains(gc)) {
			throw new IllegalArgumentException("You already have this character");
		} else if (gc.getCost() > this.coins) {
			throw new IllegalArgumentException("You cannot afford this character");
		} else {
			coins -= gc.getCost();
			characters.add(gc);
		}
	}
	
	public Set<GameCharacter> chooseCharacters(Power...powers){
		HashSet<GameCharacter> charactersNeeded = new HashSet<>();
		List<GameCharacter> charactersToBuy = new ArrayList<>();
		List<GameCharacter> charactersAll = new ArrayList<>(GameCharacters.getAllCharacters());
		Collections.sort(charactersAll);
		
		boolean powerFound;
		int totalPurchaseAmount  = this.coins;
		
		for (Power power : powers) {
			powerFound = false;
			
			for (GameCharacter gcToBuy : charactersToBuy) {
				if (gcToBuy.getPowers().contains(power)) {
					powerFound = true;
					break;
				}
			}
			
			for (GameCharacter gcOwned : characters) {
				if (gcOwned.getPowers().contains(power)) {
					powerFound = true;
					charactersNeeded.add(gcOwned);
					break;
				}
			}
			
			if (!powerFound) {
				for (GameCharacter chr : charactersAll) {
					if (!characters.contains(chr) && chr.getPowers().contains(power)) {
						if (chr.getCost() <= totalPurchaseAmount) {
							totalPurchaseAmount -= chr.getCost();
							charactersToBuy.add(chr);
							powerFound = true;
							break;
						}
					}
				}
			}
			
			if (!powerFound) {
				return null;
			}
		}
		
		for (GameCharacter gc : charactersToBuy) {
			buy(gc);
			charactersNeeded.add(gc);
		}
		return charactersNeeded;
	}
}
