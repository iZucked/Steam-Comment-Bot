package com.mmxlabs.common.parser.series;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.series.functions.ShiftedSeries;

public class ShiftFunctionConstructor implements IExpression<ISeries> {

	private ShiftFunctionMapper shiftMapper;
	private List<IExpression<ISeries>> arguments;

	public ShiftFunctionConstructor(ShiftFunctionMapper shiftMapper, final List<IExpression<ISeries>> arguments) {
		this.shiftMapper = shiftMapper;
		this.arguments = arguments;
		if (shiftMapper == null) {
			throw new IllegalStateException("No shift mapper function defined");
		}
	}

	public ShiftFunctionConstructor(ShiftFunctionMapper shiftMapper, final IExpression<ISeries> e1, IExpression<ISeries> e2) {
		if (shiftMapper == null) {
			throw new IllegalStateException("No shift mapper function defined");
		}
		this.shiftMapper = shiftMapper;
		this.arguments = CollectionsUtil.makeArrayList(e1, e2);
	}

	@Override
	public @NonNull ISeries evaluate() {
		@NonNull
		ISeries shiftExpression = arguments.get(1).evaluate();
		Number evaluate = shiftExpression.evaluate(0);
		return new ShiftedSeries(arguments.get(0).evaluate(), evaluate.intValue(), shiftMapper);
	}
}
