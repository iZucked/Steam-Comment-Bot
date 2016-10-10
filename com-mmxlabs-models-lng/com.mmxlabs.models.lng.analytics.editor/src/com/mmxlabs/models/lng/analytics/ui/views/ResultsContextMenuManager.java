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
	private final @NonNull GridTreeViewer baseCaseViewer;

	private final @NonNull IScenarioEditingLocation scenarioEditingLocation;

	private final @NonNull MenuManager mgr;
	private OptionAnalysisModel optionAnalysisModel;
	private Menu menu;

	public ResultsContextMenuManager(@NonNull final GridTreeViewer viewer, @NonNull final GridTreeViewer baseCaseViewer, @NonNull final IScenarioEditingLocation scenarioEditingLocation, @NonNull final MenuManager mgr) {
		this.baseCaseViewer = baseCaseViewer;
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

			Object selectedElement = selection.getFirstElement();
			ResultSet resultSet;
			if (selectedElement instanceof ResultSet) {
				resultSet = (ResultSet) selectedElement;
			} else if (selectedElement instanceof AnalysisResultRow) {
				EObject eContainer = ((AnalysisResultRow) selectedElement).eContainer();
				if (eContainer instanceof ResultSet) {
					resultSet = (ResultSet) eContainer;
				} else {
					resultSet = null;
				}
			} else {
				resultSet = null;
			}
			mgr.add(new RunnableAction("Create fork", () -> {
				if (resultSet != null) {
					BaseCase bc = createBaseCaseFromRS(resultSet, true);
					String newForkName = ScenarioServiceModelUtils.getNewForkName(scenarioEditingLocation.getScenarioInstance(), false);
					OptionAnalysisModel optionAnalysisModel2 = AnalyticsFactory.eINSTANCE.createOptionAnalysisModel();
					optionAnalysisModel2.setBaseCase(bc);
					optionAnalysisModel2.getBuys().addAll(bc.getBaseCase().stream().map(b -> b.getBuyOption()).collect(Collectors.toList()));
					optionAnalysisModel2.getSells().addAll(bc.getBaseCase().stream().map(b -> b.getSellOption()).collect(Collectors.toList()));
					BaseCaseEvaluator.evaluate(scenarioEditingLocation, optionAnalysisModel2, bc, true, newForkName);
				}
			}));
			
			mgr.add(new RunnableAction("Convert to base case", () -> {
				if (resultSet != null) {
					BaseCase bc = updateBaseCaseFromRS(resultSet, optionAnalysisModel.getBaseCase() != null ? optionAnalysisModel.getBaseCase() : AnalyticsFactory.eINSTANCE.createBaseCase());
					optionAnalysisModel.setBaseCase(bc);
					// add new slots
					optionAnalysisModel.getBuys().addAll(bc.getBaseCase().stream().filter(b -> !optionAnalysisModel.getBuys().contains(b.getBuyOption())).map(b -> b.getBuyOption()).collect(Collectors.toList()));
					optionAnalysisModel.getSells().addAll(bc.getBaseCase().stream().filter(b -> !optionAnalysisModel.getSells().contains(b.getBuyOption())).map(b -> b.getSellOption()).collect(Collectors.toList()));
					baseCaseViewer.refresh();
					baseCaseViewer.notifyAll();
					baseCaseViewer.notify();
					baseCaseViewer.expandAll();
					baseCaseViewer.refresh();
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
	
	private BaseCase createBaseCaseFromRS(ResultSet rs, boolean copy) {
		BaseCase bc = AnalyticsFactory.eINSTANCE.createBaseCase();
		EList<BaseCaseRow> baseCase = bc.getBaseCase();
		for (AnalysisResultRow analysisResultRow : rs.getRows()) {
			BaseCaseRow bcr = AnalyticsFactory.eINSTANCE.createBaseCaseRow();
			bcr.setBuyOption(getFixedBuyOption(analysisResultRow, copy));
			bcr.setSellOption(getFixedSellOption(analysisResultRow, copy));
//			bcr.setShipping(copy ? EcoreUtil.copy(analysisResultRow.getShipping()) : analysisResultRow.getShipping());
			bcr.setShipping(EcoreUtil.copy(analysisResultRow.getShipping()));
			baseCase.add(bcr);
		}
		return bc;
	}

	private BaseCase updateBaseCaseFromRS(ResultSet rs, BaseCase bc) {
		EList<BaseCaseRow> baseCase = bc.getBaseCase();
		baseCase.clear();
		for (AnalysisResultRow analysisResultRow : rs.getRows()) {
			BaseCaseRow bcr = AnalyticsFactory.eINSTANCE.createBaseCaseRow();
			bcr.setBuyOption(getFixedBuyOption(analysisResultRow, false));
			bcr.setSellOption(getFixedSellOption(analysisResultRow, false));
			bcr.setShipping(EcoreUtil.copy(analysisResultRow.getShipping()));
			baseCase.add(bcr);
		}
		return bc;
	}
	
	private BuyOption getFixedBuyOption(AnalysisResultRow row, boolean createCopy) {
		BuyOption buyOption = row.getBuyOption();
		if (row.getResultDetail() instanceof BreakEvenResult) {
			BreakEvenResult result = (BreakEvenResult) row.getResultDetail();
			if (buyOption instanceof BuyOpportunity) {
				if (((BuyOpportunity) buyOption).getPriceExpression().contains("?")) {
					BuyOpportunity buyOpportunity;
					if (createCopy) {
						buyOpportunity = (BuyOpportunity) EcoreUtil.copy(buyOption);
					} else {
						buyOpportunity = (BuyOpportunity) buyOption;
					}
					buyOpportunity.setPriceExpression(""+result.getPrice());
					return buyOpportunity;
				}
			} else if (buyOption instanceof BuyReference) {
				if (((BuyReference) buyOption).getSlot().getPriceExpression().contains("?")) {
//					LoadSlot slotCopy = EcoreUtil.copy((LoadSlot) ((BuyReference) buyOption).getSlot());
//					BuyReference copy = AnalyticsFactory.eINSTANCE.createBuyReference();
//					slotCopy.setPriceExpression(""+result.getPrice());
//					copy.setSlot(slotCopy);
//					return copy;
				}
			}
		}
		if (createCopy) {
			return EcoreUtil.copy(row.getBuyOption());
		} else {
			return row.getBuyOption();
		}
	}

	private SellOption getFixedSellOption(AnalysisResultRow row, boolean createCopy) {
		SellOption sellOption = row.getSellOption();
		if (row.getResultDetail() instanceof BreakEvenResult) {
			BreakEvenResult result = (BreakEvenResult) row.getResultDetail();
			if (sellOption instanceof BuyOpportunity) {
				if (((SellOpportunity) sellOption).getPriceExpression().contains("?")) {
					SellOpportunity opportunity;
					if (createCopy) {
						opportunity = (SellOpportunity) EcoreUtil.copy(sellOption);
					} else {
						opportunity = (SellOpportunity) sellOption;
					}
					opportunity.setPriceExpression(""+result.getPrice());
					return opportunity;
				}
			} else if (sellOption instanceof SellReference) {
				if (((SellReference) sellOption).getSlot().getPriceExpression().contains("?")) {
//					DischargeSlot slotCopy = EcoreUtil.copy((DischargeSlot) ((SellReference) sellOption).getSlot());
//					SellReference copy = AnalyticsFactory.eINSTANCE.createSellReference();
//					slotCopy.setPriceExpression(""+result.getPrice());
//					copy.setSlot(slotCopy);
//					return copy;
				}
			}
		}
		if (createCopy) {
			return EcoreUtil.copy(sellOption);
		} else {
			return sellOption;
		}
	}

}
