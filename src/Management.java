import java.util.Date;
import java.util.Random;
import java.util.Scanner;

//import javax.swing.ProgressMonitorInputStream;

import enigma.console.TextAttributes;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
//import java.text.DecimalFormat;

//////////round function; child leave center!!!!
public class Management {
	Scanner scan = new Scanner(System.in);
	Random random = new Random();
	private CareCenter carecenter;
	private Worker worker;
	private Carer carer;
	private Child child;
	private int turn = 0;
	private int day = 0;
	private String input = "";
	private double happiness = 0;
	public KeyListener klis;
	public int keypr; // key pressed?
	public int rkey; // key (for press/release)
	public char ckey;
	private boolean isHelp = false;

	public Management() throws InterruptedException 
	{
		klis = new KeyListener() 
		{
			public void keyTyped(KeyEvent e) {}

			public void keyPressed(KeyEvent e) {
				if (keypr == 0) {
					keypr = 1;
					rkey = e.getKeyCode();
					ckey = e.getKeyChar();
				}
			}
			
			public void keyReleased(KeyEvent e) {}
		};
		Main.cn.getTextWindow().addKeyListener(klis);

		carecenter = new CareCenter();
		AddCarer("cr01");
		AddChild("ch01", getRandomSupplyValues(), getRandomSupplyValues(), getRandomSupplyValues(), getRandomSupplyValues());
		AddWorker("wr01");
		
		long startTime = new Date().getTime();

		while (turn <= 50) 
		{
			long endTime = new Date().getTime();
			long difference = endTime - startTime;

			if (difference / 1000 >= 2) 
			{
				ClearScreen();
				
				calculateAverageHappiness();
				calculateScore();
				GetInformation();
				Screen();
				CheckScreen();
				
				Shopping();

				CreateRandomApplication();
				ApplicationProgress();
				ControlforApplications();

				DecreaseSuppliesofChildren();
				GiveSupplytoChildren();
				CalculateHappinessRate();
				DefineMissingValueforChildren();
				ControlMissingofChildren();
				
				FindingProcess();

				DecreaseTimerforCarer();
				DecreaseTimerforWorker();

				startTime = endTime;
				if (finishState() == true)
					break;
			}

			if (keypr == 1) 
			{
				if ((rkey >= KeyEvent.VK_NUMPAD0 && rkey <= KeyEvent.VK_NUMPAD9) || (rkey >= KeyEvent.VK_A && rkey <= KeyEvent.VK_Z) || rkey == KeyEvent.VK_SPACE
						|| rkey == KeyEvent.VK_ENTER || rkey == KeyEvent.VK_BACK_SPACE) 
				{
					Main.cn.getTextWindow().setCursorPosition(0, 42);
					Main.cn.getTextWindow().output("Command > ", new TextAttributes(Color.red));
					input = input + ckey;

					Main.cn.getTextWindow().setCursorPosition(11, 42);
					Main.cn.getTextWindow().output(input);

					if (rkey == KeyEvent.VK_BACK_SPACE && input.length() > 1) 
					{
						if (input.charAt(input.length() - 1) == '\b') 
						{
							input = removeLastChar(input);
							Main.cn.getTextWindow().setCursorPosition(11, 42);
							Main.cn.getTextWindow().output(input + " ");
						}

						input = removeLastChar(input);
						Main.cn.getTextWindow().setCursorPosition(11, 42);
						Main.cn.getTextWindow().output(input + " ");
					}

					if (rkey == KeyEvent.VK_ENTER) 
					{
						ClearCommand();
						GetCommand(input);
						input = "                              ";
						Main.cn.getTextWindow().setCursorPosition(11, 42);
						Main.cn.getTextWindow().output(input);
						input = "";
						Main.cn.getTextWindow().setCursorPosition(11, 42);
						Main.cn.getTextWindow().output(input);
						keypr = 0;
						if(isHelp == true)
						{
							ClearScreen();
							while(isHelp)
							{
								HelpScreen();
								if (keypr == 1 && rkey == KeyEvent.VK_DELETE) 
								{
									ClearCommand();
									ClearScreen();
									Screen();
									isHelp = false;
									break;
								} 
							}
						}
					}
				}

				if (rkey == KeyEvent.VK_ESCAPE) 
				{
					ClearScreen();
					keypr = 0;
					while (true) 
					{
						PauseScreen();
						GetPauseInformation();
						if (keypr == 1 && rkey == KeyEvent.VK_DELETE) 
						{
							ClearCommand();
							ClearScreen();
							Screen();
							break;
						} 
						else
							keypr = 0;
					}
				}
				keypr = 0;
			}
		}
	}

	public void GetCommand(String value) 
	{
		String[] inputArray = value.split(" ");

		inputArray[inputArray.length - 1] = removeLastChar(inputArray[inputArray.length - 1]);
		
		if(inputArray.length >= 1 && inputArray[0].length() >= 4)
		{
			if (inputArray[0].substring(0, 2).equalsIgnoreCase("cr") && inputArray[1].length() >= 1 && findCarerByName(inputArray[0]) != null && findChildByName(inputArray[2]) != null) 
			{
				if (inputArray.length == 4 && inputArray[2].length() >= 4 && inputArray[3].length() >= 1  && findCarerByName(inputArray[0]).getTimer() == 0 && findChildByName(inputArray[2]).getCarer() == null && findChildByName(inputArray[2]).getWorker() == null && Double.parseDouble(inputArray[3]) >= 1) 
				{
					if (inputArray[1].equalsIgnoreCase("s")) 
					{
						findCarerByName(inputArray[0]).getSupply().setType(inputArray[1]);
						findCarerByName(inputArray[0]).getSupply().setAmount(round(Integer.parseInt(inputArray[3])));
						SleepLimited(findChildByName(inputArray[2]), findCarerByName(inputArray[0]), Integer.parseInt(inputArray[3]));
					}

					else 
					{
						findChildByName(inputArray[2]).setCarer(findCarerByName(inputArray[0]));
						findCarerByName(inputArray[0]).setChild(findChildByName(inputArray[2]));
						findCarerByName(inputArray[0]).getSupply().setType(inputArray[1]);
						findCarerByName(inputArray[0]).getSupply().setAmount(round(Double.parseDouble(inputArray[3])));
					
						if(findCarerByName(inputArray[0]).getSupply().getAmount() > 25)
						{
							findCarerByName(inputArray[0]).getSupply().setAmount(25);
						}
					
						findCarerByName(inputArray[0]).setTimer(roundUP(findCarerByName(inputArray[0]).getSupply().getAmount() / findCarerByName(inputArray[0]).getMeetingFoodRate()));
					}
				}

				if (inputArray.length == 3 && inputArray[2].length() >= 4 && findCarerByName(inputArray[0]).getTimer() == 0 && findChildByName(inputArray[2]).getCarer() == null  && findChildByName(inputArray[2]).getWorker() == null) 
				{
					if (inputArray[1].equalsIgnoreCase("c")) 
					{
						CheckChild(findChildByName(inputArray[2]), findCarerByName(inputArray[0]));
					}

					if (inputArray[1].equalsIgnoreCase("s")) 
					{
						findCarerByName(inputArray[0]).getSupply().setType(inputArray[1]);
						findCarerByName(inputArray[0]).getSupply().setAmount(round(Integer.MAX_VALUE));
						SleepUnlimited(findChildByName(inputArray[2]), findCarerByName(inputArray[0]));
					}
				}

				if (inputArray.length == 2) 
				{
					if (inputArray[1].equalsIgnoreCase("r") && findCarerByName(inputArray[0]).getTimer() != 0) 
					{
						CarerBreakJob(findCarerByName(inputArray[0]));
					}

					if (inputArray[1].equalsIgnoreCase("t")) 
					{
						CarerTerminate(findCarerByName(inputArray[0]));
					}
				}
			}

			if (inputArray[0].substring(0, 2).equalsIgnoreCase("wr") && inputArray[1].length() == 1  && findWorkerByName(inputArray[0]) != null) 
			{
				if (inputArray.length == 2 && inputArray[1].equalsIgnoreCase("t")) 
				{
					WorkerTerminate(findWorkerByName(inputArray[0]));
				}
				
				if (inputArray[1].equalsIgnoreCase("r")  && findWorkerByName(inputArray[0]).getTimer() != 0) 
				{
					WorkerBreakJob(findWorkerByName(inputArray[0]));
				}

				if (inputArray.length == 3 && inputArray[1].equalsIgnoreCase("c") && findWorkerByName(inputArray[0]).getTimer() == 0 && findChildByName(inputArray[2]) != null && findChildByName(inputArray[2]).getCarer() == null && findChildByName(inputArray[2]).getWorker() == null) 
				{
					FindChild(findChildByName(inputArray[2]), findWorkerByName(inputArray[0]));
				}

				if (inputArray[1].equalsIgnoreCase("m") && findWorkerByName(inputArray[0]).getTimer() == 0) 
				{
					findWorkerByName(inputArray[0]).setTimer(10);
					double total = 0;
					Supply[] shoppingList = new Supply[3];

					if (inputArray.length >= 3 && inputArray[2] != null && inputArray[2].length() >= 3 && Double.parseDouble(inputArray[2].split("-")[1]) >= 1) 
					{
						String[] parse = inputArray[2].split("-");
						Supply supply = new Supply();
						supply.setType(parse[0]);
						supply.setAmount(Double.parseDouble(parse[1]));
						shoppingList[0] = supply;
						total += shoppingList[0].getAmount();
					}

					if (inputArray.length >= 4 && inputArray[3] != null && inputArray[3].length() >= 3 && Double.parseDouble(inputArray[3].split("-")[1]) >= 1) 
					{
						String[] parse = inputArray[3].split("-");
						Supply supply = new Supply();
						supply.setType(parse[0]);
						supply.setAmount(Double.parseDouble(parse[1]));
						shoppingList[1] = supply;
						total += shoppingList[1].getAmount();
					}

					if (inputArray.length >= 5 && inputArray[4] != null && inputArray[4].length() >= 3 && Double.parseDouble(inputArray[2].split("-")[4]) >= 1) 
					{
						String[] parse = inputArray[4].split("-");
						Supply supply = new Supply();
						supply.setType(parse[0]);
						supply.setAmount(Double.parseDouble(parse[1]));
						shoppingList[2] = supply;
						total += shoppingList[2].getAmount();
					}
				
					if(inputArray.length < 4 && total > 100)
					{
						shoppingList[0].setAmount(100);
					}
					if(inputArray.length < 5 && (total > 100))
					{
						shoppingList[0].setAmount(round(100 * shoppingList[0].getAmount() / total));
						shoppingList[1].setAmount(round(100 * shoppingList[1].getAmount() / total));
					}
					if(inputArray.length < 6 && (total > 100))
					{
						shoppingList[0].setAmount(round(100 * shoppingList[0].getAmount() / total));
						shoppingList[1].setAmount(round(100 * shoppingList[1].getAmount() / total));
						shoppingList[2].setAmount(round(100 * shoppingList[2].getAmount() / total));
					}

					findWorkerByName(inputArray[0]).setSupply(shoppingList);
				}
			}

			if (inputArray.length == 1 && inputArray[0].substring(0, 2).equalsIgnoreCase("ap") && inputArray[0].length() == 4) 
			{
				if (inputArray[0].charAt(3) == '1') {
					if (CareCenter.applicationArray[0] != -1)
					{
						AddChild(child.getName(), child.getFood(), child.getGame(), child.getSleep(), child.getHygiene());
						CareCenter.applicationArray[0] = -1;
					}
				}

				if (inputArray[0].charAt(3) == '2') 
				{
					if (CareCenter.applicationArray[1] != -1) 
					{
						AddWorker(worker.createName());
						CareCenter.applicationArray[1] = -1;
					}
				}

				if (inputArray[0].charAt(3) == '3') 
				{
					if (CareCenter.applicationArray[2] != -1) 
					{
						AddCarer(carer.createName());
						CareCenter.applicationArray[2] = -1;
					}
				}

				ClearScreen();
				GetInformation();
			}
			if(inputArray[0].equalsIgnoreCase("help"))
			{
				isHelp = true;
			}
		}
	}

	
	public void AddWorker(String name) 
	{
		Worker worker = new Worker(name);

		for (int i = 0; i < CareCenter.workerArray.length; i++) 
		{
			if (CareCenter.workerArray[i] == null) 
			{
				CareCenter.workerArray[i] = worker;
				break;
			}
		}
		CareCenter.sizeOfWorker++;
	}
	
