/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.bugreport;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.html.HtmlEscapers;
import com.mmxlabs.rcp.common.appversion.VersionHelper;
import com.mmxlabs.rcp.common.bugreporting.BugReportException;
import com.mmxlabs.rcp.common.bugreporting.BugReportExceptionHandler;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;

public class BugReportWizard extends Wizard implements IImportWizard {

	private static final Logger LOG = LoggerFactory.getLogger(BugReportWizard.class);

	private BugReportScenarioSelectionPage bip;
	private List<ScenarioInstance> selectedScenarios;
	private final ScenarioInstance currentScenario;

	public BugReportWizard(final ScenarioInstance scenarioInstance, final String windowTitle) {
		currentScenario = scenarioInstance;
		setWindowTitle(windowTitle);
	}

	@Override
	public void init(final IWorkbench workbench, final IStructuredSelection selection) {
		bip = new BugReportScenarioSelectionPage("selectScenarios", currentScenario);
		this.setForcePreviousAndNextButtons(false);
		this.setNeedsProgressMonitor(true);
	}

	@Override
	public void addPages() {
		super.addPages();
		addPage(bip);
	}

	@Override
	public boolean performFinish() {
		selectedScenarios = bip.getSelectedScenarios();
		final Path tempDirectory;
		try {
			tempDirectory = Files.createTempDirectory(ScenarioStorageUtil.getTempDirectory().toPath(), "debug");
		} catch (final IOException e) {
			LOG.error("Error creating temp folder for bug report " + e.getMessage(), e);
			return false;
		}

		final boolean useSavedState = false;
		final boolean incldueErrorLog = bip.isIncludeErrorLog();

		final String subject = bip.getSubject();
		String bodyMsg = bip.getBody();

		final IRunnableWithProgress runnable = monitor -> {

			final List<ScenarioInstance> scenarios = getSelectedScenarios();
			final List<BugReportException.AttachedFile> attachments = new LinkedList<>();
			for (final ScenarioInstance scenarioInstance : scenarios) {
				final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenarioInstance);
				try (IScenarioDataProvider sdp = modelRecord.aquireScenarioDataProvider("BugReportWizard:1")) {
					final File file = Files.createTempFile(tempDirectory, "scenario", ".lingo").toFile();
					ScenarioStorageUtil.storeCopyToFile(sdp, file);

					attachments.add(new BugReportException.AttachedFile(file, scenarioInstance.getName()));
				} catch (final IOException e) {
					LOG.error("Error saving scenario for bug report " + e.getMessage(), e);
				}
			}

			if (incldueErrorLog) {
				final File errorLog = new File(tempDirectory.toFile(), "error.log");
				try {
					Files.copy(Platform.getLogFileLocation().toFile().toPath(), errorLog.toPath());

					attachments.add(new BugReportException.AttachedFile(errorLog, "error.log"));

				} catch (final IOException e) {
					LOG.error("Error saving error log for bug report " + e.getMessage(), e);
				}

			}

			// Email body will be in HTML format, so sanitise
			String msg = bodyMsg;
			msg = HtmlEscapers.htmlEscaper().escape(msg);
			msg = msg.replace("\n", "<br>");

			// Add in lingo version
			{
				final String clientVersion = VersionHelper.getInstance().getClientVersion();
				if (clientVersion != null) {
					msg = msg + "<br><br>" + HtmlEscapers.htmlEscaper().escape("LiNGO Version: " + clientVersion);
				} else {
					msg = msg + "<br><br>" + HtmlEscapers.htmlEscaper().escape("LiNGO Version: <unknown>");
				}
			}

			// TODO: Try to look for users outlook sig

			// https://stackoverflow.com/questions/55815930/how-to-include-signature-in-outlook-javamail
			final BugReportException bugReport = new BugReportException(tempDirectory, "", "", subject, msg, attachments);
			final BugReportExceptionHandler handler = new BugReportExceptionHandler();
			handler.handleBugReportException(bugReport, false);
		};

		getContainer().getShell().getDisplay().syncExec(() -> {
			try {
				getContainer().run(true, false, runnable);
			} catch (final Exception e) {
				LOG.error(e.getMessage(), e);
			}
		});

		return true;
	}

	public List<ScenarioInstance> getSelectedScenarios() {
		return selectedScenarios;
	}

	@Override
	public boolean canFinish() {
		return bip.isPageComplete();
	}
}
