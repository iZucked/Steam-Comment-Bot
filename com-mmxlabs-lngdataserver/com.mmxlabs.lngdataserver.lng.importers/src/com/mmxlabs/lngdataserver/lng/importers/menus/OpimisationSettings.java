package com.mmxlabs.lngdataserver.lng.importers.menus;

import java.time.LocalDate;
import java.time.YearMonth;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.YearMonthSerializer;

public class OpimisationSettings {
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@JsonSerialize(using = LocalDateSerializer.class)
	LocalDate periodStartDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM")
	@JsonSerialize(using = YearMonthSerializer.class)
	YearMonth periodEnd;
	Boolean shippingOnly;
	Boolean generateCharterOuts;
	Boolean withCharterLength;
	Boolean withSpotCargoMarkets;
	String similarityMode;
}
