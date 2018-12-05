package org.processmining.streamconformance.soft.plugins.visualizer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.processmining.contexts.uitopia.annotations.UIExportPlugin;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.streamconformance.soft.plugins.models.SoftConformanceReport;
import org.processmining.streamconformance.soft.plugins.models.SoftConformanceStatus;

@Plugin(name = "", returnLabels = {}, returnTypes = {}, parameterLabels = { "", "" }, userAccessible = true)
@UIExportPlugin(description = "CSV file", extension = "csv")
public class SoftConformanceReportExporter {

	@PluginVariant(variantLabel = "", requiredParameterLabels = { 0, 1 })
	public static void export(PluginContext context, SoftConformanceReport report, File file) throws FileNotFoundException {
		PrintWriter pw = new PrintWriter(file);
		StringBuilder sb = new StringBuilder();

		sb.append("\"Case Id\",");
		sb.append("\"Soft conformance\",");
		sb.append("\"Mean of probabilities\",");
		sb.append("\"Sequence probability\",");
		sb.append("\"Sequence log probability\"\n");

		for (String s : report.keySet()) {
			SoftConformanceStatus status = report.get(s);
			sb.append("\"" + s + "\",");
			sb.append(status.getSoftConformance() + ",");
			sb.append(status.getMeanProbabilities() + ",");
			sb.append(status.getSequenceProbability() + ",");
			sb.append(status.getSequenceLogProbability() + "\n");
		}

		pw.write(sb.toString());
		pw.close();
	}
}
