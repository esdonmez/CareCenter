
public class Supply 
{	
	private String type;
	private double credit;
	private double amount;
	
	
	public Supply(String type, double amount)
	{
		//for carer
		this.type = type;
		this.amount = amount;
	}
	
	public Supply() 
	{
		//for worker
		this.type = null;
		this.credit = 0;
		this.amount = 0;
	}

	
	public double getCredit() {
		return credit;
	}

	public void setCredit(double credit) {
		this.credit = credit;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

}
