package com.mmxlabs.models.lng.scenario.importWizards.paperdeals.fromexcel;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.ops4j.peaberry.Peaberry;
import org.ops4j.peaberry.eclipse.EclipseRegistry;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.BuyPaperDeal;
import com.mmxlabs.models.lng.cargo.SellPaperDeal;
import com.mmxlabs.models.lng.cargo.util.DefaulPaperDealExcelExporter;
import com.mmxlabs.models.lng.cargo.util.IExposuresCustomiser;
import com.mmxlabs.models.lng.cargo.util.IPaperDealExporter;
import com.mmxlabs.models.lng.scenario.importWizards.AbstractImportPage;
import com.mmxlabs.models.lng.scenario.importWizards.AbstractImportWizard;
import com.mmxlabs.models.lng.scenario.importWizards.paperdeals.PaperDealsImportAction;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.ui.actions.ImportAction;
import com.mmxlabs.models.lng.ui.actions.ImportAction.ImportHooksProvider;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class ImportPaperDealsFromExcelWizard extends AbstractImportWizard {
	
	private ImportPaperDealsFromExcelPage importPage;
	
	@Inject
	private Iterable<IPaperDealExporter> paperDealExporters;

	public ImportPaperDealsFromExcelWizard(ScenarioInstance scenarioInstance, String windowTitle) {
		super(scenarioInstance, windowTitle);
		
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
		System.out.println("Filename: " + importPage.getImportFilename());
		System.out.println("Worksheet: " + importPage.getSelectedWorksheetName());
		
		ExcelReader excelReader = null;
		
		try(final FileInputStream fis = new FileInputStream(importPage.getImportFilename())){
			
			final Iterator<IPaperDealExporter> iterPaperDealExporters = paperDealExporters.iterator();
			final IPaperDealExporter paperDealExporter = iterPaperDealExporters.hasNext() ? iterPaperDealExporters.next(): new DefaulPaperDealExcelExporter();
			final Pair<List<BuyPaperDeal>, List<SellPaperDeal>> paperDealsPair = paperDealExporter.getPaperDeals(fis);
			if (paperDealExporter != null) {
				// TODO: create a CompoundCommand and attach paper deals to the model
			}
			
//			try {
//				excelReader = new ExcelReader(fis, importPage.getSelectedWorksheetName());
//			} catch (IOException e) {
//				// TODO Handle exceptions properly
//				
//				return false;
//			}
		} catch (final Exception e) {
			System.out.println(e.getMessage());
		}
		

		return true;
	}
	
	
	
	
}
