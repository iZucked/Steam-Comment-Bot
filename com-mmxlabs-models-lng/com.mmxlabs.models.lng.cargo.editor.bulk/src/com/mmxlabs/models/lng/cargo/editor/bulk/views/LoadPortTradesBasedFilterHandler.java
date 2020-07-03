/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.editor.bulk.views;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Menu;

import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.Row;
import com.mmxlabs.models.lng.cargo.editor.bulk.ui.editorpart.BulkTradesTablePane;
import com.mmxlabs.models.lng.cargo.editor.bulk.ui.editorpart.ColumnFilters;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.DefaultMenuCreatorAction;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.rcp.common.actions.RunnableAction;

public class LoadPortTradesBasedFilterHandler implements ITradesBasedFilterHandler {

	@Override
	public Action activateAction(final ColumnFilters columnFilters, final Set<ITradesBasedFilterHandler> activeFilters, final BulkTradesTablePane viewer) {
		DefaultMenuCreatorAction action = new DefaultMenuCreatorAction("Load ports") {
			@Override
			protected void populate(Menu menu) {

				CargoModel cargoModel = ScenarioModelUtil.getCargoModel(viewer.getJointModelEditorPart().getScenarioDataProvider());
				
				List<Port> actualLoads = new LinkedList<>();
				for (Slot s : cargoModel.getLoadSlots()) {
					if (!actualLoads.contains(s.getPort())) {
						actualLoads.add(s.getPort());
					}
				}
				
				Collections.sort(actualLoads, (a, b) -> a.getName().compareTo(b.getName()));
				
				for (Port p : actualLoads) {
					RunnableAction rA = new RunnableAction(p.getName(), () -> {
						Iterator<ITradesBasedFilterHandler> itr = activeFilters.iterator();
						while(itr.hasNext()) {
							ITradesBasedFilterHandler filter = itr.next();
							if (filter instanceof PortTradesBasedFilterHandler) {
								itr.remove();
							}
						}
						activeFilters.add(new PortTradesBasedFilterHandler(p));
						viewer.getScenarioViewer().refresh();
					});
					for (final ITradesBasedFilterHandler f : activeFilters) {
						if (f instanceof PortTradesBasedFilterHandler) {
							final PortTradesBasedFilterHandler filter = (PortTradesBasedFilterHandler) f;
							if (filter.referencePort.equals(p)) {
								rA.setChecked(true);
							}
						}
					}
					addActionToMenu(rA, menu);
				}
			}

			@Override
			public void run() {
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
	}

	@Override
	public void deactivate(final ColumnFilters columnFilters, final Set<ITradesBasedFilterHandler> activeFilters) {
	}
	
	private class PortTradesBasedFilterHandler implements ITradesBasedFilterHandler {

		private Port referencePort;

		public PortTradesBasedFilterHandler(Port p) {
			this.referencePort = p;
		}

		@Override
		public Action activateAction(final ColumnFilters columnFilters, final Set<ITradesBasedFilterHandler> activeFilters, final BulkTradesTablePane viewer) {
			return null;
		}

		@Override
		public Action deactivateAction(ColumnFilters columnFilters, final Set<ITradesBasedFilterHandler> activeFilters, BulkTradesTablePane viewer) {
			return null;
		}

		@Override
		public void activate(ColumnFilters columnFilters, Set<ITradesBasedFilterHandler> activeFilters) {
		}

		@Override
		public void deactivate(final ColumnFilters columnFilters, final Set<ITradesBasedFilterHandler> activeFilters) {
			activeFilters.remove(this);
		}

		@Override
		public boolean isRowVisible(Row row) {
			if (row.getLoadSlot() != null) {
				Port port = row.getLoadSlot().getPort();
				if (port != null && port == referencePort) {
					return true;
				}
			}
			return false;
		}

		@Override
		public boolean isDefaultFilter() {
			return false;
		}

	}

}
