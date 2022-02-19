package version1;
import java.util.Vector;
import java.io.*;

public class drive {

	public static void main(String[] args) {

		int numLanes = 3;
		int maxPatronsPerParty=5;
		ControlDesk controlDesk = new ControlDesk(numLanes);

		new ControlDeskView( controlDesk, maxPatronsPerParty);

	}
}
