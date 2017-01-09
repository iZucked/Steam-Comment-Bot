/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.excelcomparisonsheets;

import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class POIUtils {

	public abstract class ExcelCell <T> {
		int cellType;
		T value;
		
		public ExcelCell(T value) {
			this.value = value;
		}
		
		public abstract void setValue(Cell cell);
	}
	
	public class NumericExcelCell extends ExcelCell<Double> {
		public NumericExcelCell(Double value) {
			super(value);
			cellType = Cell.CELL_TYPE_NUMERIC;
		}
		
		public void setValue(Cell cell) {
			cell.setCellValue(value);
		}

	}
	
	public class StringExcelCell extends ExcelCell<String> {
		public StringExcelCell(String value) {
			super(value);
			cellType = Cell.CELL_TYPE_STRING;
		}
		
		public void setValue(Cell cell) {
			cell.setCellValue(value);
		}
	}
	
	public class FormulaExcelCell extends ExcelCell<String> {
		public FormulaExcelCell(String value) {
			super(value);
			cellType = Cell.CELL_TYPE_FORMULA;
		}
		
		public void setValue(Cell cell) {
			cell.setCellFormula(value);
		}

	}

	public int writeToSheet(final Sheet sheet, int noRows, final Workbook workbook, List<ExcelCell> rowData) {
		addGenericRowToSheet(sheet, rowData, noRows);
		noRows++;
		return noRows;
	}
	
	public int writeToSheet(final String[] text, final Sheet sheet, int rows, final Workbook workbook) {
		addTextRowToSheet(sheet, text, rows);
		rows++;
		return rows;
	}

	private void addGenericRowToSheet(final Sheet sheet, final List<ExcelCell> cells, final int rowNo) {
		final Row row = sheet.createRow(rowNo - 1);
		for (int i = 0; i < cells.size(); i++) {
			final Cell cell = row.createCell(i);
			cell.setCellType(cells.get(i).cellType);
			cells.get(i).setValue(cell);
		}
	}

	public void addTextRowToSheet(final Sheet sheet, final String[] text, final int rowNo) {
		final Row row = sheet.createRow(rowNo - 1);
		for (int i = 0; i < text.length; i++) {
			final Cell cell = row.createCell(i);
			cell.setCellType(Cell.CELL_TYPE_STRING);
			cell.setCellValue(text[i]);
		}
	}

}
