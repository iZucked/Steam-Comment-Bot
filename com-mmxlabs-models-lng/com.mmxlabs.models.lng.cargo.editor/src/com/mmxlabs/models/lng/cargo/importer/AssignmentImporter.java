/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.importer;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.mmxlabs.common.csv.CSVReader;
import com.mmxlabs.common.csv.IDeferment;
import com.mmxlabs.common.csv.IExportContext;
import com.mmxlabs.common.csv.IFieldMap;
import com.mmxlabs.common.csv.IImportContext;
import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.util.AssignmentEditorHelper;
import com.mmxlabs.models.lng.cargo.util.CollectedAssignment;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.util.ModelDistanceProvider;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.util.importer.IMMXImportContext;

/**
 */
public class AssignmentImporter {

	public void importAssignments(final CSVReader reader, final IImportContext context) {
		IFieldMap row;
		try {
			while ((row = reader.readRow(true)) != null) {
				final String vesselAssignment = row.get("vesselassignment");
				final String assignedObjects = row.get("assignedobjects");
				final String spotIndexStr = row.get("spotindex");
				final String charterIndexStr = row.get("charterindex");

				Integer spotIndexTmp = null;
				if (spotIndexStr != null && !spotIndexStr.isEmpty()) {
					try {
						spotIndexTmp = Integer.parseInt(spotIndexStr);
					} catch (final NumberFormatException nfe) {
						context.createProblem("Error parsing spot index", true, true, true);
					}
				}
				final Integer spotIndex = spotIndexTmp;

				Integer charterIndexTmp = null;
				if (charterIndexStr != null && !charterIndexStr.isEmpty()) {
					try {
						charterIndexTmp = Integer.parseInt(charterIndexStr);
					} catch (final NumberFormatException nfe) {
						context.createProblem("Error parsing charter index", true, true, true);
					}
				}
				final Integer charterIndex = charterIndexTmp;
				final String[] assignedObjectNames = assignedObjects.split(",");
				int index = 1;
				for (final String aon : assignedObjectNames) {
					final int seq = index++;
					context.doLater(new IDeferment() {
						@Override
						public void run(final IImportContext importContext) {

							final IMMXImportContext context = (IMMXImportContext) importContext;
							// Loop over all named objects and find the first object which can be used.
							for (final NamedObject o : context.getNamedObjects(aon.trim())) {

								AssignableElement assignableElement = null;
								if (o instanceof VesselEvent) {
									assignableElement = (AssignableElement) o;
								} else if (o instanceof LoadSlot) {
									final LoadSlot loadSlot = (LoadSlot) o;
									if (!loadSlot.isDESPurchase()) {
										assignableElement = loadSlot.getCargo();
									}
								} else if (o instanceof AssignableElement) {
									assignableElement = (AssignableElement) o;
								}

								if (vesselAssignment != null && !vesselAssignment.isEmpty()) {
									if (assignableElement != null) {
										// new style
										if (spotIndex != null) {
											final CharterInMarket charterInMarket = (CharterInMarket) context.getNamedObject(vesselAssignment.trim(), SpotMarketsPackage.Literals.CHARTER_IN_MARKET);
											if (charterInMarket != null) {
												assignableElement.setVesselAssignmentType(charterInMarket);
												assignableElement.setSpotIndex(spotIndex.intValue());
											}
											// Do not set round trip caroges
											if (spotIndex.intValue() != -1) {
												assignableElement.setSequenceHint(seq);
											}
										} else {
											final Vessel v = (Vessel) context.getNamedObject(vesselAssignment.trim(), FleetPackage.Literals.VESSEL);
											if (v != null) {
												final VesselCharter availability = AssignmentEditorHelper.findVesselCharter(v, assignableElement,
														((LNGScenarioModel) context.getRootObject()).getCargoModel().getVesselCharters(), charterIndex);

												assignableElement.setVesselAssignmentType(availability);
											}
											assignableElement.setSequenceHint(seq);
										}
									}
								}
							}
						}

						@Override
						public int getStage() {
							return IMMXImportContext.STAGE_MODIFY_SUBMODELS;
						}
					});
				}
			}
		} catch (final IOException e) {
		}
	}

	public Collection<Map<String, String>> exportObjects(final CargoModel cargoModel, final PortModel portModel, final SpotMarketsModel spotMarketsModel, ModelDistanceProvider modelDistanceProvider,
			final IExportContext context) {
		final List<Map<String, String>> result = new LinkedList<>();
		final List<CollectedAssignment> collectAssignments = AssignmentEditorHelper.collectAssignments(cargoModel, portModel, spotMarketsModel, modelDistanceProvider);
		for (final CollectedAssignment collectAssignment : collectAssignments) {
			final Map<String, String> row = new HashMap<>();
			if (collectAssignment.getVesselCharter() != null) {
				result.add(row);
				row.put("vesselassignment", collectAssignment.getVesselCharter().getVessel().getName());
				row.put("charterindex", Integer.toString(collectAssignment.getVesselCharter().getCharterNumber()));
			} else if (collectAssignment.getCharterInMarket() != null) {
				result.add(row);

				row.put("vesselassignment", collectAssignment.getCharterInMarket().getName());
				row.put("spotindex", Integer.toString(collectAssignment.getSpotIndex()));
			}
			final StringBuilder sb = new StringBuilder();
			boolean first = true;
			for (final AssignableElement as : collectAssignment.getAssignedObjects()) {
				if (first) {
					first = false;
				} else {
					sb.append(",");
				}
				if (as instanceof Cargo) {
					sb.append(((Cargo) as).getLoadName());
				} else if (as instanceof NamedObject) {
					sb.append(((NamedObject) as).getName());
				}

			}
			row.put("assignedobjects", sb.toString());

		}
		for (final LoadSlot loadSlot : cargoModel.getLoadSlots()) {
			// N/A
		}
		for (final DischargeSlot loadSlot : cargoModel.getDischargeSlots()) {
			// N/A
		}
		return result;
	}
}
