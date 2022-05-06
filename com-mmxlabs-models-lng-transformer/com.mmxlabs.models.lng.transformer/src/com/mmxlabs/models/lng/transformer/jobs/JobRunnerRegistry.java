package com.mmxlabs.models.lng.transformer.jobs;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

@NonNullByDefault
public class JobRunnerRegistry {

	private final Map<String, @Nullable Supplier<IJobRunner>> jobRunnerFactories = new ConcurrentHashMap<>();

	public void registerJobRunnerFactory(final String name, final Supplier<IJobRunner> factory) {
		jobRunnerFactories.put(name, factory);
	}

	public @Nullable IJobRunner createJobRunner(final String name) {
		final Supplier<IJobRunner> factory = jobRunnerFactories.get(name);
		if (factory != null) {
			return factory.get();
		}
		return null;
	}

}
