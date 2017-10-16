package net.ddns.endercrypt.web.page;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.google.common.io.Files;

import net.ddns.endercrypt.web.page.exception.PageFieldNotFound;
import net.ddns.endercrypt.web.page.exception.PageFieldNotModified;

public class PageBuild
{
	private static final char FIELD_PREFIX = '[';
	private static final char FIELD_SUFIX = ']';

	private File file;
	private String html;
	private List<PageField> fields = new ArrayList<>();

	public PageBuild(String file) throws IOException
	{
		this(new File(file));
	}

	public PageBuild(File file) throws IOException
	{
		this.file = file;
		html = Files.toString(file, Charset.defaultCharset());
		// find all fields
		int i = 0;
		while (i < html.length())
		{
			int start = html.indexOf(FIELD_PREFIX, i);
			if (start == -1)
			{
				break;
			}
			int end = html.indexOf(FIELD_SUFIX, i);
			if (end == -1)
			{
				throw new RuntimeException("missing " + FIELD_SUFIX + " expected to close " + FIELD_PREFIX + " at " + start + " in file " + file);
			}
			i = end + 1;
			String field = html.substring(start + 1, end);
			// add field
			fields.add(new PageField(start, field));
		}
	}

	public void setField(String field, int value)
	{
		setField(field, String.valueOf(value));
	}

	public void setField(String field, double value)
	{
		setField(field, String.valueOf(value));
	}

	public void setField(String field, String value)
	{
		boolean stateChanged = false;
		for (PageField pageField : fields)
		{
			if (pageField.getFieldName().equals(field))
			{
				stateChanged = true;
				pageField.setFieldValue(value);
			}
		}
		if (stateChanged == false)
		{
			throw new PageFieldNotFound("could not find field " + field + " inside " + file);
		}
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		int from = 0;
		for (PageField pageField : fields)
		{
			if (pageField.isModified() == false)
			{
				throw new PageFieldNotModified(FIELD_PREFIX + pageField.getFieldName() + FIELD_SUFIX + " has not been modified for file " + file);
			}
			sb.append(html, from, pageField.getIndex());
			sb.append(pageField.getFieldValue());
			from = pageField.getEndIndex() + 2;
		}
		sb.append(html, from, html.length());
		return sb.toString();
	}
}
