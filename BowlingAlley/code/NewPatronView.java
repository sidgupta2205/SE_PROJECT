/* AddPartyView.java
 *
 *  Version
 *  $Id$
 * 
 *  Revisions:
 * 		$Log: NewPatronView.java,v $
 * 		Revision 1.3  2003/02/02 16:29:52  ???
 * 		Added ControlDeskEvent and ControlDeskObserver. Updated Queue to allow access to Vector so that contents could be viewed without destroying. Implemented observer model for most of ControlDesk.
 * 		
 * 
 */

/**
 * Class for GUI components need to add a patron
 *
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import java.util.*;
import java.text.*;

public class NewPatronView implements ActionListener {

	private int maxSize;

	private JFrame win;
	private JButton abort, finished;
	private JLabel nickLabel, fullLabel, emailLabel;
	private JTextField nickField, fullField, emailField;
	private String nick, full, email;

	private boolean done;

	private String selectedNick, selectedMember;
	private AddPartyView addParty;
	// HRK Comment: Create a separate class which will be inherited by all the classes who wants to create a view. 
	// Code Smell: Code duplication.
	public void addButton(JButton mybutton,JPanel myPanel) {
		myPanel.setLayout(new FlowLayout());
		mybutton.addActionListener(this);
		myPanel.add(mybutton);
		
	}	
	public void addLabel(JPanel myPanel, JLabel mylabel, JTextField tfield,String labelcontent, int length) {
		myPanel.setLayout(new FlowLayout());
		mylabel=new JLabel(labelcontent);
		tfield=new JTextField("",length);
		myPanel.add(mylabel);
		myPanel.add(tfield);
	}
	public NewPatronView(AddPartyView v) {

		addParty=v;	
		done = false;

		win = new JFrame("Add Patron");
		win.getContentPane().setLayout(new BorderLayout());
		((JPanel) win.getContentPane()).setOpaque(false);

		JPanel colPanel = new JPanel();
		colPanel.setLayout(new BorderLayout());

		// Patron Panel
		JPanel patronPanel = new JPanel();
		patronPanel.setLayout(new GridLayout(3, 1));
		patronPanel.setBorder(new TitledBorder("Your Info"));

		JPanel nickPanel = new JPanel();
		addLabel(nickPanel,nickLabel,nickField,"Nick Name",15);

		JPanel fullPanel = new JPanel();
		addLabel(fullPanel,fullLabel,fullField,"Full Name",15);

		JPanel emailPanel = new JPanel();
		addLabel(emailPanel,emailLabel,emailField,"EMail",15);

		patronPanel.add(nickPanel);
		patronPanel.add(fullPanel);
		patronPanel.add(emailPanel);

		// Button Panel
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(4, 1));

		Insets buttonMargin = new Insets(4, 4, 4, 4);

		finished = new JButton("Add Patron");
		JPanel finishedPanel = new JPanel();
		addButton(finished, finishedPanel);


		abort = new JButton("Abort");
		JPanel abortPanel = new JPanel();
		addButton(abort, abortPanel);

		buttonPanel.add(abortPanel);
		buttonPanel.add(finishedPanel);

		// Clean up main panel
		colPanel.add(patronPanel, "Center");
		colPanel.add(buttonPanel, "East");

		win.getContentPane().add("Center", colPanel);

		win.pack();

		// Center Window on Screen
		Dimension screenSize = (Toolkit.getDefaultToolkit()).getScreenSize();
		win.setLocation(
			((screenSize.width) / 2) - ((win.getSize().width) / 2),
			((screenSize.height) / 2) - ((win.getSize().height) / 2));
		win.show();

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(abort)) {
			done = true;
			win.hide();
		}

		if (e.getSource().equals(finished)) {
			nick = nickField.getText();
			full = fullField.getText();
			email = emailField.getText();
			done = true;
			addParty.updateNewPatron( this );
			win.hide();
		}

	}

	public boolean done() {
		return done;
	}

	public String getNick() {
		return nick;
	}

	public String getFull() {
		return full;
	}

	public String getEmail() {
		return email;
	}
}
