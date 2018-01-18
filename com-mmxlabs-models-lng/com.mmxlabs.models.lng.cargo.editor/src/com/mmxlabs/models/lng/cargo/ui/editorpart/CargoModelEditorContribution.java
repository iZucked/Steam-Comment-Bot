/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.awt.Desktop.Action;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.google.common.collect.Lists;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.EndHeelOptions;
import com.mmxlabs.models.lng.cargo.Inventory;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.StartHeelOptions;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.ui.editorpart.alternatives.IAlternativeEditorProvider;
import com.mmxlabs.models.lng.cargo.ui.editorpart.alternatives.TradesTableEditorExtension;
import com.mmxlabs.models.lng.cargo.ui.editorpart.alternatives.TradesTableEditorProviderModule;
import com.mmxlabs.models.lng.commercial.SlotContractParams;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.ui.editorpart.BaseJointModelEditorContribution;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialogUtil;
import com.mmxlabs.models.ui.tabular.Activator;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.rcp.common.actions.RunnableAction;

/**
 */
public class CargoModelEditorContribution extends BaseJointModelEditorContribution<CargoModel> {
	private int currentEditorIndex = 0;
	private int tradesViewerPageNumber = -1;
	private ScenarioTableViewerPane tradesViewer;
	private VesselViewerPane_Editor vesselViewerPane;
	// private VesselClassViewerPane vesselClassViewerPane;
	private VesselEventViewerPane eventViewerPane;
	private int eventPage;
	private MarketOverrideViewerPane_Editor marketOverrideViewerPane;
	private int overridePage;

	private InventoryFeedPane inventoryFeedPane;
	private InventoryOfftakePane inventoryOfftakePane;
	private InventoryCapacityPane inventoryCapacityPane;
	int inventoryPage;

	private List<IAlternativeEditorProvider> extensions = new LinkedList<>();

	@Override
	public void addPages(final Composite parent) {
		initialiseExtensions();
		if (!extensions.isEmpty()) {
			{
				IAlternativeEditorProvider provider = extensions.get(0);
				this.tradesViewer = provider.init(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars(), parent, modelObject);
			}
			tradesViewerPageNumber = editorPart.addPage(tradesViewer.getControl());
			editorPart.setPageText(tradesViewerPageNumber, "Trades");
			currentEditorIndex = 0;
			if (extensions.size() > 1) {
				toggleAction = new RunnableAction("Toggle editor mode", () -> {
					editorPart.setControl(tradesViewerPageNumber, null);

					tradesViewer.getControl().dispose();
					++currentEditorIndex;
					if (currentEditorIndex >= extensions.size()) {
						currentEditorIndex = 0;
					}
					IAlternativeEditorProvider provider = extensions.get(currentEditorIndex);
					this.tradesViewer = provider.init(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars(), parent, modelObject);

					editorPart.setControl(tradesViewerPageNumber, tradesViewer.getControl());

					tradesViewer.getToolBarManager().add(toggleAction);
					tradesViewer.getToolBarManager().update(true);

				});
				toggleAction.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.models.lng.cargo.editor", "/icons/editormode.gif"));
				tradesViewer.getToolBarManager().add(toggleAction);
				tradesViewer.getToolBarManager().update(true);
			}
		}
		{

			final SashForm sash = new SashForm(parent, SWT.VERTICAL);

			vesselViewerPane = new VesselViewerPane_Editor(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars());
			vesselViewerPane.createControl(sash);
			vesselViewerPane.init(Lists.newArrayList(CargoPackage.eINSTANCE.getCargoModel_VesselAvailabilities()), editorPart.getAdapterFactory(), editorPart.getModelReference());

			eventViewerPane = new VesselEventViewerPane(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars());
			eventViewerPane.createControl(sash);
			eventViewerPane.init(Lists.newArrayList(CargoPackage.eINSTANCE.getCargoModel_VesselEvents()), editorPart.getAdapterFactory(), editorPart.getModelReference());

			vesselViewerPane.getViewer().setInput(modelObject);
			eventViewerPane.getViewer().setInput(modelObject);

			eventPage = editorPart.addPage(sash);
			editorPart.setPageText(eventPage, "Fleet");

		}
		if (!modelObject.getCharterInMarketOverrides().isEmpty()) {

			marketOverrideViewerPane = new MarketOverrideViewerPane_Editor(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars());
			marketOverrideViewerPane.createControl(parent);
			marketOverrideViewerPane.init(Lists.newArrayList(CargoPackage.eINSTANCE.getCargoModel_CharterInMarketOverrides()), editorPart.getAdapterFactory(), editorPart.getModelReference());

			marketOverrideViewerPane.getViewer().setInput(modelObject);

			overridePage = editorPart.addPage(marketOverrideViewerPane.getControl());
			editorPart.setPageText(overridePage, "Overrides");
		}

		if (LicenseFeatures.isPermitted("features:inventory-model")) {

			// TODO: Add/Remove facilities
			// TODO: CSV Import/Export
			// TODO: Validation hooks and validation model

			final Composite sash = new Composite(parent, SWT.NONE);

			sash.setLayout(new GridLayout(3, true));
			final Composite selector = new Composite(sash, SWT.NONE);
			selector.setLayoutData(GridDataFactory.fillDefaults().span(3, 1).create());

			Button btn_new = new Button(selector, SWT.PUSH);
			btn_new.setText("New");

			final Label label = new Label(selector, SWT.NONE);
			label.setText("Facility: ");
			selector.setLayout(new GridLayout(4, false));
			final ComboViewer comboViewer = new ComboViewer(selector);
			comboViewer.getControl().setLayoutData(GridDataFactory.fillDefaults().hint(70, SWT.DEFAULT).create());

			{
				Button btn = new Button(selector, SWT.PUSH);
				btn.setText("Edit");
				btn.addSelectionListener(new SelectionListener() {

					@Override
					public void widgetSelected(SelectionEvent e) {
						// TODO Auto-generated method stub
						IStructuredSelection selection = comboViewer.getStructuredSelection();
						DetailCompositeDialogUtil.editSelection(editorPart, selection);
						comboViewer.refresh();
					}

					@Override
					public void widgetDefaultSelected(SelectionEvent e) {

					}
				});
			}

			inventoryFeedPane = new InventoryFeedPane(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars());
			inventoryFeedPane.createControl(sash);
			inventoryFeedPane.init(Lists.newArrayList(CargoPackage.eINSTANCE.getInventory_Feeds()), editorPart.getAdapterFactory(), editorPart.getModelReference());
			inventoryFeedPane.getControl().setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());

			inventoryOfftakePane = new InventoryOfftakePane(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars());
			inventoryOfftakePane.createControl(sash);
			inventoryOfftakePane.init(Lists.newArrayList(CargoPackage.eINSTANCE.getInventory_Offtakes()), editorPart.getAdapterFactory(), editorPart.getModelReference());
			inventoryOfftakePane.getControl().setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());

