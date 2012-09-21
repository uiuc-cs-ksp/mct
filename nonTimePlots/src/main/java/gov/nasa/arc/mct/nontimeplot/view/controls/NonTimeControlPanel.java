package gov.nasa.arc.mct.nontimeplot.view.controls;

import gov.nasa.arc.mct.nontimeplot.view.controls.LabeledTextField.OutputType;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

public class NonTimeControlPanel extends JPanel {
	private static final long serialVersionUID = -1416453078528423628L;

	private static Icon EXAMPLE_PLOT;
	private JComponent control[] = new JComponent[3]; // x, y, z
	private JComponent image = null;
	
	
	
	public NonTimeControlPanel() {
		if (EXAMPLE_PLOT == null) {
			EXAMPLE_PLOT = new ImageIcon(getClass().getClassLoader().getResource("images/nonTimeExample.png"));
		}
		

		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder("Plot Setup"));
		
		control[0] = new NonTimeMinMaxPanel(true);
		control[1] = new NonTimeMinMaxPanel(false);
		control[2] = new LabeledTextField("Data Points: ", OutputType.INTEGER);
		image = new JLabel(EXAMPLE_PLOT);
		
		for (JComponent c : control) panel.add(c);
		panel.add(image);
		
		setupLayout(panel);
		
		setLayout(new BorderLayout());
		JPanel p = new JPanel (new BorderLayout());
		p.add(panel, BorderLayout.WEST);
		add(p, BorderLayout.NORTH);
		
	}
	
	private void setupLayout(JPanel p) {
		SpringLayout layout = new SpringLayout();
		p.setLayout(layout);
		
		layout.putConstraint(SpringLayout.WEST, control[1], 0, SpringLayout.WEST, p);
		layout.putConstraint(SpringLayout.WEST, image, 0, SpringLayout.EAST, control[1]);
		layout.putConstraint(SpringLayout.EAST, p, 0, SpringLayout.EAST, image);
		
		layout.putConstraint(SpringLayout.NORTH, image, 0, SpringLayout.NORTH, p);
		layout.putConstraint(SpringLayout.NORTH, control[0], 0, SpringLayout.SOUTH, image);
		layout.putConstraint(SpringLayout.SOUTH, p, 0, SpringLayout.SOUTH, control[0]);
		
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, control[1], 0, SpringLayout.VERTICAL_CENTER, image);
		layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, control[0], 0, SpringLayout.HORIZONTAL_CENTER, image);
		
//		layout.putConstraint(SpringLayout.WEST,  control[2], 0, SpringLayout.WEST,  p);
//		layout.putConstraint(SpringLayout.SOUTH, control[2], 0, SpringLayout.SOUTH, p);
		layout.putConstraint(SpringLayout.NORTH, control[2], 0, SpringLayout.SOUTH, image);
		layout.putConstraint(SpringLayout.EAST,  control[2], 0, SpringLayout.WEST,  image);
		
	}
	
}
