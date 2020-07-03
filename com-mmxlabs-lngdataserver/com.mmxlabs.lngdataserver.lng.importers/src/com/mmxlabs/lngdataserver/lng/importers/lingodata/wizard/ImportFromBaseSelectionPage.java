/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.lingodata.wizard;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.mmxlabs.lngdataserver.lng.importers.lingodata.wizard.SharedScenarioDataUtils.DataOptions;

public class ImportFromBaseSelectionPage extends WizardPage {

	public static class DataOptionGroup {
		String name;
		List<DataOptions> options;
		boolean enabled = true;
		boolean needsUpdate;
		boolean selected = false;

		public DataOptionGroup(String name, boolean needsUpdate, DataOptions... opts) {
			this(name, needsUpdate, true, true, opts);
		}

		public DataOptionGroup(String name, boolean needsUpdate, boolean enabled, boolean selected, DataOptions... opts) {
			this.name = name;
			this.needsUpdate = needsUpdate;
			this.enabled = enabled;
			this.selected = selected;
			this.options = new LinkedList<>();
			for (DataOptions opt : opts) {
				options.add(opt);
			}
		}
	}

	private Composite container;

	private List<DataOptionGroup> data;

	private String message;

	public ImportFromBaseSelectionPage(String title, String message, List<DataOptionGroup> data) {
		super(title);
		setDescription(message);
		this.message = message;
		this.data = data;
		setTitle(title);
	}

	@Override

	public void createControl(final Composite parent) {

		container = new Composite(parent, SWT.NONE);

		final GridLayout layout = new GridLayout(3, false);
		container.setLayout(layout);

		// Col headers
		{
			Label lbl = new Label(container, SWT.NONE);
			lbl.setText("Data");

			Label status = new Label(container, SWT.NONE);
			status.setText("Status");

			Label update = new Label(container, SWT.NONE);
			update.setText("Update?");
		}
		for (DataOptionGroup option : data) {
			Label lbl = new Label(container, SWT.NONE);
			lbl.setText(option.name);

			Label status = new Label(container, SWT.NONE);
			boolean isOK = !option.needsUpdate;
			status.setText(isOK ? "OK" : "Differs");

			Button onOff = new Button(container, SWT.CHECK);
			if (!option.enabled || isOK) {
				onOff.setSelection(false);
				onOff.setEnabled(false);
			} else {
				onOff.setSelection(option.selected);
				onOff.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						super.widgetSelected(e);
						option.selected = onOff.getSelection();
						updatePageStatus();
					}
				});
			}
		}
		setControl(container);

		updatePageStatus();
	}

	private void updatePageStatus() {
		setPageComplete(data.stream() //
				.filter(e -> (e.enabled && e.selected)) //
				.count() > 0);
	}
}