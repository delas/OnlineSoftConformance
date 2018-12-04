package org.processmining.streamconformance.soft.plugins;

import org.deckfour.uitopia.api.event.TaskListener.InteractionResult;
import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginCategory;
import org.processmining.streamconformance.soft.gui.panels.AttributeSelection;
import org.processmining.streamconformance.soft.models.PDFA;

public class MinePDFA {

	@Plugin(
		name = "Extract PDFA",
		returnLabels = { "A PDFA from the model" },
		returnTypes = { PDFA.class },
		parameterLabels = {
			"An event log",
		},
		categories = PluginCategory.Discovery,
		help = "This plugin extracts a PDFA from the given log.",
		userAccessible = true)
	@UITopiaVariant(author = "A. Burattin", email = "", affiliation = "DTU")
	public static PDFA mine(UIPluginContext context, XLog log) {
		
		AttributeSelection as = new AttributeSelection();
		InteractionResult ir = context.showConfiguration("Select the attribute to be used for the events", as);
		if (ir.equals(InteractionResult.CANCEL)) {
			return null;
		}
		
		PDFA model = new PDFA();
		model.setAttributeNameUsed(as.getAttributeName());
		
		return model;
	}
}
