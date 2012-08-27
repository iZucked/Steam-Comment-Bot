/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.rcp.common.actions.LockableAction;

public class CargoWiringViewer extends Composite {
	private final IScenarioEditingLocation location;
	protected ToolBarManager toolBarManager;
	private CargoWiringComposite wiringComposite;
	private ToolBar actionBar;

	public CargoWiringViewer(final Composite parent, final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(parent, SWT.NONE);
		this.location = location;

		actionBar = new ToolBar(this, SWT.FLAT | SWT.WRAP);

		final GridLayout gl_shell = new GridLayout(1, false);
		gl_shell.verticalSpacing = 6;
		this.setLayout(gl_shell);

		final ScrolledComposite scrolledComposite = new ScrolledComposite(this, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		// scrolledComposite.setBounds(0, 0, 400 ,400);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		scrolledComposite.setAlwaysShowScrollBars(true);

		wiringComposite = new CargoWiringComposite(scrolledComposite, SWT.NONE, part.getSite());
		scrolledComposite.setContent(wiringComposite);
		scrolledComposite.setMinSize(wiringComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		scrolledComposite.setLayout(new FillLayout());
		wiringComposite.setLocation(location);

		this.addControlListener(new ControlAdapter() {
			public void controlResized(final ControlEvent e) {
				scrolledComposite.setMinSize(wiringComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			}
		});
		final CargoModel cargoModel = location.getRootObject().getSubModel(CargoModel.class);

		wiringComposite.setCargoes(cargoModel.getCargoes());
	}

	public void setLocked(final boolean locked) {
		wiringComposite.setLocked(locked);

		for (final IContributionItem item : getToolBarManager().getItems()) {
			if (item instanceof ActionContributionItem) {
				final IAction action = ((ActionContributionItem) item).getAction();
				if (action instanceof LockableAction) {
					((LockableAction) action).setLocked(locked);
				}
			}
		}
	}

	public ToolBarManager getToolBarManager() {
		if (toolBarManager == null) {
			toolBarManager = new ToolBarManager(actionBar);
		}
		return toolBarManager;
	}

	@Override
	public void dispose() {

		super.dispose();
	}
}