			inventoryCapacityPane = new InventoryCapacityPane(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars());
			inventoryCapacityPane.createControl(sash);
			inventoryCapacityPane.init(Lists.newArrayList(CargoPackage.eINSTANCE.getInventory_Capacities()), editorPart.getAdapterFactory(), editorPart.getModelReference());
			inventoryCapacityPane.getControl().setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());

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
					final Inventory inventory = (Inventory) ((IStructuredSelection) comboViewer.getSelection()).getFirstElement();
					inventoryFeedPane.getViewer().setInput(inventory);
					inventoryOfftakePane.getViewer().setInput(inventory);
					inventoryCapacityPane.getViewer().setInput(inventory);

				}
			});

			btn_new.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetSelected(SelectionEvent e) {

					final Inventory inventory = CargoFactory.eINSTANCE.createInventory();
					editorPart.getModelReference().executeWithTryLock(true, 200, () -> {

						final CompoundCommand cmd = new CompoundCommand("New storage");
						cmd.append(AddCommand.create(editorPart.getEditingDomain(), modelObject, CargoPackage.eINSTANCE.getCargoModel_InventoryModels(), inventory));

						CommandStack commandStack = editorPart.getModelReference().getCommandStack();
						commandStack.execute(cmd);
						DetailCompositeDialogUtil.editSingleObjectWithUndoOnCancel(editorPart, inventory, commandStack.getMostRecentCommand());
					});
					comboViewer.setInput(modelObject.getInventoryModels());
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {

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

	private void initialiseExtensions() {
		class Temp {
			@Inject(optional = true)
			public Iterable<TradesTableEditorExtension> extensions;
		}
		final Injector injector = Guice.createInjector(new TradesTableEditorProviderModule());
		Temp t = new Temp();
		injector.injectMembers(t);
		for (TradesTableEditorExtension e : t.extensions) {
			extensions.add(e.getInstance());
		}
		if (extensions.size() > 1) {
			Collections.sort(extensions, (a, b) -> Integer.compare(a.getPriority(), b.getPriority()));
		}

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
	private RunnableAction toggleAction;

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
}