	public void AddChild(String name, double food, double game, double sleep, double hygiene) 
	{
		Child child = new Child(name, food, game, sleep, hygiene);

		for (int i = 0; i < CareCenter.childArray.length; i++) 
		{
			if (CareCenter.childArray[i] == null) 
			{
				CareCenter.childArray[i] = child;
				break;
			}
		}
		CareCenter.sizeOfChild++;
	}
	
	public void AddCarer(String name) 
	{
		Carer carer = new Carer(name);

		for (int i = 0; i < CareCenter.carerArray.length; i++) 
		{
			if (CareCenter.carerArray[i] == null) 
			{
				CareCenter.carerArray[i] = carer;
				break;
			}
		}
		CareCenter.sizeOfCarer++;
	}
	
	
	public void FindChild(Child child, Worker worker) 
	{
		worker.setChild(child);
		child.setWorker(worker);
		worker.setTimer(child.getMissing());
	}

	public void FindingProcess() 
	{
		for (int i = 0; i < CareCenter.childArray.length; i++) 
		{
			if (CareCenter.childArray[i] != null && CareCenter.childArray[i].getWorker() != null && CareCenter.childArray[i].getMissing() != 0) 
			{
				CareCenter.childArray[i].setMissing(CareCenter.childArray[i].getMissing() - 1);
				CareCenter.childArray[i].getWorker().setTimer(CareCenter.childArray[i].getMissing());
			}
		}
	}
	
	public void CheckChild(Child child, Carer carer) 
	{
		carer.setChild(child);
		child.setCarer(carer);
		carer.setTimer(2);
	}

	public void CheckScreen() 
	{
		for (int i = 0; i < CareCenter.childArray.length; i++) 
		{
			if (CareCenter.childArray[i] != null && CareCenter.childArray[i].getCarer() != null && CareCenter.childArray[i].getCarer().getTimer() != 0
					&& CareCenter.childArray[i].getCarer().getSupply().getType() == null) 
			{
				if (CareCenter.childArray[i].getMissing() == 0) 
				{
					Main.cn.getTextWindow().setCursorPosition(90, i * 7 + 7);
					Main.cn.getTextWindow().output(CareCenter.childArray[i].getName(), new TextAttributes(Color.orange));

					Main.cn.getTextWindow().setCursorPosition(90, i * 7 + 8);
					Main.cn.getTextWindow().output("Happy: " + CareCenter.childArray[i].getHappiness());

					Main.cn.getTextWindow().setCursorPosition(90, i * 7 + 9);
					Main.cn.getTextWindow().output("F: " + CareCenter.childArray[i].getFood());

					Main.cn.getTextWindow().setCursorPosition(90, i * 7 + 10);
					Main.cn.getTextWindow().output("G: " + CareCenter.childArray[i].getGame());

					Main.cn.getTextWindow().setCursorPosition(90, i * 7 + 11);
					Main.cn.getTextWindow().output("S: " + CareCenter.childArray[i].getSleep());

					Main.cn.getTextWindow().setCursorPosition(90, i * 7 + 12);
					Main.cn.getTextWindow().output("H: " + CareCenter.childArray[i].getHygiene());
				}

				else 
				{
					Main.cn.getTextWindow().setCursorPosition(90, i * 7 + 7);
					Main.cn.getTextWindow().output(CareCenter.childArray[i].getName(), new TextAttributes(Color.orange));

					Main.cn.getTextWindow().setCursorPosition(90, i * 7 + 8);
					Main.cn.getTextWindow().output("M: " + CareCenter.childArray[i].getMissing());
				}
			}
		}
	}
	
