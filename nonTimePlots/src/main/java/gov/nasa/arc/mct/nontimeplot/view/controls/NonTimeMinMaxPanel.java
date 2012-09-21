package gov.nasa.arc.mct.nontimeplot.view.controls;

import java.awt.BorderLayout;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class NonTimeMinMaxPanel extends JPanel {
	private static final long serialVersionUID = 4883137388714665327L;

	public NonTimeMinMaxPanel(boolean horizontal) {
		JLabel minLabel = new JLabel("Min: ");
		JLabel maxLabel = new JLabel("Max: ");
		JTextField minField = new JTextField(8);
		JTextField maxField = new JTextField(8);
		
		setLayout (new BorderLayout());
		
		//add (makePanel(minLabel, minField), horizontal ? BorderLayout.WEST : BorderLayout.SOUTH);
		//add (makePanel(maxLabel, maxField), horizontal ? BorderLayout.EAST : BorderLayout.NORTH);
		add (new LabeledTextField("Min: "), horizontal ? BorderLayout.WEST : BorderLayout.SOUTH);
		add (new LabeledTextField("Max: "), horizontal ? BorderLayout.EAST : BorderLayout.NORTH);
		add (horizontal ? Box.createHorizontalStrut(16) : Box.createVerticalStrut(12), BorderLayout.CENTER);		
	}
	
	private JPanel makePanel(JComponent left, JComponent right) {
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		p.add(left, BorderLayout.WEST);
		p.add(right, BorderLayout.EAST);
		return p;
	}
}
