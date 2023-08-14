/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.importWizards;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.csv.CSVReader;
import com.mmxlabs.common.csv.FileCSVReader;
import com.mmxlabs.common.csv.IImportContext;
import com.mmxlabs.models.lng.actuals.ActualsModel;
import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.migration.ModelsLNGVersionMaker;
import com.mmxlabs.models.lng.nominations.NominationsModel;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.pricing.CostModel;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.scenario.internal.Activator;
import com.mmxlabs.models.lng.scenario.model.LNGReferenceModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioFactory;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.wizards.BulkImportPage.RadioSelectionGroup;
import com.mmxlabs.models.lng.scenario.wizards.ScenarioServiceNewScenarioPage;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.transfers.TransferModel;
import com.mmxlabs.models.ui.editors.util.EditorUtils;
import com.mmxlabs.models.util.importer.IExtraModelImporter;
import com.mmxlabs.models.util.importer.IPostModelImporter;
import com.mmxlabs.models.util.importer.ISubmodelImporter;
import com.mmxlabs.models.util.importer.impl.DefaultImportContext;
import com.mmxlabs.models.util.importer.registry.IImporterRegistry;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.SimpleScenarioDataProvider;

public class ImportCSVFilesPage extends WizardPage {
	
	private static final String FILTER_DIRECTORY_KEY = "lastSelection";

	private static final String SECTION_NAME = "ImportCSVFilesPage.section";

	private static final String DELIMITER_KEY = "lastDelimiter";
	private static final String DECIMAL_SEPARATOR_KEY = "lastDecimalSeparator";

	private static final Logger LOG = LoggerFactory.getLogger(ImportCSVFilesPage.class);
	
	private static final IImporterRegistry importRegistry = Activator.getDefault().getImporterRegistry();

	private final List<SubModelChunk> subModelChunks = new LinkedList<>();
	private final List<ExtraModelChunk> extraModelChunks = new LinkedList<>();

	private IImportContext importContext;

	private final ScenarioServiceNewScenarioPage mainPage;

	private ScenarioInstance scenarioInstance;
	private static final int CHOICE_COMMA = 0;
	private static final int CHOICE_SEMICOLON = 1;
	private static final int CHOICE_PERIOD = 1;
	private RadioSelectionGroup csvSelectionGroup;
	private RadioSelectionGroup decimalSelectionGroup;

	public IImportContext getImportContext() {
		return importContext;
	}

	protected ImportCSVFilesPage(final String pageName, final ScenarioServiceNewScenarioPage mainPage) {
		super(pageName);
		setTitle("Choose CSV Files");
		this.mainPage = mainPage;
	}

	@Override
	public void createControl(final Composite arg0) {	
		final ScrolledComposite c1 = new ScrolledComposite(arg0, SWT.BORDER | SWT.V_SCROLL);

		final Composite top = new Composite(c1, SWT.NONE);
		top.setLayout(new GridLayout(1, false));
		top.setToolTipText("Top");

		final Composite holder = new Composite(top, SWT.NONE);
		final GridLayout gl = new GridLayout(3, false);
		gl.marginLeft = gl.marginWidth = 0;
		holder.setLayout(gl);
		holder.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());

		final IDialogSettings section = getSection();
		final String lastDirectoryName = section.get(FILTER_DIRECTORY_KEY);

		// Create selection dialogs
		final Button auto = new Button(holder, SWT.NONE);
		final Label directory = new Label(holder, SWT.NONE);
		auto.setText("Choose &Directory...");
		directory.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
		if (lastDirectoryName != null) {
			directory.setText(lastDirectoryName);
		}
		auto.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				final String filter = section.get(FILTER_DIRECTORY_KEY);
				// display file open dialog and then fill out files if the exist.
				final DirectoryDialog dd = new DirectoryDialog(getShell());
				if (filter != null) {
					dd.setFilterPath(filter);
				}

				final String d = dd.open();
				
