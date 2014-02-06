package fr.acensi.jiraExport.exception;

public class JiraExportException extends Exception {

	private String message ;
	/**
	 * 
	 */
	private static final long serialVersionUID = 7829524691151293992L;
	
	public JiraExportException(Exception e) {
		message = e.getMessage();
	}

	
	public String getMessage() {
		return message;
	}

	
}
