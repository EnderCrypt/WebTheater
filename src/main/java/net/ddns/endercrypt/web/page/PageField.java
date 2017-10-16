package net.ddns.endercrypt.web.page;

/**
 * class representing a {field} inside a html in {@link PageBuild}
 * @author EnderCrypt
 */
public class PageField
{
	private int index;
	private String fieldName;
	private String fieldValue;

	public PageField(int index, String fieldName)
	{
		this.index = index;
		this.fieldName = fieldName;
	}

	public int getIndex()
	{
		return index;
	}

	public int getEndIndex()
	{
		return index + fieldName.length();
	}

	public String getFieldName()
	{
		return fieldName;
	}

	public void setFieldValue(String fieldValue)
	{
		this.fieldValue = fieldValue;
	}

	public String getFieldValue()
	{
		return fieldValue;
	}

	public boolean isModified()
	{
		return (getFieldValue() != null);
	}
}
