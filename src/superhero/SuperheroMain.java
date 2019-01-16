package superhero;

import java.util.HashSet;
import java.util.Set;

/**
 * A class with a main method, for testing the classes implemented in the JP2 lab exam 2018.
 * 
 * Feel free to modify this class to test your code under various configurations.
 * 
 * @author mefoster
 *
 */
public class SuperheroMain {

	public static void main(String[] args) {
		// Load the complete set of characters and pick out the ones that our player will have
		Set<GameCharacter> characters = new HashSet<>();
		for (GameCharacter gc : GameCharacters.getAllCharacters()) {
			switch (gc.getName()) {
			case "Robin":
			case "Starfire":
			case "Cyborg":
			case "Beast Boy":
			case "Raven":
				characters.add(gc);
				break;
			}
		}
		
		// Create the Player object and print out some information about them
		Player player = new Player(150, characters);
		System.out.println("Player coins at start: " + player.getCoins());
		System.out.println("Player characters at start:");
		player.getCharacters().forEach(System.out::println);
		System.out.println();

		// Find characters for a specific set of powers -- feel free to modify this if you want to test other
		// aspects of your code
		Set<GameCharacter> chosenCharacters = player.chooseCharacters(Power.COMPUTER, Power.MAGIC, Power.WATER,
				Power.SCIENCE, Power.SMALL);
		System.out.println("Chosen character(s):");
		chosenCharacters.forEach(System.out::println);
		System.out.println();

		// Print out the final version of the player to verify that things worked as expected
		System.out.println("Player coins now: " + player.getCoins());
		System.out.println("Player characters now:");
		player.getCharacters().forEach(System.out::println);
	}

}
