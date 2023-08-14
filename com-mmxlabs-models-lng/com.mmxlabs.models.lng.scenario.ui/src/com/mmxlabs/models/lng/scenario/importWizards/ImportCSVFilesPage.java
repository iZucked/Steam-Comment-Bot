/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.importWizards;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
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
import org.eclipse.swt.widgets.FileDialog;
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
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.SimpleScenarioDataProvider;

public class ImportCSVFilesPage extends WizardPage {

	private static final int BUFFER = 2048; // For unzipping
	
	private static final String FILTER_DIRECTORY_KEY = "lastFolderSelection";
	private static final String FILTER_ZIP_KEY = "lastZipSelection";
	private static final String FILTER_PREVIOUS_KEY = "lastSelectionOption"; // Boolean distinguishing previous selection choice

	private static final String SECTION_NAME = "ImportCSVFilesPage.section";

	private static final String DELIMITER_KEY = "lastDelimiter";
	private static final String DECIMAL_SEPARATOR_KEY = "lastDecimalSeparator";

	private static final Logger LOG = LoggerFactory.getLogger(ImportCSVFilesPage.class);

	private abstract class Chunk {
		public final Map<String, String> keys;
		public final Map<String, String> friendlyNames = new HashMap<>();

		public final Map<String, FileFieldEditor> editors = new HashMap<>();

		public Chunk() {
			super();
			this.keys = new HashMap<>();
		}

		public void setFromDirectory(final File directory) {
			for (final Map.Entry<String, String> entry : friendlyNames.entrySet()) {
				final String k = entry.getKey();
				final String v = entry.getValue();
				final File sub = new File(directory, v + ".csv");
				if (sub.exists()) {
					try {
						final String str = sub.getCanonicalPath();
						editors.get(k).setStringValue(str);
						keys.put(k, str); // CME?
					} catch (final IOException e) {
					}
				}
			}
		}
	}

	private class SubModelChunk extends Chunk {
		public final ISubmodelImporter importer;

		public SubModelChunk(final ISubmodelImporter importer) {
			super();
			this.importer = importer;
		}
	}

	private class ExtraModelChunk extends Chunk {
		public final IExtraModelImporter importer;

