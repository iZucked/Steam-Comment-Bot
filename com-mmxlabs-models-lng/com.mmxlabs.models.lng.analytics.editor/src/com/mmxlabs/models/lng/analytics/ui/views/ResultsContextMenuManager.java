package com.mmxlabs.models.lng.analytics.ui.views;

import java.util.Collection;
import java.util.LinkedList;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Menu;

import com.mmxlabs.models.lng.analytics.AnalysisResultRow;
import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BaseCase;
import com.mmxlabs.models.lng.analytics.BaseCaseRow;
import com.mmxlabs.models.lng.analytics.BreakEvenResult;
import com.mmxlabs.models.lng.analytics.BuyOpportunity;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.BuyReference;
import com.mmxlabs.models.lng.analytics.FleetShippingOption;
import com.mmxlabs.models.lng.analytics.NominatedShippingOption;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.PartialCaseRow;
import com.mmxlabs.models.lng.analytics.ResultSet;
import com.mmxlabs.models.lng.analytics.RoundTripShippingOption;
import com.mmxlabs.models.lng.analytics.SellOpportunity;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.SellReference;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.AnalyticsBuilder;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.BaseCaseEvaluator;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.AnalyticsBuilder.ShippingType;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialogUtil;
import com.mmxlabs.rcp.common.actions.RunnableAction;
import com.mmxlabs.scenario.service.model.util.*;
import com.mmxlabs.scenario.service.ui.ScenarioServiceModelUtils;
public class ResultsContextMenuManager implements MenuDetectListener {

	private final @NonNull GridTreeViewer viewer;
	private final @NonNull IScenarioEditingLocation scenarioEditingLocation;

	private final @NonNull MenuManager mgr;
	private OptionAnalysisModel optionAnalysisModel;
	private Menu menu;

