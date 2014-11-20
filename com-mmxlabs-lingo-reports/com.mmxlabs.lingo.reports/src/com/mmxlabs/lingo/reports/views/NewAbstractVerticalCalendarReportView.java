/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.nebula.jface.gridviewer.GridColumnLabelProvider;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.DataVisualizer;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridColumnGroup;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.ViewPart;

import com.mmxlabs.common.Pair;
import com.mmxlabs.lingo.reports.ColourPalette;
import com.mmxlabs.lingo.reports.IScenarioInstanceElementCollector;
import com.mmxlabs.lingo.reports.IScenarioViewerSynchronizerOutput;
import com.mmxlabs.lingo.reports.ScenarioViewerSynchronizer;
import com.mmxlabs.lingo.reports.views.AbstractVerticalCalendarReportView.ScheduleSequenceData;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.MaintenanceEvent;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Cooldown;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SequenceType;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.schedule.impl.SequenceImpl;
import com.mmxlabs.models.lng.schedule.impl.SlotVisitImpl;
import com.mmxlabs.rcp.common.actions.CopyGridToClipboardAction;
import com.mmxlabs.rcp.common.actions.CopyToClipboardActionFactory;
import com.mmxlabs.rcp.common.actions.PackActionFactory;
import com.mmxlabs.rcp.common.actions.PackGridTableColumnsAction;

/**
 * New version of the vertical report, with a separate event per display cell.
 * This requires many cells to be merged, so the behaviour of the columns is no longer
 * independent. 
 * 
 * Needs to be tested against the previous vertical report, to check correctness. Then
 * the previous report class should be replaced with this.
 * 
 * Changes:
 * - Key method #createCalendarCols now returns a list of CalendarColumn objects.
 *   These objects each require an EventProvider (which maps from Date objects to Event [] arrays)
 *   and an EventLabelProvider (which describes how to display an Event object on a particular Date).
 * 
 * Known issues:
 * - Column groups are now missing from the column headers. 
 * 	 Modify CalendarColumn constructor and ReportNebulaGridManager#createNebulaColumns to 
 * 	 create column groups (see previous version of vertical reports for guidance)
 * 
 * - Row headers (grey boxes with dates) are split across different rows. 
 *   Modify new AbstractRenderer()#paint in createPartControl.
 *   
 * - Code is even messier than before.
 *   
 * - Export to Excel format may be broken due to cell merging. Not tested.
 * 
 */

/**
 * Class for providing "vertical" schedule reports. Each row is a calendar day in the schedule; each column typically represents a sequence (series of events) in the schedule.
 * <p/>
 * 
 * Override {@link#getCols(ScheduleSequenceData data)} to modify the columns, and override {@link#getEventText(Date date, Event event)} and / or {@link#getEventText(Date date, Event [] events)} to
 * Override {@link#getEventText(Date date, Event event)} and / or {@link#getEventText(Date date, Event [] events)} to modify the sequence cell contents.
 * 
 * 
 * @author Simon McGregor
 * 
 */
public abstract class NewAbstractVerticalCalendarReportView extends ViewPart {

	protected static RGB Orange = new RGB(255, 168, 64);
	protected static RGB Light_Orange = new RGB(255, 197, 168);
	protected static RGB Grey = new RGB(168, 168, 168);
	protected static RGB Light_Grey = new RGB(240, 240, 240);
	protected static RGB Header_Grey = new RGB(228, 228, 228);
	protected static RGB Black = new RGB(0, 0, 0);
	protected GridTableViewer gridViewer;
	private ScenarioViewerSynchronizer jobManagerListener;
	protected SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yy");
	/** format for the "date" column */
	protected LNGScenarioModel root = null;
	protected Date[] dates = null;
	protected final HashMap<RGB, Color> colourMap = new HashMap<>();

	protected ReportNebulaGridManager manager;

