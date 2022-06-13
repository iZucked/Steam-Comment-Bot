/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.customizable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.hub.DataHubServiceProvider;
import com.mmxlabs.lingo.reports.internal.Activator;
import com.mmxlabs.lingo.reports.utils.ColumnConfigurationDialog.OptionInfo;
import com.mmxlabs.lingo.reports.views.schedule.ScheduleBasedReportBuilder;
import com.mmxlabs.lingo.reports.views.schedule.ScheduleSummaryReport;
import com.mmxlabs.models.ui.tabular.columngeneration.ColumnBlock;
import com.mmxlabs.models.ui.tabular.columngeneration.IColumnInfoProvider;
import com.mmxlabs.rcp.common.dialogs.InputDialogForSWTBot;
import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconMode;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;

public class CustomReportsManagerDialog extends TrayDialog {

	public static final String WIDGET_CUSTOM_REPORTS_VIEWER = "customReportsViewer";
	public static final String WIDGET_REPORT_NAME_TEXT = "text.name.report";

	private static final String ORG_ECLIPSE_SWTBOT_WIDGET_KEY = "org.eclipse.swtbot.widget.key";

	private static Logger logger = LoggerFactory.getLogger(CustomReportsManagerDialog.class);

	private static final String DIFF_TITLE = "In diff mode";

	private static final String ROWS_TITLE = "Show rows for";

	/** The list contains columns that are currently visible in viewer */
	private final List<String> visible = new ArrayList<>();
	/** The list contains columns that are note shown in viewer */
	private final List<String> nonVisible = new ArrayList<>();

	private final Map<String, ColumnBlock> blockIdToColumnBlock = new HashMap<>();

	private final List<CustomReportStatus> userReportDefinitions = new LinkedList<>();

	private final List<CustomReportStatus> teamReportDefinitions = new LinkedList<>();

	private final StoreType currentStoreType = StoreType.USER;

	private @Nullable CustomReportStatus current;

	private Button userButton;
	private Button teamButton;

	private TableViewer visibleViewer;
	private TableViewer nonVisibleViewer;
	private TableViewer customReportsViewer;

	private Button upButton;
	private Button downButton;

	private Button toVisibleBtt;
	private Button toNonVisibleBtt;

	private Button newBtn;
	private Button saveBtn;
	private Button publishBtn;
	private Button deleteBtn;
	private Button copyBtn;
	private Button renameBtn;
	private Button discardBtn;

	private Button refreshBtn;

	private Point tableLabelSize;

	private final ScheduleBasedReportBuilder builder = new ScheduleBasedReportBuilder();

	private final List<CheckboxInfoManager> checkboxInfo = new ArrayList<>();

	private final Image nonVisibleIcon = CommonImages.getImage(IconPaths.Read, IconMode.Disabled);
	private final Image visibleIcon = CommonImages.getImage(IconPaths.Read, IconMode.Enabled);

	private boolean changesMade = false;

	private final Comparator<String> comparator = (arg0, arg1) -> getColumnIndex(arg0) - getColumnIndex(arg1);

	public CustomReportsManagerDialog(final Shell parentShell) {
		super(parentShell);
		initData();
	}

	@Override
	public int open() {
		int retCode = CANCEL;
		try {
			retCode = super.open();

			// Could possible happen if the click on X in the top right corner.
			if (this.unsavedOrUnpublishedChanges()) {
				final int savePublishNow = MessageDialog.open(MessageDialog.QUESTION_WITH_CANCEL, getShell(), "Warning",
						"Some changes have been made to user reports without changes or team reports without publishing. Save and/or publish these now?", SWT.NONE, "Save/Publish", "Cancel");
				if (savePublishNow == 0) {
					this.savedOrPublishAnyChanges();
				}
			}

			if (retCode == OK && this.changesMade) {
				final int restartNow = MessageDialog.open(MessageDialog.QUESTION_WITH_CANCEL, getShell(), "Warning", "LiNGO must be restarted to update views menus. Restart now?", SWT.NONE,
						"Restart now", "Later");
				if (restartNow == 0) {
					PlatformUI.getWorkbench().restart();
				}
			}
		} catch (final Exception ex) {
			logger.error("Problem opening CustomReportsManagerDialog", ex);
		}
		return retCode;
	}

	@Override
	protected void okPressed() {
		if (!this.checkForUnsavedUnpublishedChanges()) {
			// Discard changes and exit the dialog.
			super.okPressed();
		}
	}

	private void initData() {
		this.loadDefaults();

		for (final var def : CustomReportsRegistry.getInstance().readUserCustomReportDefinitions()) {
			userReportDefinitions.add(CustomReportStatus.clean(StoreType.USER, def));
		}
		for (final var def : CustomReportsRegistry.getInstance().readTeamCustomReportDefinitions()) {
			teamReportDefinitions.add(CustomReportStatus.clean(StoreType.TEAM, def));
		}

		addDialogCheckBoxes();
	}

	@Override
	protected void configureShell(final Shell shell) {
		super.configureShell(shell);
		shell.setText("Reports Manager");
	}

	@Override
	protected void createButtonsForButtonBar(final Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
	}

	protected void addDialogCheckBoxes() {
		this.addCheckBoxInfo(ROWS_TITLE, builder.ROW_FILTER_ALL, builder::getRowFilterInfo);
		this.addCheckBoxInfo(DIFF_TITLE, builder.DIFF_FILTER_ALL, builder::getDiffFilterInfo);
	}