				if (d != null) {
					final File dir = new File(d);
					if (dir.exists() && dir.isDirectory()) {
						join(subModelChunks, extraModelChunks).forEach(c -> c.setFromDirectory(dir));
					}
					directory.setText(d);

					// Trigger 'Next' button focus
					setPageComplete(true);
					section.put(FILTER_DIRECTORY_KEY, dir.toString());
				}
			}
		});

		GridData layout;

		final ExpandableComposite fieldComposite = new ExpandableComposite(top, SWT.NONE);
		fieldComposite.setText("Advanced");
		fieldComposite.setLayout(new GridLayout(1, false));
		layout = new GridData(SWT.FILL, SWT.FILL, true, true);
		// for some reason, horizontal and vertical fill do not work without width /
		// height hints set
		layout.heightHint = 300;
		layout.widthHint = 600;
		fieldComposite.setLayoutData(layout);

		final ScrolledComposite fieldScroller = new ScrolledComposite(fieldComposite, SWT.V_SCROLL);
		fieldScroller.setLayout(new GridLayout(1, false));

		final Composite inner = new Composite(fieldScroller, SWT.NONE);
		inner.setLayout(new GridLayout(1, false));

		{
			final Composite radioCompo = new Composite(inner, SWT.NONE);
			radioCompo.setLayout(GridLayoutFactory.fillDefaults().margins(0, 0).numColumns(2).equalWidth(true).create());

			csvSelectionGroup = new RadioSelectionGroup(radioCompo, "Format separator", SWT.NONE, new String[] { "comma (\",\")", "semicolon (\";\")" }, new int[] { CHOICE_COMMA, CHOICE_SEMICOLON });
			decimalSelectionGroup = new RadioSelectionGroup(radioCompo, "Decimal separator", SWT.NONE, new String[] { "comma (\",\")", "period (\".\")" }, new int[] { CHOICE_COMMA, CHOICE_PERIOD });
			// get the default export directory from the settings

			int delimiterValue = CHOICE_COMMA;
			if (section.get(DELIMITER_KEY) != null) {
				delimiterValue = section.getInt(DELIMITER_KEY);
			}
			int decimalValue = CHOICE_PERIOD;
			if (section.get(DECIMAL_SEPARATOR_KEY) != null) {
				decimalValue = section.getInt(DECIMAL_SEPARATOR_KEY);
			}
			// use it to populate the editor
			csvSelectionGroup.setSelectedIndex(delimiterValue);
			decimalSelectionGroup.setSelectedIndex(decimalValue);

		}

		File lastDirectoryFile = null;
		if (lastDirectoryName != null) {
			final File dir = new File(lastDirectoryName);
			if (dir.exists() && dir.isDirectory()) {
				lastDirectoryFile = dir;
			}
		}

		// Add a load of fields to the editor based on registered submodel and extra importers
		for (final ISubmodelImporter importer : importRegistry.getAllSubModelImporters()) {
			if (importer == null) {
				continue;
			}
			final EClass subModelClass = importer.getEClass();
			final SubModelChunk chunk = new SubModelChunk(importer);
			final Map<String, String> parts = importer.getRequiredInputs();
			subModelChunks.add(chunk);
			chunk.friendlyNames.putAll(parts);
			if (parts.keySet().isEmpty()) {
				continue;
			}

			final Group g = new Group(inner, SWT.NONE);
			g.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			g.setLayout(new RowLayout(SWT.VERTICAL));
			g.setText(EditorUtils.unmangle(subModelClass.getName()));
			for (final Map.Entry<String, String> entry : parts.entrySet()) {
				final String key = entry.getKey();
				final FileFieldEditor ffe = new FileFieldEditor(key, entry.getValue(), g);
				ffe.getTextControl(g).addModifyListener(
					modifyEvent -> {
						final String stringValue = ffe.getStringValue();
						if (stringValue.isEmpty()) {
							chunk.keys.remove(key);
						}
						else {
							chunk.keys.put(key, stringValue);
						}
				});
				chunk.editors.put(key, ffe);
			}
			chunk.setFromDirectory(lastDirectoryFile);
		}

		for (final IExtraModelImporter importer : importRegistry.getExtraModelImporters()) {
			if (importer == null) {
				continue;
			}
			final ExtraModelChunk chunk = new ExtraModelChunk(importer);
			final Map<String, String> parts = importer.getRequiredInputs();
			extraModelChunks.add(chunk);
			chunk.friendlyNames.putAll(parts);
			if (parts.keySet().isEmpty()) {
				continue;
			}

			final Group g = new Group(inner, SWT.NONE);
			g.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			g.setLayout(new RowLayout(SWT.VERTICAL));
			// g.setText(EditorUtils.unmangle(subModelClass.getName()));
			for (final Map.Entry<String, String> entry : parts.entrySet()) {
				final String key = entry.getKey();
				final FileFieldEditor ffe = new FileFieldEditor(key, entry.getValue(), g);
				ffe.getTextControl(g).addModifyListener(
						modifyEvent -> {
							final String stringValue = ffe.getStringValue();
							if (stringValue.isEmpty()) {
								chunk.keys.remove(key);
							}
							else {
								chunk.keys.put(key, stringValue);
							}
					});
				chunk.editors.put(key, ffe);
			}
			chunk.setFromDirectory(lastDirectoryFile);
		}

		fieldComposite.setClient(fieldScroller);
		fieldScroller.setContent(inner);
		fieldScroller.setExpandHorizontal(true);
		inner.setSize(inner.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		c1.setContent(top);
		c1.setExpandHorizontal(true);
		top.setSize(top.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		setControl(c1);
	}

	public IScenarioDataProvider doImport(final DefaultImportContext context) {

		final LNGScenarioModel scenarioModel = LNGScenarioFactory.eINSTANCE.createLNGScenarioModel();
		final LNGReferenceModel referenceModel = LNGScenarioFactory.eINSTANCE.createLNGReferenceModel();
		scenarioModel.setReferenceModel(referenceModel);
		scenarioModel.setAnalyticsModel(AnalyticsFactory.eINSTANCE.createAnalyticsModel());

		final char delimiter = csvSelectionGroup.getSelectedValue() == CHOICE_COMMA ? ',' : ';';

		context.setRootObject(scenarioModel);
		final SimpleScenarioDataProvider scenarioDataProvider = SimpleScenarioDataProvider.make(ModelsLNGVersionMaker.createDefaultManifest(), scenarioModel);
	
		for (final Chunk c : join(subModelChunks, extraModelChunks)) {
			final HashMap<String, CSVReader> readers = new HashMap<>();
			try {
				for (final String key : c.keys.keySet()) {
					try {
						final CSVReader r = new FileCSVReader(new File(c.keys.get(key)), delimiter);
						readers.put(key, r);
					} catch (final IOException e) {
						LOG.error(e.getMessage(), e);
					}
				}
				try {
					if (c instanceof SubModelChunk smc) {
						final EObject subModel = smc.importer.importModel(readers, context);
						setSubModel(scenarioModel, subModel);
					}
					else if (c instanceof ExtraModelChunk emc) {
						emc.importer.importModel(scenarioModel, readers, context);
					}
					
				} catch (final Throwable th) {
					LOG.error(th.getMessage(), th);
				}
			} finally {
				for (final CSVReader r : readers.values()) {
					try {
						r.close();
					} catch (final IOException e) {
						LOG.error(e.getMessage(), e);
					}
				}
			}
		}

		context.setRootObject(scenarioModel);
		context.run();

		for (final IPostModelImporter postModelImporter : importRegistry.getPostModelImporters()) {
			postModelImporter.onPostModelImport(context, scenarioModel);
		}

		return scenarioDataProvider;
	}

	private void setSubModel(final LNGScenarioModel scenarioModel, final EObject subModel) {
		final LNGReferenceModel referenceModel = scenarioModel.getReferenceModel();
		if (subModel instanceof final PortModel portModel) {
			referenceModel.setPortModel(portModel);
		} else if (subModel instanceof final FleetModel fleetModel) {
			referenceModel.setFleetModel(fleetModel);
		} else if (subModel instanceof final PricingModel pricingModel) {
			referenceModel.setPricingModel(pricingModel);
		} else if (subModel instanceof final CostModel costModel) {
			referenceModel.setCostModel(costModel);
		} else if (subModel instanceof final CommercialModel commercialModel) {
			referenceModel.setCommercialModel(commercialModel);
		} else if (subModel instanceof final SpotMarketsModel spotMarketsModel) {
			referenceModel.setSpotMarketsModel(spotMarketsModel);
		} else if (subModel instanceof final AnalyticsModel analyticsModel) {
			scenarioModel.setAnalyticsModel(analyticsModel);
		} else if (subModel instanceof final CargoModel cargoModel) {
			scenarioModel.setCargoModel(cargoModel);
		} else if (subModel instanceof final ActualsModel actualsModel) {
			scenarioModel.setActualsModel(actualsModel);
		} else if (subModel instanceof final ScheduleModel scheduleModel) {
			scenarioModel.setScheduleModel(scheduleModel);
		} else if (subModel instanceof final NominationsModel scheduleModel) {
			scenarioModel.setNominationsModel(scheduleModel);
		} else if (subModel instanceof final TransferModel transferModel) {
			scenarioModel.setTransferModel(transferModel);
		} else {
			LOG.error("Unknown sub model type: " + subModel.eClass().getName());
		}
	}

	@Override
	public IWizardPage getNextPage() {
		setMessage("");
		this.importContext = null;
		final char decimalSeparator = decimalSelectionGroup.getSelectedValue() == CHOICE_COMMA ? ',' : '.';

		try {
			getContainer().run(false, false, new IRunnableWithProgress() {

				@SuppressWarnings("null")
				@Override
				public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

					SubMonitor subMonitor = SubMonitor.convert(monitor, "Import Scenario", 3);
					try {
						final DefaultImportContext context = new DefaultImportContext(decimalSeparator);

						final IScenarioDataProvider scenarioDataProvider = doImport(context);
						if (scenarioDataProvider != null) {

							subMonitor.worked(1);

							ImportCSVFilesPage.this.importContext = context;
							final Container container = mainPage.getScenarioContainer();
							assert container != null;

							final IScenarioService scenarioService = SSDataManager.Instance.findScenarioService(container);
							final ScenarioInstance newInstance = scenarioService.copyInto(container, scenarioDataProvider, mainPage.getFileName(), subMonitor.split(1));
							subMonitor.worked(1);

							ImportCSVFilesPage.this.setScenarioInstance(newInstance);	
						}
					}
					catch (final Exception e) {
						LOG.error(e.getMessage(), e);
						setErrorMessage(e.getMessage());	
					}
					finally {
						monitor.done();
					}
				}
			});
		} catch (final Exception e) {
			LOG.error(e.getMessage());
			return null;
		}

		return super.getNextPage();
	}

	/**
	 * Saves the value of the directory editor field to persistent storage
	 */
	public void saveDirectorySetting() {
		final IDialogSettings section = getSection();
		section.put(DELIMITER_KEY, csvSelectionGroup.getSelectedValue());
		section.put(DECIMAL_SEPARATOR_KEY, decimalSelectionGroup.getSelectedValue());
	}

	@Override
	public boolean canFlipToNextPage() {
		return isPageComplete();
	}

	public ScenarioInstance getScenarioInstance() {
		return scenarioInstance;
	}

	protected void setScenarioInstance(final ScenarioInstance scenarioInstance) {
		this.scenarioInstance = scenarioInstance;
	}
	
	private <T> List<T> join(List<? extends T> list1, List<? extends T> list2) {		
		return Stream.concat(list1.stream(), list2.stream()).toList();
	}
	
	private IDialogSettings getSection() {
		final IDialogSettings dialogSettings = Activator.getDefault().getDialogSettings();
		IDialogSettings section = dialogSettings.getSection(SECTION_NAME);
		if (section == null) {
			section = dialogSettings.addNewSection(SECTION_NAME);
		}
		return section;
	}
}
