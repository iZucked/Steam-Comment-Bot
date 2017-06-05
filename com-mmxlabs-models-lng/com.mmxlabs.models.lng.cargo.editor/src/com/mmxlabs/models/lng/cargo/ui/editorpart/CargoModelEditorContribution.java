/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.time.LocalDate;
import java.util.Collections;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;

import com.google.common.collect.Lists;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.EndHeelOptions;
import com.mmxlabs.models.lng.cargo.Inventory;
import com.mmxlabs.models.lng.cargo.InventoryCapacityRow;
import com.mmxlabs.models.lng.cargo.InventoryEventRow;
import com.mmxlabs.models.lng.cargo.InventoryFrequency;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.StartHeelOptions;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.commercial.SlotContractParams;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.models.ui.editorpart.BaseJointModelEditorContribution;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

/**
 */
public class CargoModelEditorContribution extends BaseJointModelEditorContribution<CargoModel> {
	private int tradesViewerPageNumber = -1;
	private TradesWiringViewer tradesViewer;
	private VesselViewerPane_Editor vesselViewerPane;
	// private VesselClassViewerPane vesselClassViewerPane;
	private VesselEventViewerPane eventViewerPane;
	private int eventPage;

	private InventoryFeedPane inventoryFeedPane;
	private InventoryOfftakePane inventoryOfftakePane;
	private InventoryCapacityPane inventoryCapacityPane;
	int inventoryPage;

	@Override
	public void addPages(final Composite parent) {

		this.tradesViewer = new TradesWiringViewer(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars());
		tradesViewer.createControl(parent);
		tradesViewer.init(Collections.<EReference> emptyList(), editorPart.getAdapterFactory(), editorPart.getEditingDomain().getCommandStack());
		tradesViewer.getViewer().setInput(modelObject);
		tradesViewerPageNumber = editorPart.addPage(tradesViewer.getControl());
		editorPart.setPageText(tradesViewerPageNumber, "Trades");
		{

			final SashForm sash = new SashForm(parent, SWT.VERTICAL);

			vesselViewerPane = new VesselViewerPane_Editor(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars());
			vesselViewerPane.createControl(sash);
			vesselViewerPane.init(Lists.newArrayList(CargoPackage.eINSTANCE.getCargoModel_VesselAvailabilities()), editorPart.getAdapterFactory(), editorPart.getEditingDomain().getCommandStack());

			eventViewerPane = new VesselEventViewerPane(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars());
			eventViewerPane.createControl(sash);
			eventViewerPane.init(Lists.newArrayList(CargoPackage.eINSTANCE.getCargoModel_VesselEvents()), editorPart.getAdapterFactory(), editorPart.getEditingDomain().getCommandStack());

			vesselViewerPane.getViewer().setInput(modelObject);
			eventViewerPane.getViewer().setInput(modelObject);

			eventPage = editorPart.addPage(sash);
			editorPart.setPageText(eventPage, "Fleet");

		}

		if (LicenseFeatures.isPermitted("features:inventory-model")) {
			
			// TODO: Add/Remove facilities
			// TODO: CSV Import/Export
			// TODO: Validation hooks and validation model
			
			final Composite sash = new Composite(parent, SWT.NONE);

			sash.setLayout(new GridLayout(3, true));
			final Composite selector = new Composite(sash, SWT.NONE);
			final Label label = new Label(selector, SWT.NONE);
			label.setText("Facility: ");
			selector.setLayout(new GridLayout(2, false));
			final ComboViewer comboViewer = new ComboViewer(selector);
			selector.setLayoutData(GridDataFactory.fillDefaults().span(3, 1).create());

			inventoryFeedPane = new InventoryFeedPane(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars());
			inventoryFeedPane.createControl(sash);
			inventoryFeedPane.init(Lists.newArrayList(CargoPackage.eINSTANCE.getInventory_Feeds()), editorPart.getAdapterFactory(), editorPart.getEditingDomain().getCommandStack());
			inventoryFeedPane.getControl().setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());

			inventoryOfftakePane = new InventoryOfftakePane(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars());
			inventoryOfftakePane.createControl(sash);
			inventoryOfftakePane.init(Lists.newArrayList(CargoPackage.eINSTANCE.getInventory_Offtakes()), editorPart.getAdapterFactory(), editorPart.getEditingDomain().getCommandStack());
			inventoryOfftakePane.getControl().setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());

