/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart.viewer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.IElementComparer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.mmxlabs.widgets.schedulechart.IScheduleChartEventListener;
import com.mmxlabs.widgets.schedulechart.IScheduleChartSettings;
import com.mmxlabs.widgets.schedulechart.ScheduleCanvas;
import com.mmxlabs.widgets.schedulechart.ScheduleChartRow;
import com.mmxlabs.widgets.schedulechart.ScheduleEvent;
import com.mmxlabs.widgets.schedulechart.ScheduleEventAnnotation;
import com.mmxlabs.widgets.schedulechart.providers.IScheduleChartModelUpdater;
import com.mmxlabs.widgets.schedulechart.providers.IScheduleEventProvider;
import com.mmxlabs.widgets.schedulechart.providers.ScheduleChartProviders;

public class ScheduleChartViewer<T> extends TypedViewer<T> {
	
	protected final ScheduleCanvas canvas;
	protected final IScheduleEventProvider<T> eventProvider;
	private IScheduleChartModelUpdater modelUpdater;
	private IElementComparer comparer;
	private ISelection selection;
	
	private T input;
	private Map<Object, ScheduleEvent> internalDataMap = new HashMap<>();
	
	public ScheduleChartViewer(final Composite parent, ScheduleChartProviders providers, IScheduleEventProvider<T> eventProvider, IScheduleChartModelUpdater modelUpdater) {
		this(new ScheduleCanvas(parent, providers), eventProvider, modelUpdater);
	}
	
	public ScheduleChartViewer(final Composite parent, ScheduleChartProviders providers, IScheduleEventProvider<T> eventProvider, IScheduleChartModelUpdater modelUpdater, IScheduleChartSettings settings) {
		this(new ScheduleCanvas(parent, providers, settings), eventProvider, modelUpdater);
	}
	
	public ScheduleChartViewer(final ScheduleCanvas canvas, IScheduleEventProvider<T> eventProvider, IScheduleChartModelUpdater modelUpdater) {
		this.canvas = canvas;
		this.eventProvider = eventProvider;
		this.modelUpdater = modelUpdater;
		this.input = null;
		
		hookControl(canvas);
		initListeners();
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

		// Always pass in the same events for the internal map and to the classify function, otherwise selection won't work correctly.
		final List<ScheduleEvent> events = eventProvider.getEvents(input);
		
		// Initialise internal map
		internalDataMap.clear();
		events.forEach(e -> internalDataMap.put(e.getData(), e));

		final List<ScheduleChartRow> newRows = eventProvider.classifyEventsIntoRows(events);
		canvas.addAll(newRows);
		
		if (oldInput == null) {
			canvas.getFilterSupport().applyDefaultFilters();
		}

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
		return selection;
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSelection(ISelection selection, boolean reveal) {
		Control control = getControl();
		if (control == null || control.isDisposed() || selection.equals(this.selection)) {
			return;
		}
		
		this.selection = selection;
		setSelectionToCanvas(selection, reveal);
		fireSelectionChanged(new SelectionChangedEvent(this, selection));
	}
	
	private void setSelectionToCanvas(ISelection selection, boolean reveal) {
		if (selection instanceof IStructuredSelection ss) {
			setSelectionToCanvas(ss.toList(), reveal);
		} else {
			setSelectionToCanvas((List<?>) null, reveal);
		}
	}

	private void setSelectionToCanvas(List<?> l, boolean reveal) {
		List<ScheduleEvent> selectedEvents = new ArrayList<>(l.size());

		for (final var o: l) {
			if (o == null) continue;
			if (internalDataMap.containsKey(o)) {
				selectedEvents.add(internalDataMap.get(o));
			} else if (getComparer() != null) {
				for (final Map.Entry<Object, ScheduleEvent> e : internalDataMap.entrySet()) {
					if (getComparer().equals(e.getKey(), o)) {
						final ScheduleEvent se = internalDataMap.get(e.getKey());
						selectedEvents.add(se);
					}
				}
			}
		}
		
		canvas.setSelectedEvents(selectedEvents);
	}

	// Reimplementation of ContentViewer hookControl
	protected void hookControl(Control control) {
		control.addDisposeListener(this::handleDispose);
	}
	
	protected void handleDispose(DisposeEvent e) {
		// Do nothing for now
	}

	private void initListeners() {
		IScheduleChartEventListener l = new ScheduleChartEventListenerAdapter() {
			
			@Override
			public void eventSelected(ScheduleEvent event, Collection<ScheduleEvent> allSelectedEvents, MouseEvent me) {
				final List<Object> selectedObjects = allSelectedEvents.stream().map(e -> e.getData()).toList();
				final StructuredSelection s = new StructuredSelection(selectedObjects, comparer);
				setSelection(s);
			}
			
			@Override
			public void annotationEdited(ScheduleEvent se, ScheduleEventAnnotation old, ScheduleEventAnnotation updated) {
				modelUpdater.annotationEdited(se.getData(), updated.getData(), updated.getDates().get(0), updated.getDates().get(1));		
			}
			
		};
		
		canvas.addScheduleEventListener(l);
	}

	public IElementComparer getComparer() {
		return comparer;
	}

	public void setComparer(IElementComparer comparer) {
		this.comparer = comparer;
	}
	
}