	public void Screen() 
	{
		Main.cn.getTextWindow().setCursorPosition(0, 0);
		Main.cn.getTextWindow().output("Day : ", new TextAttributes(Color.green));
		Main.cn.getTextWindow().output("" + day);

		Main.cn.getTextWindow().setCursorPosition(10, 0);
		Main.cn.getTextWindow().output("Turn : ", new TextAttributes(Color.green));
		Main.cn.getTextWindow().output("" + turn);
		
		Main.cn.getTextWindow().setCursorPosition(0, 6);
		Main.cn.getTextWindow().output("---Children---", new TextAttributes(Color.magenta));
		
		int a = 7;
		int b = 0;

		for (int i = 0; i < CareCenter.childArray.length; i++) 
		{
			if (CareCenter.childArray[i] != null) 
			{
				if (i != 0 && i % 4 == 0) 
				{
					a += 6;
					b = 0;
				}
				
				if (CareCenter.childArray[i].getCarer() != null) 
				{
					if (CareCenter.childArray[i].getCarer().getSupply().getType() == null && CareCenter.childArray[i].getMissing() != 0) 
					{
						Main.cn.getTextWindow().setCursorPosition(b * 17, a);
						Main.cn.getTextWindow().output(CareCenter.childArray[i].getName(), new TextAttributes(Color.orange));
						Main.cn.getTextWindow().output(CareCenter.childArray[i].getName() + " M:" + CareCenter.childArray[i].getMissing());
						Main.cn.getTextWindow().setCursorPosition(b * 17, a + 1);
						Main.cn.getTextWindow().output(CareCenter.childArray[i].getCarer().getName() + " C");
					}
					
					if (CareCenter.childArray[i].getCarer().getSupply().getType() == null && CareCenter.childArray[i].getMissing() == 0) 
					{
						Main.cn.getTextWindow().setCursorPosition(b * 17, a);
						Main.cn.getTextWindow().output(CareCenter.childArray[i].getName(), new TextAttributes(Color.orange));
						Main.cn.getTextWindow().setCursorPosition(b * 17, a + 1);
						Main.cn.getTextWindow().output(CareCenter.childArray[i].getCarer().getName() + " C");
						Main.cn.getTextWindow().setCursorPosition(b * 17, a + 2);
						Main.cn.getTextWindow().output("S:" + round(CareCenter.childArray[i].getSleep()) + " F:" + round(CareCenter.childArray[i].getFood()));
						Main.cn.getTextWindow().setCursorPosition(b * 17, a + 3);
						Main.cn.getTextWindow().output("H:" + round(CareCenter.childArray[i].getHygiene()) + " G:" + round(CareCenter.childArray[i].getGame()));
					}
					
					if (CareCenter.childArray[i].getCarer().getSupply().getType() != null) 
					{
						Main.cn.getTextWindow().setCursorPosition(b * 17, a);
						Main.cn.getTextWindow().output(CareCenter.childArray[i].getName(), new TextAttributes(Color.orange));
						if (CareCenter.childArray[i].getCarer().getSupply().getType().equalsIgnoreCase("f")) 
						{
							Main.cn.getTextWindow().setCursorPosition(b * 17, a);
							Main.cn.getTextWindow().output(CareCenter.childArray[i].getName(), new TextAttributes(Color.orange));
							Main.cn.getTextWindow().output(" " + CareCenter.childArray[i].getCarer().getSupply().getType().toUpperCase()
									+ ":" + round(CareCenter.childArray[i].getFood()));
							
							Main.cn.getTextWindow().setCursorPosition(90, i * 7 + 7);
							Main.cn.getTextWindow().output(CareCenter.childArray[i].getName(), new TextAttributes(Color.orange));

							Main.cn.getTextWindow().setCursorPosition(90, i * 7 + 8);
							Main.cn.getTextWindow().output("Happy: " + CareCenter.childArray[i].getHappiness());

							Main.cn.getTextWindow().setCursorPosition(90, i * 7 + 9);
							Main.cn.getTextWindow().output("F: " + CareCenter.childArray[i].getFood());
						} 
						
						else if (CareCenter.childArray[i].getCarer().getSupply().getType().equalsIgnoreCase("g")) 
						{
							Main.cn.getTextWindow().setCursorPosition(b * 17, a);
							Main.cn.getTextWindow().output(CareCenter.childArray[i].getName(), new TextAttributes(Color.orange));
							Main.cn.getTextWindow().output(" " + CareCenter.childArray[i].getCarer().getSupply().getType().toUpperCase()
									+ ":" + round(CareCenter.childArray[i].getGame()));
							
							Main.cn.getTextWindow().setCursorPosition(90, i * 7 + 7);
							Main.cn.getTextWindow().output(CareCenter.childArray[i].getName(), new TextAttributes(Color.orange));

							Main.cn.getTextWindow().setCursorPosition(90, i * 7 + 8);
							Main.cn.getTextWindow().output("Happy: " + CareCenter.childArray[i].getHappiness());

							Main.cn.getTextWindow().setCursorPosition(90, i * 7 + 9);
							Main.cn.getTextWindow().output("G: " + CareCenter.childArray[i].getGame());
						}
						
						else if (CareCenter.childArray[i].getCarer().getSupply().getType().equalsIgnoreCase("h")) 
						{
							Main.cn.getTextWindow().setCursorPosition(b * 17, a);
							Main.cn.getTextWindow().output(CareCenter.childArray[i].getName(), new TextAttributes(Color.orange));
							Main.cn.getTextWindow().output(" " + CareCenter.childArray[i].getCarer().getSupply().getType().toUpperCase()
											+ ":" + round(CareCenter.childArray[i].getHygiene()));
							
							Main.cn.getTextWindow().setCursorPosition(90, i * 7 + 7);
							Main.cn.getTextWindow().output(CareCenter.childArray[i].getName(), new TextAttributes(Color.orange));

							Main.cn.getTextWindow().setCursorPosition(90, i * 7 + 8);
							Main.cn.getTextWindow().output("Happy: " + CareCenter.childArray[i].getHappiness());

							Main.cn.getTextWindow().setCursorPosition(90, i * 7 + 9);
							Main.cn.getTextWindow().output("H: " + CareCenter.childArray[i].getHygiene());
						} 
						
						else if (CareCenter.childArray[i].getCarer().getSupply().getType().equalsIgnoreCase("s")) 
						{
							Main.cn.getTextWindow().setCursorPosition(b * 17, a);
							Main.cn.getTextWindow().output(CareCenter.childArray[i].getName(), new TextAttributes(Color.orange));
							Main.cn.getTextWindow().output(" " + CareCenter.childArray[i].getCarer().getSupply().getType().toUpperCase()
											+ ":" + round(CareCenter.childArray[i].getSleep()));
							
							Main.cn.getTextWindow().setCursorPosition(90, i * 7 + 7);
							Main.cn.getTextWindow().output(CareCenter.childArray[i].getName(), new TextAttributes(Color.orange));

							Main.cn.getTextWindow().setCursorPosition(90, i * 7 + 8);
							Main.cn.getTextWindow().output("Happy: " + CareCenter.childArray[i].getHappiness());

							Main.cn.getTextWindow().setCursorPosition(90, i * 7 + 9);
							Main.cn.getTextWindow().output("S: " + CareCenter.childArray[i].getSleep());
						}

						Main.cn.getTextWindow().setCursorPosition(b * 17, a + 1);
						Main.cn.getTextWindow().output(CareCenter.childArray[i].getCarer().getName() + " " + CareCenter.childArray[i].getCarer().getSupply().getType().toUpperCase() + ":"
										+ round(CareCenter.childArray[i].getCarer().getSupply().getAmount()));
					}
				} 
				
				else if (CareCenter.childArray[i].getWorker() != null && CareCenter.childArray[i].getMissing() != 0) 
				{
					Main.cn.getTextWindow().setCursorPosition(b * 17, a);
					Main.cn.getTextWindow().output(CareCenter.childArray[i].getName(), new TextAttributes(Color.orange));
					Main.cn.getTextWindow().output(" M:" + CareCenter.childArray[i].getMissing());
					Main.cn.getTextWindow().setCursorPosition(b * 17, a + 1);
					Main.cn.getTextWindow().output(CareCenter.childArray[i].getWorker().getName());
					
					Main.cn.getTextWindow().setCursorPosition(90, i * 7 + 7);
					Main.cn.getTextWindow().output(CareCenter.childArray[i].getName(), new TextAttributes(Color.orange));

					Main.cn.getTextWindow().setCursorPosition(90, i * 7 + 9);
					Main.cn.getTextWindow().output("M: " + CareCenter.childArray[i].getMissing());
				} 
				
				else 
				{
					Main.cn.getTextWindow().setCursorPosition(b * 17, a);
					Main.cn.getTextWindow().output(CareCenter.childArray[i].getName());
				}
				b++;				
			}	
		}

		Main.cn.getTextWindow().setCursorPosition(0, a + 6);
		Main.cn.getTextWindow().output("---Carers---", new TextAttributes(Color.magenta));

		for (int i = 0; i < CareCenter.carerArray.length; i++) 
		{
			if (CareCenter.carerArray[i] != null && CareCenter.carerArray[i].getChild() != null && CareCenter.carerArray[i].getTimer() != 0 && CareCenter.carerArray[i].getSupply().getType() != null) 
			{
				Main.cn.getTextWindow().setCursorPosition(0, a + 7);
				Main.cn.getTextWindow().output(CareCenter.carerArray[i].getName(), new TextAttributes(Color.orange));
				Main.cn.getTextWindow().output(" (" + CareCenter.carerArray[i].getChild().getName() + " "
								+ CareCenter.carerArray[i].getSupply().getType().toUpperCase() + ":" + CareCenter.carerArray[i].getSupply().getAmount() + ")   - F:"
								+ CareCenter.carerArray[i].getMeetingFoodRate() + "   G:" + CareCenter.carerArray[i].getMeetingGameRate() + "   S:"
								+ CareCenter.carerArray[i].getMeetingSleepRate() + "   H:" + CareCenter.carerArray[i].getMeetingHygieneRate());
				a++;
			} 
			else if (CareCenter.carerArray[i] != null && CareCenter.carerArray[i].getChild() != null && CareCenter.carerArray[i].getTimer() != 0 && CareCenter.carerArray[i].getSupply().getType() == null) 
			{
				Main.cn.getTextWindow().setCursorPosition(0, a + 7);
				Main.cn.getTextWindow().output(CareCenter.carerArray[i].getName(), new TextAttributes(Color.orange));
				Main.cn.getTextWindow().output(" (" + CareCenter.carerArray[i].getChild().getName() + " " + ")   - F:"
								+ CareCenter.carerArray[i].getMeetingFoodRate() + "   G:" + CareCenter.carerArray[i].getMeetingGameRate() + "   S:"
								+ CareCenter.carerArray[i].getMeetingSleepRate() + "   H:" + CareCenter.carerArray[i].getMeetingHygieneRate());
				a++;
			}
			else if(CareCenter.carerArray[i] != null && CareCenter.carerArray[i].getChild() == null && CareCenter.carerArray[i].getTimer() == 0 /*&& CareCenter.carerArray[i].getSupply().getType() == null*/)
			{
				Main.cn.getTextWindow().setCursorPosition(0, a + 7);
				Main.cn.getTextWindow().output("" + CareCenter.carerArray[i].getName());
				a++;
			}
		}
		
		Main.cn.getTextWindow().setCursorPosition(0, a + 10);
		Main.cn.getTextWindow().output("---Workers---", new TextAttributes(Color.magenta));

		for (int i = 0; i < CareCenter.workerArray.length; i++) 
		{
			if (CareCenter.workerArray[i] != null && CareCenter.workerArray[i].getTimer() != 0 && CareCenter.workerArray[i].getChild() == null) 
			{
				int count = 0;
				Main.cn.getTextWindow().setCursorPosition(0, a + 11);
				for (int j = 0; j < 3; j++) 
				{
					if (CareCenter.workerArray[i].getSupply()[j] != null) 
					{
						count+=j+1;
					}
				}
				if(count == 6)
				{
					Main.cn.getTextWindow().output(CareCenter.workerArray[i].getName(), new TextAttributes(Color.orange));
					Main.cn.getTextWindow().output(" (Market - " + CareCenter.workerArray[i].getSupply()[0].getType().toUpperCase() + ":"
							+ CareCenter.workerArray[i].getSupply()[0].getAmount() + ", " + CareCenter.workerArray[i].getSupply()[1].getType().toUpperCase() + ":"
							+ CareCenter.workerArray[i].getSupply()[1].getAmount() + ", " + CareCenter.workerArray[i].getSupply()[2].getType().toUpperCase() + ":"
							+ CareCenter.workerArray[i].getSupply()[2].getAmount() + ")");
				}
					
				else if(count == 3)
				{
					Main.cn.getTextWindow().output(CareCenter.workerArray[i].getName(), new TextAttributes(Color.orange));
					Main.cn.getTextWindow().output(" (Market - " + CareCenter.workerArray[i].getSupply()[0].getType().toUpperCase() + ":"
							+ CareCenter.workerArray[i].getSupply()[0].getAmount() + ", " + CareCenter.workerArray[i].getSupply()[1].getType().toUpperCase() + ":"
							+ CareCenter.workerArray[i].getSupply()[1].getAmount() + ")");
				}
					
				else if(count == 4)
				{
					Main.cn.getTextWindow().output(CareCenter.workerArray[i].getName(), new TextAttributes(Color.orange));
					Main.cn.getTextWindow().output(" (Market - " + CareCenter.workerArray[i].getSupply()[0].getType().toUpperCase() + ":"
							+ CareCenter.workerArray[i].getSupply()[0].getAmount() + ", " + CareCenter.workerArray[i].getSupply()[2].getType().toUpperCase() + ":"
							+ CareCenter.workerArray[i].getSupply()[2].getAmount() + ")");
				}
					
				else if(count == 5)
				{
					Main.cn.getTextWindow().output(CareCenter.workerArray[i].getName(), new TextAttributes(Color.orange));
					Main.cn.getTextWindow().output(" (Market - " + CareCenter.workerArray[i].getSupply()[1].getType().toUpperCase() + ":"
							+ CareCenter.workerArray[i].getSupply()[1].getAmount() + ", " + CareCenter.workerArray[i].getSupply()[2].getType().toUpperCase() + ":"
							+ CareCenter.workerArray[i].getSupply()[2].getAmount() + ")");
				}
					
				else
				{
					for(int j = 0; j < 3; j++)
					{
						if(CareCenter.workerArray[i].getSupply()[j] != null)
						{
							Main.cn.getTextWindow().output(CareCenter.workerArray[i].getName(), new TextAttributes(Color.orange));
							Main.cn.getTextWindow().output(" (Market - " + CareCenter.workerArray[i].getSupply()[j].getType().toUpperCase() + ":"
									+ CareCenter.workerArray[i].getSupply()[j].getAmount() + ")");
						}	
					}
				}
				a++;
			}		
			else if (CareCenter.workerArray[i] != null && CareCenter.workerArray[i].getChild() != null) 
			{
				Main.cn.getTextWindow().setCursorPosition(0, a + 11);
				Main.cn.getTextWindow().output(CareCenter.workerArray[i].getName(), new TextAttributes(Color.orange));
				Main.cn.getTextWindow().output("(Missing " + CareCenter.workerArray[i].getChild().getName() + ")");
				a++;
			}	
			else if (CareCenter.workerArray[i] != null && CareCenter.workerArray[i].getChild() == null && CareCenter.workerArray[i].getTimer() == 0 && CareCenter.workerArray[i].getSupply() == null)
			{
				Main.cn.getTextWindow().setCursorPosition(0, a + 11);
				Main.cn.getTextWindow().output("" + CareCenter.workerArray[i].getName());
				a++;
			}
		}
	}
	
