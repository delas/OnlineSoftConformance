package org.processmining.streamconformance.soft.models;

import org.processmining.models.graphbased.directed.AbstractDirectedGraphNode;

public class PDFANode extends AbstractDirectedGraphNode {

	private PDFA graph;
	private String label;
	
	public PDFANode(String label, PDFA graph) {
		this.graph = graph;
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}
	
	@Override
	public boolean equals(Object o) {
		return (o instanceof PDFANode ? label.equals(((PDFANode) o).label) : false);
	}
	
	@Override
	public int hashCode() {
		return label.hashCode();
	}
	
	@Override
	public PDFA getGraph() {
		return graph;
	}
}
