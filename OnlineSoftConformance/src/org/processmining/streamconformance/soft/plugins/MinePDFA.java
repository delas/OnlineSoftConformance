package org.processmining.streamconformance.soft.plugins;

import java.util.HashMap;
import java.util.Map;

import org.deckfour.uitopia.api.event.TaskListener.InteractionResult;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginCategory;
import org.processmining.streamconformance.soft.gui.panels.AttributeSelection;
import org.processmining.streamconformance.soft.models.PDFA;
import org.processmining.streamconformance.soft.models.PDFAEdge;
import org.processmining.streamconformance.soft.models.PDFANode;
import org.processmining.streamconformance.soft.utils.XLogHelper;

public class MinePDFA {

	@Plugin(
		name = "Extract PDFA",
		returnLabels = { "A PDFA from the model" },
		returnTypes = { PDFA.class },
		parameterLabels = {
			"An event log",
		},
		categories = PluginCategory.Discovery,
		help = "This plugin extracts a PDFA from the given log.",
		userAccessible = true)
	@UITopiaVariant(author = "A. Burattin", email = "", affiliation = "DTU")
	public static PDFA mine(UIPluginContext context, XLog log) {
		
		AttributeSelection as = new AttributeSelection();
		InteractionResult ir = context.showConfiguration("Select the attribute to be used for the events", as);
		if (ir.equals(InteractionResult.CANCEL)) {
			return null;
		}
		
		PDFA pdfa = new PDFA();
		pdfa.setAttributeNameUsed(as.getAttributeName());
		
		// add all frequencies
		for (XTrace t : log) {
			String prevEvent = null;
			for (XEvent e : t) {
				String event = XLogHelper.getStringAttribute(e, as.getAttributeName());
				if (event != null) {
					if (pdfa.findNode(event) == null) {
						pdfa.addNode(event);
					}
					if (prevEvent != null) {
						PDFAEdge edge = pdfa.findEdge(prevEvent, event);
						if (edge == null) {
							pdfa.addEdge(prevEvent, event, 1);
						} else {
							edge.setProbability(edge.getProbability() + 1);
						}
					}
					prevEvent = event;
				}
			}
		}
		
		// normalize weights such that all outgoing from same edge sum to 1
		Map<PDFANode, Double> sums = new HashMap<PDFANode, Double>();
		for (PDFANode source : pdfa.getNodes()) {
			double sum = 0;
			for (PDFAEdge out : pdfa.getOutEdges(source)) {
				sum += out.getProbability();
			}
			sums.put(source, sum);
		}
		
		for (PDFAEdge edge : pdfa.getEdges()) {
			edge.setProbability(edge.getProbability() / sums.get(edge.getSource()));
		}
		
		return pdfa;
	}
}
