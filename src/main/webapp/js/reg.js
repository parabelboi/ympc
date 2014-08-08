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
	getMetaData();
	readServices();
	if (!notificationsSupported())
		$('#btnnotify').addClass("disabled");
	else if ($.cookie("notification") === "true")
		$('#btnnotify').addClass("active")
});

function getMetaData() {
	try {
		OData.read("/registry/$metadata", function(data) {
			metadata = data;
			OData.defaultMetadata.push(metadata);
			$('.top-right').notify({
				message : {
					text : "connected to ympd"
				},
				fadeOut : {
					enabled : true,
					delay : 500
				}
			});
		}, function(err) {
			alert(JSON.stringify(err));
		}, OData.metadataHandler);
	} catch (exception) {
		alert('<p>Error' + exception);
	}
}

function readServices() {
	try {
		OData.read({
			requestUri : "/registry/Services"
		}, function(data) {
			$.each(data.results, function() {
				$("<tr serviceid=\""+this.Id+"\">"
						+ "<td>" + this.Id + "</td>"
						+ "<td>" + this.Name + "</td>"
						+ "<td>" + this.Url + "</td>"
						+ "<td><button class=\"btn btn-default\" onclick=\"removeService(\'"+this.Id+"\');\">-</button></td>"
					+ "</tr>").appendTo($("#services > tbody"));
			});
		}, function(err) {
			$('.top-right').notify({
				message : {
					text : err.message
				},
				type : "danger",
			}).show();
		});
	} catch (exception) {
		alert('<p>Error' + exception);
	}
}

function removeService(serviceId) {
	try {
		console.log("removing service");
		OData.request({
			requestUri : "/registry/Services(\'"+serviceId+"\')",
			method : "DELETE",
		}, function(deletedItem) {
			console.log("removed service");
			$("#services > tbody > tr[serviceid="+serviceId+"]").remove();
		}, function(err) {
			console.log("Error occurred");
		});
	} catch (exception) {
		alert('<p>Error' + exception);
	}
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

$("#addbtn").on('click', function(e) {
	try {
		console.log("posting sample data");
		OData.request({
			requestUri : "/registry/Services",
			method : "POST",
			data : {
				Id : $("#Id").val(),
				Name : $("#Name").val(),
				Url : $("#Url").val(),
			}
		}, function(insertedItem) {
			$( "<tr serviceid="+insertedItem.Id+">"
				+ "<td>" + insertedItem.Id + "</td>"
				+ "<td>" + insertedItem.Name + "</td>"
				+ "<td>" + insertedItem.Url + "</td>"
				+ "<td><button class=\"btn btn-default\" onclick=\"removeService(\'"+insertedItem.Id+"\');\">-</button></td>"
			+ "</tr>").appendTo($("#services > tbody"));			
		}, function(err) {
			console.log("Error occurred");
		});
	} catch (exception) {
		alert('<p>Error' + exception);
	}
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
