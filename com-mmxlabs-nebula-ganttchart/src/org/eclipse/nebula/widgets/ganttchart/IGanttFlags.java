package org.eclipse.nebula.widgets.ganttchart;


/**
 * Valid styles outside of this class are as follows:
 * <p>
 * <code>SWT.SINGLE</code> - Single event selection in the chart<br>
 * <code>SWT.MULTI</code> - Multi event selection in the chart<br>
 * <p>
 * Any other styles not listed here will be ignored.
 */
interface IGanttFlags {

	// SWT.MULTI is taken (1 << 1)
	// SWT.SINGLE is taken (1 << 2)
	// SWT.BORDER is taken (1 << 11)
	
	/**
	 * Creates the chart with an infinite scrollbar. This is the default unless one is set explicitly.
	 */
	public static final int	H_SCROLL_INFINITE		= 1 << 3;

	/**
	 * Creates the chart with a scrollbar fixed to the range of the dates on the chart.
	 */
	public static final int	H_SCROLL_FIXED_RANGE	= 1 << 4;

	/**
	 * Creates the chart with no horizontal scrollbar. It's up to you to handle any horizontal scrolling manually.
	 */
	public static final int	H_SCROLL_NONE			= 1 << 5;

}
