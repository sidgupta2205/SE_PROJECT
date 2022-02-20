package Views;
/* ControlDeskView.java
 *
 *  Version:
 *			$Id$
 * 
 *  Revisions:
 * 		$Log$
 * 
 */

/**
 * Class for representation of the control desk
 *
 */
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import controllers.ControlDeskEvent;
import controllers.ControlDeskObserver;
import models.ControlDesk;
import models.Lane;
import models.Pinsetter;

public class ControlDeskView implements ControlDeskObserver {

	private JButton addParty, finished;
	private JFrame win;
	private JList partyList;
	
	/** The maximum  number of members in a party */
	private int maxMembers;
	
	private ControlDesk controlDesk;
	private ControlDeskView CDView;

	/*  Displays a GUI representation of the ControlDesk */
	public ControlDeskView(ControlDesk controlDesk, int maxMembers) {
		this.CDView = this;
		this.controlDesk = controlDesk;
		this.maxMembers = maxMembers;
		int numLanes = controlDesk.numLanes;

		win = new JFrame("Control Desk");
		win.getContentPane().setLayout(new BorderLayout());
		((JPanel) win.getContentPane()).setOpaque(false);

		GeneralView gview=new GeneralView();
		
		JPanel colPanel = new JPanel();
		colPanel.setLayout(new BorderLayout());

		// Controls Panel
		JPanel controlsPanel = new JPanel();
		gview.setGridLayout(controlsPanel, 3, 1, "Controls");

		addParty = new JButton("Add Party");
		JPanel addPartyPanel = new JPanel();	
		addParty.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AddPartyView addPartyWin = new AddPartyView(CDView, maxMembers);
			}
		});
		gview.addButton(addParty,addPartyPanel);
		controlsPanel.add(addPartyPanel);

		finished = new JButton("Finished");
		JPanel finishedPanel = new JPanel();       
		finished.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				win.hide();
				System.exit(0);
			}
		});
		gview.addButton(finished,finishedPanel);
		controlsPanel.add(finishedPanel);

		// Lane Status Panel
		JPanel laneStatusPanel = new JPanel();
		gview.setGridLayout(laneStatusPanel, numLanes, 1,"Lane Status");

		HashSet lanes=controlDesk.lanes;
		Iterator it = lanes.iterator();
		int laneCount=0;
		while (it.hasNext()) {
			Lane curLane = (Lane) it.next();
			LaneStatusView laneStat = new LaneStatusView(curLane,(laneCount+1));
			curLane.subscribe(laneStat);                                            
			((Pinsetter)curLane.getPinsetter()).subscribe(laneStat);
			JPanel lanePanel = laneStat.showLane();
			lanePanel.setBorder(new TitledBorder("Lane" + ++laneCount ));
			laneStatusPanel.add(lanePanel);
		}

		// Party Queue Panel
		JPanel partyPanel = new JPanel();
		gview.setFlowLayout(partyPanel,"Party Queue");

		Vector empty = new Vector();
		empty.add("(Empty)");

		partyList = new JList(empty);
		gview.addJList(partyList,10 ,120);
		JScrollPane partyPane = new JScrollPane(partyList);
		partyPane.setVerticalScrollBarPolicy(
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		partyPanel.add(partyPane);
		//	partyPanel.add(partyList);

		// Clean up main panel
		colPanel.add(controlsPanel, "East");                   
		colPanel.add(laneStatusPanel, "Center");                             
		colPanel.add(partyPanel, "West");

		win.getContentPane().add("Center", colPanel);
		win.pack();

		/* Close program when this window closes */
		win.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		// Center Window on Screen
		Dimension screenSize = (Toolkit.getDefaultToolkit()).getScreenSize();
		win.setLocation(
			((screenSize.width) / 2) - ((win.getSize().width) / 2),
			((screenSize.height) / 2) - ((win.getSize().height) / 2));
		win.show();

	}


	/**
	 * Receive a new party from andPartyView.
	 *
	 * @param addPartyView	the AddPartyView that is providing a new party
	 *
	 */

	public void updateAddParty(AddPartyView addPartyView) {
		controlDesk.addPartyQueue(addPartyView.getParty());
	}

	/**
	 * Receive a broadcast from a ControlDesk
	 *
	 * @param ce	the ControlDeskEvent that triggered the handler
	 *
	 */

	public void receiveControlDeskEvent(ControlDeskEvent ce) {
		partyList.setListData(((Vector) ce.getPartyQueue()));
	}
}
