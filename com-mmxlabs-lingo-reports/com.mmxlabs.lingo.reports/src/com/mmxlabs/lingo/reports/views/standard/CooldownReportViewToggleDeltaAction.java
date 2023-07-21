/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;

import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconMode;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;

public class CooldownReportViewToggleDeltaAction extends Action {
	private CooldownReportView view;
	private Viewer viewer;
	
	public CooldownReportViewToggleDeltaAction(CooldownReportView view, Boolean startChecked, Viewer viewer) {
		super("Toggle Diff", Action.AS_CHECK_BOX);
		setImageDescriptor(CommonImages.getImageDescriptor(IconPaths.Delta, IconMode.Enabled));
		this.view = view;
		this.viewer = viewer;
		setChecked(startChecked);
	}
	
	@Override
	public void run() {
		boolean set = isChecked();
		view.setDeltaMode(set);
	}
}
