/* ympc
   (c) 2013-2014 Andrew Karpow <andy@ndyk.de>
   (c) 2014 Christian Krafft <parabelboi@gmail.com>
   This project's homepage is: http://www.ympd.org
   
   This program is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; version 2 of the License.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License along
   with this program; if not, write to the Free Software Foundation, Inc.,
   Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

var socket;
var reg_socket;
var last_state;
var current_app;

$(document).ready(function() {
	webSocketConnect();
	if (!notificationsSupported())
		$('#btnnotify').addClass("disabled");
	else if ($.cookie("notification") === "true")
		$('#btnnotify').addClass("active")
});

function webSocketConnect() {
	if (typeof MozWebSocket != "undefined") {
		socket = new MozWebSocket(get_url());
	} else {
		socket = new WebSocket(get_url());
	}

	try {
		socket.onopen = function() {
			console.log("connected to ympc");
			$('.top-right').notify({
				message : {
					text : "Connected to ympc daemon"
				},
				fadeOut : {
					enabled : true,
					delay : 500
				}
			}).show();
			console.log("requesting service url");
			socket.send('REG_API_GET_URL');			
		}

		socket.onmessage = function got_packet(msg) {

			if (msg.data === last_state || msg.data.length == 0)
				return;

			var obj = JSON.parse(msg.data);

			switch (obj.type) {
			case "state":
				if (JSON.stringify(obj) === JSON.stringify(last_state))
					break;

				last_state = obj;
				break;
			case "disconnected":
				if ($('.top-right').has('div').length == 0)
					$('.top-right').notify({
						message : {
							text : "ympc lost connection to service registry "
						},
						type : "danger",
						fadeOut : {
							enabled : true,
							delay : 1000
						},
					}).show();
				break;
			case "url":
				$('#url').val(obj.data.url);
				bootstrapServices(obj.data.url);
				break;
			case "error":
				$('.top-right').notify({
					message : {
						text : obj.data
					},
					type : "danger",
				}).show();
			default:
				break;
			}
		}

		socket.onclose = function() {
			console.log("disconnected from ympc");
			$('.top-right').notify({
				message : {
					text : "Connection to ympc lost, retrying in 3 seconds "
				},
				type : "danger",
				onClose : function() {
					webSocketConnect();
				}
			}).show();
		}

	} catch (exception) {
		alert('<p>Error' + exception);
	}

}

function bootstrapServices(url) {
	console.log("connecting to service url: " + url);
	try {
		OData.read(url, function(data) {
			console.log("receiving results");
			var html = "";
			$.each(data.results, function(e) {
				html += "<tr><td>" + e.Id + "</td><td>" + e.Name + "</td><td>"
						+ e.Description + "</td><td>" + e.ServiceUrl
						+ "</td><td>" + e.ResourceUrl + "</td></tr>";
			});
			$(html).appendTo($("#services > tbody"));
		});
	} catch (exception) {
		alert('<p>Error' + exception);
	}
}

function get_url() {
	var pcol;
	var u = document.URL;

	/*
	 * /* We open the websocket encrypted if this page came on an /* https://
	 * url itself, otherwise unencrypted /
	 */

	if (u.substring(0, 5) == "https") {
		pcol = "wss://";
		u = u.substr(8);
	} else {
		pcol = "ws://";
		if (u.substring(0, 4) == "http")
			u = u.substr(7);
	}

	u = u.split('/');

	return pcol + u[0] + "/ws";
}

function basename(path) {
	return path.split('/').reverse()[0];
}

$('#btnnotify').on('click', function(e) {
	if ($.cookie("notification") === "true") {
		$.cookie("notification", false);
	} else {
		Notification.requestPermission(function(permission) {
			if (!('permission' in Notification)) {
				Notification.permission = permission;
			}

			if (permission === "granted") {
				$.cookie("notification", true, {
					expires : 424242
				});
				$('btnnotify').addClass("active");
			}
		});
	}
});

function getUrl() {
	function onEnter(event) {
		if (event.which == 13) {
			confirmSettings();
		}
	}

	$('#url').keypress(onEnter);
}

function confirmSettings() {
	socket.send('REG_API_ADD_URL,' + $('#url').val());
	bootstrapServices();
	$('#settings').modal('hide');
}

function notificationsSupported() {
	return "Notification" in window;
}

function notify(title, artist, album) {
	var textNotification = "";
	if (typeof artist != 'undefined' && artist.length > 0)
		textNotification += " " + artist;
	if (typeof album != 'undefined' && album.length > 0)
		textNotification += "\n " + album;

	var notification = new Notification(title, {
		icon : 'assets/favicon.ico',
		body : textNotification
	});
	setTimeout(function(notification) {
		notification.close();
	}, 3000, notification);
}
