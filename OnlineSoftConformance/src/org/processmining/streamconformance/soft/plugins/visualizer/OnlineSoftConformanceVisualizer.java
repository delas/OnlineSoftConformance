package org.processmining.streamconformance.soft.plugins.visualizer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.model.XTrace;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.contexts.uitopia.annotations.Visualizer;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.operationalsupport.xml.OSXMLConverter;
import org.processmining.streamconformance.soft.gui.widgets.ChartVisualizer;
import org.processmining.streamconformance.soft.gui.widgets.ConformanceListEntry;
import org.processmining.streamconformance.soft.gui.widgets.ConformanceListEntryRenderer;
import org.processmining.streamconformance.soft.models.stream.SoftConformanceStatus;
import org.processmining.streamconformance.soft.models.stream.SoftConformanceTracker;
import org.processmining.streamconformance.soft.utils.GUICustomUtils;
import org.processmining.streamconformance.soft.utils.UIColors;
import org.processmining.streamconformance.soft.utils.XLogHelper;

import com.fluxicon.slickerbox.components.SlickerTabbedPane;
import com.fluxicon.slickerbox.factory.SlickerFactory;

public class OnlineSoftConformanceVisualizer extends JPanel {

	private static final long serialVersionUID = -4781114837188265638L;

	protected OnlineSoftConformanceConfiguration configuration;
	protected UIPluginContext context;
	protected OSXMLConverter converter = new OSXMLConverter();
	protected Runtime runtime = Runtime.getRuntime();

	private int port;
	private InetAddress address;
	private JLabel status;
	private long chartsUpdateInteval = 5000;
	private long listUpdateInteval = 10000;
//	private double errorThreshold = 0.34;
	private ChartVisualizer tracesObservedChart;
	private ChartVisualizer memoryUsedChart;
	private ChartVisualizer eventsPerSecondChart;
	private ChartVisualizer errorsObservedChart;

	private Date startTime;
	private long eventsReceived = 0;
//	private long errorsObserved = 0;

	private SlickerTabbedPane tabs = SlickerFactory.instance().createTabbedPane("Online Soft Conformance", UIColors.lightGray,
			Color.white,
			Color.lightGray);
	private JButton start;
	private JButton stop;
	private DefaultListModel<ConformanceListEntry> dlm;
	private JList<ConformanceListEntry> list;
//	private JLabel errorDetails;
	private JComboBox<String> sort;

	private SoftConformanceTracker tracker;
	private Map<String, Comparator<String>> sortersAvailable;

