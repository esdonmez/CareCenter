import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class Carer {
	private String name;
	private int timer;
	private int wage; // equals payment.
	private Child child;
	private double meetingFoodRate;
	private double meetingGameRate;
	private double meetingSleepRate;
	private double meetingHygieneRate;
	private Supply supply = new Supply(null, 0);
	private static int indexName = 2;

	public Carer() {

	}

	public Carer(String name) {
		this.name = name;
		this.wage = 50;
		this.timer = 0;
		this.child = null;
		this.meetingFoodRate = getRandomMeetingRate(6.00, 18.00);
		this.meetingGameRate = getRandomMeetingRate(3.00, 9.00);
		this.meetingSleepRate = getRandomMeetingRate(5.00, 15.00);
		this.meetingHygieneRate = getRandomMeetingRate(8.00, 24.00);
	}

	
	public String createName() {
		String name = null;

		if (CareCenter.sizeOfCarer < 9) {
			name = "cr0" + indexName;
		} else if (CareCenter.sizeOfCarer >= 9) {
			name = "cr" + indexName;
		}
		indexName++;
		return name;
	}

	public double getRandomMeetingRate(double first, double second) {
		Random rnd = new Random();
		return round(first + (second - first) * rnd.nextDouble());
	}

	private double round(double value) 
	{
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(2, RoundingMode.HALF_UP);

		return bd.doubleValue();
	}

	
	public int getWage() {
		return wage;
	}

	public void setWage(int wage) {
		this.wage = wage;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Child getChild() {
		return child;
	}

	public void setChild(Child child) {
		this.child = child;
	}

	public double getMeetingFoodRate() {
		return meetingFoodRate;
	}

	public void setMeetingFoodRate(double meetingFoodRate) {
		this.meetingFoodRate = meetingFoodRate;
	}

	public double getMeetingGameRate() {
		return meetingGameRate;
	}

	public void setMeetingGameRate(double meetingGameRate) {
		this.meetingGameRate = meetingGameRate;
	}

	public double getMeetingSleepRate() {
		return meetingSleepRate;
	}

	public void setMeetingSleepRate(double meetingSleepRate) {
		this.meetingSleepRate = meetingSleepRate;
	}

	public double getMeetingHygieneRate() {
		return meetingHygieneRate;
	}

	public void setMeetingHygieneRate(double meetingHygieneRate) {
		this.meetingHygieneRate = meetingHygieneRate;
	}

	public int getTimer() {
		return timer;
	}

	public void setTimer(int timer) {
		this.timer = timer;
	}

	public Supply getSupply() {
		return supply;
	}

	public void setSupply(Supply supply) {
		this.supply = supply;
	}

}
