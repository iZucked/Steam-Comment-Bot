/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.ganttviewer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.nebula.widgets.ganttchart.AdvancedTooltip;
import org.eclipse.nebula.widgets.ganttchart.GanttChart;
import org.eclipse.nebula.widgets.ganttchart.GanttEvent;
import org.eclipse.nebula.widgets.ganttchart.GanttEventListenerAdapter;
import org.eclipse.nebula.widgets.ganttchart.GanttGroup;
import org.eclipse.nebula.widgets.ganttchart.GanttSection;
import org.eclipse.nebula.widgets.ganttchart.IColorManager;
import org.eclipse.nebula.widgets.ganttchart.IGanttEventListener;
import org.eclipse.nebula.widgets.ganttchart.ISettings;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A {@link Viewer} implementation to wrap around a {@link GanttChart} with the aim of showing schedules. Content providers should implement {@link IGanttChartContentProvider} to pass in
 * {@link Calendar} objects for start and end dates. Label providers should implement {@link IGanttChartToolTipProvider} if {@link AdvancedTooltip}s are required. If the {@link GanttEvent} colour
 * needs changing, then the label provider should implement {@link IColorProvider}.
 * 
 * @author Simon Goodall
 * 
 */
public class GanttChartViewer extends StructuredViewer {

	private static final Logger log = LoggerFactory.getLogger(GanttChartViewer.class);

	protected final GanttChart ganttChart;

	protected final Map<Object, GanttEvent> internalMap = new HashMap<Object, GanttEvent>();
	protected final Map<GanttEvent, Object> internalReverseMap = new HashMap<GanttEvent, Object>();

	private final IGanttEventListener ganttEventListener;

