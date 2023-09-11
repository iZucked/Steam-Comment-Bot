package com.mmxlabs.models.lng.scenario.importWizards.paperdeals.fromexcel;

import java.io.FileInputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
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
import com.mmxlabs.models.lng.pricing.CommodityCurve;
import com.mmxlabs.models.lng.scenario.importWizards.AbstractImportPage;
import com.mmxlabs.models.lng.scenario.importWizards.AbstractImportWizard;
import com.mmxlabs.models.lng.scenario.importWizards.paperdeals.PaperDealsImportAction;
import com.mmxlabs.models.lng.scenario.importWizards.paperdeals.fromexcel.util.DefaulPaperDealExcelExporter;
import com.mmxlabs.models.lng.scenario.importWizards.paperdeals.fromexcel.util.DefaultCommodityCurveImporter;
import com.mmxlabs.models.lng.scenario.importWizards.paperdeals.fromexcel.util.ExcelReader;
import com.mmxlabs.models.lng.scenario.importWizards.paperdeals.fromexcel.util.ICommodityCurveImporter;
import com.mmxlabs.models.lng.scenario.importWizards.paperdeals.fromexcel.util.IPaperDealExporter;
import com.mmxlabs.models.lng.scenario.importWizards.paperdeals.fromexcel.util.PaperDealExcelImportResultDescriptor;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.ui.actions.ImportAction;
import com.mmxlabs.models.lng.ui.actions.ImportAction.ImportHooksProvider;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;

public class ImportPaperDealsFromExcelWizard extends AbstractImportWizard {
	
	private static final List<PaperDealExcelImportResultDescriptor> messages = new ArrayList<>();
	
	private ImportPaperDealsFromExcelPage importPage;
	
	private ScenarioInstance scenarioInstance;
	
	@Inject
	private Iterable<IPaperDealExporter> paperDealExporters;
	
	@Inject
	private Iterable<ICommodityCurveImporter> commodityCurveImporters;

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
		messages.clear();
		try(final FileInputStream fis = new FileInputStream(importPage.getImportFilename())){
			final ExcelReader reader = new ExcelReader(fis, importPage.getSelectedWorksheetName());
			final Iterator<IPaperDealExporter> iterPaperDealExporters = paperDealExporters.iterator();
			final Iterator<ICommodityCurveImporter> itercommodityCurveImporters = commodityCurveImporters.iterator();
			final IPaperDealExporter paperDealExporter = iterPaperDealExporters.hasNext() ? iterPaperDealExporters.next(): new DefaulPaperDealExcelExporter();
			final ICommodityCurveImporter curveImporter = itercommodityCurveImporters.hasNext() ? itercommodityCurveImporters.next(): new DefaultCommodityCurveImporter();
			
			final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenarioInstance);
	        try (final IScenarioDataProvider scenarioDataProvider = modelRecord.aquireScenarioDataProvider("ScenarioDataProvider:1")) {
	        	final CompoundCommand command = new CompoundCommand("Import paper deals from excel");
	        	
	        	final IRunnableWithProgress opertaion = new IRunnableWithProgress() {

					@Override
					public void run(IProgressMonitor monitor) {
						final List<CommodityCurve> curves = curveImporter.getCommodityCurves(reader);
						final Pair<List<BuyPaperDeal>, List<SellPaperDeal>> paperDealsPair = paperDealExporter.getPaperDeals(reader, ScenarioModelUtil.getScenarioModel(scenarioDataProvider), messages, monitor);
						if (paperDealExporter != null) {
							if(!paperDealsPair.getFirst().isEmpty()) {
								command.append(
										AddCommand.create(scenarioDataProvider.getEditingDomain(), 
												ScenarioModelUtil.getCargoModel(scenarioDataProvider), 
												CargoPackage.Literals.CARGO_MODEL__PAPER_DEALS, 
												paperDealsPair.getFirst())
										);
							}
							if(!paperDealsPair.getSecond().isEmpty()) {
								command.append(
										AddCommand.create(scenarioDataProvider.getEditingDomain(), 
												ScenarioModelUtil.getCargoModel(scenarioDataProvider),
												CargoPackage.Literals.CARGO_MODEL__PAPER_DEALS, 
												paperDealsPair.getSecond())
										);
							}
						}
					}
	        		
	        	};
	        	
	        	getContainer().run(true, false, opertaion);
	        	scenarioDataProvider.getCommandStack().execute(command);
	        	
	        }
			
		} catch (final Exception e) {
			MessageDialog.openError(getShell(), "Import error", "Unable to import paper deals, reason: " + e.getMessage());
			return false;
		}
		
		StringBuilder errorMessage = new StringBuilder();
		StringBuilder infoMessage = new StringBuilder();
		for(PaperDealExcelImportResultDescriptor message : messages) {
			if(message.getPaperDealName().equals("END"))
				infoMessage.append(message).append("\n");
			else
				errorMessage.append(message).append("\n");
		}
		
		// Display any error and info messages
		if(!infoMessage.isEmpty())
			MessageDialog.openInformation(getShell(), "Import Result", infoMessage.toString());
		
		if(!errorMessage.isEmpty())
			MessageDialog.openError(getShell(), "Import Result", errorMessage.toString());

		return true;
	}
	
	
	
	
}
