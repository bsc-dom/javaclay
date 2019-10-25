
/**
 * @file GeneralException.java
 * 
 * @date Oct 22, 2012
 */

package es.bsc.dataclay.exceptions;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.IdentityHashMap;
import java.util.ListIterator;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.bsc.dataclay.DataClayObject;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;
import es.bsc.dataclay.serialization.DataClaySerializable;
import es.bsc.dataclay.serialization.buffer.DataClayByteBuffer;
import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.DataClayObjectMetaData;
import es.bsc.dataclay.util.ReferenceCounting;
import es.bsc.dataclay.util.ids.MetaClassID;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;

/**
 * This class represents the exceptions produced.
 */
public class DataClayException extends RuntimeException implements DataClaySerializable {

	private static final Logger logger = LogManager.getLogger("exceptions.DataClayException");

	/** Serial version UID. */
	private static final long serialVersionUID = -8540632314951324280L;

	/** Exception message. */
	private String exceptionMessage;

	/** Exception code. By default is a generic Language exception. */
	private ERRORCODE errorCode = ERRORCODE.UNKNOWN_EXCEPTION;

	/**
	 * Empty constructor used for deserialization.
	 */
	public DataClayException() {
		super();
	}

	public DataClayException(Throwable e) {
		super(e);
		this.exceptionMessage = this.getCause().getMessage();
		if (e instanceof DataClayException) {
			this.errorCode = ((DataClayException) e).getErrorcode();
		}
	}

	/**
	 * DataClayException constructor
	 * 
	 * @param theerrorCode
	 *            The error code
	 */
	public DataClayException(final ERRORCODE theerrorCode) {
		super("DataClayException produced with error code: " + theerrorCode.name());
		this.exceptionMessage = this.getMessage();
		this.setErrorCode(theerrorCode);
	}

	/**
	 * DataClayException constructor
	 * 
	 * @param theerrorCode
	 *            The error code
	 * @param theexceptionMessage
	 *            The exception message
	 * @param verbose
	 *            whether to pass cause of the exception or not
	 */
	public DataClayException(final ERRORCODE theerrorCode, final String theexceptionMessage, final boolean verbose) {
		super(theexceptionMessage);
		this.setErrorCode(theerrorCode);
	}

	/**
	 * Conversion from DataclayException to StatusRuntimeException for GRPC
	 * 
	 * @return the representation of this DataClayException in terms of GRPC
	 */
	public StatusRuntimeException asGrpcException() {
		final StatusRuntimeException st = Status.INTERNAL
				.withDescription("CODE: " + this.getErrorcode() + "\nMSG: " + this.getLocalizedMessage())
				.asRuntimeException();
		return st;
	}

	@Override
	public final String getLocalizedMessage() {
		return exceptionMessage;
	}

	/**
	 * Set the DataClayException::exceptionMessage
	 * 
	 * @param newexceptionMessage
	 *            the exceptionMessage to set
	 */
	public final void setExceptionMessage(final String newexceptionMessage) {
		if (newexceptionMessage == null) {
			throw new IllegalArgumentException("Exception Message cannot be null");
		}
		this.exceptionMessage = newexceptionMessage;

	}

	/**
	 * Get the DataClayException::errorCode
	 * 
	 * @return the errorCode
	 */
	public final ERRORCODE getErrorcode() {
		return errorCode;
	}

	/**
	 * Set the DataClayException::errorCode
	 * 
	 * @param newerrorCode
	 *            the errorCode to set
	 */
	public final void setErrorCode(final ERRORCODE newerrorCode) {
		if (newerrorCode == null) {
			throw new IllegalArgumentException("ErrorCode cannot be null");
		}
		this.errorCode = newerrorCode;
	}

	/**
	 * Print stack trace
	 */
	@Override
	public final void printStackTrace() {
		super.printStackTrace();
	}

	@Override
	public final int hashCode() {
		return this.errorCode.hashCode();
	}

	@Override
	public final boolean equals(final Object candidate) {
		if (candidate instanceof DataClayException) {
			final DataClayException candidateExc = (DataClayException) candidate;
			return this.getErrorcode().equals(candidateExc.getErrorcode());
		} else {
			return false;
		}
	}

	// CHECKSTYLE:OFF
	@Override
	public void serialize(final DataClayByteBuffer dcBuffer, final boolean ignoreUserTypes,
			final Map<MetaClassID, byte[]> ifaceBitMaps, final IdentityHashMap<Object, Integer> curSerializedObjs,
			final ListIterator<DataClayObject> pendingObjs, ReferenceCounting referenceCounting) {

		// Serialize DataClay exception using Java serialization mechanism. This can be
		// slow but
		// we guarantee that all the exception is properly serialized (Inspired by
		// JavaExecutionException code).

		final ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		try {
			out = new ObjectOutputStream(bos);
			if (this.getCause() == null) {
				out.writeObject(this);
			} else {
				out.writeObject(this.getCause());
			}
			out.flush();
			final byte[] yourBytes = bos.toByteArray();

			// Serialize error code
			dcBuffer.writeInt(this.errorCode.ordinal());

			// Serialize bytes
			dcBuffer.writeByteArray(yourBytes);

		} catch (final IOException ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (final IOException ex) {
				// ignore close exception
				ex.printStackTrace();
			}
			try {
				bos.close();
			} catch (final IOException ex) {
				// ignore close exception
				ex.printStackTrace();
			}
		}

	}

	@Override
	public void deserialize(final DataClayByteBuffer dcByteBuffer, final Map<MetaClassID, byte[]> ifaceBitMaps,
			final DataClayObjectMetaData metadata, final Map<Integer, Object> curDeserializedJavaObjs) {

		// Deserialize error code
		this.errorCode = ERRORCODE.values()[dcByteBuffer.readInt()];

		// Read bytes
		final byte[] bytes = dcByteBuffer.readByteArray();

		final ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		ObjectInput in = null;
		RuntimeException re = null;
		try {
			in = new ObjectInputStream(bis);
			Object o = in.readObject();
			re = (RuntimeException) o;

			// Deserialize exception
			this.initCause(re);

		} catch (final Exception ex) {
			throw new RuntimeException(ex);

		} finally {
			try {
				bis.close();
			} catch (final IOException ex) {
				// ignore close exception
			}
			try {
				if (in != null) {
					in.close();
				}
			} catch (final IOException ex) {
				// ignore close exception
			}
		}

	}

}
