package com.mmxlabs.lingo.reports.customizable;

import org.junit.jupiter.api.Test;

public class JSONToXMLHelperTest {

	@Test
	public String testJsonToXML() {
		
		String jsonStr = "{'name':'JSON','integer':1,'double':2.0,'boolean':true,'nested':{'id':42},'array':[1,2,3]}";  
		
		String xmlStr = JSONToXMLHelper.convertToXml(jsonStr);
		
		System.out.println(xmlStr);
		
		return xmlStr;
	}
	
	@Test
	public void testXMLToJson() {
	
		String xmlStr = testJsonToXML();
		
		String jsonStr = JSONToXMLHelper.convertToJson(xmlStr);
		
		System.out.println(jsonStr);
	}
}
