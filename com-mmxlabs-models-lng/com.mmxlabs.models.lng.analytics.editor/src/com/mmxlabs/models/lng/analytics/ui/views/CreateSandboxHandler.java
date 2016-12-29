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
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.rcp.common.editors.IPartGotoTarget;
import com.mmxlabs.scenario.service.model.ModelReference;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.OpenScenarioUtils;

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
						if (!(rootObject instanceof LNGScenarioModel)) {
							continue;
						}
						
						final @NonNull AnalyticsModel analyticsModel = ScenarioModelUtil.getAnalyticsModel((LNGScenarioModel) rootObject);

						final EditingDomain domain = (EditingDomain) instance.getAdapters().get(EditingDomain.class);
						final Display display = PlatformUI.getWorkbench().getDisplay();

						final ImageDescriptor d = AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.models.lng.analytics.editor", "icons/sandbox.gif");
						final Image img = d.createImage();
						final InputDialog dialog = new InputDialog(display.getActiveShell(), "Create sandbox", "Choose name for sandbox", "sandbox", null) {
							@Override
							protected void configureShell(final Shell shell) {
								super.configureShell(shell);
								shell.setImage(img);
							}

							@Override
							public boolean close() {
								img.dispose();
								return super.close();
							}
						};

						if (dialog.open() == Window.OK) {
							final String name = dialog.getValue();

							final OptionAnalysisModel model = AnalyticsFactory.eINSTANCE.createOptionAnalysisModel();

							model.setName(name);

							model.setBaseCase(AnalyticsFactory.eINSTANCE.createBaseCase());
							model.setPartialCase(AnalyticsFactory.eINSTANCE.createPartialCase());

							final CompoundCommand cmd = new CompoundCommand("Create sandbox");
							cmd.append(AddCommand.create(domain, analyticsModel, AnalyticsPackage.eINSTANCE.getAnalyticsModel_OptionModels(), Collections.singletonList(model)));
							domain.getCommandStack().execute(cmd);

							final IWorkbenchPage activePage = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();
							BusyIndicator.showWhile(HandlerUtil.getActiveShellChecked(event).getDisplay(), new Runnable() {

								@Override
								public void run() {
									final IEditorPart part = openEditor(activePage, instance);
									if (part instanceof IPartGotoTarget) {
										((IPartGotoTarget) part).gotoTarget(model);
									} else {
										final Object adapter = part.getAdapter(IPartGotoTarget.class);
										if (adapter != null) {
											((IPartGotoTarget) adapter).gotoTarget(model);
										}
									}
								}
							});

							return null;
						}

					}
				}
			}
		}
		return null;
	}

	private IEditorPart openEditor(final IWorkbenchPage activePage, final Object element) {
		try {
			return OpenScenarioUtils.openAndReturnEditorPart(activePage, (ScenarioInstance) element);
		} catch (final PartInitException e) {
			MessageDialog.openError(activePage.getWorkbenchWindow().getShell(), "Error opening editor", e.getMessage());
		}
		return null;
	}
}
