/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.reports.views;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.IElementComparer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.PropertySheet;
import org.eclipse.ui.views.properties.PropertySheetPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.mmxlabs.common.CollectionsUtil.ASet;
import com.mmxlabs.common.Equality;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.ui.tabular.EObjectTableViewer;
import com.mmxlabs.models.ui.tabular.ICellManipulator;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.ui.tabular.filter.FilterField;
import com.mmxlabs.models.util.emfpath.CompiledEMFPath;
import com.mmxlabs.models.util.emfpath.EMFPath;
import com.mmxlabs.rcp.common.actions.CopyGridToClipboardAction;
import com.mmxlabs.rcp.common.actions.PackActionFactory;
import com.mmxlabs.shiplingo.platform.reports.IScenarioInstanceElementCollector;
import com.mmxlabs.shiplingo.platform.reports.IScenarioViewerSynchronizerOutput;
import com.mmxlabs.shiplingo.platform.reports.ScenarioViewerSynchronizer;
import com.mmxlabs.shiplingo.platform.reports.properties.ScheduledEventPropertySourceProvider;

/**
 * Base class for views which show things from the EMF output model.
 * 
 * This shares a lot of function with EObjectTableViewer
 * 
 * @author hinton
 * 
 */
/**
 * @author berkan
 *
 */
/**
 * @author berkan
 *
 */
public abstract class EMFReportView extends ViewPart implements ISelectionListener {
	private static final Logger log = LoggerFactory.getLogger(new Object(){}.getClass().getEnclosingClass());
	
	private final List<ColumnHandler> handlers = new ArrayList<ColumnHandler>();
	private final List<ColumnHandler> handlersInOrder = new ArrayList<ColumnHandler>();
	private FilterField filterField;

	private boolean currentlyPinned = false;

	private int numberOfSchedules;

	private final Map<String, List<EObject>> allObjectsByKey = new LinkedHashMap<String, List<EObject>>();


	
	protected EMFReportView() {
		this(null);
	}

	protected EMFReportView(final String helpContextId) {
		this.helpContextId = helpContextId;
	}

	private final ICellManipulator noEditing = new ICellManipulator() {
		@Override
		public void setValue(final Object object, final Object value) {

		}

		@Override
		public Object getValue(final Object object) {
			return null;
		}

		@Override
		public CellEditor getCellEditor(final Composite parent, final Object object) {
			return null;
		}

		@Override
		public boolean canEdit(final Object object) {
			return false;
		}
	};

	protected class ColumnHandler {
		private static final String COLUMN_HANDLER = "COLUMN_HANDLER";
		private final IFormatter formatter;
		private final EMFPath path;
		private final String title;
		private final String tooltip;
		public GridViewerColumn column;

		public ColumnHandler(final IFormatter formatter, final Object[] features, final String title) {
			this(formatter, features, title, null);
		}

		public ColumnHandler(final IFormatter formatter, final Object[] features, final String title, final String tooltip) {
			super();
			this.formatter = formatter;
			this.path = new CompiledEMFPath(getClass().getClassLoader(), true, features);
			this.title = title;
			this.tooltip = tooltip;
		}

		public GridViewerColumn createColumn(final EObjectTableViewer viewer) {
			final GridViewerColumn column = viewer.addColumn(title, new ICellRenderer() {
				@Override
				public String render(final Object object) {
					return formatter.format(object);
				}

				@Override
				public Iterable<Pair<Notifier, List<Object>>> getExternalNotifiers(final Object object) {
					// TODO fix this
					return Collections.emptySet();
				}

				@Override
				public Comparable getComparable(final Object object) {
					return formatter.getComparable(object);
				}

				@Override
				public Object getFilterValue(final Object object) {
					return formatter.getFilterable(object);
				}
			}, noEditing, path);

			final GridColumn tc = column.getColumn();
			tc.setData(COLUMN_HANDLER, this);
			this.column = column;

			if (tooltip != null) {
				column.getColumn().setHeaderTooltip(tooltip);
			}

			return column;
		}
	}

