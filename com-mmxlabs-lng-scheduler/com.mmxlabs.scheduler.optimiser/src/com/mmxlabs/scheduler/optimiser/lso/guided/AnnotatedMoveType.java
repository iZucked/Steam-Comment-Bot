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

	public static MoveTypesAnnotation annotatedWith(GuidedMoveTypes moveType) {
		return new AnnotatedMoveType(moveType);
	}

	private final GuidedMoveTypes value;

	private AnnotatedMoveType(GuidedMoveTypes value) {
		this.value = value;
	}

	public GuidedMoveTypes value() {
		return this.value;
	}

	public int hashCode() {
		// This is specified in java.lang.Annotation.
		return (127 * "value".hashCode()) ^ value.hashCode();
	}

	public boolean equals(Object o) {
		if (!(o instanceof AnnotatedMoveType)) {
			return false;
		}

		AnnotatedMoveType other = (AnnotatedMoveType) o;
		return value == other.value();
	}

	public String toString() {
		return "@" + AnnotatedMoveType.class.getName() + "(value=" + value + ")";
	}

	public Class<? extends Annotation> annotationType() {
		return AnnotatedMoveType.class;
	}

	private static final long serialVersionUID = 0;
}
