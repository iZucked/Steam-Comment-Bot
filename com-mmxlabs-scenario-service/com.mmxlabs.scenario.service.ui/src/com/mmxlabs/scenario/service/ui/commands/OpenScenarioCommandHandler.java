/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.commands;

import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.editors.IPartGotoTarget;
import com.mmxlabs.scenario.service.model.ScenarioFragment;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.model.util.IScenarioFragmentOpenHandler;
import com.mmxlabs.scenario.service.ui.OpenScenarioUtils;

public class OpenScenarioCommandHandler extends AbstractHandler {

	private static final Logger log = LoggerFactory.getLogger(OpenScenarioCommandHandler.class);

	/**
	 * the command has been executed, so extract extract the needed information from the application context.
	 */
	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {

		final IWorkbenchPage activePage = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();
		BusyIndicator.showWhile(HandlerUtil.getActiveShellChecked(event).getDisplay(), new Runnable() {

			@Override
			public void run() {
				final ISelection selection = activePage.getSelection();
				if (selection instanceof IStructuredSelection) {
					final IStructuredSelection strucSelection = (IStructuredSelection) selection;
					for (final Iterator<?> iterator = strucSelection.iterator(); iterator.hasNext();) {
						final Object element = iterator.next();
						if (element instanceof ScenarioInstance) {
							openEditor(activePage, element);
						} else if (element instanceof ScenarioFragment) {
							final ScenarioFragment scenarioFragment = (ScenarioFragment) element;

							// There is a delay between loading a scenario and the Scenario Fragment being correctly populated.
							// Use a completable future to wait about a second for this to happen after the scenario is loaded.
							final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenarioFragment.getScenarioInstance());
							final ModelReference ref = modelRecord.aquireReference("OpenScenarioCommandHandler");
							RunnerHelper.asyncExec(() -> {
								try {
									int i = 10;
									while (scenarioFragment.getFragment() == null) {
										try {
											Thread.sleep(100);
										} catch (final InterruptedException e) {
											e.printStackTrace();
										}
										if (--i < 0) {
											break;
										}
									}
									if (scenarioFragment.getFragment() != null) {
										final IScenarioFragmentOpenHandler handler = (IScenarioFragmentOpenHandler) Platform.getAdapterManager().loadAdapter(scenarioFragment.getFragment(),
												IScenarioFragmentOpenHandler.class.getCanonicalName());
										if (handler != null) {
											handler.open(scenarioFragment, scenarioFragment.getScenarioInstance());
											return;
										}

										final IEditorPart part = openEditor(activePage, scenarioFragment.getScenarioInstance());
										if (part instanceof IPartGotoTarget) {
											((IPartGotoTarget) part).gotoTarget(scenarioFragment.getFragment());
										} else {
											final @Nullable IPartGotoTarget adapter = part.getAdapter(IPartGotoTarget.class);
											if (adapter != null) {
												adapter.gotoTarget(scenarioFragment.getFragment());
											}
										}
									}
								} finally {
									ref.close();
								}
							});
						}
					}
				}
			}
		});

		return null;
	}

	private IEditorPart openEditor(final IWorkbenchPage activePage, final Object element) {
		try {
			return OpenScenarioUtils.openAndReturnEditorPart(activePage, (ScenarioInstance) element);
		} catch (final PartInitException e) {

			MessageDialog.openError(activePage.getWorkbenchWindow().getShell(), "Error opening editor", e.getMessage());

			log.error(e.getMessage(), e);
		}
		return null;
	}

}
