package com.mmxlabs.widgets.schedulechart.viewer;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.mmxlabs.widgets.schedulechart.IScheduleChartColourScheme;
import com.mmxlabs.widgets.schedulechart.IScheduleChartSettings;
import com.mmxlabs.widgets.schedulechart.ScheduleCanvas;
import com.mmxlabs.widgets.schedulechart.providers.IScheduleEventProvider;
import com.mmxlabs.widgets.schedulechart.providers.IScheduleEventStylingProvider;

public class ScheduleChartViewer<T> extends TypedViewer<T> {
	
	protected final ScheduleCanvas canvas;
	protected final IScheduleEventProvider<T> eventProvider;
	
	private T input;
	
	public ScheduleChartViewer(final Composite parent, IScheduleEventProvider<T> eventProvider, IScheduleEventStylingProvider eventStylingProvider) {
		this(new ScheduleCanvas(parent, eventStylingProvider), eventProvider);
	}
	public ScheduleChartViewer(final Composite parent, IScheduleChartSettings settings, IScheduleChartColourScheme colourScheme, IScheduleEventProvider<T> eventProvider, IScheduleEventStylingProvider eventStylingProvider) {
		this(new ScheduleCanvas(parent, eventStylingProvider, settings, colourScheme), eventProvider);
	}
	
	public ScheduleChartViewer(final ScheduleCanvas canvas, IScheduleEventProvider<T> eventProvider) {
		this.canvas = canvas;
		this.eventProvider = eventProvider;
		this.input = null;
		hookControl(canvas);
	}

	@Override
	public void typedSetInput(T input) {
		Control control = getControl();
		if (control == null || control.isDisposed()) {
			throw new IllegalStateException(
					"Need an underlying widget to be able to set the input." + //$NON-NLS-1$
							"(Has the widget been disposed?)"); //$NON-NLS-1$
		}
		
		T oldInput = getInput();
		this.input = input;
		
		// Call hook
		typedInputChanged(input, oldInput);
	}

	@Override
	public void typedInputChanged(T input, T oldInput) {
		canvas.clear();
		var newRows = eventProvider.classifyEventsIntoRows(eventProvider.getEvents(input));
		canvas.addAll(newRows);
		canvas.redraw();
	}

	@Override
	public T getInput() {
		return input;
	}

	@Override
	public Control getControl() {
		return canvas;
	}
	
	public ScheduleCanvas getCanvas() {
		return canvas;
	}

	@Override
	public ISelection getSelection() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSelection(ISelection selection, boolean reveal) {
		// TODO Auto-generated method stub
		
	}
	
	// Reimplementation of ContentViewer hookControl
	protected void hookControl(Control control) {
		control.addDisposeListener(this::handleDispose);
	}
	
	protected void handleDispose(DisposeEvent e) {
	}

}
