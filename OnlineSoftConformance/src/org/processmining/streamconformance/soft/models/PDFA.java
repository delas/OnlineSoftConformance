package org.processmining.streamconformance.soft.models;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.processmining.framework.annotations.AuthoredType;
import org.processmining.models.graphbased.directed.AbstractDirectedGraph;
import org.processmining.models.graphbased.directed.DirectedGraph;
import org.processmining.models.graphbased.directed.DirectedGraphEdge;
import org.processmining.models.graphbased.directed.DirectedGraphElement;
import org.processmining.models.graphbased.directed.DirectedGraphNode;

@AuthoredType(
		typeName = "Probabilistic Deterministic Finite Automata (PDFA)",
		author = "A. Burattin",
		email = "",
		affiliation = "DTU")
public class PDFA extends AbstractDirectedGraph<PDFANode, PDFAEdge> {

	private Set<PDFANode> nodes = new LinkedHashSet<PDFANode>();
	private Set<PDFAEdge> edges = new LinkedHashSet<PDFAEdge>();
	
	public synchronized double getSeqenceProbability(String source, String target) {
		PDFAEdge e = findEdge(source, target);
		if (e == null) {
			return 0d;
		}
		return e.getProbability();
	}
	
	public synchronized boolean addNode(String label) {
		PDFANode snNode = new PDFANode(label, this);
		if (nodes.add(snNode)) {
			graphElementAdded(snNode);
			return true;
		} else {
			return false;
		}
	}
	
	public synchronized PDFANode findNode(Object identifier) {
		for (PDFANode s : nodes) {
			if (s.getLabel().equals(identifier)) {
				return s;
			}
		}
		return null;
	}
	
	public synchronized boolean addEdge(Object fromNodeID, Object toNodeID, double probability) {
		PDFANode source = findNode(fromNodeID);
		PDFANode target = findNode(toNodeID);
		checkAddEdge(source, target);
		PDFAEdge trans = new PDFAEdge(source, target, probability);
		if (edges.add(trans)) {
			graphElementAdded(trans);
			return true;
		} else {
			return false;
		}
	}
	
	public synchronized PDFAEdge findEdge(Object fromSNNode, Object toSNNode) {
		for (PDFAEdge t : getEdges(findNode(fromSNNode), findNode(toSNNode), getEdges())) {
			return t;
		}
		return null;
	}
	
	public synchronized PDFAEdge findEdge(Object fromSNNode, Object toSNNode, double probability) {
		for (PDFAEdge t : getEdges(findNode(fromSNNode), findNode(toSNNode), getEdges())) {
			if (t.getProbability() == probability) {
				return t;
			}
		}
		return null;
	}
	
	@Override
	public synchronized Set<PDFANode> getNodes() {
		return nodes;
	}

	@Override
	public synchronized Set<PDFAEdge> getEdges() {
		return edges;
	}

	@Override
	protected AbstractDirectedGraph<PDFANode, PDFAEdge> getEmptyClone() {
		return new PDFA();
	}

	@Override
	protected Map<? extends DirectedGraphElement, ? extends DirectedGraphElement> cloneFrom(
			DirectedGraph<PDFANode, PDFAEdge> graph) {
		assert (graph instanceof PDFA);
		Map<DirectedGraphElement, DirectedGraphElement> mapping = new HashMap<DirectedGraphElement, DirectedGraphElement>();
		
		PDFA orig = (PDFA) graph;
		for (PDFANode n : orig.nodes) {
			addNode(n.getLabel());
			mapping.put(n, findNode(n.getLabel()));
		}
		for (PDFAEdge e : orig.edges) {
			addEdge(e.getSource(), e.getTarget(), e.getProbability());
			mapping.put(e, findEdge(e.getSource(), e.getTarget(), e.getProbability()));
		}
		return mapping;
	}

	@Override
	public synchronized void removeNode(DirectedGraphNode node) {
		if (node instanceof PDFANode) {
			removePDFANodeInternal((PDFANode) node);
		} else {
			assert (false);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public synchronized void removeEdge(DirectedGraphEdge edge) {
		assert (edge instanceof PDFAEdge);
		PDFAEdge t = (PDFAEdge) edge;
		removePDFAEdge(t);
	}
	
	private synchronized PDFAEdge removePDFAEdge(PDFAEdge PDFAEdge) {
		PDFAEdge result = removeNodeFromCollection(edges, PDFAEdge);
		return (result == null ? null : result);
	}

	private synchronized PDFANode removePDFANodeInternal(PDFANode node) {
		PDFANode result = removeNodeFromCollection(nodes, findNode(node));
		return (result == null ? null : result);
	}
}