	@Plugin(
			name = "Online Conformance Checker",
			parameterLabels = { "" },
			returnLabels = { "" },
			returnTypes = { JComponent.class },
			userAccessible = true)
	@UITopiaVariant(author = "A. Burattin", email = "", affiliation = "")
	@Visualizer(name = "Online Conformance Checker")
	public JComponent visualize(UIPluginContext context, OnlineSoftConformanceConfiguration configuration) {

		this.configuration = configuration;
		this.context = context;
		this.port = configuration.getPort();
		this.address = configuration.getAddress();
		this.tracker = new SoftConformanceTracker(configuration.getModel(), configuration.getNoMaxParallelInstances());

		this.dlm = new DefaultListModel<ConformanceListEntry>();
		this.list = new JList<ConformanceListEntry>(dlm);
		this.list.setCellRenderer(new ConformanceListEntryRenderer<>("Mean of prob."));
		this.list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.list.setBackground(UIColors.lightLightGray);

		this.sortersAvailable = new HashMap<String, Comparator<String>>();
		this.sortersAvailable.put("higher mean first", new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				SoftConformanceStatus cs1 = tracker.get(o1);
				SoftConformanceStatus cs2 = tracker.get(o2);
				if (cs1 == null || cs2 == null) {
					return 0;
				}
				if (cs1.getMeanProbabilities() > cs2.getMeanProbabilities()) {
					return 1;
				} else if (cs1.getMeanProbabilities() < cs2.getMeanProbabilities()) {
					return -1;
				} else {
					return cs2.getLastUpdate().compareTo(cs1.getLastUpdate());
				}
			}
		});
		this.sortersAvailable.put("higher sequence probability first", new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				SoftConformanceStatus cs1 = tracker.get(o1);
				SoftConformanceStatus cs2 = tracker.get(o2);
				if (cs1 == null || cs2 == null) {
					return 0;
				}
				if (cs1.getSequenceProbability() > cs2.getSequenceProbability()) {
					return -1;
				} else if (cs1.getSequenceProbability() < cs2.getSequenceProbability()) {
					return 1;
				} else {
					return cs2.getLastUpdate().compareTo(cs1.getLastUpdate());
				}
			}
		});
		this.sortersAvailable.put("recently updated", new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				SoftConformanceStatus cs1 = tracker.get(o1);
				SoftConformanceStatus cs2 = tracker.get(o2);
				if (cs1 == null || cs2 == null) {
					return 0;
				}
				if (cs1.getLastUpdate().compareTo(cs2.getLastUpdate()) < 0) {
					return 1;
				} else if (cs1.getLastUpdate().compareTo(cs2.getLastUpdate()) > 0) {
					return -1;
				} else {
					if (cs1.getSequenceProbability() > cs2.getSequenceProbability()) {
						return -1;
					} else if (cs1.getSequenceProbability() < cs2.getSequenceProbability()) {
						return 1;
					} else {
						return 0;
					}
				}
			}
		});

		initComponents();

		return this;
	}

	/*
	 * Graphical components initializer
	 */
	@SuppressWarnings("unchecked")
	private void initComponents() {

		setBackground(new Color(40, 40, 40));
		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		// main area
		// ---------------------------------------------------------------------

		// list of ongoing cases
		JScrollPane scrollPane = new JScrollPane(list);
		scrollPane.setBackground(UIColors.lightLightGray);
		scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		GUICustomUtils.customizeScrollBard(scrollPane);

//		errorDetails = new JLabel();
//		errorDetails.setText("Double click on an error entry to see details.");

		JPanel tracesArea = SlickerFactory.instance().createRoundedPanel(15, UIColors.lightLightGray);
		tracesArea.setLayout(new BorderLayout(0, 10));
		tracesArea.add(scrollPane, BorderLayout.CENTER);
//		tracesArea.add(errorDetails, BorderLayout.SOUTH);

		// statistics panel
		tracesObservedChart = new ChartVisualizer(Color.WHITE, "Traces in memory over time", false, 25);
		memoryUsedChart = new ChartVisualizer(Color.WHITE, "Memory usage over time (MB)", false, 25);
		eventsPerSecondChart = new ChartVisualizer(Color.WHITE, "Events / second over time", false, 50);
		errorsObservedChart = new ChartVisualizer(Color.RED, "Probability of each event", false, 20);

		JPanel memStatsArea = new JPanel();
		memStatsArea.setOpaque(false);
		memStatsArea.setLayout(new BoxLayout(memStatsArea, BoxLayout.X_AXIS));
		memStatsArea.add(tracesObservedChart);
		memStatsArea.add(memoryUsedChart);

		JPanel statsArea = SlickerFactory.instance().createRoundedPanel(15, UIColors.lightLightGray);
		statsArea.setLayout(new BoxLayout(statsArea, BoxLayout.Y_AXIS));
		statsArea.add(errorsObservedChart);
		statsArea.add(eventsPerSecondChart);
		statsArea.add(memStatsArea);

		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tracesArea, statsArea);
		split.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
		split.setOpaque(false);
		split.setDividerSize(10);
		split.setDividerLocation(400);

		split.setUI(new BasicSplitPaneUI() {
			@Override
			public BasicSplitPaneDivider createDefaultDivider() {
				return new BasicSplitPaneDivider(this) {
					private static final long serialVersionUID = 9030573699042574015L;

					@Override
					public void paint(Graphics g) {
						g.setColor(new Color(0, 0, 0, 0));
						g.fillRect(0, 0, getSize().width, getSize().height);
						super.paint(g);
					}

					@Override
					public void setBorder(Border b) {
					}
				};
			}
		});

		// sort / player controls
		// ---------------------------------------------------------------------
		start = SlickerFactory.instance().createButton("Start conformance checker");
		stop = SlickerFactory.instance().createButton("Stop");
		stop.setEnabled(false);
		start.addActionListener(new StartListener());
		stop.addActionListener(new StopListener());

		status = new JLabel("");

		sort = SlickerFactory.instance().createComboBox(sortersAvailable.keySet().toArray());
		JPanel sortContainer = new JPanel();
		sortContainer.setOpaque(false);
		sortContainer.setLayout(new FlowLayout(FlowLayout.RIGHT));
		sortContainer.add(GUICustomUtils.prepareLabel("Sort traces by:"));
		sortContainer.add(sort);

		JPanel buttonContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonContainer.setOpaque(false);
		buttonContainer.add(start);
		buttonContainer.add(stop);
		buttonContainer.add(Box.createHorizontalStrut(5));
		buttonContainer.add(status);

		JPanel sideContainer = new JPanel();
		sideContainer.setOpaque(false);
		sideContainer.setLayout(new BorderLayout());
		sideContainer.add(buttonContainer, BorderLayout.WEST);
		sideContainer.add(sortContainer, BorderLayout.EAST);

		// add everything to the main panel
		// ---------------------------------------------------------------------
		JPanel mainPanel = new JPanel();
		mainPanel.setOpaque(false);
		mainPanel.setLayout(new BorderLayout(0, 5));
		mainPanel.add(sideContainer, BorderLayout.SOUTH);
		mainPanel.add(split, BorderLayout.CENTER);

		// add the tab and the tab list
		tabs.addTab("Overall Information", mainPanel);

		setLayout(new BorderLayout(0, 5));
		add(tabs, BorderLayout.CENTER);
	}

	public void updateCharts() {
		double secondsSinceStart = (System.currentTimeMillis() - startTime.getTime()) / 1000d;

		double eventsPerSecond = eventsReceived / secondsSinceStart;
		double tracesObserved = tracker.keySet().size();
		double memoryUsed = (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024);

		// add observations to the charts
		eventsPerSecondChart.addObservation(eventsPerSecond);
		tracesObservedChart.addObservation(tracesObserved);
//		errorsObservedChart.addObservation(errorsObserved);
		memoryUsedChart.addObservation(memoryUsed);

//		System.out.println(new Date().toString() + "\t" + eventsPerSecond + "\t" + tracesObserved + "\t" + errorsObserved + "\t" + memoryUsed);

		// reset of the errors counter for this time fragment
//		errorsObserved = 0;
	}

	public void updateTraces() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				list.clearSelection();
				dlm.clear();
				List<String> handled = new LinkedList<String>(tracker.keySet());
				Collections.sort(handled, sortersAvailable.get(sort.getSelectedItem()));