	public GanttChartViewer(final Composite parent) {
		this(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
	}

	public GanttChartViewer(final Composite parent, final int style) {
		this(new GanttChart(parent, style));
	}

	public GanttChartViewer(final Composite parent, final int style, final ISettings settings) {
		this(new GanttChart(parent, style, settings));
	}

	public GanttChartViewer(final Composite parent, final int style, final ISettings settings, final IColorManager colourManager) {
		this(new GanttChart(parent, style, settings, colourManager));
	}

	public GanttChartViewer(final GanttChart ganttChart) {
		this.ganttChart = ganttChart;
		hookControl(ganttChart);
		final GanttChartViewer chartViewer = this;
		ganttEventListener = new GanttEventListenerAdapter() {

			@Override
			public void eventDoubleClicked(final GanttEvent event, final MouseEvent me) {
				// generate event
				chartViewer.fireDoubleClick(new DoubleClickEvent(chartViewer, new StructuredSelection(getSelectionFromObjects(Collections.singletonList(event)))));
			}

			@SuppressWarnings("rawtypes")
			@Override
			public void eventSelected(final GanttEvent event, final java.util.List allSelectedEvents, final org.eclipse.swt.events.MouseEvent me) {

				// Use reverse map to get underlying objects
				@SuppressWarnings("unchecked")
				final List<Object> selectedObjects = getSelectionFromObjects(allSelectedEvents);

				// Create a selection object and fire the selection change hook
				final StructuredSelection selection = new StructuredSelection(selectedObjects);
				setSelection(selection);
			};
		};

		// Register listener
		ganttChart.addGanttEventListener(ganttEventListener);

	}

	@Override
	public Control getControl() {
		return ganttChart;
	}

	public GanttChart getGanttChart() {
		return ganttChart;
	}

	@Override
	protected Widget doFindInputItem(final Object element) {
		if ((element != null) && equals(element, getRoot())) {
			return getControl();
		}

		return null;
	}

	@Override
	protected Widget doFindItem(final Object element) {

		if ((element != null) && internalMap.containsKey(element)) {
			return getControl();
		}

		return null;
	}

	@Override
	protected void doUpdateItem(final Widget item, final Object element, final boolean fullMap) {
		if ((element != null) && internalMap.containsKey(element)) {
			final GanttEvent event = internalMap.get(element);
			final ILabelProvider labelProvider = (ILabelProvider) getLabelProvider();

			final String text = getLabelProviderText(labelProvider, element);
			if (text != null) {
				event.setName(text);
			}

			final Image image = getLabelProviderImage(labelProvider, element);
			if (image == null) {
				event.setImage(false);
				event.setPicture(null);
			} else {
				event.setImage(true);
				event.setPicture(image);
			}

			// Notify gantt chart the event has changed
			event.update(true);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected List<?> getSelectionFromWidget() {

		return getSelectionFromObjects(ganttChart.getGanttComposite().getSelectedEvents());
	}

	private List<Object> getSelectionFromObjects(final List<GanttEvent> allSelectedEvents) {

		// Use reverse map to get underlying objects
		final List<Object> selectedObjects = new ArrayList<Object>(allSelectedEvents.size());

		for (final GanttEvent ge : allSelectedEvents) {

			final Object obj = ge.getData();

			if (obj != null) {
				selectedObjects.add(obj);
			}

		}

		return selectedObjects;
	}

	@Override
	protected void internalRefresh(final Object element) {
		// TODO Auto-generated method stub
	}

	@Override
	public void reveal(final Object element) {

		if ((element != null) && internalMap.containsKey(element)) {
			final GanttEvent ge = internalMap.get(element);
			ganttChart.getGanttComposite().showEvent(ge, SWT.CENTER);
		}
	}

	@Override
	protected void setSelectionToWidget(@SuppressWarnings("rawtypes") final List l, final boolean reveal) {

		final ArrayList<GanttEvent> selectedEvents;
		if (l != null) {
			// Use the internalMap to obtain the list of events we are selecting
			selectedEvents = new ArrayList<GanttEvent>(l.size());
			for (final Object obj : l) {
				if (obj != null) {
					if (internalMap.containsKey(obj)) {
						selectedEvents.add(internalMap.get(obj));
					} else if (getComparer() != null) {
						for (final Map.Entry<Object, GanttEvent> e : internalMap.entrySet()) {
							if (getComparer().equals(e.getKey(), obj)) {

								selectedEvents.add(internalMap.get(e.getKey()));
							}
						}
					}
				}
			}
		} else {
			// Clear selection
			selectedEvents = new ArrayList<GanttEvent>(0);
		}
		ganttChart.getGanttComposite().setSelection(selectedEvents);
		if (selectedEvents.isEmpty() == false) {
			final GanttEvent sel = selectedEvents.get(0);
			if (!ganttChart.getGanttComposite().isEventVisible(sel, ganttChart.getGanttComposite().getBounds())) {
				ganttChart.getGanttComposite().showEvent(sel, SWT.CENTER);
			}
		}
	}

	@Override
	protected synchronized void inputChanged(final Object input, final Object oldInput) {

		// TODO: Extract into separate method

		// Clear existing data
		ganttChart.getGanttComposite().clearChart();
		internalMap.clear();
		internalReverseMap.clear();

		// if (oldInput != null) {
		//
		// Process content and label providers to get some content
		final IContentProvider contentProvider = getContentProvider();
		final ILabelProvider labelProvider = (ILabelProvider) getLabelProvider();

		if (contentProvider instanceof ITreeContentProvider) {
			final ITreeContentProvider treeContentProvider = (ITreeContentProvider) contentProvider;
			final Object[] resources = treeContentProvider.getElements(getInput());

			if (resources == null) {
				return;
			}

			// Sort resources using the ViewerComparator if set
			final ViewerComparator comparator = getComparator();
			if (comparator != null) {
				comparator.sort(this, resources);
			}

			int layer = 0;
			// Each resource to map to a GanntSection
			try {

				Map<Object, GanttEvent> eventMap = new HashMap<Object, GanttEvent>();

				for (final Object r : resources) {
					final String rName = getLabelProviderText(labelProvider, r);
					final GanttSection section = new GanttSection(ganttChart, rName);

					final Map<String, GanttGroup> ganttGroups = new TreeMap<String, GanttGroup>();

					if (treeContentProvider.hasChildren(r)) {
						final GanttGroup defaultGroup = new GanttGroup(ganttChart);
						defaultGroup.setVerticalEventAlignment(SWT.CENTER);

						final Object[] children = treeContentProvider.getChildren(r);
						for (final Object c : children) {

							final String cName = getLabelProviderText(labelProvider, c);

							final Image image = getLabelProviderImage(labelProvider, c);

							final String ganttGroup = getGanttGroup(treeContentProvider, c);

							// Get date/time information from content provider
							final Calendar startDate = getEventStartDate(treeContentProvider, c);
							final Calendar endDate = getEventEndDate(treeContentProvider, c);

							final Calendar plannedStartDate = getEventPlannedStartDate(treeContentProvider, c);
							final Calendar plannedEndDate = getEventPlannedEndDate(treeContentProvider, c);

							final GanttEvent event;
							if (plannedStartDate != null) {
								event = new GanttEvent(ganttChart, c, cName, plannedStartDate, plannedEndDate, startDate, endDate, 0);
							} else {
								event = new GanttEvent(ganttChart, c, cName, startDate, endDate, 0);
							}
							eventMap.put(c, event);
							event.setVerticalEventAlignment(SWT.CENTER);
							event.setGanttSection(section);
							
							if (image != null) {
								event.setImage(true);
								event.setPicture(image);
							}

							// Get colour from label provider
							final Color statusColour = getLabelProviderColor(labelProvider, c);
							if (statusColour != null) {
								event.setStatusColor(statusColour);
							}

							final Color statusBorderColour = getLabelProviderBorderColor(labelProvider, c);
							if (statusBorderColour != null) {
								event.setStatusBorderColor(statusBorderColour);
							}

							event.setStatusBorderWidth(getLabelProviderBorderWidth(labelProvider, c));
							event.setStatusAlpha(getLabelProviderAlpha(labelProvider, c));

							event.setHorizontalTextLocation(SWT.CENTER);

							// Get tooltip from label provider
							final AdvancedTooltip toolTip = getTooltip(labelProvider, c);
							if (toolTip != null) {
								event.setAdvancedTooltip(toolTip);
							}

							// Standard parameters
							event.setMoveable(false);
							event.setResizable(false);

							event.setLayer(layer);

							if (ganttGroup == null) {
								defaultGroup.addEvent(event);
							} else {
								GanttGroup g;
								if (ganttGroups.containsKey(ganttGroup)) {
									g = ganttGroups.get(ganttGroup);
								} else {
									g = new GanttGroup(ganttChart);
									g.setVerticalEventAlignment(SWT.CENTER);
									ganttGroups.put(ganttGroup, g);
								}
								g.addEvent(event);
							}

							internalMap.put(c, event);
							internalReverseMap.put(event, c);

						}
						// Make section text horizontal rather than vertical as we
						// expect only a single line of entries due to the group
						section.setTextOrientation(SWT.HORIZONTAL);

						// Include the default group if there are no special groups or if it has some content.
						if (ganttGroups.isEmpty() || defaultGroup.getEventMembers().size() > 0) {
							section.addGanttEvent(defaultGroup);
						}
						// Add in the special groups
						for (final GanttGroup g : ganttGroups.values()) {
							section.addGanttEvent(g);
						}
					}

					layer++;
				}
				if (contentProvider instanceof IGanttChartContentProvider) {
					IGanttChartContentProvider cp = (IGanttChartContentProvider) contentProvider;

					for (Object c : eventMap.keySet()) {
						Object elementDependency = cp.getElementDependency(c);
						if (elementDependency != null) {
							ganttChart.addDependency(eventMap.get(c), eventMap.get(elementDependency));
						}
					}
				}

			} catch (final Exception ex) {
				log.error(ex.getMessage(), ex);
			}
		} else {
			throw new IllegalArgumentException("ContentProvider should be an instance of " + ITreeContentProvider.class.getCanonicalName());
		}
	}

	protected String getGanttGroup(IContentProvider provider, Object c) {
		if (provider instanceof IGanttChartContentProvider) {
			return ((IGanttChartContentProvider) provider).getGroupIdentifier(c);
		}
		return null;
	}

	protected Color getLabelProviderColor(final ILabelProvider labelProvider, final Object c) {
		if (labelProvider instanceof IColorProvider) {
			return ((IColorProvider) labelProvider).getBackground(c);
		}
		return null;
	}

	protected Color getLabelProviderBorderColor(final ILabelProvider labelProvider, final Object c) {
		if (labelProvider instanceof IGanttChartColourProvider) {
			return ((IGanttChartColourProvider) labelProvider).getBorderColour(c);
		}
		return null;
	}

	protected int getLabelProviderBorderWidth(final ILabelProvider labelProvider, final Object c) {
		if (labelProvider instanceof IGanttChartColourProvider) {
			return ((IGanttChartColourProvider) labelProvider).getBorderWidth(c);
		}
		return 1;
	}

	protected int getLabelProviderAlpha(final ILabelProvider labelProvider, final Object c) {
		if (labelProvider instanceof IGanttChartColourProvider) {
			return ((IGanttChartColourProvider) labelProvider).getAlpha(c);
		}
		return 255;
	}

	private AdvancedTooltip getTooltip(final ILabelProvider labelProvider, final Object c) {

		if (labelProvider instanceof IGanttChartToolTipProvider) {
			final IGanttChartToolTipProvider toolTipProvider = (IGanttChartToolTipProvider) labelProvider;
			final String title = toolTipProvider.getToolTipTitle(c);
			final String text = toolTipProvider.getToolTipText(c);
			final Image image = toolTipProvider.getToolTipImage(c);

			if ((title == null) && (text == null) && (image == null)) {
				return null;
			}

			final AdvancedTooltip toolTip = new AdvancedTooltip(title, text, image);

			return toolTip;

		}

		return null;
	}

	/**
	 * Return the text for the element from the labelProvider. If it is null then return the empty String.
	 * 
	 * @param labelProvider
	 *            ILabelProvider
	 * @param element
	 * @return String. Return the emptyString if the labelProvider returns null for the text.
	 * 
	 */
	private String getLabelProviderText(final ILabelProvider labelProvider, final Object element) {
		final String text = labelProvider.getText(element);
		if (text == null) {
			return "";//$NON-NLS-1$
		}
		return text;
	}

	private Image getLabelProviderImage(final ILabelProvider labelProvider, final Object element) {
		final Image image = labelProvider.getImage(element);
		if (image == null) {
			return null;
		}
		return image;
	}

	private Calendar getEventStartDate(final IContentProvider contentProvider, final Object element) {
		if (contentProvider instanceof IGanttChartContentProvider) {
			final IGanttChartContentProvider gcProvider = (IGanttChartContentProvider) contentProvider;
			return gcProvider.getElementStartTime(element);
		}
		return null;
	}

	private Calendar getEventEndDate(final IContentProvider contentProvider, final Object element) {
		if (contentProvider instanceof IGanttChartContentProvider) {
			final IGanttChartContentProvider gcProvider = (IGanttChartContentProvider) contentProvider;
			return gcProvider.getElementEndTime(element);
		}
		return null;
	}

	private Calendar getEventPlannedStartDate(final IContentProvider contentProvider, final Object element) {
		if (contentProvider instanceof IGanttChartContentProvider) {
			final IGanttChartContentProvider gcProvider = (IGanttChartContentProvider) contentProvider;
			return gcProvider.getElementPlannedStartTime(element);
		}
		return null;
	}

	private Calendar getEventPlannedEndDate(final IContentProvider contentProvider, final Object element) {
		if (contentProvider instanceof IGanttChartContentProvider) {
			final IGanttChartContentProvider gcProvider = (IGanttChartContentProvider) contentProvider;
			return gcProvider.getElementPlannedEndTime(element);
		}
		return null;
	}
}
