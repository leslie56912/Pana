package com.yfwang.panorama.shared;

import java.io.Serializable;

public class Marker implements Serializable
{
	private Long ID;
	private Long goingAreaID;
	private String name;
	private String tooltip;
	private String location;
	private String iconPath;
	private Double latitude;
	private Double lontitude;
	private String introduction;
	private boolean isShowMsg;

	public String getIntroduction()
	{
		return introduction;
	}

	public void setIntroduction(String introduction)
	{
		this.introduction = introduction;
	}

	public Long getGoingAreaID()
	{
		return goingAreaID;
	}

	public void setGoingAreaID(Long goingAreaID)
	{
		this.goingAreaID = goingAreaID;
	}

	public boolean isShowMsg()
	{
		return isShowMsg;
	}

	public void setShowMsg(boolean isShowMsg)
	{
		this.isShowMsg = isShowMsg;
	}

	public Long getID()
	{
		return ID;
	}

	public void setID(Long iD)
	{
		ID = iD;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getTooltip()
	{
		return tooltip;
	}

	public void setTooltip(String tooltip)
	{
		this.tooltip = tooltip;
	}

	public String getLocation()
	{
		return location;
	}

	public void setLocation(String location)
	{
		this.location = location;
	}

	public String getIconPath()
	{
		return iconPath;
	}

	public void setIconPath(String iconPath)
	{
		this.iconPath = iconPath;
	}

	public Double getLatitude()
	{
		return latitude;
	}

	public void setLatitude(Double latitude)
	{
		this.latitude = latitude;
	}

	public Double getLontitude()
	{
		return lontitude;
	}

	public void setLontitude(Double lontitude)
	{
		this.lontitude = lontitude;
	}

}
