/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.EcoreUtil.Copier;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.widgets.Menu;

import com.mmxlabs.models.lng.analytics.AnalysisResultDetail;
import com.mmxlabs.models.lng.analytics.AnalysisResultRow;
import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BaseCase;
import com.mmxlabs.models.lng.analytics.BaseCaseRow;
import com.mmxlabs.models.lng.analytics.BreakEvenResult;
import com.mmxlabs.models.lng.analytics.BuyOpportunity;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.BuyReference;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.ProfitAndLossResult;
import com.mmxlabs.models.lng.analytics.Result;
import com.mmxlabs.models.lng.analytics.ResultSet;
import com.mmxlabs.models.lng.analytics.SellOpportunity;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.SellReference;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.analytics.services.IAnalyticsScenarioEvaluator;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.rcp.common.actions.RunnableAction;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.ui.ScenarioResult;
import com.mmxlabs.scenario.service.ui.ScenarioServiceModelUtils;

public class ResultsContextMenuManager implements MenuDetectListener {

	private final @NonNull GridTreeViewer viewer;

	private final @NonNull IScenarioEditingLocation scenarioEditingLocation;
	private final @NonNull OptionModellerView optionModellerView;

	private final @NonNull MenuManager mgr;
	private OptionAnalysisModel optionAnalysisModel;
	private Menu menu;

	public ResultsContextMenuManager(@NonNull final GridTreeViewer viewer, @NonNull final IScenarioEditingLocation scenarioEditingLocation, @NonNull final OptionModellerView optionModellerView,
			@NonNull final MenuManager mgr) {
		this.optionModellerView = optionModellerView;
		this.mgr = mgr;
		this.scenarioEditingLocation = scenarioEditingLocation;
		this.viewer = viewer;
	}

