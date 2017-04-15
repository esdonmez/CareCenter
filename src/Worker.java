
public class Worker {
	private String name;
	private int wage;
	private int timer;
	private Child child;
	private Supply[] supply = {null, null, null};
	private static int indexName = 2;

	public Worker() {

	}

	public Worker(String name) {
		this.name = name;
		this.wage = 30;
		this.timer = 0;
		this.child = null;
		this.supply = null;
	}

	
	public String createName() {
		String name = null;

		if (CareCenter.sizeOfWorker < 9) {
			name = "wr0" + indexName;
		} else if (CareCenter.sizeOfWorker >= 9) {
			name = "wr" + indexName;
		}
		indexName++;
		return name;
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

	public int getTimer() {
		return timer;
	}

	public void setTimer(int timer) {
		this.timer = timer;
	}

	public Child getChild() {
		return child;
	}

	public void setChild(Child child) {
		this.child = child;
	}

	public Supply[] getSupply() {
		return supply;
	}

	public void setSupply(Supply[] supply) {
		this.supply = supply;
	}

}
