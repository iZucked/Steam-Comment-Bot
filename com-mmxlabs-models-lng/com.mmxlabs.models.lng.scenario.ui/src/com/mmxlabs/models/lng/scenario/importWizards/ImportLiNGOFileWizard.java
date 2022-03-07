/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.importWizards;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;
import com.mmxlabs.scenario.service.model.util.ScenarioServiceUtils;

public class ImportLiNGOFileWizard extends Wizard implements IImportWizard {

	private static final Logger log = LoggerFactory.getLogger(ImportLiNGOFileWizard.class);

	private ImportLiNGOFilePage filesPage;

	public ImportLiNGOFileWizard() {
		super();
		this.setNeedsProgressMonitor(true);
	}

	@Override
	public boolean performFinish() {
		if (filesPage.getScenarioContainer() == null || filesPage.getFilePath() == null) {
			return false;
		}

		final String filePath = filesPage.getFilePath();
		try {
			getContainer().run(true, false, new IRunnableWithProgress() {

				@Override
				public void run(final IProgressMonitor parentMonitor) throws InvocationTargetException, InterruptedException {

					parentMonitor.beginTask("Import Scenario", 9);
					final SubMonitor monitor = SubMonitor.convert(parentMonitor);
					try {

						final Container container = filesPage.getScenarioContainer();
						assert container != null;

						final Set<String> existingNames = ScenarioServiceUtils.getExistingNames(container);
						final File file = new File(filePath);
						final String scenarioName = ScenarioServiceUtils.stripFileExtension(file.getName());
						// final EObject rootObjectCopy;
						final URL scenarioURL = file.toURI().toURL();
						// SubMonitor - remember to be careful, only one #split monitor will work at a
						// time.
						ScenarioStorageUtil.withExternalScenarioFromResourceURLConsumer(scenarioURL, (modelRecord, modelReference) -> {
							try {
								// This is the *second* progress monitor. This is expected to be called once the
								// other #split monitor has completed.
								ScenarioServiceUtils.copyScenario(modelRecord, container, scenarioName, existingNames, monitor.split(4));
							} catch (final Exception e) {
								log.error(e.getMessage(), e);
							}
						}, monitor.split(5));
					} catch (final Exception e) {
						log.error(e.getMessage());
					} finally {
						monitor.done();
					}
				}
			});
			return true;
		} catch (final Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public void init(final IWorkbench workbench, final IStructuredSelection selection) {
		setWindowTitle("Import LiNGO file Wizard"); // NON-NLS-1
		setNeedsProgressMonitor(true);
		filesPage = new ImportLiNGOFilePage(selection);
	}

	@Override
	public void addPages() {
		super.addPages();
		addPage(filesPage);
	}

	@Override
	public boolean canFinish() {
		return filesPage.isPageComplete();
	}
}
