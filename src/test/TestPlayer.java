package test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import superhero.GameCharacter;
import superhero.GameCharacters;
import superhero.Player;
import superhero.Power;

class TestPlayer {

	private Player player;
	private GameCharacter robin, brain, cinderblock;

	@BeforeEach
	void setUp() throws Exception {
		Set<GameCharacter> characters = new HashSet<>();
		for (GameCharacter gc : GameCharacters.getAllCharacters()) {
			switch (gc.getName()) {
			case "Robin":
				robin = gc;
			case "Starfire":
			case "Cyborg":
			case "Beast Boy":
			case "Raven":
				characters.add(gc);
				break;

			case "Brain":
				brain = gc;
				break;

			case "Cinderblock":
				cinderblock = gc;
			}
		}
		player = new Player(150, characters);
	}

	@AfterEach
	void tearDown() throws Exception {
		player = null;
		robin = null;
		brain = null;
		cinderblock = null;
	}
	
	@Test
	public void noExtraPublicMethods() {
		List<String> methodNames = new ArrayList<>(Arrays.asList("getCoins", "getCharacters", "buy", "chooseCharacters"));
		for (Method method : Player.class.getDeclaredMethods()) {
			if (Modifier.isPublic(method.getModifiers())) {
				if (!methodNames.remove(method.getName())) {
					if (method.getName().equals("toString")) {
						// Okay
					} else {
						Assertions.fail("Unexpected public method: " + method.getName());
					}
				}
			}
		}
		if (!methodNames.isEmpty()) {
			Assertions.fail("Missing public method(s): " + methodNames);
		}
	}

	@Test
	public void buyShouldThrowExceptionIfCharacterIsOwned() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			player.buy(robin);
		}, "buy() should throw exception if player already owns character");
	}

	@Test
	public void buyShouldThrowExceptionIfCharacterIsTooExpensive() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			player.buy(brain);
		}, "buy() should throw exception if character is too expensive");
	}

	@Test
	public void buyShouldAddCharacterAndSubtractCost() {
		player.buy(cinderblock);
		Assertions.assertEquals(100, player.getCoins(), "buy() should subtract cost of character");
		Assert.assertThat(player.getCharacters(), CoreMatchers.hasItem(cinderblock));
	}

	@Test
	public void chooseCharactersShouldUseExistingCharacters() {
		Set<GameCharacter> playerCharacters = new HashSet<>(player.getCharacters());
		int playerCoins = player.getCoins();
		Set<GameCharacter> chosenCharacters = player.chooseCharacters(Power.FLIGHT, Power.STRENGTH,
				Power.TRANSFORMATION);

		Set<Power> powers = chosenCharacters.stream().map(c -> c.getPowers()).flatMap(s -> s.stream())
				.collect(Collectors.toSet());
		Assert.assertThat("Chosen characters should cover required powers", powers,
				CoreMatchers.hasItems(Power.FLIGHT, Power.STRENGTH, Power.TRANSFORMATION));
		Assertions.assertIterableEquals(playerCharacters, player.getCharacters(),
				"Player characters should not change if powers already available");
		Assertions.assertEquals(playerCoins, player.getCoins(),
				"Player coins should not change if powers already available");
		Assert.assertThat(playerCharacters, CoreMatchers.hasItems(chosenCharacters.toArray(new GameCharacter[0])));
	}

	@Test
	public void chooseCharactersShouldAddExtraCharactersWhereAvailable() {
		Set<GameCharacter> playerCharacters = new HashSet<>(player.getCharacters());
		int playerCoins = player.getCoins();
		Set<GameCharacter> chosenCharacters = player.chooseCharacters(Power.COMPUTER, Power.MAGIC, Power.WATER,
				Power.SCIENCE, Power.SMALL);

		Set<Power> powers = chosenCharacters.stream().map(c -> c.getPowers()).flatMap(s -> s.stream())
				.collect(Collectors.toSet());
		Assert.assertThat("Chosen characters should cover required powers", powers,
				CoreMatchers.hasItems(Power.COMPUTER, Power.MAGIC, Power.WATER, Power.SCIENCE, Power.SMALL));

		// New characters should be a super-set of old characters
		Set<GameCharacter> newCharacters = player.getCharacters();
		Assert.assertThat("New characters should be super-set of old characters", newCharacters,
				CoreMatchers.hasItems(playerCharacters.toArray(new GameCharacter[0])));

		// Anything bought should be affordable
		int newPlayerCoins = player.getCoins();
		Set<GameCharacter> addedCharacters = new HashSet<>(newCharacters);
		addedCharacters.removeAll(playerCharacters);
		int totalCost = addedCharacters.stream().mapToInt(c -> c.getCost()).sum();
		Assertions.assertEquals(playerCoins - newPlayerCoins, totalCost, "Total cost spent should be cost of new characters");
		Assertions.assertTrue(newPlayerCoins >= 0, "Player coins should not be negative");
	}
	
	@Test
	public void chooseCharactersShouldReturnNullIfNotPossible() {
		Set<GameCharacter> playerCharacters = new HashSet<>(player.getCharacters());
		int playerCoins = player.getCoins();
		Set<GameCharacter> chosenCharacters = player.chooseCharacters(Power.MAGNETISM);
		Assertions.assertNull(chosenCharacters, "chooseCharacters should return null if no solution possible");
		
		// Nothing should change
		Assertions.assertIterableEquals(playerCharacters, player.getCharacters(),
				"Player characters should not change if no solution possible");
		Assertions.assertEquals(playerCoins, player.getCoins(),
				"Player coins should not change if no solution possible");
	}

	@Test
	public void chooseCharacterShouldNotProduceOutput() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		System.setOut(new PrintStream(baos));
		player.chooseCharacters(Power.FLIGHT, Power.STRENGTH, Power.TRANSFORMATION);
		Assertions.assertEquals("", baos.toString(), "chooseCharacters should not produce any output to System.out");
	}
}
