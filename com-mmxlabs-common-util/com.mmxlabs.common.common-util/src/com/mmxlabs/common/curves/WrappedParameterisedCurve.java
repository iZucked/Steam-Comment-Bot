package com.mmxlabs.common.curves;

import java.util.Map;

/**
 * Wraps an ICurve instance and exposes both {@link ICurve} and
 * {@link IParameterisedCurve} API
 * 
 * @author Simon Goodall
 *
 */
public class WrappedParameterisedCurve implements IParameterisedCurve, ICurve {

	private final ICurve curve;

	public WrappedParameterisedCurve(final ICurve curve) {
		this.curve = curve;
	}

	@Override
	public int getValueAtPoint(final int point, final Map<String, String> params) {
		return curve.getValueAtPoint(point);
	}

	@Override
	public int getValueAtPoint(final int point) {
		return curve.getValueAtPoint(point);
	}
	
	@Override
	public boolean hasParameters() {
		return false;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof final WrappedParameterisedCurve pc) {
			return curve.equals(pc.curve);
		}
		return curve.equals(obj);
	}

	@Override
	public int hashCode() {
		return curve.hashCode();
	}
}
