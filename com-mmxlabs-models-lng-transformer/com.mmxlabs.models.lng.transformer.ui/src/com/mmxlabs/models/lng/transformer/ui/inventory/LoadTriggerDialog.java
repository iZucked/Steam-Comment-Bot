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
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.types.PortCapability;

public class LoadTriggerDialog extends TitleAreaDialog {
	public static final int DEFAULT_GLOBAL_LOAD_TRIGGER = 200_000;
	public static final int DEFAULT_VOLUME = 158_000;
	private LocalDate selectedDate = LocalDate.now();
	private Integer globalLoadTrigger = DEFAULT_GLOBAL_LOAD_TRIGGER;
	private Integer cargoVolume = DEFAULT_VOLUME;
	private LNGScenarioModel model;
	private ComboViewer contractsCombo = null;
	private PurchaseContract selectedContract = null;
	private ComboViewer inventoriesCombo = null;
	private Inventory selectedInventory = null;
	
	public LoadTriggerDialog(Shell shell, LNGScenarioModel model, LocalDate promptStart) {
		super(shell);
		this.model = model;
	}
	
    @Override
    public void create() {
        super.create();
        setTitle("Load trigger");
    }

	@Override
	protected Control createDialogArea(Composite parent) {
		
        Composite container = (Composite) super.createDialogArea(parent);
		final Composite importingDate = new Composite(container, SWT.ALL);
		
		final GridLayout gridLayoutImportingDateRadios = new GridLayout(2, false);
		gridLayoutImportingDateRadios.marginLeft -= 5;
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
		
		new Label(importingDate, SWT.NONE).setText("Global load trigger (m³)");
	    Text loadTriggerText = new Text(importingDate, SWT.FILL | SWT.BORDER);
	    loadTriggerText.setLayoutData(GridDataFactory.swtDefaults().minSize(10000, -1).create());
	    loadTriggerText.setText(String.valueOf(DEFAULT_GLOBAL_LOAD_TRIGGER));
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
					setGlobalLoadTrigger(Integer.valueOf(text));
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
				}
			}
		});
		
		new Label(importingDate, SWT.NONE).setText("Purchase contract");
		this.contractsCombo = new ComboViewer(importingDate);
		this.contractsCombo.setContentProvider(new ArrayContentProvider());
		this.contractsCombo.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(final Object element) {
				return ((PurchaseContract) element).getName();
			}
		});
		this.contractsCombo.setInput(getPurchaseContracts(this.model));
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
	
	private List<String> checkModel(LNGScenarioModel model) {
		List<String> errors = new LinkedList<>();
		if (model.getScheduleModel() == null || model.getScheduleModel().getSchedule() == null) {
			errors.add("scenario not evaluated");
		}
		if (getInventories(model).isEmpty()) {
			errors.add("inventory not set up");
		} else {
			if (selectedInventory != null) {
				if (selectedInventory.getFacilityType() == InventoryFacilityType.DOWNSTREAM) {
					errors.add(String.format("inventory at %s must be hub or downstream", selectedInventory.getPort().getName()));
				}
				if (selectedInventory.getFeeds().isEmpty()) {
					errors.add(String.format("inventory at %s must have feeds", selectedInventory.getPort().getName()));
				}
				if (!selectedInventory.getPort().getCapabilities().contains(PortCapability.LOAD)) {
					errors.add(String.format("inventory %s is at the port %s which does not allow load", selectedInventory.getName(), selectedInventory.getPort().getName()));
				}
			} else {
				errors.add("please select an inventory");
			}
		}
		long missingWindows = model.getCargoModel().getLoadSlots().stream()
				.filter(l->l.getWindowStart() == null).count();
		if (missingWindows > 0) {
			errors.add("all load slots must have windows");
		}
		if (getPurchaseContracts(model).isEmpty()) {
			errors.add("at least one off-take purchase contract must be present");
		} else {
			if (selectedContract == null) {
				errors.add("please select an off-take purchase contract");
			}
		}
		return errors;
	}
	
	public Inventory getSelectedInventory() {
		return this.selectedInventory;
	}
	
	public PurchaseContract getSelectedContract() {
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

	public Integer getGlobalLoadTrigger() {
		return globalLoadTrigger;
	}

	public void setGlobalLoadTrigger(Integer globalLoadTrigger) {
		this.globalLoadTrigger = globalLoadTrigger;
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
	
	private List<PurchaseContract> getPurchaseContracts(final LNGScenarioModel model){
		final List<PurchaseContract> result = new ArrayList();
		if (model != null) {
			final CommercialModel commercialModel = ScenarioModelUtil.getCommercialModel(model);
			result.addAll(commercialModel.getPurchaseContracts());
		}
		return result;
	}

	private ISelectionChangedListener contractsComboSelectionChangedListener = new ISelectionChangedListener() {

		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			if (contractsCombo != null) {
				final IStructuredSelection iss = contractsCombo.getStructuredSelection();
				if (iss != null && iss.getFirstElement() instanceof PurchaseContract pc) {
					selectedContract = pc;
				}
				processModelChanges();
			}
		}
	};
	
	private List<Inventory> getInventories(final LNGScenarioModel model){
		final List<Inventory> result = new ArrayList();
		if (model != null) {
			for(final Inventory inventory : model.getCargoModel().getInventoryModels()) {
				if (inventory.getFacilityType() != InventoryFacilityType.DOWNSTREAM) {
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
