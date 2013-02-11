/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.parseutils;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mmxlabs.common.parser.ExpressionParser;
import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.IFunctionFactory;
import com.mmxlabs.common.parser.IInfixOperatorFactory;
import com.mmxlabs.common.parser.IPrefixOperatorFactory;
import com.mmxlabs.common.parser.ITermFactory;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.ExpressionPriceParameters;
import com.mmxlabs.models.lng.commercial.IndexPriceParameters;
import com.mmxlabs.models.lng.commercial.LNGPriceCalculatorParameters;
import com.mmxlabs.models.lng.pricing.Index;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;

/**
 * Utility class to calculate schedule exposure to market indices.
 * Provides static methods  
 * 
 * @author Simon McGregor
 * @since 3.0
 */
public class Exposures {

	/**
	 * Class to store cumulative numeric data by key.  
	 */
	public static class CumulativeMap<T> extends HashMap<T,Double> {
		private static final long serialVersionUID = 1L;

		/**
		 * Adds a cumulative numeric value to the map. If the key is contained in the map,
		 * the new value is added to the existing value. Otherwise, the new value is entered into
		 * the map. In other words, the map conceptually defaults new keys to zero before adding
		 * the specified value.
		 *  
		 * @param key
		 * @param value
		 */
		public void plusEquals(T key, Double value) {
			if (containsKey(key)) {
				put(key, get(key) + value);
			}
			else {
				put(key, value);
			}
		}
	}
	
	/**
	 * Simple class to represent a month and year. 
	 * 
	 * @author Simon McGregor
	 *
	 */
	// TODO: move this out of here into a top-level class of its own somewhere useful.
	public static class MonthYear implements Comparable<MonthYear> {
		private int month;
		private int year;
		
		public MonthYear(Date date) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			this.month = calendar.get(Calendar.MONTH) + 1;
			this.year = calendar.get(Calendar.YEAR);
		}
		
		public MonthYear(int month, int year) {
			this.month = month;
			this.year = year;
					
		}
		
		/**
		 * Returns a new MonthYear which is a given number of months later than this one.
		 *  
		 * @param months The total number of months to add to this MonthYear
		 * @return A new MonthYear which is the specified number of months later.
		 */
		public MonthYear addMonths(int months) {
			int convenienceMonth = month - 1 + months;
			int newYear = year + convenienceMonth / 12;
			int newMonth = 1 + convenienceMonth % 12;
			return new MonthYear(newMonth, newYear);
		}
		
		public int getMonth() {
			return month;
		}
		
		public int getYear() {
			return year;
		}
		
		public boolean after(MonthYear my) {
			return compareTo(my) > 0;
		}

		public boolean before(MonthYear my) {
			return compareTo(my) < 0;
		}
		
		@Override
		// need to override equals and hashCode to provide sensible hashing behaviour
		public boolean equals(Object object) {
			if (object instanceof MonthYear) {
				MonthYear my = (MonthYear) object;
				return (month == my.getMonth()) && (year == my.getYear());
			}
			return false;
		}
		
		@Override
		// need to override equals and hashCode to provide sensible hashing behaviour
		public int hashCode() {
			int result = year * 100 + month; 
			return result;
		}

