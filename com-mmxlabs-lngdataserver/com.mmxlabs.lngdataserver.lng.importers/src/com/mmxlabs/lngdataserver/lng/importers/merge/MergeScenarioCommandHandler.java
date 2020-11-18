/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.merge;

import java.io.File;
import java.net.URL;
import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;

/**
 * Handler to import the an ADP scenario into a base case. Note this will need a
 * menu items in the plugin.xml. E.g.
 * 
 * <pre>
     <extension
           point="org.eclipse.ui.menus">   
            <menuContribution
               allPopups="false"
               locationURI="menu:data?after=dataEnd">
            <command
                  commandId="com.mmxlabs.lingo.k.extensions.importadp"
                  label="Import ADP"
                  style="push">
            </command>
         </menuContribution>
   </extension>
 * </pre>
 * 
 * @author Simon Goodall
 *
 */
public class MergeScenarioCommandHandler extends AbstractHandler {

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

		// TODO: Prompt the user!
		URL resourceURL = new File("C://temp//adp.lingo").toURL();
		ScenarioStorageUtil.withExternalScenarioFromResourceURLConsumer(resourceURL, (scenarioModelRecord, sdp) -> {

			MergeScenarioTool convertor = new MergeScenarioTool();
			convertor.doIt(sdp, scenarioInstance);

		});
	}
}
