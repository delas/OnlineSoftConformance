package org.processmining.streamconformance.soft.gui.widgets;

import java.util.Date;
import java.util.Queue;

import org.processmining.models.graphbased.directed.transitionsystem.State;
import org.processmining.streamconformance.soft.utils.Quadruple;

public class ConformanceListEntry {

	protected String caseId;
	protected int totalCost;
	protected double colorCost;
	protected Date lastUpdate;
	protected boolean traceReachedAcceptState;
	protected Queue<Quadruple<State, String, State, Integer>> errors;

	public ConformanceListEntry(String caseId, int totalCost, double colorCost, Date lastUpdate, boolean traceReachedAcceptState,
			Queue<Quadruple<State, String, State, Integer>> errors) {
		this.caseId = caseId;
		this.totalCost = totalCost;
		this.colorCost = colorCost;
		this.lastUpdate = lastUpdate;
		this.traceReachedAcceptState = traceReachedAcceptState;
		this.errors = errors;
	}

	public Queue<Quadruple<State, String, State, Integer>> getErrors() {
		return errors;
	}

	public String getCaseId() {
		return caseId;
	}

	public int getTotalCost() {
		return totalCost;
	}

	public double getColorCost() {
		return colorCost;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public boolean traceReachedAcceptState() {
		return traceReachedAcceptState;
	}
}
