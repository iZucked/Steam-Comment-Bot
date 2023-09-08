package com.mmxlabs.models.lng.scenario.importWizards.paperdeals.fromexcel;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.AddCommand;
import org.ops4j.peaberry.Peaberry;
import org.ops4j.peaberry.eclipse.EclipseRegistry;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.BuyPaperDeal;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.SellPaperDeal;
import com.mmxlabs.models.lng.cargo.util.IExposuresCustomiser;
import com.mmxlabs.models.lng.scenario.importWizards.AbstractImportPage;
import com.mmxlabs.models.lng.scenario.importWizards.AbstractImportWizard;
import com.mmxlabs.models.lng.scenario.importWizards.paperdeals.PaperDealsImportAction;
import com.mmxlabs.models.lng.scenario.importWizards.paperdeals.fromexcel.util.DefaulPaperDealExcelExporter;
import com.mmxlabs.models.lng.scenario.importWizards.paperdeals.fromexcel.util.ExcelReader;
import com.mmxlabs.models.lng.scenario.importWizards.paperdeals.fromexcel.util.IPaperDealExporter;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.ui.actions.ImportAction;
import com.mmxlabs.models.lng.ui.actions.ImportAction.ImportHooksProvider;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;

public class ImportPaperDealsFromExcelWizard extends AbstractImportWizard {
	
	private ImportPaperDealsFromExcelPage importPage;
	
	private ScenarioInstance scenarioInstance;
	
	@Inject
	private Iterable<IPaperDealExporter> paperDealExporters;

	public ImportPaperDealsFromExcelWizard(ScenarioInstance scenarioInstance, String windowTitle) {
		super(scenarioInstance, windowTitle);
		
		this.scenarioInstance = scenarioInstance;
		final BundleContext bc = FrameworkUtil.getBundle(ImportPaperDealsFromExcelWizard.class).getBundleContext();
		final Injector injector = Guice.createInjector(Peaberry.osgiModule(bc, EclipseRegistry.eclipseRegistry()), new ImportPaperDealsFromExcelProviderModule());
		injector.injectMembers(this);
	}

	@Override
	protected AbstractImportPage getImportPage(String pageName, ScenarioInstance currentScenario) {
		importPage = new ImportPaperDealsFromExcelPage(pageName, currentScenario);
		return importPage;
	}

	@Override
	protected ImportAction getImportAction(ImportHooksProvider ihp, IScenarioDataProvider scenarioDataProvider) {
		return new PaperDealsImportAction(ihp, ScenarioModelUtil.getCargoModel(scenarioDataProvider));
	}

	@Override
	public boolean performFinish() {
		try(final FileInputStream fis = new FileInputStream(importPage.getImportFilename())){
			final ExcelReader reader = new ExcelReader(fis, importPage.getSelectedWorksheetName());
			final Iterator<IPaperDealExporter> iterPaperDealExporters = paperDealExporters.iterator();
			final IPaperDealExporter paperDealExporter = iterPaperDealExporters.hasNext() ? iterPaperDealExporters.next(): new DefaulPaperDealExcelExporter();
			
			final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenarioInstance);
	        try (final IScenarioDataProvider scenarioDataProvider = modelRecord.aquireScenarioDataProvider("ScenarioDataProvider:1")) {
	        	final Pair<List<BuyPaperDeal>, List<SellPaperDeal>> paperDealsPair = paperDealExporter.getPaperDeals(reader, ScenarioModelUtil.getScenarioModel(scenarioDataProvider));
				if (paperDealExporter != null && !paperDealsPair.getFirst().isEmpty() && !paperDealsPair.getSecond().isEmpty()) {
					CompoundCommand command = new CompoundCommand("Import paper deals from excel");
					command.append(
							AddCommand.create(scenarioDataProvider.getEditingDomain(), 
									ScenarioModelUtil.getCargoModel(scenarioDataProvider), 
									CargoPackage.Literals.CARGO_MODEL__PAPER_DEALS, 
									paperDealsPair.getFirst())
							);
					command.append(
							AddCommand.create(scenarioDataProvider.getEditingDomain(), 
									ScenarioModelUtil.getCargoModel(scenarioDataProvider),
									CargoPackage.Literals.CARGO_MODEL__PAPER_DEALS, 
									paperDealsPair.getSecond())
							);
					scenarioDataProvider.getCommandStack().execute(command);
				}
	        }
			
			
			
			
		} catch (final Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			return false;
		}
		
		
		

		return true;
	}
	
	
	
	
}
