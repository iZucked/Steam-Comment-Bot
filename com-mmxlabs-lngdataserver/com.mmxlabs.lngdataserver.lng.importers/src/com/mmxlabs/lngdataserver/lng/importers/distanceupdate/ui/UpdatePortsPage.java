/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
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
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.nebula.widgets.grid.internal.DefaultCellRenderer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.lngdataserver.lng.importers.distanceupdate.DistancesLinesToScenarioCopier;
import com.mmxlabs.lngdataserver.lng.importers.distanceupdate.LocationsToScenarioCopier;
import com.mmxlabs.lngdataserver.lng.importers.distanceupdate.model.AtoBviaCLookupRecord;
import com.mmxlabs.lngdataserver.lng.importers.distanceupdate.model.LocationsVersion;
import com.mmxlabs.lngdataserver.lng.importers.update.UpdateItem;
import com.mmxlabs.lngdataserver.lng.importers.update.UpdateItemComparator;
import com.mmxlabs.lngdataserver.lng.importers.update.UpdateItemLabelProvider;
import com.mmxlabs.lngdataserver.lng.importers.update.UpdateStep;
import com.mmxlabs.lngdataserver.lng.importers.update.UpdateWarning;
import com.mmxlabs.lngdataserver.lng.importers.update.UserUpdateStep;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.scenario.service.model.manager.ModelReference;

public class UpdatePortsPage extends WizardPage {

	private GridTableViewer viewer;

	final ExtensibleURIConverterImpl uc = new ExtensibleURIConverterImpl();
	private LocationsVersion locationRecords;
	private List<AtoBviaCLookupRecord> distanceRecords;

	private ModelReference modelReference;

	private List<UpdateItem> steps;

	protected UpdatePortsPage(final String pageName, ModelReference modelReference, LocationsVersion locationRecords, List<AtoBviaCLookupRecord> distanceRecords) {
		super(pageName);
		this.modelReference = modelReference;
		this.locationRecords = locationRecords;
		this.distanceRecords = distanceRecords;

		setTitle("Review changes");
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
			public boolean select(Viewer viewer, Object parentElement, Object element) {
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
			GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.NONE);
			gvc.getColumn().setText("Issue");
			gvc.getColumn().setWidth(400);
			gvc.setLabelProvider(new UpdateItemLabelProvider(0));
		}
		{
			GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.CHECK);
			gvc.getColumn().setText("Apply suggestion?");
			gvc.getColumn().setWidth(200);
			gvc.setLabelProvider(new UpdateItemLabelProvider(1));
			gvc.getColumn().setCellRenderer(new DefaultCellRenderer() {

				@Override
				public void paint(GC gc, Object value) {

					// Record existing state
					boolean state = isCheck();

					GridItem item = (GridItem) value;
					Object element = item.getData();
					boolean isReallyCheck = false;
					if (element instanceof UpdateWarning) {
						UpdateWarning updateWarning = (UpdateWarning) element;
						if (updateWarning.isHasQuickFix()) {
							isReallyCheck = true;
						}
					}
					if (element instanceof UserUpdateStep) {
						UserUpdateStep step = (UserUpdateStep) element;
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
		viewer.getGrid().addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				GridItem item = (GridItem) e.item;
				if (e.detail == SWT.CHECK) {

					Object element = item.getData();
					if (element instanceof UpdateWarning) {
						UpdateWarning updateWarning = (UpdateWarning) element;
						if (updateWarning.isHasQuickFix()) {
							updateWarning.setApplyQuickFix(item.getChecked(1));
						}
					} else if (element instanceof UserUpdateStep) {
						UserUpdateStep step = (UserUpdateStep) element;
						if (step.isHasQuickFix()) {
							step.setApplyQuickFix(item.getChecked(1));
						}
					}
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});

		viewer.setComparator(new UpdateItemComparator());

		setControl(holder);
		// Generate the list of changes needed

		steps = generatePortUpdateSteps();
		viewer.setInput(steps);

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
								final CompoundCommand command = DistancesLinesToScenarioCopier.getUpdateCommand(editingDomain, portModel, distanceRecords);
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
			List<UpdateItem> lsteps = new LinkedList<>();
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
		} catch (Exception e) {
			setErrorMessage(e.getMessage());
		} finally {
			getContainer().updateMessage();
		}
		return false;
	}
}