		public ExtraModelChunk(final IExtraModelImporter importer) {
			super();
			this.importer = importer;
		}
	}

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
		setTitle("Choose CSV/ZIP Files");
		this.mainPage = mainPage;
	}
	
	private boolean isZip(File f) {
		try {
			ZipFile zip = new ZipFile(f);
		} catch (ZipException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	
	private File unzip(String fn) {
		try (ZipFile zip = new ZipFile(fn)) {
			// Unzips to new temp directory
			File destDir = new File(zip.getName().replace(".", "") + "_lingo_unzipped");
			while (destDir.exists()) {
				destDir = new File(destDir.getName()+"a");
			}
			destDir.mkdirs();
			destDir.deleteOnExit();
			Enumeration<? extends ZipEntry> entries = zip.entries();
			byte[] buffer = new byte[BUFFER];
			BufferedInputStream is = null;
			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				String fileName = entry.getName();
				// Directories shouldn't exist, only CSVs
				if (!fileName.substring(fileName.length()-4, fileName.length()).equals(".csv")) {
					continue;
				}
				File newFile = new File(destDir + File.separator + fileName);
				newFile.deleteOnExit();
				FileOutputStream fos = new FileOutputStream(newFile);
				is = new BufferedInputStream(zip.getInputStream(entry));
				int len;
				while ((len = is.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}
				fos.close(); 
			}
			return destDir;
		}
		catch (IOException e1) {
			// TODO: Add a warning?
			e1.printStackTrace();
		}
		return null;
	}

	@Override
	public void createControl(final Composite arg0) {
		final ScrolledComposite c1 = new ScrolledComposite(arg0, SWT.BORDER | SWT.V_SCROLL);

		final Composite top = new Composite(c1, SWT.NONE);

		top.setLayout(new GridLayout(1, false));
		top.setToolTipText("Top");

		final Composite holder = new Composite(top, SWT.NONE);
		final GridLayout gl = new GridLayout(3, false);
		gl.marginLeft = 0;
		gl.marginWidth = 0;
		holder.setLayout(gl);
		holder.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());

		final IDialogSettings dialogSettings = Activator.getDefault().getDialogSettings();
		final IDialogSettings section = dialogSettings.getSection(SECTION_NAME);
		
		// Load previous selection
		final String lastDirectoryName;
		if (section != null) {
			if (section.get(FILTER_DIRECTORY_KEY) == null) {
				lastDirectoryName = section.get(FILTER_DIRECTORY_KEY);
			}
			else if (!section.getBoolean(FILTER_PREVIOUS_KEY)) {
				lastDirectoryName = section.get(FILTER_DIRECTORY_KEY);
			}
			else {
				lastDirectoryName = section.get(FILTER_ZIP_KEY);
			}
		}
		else {
			lastDirectoryName = null;
		}

		// Create selection dialogs
		final Button auto = new Button(holder, SWT.NONE);
		auto.setText("Choose &Directory...");
		final Label directory = new Label(holder, SWT.NONE);
		directory.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
		if (lastDirectoryName != null) {
			directory.setText(lastDirectoryName);
		}
		auto.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				final IDialogSettings dialogSettings = Activator.getDefault().getDialogSettings();

				IDialogSettings section = dialogSettings.getSection(SECTION_NAME);
				final String filter = section == null ? null : section.get(FILTER_DIRECTORY_KEY);
				// display file open dialog and then fill out files if the exist.
				final DirectoryDialog dd = new DirectoryDialog(getShell());
				if (filter != null) {
					dd.setFilterPath(filter);
				}

				final String d = dd.open();

				if (d != null) {
					final File dir = new File(d);
					if (dir.exists() && dir.isDirectory()) {
						for (final Chunk c : subModelChunks) {
							c.setFromDirectory(dir);
						}
						for (final Chunk c : extraModelChunks) {
							c.setFromDirectory(dir);
						}
					}
					directory.setText(d);

					// Trigger 'Next' button focus
					setPageComplete(true);

					if (section == null) {
						section = dialogSettings.addNewSection(SECTION_NAME);
					}
					section.put(FILTER_DIRECTORY_KEY, dir.toString());
					section.put(FILTER_PREVIOUS_KEY, false);
				}
			}
		});
		
		final Composite holderZip = new Composite(top, SWT.NONE);
		gl.marginLeft = 0;
		gl.marginWidth = 0;
		holderZip.setLayout(gl);
		holderZip.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
		final Button autoZip = new Button(holderZip, SWT.NONE);
		autoZip.setText("Choose &Zip File...");
		autoZip.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				final IDialogSettings dialogSettings = Activator.getDefault().getDialogSettings();

				IDialogSettings section = dialogSettings.getSection(SECTION_NAME);
				// display file open dialog and then fill out files if the exist.
				final FileDialog dlg = new FileDialog(getShell());
				dlg.setFilterExtensions(new String[] { "*.zip",});
				final String filter = section == null ? null : section.get(FILTER_ZIP_KEY);
				if (filter != null) {
					dlg.setFilterPath(filter);
				}
				final String fn = dlg.open();
				
				if (fn != null) {
					File destDir = unzip(fn);	
					// Load from newly created directory
					directory.setText(fn);
					for (final Chunk c : subModelChunks) {
						c.setFromDirectory(destDir);
					}
					for (final Chunk c : extraModelChunks) {
						c.setFromDirectory(destDir);
					}
					setPageComplete(true);
					if (section == null) {
						section = dialogSettings.addNewSection(SECTION_NAME);
					}
					section.put(FILTER_ZIP_KEY, fn);
					section.put(FILTER_PREVIOUS_KEY, true);
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
			if (section != null && section.get(DELIMITER_KEY) != null) {
				delimiterValue = section.getInt(DELIMITER_KEY);
			}
			int decimalValue = CHOICE_PERIOD;
			if (section != null && section.get(DECIMAL_SEPARATOR_KEY) != null) {
				decimalValue = section.getInt(DECIMAL_SEPARATOR_KEY);
			}
			// use it to populate the editor
			csvSelectionGroup.setSelectedIndex(delimiterValue);
			decimalSelectionGroup.setSelectedIndex(decimalValue);

		}
		/*
		 * fieldComposite.addExpansionListener(new IExpansionListener() {
		 * 
		 * @Override public void expansionStateChanging(ExpansionEvent e) { // TODO
		 * Auto-generated method stub
		 * 
		 * }
		 * 
		 * @Override public void expansionStateChanged(ExpansionEvent e) {
		 * inner.setSize(inner.computeSize(SWT.DEFAULT, SWT.DEFAULT)); inner.layout();
		 * fieldScroller.setSize(fieldScroller.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		 * fieldScroller.layout();
		 * fieldComposite.setSize(fieldComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		 * fieldComposite.layout(); } });
		 */

		File lastDirectoryFile = null;
		if (lastDirectoryName != null) {
			final File dir = new File(lastDirectoryName);
			if (dir.exists() && dir.isDirectory()) {
				lastDirectoryFile = dir;
			}
			else if (dir.exists() && isZip(dir)) {
				lastDirectoryFile = unzip(lastDirectoryName);
			}
		}

		// add a load of fields to the editor based on registered submodel importers
		for (final ISubmodelImporter importer : Activator.getDefault().getImporterRegistry().getAllSubModelImporters()) {
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
				final FileFieldEditor ffe = new FileFieldEditor(entry.getKey(), entry.getValue(), g);
				ffe.getTextControl(g).addModifyListener(new ModifyListener() {
					@Override
					public void modifyText(final ModifyEvent e) {
						final String stringValue = ffe.getStringValue();
						if (stringValue.isEmpty()) {
							chunk.keys.remove(entry.getKey());
						} else {
							chunk.keys.put(entry.getKey(), stringValue);
						}
					}
				});
				chunk.editors.put(entry.getKey(), ffe);
			}
			if (lastDirectoryFile != null) {
				chunk.setFromDirectory(lastDirectoryFile);
			}
		}

		for (final IExtraModelImporter importer : Activator.getDefault().getImporterRegistry().getExtraModelImporters()) {
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
				final FileFieldEditor ffe = new FileFieldEditor(entry.getKey(), entry.getValue(), g);
				ffe.getTextControl(g).addModifyListener(new ModifyListener() {
					@Override
					public void modifyText(final ModifyEvent e) {
						final String stringValue = ffe.getStringValue();
						if (stringValue.isEmpty()) {
							chunk.keys.remove(entry.getKey());
						} else {
							chunk.keys.put(entry.getKey(), stringValue);
						}
					}
				});
				chunk.editors.put(entry.getKey(), ffe);
			}
			if (lastDirectoryFile != null) {
				chunk.setFromDirectory(lastDirectoryFile);
			}
		}

		fieldComposite.setClient(fieldScroller);
		fieldScroller.setContent(inner);
		fieldScroller.setExpandHorizontal(true);
		inner.setSize(inner.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		c1.setContent(top);
		// c1.setExpandVertical(true);
		c1.setExpandHorizontal(true);
		// c1.set
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

		for (final SubModelChunk c : subModelChunks) {
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
					final EObject subModel = c.importer.importModel(readers, context);
					setSubModel(scenarioModel, subModel);
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

		for (final ExtraModelChunk c : extraModelChunks) {
			final HashMap<String, CSVReader> readers = new HashMap<>();
			try {
				for (final String key : c.keys.keySet()) {
					try {
						final CSVReader r = new FileCSVReader(new File(c.keys.get(key)));
						readers.put(key, r);
					} catch (final IOException e) {
						LOG.error(e.getMessage(), e);
					}
				}
				try {
					c.importer.importModel(scenarioModel, readers, context);
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

		for (final IPostModelImporter postModelImporter : Activator.getDefault().getImporterRegistry().getPostModelImporters()) {
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

				@Override
				public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

					monitor.beginTask("Import Scenario", 3);
					try {
						final DefaultImportContext context = new DefaultImportContext(decimalSeparator);

						final IScenarioDataProvider scenarioDataProvider = doImport(context);
						if (scenarioDataProvider != null) {

							monitor.worked(1);

							ImportCSVFilesPage.this.importContext = context;
							final Container container = mainPage.getScenarioContainer();
							assert container != null;

							final IScenarioService scenarioService = SSDataManager.Instance.findScenarioService(container);

							try {
								final ScenarioInstance newInstance = scenarioService.copyInto(container, scenarioDataProvider, mainPage.getFileName(), new SubProgressMonitor(monitor, 1));
								monitor.worked(1);

								ImportCSVFilesPage.this.setScenarioInstance(newInstance);

							} catch (final Exception e) {
								// NOTE: in Java SE 7 we can incorporate this into the previous
								// exception block as catch(final IllegalArgumentException|IOException e)
								LOG.error(e.getMessage(), e);
								setErrorMessage(e.getMessage());
							}
						}
					} finally {
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
		final IDialogSettings dialogSettings = Activator.getDefault().getDialogSettings();
		IDialogSettings section = dialogSettings.getSection(SECTION_NAME);
		if (section == null) {
			section = dialogSettings.addNewSection(SECTION_NAME);
		}
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

}