	@SuppressWarnings({ "unchecked", "null" })
	@Override
	public void menuDetected(final MenuDetectEvent e) {

		final Grid grid = viewer.getGrid();
		if (menu == null) {
			menu = mgr.createContextMenu(grid);
		}
		{
			IContributionItem[] items = mgr.getItems();
			mgr.removeAll();
			for (IContributionItem item : items) {
				item.dispose();
			}
		}
		final IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
		final GridItem[] items = grid.getSelection();
		if (items.length > 0) {

			final Object selectedElement = selection.getFirstElement();
			ResultSet resultSet;
			if (selectedElement instanceof ResultSet) {
				resultSet = (ResultSet) selectedElement;
			} else if (selectedElement instanceof AnalysisResultRow) {
				final EObject eContainer = ((AnalysisResultRow) selectedElement).eContainer();
				if (eContainer instanceof ResultSet) {
					resultSet = (ResultSet) eContainer;
				} else {
					resultSet = null;
				}
			} else {
				resultSet = null;
			}

			if (false) {
				// Experimental code to generation and *OLD* style insertion plan. TODO: This should be a single scenario now.
				// TODO: Should make into a new type of object similar to the insertion plan explicitly for sandboxes.
				mgr.add(new RunnableAction("Generate suggestions", () -> {
					if (resultSet != null) {
						try {
							ScenarioInstance parent = scenarioEditingLocation.getScenarioInstance();
							final String newForkName = ScenarioServiceModelUtils.getNewForkName(parent, false);
							if (newForkName != null) {
								final IScenarioService scenarioService = SSDataManager.Instance.findScenarioService(parent);
								// Create place-holder parent
								ScenarioInstance fork = ScenarioServiceModelUtils.fork(parent, newForkName, new NullProgressMonitor());
								// Create a base case scenario
								{
									// BaseCaseEvaluator.evaluate(scenarioEditingLocation, optionAnalysisModel, optionAnalysisModel.getBaseCase(), true, "InsertionPlan-base", fork);
								}

								// Generate child result.
								int idx = 1;
								Result result = optionAnalysisModel.getResults();
								if (result != null) {
									for (ResultSet rs : result.getResultSets()) {
										final OptionAnalysisModel newModel = createNewBaseCase(rs);
										// BaseCaseEvaluator.evaluate(scenarioEditingLocation, newModel, newModel.getBaseCase(), true, String.format("InsertionPlan-%d", idx++), fork);
									}
								}
							}
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						// }
					}
				}));
			}

			mgr.add(new RunnableAction("Create fork", () -> {
				if (resultSet != null) {
					ScenarioResult scenarioResult = new ScenarioResult(scenarioEditingLocation.getScenarioInstance(), resultSet.getScheduleModel());
					ServiceHelper.<IAnalyticsScenarioEvaluator> withServiceConsumer(IAnalyticsScenarioEvaluator.class, evaluator -> evaluator.exportResult(scenarioResult, "Sandbox"));

				}
			}));

			mgr.add(new RunnableAction("Convert to new sandbox", () -> {
				if (resultSet != null) {
					final OptionAnalysisModel newModel = createNewBaseCase(resultSet);
					CompoundCommand cmd = new CompoundCommand("Convert to new sandbox");
					cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__CHILDREN,
							Collections.singletonList(newModel)));
					scenarioEditingLocation.getDefaultCommandHandler().handleCommand(cmd, optionAnalysisModel, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__CHILDREN);
					optionModellerView.setInput(newModel);
				}
			}));

		}
		menu.setVisible(true);
	}

	private OptionAnalysisModel createNewBaseCase(final ResultSet resultSet) {
		final OptionAnalysisModel newModel = AnalyticsFactory.eINSTANCE.createOptionAnalysisModel();

		final Copier copier = new Copier();
		// create a new model copy
		newModel.getBuys().addAll(copier.copyAll(optionAnalysisModel.getBuys()));
		newModel.getSells().addAll(copier.copyAll(optionAnalysisModel.getSells()));
		newModel.getShippingTemplates().addAll(copier.copyAll(optionAnalysisModel.getShippingTemplates()));
		newModel.setPartialCase(AnalyticsFactory.eINSTANCE.createPartialCase());
		// newModel.setPartialCase((PartialCase) copier.copy(optionAnalysisModel.getPartialCase()));
		// newModel.getResultSets().addAll(copier.copyAll(optionAnalysisModel.getResultSets()));
		newModel.setUseTargetPNL(optionAnalysisModel.isUseTargetPNL());
		copier.copyReferences();
		newModel.setBaseCase(createBaseCaseFromRS(resultSet, false, copier));

		// create a name from description
		final String name = createNameFromRow(resultSet);
		newModel.setName(name);

		// add new slots
		// newModel.getBuys().addAll(newModel.getBaseCase().getBaseCase().stream().filter(b -> !newModel.getBuys().contains(b.getBuyOption())).filter(b-> b.getBuyOption() !=null).map(b ->
		// b.getBuyOption()).collect(Collectors.toList()));
		// newModel.getSells().addAll(newModel.getBaseCase().getBaseCase().stream().filter(b -> !newModel.getSells().contains(b.getSellOption())).filter(b-> b.getSellOption() !=null).map(b ->
		// b.getSellOption()).collect(Collectors.toList()));
		return newModel;
	}

	private String createNameFromRow(final ResultSet resultSet) {
		final EList<AnalysisResultRow> rows = resultSet.getRows();
		final List<String> s = new LinkedList<>();
		for (final AnalysisResultRow analysisResultRow : rows) {
			final AnalysisResultDetail resultDetail = analysisResultRow.getResultDetail();
			if (resultDetail instanceof BreakEvenResult) {
				s.add(String.format("b/e %.2f", ((BreakEvenResult) resultDetail).getPrice()));
			} else if (resultDetail instanceof ProfitAndLossResult) {
				s.add(String.format("prof %d", (long) ((ProfitAndLossResult) resultDetail).getValue()));
			}
		}
		return String.join(" ", s);
	}

	public OptionAnalysisModel getOptionAnalysisModel() {
		return optionAnalysisModel;
	}

	public void setOptionAnalysisModel(final OptionAnalysisModel optionAnalysisModel) {
		this.optionAnalysisModel = optionAnalysisModel;
	}

	private BaseCase createBaseCaseFromRS(final ResultSet rs, final boolean copy, final Copier copier) {
		final BaseCase newBaseCase = AnalyticsFactory.eINSTANCE.createBaseCase();
		final EList<BaseCaseRow> baseCaseRows = newBaseCase.getBaseCase();
		for (final AnalysisResultRow analysisResultRow : rs.getRows()) {
			final BaseCaseRow bcr = AnalyticsFactory.eINSTANCE.createBaseCaseRow();
			bcr.setBuyOption(getFixedBuyOption(analysisResultRow, copy, copier));
			bcr.setSellOption(getFixedSellOption(analysisResultRow, copy, copier));
			bcr.setShipping(copier == null ? analysisResultRow.getShipping() : (ShippingOption) copier.get(analysisResultRow.getShipping()));
			baseCaseRows.add(bcr);
		}
		return newBaseCase;
	}

	private BuyOption getFixedBuyOption(final AnalysisResultRow row, final boolean createCopy, final Copier copier) {
		final BuyOption buyOption = copier != null ? (BuyOption) copier.get(row.getBuyOption()) : row.getBuyOption();
		if (row.getResultDetail() instanceof BreakEvenResult) {
			final BreakEvenResult result = (BreakEvenResult) row.getResultDetail();
			if (buyOption instanceof BuyOpportunity) {
				if (((BuyOpportunity) buyOption).getPriceExpression().contains("?")) {
					BuyOpportunity buyOpportunity;
					if (createCopy) {
						buyOpportunity = (BuyOpportunity) EcoreUtil.copy(buyOption);
					} else {
						buyOpportunity = (BuyOpportunity) buyOption;
					}
					buyOpportunity.setPriceExpression("" + result.getPrice());
					return buyOpportunity;
				}
			} else if (buyOption instanceof BuyReference) {
				// if (((BuyReference) buyOption).getSlot().getPriceExpression() != null && ((BuyReference) buyOption).getSlot().getPriceExpression().contains("?")) {
				// LoadSlot slotCopy = EcoreUtil.copy((LoadSlot) ((BuyReference) buyOption).getSlot());
				// BuyReference copy = AnalyticsFactory.eINSTANCE.createBuyReference();
				// slotCopy.setPriceExpression(""+result.getPrice());
				// copy.setSlot(slotCopy);
				// return copy;
				// }
			}
		}
		if (createCopy) {
			return EcoreUtil.copy(row.getBuyOption());
		} else {
			return buyOption;
		}
	}

	private SellOption getFixedSellOption(final AnalysisResultRow row, final boolean createCopy, final Copier copier) {
		final SellOption sellOption = copier != null ? (SellOption) copier.get(row.getSellOption()) : row.getSellOption();
		if (row.getResultDetail() instanceof BreakEvenResult) {
			final BreakEvenResult result = (BreakEvenResult) row.getResultDetail();
			if (sellOption instanceof SellOpportunity) {
				if (((SellOpportunity) sellOption).getPriceExpression().contains("?")) {
					SellOpportunity opportunity;
					if (createCopy) {
						opportunity = (SellOpportunity) EcoreUtil.copy(sellOption);
					} else {
						opportunity = (SellOpportunity) sellOption;
					}
					opportunity.setPriceExpression("" + result.getPrice());
					return opportunity;
				}
			} else if (sellOption instanceof SellReference) {
				// if (((SellReference) sellOption).getSlot().getPriceExpression().contains("?")) {
				// // DischargeSlot slotCopy = EcoreUtil.copy((DischargeSlot) ((SellReference) sellOption).getSlot());
				// // SellReference copy = AnalyticsFactory.eINSTANCE.createSellReference();
				// // slotCopy.setPriceExpression(""+result.getPrice());
				// // copy.setSlot(slotCopy);
				// // return copy;
				// }
			}
		}
		if (createCopy) {
			return EcoreUtil.copy(sellOption);
		} else {
			return sellOption;
		}
	}

}