//				double maxCost = Double.MIN_VALUE;
//				for (String caseId : handled) {
//					SoftConformanceStatus cs = tracker.get(caseId);
//					maxCost = Math.max(maxCost, cs.getMeanProbabilities());
//				}
				for (String caseId : handled) {
					SoftConformanceStatus cs = tracker.get(caseId);
					if (cs != null) {
						dlm.addElement(new ConformanceListEntry(
								caseId,
								(int) (100 * cs.getMeanProbabilities()),
								1 - cs.getMeanProbabilities(),
								cs.getLastUpdate(),
								false,
								null)
							);
					}
				}
			}

		});
	}

	protected void cleanupObjects() {
//		ProvidedObjectManager pom = context.getProvidedObjectManager();
//		List<ProvidedObjectID> allIDs = pom.getProvidedObjects();
//		Set<ProvidedObjectID> toRemove = new HashSet<ProvidedObjectID>();
//		// identify the object to be removed
//		for (ProvidedObjectID poId : allIDs) {
//			try {
//				String label = pom.getProvidedObjectLabel(poId);
//				// String type = pom.get
//				if (label.startsWith(ProcessInstanceDetails.DATA_OBJ_PREFIX)) {
//					toRemove.add(poId);
//				}
//			} catch (ProvidedObjectDeletedException e) {
//				e.printStackTrace();
//			}
//		}
//		// proceed with actual removal
//		for (ProvidedObjectID poId : toRemove) {
//			try {
//				pom.deleteProvidedObject(poId);
//			} catch (ProvidedObjectDeletedException e) {
//				// it's a tough world: there are always problems... just ignore them
//			}
//		}
	}

	/*
	 * Listener for the start button
	 */
	private class StartListener implements ActionListener {

		private boolean execute = true;
		private Thread listener;
		private Timer timerCharts;
		private Timer timerList;
		private Timer timerCleanup;

		@Override
		public void actionPerformed(ActionEvent e) {
			stop.setEnabled(true);
			start.setEnabled(false);

			resetPlayerThread();

			execute = true;
			startTime = new Date();
			listener.start();
		}

		public void resetPlayerThread() {
			listener = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Socket s = new Socket(address, port);

						status.setText("Stream started");
						status.setIcon(UIColors.loadingIcon);

						BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
						String str = "";
						XTrace t;
//						errorsObserved = 0;
						while (execute && (str = br.readLine()) != null) {
							status.setText((eventsReceived++) + " events observed");

							// extract the observed components
							t = (XTrace) converter.fromXML(str);
							String caseId = XConceptExtension.instance().extractName(t);
							String newEventName = XLogHelper.getStringAttribute(t.get(0), configuration.getEventAttributeName());

							// replay the extracted event
							SoftConformanceStatus returned = tracker.replay(caseId, newEventName);

							// check if the replay was an error
//							if (returned.getLastProbability() < errorThreshold) {
//								errorsObserved++;
//							}
							errorsObservedChart.addObservation(returned.getLastProbability());
						}
						br.close();
						s.close();

					} catch (IOException e) {
						JOptionPane.showMessageDialog(OnlineSoftConformanceVisualizer.this, e.getLocalizedMessage(), "Network Exception",
								JOptionPane.ERROR_MESSAGE);
					}

					stop.setEnabled(false);
					start.setEnabled(true);
					status.setText("Stream completed");
					status.setIcon(null);
				}
			});

			timerCharts = new Timer();
			timerList = new Timer();
			timerCleanup = new Timer();

			timerCharts.schedule(new TimerTask() {
				@Override
				public void run() {
					if (execute) {
						updateCharts();
					} else {
						timerCharts.cancel();
						timerCharts.purge();
					}
				}
			}, 1000, chartsUpdateInteval);

			timerList.schedule(new TimerTask() {
				@Override
				public void run() {
					if (execute) {
						updateTraces();
					} else {
						timerList.cancel();
						timerList.purge();
					}
				}
			}, 1000, listUpdateInteval);

			timerCleanup.schedule(new TimerTask() {
				@Override
				public void run() {
					if (execute) {
						cleanupObjects();
					} else {
						timerCleanup.cancel();
						timerCleanup.purge();
					}
				}
			}, 1000, 1000);
		}

		public void stop() {
			execute = false;

			try {
				listener.join();
			} catch (InterruptedException e) {
			}

			status.setText("Analyzer stopped");
			status.setIcon(null);
		}
	}

	/*
	 * Listener for the stop button
	 */
	private class StopListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			((StartListener) start.getActionListeners()[0]).stop();

			stop.setEnabled(false);
			start.setEnabled(true);
		}
	}
}
