package com.mmxlabs.common.parser.series;

/**
 * The shift function needs to be "context" aware. E.g. we would like shift(curve, 1) to mean shift by 1 month. However this is a variable quantity of time depending on where you currently are. This
 * interface defines a callback to the SHIFT function to apply a mapping.
 * 
 * @author Simon Goodall
 *
 */
@FunctionalInterface
public interface ShiftFunctionMapper {

	int mapChangePoint(int currentChangePoint, int shiftAmount);
}
