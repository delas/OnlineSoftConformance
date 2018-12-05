package org.processmining.streamconformance.soft.plugins.models;

import java.util.Date;

import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.processmining.streamconformance.soft.models.PDFA;

public class SoftConformanceStatus {

	private PDFA model;
	private String caseId;
	private String lastAct = null;
	private double lastProb = 0;
	private double prob = 1;
	private double logProb = 1;
	private Mean mean = new Mean();
	private long lastUpdate = System.currentTimeMillis();

	public SoftConformanceStatus(PDFA model, String caseId) {
		this.model = model;
		this.caseId = caseId;
	}

	public void replayEvent(String eventName) {
		if (lastAct != null) {
			lastProb = model.getSeqenceProbability(lastAct, eventName);
			prob *= lastProb;
			logProb += -Math.log(lastProb);
			mean.increment(lastProb);
		}
		
		lastUpdate = System.currentTimeMillis();
		lastAct = eventName;
	}
	
	public double getLastProbability() {
		return lastProb;
	}
	
	public double getSequenceProbability() {
		return prob;
	}
	
	public double getSequenceLogProbability() {
		return logProb;
	}
	
	public double getMeanProbabilities() {
		return mean.getResult();
	}
	
	public Date getLastUpdate() {
		return new Date(lastUpdate);
	}
	
	public String getCaseId() {
		return caseId;
	}
	
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof SoftConformanceStatus) && caseId.equals(((SoftConformanceStatus) obj).caseId);
	}
	
	@Override
	public int hashCode() {
		return caseId.hashCode();
	}
}
