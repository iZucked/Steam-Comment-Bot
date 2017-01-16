/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.commands;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.ui.EclipseUIPlugin;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mmxlabs.scenario.service.model.ScenarioFragment;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class DeleteScenarioFragmentCommandHandler extends AbstractHandler {
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
						if (element instanceof ScenarioFragment) {
							final ScenarioFragment fragment = (ScenarioFragment) element;
							final ScenarioInstance instance = fragment.getScenarioInstance();
							final EditingDomain domain = (EditingDomain) instance.getAdapters().get(EditingDomain.class);
							final EObject fragmentObject = fragment.getFragment();
							if (fragmentObject != null) {
								if (fragmentObject.eContainer() != null) {
									domain.getCommandStack().execute(DeleteCommand.create(domain, fragmentObject));
								} else {
									final Map<EObject, Collection<EStructuralFeature.Setting>> usagesByCopy = EcoreUtil.UsageCrossReferencer.findAll(Collections.singleton(fragmentObject),
											instance.getInstance());
									final Collection<EStructuralFeature.Setting> usages = usagesByCopy.get(fragmentObject);
									CompoundCommand cmd = new CompoundCommand("Delete fragment");
									if (usages != null) {
										for (final EStructuralFeature.Setting setting : usages) {
											if (setting.getEStructuralFeature().isMany()) {
												cmd.append(RemoveCommand.create(domain, setting.getEObject(), setting.getEStructuralFeature(), fragmentObject));
											} else {
												cmd.append(SetCommand.create(domain, setting.getEObject(), setting.getEStructuralFeature(), SetCommand.UNSET_VALUE));
											}
										}
									}

									if (!cmd.isEmpty()) {
										domain.getCommandStack().execute(cmd);
									}
								}
							}
						}
					}
				}
			}
		});

		return null;
	}
}
