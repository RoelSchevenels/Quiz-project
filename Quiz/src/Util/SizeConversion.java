/**
 * voor Sizes human readable weer te geven
 * @author vrolijkx
 */
package Util;

public class SizeConversion {
	/**
	 * makes the size human readable like "2GB"
	 * @param size the size in bytes to convert
	 * @param precision the amount of digit after the comma.
	 * @return string that 
	 */
	public static String makeReadable(Long bytes,int precision,boolean si) {
		int unit = si ? 1000 : 1024;
	    if (bytes < unit) {
	    	return bytes + " B";
	    }
	    int exp = (int) (Math.log(bytes) / Math.log(unit));
	    String size = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
	    
	    
	    String format = precision > 1 ? String.format("%%.%df %%sB",precision) : "%.1f %sB";
	    
	    return String.format(format, bytes / Math.pow(unit, exp), size);
	}
	
	public static String makeReadable(Long bytes) {
		return makeReadable(bytes,2,true);
	}
	
	

}
