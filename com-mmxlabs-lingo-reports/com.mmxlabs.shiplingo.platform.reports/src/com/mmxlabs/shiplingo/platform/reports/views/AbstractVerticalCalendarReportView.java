package com.mmxlabs.shiplingo.platform.reports.views;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import org.eclipse.emf.ecore.EClass;
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
import org.eclipse.ui.part.ViewPart;

import com.mmxlabs.models.lng.fleet.VesselAvailability;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SequenceType;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.shiplingo.platform.reports.IScenarioInstanceElementCollector;
import com.mmxlabs.shiplingo.platform.reports.IScenarioViewerSynchronizerOutput;
import com.mmxlabs.shiplingo.platform.reports.ScenarioViewerSynchronizer;

public class AbstractVerticalCalendarReportView extends ViewPart {

	protected GridTableViewer gridViewer;
	private ScenarioViewerSynchronizer jobManagerListener;
	private Object input;
	
	@Override
	public void createPartControl(Composite parent) {
		final Composite container = new Composite(parent, SWT.NONE);
		final FillLayout layout = new FillLayout();
		layout.marginHeight = layout.marginWidth = 0;
		container.setLayout(layout);
		
		gridViewer = new GridTableViewer(container, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);		
		gridViewer.setContentProvider(createContentProvider());

		gridViewer.getGrid().setHeaderVisible(true);
		gridViewer.getGrid().setLinesVisible(true);
		
		jobManagerListener = ScenarioViewerSynchronizer.registerView(gridViewer, createElementCollector());

	}
	
