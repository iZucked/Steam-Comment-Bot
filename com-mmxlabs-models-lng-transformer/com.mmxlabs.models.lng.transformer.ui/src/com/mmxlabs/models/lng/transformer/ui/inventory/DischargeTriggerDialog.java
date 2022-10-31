/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.inventory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.mmxlabs.models.lng.cargo.Inventory;
import com.mmxlabs.models.lng.cargo.InventoryFacilityType;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.lng.types.VolumeUnits;

public class DischargeTriggerDialog extends TitleAreaDialog {
	public static final int DEFAULT_GLOBAL_DISCHARGE_TRIGGER = 30_000;
	public static final int DEFAULT_MATCHING_FLEXIBILITY_DAYS = 2;
	public static final int DEFAULT_MIN_QUANTITY = 134_000;
	public static final int DEFAULT_MAX_QUANTITY = 158_000;
	public static final VolumeUnits DEFAULT_VOLUME_UNITS = VolumeUnits.M3;
	
	private LNGScenarioModel model;
	private LocalDate promptStart;
	private DischargeTriggerRecord returnRecord;
	
	private ComboViewer contractsCombo = null;
	private ComboViewer inventoriesCombo = null;
	private Text dischargeTriggerText = null;
	private Text cargoMinCapacityText = null;
	private Text cargoMaxCapacityText = null;
	private ComboViewer cargoVolumeUnitsCombo = null;
	
	public DischargeTriggerDialog(Shell shell, LNGScenarioModel model, LocalDate promptStart) {
		super(shell);
		this.model = model;
		this.promptStart = promptStart;
		
		{
			returnRecord = new DischargeTriggerRecord();
			returnRecord.cutOffDate = LocalDate.now();
			returnRecord.trigger = DEFAULT_GLOBAL_DISCHARGE_TRIGGER;
			returnRecord.matchingFlexibilityDays = DEFAULT_MATCHING_FLEXIBILITY_DAYS;
			returnRecord.minQuantity = DEFAULT_MIN_QUANTITY;
			returnRecord.maxQuantity = DEFAULT_MAX_QUANTITY;
			returnRecord.volumeUnits = VolumeUnits.M3;
			
			// Null contract and inventory for later checks
			returnRecord.contract = null;
			returnRecord.inventory = null;
		}
	}
	
    @Override
    public void create() {
        super.create();
        setTitle("Discharge trigger");
    }

