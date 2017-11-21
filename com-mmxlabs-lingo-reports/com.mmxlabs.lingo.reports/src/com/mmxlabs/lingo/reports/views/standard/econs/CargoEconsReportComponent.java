/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard.econs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerRow;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.PropertySheet;

import com.mmxlabs.common.Pair;
import com.mmxlabs.lingo.reports.IReportContents;
import com.mmxlabs.lingo.reports.internal.Activator;
import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.lingo.reports.services.SelectedScenariosService;
import com.mmxlabs.lingo.reports.views.standard.econs.StandardEconsRowFactory.EconsOptions;
import com.mmxlabs.lingo.reports.views.standard.econs.StandardEconsRowFactory.EconsOptions.MarginBy;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.MarketAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelUtils;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;
import com.mmxlabs.models.ui.tabular.renderers.ColumnHeaderRenderer;
import com.mmxlabs.rcp.common.SelectionHelper;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.rcp.common.actions.CopyGridToHtmlStringUtil;
import com.mmxlabs.rcp.common.application.BindSelectionListener;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.ModelRecord;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.ui.ScenarioResult;
import com.mmxlabs.scenario.service.ui.editing.IScenarioServiceEditorInput;

/**
 * The {@link CargoEconsReportComponent} is a vertical report similar in concept to the Properties View. This table is the transpose of most other tables. Columns represent the input data and rows are
 * pre-defined.
 * 
 * @author Simon Goodall
 * 
 */
public class CargoEconsReportComponent implements IAdaptable /* extends ViewPart */ {

	@Inject
	private ESelectionService selectionService;

	@Inject
	private SelectedScenariosService selectedScenariosService;
	private List<Object> selectedObjects;
		
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.mmxlabs.shiplingo.platform.reports.views.CargoEconsReport";
	private GridTableViewer viewer;
	private final Collection<Pair<String, org.eclipse.e4.ui.workbench.modeling.ISelectionListener>> selectionListeners = new ConcurrentLinkedQueue<>();

	/**
	 * List of dynamically generated columns to be disposed on selection changes
	 */
	private final List<GridViewerColumn> dataColumns = new LinkedList<GridViewerColumn>();
	private final EconsOptions options = new EconsOptions();

	private Image pinImage = null;


	private boolean compareMode = true;
	private boolean onlyDiffMode = false;
	
