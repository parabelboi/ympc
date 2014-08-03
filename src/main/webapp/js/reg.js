/* jmpd
   (c) 2013-2014 Andrew Karpow <andy@ndyk.de>
   (c) 2014 Christian Krafft <parabelboi@gmail.com>
   This project's homepage is: http://www.jmpd.eu
   
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
var metadata;

$(document).ready(function() {
	regConnect();
	if (!notificationsSupported())
		$('#btnnotify').addClass("disabled");
	else if ($.cookie("notification") === "true")
		$('#btnnotify').addClass("active")
});

function regConnect() {
	try {
		OData.read("/registry/$metadata", 
		function(data) {
			console.log("getting results");
			metadata = data;
			OData.defaultMetadata.push(metadata);
			$('.top-right').notify({
				message : {
					text : "loading namespace: " + metadata.dataServices.schema[0].namespace
				},
				fadeOut : {
					enabled : true,
					delay : 500
				}
			});
		}, function(err) {
			alert(JSON.stringify(err));
		}, OData.metadataHandler);

		OData.read({
			requestUri : "/registry/Services"
		}, function(data) {
			var html = "";
			$.each(data.results, function() {
				html += "<tr><td>" + this.Id + "</td><td>" + this.Name + "</td><td>"
						+ this.Url + "</td></tr>";
			});
			$(html).appendTo($("#services > tbody"));
		}, function(err) {
			$('.top-right').notify({
				message : {
					text : err.message
				},
				type : "danger",
			}).show();
			console.log("Error occurred: " + err.message);
		});
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
						+ e.Url + "</td></tr>";
			});
			$(html).appendTo($("#services > tbody"));
		});
	} catch (exception) {
		alert('<p>Error' + exception);
	}
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

$('#add-service').on('submit', function(e) {
	try {
		console.log("posting sample data");
		id = $('#Id').value;
		name = $('#Name').value;
		url= $('#Url').value;
		OData.request({
			requestUri : "/registry/Services",
			method : "POST",
			data : {
				Id : id,
				Name : name,
				Url : url,
			}
		}, function(err) {
			console.log("Error occurred");
		});
	} catch (exception) {
		alert('<p>Error' + exception);
	}

	$('#settings').modal('hide');
});

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
