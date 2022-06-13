package com.mmxlabs.models.lng.adp.mull.algorithm;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Map.Entry;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.adp.mull.InventoryDateTimeEvent;

public class HourlyToDailyEventIterator implements Iterator<Pair<LocalDate, Integer>> {

	private Entry<LocalDateTime, InventoryDateTimeEvent> cachedEntry = null;
	private final Iterator<Entry<LocalDateTime, InventoryDateTimeEvent>> entries;

	private LocalDate cachedLastNextDate;
	private int cachedLastNextVolume = 0;

	public HourlyToDailyEventIterator(Iterator<Entry<LocalDateTime, InventoryDateTimeEvent>> entries) {
		if (entries.hasNext()) {
			cachedEntry = entries.next();
		} else {
			throw new IllegalStateException("Should not initialise without inventory data");
		}
		cachedLastNextDate = cachedEntry.getKey().toLocalDate().minusDays(1);
		this.entries = entries;
	}

	@Override
	public boolean hasNext() {
		return cachedEntry != null;
	}

	@Override
	public Pair<LocalDate, Integer> next() {
		final Entry<LocalDateTime, InventoryDateTimeEvent> previousCachedEntry = cachedEntry;
		final LocalDate dateToFetch = cachedEntry.getKey().toLocalDate();
		int volumeProduced = cachedEntry.getValue().getNetVolumeIn();
		while (entries.hasNext()) {
			final Entry<LocalDateTime, InventoryDateTimeEvent> entry = entries.next();
			if (dateToFetch.equals(entry.getKey().toLocalDate())) {
				volumeProduced += entry.getValue().getNetVolumeIn();
			} else {
				cachedEntry = entry;
				break;
			}
		}
		if (cachedEntry == previousCachedEntry) {
			cachedEntry = null;
		}
		cachedLastNextDate = dateToFetch;
		cachedLastNextVolume = volumeProduced;
		return Pair.of(dateToFetch, volumeProduced);
	}

	public LocalDate getCachedLastNextDate() {
		return cachedLastNextDate;
	}

	public int getCachedLastNextVolume() {
		return cachedLastNextVolume;
	}
}
