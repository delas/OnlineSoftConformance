package org.processmining.streamconformance.soft.plugins;

import java.util.HashMap;
import java.util.Map;

import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginCategory;
import org.processmining.models.graphbased.directed.socialnetwork.SocialNetwork;
import org.processmining.streamconformance.soft.models.PDFA;
import org.processmining.streamconformance.soft.models.stream.SoftConformanceStatus;
import org.processmining.streamconformance.soft.utils.XLogHelper;

public class LogSoftConformance {

	@Plugin(
		name = "Soft Conformance - Handover of Work",
		returnLabels = { "A string representation of the conformance" },
		returnTypes = { String.class },
		parameterLabels = {
			"An event log",
			"The handover of work social network"
		},
		categories = PluginCategory.Analytics,
		help = "This plugin computes the conformance of a given model with respect to an event streams.",
		userAccessible = true)
	@UITopiaVariant(author = "A. Burattin", email = "", affiliation = "DTU")
	public static String plugin(PluginContext context, XLog log, SocialNetwork sn) {
		
		// construct the model
		PDFA model = SocialNetworkToPDFA.social2PDFA(context, sn);
		
		// compute conformance for each model
		Map<String, SoftConformanceStatus> conformance = new HashMap<String, SoftConformanceStatus>();
		for (XTrace t : log) {
			String caseId = XLogHelper.getName(t);
			SoftConformanceStatus tracker = new SoftConformanceStatus(model, caseId);
			for (XEvent e : t) {
				String event = XLogHelper.getStringAttribute(e, "org:resource");
				if (event != null) {
					tracker.replayEvent(event);
				}
			}
			conformance.put(caseId, tracker);
		}
		
		// prepare response
		String response = "<html><body>";
		response += "<table><tr><th>Case Id</th><th>Mean of probabilities</th><th>Sequence probability</th><th>Sequence log probability</th>";
		for (String s : conformance.keySet()) {
			SoftConformanceStatus status = conformance.get(s);
			response += "<tr>"
					+ "<td>" + s + "</td>"
					+ "<td>" + status.getMeanProbabilities() + "</td>"
					+ "<td>" + status.getSequenceProbability() + "</td>"
					+ "<td>" + status.getSequenceLogProbability() + "</td></tr>";
		}
		response += "</table>";
		response += "</body></html>";
		
		return response;
	}
	
}
