/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.importer;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.csv.CSVReader;
import com.mmxlabs.common.csv.IDeferment;
import com.mmxlabs.common.csv.IExportContext;
import com.mmxlabs.common.csv.IImportContext;
import com.mmxlabs.models.lng.port.ContingencyMatrix;
import com.mmxlabs.models.lng.port.ContingencyMatrixEntry;
import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.util.importer.IMMXImportContext;
import com.mmxlabs.models.util.importer.impl.NumberAttributeImporter;
import com.mmxlabs.models.util.importer.impl.SetReference;

public class ContingencyMatrixImporter {
	private static final String FROM = "from";

	public ContingencyMatrix importMatrix(final @NonNull CSVReader reader, final @NonNull IMMXImportContext context) {
		final ContingencyMatrix result = PortFactory.eINSTANCE.createContingencyMatrix();

		final NumberAttributeImporter nai = new NumberAttributeImporter(context.getDecimalSeparator());

		try {
			context.pushReader(reader);
			Map<String, String> row = null;
			while (null != (row = reader.readRow(true))) {
				String fromName = row.get(FROM);
				if (fromName == null || fromName.isEmpty()) {
					// Skip row if there is no from value.
					continue;
				}
				for (final Map.Entry<String, String> entry : row.entrySet()) {
					String toName = entry.getKey();
					if (FROM.equalsIgnoreCase(toName)) {
						// Skip FROM column
						continue;
					}
					if (toName == null || toName.isEmpty()) {
						// Skip column if there is no to value.
						continue;
					}
					if (fromName.equalsIgnoreCase(toName)) {
						// Skip FROM column
						continue;
					}
					final int duration;
					try {
						duration = nai.stringToInt(entry.getValue(), PortPackage.Literals.CONTINGENCY_MATRIX_ENTRY__DURATION);
					} catch (final ParseException nfe) {
						context.createProblem(String.format("Unable to parse duration %s for %s to %s", entry.getValue(), fromName, toName), true, true, true);
						continue;
					}
					final ContingencyMatrixEntry matrixEntry = PortFactory.eINSTANCE.createContingencyMatrixEntry();
					matrixEntry.setDuration(duration);

					context.doLater(new SetReference(matrixEntry, PortPackage.eINSTANCE.getContingencyMatrixEntry_FromPort(), fromName, context));
					context.doLater(new SetReference(matrixEntry, PortPackage.eINSTANCE.getContingencyMatrixEntry_ToPort(), toName, context));
					context.doLater(new IDeferment() {

						@Override
						public void run(IImportContext context) {
							if (matrixEntry.getFromPort() != null && matrixEntry.getToPort() != null) {
								result.getEntries().add(matrixEntry);
							}
						}

						@Override
						public int getStage() {
							return IMMXImportContext.STAGE_MODIFY_SUBMODELS;
						}
					});
				}
			}
		} catch (IOException e) {
			// Ignore errors ?
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
	public Collection<Map<String, String>> exportMatrix(final ContingencyMatrix matrix, IExportContext context) {
		final Map<String, Map<String, String>> rows = new TreeMap<>();
		final NumberAttributeImporter nai = new NumberAttributeImporter(context.getDecimalSeparator());

		for (final ContingencyMatrixEntry matrixEntry : matrix.getEntries()) {
			String fromName = matrixEntry.getFromPort().getName();
			Map<String, String> row = rows.get(fromName);
			if (row == null) {
				row = new TreeMap<>(new Comparator<String>() {
					@Override
					public int compare(final String o1, final String o2) {

						// Always sort name column first
						if (FROM.equals(o1) && FROM.equals(o2)) {
							return 0;
						}
						if (FROM.equals(o1)) {
							return -1;
						} else if (FROM.equals(o2)) {
							return 1;
						}

						return o1.compareTo(o2);
					}
				});
				row.put(FROM, fromName);
				rows.put(fromName, row);
				// Add in blank field for from -> from distance for sorting in export
				// Not strictly required otherwise
				row.put(fromName, "");

			}

			row.put(matrixEntry.getToPort().getName(), nai.intToString(matrixEntry.getDuration(), PortPackage.Literals.CONTINGENCY_MATRIX_ENTRY__DURATION));
		}

		return new ArrayList<>(rows.values());
	}
}
