package org.processmining.streamconformance.soft.plugins.visualizer;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.contexts.uitopia.annotations.Visualizer;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.streamconformance.soft.plugins.models.SoftConformanceReport;
import org.processmining.streamconformance.soft.plugins.models.SoftConformanceStatus;

public class SoftConformanceReportVisualizer {

	@Plugin(
		name = "Soft Conformance Report Visualizer",
		parameterLabels = { "" },
		returnLabels = { "" },
		returnTypes = { JComponent.class },
		userAccessible = true)
	@UITopiaVariant(author = "A. Burattin", email = "", affiliation = "")
	@Visualizer(name = "Online Conformance Checker")
	public static JComponent visualize(UIPluginContext context, SoftConformanceReport report) {
		
		String response = "<html><body>";
		response += "<table border=1><tr>"
				+ "<th>Case Id</th>"
				+ "<th>Soft conformance</th>"
				+ "<th>Mean of probabilities</th>"
				+ "<th>Sequence probability</th>"
				+ "<th>Sequence log probability</th></tr>";
		
		for (String s : report.keySet()) {
			SoftConformanceStatus status = report.get(s);
			response += "<tr>"
					+ "<th>" + s + "</th>"
					+ "<th>" + status.getSoftConformance() + "</th>"
					+ "<td>" + status.getMeanProbabilities() + "</td>"
					+ "<td>" + status.getSequenceProbability() + "</td>"
					+ "<td>" + status.getSequenceLogProbability() + "</td></tr>";
		}
		
		response += "</table>";
		response += "</body></html>";
		
		JTextPane lblResponse = new JTextPane();
		lblResponse.setContentType("text/html");
		lblResponse.setText(response);
		
		return new JScrollPane(lblResponse);
	}
}
