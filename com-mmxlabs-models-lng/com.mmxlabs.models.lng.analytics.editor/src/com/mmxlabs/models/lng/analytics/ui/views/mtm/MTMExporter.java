/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.mtm;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.extensions.XSSFCellBorder.BorderSide;

import com.mmxlabs.lingo.reports.views.schedule.model.Table;

public class MTMExporter {
	
	private final Map<Integer, XSSFCellStyle> dateStyles = new HashMap<>();
	private final Map<Integer, XSSFCellStyle> genStyles = new HashMap<>();
	private final java.awt.Color greyColour = new Color(191, 191, 191);
	private final java.awt.Color alternateColour = new Color(239, 246, 251);
	private final java.awt.Color orangeColour = new Color(248, 203, 173);
	private final java.awt.Color greenColour = new Color(226, 239, 218);
	private final java.awt.Color whiteColour = new Color(255, 255, 255);
	
	public static void export(final Table table, String fileName, YearMonth start, YearMonth end) {
		if (!"".equals(fileName) && table != null) {
			new MTMExporter().save(table, new File(fileName), start, end);
		}
	}
	
	public void save(final Table table, File file, final YearMonth start, final YearMonth end) {
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Cargo Schedule");
		int rowNum = 0;
		rowNum = writeEcons(table, sheet, rowNum, start, end);
		
		for (int i = 0; i <=39; i++) {
			sheet.autoSizeColumn(i);
		}
		
		try (FileOutputStream outputStream = new FileOutputStream(file)) {
			workbook.write(outputStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private int writeEcons(final Table table, XSSFSheet sheet, int startRow, final YearMonth start, final YearMonth end) {
		int rowNum = startRow;
		// Write header

		XSSFWorkbook workbook = sheet.getWorkbook();

		generateStyles(workbook);
			
		XSSFFont boldFont = workbook.createFont();
		boldFont.setBold(true);
		boldFont.setFontName("Calibri");
		//right border
		XSSFCellStyle rightBorder = workbook.createCellStyle();
		rightBorder.setBorderRight((short) 1);
		rightBorder.setAlignment(HorizontalAlignment.CENTER);
		//right and left border
		XSSFCellStyle lrb = workbook.createCellStyle();
		lrb.setAlignment(HorizontalAlignment.CENTER);
		lrb.setBorderRight((short) 1);
		lrb.setBorderLeft((short) 1);
		//top and bottom borders
		XSSFCellStyle tbb = workbook.createCellStyle();
		tbb.setAlignment(HorizontalAlignment.CENTER);
		tbb.setBorderTop((short) 1);
		tbb.setBorderBottom((short) 1);
		
		// side month style
		XSSFCellStyle monthStyle = workbook.createCellStyle();
		monthStyle.setDataFormat(workbook.createDataFormat().getFormat("mmm-yy"));
		monthStyle.setAlignment(HorizontalAlignment.CENTER);
		monthStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		monthStyle.setRotation((short) 90);
		monthStyle.setBorderRight((short) 1);
		monthStyle.setBorderLeft((short) 1);
		monthStyle.setBorderTop((short) 1);
		monthStyle.setBorderBottom((short) 1);
		monthStyle.setFont(boldFont);
		
		// side month style. alt
		XSSFCellStyle monthStyleA = workbook.createCellStyle();
		monthStyleA.setDataFormat(workbook.createDataFormat().getFormat("mmm-yy"));
		monthStyleA.setAlignment(HorizontalAlignment.CENTER);
		monthStyleA.setVerticalAlignment(VerticalAlignment.CENTER);
		monthStyleA.setFillForegroundColor(new XSSFColor(alternateColour));
		monthStyleA.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		monthStyleA.setRotation((short) 90);
		monthStyleA.setBorderRight((short) 1);
		monthStyleA.setBorderLeft((short) 1);
		monthStyleA.setBorderTop((short) 1);
		monthStyleA.setBorderBottom((short) 1);
		monthStyleA.setFont(boldFont);

		//rowNum = makeTitle(sheet, rowNum, workbook);
		
		rowNum = makeHeader(sheet, rowNum, workbook, rightBorder);
		
//		{
//			final List<CargoScheduleModel> csmList = CargoScheduleTransformer.transformTable(table, start, end);
//			
//			Collections.sort(csmList, new Comparator<CargoScheduleModel>() {
//
//				@Override
//				public int compare(CargoScheduleModel o1, CargoScheduleModel o2) {
//					if(o1.DWStart != null) {
//						if (o2.DWStart != null) {
//							return (o1.DWStart.compareTo(o2.DWStart));
//						} else if (o2.LWStart!= null){
//							return (o1.DWStart.compareTo(o2.LWStart.plusDays(16)));
//						}
//					} else if(o1.LWStart != null) {
//						if (o2.DWStart != null) {
//							return (o1.LWStart.compareTo(o2.DWStart));
//						} else if (o2.LWStart!= null){
//							return (o1.LWStart.compareTo(o2.LWStart.plusDays(16)));
//						}
//					}
//					return 0;
//				}
//			});
//			
//			boolean alt = false;
//			//that is one ugly if-statement!
//			final CargoScheduleModel temp0 = csmList.get(0);
//			YearMonth currentMonth = YearMonth.from(temp0.DWEnd != null ? temp0.DWEnd : //
//				temp0.DWStart != null ? temp0.DWStart : //
//				temp0.LWEnd != null ? temp0.LWEnd : temp0.LWStart);
//			int cmrb = rowNum; //current month row beginning
//			for (final CargoScheduleModel csm : csmList) {
//				
//				final Row row;
//				final YearMonth thisMonth = YearMonth.from(csm.DWEnd != null ? csm.DWEnd : //
//					csm.DWStart != null ? csm.DWStart : //
//					csm.LWEnd != null ? csm.LWEnd : csm.LWStart);
//				if (!currentMonth.equals(thisMonth)) {
//					currentMonth = thisMonth;
//					sheet.addMergedRegion(new CellRangeAddress(cmrb, rowNum - 1, 1, 1));
//					generateMonthGap(sheet, rowNum++, tbb);
//					cmrb = rowNum;
//					
//					row = sheet.createRow(rowNum++);
//					{
//						Cell cell = row.createCell(0);
//						cell.setCellStyle(rightBorder);
//					}
//					
//					alt = !alt;
//				} else {
//					row = sheet.createRow(rowNum++);
//					{
//						Cell cell = row.createCell(0);
//						cell.setCellStyle(rightBorder);
//					}
//				}
//				final int type = csm.type;
//				XSSFCellStyle s = getStyle(type, alt);
//				XSSFCellStyle d = getDateStyle(type, alt);
//				
//				{
//					Cell cell = row.createCell(1);
//					final LocalDate foo = LocalDate.of(currentMonth.getYear(), currentMonth.getMonthValue(), 1);
//					cell.setCellValue(Date.valueOf(foo));
//					cell.setCellStyle(alt?monthStyleA:monthStyle);
//				}
//				
//				if(type != 2){
//					{ //REF
//						Cell cell = row.createCell(2);
//						cell.setCellValue(csm.purchaseRef);
//						XSSFCellStyle s2 = (XSSFCellStyle) s.clone();
//						s2.setBorderLeft((short) 1);
//						s2.setFont(boldFont);
//						cell.setCellStyle(s2);
//					}
//					{ //CN
//						Cell cell = row.createCell(3);
//						cell.setCellValue(csm.purchaseCN);
//						cell.setCellStyle(s);
//					}
//					if (csm.purchaseSourceNom != null) {
//						row.createCell(4).setCellValue(Date.valueOf(csm.purchaseSourceNom));
//						row.getCell(4).setCellStyle(d);
//					} else {
//						setEmptyStyle(row, 4, s);
//					}
//					{ //VOLUME
//						Cell cell = row.createCell(5);
//						cell.setCellValue(csm.purchaseVolume);
//						cell.setCellStyle(s);
//					}
//					{ //SPECS
//						Cell cell = row.createCell(6);
//						cell.setCellValue(csm.purchaseSpecs);
//						cell.setCellStyle(s);
//					}
//					{ //TERMS
//						Cell cell = row.createCell(7);
//						cell.setCellValue(csm.purchaseTerms);
//						cell.setCellStyle(s);
//					}
//					{ //DISPORT
//						Cell cell = row.createCell(8);
//						cell.setCellValue(csm.disPort);
//						cell.setCellStyle(s);
//					}
//
//					if (csm.disPortNom != null) {
//						row.createCell(9).setCellValue(Date.valueOf(csm.disPortNom));
//						row.getCell(9).setCellStyle(d);
//					} else {
//						setEmptyStyle(row, 9, s);
//					}
//
//					if (csm.DWStart != null) {
//						row.createCell(10).setCellValue(Date.valueOf(csm.DWStart));
//						row.getCell(10).setCellStyle(d);
//					} else {
//						setEmptyStyle(row, 10, s);
//					}
//
//					if (csm.DWEnd != null) {
//						row.createCell(11).setCellValue(Date.valueOf(csm.DWEnd));
//						row.getCell(11).setCellStyle(d);
//					} else {
//						setEmptyStyle(row, 11, s);
//					}
//
//					if (csm.DWNom != null) {
//						row.createCell(12).setCellValue(Date.valueOf(csm.DWNom));
//						row.getCell(12).setCellStyle(d);
//					}else{
//						setEmptyStyle(row, 12, s);
//					}
//					{ //PRICE
//						Cell cell = row.createCell(13);
//						cell.setCellValue(csm.purchasePrice);
//						cell.setCellStyle(s);
//					}
//					{ //SOURCE
//						Cell cell = row.createCell(14);
//						cell.setCellValue(csm.source);
//						cell.setCellStyle(s);
//					}
//					if (csm.LWStart != null) {
//						row.createCell(15).setCellValue(Date.valueOf(csm.LWStart));
//						row.getCell(15).setCellStyle(d);
//					}
//					if (csm.LWEnd != null) {
//						row.createCell(16).setCellValue(Date.valueOf(csm.LWEnd));
//						row.getCell(16).setCellStyle(d);
//					}
//					{//SELLER
//						Cell cell = row.createCell(17);
//						cell.setCellValue(csm.purchaseSeller);
//						cell.setCellStyle(s);
//					}
//					{//BUYER
//						Cell cell = row.createCell(18);
//						cell.setCellValue(csm.purchaseBuyer);
//						cell.setCellStyle(rightBorder);
//						cell.setCellStyle(s);
//					}
//				} else {
//					Cell cell = row.createCell(2);
//					XSSFCellStyle s2 = (XSSFCellStyle) getStyle(0, alt).clone();
//					s2.setBorderLeft((short) 1);
//					cell.setCellStyle(s2);
//					for (int i = 3; i < 19; i++) {
//						setEmptyStyle(row, i, getStyle(0, alt));
//					}
//				}
//				{
//					Cell cell = row.createCell(19);
//					cell.setCellStyle(lrb);
//				}
//				{ //VESSEL
//					Cell cell = row.createCell(20);
//					cell.setCellValue(csm.vessel);
//					cell.setCellStyle(getStyle(0, alt));
//				}
//				if (csm.vesselNom != null) {
//					row.createCell(21).setCellValue(Date.valueOf(csm.vesselNom));
//					row.getCell(21).setCellStyle(getDateStyle(0, alt));
//				} else {
//					setEmptyStyle(row, 21, getStyle(0, alt));
//				}
//				{
//					Cell cell = row.createCell(22);
//					cell.setCellStyle(lrb);
//				}
//				if (type != 1){
//					{ //SELLER
//						Cell cell = row.createCell(23);
//						cell.setCellValue(csm.saleSeller);
//						cell.setCellStyle(s);
//					}
//					{ //BUYER
//						Cell cell = row.createCell(24);
//						cell.setCellValue(csm.saleBuyer);
//						cell.setCellStyle(s);
//					}
//					if (csm.DWStart != null) {
//						row.createCell(25).setCellValue(Date.valueOf(csm.DWStart));
//						row.getCell(25).setCellStyle(d);
//					} else {
//						setEmptyStyle(row, 25, s);
//					}
//					
//					if (csm.DWEnd != null) {
//						row.createCell(26).setCellValue(Date.valueOf(csm.DWEnd));
//						row.getCell(26).setCellStyle(d);
//					} else {
//						setEmptyStyle(row, 26, s);
//					}
//					if (csm.DWNom != null) {
//						row.createCell(27).setCellValue(Date.valueOf(csm.DWNom));
//						row.getCell(27).setCellStyle(d);
//					} else{
//						setEmptyStyle(row, 27, s);
//					}
//					{ //DISPORT
//						Cell cell = row.createCell(28);
//						cell.setCellValue(csm.disPort);
//						cell.setCellStyle(s);
//					}
//					if (csm.disPortNom != null) {
//						row.createCell(29).setCellValue(Date.valueOf(csm.disPortNom));
//						row.getCell(29).setCellStyle(d);
//					} else {
//						setEmptyStyle(row, 29, s);
//					}
//					{ //PRICE
//						Cell cell = row.createCell(30);
//						cell.setCellValue(csm.salePrice);
//						cell.setCellStyle(s);
//					}
//					if (csm.type == 0) { //SOURCE
//						Cell cell = row.createCell(31);
//						cell.setCellValue(csm.source);
//						cell.setCellStyle(s);
//					} else {
//						setEmptyStyle(row, 31, s);
//					}
//					if (csm.saleSourceNom != null) {
//						row.createCell(32).setCellValue(Date.valueOf(csm.saleSourceNom));
//						row.getCell(32).setCellStyle(d);
//					} else {
//						setEmptyStyle(row, 32, s);
//					}
//					{ //TERMS
//						Cell cell = row.createCell(33);
//						cell.setCellValue(csm.saleTerms);
//						cell.setCellStyle(s);
//					}
//					{ //SPECS
//						Cell cell = row.createCell(34);
//						cell.setCellValue(csm.saleSpecs);
//						cell.setCellStyle(s);
//					}
//					{ //VOLUME
//						Cell cell = row.createCell(35);
//						cell.setCellValue(csm.saleVolume);
//						cell.setCellStyle(s);
//					}
//					{ //REF
//						Cell cell = row.createCell(36);
//						cell.setCellValue(csm.saleRef);
//						XSSFCellStyle s2 = (XSSFCellStyle) s.clone();
//						s2.setFont(boldFont);
//						cell.setCellStyle(s2);
//						s2 = null;
//					}
//				} else {
//					for (int i = 23; i < 37; i++) {
//						setEmptyStyle(row, i, getStyle(0, alt));
//					}
//				}
//				{ //CN
//					Cell cell = row.createCell(37);
//					cell.setCellValue(csm.saleCN);
//					cell.setCellStyle(type == 2? getStyle(2988, false) : getStyle(1988, alt));
//				}
//				{ //NOTE
//					Cell cell = row.createCell(38);
//					cell.setCellValue(csm.note);
//					cell.setCellStyle(getStyle(1990, alt));
//				}
//			}
//			sheet.addMergedRegion(new CellRangeAddress(cmrb, rowNum - 1, 1, 1));
//			{ //Border at the bottom
//				XSSFCellStyle topB = workbook.createCellStyle();
//				topB.setBorderTop((short) 1);	
//				Row row = sheet.createRow(rowNum++);
//				for (int i = 1; i <=38; i++) {
//					Cell cell = row.createCell(i);
//					cell.setCellStyle(topB);
//				}
//			}
//		}

		return rowNum;
	}

	private int makeTitle(XSSFSheet sheet, int rowNum, XSSFWorkbook workbook) {
		{
			// title
			XSSFFont titleFont = workbook.createFont();
			titleFont.setBold(true);
			titleFont.setFontHeight(22);
			titleFont.setFontName("Arial");
			
			XSSFCellStyle titleStyle = workbook.createCellStyle();
			titleStyle.setFont(titleFont);
			titleStyle.setAlignment(HorizontalAlignment.CENTER);
			
			XSSFCellStyle titleDateStyle = workbook.createCellStyle();
			titleDateStyle.setFont(titleFont);
			titleDateStyle.setAlignment(HorizontalAlignment.CENTER);
			titleDateStyle.setDataFormat(workbook.createDataFormat().getFormat("[$-809]dd mmmm yyyy;@"));
			{
				//21 GGLNG/GMTS Cargo Schedule
				Row row = sheet.createRow(rowNum++);
				row.createCell(18);
				row.getCell(18).setCellValue("GGLNG/GMTS Cargo Schedule");
				row.getCell(18).setCellStyle(titleStyle);
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 18, 25));
			}
			{
				Row row = sheet.createRow(rowNum++);
				
				//7 LONGS
				row.createCell(6);
				row.getCell(6).setCellValue("LONGS");
				row.getCell(6).setCellStyle(titleStyle);
				//18-25 DATE
				row.createCell(18);
				row.getCell(18).setCellValue(Date.valueOf(LocalDate.now()));
				row.getCell(18).setCellStyle(titleDateStyle);
				sheet.addMergedRegion(new CellRangeAddress(1, 1, 18, 25));
				//29
				row.createCell(28);
				row.getCell(28).setCellValue("SHORTS");
				row.getCell(28).setCellStyle(titleStyle);
			}
			{
				Row row = sheet.createRow(rowNum++);
				row.setHeightInPoints((float) 10);
			}
		}
		return rowNum;
	}

	private int makeHeader(XSSFSheet sheet, int rowNum, XSSFWorkbook workbook, XSSFCellStyle rightBorder) {
		{
			// smaller header
			XSSFFont headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setFontHeight(12);
			headerFont.setFontName("Calibri");
			
			XSSFCellStyle headerStyle = workbook.createCellStyle();
			headerStyle.setFont(headerFont);
			headerStyle.setBorderBottom((short) 1);
			headerStyle.setBorderTop((short) 1);
			headerStyle.setAlignment(HorizontalAlignment.CENTER);
			XSSFCellStyle ab = workbook.createCellStyle();
			ab.setFont(headerFont);
			ab.setBorderRight((short) 1);
			ab.setBorderLeft((short) 1);
			ab.setBorderBottom((short) 1);
			ab.setBorderTop((short) 1);
			ab.setAlignment(HorizontalAlignment.CENTER);

			String[] header = {"REF", "CN", "SOURCE NOM", "VOLUME", "SPECS", "TERMS", //
					"DISPORT", "DISPORT NOM", "DW start", "DW end", "DW NOM", "PURCHASE PRICE", //
					"SOURCE", "LW start", "LW end", "SELLER", "BUYER", "",// LONGS
					"VESSEL", "VESSEL NOM", "",// VESSEL
					"SELLER", "BUYER", "DW start", "DW end", "DW NOM", "DISPORT", "DISPORT NOM", // SHORTS
					"SALE PRICE", "SOURCE", "SOURCE NOM", "TERMS", "SPECS", "VOLUME", //
					"REF", "CN", "NOTES"};
			
			Row row = sheet.createRow(rowNum++);
			
			Cell foo = row.createCell(1);
			foo.setCellStyle(ab);
			int colNum = 2;
			for (String field : header) {
				Cell cell = row.createCell(colNum++);
				cell.setCellValue(field);
				cell.setCellStyle(headerStyle);
			}
			foo = row.getCell(19);
			foo.setCellStyle(ab);
			foo = row.getCell(22);
			foo.setCellStyle(ab);
			foo = row.getCell(37);
			foo.setCellStyle(ab);
			foo = row.getCell(38);
			foo.setCellStyle(ab);
		}
		return rowNum;
	}
	
	//Experimenting atm
	private void generateStyles(final XSSFWorkbook workbook) {
		
		
		XSSFCellStyle dateStyle = workbook.createCellStyle();
		dateStyle.setFillForegroundColor(new XSSFColor(whiteColour));
		dateStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		dateStyle.setAlignment(HorizontalAlignment.CENTER);
		dateStyle.setDataFormat(workbook.createDataFormat().getFormat("dd-mmm"));
		dateStyle.setBorderRight((short) 1);
		dateStyle.setBorderColor(BorderSide.RIGHT, new XSSFColor(greyColour));
		dateStyle.setBorderBottom((short) 1);
		dateStyle.setBorderColor(BorderSide.BOTTOM, new XSSFColor(greyColour));
		// date style for load
		XSSFCellStyle dateSL = workbook.createCellStyle();
		dateSL.setAlignment(HorizontalAlignment.CENTER);
		dateSL.setDataFormat(workbook.createDataFormat().getFormat("dd-mmm"));
		dateSL.setFillForegroundColor(new XSSFColor(greenColour));
		dateSL.setFillPattern(CellStyle.SOLID_FOREGROUND);
		dateSL.setBorderBottom((short) 1);
		dateSL.setBorderColor(BorderSide.BOTTOM, new XSSFColor(greyColour));
		dateSL.setBorderRight((short) 1);
		dateSL.setBorderColor(BorderSide.RIGHT, new XSSFColor(greyColour));
		
		//date style for sale
		XSSFCellStyle dateSS = workbook.createCellStyle();
		dateSS.setAlignment(HorizontalAlignment.CENTER);
		dateSS.setDataFormat(workbook.createDataFormat().getFormat("dd-mmm"));

		dateSS.setFillForegroundColor(new XSSFColor(orangeColour));
		dateSS.setFillPattern(CellStyle.SOLID_FOREGROUND);
		dateSS.setBorderBottom((short) 1);
		dateSS.setBorderColor(BorderSide.BOTTOM, new XSSFColor(greyColour));
		dateSS.setBorderRight((short) 1);
		dateSS.setBorderColor(BorderSide.RIGHT, new XSSFColor(greyColour));
		//
		XSSFCellStyle dateStyleAlt = workbook.createCellStyle();
		dateStyleAlt.setAlignment(HorizontalAlignment.CENTER);
		dateStyleAlt.setDataFormat(workbook.createDataFormat().getFormat("dd-mmm"));
		dateStyleAlt.setFillForegroundColor(new XSSFColor(alternateColour));
		dateStyleAlt.setFillPattern(CellStyle.SOLID_FOREGROUND);
		dateStyleAlt.setBorderBottom((short) 1);
		dateStyleAlt.setBorderColor(BorderSide.BOTTOM, new XSSFColor(greyColour));
		dateStyleAlt.setBorderRight((short) 1);
		dateStyleAlt.setBorderColor(BorderSide.RIGHT, new XSSFColor(greyColour));
		//
		dateStyles.put(0, dateStyle);
		dateStyles.put(1, dateSL);
		dateStyles.put(2, dateSS);
		dateStyles.put(1987, dateStyleAlt);
		
		XSSFCellStyle cargo = workbook.createCellStyle();
		cargo.setFillForegroundColor(new XSSFColor(whiteColour));
		cargo.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cargo.setAlignment(HorizontalAlignment.CENTER);
		cargo.setBorderRight((short) 1);
		cargo.setBorderColor(BorderSide.RIGHT, new XSSFColor(greyColour));
		cargo.setBorderBottom((short) 1);
		cargo.setBorderColor(BorderSide.BOTTOM, new XSSFColor(greyColour));
		XSSFCellStyle longs = workbook.createCellStyle();
		longs.setAlignment(HorizontalAlignment.CENTER);
		longs.setFillForegroundColor(new XSSFColor(greenColour));
		longs.setFillPattern(CellStyle.SOLID_FOREGROUND);
		longs.setBorderBottom((short) 1);
		longs.setBorderColor(BorderSide.BOTTOM, new XSSFColor(greyColour));
		longs.setBorderRight((short) 1);
		longs.setBorderColor(BorderSide.RIGHT, new XSSFColor(greyColour));
		XSSFCellStyle shorts = workbook.createCellStyle();
		shorts.setAlignment(HorizontalAlignment.CENTER);
		shorts.setFillForegroundColor(new XSSFColor(orangeColour));
		shorts.setFillPattern(CellStyle.SOLID_FOREGROUND);
		shorts.setBorderBottom((short) 1);
		shorts.setBorderColor(BorderSide.BOTTOM, new XSSFColor(greyColour));
		shorts.setBorderRight((short) 1);
		shorts.setBorderColor(BorderSide.RIGHT, new XSSFColor(greyColour));
		//
		XSSFCellStyle cargoAlt = workbook.createCellStyle();
		cargoAlt.setAlignment(HorizontalAlignment.CENTER);
		cargoAlt.setFillForegroundColor(new XSSFColor(alternateColour));
		cargoAlt.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cargoAlt.setBorderBottom((short) 1);
		cargoAlt.setBorderColor(BorderSide.BOTTOM, new XSSFColor(greyColour));
		cargoAlt.setBorderRight((short) 1);
		cargoAlt.setBorderColor(BorderSide.RIGHT, new XSSFColor(greyColour));
		//right and left border
		XSSFCellStyle lrb = workbook.createCellStyle();
		lrb.setFillForegroundColor(new XSSFColor(whiteColour));
		lrb.setFillPattern(CellStyle.SOLID_FOREGROUND);
		lrb.setAlignment(HorizontalAlignment.CENTER);
		lrb.setBorderRight((short) 1);
		lrb.setBorderLeft((short) 1);
		//
		XSSFCellStyle lrbS = workbook.createCellStyle();
		lrbS.setAlignment(HorizontalAlignment.CENTER);
		lrbS.setBorderRight((short) 1);
		//lrbS.setBorderColor(BorderSide.RIGHT, new XSSFColor(greyColour));
		lrbS.setBorderLeft((short) 1);
		//lrbS.setBorderColor(BorderSide.LEFT, new XSSFColor(greyColour));
		lrbS.setFillForegroundColor(new XSSFColor(orangeColour));
		lrbS.setFillPattern(CellStyle.SOLID_FOREGROUND);
		lrbS.setBorderBottom((short) 1);
		lrbS.setBorderColor(BorderSide.BOTTOM, new XSSFColor(greyColour));
		//right and left border
		XSSFCellStyle lrbAlt = workbook.createCellStyle();
		lrbAlt.setAlignment(HorizontalAlignment.CENTER);
		lrbAlt.setBorderRight((short) 1);
		//lrbAlt.setBorderColor(BorderSide.RIGHT, new XSSFColor(greyColour));
		lrbAlt.setBorderLeft((short) 1);
		//lrbAlt.setBorderColor(BorderSide.LEFT, new XSSFColor(greyColour));
		lrbAlt.setFillForegroundColor(new XSSFColor(alternateColour));
		lrbAlt.setFillPattern(CellStyle.SOLID_FOREGROUND);
		lrbAlt.setBorderBottom((short) 1);
		lrbAlt.setBorderColor(BorderSide.BOTTOM, new XSSFColor(greyColour));
		//right border
		XSSFCellStyle rightBorder = workbook.createCellStyle();
		rightBorder.setFillForegroundColor(new XSSFColor(whiteColour));
		rightBorder.setFillPattern(CellStyle.SOLID_FOREGROUND);
		rightBorder.setAlignment(HorizontalAlignment.CENTER);
		rightBorder.setBorderRight((short) 1);
		
		
		XSSFCellStyle rightBorderAlt = workbook.createCellStyle();
		rightBorderAlt.setAlignment(HorizontalAlignment.CENTER);
		rightBorderAlt.setBorderRight((short) 1);
		rightBorderAlt.setFillForegroundColor(new XSSFColor(alternateColour));
		rightBorderAlt.setFillPattern(CellStyle.SOLID_FOREGROUND);
		rightBorderAlt.setBorderBottom((short) 1);
		rightBorderAlt.setBorderColor(BorderSide.BOTTOM, new XSSFColor(greyColour));
		//
		genStyles.put(0, cargo);
		genStyles.put(1, longs);
		genStyles.put(2, shorts);
		genStyles.put(1987, cargoAlt);
		genStyles.put(1988, lrb);
		genStyles.put(2988, lrbS);
		genStyles.put(1989, lrbAlt);
		genStyles.put(1990, rightBorder);
		genStyles.put(1991, rightBorderAlt);
	}
	
	private XSSFCellStyle getDateStyle(final int type, final boolean alt){
		if (alt && type == 0) return dateStyles.get(1987);
		return dateStyles.get(type);
	}
	private XSSFCellStyle getStyle(final int type, final boolean alt){
		if (alt && type == 0) return genStyles.get(1987);
		if (type == 1988) return genStyles.get(alt?1989:1988);
		if (type == 1990) return genStyles.get(alt?1991:1990);
		return genStyles.get(type);
	}
	
	private void setEmptyStyle(final Row row, final int i, final XSSFCellStyle s) {
		row.createCell(i);
		row.getCell(i).setCellStyle(s);
	}
	
	private Row generateMonthGap(final XSSFSheet sheet, int rowNum, final XSSFCellStyle style) {
		Row row = sheet.createRow(rowNum);// createRow(rowNum++);
		for (int i = 1; i <=38; i++) {
			Cell cell = row.createCell(i);
			cell.setCellStyle(style);
		}
		return row;
	}

}
