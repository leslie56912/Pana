package com.yfwang.panorama.client.util;

public class PanaUtil {

	public static native boolean isPC()/*-{
		return $wnd.isPC();
	}-*/;

	public static native void initPana(String url)/*-{
		$wnd.PSV = new $wnd.PhotoSphereViewer({
			container : 'photosphere',
			panorama : url,
			anim_speed : '2rpm',
			move_speed : 4.1,
			time_anim : false,
			latitude_range:[-3*Math.PI/4, Math.PI/9],
			gyroscope : true,
			webgl : true,
			loading_img : 'img/loading.gif',
			usexmpdata : false,
			navbar : [ 'markers', 'gyroscope', 'fullscreen' ],
			lang : {
				markers : "景点介绍",
			}
		});
		$wnd.PSV.on('panorama-loaded', function() {
			console.log('dd');
		});

		$wnd.PSV.on('ready', function() {
			$wnd.ready = true;
			@com.yfwang.panorama.client.util.PanaUtil::addMarkers()();
		});

		$wnd.PSV.on('click', function(e) {
			$wnd.chosenMarkerId = "";
		});

		$wnd.PSV.on('select-marker', function(marker, dblclick) {
			$wnd.chosenMarkerId = marker.id;
		});

	}-*/;

	public static native void clearMakers()/*-{
		$wnd.markers.splice(0, $wnd.markers.length);
		$wnd.PSV.clearMarkers();
	}-*/;

	public static native void swithPana(String url)/*-{
		if ($wnd.ready) {
			$wnd.ready = false;
			$wnd.PSV.setPanorama(url, {
				longitude : 0,
				latitude : 0
			}, true).then(function() {
				$wnd.ready = true;
				$wnd.addMarkers();
			});
		}
	}-*/;

	public static native void addMarkers()/*-{
		for (var i = 0; i < $wnd.markers.length; i++) {
			$wnd.PSV.addMarker($wnd.markers[i]);
		}
	}-*/;

	public static native void addMarker(String id, String name, String imgPath,
			String longtitude, String latitude, String tooltip)/*-{
		$wnd.markers.push({
			id : id,
			name : name,
			html : imgPath,
			longitude : longtitude,
			latitude : latitude,
			anchor : 'bottom center',
			tooltip : tooltip
		});
	}-*/;

	public static native void preload(String url)/*-{
		$wnd.PSV.preloadPanorama(url);
	}-*/;

	public static native boolean isViewReady()/*-{
		return $wnd.ready;
	}-*/;

	public static native void resetCurrentMarker()/*-{
		$wnd.chosenMarkerId = -1;
	}-*/;

	public static native void goToMaker(int i)/*-{
		$wnd.PSV.hud.gotoMarker($wnd.markers[i], 1000);
	}-*/;

	public static native String getChosenMarkerId()/*-{
		return $wnd.chosenMarkerId;
	}-*/;

	public static native void toggleGeo()/*-{
		if ($wnd.isGeoEnabled) {
			$wnd.PSV.stopGyroscopeControl();
			$wnd.isGeoEnabled = false;
		} else {
			$wnd.PSV.startGyroscopeControl();
			$wnd.isGeoEnabled = true;
		}
	}-*/;
}
