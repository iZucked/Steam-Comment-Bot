/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.customizable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

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
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.internal.Workbench;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lingo.reports.internal.Activator;
import com.mmxlabs.lingo.reports.utils.ColumnConfigurationDialog;
import com.mmxlabs.lingo.reports.utils.ColumnConfigurationDialog.IColumnUpdater;
import com.mmxlabs.lingo.reports.utils.ColumnConfigurationDialog.OptionInfo;
import com.mmxlabs.lingo.reports.views.schedule.ScheduleBasedReportBuilder;
import com.mmxlabs.lingo.reports.views.schedule.ScheduleSummaryReport;
import com.mmxlabs.models.ui.tabular.columngeneration.ColumnBlock;
import com.mmxlabs.models.ui.tabular.columngeneration.IColumnInfoProvider;

public class CustomReportsManagerDialog extends TrayDialog {

	private static Logger logger = LoggerFactory.getLogger(CustomReportsManagerDialog.class);

	private static final String DIFF_TITLE = "In diff mode";

	private static final String ROWS_TITLE = "Show rows for";

	private static int RESET_ID = IDialogConstants.CLIENT_ID + 1;

	private static int SAVE_ID = RESET_ID + 1;

	private static int PUBLISH_ID = SAVE_ID + 1;

	/** The list contains columns that are currently visible in viewer */
	private List<ColumnBlock> visible = new ArrayList<>();

	/** The list contains columns that are note shown in viewer */
	private List<ColumnBlock> nonVisible = new ArrayList<>();

	private List<CustomReportDefinition> userReportDefinitions;

	private List<CustomReportDefinition> teamReportDefinitions;

	enum StoreType {
		User, Team
	}

	class Change {
		StoreType storeType;
		CustomReportDefinition report;
		boolean newReport;
		boolean saved;
		boolean published;
	}

	private HashMap<String, Change> uuidToChangedReports = new HashMap<>();

	private StoreType currentStoreType;

	private CustomReportDefinition current;

	private CustomReportDefinition currentBeforeChanges;

	private Button userButton, teamButton;

	private TableViewer visibleViewer, nonVisibleViewer, customReportsViewer;

	private Button upButton, downButton;

	private Button toVisibleBtt, toNonVisibleBtt;

	private Button newBtn, saveBtn, publishBtn, deleteBtn, copyBtn, renameBtn, discardBtn;

	private Button refreshBtn;

	private Label widthLabel;
	private Text widthText;

	private Point tableLabelSize;

	final ScheduleBasedReportBuilder builder = new ScheduleBasedReportBuilder();

	final List<CheckboxInfoManager> checkboxInfo = new ArrayList<>();

	final Image nonVisibleIcon = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/read_obj_disabled.gif").createImage();
	final Image visibleIcon = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/read_obj.gif").createImage();

	boolean changesMade = false;

	boolean modeChanged = false;

	private final Comparator<ColumnBlock> comparator = new Comparator<>() {
		@Override
		public int compare(final ColumnBlock arg0, final ColumnBlock arg1) {
			return getColumnIndex(arg0) - getColumnIndex(arg1);
		}
	};

	public CustomReportsManagerDialog(final Shell parentShell) {
		super(parentShell);
		initData();
	}

	public void dispose() {
		nonVisibleIcon.dispose();
		visibleIcon.dispose();
	}

	@Override
	public int open() {
		int retCode = CANCEL;
		try {
			retCode = super.open();

			// Could possible happen if the click on X in the top right corner.
			if (this.unsavedOrUnpublishedChanges()) {
				int savePublishNow = MessageDialog.open(MessageDialog.QUESTION_WITH_CANCEL, getShell(), "Warning",
						"Some changes have been made to user reports without changes or team reports without publishing. Save and/or publish these now?", SWT.NONE, "Save/Publish", "Cancel");
				if (savePublishNow == 0) {
					this.savedOrPublishAnyChanges();
				}
			}

			if (retCode == OK && this.changesMade) {
				int restartNow = MessageDialog.open(MessageDialog.QUESTION_WITH_CANCEL, getShell(), "Warning", "LiNGO must be restarted to update views menus. Restart now?", SWT.NONE, "Restart now",
						"Later");
				if (restartNow == 0) {
					Workbench.getInstance().restart();
				}
			}
		} catch (Exception ex) {
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

	void initData() {
		this.loadDefaults();

		this.userReportDefinitions = CustomReportsRegistry.getInstance().readUserCustomReportDefinitions();

		this.teamReportDefinitions = CustomReportsRegistry.getInstance().readTeamCustomReportDefinitions();

		addDialogCheckBoxes();
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("Reports Manager");
	}

	@Override
	protected void createButtonsForButtonBar(final Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
	}

	protected void addDialogCheckBoxes() {
		this.addCheckBoxInfo(ROWS_TITLE, builder.ROW_FILTER_ALL, () -> builder.getRowFilterInfo());
		this.addCheckBoxInfo(DIFF_TITLE, builder.DIFF_FILTER_ALL, () -> builder.getDiffFilterInfo());
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
	 * @see org.eclipse.ui.internal.views.markers.ViewerSettingsAndStatusDialog# createDialogContentArea(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createDialogContentArea(final Composite dialogArea) {
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
				button.addListener(SWT.Selection, new Listener() {
					@Override
					public void handleEvent(final Event event) {
						if (store.contains(option.id)) {
							store.remove(option.id);
						} else {
							store.add(option.id);
						}
						handleOptionChanged(event);
					}
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
	Control createNewSavePublishDeleteButtons(final Composite parent) {
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
		newBtn.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(final Event event) {
				handleNewBtn(event);
			}
		});
		setButtonLayoutData(newBtn);
		((GridData) newBtn.getLayoutData()).verticalIndent = tableLabelSize.y;
		newBtn.setEnabled(true);

		copyBtn = new Button(bttArea, SWT.PUSH);
		copyBtn.setText("Copy");
		copyBtn.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(final Event event) {
				handleCopyBtn(event);
			}
		});
		setButtonLayoutData(copyBtn);
		copyBtn.setEnabled(false);

		renameBtn = new Button(bttArea, SWT.PUSH);
		renameBtn.setText("Rename");
		renameBtn.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(final Event event) {
				handleRenameBtn(event);
			}
		});
		setButtonLayoutData(renameBtn);
		renameBtn.setEnabled(false);

