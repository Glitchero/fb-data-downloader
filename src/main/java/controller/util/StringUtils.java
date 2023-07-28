package controller.util;


public class StringUtils {
	   public static String stripQuotation(String s) {
	      if(s.startsWith("\"")) {
	         s = s.substring(1, s.length());
	      }
	      if(s.endsWith("\"")) {
	         s = s.substring(0, s.length()-1);
	      }
	      return s;
	   }

	   public static boolean isEmpty(String text) {
	      return text == null || text.equals("");
	   }

	   public static String cleanup(String text){
	      String[] parts = text.replaceAll("[^a-zA-Z0-9 ]", "").toLowerCase().split("\\s+");
	      StringBuilder sb = new StringBuilder();
	      for(String p : parts) {
	         sb.append(p);
	      }

	      return sb.toString();
	   }
	}