	@Override
	protected Control createDialogArea(final Composite parent) {
		final Composite dialogArea = (Composite) super.createDialogArea(parent);

		dialogArea.setLayout(new GridLayout(1, true));

		initializeDialogUnits(dialogArea);

		createDialogContentArea(dialogArea).setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		applyDialogFont(dialogArea);

		return dialogArea;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.internal.views.markers.ViewerSettingsAndStatusDialog#
	 * createDialogContentArea(org.eclipse.swt.widgets.Composite)
	 */
	private Control createDialogContentArea(final Composite dialogArea) {
		final Composite composite = new Composite(dialogArea, SWT.NONE);
		final GridLayout gridLayout = new GridLayout(6, false);
		gridLayout.marginHeight = 0;
		composite.setLayout(gridLayout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		createCustomReportsTable(composite);

		createNewSavePublishDeleteButtons(composite);

		createInvisibleTable(composite);
		createMoveButtons(composite);
		createVisibleTable(composite);
		createUpDownBtt(composite);

		for (final CheckboxInfoManager manager : checkboxInfo) {
			createCheckBoxes(composite, manager);
		}

		final Object element = visibleViewer.getElementAt(0);
		if (element != null) {
			visibleViewer.setSelection(new StructuredSelection(element));
		}
		visibleViewer.getTable().setFocus();

		if (this.userButton.getSelection()) {
			activateUserMode();
		} else {
			activateTeamMode();
		}

		return composite;
	}

	private Control createCheckBoxes(final Composite parent, final CheckboxInfoManager manager) {
		final Composite composite = new Composite(parent, SWT.NONE);
		if (manager != null && manager.options != null && manager.options.length > 0) {
			final GridLayout compositeLayout = new GridLayout();
			compositeLayout.marginHeight = 0;
			compositeLayout.marginWidth = 0;
			composite.setLayout(compositeLayout);
			composite.setLayoutData(new GridData(SWT.NONE, SWT.FILL, false, true));

			final Label label = new Label(composite, SWT.NONE);
			label.setText(manager.title);
			final Set<String> store = manager.store.get();
			for (final OptionInfo option : manager.options) {
				final Button button = new Button(composite, SWT.CHECK);
				option.button = button;
				button.setText(option.label);
				button.setSelection(store.contains(option.id));
				button.addListener(SWT.Selection, event -> {
					if (store.contains(option.id)) {
						store.remove(option.id);
					} else {
						store.add(option.id);
					}
					handleOptionChanged();
				});
			}
		}
		return composite;
	}

	/**
	 * The new, save, publish and delete buttons to the .
	 * 
	 * @param parent
	 */
	private Control createNewSavePublishDeleteButtons(final Composite parent) {
		final Composite composite = new Composite(parent, SWT.NONE);
		final GridLayout compositeLayout = new GridLayout();
		compositeLayout.marginHeight = 0;
		compositeLayout.marginWidth = 0;
		composite.setLayout(compositeLayout);
		composite.setLayoutData(new GridData(SWT.NONE, SWT.FILL, false, true));

		final Composite bttArea = new Composite(composite, SWT.NONE);
		final GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		bttArea.setLayout(layout);
		bttArea.setLayoutData(new GridData(SWT.NONE, SWT.CENTER, false, true));

		newBtn = new Button(bttArea, SWT.PUSH);
		newBtn.setText("New");
		newBtn.addListener(SWT.Selection, event -> handleNewBtn());
		setButtonLayoutData(newBtn);
		((GridData) newBtn.getLayoutData()).verticalIndent = tableLabelSize.y;
		newBtn.setEnabled(true);

		copyBtn = new Button(bttArea, SWT.PUSH);
		copyBtn.setText("Copy");
		copyBtn.addListener(SWT.Selection, event -> handleCopyBtn());
		setButtonLayoutData(copyBtn);
		copyBtn.setEnabled(false);

		renameBtn = new Button(bttArea, SWT.PUSH);
		renameBtn.setText("Rename");
		renameBtn.addListener(SWT.Selection, event -> handleRenameBtn());
		setButtonLayoutData(renameBtn);
		renameBtn.setEnabled(false);

		discardBtn = new Button(bttArea, SWT.PUSH);
		discardBtn.setText("Revert");
		discardBtn.addListener(SWT.Selection, event -> handleDiscardBtn());
		setButtonLayoutData(discardBtn);
		discardBtn.setEnabled(false);

		deleteBtn = new Button(bttArea, SWT.PUSH);
		deleteBtn.setText("Delete");
		deleteBtn.addListener(SWT.Selection, CustomReportsManagerDialog.this::handleDeleteBtn);
		setButtonLayoutData(deleteBtn);
		deleteBtn.setEnabled(false);

		// This button doesn't do anything, just placed to create a button sized gap
		// between buttons.
		final Button spacerBtn = new Button(bttArea, SWT.PUSH);
		setButtonLayoutData(spacerBtn);
		spacerBtn.setVisible(false);

		saveBtn = new Button(bttArea, SWT.PUSH);
		saveBtn.setText("Save");
		saveBtn.addListener(SWT.Selection, CustomReportsManagerDialog.this::handleSaveBtn);
		setButtonLayoutData(saveBtn);
		saveBtn.setEnabled(false);

		publishBtn = new Button(bttArea, SWT.PUSH);
		publishBtn.setText("Publish");
		publishBtn.addListener(SWT.Selection, CustomReportsManagerDialog.this::handlePublishBtn);
		setButtonLayoutData(publishBtn);
		publishBtn.setEnabled(false);

		refreshBtn = new Button(bttArea, SWT.PUSH);
		refreshBtn.setText("Refresh");
		refreshBtn.addListener(SWT.Selection, CustomReportsManagerDialog.this::handleRefreshBtn);
		setButtonLayoutData(refreshBtn);
		refreshBtn.setEnabled(true);

		return bttArea;
	}

	private void handleRefreshBtn(final Event event) {
		if (this.teamButton.getSelection()) {

			// Check for changes first.
			if (!this.checkForUnsavedUnpublishedChanges()) {

				BusyIndicator.showWhile(Display.getCurrent(), () -> {
					try {
						CustomReportsRegistry.getInstance().refreshTeamReports();
					} catch (final IOException e) {
						displayErrorDialog("Could not connect to DataHub to refresh reports.");
						return;
					}
					try {
						Thread.sleep(1000);
					} catch (final InterruptedException e) {
					}

					teamReportDefinitions.clear();
					for (final var def : CustomReportsRegistry.getInstance().readTeamCustomReportDefinitions()) {
						teamReportDefinitions.add(CustomReportStatus.clean(StoreType.TEAM, def));
					}

					updateReports(StoreType.TEAM);
				});

			}
		}
	}

	private void handleDiscardBtn() {
		discardCurrentlySelectedChanges();
	}

	private void displayErrorDialog(final String errorMessage) {
		MessageDialog.openError(getShell(), "Error", errorMessage);
	}

	private void discardCurrentlySelectedChanges() {
		final CustomReportStatus currentReport = this.current;

		if (currentReport.newReport) {
			// Remove newly created, but unsaved report.
			deleteReport(currentReport);
		} else {
			// Undo any changes to the visible / hidden columns by reloading the currently
			// selected report definition.
			currentReport.revert();
			currentReport.saved = true;

			updateViewWithReportDefinition(this.current);
		}

		// Have to make some changes to be able to discard again.
		this.discardBtn.setEnabled(false);
		this.saveBtn.setEnabled(false);
		this.publishBtn.setEnabled(false);

	}

	private void deleteReport(final CustomReportStatus toDelete) {
		switch (toDelete.storeType) {
		case USER:
			if (!toDelete.newReport) {
				CustomReportsRegistry.getInstance().deleteUserReport(toDelete.report);
			}
			this.userReportDefinitions.remove(toDelete);
			break;
		case TEAM:
			if (!toDelete.newReport) {
				try {
					CustomReportsRegistry.getInstance().removeFromDatahub(toDelete.report);
				} catch (final Exception ex) {
					final String errorMessage = "Error connecting to datahub whilst attempting to remove report \"" + toDelete.report.getName()
							+ "\" from team folder. Please check error log for more details.";
					displayErrorDialog(errorMessage);
					logger.error(errorMessage, ex);
					return;
				}
			}
			CustomReportsRegistry.getInstance().deleteTeamReport(toDelete.report);
			this.teamReportDefinitions.remove(toDelete);
			break;
		default:
			logger.error("Unimplemented store type for deleteReport method: {0}", toDelete.storeType.toString());
			break;
		}
		if (!toDelete.newReport) {
			changesMade = true;
		}
		// Set these flags to clear any dirty status
		toDelete.saved = true;
		toDelete.newReport = false;
		this.loadDefaults();
		this.refreshViewers();
	}

	private void handleRenameBtn() {

		final CustomReportStatus status = this.current;
		if (status != null) {
			final String type = status.storeType == StoreType.USER ? "save" : "publish";
			final InputDialog dialog = new InputDialogForSWTBot(this.getShell(), // Shell
					String.format("Rename and %s the currently selected report", type), // Title
					"Choose a new name for the report and press OK to " + type, // message
					status.report.getName(), // Current name
					getReportNameValidator(), // Validator
					WIDGET_REPORT_NAME_TEXT // ID for text field
			);

			if (dialog.open() == Window.OK) {
				final String name = dialog.getValue();
				status.report.setName(name);
				if (this.currentStoreType == StoreType.USER) {
					CustomReportsRegistry.getInstance().writeToJSON(status.report);
					this.changesMade = true;
					status.newReport = false;
					status.saved = true;
				} else {
					try {
						CustomReportsRegistry.getInstance().publishReport(status.report);
						status.newReport = false;
						status.saved = true;
						this.changesMade = true;
					} catch (final Exception e) {
						final String errorMsg = "Problem publishing renamed report \"" + status.report.getName() + "\" to Data Hub.";
						logger.error(errorMsg, e);
						displayErrorDialog(errorMsg);
					}
				}
				this.refreshViewers();

			}
		} else {
			logger.error("No report currently selected to rename.");
		}
	}

	private CustomReportNameValidator getReportNameValidator() {
		final List<String> existingNames = getExistingReportNames();
		return new CustomReportNameValidator(existingNames);
	}

	private List<String> getExistingReportNames() {
		final List<String> existingNames = new ArrayList<>();
		userReportDefinitions.stream().forEach(r -> existingNames.add(r.report.getName().toLowerCase()));
		teamReportDefinitions.stream().forEach(r -> existingNames.add(r.report.getName().toLowerCase()));
		return existingNames;
	}

	private void handleDeleteBtn(final Event event) {

		final CustomReportStatus status = this.current;
		if (status != null) {
			if (!MessageDialog.openQuestion(getShell(), "Are you sure?", "Are you sure you want to delete the report \"" + status.report.getName() + "\"")) {
				return;
			}
			deleteReport(status);
		}
	}

	private CustomReportStatus checkForTeamReport(final String name) {
		for (final CustomReportStatus crd : this.teamReportDefinitions) {
			if (crd.report.getName().equalsIgnoreCase(name)) {
				return crd;
			}
		}
		return null;
	}

	private void handlePublishBtn(final Event event) {
		final CustomReportStatus existing = this.current;
		if (existing != null) {
			final CustomReportStatus newTeamReportDefinition;

			// User report - publish as new or replace existing team report.
			if (existing.storeType == StoreType.USER) {
				if (!existing.saved) {
					final boolean save = MessageDialog.open(MessageDialog.QUESTION, this.getShell(), "Save before publish?",
							"Report " + existing.report.getName() + " has unsaved changed. Save before publishing?", SWT.NONE);

					if (save) {

						// Save down, before publishing for user report.
						CustomReportsRegistry.getInstance().writeToJSON(existing.report);
						existing.createCheckpoint();

						// Update change state
						existing.saved = true;
						existing.newReport = false;

						this.changesMade = true;
						this.saveBtn.setEnabled(false);
						this.discardBtn.setEnabled(false);
					}
				}

				// Check if report with that name already exists and if so prompt the user if
				// they want to overwrite it or cancel.
				final CustomReportStatus existingTeamReport = this.checkForTeamReport(existing.report.getName());
				if (existingTeamReport == null) {

					// If we are adding a user report to team, we need to clone it and create a new
					// UUID as per PS, so that team version appears separately from user version.

					// Create a new definition - copy name, new UUID
					newTeamReportDefinition = createLightCopyOfReportDefinition(StoreType.TEAM, existing.report);
					// Set the current column definition into the report
					updateReportDefinitionWithChangesFromDialog(newTeamReportDefinition.report);
				} else {
					// If it is already a team report, ask the user if they want to overwrite it
					// with their new one.
					final int overwrite = MessageDialog.open(MessageDialog.QUESTION_WITH_CANCEL, getShell(), "Overwrite existing team report?",
							"There is an existing team report " + existingTeamReport.report.getName() + ". Do you want to overwrite this?", SWT.NONE, "Overwrite", "Cancel");

					if (overwrite == 0) {
						final CustomReportDefinition copy = new CustomReportDefinition();
						copy.setUuid(existingTeamReport.report.getUuid());
						copy.setName(existingTeamReport.report.getName());
						copy.setType(existingTeamReport.report.getType());
						newTeamReportDefinition = CustomReportStatus.newReport(StoreType.TEAM, copy);
						newTeamReportDefinition.newReport = false;

						updateReportDefinitionWithChangesFromDialog(newTeamReportDefinition.report);
					} else {
						// Cancel and do nothing.
						return;
					}
				}
			} else if (this.currentStoreType == StoreType.TEAM) {
				// Already a team report
				newTeamReportDefinition = existing;
			} else {
				throw new IllegalStateException();
			}

			try {
				// Publish the report definition
				CustomReportsRegistry.getInstance().publishReport(newTeamReportDefinition.report);
			} catch (final Exception e) {
				final String errorMsg = "Problem publishing report \"" + newTeamReportDefinition.report.getName() + "\" to Data Hub.";
				logger.error(errorMsg, e);
				displayErrorDialog(errorMsg);

				return;
			}

			newTeamReportDefinition.createCheckpoint();
			newTeamReportDefinition.saved = true;
			newTeamReportDefinition.newReport = false;

			final int idx = teamReportDefinitions.indexOf(existing);
			if (idx >= 0) {
				// Replace existing...
				teamReportDefinitions.set(idx, newTeamReportDefinition);
			} else {
				// Or add a new one
				teamReportDefinitions.add(newTeamReportDefinition);
			}

			// Make sure we refresh team reports if team reports selected.
			this.changesMade = true;

			this.publishBtn.setEnabled(false);

		}
	}

	private CustomReportStatus createLightCopyOfReportDefinition(final StoreType type, final CustomReportDefinition rd) {
		final CustomReportDefinition copy = new CustomReportDefinition();
		copy.setUuid(ScheduleSummaryReport.UUID_PREFIX + UUID.randomUUID().toString());
		copy.setName(rd.getName());
		copy.setType("ScheduleSummaryReport");
		return CustomReportStatus.newReport(type, copy);
	}

	private String getUserCopyName(final String currentReportName) {
		int copyNo = 0;

		String copyName = currentReportName + " (User Copy)";
		boolean copyNameFound = false;

		// Check user folder for name.
		while (!copyNameFound) {
			copyNameFound = true;
			for (final CustomReportStatus crd : this.userReportDefinitions) {
				if (crd.report.getName().equalsIgnoreCase(copyName)) {
					copyNo++;
					copyName = currentReportName + " (User Copy " + Integer.toString(copyNo) + ")";
					copyNameFound = false;
					break;
				}
			}
		}

		return copyName;
	}

	private void handleSaveBtn(final Event event) {

		CustomReportStatus toSave = current;
		if (toSave != null) {

			// If we are copying from team folder, clone and change UUID.
			if (teamButton.getSelection()) {
				// Create report definition object with defaults and save.
				final CustomReportDefinition copy = new CustomReportDefinition();

				copy.setUuid(ScheduleSummaryReport.UUID_PREFIX + UUID.randomUUID().toString());
				final String copyName = getUserCopyName(toSave.report.getName());
				copy.setName(copyName);
				copy.setType("ScheduleSummaryReport");

				updateReportDefinitionWithChangesFromDialog(copy);

				toSave = CustomReportStatus.newReport(StoreType.USER, copy);
				this.userReportDefinitions.add(toSave);
			}

			CustomReportsRegistry.getInstance().writeToJSON(toSave.report);
			toSave.createCheckpoint();
			toSave.saved = true;
			toSave.newReport = false;

			changesMade = true;
			this.discardBtn.setEnabled(false);
			this.saveBtn.setEnabled(false);

			refreshViewers();
		}
	}

	private void handleNewBtn() {
		createNewViewDefinition(false, "New custom report view", null);
	}

	private void handleCopyBtn() {
		String nameHint = null;
		if (this.current != null) {
			nameHint = this.current.report.getName() + " (Copy)";
		}
		createNewViewDefinition(true, "Copy custom report view", nameHint);
	}

	private void createNewViewDefinition(final boolean initialiseWithSelectedReport, final String title, @Nullable String nameHint) {
		if (nameHint == null) {
			nameHint = "<Enter a new view name>";
		}
		final InputDialog dialog = new InputDialogForSWTBot(this.getShell(), title, "Choose name for the new custom report.", nameHint, getReportNameValidator(), WIDGET_REPORT_NAME_TEXT);

		if (dialog.open() == Window.OK) {
			final String name = dialog.getValue();

			// Create report definition object with defaults and save.
			final CustomReportDefinition reportDefinition = new CustomReportDefinition();

			reportDefinition.setUuid(ScheduleSummaryReport.UUID_PREFIX + UUID.randomUUID().toString());
			reportDefinition.setName(name);
			reportDefinition.setType("ScheduleSummaryReport");

			if (!initialiseWithSelectedReport) {
				this.loadDefaults();
			}

			updateReportDefinitionWithChangesFromDialog(reportDefinition);
			final CustomReportStatus newReport = CustomReportStatus.newReport(currentStoreType, reportDefinition);

			// Add to report definitions list and select it.
			if (this.userButton.getSelection()) {
				this.userReportDefinitions.add(newReport);
				this.customReportsViewer.setInput(this.userReportDefinitions);
			} else if (this.teamButton.getSelection()) {
				this.teamReportDefinitions.add(newReport);
				this.customReportsViewer.setInput(this.teamReportDefinitions);
			} else {
				logger.error("Mode not fully implemented for: private void CustomReportsManagerDialog.createNewViewDefinition(boolean initialiseWithSelectedReport, String title)");
			}
			this.customReportsViewer.setSelection(new StructuredSelection(newReport));
		}
	}

//	private void addChangedReport(final CustomReportDefinition rd) {
//		final Change change = new Change();
//		change.report = rd;
//		change.saved = false;
//		change.published = false;
//		change.storeType = this.userButton.getSelection() ? StoreType.USER : StoreType.TEAM;
//		this.uuidToChangedReports.put(rd.getUuid(), change);
//	}

	private boolean unsavedOrUnpublishedChanges() {
		for (final var status : this.userReportDefinitions) {
			if (unsavedOrUnpublishedChanges(status)) {
				return true;
			}
		}
		for (final var status : this.teamReportDefinitions) {
			if (unsavedOrUnpublishedChanges(status)) {
				return true;
			}
		}
		return false;
	}

	private boolean unsavedOrUnpublishedChanges(final CustomReportStatus status) {
		if (!status.saved || status.newReport) {
			return true;
		}

		return false;
	}

	private void savedOrPublishAnyChanges() {

		for (final var status : this.userReportDefinitions) {
			if (unsavedOrUnpublishedChanges(status)) {
				CustomReportsRegistry.getInstance().writeToJSON(status.report);
				status.createCheckpoint();
				status.saved = true;
				status.newReport = false;
				this.changesMade = true;
			}
		}
		for (final var status : this.teamReportDefinitions) {
			if (unsavedOrUnpublishedChanges(status)) {
				try {
					CustomReportsRegistry.getInstance().publishReport(status.report);

					status.createCheckpoint();
					status.saved = true;
					status.newReport = false;
				} catch (final Exception ex) {
					final String errorMsg = "Problem publishing report \"" + status.report.getName() + "\" to DataHub.";
					logger.error(errorMsg, ex);
					displayErrorDialog(errorMsg);
				}
			}
		}
	}

	private void updateReportDefinitionWithChangesFromDialog(final CustomReportDefinition reportDefinition) {
		reportDefinition.getColumns().clear();
		for (final String blockID : getVisible()) {
			reportDefinition.getColumns().add(blockID);
		}
		for (final CheckboxInfoManager cbim : this.checkboxInfo) {
			if (cbim.title.equals(ROWS_TITLE)) {
				reportDefinition.getFilters().clear();
				for (final OptionInfo oi : cbim.options) {
					if (oi.button.getSelection()) {
						reportDefinition.getFilters().add(oi.type);
					}
				}
			} else if (cbim.title.equals(DIFF_TITLE)) {
				reportDefinition.getDiffOptions().clear();
				for (final OptionInfo oi : cbim.options) {
					if (oi.button.getSelection()) {
						reportDefinition.getDiffOptions().add(oi.type);
					}
				}
			}
		}
	}

	/**
	 * The Up and Down button to change column ordering.
	 * 
	 * @param parent
	 */
	private Control createUpDownBtt(final Composite parent) {
		final Composite composite = new Composite(parent, SWT.NONE);
		final GridLayout compositeLayout = new GridLayout();
		compositeLayout.marginHeight = 0;
		compositeLayout.marginWidth = 0;
		composite.setLayout(compositeLayout);
		composite.setLayoutData(new GridData(SWT.NONE, SWT.FILL, false, true));

		final Composite bttArea = new Composite(composite, SWT.NONE);
		final GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		bttArea.setLayout(layout);
		bttArea.setLayoutData(new GridData(SWT.NONE, SWT.CENTER, false, true));
		upButton = new Button(bttArea, SWT.PUSH);
		upButton.setText(JFaceResources.getString("ConfigureColumnsDialog_up")); //$NON-NLS-1$
		upButton.addListener(SWT.Selection, event -> handleUpButton());
		setButtonLayoutData(upButton);
		((GridData) upButton.getLayoutData()).verticalIndent = tableLabelSize.y;
		upButton.setEnabled(false);

		downButton = new Button(bttArea, SWT.PUSH);
		downButton.setText(JFaceResources.getString("ConfigureColumnsDialog_down")); //$NON-NLS-1$
		downButton.addListener(SWT.Selection, event -> handleDownButton());
		setButtonLayoutData(downButton);
		downButton.setEnabled(false);
		return bttArea;
	}

	/**
	 * Creates the table that lists out custom reports in the viewer
	 * 
	 * @param parent
	 * @return {@link Control}
	 */
	private Control createCustomReportsTable(final Composite parent) {

		final Composite composite = new Composite(parent, SWT.NONE);
		final GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.marginHeight = 0;
		composite.setLayout(gridLayout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		this.userButton = new Button(composite, SWT.RADIO);
		this.userButton.setText("User reports");
		this.userButton.setSelection(true);
		this.userButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				final CustomReportStatus status = current;

				if (status != null && userButton.getSelection()) {

					if (unsavedOrUnpublishedChanges(status)) {
						if (promptToDiscardOrGoBack(status)) {
							// Discards the previously selected report selection changes.
							discardCurrentlySelectedChanges();
						} else {
							teamButton.setSelection(true);
							userButton.setSelection(false);
							updateButtonState(status);
							return;
						}
					}
				}
				activateUserMode();
				updateReports(StoreType.USER);

			}
		});
		tableLabelSize = userButton.computeSize(SWT.DEFAULT, SWT.DEFAULT);

		this.teamButton = new Button(composite, SWT.RADIO);
		this.teamButton.setText("Team reports");
		this.teamButton.setSelection(false);
		this.teamButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent e) {
				final CustomReportStatus status = current;

				if (status != null && teamButton.getSelection()) {
					if (unsavedOrUnpublishedChanges(status)) {
						if (promptToDiscardOrGoBack(status)) {
							// Discards the previously selected report selection changes.
							discardCurrentlySelectedChanges();
						} else {
							teamButton.setSelection(false);
							userButton.setSelection(true);
							updateButtonState(status);
							return;
						}
					}
				}
				activateTeamMode();
				updateReports(StoreType.TEAM);

			}
		});

		if (!CustomReportsRegistry.getInstance().hasTeamReportsCapability()) {
			// Hide the team reports view if hub does not support or user does not have
			// permissions.
			this.userButton.setVisible(false);
			this.teamButton.setVisible(false);
		}

		final Table table = new Table(composite, SWT.BORDER | SWT.SINGLE);

		final GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.widthHint = convertWidthInCharsToPixels(20);
		data.heightHint = table.getItemHeight() * 15;
		table.setLayoutData(data);
		table.setData(ORG_ECLIPSE_SWTBOT_WIDGET_KEY, WIDGET_CUSTOM_REPORTS_VIEWER); // this id is used in swtbot tests

		final TableColumn column = new TableColumn(table, SWT.NONE);
		column.setText("Custom Reports");
		table.addListener(SWT.Resize, event -> column.setWidth(table.getClientArea().width));

		customReportsViewer = new TableViewer(table);
		ColumnViewerToolTipSupport.enableFor(customReportsViewer);

		customReportsViewer.setLabelProvider(this.getReportsDefinitionNameLabelProvider());
		customReportsViewer.setContentProvider(ArrayContentProvider.getInstance());
		customReportsViewer.addSelectionChangedListener(event -> handleCustomReportSelection(event.getStructuredSelection()));
		customReportsViewer.setInput(userReportDefinitions);
		return table;
	}

	private void activateUserMode() {
		this.newBtn.setEnabled(true);
		this.saveBtn.setEnabled(false);
		this.saveBtn.setText("Save");
		this.publishBtn.setText("Add to Team");
		this.publishBtn.setEnabled(false);
		this.copyBtn.setEnabled(false);
		this.deleteBtn.setEnabled(false);
		this.renameBtn.setEnabled(false);
		this.discardBtn.setEnabled(false);
		this.refreshBtn.setVisible(false);
		disableEditingControls();
	}

	private void disableEditingControls() {
		this.upButton.setEnabled(false);
		this.downButton.setEnabled(false);
		this.toNonVisibleBtt.setEnabled(false);
		this.toVisibleBtt.setEnabled(false);
	}

	private void activateTeamMode() {
		if (CustomReportsRegistry.getInstance().hasTeamReportsPublishPermission()) {
			this.newBtn.setEnabled(true);
		} else {
			this.newBtn.setEnabled(false);
		}
		this.saveBtn.setEnabled(false);
		this.saveBtn.setText("Add to User");
		this.publishBtn.setText("Publish");
		this.publishBtn.setEnabled(false);
		this.copyBtn.setEnabled(false);
		this.deleteBtn.setEnabled(false);
		this.renameBtn.setEnabled(false);
		this.discardBtn.setEnabled(false);
		this.refreshBtn.setVisible(true);
		disableEditingControls();
	}

	/**
	 * Update the reports view
	 * 
	 * @param userReports - true, if we want to display user's own reports, false,
	 *                    to display team reports
	 */
	private void updateReports(final StoreType storeType) {
		if (storeType == StoreType.USER) {
			customReportsViewer.setInput(userReportDefinitions);
		} else if (storeType == StoreType.TEAM) {
			customReportsViewer.setInput(teamReportDefinitions);
		} else {
			throw new IllegalStateException();
		}
	}

	private void handleCustomReportSelection(final IStructuredSelection selection) {
		final List<CustomReportStatus> selectedReports = selection.toList();
		// Older comments indicate the selection can be > 1 when switching report modes
		final CustomReportStatus newCurrent = selectedReports.size() == 1 ? selectedReports.get(0) : null;
		final CustomReportStatus oldCurrent = this.current;
		if (newCurrent == oldCurrent) {
			// No change in selection
			return;
		}
		if (oldCurrent != null) {
			if (unsavedOrUnpublishedChanges(oldCurrent)) {
				if (promptToDiscardOrGoBack(oldCurrent)) {
					// Discards the previously selected report selection changes.
					this.discardCurrentlySelectedChanges();
				} else {
					customReportsViewer.setSelection(new StructuredSelection(oldCurrent));
					return;
				}
			}
		}

		this.current = newCurrent;

		updateViewWithReportDefinition(current);
		updateButtonState(current);
	}

	/**
	 * Prompt use to discard changes or return to current selection.
	 * 
	 * @return Returns true if discard selected, or false to stay put
	 */
	private boolean promptToDiscardOrGoBack(@NonNull final CustomReportStatus status) {
		final String reportName = status.report.getName();
		final String unSavedOrUnPublished = this.userButton.getSelection() ? " unsaved " : " un-published ";
		final int discardChanges = MessageDialog.open(MessageDialog.QUESTION_WITH_CANCEL, getShell(), "Warning",
				"You have" + unSavedOrUnPublished + "changes to \"" + reportName + "\". Discard these changes or go back?", SWT.NONE, "Discard", "Back");
		return discardChanges == 0;
	}

	private void updateButtonState(@Nullable final CustomReportStatus status) {

		if (status == null) {
			saveBtn.setEnabled(false);
			deleteBtn.setEnabled(false);
			copyBtn.setEnabled(false);
			renameBtn.setEnabled(false);
			publishBtn.setEnabled(false);
		} else {

			// Only enable publish/write access, if in user mode or if user has team reports
			// publish permission.
			if (status.storeType == StoreType.USER) {
				// User reports mode.
				saveBtn.setEnabled(!status.saved);
				deleteBtn.setEnabled(true);
				copyBtn.setEnabled(true);
				renameBtn.setEnabled(true);
				if (DataHubServiceProvider.getInstance().isOnlineAndLoggedIn() && CustomReportsRegistry.getInstance().hasTeamReportsPublishPermission()) {
					publishBtn.setEnabled(true);
				}
			} else {
				// Team reports mode.
				if (CustomReportsRegistry.getInstance().hasTeamReportsDeletePermission()) {
					deleteBtn.setEnabled(true);
				}
				if (CustomReportsRegistry.getInstance().hasTeamReportsReadPermission() && CustomReportsRegistry.getInstance().hasTeamReportsPublishPermission()) {
					copyBtn.setEnabled(true);
					// always allow to copy to user
					saveBtn.setEnabled(true);
				}
				if (CustomReportsRegistry.getInstance().hasTeamReportsPublishPermission()) {
					renameBtn.setEnabled(true);
					// only publish if report is unpublished
					publishBtn.setEnabled(DataHubServiceProvider.getInstance().isOnlineAndLoggedIn());
				}
			}
		}
	}

	/**
	 * Check if there are any unsaved/unpublished changes and ask the user what to
	 * do and do it. Side effects: either discards the changes or selected the
	 * previously selected report.
	 * 
	 * @return true, if we went back, false, if we discarded the previously selected
	 *         report's changes or there were no unsaved/unpublished changes.
	 */
	private boolean checkForUnsavedUnpublishedChanges() {
		if (this.current != null) {
			if (this.unsavedOrUnpublishedChanges(this.current)) {
				final String reportName = this.current.report.getName();
				final String unSavedOrUnPublished = this.userButton.getSelection() ? " unsaved " : " un-published ";
				final int discardChanges = MessageDialog.open(MessageDialog.QUESTION_WITH_CANCEL, getShell(), "Warning",
						"You have" + unSavedOrUnPublished + "changes to \"" + reportName + "\". Discard these changes or go back?", SWT.NONE, "Discard", "Back");
				if (discardChanges == 0) {
					// Discards the previously selected report selection changes.
					this.discardCurrentlySelectedChanges();
				} else {
					if (this.userButton.getSelection() && this.currentStoreType == StoreType.TEAM) {
						// Back to the correct view.
						this.userButton.setSelection(false);
						this.teamButton.setSelection(true);
						activateTeamMode();
						this.customReportsViewer.setInput(teamReportDefinitions);
					}

					if (this.teamButton.getSelection() && this.currentStoreType == StoreType.USER) {
						// Back to the correct view.
						this.userButton.setSelection(true);
						this.teamButton.setSelection(false);
						activateUserMode();
						this.customReportsViewer.setInput(userReportDefinitions);
					}

					// Go back to the previously selected report.
					this.customReportsViewer.setSelection(new StructuredSelection(this.current));

					// And ignore this new selection.
					return true;
				}
			}
		}
		return false;
	}

	private synchronized void updateViewWithReportDefinition(final @Nullable CustomReportStatus status) {

		if (status == null) {
			visible.clear();
			nonVisible.clear();
			for (final CheckboxInfoManager cbim : checkboxInfo) {
				for (final OptionInfo oi : cbim.options) {
					if (oi.button != null) {
						oi.button.setSelection(false);
					}
				}
			}

			discardBtn.setEnabled(false);
		} else {

			final CustomReportDefinition reportDefinition = status.report;

			this.loadDefaults();

			final List<String> hiddenColumnsAfter = new ArrayList<>();
			hiddenColumnsAfter.addAll(nonVisible);
			hiddenColumnsAfter.removeIf(reportDefinition.getColumns()::contains);

			final List<String> visibleColumnsAfter = new ArrayList<>();
			for (final String blockId : reportDefinition.getColumns()) {
				visibleColumnsAfter.add(blockId);
			}

			this.nonVisible.clear();
			this.nonVisible.addAll(hiddenColumnsAfter);

			this.visible.clear();
			this.visible.addAll(visibleColumnsAfter);

			for (final CheckboxInfoManager cbim : this.checkboxInfo) {
				if (cbim.title.equals(ROWS_TITLE)) {
					for (final OptionInfo oi : cbim.options) {
						if (oi.button != null) {
							oi.button.setSelection(reportDefinition.getFilters().contains(oi.type));
						}
					}
				} else if (cbim.title.equals(DIFF_TITLE)) {
					for (final OptionInfo oi : cbim.options) {
						if (oi.button != null) {
							oi.button.setSelection(reportDefinition.getDiffOptions().contains(oi.type));
						}
					}
				}
			}
			discardBtn.setEnabled(!status.saved);
		}

		this.refreshViewers();
	}

	/**
	 * Creates the table that lists out visible columns in the viewer
	 * 
	 * @param parent
	 * @return {@link Control}
	 */
	private Control createVisibleTable(final Composite parent) {

		final Composite composite = new Composite(parent, SWT.NONE);
		final GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.marginHeight = 0;
		composite.setLayout(gridLayout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		final Label label = new Label(composite, SWT.NONE);
		label.setText("Enabled Columns");

		final Table table = new Table(composite, SWT.BORDER | SWT.MULTI);

		final GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.widthHint = convertWidthInCharsToPixels(20);
		data.heightHint = table.getItemHeight() * 15;
		table.setLayoutData(data);

		final TableColumn column = new TableColumn(table, SWT.NONE);
		column.setText("Enabled Columns");
		final Listener columnResize = event -> column.setWidth(table.getClientArea().width);
		table.addListener(SWT.Resize, columnResize);

		visibleViewer = new TableViewer(table);
		ColumnViewerToolTipSupport.enableFor(visibleViewer);
		visibleViewer.setLabelProvider(doGetLabelProvider());
		visibleViewer.setContentProvider(ArrayContentProvider.getInstance());
		visibleViewer.addSelectionChangedListener(event -> handleVisibleSelection(event.getStructuredSelection()));
		visibleViewer.setInput(getVisible());
		return table;
	}

	private List<String> getVisible() {
		return this.visible;
	}

	/**
	 * To configure the columns we need further information. The supplied column
	 * objects are adapted for its properties via {@link IColumnInfoProvider}
	 */
	private ICustomColumnInfoProvider getColumnInfoProvider(@NonNull final CustomReportStatus status) {
		return new ICustomColumnInfoProvider() {
			@Override
			public int getColumnIndex(final String blockID) {
				return status.report.getColumns().indexOf(blockID);
			}

			@Override
			public boolean isColumnVisible(final String blockID) {
				return status.report.getColumns().contains(blockID);
			}
		};
	}

	/**
	 * Creates the table that lists out non-visible columns in the viewer
	 * 
	 * @param parent
	 * @return {@link Control}
	 */
	private Control createInvisibleTable(final Composite parent) {

		final Composite composite = new Composite(parent, SWT.NONE);
		final GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.marginHeight = 0;
		composite.setLayout(gridLayout);

		composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		final Label label = new Label(composite, SWT.NONE);
		label.setText("Disabled Columns");
		applyDialogFont(label);

		final Table table = new Table(composite, SWT.BORDER | SWT.MULTI);
		final GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.widthHint = convertWidthInCharsToPixels(20);
		data.heightHint = table.getItemHeight() * 15;
		table.setLayoutData(data);

		final TableColumn column = new TableColumn(table, SWT.NONE);
		column.setText("Disabled Columns");
		table.addListener(SWT.Resize, event -> column.setWidth(table.getClientArea().width));

		nonVisibleViewer = new TableViewer(table);
		ColumnViewerToolTipSupport.enableFor(nonVisibleViewer);

		nonVisibleViewer.setLabelProvider(doGetLabelProvider());
		nonVisibleViewer.setContentProvider(ArrayContentProvider.getInstance());
		nonVisibleViewer.addSelectionChangedListener(event -> handleNonVisibleSelection(event.getStructuredSelection()));
		nonVisibleViewer.setInput(getNonVisible());
		nonVisibleViewer.setComparator(new ViewerComparator() {
			@Override
			public int compare(final Viewer viewer, final Object e1, final Object e2) {
				if (e1 instanceof final ColumnBlock b1 && e2 instanceof final ColumnBlock b2) {
					return b1.blockName.compareTo(b2.blockName);
				}
				return super.compare(viewer, e1, e2);
			}
		});
		return table;
	}

	private void handleToVisibleButton() {

		final CustomReportStatus status = this.current;
		if (status != null) {
			final IStructuredSelection selection = nonVisibleViewer.getStructuredSelection();
			// Make a mutable copy
			final List<String> selVCols = new LinkedList<>(selection.toList()); // Do not allow invalid definition to be made visible
			selVCols.removeIf(id -> blockIdToColumnBlock.get(id) == null);
			this.nonVisible.removeAll(selVCols);
			this.visible.addAll(selVCols);
			Collections.sort(this.visible, comparator);

			this.updateReportDefinitionWithChangesFromDialog(status.report);
			status.saved = false;

			refreshViewers();
			visibleViewer.setSelection(selection);
			handleVisibleSelection(selection);
			handleNonVisibleSelection(nonVisibleViewer.getStructuredSelection());

			onReportModified();
		}
	}

	/**
	 * Handles a selection change in the viewer that lists out the visible columns.
	 * Takes care of various enablements.
	 * 
	 * @param selection
	 */
	private void handleVisibleSelection(final IStructuredSelection selection) {

		final CustomReportStatus status = current;
		if (status == null) {
			return;
		}

		assert selection != null;
		final List<String> selVCols = selectionToList(selection);
		final List<String> allVCols = getVisible();
		toNonVisibleBtt.setEnabled(!selVCols.isEmpty() && allVCols.size() > selVCols.size());
		if (!selection.isEmpty()) {
			nonVisibleViewer.setSelection(null);
		}

		final ICustomColumnInfoProvider infoProvider = getColumnInfoProvider(status);
		boolean moveDown = !selVCols.isEmpty();
		boolean moveUp = !selVCols.isEmpty();
		final Iterator<String> iterator = selVCols.iterator();
		while (iterator.hasNext()) {
			final String columnObj = iterator.next();
			if (!infoProvider.isColumnMovable(columnObj)) {
				moveUp = false;
				moveDown = false;
				break;
			}
			final int i = allVCols.indexOf(columnObj);
			if (i == 0) {
				moveUp = false;
				if (!moveDown) {
					break;
				}
			}
			if (i == (allVCols.size() - 1)) {
				moveDown = false;
				if (!moveUp) {
					break;
				}
			}
		}
		upButton.setEnabled(moveUp && this.current != null);
		downButton.setEnabled(moveDown && this.current != null);
	}

	/**
	 * Handles a selection change in the viewer that lists out the non-visible
	 * columns
	 * 
	 * @param selection
	 */
	private void handleNonVisibleSelection(final IStructuredSelection selection) {
		final String[] nvKeys = selectionToArray(selection);
		if (selection != null && !selection.isEmpty()) {
			visibleViewer.setSelection(null);
		}
		toVisibleBtt.setEnabled(nvKeys.length > 0 && this.current != null);
	}

	/**
	 * Applies to visible columns, and handles the changes in the order of columns
	 * 
	 * @param e event from the button click
	 */
	private void handleDownButton() {
		final CustomReportStatus status = this.current;
		if (status != null) {
			final IStructuredSelection selection = visibleViewer.getStructuredSelection();
			final String[] selVCols = selectionToArray(selection);
			final List<String> allVCols = getVisible();
			for (int i = selVCols.length - 1; i >= 0; i--) {
				final String colObj = selVCols[i];
				final int visibleIndex = allVCols.indexOf(colObj);
				allVCols.remove(visibleIndex);
				allVCols.add(visibleIndex + 1, colObj);
			}
			visibleViewer.refresh();
			handleVisibleSelection(selection);
			this.updateReportDefinitionWithChangesFromDialog(status.report);
			status.saved = false;
			onReportModified();
		}
	}

	/**
	 * Applies to visible columns, and handles the changes in the order of columns
	 * 
	 * @param e event from the button click
	 */
	private void handleUpButton() {
		final CustomReportStatus status = this.current;
		if (status != null) {
			final IStructuredSelection selection = visibleViewer.getStructuredSelection();
			final String[] selVCols = selectionToArray(selection);
			final List<String> allVCols = getVisible();
			for (int i = 0; i < selVCols.length; i++) {
				final String colObj = selVCols[i];
				final int visibleIndex = allVCols.indexOf(colObj);
				allVCols.remove(visibleIndex);
				allVCols.add(visibleIndex - 1, colObj);
			}
			visibleViewer.refresh();
			handleVisibleSelection(selection);
			this.updateReportDefinitionWithChangesFromDialog(status.report);
			status.saved = false;

			onReportModified();
		}
	}

	private void onReportModified() {
		this.discardBtn.setEnabled(true);
		this.saveBtn.setEnabled(true);
		this.publishBtn.setEnabled(true);
	}

	/**
	 * Moves selected columns from visible to non-visible state
	 * 
	 * @param e event from the button click
	 */
	private void handleToNonVisibleButton() {
		final CustomReportStatus status = this.current;
		if (status != null) {
			final IStructuredSelection selection = visibleViewer.getStructuredSelection();
			// Make a mutable copy
			final List<String> selVCols = new LinkedList<>(selection.toList());
			getVisible().removeAll(selVCols);

			// Do not allow invalid definition to be added to the non-visible list
			selVCols.removeIf(id -> blockIdToColumnBlock.get(id) == null);

			getNonVisible().addAll(selVCols);

			Collections.sort(getNonVisible(), comparator);

			this.updateReportDefinitionWithChangesFromDialog(status.report);
			status.saved = false;

			refreshViewers();
			nonVisibleViewer.setSelection(selection);
			handleVisibleSelection(visibleViewer.getStructuredSelection());
			handleNonVisibleSelection(nonVisibleViewer.getStructuredSelection());

			onReportModified();
		}
	}

	private List<String> getNonVisible() {
		return this.nonVisible;
	}

	/**
	 * Creates buttons for moving columns from non-visible to visible and vice-versa
	 * 
	 * @param parent
	 * @return {@link Control}
	 */
	private Control createMoveButtons(final Composite parent) {
		// create the manager and bind to a widget
		final LocalResourceManager resManager = new LocalResourceManager(JFaceResources.getResources(), parent);

		final ImageDescriptor leftImageDescriptor = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/nav_backward.gif");
		final Image leftImage = resManager.createImage(leftImageDescriptor);
		final ImageDescriptor rightImageDescriptor = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/nav_forward.gif");
		final Image rightImage = resManager.createImage(rightImageDescriptor);

		final Composite composite = new Composite(parent, SWT.NONE);
		final GridLayout compositeLayout = new GridLayout();
		compositeLayout.marginHeight = 0;
		compositeLayout.marginWidth = 0;
		composite.setLayout(compositeLayout);
		composite.setLayoutData(new GridData(SWT.NONE, SWT.FILL, false, true));

		final Composite bttArea = new Composite(composite, SWT.NONE);
		final GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		bttArea.setLayout(layout);
		bttArea.setLayoutData(new GridData(SWT.NONE, SWT.CENTER, false, true));

		toVisibleBtt = new Button(bttArea, SWT.PUSH);
		toVisibleBtt.setText("Show");
		toVisibleBtt.setImage(rightImage);
		setButtonLayoutData(toVisibleBtt);
		((GridData) toVisibleBtt.getLayoutData()).verticalIndent = tableLabelSize.y;
		toVisibleBtt.addListener(SWT.Selection, event -> handleToVisibleButton());
		toVisibleBtt.setEnabled(false);

		toNonVisibleBtt = new Button(bttArea, SWT.PUSH);
		toNonVisibleBtt.setText("Hide");
		toNonVisibleBtt.setImage(leftImage);
		setButtonLayoutData(toNonVisibleBtt);

		toNonVisibleBtt.addListener(SWT.Selection, event -> handleToNonVisibleButton());
		toNonVisibleBtt.setEnabled(false);

		return bttArea;
	}

	private boolean isVisible(final String blockID) {
		if (this.current != null) {
			return this.current.report.getColumns().contains(blockID);
		} else {
			final ColumnBlock block = blockIdToColumnBlock.get(blockID);
			if (block != null) {
				return CustomReportsRegistry.getInstance().isBlockDefaultVisible(block);
			}
			// Unknown block ID
			return false;
		}
	}

	private void loadDefaults() {
		final List<ColumnBlock> defaultColumns = CustomReportsRegistry.getInstance().getColumnDefinitions();
		this.visible.clear();
		this.nonVisible.clear();
		this.blockIdToColumnBlock.clear();

		for (final ColumnBlock block : defaultColumns) {
			blockIdToColumnBlock.put(block.blockID, block);

			if (isVisible(block.blockID)) {
				this.visible.add(block.blockID);
			} else {
				this.nonVisible.add(block.blockID);
			}
		}

		builder.setDiffFilter(CustomReportsRegistry.getInstance().getDefaults().getDiffOptions().toArray(new String[0]));
		builder.setRowFilter(CustomReportsRegistry.getInstance().getDefaults().getRowFilters().toArray(new String[0]));
	}

	private int getColumnIndex(final String blockID) {
		if (this.current != null) {
			final int index = this.current.report.getColumns().indexOf(blockID);
			if (index >= 0) {
				return index;
			}
		}

		final ColumnBlock block = blockIdToColumnBlock.get(blockID);
		if (block != null) {
			return CustomReportsRegistry.getInstance().getBlockDefaultIndex(block);
		}
		// Unknown block ID
		return -1;
	}

	/**
	 * Updates the UI based on values of the variable
	 */
	private void refreshViewers() {
		if (nonVisibleViewer != null) {
			nonVisibleViewer.refresh();
		}
		if (visibleViewer != null) {
			visibleViewer.refresh();
		}
		if (customReportsViewer != null) {
			customReportsViewer.refresh();
		}
	}

	private IBaseLabelProvider doGetLabelProvider() {
		return getLabelProvider();
	}

	private ColumnLabelProvider getLabelProvider() {
		return new ColumnLabelProvider() {
			@Override
			public String getText(final Object element) {
				final String blockID = (String) element;

				final ColumnBlock block = blockIdToColumnBlock.get(blockID);
				if (block != null) {
					return block.blockName;
				}
				return "<Unknown column>";
			}

			@Override
			public Image getImage(final Object element) {
				final String block = (String) element;
				if (isVisible(block)) {
					return visibleIcon;
				} else {
					return nonVisibleIcon;
				}
			}

			@Override
			public String getToolTipText(final Object element) {
				final String blockID = (String) element;

				final ColumnBlock block = blockIdToColumnBlock.get(blockID);
				if (block != null) {
					return block.tooltip;
				}
				return null;
			}
		};
	}

	private ColumnLabelProvider getReportsDefinitionNameLabelProvider() {
		return new ColumnLabelProvider() {
			@Override
			public String getText(final Object element) {
				if (element instanceof final CustomReportStatus status) {
					String prefix = "";
					if (!status.saved || status.newReport) {
						prefix = "* ";
					}
					return prefix + status.report.getName();
				}
				return "<Unknown object>";
			}

			@Override
			public String getToolTipText(final Object element) {
				return ((CustomReportStatus) element).report.getName();
			}
		};
	}

	private void addCheckBoxInfo(final String title, final OptionInfo[] options, final Supplier<Set<String>> store) {
		checkboxInfo.add(new CheckboxInfoManager(title, options, store));
	}

	private class CheckboxInfoManager {
		final String title;
		final OptionInfo[] options;
		final Supplier<Set<String>> store;

		public CheckboxInfoManager(final String title, final OptionInfo[] options, final Supplier<Set<String>> store) {
			this.title = title;
			this.options = options;
			this.store = store;
		}
	}

	private void handleOptionChanged() {
		final CustomReportStatus status = this.current;
		if (status != null) {
			this.updateReportDefinitionWithChangesFromDialog(status.report);
			status.saved = false;
			onReportModified();
		}
	}

	private String[] selectionToArray(final IStructuredSelection ss) {
		return ((List<String>) ss.toList()).toArray(new String[0]);
	}

	private List<String> selectionToList(final IStructuredSelection ss) {
		return ss.toList();
	}
}