	public ResultsContextMenuManager(@NonNull final GridTreeViewer viewer, @NonNull final IScenarioEditingLocation scenarioEditingLocation, @NonNull final MenuManager mgr) {
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
		mgr.removeAll();

		final Point mousePoint = grid.toControl(new Point(e.x, e.y));
		final GridColumn column = grid.getColumn(mousePoint);

		final IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
		final GridItem[] items = grid.getSelection();
		if (items.length > 0) {

			mgr.add(new RunnableAction("Create fork", () -> {
				Object selectedElement = selection.getFirstElement();
				ResultSet resultSet = null;
				if (selectedElement instanceof ResultSet) {
					resultSet = (ResultSet) selectedElement;
				} else if (selectedElement instanceof AnalysisResultRow) {
					EObject eContainer = ((AnalysisResultRow) selectedElement).eContainer();
					if (eContainer instanceof ResultSet) {
						resultSet = (ResultSet) eContainer;
					}
				}
				if (resultSet != null) {
					BaseCase bc = createBaseCaseFromRS((ResultSet) selectedElement);
					String newForkName = ScenarioServiceModelUtils.getNewForkName(scenarioEditingLocation.getScenarioInstance(), false);
					OptionAnalysisModel optionAnalysisModel = AnalyticsFactory.eINSTANCE.createOptionAnalysisModel();
					optionAnalysisModel.setBaseCase(bc);
					optionAnalysisModel.getBuys().addAll(bc.getBaseCase().stream().map(b -> b.getBuyOption()).collect(Collectors.toList()));
					optionAnalysisModel.getSells().addAll(bc.getBaseCase().stream().map(b -> b.getSellOption()).collect(Collectors.toList()));
					BaseCaseEvaluator.evaluate(scenarioEditingLocation, optionAnalysisModel, bc, true, newForkName);
				}
//				ScenarioServiceModelUtils.openNewNameForForkPrompt(scenarioEditingLocation.getScenarioInstance().getName(), "~"+scenarioEditingLocation.getScenarioInstance().getName(), existingNames);
//				selection.iterator().forEachRemaining(ee -> c.add((EObject) ee));
//
//				scenarioEditingLocation.getDefaultCommandHandler().handleCommand(DeleteCommand.create(scenarioEditingLocation.getEditingDomain(), c), null, null);

			}));

			mgr.add(new RunnableAction("Copy to What if?", () -> {
				final Collection<EObject> c = new LinkedList<>();
				selection.iterator().forEachRemaining(ee -> {
					if (ee instanceof BaseCaseRow) {
						BaseCaseRow baseCaseRow = (BaseCaseRow) ee;
						PartialCaseRow partialCaseRow = AnalyticsFactory.eINSTANCE.createPartialCaseRow();
						if (baseCaseRow.getBuyOption() != null) {
							partialCaseRow.getBuyOptions().add(baseCaseRow.getBuyOption());
						}
						if (baseCaseRow.getSellOption() != null) {
							partialCaseRow.getSellOptions().add(baseCaseRow.getSellOption());
						}
						if (baseCaseRow.getShipping() != null) {
							partialCaseRow.setShipping(EcoreUtil.copy(baseCaseRow.getShipping()));
						}
						if (!(partialCaseRow.getBuyOptions().isEmpty() && partialCaseRow.getSellOptions().isEmpty() && partialCaseRow.getShipping() == null)) {
							c.add(partialCaseRow);
						}
					}
				});

				if (!c.isEmpty()) {
					scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
							AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel.getPartialCase(), AnalyticsPackage.Literals.PARTIAL_CASE__PARTIAL_CASE, c),
							optionAnalysisModel.getPartialCase(), AnalyticsPackage.Literals.PARTIAL_CASE__PARTIAL_CASE);
				}
			}));
		}
		menu.setVisible(true);
	}

	public OptionAnalysisModel getOptionAnalysisModel() {
		return optionAnalysisModel;
	}

	public void setOptionAnalysisModel(OptionAnalysisModel optionAnalysisModel) {
		this.optionAnalysisModel = optionAnalysisModel;
	}
	
	private BaseCase createBaseCaseFromRS(ResultSet rs) {
		BaseCase bc = AnalyticsFactory.eINSTANCE.createBaseCase();
		EList<BaseCaseRow> baseCase = bc.getBaseCase();
		for (AnalysisResultRow analysisResultRow : rs.getRows()) {
			BaseCaseRow bcr = AnalyticsFactory.eINSTANCE.createBaseCaseRow();
			bcr.setBuyOption(getFixedBuyOption(analysisResultRow));
			bcr.setSellOption(getFixedSellOption(analysisResultRow));
			bcr.setShipping(EcoreUtil.copy(analysisResultRow.getShipping()));
			baseCase.add(bcr);
		}
		return bc;
	}
	
	private BuyOption getFixedBuyOption(AnalysisResultRow row) {
		BuyOption buyOption = row.getBuyOption();
		if (row.getResultDetail() instanceof BreakEvenResult) {
			BreakEvenResult result = (BreakEvenResult) row.getResultDetail();
			if (buyOption instanceof BuyOpportunity) {
				if (((BuyOpportunity) buyOption).getPriceExpression().contains("?")) {
					BuyOpportunity copy = (BuyOpportunity) EcoreUtil.copy(buyOption);
					copy.setPriceExpression(""+result.getPrice());
					return copy;
				}
			} else if (buyOption instanceof BuyReference) {
				if (((BuyReference) buyOption).getSlot().getPriceExpression().contains("?")) {
					LoadSlot slotCopy = EcoreUtil.copy((LoadSlot) ((BuyReference) buyOption).getSlot());
					BuyReference copy = AnalyticsFactory.eINSTANCE.createBuyReference();
					slotCopy.setPriceExpression(""+result.getPrice());
					copy.setSlot(slotCopy);
					return copy;
				} else {
					LoadSlot slotCopy = EcoreUtil.copy((LoadSlot) ((BuyReference) buyOption).getSlot());
					BuyReference copy = AnalyticsFactory.eINSTANCE.createBuyReference();
					copy.setSlot(slotCopy);
					return copy;
				}
			}
		} else {
			if (buyOption instanceof BuyReference) {
					LoadSlot slotCopy = EcoreUtil.copy((LoadSlot) ((BuyReference) buyOption).getSlot());
					BuyReference copy = AnalyticsFactory.eINSTANCE.createBuyReference();
					copy.setSlot(slotCopy);
					return copy;
			} else if (buyOption instanceof BuyOpportunity) {
//				BuyOpportunity copy = AnalyticsFactory.eINSTANCE.createBuyOpportunity();
//				copy.setContract(((BuyOpportunity) buyOption).getContract());
//				copy.setPort(((BuyOpportunity) buyOption).getPort());
//				copy.setPriceExpression(((BuyOpportunity) buyOption).getPriceExpression());
//				copy.setDate(((BuyOpportunity) buyOption).getDate());
//				copy.setCv(((BuyOpportunity) buyOption).getCv());
//				copy.setEntity(((BuyOpportunity) buyOption).getEntity());
//				return copy;
			}
		}
		return EcoreUtil.copy(row.getBuyOption());
	}

	private SellOption getFixedSellOption(AnalysisResultRow row) {
		SellOption sellOption = row.getSellOption();
		if (row.getResultDetail() instanceof BreakEvenResult) {
			BreakEvenResult result = (BreakEvenResult) row.getResultDetail();
			if (sellOption instanceof BuyOpportunity) {
				if (((SellOpportunity) sellOption).getPriceExpression().contains("?")) {
					SellOpportunity copy = (SellOpportunity) EcoreUtil.copy(sellOption);
					copy.setPriceExpression(""+result.getPrice());
					return copy;
				}
			} else if (sellOption instanceof SellReference) {
				if (((SellReference) sellOption).getSlot().getPriceExpression().contains("?")) {
					DischargeSlot slotCopy = EcoreUtil.copy((DischargeSlot) ((SellReference) sellOption).getSlot());
					SellReference copy = AnalyticsFactory.eINSTANCE.createSellReference();
					slotCopy.setPriceExpression(""+result.getPrice());
					copy.setSlot(slotCopy);
					return copy;
				} else {
					DischargeSlot slotCopy = EcoreUtil.copy((DischargeSlot) ((SellReference) sellOption).getSlot());
					SellReference copy = AnalyticsFactory.eINSTANCE.createSellReference();
					copy.setSlot(slotCopy);
					return copy;
				}
			}
		} else {
			if (sellOption instanceof SellReference) {
					DischargeSlot slotCopy = EcoreUtil.copy((DischargeSlot) ((SellReference) sellOption).getSlot());
					SellReference copy = AnalyticsFactory.eINSTANCE.createSellReference();
					copy.setSlot(slotCopy);
					return copy;
				} else if (sellOption instanceof SellOpportunity) {
//					SellOpportunity copy = AnalyticsFactory.eINSTANCE.createSellOpportunity();
//					copy.setContract(((SellOpportunity) sellOption).getContract());
//					copy.setPort(((SellOpportunity) sellOption).getPort());
//					copy.setPriceExpression(((SellOpportunity) sellOption).getPriceExpression());
//					copy.setDate(((SellOpportunity) sellOption).getDate());
//					copy.setEntity(((SellOpportunity) sellOption).getEntity());
//					return copy;
				}
		}
		return EcoreUtil.copy(row.getSellOption());
	}

}
