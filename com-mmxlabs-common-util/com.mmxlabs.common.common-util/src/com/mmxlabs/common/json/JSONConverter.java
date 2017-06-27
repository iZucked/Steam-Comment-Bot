package com.mmxlabs.common.json;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class JSONConverter {
	
	public static Map<String, Object> jsonToMap(JSONObject json) {
	    Map<String, Object> retMap = new HashMap<String, Object>();

	    if(json.isEmpty()) {
	        retMap = toMap(json);
	    }
	    return retMap;
	}

	public static Map<String, Object> toMap(JSONObject object) {
	    Map<String, Object> map = new HashMap<String, Object>();

	    Iterator<Object> keysItr = object.keySet().iterator();
	    while(keysItr.hasNext()) {
	        String key = (String)keysItr.next();
	        Object value = object.get(key);

	        if(value instanceof JSONArray) {
//	            value = toList((JSONArray) value);
	        	value = (JSONArray) value;
	        }

	        else if(value instanceof JSONObject) {
	            value = toMap((JSONObject) value);
	        }
	        map.put(key, value);
	    }
	    return map;
	}

//	public static List<Object> toList(JSONArray array) {
//	    List<Object> list = new ArrayList<Object>();
//	    for(int i = 0; i < array.size(); i++) {
//	        Object value = array.get(i);
//	        if(value instanceof JSONArray) {
//	            value = toList((JSONArray) value);
//	        }
//
//	        else if(value instanceof JSONObject) {
//	            value = toMap((JSONObject) value);
//	        }
//	        list.add(value);
//	    }
//	    return list;
//	}
}
