/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.valuematrix;

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
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
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

import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BuyMarket;
import com.mmxlabs.models.lng.analytics.BuyReference;
import com.mmxlabs.models.lng.analytics.ExistingVesselCharterOption;
import com.mmxlabs.models.lng.analytics.SellMarket;
import com.mmxlabs.models.lng.analytics.SellReference;
import com.mmxlabs.models.lng.analytics.SwapValueMatrixModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconMode;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.ModelRecord;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.ui.OpenScenarioUtils;

public class CreateValueMatrixHandler extends AbstractHandler {

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {

		final ISelection selection = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().getSelection();

		if ((selection != null) && (selection instanceof @NonNull final IStructuredSelection structuredSelection)) {
			final Iterator<?> itr = structuredSelection.iterator();
			while (itr.hasNext()) {
				final Object obj = itr.next();
				if (obj instanceof @NonNull final ScenarioInstance instance) {
					final ModelRecord modelRecord = SSDataManager.Instance.getModelRecord(instance);

					try (ModelReference reference = modelRecord.aquireReference("CreateValueMatrixHandler")) {
						final EObject rootObject = reference.getInstance();
						if (!(rootObject instanceof LNGScenarioModel)) {
							continue;
						}

						@NonNull
						final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;

						@NonNull
						final AnalyticsModel analyticsModel = ScenarioModelUtil.getAnalyticsModel((LNGScenarioModel) rootObject);

						@Nullable
						final ADPModel adpModel = ScenarioModelUtil.getADPModel(lngScenarioModel);
						if (adpModel != null) {
							MessageDialog.openError(HandlerUtil.getActiveWorkbenchWindow(event).getShell(), "Unable to create", "Value matrix analysis cannot be use with ADP scenarios");
							continue;
						}

						final EditingDomain domain = reference.getEditingDomain();
						final Display display = PlatformUI.getWorkbench().getDisplay();

						final Image img = CommonImages.getImage(IconPaths.Sandbox, IconMode.Enabled);
						final InputDialog dialog = new InputDialog(display.getActiveShell(), "Create value matrix", "Choose name for value matrix", "value matrix", null) {
							@Override
							protected void configureShell(final Shell shell) {
								super.configureShell(shell);
								shell.setImage(img);
							}
						};
						if (dialog.open() == Window.OK) {
							final String name = dialog.getValue();
						
							final SwapValueMatrixModel model = AnalyticsFactory.eINSTANCE.createSwapValueMatrixModel();
							model.setName(name);
							final BuyReference buyReference = AnalyticsFactory.eINSTANCE.createBuyReference();
							final SellReference sellReference = AnalyticsFactory.eINSTANCE.createSellReference();
							final ExistingVesselCharterOption vesselCharterReference = AnalyticsFactory.eINSTANCE.createExistingVesselCharterOption();
							final BuyMarket buyMarket = AnalyticsFactory.eINSTANCE.createBuyMarket();
							final SellMarket sellMarket = AnalyticsFactory.eINSTANCE.createSellMarket();
							model.setBaseLoad(buyReference);
							model.setBaseDischarge(sellReference);
							model.setSwapLoadMarket(buyMarket);
							model.setSwapDischargeMarket(sellMarket);
							model.setBaseVesselCharter(vesselCharterReference);
							
							final CompoundCommand cmd = new CompoundCommand("Create value matrix");
							cmd.append(AddCommand.create(domain, analyticsModel, AnalyticsPackage.eINSTANCE.getAnalyticsModel_SwapValueMatrixModels(), Collections.singletonList(model)));
							
							domain.getCommandStack().execute(cmd);

							final IWorkbenchPage activePage = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();
							BusyIndicator.showWhile(HandlerUtil.getActiveShellChecked(event).getDisplay(), new Runnable() {

								@Override
								public void run() {
									final IEditorPart part = openEditor(activePage, instance);
									ValueMatrixScenario.open(instance, model);
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
