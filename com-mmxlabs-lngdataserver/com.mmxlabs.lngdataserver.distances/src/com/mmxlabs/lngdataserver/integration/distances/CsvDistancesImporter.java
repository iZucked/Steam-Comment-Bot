/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.distances;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.csv.CSVReader;
import com.mmxlabs.models.util.importer.IMMXImportContext;
import com.mmxlabs.models.util.importer.impl.NumberAttributeImporter;

public class CsvDistancesImporter {

	private static final Logger LOG = LoggerFactory.getLogger(CsvDistancesImporter.class);
	private static final String FROM = "from";

	@SuppressWarnings("null")
	public static Map<String, Map<String, Integer>> importMatrix(CSVReader reader, final IMMXImportContext context) {

		final NumberAttributeImporter nai = new NumberAttributeImporter(context.getDecimalSeparator());

		Map<String, Map<String, Integer>> result = new HashMap<String, Map<String, Integer>>();

		try {
			context.pushReader(reader);
			Map<String, String> row = null;

			while (null != (row = reader.readRow(true))) {

				String fromName = row.get(FROM).toLowerCase();
				Map<String, Integer> rowMap = new HashMap<>();
				for (final Entry<String, String> entry : row.entrySet()) {
					try {
						if ("from".equals(entry.getKey())) {
							continue;
						}
						rowMap.put(entry.getKey(), nai.stringToInt(entry.getValue(), null));
					} catch (ParseException e) {
						if (entry.getValue().startsWith("e:") || entry.getValue().equals("")) {
							// known error... just return int max value
							rowMap.put(entry.getKey(), Integer.MAX_VALUE);
						} else {
							LOG.warn("Error parsing " + fromName + " > " + entry.getKey(), e);
						}
					}
				}

				result.put(fromName, rowMap);
			}
		} catch (IOException e) {
			LOG.warn(e.getMessage());
		} finally {
			context.popReader();
			try {
				reader.close();
			} catch (IOException e) {
				LOG.warn(e.getMessage());
			}
		}

		return result;
	}

}