	public void PauseScreen() 
	{
		Main.cn.getTextWindow().setCursorPosition(0, 5);
		Main.cn.getTextWindow().output("---Children---", new TextAttributes(Color.magenta));
		int a = 6;
		int b = 0;

		for (int i = 0; i < CareCenter.childArray.length; i++) 
		{
			if (CareCenter.childArray[i] != null) 
			{
				if (i != 0 && i % 3 == 0) 
				{
					a += 6;
					b = 0;
				}
				Main.cn.getTextWindow().setCursorPosition(b * 17, a);
				Main.cn.getTextWindow().output(CareCenter.childArray[i].getName(), new TextAttributes(Color.orange));
				Main.cn.getTextWindow().output(" M:" + CareCenter.childArray[i].getMissing()
								+ " Happiness:" + CareCenter.childArray[i].getHappiness());
				Main.cn.getTextWindow().setCursorPosition(b * 17, a + 1);
				Main.cn.getTextWindow().output("F:" + round(CareCenter.childArray[i].getFood()) + "  FoodDecreasing:"
						+ round(CareCenter.childArray[i].getFoodDecreasingRate()));
				Main.cn.getTextWindow().setCursorPosition(b * 17, a + 2);
				Main.cn.getTextWindow().output("G:" + round(CareCenter.childArray[i].getGame()) + "  GameDecreasing:"
						+ round(CareCenter.childArray[i].getGameDecreasingRate()));
				Main.cn.getTextWindow().setCursorPosition(b * 17, a + 3);
				Main.cn.getTextWindow().output("H:" + round(CareCenter.childArray[i].getHygiene())
						+ "  HygieneDecreasing:" + round(CareCenter.childArray[i].getHygieneDecreasingRate()));
				Main.cn.getTextWindow().setCursorPosition(b * 17, a + 4);
				Main.cn.getTextWindow().output("S:" + round(CareCenter.childArray[i].getSleep()) + "  SleepDecreasing:"
						+ round(CareCenter.childArray[i].getSleepDecreasingRate()));
				b += 2;
			}
		}
		
		Main.cn.getTextWindow().setCursorPosition(0, a + 6);
		Main.cn.getTextWindow().output("---Carers---", new TextAttributes(Color.magenta));
		
		for (int i = 0; i < CareCenter.carerArray.length; i++) 
		{
			if (CareCenter.carerArray[i] != null) 
			{
				if (CareCenter.carerArray[i].getChild() != null) 
				{
					Main.cn.getTextWindow().setCursorPosition(0, a + 7);
					Main.cn.getTextWindow().output(CareCenter.carerArray[i].getName(), new TextAttributes(Color.orange));
					Main.cn.getTextWindow().output(" Child:" + CareCenter.carerArray[i].getChild().getName());
				} 
				else if (CareCenter.carerArray[i].getChild() == null) 
				{
					Main.cn.getTextWindow().setCursorPosition(0, a + 7);
					Main.cn.getTextWindow().output(CareCenter.carerArray[i].getName(), new TextAttributes(Color.orange));
				}
				Main.cn.getTextWindow().setCursorPosition(0, a + 8);
				Main.cn.getTextWindow().output("FoodMeeting:" + round(CareCenter.carerArray[i].getMeetingFoodRate())
						+ "    GameMeeting:" + round(CareCenter.carerArray[i].getMeetingGameRate()));
				Main.cn.getTextWindow().setCursorPosition(0, a + 9);
				Main.cn.getTextWindow().output("HygieneMeeting:" + round(CareCenter.carerArray[i].getMeetingHygieneRate())
								+ " SleepMeeting:" + round(CareCenter.carerArray[i].getMeetingSleepRate()));

				a+=4;
			}
		}

		Main.cn.getTextWindow().setCursorPosition(0, a + 7);
		Main.cn.getTextWindow().output("---Workers---", new TextAttributes(Color.magenta));
		
		for (int i = 0; i < CareCenter.workerArray.length; i++) 
		{
			if (CareCenter.workerArray[i] != null) 
			{
				if (CareCenter.workerArray[i].getChild() != null) 
				{
					Main.cn.getTextWindow().setCursorPosition(0, a + 8);
					Main.cn.getTextWindow().output(CareCenter.workerArray[i].getName(), new TextAttributes(Color.orange));
					Main.cn.getTextWindow().output(" Child:" + CareCenter.workerArray[i].getChild().getName());
				} 
				else if (CareCenter.workerArray[i].getChild() == null) 
				{
					Main.cn.getTextWindow().setCursorPosition(0, a + 8);
					Main.cn.getTextWindow().output(CareCenter.workerArray[i].getName());
					
					if (CareCenter.workerArray[i].getSupply() != null) 
					{
						int count = 0;
						for (int j = 0; j < 3; j++) 
						{
							if (CareCenter.workerArray[i].getSupply()[j] != null) 
							{
								count+=j+1;
							}
						}
						if(count == 6)
						{
							Main.cn.getTextWindow().output(CareCenter.workerArray[i].getName(), new TextAttributes(Color.orange));
							Main.cn.getTextWindow().output(" (Market - " + CareCenter.workerArray[i].getSupply()[0].getType().toUpperCase() + ":"
									+ CareCenter.workerArray[i].getSupply()[0].getAmount() + ", " + CareCenter.workerArray[i].getSupply()[1].getType().toUpperCase() + ":"
									+ CareCenter.workerArray[i].getSupply()[1].getAmount() + ", " + CareCenter.workerArray[i].getSupply()[2].getType().toUpperCase() + ":"
									+ CareCenter.workerArray[i].getSupply()[2].getAmount() + ")");
						}
							
						else if(count == 3)
						{
							Main.cn.getTextWindow().output(CareCenter.workerArray[i].getName(), new TextAttributes(Color.orange));
							Main.cn.getTextWindow().output(" (Market - " + CareCenter.workerArray[i].getSupply()[0].getType().toUpperCase() + ":"
									+ CareCenter.workerArray[i].getSupply()[0].getAmount() + ", " + CareCenter.workerArray[i].getSupply()[1].getType().toUpperCase() + ":"
									+ CareCenter.workerArray[i].getSupply()[1].getAmount() + ")");
						}
							
						else if(count == 4)
						{
							Main.cn.getTextWindow().output(CareCenter.workerArray[i].getName(), new TextAttributes(Color.orange));
							Main.cn.getTextWindow().output(" (Market - " + CareCenter.workerArray[i].getSupply()[0].getType().toUpperCase() + ":"
									+ CareCenter.workerArray[i].getSupply()[0].getAmount() + ", " + CareCenter.workerArray[i].getSupply()[2].getType().toUpperCase() + ":"
									+ CareCenter.workerArray[i].getSupply()[2].getAmount() + ")");
						}
							
						else if(count == 5)
						{
							Main.cn.getTextWindow().output(CareCenter.workerArray[i].getName(), new TextAttributes(Color.orange));
							Main.cn.getTextWindow().output(" (Market - " + CareCenter.workerArray[i].getSupply()[1].getType().toUpperCase() + ":"
									+ CareCenter.workerArray[i].getSupply()[1].getAmount() + ", " + CareCenter.workerArray[i].getSupply()[2].getType().toUpperCase() + ":"
									+ CareCenter.workerArray[i].getSupply()[2].getAmount() + ")");
						}
							
						else
						{
							for(int j = 0; j < 3; j++)
							{
								if(CareCenter.workerArray[i].getSupply()[j] != null)
								{
									Main.cn.getTextWindow().output(CareCenter.workerArray[i].getName(), new TextAttributes(Color.orange));
									Main.cn.getTextWindow().output(CareCenter.workerArray[i].getName() + " (Market - " + CareCenter.workerArray[i].getSupply()[j].getType().toUpperCase() + ":"
											+ CareCenter.workerArray[i].getSupply()[j].getAmount() + ")");
								}
							}
						}
					}
				}
				a++;
			}
		}
	}