		discardBtn = new Button(bttArea, SWT.PUSH);
		discardBtn.setText("Revert");
		discardBtn.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(final Event event) {
				handleDiscardBtn(event);
			}
		});
		setButtonLayoutData(discardBtn);
		discardBtn.setEnabled(false);

		deleteBtn = new Button(bttArea, SWT.PUSH);
		deleteBtn.setText("Delete");
		deleteBtn.addListener(SWT.Selection, CustomReportsManagerDialog.this::handleDeleteBtn);
		setButtonLayoutData(deleteBtn);
		deleteBtn.setEnabled(false);

		// This button doesn't do anything, just placed to create a button sized gap between buttons.
		Button spacerBtn = new Button(bttArea, SWT.PUSH);
		setButtonLayoutData(spacerBtn);
		spacerBtn.setVisible(false);

		saveBtn = new Button(bttArea, SWT.PUSH);
		saveBtn.setText("Save");
		saveBtn.addListener(SWT.Selection, CustomReportsManagerDialog.this::handleSaveBtn);
		setButtonLayoutData(saveBtn);
		saveBtn.setEnabled(false);

		publishBtn = new Button(bttArea, SWT.PUSH);
		publishBtn.setText("Publish");
		publishBtn.addListener(SWT.Selection,  CustomReportsManagerDialog.this::handlePublishBtn);
		setButtonLayoutData(publishBtn);
		publishBtn.setEnabled(false);

		refreshBtn = new Button(bttArea, SWT.PUSH);
		refreshBtn.setText("Refresh");
		refreshBtn.addListener(SWT.Selection,  CustomReportsManagerDialog.this::handleRefreshBtn);
		setButtonLayoutData(refreshBtn);
		refreshBtn.setEnabled(true);

		return bttArea;
	}

	protected void handleRefreshBtn(Event event) {
		if (this.teamButton.getSelection()) {

			// Check for changes first.
			if (!this.checkForUnsavedUnpublishedChanges()) {
				try {
					CustomReportsRegistry.getInstance().refreshTeamReports();
				} catch (IOException e) {
					displayErrorDialog("Could not connect to DataHub to refresh reports.");
					return;
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}

				this.teamReportDefinitions = CustomReportsRegistry.getInstance().readTeamCustomReportDefinitions();
				updateReports(false);
			}
		}
	}

	protected void handleDiscardBtn(Event event) {
		discardCurrentlySelectedChanges();
	}

	private void displayErrorDialog(String errorMessage) {
		MessageDialog.openError(getShell(), "Error", errorMessage);
	}

	private void discardCurrentlySelectedChanges() {
		Change change = this.uuidToChangedReports.get(this.current.getUuid());
		String uuidDiscarded = this.current.getUuid();
		boolean newReport = (change != null && change.newReport);
		if (newReport) {
			// Remove newly created, but unsaved report.
			deleteReport(this.currentStoreType, this.current);
		} else {
			// Undo any changes to the visible / hidden columns by reloading the currently selected report definition.
			if (this.currentStoreType == StoreType.User) {
				int index = this.userReportDefinitions.indexOf(this.current);
				this.userReportDefinitions.set(index, this.currentBeforeChanges);
			} else if (this.currentStoreType == StoreType.Team) {
				int index = this.teamReportDefinitions.indexOf(this.current);
				this.teamReportDefinitions.set(index, this.currentBeforeChanges);
			} else {
				logger.error("Unimplemented mode in protected void CustomReportsManagerDialog.handleDiscardBtn(Event event)");
			}
			this.current = this.currentBeforeChanges;
			this.currentBeforeChanges = this.current.copy();
			updateViewWithReportDefinition(this.current);
		}

		// Have to make some changes to be able to discard again.
		this.discardBtn.setEnabled(false);
		this.saveBtn.setEnabled(false);
		this.publishBtn.setEnabled(false);

		// Changes undone, so not pending anymore - clear the discarded change only (as could be two changes
		// e.g. in the case where new report, copy without saving both.
		this.uuidToChangedReports.remove(uuidDiscarded);

		// Below is only set if changes saved or published already, in which case
		// we do not want to un-set it as we want LiNGO restarted in that case to
		// reflect changes in the relevant reports.
		// this.changesMade = false;
	}

	private void deleteReport(StoreType storeType, CustomReportDefinition toDelete) {
		Change change = this.uuidToChangedReports.get(toDelete.getUuid());
		boolean newReport = (change != null && change.newReport);

		switch (storeType) {
		case User:
			if (!newReport) {
				CustomReportsRegistry.getInstance().deleteUserReport(toDelete);
			}
			this.userReportDefinitions.remove(toDelete);
			break;
		case Team:
			if (!newReport) {
				try {
					CustomReportsRegistry.getInstance().deleteTeamReport(toDelete);
					CustomReportsRegistry.getInstance().removeFromDatahub(toDelete);
					this.teamReportDefinitions.remove(toDelete);
				} catch (Exception ex) {
					final String errorMessage = "Error connecting to datahub whilst attempting to remove report \"" + toDelete.getName()
							+ "\" from team folder. Please check error log for more details.";
					displayErrorDialog(errorMessage);
					logger.error(errorMessage, ex);
				}
			} else {
				// New report.
				CustomReportsRegistry.getInstance().deleteTeamReport(toDelete);
				this.teamReportDefinitions.remove(toDelete);
			}
			break;
		default:
			logger.error("Unimplemented store type for deleteReport method: ", storeType.toString());
			break;
		}
		this.loadDefaults();
		this.refreshViewers();

		if (!newReport) {
			changesMade = true;
		}
	}

	void handleRenameBtn(Event event) {
		if (this.current != null) {
			final InputDialog dialog = new InputDialog(this.getShell(), "Rename the currently selected report", "Choose a new name for the report.", this.current.getName(), getReportNameValidator());

			if (dialog.open() == Window.OK) {
				final String name = dialog.getValue();
				this.current.setName(name);
				if (this.currentStoreType == StoreType.User) {
					CustomReportsRegistry.getInstance().writeToJSON(this.current);
					this.changesMade = true;
				} else {
					try {
						CustomReportsRegistry.getInstance().publishReport(this.current);
						this.changesMade = true;
					} catch (Exception e) {
						String errorMsg = "Problem publishing renamed report \"" + this.current.getName() + "\" to DataHub.";
						logger.error(errorMsg, e);
						displayErrorDialog(errorMsg);
					}
				}
				this.updateViewWithReportDefinition(this.current);
			}
		} else {
			logger.error("No report currently selected to rename.");
		}
	}

	private CustomReportNameValidator getReportNameValidator() {
		List<String> existingNames = getExistingReportNames();
		return new CustomReportNameValidator(existingNames);
	}

	private List<String> getExistingReportNames() {
		List<String> existingNames = new ArrayList<>();
		this.getUserReportDefinitions().stream().forEach(r -> existingNames.add(r.getName().toLowerCase()));
		this.getTeamReportDefinitions().stream().forEach(r -> existingNames.add(r.getName().toLowerCase()));
		return existingNames;
	}

	protected void handleDeleteBtn(Event event) {
		if (this.current != null) {
			if (!MessageDialog.openQuestion(getShell(), "Are you sure?", "Are you sure you want to delete the report \"" + current.getName() + "\"")) {
				return;
			}
		}

		IStructuredSelection selected = this.customReportsViewer.getStructuredSelection();
		if (selected != null && selected.size() == 1) {
			CustomReportDefinition toDelete = (CustomReportDefinition) selected.getFirstElement();
			StoreType storeType = userButton.getSelection() ? StoreType.User : StoreType.Team;
			this.deleteReport(storeType, toDelete);
			this.uuidToChangedReports.remove(current.getUuid());
			this.current = null;
		}
	}

	private CustomReportDefinition checkForTeamReport(String name) {
		for (CustomReportDefinition crd : this.teamReportDefinitions) {
			if (crd.getName().equalsIgnoreCase(name)) {
				return crd;
			}
		}
		return null;
	}

	protected void handlePublishBtn(Event event) {
		IStructuredSelection selected = this.customReportsViewer.getStructuredSelection();
		if (selected != null && selected.size() == 1) {
			// Update view with latest.
			CustomReportDefinition rd = (CustomReportDefinition) selected.getFirstElement();
			updateReportDefinitionWithChangesFromDialog(rd);

			// Add to team case.
			if (this.currentStoreType == StoreType.User) {
				Change change = this.uuidToChangedReports.get(rd.getUuid());
				if (change != null && !change.saved) {
					boolean save = MessageDialog.open(MessageDialog.QUESTION, this.getShell(), "Save before publish?", "Report " + rd.getName() + " has unsaved changed. Save before publishing?",
							SWT.NONE);

					if (save) {
						// Save down, before publishing for user report.
						CustomReportsRegistry.getInstance().writeToJSON(rd);
						change.saved = true;
						this.changesMade = true;
						this.saveBtn.setEnabled(false);
					}
				}

				// Check if report with that name already exists and if so prompt the user if they want to overwrite it or cancel.
				CustomReportDefinition existingReport = this.checkForTeamReport(rd.getName());
				if (existingReport == null) {

					// If we are adding a user report to team, we need to clone it and create a new UUID as per PS, so
					// that team version appears separately from user version.
					CustomReportDefinition copy = new CustomReportDefinition();
					copy.setUuid(ScheduleSummaryReport.UUID_PREFIX + UUID.randomUUID().toString());
					copy.setName(rd.getName());
					copy.setType("ScheduleSummaryReport");
					updateReportDefinitionWithChangesFromDialog(copy);

					rd = copy;
				} else {
					// If it is already a team report, ask the user if they want to overwrite it with their new one.
					int overwrite = MessageDialog.open(MessageDialog.QUESTION_WITH_CANCEL, getShell(), "Overwrite existing team report?",
							"There is an existing team report " + existingReport.getName() + ". Do you want to overwrite this?", SWT.NONE, "Overwrite", "Cancel");

					if (overwrite == 0) {
						updateReportDefinitionWithChangesFromDialog(existingReport);
						rd = existingReport;
					} else {
						// Cancel and do nothing.
						return;
					}
				}
			}
			try {

				CustomReportsRegistry.getInstance().publishReport(rd);

				// Add to team case.
				if (this.currentStoreType == StoreType.User) {
					this.addChangedReport(rd);
					this.uuidToChangedReports.get(rd.getUuid()).storeType = StoreType.Team;
				}

				// Make sure we refresh team reports if team reports selected.
				this.changesMade = true;
				Change change = this.uuidToChangedReports.get(rd.getUuid());
				if (change != null) {
					// If team report or it has already been saved, then set disable discard button again.
					if (this.teamButton.getSelection() || change.saved) {
						this.discardBtn.setEnabled(false);
					}
					this.uuidToChangedReports.get(rd.getUuid()).published = true;
					this.uuidToChangedReports.get(rd.getUuid()).newReport = false;
				}
				this.publishBtn.setEnabled(false);

			} catch (Exception e) {
				String errorMsg = "Problem publishing report \"" + rd.getName() + "\" to DataHub.";
				logger.error(errorMsg, e);
				displayErrorDialog(errorMsg);
			}
		}
	}

	private String getUserCopyName(String currentReportName) {
		int copyNo = 0;

		String copyName = currentReportName + " (User Copy)";
		boolean copyNameFound = false;

		// Check user folder for name.
		while (!copyNameFound) {
			copyNameFound = true;
			for (CustomReportDefinition crd : this.userReportDefinitions) {
				if (crd.getName().equalsIgnoreCase(copyName)) {
					copyNo++;
					copyName = currentReportName + " (User Copy " + Integer.toString(copyNo) + ")";
					copyNameFound = false;
					break;
				}
			}
		}

		return copyName;
	}

	private void handleSaveBtn(Event event) {
		IStructuredSelection selected = this.customReportsViewer.getStructuredSelection();
		if (selected != null && selected.size() == 1) {

			CustomReportDefinition toSave = (CustomReportDefinition) selected.getFirstElement();

			// If we are copying from team folder, clone and change UUID.
			if (teamButton.getSelection()) {
				// Create report definition object with defaults and save.
				CustomReportDefinition copy = new CustomReportDefinition();

				copy.setUuid(ScheduleSummaryReport.UUID_PREFIX + UUID.randomUUID().toString());
				String copyName = getUserCopyName(toSave.getName());
				copy.setName(copyName);
				copy.setType("ScheduleSummaryReport");

				updateReportDefinitionWithChangesFromDialog(copy);
				this.addChangedReport(copy);
				this.uuidToChangedReports.get(copy.getUuid()).newReport = true;
				this.uuidToChangedReports.get(copy.getUuid()).storeType = StoreType.User;
				this.userReportDefinitions.add(copy);
				toSave = copy;
			} else {
				// Update view with latest.
				updateReportDefinitionWithChangesFromDialog(toSave);
			}

			CustomReportsRegistry.getInstance().writeToJSON(toSave);
			changesMade = true;
			this.discardBtn.setEnabled(false);
			// Prevent discard beyond saved state.
			this.currentBeforeChanges = this.current.copy();
			this.uuidToChangedReports.get(toSave.getUuid()).saved = true;
			this.uuidToChangedReports.get(toSave.getUuid()).newReport = false;

			this.saveBtn.setEnabled(false);
		}
	}

	private void handleNewBtn(Event event) {
		createNewViewDefinition(false, "New custom report view");
	}

	private void handleCopyBtn(Event event) {
		String nameHint = null;
		if (this.current != null) {
			nameHint = this.current.getName() + " (Copy)";
		}
		createNewViewDefinition(true, "Copy custom report view", nameHint);
	}

	private void createNewViewDefinition(boolean initialiseWithSelectedReport, String title) {
		createNewViewDefinition(false, "New custom report view", null);
	}

	private void createNewViewDefinition(boolean initialiseWithSelectedReport, String title, String nameHint) {
		if (nameHint == null) {
			nameHint = "<Enter a new view name>";
		}
		final InputDialog dialog = new InputDialog(this.getShell(), title, "Choose name for the new custom report.", nameHint, getReportNameValidator());

		if (dialog.open() == Window.OK) {
			final String name = dialog.getValue();

			// Create report definition object with defaults and save.
			CustomReportDefinition reportDefinition = new CustomReportDefinition();

			reportDefinition.setUuid(ScheduleSummaryReport.UUID_PREFIX + UUID.randomUUID().toString());
			reportDefinition.setName(name);
			reportDefinition.setType("ScheduleSummaryReport");

			if (!initialiseWithSelectedReport) {
				this.loadDefaults();
			}

			updateReportDefinitionWithChangesFromDialog(reportDefinition);
			this.addChangedReport(reportDefinition);
			this.uuidToChangedReports.get(reportDefinition.getUuid()).newReport = true;

			// Add to report definitions list and select it.
			if (this.userButton.getSelection()) {
				this.userReportDefinitions.add(reportDefinition);
				this.customReportsViewer.setInput(this.userReportDefinitions);
			} else if (this.teamButton.getSelection()) {
				this.teamReportDefinitions.add(reportDefinition);
				this.customReportsViewer.setInput(this.teamReportDefinitions);
			} else {
				logger.error("Mode not fully implemented for: private void CustomReportsManagerDialog.createNewViewDefinition(boolean initialiseWithSelectedReport, String title)");
			}
			this.customReportsViewer.setSelection(new StructuredSelection(reportDefinition));
		}
	}

	private void addChangedReport(CustomReportDefinition rd) {
		Change change = new Change();
		change.report = rd;
		change.saved = false;
		change.published = false;
		change.storeType = this.userButton.getSelection() ? StoreType.User : StoreType.Team;
		this.uuidToChangedReports.put(rd.getUuid(), change);
	}

	private boolean unsavedOrUnpublishedChanges() {
		for (String uuid : this.uuidToChangedReports.keySet()) {
			if (unsavedOrUnpublishedChanges(uuid)) {
				return true;
			}
		}
		return false;
	}

	private boolean unsavedOrUnpublishedChanges(String reportUUID) {
		Change change = this.uuidToChangedReports.get(reportUUID);

		if (change != null) {
			if (change.storeType == StoreType.User && !change.saved) {
				return true;
			}
			if (change.storeType == StoreType.Team && !change.published) {
				return true;
			}
		}

		return false;
	}

	private void savedOrPublishAnyChanges() {
		for (String uuid : this.uuidToChangedReports.keySet()) {
			Change change = this.uuidToChangedReports.get(uuid);
			if (change.storeType == StoreType.User && !change.saved) {
				CustomReportsRegistry.getInstance().writeToJSON(change.report);
				this.changesMade = true;
			}
			if (change.report != null && change.storeType == StoreType.Team && !change.published) {
				try {
					CustomReportsRegistry.getInstance().publishReport(change.report);
				} catch (Exception e) {
					String errorMsg = "Problem publishing report \"" + change.report.getName() + "\" to DataHub.";
					logger.error(errorMsg, e);
					displayErrorDialog(errorMsg);
				}
				// FIXME: As per SG, do not restart LiNGO, as upload may not have completed in time.
				// If we crack how to do dynamic update of views menus, this would resolve this.
			}
		}
	}

	private void updateReportDefinitionWithChangesFromDialog(CustomReportDefinition reportDefinition) {
		reportDefinition.getColumns().clear();
		for (ColumnBlock cb : getVisible()) {
			reportDefinition.getColumns().add(cb.blockID);
		}
		for (CheckboxInfoManager cbim : this.checkboxInfo) {
			if (cbim.title.equals(ROWS_TITLE)) {
				reportDefinition.getFilters().clear();
				for (OptionInfo oi : cbim.options) {
					if (oi.button.getSelection()) {
						reportDefinition.getFilters().add(oi.type);
					}
				}
			} else if (cbim.title.equals(DIFF_TITLE)) {
				reportDefinition.getDiffOptions().clear();
				for (OptionInfo oi : cbim.options) {
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
	Control createUpDownBtt(final Composite parent) {
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
		upButton.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(final Event event) {
				handleUpButton(event);
			}
		});
		setButtonLayoutData(upButton);
		((GridData) upButton.getLayoutData()).verticalIndent = tableLabelSize.y;
		upButton.setEnabled(false);

		downButton = new Button(bttArea, SWT.PUSH);
		downButton.setText(JFaceResources.getString("ConfigureColumnsDialog_down")); //$NON-NLS-1$
		downButton.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(final Event event) {
				handleDownButton(event);
			}
		});
		setButtonLayoutData(downButton);
		downButton.setEnabled(false);
		return bttArea;
	}

	/**
	 * Create the controls responsible to display/edit column widths.
	 * 
	 * @param parent
	 * @return {@link Control}
	 */
	Control createWidthArea(final Composite parent) {
		final Label dummy = new Label(parent, SWT.NONE);
		dummy.setLayoutData(new GridData(SWT.NONE, SWT.NONE, false, false, 2, 1));

		final Composite widthComposite = new Composite(parent, SWT.NONE);
		final GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.marginHeight = 0;
		widthComposite.setLayout(gridLayout);
		widthComposite.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 2, 1));

		widthLabel = new Label(widthComposite, SWT.NONE);
		GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		widthLabel.setLayoutData(gridData);

		widthText = new Text(widthComposite, SWT.BORDER);
		widthText.addVerifyListener(new VerifyListener() {
			@Override
			public void verifyText(final VerifyEvent e) {
				if (e.character != 0 && e.keyCode != SWT.BS && e.keyCode != SWT.DEL && !Character.isDigit(e.character)) {
					e.doit = false;
				}
			}
		});

		gridData = new GridData();
		gridData.widthHint = convertWidthInCharsToPixels(5);
		widthText.setLayoutData(gridData);
		return widthText;
	}

	/**
	 * Creates the table that lists out custom reports in the viewer
	 * 
	 * @param parent
	 * @return {@link Control}
	 */
	Control createCustomReportsTable(final Composite parent) {

		final Composite composite = new Composite(parent, SWT.NONE);
		final GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.marginHeight = 0;
		composite.setLayout(gridLayout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		this.userButton = new Button(composite, SWT.RADIO);
		this.userButton.setText("User reports");
		this.userButton.setSelection(true);
		this.userButton.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				activateUserMode();
				updateReports(true);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
		tableLabelSize = userButton.computeSize(SWT.DEFAULT, SWT.DEFAULT);

		this.teamButton = new Button(composite, SWT.RADIO);
		this.teamButton.setText("Team reports");
		this.teamButton.setSelection(false);
		this.teamButton.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				activateTeamMode();
				updateReports(false);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});

		if (!CustomReportsRegistry.getInstance().hasTeamReportsCapability()) {
			// Hide the team reports view if hub does not support or user does not have permissions.
			this.userButton.setVisible(false);
			this.teamButton.setVisible(false);
		}

		final Table table = new Table(composite, SWT.BORDER | SWT.SINGLE);

		final GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.widthHint = convertWidthInCharsToPixels(20);
		data.heightHint = table.getItemHeight() * 15;
		table.setLayoutData(data);
		table.setData("org.eclipse.swtbot.widget.key", "customReportsViewer"); // this id is used in swtbot tests

		final TableColumn column = new TableColumn(table, SWT.NONE);
		column.setText("Custom Reports");
		final Listener columnResize = new Listener() {
			@Override
			public void handleEvent(final Event event) {
				column.setWidth(table.getClientArea().width);
			}
		};
		table.addListener(SWT.Resize, columnResize);

		customReportsViewer = new TableViewer(table);
		ColumnViewerToolTipSupport.enableFor(customReportsViewer);
		customReportsViewer.setLabelProvider(this.getReportsDefinitionNameLabelProvider());
		customReportsViewer.setContentProvider(ArrayContentProvider.getInstance());
		customReportsViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(final SelectionChangedEvent event) {
				handleCustomReportSelection(event.getSelection());
			}
		});
		customReportsViewer.setInput(getUserReportDefinitions());
		return table;
	}

	protected void activateUserMode() {
		this.modeChanged = true;
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

	protected void activateTeamMode() {
		this.modeChanged = true;
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
	 * @param userReports
	 *                        - true, if we want to display user's own reports, false, to display team reports
	 */
	protected void updateReports(boolean userReports) {
		if (userReports) {
			customReportsViewer.setInput(getUserReportDefinitions());
		} else {
			this.teamReportDefinitions = CustomReportsRegistry.getInstance().readTeamCustomReportDefinitions();
			customReportsViewer.setInput(getTeamReportDefinitions());
		}
	}

	private void handleCustomReportSelection(ISelection selection) {
		final List<CustomReportDefinition> selectedReports = ((IStructuredSelection) selection).toList();

		// It is possible in some cases when moving from team to user reports to select more than 1 report, but we are
		// only interested in the case when 1 report has been selected and the list box itself should in most cases
		// enforce this SINGLE selection property also.
		if (selectedReports.size() == 1 && (selectedReports.get(0) != this.current || modeChanged)) {
			// Check for unsaved changes to previously selected report.
			if (selectedReports.size() == 1 && selectedReports.get(0) != this.current && !checkForUnsavedUnpublishedChanges()) {
				this.currentStoreType = this.userButton.getSelection() ? StoreType.User : StoreType.Team;
				this.current = selectedReports.get(0);
				this.currentBeforeChanges = this.current.copy();
				updateViewWithReportDefinition(selectedReports.get(0));
			}

			// Determine if report needs save or publish action
			boolean needsSaving = false;
			boolean needsPublishing = false;
			final Change currentChange = uuidToChangedReports.get(current.uuid);
			if (currentStoreType != null && currentChange != null) {
				if (currentStoreType == StoreType.User && !currentChange.saved) {
					needsSaving = true;
				}
				if (currentStoreType == StoreType.Team && !currentChange.published) {
					needsPublishing = true;
				}
			}

			// Only enable publish/write access, if in user mode or if user has team reports publish permission.
			if (this.userButton.getSelection()) {
				// User reports mode.
				this.saveBtn.setEnabled(needsSaving);
				this.deleteBtn.setEnabled(true);
				this.copyBtn.setEnabled(true);
				this.renameBtn.setEnabled(true);
				if (CustomReportsRegistry.getInstance().hasTeamReportsPublishPermission()) {
					this.publishBtn.setEnabled(true);
				}
			} else {
				// Team reports mode.
				if (CustomReportsRegistry.getInstance().hasTeamReportsDeletePermission()) {
					this.deleteBtn.setEnabled(true);
				}
				if (CustomReportsRegistry.getInstance().hasTeamReportsReadPermission() && CustomReportsRegistry.getInstance().hasTeamReportsPublishPermission()) {
					this.copyBtn.setEnabled(true);
					// always allow to copy to user
					this.saveBtn.setEnabled(true);
				}
				if (CustomReportsRegistry.getInstance().hasTeamReportsPublishPermission()) {
					this.renameBtn.setEnabled(true);
					// only publish if report is unpublished
					this.publishBtn.setEnabled(needsPublishing);
				}
			}

			// Remember we have dealt with the change from user reports to team reports.
			modeChanged = false;
		}
	}

	/**
	 * Check if there are any unsaved/unpublished changes and ask the user what to do and do it. Side effects: either discards the changes or selected the previously selected report.
	 * 
	 * @return true, if we went back, false, if we discarded the previously selected report's changes or there were no unsaved/unpublished changes.
	 */
	private boolean checkForUnsavedUnpublishedChanges() {
		if (this.current != null) {
			if (this.unsavedOrUnpublishedChanges(this.current.getUuid())) {
				String reportName = this.current.getName();
				String unSavedOrUnPublished = this.userButton.getSelection() ? " unsaved " : " un-published ";
				int discardChanges = MessageDialog.open(MessageDialog.QUESTION_WITH_CANCEL, getShell(), "Warning",
						"You have" + unSavedOrUnPublished + "changes to \"" + reportName + "\". Discard these changes or go back?", SWT.NONE, "Discard", "Back");
				if (discardChanges == 0) {
					// Discards the previously selected report selection changes.
					this.discardCurrentlySelectedChanges();
				} else {
					if (this.userButton.getSelection() && this.currentStoreType == StoreType.Team) {
						// Back to the correct view.
						this.userButton.setSelection(false);
						this.teamButton.setSelection(true);
						activateTeamMode();
						this.customReportsViewer.setInput(this.getTeamReportDefinitions());
					}

					if (this.teamButton.getSelection() && this.currentStoreType == StoreType.User) {
						// Back to the correct view.
						this.userButton.setSelection(true);
						this.teamButton.setSelection(false);
						activateUserMode();
						this.customReportsViewer.setInput(this.getUserReportDefinitions());
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

	private void updateViewWithReportDefinition(CustomReportDefinition reportDefinition) {
		HashMap<String, ColumnBlock> blockIdToColumnBlock = new HashMap<>();
		for (ColumnBlock cb : visible) {
			blockIdToColumnBlock.put(cb.blockID, cb);
		}
		for (ColumnBlock cb : nonVisible) {
			blockIdToColumnBlock.put(cb.blockID, cb);
		}
		List<ColumnBlock> hiddenColumnsAfter = new ArrayList<>();
		hiddenColumnsAfter.addAll(visible);
		hiddenColumnsAfter.addAll(nonVisible);
		hiddenColumnsAfter.removeIf(k -> reportDefinition.getColumns().contains(k.blockID));

		List<ColumnBlock> visibleColumnsAfter = new ArrayList<>();
		for (String blockId : reportDefinition.getColumns()) {
			visibleColumnsAfter.add(blockIdToColumnBlock.get(blockId));
		}

		this.nonVisible.clear();
		this.nonVisible.addAll(hiddenColumnsAfter);

		this.visible.clear();
		this.visible.addAll(visibleColumnsAfter);

		for (CheckboxInfoManager cbim : this.checkboxInfo) {
			if (cbim.title.equals(ROWS_TITLE)) {
				for (OptionInfo oi : cbim.options) {
					if (oi.button != null) {
						oi.button.setSelection(reportDefinition.getFilters().contains(oi.type));
					}
				}
			} else if (cbim.title.equals(DIFF_TITLE)) {
				for (OptionInfo oi : cbim.options) {
					if (oi.button != null) {
						oi.button.setSelection(reportDefinition.getDiffOptions().contains(oi.type));
					}
				}
			}
		}

		this.refreshViewers();
	}

	public List<CustomReportDefinition> getUserReportDefinitions() {
		return this.userReportDefinitions;
	}

	public List<CustomReportDefinition> getTeamReportDefinitions() {
		return this.teamReportDefinitions;
	}

	/**
	 * Creates the table that lists out visible columns in the viewer
	 * 
	 * @param parent
	 * @return {@link Control}
	 */
	Control createVisibleTable(final Composite parent) {

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
		final Listener columnResize = new Listener() {
			@Override
			public void handleEvent(final Event event) {
				column.setWidth(table.getClientArea().width);
			}
		};
		table.addListener(SWT.Resize, columnResize);

		visibleViewer = new TableViewer(table);
		ColumnViewerToolTipSupport.enableFor(visibleViewer);
		visibleViewer.setLabelProvider(doGetLabelProvider());
		visibleViewer.setContentProvider(ArrayContentProvider.getInstance());
		visibleViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(final SelectionChangedEvent event) {
				handleVisibleSelection(event.getSelection());
			}
		});
		visibleViewer.setInput(getVisible());
		return table;
	}

	List<ColumnBlock> getVisible() {
		return this.visible;
	}

	/**
	 * Internal helper to @see {@link ColumnConfigurationDialog#getColumnInfoProvider()}
	 */
	IColumnInfoProvider doGetColumnInfoProvider() {
		return getColumnInfoProvider();
	}

	/**
	 * To configure the columns we need further information. The supplied column objects are adapted for its properties via {@link IColumnInfoProvider}
	 */
	protected IColumnInfoProvider getColumnInfoProvider() {
		IColumnInfoProvider columnInfoProvider = new ColumnConfigurationDialog.ColumnInfoAdapter() {
			@Override
			public int getColumnIndex(final Object columnObj) {
				ColumnBlock cb = (ColumnBlock) columnObj;
				return current.getColumns().indexOf(cb.blockID);
			}

			@Override
			public boolean isColumnVisible(final Object columnObj) {
				ColumnBlock cb = (ColumnBlock) columnObj;
				return current.getColumns().contains(cb.blockID);
			}
		};
		return columnInfoProvider;
	};

	/**
	 * Internal helper to @see {@link ColumnConfigurationDialog#getColumnUpdater()}
	 */
	IColumnUpdater doGetColumnUpdater() {
		return getColumnUpdater();
	}

	/**
	 * To configure properties/order of the columns is achieved via {@link IColumnUpdater}
	 */
	protected IColumnUpdater getColumnUpdater() {
		return new ColumnConfigurationDialog.ColumnUpdaterAdapter() {

			@Override
			public void setColumnVisible(final Object columnObj, final boolean visible) {
				// ((ColumnBlock) columnObj).setUserVisible(visible);
			}

			@Override
			public void swapColumnPositions(final Object columnObj1, final Object columnObj2) {
				// getBlockManager().swapBlockOrder((ColumnBlock) columnObj1, (ColumnBlock) columnObj2);
			}

			@Override
			public Object[] resetColumnStates() {
				/*
				 * // Hide everything for (final String blockId : getBlockManager().getBlockIDOrder()) { getBlockManager().getBlockByID(blockId).setUserVisible(false); } // Apply the initial state
				 * setInitialState(); // Return! return getBlockManager().getBlocksInVisibleOrder().toArray();
				 */
				return new Object[0];
			}
		};
	}

	/**
	 * Creates the table that lists out non-visible columns in the viewer
	 * 
	 * @param parent
	 * @return {@link Control}
	 */
	Control createInvisibleTable(final Composite parent) {

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
		final Listener columnResize = new Listener() {
			@Override
			public void handleEvent(final Event event) {
				column.setWidth(table.getClientArea().width);
			}
		};
		table.addListener(SWT.Resize, columnResize);

		nonVisibleViewer = new TableViewer(table);
		ColumnViewerToolTipSupport.enableFor(nonVisibleViewer);

		nonVisibleViewer.setLabelProvider(doGetLabelProvider());
		nonVisibleViewer.setContentProvider(ArrayContentProvider.getInstance());
		nonVisibleViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(final SelectionChangedEvent event) {
				handleNonVisibleSelection(event.getSelection());
			}
		});
		nonVisibleViewer.setInput(getNonVisible());
		nonVisibleViewer.setComparator(new ViewerComparator() {
			@Override
			public int compare(final Viewer viewer, final Object e1, final Object e2) {
				if (e1 instanceof ColumnBlock && e2 instanceof ColumnBlock) {
					final ColumnBlock b1 = (ColumnBlock) e1;
					final ColumnBlock b2 = (ColumnBlock) e2;
					return b1.blockName.compareTo(b2.blockName);
				}
				return super.compare(viewer, e1, e2);
			}
		});
		return table;
	}

	protected void handleToVisibleButton(Event event) {

		if (this.current != null) {
			final IStructuredSelection selection = (IStructuredSelection) nonVisibleViewer.getSelection();
			final List<ColumnBlock> selVCols = (List<ColumnBlock>) selection.toList();
			this.nonVisible.removeAll(selVCols);
			this.visible.addAll(selVCols);
			Collections.sort(this.visible, comparator);

			this.updateReportDefinitionWithChangesFromDialog(this.current);
			this.addChangedReport(this.current);

			visibleViewer.refresh();
			visibleViewer.setSelection(selection);
			nonVisibleViewer.refresh();

			handleVisibleSelection(selection);
			handleNonVisibleSelection(nonVisibleViewer.getSelection());

			onReportModified();
		}
	}

	/**
	 * Handles a selection change in the viewer that lists out the visible columns. Takes care of various enablements.
	 * 
	 * @param selection
	 */
	void handleVisibleSelection(final ISelection selection) {
		assert selection != null;
		final List<?> selVCols = ((IStructuredSelection) selection).toList();
		final List<ColumnBlock> allVCols = getVisible();
		toNonVisibleBtt.setEnabled(!selVCols.isEmpty() && allVCols.size() > selVCols.size() && this.current != null);
		if (!selection.isEmpty()) {
			nonVisibleViewer.setSelection(null);
		}

		final IColumnInfoProvider infoProvider = doGetColumnInfoProvider();
		boolean moveDown = !selVCols.isEmpty();
		boolean moveUp = !selVCols.isEmpty();
		final Iterator<?> iterator = selVCols.iterator();
		while (iterator.hasNext()) {
			final Object columnObj = iterator.next();
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
	 * Handles a selection change in the viewer that lists out the non-visible columns
	 * 
	 * @param selection
	 */
	void handleNonVisibleSelection(final ISelection selection) {
		final Object[] nvKeys = ((IStructuredSelection) selection).toArray();
		if (selection != null && selection.isEmpty() == false) {
			visibleViewer.setSelection(null);
		}
		toVisibleBtt.setEnabled(nvKeys.length > 0 && this.current != null);
	}

	/**
	 * Applies to visible columns, and handles the changes in the order of columns
	 * 
	 * @param e
	 *              event from the button click
	 */
	void handleDownButton(final Event e) {
		if (this.current != null) {
			final IStructuredSelection selection = (IStructuredSelection) visibleViewer.getSelection();
			final Object[] selVCols = selection.toArray();
			final List<ColumnBlock> allVCols = getVisible();
			for (int i = selVCols.length - 1; i >= 0; i--) {
				final ColumnBlock colObj = (ColumnBlock) selVCols[i];
				final int visibleIndex = allVCols.indexOf(colObj);
				allVCols.remove(visibleIndex);
				allVCols.add(visibleIndex + 1, colObj);
			}
			visibleViewer.refresh();
			handleVisibleSelection(selection);
			this.updateReportDefinitionWithChangesFromDialog(this.current);
			this.addChangedReport(this.current);
			onReportModified();
		}
	}

	/**
	 * Applies to visible columns, and handles the changes in the order of columns
	 * 
	 * @param e
	 *              event from the button click
	 */
	void handleUpButton(final Event e) {
		if (this.current != null) {
			final IStructuredSelection selection = (IStructuredSelection) visibleViewer.getSelection();
			final Object[] selVCols = selection.toArray();
			final List<ColumnBlock> allVCols = getVisible();
			for (int i = 0; i < selVCols.length; i++) {
				final ColumnBlock colObj = (ColumnBlock) selVCols[i];
				final int visibleIndex = allVCols.indexOf(colObj);
				allVCols.remove(visibleIndex);
				allVCols.add(visibleIndex - 1, colObj);
			}
			visibleViewer.refresh();
			handleVisibleSelection(selection);
			this.updateReportDefinitionWithChangesFromDialog(this.current);
			this.addChangedReport(this.current);

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
	 * @param e
	 *              event from the button click
	 */
	protected void handleToNonVisibleButton(final Event e) {

		if (this.current != null) {
			final IStructuredSelection selection = (IStructuredSelection) visibleViewer.getSelection();
			final List<ColumnBlock> selVCols = selection.toList();
			getVisible().removeAll(selVCols);
			getNonVisible().addAll(selVCols);

			Collections.sort(getNonVisible(), comparator);

			this.updateReportDefinitionWithChangesFromDialog(this.current);
			this.addChangedReport(this.current);

			nonVisibleViewer.refresh();
			nonVisibleViewer.setSelection(selection);
			visibleViewer.refresh();
			handleVisibleSelection(visibleViewer.getSelection());
			handleNonVisibleSelection(nonVisibleViewer.getSelection());

			onReportModified();
		}
	}

	List<ColumnBlock> getNonVisible() {
		return this.nonVisible;
	}

	/**
	 * Creates buttons for moving columns from non-visible to visible and vice-versa
	 * 
	 * @param parent
	 * @return {@link Control}
	 */
	Control createMoveButtons(final Composite parent) {
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
		toVisibleBtt.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(final Event event) {
				handleToVisibleButton(event);
			}
		});
		toVisibleBtt.setEnabled(false);

		toNonVisibleBtt = new Button(bttArea, SWT.PUSH);
		toNonVisibleBtt.setText("Hide");
		toNonVisibleBtt.setImage(leftImage);
		setButtonLayoutData(toNonVisibleBtt);

		toNonVisibleBtt.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(final Event event) {
				handleToNonVisibleButton(event);
			}
		});
		toNonVisibleBtt.setEnabled(false);

		return bttArea;
	}

	private boolean isVisible(ColumnBlock block) {
		if (this.current != null) {
			return this.current.getColumns().contains(block.blockID);
		} else {
			return CustomReportsRegistry.getInstance().isBlockVisible(block);
		}
	}

	private void loadDefaults() {
		List<ColumnBlock> defaultColumns = CustomReportsRegistry.getInstance().getColumnDefinitions();
		this.visible.clear();
		this.nonVisible.clear();

		for (ColumnBlock block : defaultColumns) {
			if (isVisible(block)) {
				this.visible.add(block);
			} else {
				this.nonVisible.add(block);
			}
		}

		builder.setDiffFilter(CustomReportsRegistry.getInstance().getDefaults().getDiffOptions().toArray(new String[0]));
		builder.setRowFilter(CustomReportsRegistry.getInstance().getDefaults().getRowFilters().toArray(new String[0]));
	}

	public int getColumnIndex(final ColumnBlock columnObj) {
		if (this.current != null) {
			int index = this.current.getColumns().indexOf(columnObj.blockID);
			if (index >= 0) {
				return index;
			}
		}
		return CustomReportsRegistry.getInstance().getBlockIndex(columnObj);
	}

	protected void performDefaults() {
		loadDefaults();
		refreshViewers();
	}

	/**
	 * Updates the UI based on values of the variable
	 */
	void refreshViewers() {
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

	IBaseLabelProvider doGetLabelProvider() {
		return getLabelProvider();
	}

	protected ColumnLabelProvider getLabelProvider() {
		return new ColumnLabelProvider() {
			@Override
			public String getText(final Object element) {
				final ColumnBlock block = (ColumnBlock) element;
				return block.blockName;
			}

			@Override
			public Image getImage(final Object element) {
				final ColumnBlock block = (ColumnBlock) element;
				if (isVisible(block)) {
					return visibleIcon;
				} else {
					return nonVisibleIcon;
				}
			}

			@Override
			public String getToolTipText(final Object element) {
				final ColumnBlock block = (ColumnBlock) element;
				return block.tooltip;
			}
		};
	}

	protected ColumnLabelProvider getReportsDefinitionNameLabelProvider() {
		return new ColumnLabelProvider() {
			@Override
			public String getText(final Object element) {
				return ((CustomReportDefinition) element).getName();
			}

			@Override
			public Image getImage(final Object element) {
				return super.getImage(element);
			}

			@Override
			public String getToolTipText(final Object element) {
				return ((CustomReportDefinition) element).getName();
			}
		};
	}

	public void addCheckBoxInfo(final String title, final OptionInfo[] options, final Supplier<Set<String>> store) {
		checkboxInfo.add(new CheckboxInfoManager(title, options, store));
	}

	class CheckboxInfoManager {
		final String title;
		final OptionInfo[] options;
		final Supplier<Set<String>> store;

		public CheckboxInfoManager(final String title, final OptionInfo[] options, final Supplier<Set<String>> store) {
			this.title = title;
			this.options = options;
			this.store = store;
		}
	}

	protected void handleOptionChanged(Event event) {
		this.updateReportDefinitionWithChangesFromDialog(this.current);
		this.addChangedReport(this.current);
		onReportModified();
	}

	protected void refreshCheckboxes() {
		for (final CheckboxInfoManager manager : checkboxInfo) {
			final Set<String> store = manager.store.get();
			for (final OptionInfo option : manager.options) {
				final Button button = option.button;
				if (button != null && !button.isDisposed()) {
					button.setSelection(store.contains(option.id));
				}
			}
		}
	}
}
