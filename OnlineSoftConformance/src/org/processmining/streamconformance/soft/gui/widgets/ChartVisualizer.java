package org.processmining.streamconformance.soft.gui.widgets;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.DefaultXYItemRenderer;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.processmining.streamconformance.soft.utils.GUICustomUtils;

public class ChartVisualizer extends JPanel {

	private static final long serialVersionUID = -2481373568509521714L;
	private ChartPanel chartPanel;
	private TimeSeries observations;

	public ChartVisualizer(Color foreground, String title) {
		this(foreground, title, false, 50);
	}

	public ChartVisualizer(Color foreground, String title, boolean areaAxis, int maxItemShown) {
		super(new BorderLayout(5, 5));

		this.observations = new TimeSeries(title);
		this.observations.setMaximumItemCount(maxItemShown);
		this.setOpaque(false);

		TimeSeriesCollection dataset = new TimeSeriesCollection();
		dataset.addSeries(observations);

		DateAxis domain = new DateAxis();
		NumberAxis range = new NumberAxis();

		XYItemRenderer renderer = new DefaultXYItemRenderer();
		if (areaAxis) {
			renderer = new XYAreaRenderer();
		}
		renderer.setSeriesPaint(0, foreground);
		renderer.setSeriesShape(2, new Rectangle());
		renderer.setSeriesStroke(0, new BasicStroke(2f));

		XYPlot xyplot = new XYPlot(dataset, domain, range, renderer);
		xyplot.setBackgroundPaint(Color.darkGray);

		domain.setAutoRange(true);
		domain.setLowerMargin(0.0);
		domain.setUpperMargin(0.0);
		domain.setTickLabelsVisible(false);

		range.setTickMarksVisible(true);
		range.setTickLabelsVisible(true);
		range.setRange(0.0, 100.0);
		range.setAutoRange(true);
		range.setAutoRangeIncludesZero(true);

		JFreeChart chart = new JFreeChart(null, null, xyplot, false);
		chart.setBackgroundPaint(Color.lightGray);
		chart.setAntiAlias(true);

		chartPanel = new ChartPanel(chart);
		// chartPanel.setPreferredSize(new Dimension(800, 100));
		// chartPanel.setMaximumDrawHeight(100);
		// chartPanel.setMinimumDrawHeight(10);
		//
		// chartPanel.setMaximumDrawWidth(2000);
		// chartPanel.setMinimumDrawWidth(10);

		add(GUICustomUtils.prepareLabel(title), BorderLayout.NORTH);
		add(chartPanel, BorderLayout.CENTER);
		setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 0));

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				chartPanel.setMaximumDrawHeight(e.getComponent().getHeight());
				chartPanel.setMaximumDrawWidth(e.getComponent().getWidth());
				chartPanel.setMinimumDrawWidth(e.getComponent().getWidth());
				chartPanel.setMinimumDrawHeight(e.getComponent().getHeight());
				chartPanel.updateUI();
				chartPanel.repaint();
			}
		});
	}

	/**
	 * Adds an observation to the time series.
	 *
	 * @param newObservation
	 *            the new observation
	 */
	public void addObservation(double newObservation) {
		this.observations.add(new Millisecond(), newObservation);
	}

}