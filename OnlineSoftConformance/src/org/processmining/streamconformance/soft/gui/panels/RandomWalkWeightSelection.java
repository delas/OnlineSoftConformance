package org.processmining.streamconformance.soft.gui.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JTextField;

import org.processmining.streamconformance.soft.utils.GUICustomUtils;

public class RandomWalkWeightSelection extends JPanel {

	private static final long serialVersionUID = -7669190410170225552L;
	private JTextField fieldWeight;

	/**
	 * Basic class constructor
	 */
	public RandomWalkWeightSelection() {
		initComponents();
	}

	public double getWeight() {
		return Double.parseDouble(fieldWeight.getText());
	}

	private void initComponents() {
		fieldWeight = GUICustomUtils.prepareDoubleField(0.9, 0d, 1d);
		GridBagConstraints c = new GridBagConstraints();

		setOpaque(false);
		setLayout(new GridBagLayout());

		int row = 0;
		c.gridx = 0;
		c.gridy = row;
		c.gridwidth = 2;
		c.insets = new Insets(0, 0, 15, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		add(GUICustomUtils.prepareLabel("Use this form to configure the weight of the random walk."), c);

		row++;
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = row;
		c.anchor = GridBagConstraints.NORTH;
		c.insets = new Insets(5, 0, 5, 0);
		add(GUICustomUtils.prepareLabel("Weight (1 means only reference; 0 only random):"), c);

		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = row;
		c.weightx = 1;
		c.weighty = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.NORTH;
		c.insets = new Insets(0, 5, 5, 0);
		add(GUICustomUtils.wrapInRoundedPanel(fieldWeight), c);
	}
}
