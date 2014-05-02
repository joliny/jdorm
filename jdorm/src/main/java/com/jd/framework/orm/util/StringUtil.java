package com.jd.framework.orm.util;

import java.util.Collection;

public class StringUtil {
	public static boolean isNullOrEmpty(Object paramObject){
	    return (paramObject == null) || ("".equals(paramObject.toString()))||(paramObject.equals("null")||paramObject.toString().trim().equals(""));
	  }

	 public static String toString(Object paramObject){
	    if (paramObject == null)
	      return "null";
	    return paramObject.toString();
	  }

	  public static String join(Collection paramCollection, String paramString)
	  {
	    StringBuffer localStringBuffer = new StringBuffer();
	    //paramCollection = (Collection) paramCollection.iterator();
	    while (paramCollection.iterator().hasNext())
	    {
	      localStringBuffer.append("'"+paramCollection.iterator().next()+"'");
	      if (!paramCollection.iterator().hasNext())
	        continue;
	      localStringBuffer.append(paramString);
	    }
	    return localStringBuffer.toString();
	  }

	  

	  public static boolean isInteger(String paramString){
	    return true;
	  }
}
