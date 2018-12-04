package org.processmining.streamconformance.soft.plugins.visualizer;

import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;

import org.deckfour.uitopia.api.event.TaskListener.InteractionResult;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginCategory;
import org.processmining.framework.util.Pair;
import org.processmining.models.graphbased.directed.socialnetwork.SocialNetwork;
import org.processmining.streamconformance.soft.gui.panels.NetworkConfiguration;
import org.processmining.streamconformance.soft.plugins.SocialNetworkToPDFA;

public class OnlineSoftConformancePlugin {

	@Plugin(
		name = "Online Soft Conformance - Handover of Work",
		returnLabels = { "A configuration for online soft conformance checker" },
		returnTypes = { OnlineSoftConformanceConfiguration.class },
		parameterLabels = {
			"The handover of work social network"
		},
		categories = PluginCategory.Analytics,
		help = "This plugin computes the conformance of a given model with respect to an event streams.",
		userAccessible = true)
	@UITopiaVariant(author = "A. Burattin", email = "", affiliation = "DTU")
	public static OnlineSoftConformanceConfiguration plugin(UIPluginContext context, SocialNetwork sn) throws UnknownHostException {

		// prepare the configuration panels
		NetworkConfiguration configNetwork = new NetworkConfiguration();
		SoftConformanceParameters configConformance = new SoftConformanceParameters();

		List<Pair<String, JPanel>> configurations = new LinkedList<Pair<String, JPanel>>();
		configurations.add(new Pair<String, JPanel>("Network Setup", configNetwork));
		configurations.add(new Pair<String, JPanel>("Conformance Parameters", configConformance));

		// ask the user for the configuration parameters
		InteractionResult result = InteractionResult.NEXT;
		int currentStep = 0;
		int nofSteps = configurations.size();
		boolean configurationOngoing = true;
		while (configurationOngoing && currentStep < nofSteps) {
			Pair<String, JPanel> config = configurations.get(currentStep);
			result = context.showWizard(
					config.getFirst(),
					currentStep == 0,
					currentStep == nofSteps - 1,
					config.getSecond());

			switch (result) {
			case NEXT:
				currentStep++;
				break;
			case PREV:
				currentStep--;
				break;
			case FINISHED:
				configurationOngoing = false;
				break;
			case CANCEL:
				return null;
			default:
				configurationOngoing = false;
				break;
			}
		}
		if (result != InteractionResult.FINISHED) {
			return null;
		}
		
		// get the final values
		OnlineSoftConformanceConfiguration occ = new OnlineSoftConformanceConfiguration();

		occ.setModel(SocialNetworkToPDFA.social2PDFA(context, sn));
		occ.setAddress(configNetwork.getAddress());
		occ.setPort(configNetwork.getPort());
		occ.setNoMaxParallelInstances(configConformance.getNoMaxParallelInstances());
		occ.setEventAttributeName(configConformance.getAttributeName());

		return occ;
	}
}
