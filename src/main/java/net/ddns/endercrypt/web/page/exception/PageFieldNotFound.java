package net.ddns.endercrypt.web.page.exception;

/**
 * exception when attempting to {@link PageBuild#setField(String, String)} on a key that doesent exist on the loaded page 
 * @author EnderCrypt
 */
@SuppressWarnings("serial")
public class PageFieldNotFound extends RuntimeException
{
	public PageFieldNotFound()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public PageFieldNotFound(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public PageFieldNotFound(String message, Throwable cause)
	{
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public PageFieldNotFound(String message)
	{
		super(message);
		// TODO Auto-generated constructor stub
	}

	public PageFieldNotFound(Throwable cause)
	{
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
