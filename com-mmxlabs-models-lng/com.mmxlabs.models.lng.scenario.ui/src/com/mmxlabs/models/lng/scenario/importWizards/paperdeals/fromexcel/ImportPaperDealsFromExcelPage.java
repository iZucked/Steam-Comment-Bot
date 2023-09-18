/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.importWizards.paperdeals.fromexcel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.BuyPaperDeal;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.PaperDeal;
import com.mmxlabs.models.lng.cargo.SellPaperDeal;
import com.mmxlabs.models.lng.pricing.CommodityCurve;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.SettleStrategy;
import com.mmxlabs.models.lng.scenario.importWizards.AbstractImportPage;
import com.mmxlabs.models.lng.scenario.importWizards.paperdeals.fromexcel.util.DefaulPaperDealExcelExporter;
import com.mmxlabs.models.lng.scenario.importWizards.paperdeals.fromexcel.util.DefaultCommodityCurveImporter;
import com.mmxlabs.models.lng.scenario.importWizards.paperdeals.fromexcel.util.DefaultInstrumentCreator;
import com.mmxlabs.models.lng.scenario.importWizards.paperdeals.fromexcel.util.ExcelImportResultDescriptor;
import com.mmxlabs.models.lng.scenario.importWizards.paperdeals.fromexcel.util.ExcelImportResultDescriptor.MessageContext;
import com.mmxlabs.models.lng.scenario.importWizards.paperdeals.fromexcel.util.ExcelImportResultDescriptor.MessageType;
import com.mmxlabs.models.lng.scenario.importWizards.paperdeals.fromexcel.util.ExcelReader;
import com.mmxlabs.models.lng.scenario.importWizards.paperdeals.fromexcel.util.ICommodityCurveImporter;
import com.mmxlabs.models.lng.scenario.importWizards.paperdeals.fromexcel.util.IPaperDealExporter;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.ui.commands.ScheduleModelInvalidateCommandProvider;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;

public class ImportPaperDealsFromExcelPage extends AbstractImportPage {

	private Combo dropDownMenu;
	private Composite dropDownComposite;
	private boolean finishedImporting;
	
	public ImportPaperDealsFromExcelPage(final String pageName, final ScenarioInstance currentScenario) {
		super(pageName, currentScenario);
		setTitle("Select Excel file and worksheet");
		setDescription("Choose Excel file and respective worksheet from Excel file to import paper deals.");
		finishedImporting = false;
	}
	
	@Override
	public String getItemDescription() {
		return "paper deals from excel";
	}
	
	public String getSelectedWorksheetName() {
		if(dropDownMenu != null)
			return dropDownMenu.getText();
		return "";
	}
	
	@Override
	public void createControl(final Composite parent) {
		final Composite container = new Composite(parent, SWT.NONE);

		// set the layout for the whole functional region
		final GridLayout layout = new GridLayout(2, false);
		layout.verticalSpacing = 0;
		layout.marginBottom = 0;
		layout.marginRight = 0;
		layout.marginWidth = 0;
		container.setLayout(layout);
		final GridData ld = new GridData();
		ld.verticalIndent = 0;
		ld.horizontalAlignment = SWT.FILL;
		ld.grabExcessHorizontalSpace = true;
		container.setLayoutData(ld);

		final Composite datafileC = new Composite(container, SWT.NONE);
		final GridData gd = new GridData();
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalSpan = 2;
		datafileC.setLayoutData(gd);

		importFileEditor = new FileFieldEditor("Workbook file", "Workbook file", datafileC);
		importFileEditor.setFileExtensions(new String[] { "*.xlsx", "*.xlsm", "*.xlsb" });
		importFileEditor.getTextControl(datafileC).addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(final ModifyEvent e) {
				// Populate drop down menu
				List<String> sheetNames = new ArrayList<>();
				
				// TODO: Handle exceptions correctly
				try {
					sheetNames = ExcelReader.getSheetNames(getImportFilename());
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				dropDownMenu.setItems(sheetNames.stream().toArray(String[]::new));
				
				// Show worksheet drop down menu
				dropDownComposite.setVisible(true);
			}
		});
		