	@Override
	protected Control createDialogArea(Composite parent) {
		
        Composite container = (Composite) super.createDialogArea(parent);
		final Composite importingDate = new Composite(container, SWT.ALL);
		
		final GridLayout gridLayoutImportingDateRadios = new GridLayout(2, false);
		importingDate.setLayout(gridLayoutImportingDateRadios);
		final GridData gdDate = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		gdDate.horizontalSpan = 2;
		importingDate.setLayoutData(gdDate);
		
		new Label(importingDate, SWT.NONE).setText("Inventory");
		this.inventoriesCombo = new ComboViewer(importingDate);
		this.inventoriesCombo.setContentProvider(new ArrayContentProvider());
		this.inventoriesCombo.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(final Object element) {
				return ((Inventory) element).getName();
			}
		});
		this.inventoriesCombo.setInput(getInventories(this.model));
		if (inventoriesComboSelectionChangedListener != null) {
			this.inventoriesCombo.addSelectionChangedListener(this.inventoriesComboSelectionChangedListener);
		}
		
		new Label(importingDate, SWT.NONE).setText("Cut off date");

		DateTime importStartEditor = new DateTime(importingDate, SWT.DATE | SWT.BORDER | SWT.DROP_DOWN);
		importStartEditor.setLayoutData(GridDataFactory.swtDefaults().minSize(1000, -1).create());
		
		{
			final LocalDate date = returnRecord.cutOffDate;
			importStartEditor.setYear(date.getYear());
			importStartEditor.setMonth(date.getMonthValue() - 1);
			importStartEditor.setDay(date.getDayOfMonth());

		}
		importStartEditor.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(final SelectionEvent e) {
				returnRecord.cutOffDate = LocalDate.of(importStartEditor.getYear(), importStartEditor.getMonth() + 1, importStartEditor.getDay());
				processModelChanges();
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent e) {

			}
		});

		/*
		 * Hide date if not needed
		 */
		importStartEditor.setVisible(true);

		importingDate.setLayout(gridLayoutImportingDateRadios);
		gdDate.horizontalSpan = 2;
		importingDate.setLayoutData(gdDate);
		
		new Label(importingDate, SWT.NONE).setText("Global discharge trigger (m³)");
		dischargeTriggerText = new Text(importingDate, SWT.FILL | SWT.BORDER);
		dischargeTriggerText.setLayoutData(GridDataFactory.swtDefaults().minSize(10000, -1).hint(50, -1).create());
		dischargeTriggerText.setText(String.valueOf(DEFAULT_GLOBAL_DISCHARGE_TRIGGER));
		dischargeTriggerText.addListener(SWT.Verify, new Listener() {
			@Override
			public void handleEvent(Event e) {
				String string = e.text;
				char[] chars = new char[string.length()];
				string.getChars(0, chars.length, chars, 0);
				for (int i = 0; i < chars.length; i++) {
					if (!('0' <= chars[i] && chars[i] <= '9')) {
						e.doit = false;
						return;
					}
				}
			}
		});
		
		dischargeTriggerText.addModifyListener(triggerListener);
	    
	    new Label(importingDate, SWT.NONE).setText("Matching flexibility (days)");
	    Text matchingFlexibilityText = new Text(importingDate, SWT.FILL | SWT.BORDER);
	    matchingFlexibilityText.setLayoutData(GridDataFactory.swtDefaults().minSize(10000, -1).hint(50, -1).create());
	    matchingFlexibilityText.setText(String.valueOf(DEFAULT_MATCHING_FLEXIBILITY_DAYS));
	    matchingFlexibilityText.addListener(SWT.Verify, new Listener() {
			@Override
			public void handleEvent(Event e) {
				String string = e.text;
				char[] chars = new char[string.length()];
				string.getChars(0, chars.length, chars, 0);
				for (int i = 0; i < chars.length; i++) {
					if (!('0' <= chars[i] && chars[i] <= '9')) {
						e.doit = false;
						return;
					}
				}
			}
		});
		
	    matchingFlexibilityText.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				String text = matchingFlexibilityText.getText();
				if (text.matches("[0-9]+")) {
					returnRecord.matchingFlexibilityDays = Integer.valueOf(text);
				}
			}
		});

	    new Label(importingDate, SWT.NONE).setText("Sales contract");
		this.contractsCombo = new ComboViewer(importingDate);
		this.contractsCombo.setContentProvider(new ArrayContentProvider());
		this.contractsCombo.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(final Object element) {
				return ((SalesContract) element).getName();
			}
		});
		this.contractsCombo.setInput(getSalesContracts(this.model));
		if (contractsComboSelectionChangedListener != null) {
			this.contractsCombo.addSelectionChangedListener(this.contractsComboSelectionChangedListener);
		}
		
		new Label(importingDate, SWT.NONE).setText("Min cargo quantity");
	    cargoMinCapacityText = new Text(importingDate, SWT.FILL | SWT.BORDER);
	    cargoMinCapacityText.setLayoutData(GridDataFactory.swtDefaults().minSize(10000, -1).create());
	    cargoMinCapacityText.setText(String.valueOf(DEFAULT_MIN_QUANTITY));
	    cargoMinCapacityText.addListener(SWT.Verify, new Listener() {
			@Override
			public void handleEvent(Event e) {
				String string = e.text;
				char[] chars = new char[string.length()];
				string.getChars(0, chars.length, chars, 0);
				for (int i = 0; i < chars.length; i++) {
					if (!('0' <= chars[i] && chars[i] <= '9')) {
						e.doit = false;
						return;
					}
				}
			}
		});
		
	    cargoMinCapacityText.addModifyListener(minCapacityListener);
		
		new Label(importingDate, SWT.NONE).setText("Max cargo quantity");
	    cargoMaxCapacityText = new Text(importingDate, SWT.FILL | SWT.BORDER);
	    cargoMaxCapacityText.setLayoutData(GridDataFactory.swtDefaults().minSize(10000, -1).create());
	    cargoMaxCapacityText.setText(String.valueOf(DEFAULT_MAX_QUANTITY));
	    cargoMaxCapacityText.addListener(SWT.Verify, new Listener() {
			@Override
			public void handleEvent(Event e) {
				String string = e.text;
				char[] chars = new char[string.length()];
				string.getChars(0, chars.length, chars, 0);
				for (int i = 0; i < chars.length; i++) {
					if (!('0' <= chars[i] && chars[i] <= '9')) {
						e.doit = false;
						return;
					}
				}
			}
		});
		
		cargoMaxCapacityText.addModifyListener(maxCapacityListener);
		
		new Label(importingDate, SWT.NONE).setText("Volume units");
		cargoVolumeUnitsCombo = new ComboViewer(importingDate);
		cargoVolumeUnitsCombo.setContentProvider(new ArrayContentProvider());
		cargoVolumeUnitsCombo.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(final Object element) {
				return ((VolumeUnits) element).getName();
			}
		});
		cargoVolumeUnitsCombo.setInput(VolumeUnits.VALUES);
		if (cargoVolumeUnitsCombo != null) {
			cargoVolumeUnitsCombo.addSelectionChangedListener(this.cargoVolumeUnitsComboSelectionChangedListener);
		}

		return container;
	}

	@Override
	protected Control createButtonBar(Composite parent) {
		 Control bar = super.createButtonBar(parent);
		 processModelChanges();
		 return bar;
	}
	
	private void processModelChanges() {
		final List<String> modelErrors = checkModel(model);
		if (modelErrors.size() > 0) {
			this.setErrorMessage(String.format("Model errors: %s", String.join(",", modelErrors)));
			this.getOKButton().setEnabled(false);
		} else if (modelErrors.isEmpty()) {
			this.setErrorMessage(null);
			this.getOKButton().setEnabled(true);
		}
	}
	
	private List<String> checkModel(final LNGScenarioModel model) {
		List<String> errors = new LinkedList<>();
		if (this.promptStart.isAfter(returnRecord.cutOffDate)) {
			errors.add("cut-off date must be after prompt start date");
		}
		if (model.getScheduleModel() == null || model.getScheduleModel().getSchedule() == null) {
			errors.add("scenario not evaluated");
		}
		if (getInventories(model).isEmpty()) {
			errors.add("inventory not set up or no hub or downstream inventories are present");
		} else {
			if (returnRecord.inventory != null) {
				if (returnRecord.inventory.getFacilityType() == InventoryFacilityType.UPSTREAM) {
					errors.add(String.format("inventory at %s must be hub or downstream", returnRecord.inventory.getPort().getName()));
				}
				if (returnRecord.inventory.getOfftakes().isEmpty()) {
					errors.add(String.format("inventory at %s must have off-takes", returnRecord.inventory.getPort().getName()));
				}
				if (!returnRecord.inventory.getPort().getCapabilities().contains(PortCapability.DISCHARGE)) {
					errors.add(String.format("inventory %s is at the port %s which does not allow discharge", returnRecord.inventory.getName(), returnRecord.inventory.getPort().getName()));
				}
			} else {
				errors.add("please select an inventory");
			}
		}
		long missingWindows = model.getCargoModel().getDischargeSlots().stream()
				.filter(l->l.getWindowStart() == null).count();
		if (missingWindows > 0) {
			errors.add("all discharge slots must have windows");
		}
		if (getSalesContracts(model).isEmpty()) {
			errors.add("at least one feed-in sales contract must be present");
		} else {
			if (returnRecord.contract == null) {
				errors.add("please select a feed-in sales contract");
			} else {
				if (returnRecord.maxQuantity > returnRecord.contract.getMaxQuantity()) {
					errors.add("cargo volume should be less than the contract max quantity");
				}
			}
		}
		return errors;
	}
	
	@Override
	public boolean close() {
		if (this.contractsCombo != null && this.contractsComboSelectionChangedListener != null) {
			this.contractsCombo.removeSelectionChangedListener(contractsComboSelectionChangedListener);
		}
		if (this.inventoriesCombo != null && this.inventoriesComboSelectionChangedListener != null) {
			this.inventoriesCombo.removeSelectionChangedListener(inventoriesComboSelectionChangedListener);
		}
		return super.close();
	}
	
	private List<SalesContract> getSalesContracts(final LNGScenarioModel model){
		final List<SalesContract> result = new ArrayList();
		if (model != null) {
			final CommercialModel commercialModel = ScenarioModelUtil.getCommercialModel(model);
			result.addAll(commercialModel.getSalesContracts());
		}
		return result;
	}

	private ISelectionChangedListener contractsComboSelectionChangedListener = new ISelectionChangedListener() {

		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			if (contractsCombo != null) {
				final IStructuredSelection iss = contractsCombo.getStructuredSelection();
				if (iss != null && iss.getFirstElement() instanceof SalesContract sc) {
					returnRecord.contract = sc;
					cargoMinCapacityText.setText(String.valueOf(sc.getMinQuantity()));
					cargoMaxCapacityText.setText(String.valueOf(sc.getMaxQuantity()));
					cargoVolumeUnitsCombo.setSelection(new StructuredSelection(sc.getVolumeLimitsUnit()));
				}
				processModelChanges();
			}
		}
	};
	
	private ModifyListener minCapacityListener = new ModifyListener() {
		
		@Override
		public void modifyText(ModifyEvent e) {
			if (cargoMinCapacityText != null) {
				final String text = cargoMinCapacityText.getText();
				if (text.matches("[0-9]+")) {
					returnRecord.minQuantity = Integer.valueOf(text);
					processModelChanges();
				}
			}
		}
	};
	
	private ModifyListener maxCapacityListener = new ModifyListener() {
		
		@Override
		public void modifyText(ModifyEvent e) {
			if (cargoMaxCapacityText != null) {
				final String text = cargoMaxCapacityText.getText();
				if (text.matches("[0-9]+")) {
					returnRecord.maxQuantity = Integer.valueOf(text);
					processModelChanges();
				}
			}
		}
	};
	
	private ISelectionChangedListener cargoVolumeUnitsComboSelectionChangedListener = new ISelectionChangedListener() {

		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			if (cargoVolumeUnitsCombo != null) {
				final IStructuredSelection iss = cargoVolumeUnitsCombo.getStructuredSelection();
				if (iss != null && iss.getFirstElement() instanceof VolumeUnits volumeUnit) {
					returnRecord.volumeUnits = volumeUnit;
				}
				processModelChanges();
			}
		}
	};
	
	private List<Inventory> getInventories(final LNGScenarioModel model){
		final List<Inventory> result = new ArrayList();
		if (model != null) {
			for(final Inventory inventory : model.getCargoModel().getInventoryModels()) {
				if (inventory.getFacilityType() != InventoryFacilityType.UPSTREAM) {
					result.add(inventory);
				}
			}
		}
		return result;
	}
	
	private ISelectionChangedListener inventoriesComboSelectionChangedListener = new ISelectionChangedListener() {

		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			if (inventoriesCombo != null) {
				final IStructuredSelection iss = inventoriesCombo.getStructuredSelection();
				if (iss != null && iss.getFirstElement() instanceof Inventory inventory) {
					returnRecord.inventory = inventory;
				}
				processModelChanges();
			}
		}
	};
	
	private ModifyListener triggerListener = new ModifyListener() {
		
		@Override
		public void modifyText(ModifyEvent e) {
			if (dischargeTriggerText != null) {
				String text = dischargeTriggerText.getText();
				if (text.matches("[0-9]+")) {
					returnRecord.trigger = Integer.valueOf(text);
				}
			}
		}
	};
	
	public DischargeTriggerRecord getDischargeTriggerRecord() {
		return this.returnRecord;
	}
}
