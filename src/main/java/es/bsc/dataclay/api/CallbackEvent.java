package es.bsc.dataclay.api;

/**
 * Class that represents an event for a callback that must be handled.
 * 
 */
public final class CallbackEvent {

	/**
	 * Event types.
	 */
	public enum EventType {
		/** Error. */
		FAIL,
		/** Ok. */
		SUCCESS;
	}

	/** Request ID. */
	private String requestID;

	/** Type of event. */
	private EventType type;

	/** Data related with the event. */
	private Object content;

	/** Class name of the method that produced this event. */
	private String classNameOfMethod;

	/** Message for error passing, etc. */
	private String message;

	/** Operation signature that produced the result. */
	private String operationSignature;

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            id of the execution request related to the callback event.
	 * @param eventType
	 *            type of the event.
	 * @param response
	 *            result obtained from the execution request.
	 * @param newclassNameOfMethod
	 *            name of the class where the original method that produces the response resides.
	 * @param newOpSignature
	 *            signature of the operation that produced this result
	 */
	public CallbackEvent(final String id, final EventType eventType, final Object response, final String newclassNameOfMethod,
			final String newOpSignature) {
		setRequestID(id);
		setType(eventType);
		setContent(response);
		setClassNameOfMethod(newclassNameOfMethod);
		setOperationSignature(newOpSignature);
	}

	/**
	 * Constructor for results different than SUCCESS.
	 * 
	 * @param id
	 *            id of the execution request related to the callback event.
	 * @param eventType
	 *            type of the event.
	 * @param newMessage
	 *            message of failure
	 */
	public CallbackEvent(final String id, final EventType eventType, final String newMessage) {
		setRequestID(id);
		setType(eventType);
		setMessage(newMessage);
	}

	/**
	 * Get the CallbackEvent::requestID
	 * 
	 * @return the requestID
	 */
	public String getRequestID() {
		return requestID;
	}

	/**
	 * Set the CallbackEvent::requestID
	 * 
	 * @param newrequestID
	 *            the requestID to set
	 */
	public void setRequestID(final String newrequestID) {
		if (newrequestID == null) {
			throw new IllegalArgumentException("requestID cannot be null");
		}
		this.requestID = newrequestID;
	}

	/**
	 * Get the CallbackEvent::type
	 * 
	 * @return the type
	 */
	public EventType getType() {
		return type;
	}

	/**
	 * Set the CallbackEvent::type
	 * 
	 * @param newtype
	 *            the type to set
	 */
	public void setType(final EventType newtype) {
		if (newtype == null) {
			throw new IllegalArgumentException("type cannot be null");
		}
		this.type = newtype;
	}

	/**
	 * Get the CallbackEvent::content
	 * 
	 * @return the content
	 */
	public Object getContent() {
		return content;
	}

	/**
	 * Set the CallbackEvent::content
	 * 
	 * @param newcontent
	 *            the content to set
	 */
	public void setContent(final Object newcontent) {
		this.content = newcontent;
	}

	/**
	 * Get the CallbackEvent::classNameOfMethod
	 * 
	 * @return the classNameOfMethod
	 */
	public String getClassNameOfMethod() {
		return classNameOfMethod;
	}

	/**
	 * Set the CallbackEvent::classNameOfMethod
	 * 
	 * @param newclassNameOfMethod
	 *            the classNameOfMethod to set
	 */
	public void setClassNameOfMethod(final String newclassNameOfMethod) {
		if (newclassNameOfMethod == null || newclassNameOfMethod.isEmpty()) {
			throw new IllegalArgumentException("classNameOfMethod cannot be null or empty");
		}
		this.classNameOfMethod = newclassNameOfMethod;
	}

	/**
	 * Get the CallbackEvent::message
	 * 
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Set the CallbackEvent::message
	 * 
	 * @param newmessage
	 *            the message to set
	 */
	public void setMessage(final String newmessage) {
		this.message = newmessage;
	}

	/**
	 * Get the CallbackEvent::operationSignature
	 * 
	 * @return the operationSignature
	 */
	public String getOperationSignature() {
		return operationSignature;
	}

	/**
	 * Set the CallbackEvent::operationSignature
	 * 
	 * @param newoperationSignature
	 *            the operationSignature to set
	 */
	public void setOperationSignature(final String newoperationSignature) {
		if (newoperationSignature == null) {
			throw new IllegalArgumentException("operationSignature cannot be null");
		}
		this.operationSignature = newoperationSignature;
	}
}