	public void HelpScreen()
	{
		Main.cn.getTextWindow().setCursorPosition(50, 5);
		Main.cn.getTextWindow().output("---HELP---", new TextAttributes(Color.magenta));
		Main.cn.getTextWindow().setCursorPosition(20, 8);
		Main.cn.getTextWindow().output("Command > cr02 f ch05 23		 // carer 02: 23 food for child 05");
		Main.cn.getTextWindow().setCursorPosition(20, 9);
		Main.cn.getTextWindow().output("Command > cr0c f ch02			// carer 03: 23 check child 02");
		Main.cn.getTextWindow().setCursorPosition(20, 10);
		Main.cn.getTextWindow().output("Command > cr07 s ch15			// carer 07: sleep child 15 for unlimited time");
		Main.cn.getTextWindow().setCursorPosition(20, 11);
		Main.cn.getTextWindow().output("Command > cr07 s ch15			// carer 07: sleep child 15 for 20 turns");
		Main.cn.getTextWindow().setCursorPosition(20, 12);
		Main.cn.getTextWindow().output("Command > cr07 r				 // carer 07: break the job and return");
		Main.cn.getTextWindow().setCursorPosition(20, 13);
		Main.cn.getTextWindow().output("Command > wr04 m f-55 g-45	   // worker 04: go to market for 55 food, 45 game");
		Main.cn.getTextWindow().setCursorPosition(20, 14);
		Main.cn.getTextWindow().output("Command > wr02 c ch16			// worker 02: find child 16");
		Main.cn.getTextWindow().setCursorPosition(20, 15);
		Main.cn.getTextWindow().output("Command > ap01				   // accept application 01");
		Main.cn.getTextWindow().setCursorPosition(20, 16);
		Main.cn.getTextWindow().output("Command > wr02 t				 // terminate contract for worker");
	}
	
		
	public void CarerBreakJob(Carer carer) 
	{
		findChildByName(carer.getChild().getName()).setCarer(null);
		carer.setChild(null);

		carer.getSupply().setType(null);
		carer.getSupply().setAmount(0);
		carer.setTimer(0);
	}
	
	public void WorkerBreakJob(Worker worker) 
	{
		findChildByName(worker.getChild().getName()).setWorker(null);
		worker.setChild(null);

		for (int i = 0; i < 3; i++) // 3 = supply size
		{
			if (worker.getSupply() != null && worker.getSupply()[i] != null) 
			{
				worker.getSupply()[i].setType(null);
				worker.getSupply()[i].setAmount(0);
			}
		}
		worker.setTimer(0);
	}
	
	public void WorkerTerminate(Worker worker) 
	{
		if(worker.getChild() != null)
		{
			findChildByName(worker.getChild().getName()).setWorker(null);
			worker.setChild(null);
		}
		
		carecenter.setCredit(round(carecenter.getCredit() - worker.getWage()));

		for (int i = 0; i < 3; i++) 
		{
			if (worker.getSupply() != null && worker.getSupply()[i] != null) 
			{
				worker.getSupply()[i].setType(null);
				worker.getSupply()[i].setAmount(0);
			}
		}

		worker.setTimer(0);

		for (int i = 0; i < CareCenter.workerArray.length; i++) 
		{
			if (CareCenter.workerArray[i] != null && CareCenter.workerArray[i] == worker) 
			{
				CareCenter.workerArray[i] = null;
				CareCenter.sizeOfWorker--;
				break;
			}

		}
	}
	
	public void CarerTerminate(Carer carer) 
	{
		if (carer.getChild() != null) 
		{
			findChildByName(carer.getChild().getName()).setCarer(null);
			carer.setChild(null);
		}

		carecenter.setCredit(round(carecenter.getCredit() - carer.getWage()));

		for (int i = 0; i < CareCenter.carerArray.length; i++) 
		{
			if (CareCenter.carerArray[i] != null && CareCenter.carerArray[i] == carer) 
			{
				CareCenter.carerArray[i] = null;
				CareCenter.sizeOfCarer--;
				break;
			}
		}
	}
	
	
	public Carer findCarerByName(String name) 
	{
		for (int i = 0; i < CareCenter.carerArray.length; i++) 
		{
			if (CareCenter.carerArray[i] != null && name.equalsIgnoreCase(CareCenter.carerArray[i].getName()))
			{
				return CareCenter.carerArray[i];
			}
		}

		return null;
	}
	
