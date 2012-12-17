package com.mmxlabs.shiplingo.platform.reports.views;

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
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.IndexPriceContract;
import com.mmxlabs.models.lng.commercial.PriceExpressionContract;
import com.mmxlabs.models.lng.commercial.ProfitSharePurchaseContract;
import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.DerivedIndex;
import com.mmxlabs.models.lng.pricing.Index;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.mmxcore.MMXRootObject;

/**
 * @since 2.0
 */
public class Exposures {
	static class Node {
		public final String token;
		public final Node [] children;
		
		public Node(String token, Node [] children) {
			this.token = token;
			this.children = children;
		}
	}
	
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
			// TODO Auto-generated method stub
			return node;
		}
	}
	
	static class TokenParser extends ExpressionParser<Node> {
		public TokenParser() {
			setInfixOperatorFactory(new IInfixOperatorFactory<Node>() {

				@Override
				public IExpression<Node> createInfixOperator(
						char operator, IExpression<Node> lhs,
						IExpression<Node> rhs) {
					// TODO Auto-generated method stub
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
					// TODO Auto-generated method stub
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
	public static double getExposureCoefficient(Node node, Index index) {
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
	
	/**
	 * Determines the amount of exposure to a particular index which is created by a specific contract 
	 * 
	 * @param contract
	 * @param index
	 * @return
	 */
	public static double getExposureCoefficient(Slot slot, Index index) {
		String priceExpression = null;
		if (slot.isSetPriceExpression()) {
			priceExpression = slot.getPriceExpression();
		}
		else {
			Contract contract = slot.getContract(); 
			// do a case switch on contract class
			// TODO: refactor this into the actual contract classes?
			if (contract instanceof IndexPriceContract) {
				IndexPriceContract ipc = (IndexPriceContract) contract;
				if (ipc.getIndex().equals(index)) {
					return ipc.getMultiplier(); 
				}
			}
			else if (contract instanceof ProfitSharePurchaseContract) {
				// don't know how these work
			}
			else if (contract instanceof PriceExpressionContract) {
				PriceExpressionContract pec = (PriceExpressionContract) contract;
				priceExpression = pec.getPriceExpression();
			}
		}
		
		if (priceExpression != null) {
			TokenParser parser = new TokenParser();
			IExpression<Node> parsed = parser.parse(priceExpression);
			return getExposureCoefficient(parsed.evaluate(), index);
		}		
		
		return 0;
	}

	public static Map<Date, Double> getExposuresByMonth(Schedule schedule, Index index) {
		HashMap<Date, Double> result = new HashMap<Date, Double>();
		
		for (CargoAllocation allocation: schedule.getCargoAllocations()) {
			int loadVolume = allocation.getLoadVolume();
			int dischargeVolume = allocation.getDischargeVolume();
			
			Slot loadSlot = allocation.getLoadAllocation().getSlot();
			Slot dischargeSlot = allocation.getDischargeAllocation().getSlot();
	
			double purchaseExposureCoefficient = getExposureCoefficient(loadSlot, index);
			double salesExposureCoefficient = getExposureCoefficient(dischargeSlot, index);
			
			Date purchaseMonth = allocation.getLoadAllocation().getSlotVisit().getStart();
			Date salesMonth = allocation.getDischargeAllocation().getSlotVisit().getStart();
			
			purchaseMonth = new Date(purchaseMonth.getYear(), purchaseMonth.getMonth(), 1);
			salesMonth = new Date(salesMonth.getYear(), salesMonth.getMonth(), 1);
			
			double purchaseExposure = loadVolume * purchaseExposureCoefficient;
			double salesExposure = - dischargeVolume * salesExposureCoefficient; 
			
			if (result.containsKey(purchaseMonth)) {
				result.put(purchaseMonth, result.get(purchaseMonth) + purchaseExposure);
			}
			else {
				result.put(purchaseMonth, purchaseExposure);				
			}

			if (result.containsKey(salesMonth)) {
				result.put(salesMonth, result.get(salesMonth) + salesExposure);
			}
			else {
				result.put(salesMonth, salesExposure);				
			}

		}
		
		return result;
		
	}
}