	@Override
	public void createPartControl(final Composite parent) {
		final Composite container = new Composite(parent, SWT.NONE);
		final FillLayout layout = new FillLayout();
		layout.marginHeight = layout.marginWidth = 0;
		container.setLayout(layout);

		gridViewer = new GridTableViewer(container, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		gridViewer.setContentProvider(createContentProvider());

		gridViewer.getGrid().setHeaderVisible(true);
		gridViewer.getGrid().setLinesVisible(true);
		// gridViewer.getGrid().setAutoHeight(true);

		gridViewer.getGrid().setRowHeaderVisible(true);

		jobManagerListener = ScenarioViewerSynchronizer.registerView(gridViewer, createElementCollector());

		makeActions();
	}

	protected void makeActions() {
		final PackGridTableColumnsAction packColumnsAction = PackActionFactory.createPackColumnsAction(gridViewer);
		final CopyGridToClipboardAction copyToClipboardAction = CopyToClipboardActionFactory.createCopyToClipboardAction(gridViewer);
		copyToClipboardAction.setRowHeadersIncluded(true);
		getViewSite().getActionBars().setGlobalActionHandler(ActionFactory.COPY.getId(), copyToClipboardAction);

		getViewSite().getActionBars().getToolBarManager().add(packColumnsAction);
		getViewSite().getActionBars().getToolBarManager().add(copyToClipboardAction);
	}

	private IScenarioInstanceElementCollector createElementCollector() {
		return new IScenarioInstanceElementCollector() {

			@Override
			public void endCollecting() {

			}

			@Override
			public Collection<? extends Object> collectElements(final LNGScenarioModel rootObject, final boolean isPinned) {
				final ArrayList<LNGScenarioModel> result = new ArrayList<LNGScenarioModel>();
				result.add(rootObject);
				return result;
			}

			@Override
			public void beginCollecting() {

			}
		};
	}

	protected IStructuredContentProvider createContentProvider() {
		manager = new ReportNebulaGridManager();
		return manager;
	}

	/**
	 * Default implementation of setData() method: do nothing when the relevant data is set for this report. Override this method to perform any custom processing (e.g. setup) when data is provided to
	 * this report.
	 * 
	 * @param data
	 */
	protected void setData(final ScheduleSequenceData data) {
	}

	/**
	 * Returns a list of all 00h00 GMT Date objects which fall within the specified range
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public static List<Date> getGMTDaysBetween(final Date start, final Date end) {
		final ArrayList<Date> result = new ArrayList<Date>();
		if (start != null && end != null) {
			final Calendar c = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT"));
			c.setTime(start);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			while (!c.getTime().after(end)) {
				result.add(c.getTime());
				c.add(Calendar.DAY_OF_MONTH, 1);
			}

		}

		return result;
	}

	public static Date getGMTDayFor(final Date date) {
		final Calendar c = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT"));
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}

	/**
	 * Returns the events, if any, occurring between the two dates specified.
	 */
	protected static Event[] getEvents(final Sequence seq, final Date start, final Date end) {
		final ArrayList<Event> result = new ArrayList<>();
		if (seq != null && start != null && end != null) {
			for (final Event event : seq.getEvents()) {
				// when we get to an event after the search window, break the loop
				// NO: events are not guaranteed to be sorted by date :(
				if (event.getStart().after(end)) {
					// break;
				}
				// otherwise, as long as the event is in the search window, add it to the results
				// if the event ends at midnight, we do *not* count it towards this day
				else if (start.before(event.getEnd())) {
					result.add(event);
				}
			}
		}
		return result.toArray(new Event[0]);
	}

	/**
	 * Returns all events in the specified sequence which overlap with the 24 hr period starting with the specified date
	 * 
	 * @param seq
	 * @param date
	 * @return
	 */
	protected static Event[] getEvents(final Sequence seq, final Date date) {
		return getEvents(seq, date, new Date(date.getTime() + 1000 * 24 * 3600));
	}

	/**
	 * Is the specified day outside of the actual slot visit itself? (A SlotVisit event can be associated with a particular day if its slot window includes that day.)
	 * 
	 * @param day
	 * @param visit
	 * @return
	 */
	protected static boolean isDayOutsideActualVisit(final Date day, final SlotVisit visit) {
		final Date nextDay = new Date(day.getTime() + 1000 * 24 * 3600);

		final Slot slot = ((SlotVisit) visit).getSlotAllocation().getSlot();

		return (nextDay.before(visit.getStart()) || (visit.getEnd().after(day) == false));
	}

	@Override
	public void setFocus() {
	}

	@Override
	public void dispose() {
		ScenarioViewerSynchronizer.deregisterView(jobManagerListener);

		for (final Color colour : colourMap.values()) {
			colour.dispose();
		}
		colourMap.clear();

		super.dispose();
	}

	/**
	 * Class to allow EventProvider objects to filter out particular events from their results.
	 * 
	 * @author mmxlabs
	 * 
	 */
	public interface EventFilter {
		boolean isEventFiltered(Date date, Event event);
	}

	/**
	 * Concrete implementation of the EventFilter interface.
	 * 
	 * @author mmxlabs
	 * 
	 */
	public abstract static class BaseEventFilter implements EventFilter {
		protected final EventFilter filter; // allow filters to be chained if necessary

		public BaseEventFilter(final EventFilter filter) {
			this.filter = filter;
		}

		protected abstract boolean isEventDirectlyFiltered(Date date, Event event);

		@Override
		public boolean isEventFiltered(final Date date, final Event event) {
			if (filter != null) {
				// if the previous filter filtered stuff out
				if (filter.isEventFiltered(date, event) == true) {
					return true;
				}
			}

			return isEventDirectlyFiltered(date, event);
		}
	}

	/**
	 * Abstract event filter superclass which filters out Event objects unless they have a particular field set to one of a particular set of values.
	 * 
	 * Implement getEventField() to produce a filter which filters on a specific field.
	 * 
	 * @author mmxlabs
	 * 
	 * @param <T>
	 */
	static abstract protected class FieldEventFilter<T> extends BaseEventFilter {
		final private List<T> permittedValues;

		public FieldEventFilter(final EventFilter filter, final List<T> values) {
			super(filter);
			permittedValues = values;
		}

		public FieldEventFilter(final List<T> values) {
			this(null, values);
		}

		@Override
		public boolean isEventDirectlyFiltered(final Date date, final Event event) {
			return (permittedValues.contains(getEventField(event)) == false);
		}

		abstract T getEventField(Event event);

	}

	/**
	 * Filter to filter out events which do not occur at one of a specified list of ports.
	 * 
	 * @author mmxlabs
	 * 
	 */
	static protected class PortEventFilter extends FieldEventFilter<Port> {

		public PortEventFilter(final EventFilter filter, final List<Port> values) {
			super(filter, values);
		}

		public PortEventFilter(final List<Port> values) {
			this(null, values);
		}

		@Override
		Port getEventField(final Event event) {
			return event.getPort();
		}

	}

	/**
	 * Filter to filter out events which are not associated with one of a specified list of contracts.
	 * 
	 * @author mmxlabs
	 * 
	 */
	static protected class ContractEventFilter extends FieldEventFilter<Contract> {
		public ContractEventFilter(final EventFilter filter, final List<Contract> values) {
			super(filter, values);
		}

		public ContractEventFilter(final List<Contract> values) {
			this(null, values);
		}

		@Override
		Contract getEventField(final Event event) {
			if (event instanceof SlotVisit) {
				return ((SlotVisit) event).getSlotAllocation().getContract();
			}
			return null;
		}
	}

	/**
	 * Class to provide events to an EventDisplay column.
	 * 
	 * Descendant classes should override {@link#getUnfilteredEvents(Date date)} and / or {@link#filterEventOut(Date date, Event event)} to maintain filtered behaviour.
	 * 
	 * If {@link#getEvents(Date date)} is overridden without preserving the filter logic, {@link#filterEventOut} should be made {@code final} in the overriding class.
	 * 
	 * 
	 * @author Simon McGregor
	 * 
	 * @param <T>
	 *            The data type to initialise the event provider with.
	 */
	public abstract static class EventProvider {
		final protected EventFilter filter;

		public EventProvider(final EventFilter filter) {
			this.filter = filter;
		}

		public Event[] getEvents(final Date date) {
			final ArrayList<Event> result = new ArrayList<>();

			for (final Event event : getUnfilteredEvents(date)) {
				if (filterEventOut(date, event) == false) {
					result.add(event);
				}
			}

			return result.toArray(new Event[0]);
		}

		/** Must be overridden to provide a list of events for any particular date */
		protected abstract Event[] getUnfilteredEvents(Date date);

		/** Returns {@code true} if an event should not be returned by this event provider for a particular date. */
		protected boolean filterEventOut(final Date date, final Event event) {
			if (filter != null) {
				return filter.isEventFiltered(date, event);
			}
			return false;
		}
	}

	/**
	 * Event provider
	 * 
	 * @author mmxlabs
	 * 
	 */
	static protected class HashMapEventProvider extends EventProvider {
		Map<Date, List<Event>> events = new HashMap<>();
		final Event[] noEvents = new Event[0];

		public HashMapEventProvider(final Date start, final Date end, final EventProvider wrapped) {
			this(null);
			for (final Date day : getGMTDaysBetween(start, end)) {
				for (final Event event : wrapped.getEvents(day)) {
					addEvent(day, event);
				}
			}
		}

		public HashMapEventProvider(final EventFilter filter) {
			super(filter);
		}

		public void addEvent(final Date date, final Event event) {
			final List<Event> list = events.containsKey(date) ? events.get(date) : new ArrayList<Event>();
			list.add(event);

			// we actually want the events to be keyed on 00:00 GMT of the same day
			this.events.put(getGMTDayFor(date), list);
		}

		@Override
		protected Event[] getUnfilteredEvents(final Date date) {
			if (events.containsKey(date)) {
				return events.get(date).toArray(noEvents);
			}
			return noEvents;
		}

	}

	/**
	 * Class to provide the events on a given date from one or more Sequence objects.
	 * 
	 * 
	 * Example Usage:
	 * 
	 * column.setLabelProvider(new EventColumnLabelProvider(new SequenceEventProvider(sequence));
	 * 
	 * Override the {@link#filterEventOut(Date date, Event event)} method to filter the events more specifically.
	 * 
	 * @author Simon McGregor
	 * 
	 */
	public static class SequenceEventProvider extends EventProvider {
		final protected Sequence[] data;

		public SequenceEventProvider(final Sequence[] data, final EventFilter filter) {
			super(filter);
			this.data = data;
		}

		public SequenceEventProvider(final Sequence seq, final EventFilter filter) {
			this(new Sequence[] { seq }, filter);
		}

		@Override
		public Event[] getUnfilteredEvents(final Date date) {
			final ArrayList<Event> result = new ArrayList<>();

			for (final Sequence seq : data) {
				if (seq != null) {
					final Event[] events = AbstractVerticalCalendarReportView.getEvents(seq, date);
					for (final Event event : events) {
						result.add(event);
					}
				}
			}

			return result.toArray(new Event[0]);
		}
	}

	protected enum PrecedenceType {
		COLOUR, TEXT
	}

	/**
	 * Record class for holding information on the sequences in a Schedule. Provides the following fields:
	 * 
	 * <ul>
	 * <li>{@code Sequence [] vessels} (all non-fob non-des sequences)</li>
	 * <li>{@code Sequence fobSales}</li>
	 * <li>{@code Date start} (first event date)</li>
	 * <li>{@code Date end} (last event date)</li>
	 * </ul>
	 * 
	 */
	public static class ScheduleSequenceData {
		final public Sequence[] vessels;
		final public Sequence fobSales;
		final public Sequence desPurchases;
		final public VirtualSequence longLoads;
		final public VirtualSequence shortDischarges;
		final public Date start;
		final public Date end;

		/** Extracts the relevant information from the model */
		public ScheduleSequenceData(final LNGScenarioModel model) {
			final ScheduleModel scheduleModel = (model == null ? null : model.getPortfolioModel().getScheduleModel());
			final Schedule schedule = (scheduleModel == null ? null : scheduleModel.getSchedule());

			if (schedule == null) {
				vessels = null;
				fobSales = desPurchases = null;
				start = end = null;
				longLoads = null;
				shortDischarges = null;
				return;
			}

			Date startDate = null;
			Date endDate = null;

			// find start and end dates of entire calendar
			for (final Sequence seq : schedule.getSequences()) {
				for (final Event event : seq.getEvents()) {
					final Date sDate = event.getStart();
					final Date eDate = event.getEnd();
					if (startDate == null || startDate.after(sDate)) {
						startDate = sDate;
					}
					if (endDate == null || endDate.before(eDate)) {
						endDate = eDate;
					}
				}
			}
			// set the final record fields
			start = startDate;
			end = endDate;

			// find the sequences per vessel, and the FOB & DES sequences
			Sequence tempDes = null;
			Sequence tempFob = null;

			final ArrayList<Sequence> vesselList = new ArrayList<Sequence>();

			for (final Sequence seq : schedule.getSequences()) {
				if (seq.getSequenceType() == SequenceType.DES_PURCHASE) {
					tempDes = seq;
				} else if (seq.getSequenceType() == SequenceType.FOB_SALE) {
					tempFob = seq;
				} else {
					vesselList.add(seq);
				}
			}
			// set the final record fields
			desPurchases = tempDes;
			fobSales = tempFob;
			vessels = vesselList.toArray(new Sequence[0]);

			// find the open slots in the schedule
			final List<VirtualSlotVisit> longLoadList = new ArrayList<>();
			final List<VirtualSlotVisit> shortDischargeList = new ArrayList<>();

			for (final OpenSlotAllocation allocation : schedule.getOpenSlotAllocations()) {
				final Slot slot = allocation.getSlot();
				if (slot instanceof LoadSlot) {
					longLoadList.add(new VirtualSlotVisit(slot));
				} else if (slot instanceof DischargeSlot) {
					shortDischargeList.add(new VirtualSlotVisit(slot));
				}
			}

			this.longLoads = new VirtualSequence(longLoadList);
			this.shortDischarges = new VirtualSequence(shortDischargeList);
		}

	}

	/**
	 * Should return the text associated with an individual event on a given day.
	 * 
	 * @param eventColumnLabelProvider
	 */
	protected GridViewerColumn createColumn(final ColumnLabelProvider labeller, final String title) {
		return createColumn(labeller, title, (GridColumn) null);
	}

	protected GridViewerColumn createColumn(final ColumnLabelProvider labeller, final String name, final GridColumnGroup columnGroup) {
		final GridColumn column = new GridColumn(columnGroup, SWT.NONE);
		return createColumn(labeller, name, column);

	}

	protected GridViewerColumn createColumn(final ColumnLabelProvider labeller, final String name, final GridColumn column) {
		final GridViewerColumn result;
		if (column == null) {
			result = new GridViewerColumn(gridViewer, SWT.NONE);
		} else {
			result = new GridViewerColumn(gridViewer, column);
		}
		result.setLabelProvider(labeller);
		result.getColumn().setText(name);
		result.getColumn().pack();

		return result;

	}

	protected Color getColour(final RGB rgb) {
		if (colourMap.containsKey(rgb)) {
			return colourMap.get(rgb);
		} else {
			final Color result = new Color(Display.getCurrent(), rgb);
			colourMap.put(rgb, result);
			return result;
		}
	}

	protected Color getEventBackgroundColor(final Date date, final Event event) {
		if (event instanceof SlotVisit) {
			return getColorFor(date, (SlotVisit) event);
		}
		if (event instanceof Journey) {
			if (((Journey) event).isLaden()) {
				return getColour(ColourPalette.Vessel_Laden_Journey);
			} else
				return getColour(ColourPalette.Vessel_Ballast_Journey);
		}
		if (event instanceof CharterOutEvent) {
			return getColour(ColourPalette.Vessel_Charter_Out);
		}
		if (event instanceof GeneratedCharterOut) {
			return getColour(ColourPalette.Vessel_Generated_Charter_Out);
		}

		if (event instanceof Idle) {
			if (((Idle) event).isLaden()) {
				return getColour(ColourPalette.Vessel_Laden_Idle);
			} else
				return getColour(ColourPalette.Vessel_Ballast_Idle);
		}

		return null;

	}

	protected Color getColorFor(final Date date, final SlotVisit visit) {
		final SlotAllocation allocation = visit.getSlotAllocation();
		final boolean isWindow = isDayOutsideActualVisit(date, visit);

		if (allocation != null) {
			final Slot slot = allocation.getSlot();
			if (slot != null) {
				final Cargo cargo = slot.getCargo();
				if (cargo != null && cargo.isAllowRewiring() == false) {
					return isWindow ? getColour(Light_Grey) : getColour(Grey);
				}
			}
		}
		return isWindow ? getColour(Light_Orange) : getSlotColour(visit);
	}

	protected Color getSlotColour(final SlotVisit visit) {
		return getColour(Orange);
	}

	protected String getEventText(final Date date, final Event event) {
		if (date == null || event == null) {
			return "";
		}

		// how many days since the start of the event?
		Long days = (date.getTime() - event.getStart().getTime()) / (24 * 1000 * 3600);

		// Journey events just show the day number
		if (event instanceof Journey) {
			days += 1;
			return days.toString() + (days == 1 ? String.format(" (%.02f)", ((Journey) event).getSpeed()) : "");
		}

		else if (event instanceof SlotVisit) {
			final SlotVisit visit = (SlotVisit) event;
			if (date != null && isDayOutsideActualVisit(date, visit)) {
				return "";
			}
			String result = getShortPortName(visit.getPort());

			final SlotAllocation allocation = visit.getSlotAllocation();
			if (allocation != null) {
				final Slot slot = allocation.getSlot();
				if (slot != null) {
					result += " " + slot.getName();
				}
			}

			return result;
		} else if (event instanceof Idle) {
			return "";
		} else if (event instanceof VesselEventVisit) {
			final VesselEvent vesselEvent = ((VesselEventVisit) event).getVesselEvent();
			if (vesselEvent instanceof CharterOutEvent) {
				return "CO";
			} else if (vesselEvent instanceof DryDockEvent) {
				return "Dry Dock";
			} else if (vesselEvent instanceof MaintenanceEvent) {
				return "Maintenance";
			}

		} else if (event instanceof GeneratedCharterOut) {
			return "GCO";
		} else if (event instanceof StartEvent) {
			return "Start";
		} else if (event instanceof EndEvent) {
			return "End";
		} else if (event instanceof Cooldown) {
			return "Cooldown";
		}

		final EClass eventClass = event.eClass();
		return eventClass.getName() + " '" + event.name() + "' " + days.toString();
	}

	/**
	 * Default to port name
	 * 
	 * @param port
	 * @return
	 */
	public String getShortPortName(final Port port) {
		return port.getName();
	}

	/**
	 * Virtual "Event" class for additional data which isn't organised in a Sequence.
	 * 
	 */
	public static class VirtualSlotVisit extends SlotVisitImpl {
		public VirtualSlotVisit(final Slot slot) {
			super();
			this.setStart(slot.getWindowStartWithSlotOrPortTime());
			this.setEnd(slot.getWindowEndWithSlotOrPortTime());
			this.setPort(slot.getPort());
			final SlotAllocation sa = ScheduleFactory.eINSTANCE.createSlotAllocation();
			sa.setSlot(slot);
			sa.setSlotVisit(this);
			this.setSlotAllocation(sa);
		}
	}

	public static class VirtualSequence extends SequenceImpl {
		public VirtualSequence(final List<? extends Event> events) {
			super();
			this.getEvents().addAll(events);
		}
	}

	/**
	 * Class to provide formatting information for individual calendar events.
	 * 
	 * @author mmxlabs
	 * 
	 */
	public class EventLabelProvider {
		protected String getText(final Date date, final Event event) {
			return getEventText(date, event);
		}

		protected Font getFont(final Date date, final Event event) {
			return getEventFont(date, event);
		}

		protected Color getBackground(final Date date, final Event event) {
			return getEventBackgroundColor(date, event);
		}

		protected Color getForeground(final Date date, final Event event) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	protected class NewCalendarColumnLabelProvider extends GridColumnLabelProvider {
		protected EventProvider provider;
		protected EventLabelProvider labeller;
		protected ReportNebulaGridManager manager;

		public NewCalendarColumnLabelProvider(final EventProvider provider, final EventLabelProvider labeller, final ReportNebulaGridManager manager) {
			this.provider = provider;
			this.labeller = labeller;
			this.manager = manager;
		}

		public Event getData(final Pair<Date, Integer> key) {
			final Date date = key.getFirst();
			final Integer index = key.getSecond();
			final Event[] result = provider.getEvents(date);
			if (result == null || result.length <= index) {
				return null;
			}
			return result[index];
		}

		@Override
		public String getRowHeaderText(final Object element) {
			final Pair<Date, Integer> pair = (Pair<Date, Integer>) element;
			final Date date = pair.getFirst();
			return sdf.format(date);
		}

		@Override
		public String getText(final Object element) {
			final Pair<Date, Integer> pair = (Pair<Date, Integer>) element;
			final Date date = pair.getFirst();
			return labeller.getText(date, getData(pair));
		}

		@Override
		public Font getFont(final Object element) {
			final Pair<Date, Integer> pair = (Pair<Date, Integer>) element;
			final Date date = pair.getFirst();
			return labeller.getFont(date, getData(pair));
		}

		@Override
		public Color getBackground(final Object element) {
			final Pair<Date, Integer> pair = (Pair<Date, Integer>) element;
			final Date date = pair.getFirst();
			return labeller.getBackground(date, getData(pair));
		}

		@Override
		public Color getForeground(final Object element) {
			final Pair<Date, Integer> pair = (Pair<Date, Integer>) element;
			final Date date = pair.getFirst();
			return labeller.getForeground(date, getData(pair));
		}

		@Override
		public void update(final ViewerCell cell) {
			super.update(cell);
			manager.updateCell(cell);
		}

	}

	protected class CalendarColumn {
		private final EventProvider provider;
		private final EventLabelProvider labeller;
		private final String title;
		private final GridColumnGroup columnGroup;

		public CalendarColumn(final EventProvider provider, final EventLabelProvider labeller, final String title, final GridColumnGroup columnGroup) {
			this.provider = provider;
			this.labeller = labeller;
			this.title = title;
			this.columnGroup = columnGroup;
		}

		public ColumnLabelProvider createColumnLabelProvider(final ReportNebulaGridManager manager) {
			return new NewCalendarColumnLabelProvider(provider, labeller, manager);
		}

		public String getTitle() {
			return title;
		}

		@Override
		public String toString() {
			return (title == null) ? "(null)" : title;
		}

		public GridColumnGroup getColumnGroup() {
			return columnGroup;
		}
	}

	/**
	 * Class that makes the relevant API calls to the nebula Grid widget viewer framework.
	 * 
	 * The grid widget assumes a data model based on rows, which in our case have to correspond to <Date, Int> pairs indicating a date and an event index.
	 * 
	 * This class has to create the relevant content model expected by the Grid framework, calculate which Grid cells are merged in every column, pass this information on to the widget, create
	 * relevant column formatting objects, and hook them up to the widget.
	 * 
	 * @author mmxlabs
	 * 
	 */
	protected class ReportNebulaGridManager implements IStructuredContentProvider {
		private Pair<Date, Integer>[] nebulaElements;
		private List<CalendarColumn> calendarColumns;
		private List<GridViewerColumn> nebulaColumns;

		public Event[] getLogicalCellContents(final Date date, final CalendarColumn column) {
			return column.provider.getEvents(date);
		}

		public int getNumRowsRequired(final Date date, final CalendarColumn column) {
			final Event[] contents = getLogicalCellContents(date, column);
			if (contents != null && contents.length > 1) {
				return contents.length;
			}
			return 1;
		}

		public int getNumRowsRequired(final Date date) {
			int result = 1;
			for (final CalendarColumn column : calendarColumns) {
				final int num = getNumRowsRequired(date, column);
				if (num > result) {
					result = num;
				}
			}
			return result;
		}

		@Override
		public void dispose() {
			nebulaElements = null;
		}

		@Override
		public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
			if (newInput != null) {
				// svso.getCollectedElements in this case returns a singleton list containing the root object
				final IScenarioViewerSynchronizerOutput svso = (IScenarioViewerSynchronizerOutput) newInput;
				for (final Object element : svso.getCollectedElements()) {
					root = (LNGScenarioModel) element;
				}
				// extract the relevant data from the root object
				final ScheduleSequenceData data = new ScheduleSequenceData(root);
				setData(data);
				// setup table columns and rows
				setCols(data);
				setRows(data);

			}
		}

		@Override
		public Object[] getElements(final Object inputElement) {
			// TODO Auto-generated method stub
			return nebulaElements;
		}

		protected void setCols(final ScheduleSequenceData data) {
			// clear the grid columns; we will have to replace them with vessels from the new scenario
			for (final GridColumn column : gridViewer.getGrid().getColumns()) {
				column.dispose();
			}

			if (root != null) {
				calendarColumns = createCalendarCols(data);
			}

			createNebulaColumns();

			gridViewer.refresh();

		}

		@SuppressWarnings("unchecked")
		private void setRows(final ScheduleSequenceData data) {
			final List<Pair<Date, Integer>> result = new LinkedList<>();

			final Date[] allDates = getGMTDaysBetween(data.start, data.end).toArray(new Date[0]);
			for (final Date date : allDates) {
				for (int i = 0; i < getNumRowsRequired(date); i++) {
					result.add(new Pair<>(date, i));
				}
			}

			nebulaElements = result.toArray(new Pair[0]);
		}

		private void createNebulaColumns() {
			nebulaColumns = new ArrayList<>();
			if (calendarColumns != null) {
				for (final CalendarColumn column : calendarColumns) {
					if (column.getColumnGroup() != null) {
						nebulaColumns.add(createColumn(column.createColumnLabelProvider(this), column.getTitle(), column.getColumnGroup()));
					} else {
						nebulaColumns.add(createColumn(column.createColumnLabelProvider(this), column.getTitle()));
					}
				}
			}
		}

		void updateCell(final ViewerCell cell) {
			final GridItem item = (GridItem) cell.getItem();
			final int i = cell.getColumnIndex();

			final DataVisualizer dv = gridViewer.getGrid().getDataVisualizer();

			@SuppressWarnings("unchecked")
			final Pair<Date, Integer> pair = (Pair<Date, Integer>) item.getData();
			final Date date = pair.getFirst();
			final int index = pair.getSecond();

			final int totalRows = getNumRowsRequired(date);

			// we may need to merge the cells in the visualiser
			// if there are fewer events in this column than
			// in another column, the last event in this column
			// will have to take up additional rows
			final CalendarColumn column = calendarColumns.get(i);
			final int cellRows = getNumRowsRequired(date, column);
			if (index == cellRows - 1) {
				dv.setRowSpan(item, i, totalRows - cellRows);
			}
		}

	}

	/**
	 * Override this method to control the columns in the vertical report.
	 * 
	 * @param data
	 */
	protected abstract List<CalendarColumn> createCalendarCols(final ScheduleSequenceData data);

	protected abstract Font getEventFont(Date date, Event event);

}
