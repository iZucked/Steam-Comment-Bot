package com.mmxlabs.models.util.importer.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.util.emfpath.EMFUtils;
import com.mmxlabs.models.util.importer.CSVReader;
import com.mmxlabs.models.util.importer.IImportContext;

public class DefaultImportContext implements IImportContext {
	private LinkedList<IDeferment> deferments = new LinkedList<IDeferment>();
	private LinkedList<IDeferment> reschedule = new LinkedList<IDeferment>();
	private boolean running = false;
	
	private HashMap<String, List<NamedObject>> namedObjects = new HashMap<String, List<NamedObject>>();
	
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
		while (deferments.isEmpty() == false && reschedule.isEmpty() == false) {
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

	@Override
	public void addProblem(String column, String value, String message) {
		
	}
	
	private Stack<CSVReader> readerStack = new Stack<CSVReader>();
	public void pushReader(final CSVReader reader) {
		readerStack.push(reader);
	}
	public void popReader() {
		readerStack.pop();
	}
}
