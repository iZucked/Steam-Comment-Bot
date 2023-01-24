/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.lingodata.wizard;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mmxlabs.lngdataserver.lng.importers.lingodata.wizard.ImportFromBaseSelectionPage.DataOptionGroup;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;
import com.mmxlabs.scenario.service.ui.IBaseCaseVersionsProvider;

public class CopyFromBaseCaseCommandHandler extends AbstractHandler {
//
//	@Override
//	public void setEnabled(Object evaluationContext) {
//
//		boolean enabled = true;
//		final IWorkbench workbench = PlatformUI.getWorkbench();
//		if (workbench == null) {
//			return;
//		}
//		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
//		if (window == null) {
//			return;
//		}
//		IWorkbenchPage activePage = window.getActivePage();
//		if (activePage == null) {
//			return;
//		}
//		final ISelection selection = activePage.getSelection();
//		if (selection instanceof IStructuredSelection) {
//			final IStructuredSelection strucSelection = (IStructuredSelection) selection;
//			final Iterator<?> itr = strucSelection.iterator();
//			while (itr.hasNext()) {
//				final Object element = itr.next();
//
//				if (element instanceof ScenarioInstance) {
//					final ScenarioInstance instance = (ScenarioInstance) element;
//					final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(instance);
//
//					try (ModelReference ref = modelRecord.aquireReferenceIfLoaded("CreateADPCommandHandler:setEnabled")) {
//						if (ref != null) {
//							final LNGScenarioModel scenarioModel = (LNGScenarioModel) ref.getInstance();
//							enabled = scenarioModel.getAdpModel() == null;
//							break;
//						}
//					}
//				}
//			}
//		}
//
//		setBaseEnabled(enabled);
//	}

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final IWorkbenchPage activePage = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();

		final Exception exceptions[] = new Exception[1];

		BusyIndicator.showWhile(HandlerUtil.getActiveShellChecked(event).getDisplay(), new Runnable() {

			@Override
			public void run() {
				final ISelection selection = activePage.getSelection();
				if (selection instanceof IStructuredSelection) {
					final IStructuredSelection strucSelection = (IStructuredSelection) selection;
					final Iterator<?> itr = strucSelection.iterator();
					while (itr.hasNext()) {
						final Object element = itr.next();

						if (element instanceof ScenarioInstance) {
							final ScenarioInstance instance = (ScenarioInstance) element;
							try {
								doCopyBaseCase(instance);
							} catch (final Exception e) {
								exceptions[0] = e;
							}
						}
					}
				}
			}

		});

		if (exceptions[0] != null) {
			throw new ExecutionException("Unable to copy data from base case: " + exceptions[0], exceptions[0]);
		}

		return null;

	}

	private void doCopyBaseCase(final ScenarioInstance scenarioInstance) throws Exception {
		ServiceHelper.<IBaseCaseVersionsProvider> withServiceConsumer(IBaseCaseVersionsProvider.class, p -> {
			final ScenarioInstance baseCase = p.getBaseCase();
			if (baseCase != null) {

				Map<String, String> scenarioDataVersions = ScenarioStorageUtil.extractScenarioDataVersions(scenarioInstance.getManifest());
				if (scenarioDataVersions != null) {
					final List<DataOptionGroup> groups = SharedScenarioDataUtils.createGroups(p, scenarioDataVersions);

					final ImportFromBaseImportWizard wizard = new ImportFromBaseImportWizard(baseCase, scenarioInstance, groups);
					wizard.init(PlatformUI.getWorkbench(), StructuredSelection.EMPTY);
					wizard.setForcePreviousAndNextButtons(true);
					final Shell parent = PlatformUI.getWorkbench().getDisplay().getActiveShell();
					final WizardDialog dialog = new WizardDialog(parent, wizard);
					dialog.create();
					dialog.open();
				}
			}
		});
	}
}
