/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.ui.validation;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.OptionalInt;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.mmxcore.NamedObject;

/**
 * Factory class to help build validation status objects.
 * 
 * @author Simon Goodall
 *
 */
public class DetailConstraintStatusFactory {

	private OptionalInt severity = OptionalInt.empty();
	private final List<Pair<EObject, EStructuralFeature>> features = new LinkedList<>();
	private @NonNull String message = "";
	private @Nullable String name;
	private @Nullable String prefix;
	private @Nullable ValidationGroup tag;
	private @Nullable Object constraintKey;

	private DetailConstraintStatusFactory() {

	}

	/**
	 * Create new {@link DetailConstraintStatusFactory} with the same name and tag details. Useful in a validation constraint with multiple conditions, we can avoid duplicating the name component.
	 * 
	 * @return
	 */
	public DetailConstraintStatusFactory copyName() {
		final DetailConstraintStatusFactory f = new DetailConstraintStatusFactory();
		f.withName(this.name);
		f.withTag(this.tag);
		return f;
	}

	public static DetailConstraintStatusFactory makeStatus() {
		return new DetailConstraintStatusFactory();
	}

	public DetailConstraintStatusFactory withSeverity(final int severity) {

		this.severity = OptionalInt.of(severity);

		return this;
	}

	public DetailConstraintStatusFactory withMessage(@NonNull final String message) {
		this.message = message;

		return this;
	}

	public DetailConstraintStatusFactory withFormattedMessage(@NonNull final String format, Object... args) {
		this.message = String.format(format, args);

		return this;
	}

	public DetailConstraintStatusFactory withName(@Nullable final String name) {
		this.name = name;

		return this;
	}

	public DetailConstraintStatusFactory withPrefix(@Nullable final String prefix) {
		this.prefix = prefix;

		return this;
	}
	
	public DetailConstraintStatusFactory withConstraintKey(@Nullable final Object key) {
		this.constraintKey = key;
		return this;
	}

	public DetailConstraintStatusFactory withTypedName(@NonNull final String type, @NonNull final String name) {
		this.name = String.format("%s |'%s'", type, name);

		return this;
	}

	public DetailConstraintStatusFactory withObjectAndFeature(@NonNull final EObject target, final EStructuralFeature feature) {

		this.features.add(new Pair<>(target, feature));

		return this;
	}

	public DetailConstraintStatusFactory withObjectAndFeatures(final Pair<EObject, EStructuralFeature>... extraFeatures) {
		for (final Pair<EObject, EStructuralFeature> f : extraFeatures) {
			this.features.add(f);
		}

		return this;
	}

	public DetailConstraintStatusDecorator make(final IValidationContext ctx, final Collection<IStatus> statuses) {
		final DetailConstraintStatusDecorator status = make(ctx);
		statuses.add(status);
		return status;
	}

	public DetailConstraintStatusDecorator make(final IValidationContext ctx) {
		String finalMessage = name == null ? message : String.format("%s: %s", name, message);
		if (prefix != null) {
			finalMessage = prefix + finalMessage;
		}

		final IConstraintStatus status = (IConstraintStatus) ctx.createFailureStatus(finalMessage);

		DetailConstraintStatusDecorator decorator;
		if (severity.isPresent()) {
			decorator = new DetailConstraintStatusDecorator(status, severity.getAsInt());
		} else {
			decorator = new DetailConstraintStatusDecorator(status);
		}
		features.forEach(p -> decorator.addEObjectAndFeature(p.getFirst(), p.getSecond()));

		if (name != null) {
			decorator.setName(name);
		}
		decorator.setBaseMessage(message);

		if (tag != null) {
			decorator.setTag(tag);
		}

		if (constraintKey != null) {
			decorator.setConstraintKey(constraintKey);
		}
		
		return decorator;
	}

	public static @NonNull String getName(@Nullable final NamedObject namedObject) {
		if (namedObject != null) {
			final String name = namedObject.getName();
			if (name != null && !name.trim().isEmpty()) {
				return name.trim();
			}
		}
		return "<No name>";
	}

	public static @NonNull String getName(@Nullable final NamedObject namedObject, @NonNull final String defaultName) {
		if (namedObject != null) {
			final String name = namedObject.getName();
			if (name != null && !name.trim().isEmpty()) {
				return name.trim();
			}
		}
		return defaultName;
	}

	public DetailConstraintStatusFactory withTag(final ValidationGroup tag) {
		this.tag = tag;

		return this;
	}
}
