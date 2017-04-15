import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class CareCenter {
	private double foodStock;
	private double gameStock;
	private double hygieneStock;
	private double avgHappiness;
	private double previousDayHappiness;
	private double credit;
	private double score;
	public static Child[] childArray = new Child[100];
	public static Carer[] carerArray = new Carer[100];
	public static Worker[] workerArray = new Worker[100];
	public static int[] applicationArray = { -1, -1, -1 };
	public static int sizeOfChild;
	public static int sizeOfCarer;
	public static int sizeOfWorker;
	public static int sizeOfApplications;

	public CareCenter() {

		foodStock = round(getRandomStock());
		gameStock = round(getRandomStock());
		hygieneStock = round(getRandomStock());
		avgHappiness = 0.0;
		credit = 200;
		score = 0;
		previousDayHappiness = 70.0;
	}

	private double round(double value) {
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(2, RoundingMode.HALF_UP);

		return bd.doubleValue();
	}

	
	public double getFoodStock() {
		return foodStock;
	}

	public void setFoodStock(double foodStock) {
		this.foodStock = foodStock;
	}

	public double getGameStock() {
		return gameStock;
	}

	public void setGameStock(double gameStock) {
		this.gameStock = gameStock;
	}

	public double getHygieneStock() {
		return hygieneStock;
	}

	public void setHygieneStock(double hygieneStock) {
		this.hygieneStock = hygieneStock;
	}

	public double getAvgHappiness() {
		return avgHappiness;
	}

	public void setAvgHappiness(double avgHappiness) {
		this.avgHappiness = avgHappiness;
	}

	public double getCredit() {
		return credit;
	}

	public void setCredit(double credit) {
		this.credit = credit;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public double getPreviousDayHappiness() {
		return previousDayHappiness;
	}

	public void setPreviousDayHappiness(double previousDayHappiness) {
		this.previousDayHappiness = previousDayHappiness;
	}

	
	public double getRandomStock() {
		Random random = new Random();
		return round(100 * random.nextDouble());
	}

}
