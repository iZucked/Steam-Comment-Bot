/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.distanceupdate.ui;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.DefaultCellRenderer;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.lngdataserver.lng.importers.distanceupdate.DistancesLinesToScenarioCopier;
import com.mmxlabs.lngdataserver.lng.importers.distanceupdate.LocationsToScenarioCopier;
import com.mmxlabs.lngdataserver.lng.importers.distanceupdate.model.AtoBviaCLookupRecord;
import com.mmxlabs.lngdataserver.lng.importers.distanceupdate.model.DistanceDataVersion;
import com.mmxlabs.lngdataserver.lng.importers.distanceupdate.model.LocationsVersion;
import com.mmxlabs.lngdataserver.lng.importers.update.UpdateItem;
import com.mmxlabs.lngdataserver.lng.importers.update.UpdateItemComparator;
import com.mmxlabs.lngdataserver.lng.importers.update.UpdateItemLabelProvider;
import com.mmxlabs.lngdataserver.lng.importers.update.UpdateStep;
import com.mmxlabs.lngdataserver.lng.importers.update.UpdateWarning;
import com.mmxlabs.lngdataserver.lng.importers.update.UserUpdateStep;
import com.mmxlabs.models.lng.parameters.ParametersPackage;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.scenario.service.model.manager.ModelReference;

public class UpdatePortsPage extends WizardPage {

	private GridTableViewer viewer;

	final ExtensibleURIConverterImpl uc = new ExtensibleURIConverterImpl();
	private final DistanceDataVersion dataVersion;
	private final LocationsVersion locationRecords;
	private final List<AtoBviaCLookupRecord> distanceRecords;
	private final List<AtoBviaCLookupRecord> manualRecords;

	private final ModelReference modelReference;

	private List<UpdateItem> steps;


	protected UpdatePortsPage(final String pageName, final ModelReference modelReference, final DistanceDataVersion dataVersion, final LocationsVersion locationRecords, final List<AtoBviaCLookupRecord> distanceRecords,
			final List<AtoBviaCLookupRecord> manualRecords) {
		super(pageName);
		this.modelReference = modelReference;
		this.dataVersion = dataVersion;
		this.locationRecords = locationRecords;
		this.distanceRecords = distanceRecords;
		this.manualRecords = manualRecords;

		setTitle("Review changes");
		String currentVersion = "unknown";
		if (modelReference.getInstance() instanceof final LNGScenarioModel scenarioModel) {
			final PortModel portModel = ScenarioModelUtil.getPortModel(scenarioModel);
			final String v = portModel.getMmxDistanceVersion();
			if (v != null && !v.isBlank()) {
				currentVersion = v;
			}
		}
		setMessage(String.format("Distances version '%s' found. The current scenario version is '%s'. Updating will replace all existing distances.", dataVersion.getVersion(), currentVersion));
	}

	@Override
	public void createControl(final Composite arg0) {

		final Composite holder = new Composite(arg0, SWT.NONE);
		final GridLayout gl = new GridLayout(1, false);
		gl.marginLeft = 0;
		gl.marginWidth = 0;
		holder.setLayout(gl);
		holder.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());

		viewer = new GridTableViewer(holder, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.WRAP);

		{
			final GridData gdViewer = new GridData(SWT.FILL, SWT.FILL, true, true);
			gdViewer.heightHint = 200;
			viewer.getControl().setLayoutData(gdViewer);
		}

		viewer.getGrid().setLinesVisible(true);
		viewer.getGrid().setHeaderVisible(true);

//		viewer.setContentProvider(new GroupedValidationStatusContentProvider());
		viewer.setContentProvider(new ArrayContentProvider());
		viewer.setFilters(new ViewerFilter() {

			@Override
			public boolean select(final Viewer viewer, final Object parentElement, final Object element) {
				if (element instanceof UpdateWarning) {
					return true;
				}
				if (element instanceof UserUpdateStep) {
					return true;
				}
				return false;
			}
		});