			inventoryCapacityPane = new InventoryCapacityPane(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars());
			inventoryCapacityPane.createControl(sash);
			inventoryCapacityPane.init(Lists.newArrayList(CargoPackage.eINSTANCE.getInventory_Capacities()), editorPart.getAdapterFactory(), editorPart.getEditingDomain().getCommandStack());
			inventoryCapacityPane.getControl().setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());

			if (false && modelObject.getInventoryModels().isEmpty()) {
				// Demo data - note! outside of commands!
				final Inventory bin = CargoFactory.eINSTANCE.createInventory();
				bin.setName("Bintulu");

				makeCapacity(bin, LocalDate.of(2017, 1, 1), 6_000, 460_000);
				makeCapacity(bin, LocalDate.of(2017, 6, 1), 6_000, 540_000);
				makeCapacity(bin, LocalDate.of(2017, 8, 1), 6_000, 480_000);

				makeFeed(bin, LocalDate.of(2017, 1, 1), LocalDate.of(2017, 1, 1), "Upstream", InventoryFrequency.LEVEL, 400_000);
				makeFeed(bin, LocalDate.of(2017, 1, 1), LocalDate.of(2017, 5, 31), "Upstream", InventoryFrequency.DAILY, 20_000);
				makeFeed(bin, LocalDate.of(2017, 5, 1), LocalDate.of(2017, 5, 31), "Upstream", InventoryFrequency.DAILY, 15_000);

				makeOfftake(bin, LocalDate.of(2017, 1, 15), LocalDate.of(2017, 1, 15), InventoryFrequency.CARGO, "Tepco", 150_000);

				final Inventory sing = CargoFactory.eINSTANCE.createInventory();
				sing.setName("Singapore");
				makeCapacity(sing, LocalDate.of(2017, 1, 1), 1_000, 300_000);

				for (final LoadSlot slot : modelObject.getLoadSlots()) {
					if (slot.getPort().getName().equals("Bintulu")) {
						int volums = slot.getSlotOrContractMaxQuantity();
						if (slot.getVolumeLimitsUnit() == VolumeUnits.MMBTU) {
							volums = (int) ((double) volums / slot.getSlotOrDelegatedCV());
						}
						makeOfftake(bin, slot.getWindowStart(), slot.getWindowStart(), InventoryFrequency.CARGO, slot.getName(), volums);
					}
				}

				final Inventory dragon = CargoFactory.eINSTANCE.createInventory();
				dragon.setName("Dragon");

				modelObject.getInventoryModels().add(bin);
				modelObject.getInventoryModels().add(sing);
				modelObject.getInventoryModels().add(dragon);
			}
			inventoryPage = editorPart.addPage(sash);
			editorPart.setPageText(inventoryPage, "Inventory");

			comboViewer.setContentProvider(new ArrayContentProvider());
			comboViewer.setLabelProvider(new LabelProvider() {
				@Override
				public String getText(final Object element) {
					return ((Inventory) element).getName();
				}
			});
			comboViewer.addSelectionChangedListener(new ISelectionChangedListener() {

				@Override
				public void selectionChanged(final SelectionChangedEvent event) {
					// TODO Auto-generated method stub
					final Inventory inventory = (Inventory) ((IStructuredSelection) comboViewer.getSelection()).getFirstElement();
					inventoryFeedPane.getViewer().setInput(inventory);
					inventoryOfftakePane.getViewer().setInput(inventory);
					inventoryCapacityPane.getViewer().setInput(inventory);

				}
			});

			comboViewer.setInput(modelObject.getInventoryModels());
			if (!modelObject.getInventoryModels().isEmpty()) {
				// Pick first model.
				final Inventory inventory = modelObject.getInventoryModels().get(0);
				inventoryFeedPane.getViewer().setInput(inventory);
				inventoryOfftakePane.getViewer().setInput(inventory);
				inventoryCapacityPane.getViewer().setInput(inventory);
				comboViewer.setSelection(new StructuredSelection(inventory));
			}
		}
		PlatformUI.getWorkbench().getHelpSystem().setHelp(tradesViewer.getControl(), "com.mmxlabs.lingo.doc.Editor_Trades");
		PlatformUI.getWorkbench().getHelpSystem().setHelp(vesselViewerPane.getControl(), "com.mmxlabs.lingo.doc.Editor_Fleet");
		PlatformUI.getWorkbench().getHelpSystem().setHelp(eventViewerPane.getControl(), "com.mmxlabs.lingo.doc.Editor_Fleet");

	}

	@Override
	public void setLocked(final boolean locked) {
		if (tradesViewer != null) {
			tradesViewer.setLocked(locked);
		}
		if (vesselViewerPane != null) {
			vesselViewerPane.setLocked(locked);
		}
		if (eventViewerPane != null) {
			eventViewerPane.setLocked(locked);
		}
		if (inventoryFeedPane != null) {
			inventoryFeedPane.setLocked(locked);
		}
		if (inventoryOfftakePane != null) {
			inventoryOfftakePane.setLocked(locked);
		}
		if (inventoryCapacityPane != null) {
			inventoryCapacityPane.setLocked(locked);
		}
	}

	private static final Class<?>[] handledClasses = { Vessel.class, VesselAvailability.class, VesselEvent.class, StartHeelOptions.class, EndHeelOptions.class, Cargo.class, LoadSlot.class,
			DischargeSlot.class, SlotContractParams.class, SlotVisit.class, EndEvent.class };

	@Override
	public boolean canHandle(final IStatus status) {

		if (status instanceof DetailConstraintStatusDecorator) {
			final DetailConstraintStatusDecorator dcsd = (DetailConstraintStatusDecorator) status;

			final EObject target = dcsd.getTarget();

			for (final Class<?> clazz : handledClasses) {
				if (clazz.isInstance(target)) {
					return true;
				}
			}
			for (final EObject o : dcsd.getObjects()) {
				for (final Class<?> clazz : handledClasses) {
					if (clazz.isInstance(o)) {
						return true;
					}
				}
			}

		}

		return false;
	}

	@Override
	public void handle(final IStatus status) {
		if (status instanceof DetailConstraintStatusDecorator) {
			final DetailConstraintStatusDecorator dcsd = (DetailConstraintStatusDecorator) status;

			EObject target = dcsd.getTarget();
			// Look in child items for potentially handles classes.
			{
				boolean foundTarget = false;
				for (final Class<?> clazz : handledClasses) {
					if (clazz.isInstance(target)) {
						foundTarget = true;
						break;
					}
				}
				if (!foundTarget) {
					for (final EObject o : dcsd.getObjects()) {
						for (final Class<?> clazz : handledClasses) {
							if (clazz.isInstance(o)) {
								target = o;
								foundTarget = true;
							}
						}
					}
				}
			}

			Cargo cargo = null;
			LoadSlot loadSlot = null;
			DischargeSlot dischargeSlot = null;
			if (target instanceof SlotContractParams) {
				target = target.eContainer();
			}
			if (target instanceof SlotVisit) {
				if (((SlotVisit) target).getSlotAllocation() != null) {
					target = ((SlotVisit) target).getSlotAllocation().getSlot();
				}
			}
			if (target instanceof VesselEventVisit) {
				target = ((VesselEventVisit) target).getVesselEvent();
			}
			if (target instanceof EndEvent) {
				final VesselAvailability availability = ((EndEvent) target).getSequence().getVesselAvailability();
				if (availability != null) {
					target = availability;
				}
			}

			if (target instanceof Cargo) {
				cargo = (Cargo) target;
			} else if (target instanceof LoadSlot) {
				loadSlot = (LoadSlot) target;
				cargo = loadSlot.getCargo();
			} else if (target instanceof DischargeSlot) {
				dischargeSlot = (DischargeSlot) target;
				cargo = dischargeSlot.getCargo();
			}
			if (cargo != null) {
				if (tradesViewer != null) {
					editorPart.setActivePage(tradesViewerPageNumber);
					tradesViewer.getScenarioViewer().setSelection(new StructuredSelection(cargo), true);
					return;
				}
			} else if (loadSlot != null) {
				if (tradesViewer != null) {
					editorPart.setActivePage(tradesViewerPageNumber);
					tradesViewer.getScenarioViewer().setSelection(new StructuredSelection(loadSlot), true);
					return;
				}
			} else if (dischargeSlot != null) {
				if (tradesViewer != null) {
					editorPart.setActivePage(tradesViewerPageNumber);
					tradesViewer.getScenarioViewer().setSelection(new StructuredSelection(dischargeSlot), true);
					return;
				}
			}

			editorPart.setActivePage(eventPage);

			// extract viewable target from a faulty HeelOptions object
			if (target instanceof StartHeelOptions) {
				final EObject container = target.eContainer();
				if (container instanceof VesselAvailability) {
					target = (VesselAvailability) container;
				} else if (container instanceof CharterOutEvent) {
					target = container;
				}
			} else if (target instanceof EndHeelOptions) {
				final EObject container = target.eContainer();
				if (container instanceof VesselAvailability) {
					target = (VesselAvailability) container;
				} else if (container instanceof CharterOutEvent) {
					target = container;
				}
			}

			if (target instanceof Vessel) {
				final Vessel vessel = (Vessel) target;
				vesselViewerPane.getScenarioViewer().setSelection(new StructuredSelection(vessel), true);
			} else if (target instanceof VesselAvailability) {
				final VesselAvailability vesselAvailability = (VesselAvailability) target;
				vesselViewerPane.getScenarioViewer().setSelection(new StructuredSelection(vesselAvailability), true);
			} else if (target instanceof VesselEvent) {
				final VesselEvent vesselEvent = (VesselEvent) target;
				eventViewerPane.getScenarioViewer().setSelection(new StructuredSelection(vesselEvent), true);
			}

		}
	}

	private void makeFeed(final Inventory inventory, final LocalDate start, final LocalDate end, final String counterParty, final InventoryFrequency freq, final int volume) {
		final InventoryEventRow row = CargoFactory.eINSTANCE.createInventoryEventRow();
		row.setStartDate(start);
		row.setEndDate(end);
		row.setCounterParty(counterParty);
		row.setPeriod(freq);
		row.setVolume(volume);

		inventory.getFeeds().add(row);
	}

	private void makeOfftake(final Inventory inventory, final LocalDate start, final LocalDate end, final InventoryFrequency freq, final String counterParty, final int volume) {
		final InventoryEventRow row = CargoFactory.eINSTANCE.createInventoryEventRow();
		row.setStartDate(start);
		row.setEndDate(end);
		row.setPeriod(freq);
		row.setCounterParty(counterParty);
		row.setVolume(volume);

		inventory.getOfftakes().add(row);
	}

	private void makeCapacity(final Inventory inventory, final LocalDate start, final int minVolume, final int maxVolume) {
		final InventoryCapacityRow row = CargoFactory.eINSTANCE.createInventoryCapacityRow();
		row.setDate(start);
		row.setMinVolume(minVolume);
		row.setMaxVolume(maxVolume);

		inventory.getCapacities().add(row);
	}
}
