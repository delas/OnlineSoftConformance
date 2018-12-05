package org.processmining.streamconformance.soft.plugins;

import org.deckfour.uitopia.api.event.TaskListener.InteractionResult;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginCategory;
import org.processmining.streamconformance.soft.gui.panels.RandomWalkWeightSelection;
import org.processmining.streamconformance.soft.models.PDFA;
import org.processmining.streamconformance.soft.models.PDFAEdge;
import org.processmining.streamconformance.soft.models.PDFANode;

public class Normalizer {

	@Plugin(
		name = "Normalize PDFA to include deviations",
		returnLabels = { "A PDFA" },
		returnTypes = { PDFA.class },
		parameterLabels = { "A noise unaware PDFA" },
		categories = PluginCategory.Discovery,
		help = "This plugin is useful.",
		userAccessible = true)
	@UITopiaVariant(author = "A. Burattin", email = "", affiliation = "DTU")
	public static PDFA normalize(UIPluginContext context, PDFA pdfa) {
		RandomWalkWeightSelection rwws = new RandomWalkWeightSelection();
		InteractionResult ir = context.showConfiguration("Select the weight of random walk", rwws);
		if (ir.equals(InteractionResult.CANCEL)) {
			return null;
		}
		return normalize(context, pdfa, rwws.getWeight());
	}
	
	@Plugin(
		name = "Normalize PDFA to include deviations",
		returnLabels = { "A PDFA" },
		returnTypes = { PDFA.class },
		parameterLabels = { "A noise unaware PDFA" },
		categories = PluginCategory.Discovery,
		help = "This plugin is useful.",
		userAccessible = true)
	@UITopiaVariant(author = "A. Burattin", email = "", affiliation = "DTU")
	public static PDFA normalize(PluginContext context, PDFA pdfa, Double alpha) {
		
		System.out.println(alpha);
		PDFA newPdfa = pdfa.getNewCopy();
		
		// update old connections
		double ratio = 1d / newPdfa.getNodes().size();
		for (PDFAEdge edge : newPdfa.getEdges()) {
			double value = edge.getProbability();
			edge.setProbability((alpha * value) + ((1d - alpha) * ratio));
		}
		
		// add rest of connections
		for (PDFANode source : newPdfa.getNodes()) {
			for (PDFANode target : newPdfa.getNodes()) {
				if (newPdfa.findEdge(source.getLabel(), target.getLabel()) == null) {
					// there's need to add this edge
					newPdfa.addEdge(source.getLabel(), target.getLabel(), (1d - alpha) * ratio);
				}
			}
		}
		
		return newPdfa;
	}
}
