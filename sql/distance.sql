SELECT ST_Distance(
	/*--ST_Point(lon,lat)::geography
	--киев
    -- ST_Point(30.235748,50.584981)::geography,
	--львов
	 */
	ST_Point(24.0166667,49.85)::geography,

     ST_Point(30.7333333,46.4666667)::geography
) as distance;