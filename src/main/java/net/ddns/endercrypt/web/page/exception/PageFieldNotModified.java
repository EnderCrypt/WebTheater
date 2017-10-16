package net.ddns.endercrypt.web.page.exception;

/**
 * exception that occurs when attempting to build a page with un-changed fields
 * @author EnderCrypt
 */
@SuppressWarnings("serial")
public class PageFieldNotModified extends RuntimeException
{
	public PageFieldNotModified()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public PageFieldNotModified(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public PageFieldNotModified(String message, Throwable cause)
	{
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public PageFieldNotModified(String message)
	{
		super(message);
		// TODO Auto-generated constructor stub
	}

	public PageFieldNotModified(Throwable cause)
	{
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
