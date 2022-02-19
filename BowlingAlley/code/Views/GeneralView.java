package Views;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import java.awt.FlowLayout;
import java.awt.GridLayout;

public class GeneralView {
	public GeneralView() {
		// Empty Constructor.
	}
	public void addJList(JList myList, int visibleRowCount, int FixedCellWidth) {
		myList.setVisibleRowCount(visibleRowCount);
		myList.setFixedCellWidth(FixedCellWidth);
	}

	public void setFlowLayout(JPanel myPanel, String title) {
		myPanel.setLayout(new FlowLayout());
		if(title.length()>0) {
			myPanel.setBorder(new TitledBorder(title));
		}
	}
	public void setGridLayout(JPanel myPanel,int rows, int cols, String title) {
		myPanel.setLayout(new GridLayout(rows, cols));
		if(title.length()>0) {
			myPanel.setBorder(new TitledBorder(title));
		}
	}
	
}