	private IScenarioInstanceElementCollector createElementCollector() {
		return new IScenarioInstanceElementCollector() {
			
			@Override
			public void endCollecting() {
				
			}
			
			@Override
			public Collection<? extends Object> collectElements(
					LNGScenarioModel rootObject, boolean isPinned) {
				ArrayList<LNGScenarioModel> result = new ArrayList<LNGScenarioModel>();
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
			Date startDate = null;
			Date endDate = null;
			Date [] dates = null;

			@Override
			public void dispose() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
				input = newInput;
				setCols((IScenarioViewerSynchronizerOutput) newInput);
				setRows((IScenarioViewerSynchronizerOutput) newInput);
				
			}
			
			private void setRows(IScenarioViewerSynchronizerOutput sync) {
				startDate = null;
				endDate = null;
				
				if (sync == null) {
					dates = new Date[0];
					return;
				}
				
				for (LNGScenarioModel model: sync.getLNGScenarioModels()) {
					ScheduleModel scheduleModel = model.getPortfolioModel().getScheduleModel();
					if (scheduleModel != null) {
						Schedule schedule = scheduleModel.getSchedule();
						if (schedule != null) {
							for (Sequence seq: schedule.getSequences()) {
								for (Event event: seq.getEvents()) {
									Date sDate = event.getStart();
									Date eDate = event.getEnd();
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
				}
				
				if (startDate != null && endDate != null) {
					ArrayList<Date> allDates = new ArrayList<Date>();
					final Calendar c = Calendar.getInstance();
					c.setTime(startDate);
					c.set(Calendar.HOUR_OF_DAY, 0);
					c.set(Calendar.MINUTE, 0);
					c.set(Calendar.SECOND, 0);
					allDates.add(c.getTime());
					while (!c.getTime().after(endDate)) {
						c.add(Calendar.DAY_OF_MONTH, 1);
						allDates.add(c.getTime());
					}
					
					dates = allDates.toArray(new Date[0]);
				}
			}
				

			@Override
			public Object[] getElements(Object inputElement) {
				return dates;
			}
			
		};
	}

	protected void setCols(IScenarioViewerSynchronizerOutput sync) {
		// clear the grid columns; we will have to replace them with vessels from the new scenario
		for (GridColumn column: gridViewer.getGrid().getColumns()) {
			column.dispose();
		}

		// add a "Date" column
		GridViewerColumn dateColumn = new GridViewerColumn(gridViewer, SWT.COLOR_GRAY);
		dateColumn.setLabelProvider(new DateColumnLabelProvider());
		dateColumn.getColumn().setText("Date");
		dateColumn.getColumn().setWidth(60);
		
		
		if (sync == null) {
			return;
		}
		
		Sequence desSequence = null;
		Sequence fobSequence = null;
		
		for (LNGScenarioModel model: sync.getLNGScenarioModels()) {
			ScheduleModel scheduleModel = model.getPortfolioModel().getScheduleModel();
			if (scheduleModel != null) {
				Schedule schedule = scheduleModel.getSchedule();

				if (schedule != null) {
					for (Sequence seq: schedule.getSequences()) {
						if (seq.getSequenceType() == SequenceType.DES_PURCHASE) {
							desSequence = seq;
						}
						else if (seq.getSequenceType() == SequenceType.FOB_SALE) {
							fobSequence = seq;
						}
						else {
							GridViewerColumn column = new GridViewerColumn(gridViewer, SWT.NONE);
							column.setLabelProvider(new EventColumnLabelProvider(new SequenceEventProvider(seq)));
							column.getColumn().setText(seq.getName());
							column.getColumn().pack();
						}
					}
				}
			}
		}		

		// add a "FOB/DES" column
		if (desSequence != null || fobSequence != null) {
			Sequence [] sequences;
			if (desSequence == null) { sequences = new Sequence [] { fobSequence }; } 
			else if (fobSequence == null) { sequences = new Sequence [] { desSequence }; }
			else { sequences = new Sequence [] { desSequence, fobSequence }; }
			GridViewerColumn fobDesColumn = new GridViewerColumn(gridViewer, SWT.COLOR_GRAY);
			fobDesColumn.setLabelProvider(new EventColumnLabelProvider(new SequenceEventProvider(sequences)));
			fobDesColumn.getColumn().setText("FOB/DES");
			fobDesColumn.getColumn().pack();
		}
		
		gridViewer.refresh();
	}
	
	/**
	 * Returns the events, if any, occurring between the two dates specified.
	 */
	protected static Event [] getEvents(Sequence seq, Date start, Date end) {
		ArrayList<Event> result = new ArrayList<>();
		if (seq != null && start != null && end != null) {
			for (Event event: seq.getEvents()) {
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
	 * @param seq
	 * @param date
	 * @return
	 */
	protected static Event [] getEvents(Sequence seq, Date date) {
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
	
	static abstract class CalendarColumnLabelProvider<T> extends ColumnLabelProvider {
		/**
		 * This class allows for convenient column label provider creation in a calendar
		 * grid: the provider is initialised with a particular data object (e.g. a 
		 * sequence from a schedule) and will delegate cell formatting & contents
		 * to methods based on the data object and the date.
		 */
		protected T data;
		
		public CalendarColumnLabelProvider(T object) {
			data = object;
		}
		
		@Override
		public String getText(Object element) {
			return getText((Date) element, data);
		}

		@Override
		public Font getFont(Object element) {
			return getFont((Date) element, data);
		}


		@Override
		public Color getBackground(Object element) {
			return getBackground((Date) element, data);
		}

		@Override
		public Color getForeground(Object element) {
			return getForeground((Date) element, data);
		}

		/** Returns the text content of the cell. */ 
		abstract protected String getText(Date element, T object);
		
		/** Returns the desired font of the cell. */
		protected Font getFont(Date element, T object) {
			return null;
		}
		
		/** Returns the desired font of the cell. */
		protected Color getBackground(Date element, T object) {
			return null;
		}
		
		/** Returns the desired font of the cell. */
		protected Color getForeground(Date element, T object) {
			return null;
		}
		
	}
	
	class EventColumnLabelProvider extends CalendarColumnLabelProvider<EventProvider<?>> {
		public EventColumnLabelProvider(EventProvider<?> provider) {
			super(provider);
		}
		
		protected String getText(Date element, Event event) {
			// how many days since the start of the event?
			Long days = (element.getTime() - event.getStart().getTime()) / (24*1000*3600);
			
			// Journey events just show the day number
			if (event instanceof Journey) {
				days += 1;
				return days.toString();
			}
			
			else if (event instanceof SlotVisit || event instanceof Idle) {
				// find the preceding journey event
				Event prev = event.getPreviousEvent();
				while (prev != null && (prev instanceof Journey) == false ) {
					prev = prev.getPreviousEvent();
				}
				
				if (prev instanceof Journey) {
					Journey journey = (Journey) prev;
					String portName = journey.getDestination().getName();
					double durationInDays = (double) journey.getDuration() / 24.0; 
					double speedInKnots = journey.getSpeed();
					int distanceInMiles = journey.getDistance();
					return String.format("Arrival at %s (%.02f days at %.02f knots - %d miles)", portName, durationInDays, speedInKnots, distanceInMiles);
				}
			}
			
			EClass eventClass = event.eClass();
			return eventClass.getName() + " '" + event.name() + "' " + days.toString();
			
		}

		@Override
		protected String getText(Date element, EventProvider<?> provider) {
			// find the event for the date given
			Event [] events = provider.getEvents(element);
			
			String result = "";
			
			for (Event event: events) {
				if (result.equals("") == false) {
					result += "; ";
				}
				result += getText(element, event);
			}
			
			return result;
		}
		
	}
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM");

	class DateColumnLabelProvider extends ColumnLabelProvider {
		@Override
		public String getText(Object element) {
			return sdf.format(element);
		}
		
	}
	
	/**
	 * Class to provide events to an EventDisplay column.
	 * 
	 * @author Simon McGregor
	 *
	 * @param <T> The data type to initialise the event provider with.
	 */
	abstract static class EventProvider<T> {
		final protected T data;
		
		public EventProvider(T data) {
			this.data = data;
		}
		
		protected Event [] getEvents(Date date) {
			ArrayList<Event> result = new ArrayList<>();
			
			for (Event event: getUnfilteredEvents(date)) {
				if (filterEventOut(date, event) == false) {
					result.add(event);
				}
			}
			
			return result.toArray(new Event[0]);
		}
		
		protected abstract Event [] getUnfilteredEvents(Date date); 

		protected boolean filterEventOut(Date date, Event event) {
			return false;
		}
	}
	
	static class SequenceEventProvider extends EventProvider<Sequence []> {
		public SequenceEventProvider(Sequence [] data) {
			super(data);
		}
		
		public SequenceEventProvider(Sequence seq) {
			super(new Sequence[] { seq });
		}

		@Override
		public Event [] getUnfilteredEvents(Date date) {
			ArrayList<Event> result = new ArrayList<>();
			
			for (Sequence seq: data) {
				Event [] events = AbstractVerticalCalendarReportView.getEvents(seq, date);
				for (Event event: events) {
					result.add(event);
				}
			}
			
			return result.toArray(new Event[0]);
		}		
	}

	
	
	class NonFleetColumnLabelProvider extends CalendarColumnLabelProvider<Schedule> {
		Sequence desPurchases;
		Sequence fobSales;

		public NonFleetColumnLabelProvider(Schedule schedule) {
			super(schedule);
			if (schedule != null) {
				for (Sequence seq: schedule.getSequences()) {
					VesselAvailability availability = seq.getVesselAvailability();
					
				}
			}
		}

		@Override
		protected String getText(Date element, Schedule object) {
			
			// TODO Auto-generated method stub
			return null;
		}


		
	}
	
}
