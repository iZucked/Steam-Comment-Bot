/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.models.manifest.importWizards;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.mmxcore.MMXCoreFactory;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.ui.editors.util.EditorUtils;
import com.mmxlabs.models.util.Activator;
import com.mmxlabs.models.util.importer.CSVReader;
import com.mmxlabs.models.util.importer.IImportContext;
import com.mmxlabs.models.util.importer.ISubmodelImporter;
import com.mmxlabs.models.util.importer.impl.DefaultImportContext;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.Metadata;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.shiplingo.platform.models.manifest.ManifestJointModel;
import com.mmxlabs.shiplingo.platform.models.manifest.wizards.ScenarioServiceNewScenarioPage;

public class ImportCSVFilesPage extends WizardPage {

	private static final Logger log = LoggerFactory.getLogger(ImportCSVFilesPage.class);

	private class Chunk {
		public final EClass eClass;
		public final ISubmodelImporter importer;
		public final Map<String, String> keys;
		public final Map<String, String> friendlyNames = new HashMap<String, String>();

		public final Map<String, FileFieldEditor> editors = new HashMap<String, FileFieldEditor>();

		public Chunk(final EClass eClass, final ISubmodelImporter importer) {
			super();
			this.eClass = eClass;
			this.importer = importer;
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

	private final List<Chunk> chunks = new LinkedList<Chunk>();

	private IImportContext importContext;

	private final ScenarioServiceNewScenarioPage mainPage;

	private ScenarioInstance scenarioInstance;

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
		for (final EClass subModelClass : ManifestJointModel.getSubmodelClasses()) {
			final ISubmodelImporter importer = Activator.getDefault().getImporterRegistry().getSubmodelImporter(subModelClass);
			if (importer == null)
				continue;
			final Chunk chunk = new Chunk(subModelClass, importer);
			final Map<String, String> parts = importer.getRequiredInputs();
			chunks.add(chunk);
			chunk.friendlyNames.putAll(parts);
			if (parts.keySet().isEmpty())
				continue;
			final Group g = new Group(top, SWT.NONE);
			g.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			g.setLayout(new RowLayout(SWT.VERTICAL));
			g.setText(EditorUtils.unmangle(subModelClass.getName()));
			for (final Map.Entry<String, String> entry : parts.entrySet()) {
				final FileFieldEditor ffe = new FileFieldEditor(entry.getKey(), entry.getValue(), g);
				ffe.getTextControl(g).addModifyListener(new ModifyListener() {
					@Override
					public void modifyText(final ModifyEvent e) {
						String stringValue = ffe.getStringValue();
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

		final Button auto = new Button(top, SWT.NONE);
		auto.setText("Choose &Directory...");

		auto.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				// display file open dialog and then fill out files if the exist.
				final DirectoryDialog dd = new DirectoryDialog(getShell());
				final String d = dd.open();

				if (d != null) {
					final File dir = new File(d);
					if (dir.exists() && dir.isDirectory()) {
						for (final Chunk c : chunks) {
							c.setFromDirectory(dir);
						}
					}
				}
			}
		});

		c1.setContent(top);
		// c1.setExpandVertical(true);
		c1.setExpandHorizontal(true);
		// c1.set
		top.setSize(top.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		setControl(c1);
	}

	public MMXRootObject doImport(final DefaultImportContext context, final Collection<EObject> models) {
		final MMXRootObject root = MMXCoreFactory.eINSTANCE.createMMXRootObject();
		// final DefaultImportContext context = new DefaultImportContext();

		for (final Chunk c : chunks) {
			final HashMap<String, CSVReader> readers = new HashMap<String, CSVReader>();
			for (final String key : c.keys.keySet()) {
				try {
					final CSVReader r = new CSVReader(c.keys.get(key));
					readers.put(key, r);
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
			try {
			final UUIDObject subModel = c.importer.importModel(readers, context);
			models.add(subModel);
			root.addSubModel(subModel);
			} catch (Throwable th) {
				th.printStackTrace();
			}
			
		}

		context.setRootObject(root);

		context.run();
		return root;
	}

	@Override
	public IWizardPage getNextPage() {
		setMessage("");
		this.importContext = null;

		try {
			getContainer().run(false, false, new IRunnableWithProgress() {

				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

					monitor.beginTask("Import Scenario", 2);
					try {
						final DefaultImportContext context = new DefaultImportContext();

						final List<EObject> models = new LinkedList<EObject>();
						if (doImport(context, models) != null) {

							monitor.worked(1);

							ImportCSVFilesPage.this.importContext = context;
							final Container container = mainPage.getScenarioContainer();
							final IScenarioService scenarioService = container.getScenarioService();

							try {
								final ScenarioInstance instance = scenarioService.insert(container, Collections.<ScenarioInstance> emptySet(), models);
								monitor.worked(1);

								instance.setName(mainPage.getFileName());

								final Metadata metadata = instance.getMetadata();
								metadata.setCreated(new Date());
								metadata.setLastModified(new Date());
								metadata.setContentType("com.mmxlabs.shiplingo.platform.models.manifest.scnfile");

								ImportCSVFilesPage.this.setScenarioInstance(instance);

							} catch (final IOException e) {
								log.error(e.getMessage(), e);
								setErrorMessage(e.getMessage());
							}
						}
					} finally {
						monitor.done();
					}
				}
			});
		} catch (InvocationTargetException e) {
			return null;
		} catch (InterruptedException e) {
			return null;
		}

		return super.getNextPage();
	}

	public boolean canFlipToNextPage() {
		return isPageComplete();
	}

	public ScenarioInstance getScenarioInstance() {
		return scenarioInstance;
	}

	protected void setScenarioInstance(ScenarioInstance scenarioInstance) {
		this.scenarioInstance = scenarioInstance;
	}
}