	/**
	 * @since 5.0
	 */
	public interface IFormatter {
		public String format(final Object object);

		public Comparable getComparable(final Object object);

		public Object getFilterable(final Object object);
	}

	protected final IFormatter containingScheduleFormatter = new BaseFormatter() {
		@Override
		public String format(final Object object) {
			return synchronizerOutput.getScenarioInstance(object).getName();
		}

	};
	protected final IFormatter objectFormatter = new BaseFormatter();

	public class BaseFormatter implements IFormatter {
		@Override
		public String format(final Object object) {
			if (object == null) {
				return "";
			} else {
				return object.toString();
			}
		}

		@Override
		public Comparable getComparable(final Object object) {
			return format(object);
		}

		@Override
		public Object getFilterable(final Object object) {
			return getComparable(object);
		}
	}

	public class IntegerFormatter implements IFormatter {
		public Integer getIntValue(final Object object) {
			if (object == null) {
				return null;
			}
			return ((Number) object).intValue();
		}

		@Override
		public String format(final Object object) {
			if (object == null) {
				return "";
			}
			final Integer x = getIntValue(object);
			if (x == null) {
				return "";
			}
			return String.format("%,d", x);
		}

		@Override
		public Comparable getComparable(final Object object) {
			final Integer x = getIntValue(object);
			if (x == null) {
				return -Integer.MAX_VALUE;
			}
			return x;
		}

		@Override
		public Object getFilterable(final Object object) {
			return getComparable(object);
		}
	}

	/**
	 * Formatter to format a floating point number to a given number of decimal places.
	 * 
	 * @author Simon Goodall
	 * @since 5.1
	 * 
	 */
	public class NumberOfDPFormatter implements IFormatter {

		private final int dp;

		public NumberOfDPFormatter(final int dp) {
			Preconditions.checkArgument(dp >= 0);
			this.dp = dp;
		}

		public Double getDoubleValue(final Object object) {
			if (object == null) {
				return null;
			}
			return ((Number) object).doubleValue();
		}

		@Override
		public String format(final Object object) {
			if (object == null) {
				return "";
			}
			final Double x = getDoubleValue(object);
			if (x == null) {
				return "";
			}
			return String.format("%,." + dp + "f", x);
		}

		@Override
		public Comparable getComparable(final Object object) {
			final Double x = getDoubleValue(object);
			if (x == null) {
				return -Double.MAX_VALUE;
			}
			return x;
		}

		@Override
		public Object getFilterable(final Object object) {
			return getComparable(object);
		}
	}

	/**
	 * @since 4.0
	 */
	public class CostFormatter implements IFormatter {

		private final boolean includeUnits;

		public CostFormatter(final boolean includeUnits) {
			this.includeUnits = includeUnits;
		}

		public Integer getIntValue(final Object object) {
			if (object == null) {
				return null;
			}
			return ((Number) object).intValue();
		}

		@Override
		public String format(final Object object) {
			if (object == null) {
				return "";
			}
			final Integer x = getIntValue(object);
			if (x == null) {
				return "";
			}
			return String.format(includeUnits ? "$%,d" : "%,d", x);
		}

		@Override
		public Comparable getComparable(final Object object) {
			final Integer x = getIntValue(object);
			if (x == null) {
				return -Integer.MAX_VALUE;
			}
			return x;
		}

		@Override
		public Object getFilterable(final Object object) {
			return getComparable(object);
		}
	}

	/**
	 * @since 4.0
	 */
	public class PriceFormatter implements IFormatter {

		private final boolean includeUnits;

		public PriceFormatter(final boolean includeUnits) {
			this.includeUnits = includeUnits;
		}

		public Double getDoubleValue(final Object object) {
			if (object == null) {
				return null;
			}
			return ((Number) object).doubleValue();
		}

		@Override
		public String format(final Object object) {
			if (object == null) {
				return "";
			}
			final Double x = getDoubleValue(object);
			if (x == null) {
				return "";
			}
			return String.format(includeUnits ? "$%,.2f" : "%,.2f", x);
		}

