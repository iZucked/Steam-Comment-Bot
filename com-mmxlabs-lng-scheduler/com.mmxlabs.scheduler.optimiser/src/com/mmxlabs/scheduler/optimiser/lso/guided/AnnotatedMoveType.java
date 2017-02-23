package com.mmxlabs.scheduler.optimiser.lso.guided;

import java.lang.annotation.Annotation;

import com.google.inject.name.Names;

/**
 * Based on the {@link Names} class
 * 
 * @author Simon Goodall
 *
 */
public class AnnotatedMoveType implements MoveTypesAnnotation {

	public static MoveTypesAnnotation annotatedWith(final GuidedMoveTypes moveType) {
		return new AnnotatedMoveType(moveType);
	}

	private final GuidedMoveTypes value;

	private AnnotatedMoveType(final GuidedMoveTypes value) {
		this.value = value;
	}

	public GuidedMoveTypes value() {
		return this.value;
	}

	public int hashCode() {
		// This is specified in java.lang.Annotation.
		return (127 * "value".hashCode()) ^ value.hashCode();
	}

	public boolean equals(final Object o) {
		if (!(o instanceof MoveTypesAnnotation)) {
			return false;
		}

		final MoveTypesAnnotation other = (MoveTypesAnnotation) o;
		return value == other.value();
	}

	public String toString() {
		return "@" + MoveTypesAnnotation.class.getName() + "(value=" + value + ")";
	}

	public Class<? extends Annotation> annotationType() {
		return MoveTypesAnnotation.class;
	}

	private static final long serialVersionUID = 0;
}
