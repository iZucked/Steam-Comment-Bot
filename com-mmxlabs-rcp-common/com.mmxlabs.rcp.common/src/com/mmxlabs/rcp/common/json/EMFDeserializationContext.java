/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.json;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
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

public class EMFDeserializationContext extends DefaultDeserializationContext {
	private class State {
		final List<Runnable> actions = new LinkedList<>();
		final Map<Pair<String, String>, Object> nameMap = new HashMap<>();
		final Map<Pair<String, String>, Object> idMap = new HashMap<>();
		final Collection<EStructuralFeature> ignoredFeatures = new HashSet<>();
	}

	private final State state;

	public void ignoreFeature(final EStructuralFeature feature) {
		state.ignoredFeatures.add(feature);
	}

	public void registerType(final Object value) {

		if (value instanceof EObject) {
			final EObject eObject = (EObject) value;
			final EStructuralFeature nameFeature = eObject.eClass().getEStructuralFeature("name");
			final EStructuralFeature mmxidFeature = eObject.eClass().getEStructuralFeature("mmxid");
			final String name = nameFeature == null ? null : (String) eObject.eGet(nameFeature);
			String id = mmxidFeature == null ? null : (String) eObject.eGet(mmxidFeature);
			if (id == null) {
				for (final EOperation op : eObject.eClass().getEOperations()) {
					if (op.getName().equalsIgnoreCase("gettempmmxid")) {
						try {
							id = (String) eObject.eInvoke(op, ECollections.emptyEList());
						} catch (final InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
			final String type = String.format("%s/%s", eObject.eClass().getEPackage().getNsURI(), eObject.eClass().getName());
			registerType(type, id, name, eObject);
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

		Object referenceObject = lookupType(ref);
		if (referenceObject == null) {
			if (true) {
				throw new IllegalArgumentException();
			} else {
				System.out.println("Unknown reference " + ref.getName() + "  " + ref.getGlobalId() + " " + ref.getClassType());
			}
		} else {

			if (feature.isMany()) {
				state.actions.add(() -> ((List) owner.eGet(feature)).add(referenceObject));
			} else {
				state.actions.add(() -> {
					owner.eSet(feature, referenceObject);
				});
			}
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