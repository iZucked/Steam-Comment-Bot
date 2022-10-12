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
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
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

public class DischargeTriggerDialog extends TitleAreaDialog {
	public static final int DEFAULT_GLOBAL_DISCHARGE_TRIGGER = 30_000;
	public static final int DEFAULT_MATCHING_FLEXIBILITY_DAYS = 2;
	public static final int DEFAULT_VOLUME = 158_000;
	private LocalDate selectedDate = LocalDate.now();
	private Integer globalDischargeTrigger = DEFAULT_GLOBAL_DISCHARGE_TRIGGER;
	private Integer cargoVolume = DEFAULT_VOLUME;
	private LNGScenarioModel model;
	private ComboViewer contractsCombo = null;
	private SalesContract selectedContract = null;
	private ComboViewer inventoriesCombo = null;
	private Inventory selectedInventory = null;
	private LocalDate promptStart;
	private Integer matchingFlexibilityDays = DEFAULT_MATCHING_FLEXIBILITY_DAYS;
	
	public DischargeTriggerDialog(Shell shell, LNGScenarioModel model, LocalDate promptStart) {
		super(shell);
		this.model = model;
		this.promptStart = promptStart;
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
		//gridLayoutImportingDateRadios.marginLeft -= 5;
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
		
		new Label(importingDate, SWT.NONE).setText("Start date");

		DateTime importStartEditor = new DateTime(importingDate, SWT.DATE | SWT.BORDER | SWT.DROP_DOWN);
		importStartEditor.setLayoutData(GridDataFactory.swtDefaults().minSize(1000, -1).create());
		
		{
			final LocalDate date = selectedDate;
			importStartEditor.setYear(date.getYear());
			importStartEditor.setMonth(date.getMonthValue() - 1);
			importStartEditor.setDay(date.getDayOfMonth());

		}
		importStartEditor.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(final SelectionEvent e) {
				selectedDate = LocalDate.of(importStartEditor.getYear(), importStartEditor.getMonth() + 1, importStartEditor.getDay());
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
	    Text loadTriggerText = new Text(importingDate, SWT.FILL | SWT.BORDER);
	    loadTriggerText.setLayoutData(GridDataFactory.swtDefaults().minSize(10000, -1).create());
	    loadTriggerText.setText(String.valueOf(DEFAULT_GLOBAL_DISCHARGE_TRIGGER));
	    loadTriggerText.addListener(SWT.Verify, new Listener() {
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
		
	    loadTriggerText.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				String text = loadTriggerText.getText();
				if (text.matches("[0-9]+")) {
					setGlobalDischargeTrigger(Integer.valueOf(text));
				}
			}
		});
	    
	    new Label(importingDate, SWT.NONE).setText("Matching flexibility (days)");
	    Text matchingFlexibilityText = new Text(importingDate, SWT.FILL | SWT.BORDER);
	    matchingFlexibilityText.setLayoutData(GridDataFactory.swtDefaults().minSize(10000, -1).create());
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
					setMatchingFlexibilityDays(Integer.valueOf(text));
				}
			}
		});

		
		new Label(importingDate, SWT.NONE).setText("Cargo volume  (m³)");
	    Text cargoVolText = new Text(importingDate, SWT.FILL | SWT.BORDER);
	    cargoVolText.setLayoutData(GridDataFactory.swtDefaults().minSize(10000, -1).create());
	    cargoVolText.setText(String.valueOf(DEFAULT_VOLUME));
		cargoVolText.addListener(SWT.Verify, new Listener() {
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
		
		cargoVolText.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				String text = cargoVolText.getText();
				if (text.matches("[0-9]+")) {
					setCargoVolume(Integer.valueOf(text));
					processModelChanges();
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
		if (this.promptStart.isAfter(getSelectedDate())) {
			errors.add("start date must be after prompt start date");
		}
		if (model.getScheduleModel() == null || model.getScheduleModel().getSchedule() == null) {
			errors.add("scenario not evaluated");
		}
		if (getInventories(model).isEmpty()) {
			errors.add("inventory not set up or no hub or downstream inventories are present");
		} else {
			if (selectedInventory != null) {
				if (selectedInventory.getFacilityType() == InventoryFacilityType.UPSTREAM) {
					errors.add(String.format("inventory at %s must be hub or downstream", selectedInventory.getPort().getName()));
				}
				if (selectedInventory.getOfftakes().isEmpty()) {
					errors.add(String.format("inventory at %s must have off-takes", selectedInventory.getPort().getName()));
				}
				if (!selectedInventory.getPort().getCapabilities().contains(PortCapability.DISCHARGE)) {
					errors.add(String.format("inventory %s is at the port %s which does not allow discharge", selectedInventory.getName(), selectedInventory.getPort().getName()));
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
			if (selectedContract == null) {
				errors.add("please select a feed-in sales contract");
			} else {
				if (cargoVolume > selectedContract.getMaxQuantity()) {
					errors.add("cargo volume should be less than the contract max quantity");
				}
			}
		}
		return errors;
	}
	
	public Inventory getSelectedInventory() {
		return this.selectedInventory;
	}
	
	public SalesContract getSelectedContract() {
		return this.selectedContract;
	}

	public LocalDate getSelectedDate() {
		return selectedDate;
	}

	public void setSelectedDate(LocalDate selectedDate) {
		this.selectedDate = selectedDate;
	}

	public Integer getCargoVolume() {
		return cargoVolume;
	}

	public void setCargoVolume(Integer cargoVolume) {
		this.cargoVolume = cargoVolume;
	}

	public Integer getGlobalDischargeTrigger() {
		return globalDischargeTrigger;
	}

	public void setGlobalDischargeTrigger(Integer globalDischargeTrigger) {
		this.globalDischargeTrigger = globalDischargeTrigger;
	}
	
	public Integer getMatchingFlexibilityDays() {
		return matchingFlexibilityDays;
	}

	public void setMatchingFlexibilityDays(Integer matchingFlexibilityDays) {
		this.matchingFlexibilityDays = matchingFlexibilityDays;
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
					selectedContract = sc;
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
					selectedInventory = inventory;
				}
				processModelChanges();
			}
		}
	};
}