		datafileC.setVisible(!guided);

		// create a control to display the drop down menu
        dropDownComposite = new Composite(container, SWT.NONE);
        dropDownComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        dropDownComposite.setLayout(new GridLayout(2, false));

        Label label = new Label(dropDownComposite, SWT.LEFT);
        label.setText("Select worksheet: ");

        // Create the drop down menu for worksheets
        dropDownMenu = new Combo(dropDownComposite, SWT.DROP_DOWN | SWT.READ_ONLY);
        dropDownMenu.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        dropDownMenu.addModifyListener(new ModifyListener() {
        	@Override
			public void modifyText(final ModifyEvent e) {
        		// Let the finish button show
        		getContainer().updateButtons();
        	}
        });
        
        dropDownComposite.setVisible(false);

		setControl(container);
		control = container;
	}
	
	@Override
	public boolean canFlipToNextPage() {
		final File file = new File(getImportFilename());
		return file.isFile() && file.canRead() && !getSelectedWorksheetName().isBlank();
	}
	
	@Override
	public IWizardPage getNextPage() {
		// Import paper deals
		final ImportPaperDealsFromExcelWizard wizard = (ImportPaperDealsFromExcelWizard) getWizard();
		
		wizard.messages.clear();
		try (final FileInputStream fis = new FileInputStream(getImportFilename())) {
			final ExcelReader reader = new ExcelReader(fis, getSelectedWorksheetName());
			final Iterator<IPaperDealExporter> iterPaperDealExporters = wizard.paperDealExporters.iterator();
			final Iterator<ICommodityCurveImporter> itercommodityCurveImporters = wizard.commodityCurveImporters.iterator();
			final IPaperDealExporter paperDealExporter = iterPaperDealExporters.hasNext() ? iterPaperDealExporters.next() : new DefaulPaperDealExcelExporter();
			final ICommodityCurveImporter curveImporter = itercommodityCurveImporters.hasNext() ? itercommodityCurveImporters.next() : new DefaultCommodityCurveImporter();

			final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(wizard.scenarioInstance);
			try (final IScenarioDataProvider scenarioDataProvider = modelRecord.aquireScenarioDataProvider("ScenarioDataProvider:1")) {
				final EditingDomain ed = scenarioDataProvider.getEditingDomain();
				final CompoundCommand instrumentCommand = new CompoundCommand("Create instruments");
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
							final List<CommodityCurve> createdCurves = curveImporter.getCommodityCurves(reader, monitor, wizard.messages);
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
				
				// Create instruments if not present
				String[] instrumentNames = {"JKM_SWAP", "BRENT_FUTURES", "DATED_BRENT_SWAP", "TFU_SWAP", "TTF_SWAP"};
				List<SettleStrategy> newInstruments = new ArrayList<>();
				for(String instument : instrumentNames) {
					// Check if not present
					if(ScenarioModelUtil.getPricingModel(scenarioDataProvider).getSettleStrategies().stream().noneMatch(ss -> ss.getName().equals(instument))) {
						newInstruments.add(DefaultInstrumentCreator.getInstrument(instument));
					}
				}
				
				if(!newInstruments.isEmpty()) {
					instrumentCommand.append(
							AddCommand.create(scenarioDataProvider.getEditingDomain(), ScenarioModelUtil.getPricingModel(scenarioDataProvider), 
									PricingPackage.Literals.PRICING_MODEL__SETTLE_STRATEGIES, newInstruments)
							);
					scenarioDataProvider.getCommandStack().execute(instrumentCommand);
				}
				
				
				
				// Create paper deals
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
									wizard.messages, monitor);

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

				finishedImporting = true;
				
				// Make Next button visible
				getContainer().updateButtons(); 
			}

		} catch (final Exception e) {
			wizard.messages.add(new ExcelImportResultDescriptor(MessageType.ERROR, MessageContext.PAPER_DEAL, "Unable to import paper deals, reason: " + e.getMessage()));
			
		}
		
		return super.getNextPage();
	}
	
	public boolean isFinishedImporting() {
		return finishedImporting;
	}
	
}
