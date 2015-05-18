package edu.rosehulman.testing;

import java.util.HashMap;
import java.util.Set;



public class Tests {

	public static void main(String[] args) {

		HashMap<String, Integer> map = new HashMap<String, Integer>();
		
		map.put("a", 1);
		map.put("b", 1);
		map.put("c", 1);
		map.put("d", 1);
		map.put("e", 1);
		map.put("f", 1);
		
		Set<String> keys = map.keySet();
		for(String key : keys){
			map.remove(key);
		}
		
	}
}
