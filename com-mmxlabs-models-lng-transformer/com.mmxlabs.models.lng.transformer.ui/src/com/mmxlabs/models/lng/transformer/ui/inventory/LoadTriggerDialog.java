/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.inventory;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.layout.GridDataFactory;
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
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;

public class LoadTriggerDialog extends TitleAreaDialog {
	public static final int DEFAULT_GLOBAL_LOAD_TRIGGER = 200_000;
	public static final int DEFAULT_VOLUME = 158_000;
	private LocalDate selectedDate = LocalDate.now();
	private Integer globalLoadTrigger = DEFAULT_GLOBAL_LOAD_TRIGGER;
	private Integer cargoVolume = DEFAULT_VOLUME;
	private LNGScenarioModel model;
	
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

		return container;
	}

	@Override
	protected Control createButtonBar(Composite parent) {
		 Control bar = super.createButtonBar(parent);
		 
			List<String> modelErrors = checkModel(model);
			if (modelErrors.size() > 0) {
				this.setErrorMessage(String.format("Model errors: %s", String.join(",", modelErrors)));
				this.getOKButton().setEnabled(false);
			}

		 return bar;
	}
	
	private List<String> checkModel(LNGScenarioModel model) {
		List<String> errors = new LinkedList<>();
		if (model.getScheduleModel() == null || model.getScheduleModel().getSchedule() == null) {
			errors.add("scenario not evaluated");
		}
		if (model.getCargoModel().getInventoryModels() == null || model.getCargoModel().getInventoryModels().isEmpty()) {
			errors.add("inventory not set up");
		} else {
			for (Inventory inventory : model.getCargoModel().getInventoryModels()) {
				if (inventory.getFeeds().isEmpty()) {
					errors.add(String.format("inventory at %s must have feeds", inventory.getPort().getName()));
				}
			}
		}
		long missingWindows = model.getCargoModel().getLoadSlots().stream()
				.filter(l->l.getWindowStart() == null).count();
		if (missingWindows > 0) {
			errors.add("all load slots must have windows");
		}
		return errors;
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
}
