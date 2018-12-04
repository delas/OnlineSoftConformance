package org.processmining.streamconformance.soft.gui.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JTextField;

import org.processmining.streamconformance.soft.utils.GUICustomUtils;

public class SoftConformanceParameters extends JPanel {

	private static final long serialVersionUID = -7669190410170225552L;
	private JTextField fieldNoMaxParallelInstances;
	private JTextField fieldBacktrackingSteps;
	private JTextField fieldErrorsPerTrace;
	private JTextField fieldNumberMostFrequentErrors;
	private JTextField fieldAttributeName;

	/**
	 * Basic class constructor
	 */
	public SoftConformanceParameters() {
		initComponents();
	}

	public int getNoMaxParallelInstances() {
		return Integer.parseInt(fieldNoMaxParallelInstances.getText());
	}

	public int getBacktrackingSteps() {
		return Integer.parseInt(fieldBacktrackingSteps.getText());
	}

	public int getNoErrorsPerTrace() {
		return Integer.parseInt(fieldErrorsPerTrace.getText());
	}

	public int getNoMostFrequentErrors() {
		return Integer.parseInt(fieldNumberMostFrequentErrors.getText());
	}

	public String getAttributeName() {
		return fieldAttributeName.getText();
	}

	/*
	 * Graphical components initializer
	 */
	private void initComponents() {
		fieldNoMaxParallelInstances = GUICustomUtils.prepareIntegerField(1000);
		fieldBacktrackingSteps = GUICustomUtils.prepareIntegerField(1);
		fieldErrorsPerTrace = GUICustomUtils.prepareIntegerField(10);
		fieldNumberMostFrequentErrors = GUICustomUtils.prepareIntegerField(100);
		fieldAttributeName = GUICustomUtils.prepareTextField("org:resource");
		GridBagConstraints c = new GridBagConstraints();

		setOpaque(false);
		setLayout(new GridBagLayout());

		int row = 0;
		c.gridx = 0;
		c.gridy = row;
		c.gridwidth = 2;
		c.insets = new Insets(0, 0, 15, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		add(GUICustomUtils.prepareLabel("Use this form to configure the size of the conformance data structures."), c);

		row++;
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = row;
		c.insets = new Insets(0, 0, 5, 0);
		add(GUICustomUtils.prepareLabel("Max no. of parallel instances:"), c);

		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = row;
		c.weightx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 5, 5, 0);
		add(GUICustomUtils.wrapInRoundedPanel(fieldNoMaxParallelInstances), c);
		

		row++;
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = row;
		c.insets = new Insets(0, 0, 5, 0);
		add(GUICustomUtils.prepareLabel("Attribute name to use for events:"), c);

		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = row;
		c.weightx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 5, 5, 0);
		add(GUICustomUtils.wrapInRoundedPanel(fieldAttributeName), c);

		// row++;
		// c = new GridBagConstraints();
		// c.gridx = 0;
		// c.gridy = row;
		// c.insets = new Insets(0, 0, 5, 0);
		// add(GUICustomUtils.prepareLabel("Max no. of backtracking steps:"), c);
		//
		// c = new GridBagConstraints();
		// c.gridx = 1;
		// c.gridy = row;
		// c.weightx = 1;
		// c.fill = GridBagConstraints.HORIZONTAL;
		// c.insets = new Insets(0, 5, 5, 0);
		// add(GUICustomUtils.wrapInRoundedPanel(fieldBacktrackingSteps), c);

//		row++;
//		c = new GridBagConstraints();
//		c.gridx = 0;
//		c.gridy = row;
//		c.insets = new Insets(0, 0, 5, 0);
//		add(GUICustomUtils.prepareLabel("Max no. of information on errors pre trace:"), c);
//
//		c = new GridBagConstraints();
//		c.gridx = 1;
//		c.gridy = row;
//		c.weightx = 1;
//		c.fill = GridBagConstraints.HORIZONTAL;
//		c.insets = new Insets(0, 5, 5, 0);
//		add(GUICustomUtils.wrapInRoundedPanel(fieldErrorsPerTrace), c);
//
//		row++;
//		c = new GridBagConstraints();
//		c.gridx = 0;
//		c.gridy = row;
//		c.insets = new Insets(0, 0, 5, 0);
//		c.anchor = GridBagConstraints.NORTH;
//		add(GUICustomUtils.prepareLabel("Max no. of information on frequent errors:"), c);
//
//		c = new GridBagConstraints();
//		c.gridx = 1;
//		c.gridy = row;
//		c.weightx = 1;
//		c.weighty = 1;
//		c.fill = GridBagConstraints.HORIZONTAL;
//		c.anchor = GridBagConstraints.NORTH;
//		c.insets = new Insets(0, 5, 5, 0);
//		add(GUICustomUtils.wrapInRoundedPanel(fieldNumberMostFrequentErrors), c);
	}
}
