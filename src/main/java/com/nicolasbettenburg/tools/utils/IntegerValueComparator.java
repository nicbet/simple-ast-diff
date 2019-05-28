package com.nicolasbettenburg.tools.utils;

import java.util.Comparator;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class IntegerValueComparator implements Comparator {

	  Map base;
	  public IntegerValueComparator(Map base) {
	      this.base = base;
	  }

	  public int compare(Object a, Object b) {

	    if((Integer)base.get(a) < (Integer)base.get(b)) {
	      return 1;
	    } else if((Integer)base.get(a) == (Integer)base.get(b)) {
	      return 0;
	    } else {
	      return -1;
	    }
	  }
	}