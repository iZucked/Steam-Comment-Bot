package com.mmxlabs.models.lng.analytics.ui.views;

import java.util.Collections;
import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;
import com.mmxlabs.scenario.service.model.ModelReference;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class CreateSandboxHandler extends AbstractHandler {

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {

		final ISelection selection = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().getSelection();

		if ((selection != null) && (selection instanceof IStructuredSelection)) {
			final IStructuredSelection strucSelection = (IStructuredSelection) selection;

			final Iterator<?> itr = strucSelection.iterator();
			while (itr.hasNext()) {
				final Object obj = itr.next();
				if (obj instanceof ScenarioInstance) {

					final ScenarioInstance instance = (ScenarioInstance) obj;

					try (ModelReference reference = instance.getReference("CreateSandboxHandler")) {
						final EObject rootObject = reference.getInstance();
						final EditingDomain domain = (EditingDomain) instance.getAdapters().get(EditingDomain.class);
						final Display display = PlatformUI.getWorkbench().getDisplay();
						final InputDialog dialog = new InputDialog(display.getActiveShell(), "Create sandbox", "Choose name for sandbox", "sandbox", null);

						if (dialog.open() == Window.OK) {
							final String name = dialog.getValue();

							final OptionAnalysisModel model = AnalyticsFactory.eINSTANCE.createOptionAnalysisModel();

							model.setName(name);

							model.setBaseCase(AnalyticsFactory.eINSTANCE.createBaseCase());
							model.setPartialCase(AnalyticsFactory.eINSTANCE.createPartialCase());

							final CompoundCommand cmd = new CompoundCommand("Create sandbox");
							cmd.append(AddCommand.create(domain, rootObject, LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_OptionModels(), Collections.singletonList(model)));
							domain.getCommandStack().execute(cmd);
						}
					}
				}
			}
		}
		return null;
	}

}
