import java.util.Vector;

import Views.ControlDeskView;
import models.Alley;
import models.ControlDesk;

import java.io.*;

import config.*;

public class drive {

	public static void main(String[] args) {

		// int numLanes = 3;
		int numLanes = config.c_numLanes;

		// int maxPatronsPerParty=5;
		int maxPatronsPerParty = config.c_maxPatronsPerParty;

		Alley a = new Alley( numLanes );
		ControlDesk controlDesk = a.getControlDesk();

		ControlDeskView cdv = new ControlDeskView( controlDesk, maxPatronsPerParty);
		controlDesk.subscribe( cdv );

	}
}
