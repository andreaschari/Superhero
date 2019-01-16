package test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
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
import superhero.Power;

class TestGameCharacter {

	private GameCharacter robin, cyborg, beastBoy, starfire, raven;

	@BeforeEach
	void setUp() throws Exception {
		robin = new GameCharacter("Robin", 100, Power.WEAPONS);
		starfire = new GameCharacter("Starfire", 200, Power.STRENGTH, Power.FLIGHT, Power.ENERGY_BLAST);
		cyborg = new GameCharacter("Cyborg", 150, Power.STRENGTH, Power.COMPUTER, Power.FLIGHT, Power.WEAPONS);
		beastBoy = new GameCharacter("Beast Boy", 150, Power.TRANSFORMATION);
		raven = new GameCharacter("Raven", 100, Power.MAGIC);
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	public void powerHasCorrectValues() {
		List<String> actualNames = Arrays.asList(Power.values()).stream().map(p -> p.name())
				.collect(Collectors.toList());
		List<String> expectedPowers = Arrays.asList("CLONING", "COMPUTER", "ENERGY_BLAST", "FLIGHT", "INVINCIBILITY",
				"MAGIC", "MAGNETISM", "SCIENCE", "SMALL", "SPEED", "STRENGTH", "TELEPATHY", "TRANSFORMATION", "WEAPONS",
				"WATER");
		Assertions.assertIterableEquals(expectedPowers, actualNames, "Power enum not as expected");
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void constructorIsVarargs() {
		Constructor[] constructors = GameCharacter.class.getConstructors();
		Assertions.assertEquals(1, constructors.length, "GameCharacter should only have one constructor");
		Constructor cons = constructors[0];
		Assertions.assertTrue(cons.isVarArgs(), "GameCharacter constructor should be varargs");
	}

	@Test
	public void fieldsArePrivateAndFinal() {
		for (Field field : GameCharacter.class.getDeclaredFields()) {
			Assertions.assertTrue(Modifier.isPrivate(field.getModifiers()), "Field " + field + " should be private");
			Assertions.assertTrue(Modifier.isFinal(field.getModifiers()), "Field " + field + " should be final");
		}
	}
	
	@Test
	public void classIsFinal() {
		Assertions.assertTrue(Modifier.isFinal(GameCharacter.class.getModifiers()), "Class GameCharacter should be final");
	}

	@Test
	public void equalsAndHashCodeSameForIdenticalCharacters() {
		GameCharacter robinCopy = new GameCharacter("Robin", 100, Power.WEAPONS);
		Assertions.assertEquals(robin, robinCopy, "Two identical characters should be equal()");
		Assertions.assertEquals(robin, robinCopy, "Two identical characters should have same hashCode()");
	}

	@Test
	public void equalsAndHashCodeDifferForDifferentCharacters() {
		GameCharacter robinCopy = new GameCharacter("Robin2", 100, Power.WEAPONS);
		Assertions.assertNotEquals(robin, robinCopy, "Two different characters should not be equal()");
		Assertions.assertNotEquals(robin, robinCopy, "Two different characters should not have same hashCode()");
		robinCopy = new GameCharacter("Robin", 110, Power.WEAPONS);
		Assertions.assertNotEquals(robin, robinCopy, "Two different characters should not be equal()");
		Assertions.assertNotEquals(robin, robinCopy, "Two different characters should not have same hashCode()");
		robinCopy = new GameCharacter("Robin", 100);
		Assertions.assertNotEquals(robin, robinCopy, "Two different characters should not be equal()");
		Assertions.assertNotEquals(robin, robinCopy, "Two different characters should not have same hashCode()");
	}

	@Test
	public void toStringIsOverridden() {
		String ravenString = raven.toString();
		Assert.assertThat("GameCharacter.toString() should contain all properties", ravenString,
				CoreMatchers.allOf(CoreMatchers.containsString("Raven"), CoreMatchers.containsString("100"),
						CoreMatchers.containsString("MAGIC")));
	}

	@Test
	public void compareToComparesProperly() {
		Assertions.assertEquals(cyborg.compareTo(beastBoy), 0, "Two characters with same cost should compare equal");
		Assertions.assertTrue(cyborg.compareTo(starfire) < 0,
				"Character with less cost should compare before character with greater cost");
		Assertions.assertTrue(beastBoy.compareTo(robin) > 0,
				"Character with greater cost should compare after character with less cost");
	}

	@Test
	public void constructorMakesCopyOfInput() {
		Power[] powers = new Power[] { Power.INVINCIBILITY, Power.FLIGHT, Power.ENERGY_BLAST };
		Set<Power> initPowers = new HashSet<>(Arrays.asList(powers));
		GameCharacter superman = new GameCharacter("Superman", 1000, powers);
		powers[1] = Power.COMPUTER;
		Assertions.assertIterableEquals(initPowers, superman.getPowers(),
				"Should not be possible to change character powers after construction");
	}

	@Test
	public void getPowersReturnsACopy() {
		Set<Power> cyborgPowers = new HashSet<>(cyborg.getPowers());
		try {
			cyborg.getPowers().add(Power.INVINCIBILITY);
			Assertions.assertIterableEquals(cyborgPowers, cyborg.getPowers(),
					"Should not be possible to change character powers through getPowers");
		} catch (UnsupportedOperationException ex) {
			// This is all right too as a different approach to immutability
		}
	}

}