		@Override
		public Comparable getComparable(final Object object) {
			final Double x = getDoubleValue(object);
			if (x == null) {
				return -Double.MAX_VALUE;
			}
			return x;
		}

		@Override
		public Object getFilterable(final Object object) {
			return getComparable(object);
		}
	}

	protected class CalendarFormatter extends BaseFormatter {
		final DateFormat dateFormat;
		final boolean showZone;

		public CalendarFormatter(final DateFormat dateFormat, final boolean showZone) {
			this.dateFormat = dateFormat;
			this.showZone = showZone;
		}

		@Override
		public String format(final Object object) {
			if (object == null) {
				return "";
			}
			final Calendar cal = (Calendar) object;

			dateFormat.setCalendar(cal);
			return dateFormat.format(cal.getTime()) + (showZone ? (" (" + cal.getTimeZone().getDisplayName(false, TimeZone.SHORT) + ")") : "");
		}

		@Override
		public Comparable getComparable(final Object object) {
			if (object == null) {
				return new Date(-Long.MAX_VALUE);
			}
			return ((Calendar) object).getTime();
		}

		@Override
		public Object getFilterable(final Object object) {
			if (object instanceof Calendar) {
				return object;
			}
			return object;
		}
	}

	protected final IFormatter calendarFormatter = new CalendarFormatter(DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT), true);
	protected final IFormatter calendarFormatterNoTZ = new CalendarFormatter(DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT), false);

	protected final IFormatter datePartFormatter = new CalendarFormatter(DateFormat.getDateInstance(DateFormat.SHORT), false);
	protected final IFormatter timePartFormatter = new CalendarFormatter(DateFormat.getTimeInstance(DateFormat.SHORT), false);

	protected final IntegerFormatter integerFormatter = new IntegerFormatter();
	//
	// protected final IFormatter costFormatter = new IntegerFormatter() {
	// @Override
	// public Integer getIntValue(final Object object) {
	// if (object == null) {
	// return null;
	// }
	// return -super.getIntValue(object);
	// }
	// };

	/**
	 * @since 4.0
	 */
	protected EObjectTableViewer viewer;

	private Action packColumnsAction;
	private Action copyTableAction;
