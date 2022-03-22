package controllers;
import java.util.Random;

public class SimulateThrow {
	
	public boolean foul;
	public boolean[] pins; 
	private Random rnd;
	public SimulateThrow()
	{
		foul = false;
		pins = new boolean[10];
		rnd = new Random();
		resetPins();
	}
	
	public void resetPins() {
		for (int i=0; i <= 9; i++) {
			pins[i] = true;
		}
	}	
	
	public boolean isFoul()
	{
		System.out.println(foul);
		return foul;
	}
	
	public int ballThrown() {
		// simulated event of ball hits sensor
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		
		int count = 0;
		foul = false;
		
		double foulluck = rnd.nextDouble();
		System.out.println(foulluck);
		if (foulluck <= .0002){ 
			
			System.out.println("Foul commited");
			foul = true;
			return count;
		}
		else
		{
			foul = false;
			System.out.println("Makeing foul negative");
		}
		
		
		
		double skill = rnd.nextDouble();
		for (int i=0; i <= 9; i++) {
			if (pins[i]) {
				double pinluck = rnd.nextDouble();
				if ( ((skill + pinluck)/2.0 * 1.2) > .5 ){
					pins[i] = false;
					count++;
				} 
				
			}
			
			
			
		}

		try {
			Thread.sleep(500);				// pinsetter is where delay will be in a real game
		} catch (Exception e) {}

		return count;
		
	}
	
}
