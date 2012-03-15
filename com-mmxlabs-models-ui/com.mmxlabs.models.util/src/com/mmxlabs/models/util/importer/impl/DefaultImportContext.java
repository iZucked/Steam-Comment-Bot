/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.util.importer.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.eclipse.emf.ecore.EClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.mmxcore.MMXRootObject;
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
				if (thisMatch <= match) {
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
	
	private class DefaultImportProblem implements IImportProblem {
		private final String filename;
		private final Integer lineNumber;
		private final String field;
		private final String message;
		
		
		public DefaultImportProblem(String filename, Integer lineNumber, String field, String message) {
			super();
			this.filename = filename;
			this.lineNumber = lineNumber;
			this.field = field;
			this.message = message;
		}

		@Override
		public String getFilename() {
			return filename;
		}


		@Override
		public Integer getLineNumber() {
			return lineNumber;
		}

		@Override
		public String getField() {
			return field;
		}

		@Override
		public String getProblemDescription() {
			return message;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((field == null) ? 0 : field.hashCode());
			result = prime * result + ((filename == null) ? 0 : filename.hashCode());
			result = prime * result + ((lineNumber == null) ? 0 : lineNumber.hashCode());
			result = prime * result + ((message == null) ? 0 : message.hashCode());
			return result;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			DefaultImportProblem other = (DefaultImportProblem) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (field == null) {
				if (other.field != null)
					return false;
			} else if (!field.equals(other.field))
				return false;
			if (filename == null) {
				if (other.filename != null)
					return false;
			} else if (!filename.equals(other.filename))
				return false;
			if (lineNumber == null) {
				if (other.lineNumber != null)
					return false;
			} else if (!lineNumber.equals(other.lineNumber))
				return false;
			if (message == null) {
				if (other.message != null)
					return false;
			} else if (!message.equals(other.message))
				return false;
			return true;
		}

		private DefaultImportContext getOuterType() {
			return DefaultImportContext.this;
		}
	}
	
	@Override
	public IImportProblem createProblem(final String string, boolean trackFile, boolean trackLine, boolean trackField) {
		final CSVReader reader = readerStack.peek();
		final Integer line = trackLine ? reader.getLineNumber() : null;
		final String lowerField = reader.getLastField().toLowerCase();
		final String upperField = reader.getCasedColumnName(lowerField);
		final String field = trackField ? (upperField == null ? lowerField : upperField) : null;
		final String file = trackFile ? readerStack.peek().getFileName() : null;
		
		return new DefaultImportProblem(file, line, field, string);
	}

	private LinkedHashSet<IImportProblem> problems = new LinkedHashSet<IImportContext.IImportProblem>();
	private MMXRootObject rootObject;
	
	@Override
	public List<IImportProblem> getProblems() {
		return new ArrayList<IImportProblem>(problems);
	}

	@Override
	public MMXRootObject getRootObject() {
		return rootObject;
	}

	public void setRootObject(MMXRootObject root) {
		this.rootObject = root;
	}
}
