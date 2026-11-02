package main.java;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class RoadTripCalculator extends JFrame {
	private ArrayList<Setting> firmSettings;

	private String[] firmNames;
	private static double[] firmCosts;
	/*
	 * [0] Routine maintenance costs (per 5k miles) [1] Large maintenance costs (per
	 * 100k miles) [2] Tire replacement costs (per 80k miles)
	 */

	private ArrayList<Setting> softSettings;

	private String[] softNames;
	private static double[] softCosts;
	/*
	 * [0] Miles Travelled [1] Gas Price in $ [2] Miles Per Gallon
	 */

	// Other
	private double totalCost;

	// UI Layout
	private JMenuBar menuBar;
	private JButton home;
	private JButton result;
	private JButton settings;
	private JButton calculate;
	private JMenu presets;

	private CardLayout layout;
	private JPanel cards;

	private JPanel homePanel;
	private JPanel resultPanel;
	private JPanel settingsPanel;

	public RoadTripCalculator() {
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				HibernateIO.shutdown();
				dispose();
				System.exit(0);
			}
		});

		setTitle("Road Trip Calculator");

		layout = new CardLayout();
		cards = new JPanel(layout);
		add(cards);

		setResizable(false);
		pack();
		setLocationRelativeTo(null);
		setSize(600, 250);

		menuBar = new JMenuBar();
		presets = new JMenu("Presets");

		homePanel = new JPanel();
		homePanel.setLayout(new BoxLayout(homePanel, BoxLayout.Y_AXIS));
		homePanel.setAlignmentX(CENTER_ALIGNMENT);

		resultPanel = new JPanel();
		resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
		resultPanel.setAlignmentX(CENTER_ALIGNMENT);

		settingsPanel = new JPanel();
		settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));
		settingsPanel.setAlignmentX(CENTER_ALIGNMENT);

		instantiateVariables();

		// HOME
		// ==============================================================================
		home = createNavButton("Home", "HOME");
		homePanel.add(createTitleLabel("Travel Costs"), 0);
		cards.add(homePanel, "HOME");

		// RESULT
		// ============================================================================
		result = createNavButton("Result", "RESULT");
		JLabel gasCost = createCostLabel("Gas per mile: N/A");
		JLabel routineCost = createCostLabel("Routine maintenance: N/A");
		JLabel largeCost = createCostLabel("Large maintenance: N/A");
		JLabel tireCost = createCostLabel("Tire maintenance: N/A");
		JLabel finalCost = createCostLabel("Final Cost: N/A");
		resultPanel.add(createTitleLabel("Costs"));

		resultPanel.add(gasCost);
		resultPanel.add(routineCost);
		resultPanel.add(largeCost);
		resultPanel.add(tireCost);
		resultPanel.add(finalCost);
		cards.add(resultPanel, "RESULT");

		// SETTINGS
		// ==========================================================================
		settings = createNavButton("Settings", "SETTINGS");
		settingsPanel.add(createTitleLabel("Maintenance Costs"), 0);
		cards.add(settingsPanel, "SETTINGS");

		calculate = new JButton("Calculate");
		calculate.setFont(new Font(Font.DIALOG, Font.BOLD, 12));
		calculate.setBackground(Color.green);
		calculate.addActionListener(e -> {
			boolean valid = true;

			for (int i = 0; i < softCosts.length; i++) {
				softCosts[i] = softSettings.get(i).getInput();
			}

			for (double cost : softCosts) {
				valid = (cost <= 0.00) ? false : true;
			}

			if (!valid) {
				JDialog dialog = new JDialog(this, "Error", true);
				dialog.setSize(150, 110);

				dialog.setLayout(new BorderLayout());
				JLabel label = new JLabel("Fill in all values!");
				label.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
				label.setMaximumSize(label.getPreferredSize());
				label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
				label.setAlignmentX(CENTER_ALIGNMENT);

				JPanel labelPanel = new JPanel();
				labelPanel.add(label);

				dialog.add(labelPanel, BorderLayout.NORTH);

				JButton button = new JButton("OK");
				button.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
				button.setMaximumSize(button.getPreferredSize());
				button.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
				button.setAlignmentX(CENTER_ALIGNMENT);

				button.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						dialog.dispose();
					}
				});

				JPanel buttonPanel = new JPanel();
				buttonPanel.add(button);
				dialog.add(buttonPanel, BorderLayout.SOUTH);

				dialog.setLocationRelativeTo(this);
				dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
				dialog.setVisible(true);
				dialog.setResizable(false);

				gasCost.setText("Gas per mile: N/A");
				routineCost.setText("Routine mintenance: N/A");
				largeCost.setText("Large maintenance: N/A");
				tireCost.setText("Tire maintenance");
				finalCost.setText("Final Cost: N/A");
			} else {
				totalCost = 0.0;
				double gas = (softCosts[0] / softCosts[2]) * softCosts[1]; // Gas cost per mile
				double routine = (firmCosts[0] / 5000) * softCosts[0]; // Routine maintenance per mile
				double large = (firmCosts[1] / 100000) * softCosts[0]; // Large maintenance per mile
				double tire = (firmCosts[2] / 80000) * softCosts[0]; // Tire maintenance per mile
				totalCost += toDollars(gas);
				totalCost += toDollars(routine);
				totalCost += toDollars(large);
				totalCost += toDollars(tire);

				gasCost.setText("Gas per mile: $" + String.format("%.2f", gas));
				routineCost.setText("Routine maintenance: $" + String.format("%.2f", routine));
				largeCost.setText("Large maintenance: $" + String.format("%.2f", large));
				tireCost.setText("Tire maintenance: $" + String.format("%.2f", tire));
				finalCost.setText("Final Cost: $" + String.format("%.2f", totalCost));

				resultPanel.revalidate();
				resultPanel.repaint();

				layout.show(cards, "RESULT");
			}
		});

		// PRESETS ==================================================================

		List<VehicleModel> models = HibernateIO.getVehicleModels();
		
		JButton addNewPreset = new JButton("Add Preset");
		addNewPreset.addActionListener(e -> {
			Prompt dialog = new Prompt(this);
			VehicleModel vehicle = dialog.getVehicleModel();
			if (vehicle != null) {
				HibernateIO.save(vehicle);
			}
		});

		presets.add(addNewPreset);
		
		for (VehicleModel vehicle : models) {
			presets.add(createPreset(vehicle));
		}
		
		menuBar.add(presets);
		
		menuBar.add(home);
		menuBar.add(result);
		menuBar.add(settings);
		menuBar.add(calculate);

		setJMenuBar(menuBar);

		setVisible(true); // DO THIS AT THE END
	}

	private JButton createNavButton(String title, String card) {
		Font font = new Font(Font.DIALOG, Font.BOLD, 12);
		JButton button = new JButton(title);
		button.setFont(font);
		button.addActionListener(e -> {
			layout.show(cards, card);
		});
		return button;
	}

	private JLabel createTitleLabel(String title) {
		Font font = new Font(Font.DIALOG, Font.BOLD, 20);
		JLabel label = new JLabel(title);
		label.setFont(font);
		label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		label.setAlignmentX(CENTER_ALIGNMENT);

		return label;
	}

	private JLabel createCostLabel(String text) {
		Font font = new Font(Font.DIALOG, Font.PLAIN, 15);
		JLabel label = new JLabel(text);
		label.setFont(font);
		label.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		label.setAlignmentX(CENTER_ALIGNMENT);
		return label;
	}

	private double toDollars(double amount) {
		return (int) ((amount + 0.005) * 100) / 100.0;
	}

	private void instantiateVariables() {
		firmNames = new String[3];
		firmNames[0] = "Routine, per 5k";
		firmNames[1] = "Large, per 100k";
		firmNames[2] = "Tires, per 80k";
		firmCosts = new double[3];

		softNames = new String[3];
		softNames[0] = "Miles Travelled";
		softNames[1] = "Gas Price $";
		softNames[2] = "MPG";
		softCosts = new double[3];

		firmSettings = new ArrayList<Setting>();
		softSettings = new ArrayList<Setting>();

		for (int i = 0; i < 3; i++) {
			firmSettings.add(new Setting(firmNames[i], firmCosts[i], i, false));
			softSettings.add(new Setting(softNames[i], softCosts[i], i, true));
		}

		for (Setting set : softSettings) {
			homePanel.add(set);
			homePanel.add(Box.createVerticalStrut(10));
		}

		for (Setting set : firmSettings) {
			settingsPanel.add(set);
			settingsPanel.add(Box.createVerticalStrut(10));
		}

		homePanel.revalidate();
		homePanel.repaint();

		resultPanel.revalidate();
		resultPanel.repaint();

		settingsPanel.revalidate();
		settingsPanel.repaint();
	}

	public static void setSoftCost(double cost, int index) {
		softCosts[index] = cost;
	}

	public static void setFirmCost(double cost, int index) {
		firmCosts[index] = cost;
	}

	private JMenuItem createPreset(VehicleModel vehicle) {
		JMenuItem item = new JMenuItem(vehicle.getName());
		item.addActionListener(e -> {
			firmCosts[0] = vehicle.getRoutineMaintenanceCost();
			firmSettings.get(0).setText("" + firmCosts[0]);
			firmCosts[1] = vehicle.getLargeMaintenanceCost();
			firmSettings.get(1).setText("" + firmCosts[1]);
			firmCosts[2] = vehicle.getSmallMaintenanceCost();
			firmSettings.get(2).setText("" + firmCosts[2]);

			softCosts[2] = vehicle.getMPG();
			softSettings.get(2).setText("" + softCosts[2]);

			layout.show(cards, "HOME");
		});

		return item;
	}
}
