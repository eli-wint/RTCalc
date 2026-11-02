package main.java;

import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Prompt extends JDialog {
	private JLabel nameLabel;
	private JTextField nameTextField;
	
	private JLabel mpgLabel;
	private JTextField mpgTextField;
	
	private JLabel routineMaintenanceLabel;
	private JTextField routineMaintenanceTextField;
	
	private JLabel largeMaintenanceLabel;
	private JTextField largeMaintenanceTextField;
	
	private JLabel smallMaintenanceLabel;
	private JTextField smallMaintenanceTextField;
	
	private JButton confirm;
	
	 public Prompt(JFrame parent) {
		 super(parent, "New Preset", true);
		 
		 setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		 setSize(300, 600);
		 
		 setLayout(new GridLayout(0, 2));
		 
		 nameLabel = createLabel("Model name: ");
		 add(nameLabel);
		 nameTextField = createTextField();
		 add(nameTextField);
		 
		 mpgLabel = createLabel("Vehicle MPG: ");
		 add(mpgLabel);
		 mpgTextField = createTextField();
		 add(mpgTextField);
		 
		 routineMaintenanceLabel = createLabel("Routine Maintenance: ");
		 add(routineMaintenanceLabel);
		 routineMaintenanceTextField = createTextField();
		 add(routineMaintenanceTextField);
		 
		 largeMaintenanceLabel = createLabel("Large Maintenance: ");
		 add(largeMaintenanceLabel);
		 largeMaintenanceTextField = createTextField();
		 add(largeMaintenanceTextField);
		 
		 smallMaintenanceLabel = createLabel("Small maintenance: ");
		 add(smallMaintenanceLabel);
		 smallMaintenanceTextField = createTextField();
		 add(smallMaintenanceTextField);
		 
		 confirm = new JButton("Confirm");
		 confirm.addActionListener(e -> {
			 dispose();
		 });
		 add(confirm);
		 
		 setVisible(true);
		 setResizable(false);
	 }
	 
	 private JLabel createLabel(String text) {
		 JLabel label = new JLabel(text);
		 
		 return label;
	 }
	 
	 private JTextField createTextField() {
		 JTextField textField = new JTextField();
		 
		 textField.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				textField.setText("");
			}
			@Override
			public void focusLost(FocusEvent e) {
			}
		 });
		 
		 return textField;
	 }
	 
	 public VehicleModel getVehicleModel() {
		 try {
			 return new VehicleModel(nameTextField.getText(),
					 toDouble(mpgTextField),
					 toDouble(routineMaintenanceTextField),
					 toDouble(largeMaintenanceTextField),
					 toDouble(smallMaintenanceTextField));
		 } catch (Exception e) {
			 return null;
		 }
	 }
	 
	 private double toDouble(JTextField input) {
		 return Double.parseDouble(input.getText());
	 }
}
