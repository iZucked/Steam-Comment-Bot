/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.importer;

import java.io.IOException;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.fleet.AssignableElement;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.VesselEvent;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.util.importer.CSVReader;
import com.mmxlabs.models.util.importer.IFieldMap;
import com.mmxlabs.models.util.importer.IImportContext;
import com.mmxlabs.models.util.importer.IImportContext.IDeferment;

/**
 * @since 2.0
 */
public class AssignmentImporter {

	public void importAssignments(final CSVReader reader, final IImportContext context) {
		IFieldMap row;
		try {
			while ((row = reader.readRow(true)) != null) {
				final String vesselName = row.get("vessels");
				final String assignedObjects = row.get("assignedobjects");
				final String spotIndexStr = row.get("spotindex");

				int spotIndexTmp = 0;
				try {
					spotIndexTmp = Integer.parseInt(spotIndexStr);
				} catch (final NumberFormatException nfe) {
					context.createProblem("Error parsing spot index", true, true, true);
				}
				final int spotIndex = spotIndexTmp;
				final String[] assignedObjectNames = assignedObjects.split(",");
				int index = 0;
				for (final String aon : assignedObjectNames) {
					final int seq = index++;
					context.doLater(new IDeferment() {
						@Override
						public void run(final IImportContext context) {
							// Loop over all named objects and find the first object which can be used.
							for (final NamedObject o : context.getNamedObjects(aon.trim())) {

								AssignableElement assignableElement = null;
								if (o instanceof VesselEvent) {
									assignableElement = (AssignableElement) o;
								}
								if (assignableElement == null) {
									if (o instanceof LoadSlot) {
										final LoadSlot loadSlot = (LoadSlot) o;
										if (loadSlot.isDESPurchase()) {
											assignableElement = loadSlot;
										}
									}
								}
								if (assignableElement == null) {
									if (o instanceof DischargeSlot) {
										final DischargeSlot dischargeSlot = (DischargeSlot) o;
										if (dischargeSlot.isFOBSale()) {
											assignableElement = dischargeSlot;
										}
									}
								}
								if (assignableElement == null) {
									if (o instanceof Cargo) {
										final Cargo cargo = (Cargo) o;
										assignableElement = cargo;
										for (final Slot s : cargo.getSlots()) {
											if (s instanceof LoadSlot) {
												final LoadSlot loadSlot = (LoadSlot) s;
												if (loadSlot.isDESPurchase()) {
													assignableElement = loadSlot;
													break;
												}
											} else if (s instanceof DischargeSlot) {
												final DischargeSlot dischargeSlot = (DischargeSlot) s;
												if (dischargeSlot.isFOBSale()) {
													assignableElement = dischargeSlot;
													break;
												}
											}
										}
									}
								}
								if (assignableElement != null) {
									assignableElement.setSequenceHint(seq);

									// Try named vessel first...
									final Vessel v = (Vessel) context.getNamedObject(vesselName.trim(), FleetPackage.eINSTANCE.getVessel());
									if (v != null) {
										assignableElement.setAssignment((Vessel) v);
									} else {
										// ...then generic set
										final NamedObject v2 = context.getNamedObject(vesselName.trim(), TypesPackage.eINSTANCE.getAVesselSet());
										if (v2 instanceof VesselClass) {
											assignableElement.setSpotIndex(spotIndex);
											assignableElement.setAssignment((VesselClass) v2);
										}
									}
									break;
								}
							}
						}

						@Override
						public int getStage() {
							return IImportContext.STAGE_MODIFY_SUBMODELS;
						}
					});
				}
			}
		} catch (final IOException e) {
		}
	}
}