	@PostConstruct
	public void createPartControl(final Composite parent) {

		final ImageDescriptor imageDescriptor = Activator.getPlugin().getImageDescriptor("icons/Pinned.gif");
		pinImage = imageDescriptor.createImage();

		viewer = new GridTableViewer(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		GridViewerHelper.configureLookAndFeel(viewer);
		ColumnViewerToolTipSupport.enableFor(viewer);

		// Add the name column
		{
			final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.NONE);
			GridViewerHelper.configureLookAndFeel(gvc);
			gvc.getColumn().setText("Name");
			gvc.setLabelProvider(new FieldTypeNameLabelProvider());
			gvc.getColumn().setWidth(100);
		}

		// All other columns dynamically added.

		// Array content provider as we pass in an array of enums
		viewer.setContentProvider(new ArrayContentProvider());
		// Our input!

		final List<CargoEconsReportRow> rows = new LinkedList<CargoEconsReportRow>();
		ServiceHelper.withAllServices(IEconsRowFactory.class, null, factory -> {
			rows.addAll(factory.createRows(options, null));
			return true;
		});
		Collections.sort(rows, (a, b) -> a.order - b.order);

		viewer.setInput(rows);
		viewer.getGrid().setHeaderVisible(true);

		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "com.mmxlabs.lingo.doc.Reports_CargoEcons");
	}

	@PreDestroy
	public void dispose() {
		if (pinImage != null) {
			pinImage.dispose();
			pinImage = null;

		}
		selectedObjects.clear();
		for (final Pair<String, org.eclipse.e4.ui.workbench.modeling.ISelectionListener> p : selectionListeners) {
			if (p.getFirst() == null) {
				selectionService.removePostSelectionListener(p.getSecond());
			} else {
				selectionService.removePostSelectionListener(p.getFirst(), p.getSecond());
			}
		}

	}

	@Focus
	public void setFocus() {
		ViewerHelper.setFocus(viewer);
	}
	
	public void setSelectedObject(Collection<Object> objects) {
		selectedObjects = new ArrayList(objects);
	}
	
	public List<Object> getSelectedObject() {
		return selectedObjects;
	}


	/**
	 * The {@link FieldType} is the row data. Each enum is a different row. Currently there is no table sorter so enum order is display order
	 * 
	 */

	/**
	 * A label provider for the "Name" column
	 * 
	 */
	private static class FieldTypeNameLabelProvider extends ColumnLabelProvider {

		@Override
		public Image getImage(final Object element) {
			return null;
		}

		@Override
		public String getText(final Object element) {
			if (element instanceof CargoEconsReportRow) {
				final CargoEconsReportRow fieldType = (CargoEconsReportRow) element;
				return fieldType.name; // + " (" + fieldType.getUnit() + ")";
			}
			return null;
		}

//		@Override
//		public void update(final ViewerCell cell) {
//			cell.setText(getText(cell.getElement()));
//		}

	}

	/**
	 * Label Provider created for each column. Returns text for the given {@link CargoEconsReportRow} enum (as the input element) for the {@link IFieldTypeMapper} object passed in during creation.
	 * 
	 */
	private static class FieldTypeMapperLabelProvider extends ColumnLabelProvider {

		private final Object columnElement;

		public FieldTypeMapperLabelProvider(final Object columnElement) {
			this.columnElement = columnElement;
		}

		@Override
		public Image getImage(final Object element) {
			return null;
		}

		@Override
		public String getText(final Object element) {
			if (element instanceof CargoEconsReportRow) {
				final CargoEconsReportRow row = (CargoEconsReportRow) element;
				return row.formatter.render(columnElement);
			}
			return null;
		}

		@Override
		public String getToolTipText(final Object element) {
			if (element instanceof CargoEconsReportRow) {
				final CargoEconsReportRow row = (CargoEconsReportRow) element;
				final Supplier<String> tooltip = row.tooltip;
				if (tooltip != null) {
					return tooltip.get();
				}
			}
			return null;
		}

		@Override
		public Color getForeground(final Object element) {
			if (element instanceof CargoEconsReportRow) {
				final CargoEconsReportRow row = ((CargoEconsReportRow) element);
				if (row.colourProvider != null) {
					return row.colourProvider.getForeground(columnElement);
				}
			}
			return null;
		}
		
		@Override
		public void update(final ViewerCell cell) {
			cell.setText(getText(cell.getElement()));
			Object element = cell.getElement();
			if (element instanceof CargoEconsReportRow) {
				final CargoEconsReportRow row = (CargoEconsReportRow) element;
				
				if (columnElement instanceof DeltaPair || columnElement instanceof List<?>) {
					String formattedValue = getText(element);
					
					if (row.isCost && formattedValue.contains("-")) {
						final ImageDescriptor imageDescriptor = Activator.getPlugin().getImageDescriptor("icons/green_arrow_down.png");
						Image cellImage = imageDescriptor.createImage();
						cell.setImage(cellImage);
					} else if (row.isCost && !formattedValue.contains("-")) {
						final ImageDescriptor imageDescriptor = Activator.getPlugin().getImageDescriptor("icons/red_arrow_up.png");
						Image cellImage = imageDescriptor.createImage();
						cell.setImage(cellImage);
					} else if (!row.isCost && !formattedValue.contains("-")) {
						final ImageDescriptor imageDescriptor = Activator.getPlugin().getImageDescriptor("icons/green_arrow_up.png");
						Image cellImage = imageDescriptor.createImage();
						cell.setImage(cellImage);
					} else if (!row.isCost && formattedValue.contains("-")) {
						final ImageDescriptor imageDescriptor = Activator.getPlugin().getImageDescriptor("icons/red_arrow_down.png");
						Image cellImage = imageDescriptor.createImage();
						cell.setImage(cellImage);
					}
				}
			}
		}
	}

	public void toggleShowDiffOnly() {
		onlyDiffMode = !onlyDiffMode;
	}
	public void toggleCompare() {
		final ISelectedDataProvider currentSelectedDataProvider = selectedScenariosService.getCurrentSelectedDataProvider();
		ScenarioResult scenario = selectedScenariosService.getPinnedScenario();
		
		if (scenario != null) {
			compareMode = true;
		} else  {
			compareMode = false; 
		}
	}
	/**
	 * This method processes an {@link ISelection} object and return a list of objects that this report knows how to process. Currently we try to find associated {@link CargoAllocation} objects.
	 * 
	 * @param selection
	 * @return
	 */
	private Collection<Object> processSelection(final IWorkbenchPart part, final ISelection selection) {
		final Collection<Object> validObjects = new LinkedHashSet<Object>();

		if (selection instanceof IStructuredSelection) {

			final Iterator<?> itr = ((IStructuredSelection) selection).iterator();
			while (itr.hasNext()) {
				Object obj = itr.next();
				if (obj instanceof Event) {
					obj = ScheduleModelUtils.getSegmentStart((Event) obj);
				}

				if (obj instanceof VesselEventVisit) {
					validObjects.add(obj);
				} else if (obj instanceof CargoAllocation) {
					validObjects.add(obj);
				} else if (obj instanceof SlotAllocation) {
					final SlotAllocation slotAllocation = (SlotAllocation) obj;
					if (slotAllocation.getCargoAllocation() != null) {
						validObjects.add(slotAllocation.getCargoAllocation());
					}
					if (slotAllocation.getMarketAllocation() != null) {
						validObjects.add(slotAllocation.getMarketAllocation());
					}
				} else if (obj instanceof SlotVisit) {
					validObjects.add((((SlotVisit) obj).getSlotAllocation().getCargoAllocation()));
				} else if (obj instanceof Cargo || obj instanceof Slot || obj instanceof VesselEvent) {
					Cargo cargo = null;
					Slot slot = null;
					VesselEvent event = null;
					if (obj instanceof VesselEvent) {
						event = (VesselEvent) obj;
					} else if (obj instanceof Cargo) {
						cargo = (Cargo) obj;
					} else {
						// Must be a slot
						assert obj instanceof Slot;
						if (obj instanceof LoadSlot) {
							cargo = ((LoadSlot) obj).getCargo();
							slot = (LoadSlot) obj;
						} else if (obj instanceof DischargeSlot) {
							cargo = ((DischargeSlot) obj).getCargo();
							slot = (DischargeSlot) obj;
						}
					}

					if (cargo != null) {

						// TODO: Look up ScheduleModel somehow....
						final Cargo pCargo = cargo;
						getValidObject(part, (lngScenarioModel) -> {
							final ScheduleModel scheduleModel = lngScenarioModel.getScheduleModel();
							if (scheduleModel != null) {
								final Schedule schedule = scheduleModel.getSchedule();
								if (schedule != null) {
									for (final CargoAllocation cargoAllocation : schedule.getCargoAllocations()) {
										if (ScheduleModelUtils.matchingSlots(pCargo, cargoAllocation)) {
											validObjects.add(cargoAllocation);
											break;
										}
									}
								}
							}
						});
					} else if (slot != null) {
						final Slot pSlot = slot;

						getValidObject(part, (lngScenarioModel) -> {
							final ScheduleModel scheduleModel = lngScenarioModel.getScheduleModel();
							if (scheduleModel != null) {
								final Schedule schedule = scheduleModel.getSchedule();
								if (schedule != null) {
									for (final MarketAllocation marketAllocation : schedule.getMarketAllocations()) {
										if (pSlot == marketAllocation.getSlot()) {
											validObjects.add(marketAllocation);
											break;
										}
									}
								}
							}
						});
					} else if (event != null) {
						final VesselEvent pEvent = event;

						getValidObject(part, (lngScenarioModel) -> {
							final ScheduleModel scheduleModel = lngScenarioModel.getScheduleModel();
							if (scheduleModel != null) {
								final Schedule schedule = scheduleModel.getSchedule();
								if (schedule != null) {
									LOOP: for (final Sequence sequence : schedule.getSequences()) {
										for (final Event evt : sequence.getEvents()) {
											if (evt instanceof VesselEventVisit) {
												final VesselEventVisit vesselEventVisit = (VesselEventVisit) evt;
												if (vesselEventVisit.getVesselEvent() == pEvent) {
													validObjects.add(vesselEventVisit);
													break LOOP;
												}
											}
										}
									}
								}
							}
						});
					}
				}
			}
			// Remove invalid items
			validObjects.remove(null);
		}
		return validObjects;

	}

	public GridTableViewer getViewer() {
		return viewer;
	}
	
	public abstract static class DeltaPair {
		public Object first() {
			return null;
		}
		
		public Object second() {
			return null;
		}
		
		public String getName() {
			return null;
		}
	}
	
	public static class CargoAllocationPair extends DeltaPair {
		private CargoAllocation first;
		private CargoAllocation second;
		
		CargoAllocationPair(CargoAllocation first, CargoAllocation second) {
			this.first = first;
			this.second = second;
		}
		
		@Override
		public CargoAllocation first() {
			return first;
		}
		
		@Override
		public CargoAllocation second() {
			return second;
		}

		static public List<CargoAllocationPair> generateCargoPair(List<CargoAllocation> cargoAllocations) {
			Collections.sort(cargoAllocations, (a, b) -> a.getName().compareTo(b.getName()));
			List<CargoAllocationPair> pairs = new ArrayList<>();
			
			// Edge case, only one lonely element in the list
			if (cargoAllocations.size() == 1) {
				CargoAllocation a = cargoAllocations.get(0);
				//pairs.add(new CargoAllocationPair(a, null));
			}
			
			for(int i = 0; i < cargoAllocations.size() - 1; i++) {
				CargoAllocation a = cargoAllocations.get(i);
				CargoAllocation b = cargoAllocations.get(i + 1);
				
				if (a.getName().equals(b.getName())) {
					pairs.add(new CargoAllocationPair(a, b));
					i++;
				} else {
					//pairs.add(new CargoAllocationPair(a, null));
				}
			}

			// Process possible final lonely element
			if (cargoAllocations.size() > 1) {
				CargoAllocation a = cargoAllocations.get(cargoAllocations.size() - 2);
				CargoAllocation b = cargoAllocations.get(cargoAllocations.size() - 1);
				if (!a.getName().equals(b.getName())) {
					//pairs.add(new CargoAllocationPair(b, null));
				}
			}
			return pairs;
		}
		
		@Override
		public String getName() {
			return first.getName();
		}
	}
	
	public static class VesselEventVisitPair extends DeltaPair{
		private VesselEventVisit first;
		private VesselEventVisit second;
		
		VesselEventVisitPair(VesselEventVisit first, VesselEventVisit second) {
			this.first = first;
			this.second = second;
		}
		
		@Override
		public VesselEventVisit first() {
			return first;
		}
		
		@Override
		public VesselEventVisit second() {
			return second;
		}

		static public List<VesselEventVisitPair> generateVesselEventPair(List<VesselEventVisit> vesselEventVisits) {
			Collections.sort(vesselEventVisits, (a, b) -> a.name().compareTo(b.name()));
			List<VesselEventVisitPair> pairs = new ArrayList<>();
				// Edge case, only one lonely element in the list
			
			if (vesselEventVisits.size() == 1) {
				VesselEventVisit a = vesselEventVisits.get(0);
				//pairs.add(new VesselEventVisitPair(a, null));
			}
			
			for(int i = 0; i < vesselEventVisits.size() - 1; i++) {
				VesselEventVisit a = vesselEventVisits.get(i);
				VesselEventVisit b = vesselEventVisits.get(i + 1);
				
				if (a.name().equals(b.name())) {
					pairs.add(new VesselEventVisitPair(a, b));
					i++;
				} else {
					//pairs.add(new VesselEventVisitPair(a, null));
				}
			}
			
			if (vesselEventVisits.size() > 1) {
				VesselEventVisit a = vesselEventVisits.get(vesselEventVisits.size() - 2);
				VesselEventVisit b = vesselEventVisits.get(vesselEventVisits.size() - 1);
				if (!a.name().equals(b.name())) {
					//pairs.add(new VesselEventVisitPair(b, null));
				}
			} 
			return pairs;
		}
		
		@Override
		public String getName() {
			return first.name();
		}
	}
		
	public <T> int comparator(T a, T  b) {
		String aName = "";
		String bName = "";
		
		if (a != null && b != null) {

			if (a instanceof CargoAllocation) {
				aName = ((CargoAllocation) a).getName();
			} else if (a instanceof VesselEventVisit) {
				aName = ((VesselEventVisit) a).name();
			} else if (a instanceof DeltaPair) {
				aName = ((DeltaPair) a).getName();
			}
			
			if (b instanceof CargoAllocation) {
				bName = ((CargoAllocation) b).getName();
			} else if (b instanceof VesselEventVisit) {
				bName = ((VesselEventVisit) b).name();
			} else if (b instanceof DeltaPair) {
				bName = ((DeltaPair) b).getName();
			}
			
			if (aName != null && bName != null) {
				int res = aName.compareTo(bName);
				
				if (res == 0 && !(a instanceof DeltaPair || b instanceof DeltaPair)) {
					@Nullable
					final ISelectedDataProvider currentSelectedDataProvider = selectedScenariosService.getCurrentSelectedDataProvider();
					if (currentSelectedDataProvider != null && currentSelectedDataProvider.isPinnedObject((EObject) a)) {
						res++;
					}

					if (currentSelectedDataProvider != null && currentSelectedDataProvider.isPinnedObject((EObject) b)) {
						res--;
					}
				}
				return res;
			}
		}
		
		return Integer.MAX_VALUE;
	}
	
	/**
	 * Adds a selection listener for the given partID. Listens to everything if null
	 */
	@BindSelectionListener
	public void listenToSelectionsFrom(@Optional @Nullable final String partId) {

		final org.eclipse.e4.ui.workbench.modeling.ISelectionListener selectionListener = new org.eclipse.e4.ui.workbench.modeling.ISelectionListener() {

			@Override
			public void selectionChanged(final MPart part, final Object selectedObjects) {
				final IWorkbenchPart e3part = SelectionHelper.getE3Part(part);
				{
					// TODO: Ignore navigator
					if (e3part == CargoEconsReportComponent.this) {
						return;
					}
					if (e3part instanceof PropertySheet) {
						return;
					}
				}

				final ISelection selection = SelectionHelper.adaptSelection(selectedObjects);
				
				// Find valid, selected objects
				final Collection<Object> validObjects = CargoEconsReportComponent.this.processSelection(e3part, selection);
				setSelectedObject(validObjects);

				rebuild();
			}
		};
		if (partId != null) {
			selectionService.addPostSelectionListener(partId, selectionListener);
		} else {
			selectionService.addPostSelectionListener(selectionListener);
		}
		selectionListeners.add(new Pair<>(partId, selectionListener));
	}

	/**
	 * Adds a selection listener for the given partID. Listens to everything if null
	 */
	@SetEconsMarginMode
	public void setMarginMode(final MarginBy mode) {
		options.marginBy = mode;
		ViewerHelper.refresh(getViewer(), false);
	}

	private void getValidObject(final IWorkbenchPart part, final Consumer<LNGScenarioModel> objectFinder) {
		final ModelReference ref = getPartInstanceReference(part);
		if (ref != null) {
			try {
				final EObject instance = ref.getInstance();
				if (instance instanceof LNGScenarioModel) {
					final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) instance;
					objectFinder.accept(lngScenarioModel);
				}
			} finally {
				ref.close();
			}
		}
	}

	private @Nullable ModelReference getPartInstanceReference(final IWorkbenchPart part) {
		if (part instanceof IEditorPart) {
			final IEditorPart editorPart = (IEditorPart) part;
			final IEditorInput editorInput = editorPart.getEditorInput();
			if (editorInput instanceof IScenarioServiceEditorInput) {
				final IScenarioServiceEditorInput scenarioServiceEditorInput = (IScenarioServiceEditorInput) editorInput;
				final ScenarioInstance scenarioInstance = scenarioServiceEditorInput.getScenarioInstance();
				if (scenarioInstance != null) {
					@NonNull
					final ModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenarioInstance);
					return modelRecord.aquireReference("CargoEconsReport");
				}
			}
		}
		return null;
	}

	@Override
	public <T> T getAdapter(final Class<T> adapter) {

		if (GridTableViewer.class.isAssignableFrom(adapter)) {
			return (T) viewer;
		}
		if (Grid.class.isAssignableFrom(adapter)) {
			return (T) viewer.getGrid();
		}

		if (IReportContents.class.isAssignableFrom(adapter)) {

			final CopyGridToHtmlStringUtil util = new CopyGridToHtmlStringUtil(viewer.getGrid(), false, true);
			final String contents = util.convert();
			return (T) new IReportContents() {

				@Override
				public String getStringContents() {
					return contents;
				}
			};

		}
		return null;
	}

	public void rebuild() {
		Collection<Object> validObjects = new ArrayList<Object>(getSelectedObject());
		toggleCompare();
		// Dispose old data columns - clone list to try to avoid concurrent modification exceptions
		final List<GridViewerColumn> oldColumns = new ArrayList<GridViewerColumn>(dataColumns);
		dataColumns.clear();
		for (final GridViewerColumn gvc : oldColumns) {
			gvc.getColumn().dispose();
		}
		
		if (validObjects == null || validObjects.size() == 0) {
			return;
		}
		
		if (compareMode == true) {

			List<CargoAllocation> cargoAllocations = new ArrayList<>();
			for (final Object obj: validObjects) {
				if (obj instanceof CargoAllocation) {
					cargoAllocations.add((CargoAllocation) obj);
				}
			}
			
			List<VesselEventVisit> vesselEventVisits = new ArrayList<>();
			for (final Object obj: validObjects) {
				if (obj instanceof VesselEventVisit) {
					vesselEventVisits.add((VesselEventVisit) obj);
				}
			}
			
			// Create the row object
			final List<CargoEconsReportRow> rows = new LinkedList<CargoEconsReportRow>();
			ServiceHelper.withAllServices(IEconsRowFactory.class, null, factory -> {
				rows.addAll(factory.createRows(options, validObjects));
				return true;
			});
			Collections.sort(rows, (a, b) -> a.order - b.order);
			
			viewer.setInput(rows);

			if (onlyDiffMode == true) {
				validObjects.clear();
			}
			
			List<CargoAllocationPair> cargoAllocationPairs = CargoAllocationPair.generateCargoPair(cargoAllocations);
			List<VesselEventVisitPair> vesselEventVisitsPairs = VesselEventVisitPair.generateVesselEventPair(vesselEventVisits);
			validObjects.addAll(cargoAllocationPairs);
			validObjects.addAll(vesselEventVisitsPairs);
			
			List<Object> sortedObjects = new ArrayList<>(validObjects.size());
			
			// Create a new list to sort the elements and replace the content of the 
			// LinkedHashMap with it (insert-order)
			sortedObjects.addAll(validObjects);
			Collections.sort(sortedObjects, (a, b) -> comparator(a, b));
			
			validObjects.clear();
			validObjects.addAll(sortedObjects);
			
			List<DeltaPair> aggregateList = new ArrayList(cargoAllocationPairs.size() + vesselEventVisitsPairs.size());
			
			// The finals aggregated elements
			aggregateList.addAll(cargoAllocationPairs);
			aggregateList.addAll(vesselEventVisitsPairs);
			
			long numberOfdiffColumn = cargoAllocationPairs.stream().filter(a -> a.second() != null).count();
			numberOfdiffColumn += vesselEventVisitsPairs.stream().filter(a -> a.second() != null).count();
			
			// Only create aggregate if more than two element
			// The cargo/vesselEvent and its partial pair
			if (numberOfdiffColumn > 1) {
				validObjects.add(aggregateList);
			}
		}

		ColumnHeaderRenderer columnHeaderCenteredRenderer = new ColumnHeaderRenderer();
		columnHeaderCenteredRenderer.setCenter(true);
		
		// Feed the element to be displayed
		for (final Object selectedObject : validObjects) {
			// Currently only CargoAllocations
			if (selectedObject instanceof CargoAllocation) {
				final CargoAllocation cargoAllocation = (CargoAllocation) selectedObject;
				final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.NONE);
				GridViewerHelper.configureLookAndFeel(gvc);
				// Mark column for disposal on selection change
				dataColumns.add(gvc);
				
				gvc.getColumn().setText(cargoAllocation.getName());
				gvc.setLabelProvider(new FieldTypeMapperLabelProvider(selectedObject));
				gvc.getColumn().setWidth(100);
				@Nullable
				final ISelectedDataProvider currentSelectedDataProvider = selectedScenariosService.getCurrentSelectedDataProvider();
				if (currentSelectedDataProvider != null && currentSelectedDataProvider.isPinnedObject(cargoAllocation)) {
					gvc.getColumn().setImage(pinImage);
					gvc.getColumn().setText("");
				}
			// Diff of cargo
			} else if (selectedObject instanceof DeltaPair) {
				final DeltaPair pair = (DeltaPair) selectedObject;
				if (pair.second() != null || onlyDiffMode == true) {
					final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.NONE);
					GridViewerHelper.configureLookAndFeel(gvc);
					// Mark column for disposal on selection change
					dataColumns.add(gvc);

					gvc.getColumn().setHeaderRenderer(columnHeaderCenteredRenderer);
					gvc.getColumn().setText("𝚫");
					gvc.setLabelProvider(new FieldTypeMapperLabelProvider(selectedObject));
					gvc.getColumn().setWidth(100);
				}
			// The aggregate element
			} else if (selectedObject instanceof List<?>) {
				final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.NONE);
				GridViewerHelper.configureLookAndFeel(gvc);
				// Mark column for disposal on selection change
				dataColumns.add(gvc);
				
				gvc.getColumn().setHeaderRenderer(columnHeaderCenteredRenderer);
				gvc.getColumn().setText("Σ");
				gvc.setLabelProvider(new FieldTypeMapperLabelProvider(selectedObject));
				gvc.getColumn().setWidth(100);
			} else if (selectedObject instanceof VesselEventVisit) {
				final VesselEventVisit vesselEventVisit = (VesselEventVisit) selectedObject;

				final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.NONE);
				GridViewerHelper.configureLookAndFeel(gvc);
				// Mark column for disposal on selection change
				dataColumns.add(gvc);
				
				gvc.getColumn().setText(vesselEventVisit.name());
				gvc.setLabelProvider(new FieldTypeMapperLabelProvider(selectedObject));
				gvc.getColumn().setWidth(100);
				@Nullable
				final ISelectedDataProvider currentSelectedDataProvider = selectedScenariosService.getCurrentSelectedDataProvider();
				if (currentSelectedDataProvider != null && currentSelectedDataProvider.isPinnedObject(vesselEventVisit)) {
					gvc.getColumn().setImage(pinImage);
				}
			} else if (selectedObject instanceof MarketAllocation) {
				final MarketAllocation cargoAllocation = (MarketAllocation) selectedObject;

				final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.NONE);
				GridViewerHelper.configureLookAndFeel(gvc);

				// Mark column for disposal on selection change
				dataColumns.add(gvc);
				gvc.getColumn().setText(cargoAllocation.getSlot().getName());
				gvc.setLabelProvider(new FieldTypeMapperLabelProvider(cargoAllocation));

				gvc.getColumn().setWidth(100);

				@Nullable
				final ISelectedDataProvider currentSelectedDataProvider = selectedScenariosService.getCurrentSelectedDataProvider();
				if (currentSelectedDataProvider != null && currentSelectedDataProvider.isPinnedObject(cargoAllocation)) {
					gvc.getColumn().setImage(pinImage);
				}
			}
		}
		// Trigger view refresh
		ViewerHelper.refresh(viewer, true);
	}
}
