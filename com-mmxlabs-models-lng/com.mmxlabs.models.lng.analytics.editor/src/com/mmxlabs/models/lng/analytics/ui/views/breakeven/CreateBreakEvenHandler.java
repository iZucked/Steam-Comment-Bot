/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.breakeven;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BreakEvenAnalysisModel;
import com.mmxlabs.models.lng.analytics.BreakEvenAnalysisResult;
import com.mmxlabs.models.lng.analytics.BreakEvenAnalysisResultSet;
import com.mmxlabs.models.lng.analytics.BreakEvenAnalysisRow;
import com.mmxlabs.models.lng.analytics.BuyOpportunity;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.rcp.common.editors.IPartGotoTarget;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.ModelRecord;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.ui.OpenScenarioUtils;

public class CreateBreakEvenHandler extends AbstractHandler {

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
					final ModelRecord modelRecord = SSDataManager.Instance.getModelRecord(instance);

					try (ModelReference reference = modelRecord.aquireReference("CreateBreakEvenHandler")) {
						final EObject rootObject = reference.getInstance();
						if (!(rootObject instanceof LNGScenarioModel)) {
							continue;
						}

						final @NonNull AnalyticsModel analyticsModel = ScenarioModelUtil.getAnalyticsModel((LNGScenarioModel) rootObject);

						final EditingDomain domain = reference.getEditingDomain();
						final Display display = PlatformUI.getWorkbench().getDisplay();

						// final ImageDescriptor d = AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.models.lng.analytics.editor", "icons/sandbox.gif");
						// final Image img = d.createImage();
						// final InputDialog dialog = new InputDialog(display.getActiveShell(), "Create sandbox", "Choose name for sandbox", "sandbox", null) {
						// @Override
						// protected void configureShell(final Shell shell) {
						// super.configureShell(shell);
						// shell.setImage(img);
						// }
						//
						// @Override
						// public boolean close() {
						// img.dispose();
						// return super.close();
						// }
						// };
						//
						// if (dialog.open() == Window.OK)
						{
							// final String name = dialog.getValue();
							final SpotMarketsModel spotMarketsModel = ScenarioModelUtil.getSpotMarketsModel((LNGScenarioModel) rootObject);

							final BreakEvenAnalysisModel model = AnalyticsFactory.eINSTANCE.createBreakEvenAnalysisModel();

							// DUMMY DATA
							{
								BreakEvenAnalysisRow row1 = null;
								BreakEvenAnalysisResultSet resultSet1 = null;
								BreakEvenAnalysisResultSet resultSet2 = null;
								{
									final BreakEvenAnalysisRow row = AnalyticsFactory.eINSTANCE.createBreakEvenAnalysisRow();
									final BuyOpportunity buy = AnalyticsFactory.eINSTANCE.createBuyOpportunity();

									buy.setDate(LocalDate.of(2017, 8, 22));

									row.setBuyOption(buy);

									final BreakEvenAnalysisResultSet resultSet = AnalyticsFactory.eINSTANCE.createBreakEvenAnalysisResultSet();
									resultSet1 = resultSet;
									for (final SpotMarket sm : spotMarketsModel.getDesSalesSpotMarket().getMarkets()) {
										final BreakEvenAnalysisResult result = AnalyticsFactory.eINSTANCE.createBreakEvenAnalysisResult();
										result.setTarget(sm);
										result.setEta(LocalDate.of(2017, 8, 22));
										result.setPrice(5.0);
										result.setReferencePrice(4.5);

										resultSet.getResults().add(result);
									}
									row.getRhsResults().add(resultSet);

									model.getRows().add(row);
									row1 = row;
								}
								{
									final BreakEvenAnalysisRow row = AnalyticsFactory.eINSTANCE.createBreakEvenAnalysisRow();
									final BuyOpportunity buy = AnalyticsFactory.eINSTANCE.createBuyOpportunity();

									buy.setDate(LocalDate.of(2017, 8, 22));

									row.setBuyOption(buy);

									final BreakEvenAnalysisResultSet resultSet = AnalyticsFactory.eINSTANCE.createBreakEvenAnalysisResultSet();
									resultSet2 = resultSet;
									for (final SpotMarket sm : spotMarketsModel.getDesSalesSpotMarket().getMarkets()) {
										final BreakEvenAnalysisResult result = AnalyticsFactory.eINSTANCE.createBreakEvenAnalysisResult();
										result.setTarget(sm);
										result.setEta(LocalDate.of(2017, 8, 22));
										result.setPrice(5.0);
										result.setReferencePrice(4.5);

										resultSet.getResults().add(result);
									}
									row.getRhsResults().add(resultSet);

									model.getRows().add(row);
								}

								for (final BreakEvenAnalysisResult r : resultSet2.getResults()) {
									final BreakEvenAnalysisResultSet resultSet = AnalyticsFactory.eINSTANCE.createBreakEvenAnalysisResultSet();
									for (final SpotMarket sm : spotMarketsModel.getDesSalesSpotMarket().getMarkets()) {
										final BreakEvenAnalysisResult result = AnalyticsFactory.eINSTANCE.createBreakEvenAnalysisResult();
										result.setTarget(sm);
										result.setEta(LocalDate.of(2017, 8, 22));
										result.setPrice(5.0 + new Random().nextDouble());
										result.setReferencePrice(4.5);

										resultSet.getResults().add(result);
									}
									resultSet.setBasedOn(r);
									row1.getRhsResults().add(resultSet);
								}

								row1.setRhsBasedOn(resultSet2.getResults().get(1));

							}

							// model.setName(name);

							// model.setBaseCase(AnalyticsFactory.eINSTANCE.createBaseCase());
							// model.setPartialCase(AnalyticsFactory.eINSTANCE.createPartialCase());

							final CompoundCommand cmd = new CompoundCommand("Create b/e sandbox");
							cmd.append(AddCommand.create(domain, analyticsModel, AnalyticsPackage.eINSTANCE.getAnalyticsModel_BreakevenModels(), Collections.singletonList(model)));
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
