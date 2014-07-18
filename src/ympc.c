/* ympc
   (c) 2013-2014 Andrew Karpow <andy@ndyk.de>
   (c) 2013-2014 Christian Krafft <parabelboi@gmail.com>
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

#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include <unistd.h>
#include <getopt.h>
#include <sys/time.h>
#include <pthread.h>

#include "mongoose.h"
#include "http_server.h"
#include "registry.h"
#include "config.h"

extern char *optarg;

int force_exit = 0;

void bye()
{
    force_exit = 1;
}

static int server_callback_reg(struct mg_connection *c) {
    if (c->is_websocket)
    {
        c->content[c->content_len] = '\0';
        if(c->content_len)
            return callback_reg(c);
        else
            return MG_CLIENT_CONTINUE;
    }
    else
        return callback_http(c);
}

int main(int argc, char **argv)
{
    int n, option_index = 0;
    struct mg_server *reg_server = mg_create_server(NULL);
    //struct mg_server *mpd_server = mg_create_server(NULL);
    unsigned int current_timer = 0, last_timer = 0;
    char *run_as_user = NULL;

    atexit(bye);
    mg_set_option(reg_server, "listening_port", "8000");

    //strcpy(reg.url, "http://127.0.0.1:8000");

    static struct option long_options[] = {
    	{"url",          required_argument, 0, 'c'},
        {"listen",       required_argument, 0, 'l'},
        {"user",         required_argument, 0, 'u'},
        {"cert",         no_argument,       0, 'k'},
        {"version",      no_argument,       0, 'v'},
        {"help",         no_argument,       0,  0 },
        {0,              0,                 0,  0 }
    };

    while((n = getopt_long(argc, argv, "c:l:u:k:v",
                long_options, &option_index)) != -1) {
        switch (n) {
            case 'c':
                strncpy(reg.url, optarg, sizeof(reg.url));
                break;
            case 'l':
                mg_set_option(reg_server, "listening_port", optarg);
                break;
            case 'k':
                mg_set_option(reg_server, "ssl_certificate", optarg);
                mg_set_option(reg_server, "extra_mime_types", ".pem=application/x-pem-file");
                break;
            case 'u':
                run_as_user = strdup(optarg);
                break;
            case 'v':
                fprintf(stdout, "ympc  %d.%d.%d\n"
                        "Copyright (C) 2014 Andrew Karpow <andy@ndyk.de>\n"
                        "Copyright (C) 2014 Christian Krafft <parabelboi@gmail.com>\n"
                        "built " __DATE__ " "__TIME__ " ("__VERSION__")\n",
                        YMPC_VERSION_MAJOR, YMPC_VERSION_MINOR, YMPC_VERSION_PATCH);
                return EXIT_SUCCESS;
                break;
            default:
                fprintf(stderr, "Usage: %s [OPTION]...\n\n"
                        " -c, --url <url>\t\talternative bootstrap url\n"
                        " -l, --listen [ip:]<port>\tlisten interface/port [localhost:8000]\n"
                		" -k, --cert ssl_cert.pem\tpath to certificate\n"
                		" -u, --user <username>\t\tdrop priviliges to user after socket bind\n"
                        " -V, --version\t\t\tget version\n"
                        " --help\t\t\t\tthis help\n"
                        , argv[0]);
                return EXIT_FAILURE;
        }
    }

    /* drop privilges at last to ensure proper port binding */
    if(run_as_user != NULL)
    {
        mg_set_option(reg_server, "run_as_user", run_as_user);
        free(run_as_user);
    }

    mg_set_http_close_handler(reg_server, reg_close_handler);
    mg_set_request_handler(reg_server, server_callback_reg);

    while (!force_exit) {
        current_timer = mg_poll_server(reg_server, 200);
        mg_poll_server(reg_server, 200);
        if(current_timer - last_timer)
        {
            last_timer = current_timer;
            reg_poll(reg_server);
        }
    }

    reg_disconnect();
    mg_destroy_server(&reg_server);

    return EXIT_SUCCESS;
}
