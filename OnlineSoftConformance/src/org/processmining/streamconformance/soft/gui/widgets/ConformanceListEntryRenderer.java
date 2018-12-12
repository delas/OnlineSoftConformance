package org.processmining.streamconformance.soft.gui.widgets;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;

import org.processmining.streamconformance.soft.utils.GUICustomUtils;
import org.processmining.streamconformance.soft.utils.UIColors;

import com.fluxicon.slickerbox.factory.SlickerFactory;

public class ConformanceListEntryRenderer<E> extends JPanel implements ListCellRenderer<E> {

	private static final long serialVersionUID = -1863880605178129502L;
	protected static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	protected static NumberFormat nf = new DecimalFormat("#0.00");

	private Color highlightBackground;
	private Border highlightBorder;
	private Color normalBackground;
	private Border normalBorder;

	private JPanel colorIndicator;
	private JLabel textLeft;
	private JLabel textRight;
	private JLabel textErrors;
	
	private String costLabel;

	/**
	 * Default constructor
	 */
	public ConformanceListEntryRenderer() {
		this("Cost");
	}
	
	public ConformanceListEntryRenderer(String costLabel) {
		this(Color.white, BorderFactory.createEmptyBorder(7, 5, 7, 5), UIColors.lightLightGray, BorderFactory.createEmptyBorder(7, 5, 7, 5));
		this.costLabel = costLabel;
	}
	
	/**
	 * Class constructor with some parameters to be set
	 *
	 * @param highlightBackground
	 * @param highlightBorder
	 * @param normalBorder
	 */
	public ConformanceListEntryRenderer(Color highlightBackground, Border highlightBorder, Color normalBackground, Border normalBorder) {

		this.highlightBackground = highlightBackground;
		this.highlightBorder = highlightBorder;
		this.normalBackground = normalBackground;
		this.normalBorder = normalBorder;

		this.colorIndicator = SlickerFactory.instance().createRoundedPanel(16, Color.red);
		this.textLeft = new JLabel();
		this.textRight = new JLabel();
		this.textErrors = new JLabel();

		textLeft.setFont(getFont().deriveFont(Font.PLAIN));
		textLeft.setOpaque(false);

		textRight.setFont(getFont().deriveFont(Font.PLAIN));
		textRight.setOpaque(false);
		textRight.setAlignmentX(JLabel.RIGHT);

		textErrors.setFont(getFont().deriveFont(Font.PLAIN));
		textErrors.setOpaque(false);

		setOpaque(true);
		setLayout(new BorderLayout(5, 0));
		add(colorIndicator, BorderLayout.WEST);
		add(textLeft, BorderLayout.CENTER);
		add(textRight, BorderLayout.EAST);
		add(textErrors, BorderLayout.SOUTH);
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends E> list, E value, int index, boolean isSelected, boolean cellHasFocus) {

		ConformanceListEntry entry = (ConformanceListEntry) value;

		colorIndicator.setBackground(GUICustomUtils.fromWeightToColor(entry.getColorCost()));
		textLeft.setText("<html><b>Case ID:</b> <em>" + entry.getCaseId().substring(0, Math.min(20, entry.getCaseId().length()))
				+ "</em>&nbsp;</b><br><b>" + costLabel + ":</b> " + entry.getTotalCost() + "&nbsp;</html>");
		textRight.setText("<html><div align=right>" + df.format(entry.getLastUpdate()) + "<br>"
				+ ((entry.traceReachedAcceptState()) ? "<small>Instance potentially finished</small>" : "&nbsp;") + "</div></html>");
		textErrors.setText("");

		if (list.isSelectedIndex(index)) {
			setBorder(highlightBorder);
			setBackground(highlightBackground);
		} else {
			setBorder(normalBorder);
			setBackground(normalBackground);
		}

		return this;
	}
}
