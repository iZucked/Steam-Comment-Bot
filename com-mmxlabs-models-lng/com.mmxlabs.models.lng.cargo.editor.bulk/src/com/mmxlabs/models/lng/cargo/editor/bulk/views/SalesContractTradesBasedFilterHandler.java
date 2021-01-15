/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
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
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.rcp.common.actions.RunnableAction;

public class SalesContractTradesBasedFilterHandler implements ITradesBasedFilterHandler {

	@Override
	public Action activateAction(ColumnFilters columnFilters, Set<ITradesBasedFilterHandler> activeFilters, BulkTradesTablePane viewer) {
		DefaultMenuCreatorAction action = new DefaultMenuCreatorAction("Sale contracts") {
			@Override
			protected void populate(Menu menu) {
					CargoModel cargoModel = ScenarioModelUtil.getCargoModel(viewer.getJointModelEditorPart().getScenarioDataProvider());
				
				List<Contract> contracts = new LinkedList<>();
				for (Slot s : cargoModel.getDischargeSlots()) {
					if (!contracts.contains(s.getContract())) {
						if (s.getContract() != null && s.getContract().getName() != null)
						{
							contracts.add(s.getContract());
						}
					}
				}
				
				Collections.sort(contracts, (a, b) -> a.getName().compareTo(b.getName()));

				for (Contract c : contracts) {
					RunnableAction rA = new RunnableAction(c.getName(), () -> {
						Iterator<ITradesBasedFilterHandler> itr = activeFilters.iterator();
						while(itr.hasNext()) {
							ITradesBasedFilterHandler filter = itr.next();
							if (filter instanceof ContractTradesBasedFilterHandler) {
								itr.remove();
							}
						}
						activeFilters.add(new ContractTradesBasedFilterHandler(c));
						viewer.getScenarioViewer().refresh();
					});
					for (final ITradesBasedFilterHandler f : activeFilters) {
						if (f instanceof ContractTradesBasedFilterHandler) {
							final ContractTradesBasedFilterHandler filter = (ContractTradesBasedFilterHandler) f;
							if (filter.referenceContract.equals(c)) {
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
	public Action deactivateAction(ColumnFilters columnFilters, Set<ITradesBasedFilterHandler> activeFilters, BulkTradesTablePane viewer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void activate(ColumnFilters columnFilters, Set<ITradesBasedFilterHandler> activeFilters) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deactivate(ColumnFilters columnFilters, Set<ITradesBasedFilterHandler> activeFilters) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isRowVisible(Row row) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isDefaultFilter() {
		// TODO Auto-generated method stub
		return true;
	}
	
	private class ContractTradesBasedFilterHandler implements ITradesBasedFilterHandler {

		private Contract referenceContract;

		public ContractTradesBasedFilterHandler(Contract c) {
			this.referenceContract = c;
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
			activeFilters.remove(this); // TODO - how about isActive?
			// columnFilters.removeGroupFilter(TradesBasedColumnFactory.LOAD_START_GROUP, TradesBasedColumnFactory.LOAD_START_GROUP);
			// columnFilters.removeGroupFilter(TradesBasedColumnFactory.DISCHARGE_START_GROUP, TradesBasedColumnFactory.DISCHARGE_START_GROUP);
		}

		@Override
		public boolean isRowVisible(Row row) {
			if (row.getDischargeSlot() != null) {
				Contract contract = row.getDischargeSlot().getContract();
				if (contract != null && contract == referenceContract) {
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
