package com.mmxlabs.models.lng.scenario.importWizards.paperdeals.fromexcel;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
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
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.PaperDeal;
import com.mmxlabs.models.lng.cargo.SellPaperDeal;
import com.mmxlabs.models.lng.pricing.CommodityCurve;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.scenario.importWizards.AbstractImportPage;
import com.mmxlabs.models.lng.scenario.importWizards.AbstractImportWizard;
import com.mmxlabs.models.lng.scenario.importWizards.paperdeals.PaperDealsImportAction;
import com.mmxlabs.models.lng.scenario.importWizards.paperdeals.fromexcel.util.DefaulPaperDealExcelExporter;
import com.mmxlabs.models.lng.scenario.importWizards.paperdeals.fromexcel.util.DefaultCommodityCurveImporter;
import com.mmxlabs.models.lng.scenario.importWizards.paperdeals.fromexcel.util.ExcelReader;
import com.mmxlabs.models.lng.scenario.importWizards.paperdeals.fromexcel.util.ICommodityCurveImporter;
import com.mmxlabs.models.lng.scenario.importWizards.paperdeals.fromexcel.util.IPaperDealExporter;
import com.mmxlabs.models.lng.scenario.importWizards.paperdeals.fromexcel.util.PaperDealExcelImportResultDescriptor;
import com.mmxlabs.models.lng.scenario.importWizards.paperdeals.fromexcel.util.PaperDealExcelImportResultDescriptor.MessageType;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.ui.commands.ScheduleModelInvalidateCommandProvider;
import com.mmxlabs.models.lng.ui.actions.ImportAction;
import com.mmxlabs.models.lng.ui.actions.ImportAction.ImportHooksProvider;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;

public class ImportPaperDealsFromExcelWizard extends AbstractImportWizard {

	private static final List<PaperDealExcelImportResultDescriptor> messages = new ArrayList<>();

	private ImportPaperDealsFromExcelPage importPage;

