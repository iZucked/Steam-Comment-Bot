package com.mmxlabs.models.lng.scenario.importWizards.paperdeals.excel;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;
import org.ops4j.peaberry.Peaberry;
import org.ops4j.peaberry.eclipse.EclipseRegistry;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.mmxlabs.models.lng.scenario.importWizards.AbstractImportPage;
import com.mmxlabs.models.lng.scenario.importWizards.AbstractImportWizard;
import com.mmxlabs.models.lng.scenario.importWizards.paperdeals.PaperDealsImportAction;
import com.mmxlabs.models.lng.scenario.importWizards.paperdeals.excel.util.ExcelImportResultDescriptor;
import com.mmxlabs.models.lng.scenario.importWizards.paperdeals.excel.util.ICommodityCurveImporter;
import com.mmxlabs.models.lng.scenario.importWizards.paperdeals.excel.util.IPaperDealExporter;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.ui.actions.ImportAction;
import com.mmxlabs.models.lng.ui.actions.ImportAction.ImportHooksProvider;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class ImportPaperDealsFromExcelWizard extends AbstractImportWizard {

	protected static final List<ExcelImportResultDescriptor> messages = new ArrayList<>();

	private ImportPaperDealsFromExcelPage importPage;
	private ImportPaperDealsErrorPage warnings;
	private Injector injector;

	protected final ScenarioInstance scenarioInstance;
	

	@Inject
	protected Iterable<IPaperDealExporter> paperDealExporters;

	@Inject
	protected Iterable<ICommodityCurveImporter> commodityCurveImporters;

	public ImportPaperDealsFromExcelWizard(ScenarioInstance scenarioInstance, String windowTitle) {
		super(scenarioInstance, windowTitle);
		this.scenarioInstance = scenarioInstance;
		final BundleContext bc = FrameworkUtil.getBundle(ImportPaperDealsFromExcelWizard.class).getBundleContext();
		injector = Guice.createInjector(Peaberry.osgiModule(bc, EclipseRegistry.eclipseRegistry()), new ImportPaperDealsFromExcelProviderModule());
		injector.injectMembers(this);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		super.init(workbench, selection);
		warnings = new ImportPaperDealsErrorPage("Warnings");
	}
	
	@Override
	protected AbstractImportPage getImportPage(String pageName, ScenarioInstance currentScenario) {
		importPage = new ImportPaperDealsFromExcelPage("Import Page", scenarioInstance);
		injector.injectMembers(importPage);
		return importPage;
	}

	@Override
	protected ImportAction getImportAction(ImportHooksProvider ihp, IScenarioDataProvider scenarioDataProvider) {
		return new PaperDealsImportAction(ihp, ScenarioModelUtil.getCargoModel(scenarioDataProvider));
	}

	@Override
	public boolean canFinish() {
		return importPage.isFinishedImporting();
	}
	
	@Override
	public boolean performFinish() {
		return true;
	}
	
	public List<ExcelImportResultDescriptor> getMessages(){
		return messages;
	}
	
	@Override
	public void addPages() {
		addPage(importPage);
		addPage(warnings);
	}



}
