package main.java;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Setting extends JPanel {
	private String title;
	private double value;
	private int index;
	private boolean isSoft;

	private JLabel label;
	private JButton button;
	private JTextField textField;

	public Setting(String title, double value, int index, boolean isSoft) {
		this.title = title;
		this.value = value;
		this.index = index;
		this.isSoft = isSoft;

		Font font = new Font(Font.DIALOG, Font.PLAIN, 15);

		setLayout(new GridLayout(0, 3));
		setAlignmentX(CENTER_ALIGNMENT);

		label = new JLabel(title);
		label.setFont(font);
		label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		label.setMaximumSize(label.getPreferredSize());

		button = new JButton("Confirm");
		button.setFont(font);
		button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		button.setMaximumSize(button.getPreferredSize());
		button.addActionListener(e -> {
			double input = getInput();
			if (input > 0.0) {
				if (isSoft) {
					RoadTripCalculator.setSoftCost(input, index);
				} else {
					RoadTripCalculator.setFirmCost(input, index);
				}
				System.out.println("SET " + title + " -> " + input);
			}
		});

		textField = new JTextField((isSoft) ? "Enter value" : "" + value);
		textField.setFont(font);
		textField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		textField.setMaximumSize(textField.getPreferredSize());
		textField.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				textField.setText("");
				textField.setForeground(Color.black);
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (textField.getText().isEmpty()) {
					textField.setText("Enter value");
					textField.setForeground(Color.black);
				}
			}
		});
		add(label, 0);
		add(textField, 1);
		add(button, 2);
	}

	public double getInput() {
		String input = textField.getText().replace(",", "");
		if (input.matches("\\d*\\.?\\d+")) {
			System.out.println("SET " + title + " -> " + input);
			return Double.parseDouble(input);
		} else {
			textField.setText("INVALID");
			textField.setForeground(Color.red);
			return 0.0;
		}
	}

	public void setText(String text) {
		textField.setText(text);
	}
}
