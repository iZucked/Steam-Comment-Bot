/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;

public class CargoWiringViewer extends Composite {
	private final IScenarioEditingLocation part;

	public CargoWiringViewer(final Composite parent, final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(parent, SWT.NONE);
		this.part = location;
		final GridLayout gl_shell = new GridLayout(1, false);
		gl_shell.verticalSpacing = 6;
		this.setLayout(gl_shell);

		final ScrolledComposite scrolledComposite = new ScrolledComposite(this, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		// scrolledComposite.setBounds(0, 0, 400 ,400);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		scrolledComposite.setAlwaysShowScrollBars(true);

		final CargoWiringComposite wiringComposite = new CargoWiringComposite(scrolledComposite, SWT.NONE);
		scrolledComposite.setContent(wiringComposite);
		scrolledComposite.setMinSize(wiringComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		scrolledComposite.setLayout(new FillLayout());
		wiringComposite.setLocation(location);

		scrolledComposite.addControlListener(new ControlAdapter() {
			public void controlResized(final ControlEvent e) {
				final Rectangle r = scrolledComposite.getClientArea();
				scrolledComposite.setMinSize(parent.computeSize(r.width, SWT.DEFAULT));
			}
		});

		final Composite buttonsComposite = new Composite(this, SWT.NONE);
		buttonsComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		buttonsComposite.setBounds(0, 0, 64, 64);
		buttonsComposite.setLayout(new GridLayout(3, false));

		((GridLayout) buttonsComposite.getLayout()).marginWidth = 0;

		final CargoModel cargoModel = location.getRootObject().getSubModel(CargoModel.class);

		wiringComposite.setCargoes(cargoModel.getCargoes());
	}

	@Override
	public void dispose() {

		super.dispose();
	}

}
