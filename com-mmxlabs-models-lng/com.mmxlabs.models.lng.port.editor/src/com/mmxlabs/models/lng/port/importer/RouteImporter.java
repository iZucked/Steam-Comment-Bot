/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.importer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteLine;
import com.mmxlabs.models.util.importer.CSVReader;
import com.mmxlabs.models.util.importer.IImportContext;
import com.mmxlabs.models.util.importer.IImportContext.IDeferment;
import com.mmxlabs.models.util.importer.impl.SetReference;

/**
 * A special-case importer for distance matrices.
 * 
 * @author hinton
 * @since 2.0
 * 
 */
public class RouteImporter {
	public RouteImporter() {

	}

	public Route importRoute(final CSVReader reader, final IImportContext context) {
		final Route result = PortFactory.eINSTANCE.createRoute();

		try {
			context.pushReader(reader);
			Map<String, String> row = null;
			final LinkedList<RouteLine> lines = new LinkedList<RouteLine>();
			while (null != (row = reader.readRow())) {
				String fromName = null;
				for (final Map.Entry<String, String> entry : row.entrySet()) {
					if (entry.getValue().isEmpty()) {
						continue;
					}
					try {
						final int distance = Integer.parseInt(entry.getValue());
						final RouteLine line = PortFactory.eINSTANCE.createRouteLine();
						line.setDistance(distance);
						row.get(entry.getKey());
						context.doLater(new SetReference(line, PortPackage.eINSTANCE.getRouteLine_To(), reader.getCasedColumnName(entry.getKey()), context));
						lines.add(line);
					} catch (final NumberFormatException nfe) {
						try {
							final double distance = Double.parseDouble(entry.getValue());
							final RouteLine line = PortFactory.eINSTANCE.createRouteLine();
							line.setDistance((int) distance);
							row.get(entry.getKey());
							context.doLater(new SetReference(line, PortPackage.eINSTANCE.getRouteLine_To(), reader.getCasedColumnName(entry.getKey()), context));
							lines.add(line);
						} catch (final NumberFormatException nfe2) {
							if (entry.getValue().isEmpty() == false) {
								fromName = entry.getValue();
							}
						}
					}
				}

				if (fromName != null) {
					for (final RouteLine line : lines) {
						context.doLater(new SetReference(line, PortPackage.eINSTANCE.getRouteLine_From(), fromName, context));
						context.doLater(new IDeferment() {
							
							@Override
							public void run(IImportContext context) {
								if (line.getTo() == null || line.getFrom() == null) {
									// delete line
									final EObject eContainer = line.eContainer();
									final EStructuralFeature eContainingFeature = line.eContainingFeature();
									((List) eContainer.eGet(eContainingFeature)).remove(line);
								}
							}
							
							@Override
							public int getStage() {
								return IImportContext.STAGE_MODIFY_SUBMODELS;
							}
						});
						result.getLines().add(line);
					}
				} else {
					// TODO warn
				}
				
				lines.clear();
			}
		} catch (final IOException ex) {

		} finally {
			context.popReader();
			try {
				reader.close();
			} catch (final IOException e) {
			}
		}

		return result;
	}

	/**
	 * @param r
	 * @return
	 */
	public Collection<Map<String, String>> exportRoute(final Route r) {
		final Map<String, Map<String, String>> rows = new TreeMap<String, Map<String, String>>();
		
		for (final RouteLine line : r.getLines()) {
			Map<String, String> row = rows.get(line.getFrom().getName());
			if (row == null) {
				row = new TreeMap<String, String>(new Comparator<String>() {
					@Override
					public int compare(final String o1, final String o2) {

						// Always sort name column first
						if ("from".equals(o1) && "from".equals(o2)) {
							return 0;
						}
						if ("from".equals(o1)) {
							return -1;
						} else if ("from".equals(o2)) {
							return 1;
						}

						return o1.compareTo(o2);
					}
				});
				row.put("from", line.getFrom().getName());
				rows.put(line.getFrom().getName(), row);
				// Add in blank field for from -> from distance for sorting in export
				// Not strictly required otherwise
				row.put(line.getFrom().getName(), "");

			}
			row.put(line.getTo().getName(), line.getDistance() + "");
		}
		
		final ArrayList<Map<String, String>> result = new ArrayList<Map<String, String>>(rows.values());
		return result;
	}
}
