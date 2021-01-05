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

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CharterInMarketOverride;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.Row;
import com.mmxlabs.models.lng.cargo.editor.bulk.ui.editorpart.BulkTradesTablePane;
import com.mmxlabs.models.lng.cargo.editor.bulk.ui.editorpart.ColumnFilters;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.DefaultMenuCreatorAction;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.rcp.common.actions.RunnableAction;

public class VesselsTradesBasedFilterHandler implements ITradesBasedFilterHandler {

	@Override
	public Action activateAction(ColumnFilters columnFilters, Set<ITradesBasedFilterHandler> activeFilters, BulkTradesTablePane viewer) {
		DefaultMenuCreatorAction action = new DefaultMenuCreatorAction("Vessels") {
			@Override
			protected void populate(Menu menu) {
				
				final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(viewer.getJointModelEditorPart().getScenarioDataProvider());
				
				List<Vessel> vessels = new LinkedList<>();
				for (final Cargo c : cargoModel.getCargoes()) {
					VesselAssignmentType vesselAT = c.getVesselAssignmentType();
					if(vesselAT instanceof VesselAvailability) {
						final Vessel vessel = ((VesselAvailability) vesselAT).getVessel();
						if (vessel != null && !vessels.contains(vessel)) {
							vessels.add(vessel);
						}
					}
					if(vesselAT instanceof CharterInMarket) {
						final Vessel vessel = ((CharterInMarket) vesselAT).getVessel();
						if (vessel != null && !vessels.contains(vessel)) {
							vessels.add(vessel);
						}
					}
					if(vesselAT instanceof CharterInMarketOverride) {
						final CharterInMarket charterIn = ((CharterInMarketOverride) vesselAT).getCharterInMarket();
						if (charterIn == null) {
							continue;
						}
						final Vessel vessel = charterIn.getVessel();
						if (vessel != null && !vessels.contains(vessel)) {
							vessels.add(vessel);
						}
					}
				}
				
				Collections.sort(vessels, (a, b) -> a.getName().compareTo(b.getName()));

				for (Vessel v : vessels) {
					RunnableAction rA = new RunnableAction(v.getName(), () -> {
						Iterator<ITradesBasedFilterHandler> itr = activeFilters.iterator();
						while(itr.hasNext()) {
							ITradesBasedFilterHandler filter = itr.next();
							if (filter instanceof VesselTradesBasedFilterHandler) {
								itr.remove();
							}
						}
						activeFilters.add(new VesselTradesBasedFilterHandler(v));
						viewer.getScenarioViewer().refresh();
					});
					for (final ITradesBasedFilterHandler f : activeFilters) {
						if (f instanceof VesselTradesBasedFilterHandler) {
							final VesselTradesBasedFilterHandler filter = (VesselTradesBasedFilterHandler) f;
							if (v.equals(filter.referenceVessel)) {
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
	
	private class VesselTradesBasedFilterHandler implements ITradesBasedFilterHandler {

		private Vessel referenceVessel;

		public VesselTradesBasedFilterHandler(Vessel v) {
			this.referenceVessel = v;
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
		public boolean isRowVisible(final Row row) {
			final Cargo cargo = row.getCargo();
			if (cargo != null) {
				final VesselAssignmentType vesselAT = cargo.getVesselAssignmentType();
				if(vesselAT instanceof VesselAvailability) {
					final Vessel vessel = ((VesselAvailability) vesselAT).getVessel();
					if (vessel != null && vessel.equals(referenceVessel)) {
						return true;
					}
				}
				if(vesselAT instanceof CharterInMarket) {
					final Vessel vessel = ((CharterInMarket) vesselAT).getVessel();
					if (vessel != null && vessel.equals(referenceVessel)) {
						return true;
					}
				}
				if(vesselAT instanceof CharterInMarketOverride) {
					final CharterInMarket charterIn = ((CharterInMarketOverride) vesselAT).getCharterInMarket();
					if (charterIn == null) {
						return false;
					}
					final Vessel vessel = charterIn.getVessel();
					if (vessel != null && vessel.equals(referenceVessel)) {
						return true;
					}
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
