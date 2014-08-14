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
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridColumnGroup;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.ViewPart;

import com.mmxlabs.lingo.reports.ColourPalette;
import com.mmxlabs.lingo.reports.IScenarioInstanceElementCollector;
import com.mmxlabs.lingo.reports.IScenarioViewerSynchronizerOutput;
import com.mmxlabs.lingo.reports.ScenarioViewerSynchronizer;
import com.mmxlabs.lingo.reports.views.AbstractVerticalCalendarReportView.ScheduleSequenceData;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
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
import com.mmxlabs.models.lng.schedule.impl.SequenceImpl;
import com.mmxlabs.models.lng.schedule.impl.SlotVisitImpl;
import com.mmxlabs.rcp.common.actions.CopyGridToClipboardAction;
import com.mmxlabs.rcp.common.actions.CopyToClipboardActionFactory;
import com.mmxlabs.rcp.common.actions.PackActionFactory;
import com.mmxlabs.rcp.common.actions.PackGridTableColumnsAction;

/**
 * Class for providing "vertical" schedule reports. Each row is a calendar day in the schedule; each column typically 
 * represents a sequence (series of events) in the schedule.<p/> 
 * 
 * Override {@link#getCols(ScheduleSequenceData data)} to modify the columns, and override {@link#getEventText(Date date, Event event)} and / or {@link#getEventText(Date date, Event [] events)} to
 * Override {@link#getEventText(Date date, Event event)} and / or {@link#getEventText(Date date, Event [] events)} 
 * to modify the sequence cell contents.   
 * 
 * @author Simon McGregor
 * 
 */
public abstract class AbstractVerticalCalendarReportView extends ViewPart {

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
	protected final HashMap<Event, Vessel> vesselsByEvent = new HashMap<>();

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
		
		// following unfinished code allows the date to display as a row header
		/*
		gridViewer.getGrid().setRowHeaderVisible(true);
		gridViewer.getGrid().setRowHeaderRenderer(new AbstractRenderer() {

			@Override
			public Point computeSize(GC arg0, int arg1, int arg2, Object arg3) {
				return new Point(50, 10);
			}

			@Override
			public void paint(GC arg0, Object arg1) {
				Rectangle bounds = getBounds();
				arg0.setBackground(getColour(Light_Grey));
				arg0.fillRectangle(bounds);
				arg0.setForeground(getColour(Black));
				if (arg1 instanceof GridItem) {
					GridItem item = (GridItem) arg1;
					Date date = (Date) item.getData();
					if (date != null) {
						arg0.drawString(sdf.format(date),  bounds.x,  bounds.y);
					}
				}
			}
			
		});
		*/

		jobManagerListener = ScenarioViewerSynchronizer.registerView(gridViewer, createElementCollector());

