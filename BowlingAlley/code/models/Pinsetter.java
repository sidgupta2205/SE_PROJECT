package models;


/**
 * Class to represent the pinsetter
 *
 */

import java.util.*;

import controllers.PinsetterEvent;
import controllers.PinsetterObserver;

import java.io.Serializable;
import java.lang.Boolean;

public class Pinsetter implements Serializable {

	private Random rnd;
	private Vector subscribers;

	private boolean[] pins; 
			/* 0-9 of state of pine, true for standing, 
			false for knocked down

			6   7   8   9
			  3   4   5
			    2   1
			      0

			*/
	private boolean foul;
	private int throwNumber;

	/** sendEvent()
	 * 
	 * Sends pinsetter events to all subscribers
	 * 
	 * @pre none
	 * @post all subscribers have recieved pinsetter event with updated state
	 * */
	public void sendEvent(int jdpins) {
		// send events when our state is changd
//		throwNumber++;
		
		
		for (int i=0; i < subscribers.size(); i++) {
			((PinsetterObserver)subscribers.get(i)).receivePinsetterEvent(
				new PinsetterEvent(pins, foul, throwNumber, jdpins));
		}
	}
	
	public void sendEvent(boolean[] pins_recv,int jdpins,boolean foul) {
		// send events when our state is changd
		throwNumber++;
		
		for (int i=0; i < subscribers.size(); i++) {
			((PinsetterObserver)subscribers.get(i)).receivePinsetterEvent(
				new PinsetterEvent(pins_recv, foul, throwNumber, jdpins));
		}
		
		
		
	}
	
	

	/** Pinsetter()
	 * 
	 * Constructs a new pinsetter
	 * 
	 * @pre none
	 * @post a new pinsetter is created
	 * @return Pinsetter object
	 */
	public Pinsetter() {
		pins = new boolean[10];
		rnd = new Random();
		subscribers = new Vector();
		foul = false;
		reset();
	}

	/** ballThrown()
	 * 
	 * Called to simulate a ball thrown comming in contact with the pinsetter
	 * 
	 * @pre none
	 * @post pins may have been knocked down and the thrownumber has been incremented
	 */


	/** reset()
	 * 
	 * Reset the pinsetter to its complete state
	 * 
	 * @pre none
	 * @post pinsetters state is reset
	 */
	public void reset() {
		foul = false;
		throwNumber = 0;
		resetPins();
		
		try {
			Thread.sleep(1000);
		} catch (Exception e) {}
		
		
		sendEvent(-1);
	}

	/** resetPins()
	 * 
	 * Reset the pins on the pinsetter
	 * 
	 * @pre none
	 * @post pins array is reset to all pins up
	 */
	public void resetPins() {
		for (int i=0; i <= 9; i++) {
			pins[i] = true;
		}
	}		

	/** subscribe()
	 * 
	 * subscribe objects to send events to
	 * 
	 * @pre none
	 * @post the subscriber object will recieve events when their generated
	 */
	public void subscribe(PinsetterObserver subscriber) {
		subscribers.add(subscriber);
	}

};

