/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.importer;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;

import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteLine;
import com.mmxlabs.models.util.importer.CSVReader;
import com.mmxlabs.models.util.importer.IImportContext;
import com.mmxlabs.models.util.importer.impl.SetReference;

/**
 * A special-case importer for distance matrices.
 * 
 * @author hinton
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
						context.doLater(new SetReference(line, PortPackage.eINSTANCE.getRouteLine_To(), reader.getCasedColumnName(entry.getKey()), context));
						lines.add(line);
					} catch (final NumberFormatException nfe) {
						if (entry.getValue().isEmpty() == false) {
							fromName = entry.getValue();
						}
					}
				}

				if (fromName != null) {
					for (final RouteLine line : lines) {
						context.doLater(new SetReference(line, PortPackage.eINSTANCE.getRouteLine_From(), fromName, context));
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
}
