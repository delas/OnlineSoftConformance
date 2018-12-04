package org.processmining.streamconformance.soft.plugins;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;

import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.contexts.uitopia.annotations.Visualizer;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginCategory;
import org.processmining.plugins.graphviz.dot.Dot;
import org.processmining.plugins.graphviz.dot.DotEdge;
import org.processmining.plugins.graphviz.dot.DotNode;
import org.processmining.plugins.graphviz.visualisation.DotPanel;
import org.processmining.streamconformance.soft.models.PDFA;
import org.processmining.streamconformance.soft.models.PDFAEdge;
import org.processmining.streamconformance.soft.models.PDFANode;

public class PDFAVisualizer {

	@Plugin(
			name = "PDFA Visualizer",
			parameterLabels = { "" },
			returnLabels = { "" },
			returnTypes = { JComponent.class },
			userAccessible = true)
	@UITopiaVariant(author = "A. Burattin", email = "", affiliation = "DTU")
	@Visualizer(name = "PDFA Visualizer")
	public static JComponent visualize(UIPluginContext context, PDFA pdfa) {
		Dot dot = new Dot();
		Map<String, DotNode> map = new HashMap<String, DotNode>();
		
		for (PDFANode node : pdfa.getNodes()) {
			map.put(node.getLabel(), dot.addNode(node.getLabel()));
		}
		
		for (PDFAEdge edge : pdfa.getEdges()) {
			DotEdge e = dot.addEdge(map.get(edge.getSource().getLabel()), map.get(edge.getTarget().getLabel()));
			e.setLabel(String.format("%.2f", edge.getProbability()));
			e.setOption("penwidth", Double.toString(0.5 + (edge.getProbability() * 5.0)));
		}
		
		return new DotPanel(dot);
	}
	
	@Plugin(
		name = "Crete dummy PDFA",
		returnLabels = { "A PDFA" },
		returnTypes = { PDFA.class },
		parameterLabels = { },
		categories = PluginCategory.Discovery,
		help = "This plugin is useful.",
		userAccessible = true)
	@UITopiaVariant(author = "A. Burattin", email = "", affiliation = "DTU")
	public static PDFA creator(PluginContext context) {
		PDFA pdfa = new PDFA();
		pdfa.addNode("A");
		pdfa.addNode("B");
		pdfa.addNode("C");
		pdfa.addEdge("A", "B", 0.8);
		pdfa.addEdge("A", "A", 0.2);
		pdfa.addEdge("B", "C", 1);
		pdfa.addEdge("C", "C", 1);
		return pdfa;
	}
}
