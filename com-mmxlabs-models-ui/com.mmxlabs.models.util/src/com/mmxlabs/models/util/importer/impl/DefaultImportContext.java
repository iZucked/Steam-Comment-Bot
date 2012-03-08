/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.util.importer.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.eclipse.emf.ecore.EClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.util.emfpath.EMFUtils;
import com.mmxlabs.models.util.importer.CSVReader;
import com.mmxlabs.models.util.importer.IImportContext;

public class DefaultImportContext implements IImportContext {
	private LinkedList<IDeferment> deferments = new LinkedList<IDeferment>();
	private LinkedList<IDeferment> reschedule = new LinkedList<IDeferment>();
	private boolean running = false;
	
	private HashMap<String, List<NamedObject>> namedObjects = new HashMap<String, List<NamedObject>>();
	private static final Logger log = LoggerFactory.getLogger(DefaultImportContext.class);
	
	@Override
	public NamedObject getNamedObject(String name, EClass preferredType) {
		final List<NamedObject> matches = namedObjects.get(name);
		if (matches == null) return null;
		int match = Integer.MAX_VALUE;
		NamedObject best = null;
		for (final NamedObject o : matches) {
			if (preferredType.isSuperTypeOf(o.eClass())) {
				int thisMatch = EMFUtils.getMinimumGenerations(o.eClass(), preferredType);
				if (thisMatch < match) {
					match = thisMatch;
					best = o;
				}
			}
		}
		return best;
	}

	@Override
	public void registerNamedObject(NamedObject object) {
		final String name = object.getName();
		List<NamedObject> others = namedObjects.get(name);
		if (others == null) {
			others = new LinkedList<NamedObject>();
			namedObjects.put(name, others);
		}
		others.add(object);
	}

	@Override
	public void doLater(IDeferment deferment) {
		if (running) {
			reschedule.add(deferment);
		} else {
			deferments.add(deferment);
		}
	}

	public void run() {
		running = true;
		while (deferments.isEmpty() == false || reschedule.isEmpty() == false) {
			final Iterator<IDeferment> iterator = deferments.iterator();
			deferments.addAll(reschedule);
			reschedule.clear();
			
			Collections.sort(deferments, new Comparator<IDeferment>() {
				@Override
				public int compare(IDeferment o1, IDeferment o2) {
					return ((Integer) o1.getStage()).compareTo(o2.getStage());
				}
			});
			
			while (iterator.hasNext()) {
				if (reschedule.isEmpty() == false) break;
				final IDeferment deferment = iterator.next();
				iterator.remove();
				deferment.run(this);
			}
		}
		running = false;
	}

	private Stack<CSVReader> readerStack = new Stack<CSVReader>();
	@Override
	public void pushReader(final CSVReader reader) {
		readerStack.push(reader);
	}
	@Override
	public void popReader() {
		readerStack.pop();
	}
	
	@Override
	public void addProblem(final IImportProblem problem) {
		problems.add(problem);
	}
	
	@Override
	public IImportProblem createProblem(final String string, boolean trackFile, boolean trackLine, boolean trackField) {
		String tracking = "";
		if (trackFile || trackField) {
			final CSVReader reader = readerStack.peek();
			tracking = 
					(trackFile ? reader.getFileName() + ":" + reader.getLineNumber() : "" ) + 
					(trackField ? ", field " + reader.getLastField() : "");
		}
		log.warn(string + " " + tracking);
		
		final Integer line = trackLine ? readerStack.peek().getLineNumber() : null;
		final String field = trackField ? readerStack.peek().getCasedColumnName(readerStack.peek().getLastField().toLowerCase()) : null;
		final String file = trackFile ? readerStack.peek().getFileName() : null;
		
		return (
				new IImportProblem() {
					@Override
					public String getProblemDescription() {
						return string;
					}
					
					@Override
					public Integer getLineNumber() {
						return line;
					}
					
					@Override
					public String getFilename() {
						return file;
					}
					
					@Override
					public String getField() {
						return field;
					}
				});
	}

	private List<IImportProblem> problems = new LinkedList<IImportContext.IImportProblem>();
	
	@Override
	public List<IImportProblem> getProblems() {
		return problems;
	}
}
