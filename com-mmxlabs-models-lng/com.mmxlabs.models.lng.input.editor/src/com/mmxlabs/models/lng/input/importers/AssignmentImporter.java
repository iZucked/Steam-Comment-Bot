/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.input.importers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.VesselEvent;
import com.mmxlabs.models.lng.input.ElementAssignment;
import com.mmxlabs.models.lng.input.InputFactory;
import com.mmxlabs.models.lng.input.InputModel;
import com.mmxlabs.models.lng.input.editor.utils.AssignmentEditorHelper;
import com.mmxlabs.models.lng.input.editor.utils.CollectedAssignment;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.UUIDObject;
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
			while ((row = reader.readRow()) != null) {
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
							final MMXRootObject root = context.getRootObject();
							final InputModel im = root.getSubModel(InputModel.class);
							if (im != null) {
								// Loop over all named objects and find the first object which can be used.
								for (final NamedObject o : context.getNamedObjects(aon.trim())) {
									if (o instanceof Cargo || o instanceof VesselEvent) {
										final ElementAssignment ea = InputFactory.eINSTANCE.createElementAssignment();
										ea.setAssignedObject((UUIDObject) o);
										ea.setSequence(seq);

										final NamedObject v = context.getNamedObject(vesselName.trim(), TypesPackage.eINSTANCE.getAVesselSet());
										if (v instanceof Vessel) {
											ea.setAssignment((AVesselSet) v);
										} else if (v instanceof VesselClass) {

											ea.setSpotIndex(spotIndex);
											ea.setAssignment((AVesselSet) v);
										}

										im.getElementAssignments().add(ea);
										break;
									}
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

	public List<Map<String, String>> exportAssignments(final InputModel im, final FleetModel fm) {
		final List<Map<String, String>> result = new ArrayList<Map<String, String>>();

		final List<CollectedAssignment> assignments = AssignmentEditorHelper.collectAssignments(im, fm);

		for (final CollectedAssignment ca : assignments) {
			final HashMap<String, String> row = new HashMap<String, String>();
			result.add(row);
			row.put("vessels", ca.getVesselOrClass().getName());
			final StringBuilder sb = new StringBuilder();

			boolean comma = false;
			for (final UUIDObject u : ca.getAssignedObjects()) {
				if (comma)
					sb.append(", ");
				else
					comma = true;
				sb.append(((NamedObject) u).getName());
			}

			row.put("assignedObjects", sb.toString());
			row.put("spotIndex", Integer.toString(ca.getSpotIndex()));
		}

		return result;
	}
}
