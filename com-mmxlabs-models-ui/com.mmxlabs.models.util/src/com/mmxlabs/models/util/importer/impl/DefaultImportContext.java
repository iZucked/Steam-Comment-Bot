/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.util.importer.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.Equality;
import com.mmxlabs.common.csv.CSVReader;
import com.mmxlabs.common.csv.FileCSVReader;
import com.mmxlabs.common.csv.IDeferment;
import com.mmxlabs.common.csv.IImportProblem;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.OtherNamesObject;
import com.mmxlabs.models.util.emfpath.EMFUtils;
import com.mmxlabs.models.util.importer.IMMXImportContext;

public class DefaultImportContext implements IMMXImportContext {
	private static final Logger log = LoggerFactory.getLogger(DefaultImportContext.class);

	private final LinkedList<IDeferment> deferments = new LinkedList<>();
	private final LinkedList<IDeferment> reschedule = new LinkedList<>();
	private boolean running = false;

	private final HashMap<String, List<NamedObject>> namedObjects = new HashMap<>();

	private final LinkedHashSet<IImportProblem> problems = new LinkedHashSet<>();

	private MMXRootObject rootObject;
	private final char decimalSeparator;

	public DefaultImportContext(final char decimalSeparator) {
		this.decimalSeparator = decimalSeparator;
	}

	@Override
	public NamedObject getNamedObject(@NonNull final String name, final EClass preferredType) {
		final List<NamedObject> matches = namedObjects.get(name);
		if (matches == null) {
			log.warn("No objects with name " + name + " have been imported");
			final String lowerName = name.toLowerCase();
			if (!lowerName.equals(name)) {
				return getNamedObject(lowerName, preferredType);
			}
			return null;
		}
		int match = Integer.MAX_VALUE;
		NamedObject best = null;
		for (final NamedObject o : matches) {
			if (preferredType.isSuperTypeOf(o.eClass())) {
				final int thisMatch = EMFUtils.getMinimumGenerations(o.eClass(), preferredType);
				if (thisMatch <= match) {
					match = thisMatch;
					best = o;
				}
			}
		}

		if (best == null) {
			final ArrayList<String> typeNames = new ArrayList<String>();
			for (final NamedObject o : matches) {
				typeNames.add(o.eClass().getName());
			}
			log.warn("Could not locate instance of " + preferredType.getName() + " with name " + name + ". " + "Objects with that name are of types " + typeNames);
		}

		return best;
	}

	@Override
	public Collection<NamedObject> getNamedObjects(final String name) {
		final List<NamedObject> c = namedObjects.get(name);
		if (c == null) {
			return Collections.emptySet();
		}
		return Collections.unmodifiableCollection(c);
	}

	@Override
	public void registerNamedObject(final NamedObject object) {
		final String typedName = EncoderUtil.getTypedName(object);
		registerObjectWithName(object, typedName);

		// For backward compatibility - register the untyped name if different.
		if (!Equality.isEqual(typedName, object.getName())) {
			registerObjectWithName(object, object.getName());
		}

		if (object instanceof OtherNamesObject) {
			final OtherNamesObject otherNamesObject = (OtherNamesObject) object;
			for (final String otherName : otherNamesObject.getOtherNames()) {
				registerObjectWithName(object, otherName);
			}
		}
	}

	private void registerObjectWithName(final NamedObject object, final String name) {
		if (name == null) {
			return;
		}
		List<NamedObject> others = namedObjects.get(name);
		if (others == null) {
			others = new LinkedList<NamedObject>();
			namedObjects.put(name, others);
		}
		others.add(object);

		if (name.equals(name.toLowerCase()) == false) {
			registerObjectWithName(object, name.toLowerCase());
		}
	}

	@Override
	public void doLater(final IDeferment deferment) {
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
				public int compare(final IDeferment o1, final IDeferment o2) {
					return ((Integer) o1.getStage()).compareTo(o2.getStage());
				}
			});

			while (iterator.hasNext()) {
				if (reschedule.isEmpty() == false) {
					break;
				}
				final IDeferment deferment = iterator.next();
				iterator.remove();
				deferment.run(this);
			}
		}
		running = false;
	}

	private final Stack<CSVReader> readerStack = new Stack<CSVReader>();

	@Override
	public void pushReader(final CSVReader reader) {
		readerStack.push(reader);
	}

	@Override
	public CSVReader popReader() {
		return readerStack.pop();
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

		public DefaultImportProblem(final String filename, final Integer lineNumber, final String field, final String message) {
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

		/*
		 * (non-Javadoc)
		 * 
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

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(final Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			final DefaultImportProblem other = (DefaultImportProblem) obj;
			if (!getOuterType().equals(other.getOuterType())) {
				return false;
			}
			if (field == null) {
				if (other.field != null) {
					return false;
				}
			} else if (!field.equals(other.field)) {
				return false;
			}
			if (filename == null) {
				if (other.filename != null) {
					return false;
				}
			} else if (!filename.equals(other.filename)) {
				return false;
			}
			if (lineNumber == null) {
				if (other.lineNumber != null) {
					return false;
				}
			} else if (!lineNumber.equals(other.lineNumber)) {
				return false;
			}
			if (message == null) {
				if (other.message != null) {
					return false;
				}
			} else if (!message.equals(other.message)) {
				return false;
			}
			return true;
		}

		private DefaultImportContext getOuterType() {
			return DefaultImportContext.this;
		}
	}

	@Override
	public IImportProblem createProblem(final String string, final boolean trackFile, final boolean trackLine, final boolean trackField) {
		if (readerStack.isEmpty()) {
			return new DefaultImportProblem(null, null, null, string);
		} else {
			final CSVReader reader = readerStack.peek();
			final Integer line = trackLine ? reader.getLineNumber() : null;
			final String lowerField = reader.getLastField().toLowerCase();
			final String upperField = reader.getCasedColumnName(lowerField);
			final String field = trackField ? (upperField == null ? lowerField : upperField) : null;
			final String file;
			if (reader instanceof FileCSVReader) {
				file = trackFile ? ((FileCSVReader) readerStack.peek()).getFileName() : null;
			} else {
				file = null;
			}
			return new DefaultImportProblem(file, line, field, string);
		}
	}

	@Override
	public List<IImportProblem> getProblems() {
		return new ArrayList<IImportProblem>(problems);
	}

	@Override
	public MMXRootObject getRootObject() {
		return rootObject;
	}

	public void setRootObject(final MMXRootObject root) {
		this.rootObject = root;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.models.util.importer.IImportContext#peekReader()
	 */
	@Override
	public CSVReader peekReader() {
		return readerStack.peek();
	}

	/**
	 */
	public void registerNamedObjectsFromSubModels() {
		// first set up all existing named objects
		final TreeIterator<EObject> allObjects = rootObject.eAllContents();

		while (allObjects.hasNext()) {
			final EObject o = allObjects.next();
			if (o instanceof NamedObject) {
				final NamedObject namedObject = (NamedObject) o;
				registerNamedObject(namedObject);
			}
		}
	}

	@Override
	public char getDecimalSeparator() {
		return decimalSeparator;
	}
}
