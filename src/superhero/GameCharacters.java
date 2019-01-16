package superhero;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameCharacters {

	/** The file to load the characters from. */
	private static final String FILE_NAME = "characters.txt";
	/** The list of characters */
	private static List<GameCharacter> ALL_CHARACTERS;

	/**
	 * Returns the full list of game characters. Note that this method loads the list
	 * from a file if it is not already loaded. The returned list cannot be modified
	 * in any way.
	 * 
	 * @return The list of all available game characters
	 */
	public static List<GameCharacter> getAllCharacters() {
		if (ALL_CHARACTERS == null) {
			loadAllCharacters();
		}
		return ALL_CHARACTERS;
	}
	
	/**
	 * Private helper method to load all the characters from the file on disk.
	 */
	private static void loadAllCharacters() {
		List<GameCharacter> characters = new ArrayList<>();
		Path path = Paths.get(FILE_NAME);
		List<String> lines;
		try {
			lines = Files.readAllLines(path);
		} catch (IOException e) {
			// Shouldn't happen
			ALL_CHARACTERS = Collections.emptyList();
			e.printStackTrace();
			return;
		}
		for (String line : lines) {
			String[] items = line.split(",");
			String name = items[0];
			int cost = Integer.valueOf(items[1]);
			Power[] powers = new Power[items.length - 2];
			for (int i = 2; i < items.length; i++) {
				powers[i-2] = Power.valueOf(items[i]);
			}
			characters.add(new GameCharacter(name, cost, powers));
		}
		ALL_CHARACTERS = Collections.unmodifiableList(characters);
	}
	
	public static void main(String[] args) {
		loadAllCharacters();
		ALL_CHARACTERS.stream().sorted().forEach(System.out::println);
	}
}
