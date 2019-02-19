package com.mmxlabs.models.lng.cargo.editor.risk;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.lng.ui.views.MultiScenarioTableViewersView;

public class DealSetTableEditorView extends MultiScenarioTableViewersView{

	private final EReference[][] rootPaths = new EReference[][] {
		new EReference[] { LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_CargoModel(), CargoPackage.eINSTANCE.getCargoModel_DealSets() },
		new EReference[] { LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_CargoModel(), CargoPackage.eINSTANCE.getCargoModel_PaperDeals() },
		new EReference[] { LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_CargoModel(), CargoPackage.eINSTANCE.getCargoModel_Cargoes() } };
	
	private DealSetsPane dealSetsPane;
	private CustomPaperDealsPane papersPane;
	private CustomTradeDealsPane tradesPane;
	
	@Override
	protected EReference[][] getPaneRootPaths() {
		return rootPaths;
	}

	@Override
	protected List<ScenarioTableViewerPane> createViewerPanes() {
		final List<ScenarioTableViewerPane> result = new ArrayList<>();
		
		dealSetsPane = new DealSetsPane(getSite().getPage(), this, this, getViewSite().getActionBars());
		papersPane = new CustomPaperDealsPane(getSite().getPage(), this, this, getViewSite().getActionBars());
		tradesPane = new CustomTradeDealsPane(getSite().getPage(), this, this, getViewSite().getActionBars());
		
		result.add(dealSetsPane);
		result.add(papersPane);
		result.add(tradesPane);
		return result;
	}

	@Override
	public com.mmxlabs.models.ui.validation.IExtraValidationContext getExtraValidationContext() {
		return super.getExtraValidationContext();
	}

	@Override
	public com.mmxlabs.models.ui.validation.IStatusProvider getStatusProvider() {
		return super.getStatusProvider();
	}

}
