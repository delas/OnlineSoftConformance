package org.processmining.streamconformance.soft.plugins;

import java.util.HashMap;
import java.util.Map;

import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginCategory;
import org.processmining.models.graphbased.directed.socialnetwork.SNEdge;
import org.processmining.models.graphbased.directed.socialnetwork.SNNode;
import org.processmining.models.graphbased.directed.socialnetwork.SocialNetwork;
import org.processmining.streamconformance.soft.models.PDFA;
import org.processmining.streamconformance.soft.models.PDFAEdge;
import org.processmining.streamconformance.soft.models.PDFANode;

public class SocialNetworkToPDFA {

	@Plugin(
		name = "Convert social network to PDFA",
		returnLabels = { "A PDFA" },
		returnTypes = { PDFA.class },
		parameterLabels = { "The social network" },
		categories = PluginCategory.Discovery,
		help = "This plugin is useful.",
		userAccessible = true)
	@UITopiaVariant(author = "A. Burattin", email = "", affiliation = "DTU")
	public static PDFA social2PDFA(PluginContext context, SocialNetwork sn) {
		PDFA pdfa = new PDFA();
		
		// convert to PDFA
		for(SNNode n : sn.getNodes()) {
			pdfa.addNode(n.getLabel());
		}
		for(SNEdge e : sn.getEdges()) {
			pdfa.addEdge(e.getSource().getIdentifier(), e.getTarget().getIdentifier(), e.getWeight());
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
		
		Normalizer.normalize(context, pdfa);
		
		return pdfa;
	}
}
