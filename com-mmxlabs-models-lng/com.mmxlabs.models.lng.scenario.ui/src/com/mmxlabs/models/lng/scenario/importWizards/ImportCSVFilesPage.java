/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.importWizards;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.dialogs.IDialogSettings;
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
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.csv.CSVReader;
import com.mmxlabs.common.csv.FileCSVReader;
import com.mmxlabs.common.csv.IImportContext;
import com.mmxlabs.models.lng.actuals.ActualsModel;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.fleet.FleetModel;
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
import com.mmxlabs.models.migration.IMigrationRegistry;
import com.mmxlabs.models.ui.editors.util.EditorUtils;
import com.mmxlabs.models.util.importer.IExtraModelImporter;
import com.mmxlabs.models.util.importer.IPostModelImporter;
import com.mmxlabs.models.util.importer.ISubmodelImporter;
import com.mmxlabs.models.util.importer.impl.DefaultImportContext;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.Metadata;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class ImportCSVFilesPage extends WizardPage {

	private static final String FILTER_KEY = "lastSelection";
	private static final String SECTION_NAME = "ImportCSVFilesPage.section";

	private static final String DELIMITER_KEY = "lastDelimiter";
	private static final String DECIMAL_SEPARATOR_KEY = "lastDecimalSeparator";

	private static final Logger log = LoggerFactory.getLogger(ImportCSVFilesPage.class);

	private abstract class Chunk {
		public final Map<String, String> keys;
		public final Map<String, String> friendlyNames = new HashMap<String, String>();

		public final Map<String, FileFieldEditor> editors = new HashMap<String, FileFieldEditor>();

		public Chunk() {
			super();
			this.keys = new HashMap<String, String>();
		}

		public void setFromDirectory(final File directory) {
			for (final String k : friendlyNames.keySet()) {
				final File sub = new File(directory, friendlyNames.get(k) + ".csv");
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

	private final List<SubModelChunk> subModelChunks = new LinkedList<SubModelChunk>();
	private final List<ExtraModelChunk> extraModelChunks = new LinkedList<ExtraModelChunk>();

	private IImportContext importContext;

	private final ScenarioServiceNewScenarioPage mainPage;

	private ScenarioInstance scenarioInstance;
	private final int CHOICE_COMMA = 0;
	private final int CHOICE_SEMICOLON = 1;
	private final int CHOICE_PERIOD = 1;
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
		gl.marginLeft = 0;
		gl.marginWidth = 0;
		holder.setLayout(gl);

		// GridData gd = new GridData();
		// gd.verticalIndent = 0;
		// gd.horizontalIndent = 0;
		// // gd.grabExcessHorizontalSpace = true;
		// holder.setLayoutData(gd);

		final Button auto = new Button(holder, SWT.NONE);
		auto.setText("Choose &Directory...");

		auto.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {

				final IDialogSettings dialogSettings = Activator.getDefault().getDialogSettings();

				IDialogSettings section = dialogSettings.getSection(SECTION_NAME);
				final String filter = section == null ? null : section.get(FILTER_KEY);
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
					// Trigger 'Next' button focus
					setPageComplete(true);

					if (section == null) {
						section = dialogSettings.addNewSection(SECTION_NAME);
					}
					section.put(FILTER_KEY, dir.toString());
				}
			}
		});

		csvSelectionGroup = new RadioSelectionGroup(holder, "Format separator", SWT.NONE, new String[] { "comma (\",\")", "semicolon (\";\")" }, new int[] { CHOICE_COMMA, CHOICE_SEMICOLON });

		decimalSelectionGroup = new RadioSelectionGroup(holder, "Decimal separator", SWT.NONE, new String[] { "comma (\",\")", "period (\".\")" }, new int[] { CHOICE_COMMA, CHOICE_PERIOD });
		// get the default export directory from the settings
		final IDialogSettings dialogSettings = Activator.getDefault().getDialogSettings();
		final IDialogSettings section = dialogSettings.getSection(SECTION_NAME);
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

		GridData layout;
		
		final ExpandableComposite fieldComposite = new ExpandableComposite(top, SWT.NONE);
		fieldComposite.setText("Custom Files");
		fieldComposite.setLayout(new GridLayout(1, false));
		layout = new GridData(SWT.FILL, SWT.FILL, true, true);
		// for some reason, horizontal and vertical fill do not work without width / height hints set
		layout.heightHint = 300;
		layout.widthHint = 600;
		fieldComposite.setLayoutData(layout);
		
		final ScrolledComposite fieldScroller = new ScrolledComposite(fieldComposite, SWT.V_SCROLL);
		fieldScroller.setLayout(new GridLayout(1, false));
		
		final Composite inner = new Composite(fieldScroller, SWT.NONE);		
		inner.setLayout(new GridLayout(1, false));

		/*
		fieldComposite.addExpansionListener(new IExpansionListener() {
			
			@Override
			public void expansionStateChanging(ExpansionEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				inner.setSize(inner.computeSize(SWT.DEFAULT, SWT.DEFAULT));
				inner.layout();
				fieldScroller.setSize(fieldScroller.computeSize(SWT.DEFAULT, SWT.DEFAULT));
				fieldScroller.layout();
				fieldComposite.setSize(fieldComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
				fieldComposite.layout();
			}
		});
		*/
		
		
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
			if (parts.keySet().isEmpty())
				continue;
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
		}

		
		for (final IExtraModelImporter importer : Activator.getDefault().getImporterRegistry().getExtraModelImporters()) {
			if (importer == null) {
				continue;
			}
			final ExtraModelChunk chunk = new ExtraModelChunk(importer);
			final Map<String, String> parts = importer.getRequiredInputs();
			extraModelChunks.add(chunk);
			chunk.friendlyNames.putAll(parts);
			if (parts.keySet().isEmpty())
				continue;
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

	public LNGScenarioModel doImport(final DefaultImportContext context) {

		final LNGScenarioModel scenarioModel = LNGScenarioFactory.eINSTANCE.createLNGScenarioModel();
		final LNGReferenceModel referenceModel = LNGScenarioFactory.eINSTANCE.createLNGReferenceModel();
		scenarioModel.setReferenceModel(referenceModel);

		final char delimiter = csvSelectionGroup.getSelectedValue() == CHOICE_COMMA ? ',' : ';';

		context.setRootObject(scenarioModel);

		for (final SubModelChunk c : subModelChunks) {
			final HashMap<String, CSVReader> readers = new HashMap<String, CSVReader>();
			try {
				for (final String key : c.keys.keySet()) {
					try {
						@SuppressWarnings("resource")
						final CSVReader r = new FileCSVReader(new File(c.keys.get(key)), delimiter);
						readers.put(key, r);
					} catch (final IOException e) {
						log.error(e.getMessage(), e);
					}
				}
				try {
					final EObject subModel = c.importer.importModel(readers, context);
					setSubModel(scenarioModel, subModel);
				} catch (final Throwable th) {
					log.error(th.getMessage(), th);
				}
			} finally {
				for (final CSVReader r : readers.values()) {
					try {
						r.close();
					} catch (final IOException e) {
						log.error(e.getMessage(), e);
					}
				}
			}
		}

		for (final ExtraModelChunk c : extraModelChunks) {
			final HashMap<String, CSVReader> readers = new HashMap<String, CSVReader>();
			try {
				for (final String key : c.keys.keySet()) {
					try {
						@SuppressWarnings("resource")
						final CSVReader r = new FileCSVReader(new File(c.keys.get(key)));
						readers.put(key, r);
					} catch (final IOException e) {
						log.error(e.getMessage(), e);
					}
				}
				try {
					c.importer.importModel(scenarioModel, readers, context);
				} catch (final Throwable th) {
					log.error(th.getMessage(), th);
				}
			} finally {
				for (final CSVReader r : readers.values()) {
					try {
						r.close();
					} catch (final IOException e) {

					}
				}
			}

		}

		context.setRootObject(scenarioModel);

		context.run();

		for (final IPostModelImporter postModelImporter : Activator.getDefault().getImporterRegistry().getPostModelImporters()) {
			postModelImporter.onPostModelImport(context, scenarioModel);
		}

		return scenarioModel;
	}

	private void setSubModel(final LNGScenarioModel scenarioModel, final EObject subModel) {
		final LNGReferenceModel referenceModel = scenarioModel.getReferenceModel();
		if (subModel instanceof PortModel) {
			referenceModel.setPortModel((PortModel) subModel);
		} else if (subModel instanceof FleetModel) {
			referenceModel.setFleetModel((FleetModel) subModel);
		} else if (subModel instanceof PricingModel) {
			referenceModel.setPricingModel((PricingModel) subModel);
		} else if (subModel instanceof CostModel) {
			referenceModel.setCostModel((CostModel) subModel);
		} else if (subModel instanceof CommercialModel) {
			referenceModel.setCommercialModel((CommercialModel) subModel);
		} else if (subModel instanceof SpotMarketsModel) {
			referenceModel.setSpotMarketsModel((SpotMarketsModel) subModel);
		} else if (subModel instanceof AnalyticsModel) {
			scenarioModel.setAnalyticsModel((AnalyticsModel) subModel);
		} else if (subModel instanceof CargoModel) {
			scenarioModel.setCargoModel((CargoModel) subModel);
		} else if (subModel instanceof ActualsModel) {
			scenarioModel.setActualsModel((ActualsModel) subModel);
		} else if (subModel instanceof ScheduleModel) {
			scenarioModel.setScheduleModel((ScheduleModel) subModel);
		} else {
			log.error("Unknown sub model type: " + subModel.eClass().getName());
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

					monitor.beginTask("Import Scenario", 2);
					try {
						final DefaultImportContext context = new DefaultImportContext(decimalSeparator);

						final IMigrationRegistry migrationRegistry = Activator.getDefault().getMigrationRegistry();

						final LNGScenarioModel scenarioModel = doImport(context);
						if (scenarioModel != null) {

							monitor.worked(1);

							ImportCSVFilesPage.this.importContext = context;
							final Container container = mainPage.getScenarioContainer();
							final IScenarioService scenarioService = container.getScenarioService();

							try {
								final ScenarioInstance instance = scenarioService.insert(container, scenarioModel);

								try {
									final String scenarioVersionContext = migrationRegistry.getDefaultMigrationContext();
									final String clientVersionContext = migrationRegistry.getDefaultClientMigrationContext();
									if (scenarioVersionContext != null) {
										instance.setVersionContext(scenarioVersionContext);
										int latestContextVersion = migrationRegistry.getLatestContextVersion(scenarioVersionContext);
										// Snapshot version - so find last good version number
										if (latestContextVersion < 0) {
											latestContextVersion = migrationRegistry.getLastReleaseVersion(scenarioVersionContext);
										}
										instance.setScenarioVersion(latestContextVersion);
									}
									if (clientVersionContext != null) {
										instance.setClientVersionContext(clientVersionContext);
										int latestClientContextVersion = migrationRegistry.getLatestClientContextVersion(clientVersionContext);
										// Snapshot version - so find last good version number
										if (latestClientContextVersion < 0) {
											latestClientContextVersion = migrationRegistry.getLastReleaseClientVersion(clientVersionContext);
										}
										instance.setClientScenarioVersion(latestClientContextVersion);
									}
								} catch (final IllegalArgumentException e) {
									log.error(e.getMessage(), e);
									setErrorMessage(e.getMessage());
								}
								monitor.worked(1);

								instance.setName(mainPage.getFileName());

								final Metadata metadata = instance.getMetadata();
								metadata.setCreated(new Date());
								metadata.setLastModified(new Date());
								metadata.setContentType("com.mmxlabs.shiplingo.platform.models.manifest.scnfile");

								ImportCSVFilesPage.this.setScenarioInstance(instance);

							} catch (final IOException e) {
								// NOTE: in Java SE 7 we can incorporate this into the previous
								// exception block as catch(final IllegalArgumentException|IOException e)
								log.error(e.getMessage(), e);
								setErrorMessage(e.getMessage());
							}
						}
					} finally {
						monitor.done();
					}
				}
			});
		} catch (final InvocationTargetException e) {
			return null;
		} catch (final InterruptedException e) {
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
