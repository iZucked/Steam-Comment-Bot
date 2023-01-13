/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.ui.editorpart;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.ui.tabular.manipulators.VesselManipulatorWrapper;
import com.mmxlabs.models.lng.fleet.util.IVesselImportCommandProvider;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.ui.actions.AddModelAction;
import com.mmxlabs.models.lng.ui.actions.DuplicateAction;
import com.mmxlabs.models.lng.ui.actions.AddModelAction.IAddContext;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialog;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialogUtil;
import com.mmxlabs.models.ui.editors.dialogs.MultiDetailDialog;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.BooleanAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.MultipleReferenceManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.NumericAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.SingleReferenceManipulator;
import com.mmxlabs.rcp.common.SelectionHelper;
import com.mmxlabs.rcp.common.actions.AbstractMenuLockableAction;
import com.mmxlabs.rcp.common.actions.LockableAction;
import com.mmxlabs.rcp.common.actions.RunnableAction;
import com.mmxlabs.scenario.service.model.manager.ModelReference;

public class VesselViewerPane_View extends ScenarioTableViewerPane {

	private class ReferenceVesselFlagAttributeManipulator extends BooleanAttributeManipulator {
		public ReferenceVesselFlagAttributeManipulator(EStructuralFeature field, final ICommandHandler commandHandler) {
			super(field, commandHandler);
			setExtraCommandsHook(buildExtraCommands());
		}

		public ReferenceVesselFlagAttributeManipulator(final EStructuralFeature field, final ICommandHandler commandHandler, final String trueString, final String falseString) {
			super(field, commandHandler, trueString, falseString);
			setExtraCommandsHook(buildExtraCommands());
		}

		private IExtraCommandsHook buildExtraCommands() {
			return (ed, cmd, parent, object, value) -> {
				if (value == Boolean.TRUE) {
					final Command command = ed.createCommand(SetCommand.class, new CommandParameter(object, FleetPackage.eINSTANCE.getVessel_Reference(), null));
					cmd.append(command);
				}
			};
		}

		@Override
		protected CellEditor createCellEditor(final Composite parent, final Object object) {
			return new ComboBoxCellEditor(parent, new String[] { trueString, falseString }, SWT.READ_ONLY | SWT.FLAT | SWT.BORDER);
		}

		@Override
		public boolean canEdit(Object object) {
			if (object instanceof Vessel) {
				return !((Vessel) object).isMmxReference() && super.canEdit(object);
			}
			return super.canEdit(object);
		}
	}

	private final IScenarioEditingLocation scenarioEditingLocation;
	private Action newVesselFromReferenceAction;
	private final IVesselImportCommandProvider vesselCommandProvider;

