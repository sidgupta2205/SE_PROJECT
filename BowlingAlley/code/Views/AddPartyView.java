package Views;
/* AddPartyView.java
 *
 *  Version:
 * 		 $Id$
 * 
 *  Revisions:
 * 		$Log: AddPartyView.java,v $
 * 		Revision 1.7  2003/02/20 02:05:53  ???
 * 		Fixed addPatron so that duplicates won't be created.
 * 		
 * 		Revision 1.6  2003/02/09 20:52:46  ???
 * 		Added comments.
 * 		
 * 		Revision 1.5  2003/02/02 17:42:09  ???
 * 		Made updates to migrate to observer model.
 * 		
 * 		Revision 1.4  2003/02/02 16:29:52  ???
 * 		Added ControlDeskEvent and ControlDeskObserver. Updated Queue to allow access to Vector so that contents could be viewed without destroying. Implemented observer model for most of ControlDesk.
 * 		
 * 
 */

/**
 * Class for GUI components need to add a party
 *
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import models.Bowler;
import controllers.BowlerFile;

import java.util.*;

/* Constructor for GUI used to Add Parties to the waiting party queue.  */
public class AddPartyView implements ListSelectionListener {
	private int maxSize;
	private JFrame win;
	private JButton addPatron, newPatron, remPatron, finished;
	private JList partyList, allBowlers;
	Vector party;

	private Vector bowlerdb;

	private AddPartyView addpartyView;
	private String selectedNick, selectedMember;

	public AddPartyView(ControlDeskView controlDesk, int max) {
		this.addpartyView = this;
		maxSize = max;

		win = new JFrame("Add Party");
		win.getContentPane().setLayout(new BorderLayout());
		((JPanel) win.getContentPane()).setOpaque(false);

		JPanel colPanel = new JPanel();
		colPanel.setLayout(new GridLayout(1, 3));
		GeneralView gview=new GeneralView();

		// Party Panel
		JPanel partyPanel = new JPanel();
		gview.setFlowLayout(partyPanel, "Your Party");

		party = new Vector();
		Vector empty = new Vector();
		empty.add("(Empty)");

		partyList = new JList(empty);
		gview.addJList(partyList, 5, 120);
		partyList.addListSelectionListener(this);
		JScrollPane partyPane = new JScrollPane(partyList);
		partyPanel.add(partyPane);

		// Bowler Database
		JPanel bowlerPanel = new JPanel();
		gview.setFlowLayout(bowlerPanel, "Bowler Database");

		try {
			bowlerdb = new Vector(BowlerFile.getBowlers());
		} catch (Exception e) {
			System.err.println("File Error");
			bowlerdb = new Vector();
		}
		allBowlers = new JList(bowlerdb);
		gview.addJList(allBowlers,8,120);
		JScrollPane bowlerPane = new JScrollPane(allBowlers);
		bowlerPane.setVerticalScrollBarPolicy(
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		allBowlers.addListSelectionListener(this);
		bowlerPanel.add(bowlerPane);

		// Button Panel
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(4, 1));

		addPatron = new JButton("Add to Party");
		addPatron.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (selectedNick != null && party.size() < maxSize) {
					if (party.contains(selectedNick)) {
						System.err.println("Member already in Party");
					} else {
						party.add(selectedNick);
						partyList.setListData(party);
					}
				}
			}
		});
		JPanel addPatronPanel = new JPanel();
		gview.addButton(addPatron, addPatronPanel);

		remPatron = new JButton("Remove Member");
		remPatron.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (selectedMember != null) {
					party.removeElement(selectedMember);
					partyList.setListData(party);
				}
			}
		});
		JPanel remPatronPanel = new JPanel();
		gview.addButton(remPatron,remPatronPanel);

		newPatron = new JButton("New Patron");
		newPatron.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				NewPatronView newPatron = new NewPatronView(addpartyView);
			}
		});
		JPanel newPatronPanel = new JPanel();
		gview.addButton(newPatron, newPatronPanel);

		finished = new JButton("Finished");
		finished.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if ( party != null && party.size() > 0) {
					controlDesk.updateAddParty(addpartyView);
				}
				win.hide();
			}
		});
		JPanel finishedPanel = new JPanel();
		gview.addButton(finished, finishedPanel);

		buttonPanel.add(addPatronPanel);
		buttonPanel.add(remPatronPanel);
		buttonPanel.add(newPatronPanel);
		buttonPanel.add(finishedPanel);

		// Clean up main panel
		colPanel.add(partyPanel);
		colPanel.add(bowlerPanel);
		colPanel.add(buttonPanel);

		win.getContentPane().add("Center", colPanel);

		win.pack();

		// Center Window on Screen
		Dimension screenSize = (Toolkit.getDefaultToolkit()).getScreenSize();
		win.setLocation(
			((screenSize.width) / 2) - ((win.getSize().width) / 2),
			((screenSize.height) / 2) - ((win.getSize().height) / 2));
		win.show();

	}


/**
 * Handler for List actions
 * @param e the ListActionEvent that triggered the handler
 */

	public void valueChanged(ListSelectionEvent e) {
		if (e.getSource().equals(allBowlers)) {
			selectedNick = ((String) ((JList) e.getSource()).getSelectedValue());
		}
		else if (e.getSource().equals(partyList)) {
			selectedMember = ((String) ((JList) e.getSource()).getSelectedValue());
		}
	}

/* Accessor for Party */
	public Vector getNames() {
		return party;
	}

/**
 * Called by NewPatronView to notify AddPartyView to update
 * 
 * @param newPatron the NewPatronView that called this method
 */

	public void updateNewPatron(NewPatronView newPatron) {
		try {
			Bowler checkBowler = BowlerFile.getBowlerInfo( newPatron.getNick() );
			if ( checkBowler == null ) {
				BowlerFile.putBowlerInfo(
					newPatron.getNick(),
					newPatron.getFull(),
					newPatron.getEmail());
				bowlerdb = new Vector(BowlerFile.getBowlers());
				allBowlers.setListData(bowlerdb);
				party.add(newPatron.getNick());
				partyList.setListData(party);
			} else {
				System.err.println( "A Bowler with that name already exists." );
			}
		} catch (Exception e2) {
			System.err.println("File I/O Error");
		}
	}

/* Accessor for Party */
	public Vector getParty() {
		return party;
	}

}
