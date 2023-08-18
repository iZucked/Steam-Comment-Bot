/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart.actions;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CharterInMarketOverride;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.cargo.ui.editorpart.StructuredViewerFilterManager;
import com.mmxlabs.models.lng.cargo.ui.editorpart.TradesRowEMFPath;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.util.emfpath.EMFMultiPath;
import com.mmxlabs.models.util.emfpath.IEMFPath;

public class TrimmedVesselFilterAction extends EMFPathFilterAction {
	private static final IEMFPath vesselEMFPath = new EMFMultiPath(true,
			new TradesRowEMFPath(false, false, TradesRowEMFPath.Type.SLOT_OR_CARGO, CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE,
					CargoPackage.Literals.VESSEL_CHARTER__VESSEL),
			new TradesRowEMFPath(false, false, TradesRowEMFPath.Type.SLOT_OR_CARGO, CargoPackage.Literals.SLOT__NOMINATED_VESSEL));
	public TrimmedVesselFilterAction(StructuredViewerFilterManager filterManager , EObject sourceModel) {
		super(filterManager, "Vessels", sourceModel, FleetPackage.Literals.FLEET_MODEL__VESSELS, vesselEMFPath);	
	}
	
	@Override
	protected Collection<NamedObject> getValues() {
		if (getSourceObject() instanceof CargoModel cargoModel) {
			Set<NamedObject> vessels = new LinkedHashSet<>();
			for (final Cargo c : cargoModel.getCargoes()) {
				VesselAssignmentType vesselAT = c.getVesselAssignmentType();
				if (vesselAT instanceof VesselCharter vesselCharter) {
					final Vessel vessel = vesselCharter.getVessel();
					if (vessel != null) {
						vessels.add(vessel);
					}
				} else if (vesselAT instanceof CharterInMarket charterInMarket) {
					final Vessel vessel = charterInMarket.getVessel();
					if (vessel != null) {
						vessels.add(vessel);
					}
				} else if (vesselAT instanceof CharterInMarketOverride charterInMarketOverride) {
					final CharterInMarket charterIn = charterInMarketOverride.getCharterInMarket();
					if (charterIn == null) {
						continue;
					}
					final Vessel vessel = charterIn.getVessel();
					if (vessel != null) {
						vessels.add(vessel);
					}
				} else if (vesselAT == null) {
					Slot<?> s1 = c.getSlots().get(0);
					Slot<?> s2 = c.getSlots().get(1);
					if (s1 != null && s2 != null) {
						 if (s1.getNominatedVessel() != null && s2.getNominatedVessel() != null ) {
								if (s1.getNominatedVessel().equals(s2.getNominatedVessel())) {
									vessels.add(s1.getNominatedVessel());									
								}
							} else if (s1.getNominatedVessel() != null) {
								vessels.add(s1.getNominatedVessel());
							} else if (s2.getNominatedVessel() != null) {
								vessels.add(s2.getNominatedVessel());
							}
					} else if (s1 == null) {
						if (s2.getNominatedVessel() != null) {
							vessels.add(s2.getNominatedVessel());
						}
					} else if (s2 == null) {
						if (s1.getNominatedVessel() != null) {
							vessels.add(s1.getNominatedVessel());
						}
					}
				}
			} 			
			return vessels;
			
		} else {
			return super.getValues();
		}
	}
}
