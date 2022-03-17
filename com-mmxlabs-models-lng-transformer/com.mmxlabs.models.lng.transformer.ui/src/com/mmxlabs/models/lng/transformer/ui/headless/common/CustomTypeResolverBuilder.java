/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.headless.common;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTypeResolverBuilder;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.mmxlabs.models.lng.parameters.OptimisationStage;

@SuppressWarnings("serial")
	public class CustomTypeResolverBuilder extends DefaultTypeResolverBuilder {
		public CustomTypeResolverBuilder() {
			super(DefaultTyping.NON_FINAL);
		}

		@Override
		public boolean useForType(final JavaType t) {
			final Class<?> clazz = t.getRawClass();
			// only provide class information for OptimisationStage objects
			// don't add class information to plain Object instances that are somehow
			// assignable from OptimisationStage
			if (clazz.equals(Object.class) == false && clazz.isAssignableFrom(OptimisationStage.class)) {
				return true;
			}

			return false;
		}
	}