package com.mmxlabs.lngdataserver.lng.importers.menus;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.YearMonthSerializer;

public class OptioniserSettings {
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@JsonSerialize(using = LocalDateSerializer.class)
	public LocalDate periodStart;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM")
	@JsonSerialize(using = YearMonthSerializer.class)
	public YearMonth periodEnd;
	
	public List<String> loadIds;
	public List<String> dischargeIds;
	public List<String> eventsIds;
}