//BE	private Action sortModeAction; //BE
	
	
	private final String helpContextId;
	private ScenarioViewerSynchronizer jobManagerListener;

	protected IScenarioViewerSynchronizerOutput synchronizerOutput = null;

	private ColumnHandler scheduleColumnHandler;

	/**
	 * @since 5.0
	 */
	protected ITreeContentProvider getContentProvider() {
		return new ITreeContentProvider() {
			@Override
			public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
				synchronizerOutput = null;
				if (newInput instanceof IScenarioViewerSynchronizerOutput) {
					synchronizerOutput = (IScenarioViewerSynchronizerOutput) newInput;
					if (scheduleColumnHandler != null) {
						final GridViewerColumn c = scheduleColumnHandler.column;
						if (c != null) {
							c.getColumn().setVisible(synchronizerOutput.getLNGPortfolioModels().size() > 1);
						}
					}
		
					// Add Difference/Change columns when in Pin/Diff mode
					addDiffColumnsToTableIf(numberOfSchedules > 1 && currentlyPinned);
				}
			}

			@Override
			public void dispose() {
				synchronizerOutput = null;
			}

			@Override
			public Object[] getElements(final Object inputElement) {

				clearInputEquivalents();
				
				final Object[] result;
				Map<String, EObject> pinnedObjects = pinDiffModeManager.getPinnedObjects();				
				final Collection<EObject> pinnedObjectsSet = pinnedObjects.values();
				
				if (numberOfSchedules > 1 && currentlyPinned) {
					final List<EObject> objects = new LinkedList<EObject>();
					for (final Map.Entry<String, List<EObject>> e : allObjectsByKey.entrySet()) {
						EObject ref = null;
						final LinkedHashSet<EObject> objectsToAdd = new LinkedHashSet<EObject>();

						// Find ref...
						for (final EObject ca : e.getValue()) {
							if (pinnedObjectsSet.contains(ca)) {
								ref = ca;
								break;
							}
						}

						if (ref == null) {
							// No ref found, so add all
							objectsToAdd.addAll(e.getValue());
						} else {
							for (final EObject ca : e.getValue()) {
								if (e.getValue().size() != numberOfSchedules) {
									// Different number of elements, so add all!
									// This means something has been removed/added
									objectsToAdd.addAll(e.getValue());
								}
								if (ca == ref) {
									continue;
								}
								if (isElementDifferent(ref, ca)) {
									// There is a data difference, so add
									objectsToAdd.addAll(e.getValue());
								}
							}
						}
						objects.addAll(objectsToAdd);

					}
					result = objects.toArray();

				} else {
					if (inputElement instanceof IScenarioViewerSynchronizerOutput) {
						final IScenarioViewerSynchronizerOutput output = (IScenarioViewerSynchronizerOutput) inputElement;
						result = output.getCollectedElements().toArray(new Object[output.getCollectedElements().size()]);
					} else {
						result = new Object[0];
					}
				}

				processInputs(result);

				return result;
			}

			@Override
			public Object[] getChildren(final Object parentElement) {
				return null;
			}

			@Override
			public Object getParent(final Object element) {
				return null;
			}

			@Override
			public boolean hasChildren(final Object element) {
				return false;
			}

		};
	}

	protected ColumnHandler addScheduleColumn(final String title, final IFormatter formatter, final Object... path) {
		final ColumnHandler handler = addColumn(title, formatter, path);
		scheduleColumnHandler = handler;
		return handler;
	}

	protected ColumnHandler addColumn(final String title, final IFormatter formatter, final Object... path) {
		final ColumnHandler handler = new ColumnHandler(formatter, path, title);
		handlers.add(handler);
		handlersInOrder.add(handler);

		if (viewer != null) {
			handler.createColumn(viewer).getColumn().pack();
		}
		return handler;
	}


	/**
	 * Generate a new formatter for the previous-wiring column
	 *  
	 * Used in pin/diff mode.
	 *  
	 * @param cargoAllocationRef
	 * @return
	 */
	protected IFormatter generatePreviousWiringColumnFormatter(final EStructuralFeature cargoAllocationRef) {
		return new BaseFormatter() {
	    	 @Override
	    	 public String format(final Object obj) {
	    		 
	    		 String wiringAsString = "";
	    		 
	    		 if (obj instanceof EObject) {
	    			 final EObject eObj = (EObject) obj;

	    			 	if (eObj.eIsSet(cargoAllocationRef)) {

	    			 		final CargoAllocation thisCargoAllocation = (CargoAllocation) eObj.eGet(cargoAllocationRef);
	    			 		updateRelatedSlotAllocationSets(thisCargoAllocation);
	    			 		
	    			 		// for objects not coming from the pinned scenario, 
	    			 		// return the pinned counterpart's wiring to display as the previous wiring 
	    			 		if (! pinDiffModeManager.pinnedObjectsContains(eObj)) {
	    			 			
	    			 			try { 
	    			 				EObject pinnedObject = pinDiffModeManager.getPinnedObjectWithTheSameKeyAsThisObject(eObj);	    			 				
	    			 				final CargoAllocation pinnedCargoAllocation = (CargoAllocation) pinnedObject.eGet(cargoAllocationRef);

	    			 				updateRelatedSlotAllocationSets(pinnedCargoAllocation);
	    			 				
	    			 				// convert this cargo's wiring of slot allocations to a string
	    			 				wiringAsString = pinnedCargoAllocation.getWiringAsString();
	    			 			} 
	    			 			catch (Exception e) {
	    			 				log.warn("Error formatting previous wiring", e);
	    			 			}
	    			 		
	    			 		}
	    			 	}
	    		 }

	    		 return wiringAsString;
	    	 }
		};
	}

	
	protected Map<String, Set<String>> slotsAndTheirRelatedSets = new HashMap<String, Set<String>>();
	
	private void updateRelatedSlotAllocationSets(CargoAllocation cargoAllocation) {
		List<SlotAllocation> slotAllocations = cargoAllocation.getSlotAllocations();
		
		for (int i = 1; i < slotAllocations.size(); ++i) {

			// get the names of the slots at either end	of this wiring		
			String sA = slotAllocations.get(i-1).getName();
			String sB = slotAllocations.get(i).getName();
			
			// get/create the sets of slots these wired slots are related to
			Set<String> setA = slotsAndTheirRelatedSets.containsKey(sA) ? 
						slotsAndTheirRelatedSets.get(sA)
						: ASet.of(sA);
						
			Set<String> setB = slotsAndTheirRelatedSets.containsKey(sB) ? 
						slotsAndTheirRelatedSets.get(sB)
						: ASet.of(sB);
			
			// merge the two sets
			setA.addAll(setB);
			Set<String> mergedSet = setA;

			// make sure all slots in the mergedSet are related to the new mergedSet
			for (final String slotName : mergedSet) {
				slotsAndTheirRelatedSets.put(slotName, mergedSet);
			}
			
		}
	}
	
	/**
	 * Generate a new formatter for the previous-vessel-assignment column
	 * 
	 * Used in pin/diff mode.
	 * 
	 * @param cargoAllocationRef
	 * @return
	 */
	protected IFormatter generatePreviousVesselAssignmentColumnFormatter(final EStructuralFeature cargoAllocationRef) {
		return new BaseFormatter() {
			@Override
			public String format(final Object obj) {
				String vesselName = "";
				
				if (obj instanceof EObject) {
	    			 final EObject eObj = (EObject) obj;

	    			 	if (eObj.eIsSet(cargoAllocationRef)) {
	    			 		
	    			 		// for objects not coming from the pinned scenario,  
	    			 		// return and display the vessel used by the pinned counterpart
	    			 		if (! pinDiffModeManager.pinnedObjectsContains(eObj)) {
	    			 		
	    			 			// TODO: Q: can any of these lookups return null?
	    			 			try { 
		    			 			EObject pinnedObject = pinDiffModeManager.getPinnedObjectWithTheSameKeyAsThisObject(eObj);
	    			 				final CargoAllocation ca = (CargoAllocation) pinnedObject.eGet(cargoAllocationRef);
	    			 				final Vessel vessel = (Vessel) ca.getInputCargo().getAssignment();
	    			 				if (vessel != null)
	    			 					vesselName = vessel.getName();
	    			 			}
	    			 			catch (Exception e) {
	    			 				log.warn("Error formatting previous assignment", e);
	    			 			}	
	    			 		}	
	    			 	}
				}
				return vesselName;
			}
		};
	}                          
	
	
	/**
	 * Instance of PinDiffColumnManager which all view subclasses inherit
	 */
	protected PinDiffModeManager pinDiffModeManager = new PinDiffModeManager();	


	/**
	 * Helper method for conditionally adding or removing all registered columns 
	 * 
	 * @param add
	 */
	protected void addDiffColumnsToTableIf(final boolean add) {
		pinDiffModeManager.addAllColumnsToTableIf(add);
	}	
	
	/**
	 * Helper class for managing columns that appear when in Pin/Diff mode
	 * 
	 * @author berkan
	 */
	protected class PinDiffModeManager {
		private Map<String, IFormatter> columnRegister = new LinkedHashMap<String, IFormatter>();

		private final Map<String, EObject> pinnedObjects = new LinkedHashMap<String, EObject>();
		
		public Map<String, EObject> getPinnedObjects() { 
			return pinnedObjects; 
		}
		
		public final boolean pinnedObjectsContains(EObject obj) { 
			return pinnedObjects.containsValue(obj); 
		}
		
		public EObject getPinnedObjectWithTheSameKeyAsThisObject(EObject eObj) { 
			return pinnedObjects.get(getElementKey(eObj)); 
		}
		
		
		/**
		 * Register a column to be added to the table/view when in Pin/Diff mode
		 * 
		 * @param title
		 * @param formatter
		 * @return this, for chaining
		 */
		public PinDiffModeManager addColumn(final String title, final IFormatter formatter) {
			columnRegister.put(title, formatter); 
			return this;
		}
				
		/**
		 * Conditionally add or remove all Pin/Diff mode columns to/from the table
		 * 
		 * @param add
		 * @return this, for chaining
		 */
		public PinDiffModeManager addAllColumnsToTableIf(boolean add) {
			// BE: leaving the code in this form to be clear what it does
			
			if (add) {
				// first reset the table to its "unpinned" state
				removeAllColumnsFromTable();

				// then add all registered pin/diff columns
				for (final String title : columnRegister.keySet()) {
					addColumnToTable(title);
				}
			}
			else {
				// reset the table to its "unpinned" state
				removeAllColumnsFromTable();
			}
			
			return this;
		}

		/**
		 * Add to the table the column registered with title
		 * 
		 * @param title
		 * @return this, for chaining
		 */
		public PinDiffModeManager addColumnToTable(String title) {
			IFormatter formatter = columnRegister.get(title);
			if (title != null)
				EMFReportView.this.addColumn(title, formatter);
			return this;
		}
		
		/**
		 * Remove all registered columns from the table
		 * 
		 * @return this, for chaining
		 */
		public PinDiffModeManager removeAllColumnsFromTable() {
			for (final String title : columnRegister.keySet()) {
				removeColumnFromTable(title);
			}			
			return this;
		}
		
		
		/**
		 * Remove from the table the column registered with the title
		 * 
		 * @param title
		 * @return this, for chaining
		 */
		public PinDiffModeManager removeColumnFromTable(String title) {
			 EMFReportView.this.removeColumn(title);
			 return this;
		}		

		/**
		 * Deregister, and remove from the table the column registered with the title
		 * 
		 * @param title
		 * @return this, for chaining
		 */
		public PinDiffModeManager removeColumn(String title) {
			columnRegister.remove(title);
			removeColumnFromTable(title);
			return this;
		}
		
	}
	
		
	/**
	 * Similar to {@link #addColumn(String, IFormatter, Object...)} but supports a column tooltip
	 * 
	 * @param title
	 * @param tooltip
	 * @param formatter
	 * @param path
	 * @return
	 * @since 4.0
	 */
	protected ColumnHandler addColumn(final String title, final String tooltip, final IFormatter formatter, final Object... path) {
		final ColumnHandler handler = new ColumnHandler(formatter, path, title, tooltip);
		handlers.add(handler);
		handlersInOrder.add(handler);

		if (viewer != null) {
			handler.createColumn(viewer).getColumn().pack();
		}
		return handler;
	}

	private final HashMap<Object, Object> equivalents = new HashMap<Object, Object>();
	private final HashSet<Object> contents = new HashSet<Object>();

	protected void setInputEquivalents(final Object input, final Collection<Object> objectEquivalents) {
		for (final Object o : objectEquivalents) {
			equivalents.put(o, input);
		}
		contents.add(input);
	}

	protected void clearInputEquivalents() {
		equivalents.clear();
		contents.clear();
	}

	@Override
	public void createPartControl(final Composite parent) {
		final Composite container = new Composite(parent, SWT.NONE);
		filterField = new FilterField(container);
		final GridLayout layout = new GridLayout(1, false);
		layout.marginHeight = layout.marginWidth = 0;
		container.setLayout(layout);

		viewer = new EObjectTableViewer(container, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION) {
			@Override
			protected void inputChanged(final Object input, final Object oldInput) {
				super.inputChanged(input, oldInput);

				final boolean inputEmpty = (input == null) || ((input instanceof IScenarioViewerSynchronizerOutput) && ((IScenarioViewerSynchronizerOutput) input).getCollectedElements().isEmpty());
				final boolean oldInputEmpty = (oldInput == null)
						|| ((oldInput instanceof IScenarioViewerSynchronizerOutput) && ((IScenarioViewerSynchronizerOutput) oldInput).getCollectedElements().isEmpty());

				if (inputEmpty != oldInputEmpty) {
					// Disabled because running this takes up 50% of the runtime when displaying a new schedule (!)
					// if (packColumnsAction != null) {
					// packColumnsAction.run();
					// }
				}
			};

			@Override
			protected List<?> getSelectionFromWidget() {

				final List<?> list = super.getSelectionFromWidget();

				return adaptSelectionFromWidget(list);
			}

			/**
			 * Modify @link {AbstractTreeViewer#getTreePathFromItem(Item)} to adapt items before returning selection object.
			 */
			@Override
			protected TreePath getTreePathFromItem(Item item) {
				final LinkedList<Object> segments = new LinkedList<>();
				while (item != null) {
					final Object segment = item.getData();
					Assert.isNotNull(segment);
					segments.addFirst(segment);
					item = getParentItem(item);
				}
				final List<?> l = adaptSelectionFromWidget(segments);

				return new TreePath(l.toArray());
			}
		};

		filterField.setFilterSupport(viewer.getFilterSupport());

		viewer.getGrid().setLayoutData(new GridData(GridData.FILL_BOTH));

		// this is very slow on refresh
		viewer.setDisplayValidationErrors(false);

		//BE
//		
//		viewer.setComparator(new ViewerComparator() {			
//			@Override
//			public int category(Object obj) { log.error("comparing"); return 0; }
//		});
		//BE
		
		if (handleSelections()) {
			viewer.setComparer(new IElementComparer() {
				@Override
				public int hashCode(final Object element) {
					return element.hashCode();
				}

				@Override
				public boolean equals(Object a, Object b) {
					if (!contents.contains(a) && equivalents.containsKey(a)) {
						a = equivalents.get(a);
					}
					if (!contents.contains(b) && equivalents.containsKey(b)) {
						b = equivalents.get(b);
					}
					return Equality.isEqual(a, b);
				}
			});
		}

		viewer.getGrid().setHeaderVisible(true);
		viewer.getGrid().setLinesVisible(true);

		for (final ColumnHandler handler : handlersInOrder) {
			handler.createColumn(viewer).getColumn().pack();
		}

		if (helpContextId != null) {
			PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), helpContextId);
		}

		makeActions();
		hookContextMenu();
		contributeToActionBars();

		viewer.init(getContentProvider(), null);

		getSite().setSelectionProvider(viewer);
		if (handleSelections()) {
			getSite().getWorkbenchWindow().getSelectionService().addPostSelectionListener(this);
		}

		jobManagerListener = ScenarioViewerSynchronizer.registerView(viewer, getElementCollector());
	}

	protected abstract IScenarioInstanceElementCollector getElementCollector();

	private void hookContextMenu() {
		final MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(final IMenuManager manager) {
				EMFReportView.this.fillContextMenu(manager);
			}
		});
		final Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		final IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(final IMenuManager manager) {
	}

	private void fillContextMenu(final IMenuManager manager) {
		// Other plug-ins can contribute their actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(final IToolBarManager manager) {
		manager.add(new GroupMarker("filter"));
//BE		manager.add(new GroupMarker("sortmode")); //BE		
		manager.add(new GroupMarker("pack"));
		manager.add(new GroupMarker("additions"));
		manager.add(new GroupMarker("edit"));
		manager.add(new GroupMarker("copy"));
		manager.add(new GroupMarker("importers"));
		manager.add(new GroupMarker("exporters"));

		manager.appendToGroup("filter", filterField.getContribution());
//BE		manager.appendToGroup("sortmode", sortModeAction); //BE
		manager.appendToGroup("pack", packColumnsAction);
		manager.appendToGroup("copy", copyTableAction);
	}

	private void makeActions() {
		packColumnsAction = PackActionFactory.createPackColumnsAction(viewer);
		copyTableAction = new CopyGridToClipboardAction(viewer.getGrid());
		getViewSite().getActionBars().setGlobalActionHandler(ActionFactory.COPY.getId(), copyTableAction);

		//BE
//		sortModeAction = new Action("S", IAction.AS_CHECK_BOX) {
//			@Override
//			public void run() {
//				log.error("hello");
//				viewer.setInput(viewer.getInput());
//				viewer.refresh();
//			}
//		};
		//BE
	}

	@Override
	public void setFocus() {
		if (!viewer.getGrid().isDisposed()) {
			viewer.getGrid().setFocus();
		}
	}

	public void setInput(final Object input) {
		getSite().getShell().getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				if (!viewer.getControl().isDisposed()) {
					viewer.setInput(input);
				}
			}
		});
	}

	@Override
	public void selectionChanged(final IWorkbenchPart part, final ISelection selection) {
		if (part == this || part instanceof PropertySheet) {
			return;
		}

		viewer.setSelection(selection, true);
	}

	protected boolean handleSelections() {
		return false;
	}

	public void removeColumn(final String title) {
		for (final ColumnHandler h : handlers) {
			if (h.title.equals(title)) {
				viewer.removeColumn(h.column);
				handlers.remove(h);
				handlersInOrder.remove(h);
				break;
			}
		}
	}

	@Override
	public void dispose() {
		ScenarioViewerSynchronizer.deregisterView(jobManagerListener);

		super.dispose();
	}

	/**
	 * @since 1.0
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Object getAdapter(final Class adapter) {

		if (adapter.isAssignableFrom(IPropertySheetPage.class)) {
			final PropertySheetPage propertySheetPage = new PropertySheetPage();

			propertySheetPage.setPropertySourceProvider(new ScheduledEventPropertySourceProvider());
			return propertySheetPage;
		}
		return super.getAdapter(adapter);
	}

	/**
	 * Call from {@link IScenarioInstanceElementCollector#beginCollecting()} to reset pin mode data
	 * 
	 * @since 2.0
	 */
	protected void clearPinModeData() {
		clearInputEquivalents();
		currentlyPinned = false;
		allObjectsByKey.clear();
		pinDiffModeManager.getPinnedObjects().clear();
		numberOfSchedules = 0;
		slotsAndTheirRelatedSets.clear();
	}

	/**
	 * Call from {@link IScenarioInstanceElementCollector#collectElements(com.mmxlabs.models.mmxcore.MMXRootObject, boolean)} and pass in the list of collected objects rather than raw schedule.
	 * 
	 * @param objects
	 * @param isPinned
	 * @since 2.0
	 */
	protected void collectPinModeElements(final List<? extends EObject> objects, final boolean isPinned) {
		currentlyPinned |= isPinned;
		++numberOfSchedules;

		Map<String, EObject> pinnedObjects = pinDiffModeManager.getPinnedObjects();
		
		for (final EObject ca : objects) {
			final List<EObject> l;
			final String key = getElementKey(ca);
			if (allObjectsByKey.containsKey(key)) {
				l = allObjectsByKey.get(key);
			} else {
				l = new LinkedList<EObject>();
				allObjectsByKey.put(key, l);
			}

			l.add(ca);

			if (isPinned) {
				pinnedObjects.put(key, ca);
			}
		}
	}

	/**
	 * Compare an element to the pinned object and return true if different.
	 * 
	 * @since 2.0
	 */
	protected boolean isElementDifferent(final EObject pinnedObject, final EObject otherObject) {
		return true;
	}

	/**
	 * Returns a key of some kind for the element
	 * 
	 * @param element
	 * @return
	 * @since 2.0
	 */
	protected String getElementKey(final EObject element) {
		if (element instanceof NamedObject) {
			return ((NamedObject) element).getName();
		}
		return element.toString();
	}

	/**
	 * Process the array of elements before they are returned to e.g. map input equivalents
	 * 
	 * @since 2.0
	 * @param result
	 */
	protected void processInputs(final Object[] result) {

	}

	/**
	 * 
	 * Callback to convert the raw data coming out of the table into something usable externally. This is useful when the table data model is custom for the table rather from the real data model.
	 * 
	 * @since 4.0
	 */
	protected List<?> adaptSelectionFromWidget(final List<?> selection) {
		return selection;
	}
}
