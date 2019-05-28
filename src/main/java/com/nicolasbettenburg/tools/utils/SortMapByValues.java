package com.nicolasbettenburg.tools.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class SortMapByValues {
	
	private static final class ValueComparator<V extends Comparable<? super V>>
	implements Comparator<Map.Entry<?, V>> {
public int compare(Map.Entry<?, V> o1, Map.Entry<?, V> o2) {
	return -1 * o1.getValue().compareTo(o2.getValue());
}
}
  
  @SuppressWarnings("rawtypes")
private static final Comparator SINGLE = new ValueComparator();
  
  @SuppressWarnings({ "rawtypes", "unchecked" })
public static <K, V extends Comparable<? super V>> List<K> getKeysSortedByValue2(
			Map<K, V> map) {
		final int size = map.size();
		final List reusedList = new ArrayList(size);
		final List<Map.Entry<K, V>> meView = reusedList;
		meView.addAll(map.entrySet());
		Collections.sort(meView, SINGLE);
		final List<K> keyView = reusedList;
		for (int i = 0; i < size; i++) {
			keyView.set(i, meView.get(i).getKey());
		}
		return keyView;
	}

}
