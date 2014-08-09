/*
 * registry.h
 *
 *  Created on: Jul 16, 2014
 *      Author: ckrafft
 */

#ifndef REGISTRY_H_
#define REGISTRY_H_

#include <stdbool.h>

#include "mongoose.h"



#define MAX_SIZE 1024 * 100

#define GEN_ENUM(X) X,
#define GEN_STR(X) #X,

#define REG_CMDS(X) \
    X(REG_API_GET_URL) \
    X(REG_API_ADD_URL) \

enum reg_cmd_ids {
    REG_CMDS(GEN_ENUM)
};

struct reg_status {
	int state;
};

enum reg_conn_states {
    REG_DISCONNECTED,
    REG_FAILURE,
    REG_ERROR_SUCCESS,
    REG_CONNECTED,
    REG_RECONNECT,
    REG_DISCONNECT
};

struct t_registry {
    //int port;
    //char host[128];
    char url[128];

    struct reg_connection *conn;
    enum reg_conn_states conn_state;

    /* Reponse Buffer */
    char buf[MAX_SIZE];
    size_t buf_size;

    struct reg_status *state;
} reg;

struct t_reg_client_session {
    int some_id;
    unsigned some_version;
};

void reg_poll(struct mg_server *s);
int callback_reg_http(struct mg_connection *c);
int callback_reg_ws(struct mg_connection *c);
int reg_close_handler(struct mg_connection *c);
int reg_connection_get_error(struct reg_connection *conn);
const char * reg_connection_get_error_message(struct reg_connection *conn);
bool reg_connection_clear_error(struct reg_connection *connection);
int reg_connection_new(char *url, unsigned timeout_ms);
void reg_connection_free(struct reg_connection *connection);
void reg_disconnect();
int reg_put_state(char *buffer);
struct reg_status * reg_run_status(struct reg_connection *connection);
struct reg_status * reg_status_get_state(struct reg_connection *connection);
void reg_status_free(struct reg_status *status);
#endif /* REGISTRY_H_ */
