package org.processmining.streamconformance.soft.plugins;

import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginCategory;
import org.processmining.streamconformance.soft.models.PDFA;
import org.processmining.streamconformance.soft.plugins.models.SoftConformanceReport;
import org.processmining.streamconformance.soft.plugins.models.SoftConformanceStatus;
import org.processmining.streamconformance.soft.utils.XLogHelper;

public class LogSoftConformance {

	@Plugin(
		name = "Soft Conformance",
		returnLabels = { "A string representation of the conformance" },
		returnTypes = { SoftConformanceReport.class },
		parameterLabels = {
			"An event log",
			"The PDFA"
		},
		categories = PluginCategory.ConformanceChecking,
		help = "This plugin computes the conformance of a given model with respect to an event streams.",
		userAccessible = true)
	@UITopiaVariant(author = "A. Burattin", email = "", affiliation = "DTU")
	public static SoftConformanceReport plugin(PluginContext context, XLog log, PDFA model) {
		SoftConformanceReport conformance = new SoftConformanceReport();
		for (XTrace t : log) {
			String caseId = XLogHelper.getName(t);
			SoftConformanceStatus tracker = new SoftConformanceStatus(model, caseId);
			for (XEvent e : t) {
				String event = XLogHelper.getStringAttribute(e, model.getAttributeNameUsed());
				if (event != null) {
					tracker.replayEvent(event);
				}
			}
			conformance.put(caseId, tracker);
		}
		return conformance;
	}
	
}
