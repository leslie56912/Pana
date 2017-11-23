package com.yfwang.panorama.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Attraction implements Serializable
{

	private Long ID;
	private String name;
	private String address;
	private String QRURL;
	private List<Area> areas = new ArrayList<Area>();

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

	public void addArea(Area area)
	{
		this.areas.add(area);
	}

	public String getAddress()
	{
		return address;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}

	public String getQRURL()
	{
		return QRURL;
	}

	public void setQRURL(String qRURL)
	{
		QRURL = qRURL;
	}

	public List<Area> getAreas()
	{
		return areas;
	}

	public void setAreas(List<Area> areas)
	{
		this.areas = areas;
	}
}
