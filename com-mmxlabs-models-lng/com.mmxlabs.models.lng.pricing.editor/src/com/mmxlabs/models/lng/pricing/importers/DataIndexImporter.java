/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.importers;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.IndexPoint;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.util.importer.CSVReader;
import com.mmxlabs.models.util.importer.IClassImporter;
import com.mmxlabs.models.util.importer.IImportContext;


/**
 * Custom import logic for loading a data index.
 * 
 * @author hinton
 *
 */
public class DataIndexImporter implements IClassImporter {
	boolean parseAsInt = false;
	final DateFormat shortDate = new SimpleDateFormat("yyyy-MM-dd");
	/**
	 * @return the parseAsInt
	 */
	public boolean isParseAsInt() {
		return parseAsInt;
	}
	
	/**
	 * @param parseAsInt the parseAsInt to set
	 */
	public void setParseAsInt(boolean parseAsInt) {
		this.parseAsInt = parseAsInt;
	}

	@Override
	public Collection<EObject> importObjects(EClass targetClass, CSVReader reader, IImportContext context) {
		final List<EObject> result = new LinkedList<EObject>();
		
		Map<String, String> row;
		try {
			context.pushReader(reader);
			while (null != (row = reader.readRow())) {
				result.addAll(importObject(targetClass, row, context));
			}
		} catch (IOException e) {
			context.addProblem(context.createProblem("IO Error " + e.getMessage(), true, true, false));
		} finally {
			context.popReader();
		}
 		
		return result;
	}

	@Override
	public Collection<EObject> importObject(EClass targetClass, Map<String, String> row, IImportContext context) {
		final DataIndex<Number> result = PricingFactory.eINSTANCE.createDataIndex();
		
		if (row.containsKey("name")) {
			result.setName(row.get("name"));
		} else {
			context.addProblem(context.createProblem("Index name is missing", true, true, true));
		}
		
		for (final String s : row.keySet()) {
			try {
				final Date date = shortDate.parse(s);
				if (row.get(s).isEmpty())
					continue;
				try {
					final Number n = parseAsInt ? Integer.parseInt(row.get(s)) : Double.parseDouble(row.get(s));

					final IndexPoint<Number> point = PricingFactory.eINSTANCE.createIndexPoint();
					point.setDate(date);
					point.setValue(n);
					result.getPoints().add(point);
				} catch (final NumberFormatException nfe) {
					context.addProblem(context.createProblem("The value " + row.get(s) + " is not a number", true, true, true));
				}
			} catch (ParseException ex) {
				if (s.equals("name") == false) {
					context.addProblem(context.createProblem("The field " + s + " is not a date", true, false, true));
				}
			}
		}
		
		context.registerNamedObject(result);
		
		return Collections.singleton((EObject) result);
	}

	@Override
	public Collection<Map<String, String>> exportObjects(Collection<? extends EObject> objects) {
		// TODO Auto-generated method stub
		
		return null;
	}
}