	public VesselViewerPane_View(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(page, part, location, actionBars);
		this.scenarioEditingLocation = location;
		this.vesselCommandProvider = part.getSite().getService(IVesselImportCommandProvider.class);
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory, final ModelReference modelReference) {
		super.init(path, adapterFactory, modelReference);

		getToolBarManager().appendToGroup("edit", new BaseFuelEditorAction());
		getToolBarManager().update(true);

		final ICommandHandler commandHandler = scenarioEditingLocation.getDefaultCommandHandler();

		addTypicalColumn("Name", new VesselManipulatorWrapper<>(new BasicAttributeManipulator(MMXCorePackage.eINSTANCE.getNamedObject_Name(), commandHandler)));
		addTypicalColumn("Capacity (m³)", new VesselManipulatorWrapper<>(new NumericAttributeManipulator(FleetPackage.eINSTANCE.getVessel_Capacity(), commandHandler)));
		addTypicalColumn("Shortname", new VesselManipulatorWrapper<>(new BasicAttributeManipulator(FleetPackage.eINSTANCE.getVessel_ShortName(), commandHandler)));
		addTypicalColumn("Type", new VesselManipulatorWrapper<>(new BasicAttributeManipulator(FleetPackage.eINSTANCE.getVessel_Type(), commandHandler)));
		addTypicalColumn("Is Ref. Vessel", new VesselManipulatorWrapper<>(new ReferenceVesselFlagAttributeManipulator(FleetPackage.eINSTANCE.getVessel_ReferenceVessel(), commandHandler, "Y", "")));
		addTypicalColumn("Reference",
				new VesselManipulatorWrapper<>(new SingleReferenceManipulator(FleetPackage.eINSTANCE.getVessel_Reference(), scenarioEditingLocation.getReferenceValueProviderCache(), commandHandler)) {
					@Override
					public boolean canEdit(final Object object) {
						if (object instanceof Vessel) {
							return !((Vessel) object).isReferenceVessel() && super.canEdit(object);
						}
						return super.canEdit(object);
					}
				});
		addTypicalColumn("Inaccessible Ports", new VesselManipulatorWrapper<>(new MultipleReferenceManipulator(FleetPackage.eINSTANCE.getVessel_InaccessiblePorts(),
				scenarioEditingLocation.getReferenceValueProviderCache(), commandHandler, MMXCorePackage.eINSTANCE.getNamedObject_Name())));
		addTypicalColumn("Max Speed", new VesselManipulatorWrapper<>(new NumericAttributeManipulator(FleetPackage.eINSTANCE.getVessel_MaxSpeed(), commandHandler)));

		getToolBarManager().appendToGroup(EDIT_GROUP, new Action() {
			{
				setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.models.lng.port.editor", "/icons/group.gif"));
			}

			@Override
			public void run() {
				final MMXRootObject rootObject = scenarioEditingLocation.getRootObject();
				if (rootObject instanceof LNGScenarioModel) {
					final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
					final FleetModel fleetModel = ScenarioModelUtil.getFleetModel(lngScenarioModel);

					final DetailCompositeDialog dcd = new DetailCompositeDialog(VesselViewerPane_View.this.getScenarioEditingLocation().getShell(),
							VesselViewerPane_View.this.getScenarioEditingLocation().getDefaultCommandHandler());
					dcd.open(getScenarioEditingLocation(), getScenarioEditingLocation().getRootObject(), fleetModel, FleetPackage.eINSTANCE.getFleetModel_VesselGroups());
				}
			}
		});
		getToolBarManager().update(true);

		setTitle("Vessels", PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_DEF_VIEW));