	public Child findChildByName(String name) 
	{
		for (int i = 0; i < CareCenter.childArray.length; i++) 
		{
			if (CareCenter.childArray[i] != null && name.equalsIgnoreCase(CareCenter.childArray[i].getName())) 
			{
				return CareCenter.childArray[i];
			}
		}

		return null;
	}
	
	public Worker findWorkerByName(String name) 
	{
		for (int i = 0; i < CareCenter.workerArray.length; i++) 
		{
			if (CareCenter.workerArray[i] != null && name.equalsIgnoreCase(CareCenter.workerArray[i].getName())) 
			{
				return CareCenter.workerArray[i];
			}
		}

		return null;
	}
	
		
	public void calculateAverageHappiness() 
	{
		double sum = 0;

		for (int i = 0; i < CareCenter.childArray.length; i++) 
		{
			if (CareCenter.childArray[i] != null)
				sum += CareCenter.childArray[i].getHappiness();
			happiness = sum;
		}
		carecenter.setAvgHappiness(round(sum / CareCenter.sizeOfChild));	
	}
	
	public double creditforUnit(double price, double value) 
	{
		return round((price / 10) * value);
	}
	
	public void calculateScore() 
	{
		carecenter.setScore(round(CareCenter.sizeOfChild * (carecenter.getAvgHappiness() - 50)));
	}

	public void CalculateHappinessRate() 
	{
		for (int i = 0; i < CareCenter.childArray.length; i++) 
		{
			if (CareCenter.childArray[i] != null) 
			{
				if (25 <= CareCenter.childArray[i].getFood() && CareCenter.childArray[i].getFood() <= 75) 
				{
					CareCenter.childArray[i].setHappiness(round(CareCenter.childArray[i].getHappiness() + CareCenter.childArray[i].getHappinessRateFood()));
				}

				if (CareCenter.childArray[i].getFood() < 25) 
				{
					CareCenter.childArray[i].setHappiness(round(CareCenter.childArray[i].getHappiness() - 4 * CareCenter.childArray[i].getHappinessRateFood()));
				}

				if (CareCenter.childArray[i].getFood() > 75) 
				{
					CareCenter.childArray[i].setHappiness(round(CareCenter.childArray[i].getHappiness() - 2 * CareCenter.childArray[i].getHappinessRateFood()));
				}

				if (25 <= CareCenter.childArray[i].getGame() && CareCenter.childArray[i].getGame() <= 75) 
				{
					CareCenter.childArray[i].setHappiness(round(CareCenter.childArray[i].getHappiness() + CareCenter.childArray[i].getHappinessRateGame()));
				}

				if (CareCenter.childArray[i].getGame() < 25) 
				{
					CareCenter.childArray[i].setHappiness(round(CareCenter.childArray[i].getHappiness() - 4 * CareCenter.childArray[i].getHappinessRateGame()));
				}

				if (CareCenter.childArray[i].getGame() > 75) 
				{
					CareCenter.childArray[i].setHappiness(round(CareCenter.childArray[i].getHappiness() - 2 * CareCenter.childArray[i].getHappinessRateGame()));
				}

				if (25 <= CareCenter.childArray[i].getSleep() && CareCenter.childArray[i].getSleep() <= 75) 
				{
					CareCenter.childArray[i].setHappiness(round(CareCenter.childArray[i].getHappiness() + CareCenter.childArray[i].getHappinessRateSleep()));
				}

				if (CareCenter.childArray[i].getSleep() < 25) 
				{
					CareCenter.childArray[i].setHappiness(round(CareCenter.childArray[i].getHappiness() - 4 * CareCenter.childArray[i].getHappinessRateSleep()));
				}

				if (CareCenter.childArray[i].getSleep() > 75) 
				{
					CareCenter.childArray[i].setHappiness(round(CareCenter.childArray[i].getHappiness() - 2 * CareCenter.childArray[i].getHappinessRateSleep()));
				}

				if (25 <= CareCenter.childArray[i].getHygiene() && CareCenter.childArray[i].getHygiene() <= 75) 
				{
					CareCenter.childArray[i].setHappiness(round(CareCenter.childArray[i].getHappiness() + CareCenter.childArray[i].getHappinessRateHygiene()));
				}

				if (CareCenter.childArray[i].getHygiene() < 25) 
				{
					CareCenter.childArray[i].setHappiness(round(CareCenter.childArray[i].getHappiness() - 4 * CareCenter.childArray[i].getHappinessRateHygiene()));
				}

				if (CareCenter.childArray[i].getHygiene() > 75) 
				{
					CareCenter.childArray[i].setHappiness(round(CareCenter.childArray[i].getHappiness() - 2 * CareCenter.childArray[i].getHappinessRateHygiene()));
				}
			}
		}
	}
	
	public void CalculateCredit() 
	{
		double credit = round(carecenter.getAvgHappiness() / (1 + (carecenter.getAvgHappiness() - 50) / 50));
		carecenter.setCredit(credit);
	}
	
	
	public void GetInformation() 
	{
		Main.cn.getTextWindow().setCursorPosition(0, 0);
		Main.cn.getTextWindow().output("Day : ", new TextAttributes(Color.green));
		Main.cn.getTextWindow().output("" + day);

		Main.cn.getTextWindow().setCursorPosition(10, 0);
		Main.cn.getTextWindow().output("Turn : ", new TextAttributes(Color.green));
		Main.cn.getTextWindow().output("" + ++turn);

		if (turn % 50 == 0) 
		{
			turn = 0;

			carecenter.setPreviousDayHappiness(carecenter.getAvgHappiness());

			Main.cn.getTextWindow().setCursorPosition(0, 0);
			Main.cn.getTextWindow().output("Day : ", new TextAttributes(Color.green));
			Main.cn.getTextWindow().output("" + ++day);

			PayWageforEmployee();
			ChildLeaveCenter();
			ClearTile();
		}

		Main.cn.getTextWindow().setCursorPosition(22, 0);
		Main.cn.getTextWindow().output("Current Avg. Happiness : ", new TextAttributes(Color.green));
		Main.cn.getTextWindow().output("" + carecenter.getAvgHappiness());

		Main.cn.getTextWindow().setCursorPosition(56, 0);
		Main.cn.getTextWindow().output("Credit : ", new TextAttributes(Color.green));
		Main.cn.getTextWindow().output("" + carecenter.getCredit());

		Main.cn.getTextWindow().setCursorPosition(75, 0);
		Main.cn.getTextWindow().output("Score : ", new TextAttributes(Color.green));
		Main.cn.getTextWindow().output("" + carecenter.getScore());

		Main.cn.getTextWindow().setCursorPosition(90, 1);
		Main.cn.getTextWindow().output("---Supplies--- ", new TextAttributes(Color.cyan));
		Main.cn.getTextWindow().setCursorPosition(90, 2);
		Main.cn.getTextWindow().output("Food     : " + carecenter.getFoodStock());
		Main.cn.getTextWindow().setCursorPosition(90, 3);
		Main.cn.getTextWindow().output("Toy      : " + carecenter.getGameStock());
		Main.cn.getTextWindow().setCursorPosition(90, 4);
		Main.cn.getTextWindow().output("Hygiene  : " + carecenter.getHygieneStock());
	}
	
	public void GetPauseInformation() 
	{
		Main.cn.getTextWindow().setCursorPosition(0, 0);
		Main.cn.getTextWindow().output("Day : ", new TextAttributes(Color.green));
		Main.cn.getTextWindow().output("" + day);

		Main.cn.getTextWindow().setCursorPosition(10, 0);
		Main.cn.getTextWindow().output("Turn : ", new TextAttributes(Color.green));
		Main.cn.getTextWindow().output("" + turn);

		Main.cn.getTextWindow().setCursorPosition(22, 0);
		Main.cn.getTextWindow().output("Current Avg. Happiness : ", new TextAttributes(Color.green));
		Main.cn.getTextWindow().output("" + carecenter.getAvgHappiness());

		Main.cn.getTextWindow().setCursorPosition(54, 0);
		Main.cn.getTextWindow().output("Credit : ", new TextAttributes(Color.green));
		Main.cn.getTextWindow().output("" + carecenter.getCredit());

		Main.cn.getTextWindow().setCursorPosition(73, 0);
		Main.cn.getTextWindow().output("Score : ", new TextAttributes(Color.green));
		Main.cn.getTextWindow().output("" + carecenter.getScore());

		Main.cn.getTextWindow().setCursorPosition(90, 1);
		Main.cn.getTextWindow().output("---Supplies--- ", new TextAttributes(Color.cyan));
		Main.cn.getTextWindow().setCursorPosition(90, 2);
		Main.cn.getTextWindow().output("Food     : " + carecenter.getFoodStock());
		Main.cn.getTextWindow().setCursorPosition(90, 3);
		Main.cn.getTextWindow().output("Toy      : " + carecenter.getGameStock());
		Main.cn.getTextWindow().setCursorPosition(90, 4);
		Main.cn.getTextWindow().output("Hygiene  : " + carecenter.getHygieneStock());

	}
		