		{
			final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.NONE);
			gvc.getColumn().setText("Issue");
			gvc.getColumn().setWidth(400);
			gvc.setLabelProvider(new UpdateItemLabelProvider(0));
		}
		{
			final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.CHECK);
			gvc.getColumn().setText("Apply suggestion?");
			gvc.getColumn().setWidth(200);
			gvc.setLabelProvider(new UpdateItemLabelProvider(1));
			gvc.getColumn().setCellRenderer(new DefaultCellRenderer() {

				@Override
				public void paint(final GC gc, final Object value) {

					// Record existing state
					final boolean state = isCheck();

					final GridItem item = (GridItem) value;
					final Object element = item.getData();
					boolean isReallyCheck = false;
					if (element instanceof UpdateWarning updateWarning) {
						if (updateWarning.isHasQuickFix()) {
							isReallyCheck = true;
						}
					}
					if (element instanceof UserUpdateStep step) {
						if (step.isHasQuickFix()) {
							isReallyCheck = true;
						}
					}
					// set new check state
					setCheck(isReallyCheck);
					super.paint(gc, value);

					// Restore state
					setCheck(state);
				}

			});
		}
		viewer.getGrid().addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent e) {

				final GridItem item = (GridItem) e.item;
				if (e.detail == SWT.CHECK) {

					final Object element = item.getData();
					if (element instanceof UpdateWarning updateWarning) {
						if (updateWarning.isHasQuickFix()) {
							updateWarning.setApplyQuickFix(item.getChecked(1));
						}
					} else if (element instanceof UserUpdateStep step) {
						if (step.isHasQuickFix()) {
							step.setApplyQuickFix(item.getChecked(1));
						}
					}
				}
			}
		});

		viewer.setComparator(new UpdateItemComparator());

		setControl(holder);
		// Generate the list of changes needed

		steps = generatePortUpdateSteps();
		viewer.setInput(steps);

		final Button btn = new Button(holder, SWT.PUSH);
		btn.setText("Check all");
		btn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				for (final GridItem itm : viewer.getGrid().getItems()) {
					itm.setChecked(1, true);
					final Object element = itm.getData();
					if (element instanceof UpdateWarning updateWarning) {
						if (updateWarning.isHasQuickFix()) {
							updateWarning.setApplyQuickFix(true);
						}
					} else if (element instanceof UserUpdateStep step) {
						if (step.isHasQuickFix()) {
							step.setApplyQuickFix(true);
						}
					}
				}
			}

		});

		setPageComplete(false);
	}

	@Override
	public boolean canFlipToNextPage() {
		return isPageComplete();
	}

	private boolean doPortUpdateSteps() {
		try {
			getContainer().run(true, false, new IRunnableWithProgress() {
				@Override
				public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					monitor.beginTask("Apply port update steps", IProgressMonitor.UNKNOWN);
					try {
						{
							final CompoundCommand command = new CompoundCommand("Update ports");
							// Apply basic updates
							steps.stream() //
									.filter(UpdateStep.class::isInstance) //
									.map(UpdateStep.class::cast) //
									.map(UpdateStep::getAction) //
									.filter(Objects::nonNull) //
									.forEach(a -> a.accept(command));

							// Apply selected quick fixes
							steps.stream() //
									.filter(UserUpdateStep.class::isInstance) //
									.map(UserUpdateStep.class::cast) //
									.filter(UserUpdateStep::isApplyQuickFix) //
									.forEach(a -> a.getQuickFix().accept(command));

							steps.stream() //
									.filter(UpdateWarning.class::isInstance) //
									.map(UpdateWarning.class::cast) //
									.filter(UpdateWarning::isApplyQuickFix) //
									.forEach(a -> a.getQuickFix().accept(command));

							if (!command.isEmpty()) {
								// Set new data version
								final PortModel portModel = ScenarioModelUtil.getPortModel((LNGScenarioModel) modelReference.getInstance());
								command.append(SetCommand.create(modelReference.getEditingDomain(), portModel, PortPackage.Literals.PORT_MODEL__MMX_DISTANCE_VERSION, dataVersion.getVersion()));
								
								if (!command.canExecute()) {
									throw new RuntimeException("Unable to execute command");
								}

								RunnerHelper.syncExecDisplayOptional(() -> modelReference.getCommandStack().execute(command));
							}
							monitor.worked(1);

						}
						monitor.subTask("Generate new matrices");

						{
							final PortModel portModel = ScenarioModelUtil.getPortModel((LNGScenarioModel) modelReference.getInstance());
							final EditingDomain editingDomain = modelReference.getEditingDomain();

							{
								final CompoundCommand command = DistancesLinesToScenarioCopier.getUpdateCommand(editingDomain, portModel, locationRecords, distanceRecords, manualRecords);
								monitor.worked(1);
								monitor.subTask("Apply new matrices");
								if (!command.canExecute()) {
									throw new RuntimeException("Unable to execute command");
								}
								RunnerHelper.syncExecDisplayOptional(() -> modelReference.getCommandStack().execute(command));
							}
							monitor.worked(1);

						}
					} catch (final Exception e) {
						setErrorMessage(e.getMessage());
					} finally {
						monitor.done();
					}
				}
			});
		} catch (final InvocationTargetException e) {
			return false;
		} catch (final InterruptedException e) {
			return false;
		}
		return true;
	}

	private List<UpdateItem> generatePortUpdateSteps() {
		try {
			final List<UpdateItem> lsteps = new LinkedList<>();
			getContainer().run(true, false, new IRunnableWithProgress() {
				@Override
				public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					monitor.beginTask("Generate port update steps", IProgressMonitor.UNKNOWN);
					try {

						final PortModel portModel = ScenarioModelUtil.getPortModel((LNGScenarioModel) modelReference.getInstance());
						final EditingDomain editingDomain = modelReference.getEditingDomain();
						lsteps.addAll(LocationsToScenarioCopier.generateUpdates(editingDomain, portModel, locationRecords));

					} catch (final Exception e) {
						setErrorMessage(e.getMessage());
					} finally {
						monitor.done();
					}
				}
			});
			return lsteps;
		} catch (

		final InvocationTargetException e) {
			return null;
		} catch (final InterruptedException e) {
			return null;
		}
	}

	public boolean apply() {
		try {
			doPortUpdateSteps();
			setPageComplete(true);
			getContainer().updateButtons();
			return true;
		} catch (final Exception e) {
			setErrorMessage(e.getMessage());
		} finally {
			getContainer().updateMessage();
		}
		return false;
	}
}
