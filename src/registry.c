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

#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <stdlib.h>
#include <libgen.h>

#include "registry.h"
#include "config.h"
#include "json_encode.h"

const char * reg_cmd_strs[] = {
    REG_CMDS(GEN_STR)
};

static inline enum reg_cmd_ids get_cmd_id(char *cmd)
{
    for(int i = 0; i < sizeof(reg_cmd_strs)/sizeof(reg_cmd_strs[0]); i++)
        if(!strncmp(cmd, reg_cmd_strs[i], strlen(reg_cmd_strs[i])))
            return i;

    return -1;
}


int callback_reg(struct mg_connection *c)
{
    enum reg_cmd_ids cmd_id = get_cmd_id(c->content);
    size_t n = 0;
#ifdef WITH_REG_URL_CHANGE
    unsigned int uint_buf, uint_buf_2;
    int int_buf;
    char *p_charbuf = NULL;
#endif

    if(cmd_id == -1)
        return MG_CLIENT_CONTINUE;

    switch(cmd_id)
    {
    	case REG_API_ADD_URL:
#ifdef WITH_REG_URL_CHANGE
    		/* Commands allowed when disconnected from service registry */
            int_buf = 0;
            if(sscanf(c->content, "REG_API_ADD_URL,%d,%m[^\t\n ]", &int_buf, &p_charbuf) &&
                p_charbuf != NULL && int_buf > 0)
            {
                strncpy(reg.url, p_charbuf, sizeof(reg.url));
                free(p_charbuf);
                reg.conn_state = REG_RECONNECT;
                return MG_CLIENT_CONTINUE;
            }
#endif
            break;
        case REG_API_GET_URL:
            n = snprintf(reg.buf, MAX_SIZE, "{\"type\":\"url\", \"data\": "
                "{\"url\" : \"%s\"}"
                "}", reg.url);
            break;
    }

    if(reg.conn_state == REG_CONNECTED && reg_connection_get_error(reg.conn) != REG_ERROR_SUCCESS)
    {
        n = snprintf(reg.buf, MAX_SIZE, "{\"type\":\"error\", \"data\": \"%s\"}",
            reg_connection_get_error_message(reg.conn));

        /* Try to recover error */
        if (!reg_connection_clear_error(reg.conn))
            reg.conn_state = REG_FAILURE;
    }

    if(n > 0)
        mg_websocket_write(c, 1, reg.buf, n);

    return MG_CLIENT_CONTINUE;
}

int reg_close_handler(struct mg_connection *c)
{
    /* Cleanup session data */
    if(c->connection_param)
        free(c->connection_param);
    return 0;
}

#ifdef WITH_REG_CONNECT
static int reg_notify_callback(struct mg_connection *c) {
    size_t n;

    if(!c->is_websocket)
        return MG_REQUEST_PROCESSED;

    if(c->callback_param)
    {
        /* error message? */
        n = snprintf(reg.buf, MAX_SIZE, "{\"type\":\"error\",\"data\":\"%s\"}",
            (const char *)c->callback_param);

        mg_websocket_write(c, 1, reg.buf, n);
        return MG_REQUEST_PROCESSED;
    }

    if(!c->connection_param)
        c->connection_param = calloc(1, sizeof(struct t_reg_client_session));

    struct t_reg_client_session *s = (struct t_reg_client_session *)c->connection_param;

    if(reg.conn_state != REG_CONNECTED) {
        n = snprintf(reg.buf, MAX_SIZE, "{\"type\":\"disconnected\"}");
        mg_websocket_write(c, 1, reg.buf, n);
    }
    else
    {
        mg_websocket_write(c, 1, reg.buf, reg.buf_size);
    }

    return MG_REQUEST_PROCESSED;
}
#endif

void reg_poll(struct mg_server *s)
{
#ifdef WITH_REG_CONNECT
    switch (reg.conn_state) {
        case REG_DISCONNECTED:
            /* Try to connect */
            fprintf(stdout, "Connecting to Service Registry %s\n", reg.url);

            reg.conn = reg_connection_new(reg.url, 10000);
            if (reg.conn == NULL) {
                reg.conn_state = REG_FAILURE;
                return;
            }

            if (reg_connection_get_error(reg.conn) != REG_ERROR_SUCCESS) {
                fprintf(stderr, "Service Registry connection: %s\n", reg_connection_get_error_message(reg.conn));
                mg_iterate_over_connections(s, reg_notify_callback,
                    (void *)reg_connection_get_error_message(reg.conn));
                reg.conn_state = REG_FAILURE;
                return;
            }

            fprintf(stdout, "Service Registry connected.\n");
            reg.conn_state = REG_CONNECTED;
            break;

        case REG_FAILURE:
            fprintf(stderr, "Service Registry connection failed.\n");
            break;

        case REG_DISCONNECT:
        case REG_RECONNECT:
            if(reg.conn != NULL)
                reg_connection_free(reg.conn);
            reg.conn = NULL;
            reg.conn_state = REG_DISCONNECTED;
            break;

        case REG_CONNECTED:
            reg.buf_size = reg_put_state(reg.buf);
            mg_iterate_over_connections(s, reg_notify_callback, NULL);
            break;
    }
#endif
}

void reg_disconnect()
{
    reg.conn_state = REG_DISCONNECT;
    reg_poll(NULL);
}

#ifdef WITH_REG_CONNECT
int reg_put_state(char *buffer)
{
    struct reg_status *status;
    int len;

    status = reg_run_status(reg.conn);
    reg_run_status(reg.conn);

    if (!status) {
        fprintf(stderr, "reg_run_status: %s\n", reg_connection_get_error_message(reg.conn));
        reg.conn_state = REG_FAILURE;
        return 0;
    }

    len = snprintf(buffer, MAX_SIZE,
        "{\"type\":\"state\", \"data\":{"
        " \"state\":%d"
        "}}",
        reg_status_get_state(status));

    reg_status_free(status);
    return len;
}
#endif

int reg_connection_get_error(struct reg_connection *conn) {
    return REG_ERROR_SUCCESS;
}

const char * reg_connection_get_error_message(struct reg_connection *conn) {
    return "NotImplementedError";
}

bool reg_connection_clear_error(struct reg_connection *connection) {
    return true;
}

#ifdef WITH_REG_CONNECT
int reg_connection_new(char *url, unsigned timeout_ms) {
    return NULL;
}
#endif

void reg_connection_free(struct reg_connection *connection) {
	return;
}

struct reg_status * reg_run_status(struct reg_connection *connection) {
	return NULL;
}

struct reg_status * reg_status_get_state(struct reg_connection *connection) {
	return NULL;
}

void reg_status_free(struct reg_status *status) {
	return;
}


