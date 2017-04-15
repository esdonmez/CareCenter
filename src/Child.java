import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class Child {
	private String name;
	private double food;
	private double game;
	private double sleep;
	private double hygiene;
	private int missing;
	private int penalty;
	private double happiness;
	private double foodDecreasingRate;
	private double gameDecreasingRate;
	private double sleepDecreasingRate;
	private double hygieneDecreasingRate;
	private double happinessRateFood;
	private double happinessRateGame;
	private double happinessRateSleep;
	private double happinessRateHygiene;
	private Carer carer;
	private Worker worker;
	private static int indexName = 2;

	public Child() {
		this.name = createName();
		this.food = getRandomSupplyValues();
		this.game = getRandomSupplyValues();
		this.sleep = getRandomSupplyValues();
		this.hygiene = getRandomSupplyValues();
	}

	public Child(String name, double food, double game, double sleep, double hygiene) {
		this.name = name;
		this.food = food;
		this.game = game;
		this.sleep = sleep;
		this.hygiene = hygiene;
		this.missing = 0;
		this.penalty = 50;
		this.happiness = getInitialHappiness();
		this.carer = null;
		this.worker = null;
		this.foodDecreasingRate = getRandomDecreasingRate(0.50, 1.50);
		this.gameDecreasingRate = getRandomDecreasingRate(0.50, 1.50);
		this.sleepDecreasingRate = getRandomDecreasingRate(0.25, 0.75);
		this.hygieneDecreasingRate = getRandomDecreasingRate(0.20, 0.50);
		this.happinessRateFood = 0.24;
		this.happinessRateGame = 0.12;
		this.happinessRateHygiene = 0.06;
		this.happinessRateSleep = 0.08;
	}

	
	private double round(double value) {
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(2, RoundingMode.HALF_UP);

		return bd.doubleValue();
	}

	public String createName() {
		String name = null;

		if (CareCenter.sizeOfChild < 9) {
			name = "ch0" + indexName;
		} else if (CareCenter.sizeOfChild >= 9) {
			name = "ch" + indexName;
		}
		indexName++;
		return name;
	}

	
	public int getPenalty() {
		return penalty;
	}

	public void setPenalty(int penalty) {
		this.penalty = penalty;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getFood() {
		return food;
	}

	public void setFood(double food) {
		this.food = food;
	}

	public double getInitialHappiness() {

		Random rnd = new Random();
		return round(20 * rnd.nextDouble() + 40);
	}

	public double getRandomSupplyValues() {

		Random rnd = new Random();
		return round(25 * rnd.nextDouble() + 50);
	}

	public double getGame() {
		return game;
	}

	public void setGame(double game) {
		this.game = game;
	}

	public double getSleep() {
		return sleep;
	}

	public void setSleep(double sleep) {
		this.sleep = sleep;
	}

	public double getHygiene() {
		return hygiene;
	}

	public void setHygiene(double hygiene) {
		this.hygiene = hygiene;
	}

	public int getMissing() {
		return missing;
	}

	public void setMissing(int missing) {
		this.missing = missing;
	}

	public double getHappiness() {
		return happiness;
	}

	public void setHappiness(double happiness) {
		this.happiness = happiness;
	}

	public Carer getCarer() {
		return carer;
	}

	public void setCarer(Carer carer) {
		this.carer = carer;
	}

	public Worker getWorker() {
		return worker;
	}

	public void setWorker(Worker worker) {
		this.worker = worker;
	}

	public double getFoodDecreasingRate() {
		return foodDecreasingRate;
	}

	public void setFoodDecreasingRate(double foodDecreasingRate) {
		this.foodDecreasingRate = foodDecreasingRate;
	}

	public double getGameDecreasingRate() {
		return gameDecreasingRate;
	}

	public void setGameDecreasingRate(double gameDecreasingRate) {
		this.gameDecreasingRate = gameDecreasingRate;
	}

	public double getSleepDecreasingRate() {
		return sleepDecreasingRate;
	}

	public void setSleepDecreasingRate(double sleepDecreasingRate) {
		this.sleepDecreasingRate = sleepDecreasingRate;
	}

	public double getHygieneDecreasingRate() {
		return hygieneDecreasingRate;
	}

	public void setHygieneDecreasingRate(double hygieneDecreasingRate) {
		this.hygieneDecreasingRate = hygieneDecreasingRate;
	}

	public double getHappinessRateFood() {
		return happinessRateFood;
	}

	public void setHappinessRateFood(double happinessRateFood) {
		this.happinessRateFood = happinessRateFood;
	}

	public double getHappinessRateGame() {
		return happinessRateGame;
	}

	public void setHappinessRateGame(double happinessRateGame) {
		this.happinessRateGame = happinessRateGame;
	}

	public double getHappinessRateSleep() {
		return happinessRateSleep;
	}

	public void setHappinessRateSleep(double happinessRateSleep) {
		this.happinessRateSleep = happinessRateSleep;
	}

	public double getHappinessRateHygiene() {
		return happinessRateHygiene;
	}

	public void setHappinessRateHygiene(double happinessRateHygiene) {
		this.happinessRateHygiene = happinessRateHygiene;
	}

	public double getRandomDecreasingRate(double first, double second) {
		Random rnd = new Random();
		return round(first + (second - first) * rnd.nextDouble());
	}

}
