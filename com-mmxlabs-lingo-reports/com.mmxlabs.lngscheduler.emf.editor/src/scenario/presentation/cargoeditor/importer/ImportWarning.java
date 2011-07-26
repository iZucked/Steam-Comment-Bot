/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 */
package scenario.presentation.cargoeditor.importer;

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

	public ImportWarning(String warning, String filename, int lineNumber,
			String columnName) {
		super();
		this.warning = warning;
		this.filename = filename;
		this.lineNumber = lineNumber;
		this.columnName = columnName;
	}

	@Override
	public String toString() {
		return "In " + filename + (lineNumber > 0 ? (":" + lineNumber) : "")
				+ (columnName.isEmpty() ? "" : ("/" + columnName)) + " - "
				+ warning;
	}
}
