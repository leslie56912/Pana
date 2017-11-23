package com.yfwang.panorama.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.yfwang.panorama.client.GreetingService;
import com.yfwang.panorama.shared.Area;
import com.yfwang.panorama.shared.Attraction;
import com.yfwang.panorama.shared.FieldVerifier;
import com.yfwang.panorama.shared.Marker;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService
{

	@Override
	public Attraction findAttraction(Long attractionID) throws IllegalArgumentException
	{
		DBHelper dbHelper = new DBHelper();
		Attraction attraction = new Attraction();
		try
		{
			PreparedStatement prepareStatement;
			Connection connection = dbHelper.getConnection();

			prepareStatement = connection.prepareStatement("select ID,NAME,ADDRESS,QRURL from ATTRACTION where ID=" + attractionID);
			ResultSet resultSet = prepareStatement.executeQuery();
			if (resultSet.next())
			{

				attraction.setID(resultSet.getLong(1));
				attraction.setName(resultSet.getString(2));
				attraction.setAddress(resultSet.getString(3));
				attraction.setQRURL(resultSet.getString(4));
			}
			prepareStatement = connection
					.prepareStatement("select  a.ID,a.NAME,a.LOCATION,a.SHORT_NAME,a.PANA_URL,a.THUM_URL,m.ID,m.NAME,m.INTRO,m.LANTITUDE,m.LONGTITUDE,m.IS_SHOW_MSG,m.GOING_AREA_ID from AREA a left join MARKER m on a.ID=m.AREA_ID where ATTRACTION_ID="
							+ attractionID + " order by a.ID");
			resultSet = prepareStatement.executeQuery();
			Long areaID = -1L;
			Area area = null;
			while (resultSet.next())
			{
				long gotAreaID = resultSet.getLong(1);

				if (gotAreaID != areaID)
				{
					area = new Area();
					areaID = gotAreaID;
					area.setID(areaID);
					area.setLocation(resultSet.getString(3));
					area.setName(resultSet.getString(2));
					area.setShortName(resultSet.getString(4));
					area.setPanaUrl(resultSet.getString(5));
					area.setThumUrl(resultSet.getString(6));
					attraction.addArea(area);
				}

				Marker marker = new Marker();
				marker.setID(resultSet.getLong(7));
				marker.setName(resultSet.getString(8));
				marker.setIntroduction(resultSet.getString(9));
				marker.setLatitude(resultSet.getDouble(10));
				marker.setLontitude(resultSet.getDouble(11));
				marker.setShowMsg(resultSet.getBoolean(12));
				marker.setGoingAreaID(resultSet.getLong(13));
				area.addMarker(marker);
			}

		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
		finally
		{
			dbHelper.close();
		}
		return attraction;
	}

}
