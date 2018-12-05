package org.processmining.streamconformance.soft.test;

import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.processmining.contexts.cli.CLIContext;
import org.processmining.contexts.cli.CLIPluginContext;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.streamconformance.soft.models.PDFA;
import org.processmining.streamconformance.soft.plugins.Normalizer;
import org.processmining.streamconformance.soft.plugins.visualizer.PDFAVisualizer;

public class Tester {

	public static void main(String[] args) {
		
		PluginContext context = new CLIPluginContext(new CLIContext(), "c");
		PDFA pdfa = Normalizer.normalize(context, PDFAVisualizer.creator(context), 0.5);
		
		String[] strs = {
			"ABC",
			"AABC",
			"AAABC",
			"AAABCCC",
			"ACBCCCCC",
		};
		
		for (String str : strs) {
			System.out.println(" === Processing " + str + " ===");
			
			double prob = 1;
			double logProb = 1;
			Mean mean = new Mean();
			
			for(int i = 1; i < str.length(); i++) {
				String source = String.valueOf(str.charAt(i - 1));
				String target = String.valueOf(str.charAt(i));
				double probability = pdfa.getSeqenceProbability(source, target);
				
				prob *= probability;
				logProb += -Math.log(probability);
				mean.increment(probability);
			}
			System.out.println("Probability: " + prob);
			System.out.println("Log probability: " + logProb);
			System.out.println("Mean probability: " + mean.getResult());
			System.out.println("");
		}
	}
}
