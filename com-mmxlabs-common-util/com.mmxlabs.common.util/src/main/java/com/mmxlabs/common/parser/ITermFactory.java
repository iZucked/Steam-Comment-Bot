package com.mmxlabs.common.parser;

public interface ITermFactory<T> {
	public IExpression<T> createTerm(final String term);
}
