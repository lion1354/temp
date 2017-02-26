package com.tibco.ma.model.core;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bson.Document;

public class MaGeoPoint implements MaDataType {
	
	public static final String LATITUDE = "latitude";
	public static final String LONGITUDE = "longitude";
	
	static double EARTH_MEAN_RADIUS_KM = 6371.0D;
	static double EARTH_MEAN_RADIUS_MILE = 3958.8000000000002D;

	private double latitude = 0.0D;
	private double longitude = 0.0D;

	public MaGeoPoint(double latitude, double longitude) {
		setLatitude(latitude);
		setLongitude(longitude);
	}

	public MaGeoPoint(Document document) {
		setLatitude(document.get("latitude"));
		setLongitude(document.get("longitude"));
	}

	/**
	 * Set latitude. Valid range is (-90.0, 90.0). Extremes should not be used.
	 */
	private void setLatitude(Object latitudeObj) {
		double latitude = 0.0D;
		if (latitudeObj instanceof String) {
			latitude = Double.parseDouble((String) latitudeObj);
		} else if (latitudeObj instanceof Double) {
			latitude = (Double) latitudeObj;
		}

		if ((latitude > 90.0D) || (latitude < -90.0D)) {
			throw new IllegalArgumentException(
					"Latitude must be within the range (-90.0, 90.0).");
		}
		this.latitude = latitude;
	}

	/**
	 * Set longitude. Valid range is (-180.0, 180.0). Extremes should not be
	 * used.
	 */
	private void setLongitude(Object longitudeObj) {
		double longitude = 0.0D;
		if (longitudeObj instanceof String) {
			longitude = Double.parseDouble((String) longitudeObj);
		} else if (longitudeObj instanceof Double) {
			longitude = (Double) longitudeObj;
		}

		if ((longitude > 180.0D) || (longitude < -180.0D)) {
			throw new IllegalArgumentException(
					"Longitude must be within the range (-180.0, 180.0).");
		}
		this.longitude = longitude;
	}

	public double getLatitude() {
		return this.latitude;
	}

	public double getLongitude() {
		return this.longitude;
	}

	/**
	 * Get distance in radians between this point and another ParseGeoPoint.
	 * This is the smallest angular distance between the two points.
	 */
	public double distanceInRadiansTo(MaGeoPoint point) {
		double d2r = 0.0174532925199433D;
		double lat1rad = this.latitude * d2r;
		double long1rad = this.longitude * d2r;
		double lat2rad = point.getLatitude() * d2r;
		double long2rad = point.getLongitude() * d2r;
		double deltaLat = lat1rad - lat2rad;
		double deltaLong = long1rad - long2rad;
		double sinDeltaLatDiv2 = Math.sin(deltaLat / 2.0D);
		double sinDeltaLongDiv2 = Math.sin(deltaLong / 2.0D);

		double a = sinDeltaLatDiv2 * sinDeltaLatDiv2 + Math.cos(lat1rad)
				* Math.cos(lat2rad) * sinDeltaLongDiv2 * sinDeltaLongDiv2;

		a = Math.min(1.0D, a);
		return 2.0D * Math.asin(Math.sqrt(a));
	}

	/**
	 * Get distance between this point and another ParseGeoPoint in kilometers.
	 */
	public double distanceInKilometersTo(MaGeoPoint point) {
		return distanceInRadiansTo(point) * EARTH_MEAN_RADIUS_KM;
	}

	/**
	 * Get distance between this point and another ParseGeoPoint in miles.
	 */
	public double distanceInMilesTo(MaGeoPoint point) {
		return distanceInRadiansTo(point) * EARTH_MEAN_RADIUS_MILE;
	}

	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put(LATITUDE, latitude);
		map.put(LONGITUDE, longitude);
		map.put(KEY_TYPE, EntityColType.GeoPoint.name());
		return map;
	}
}
