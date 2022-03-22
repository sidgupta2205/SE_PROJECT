package Views;
/**
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import controllers.PinsetterEvent;
import controllers.PinsetterObserver;
import models.Bowler;
import models.Lane;
import controllers.LaneEvent;
import controllers.LaneObserver;
import models.Pinsetter;

public class LaneStatusView implements LaneObserver, PinsetterObserver {

	private JPanel jp;

	private JLabel curBowler, pinsDown, foul;
	private JButton viewLane;
	private JButton viewPinSetter, maintenance;

	private PinSetterView psv;
	private LaneView lv;
	private Lane lane;
	int laneNum;

	boolean laneShowing;
	boolean psShowing;

	public LaneStatusView(Lane lane, int laneNum ) {

		this.lane = lane;
		this.laneNum = laneNum;

		laneShowing=false;
		psShowing=false;

		psv = new PinSetterView( laneNum );
		Pinsetter ps = lane.getPinsetter();
		ps.subscribe(psv);

		lv = new LaneView( lane, laneNum );
		lane.subscribe(lv);
		GeneralView gview=new GeneralView();

		jp = new JPanel();
		jp.setLayout(new FlowLayout());
		JLabel cLabel = new JLabel( "Now Bowling: " );
		curBowler = new JLabel( "(no one)" );
		JLabel fLabel = new JLabel( "Foul: " );
		foul = new JLabel( " " );
		JLabel pdLabel = new JLabel( "Pins Down: " );
		pinsDown = new JLabel( "0" );

		// Button Panel
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());

		Insets buttonMargin = new Insets(4, 4, 4, 4);

		viewLane = new JButton("View Lane");
		viewLane.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if ( lane.isPartyAssigned() ) { 
					if ( laneShowing == false ) {
						lv.show();
						laneShowing=true;
					} else if ( laneShowing == true ) {
						lv.hide();
						laneShowing=false;
					}
				}
			}
		});
		JPanel viewLanePanel = new JPanel();
		gview.addButton(viewLane,viewLanePanel);

		viewPinSetter = new JButton("Pinsetter");
		viewPinSetter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if ( lane.isPartyAssigned() ) { 
					if (e.getSource().equals(viewPinSetter)) {
						if ( psShowing == false ) {
							psv.show();
							psShowing=true;
						} else if ( psShowing == true ) {
							psv.hide();
							psShowing=false;
						}
					}
				}
			}
		});
		JPanel viewPinSetterPanel = new JPanel();
		gview.addButton(viewPinSetter, viewPinSetterPanel);

		maintenance = new JButton("     ");
		maintenance.setBackground( Color.GREEN );
		maintenance.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if ( lane.isPartyAssigned() ) {
					lane.unPauseGame();
					maintenance.setBackground( Color.GREEN );
				}
			}
		});
		JPanel maintenancePanel = new JPanel();
		gview.addButton(maintenance, maintenancePanel);

		viewLane.setEnabled( false );
		viewPinSetter.setEnabled( false );


		buttonPanel.add(viewLanePanel);
		buttonPanel.add(viewPinSetterPanel);
		buttonPanel.add(maintenancePanel);

		jp.add( cLabel );
		jp.add( curBowler );
		jp.add( pdLabel );
		jp.add( pinsDown );
		jp.add(buttonPanel);

	}

	public JPanel showLane() {
		return jp;
	}

	public void receiveLaneEvent(LaneEvent le) {
		curBowler.setText( ( (Bowler)le.bowler).getNick() );
		if ( le.mechProb ) {
			maintenance.setBackground( Color.RED );
		}	
		if ( lane.isPartyAssigned() == false ) {
			viewLane.setEnabled( false );
			viewPinSetter.setEnabled( false );
		} 
		else {
			viewLane.setEnabled( true );
			viewPinSetter.setEnabled( true );
		}
	}

	public void receivePinsetterEvent(PinsetterEvent pe) {
		if(pe.isFoulCommited())
		{
			pinsDown.setText("Foul");
			System.out.println("Here");
		}
			
		else
			pinsDown.setText( ( new Integer(pe.totalPinsDown()) ).toString());		
	}

}
