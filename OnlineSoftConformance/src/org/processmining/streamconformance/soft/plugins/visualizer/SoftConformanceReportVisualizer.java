package org.processmining.streamconformance.soft.plugins.visualizer;

import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.contexts.uitopia.annotations.Visualizer;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.streamconformance.soft.plugins.models.SoftConformanceReport;
import org.processmining.streamconformance.soft.plugins.models.SoftConformanceStatus;
import org.processmining.streamconformance.soft.utils.GUICustomUtils;
import org.processmining.streamconformance.soft.utils.UIColors;

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
		DecimalFormat df = new DecimalFormat("#.#####");
		
		String response = "<html><body bgcolor=\"#777777\" style=\"font-family:sans-serif;\">";
		response += "<table style=\"width: 100%;\"><tr style=\"color: #FFFFFF;\">"
				+ "<th align=\"left\">Case Id</th>"
				+ "<th>Soft conformance</th>"
				+ "<th>Mean of probabilities</th>"
//				+ "<th>Sequence probability</th>"
//				+ "<th>Sequence log probability</th>"
				+ "</tr>";
		
		for (String s : report.keySet()) {
			SoftConformanceStatus status = report.get(s);
			response += "<tr style=\"background-color: #C2C2C2;\">"
					+ "<th align=\"left\" style=\"padding: 5px ;\">" + s + "</th>"
					+ "<th>" + df.format(status.getSoftConformance()) + "</th>"
					+ "<td align=\"center\">" + df.format(status.getMeanProbabilities()) + "</td>"
//					+ "<td>" + df.format(status.getSequenceProbability()) + "</td>"
//					+ "<td>" + df.format(status.getSequenceLogProbability()) + "</td>"
					+ "</tr>";
		}
		
		response += "</table>";
		response += "</body></html>";
		
		JTextPane lblResponse = new JTextPane();
		lblResponse.setContentType("text/html");
		lblResponse.setText(response);
		lblResponse.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		lblResponse.setBackground(UIColors.lightGray);
		
		JScrollPane preparedWidget = new JScrollPane(lblResponse);
		preparedWidget.setBackground(UIColors.lightGray);
		preparedWidget.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		GUICustomUtils.customizeScrollBard(preparedWidget);
		return preparedWidget;
	}
}