		makeActions();
	}

	protected void makeActions() {
		final PackGridTableColumnsAction packColumnsAction = PackActionFactory.createPackColumnsAction(gridViewer);
		final CopyGridToClipboardAction copyToClipboardAction = CopyToClipboardActionFactory.createCopyToClipboardAction(gridViewer);
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
		return new IStructuredContentProvider() {

			@Override
			public void dispose() {
				dates = null;
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

			protected void setCols(final ScheduleSequenceData data) {
				// clear the grid columns; we will have to replace them with vessels from the new scenario
				for (final GridColumn column : gridViewer.getGrid().getColumns()) {
					column.dispose();
				}

				if (root != null) {
					createCols(data);
				}
				
				// regenerate the map linking events to vessels
				vesselsByEvent.clear();
				for (Sequence sequence: data.vessels) {					
					Vessel vessel = sequence.getVesselAvailability().getVessel();
					if (vessel != null) {
						for (Event event: sequence.getEvents()) {
							vesselsByEvent.put(event, vessel);
						}
					}
				}

				gridViewer.refresh();

			}

			private void setRows(final ScheduleSequenceData data) {
				dates = getGMTDaysBetween(data.start, data.end).toArray(new Date[0]);
			}

			@Override
			/**
			 * Returns the list of calendar days for this report.
			 */
			public Object[] getElements(final Object inputElement) {
				return dates;
			}

		};
	}
	
	protected void setData(ScheduleSequenceData data) {
	}

	/**
	 * Returns a list of all 00h00 GMT Date objects which fall within the specified range
	 * @param start
	 * @param end
	 * @return
	 */
	public static List<Date> getGMTDaysBetween(Date start, Date end) {
		final ArrayList<Date> result = new ArrayList<Date>();
		if (start != null && end != null) {
			final Calendar c = Calendar.getInstance();
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
	
	public static Date getGMTDayFor(Date date) {
		final Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}

	/**
	 * Override this method to control the columns in the vertical report.
	 * 
	 * @param data
	 */

	abstract protected void createCols(ScheduleSequenceData data);

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
					//break;
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
	 * Is the specified day outside of the actual slot visit itself? (A SlotVisit event can be
	 * associated with a particular day if its slot window includes that day.) 
	 * 
	 * @param day
	 * @param visit
	 * @return
	 */
	protected static boolean isDayOutsideActualVisit(final Date day, final SlotVisit visit) {
		Date nextDay = new Date(day.getTime() + 1000 * 24 * 3600);
		
		Slot slot = ((SlotVisit) visit).getSlotAllocation().getSlot(); 
		if (slot.getName().equals("P16")) {
			slot.getName();
		}
	
		return (nextDay.before(visit.getStart()) || (visit.getEnd().after(day) == false));
	}

	@Override
	public void setFocus() {
	}

	@Override
	public void dispose() {
		ScenarioViewerSynchronizer.deregisterView(jobManagerListener);

		super.dispose();
	}

	/**
	 * This class allows for convenient column label provider creation in a calendar grid: the provider is initialised with a particular data object (e.g. a sequence from a schedule) and will delegate
	 * cell formatting & contents to methods based on the data object and the date.
	 */
	static abstract protected class CalendarColumnLabelProvider<T> extends ColumnLabelProvider {
		protected T data;

		public CalendarColumnLabelProvider(final T object) {
			data = object;
		}

		public T getData() {
			return data;
		}

		@Override
		public String getText(final Object element) {
			return getText((Date) element, data);
		}

		@Override
		public Font getFont(final Object element) {
			return getFont((Date) element, data);
		}

		@Override
		public Color getBackground(final Object element) {
			return getBackground((Date) element, data);
		}

		@Override
		public Color getForeground(final Object element) {
			return getForeground((Date) element, data);
		}

		/** Returns the text content of the cell. */
		abstract protected String getText(Date element, T object);

		/** Returns the desired font of the cell. */
		protected Font getFont(final Date element, final T object) {
			return null;
		}

		/** Returns the desired font of the cell. */
		protected Color getBackground(final Date element, final T object) {
			return null;
		}

		/** Returns the desired font of the cell. */
		protected Color getForeground(final Date element, final T object) {
			return null;
		}

	}

	public interface EventFilter {
		boolean isEventFiltered(Date date, Event event);
	}
	
	public abstract static class BaseEventFilter implements EventFilter {
		protected final EventFilter filter; // allow filters to be chained if necessary
		
		public BaseEventFilter(EventFilter filter) {
			this.filter = filter;
		}

		protected abstract boolean isEventDirectlyFiltered(Date date, Event event);
		
		@Override
		public boolean isEventFiltered(Date date, Event event) {
			if (filter != null) {
				// if the previous filter filtered stuff out
				if (filter.isEventFiltered(date, event) == true) {
					return true;
				}
			}

			return isEventDirectlyFiltered(date, event);
		}		
	}
	
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
	
	static protected class PortEventFilter extends FieldEventFilter<Port> {

		public PortEventFilter(final EventFilter filter, List<Port> values) {
			super(filter, values);
		}

		public PortEventFilter(List<Port> values) {
			this(null, values);
		}

		@Override
		Port getEventField(Event event) {
			return event.getPort();
		}
		
	}

	static protected class ContractEventFilter extends FieldEventFilter<Contract> {		
		public ContractEventFilter(final EventFilter filter, List<Contract> values) {
			super(filter, values);
		}

		public ContractEventFilter(List<Contract> values) {
			this(null, values);
		}

		@Override
		Contract getEventField(Event event) {
			if (event instanceof SlotVisit) {
				return ((SlotVisit) event).getSlotAllocation().getContract();
			}
			return null;
		}
	}

	
	/**
	 * Class which provides cell labels (and formatting if desired) for columns in a calendar-style vertical report, based on a list of events per cell.
	 * <p/>
	 * 
	 * Must be initialised with an {@link EventProvider} object which provides a list of events for a given calendar date.
	 * <p/>
	 * 
	 * Override {@link#getText(Date date, Event event)} to specify the formatting of cell contents.
	 * <p/>
	 * 
	 * Example Usage:
	 * <p/>
	 * {@code 
	 * column.setLabelProvider(new EventColumnLabelProvider(new SequenceEventProvider(sequence)); 
	 * }
	 * 
	 * @author Simon McGregor
	 * 
	 */
	public class EventColumnLabelProvider extends CalendarColumnLabelProvider<EventProvider> {
		public EventColumnLabelProvider(final EventProvider provider) {
			super(provider);
		}
//
//		public void update(ViewerCell cell) {
//			super.update(cell);
//			DataVisualizer dv = gridViewer.getGrid().getDataVisualizer();
//			
//			int col = cell.getColumnIndex();
//
//			Object element = cell.getElement();
//			int row = Arrays.asList(dates).indexOf(element);
//			int rowSpan = getRowSpan((Date) element, data);
//			dv.setRowSpan(row, col, rowSpan);
//			int colSpan = getColSpan((Date) element, data);
//
//		}		
//		
		protected int getRowSpan(final Date date, final EventProvider provider) {
			return 0;
		}

		protected int getColSpan(final Date date, final EventProvider provider) {
			return 0;
		}

		/**
		 * Returns the text for a column cell. 
		 * Defers to {@link #getEventText(Date, Event[])}; override that method if you want to change the behaviour. 
		 */
		@Override
		protected final String getText(final Date element, final EventProvider provider) {
			// find the event text for the date given
			return getEventText(element, provider.getEvents(element));
		}
		
		/**
		 * Returns 
		 * By default, defers to {@link AbstractVerticalCalendarReportView#getEventText(Date, Event[], EventColumnLabelProvider)}
		 * @param element
		 * @param events
		 * @return
		 */
		protected String getEventText(final Date element, final Event [] events) {
			return AbstractVerticalCalendarReportView.this.getEventText(element, events, EventColumnLabelProvider.this);			
		}

		/** Returns the desired background colour of the cell. */
		@Override
		protected Color getBackground(final Date element, final EventProvider provider) {
			return getEventBackgroundColor(element, provider.getEvents(element), EventColumnLabelProvider.this);
		}

		/** Returns the desired foreground colour of the cell. */
		@Override
		protected Color getForeground(final Date element, final EventProvider provider) {
			return getEventForegroundColor(element, provider.getEvents(element), EventColumnLabelProvider.this);
		}

		/** Returns the desired foreground colour of the cell. */
		@Override
		protected Font getFont(final Date element, final EventProvider provider) {
			return getEventFont(element, provider.getEvents(element), EventColumnLabelProvider.this);
		}
	}

	/**
	 * Label provider for the "date" column: provide the date in a specified format.
	 * 
	 * @author Simon McGregor
	 * 
	 */
	protected class DateColumnLabelProvider extends ColumnLabelProvider {
		public DateColumnLabelProvider() {
		}

		@Override
		public String getText(final Object element) {
			return sdf.format(element);
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
		
		public EventProvider(EventFilter filter) {
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
	
	static protected class HashMapEventProvider extends EventProvider {
		Map<Date, List<Event>> events = new HashMap<>();
		final Event [] noEvents = new Event [0];

		public HashMapEventProvider(Date start, Date end, EventProvider wrapped) {
			this(null);
			for (Date day: getGMTDaysBetween(start, end)) {
				for (Event event: wrapped.getEvents(day)) {
					addEvent(day, event);
				}
			}
		}
		
		public HashMapEventProvider(EventFilter filter) {
			super(filter);
		}

		public void addEvent(final Date date, final Event event) {
			final List<Event> list = events.containsKey(date) ? events.get(date) : new ArrayList<Event>();
			list.add(event);
			
			// we actually want the events to be keyed on 00:00 GMT of the same day			
			this.events.put(getGMTDayFor(date), list);
		}
		
		@Override
		protected Event[] getUnfilteredEvents(Date date) {
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

	protected enum PrecedenceType { COLOUR, TEXT }

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

			for (OpenSlotAllocation allocation: schedule.getOpenSlotAllocations()) {
				Slot slot = allocation.getSlot();
				if (slot instanceof LoadSlot) {
					longLoadList.add(new VirtualSlotVisit(slot));
				}
				else if (slot instanceof DischargeSlot) {
					shortDischargeList.add(new VirtualSlotVisit(slot));
				}
			}
			
			this.longLoads = new VirtualSequence(longLoadList);
			this.shortDischarges = new VirtualSequence(shortDischargeList);
		}

	}

	/**
	 * Should return the text associated with a list of events on a given day.
	 * 
	 * @param eventColumnLabelProvider
	 */
	abstract protected String getEventText(Date element, Event[] events, EventColumnLabelProvider eventColumnLabelProvider);

	abstract protected Color getEventForegroundColor(Date element, Event[] events, EventColumnLabelProvider eventColumnLabelProvider);
	
	protected Font getEventFont(Date element, Event[] events, EventColumnLabelProvider eventColumnLabelProvider) {
		return null;
	}

	protected GridViewerColumn createEventColumn(EventProvider eventProvider, String title) {
		return createColumn(new EventColumnLabelProvider(eventProvider), title);
	}

	protected GridViewerColumn createColumn(ColumnLabelProvider labeller, String title) {
		return createColumn(labeller, title, (GridColumn) null);
	}

	protected GridViewerColumn createEventColumn(EventProvider eventProvider, String name, GridColumnGroup columnGroup) {
		return createColumn(new EventColumnLabelProvider(eventProvider), name, columnGroup);
	}

	protected GridViewerColumn createColumn(ColumnLabelProvider labeller, String name, GridColumnGroup columnGroup) {
		final GridColumn column = new GridColumn(columnGroup, SWT.NONE);
		return createColumn(labeller, name, column);
		
	}

	protected GridViewerColumn createColumn(ColumnLabelProvider labeller, String name, GridColumn column) {
		final GridViewerColumn result;
		if (column == null) {
			result = new GridViewerColumn(gridViewer, SWT.NONE); 
		}
		else {
			result = new GridViewerColumn(gridViewer, column);
		}
		result.setLabelProvider(labeller);
		result.getColumn().setText(name);
		result.getColumn().pack();

		return result;
		
	}

	protected int getEventPrecedence(final Date date, final Event event, final PrecedenceType type) {		
		if (type == PrecedenceType.COLOUR) {
			if (event instanceof SlotVisit) {
				return 5;
			}
			if (event instanceof Journey) {
				return -5;
			}
		}
		else if (type == PrecedenceType.TEXT) {
			if (event instanceof SlotVisit) {
				return isDayOutsideActualVisit(date, (SlotVisit) event) ? -10 : 5;
			}
			if (event instanceof Journey) {
				return -5;
			}
			
		}
		return 0;
	}

	protected Event getRelevantEvent(final Date date, final Event[] events, final PrecedenceType type) {
		Integer best = null;
		Event result = null;
	
		for (final Event event : events) {
			final int precedence = getEventPrecedence(date, event, type);
			if (result == null || best == null || precedence > best) {
				result = event;
				best = precedence;
			}
		}
	
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

	protected Color getEventBackgroundColor(final Date date, final Event[] events,
			final EventColumnLabelProvider provider) {
			
				final Event event = getRelevantEvent(date, events, PrecedenceType.COLOUR);
			
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

	protected Color getSlotColour(SlotVisit visit) {
		return getColour(Orange);
	}

	protected String getEventText(final Date element, final Event event) {
		// how many days since the start of the event?
		Long days = (element.getTime() - event.getStart().getTime()) / (24 * 1000 * 3600);
	
		// Journey events just show the day number
		if (event instanceof Journey) {
			days += 1;
			return days.toString() + (days==1 ? String.format(" (%.02f)", ((Journey) event).getSpeed()): "");
		}
	
		else if (event instanceof SlotVisit) {
			final SlotVisit visit = (SlotVisit) event;
			if (isDayOutsideActualVisit(element, visit)) {
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
		} else if (event instanceof GeneratedCharterOut) {
			return "GCO";
		} else if (event instanceof CharterOutEvent) {
			return "CO";
		} else if (event instanceof StartEvent) {
			return "Start";
		} else if (event instanceof EndEvent) {
			return "End";
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
	public String getShortPortName(Port port) {
		return port.getName();		
	}

	public class HashLabelProvider<T> extends ColumnLabelProvider {
		
	}
	
	
	/**
	 * Virtual "Event" class for additional data which isn't organised in a Sequence. 
	 *
	 */
	public static class VirtualSlotVisit extends SlotVisitImpl {
		public VirtualSlotVisit(Slot slot) {
			super();
			this.setStart(slot.getWindowStartWithSlotOrPortTime());
			this.setEnd(slot.getWindowEndWithSlotOrPortTime());
			this.setPort(slot.getPort());
			SlotAllocation sa = ScheduleFactory.eINSTANCE.createSlotAllocation();
			sa.setSlot(slot);
			sa.setSlotVisit(this);						
			this.setSlotAllocation(sa);
		}
	}
	
	public static class VirtualSequence extends SequenceImpl {
		public VirtualSequence(List<? extends Event> events) {
			super();
			this.getEvents().addAll(events);
		}
	}

}
