package com.yfwang.panorama.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Area implements Serializable
{
	private Long ID;
	private String name;
	private String location;
	private String shortName;
	private String panaUrl;
	private String thumUrl;
	private List<Marker> markers = new ArrayList<Marker>();

	public List<Marker> getMarkers()
	{
		return markers;
	}

	public String getThumUrl()
	{
		return thumUrl;
	}

	public void setThumUrl(String thumUrl)
	{
		this.thumUrl = thumUrl;
	}

	public void setMarkers(List<Marker> markers)
	{
		this.markers = markers;
	}

	public String getPanaUrl()
	{
		return panaUrl;
	}

	public void setPanaUrl(String panaUrl)
	{
		this.panaUrl = panaUrl;
	}

	public Long getID()
	{
		return ID;
	}

	public void addMarker(Marker marker)
	{
		this.markers.add(marker);
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

	public String getLocation()
	{
		return location;
	}

	public void setLocation(String location)
	{
		this.location = location;
	}

	public String getShortName()
	{
		return shortName;
	}

	public void setShortName(String shortName)
	{
		this.shortName = shortName;
	}

}
