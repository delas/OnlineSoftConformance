package org.processmining.streamconformance.soft.plugins.visualizer;

import java.net.InetAddress;

import org.processmining.framework.annotations.AuthoredType;
import org.processmining.streamconformance.soft.models.PDFA;

@AuthoredType(
	typeName = "Online Soft Conformance Configuration",
	author = "A. Burattin",
	email = "",
	affiliation = "DTU")
public class OnlineSoftConformanceConfiguration {

	protected PDFA model;
	protected int port;
	protected InetAddress address;
	protected int noMaxParallelInstances;
	protected String eventAttributeName;
	
	public String getEventAttributeName() {
		return eventAttributeName;
	}

	public void setEventAttributeName(String eventAttributeName) {
		this.eventAttributeName = eventAttributeName;
	}

	public PDFA getModel() {
		return model;
	}
	
	public void setModel(PDFA model) {
		this.model = model;
	}
	
	public int getPort() {
		return port;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public InetAddress getAddress() {
		return address;
	}
	
	public void setAddress(InetAddress address) {
		this.address = address;
	}
	
	public int getNoMaxParallelInstances() {
		return noMaxParallelInstances;
	}
	
	public void setNoMaxParallelInstances(int noMaxParallelInstances) {
		this.noMaxParallelInstances = noMaxParallelInstances;
	}
}
