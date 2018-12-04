package org.processmining.streamconformance.soft.models;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.processmining.models.graphbased.directed.AbstractDirectedGraphEdge;

public class PDFAEdge extends AbstractDirectedGraphEdge<PDFANode, PDFANode> {

	private double probability;
	
	public PDFAEdge(PDFANode source, PDFANode target, double probability) {
		super(source, target);
		this.probability = probability;
	}
	
	public void setProbability(double probability) {
		this.probability = probability;
	}
	
	public double getProbability() {
		return probability;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof PDFAEdge) {
			PDFAEdge e = (PDFAEdge) o;
			return new EqualsBuilder()
					.append(getSource(), e.getSource())
					.append(getTarget(), e.getTarget())
					.isEquals();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.append(getSource())
				.append(getTarget())
				.toHashCode();
	}
}
