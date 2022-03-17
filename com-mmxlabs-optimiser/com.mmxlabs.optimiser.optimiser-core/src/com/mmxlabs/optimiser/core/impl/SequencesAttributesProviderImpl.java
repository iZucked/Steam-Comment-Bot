/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.impl;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.google.common.base.Objects;
import com.google.common.hash.HashCode;
import com.mmxlabs.optimiser.core.ISequencesAttributesProvider;

/**
 * 
 * @author Simon Goodall
 * 
 */
@NonNullByDefault
public class SequencesAttributesProviderImpl implements ISequencesAttributesProvider {

	private Map<Class<?>, Object> providerMap = new HashMap<>();

	@Override
	public int hashCode() {
		return providerMap.hashCode();
	}

	@Override
	public boolean equals(final @Nullable Object obj) {

		if (obj == this) {
			return true;
		}
		if (obj instanceof SequencesAttributesProviderImpl) {
			final SequencesAttributesProviderImpl other = (SequencesAttributesProviderImpl) obj;

			if (!Objects.equal(providerMap, other.providerMap)) {
				return false;
			}

			return true;

		}

		return false;
	}

	@Override
	public <T> @Nullable T getProvider(Class<T> cls) {

		return cls.cast(providerMap.get(cls));
	}

	public <@NonNull T> void addProvider(Class<T> cls, T object) {
		providerMap.put(cls, object);
	}
}
