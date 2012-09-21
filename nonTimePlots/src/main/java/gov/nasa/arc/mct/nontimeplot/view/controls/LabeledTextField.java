package gov.nasa.arc.mct.nontimeplot.view.controls;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

public class LabeledTextField extends JPanel {
	private static final long serialVersionUID = 2570435681488814783L;
	private JLabel     label;
	private JTextField field;
	
	private OutputType outputType;
	
	public LabeledTextField(String text) {
		this(text, OutputType.DOUBLE);
	}
	
	public LabeledTextField(String text, OutputType outputType) {
		this.outputType = outputType;
		add(label = new JLabel(text));
		add(field = new JTextField(outputType.fieldSize()));
		SpringLayout layout = new SpringLayout();
		layout.putConstraint(SpringLayout.WEST, label, 0, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST, field, 0, SpringLayout.EAST, label);
		layout.putConstraint(SpringLayout.EAST, this, 0, SpringLayout.EAST, field);
		
		layout.putConstraint(SpringLayout.NORTH, field, 0, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.SOUTH, this,  0, SpringLayout.SOUTH, field);
		
		layout.putConstraint(SpringLayout.NORTH, label, 0, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.SOUTH, label, 0, SpringLayout.SOUTH, this);
		
	}
	
	public static enum OutputType {
		DOUBLE() {
			@Override
			int fieldSize() {
				return 8;
			}			
		},
		INTEGER() {
			@Override
			int fieldSize() {
				return 4;
			}			
		};
		abstract int fieldSize();
	}
}
