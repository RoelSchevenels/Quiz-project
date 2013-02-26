/**
 * zorgen dat een submit op de juiste plaats terecht komt
 * @author vrolijkx
 */
package Protocol;

import java.util.HashMap;

import Protocol.submits.Submit;

public class SubmitManager {
	private static HashMap<String,SubmitListener> listeners = new HashMap<String, SubmitListener>();
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void addSubmitListener(Class SubmitClass,SubmitListener listener) {
		if(!SubmitClass.isAssignableFrom(Submit.class) || SubmitClass.equals(Submit.class)) {
			throw new IllegalArgumentException("the given class must be an extend on Submit");
		}
		
		listeners.put(SubmitClass.getName(), listener);
	}
	
	public static void fireSubmit(Submit s) {
		String name = s.getClass().getName();
		SubmitListener listenener = listeners.get(name);
		if(listenener != null) {
			listenener.handleSubmit(s);
		}
	}
	
	public static void removeSubmitListener(SubmitListener listener) {
		for(String s: listeners.keySet()) {
			if(listeners.get(s).equals(listener)) {
				listeners.remove(s);
			}
		}
	}
	
	public static void removeSubmitListener(Class submitClass) {
		listeners.remove(submitClass.getName());
	}
}