	public void PayWageforEmployee() 
	{
		carecenter.setCredit(round(carecenter.getCredit() - (50 * CareCenter.sizeOfCarer + 30 * CareCenter.sizeOfWorker)));
	}
	
	
	public void DecreaseTimerforCarer() 
	{
		for (int i = 0; i < CareCenter.carerArray.length; i++) 
		{
			if(CareCenter.carerArray[i] != null)
			{
				if (CareCenter.carerArray[i].getTimer() != 0 && CareCenter.carerArray[i].getChild() != null) 
				{
					CareCenter.carerArray[i].setTimer(CareCenter.carerArray[i].getTimer() - 1);

					if (CareCenter.carerArray[i].getTimer() == 0) 
					{
						CareCenter.carerArray[i].getChild().setCarer(null);
						CareCenter.carerArray[i].setChild(null);
					}
				}

				else if (CareCenter.carerArray[i].getTimer() != 0 && CareCenter.carerArray[i].getChild() != null
						&& CareCenter.carerArray[i].getSupply() == null) 
				{
					CareCenter.carerArray[i].setTimer(CareCenter.carerArray[i].getTimer() - 1);

					if (CareCenter.carerArray[i].getTimer() == 0) 
					{
						CareCenter.carerArray[i].getChild().setCarer(null);
						CareCenter.carerArray[i].setChild(null);
					}
				}
			}	
		}
	}
	
	public void DecreaseTimerforWorker() 
	{
		for (int i = 0; i < CareCenter.workerArray.length; i++) 
		{
			if(CareCenter.workerArray[i] != null)
			{
				if (CareCenter.workerArray[i].getTimer() != 0 && CareCenter.workerArray[i].getChild() != null) 
				{
					CareCenter.workerArray[i].setTimer(CareCenter.workerArray[i].getTimer() - 1);
					CareCenter.workerArray[i].getChild().setMissing(CareCenter.workerArray[i].getChild().getMissing() - 1);
				}

				if (CareCenter.workerArray[i].getTimer() == 0 && CareCenter.workerArray[i].getChild() != null) 
				{
					CareCenter.workerArray[i].getChild().setWorker(null);
					CareCenter.workerArray[i].setChild(null);
				}

				if (CareCenter.workerArray[i].getTimer() != 0 && CareCenter.workerArray[i].getChild() == null) 
				{
					CareCenter.workerArray[i].setTimer(CareCenter.workerArray[i].getTimer() - 1);
				}
			}
		}
	}
	
		
	public void GiveSupplytoChildren() 
	{
		for (int i = 0; i < CareCenter.childArray.length; i++) 
		{
			if (CareCenter.childArray[i] != null && CareCenter.childArray[i].getCarer() != null
					&& CareCenter.childArray[i].getCarer().getSupply().getAmount() > 0
					&& CareCenter.childArray[i].getCarer().getTimer() != 0) 
			{
				if (CareCenter.carerArray[i].getSupply().getType().equals("f")) 
				{
					if (CareCenter.childArray[i].getCarer().getSupply().getAmount() > CareCenter.childArray[i].getCarer().getMeetingFoodRate()) 
					{
						CareCenter.childArray[i].setFood(round(CareCenter.childArray[i].getFood()+ CareCenter.childArray[i].getCarer().getMeetingFoodRate()));
						carecenter.setFoodStock(round(carecenter.getFoodStock() - CareCenter.childArray[i].getCarer().getMeetingFoodRate()));
						CareCenter.childArray[i].getCarer().getSupply().setAmount(round(CareCenter.childArray[i].getCarer().getSupply().getAmount()
										- CareCenter.childArray[i].getCarer().getMeetingFoodRate()));
					} 
					else 
					{
						CareCenter.childArray[i].setFood(round(CareCenter.childArray[i].getFood()+ CareCenter.childArray[i].getCarer().getSupply().getAmount()));
						carecenter.setFoodStock(round(carecenter.getFoodStock()- CareCenter.childArray[i].getCarer().getSupply().getAmount()));
						CareCenter.childArray[i].getCarer().getSupply().setAmount(round(CareCenter.childArray[i].getCarer().getSupply().getAmount()
										- CareCenter.childArray[i].getCarer().getSupply().getAmount()));
					}
				}

				if (CareCenter.carerArray[i].getSupply().getType().equals("g")) 
				{
					if (CareCenter.childArray[i].getCarer().getSupply().getAmount() >= CareCenter.childArray[i].getCarer().getMeetingGameRate()) 
					{
						CareCenter.childArray[i].setGame(round(CareCenter.childArray[i].getGame()+ CareCenter.childArray[i].getCarer().getMeetingGameRate()));
						carecenter.setGameStock(round(carecenter.getGameStock() - CareCenter.childArray[i].getCarer().getMeetingGameRate()));
						CareCenter.childArray[i].getCarer().getSupply().setAmount(round(CareCenter.childArray[i].getCarer().getSupply().getAmount()
										- CareCenter.childArray[i].getCarer().getMeetingGameRate()));
					} 
					else 
					{
						CareCenter.childArray[i].setGame(round(CareCenter.childArray[i].getGame() + CareCenter.childArray[i].getCarer().getSupply().getAmount()));
						carecenter.setGameStock(round(carecenter.getGameStock() - CareCenter.childArray[i].getCarer().getSupply().getAmount()));
						CareCenter.childArray[i].getCarer().getSupply().setAmount(round(CareCenter.childArray[i].getCarer().getSupply().getAmount()
										- CareCenter.childArray[i].getCarer().getSupply().getAmount()));
					}
				}

				if (CareCenter.carerArray[i].getSupply().getType().equals("h")) 
				{
					if (CareCenter.childArray[i].getCarer().getSupply().getAmount() >= CareCenter.childArray[i].getCarer().getMeetingHygieneRate()) 
					{
						CareCenter.childArray[i].setHygiene(round(CareCenter.childArray[i].getHygiene() + CareCenter.childArray[i].getCarer().getMeetingHygieneRate()));
						carecenter.setHygieneStock(round(carecenter.getHygieneStock() - CareCenter.childArray[i].getCarer().getMeetingHygieneRate()));
						CareCenter.childArray[i].getCarer().getSupply().setAmount(round(CareCenter.childArray[i].getCarer().getSupply().getAmount()
										- CareCenter.childArray[i].getCarer().getMeetingHygieneRate()));
					} 
					else 
					{
						CareCenter.childArray[i].setHygiene(round(CareCenter.childArray[i].getHygiene() + CareCenter.childArray[i].getCarer().getSupply().getAmount()));
						carecenter.setHygieneStock(round(carecenter.getHygieneStock() - CareCenter.childArray[i].getCarer().getSupply().getAmount()));
						CareCenter.childArray[i].getCarer().getSupply().setAmount(round(CareCenter.childArray[i].getCarer().getSupply().getAmount()
										- CareCenter.childArray[i].getCarer().getSupply().getAmount()));
					}
				}

				if (CareCenter.carerArray[i].getSupply().getType().equals("s")) 
				{
					CareCenter.childArray[i].setSleep(round(CareCenter.childArray[i].getSleep() + CareCenter.childArray[i].getCarer().getMeetingSleepRate()));
					CareCenter.childArray[i].getCarer().getSupply().setAmount(round(CareCenter.childArray[i].getCarer().getSupply().getAmount()
									- CareCenter.childArray[i].getCarer().getMeetingSleepRate()));
				}
			}
		}
	}
	
	public void Shopping() 
	{
		for (int i = 0; i < CareCenter.workerArray.length; i++) 
		{
			if (CareCenter.workerArray[i] != null && CareCenter.workerArray[i].getTimer() == 0 && CareCenter.workerArray[i].getSupply() != null) 
			{
				for (int j = 0; j < 3; j++) 
				{
					if (CareCenter.workerArray[i].getSupply()[j] != null && CareCenter.workerArray[i].getSupply()[j].getType().equals("f")) 
					{
						carecenter.setFoodStock(round(carecenter.getFoodStock() + CareCenter.workerArray[i].getSupply()[j].getAmount()));
						carecenter.setCredit(round(carecenter.getCredit() - creditforUnit(2.0, CareCenter.workerArray[i].getSupply()[j].getAmount())));
						CareCenter.workerArray[i].getSupply()[j] = null;
					}

					if (CareCenter.workerArray[i].getSupply()[j] != null && CareCenter.workerArray[i].getSupply()[j].getType().equals("g")) 
					{
						carecenter.setGameStock(round(carecenter.getGameStock() + CareCenter.workerArray[i].getSupply()[j].getAmount()));
						carecenter.setCredit(round(carecenter.getCredit() - creditforUnit(2.0, CareCenter.workerArray[i].getSupply()[j].getAmount())));
						CareCenter.workerArray[i].getSupply()[j] = null;
					}

					if (CareCenter.workerArray[i].getSupply()[j] != null && CareCenter.workerArray[i].getSupply()[j].getType().equals("h")) 
					{
						carecenter.setHygieneStock(round(carecenter.getHygieneStock() + CareCenter.workerArray[i].getSupply()[j].getAmount()));
						carecenter.setCredit(round(carecenter.getCredit() - creditforUnit(1.0, CareCenter.workerArray[i].getSupply()[j].getAmount())));
						CareCenter.workerArray[i].getSupply()[j] = null;
					}
				}
				CareCenter.workerArray[i].setSupply(null);
			}
		}
	}
	
	
	public void SleepUnlimited(Child child, Carer carer) 
	{
		carer.setChild(child);
		child.setCarer(carer);
		carer.setTimer(Integer.MAX_VALUE);
	}
	
