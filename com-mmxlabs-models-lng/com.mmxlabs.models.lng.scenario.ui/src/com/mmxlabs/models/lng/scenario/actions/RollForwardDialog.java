/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.actions;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EAttribute;
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
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.FormDialog;
import org.eclipse.ui.forms.IManagedForm;

import com.mmxlabs.models.lng.scenario.actions.RollForwardEngine.RollForwardDescriptor;
import com.mmxlabs.models.lng.scenario.actions.impl.UpdateDateChange;
import com.mmxlabs.models.lng.scenario.internal.Activator;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.spotmarkets.CharterOutMarketParameters;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class RollForwardDialog extends FormDialog {

	private static final String DIALOG_SECTION = "com.mmxlabs.models.lng.scenario.action.RollForwardDialog";

	private static final String DIALOG_REMOVE_SLOT_EVENT_ENABLE = "remove_slot_event_enable";
	private static final String DIALOG_UPDATE_VESSELS_ENABLED = "update_vessels_enabled";
	private static final String DIALOG_REMOVE_VESSELS_ENABLED = "remove_vessels_enabled";
	
	// Following fields are used to persist checkbox settings
	private static final String DIALOG_RELATIVE_CHECKBOX_SUFFIX = "_relative_checkbox_checked"; 
	private static final String DIALOG_PROMPT_END_NAME = "prompt_end"; 
	private static final String DIALOG_CHARTER_START_NAME = "charter_start"; 
	private static final String DIALOG_FREEZE_DATE_NAME = "freeze_date";

	private static final String TOOLTIP_REMOVE_DATE = "Slots and Events before this date will be removed from the scenario.";
	private static final String TOOLTIP_FREEZE_DATE = "Cargoes and Events before or over this date will be frozen. Vessel assignment will be fixed, pairings will be fixed and arrival window will be set to currently scheduled arrival time.";
	private static final String TOOLTIP_UPDATE_VESSELS = "Vessels start date, location and heel will be updated where possible based on the first cargo or event still in the scenario assigned to the vessel.";
	private static final String TOOLTIP_REMOVE_VESSELS = "Vessels whose End By date is before the remove date will be removed from the scenaruio.";


	private final EditingDomain domain;
	private final LNGScenarioModel scenarioModel;

	private GridTableViewer previewViewer;
	private DateTime freezeDate;
	
	private DateTime promptStart;
	private DateTime promptEnd;
	private DateTime charterOutStart = null;
	private DateTime charterOutEnd = null;
	/*
	private Button removeDateCheckBox;
	private DateTime removeDate;
	private Button removeVesselsCheckBox;
	private Button updateVesselsCheckBox;
	*/
	private List<IRollForwardChange> changes;
	
	private Map<String, Button> persistenceStringsForCheckboxes = new HashMap<>();
	private Map<DateTime, Period> relativeTimeFieldPeriods = new HashMap<>();


	

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
	
	/**
	 * Recalculates all the fields that have been designated to be computed automatically from the prompt start date.
	 */
	protected void recalculateRelativeDateFields() {
		for (DateTime field: relativeTimeFieldPeriods.keySet()) {
			LocalDate newDate = getLocalDateFromDateTimeField(promptStart).plus(relativeTimeFieldPeriods.get(field));
			setDateTimeFieldFromLocalDate(field, newDate);
		}
	}
	
	/**
	 * Sets the value of a DateTime GUI control from a LocalDate object
	 * @param field
	 * @param date
	 */
	protected void setDateTimeFieldFromLocalDate(DateTime field, LocalDate date) 
	{
		field.setSeconds(0);
		field.setMinutes(0);
		field.setHours(0);

		field.setYear(date.getYear());
		field.setMonth(date.getMonthValue() - 1);
		field.setDay(date.getDayOfMonth());
	}
	
	/**
	 * Get the value of a DateTime GUI control as a LocalDate object
	 * @param field
	 * @return
	 */
	protected LocalDate getLocalDateFromDateTimeField(DateTime field) 
	{
		return LocalDate.of(field.getYear(), 1 + field.getMonth(), field.getDay());

	}
	
	/**
	 * Returns an English representation of a Period object in years, months and days.
	 * @param period
	 * @return
	 */
	protected String periodToNiceString(Period period) 
	{
		String result = "";
		
		int years = period.getYears();
		int months = period.getMonths();
		int days = period.getDays();
		
		if (years > 0) {
			result += years + " year";
			if (years > 1) {
				result += "s";
			}
		}

		if (months > 0) {
			if (result != "") {
				result += ", ";
			}
			result += months + " month";
			if (months > 1) {
				result += "s";
			}
		}

		if (days > 0) {
			if (result != "") {
				result += ", ";
			}
			result += days + " day";
			if (days > 1) {
				result += "s";
			}
		}
		
		if (result == "") {
			result = "0 days";
		}
		
		return result;
		
	}

	protected void updateFieldIfCheckboxSet(Button checkbox, DateTime dateField, Period amount) 
	{
		boolean selected = checkbox.getSelection();
		
		dateField.setEnabled(!selected);
		if (selected) {
			relativeTimeFieldPeriods.put(dateField, amount);
			recalculateRelativeDateFields();
		}
		else {
			relativeTimeFieldPeriods.remove(dateField);
		}
		
	}
	
	protected List<Control> createRelativeDateControls(IManagedForm mform, Composite parent, String label, String id, LocalDate date, Period amount)
	{
		List<Control> result = createLabelAndDateControl(mform, parent, label, date);
		DateTime dateField = (DateTime) result.get(1);

		String relativeTimeLabel = "Prompt start plus " + periodToNiceString(amount);
		
		String persistenceKey = id + DIALOG_RELATIVE_CHECKBOX_SUFFIX;
		
		Button checkbox = mform.getToolkit().createButton(parent, relativeTimeLabel, SWT.CHECK);
		result.add(checkbox);
		persistenceStringsForCheckboxes.put(persistenceKey, checkbox);				
		
		SelectionAdapter listener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateFieldIfCheckboxSet(checkbox, dateField, amount);
				refreshPreview();
			}			
		};		
		
		checkbox.addSelectionListener(listener);
		
		getBooleanValue(persistenceKey, checkbox);

		// manually trigger the checkbox update logic
		updateFieldIfCheckboxSet(checkbox, dateField, amount);
		
		return result;
	}
	
	/**
	 * Creates a form label and a matching date control, initialised with the specified values.
	 * 
	 * @param mform
	 * @param parent
	 * @param label
	 * @param date
	 * @return A {@code List} of the controls created.
	 */
	protected List<Control> createLabelAndDateControl(IManagedForm mform, Composite parent, String label, LocalDate date)
	{
		ArrayList<Control> result = new ArrayList<Control>();
		result.add( mform.getToolkit().createLabel(parent, label) );

		DateTime dateField = new DateTime(parent, SWT.DATE);
		result.add( dateField );
		
		dateField.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				recalculateRelativeDateFields();
				refreshPreview();
			}
			
		});


		mform.getToolkit().adapt(dateField, true, true);
		

		if (date != null) {
			setDateTimeFieldFromLocalDate(dateField, date);
		}
		
		return result;
	}

	@Override
	protected void createFormContent(final IManagedForm mform) {

		super.createFormContent(mform);
		mform.getForm().setText("Roll Forward");

		mform.getForm().getBody().setLayout(new GridLayout());

		// Dates section.
		{
			final Group datesGroup = new Group(mform.getForm().getBody(), SWT.NONE);
			datesGroup.setLayout(new GridLayout(3, false));
			datesGroup.setText("Dates to update");
			mform.getToolkit().adapt(datesGroup);

			promptStart = (DateTime) createLabelAndDateControl(mform, datesGroup, "Prompt Start", null).get(1);
			/*
			promptStart.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(final SelectionEvent e) {
					recalculateRelativeDateFields();
					refreshPreview();
				}
				
			});
			*/
			
			mform.getToolkit().createLabel(datesGroup, ""); // empty label for empty grid cell

			promptEnd = (DateTime) createRelativeDateControls(mform, datesGroup, "Prompt End", DIALOG_PROMPT_END_NAME, LocalDate.now().plusDays(90), Period.of(0, 0, 90)).get(1);

			final DateTime horizon = (DateTime) createLabelAndDateControl(mform, datesGroup, "Horizon", scenarioModel.getSchedulingEndDate()).get(1);
			mform.getToolkit().createLabel(datesGroup, ""); // empty label for empty grid cell
			
			CharterOutMarketParameters charterDates = ScenarioModelUtil.getSpotMarketsModel(scenarioModel).getCharterOutMarketParameters();
			
			if (charterDates.getCharterOutStartDate() != null) {
				charterOutStart = (DateTime) createRelativeDateControls(mform, datesGroup, "Charter start date", DIALOG_CHARTER_START_NAME, charterDates.getCharterOutStartDate(), 
						Period.of(0, 1, 0)).get(1);				
				
			}
			
			if (charterDates.getCharterOutEndDate() != null) {
				charterOutEnd = (DateTime) createLabelAndDateControl(mform, datesGroup, "Charter end date", charterDates.getCharterOutEndDate()).get(1);
				mform.getToolkit().createLabel(datesGroup, ""); // empty label for empty grid cell
			}

		}
		// Input Section
		{
			final Group inputGroup = new Group(mform.getForm().getBody(), SWT.NONE);
			inputGroup.setLayout(new GridLayout(3, false));
			inputGroup.setText("Input");
			mform.getToolkit().adapt(inputGroup);

			// Freeze Date
			{
				freezeDate = (DateTime) createRelativeDateControls(mform, inputGroup, "Freeze before", DIALOG_FREEZE_DATE_NAME, LocalDate.now().plusMonths(1), Period.of(0,  1,  0) ).get(1);
				
				// lbl.setToolTipText(TOOLTIP_FREEZE_DATE);
				freezeDate.setToolTipText(TOOLTIP_FREEZE_DATE);
			}

			/*
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
			*/
			
			/*
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
			*/
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

	private List<IRollForwardChange> generateDateChanges() {
		List<IRollForwardChange> result = new LinkedList<>();
		
		EAttribute promptStartFeature = LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_PromptPeriodStart();
		EAttribute promptEndFeature = LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_PromptPeriodEnd();
		
		result.add(new UpdateDateChange("Prompt Start", scenarioModel, promptStartFeature, getLocalDateFromDateTimeField(promptStart), domain));
		result.add(new UpdateDateChange("Prompt End", scenarioModel, promptEndFeature, getLocalDateFromDateTimeField(promptEnd), domain));
	
		
		CharterOutMarketParameters charterDates = ScenarioModelUtil.getSpotMarketsModel(scenarioModel).getCharterOutMarketParameters();

		if (charterOutStart != null) {			
			result.add(new UpdateDateChange("Charter Out Start", charterDates, SpotMarketsPackage.Literals.CHARTER_OUT_MARKET_PARAMETERS__CHARTER_OUT_START_DATE, getLocalDateFromDateTimeField(charterOutStart), domain));
		}

		if (charterOutEnd != null) {			
			result.add(new UpdateDateChange("Charter Out End", charterDates, SpotMarketsPackage.Literals.CHARTER_OUT_MARKET_PARAMETERS__CHARTER_OUT_END_DATE, getLocalDateFromDateTimeField(charterOutEnd), domain));
		}

		//final DateTime horizon = (DateTime) createLabelAndDateControl(mform, datesGroup, "Horizon", scenarioModel.getSchedulingEndDate()).get(1);

		return result ;
	}
	
	private void refreshPreview() {

		final RollForwardEngine engine = new RollForwardEngine();

		final RollForwardDescriptor descriptor = engine.new RollForwardDescriptor();
		{
			final LocalDate dt = LocalDate.of(freezeDate.getYear(), 1 + freezeDate.getMonth(), freezeDate.getDay());
			descriptor.put(RollForwardDescriptor.FreezeDate, dt);
		}
		/*
		if (removeDateCheckBox.getSelection()) {
			final LocalDate dt = LocalDate.of(removeDate.getYear(), 1 + removeDate.getMonth(), removeDate.getDay());
			descriptor.put(RollForwardDescriptor.RemoveDate, dt);
		}
		descriptor.put(RollForwardDescriptor.UpdateVessels, updateVesselsCheckBox.getSelection());
		descriptor.put(RollForwardDescriptor.RemoveVessels, updateVesselsCheckBox.getSelection() && removeVesselsCheckBox.getSelection());
		*/
		final Button button = getButton(IDialogConstants.OK_ID);
		try {
			final LocalDate dt = LocalDate.of(freezeDate.getYear(), 1 + freezeDate.getMonth(), freezeDate.getDay());
			changes = generateDateChanges();
			changes = engine.generateChanges(descriptor, domain, scenarioModel, dt, changes);			
			
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

		/*
		// Store dialog settings
		setBooleanValue(DIALOG_REMOVE_SLOT_EVENT_ENABLE, removeDateCheckBox);
		setBooleanValue(DIALOG_REMOVE_VESSELS_ENABLED, removeVesselsCheckBox);
		setBooleanValue(DIALOG_UPDATE_VESSELS_ENABLED, updateVesselsCheckBox);
		*/
		
		for (Entry<String, Button> entry: persistenceStringsForCheckboxes.entrySet()) {
			setBooleanValue(entry.getKey(), entry.getValue());
		}
		
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
