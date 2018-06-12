/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.editor.bulk.views;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Menu;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.CargoBulkEditorPackage;
import com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.Row;
import com.mmxlabs.models.lng.cargo.editor.bulk.ui.editorpart.BulkTradesTablePane;
import com.mmxlabs.models.lng.cargo.editor.bulk.ui.editorpart.ColumnFilters;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.DefaultMenuCreatorAction;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.util.emfpath.EMFPath;
import com.mmxlabs.rcp.common.actions.RunnableAction;

public class LoadPortTradesBasedFilterHandler implements ITradesBasedFilterHandler {

	@Override
	public Action activateAction(final ColumnFilters columnFilters, final Set<ITradesBasedFilterHandler> activeFilters, final BulkTradesTablePane viewer) {
		DefaultMenuCreatorAction action = new DefaultMenuCreatorAction("Filter port") {
			@Override
			protected void populate(Menu menu) {
				// addActionToMenu(clearAction, menu);
				PortModel portModel = ScenarioModelUtil.getPortModel(viewer.getJointModelEditorPart().getScenarioDataProvider());

				List<Port> loads = new LinkedList<>();
				for (Port p : portModel.getPorts()) {
					if (p.getCapabilities().contains(PortCapability.LOAD)) {
						loads.add(p);
					}
				}
				Collections.sort(loads, (a, b) -> a.getName().compareTo(b.getName()));

				for (Port p : loads) {
					addActionToMenu(new RunnableAction(p.getName(), () -> {
						activeFilters.add(new PortTradesBasedFilterHandler(p));
						viewer.getScenarioViewer().refresh();
					}

					), menu);
				}
				// final EMFPath loadPortPath = new EMFPath(false, CargoBulkEditorPackage.Literals.ROW__LOAD_SLOT, CargoPackage.Literals.SLOT__PORT);
				//
				// addActionToMenu(new FilterAction("Load ports", portModel, PortPackage.Literals.PORT_MODEL__PORTS, loadPortPath), menu);

			}

			@Override
			public void run() {
				// columnFilters.resetDefaultTypeFilters();
				// for (ITradesBasedFilterHandler filter : activeFilters.toArray(new ITradesBasedFilterHandler[activeFilters.size()])) {
				// if (!filter.isDefaultFilter()) {
				// filter.deactivate(columnFilters, activeFilters);
				// }
				// }
				// viewer.setColumnsVisibility(); // TODO (move this method out of NewStyleCargoEditorPane)
				// viewer.getScenarioViewer().refresh();
			}
		};
		return action;
	}

	@Override
	public Action deactivateAction(final ColumnFilters columnFilters, final Set<ITradesBasedFilterHandler> activeFilters, final BulkTradesTablePane viewer) {
		return null;
	}

	@Override
	public boolean isRowVisible(Row row) {
		return true;
	}

	@Override
	public boolean isDefaultFilter() {
		return true;
	}

	@Override
	public void activate(ColumnFilters columnFilters, Set<ITradesBasedFilterHandler> activeFilters) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deactivate(final ColumnFilters columnFilters, final Set<ITradesBasedFilterHandler> activeFilters) {
	}

}
