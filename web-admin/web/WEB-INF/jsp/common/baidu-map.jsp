<%@include file="/header.jsp"%>

<html>

<head>
	<meta charset="UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge;Chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>云聚物流 - 运维平台</title>
	<!-- jQuery & Bootstrap -->
	<script src="${ROOT}/js/jquery/jquery.min.js"></script>
	<script src="${ROOT}/js/jquery/jquery-migrate.min.js"></script>
	<link rel="stylesheet" href="${ROOT}/js/bootstrap/css/bootstrap.min.css"/>
	<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=5caeb86c33c4c856385e1e528784b567"></script>
</head>

<style type="text/css">
html{height: 100%}
body{height: 100%; margin: 0px; padding: 0px}
#divAddress
{
	position: absolute; color: #888888; top: 0px; height: 25px; width: 100%; text-align: center; border-bottom: solid 0px lightgray; font-size: 14px;
}
#divMap {position: absolute; top: 25px; left: 10px; right: 0px; bottom: 0px}
</style>

<body>
<div id='divAddress'></div>
<div id="divMap"></div> 
</body>

</html>

<script>
// TODO 添加手工输入智能快速搜索功能

var lng = -1, lat = -1, address = null;

// 初始化地图
var map = new BMap.Map("divMap");
map.enableScrollWheelZoom(true);
map.enableDragging();
map.addControl(new BMap.NavigationControl()); 
// 地图标记
var marker = new BMap.Marker(new BMap.Point(lng, lat));
map.addOverlay(marker);
// 地理信息解码器
var gc = new BMap.Geocoder();

/**
 * 初始化百度地图
 * 
 * @origLng				原经度
 * @origLat				原维度
 * @origAddress		原地址
 */
function init(origLng, origLat, origAddress)
{
	lng = origLng;
	lat = origLat;
	address = origAddress;
	$('#divAddress').html(address);

	// 地图点击选点
	map.addEventListener("click", function(e)
	{
		lng = e.point.lng;
		lat = e.point.lat;
		getLocation(e.point);

		marker.setPosition(e.point);
	});

	// alert(lng + ',' + lat);
	// 显示默认位置信息
	if (-1 != lng && -1 != lat)
	{
		// 显示当前位置信息
		$('#divAddress').html(address);

		var point = new BMap.Point(lng, lat);
		marker.setPosition(point);
		map.centerAndZoom(point, 15);
	}
	else
	{
		$('#divAddress').html('正在定位您的位置......');
		// 没有传递坐标，直接定位当前位置
		var geolocation = new BMap.Geolocation();
		geolocation.getCurrentPosition
		(
			function(r)
			{
				if (this.getStatus() == BMAP_STATUS_SUCCESS)
				{
					lng = r.point.lng;
					lat = r.point.lat;
					address = r.address.province + r.address.city + r.address.district + r.address.street;

					$('#divAddress').html(address);
					// map.setCenter(r.point);
					marker.setPosition(r.point);
					map.centerAndZoom(r.point, 15);
				}
				else
				{
					alert('定位失败，请手工选择地图位置！');
				}
			},
			{enableHighAccuracy: false}
		);
		//关于状态码
		//BMAP_STATUS_SUCCESS	检索成功。对应数值“0”。
		//BMAP_STATUS_CITY_LIST	城市列表。对应数值“1”。
		//BMAP_STATUS_UNKNOWN_LOCATION	位置结果未知。对应数值“2”。
		//BMAP_STATUS_UNKNOWN_ROUTE	导航结果未知。对应数值“3”。
		//BMAP_STATUS_INVALID_KEY	非法密钥。对应数值“4”。
		//BMAP_STATUS_INVALID_REQUEST	非法请求。对应数值“5”。
		//BMAP_STATUS_PERMISSION_DENIED	没有权限。对应数值“6”。(自 1.1 新增)
		//BMAP_STATUS_SERVICE_UNAVAILABLE	服务不可用。对应数值“7”。(自 1.1 新增)
		//BMAP_STATUS_TIMEOUT	超时。对应数值“8”。(自 1.1 新增)
	}
}

// 获取坐标点位置信息
function getLocation(point)
{
	gc.getLocation(point, function(rs)
	{
		var addr = rs.addressComponents;
    address = addr.province + addr.city + addr.district + addr.street + addr.streetNumber;
    $('#divAddress').html(address);
  });
}
</script>