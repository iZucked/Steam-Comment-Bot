package com.mmxlabs.shiplingo.platform.reports.views;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.ViewPart;

import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SequenceType;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.rcp.common.actions.CopyGridToClipboardAction;
import com.mmxlabs.rcp.common.actions.CopyToClipboardActionFactory;
import com.mmxlabs.rcp.common.actions.PackActionFactory;
import com.mmxlabs.rcp.common.actions.PackGridTableColumnsAction;
import com.mmxlabs.shiplingo.platform.reports.IScenarioInstanceElementCollector;
import com.mmxlabs.shiplingo.platform.reports.IScenarioViewerSynchronizerOutput;
import com.mmxlabs.shiplingo.platform.reports.ScenarioViewerSynchronizer;
import com.mmxlabs.shiplingo.platform.reports.views.AbstractVerticalCalendarReportView.ScheduleSequenceData;

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

	protected GridTableViewer gridViewer;
	private ScenarioViewerSynchronizer jobManagerListener;
	protected SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yy");
	/** format for the "date" column */
	protected LNGScenarioModel root = null;

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

		jobManagerListener = ScenarioViewerSynchronizer.registerView(gridViewer, createElementCollector());

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
			Date[] dates = null;

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
				}

				// extract the relevant data from the root object
				final ScheduleSequenceData data = new ScheduleSequenceData(root);
				// setup table columns and rows
				setCols(data);
				setRows(data);
			}

			protected void setCols(final ScheduleSequenceData data) {
				// clear the grid columns; we will have to replace them with vessels from the new scenario
				for (final GridColumn column : gridViewer.getGrid().getColumns()) {
					column.dispose();
				}

				if (root != null) {
					createCols(data);
				}

				gridViewer.refresh();

			}

			private void setRows(final ScheduleSequenceData data) {
				if (data.start != null && data.end != null) {
					final ArrayList<Date> allDates = new ArrayList<Date>();
					final Calendar c = Calendar.getInstance();
					c.setTime(data.start);
					c.set(Calendar.HOUR_OF_DAY, 0);
					c.set(Calendar.MINUTE, 0);
					c.set(Calendar.SECOND, 0);
					allDates.add(c.getTime());
					while (!c.getTime().after(data.end)) {
						c.add(Calendar.DAY_OF_MONTH, 1);
						allDates.add(c.getTime());
					}

					dates = allDates.toArray(new Date[0]);
				} else {
					dates = new Date[0];
					return;
				}
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
				if (event.getStart().after(end)) {
					break;
				}
				// otherwise, as long as the event is in the search window, add it to the results
				if (!event.getEnd().before(start)) {
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

		/** Returns the desired font of the cell. */
		@Override
		protected Color getBackground(final Date element, final EventProvider provider) {
			return getEventBackgroundColor(element, provider.getEvents(element), EventColumnLabelProvider.this);
		}

		/** Returns the desired font of the cell. */
		@Override
		protected Color getForeground(final Date element, final EventProvider provider) {
			return getEventForegroundColor(element, provider.getEvents(element), EventColumnLabelProvider.this);
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
	abstract static protected class EventProvider {
		protected Event[] getEvents(final Date date) {
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
			return false;
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

		public SequenceEventProvider(final Sequence[] data) {
			this.data = data;
		}

		public SequenceEventProvider(final Sequence seq) {
			this(new Sequence[] { seq });
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

	
	static abstract protected class FilteredFieldEventProvider<T> extends SequenceEventProvider {
		final private List<T> permittedValues;

		public FilteredFieldEventProvider(final Sequence [] seq, final List<T> values) {
			super(seq);
			permittedValues = values;
		}
		
		@Override
		protected boolean filterEventOut(final Date date, final Event event) {
			return (permittedValues.contains(getEventField(event)) == false);
		}

		abstract T getEventField(Event event);		
	}

	/**
	 * Class to provide the events on a given date from one or more Sequence objects, filtered by occurring at one or more specified ports.
	 * 
	 * @author mmxlabs
	 * 
	 */
	static protected class PortSequenceEventProvider extends FilteredFieldEventProvider<Port> {
		public PortSequenceEventProvider(Sequence[] seq, List<Port> values) {
			super(seq, values);
		}

		@Override
		Port getEventField(Event event) {
			return event.getPort();
		}
	}

	/**
	 * Class to provide the SlotVisit events on a given date from one or more Sequence objects, filtered by being based on one or more specified ontracts.
	 * 
	 * @author mmxlabs
	 * 
	 */
	static protected class ContractSequenceEventProvider extends FilteredFieldEventProvider<Contract> {


		public ContractSequenceEventProvider(Sequence[] seq,
				List<Contract> values) {
			super(seq, values);
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
		final public Date start;
		final public Date end;

		/** Extracts the relevant information from the model */
		public ScheduleSequenceData(final LNGScenarioModel model) {
			if (model == null) {
				vessels = null;
				fobSales = desPurchases = null;
				start = end = null;
				return;
			}

			final ScheduleModel scheduleModel = model.getPortfolioModel().getScheduleModel();

			Date startDate = null;
			Date endDate = null;

			// find start and end dates of entire calendar
			if (scheduleModel != null) {
				final Schedule schedule = scheduleModel.getSchedule();
				if (schedule != null) {
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
				}
			}

			// set the final record fields
			start = startDate;
			end = endDate;

			// find the des, fob and other sequences in the schedule
			Sequence desSequence = null;
			Sequence fobSequence = null;

			final ArrayList<Sequence> vesselList = new ArrayList<Sequence>();

			if (scheduleModel != null) {
				final Schedule schedule = scheduleModel.getSchedule();

				if (schedule != null) {
					for (final Sequence seq : schedule.getSequences()) {
						if (seq.getSequenceType() == SequenceType.DES_PURCHASE) {
							desSequence = seq;
						} else if (seq.getSequenceType() == SequenceType.FOB_SALE) {
							fobSequence = seq;
						} else {
							vesselList.add(seq);
						}
					}
				}
			}

			// set the final record fields
			fobSales = fobSequence;
			desPurchases = desSequence;

			vessels = vesselList.toArray(new Sequence[0]);

		}
	}

	/**
	 * Should return the text associated with a list of events on a given day.
	 * 
	 * @param eventColumnLabelProvider
	 */
	abstract protected String getEventText(Date element, Event[] events, EventColumnLabelProvider eventColumnLabelProvider);

	abstract protected Color getEventBackgroundColor(Date element, Event[] events, EventColumnLabelProvider eventColumnLabelProvider);

	abstract protected Color getEventForegroundColor(Date element, Event[] events, EventColumnLabelProvider eventColumnLabelProvider);
	
	protected GridViewerColumn createEventColumn(SequenceEventProvider eventProvider, String title) {
		final GridViewerColumn result = new GridViewerColumn(gridViewer, SWT.NONE);
		result.setLabelProvider(new EventColumnLabelProvider(eventProvider));
		result.getColumn().setText(title);
		result.getColumn().pack();				
		return result;		
	}

}
