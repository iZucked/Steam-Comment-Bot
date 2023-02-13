/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.ui.editorpart;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.function.Function;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.port.ContingencyMatrix;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialog;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialogUtil;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.BooleanAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.ReadOnlyManipulatorWrapper;
import com.mmxlabs.rcp.common.actions.AbstractMenuLockableAction;
import com.mmxlabs.rcp.common.actions.LockableAction;
import com.mmxlabs.scenario.service.model.manager.ModelReference;

public class PortEditorPane extends ScenarioTableViewerPane {

	public PortEditorPane(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(page, part, location, actionBars);
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory, final ModelReference modelReference) {
		super.init(path, adapterFactory, modelReference);

		addTypicalColumn("Name", new ReadOnlyManipulatorWrapper<>(new BasicAttributeManipulator(MMXCorePackage.eINSTANCE.getNamedObject_Name(), getCommandHandler())));

		addTypicalColumn("Country", new ReadOnlyManipulatorWrapper<>(new BasicAttributeManipulator(PortPackage.eINSTANCE.getLocation_Country(), getCommandHandler())),
				PortPackage.eINSTANCE.getPort_Location());
		addTypicalColumn("UN/Locode", new ReadOnlyManipulatorWrapper<>(new BasicAttributeManipulator(PortPackage.eINSTANCE.getLocation_Locode(), getCommandHandler())),
				PortPackage.eINSTANCE.getPort_Location());
		// addTypicalColumn("MMX ID ", new
		// BasicAttributeManipulator(PortPackage.eINSTANCE.getLocation_MmxId(),
		// getCommandHandler()), PortPackage.eINSTANCE.getPort_Location());

		addTypicalColumn("Load", new ReadOnlyManipulatorWrapper<>(new CapabilityManipulator(PortCapability.LOAD, getCommandHandler()) {
			@Override
			public CellEditor getCellEditor(final Composite parent, final Object object) {
				return new ComboBoxCellEditor(parent, new String[] { "Yes", "No" });
			}

			@Override
			public String render(final Object object) {
				return ((Integer) getValue(object)) == 0 ? "Yes" : "No";
			}
		}));
		addTypicalColumn("Discharge", new ReadOnlyManipulatorWrapper<>(new CapabilityManipulator(PortCapability.DISCHARGE, getCommandHandler()) {
			@Override
			public CellEditor getCellEditor(final Composite parent, final Object object) {
				return new ComboBoxCellEditor(parent, new String[] { "Yes", "No" });
			}

			@Override
			public String render(final Object object) {
				return ((Integer) getValue(object)) == 0 ? "Yes" : "No";
			}
		}));
		addTypicalColumn("Cooldown?", new ReadOnlyManipulatorWrapper<>(new BooleanAttributeManipulator(PortPackage.eINSTANCE.getPort_AllowCooldown(), getCommandHandler(), "Yes", "No") {
			@Override
			protected CellEditor createCellEditor(final Composite parent, final Object object) {
				return new ComboBoxCellEditor(parent, new String[] { "Yes", "No" }, SWT.READ_ONLY | SWT.FLAT | SWT.BORDER);
			}

			@Override
			public boolean canEdit(final Object object) {
				return ((Port) object).getCapabilities().contains(PortCapability.LOAD);
			}
		}));
		addTypicalColumn("Other Names",
				new ReadOnlyManipulatorWrapper<BasicAttributeManipulator>(new BasicAttributeManipulator(MMXCorePackage.eINSTANCE.getOtherNamesObject_OtherNames(), getCommandHandler())),
				PortPackage.eINSTANCE.getPort_Location());
		// addTypicalColumn("Timezone", new
		// BasicAttributeManipulator(PortPackage.eINSTANCE.getPort_TimeZone(),
		// getJointModelEditorPart().getEditingDomain()));
		// addTypicalColumn("Port Code", new
		// BasicAttributeManipulator(PortPackage.eINSTANCE.getPort_PortCode(),
		// getJointModelEditorPart().getEditingDomain()));

		final DistanceMatrixEditorAction dmaAction = new DistanceMatrixEditorAction();
		getToolBarManager().appendToGroup(EDIT_GROUP, dmaAction);

		getToolBarManager().appendToGroup(EDIT_GROUP, new Action("Edit Port Groups") {
			{
				setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.models.lng.port.editor", "/icons/group.gif"));
			}

			@Override
			public void run() {
				final DetailCompositeDialog dcd = new DetailCompositeDialog(PortEditorPane.this.getScenarioEditingLocation().getShell(),
						PortEditorPane.this.getScenarioEditingLocation().getDefaultCommandHandler());
				final MMXRootObject rootObject = getScenarioEditingLocation().getRootObject();
				if (rootObject instanceof LNGScenarioModel lngScenarioModel) {
					dcd.open(getScenarioEditingLocation(), rootObject, lngScenarioModel.getReferenceModel().getPortModel(), PortPackage.eINSTANCE.getPortModel_PortGroups());
				}
			}
		});

		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "com.mmxlabs.lingo.doc.Editor_Ports");
	}

	class DistanceMatrixEditorAction extends AbstractMenuLockableAction {
		public DistanceMatrixEditorAction() {
			super("Edit distances");
			try {
				setImageDescriptor(ImageDescriptor.createFromURL(new URL("platform:/plugin/com.mmxlabs.models.lng.port.editor/icons/earth.gif")));
			} catch (final MalformedURLException e) {
			}
			setToolTipText("Edit distance matrices and canals");
		}

		@Override
		protected void populate(final Menu menu) {
			final MMXRootObject rootObject = getScenarioEditingLocation().getRootObject();
			if (rootObject instanceof LNGScenarioModel) {
				final PortModel portModel = ((LNGScenarioModel) rootObject).getReferenceModel().getPortModel();
				for (final Route canal : portModel.getRoutes()) {
					final Action canalEditor = new AbstractMenuLockableAction(canal.getName()) {
						@Override
						protected void populate(final Menu menu2) {
							final Action editCanal = createMatrixEditor(canal);
							addActionToMenu(editCanal, menu2);
							final Action renameCanal = new Action("Rename...") {
								@Override
								public void run() {
									final HashSet<String> existingNames = new HashSet<>();
									for (final Route c : portModel.getRoutes()) {
										if (c != canal) {
											existingNames.add(c.getName());
										}
									}
									final InputDialog input = new InputDialog(part.getSite().getShell(), "Rename canal " + canal.getName(), "Enter a new name for the canal " + canal.getName(),
											canal.getName(), new IInputValidator() {
												@Override
												public String isValid(final String newText) {
													if (newText.trim().isEmpty()) {
														return "The canal must have a name";
													}
													if (existingNames.contains(newText)) {
														return "Another canal already has the name " + newText;
													}
													return null;
												}
											});
									if (input.open() == Window.OK) {
										final String newName = input.getValue();
										getScenarioEditingLocation().getEditingDomain().getCommandStack()
												.execute(SetCommand.create(getScenarioEditingLocation().getEditingDomain(), canal, MMXCorePackage.eINSTANCE.getNamedObject_Name(), newName));
									}
								}
							};
							addActionToMenu(renameCanal, menu2);
						}
					};
					addActionToMenu(canalEditor, menu);
				}
				if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_CONTINGENCY_IDLE_TIME)) {
					addActionToMenu(new ContingencyMatrixEditorAction(portModel), menu);
				}
			}
		};
	}

	protected Action createMatrixEditor(final Route distanceModel) {
		return new LockableAction("Edit distances...") {
			@Override
			public void run() {
				DetailCompositeDialogUtil.editSingleObject(PortEditorPane.this.getScenarioEditingLocation(), distanceModel);
			}
		};
	}

	private class ContingencyMatrixEditorAction extends LockableAction {
		private final PortModel portModel;

		public ContingencyMatrixEditorAction(final PortModel portModel) {
			super("Edit contingency matrix");
			this.portModel = portModel;
			try {
				setImageDescriptor(ImageDescriptor.createFromURL(new URL("platform:/plugin/com.mmxlabs.models.lng.port.editor/icons/earth.gif")));
			} catch (final MalformedURLException e) {
			}
			setToolTipText("Edit contingency delay matrix");
		}

		@Override
		public void run() {

			if (portModel.getContingencyMatrix() == null) {
				final CompoundCommand cmd = new CompoundCommand("Create contingency matrix");
				final ContingencyMatrix matrix = PortFactory.eINSTANCE.createContingencyMatrix();
				cmd.append(SetCommand.create(getEditingDomain(), portModel, PortPackage.Literals.PORT_MODEL__CONTINGENCY_MATRIX, matrix));
				DetailCompositeDialogUtil.editNewObjectWithUndoOnCancel(getScenarioEditingLocation(), matrix, cmd);
			} else {
				DetailCompositeDialogUtil.editSingleObject(PortEditorPane.this.getScenarioEditingLocation(), portModel.getContingencyMatrix());
			}
		}
	}

	@Override
	protected Action createDeleteAction(@Nullable Function<Collection<?>, Collection<Object>> callback) {
		return null;
	}

	@Override
	protected Action createDuplicateAction() {
		return null;
	}
}
