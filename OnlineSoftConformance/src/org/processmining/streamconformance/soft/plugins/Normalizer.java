package org.processmining.streamconformance.soft.plugins;

import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginCategory;
import org.processmining.streamconformance.soft.models.PDFA;
import org.processmining.streamconformance.soft.models.PDFAEdge;
import org.processmining.streamconformance.soft.models.PDFANode;

public class Normalizer {

	@Plugin(
		name = "Normalize PDFA to include deviations (with mean)",
		returnLabels = { "A PDFA" },
		returnTypes = { PDFA.class },
		parameterLabels = { "A noise unaware PDFA" },
		categories = PluginCategory.Discovery,
		help = "This plugin is useful.",
		userAccessible = true)
	@UITopiaVariant(author = "A. Burattin", email = "", affiliation = "DTU")
	public static PDFA normalize(PluginContext context, PDFA pdfa) {
		double alpha = 0.5;
		
		// update old connections
		double ratio = 1d / pdfa.getNodes().size();
		for (PDFAEdge edge : pdfa.getEdges()) {
			double value = edge.getProbability();
			edge.setProbability((alpha * value) + ((1d - alpha) * ratio));
		}
		
		// add rest of connections
		for (PDFANode source : pdfa.getNodes()) {
			for (PDFANode target : pdfa.getNodes()) {
				if (pdfa.findEdge(source.getLabel(), target.getLabel()) == null) {
					// there's need to add this edge
					pdfa.addEdge(source.getLabel(), target.getLabel(), (1d - alpha) * ratio);
				}
			}
		}
		
		return pdfa;
	}
}
