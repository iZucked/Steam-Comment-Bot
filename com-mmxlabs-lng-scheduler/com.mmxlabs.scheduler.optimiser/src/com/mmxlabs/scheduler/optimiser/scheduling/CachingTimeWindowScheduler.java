package com.mmxlabs.scheduler.optimiser.scheduling;

import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.caches.AbstractCache;
import com.mmxlabs.common.caches.LHMCache;
import com.mmxlabs.optimiser.core.ISequences;

public class CachingTimeWindowScheduler implements ITimeWindowScheduler {

	private static class Key {
		private final @NonNull ISequences sequences;

		public Key(final @NonNull ISequences sequences) {
			this.sequences = sequences;
		}

		@Override
		public int hashCode() {
			return sequences.hashCode();
		}

		@Override
		public boolean equals(final Object obj) {

			if (obj == this) {
				return true;
			}
			if (obj instanceof Key) {
				final Key other = (Key) obj;
				return Objects.equals(this.sequences, other.sequences);
			}
			return false;
		}
	}

	private final @NonNull AbstractCache<@NonNull Key, @Nullable ScheduledTimeWindows> cache;

	private ITimeWindowScheduler delegate;

	public CachingTimeWindowScheduler(final ITimeWindowScheduler delegate) {
		this.delegate = delegate;

		cache = new LHMCache<>("CachingTimeWindowScheduler", (key) -> {
			return new Pair<>(key, delegate.schedule(key.sequences));
		}, 50_000);
	}

	@Override
	public ScheduledTimeWindows schedule(final @NonNull ISequences fullSequences) {
		return cache.get(new Key(fullSequences));
	}
}
