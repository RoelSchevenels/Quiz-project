/**
 * simple inteface
 * @author vrolijkx
 */
package Protocol;

import Protocol.requests.Request;


public interface RequestListener {
	public void handleRequest(Request r);
}
