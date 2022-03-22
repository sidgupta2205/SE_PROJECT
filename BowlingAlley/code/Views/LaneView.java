package Views;
/*
 *  constructs a prototype Lane View
 *
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import models.Bowler;
import models.Lane;
import controllers.LaneEvent;
import controllers.LaneObserver;
import models.Party;

import java.util.*;

public class LaneView implements LaneObserver, ActionListener {

	//private int roll;
	private boolean initDone = true;

	JFrame frame;
	Container cpanel;
	Vector bowlers;
	int ballgrids;
	int ballschance;
	Iterator bowlIt;

	JPanel[][] balls;
	JLabel[][] ballLabel;
	JPanel[][] scores;
	JLabel[][] scoreLabel;
	JPanel[][] ballGrid;
	JPanel[] pins;
	GeneralView gview=new GeneralView();
	JButton maintenance;
	Lane lane;
	
	public LaneView(Lane lane, int laneNum) {

		this.lane = lane;
		this.ballgrids = 16;
		this.ballschance=this.ballgrids*2+3;
		initDone = true;
		frame = new JFrame("Lane " + laneNum + ":");
		cpanel = frame.getContentPane();
		cpanel.setLayout(new BorderLayout());

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				frame.hide();
			}
		});

		cpanel.add(new JPanel());

	}

	public void show() {
		frame.show();
	}

	public void hide() {
		frame.hide();
	}

	private JPanel makeFrame(Party party) {

		initDone = false;
		bowlers = party.getMembers();
		int numBowlers = bowlers.size();

		JPanel panel = new JPanel();

		panel.setLayout(new GridLayout(0, 1));

		balls = new JPanel[numBowlers][this.ballschance];
		ballLabel = new JLabel[numBowlers][this.ballschance];
		scores = new JPanel[numBowlers][this.ballgrids];
		scoreLabel = new JLabel[numBowlers][this.ballgrids];
		ballGrid = new JPanel[numBowlers][this.ballgrids];
		pins = new JPanel[numBowlers];

		for (int i = 0; i != numBowlers; i++) {
			for (int j = 0; j != this.ballschance; j++) {
				ballLabel[i][j] = new JLabel(" ");
				balls[i][j] = new JPanel();
				balls[i][j].setBorder(
					BorderFactory.createLineBorder(Color.BLACK));
				balls[i][j].add(ballLabel[i][j]);
			}
		}

		for (int i = 0; i != numBowlers; i++) {
			for (int j = 0; j != this.ballgrids-1; j++) {
				ballGrid[i][j] = new JPanel();
				ballGrid[i][j].setLayout(new GridLayout(0, 3));
				ballGrid[i][j].add(new JLabel("  "), BorderLayout.EAST);
				ballGrid[i][j].add(balls[i][2 * j], BorderLayout.EAST);
				ballGrid[i][j].add(balls[i][2 * j + 1], BorderLayout.EAST);
			}
			int j = this.ballgrids-1;
			ballGrid[i][j] = new JPanel();
			ballGrid[i][j].setLayout(new GridLayout(0, 3));
			ballGrid[i][j].add(balls[i][2 * j]);
			ballGrid[i][j].add(balls[i][2 * j + 1]);
			ballGrid[i][j].add(balls[i][2 * j + 2]);
		}

		for (int i = 0; i != numBowlers; i++) {
			pins[i] = new JPanel();
			pins[i].setBorder(
				BorderFactory.createTitledBorder(
					((Bowler) bowlers.get(i)).getNick()));
			pins[i].setLayout(new GridLayout(0, this.ballgrids));
			for (int k = 0; k != this.ballgrids; k++) {
				scores[i][k] = new JPanel();
				scoreLabel[i][k] = new JLabel("  ", SwingConstants.CENTER);
				scores[i][k].setBorder(
					BorderFactory.createLineBorder(Color.BLACK));
				scores[i][k].setLayout(new GridLayout(0, 1));
				scores[i][k].add(ballGrid[i][k], BorderLayout.EAST);
				scores[i][k].add(scoreLabel[i][k], BorderLayout.SOUTH);
				pins[i].add(scores[i][k], BorderLayout.EAST);
			}
			panel.add(pins[i]);
		}

		initDone = true;
		return panel;
	}

	public void receiveLaneEvent(LaneEvent le) {
		
		
		System.out.println("Lane view call");
		if (lane.isPartyAssigned()) {
			int numBowlers = le.p.getMembers().size();
			while (!initDone) {
				//System.out.println("chillin' here.");
				try {
					Thread.sleep(1);
				} catch (Exception e) {
				}
			}

			if (le.frameNum == 1
				&& le.ball == 0
				&& le.index == 0) {
				System.out.println("Making the frame.");
				cpanel.removeAll();
				cpanel.add(makeFrame(le.p), "Center");

				// Button Panel
				JPanel buttonPanel = new JPanel();
				buttonPanel.setLayout(new FlowLayout());

				maintenance = new JButton("Maintenance Call");
				JPanel maintenancePanel = new JPanel();
				gview.addButton(maintenance,maintenancePanel);
				buttonPanel.add(maintenancePanel);
				maintenance.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (e.getSource().equals(maintenance)) {
							System.out.print("button called");
							lane.pauseGame();
						}
					}
				});
				cpanel.add(buttonPanel, "South");

				frame.pack();

			}

			int[][] lescores = le.cumulScore;
			for (int k = 0; k < numBowlers; k++) {
				for (int i = 0; i <= le.frameNum - 1; i++) {
					if (lescores[k][i] != 0)
						scoreLabel[k][i].setText(
							(new Integer(lescores[k][i])).toString());
				}
				for (int i = 0; i < 25; i++) {
					if (((int[]) ((HashMap) le.score)
						.get(bowlers.get(k)))[i]
						!= -1)
						if (((int[]) ((HashMap) le.score)
							.get(bowlers.get(k)))[i]
							== this.ballgrids
							&& (i % 2 == 0 || i == 19))
							ballLabel[k][i].setText("X");
						else if (
							i > 0
								&& ((int[]) ((HashMap) le.score)
									.get(bowlers.get(k)))[i]
									+ ((int[]) ((HashMap) le.score)
										.get(bowlers.get(k)))[i
									- 1]
									== this.ballgrids
								&& i % 2 == 1)
							ballLabel[k][i].setText("/");
						else if ( ((int[])((HashMap) le.score).get(bowlers.get(k)))[i] == -2 ){
							
							ballLabel[k][i].setText("F");
						} else
							ballLabel[k][i].setText(
								(new Integer(((int[]) ((HashMap) le.score)
									.get(bowlers.get(k)))[i]))
									.toString());
				}
			}

		}
	}

	@Override
	
		public void actionPerformed(ActionEvent e) {
			if (e.getSource().equals(maintenance)) {
				System.out.print("button called");
				lane.pauseGame();
			}
		}
	

	

}