		@Override
		public int compareTo(MonthYear my) {
			return (month + year * 100) - (my.getMonth() + my.getYear() * 100);
		}
	}
	
	/**
	 * Simple tree class because Java utils inexplicably doesn't provide one
	 * @author Simon McGregor
	 *
	 */
	// TODO: move this out of here into a top-level class of its own somewhere useful.
	static class Node {
		public final String token;
		public final Node [] children;
		
		public Node(String token, Node [] children) {
			this.token = token;
			this.children = children;
		}
	}
	
	/**
	 * IExpression class for parser to produce raw tree objects
	 */
	static class NodeExpression implements IExpression<Node> {
		Node node;
		
		public NodeExpression(Node node) {
			this.node = node;
		}
		
		public NodeExpression(String token, Node [] children)  {
			this.node = new Node(token, children);
		}
		
		@Override
		public Node evaluate() {
			return node;
		}
	}
	
	
	/**
	 * Parser for price expressions returning a raw parse tree.
	 * NOTE: this class duplicates code in ISeriesParser and its ancestors
	 * so it will NOT automatically remain in synch. 
	 * 
	 * @author Simon McGregor
	 */
	static class RawTreeParser extends ExpressionParser<Node> {
		public RawTreeParser() {
			setInfixOperatorFactory(new IInfixOperatorFactory<Node>() {

				@Override
				public IExpression<Node> createInfixOperator(
						char operator, IExpression<Node> lhs,
						IExpression<Node> rhs) {
					Node [] children = { lhs.evaluate(), rhs.evaluate() }; 
					return new NodeExpression("" + operator, children);
				}

				@Override
				public boolean isOperatorHigherPriority(char a, char b) {
					if (a == b)
						return false;
					switch (a) {
					case '*':
						return true;
					case '/':
						return b == '+' || b == '-';
					case '+':
						return b == '-';
					case '-':
						return false;
					}
					return false;
				}

				@Override
				public boolean isInfixOperator(char operator) {
					return operator == '*' || operator == '/' || operator == '+' || operator == '-';
				}

			});

			setTermFactory(new ITermFactory<Node>() {

				@Override
				public IExpression<Node> createTerm(String term) {
					return new NodeExpression(term, new Node[0]);
				}
			});

			setFunctionFactory(new IFunctionFactory<Node>() {
				@Override
				public IExpression<Node> createFunction(String name, final List<IExpression<Node>> arguments) {
					Node [] children = new Node[arguments.size()];
					
					for (int i = 0; i < arguments.size(); i++) {						
						children[i] = arguments.get(i).evaluate();
					}
					
					return new NodeExpression(name, children);					
				}
			});

			setPrefixOperatorFactory(new IPrefixOperatorFactory<Node>() {
				@Override
				public boolean isPrefixOperator(char operator) {
					return false;
				}

				@Override
				public IExpression<Node> createPrefixOperator(char operator, IExpression<Node> argument) {
					throw new RuntimeException("Unknown prefix op " + operator);
				}
			});
		}
			
	}
	
	@SuppressWarnings("rawtypes")
	private static double getExposureCoefficient(Node node, Index index) {
		String indexToken = index.getName();
				
		if (node.token.equals("+")) {
			return getExposureCoefficient(node.children[0], index) + getExposureCoefficient(node.children[1], index); 
		}
		else if (node.token.equals("*")) {
			if (node.children[0].children.length > 0 || node.children[1].children.length > 0) {
				throw new RuntimeException("Expression too complex");
			}
			
			for (int i = 0; i < 2; i++) {
				if (node.children[i].token.equals(indexToken)) {
					return Double.parseDouble(node.children[1-i].token);				
				}
			}
			
		}
		else if (node.token.equals("-")) {
			return getExposureCoefficient(node.children[0], index) - getExposureCoefficient(node.children[1], index); 			
		}
		else if (node.token.equals("/")) {
			if (!node.children[0].token.equals(indexToken) || node.children[1].children.length > 0) {
				throw new RuntimeException("Expression too complex");
			}
			return 1 / Double.parseDouble(node.children[1].token);
		}
		else if (node.token.equals(indexToken)) {
			return 1;
		}
		
		return 0;
		
	}
	
	public static double getExposureCoefficient(String priceExpression, Index<?> index) {
		RawTreeParser parser = new RawTreeParser();
		try {
			IExpression<Node> parsed = parser.parse(priceExpression);
			return getExposureCoefficient(parsed.evaluate(), index);
		}
		catch (Exception e) {
			return 0;
		}
	}
	
	/**
	 * Determines the amount of exposure to a particular index which is created by a specific contract. 
	 * 
	 * @param contract
	 * @param index
	 * @return
	 */
	public static double getExposureCoefficient(Slot slot, Index<?> index) {
		String priceExpression = null;
		if (slot.isSetPriceExpression()) {
			priceExpression = slot.getPriceExpression();
		}
		else {
			LNGPriceCalculatorParameters parameters = null;
			Contract contract = slot.getContract();
			if (contract != null) {
				parameters = contract.getPriceInfo();
			}
			// do a case switch on contract class
			// TODO: refactor this into the actual contract classes?
			if (parameters instanceof IndexPriceParameters) {
				IndexPriceParameters ipc = (IndexPriceParameters) parameters;
				if (ipc.getIndex().equals(index)) {
					return ipc.getMultiplier(); 
				}
			}
			else if (parameters instanceof ExpressionPriceParameters) {
				ExpressionPriceParameters pec = (ExpressionPriceParameters) parameters;
				priceExpression = pec.getPriceExpression();
			}
		}
		
		if (priceExpression != null) {
			return getExposureCoefficient(priceExpression, index);
		}		
		
		return 0;
	}

	/**
	 * Calculates the exposure of a given schedule to a given index. Depends on the 
	 * getExposureCoefficient method to correctly determine the exposure per cubic metre
	 * associated with a load or discharge slot.
	 * 
	 * @param schedule
	 * @param index
	 * @return
	 */
	public static Map<MonthYear, Double> getExposuresByMonth(Schedule schedule, Index<?> index) {
		CumulativeMap<MonthYear> result = new CumulativeMap<MonthYear>();
		
		for (CargoAllocation allocation: schedule.getCargoAllocations()) {
			// calculate purchase and sales exposures separately
			int loadVolume = allocation.getLoadVolume();
			int dischargeVolume = allocation.getDischargeVolume();
			
			double purchaseExposureCoefficient = getExposureCoefficient(allocation.getLoadAllocation().getSlot(), index);
			double salesExposureCoefficient = getExposureCoefficient(allocation.getDischargeAllocation().getSlot(), index);
			
			// purchase is positive exposure, sales is negative
			double purchaseExposure = loadVolume * purchaseExposureCoefficient;
			double salesExposure = - dischargeVolume * salesExposureCoefficient; 
			
			// find the months associated with the sales and the purchase
			Date purchaseDate = allocation.getLoadAllocation().getSlotVisit().getStart();
			Date salesDate = allocation.getDischargeAllocation().getSlotVisit().getStart();

			// add the exposure figures into the totals per month
			result.plusEquals(new MonthYear(purchaseDate), purchaseExposure);
			result.plusEquals(new MonthYear(salesDate), salesExposure);

		}
		
		return result;
		
	}
}
