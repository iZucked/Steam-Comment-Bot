package com.mmxlabs.models.lng.scenario.actions;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.dialogs.DialogSettings;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.FormDialog;
import org.eclipse.ui.forms.IManagedForm;

import com.mmxlabs.models.lng.scenario.actions.RollForwardEngine.RollForwardDescriptor;
import com.mmxlabs.models.lng.scenario.internal.Activator;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class RollForwardDialog extends FormDialog {

	private static final String DIALOG_SECTION = "com.mmxlabs.models.lng.scenario.action.RollForwardDialog";

	private static final String DIALOG_REMOVE_SLOT_EVENT_ENABLE = "remove_slot_event_enable";
	private static final String DIALOG_UPDATE_VESSELS_ENABLED = "update_vessels_enabled";
	private static final String DIALOG_REMOVE_VESSELS_ENABLED = "remove_vessels_enabled";

	private static final String TOOLTIP_REMOVE_DATE = "Slots and Events before this date will be removed from the scenario.";
	private static final String TOOLTIP_FREEZE_DATE = "Cargoes and Events before or over this date will be frozen. Vessel assignment will be fixed, pairings will be fixed and arrival window will be set to currently scheduled arrival time.";
	private static final String TOOLTIP_UPDATE_VESSELS = "Vessels start date, location and heel will be updated where possible based on the first cargo or event still in the scenario assigned to the vessel.";
	private static final String TOOLTIP_REMOVE_VESSELS = "Vessels whose End By date is before the remove date will be removed from the scenaruio.";

	private final EditingDomain domain;
	private final LNGScenarioModel scenarioModel;

	private GridTableViewer previewViewer;
	private DateTime freezeDate;
	private Button removeDateCheckBox;
	private DateTime removeDate;
	private Button removeVesselsCheckBox;
	private Button updateVesselsCheckBox;
	private List<IRollForwardChange> changes;

	public RollForwardDialog(final Shell shell, final IScenarioDataProvider scenarioDataProvider) {
		super(shell);
		this.domain = scenarioDataProvider.getEditingDomain();
		this.scenarioModel = scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);
	}

	public RollForwardDialog(final IShellProvider parentShellProvider, final IScenarioDataProvider scenarioDataProvider) {
		super(parentShellProvider);
		this.domain = scenarioDataProvider.getEditingDomain();
		this.scenarioModel = scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);
	}

	@Override
	protected void createFormContent(final IManagedForm mform) {

		super.createFormContent(mform);
		mform.getForm().setText("Roll Forward");

		mform.getForm().getBody().setLayout(new GridLayout());
		// Input Section
		{
			final Group inputGroup = new Group(mform.getForm().getBody(), SWT.NONE);
			inputGroup.setLayout(new GridLayout(2, false));
			inputGroup.setText("Input");
			mform.getToolkit().adapt(inputGroup);

			// Freeze Date
			{
				final Label lbl = mform.getToolkit().createLabel(inputGroup, "Freeze before");

				freezeDate = new DateTime(inputGroup, SWT.DATE);
				mform.getToolkit().adapt(freezeDate, true, true);
				freezeDate.addSelectionListener(new SelectionListener() {

					@Override
					public void widgetSelected(final SelectionEvent e) {
						refreshPreview();
					}

					@Override
					public void widgetDefaultSelected(final SelectionEvent e) {

					}
				});

				{
					// Always set to zero!
					freezeDate.setSeconds(0);
					freezeDate.setMinutes(0);
					freezeDate.setHours(0);

					final Calendar cal = Calendar.getInstance();
					cal.add(Calendar.MONTH, 1);
					freezeDate.setYear(cal.get(Calendar.YEAR));
					freezeDate.setMonth(cal.get(Calendar.MONTH));
					freezeDate.setDay(cal.get(Calendar.DAY_OF_MONTH));
				}
				lbl.setToolTipText(TOOLTIP_FREEZE_DATE);
				freezeDate.setToolTipText(TOOLTIP_FREEZE_DATE);
			}
			// Remove Date
			{
				final Label lbl = mform.getToolkit().createLabel(inputGroup, "Remove before");

				final Composite c = mform.getToolkit().createComposite(inputGroup);
				c.setLayout(new GridLayout(2, false));
				removeDateCheckBox = mform.getToolkit().createButton(c, "", SWT.CHECK);
				getBooleanValue(DIALOG_REMOVE_SLOT_EVENT_ENABLE, removeDateCheckBox);

				removeDateCheckBox.addSelectionListener(new SelectionAdapter() {

					@Override
					public void widgetSelected(final SelectionEvent e) {

						removeDate.setEnabled(removeDateCheckBox.getSelection());
						removeVesselsCheckBox.setEnabled(removeDateCheckBox.getSelection() && updateVesselsCheckBox.getSelection());
						refreshPreview();
					}
				});

				removeDate = new DateTime(c, SWT.DATE);
				mform.getToolkit().adapt(removeDate, true, true);
				removeDate.addSelectionListener(new SelectionAdapter() {

					@Override
					public void widgetSelected(final SelectionEvent e) {
						refreshPreview();
					}
				});
				removeDate.setEnabled(removeDateCheckBox.getSelection());

				{
					// Always set to zero!
					removeDate.setSeconds(0);
					removeDate.setMinutes(0);
					removeDate.setHours(0);

					final Calendar cal = Calendar.getInstance();
					cal.add(Calendar.MONTH, -2);
					removeDate.setYear(cal.get(Calendar.YEAR));
					removeDate.setMonth(cal.get(Calendar.MONTH));
					removeDate.setDay(cal.get(Calendar.DAY_OF_MONTH));
				}

				lbl.setToolTipText(TOOLTIP_REMOVE_DATE);
				removeDateCheckBox.setToolTipText(TOOLTIP_REMOVE_DATE);
				removeDate.setToolTipText(TOOLTIP_REMOVE_DATE);
				c.setToolTipText(TOOLTIP_REMOVE_DATE);
			}
			// Update Vessels
			{
				final Label lbl = mform.getToolkit().createLabel(inputGroup, "Update Vessel Start");

				updateVesselsCheckBox = mform.getToolkit().createButton(inputGroup, "", SWT.CHECK);
				updateVesselsCheckBox.addSelectionListener(new SelectionAdapter() {

					@Override
					public void widgetSelected(final SelectionEvent e) {

						removeVesselsCheckBox.setEnabled(removeDateCheckBox.getSelection() && updateVesselsCheckBox.getSelection());
						refreshPreview();
					}
				});
				getBooleanValue(DIALOG_UPDATE_VESSELS_ENABLED, updateVesselsCheckBox);

				lbl.setToolTipText(TOOLTIP_UPDATE_VESSELS);
				updateVesselsCheckBox.setToolTipText(TOOLTIP_UPDATE_VESSELS);
			}

			// Remove Vessels
			{
				final Label lbl = mform.getToolkit().createLabel(inputGroup, "Remove old vessels");

				removeVesselsCheckBox = mform.getToolkit().createButton(inputGroup, "", SWT.CHECK);
				removeVesselsCheckBox.addSelectionListener(new SelectionAdapter() {

					@Override
					public void widgetSelected(final SelectionEvent e) {

						refreshPreview();
					}
				});
				removeVesselsCheckBox.setEnabled(removeDateCheckBox.getSelection() && updateVesselsCheckBox.getSelection());
				getBooleanValue(DIALOG_REMOVE_VESSELS_ENABLED, removeVesselsCheckBox);

				lbl.setToolTipText(TOOLTIP_REMOVE_VESSELS);
				removeVesselsCheckBox.setToolTipText(TOOLTIP_REMOVE_VESSELS);

			}
		}

		// Preview Section
		{
			final Group previewGroup = new Group(mform.getForm().getBody(), SWT.NONE);
			previewGroup.setLayout(new FillLayout());
			// Table will fill dialog area, but limit initial height
			final GridData layoutData = new GridData(GridData.FILL_BOTH);
			layoutData.heightHint = 100;
			previewGroup.setLayoutData(layoutData);

			previewGroup.setText("Preview of changes");
			mform.getToolkit().adapt(previewGroup);

			previewViewer = new GridTableViewer(previewGroup, SWT.H_SCROLL | SWT.V_SCROLL);
			previewViewer.setContentProvider(new ArrayContentProvider());

			final GridViewerColumn col = new GridViewerColumn(previewViewer, SWT.NONE);
			col.getColumn().setWidth(300);

			previewViewer.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(final Object element) {
					if (element instanceof IRollForwardChange) {
						final IRollForwardChange change = (IRollForwardChange) element;
						return change.getMessage();
					}

					return super.getText(element);
				}
			});

		}
		refreshPreview();
	}

	private void refreshPreview() {

		final RollForwardEngine engine = new RollForwardEngine();

		final RollForwardDescriptor descriptor = engine.new RollForwardDescriptor();
		{
			final LocalDate dt = LocalDate.of(freezeDate.getYear(), 1 + freezeDate.getMonth(), freezeDate.getDay());
			descriptor.put(RollForwardDescriptor.FreezeDate, dt);
		}
		if (removeDateCheckBox.getSelection()) {
			final LocalDate dt = LocalDate.of(removeDate.getYear(), 1 + removeDate.getMonth(), removeDate.getDay());
			descriptor.put(RollForwardDescriptor.RemoveDate, dt);
		}
		descriptor.put(RollForwardDescriptor.UpdateVessels, updateVesselsCheckBox.getSelection());
		descriptor.put(RollForwardDescriptor.RemoveVessels, updateVesselsCheckBox.getSelection() && removeVesselsCheckBox.getSelection());
		final Button button = getButton(IDialogConstants.OK_ID);
		try {
			changes = engine.generateChanges(domain, scenarioModel, descriptor);
			previewViewer.setInput(changes);
			if (button != null) {
				button.setEnabled(true);
			}
		} catch (final Exception e) {
			previewViewer.setInput(new String[] { e.getMessage() });
			if (button != null) {
				button.setEnabled(false);
			}
		}

	}

	@Override
	protected void okPressed() {
		BusyIndicator.showWhile(PlatformUI.getWorkbench().getDisplay(), new Runnable() {

			@Override
			public void run() {
				// Apply changes
				final CompoundCommand cmd = new CompoundCommand("Roll Forward");
				for (final IRollForwardChange change : changes) {
					cmd.append(change.getCommand());
				}
				if (cmd.canExecute()) {
					domain.getCommandStack().execute(cmd);
				} else {
					throw new RuntimeException("Unable to execute command");
				}
			}
		});

		// Store dialog settings
		setBooleanValue(DIALOG_REMOVE_SLOT_EVENT_ENABLE, removeDateCheckBox);
		setBooleanValue(DIALOG_REMOVE_VESSELS_ENABLED, removeVesselsCheckBox);
		setBooleanValue(DIALOG_UPDATE_VESSELS_ENABLED, updateVesselsCheckBox);

		super.okPressed();
	}

	private void getBooleanValue(final String key, final Button checkBox) {
		final IDialogSettings dialogSettings = Activator.getDefault().getDialogSettings();
		if (dialogSettings != null) {
			final IDialogSettings section = dialogSettings.getSection(DIALOG_SECTION);
			if (section != null) {
				final boolean value = section.getBoolean(key);
				checkBox.setSelection(value);
			}
		}
	}

	private void setBooleanValue(final String key, final Button checkBox) {
		final IDialogSettings dialogSettings = Activator.getDefault().getDialogSettings();
		if (dialogSettings != null) {
			final IDialogSettings section = DialogSettings.getOrCreateSection(dialogSettings, DIALOG_SECTION);
			section.put(key, checkBox.getSelection());
		}
	}
}
