package controller;

/**
*	A loading, callback interface.
*
*	@author Johan
*/
public interface ILoader {
	
	/**
	 * Called when the operation is completed, with or without errors.
	 */
	public void complete();
	
	/**
	 * Called on success only.
	 */
	public void success();
	
	/**
	 * Called on error
	 */
	public void error();
}
