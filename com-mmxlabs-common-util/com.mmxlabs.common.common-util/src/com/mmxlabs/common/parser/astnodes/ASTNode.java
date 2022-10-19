package com.mmxlabs.common.parser.astnodes;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesParser;

@NonNullByDefault
public interface ASTNode {
	/**
	 * Returns an {@link Iterable} of child nodes for navigation on the tree. Do not
	 * modify the returned object. Use {@link #replace(ASTNode, ASTNode)} to change
	 * child nodes.
	 * 
	 * @return
	 */
	Iterable<ASTNode> getChildren();

	/**
	 * Replaces the given child node with the replacement. Assume implementations
	 * use == and return on the first match. An {@link IllegalArgumentException} may
	 * be thrown if the original is not present.
	 * 
	 * @param original
	 * @param replacement
	 */
	void replace(ASTNode original, ASTNode replacement);

	/**
	 * Convert the ASTNode (and children) back into the equivalent expression
	 * string.
	 * 
	 * @return
	 */
	String asString();

	/**
	 * Converts the ASTNode into the equivalent {@link IExpression}
	 * 
	 * @param seriesParser required to access curve data and other parameters
	 * @return
	 */
	IExpression<ISeries> asExpression(SeriesParser seriesParser);
}