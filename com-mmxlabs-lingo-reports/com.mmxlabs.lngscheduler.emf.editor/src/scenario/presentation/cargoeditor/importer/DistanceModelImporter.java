/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.importer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;

import scenario.port.DistanceLine;
import scenario.port.DistanceModel;
import scenario.port.PortFactory;
import scenario.port.PortPackage;

/**
 * Special case importer for distance model.
 * 
 * @author Tom Hinton
 * 
 */
public class DistanceModelImporter extends EObjectImporter {
	@Override
	public Collection<EObject> importObjects(final CSVReader reader,
			final Collection<DeferredReference> deferredReferences,
			final NamedObjectRegistry registry) {
		final DistanceModel result = PortFactory.eINSTANCE
				.createDistanceModel();

		Map<String, String> row = null;
		final ArrayList<DistanceLine> lines = new ArrayList<DistanceLine>();
		try {

			while ((row = reader.readRow()) != null) {
				lines.clear();
				String fromPort = null;
				for (final Map.Entry<String, String> column : row.entrySet()) {
					try {
						final int x = Integer.parseInt(column.getValue());
						final DistanceLine dl = PortFactory.eINSTANCE
								.createDistanceLine();
						dl.setDistance(x);
						deferredReferences.add(new DeferredReference(dl,
								PortPackage.eINSTANCE.getDistanceLine_ToPort(),
								reader.getCasedColumnName(column.getKey())));
						lines.add(dl);
					} catch (NumberFormatException nfe) {
						fromPort = column.getValue();
					}
				}
				for (final DistanceLine dl : lines) {
					deferredReferences.add(new DeferredReference(dl,
							PortPackage.eINSTANCE.getDistanceLine_FromPort(),
							fromPort));
				}
				result.getDistances().addAll(lines);
			}
		} catch (IOException e) {
		}

		return Collections.singletonList((EObject) result);
	}

	@Override
	public EObject importObject(final Map<String, String> fields,
			final Collection<DeferredReference> deferredReferences,
			final NamedObjectRegistry registry) {
		throw new RuntimeException(
				"Distance model importer used incorrectly; you can only import a whole file at a time.");
	}
}
