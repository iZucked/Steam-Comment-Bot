/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.importer.importers;

/**
 * A holder for warnings that happen during import
 * 
 * @author hinton
 * 
 */
public class ImportWarning {
	/**
	 * The warning message
	 */
	public final String warning;
	/**
	 * The file from which the warning arises
	 */
	public final String filename;
	/**
	 * If positive, the number of a line with an error
	 */
	public final int lineNumber;
	/**
	 * If set, the name of a column with an error.
	 */
	public final String columnName;

	public ImportWarning(final String warning, final String filename, final int lineNumber, final String columnName) {
		super();
		this.warning = warning == null ? "" : warning;
		this.filename = filename == null ? "" : filename;
		this.lineNumber = lineNumber;
		this.columnName = columnName == null ? "" : columnName;
	}

	@Override
	public String toString() {
		return "In " + filename + (lineNumber > 0 ? (":" + lineNumber) : "") + (columnName.isEmpty() ? "" : ("/" + columnName)) + " - " + warning;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((columnName == null) ? 0 : columnName.hashCode());
		result = (prime * result) + ((filename == null) ? 0 : filename.hashCode());
		result = (prime * result) + lineNumber;
		result = (prime * result) + ((warning == null) ? 0 : warning.hashCode());
		return result;
	}

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
		final ImportWarning other = (ImportWarning) obj;
		if (columnName == null) {
			if (other.columnName != null) {
				return false;
			}
		} else if (!columnName.equals(other.columnName)) {
			return false;
		}
		if (filename == null) {
			if (other.filename != null) {
				return false;
			}
		} else if (!filename.equals(other.filename)) {
			return false;
		}
		if (lineNumber != other.lineNumber) {
			return false;
		}
		if (warning == null) {
			if (other.warning != null) {
				return false;
			}
		} else if (!warning.equals(other.warning)) {
			return false;
		}
		return true;
	}
}
