/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.json;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.deser.DeserializerCache;
import com.fasterxml.jackson.databind.deser.DeserializerFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.util.ToBooleanFunction;

public class EMFDeserializationContext extends DefaultDeserializationContext {
	private class State {
		final List<Runnable> actions = new LinkedList<>();
		final Map<Pair<String, String>, Object> nameMap = new HashMap<>();
		final Map<Pair<String, String>, Object> idMap = new HashMap<>();
		final Collection<EStructuralFeature> ignoredFeatures = new HashSet<>();
		ToBooleanFunction<JSONReference> missingReferenceHandler = ref -> Boolean.TRUE;
	}

	private final State state;

	public void ignoreFeature(final EStructuralFeature feature) {
		state.ignoredFeatures.add(feature);
	}

	public void setMissingFeatureHandler(final ToBooleanFunction<JSONReference> missingReferenceHandler) {
		state.missingReferenceHandler = missingReferenceHandler;
	}

	public void registerType(final Object value) {

		if (value instanceof EObject) {
			final EObject eObject = (EObject) value;

			final JSONReference ref = JSONReference.of(eObject);

			registerType(ref.getClassType(), ref.getGlobalId(), ref.getName(), eObject);
		}
	}

	public void registerType(final String type, final String id, final String name, final Object value) {
		if (name != null) {
			state.nameMap.put(new Pair<>(type, name), value);
		}
		if (id != null) {
			state.idMap.put(new Pair<>(type, id), value);
		}
	}

	public Object lookupType(final JSONReference ref) {
		final Object idValue = state.idMap.get(new Pair<>(ref.getClassType(), ref.getGlobalId()));
		if (idValue != null) {
			return idValue;
		}
		final Object nameValue = state.nameMap.get(new Pair<>(ref.getClassType(), ref.getName()));
		if (nameValue != null) {
			return nameValue;
		}

		return null;

	}

	public void deferLookup(final JSONReference ref, final EObject owner, final EReference feature) {

		// Check the ignored features list
		if (state.ignoredFeatures.contains(feature)) {
			return;
		}

		if (feature.isMany()) {
			state.actions.add(() -> {
				final Object referenceObject = lookupType(ref);
				if (referenceObject == null) {
					if (state.missingReferenceHandler.accept(ref)) {
						throw new IllegalArgumentException();
					}
					// if (true) {
					// } else {
					// System.out.println("Unknown reference " + ref.getName() + " " + ref.getGlobalId() + " " + ref.getClassType());
					// }
				}

				((List) owner.eGet(feature)).add(referenceObject);
			});
		} else {
			state.actions.add(() -> {

				final Object referenceObject = lookupType(ref);
				if (referenceObject == null) {
					if (state.missingReferenceHandler.accept(ref)) {
						throw new IllegalArgumentException();
					}
					// if (true) {
					// } else {
					// System.out.println("Unknown reference " + ref.getName() + " " + ref.getGlobalId() + " " + ref.getClassType());
					// }
				}

				owner.eSet(feature, referenceObject);
			});
		}
	}

	public void runDeferredActions() {

		final List<Runnable> actionsCopy = new LinkedList<>(state.actions);
		state.actions.clear();
		actionsCopy.forEach(Runnable::run);
	}

	/**
	 * Default constructor for a blueprint object, which will use the standard {@link DeserializerCache}, given factory.
	 */
	public EMFDeserializationContext(final DeserializerFactory df) {
		super(df, null);
		this.state = new State();
	}

	protected EMFDeserializationContext(final EMFDeserializationContext src, final DeserializationConfig config, final JsonParser jp, final InjectableValues values) {
		super(src, config, jp, values);
		this.state = src.state;

	}

	protected EMFDeserializationContext(final EMFDeserializationContext src) {
		super(src);
		this.state = src.state;

	}

	protected EMFDeserializationContext(final EMFDeserializationContext src, final DeserializerFactory factory) {
		super(src, factory);
		this.state = src.state;
	}

	@Override
	public EMFDeserializationContext copy() {
		ClassUtil.verifyMustOverride(Impl.class, this, "copy");
		return new EMFDeserializationContext(this);
	}

	@Override
	public EMFDeserializationContext createInstance(final DeserializationConfig config, final JsonParser p, final InjectableValues values) {
		return new EMFDeserializationContext(this, config, p, values);
	}

	@Override
	public EMFDeserializationContext with(final DeserializerFactory factory) {
		return new EMFDeserializationContext(this, factory);
	}

};