	public void SleepLimited(Child child, Carer carer, int time) 
	{
		carer.setChild(child);
		child.setCarer(carer);
		carer.setTimer(time);
	}

	
	public void ClearScreen() 
	{
		for (int i = 0; i < 110; i++) 
		{
			for (int j = 0; j < 41; j++) 
			{
				Main.cn.getTextWindow().setCursorPosition(i, j);
				System.out.println(" ");
			}
		}
		Main.cn.getTextWindow().setCursorPosition(2, 2);
	}

	public void ClearCommand() 
	{
		for (int i = 0; i < 100; i++) 
		{
			Main.cn.getTextWindow().setCursorPosition(i, 42);
			System.out.println(" ");
		}
	}
	
	public void ClearTile() 
	{
		for (int i = 0; i < 100; i++)
		{
			Main.cn.getTextWindow().setCursorPosition(i, 0);
			System.out.println(" ");
		}
	}

	
	public void ControlforApplications() 
	{
		for (int i = 0; i < 3; i++) 
		{
			if (CareCenter.applicationArray[i] >= -1)
			{
				if (CareCenter.applicationArray[i] != -1)
					CareCenter.applicationArray[i]--;

				else 
				{
					CareCenter.applicationArray[i] = -1;
				}
			}
		}
	}
	
	public void CreateRandomApplication() 
	{
		if (CareCenter.applicationArray[0] == -1 && carecenter.getPreviousDayHappiness() > 65)
		{
			int prob = random.nextInt(100);

			if (0 < prob && prob < 40) 
			{
				CareCenter.applicationArray[0] = 10;
			}
		}

		if (CareCenter.applicationArray[1] == -1) 
		{
			int prob = random.nextInt(100);

			if (0 < prob && prob < 40) 
			{
				CareCenter.applicationArray[1] = 10;
			}
		}

		if (CareCenter.applicationArray[2] == -1)
		{
			int prob = random.nextInt(100);

			if (0 < prob && prob < 40)
			{
				CareCenter.applicationArray[2] = 10;
			}
		}
	}
	
	public void ApplicationProgress() 
	{
		for (int i = 0; i < CareCenter.applicationArray.length; i++) 
		{
			Main.cn.getTextWindow().setCursorPosition(0, 1);
			Main.cn.getTextWindow().output("---Application---", new TextAttributes(Color.cyan));

			if (CareCenter.applicationArray[i] == 10 && i == 0) 
			{
				child = new Child();

				CareCenter.sizeOfApplications++;

				Main.cn.getTextWindow().setCursorPosition(0, 2);
				Main.cn.getTextWindow().output("1.", new TextAttributes(Color.cyan));
				Main.cn.getTextWindow().output("Child - F:" + child.getFood() + "  G:" + child.getGame() + "  S:" + child.getSleep() + "  H: " + child.getHygiene());
			}

			else if (CareCenter.applicationArray[i] > -1 && CareCenter.applicationArray[i] < 10 && i == 0) 
			{
				Main.cn.getTextWindow().setCursorPosition(0, 2);
				Main.cn.getTextWindow().output("1.", new TextAttributes(Color.cyan));
				Main.cn.getTextWindow().output("Child - F:" + child.getFood() + "  G:" + child.getGame() + "  S:" + child.getSleep() + "  H: " + child.getHygiene());
			}

			if (CareCenter.applicationArray[i] == 10 && i == 1) 
			{
				worker = new Worker();

				CareCenter.sizeOfApplications++;

				Main.cn.getTextWindow().setCursorPosition(0, 3);
				Main.cn.getTextWindow().output("2.", new TextAttributes(Color.cyan));
				Main.cn.getTextWindow().output("Worker");
			}

			else if (CareCenter.applicationArray[i] > -1 && CareCenter.applicationArray[i] < 10 && i == 1) 
			{
				Main.cn.getTextWindow().setCursorPosition(0, 3);
				Main.cn.getTextWindow().output("2.", new TextAttributes(Color.cyan));
				Main.cn.getTextWindow().output("Worker");
			}

			if (CareCenter.applicationArray[i] == 10 && i == 2) 
			{
				carer = new Carer();

				CareCenter.sizeOfApplications++;

				Main.cn.getTextWindow().setCursorPosition(0, 4);
				Main.cn.getTextWindow().output("3.", new TextAttributes(Color.cyan));
				Main.cn.getTextWindow().output("Carer");
			}

			else if (CareCenter.applicationArray[i] > -1 && CareCenter.applicationArray[i] < 10 && i == 2) 
			{
				Main.cn.getTextWindow().setCursorPosition(0, 4);
				Main.cn.getTextWindow().output("3.", new TextAttributes(Color.cyan));
				Main.cn.getTextWindow().output("Carer");
			}

		}
	}
	
		
	public void DefineMissingValueforChildren() 
	{
		for (int i = 0; i < CareCenter.childArray.length; i++) 
		{
			if (CareCenter.childArray[i] != null && CareCenter.childArray[i].getHappiness() < 25 && CareCenter.childArray[i].getMissing() == 0) 
			{
				int prob = random.nextInt(100);

				if (prob == 1) 
				{
					CareCenter.childArray[i].setMissing(CareCenter.childArray[i].getMissing() + 1);
				}
			}
		}
	}

	public void ControlMissingofChildren() 
	{
		for (int i = 0; i < CareCenter.childArray.length; i++) 
		{
			if (CareCenter.childArray[i] != null && CareCenter.childArray[i].getWorker() == null && CareCenter.childArray[i].getMissing() != 0) 
			{
				CareCenter.childArray[i].setMissing(CareCenter.childArray[i].getMissing() + 1);
			}
		}
	}
	
	public void ChildLeaveCenter() 
	{
		for (int i = 0; i < CareCenter.childArray.length; i++) 
		{
			if (CareCenter.childArray[i] != null && CareCenter.childArray[i].getHappiness() < 10) 
			{
				if (CareCenter.childArray[i].getCarer() != null) 
				{
					CareCenter.childArray[i].getCarer().setChild(null);
					CareCenter.childArray[i].setCarer(null);
				}

				carecenter.setCredit(round(carecenter.getCredit() - CareCenter.childArray[i].getPenalty()));
				CareCenter.childArray[i] = null;
				CareCenter.sizeOfChild--;
			}
		}
	}
	
	
	public void DecreaseSuppliesofChildren() 
	{
		for (int i = 0; i < CareCenter.childArray.length; i++)
		{
			if (CareCenter.childArray[i] != null && CareCenter.childArray[i].getMissing() == 0) 
			{
				CareCenter.childArray[i].setFood(round(CareCenter.childArray[i].getFood() - CareCenter.childArray[i].getFoodDecreasingRate()));
				CareCenter.childArray[i].setGame(round(CareCenter.childArray[i].getGame() - CareCenter.childArray[i].getGameDecreasingRate()));
				CareCenter.childArray[i].setSleep(round(CareCenter.childArray[i].getSleep() - CareCenter.childArray[i].getSleepDecreasingRate()));
				CareCenter.childArray[i].setHygiene(round(CareCenter.childArray[i].getHygiene() - CareCenter.childArray[i].getHygieneDecreasingRate()));
			}
		}

	}
	
	public double getRandomSupplyValues() 
	{
		return round(25 * random.nextDouble() + 50);
	}
	
	
	public boolean finishState() 
	{
		if (CareCenter.sizeOfChild == 0 || carecenter.getCredit() <= 0)
			return true;
		else
			return false;
	}
	
	
	public double round(double value) 
	{
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(2, RoundingMode.HALF_UP);

		return bd.doubleValue();
	}
	
	private static String removeLastChar(String str) 
	{
		return str.substring(0, str.length() - 1);
	}
	
	private int roundUP(double d) 
	{
		double dAbs = Math.abs(d);
		int i = (int) dAbs;
		double result = dAbs - (double) i;
		if (result == 0.0)
		{
			return (int) d;
		}
		else 
		{
			return (int) d < 0 ? -(i + 1) : i + 1;
		}
	}
}
