package com.mmxlabs.models.lng.scenario.importWizards.paperdeals.fromexcel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExcelReader {
	
	private final XSSFWorkbook workbook;
	private final XSSFSheet workSheet;
	private final FormulaEvaluator evaluator;
	private final DataFormatter formatter;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ExcelReader.class);
	
	public ExcelReader(InputStream is, String sheetName) throws IOException {
		this.workbook = getWorkbook(is);
		this.workSheet = getSheet(workbook, sheetName);
		if(workSheet == null) {
			throw new IOException(String.format
					("Worksheet \"%s\" could not be found in the workbook!", sheetName));
		}
		
		this.evaluator = workbook.getCreationHelper().createFormulaEvaluator();
		if(evaluator == null) {
			throw new IOException("Could not create an evaluator for this workbook!");
		}
		
		this.formatter = new DataFormatter();
		
		// Does this need fixing?
		//formatter.setUseCachedValuesForFormulaCells(true);
	} 
	
	public static List<String> getSheetNames(String workbookFileName) throws FileNotFoundException, IOException{
		List<String> sheetNames = new ArrayList<>();
		
		FileInputStream fileIn = new FileInputStream(workbookFileName);
		XSSFWorkbook workbook = new XSSFWorkbook(fileIn);
		
		for(int i = 0; i < workbook.getNumberOfSheets(); i++) {
			sheetNames.add(workbook.getSheetAt(i).getSheetName());
		}
		
		// Close streams
		workbook.close();
		fileIn.close();
		
		return sheetNames;
	}
	
	/**
	 * Get the string representation of a given row 0-based. 
	 * <p>
	 * Undefined cells are represented as empty Strings ("").
	 * Formulas in formula cells are evaluated using their cached value.
	 * @param rowId - 0 based index of the row
	 * @return List of strings representing the contents of 
	 * each cell in that row.
	 */
	public List<String> readRowFields(int rowId) {
		List<String> row = new ArrayList<>();

		Row workRow = workSheet.getRow(rowId);
		
		if(workRow != null) {
			int lastCol = workRow.getLastCellNum();
			for(int i = 0; i < lastCol; i++) {
				Cell cell = workRow.getCell(i);
				row.add(formatter.formatCellValue(cell));
	
			}
		}
		
		return row;
	}
	
	/**
	 * Get the contents of a STRING type cell. 
	 * <p>
	 * Undefined cells are represented as empty Strings ("").
	 * Formula cells are not further evaluated.
	 * @param rowId - 0 based index of the row; 
	 * @param columnId - 0 based index of the column
	 * @return String representation of the contents of the cell.
	 */
	public String readName(int rowId, int columnId) {
		Row row = workSheet.getRow(rowId);
		
		if(row == null) {
			return "";
		}
		
		Cell cell = row.getCell(columnId);
		
		if (cell == null) {
			return "";
		}
		
		if(cell.getCellType().equals(CellType.STRING)) {
			return readCellField(cell);
		} else {
			LOGGER.debug(String.format("Cell %s does not evaluate to a string.", 
					cell.getAddress().toString()));
			return "";
		}
	}
	
	/**
	 * Get a list of all dates in a particular row. 
	 * <p>
	 * Undefined cells or cells that don't evaluate to a date
	 * are represented as null.
	 * Formulas in formula cells are evaluated.
	 * @param rowId - 0 based index of the row
	 * @param startColumnId - 0 based index of the first column in 
	 * which there is a date
	 * @return List of LocalDate-s 
	 */
	public List<LocalDate> readRowDates(int rowId, int startColumnId) {
		List<Cell> row = readRowCells(rowId);
		
		List<LocalDate> dates = new ArrayList<>();
		final Set<LocalDate> seenDates = new HashSet<>();

		for (int i = startColumnId; i < row.size(); i++) {
			Cell cell = row.get(i);
			
			if (cell == null) {
				dates.add(null);
				continue;
			}
			
			try {
				cell = evaluator.evaluateInCell(cell);
			} catch (Exception e){
				// Manually parse the date
				LocalDate date = parseDate(cell);
				validateDate(dates, seenDates, date);
				continue;
			}
			
			if(cell.getCellType().equals(CellType.NUMERIC)) {
				if(DateUtil.isCellDateFormatted(cell)) {
					LocalDateTime date = cell.getLocalDateTimeCellValue();
					validateDate(dates, seenDates, date.toLocalDate());
				} else {
					LOGGER.debug(String.format("Cell %s is not date-formatted.", 
							cell.getAddress().toString()));
					dates.add(null);
				}
			} else if(cell.getCellType().equals(CellType.STRING)){
				String date = cell.getStringCellValue();
				LocalDate localDate = parseStringDate(date, cell);
				validateDate(dates, seenDates, localDate);
			} else {
				LOGGER.debug(String.format("Cell %s does not evaluate to a date.", 
						cell.getAddress().toString()));
				dates.add(null);
			}
		}
		
		return dates;
	}
	
	/**
	 * Get a list of all numerical values in a particular row. 
	 * <p>
	 * Undefined cells or cells that don't evaluate to a number
	 * are represented as null.
	 * Formulas in formula cells are evaluated.
	 * @param rowId - 0 based index of the row
	 * @param startColumnId - 0 based index of the first column in 
	 * which there is a numerical value
	 * @return List of BigDecimals representing the contents of 
	 * each cell in that row.
	 */
	public List<BigDecimal> readNumCells(int rowId, int startColumnId) {
		List<Cell> row = readRowCells(rowId);
		List<BigDecimal> evaluatedCells = new ArrayList<>();
		
		for (int i = startColumnId; i < row.size(); i++) {
			Cell cell = row.get(i);
			
			if (cell == null) {
				evaluatedCells.add(null);
				continue;
			}
			
			try {
				cell = evaluator.evaluateInCell(cell);
			} catch (Exception e){
				// Manually parse the value
				evaluatedCells.add(parseValue(cell));
				continue;
			}
			
			if(cell.getCellType().equals(CellType.NUMERIC)) {
				if(DateUtil.isCellDateFormatted(cell)) {
					// Dates are ignored
					LOGGER.debug(String.format("Cell %s does not evaluate to a number.", 
							cell.getAddress().toString()));
					evaluatedCells.add(null);
				} else {
					double evaluatedCell = cell.getNumericCellValue();
					evaluatedCells.add(BigDecimal.valueOf(evaluatedCell));
				}
			} else {
				// Cells that don't evaluate to a number are ignored
				LOGGER.debug(String.format("Cell %s does not evaluate to a number.", 
						cell.getAddress().toString()));
				evaluatedCells.add(null);
			}
		}
		
		return evaluatedCells;
	}
	
	/**
	 * Get the Cell representation of a given row 0-based. 
	 * <p>
	 * Undefined cells are represented as null.
	 * @param rowId - 0 based index of the row
	 * @return List of Cells representing a given row
	 */
	private List<Cell> readRowCells(int rowId) {
		List<Cell> row = new ArrayList<>();
		Row workRow = workSheet.getRow(rowId);
		
		if (workRow == null) {
			LOGGER.debug(String.format("Row %s should not be empty!", 
					rowId + 1));
			return Collections.emptyList();
		}
		
		int lastCol = workRow.getLastCellNum();
		for(int i = 0; i < lastCol; i++) {
			Cell cell = workRow.getCell(i);
			row.add(cell);

		}
		
		return row;
	}
	
	/**
	 * Get the string representation of a given cell. 
	 * <p>
	 * Undefined cells are represented as empty Strings ("").
	 * Formulas in formula cells are evaluated using their cached value.
	 * @param cell - the particular cell to be processed
	 * @return String representing the contents of the cell.
	 */
	private String readCellField(Cell cell) {
		return formatter.formatCellValue(cell);
	} 
	
	private void validateDate(List<LocalDate> dates,
			Set<LocalDate> seenDates,  LocalDate date) {
		dates.add(date);
		
		if (date == null) {
			return;
		}
		if (!seenDates.add(date)) {
			throw new IllegalStateException
			(String.format(("Multiple columns for date %s"), date.toString()));
		}
	}
	
	/**
	 * Manually parses the date in case it is a string. 
	 * <p>
	 * Cell must not be null.
	 * Formulas in formula cells are evaluated using their cached value.
	 * @param cell - the particular cell to be processed
	 * @param date - the string inside the cell
	 * @return LocalDate representing the contents of the cell.
	 */
	private LocalDate parseStringDate(String date, Cell cell) {
		LocalDate localDate;
		try {
			DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("MMM-yy");
			YearMonth yearMonthDate = YearMonth.parse(date, timeFormatter);
			localDate = yearMonthDate.atDay(1);
		} catch (Exception e){
			LOGGER.debug(String.format("Could not parse the date stored in cell %s.", 
					cell.getAddress().toString()));
			return null;
		}
		return localDate;
	}
	
	/**
	 * Manually parses the date in case the evaluator could not. 
	 * <p>
	 * Cell must not be null.
	 * Formulas in formula cells are evaluated using their cached value.
	 * @param cell - the particular cell to be processed
	 * @return LocalDate representing the contents of the cell.
	 */
	private LocalDate parseDate(Cell cell) {
		CellType type = cell.getCachedFormulaResultType();
		if (type.equals(CellType.NUMERIC)){
			String date = readCellField(cell);
			return parseStringDate(date, cell);
		} else {
			LOGGER.debug(String.format("Cell %s does not evaluate to a date.", 
					cell.getAddress().toString()));
			return null;
		}
	}

	/**
	 * Manually parses the number in case the evaluator could not. 
	 * <p>
	 * Cell must not be null.
	 * Formulas in formula cells are evaluated using their cached value.
	 * @param cell - the particular cell to be processed
	 * @return BigDecimal representing the contents of the cell..
	 */
	private BigDecimal parseValue(Cell cell) {
		CellType type = cell.getCachedFormulaResultType();
		if (type.equals(CellType.NUMERIC)){
			String val = readCellField(cell);
			double n;
			try {
				n = Double.parseDouble(val.replace(",", ""));
			} catch (Exception e){
				LOGGER.debug(String.format("Could not parse the value stored in cell %s.", 
						cell.getAddress().toString()));
				return null;
			}
			return BigDecimal.valueOf(n);
		} else {
			LOGGER.debug(String.format("Cell %s does not evaluate to a number.", 
					cell.getAddress().toString()));
			return null;
		}
	}
	
	
	private XSSFSheet getSheet(XSSFWorkbook workbook, String sheetName) {
		if (sheetName.length() > 32)
			sheetName = sheetName.substring(0,31);
		return workbook.getSheet(sheetName);
	}

	@SuppressWarnings("resource")
	private XSSFWorkbook getWorkbook(InputStream is) throws IOException {
        XSSFWorkbook newWorkbook = new XSSFWorkbook();
        try {
        	newWorkbook = new XSSFWorkbook(is);
        } catch (Exception e) {
			throw new IOException("Could not get workbook from excel file");
		}
        
        return newWorkbook;
	}
	
}
