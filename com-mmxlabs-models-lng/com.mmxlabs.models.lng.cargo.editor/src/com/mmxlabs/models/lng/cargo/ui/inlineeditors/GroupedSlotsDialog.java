/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.inlineeditors;

import org.eclipse.core.databinding.ObservablesManager;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.FormDialog;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.GroupedDischargeSlotsConstraint;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.DetailToolbarManager;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.tabular.EObjectTableViewer;
import com.mmxlabs.models.ui.tabular.manipulators.MultipleReferenceManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.NumericAttributeManipulator;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;

public class GroupedSlotsDialog extends FormDialog {

	private EObjectTableViewer tableViewer;
	private EObject container;
	private EReference containment;

	private final IScenarioEditingLocation scenarioEditingLocation;
	private ObservablesManager mgr;
	private EMFDataBindingContext dbc;

	public GroupedSlotsDialog(@NonNull final IShellProvider parentShell, @NonNull final IScenarioEditingLocation originalScenarioEditingLocation) {
		super(parentShell);
		this.scenarioEditingLocation = originalScenarioEditingLocation;
	}

	public GroupedSlotsDialog(@NonNull final Shell parentShell, @NonNull final IScenarioEditingLocation originalScenarioEditingLocation) {
		super(parentShell);
		this.scenarioEditingLocation = originalScenarioEditingLocation;
	}

	private void createFormArea(final IManagedForm mform) {
		final FormToolkit toolkit = mform.getToolkit();

		final Composite body = mform.getForm().getBody();
		body.setLayout(new GridLayout(1, false));
		final Composite mainPane = toolkit.createComposite(body);
		mainPane.setLayout(new GridLayout(1, false));
		mainPane.setLayoutData(new GridData(GridData.FILL_BOTH));

		final Group template = new Group(mainPane, SWT.NONE);
		template.setLayout(new GridLayout(2, false));
		template.setLayoutData(new GridData(GridData.FILL_BOTH));

		template.setText("Constraints");
		final DetailToolbarManager buttonManager = new DetailToolbarManager(template, SWT.TOP);

		tableViewer = new EObjectTableViewer(template, SWT.FULL_SELECTION | SWT.BORDER);
		tableViewer.init(scenarioEditingLocation.getAdapterFactory(), scenarioEditingLocation.getModelReference(), CargoPackage.Literals.CARGO_MODEL__GROUPED_DISCHARGE_SLOTS);

		final Grid table = tableViewer.getGrid();

		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setRowHeaderVisible(true);

		final Action addConstraintGroup = new Action("Add") {
			@Override
			public void run() {
				final CompoundCommand cmd = new CompoundCommand("Add constraint row");
				final GroupedDischargeSlotsConstraint newConstraint = CargoFactory.eINSTANCE.createGroupedDischargeSlotsConstraint();
				cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), ((LNGScenarioModel) scenarioEditingLocation.getRootObject()).getCargoModel(),
						CargoPackage.Literals.CARGO_MODEL__GROUPED_DISCHARGE_SLOTS, newConstraint));
				if (!cmd.isEmpty()) {
					final EditingDomain ed = scenarioEditingLocation.getEditingDomain();
					ed.getCommandStack().execute(cmd);
				}
			}
		};
		CommonImages.setImageDescriptors(addConstraintGroup, IconPaths.Plus);
		buttonManager.getToolbarManager().add(addConstraintGroup);

		final Action deleteRowsAction = new Action("Delete group constraints") {
			@Override
			public void run() {
				if (tableViewer != null) {
					final ISelection selection = tableViewer.getSelection();
					if (selection.isEmpty()) {
						return;
					}
					if (selection instanceof IStructuredSelection iStructuredSelection) {
						final CompoundCommand cc = new CompoundCommand("Delete group constraints");
						final EditingDomain ed = scenarioEditingLocation.getEditingDomain();
						final EObject firstLine = (EObject) iStructuredSelection.getFirstElement();
						cc.append(RemoveCommand.create(ed, firstLine.eContainer(), firstLine.eContainingFeature(), iStructuredSelection.toList()));
						ed.getCommandStack().execute(cc);
					}
				}
			}
		};
		CommonImages.setImageDescriptors(deleteRowsAction, IconPaths.Delete);
		deleteRowsAction.setEnabled(false);
		buttonManager.getToolbarManager().add(deleteRowsAction);

		final ICommandHandler commandHandler = scenarioEditingLocation.getDefaultCommandHandler();

		// Use specific (GroupedDischargeSlotsConstraint) reference value provider
		final IReferenceValueProvider dischargeSlotProvider = commandHandler.getReferenceValueProviderProvider().getReferenceValueProvider(CargoPackage.eINSTANCE.getGroupedDischargeSlotsConstraint(),
				CargoPackage.eINSTANCE.getGroupedSlotsConstraint_Slots());
		final MultipleReferenceManipulator slotsManipulator = new MultipleReferenceManipulator(CargoPackage.eINSTANCE.getGroupedSlotsConstraint_Slots(), commandHandler, dischargeSlotProvider,
				MMXCorePackage.eINSTANCE.getNamedObject_Name());
		tableViewer.addTypicalColumn("Slots", slotsManipulator);
		tableViewer.addTypicalColumn("Minimum Required", new NumericAttributeManipulator(CargoPackage.eINSTANCE.getGroupedSlotsConstraint_MinimumBound(), commandHandler));

		tableViewer.setInput(((LNGScenarioModel) scenarioEditingLocation.getRootObject()).getCargoModel());
		tableViewer.refresh();
		table.setLayoutData(new GridData(GridData.FILL_BOTH));

		buttonManager.getToolbarManager().update(true);
		tableViewer.addSelectionChangedListener(event -> {
			final ISelection iSelection = event.getSelection();
			deleteRowsAction.setEnabled(!iSelection.isEmpty());
		});
	}

	public EList<GroupedDischargeSlotsConstraint> getResult() {
		return (EList<GroupedDischargeSlotsConstraint>) this.container.eGet(containment);
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	@Override
	public void create() {
		super.create();
		getShell().setText("Grouped discharge slot constraints");
	}

	@Override
	protected void createFormContent(final IManagedForm mForm) {
		String title = "Edit grouped discharge slot constraints";
		mForm.getForm().setText(title);
		mForm.getToolkit().decorateFormHeading(mForm.getForm().getForm());

		mgr = new ObservablesManager();
		dbc = new EMFDataBindingContext();

		mgr.runAndCollect(() -> createFormArea(mForm));
	}

	@Override
	public boolean close() {
		if (dbc != null) {
			dbc.dispose();
			dbc = null;
		}
		if (mgr != null) {
			mgr.dispose();
			mgr = null;
		}
		return super.close();
	}
}
