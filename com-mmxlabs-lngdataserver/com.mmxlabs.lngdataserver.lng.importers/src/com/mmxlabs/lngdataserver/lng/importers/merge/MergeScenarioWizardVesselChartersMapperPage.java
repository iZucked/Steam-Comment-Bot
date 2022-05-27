/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.merge;

import java.util.List;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;

public class MergeScenarioWizardVesselChartersMapperPage extends AbstractEObjectMergeScenarioWizardDataMapperPage {
	Button checkBox;

	public MergeScenarioWizardVesselChartersMapperPage(String title) {
		super(title, s -> ScenarioModelUtil.getCargoModel(s), CargoPackage.Literals.CARGO_MODEL__VESSEL_CHARTERS);
	}

	@Override
	public void createControl(Composite parent) {
		final Composite composite = new Composite(parent, SWT.NONE | SWT.BORDER);
		GridLayout layout = new GridLayout(1, false);
		composite.setLayout(layout);
		mergeTableViewer = new TableViewer(composite, SWT.BORDER | SWT.FULL_SELECTION);
		mergeTableViewer.getTable().setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
		checkBox = new Button(composite, SWT.CHECK);
		checkBox.setLayoutData(GridDataFactory.swtDefaults().create());
		checkBox.setText("Take earliest/latest start end dates and update target fleet charters.");
		checkBox.setSelection(true);
		dialogChanged();
		setControl(composite);
	}

	@Override
	public void merge(CompoundCommand cmd, MergeHelper mergeHelper) throws Exception {
		super.merge(cmd, mergeHelper);
		if (checkBox.getSelection()) {
			mergeHelper.updateVesselCharterStartEndDates(cmd);
		}
	}

	@Override
	protected String getName(Object o) {
		if (o instanceof VesselCharter va) {
			if (va.getVessel() == null) {
				return "Unknown";
			} else {
				return va.getVessel().getName() + "-" + Integer.toString(va.getCharterNumber());
			}
		} else {
			return super.getName(o);
		}
	}

	@Override
	protected List<? extends EObject> getEObjects(LNGScenarioModel sm) {
		List<? extends EObject> emfObjects = sm.getCargoModel().getVesselCharters();
		return emfObjects;
	}
}
