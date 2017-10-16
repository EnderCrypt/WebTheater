package net.ddns.endercrypt.web.page;

import java.io.IOException;

/**
 * basic static convinience class for easily creating {@link PageBuild} of many html files
 * @author EnderCrypt
 */
public class Pages
{
	private final static String root = "data/pages/";

	// FRONT //
	public static PageBuild index() throws IOException
	{
		return new PageBuild(root + "index.html");
	}

	public static PageBuild theater() throws IOException
	{
		return new PageBuild(root + "theater.html");
	}

}
