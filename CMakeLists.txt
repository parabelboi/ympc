cmake_minimum_required(VERSION 2.6)

project (ympd)
set(CMAKE_MODULE_PATH ${CMAKE_MODULE_PATH} "${PROJECT_SOURCE_DIR}/cmake/")
set(CPACK_PACKAGE_VERSION_MAJOR "1")
set(CPACK_PACKAGE_VERSION_MINOR "2")
set(CPACK_PACKAGE_VERSION_PATCH "2")

set(SSL_KEY "NOT_GIVEN" CACHE STRING "path to signing key for ssl key generation")

option(WITH_REG_URL_CHANGE "Let users of the web frontend change the bootstrap url" OFF)
option(WITH_REG_CONNECT "Let ympc keep track of the online status of the service registry" OFF)

find_package(Threads REQUIRED)

configure_file(src/config.h.in ${PROJECT_BINARY_DIR}/config.h)
include_directories(${PROJECT_BINARY_DIR} ${PROJECT_SOURCE_DIR})

include(CheckCSourceCompiles)

set(CMAKE_C_FLAGS "-std=gnu99 -Wall") 
set(CMAKE_C_FLAGS_DEBUG "-ggdb -pedantic")

file(GLOB RESOURCES 
	RELATIVE ${PROJECT_SOURCE_DIR}
	htdocs/js/*
	htdocs/assets/*
	htdocs/css/*.min.css
	htdocs/css/*.css
	htdocs/fonts/*
	htdocs/index.html
	htdocs/git.log
	htdocs/git.diff
	htdocs/mycert.pem
	htdocs/myreq.pem
)

add_executable(mkdata htdocs/mkdata.c)
get_target_property(MKDATA_EXE mkdata LOCATION)

add_custom_command(OUTPUT ${PROJECT_BINARY_DIR}/assets.c
	COMMAND openssl req -noout -text -in myreq.pem || openssl req -new -newkey rsa:1024 -nodes -subj '/CN=ympc-'`git describe` -keyout mykey.pem -out myreq.pem && openssl req -noout -text -in myreq.pem
	COMMAND openssl req -in myreq.pem -noout -verify -key mykey.pem
	COMMAND openssl x509 -req -days 365 -in myreq.pem -signkey ${SSL_KEY} -out mycert.pem
	COMMAND openssl x509 -text -in mycert.pem > htdocs/mycert.pem
	COMMAND git log > htdocs/git.log
	COMMAND git diff > htdocs/git.diff
	COMMAND ${MKDATA_EXE} ${RESOURCES} > ${PROJECT_BINARY_DIR}/assets.c
    WORKING_DIRECTORY ${PROJECT_SOURCE_DIR}
    DEPENDS ${RESOURCES} mkdata
)

set(SOURCES
    src/ympc.c
    src/http_server.c
    src/registry.c
    src/mongoose.c
    src/json_encode.c
    assets.c
)

add_executable(ympc ${SOURCES})
target_link_libraries(ympc ${LIBMPDCLIENT_LIBRARY} ${CMAKE_THREAD_LIBS_INIT})

install(TARGETS ympc DESTINATION bin)
install(FILES ympc.1 DESTINATION ${CMAKE_INSTALL_PREFIX}/share/man/man1)