		viewer.addSelectionChangedListener(e -> {
			if (newVesselFromReferenceAction != null) {
				boolean active = false;
				final ISelection selection = viewer.getSelection();
				if (selection instanceof IStructuredSelection) {
					final IStructuredSelection ss = (IStructuredSelection) selection;
					if (ss.size() == 1) {
						final Object firstElement = ss.getFirstElement();
						if (firstElement instanceof Vessel) {
							final Vessel vessel = (Vessel) firstElement;
							active = vessel.getReference() == null;
						}
					}
				}
				newVesselFromReferenceAction.setEnabled(active);
			}
		});
	}

	@Override
	protected Action createAddAction(final EReference containment) {

		newVesselFromReferenceAction = new RunnableAction("Vessel from reference", () -> {

			final ISelection selection = viewer.getSelection();
			if (selection instanceof IStructuredSelection) {
				final IStructuredSelection ss = (IStructuredSelection) selection;
				final Object firstElement = ss.getFirstElement();
				if (firstElement instanceof Vessel) {
					final Vessel reference = (Vessel) firstElement;

					final Vessel vessel = FleetFactory.eINSTANCE.createVessel();
					vessel.setLadenAttributes(FleetFactory.eINSTANCE.createVesselStateAttributes());
					vessel.setBallastAttributes(FleetFactory.eINSTANCE.createVesselStateAttributes());
					vessel.setReference(reference);
					final CompoundCommand cmd = new CompoundCommand("New vessel");
					cmd.append(AddCommand.create(getEditingDomain(), ScenarioModelUtil.getFleetModel(getScenarioEditingLocation().getScenarioDataProvider()),
							FleetPackage.Literals.FLEET_MODEL__VESSELS, Collections.singleton(vessel)));
					final CommandStack commandStack = getEditingDomain().getCommandStack();
					commandStack.execute(cmd);
					DetailCompositeDialogUtil.editSingleObjectWithUndoOnCancel(getScenarioEditingLocation(), vessel, commandStack.getMostRecentCommand());
				}
			}
		});

		final Action duplicateAction = createDuplicateAction();
		final Action importVesselAction = new RunnableAction("Import vessel from LiNGO DB", () -> this.vesselCommandProvider.run(this.scenarioEditingLocation.getScenarioInstance()));
		final Action[] extraActions = duplicateAction == null ? new Action[] { newVesselFromReferenceAction, importVesselAction }
				: new Action[] { newVesselFromReferenceAction, duplicateAction, importVesselAction };
		return AddModelAction.create(containment.getEReferenceType(), getAddContext(containment), extraActions);
	}

	@Override
	protected @Nullable Action createDuplicateAction() {
		final DuplicateAction result = new DuplicateAction(getScenarioEditingLocation()) {
			@Override
			protected boolean isApplicableToSelection(ISelection selection) {
				if (super.isApplicableToSelection(selection)) {
					final IStructuredSelection iStructuredSelection = (IStructuredSelection) selection;
					final List<Vessel> list = (List<Vessel>) iStructuredSelection.toList();
					return list.stream().noneMatch(Vessel::isMmxReference);
				}
				return false;
			}
		};
		scenarioViewer.addSelectionChangedListener(result);
		return result;
	}

	@Override
	protected Action createDeleteAction(@Nullable final Function<Collection<?>, Collection<Object>> callback) {
		return new VesselTableViewerDeleteAction(null);
	}

	private class VesselTableViewerDeleteAction extends ScenarioTableViewerDeleteAction {

		public VesselTableViewerDeleteAction(Function<Collection<?>, Collection<Object>> callback) {
			super(callback);
		}

		@Override
		protected boolean isApplicableToSelection(final ISelection selection) {
			return super.isApplicableToSelection(selection)
					&& ((List<Object>) (((IStructuredSelection) selection).toList())).stream().filter(Vessel.class::isInstance).map(Vessel.class::cast).noneMatch(Vessel::isMmxReference);
		}
	}

	@Override
	protected void enableOpenListener() {
		scenarioViewer.addOpenListener(event -> {
			final ISelection selection = scenarioViewer.getSelection();
			if (selection instanceof IStructuredSelection) {
				final IStructuredSelection structuredSelection = (IStructuredSelection) selection;
				if (!structuredSelection.isEmpty()) {
					if (structuredSelection.size() == 1) {
						DetailCompositeDialogUtil.editInlock(scenarioEditingLocation, () -> {
							final DetailCompositeDialog dcd = new DetailCompositeDialog(scenarioEditingLocation.getShell(), scenarioEditingLocation.getDefaultCommandHandler());
							return dcd.open(scenarioEditingLocation, scenarioEditingLocation.getRootObject(), structuredSelection.toList(), false,
									((Vessel) structuredSelection.getFirstElement()).isMmxReference());
						});
					} else {
						final List<Vessel> mmxSelectedVessels = ((List<Object>) structuredSelection.toList()).stream().map(Vessel.class::cast).filter(Vessel::isMmxReference)
								.collect(Collectors.toList());
						if (mmxSelectedVessels.isEmpty()) {
							DetailCompositeDialogUtil.editInlock(scenarioEditingLocation, () -> {
								final MultiDetailDialog mdd = new MultiDetailDialog(scenarioEditingLocation.getShell(), scenarioEditingLocation.getRootObject(),
										scenarioEditingLocation.getDefaultCommandHandler());
								final List elements = structuredSelection.toList();
								return mdd.open(scenarioEditingLocation, elements);
							});
						} else {
							final String message = String.format("Selection includes read-only vessels maintained by LiNGO.%nTo multi-edit, deselect vessels: %s.",
									createCommaSeparatedVesselList(mmxSelectedVessels));
							MessageDialog.openInformation(part.getSite().getShell(), "Multi-edit not available for LiNGO DB vessels", message);
						}
					}
				}
			}
		});
	}

	private String createCommaSeparatedVesselList(@NonNull final List<Vessel> vessels) {
		final Iterator<Vessel> vesselIter = vessels.iterator();
		final StringBuilder sb = new StringBuilder();
		sb.append(vesselIter.next().getName());
		if (vesselIter.hasNext()) {
			for (int i = 1; i < vessels.size() - 1; i++) {
				sb.append(", ");
				sb.append(vesselIter.next().getName());
			}
			sb.append(" and ");
			sb.append(vesselIter.next().getName());
		}
		return sb.toString();
	}

	class BaseFuelEditorAction extends AbstractMenuLockableAction {
		public BaseFuelEditorAction() {
			super("Base Fuels");
			try {
				setImageDescriptor(ImageDescriptor.createFromURL(new URL("platform:/plugin/com.mmxlabs.models.lng.fleet.editor/icons/oildrop.png")));
			} catch (final MalformedURLException e) {
			}
		}

		@Override
		public void run() {
			final MMXRootObject rootObject = scenarioEditingLocation.getRootObject();
			if (rootObject instanceof LNGScenarioModel) {
				final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
				final FleetModel fleetModel = ScenarioModelUtil.getFleetModel(lngScenarioModel);
				final DetailCompositeDialog dcd = new DetailCompositeDialog(VesselViewerPane_View.this.getScenarioEditingLocation().getShell(),
						VesselViewerPane_View.this.getScenarioEditingLocation().getDefaultCommandHandler());
				dcd.open(getScenarioEditingLocation(), getScenarioEditingLocation().getRootObject(), fleetModel, FleetPackage.eINSTANCE.getFleetModel_BaseFuels());
			}
		}

		@Override
		protected void populate(final Menu menu) {
			if (scenarioEditingLocation.getRootObject() instanceof LNGScenarioModel) {
				final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) scenarioEditingLocation.getRootObject();
				final FleetModel fleetModel = ScenarioModelUtil.getFleetModel(lngScenarioModel);
				boolean b = false;
				for (final BaseFuel baseFuel : fleetModel.getBaseFuels()) {
					b = true;
					final Action editBase = new AbstractMenuLockableAction(baseFuel.getName()) {
						@Override
						protected void populate(final Menu submenu) {
							final LockableAction edit = new LockableAction("Edit...") {
								@Override
								public void run() {
									final DetailCompositeDialog dcd = new DetailCompositeDialog(scenarioEditingLocation.getShell(), scenarioEditingLocation.getDefaultCommandHandler());
									dcd.open(getScenarioEditingLocation(), scenarioEditingLocation.getRootObject(), Collections.singletonList((EObject) baseFuel));
								}
							};
							addActionToMenu(edit, submenu);

							final Action delete = new LockableAction("Delete...") {
								@Override
								public void run() {
									final ICommandHandler handler = scenarioEditingLocation.getDefaultCommandHandler();
									handler.handleCommand(DeleteCommand.create(handler.getEditingDomain(), Collections.singleton(baseFuel)), fleetModel,
											FleetPackage.eINSTANCE.getFleetModel_BaseFuels());
								}
							};
							addActionToMenu(delete, submenu);
						}
					};
					addActionToMenu(editBase, menu);
				}

				if (b) {
					new MenuItem(menu, SWT.SEPARATOR);
				}
				final Action newBase = AddModelAction.create(FleetPackage.eINSTANCE.getBaseFuel(), new IAddContext() {
					@Override
					public MMXRootObject getRootObject() {
						return scenarioEditingLocation.getRootObject();
					}

					@Override
					public EReference getContainment() {
						return FleetPackage.eINSTANCE.getFleetModel_BaseFuels();
					}

					@Override
					public EObject getContainer() {
						return fleetModel;
					}

					@Override
					public ICommandHandler getCommandHandler() {
						return scenarioEditingLocation.getDefaultCommandHandler();
					}

					@Override
					public IScenarioEditingLocation getEditorPart() {
						return scenarioEditingLocation;
					}

					@Override
					public @Nullable Collection<@NonNull EObject> getCurrentSelection() {
						return SelectionHelper.convertToList(viewer.getSelection(), EObject.class);
					}
				});
				if (newBase != null) {
					newBase.setText("Add new base fuel...");
					addActionToMenu(newBase, menu);
				}
			}
		}
	}
}