	private final ScenarioInstance scenarioInstance;

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
		try (final FileInputStream fis = new FileInputStream(importPage.getImportFilename())) {
			final ExcelReader reader = new ExcelReader(fis, importPage.getSelectedWorksheetName());
			final Iterator<IPaperDealExporter> iterPaperDealExporters = paperDealExporters.iterator();
			final Iterator<ICommodityCurveImporter> itercommodityCurveImporters = commodityCurveImporters.iterator();
			final IPaperDealExporter paperDealExporter = iterPaperDealExporters.hasNext() ? iterPaperDealExporters.next() : new DefaulPaperDealExcelExporter();
			final ICommodityCurveImporter curveImporter = itercommodityCurveImporters.hasNext() ? itercommodityCurveImporters.next() : new DefaultCommodityCurveImporter();

			final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenarioInstance);
			try (final IScenarioDataProvider scenarioDataProvider = modelRecord.aquireScenarioDataProvider("ScenarioDataProvider:1")) {
				final EditingDomain ed = scenarioDataProvider.getEditingDomain();
				final CompoundCommand curvesCommand = new CompoundCommand("Import commodity curves from excel");
				final CompoundCommand paperDealCommand = new CompoundCommand("Import paper deals from excel");
				final LNGScenarioModel scenarioModel = ScenarioModelUtil.getScenarioModel(scenarioDataProvider);
				// Invalidate and clear the schedule/dependent sandboxes
				curvesCommand.append(ScheduleModelInvalidateCommandProvider.createClearModelsCommand(//
						ed, scenarioModel, ScenarioModelUtil.getAnalyticsModel(scenarioModel)));
				
				final IRunnableWithProgress createCurvesOpertaion = new IRunnableWithProgress() {
					@Override
					public void run(IProgressMonitor monitor) {
						if (curveImporter != null) {
							final List<CommodityCurve> createdCurves = curveImporter.getCommodityCurves(reader, monitor, messages);
							final List<CommodityCurve> existingCurves = ScenarioModelUtil.getPricingModel(scenarioDataProvider).getCommodityCurves();

							if (createdCurves != null && !createdCurves.isEmpty()) {
								// Check if any curves already exist
								for (CommodityCurve curve : createdCurves) {
									Optional<CommodityCurve> foundCurve = existingCurves.stream().filter(c -> c.getName().equals(curve.getName())).findFirst();
									if (foundCurve.isPresent()) {
										// Update points
										curvesCommand.append(
												SetCommand.create(ed, foundCurve.get(), PricingPackage.Literals.YEAR_MONTH_POINT_CONTAINER__POINTS, SetCommand.UNSET_VALUE));

										curvesCommand.append(
												AddCommand.create(ed, foundCurve.get(), PricingPackage.Literals.YEAR_MONTH_POINT_CONTAINER__POINTS, curve.getPoints()));
									} else {
										// Create curve
										curvesCommand.append(AddCommand.create(ed, ScenarioModelUtil.getPricingModel(scenarioDataProvider),
												PricingPackage.Literals.PRICING_MODEL__COMMODITY_CURVES, curve));
									}
								}

							}
						}
					}
				};
				getContainer().run(true, false, createCurvesOpertaion);
				
				if (!curvesCommand.isEmpty()) 
					scenarioDataProvider.getCommandStack().execute(curvesCommand);
				

				final IRunnableWithProgress createPaperDealsOpertaion = new IRunnableWithProgress() {
					@Override
					public void run(IProgressMonitor monitor) {
						if (paperDealExporter != null) {
							final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(scenarioDataProvider);
							final List<PaperDeal> allPaperDeals = cargoModel.getPaperDeals();
							if (allPaperDeals != null && !allPaperDeals.isEmpty()) {
								paperDealCommand.append(RemoveCommand.create(ed, //
										cargoModel, CargoPackage.eINSTANCE.getCargoModel_PaperDeals(), allPaperDeals));
							}
							final Pair<List<BuyPaperDeal>, List<SellPaperDeal>> paperDealsPair = paperDealExporter.getPaperDeals(reader, ScenarioModelUtil.getScenarioModel(scenarioDataProvider),
									messages, monitor);

							if (!paperDealsPair.getFirst().isEmpty()) {
								paperDealCommand.append(AddCommand.create(ed, ScenarioModelUtil.getCargoModel(scenarioDataProvider),
										CargoPackage.Literals.CARGO_MODEL__PAPER_DEALS, paperDealsPair.getFirst()));
							}
							if (!paperDealsPair.getSecond().isEmpty()) {
								paperDealCommand.append(AddCommand.create(ed, ScenarioModelUtil.getCargoModel(scenarioDataProvider),
										CargoPackage.Literals.CARGO_MODEL__PAPER_DEALS, paperDealsPair.getSecond()));
							}
						}
					}

				};

				getContainer().run(true, false, createPaperDealsOpertaion);

				if (!paperDealCommand.isEmpty())
					scenarioDataProvider.getCommandStack().execute(paperDealCommand);

			}

		} catch (final Exception e) {
			MessageDialog.openError(getShell(), "Import error", "Unable to import paper deals, reason: " + e.getMessage());
			e.printStackTrace();
			return false;
		}

		StringBuilder errorMessage = new StringBuilder();
		StringBuilder infoMessage = new StringBuilder();
		for (PaperDealExcelImportResultDescriptor message : messages) {
			if (message.getType().equals(MessageType.INFO))
				infoMessage.append(message).append("\n");
			else
				errorMessage.append(message).append("\n");
		}

		// Display any error and info messages
		if (!infoMessage.isEmpty())
			MessageDialog.openInformation(getShell(), "Import Result", infoMessage.toString());

		if (!errorMessage.isEmpty())
			MessageDialog.openError(getShell(), "Import Result", errorMessage.toString());

		return true;
	}

}
