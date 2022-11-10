package com.mmxlabs.scheduler.optimiser.exposures;

import java.time.LocalDate;

public record ExposureRecord(String curveName, String currencyUnit, long unitPrice, long nativeVolume, long nativeValue, long mmbtuVolume, LocalDate date, String volumeUnit) {
}
