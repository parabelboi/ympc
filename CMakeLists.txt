cmake_minimum_required(VERSION 2.6)

project (ympc)
set(CMAKE_MODULE_PATH ${CMAKE_MODULE_PATH} "${PROJECT_SOURCE_DIR}/cmake/")
set(CPACK_PACKAGE_VERSION_MAJOR "1")
set(CPACK_PACKAGE_VERSION_MINOR "2")
set(CPACK_PACKAGE_VERSION_PATCH "2")

set(SSL_KEY "NOT_GIVEN" CACHE STRING "path to signing key for ssl key generation")

option(WITH_REG_URL_CHANGE "Let users of the web frontend change the bootstrap url" OFF)
option(WITH_REG_CONNECT "Let ympc keep track of the online status of the service registry" OFF)
option(WITH_GIT_INFO "serve information about the state of the source tree (git log, git diff)" ON)

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
	htdocs/mycert.pem
	htdocs/myreq.pem
	htdocs/git.log
	htdocs/git.diff
)

add_executable(mkdata src/mkdata.c)
get_target_property(MKDATA_EXE mkdata LOCATION)

add_custom_command(OUTPUT htdocs/mycert.pem
	COMMAND openssl req -new -newkey rsa:1024 -nodes -subj '/CN=ympc-'`git describe` -keyout mykey.pem -out myreq.pem
	COMMAND openssl req -in myreq.pem -noout -verify -key mykey.pem
	COMMAND echo "No Signing Key given, creating a self-signed certificate"
	COMMAND openssl x509 -req -days 365 -in myreq.pem -signkey mykey.pem -out htdocs/mycert.pem
	WORKING_DIRECTORY ${PROJECT_SOURCE_DIR}
)

add_custom_command(OUTPUT htdocs/myreq.pem
	COMMAND openssl x509 -text -in htdocs/mycert.pem > htdocs/myreq.pem
	WORKING_DIRECTORY ${PROJECT_SOURCE_DIR}
	DEPENDS htdocs/mycert.pem
)

add_custom_command(OUTPUT htdocs/git.log
	COMMAND git log  > htdocs/git.log
	WORKING_DIRECTORY ${PROJECT_SOURCE_DIR}
)

add_custom_command(OUTPUT htdocs/git.diff
    COMMAND git diff > htdocs/git.diff
	WORKING_DIRECTORY ${PROJECT_SOURCE_DIR}
)

add_custom_command(OUTPUT ${PROJECT_BINARY_DIR}/assets.c
	COMMAND ${MKDATA_EXE} ${RESOURCES} > ${PROJECT_BINARY_DIR}/assets.c
    WORKING_DIRECTORY ${PROJECT_SOURCE_DIR}
    DEPENDS ${RESOURCES} mkdata
)

set(SOURCES ${SOURCES}
    src/ympc.c
    src/registry.c
    src/mongoose.c
    src/json_encode.c
    assets.c
    htdocs/mycert.pem
    htdocs/myreq.pem
    htdocs/git.log
    htdocs/git.diff
)

add_executable(ympc ${SOURCES})
target_link_libraries(ympc ${CMAKE_THREAD_LIBS_INIT})

install(TARGETS jmpd DESTINATION bin)
install(FILES ympc.1 DESTINATION ${CMAKE_INSTALL_PREFIX}/share/man/man1)
install(DIRECTORY htdocs DESTINATION share/${PROJECT_NAME})
install(FILES contrib/init.debian DESTINATION /etc/init.d
    PERMISSIONS OWNER_READ OWNER_WRITE OWNER_EXECUTE GROUP_READ GROUP_EXECUTE
    WORLD_READ WORLD_EXECUTE CONFIGURATIONS debrelease RENAME ${PROJECT_NAME})
install(FILES contrib/default.debian DESTINATION /etc/default/${PROJECT_NAME}
    CONFIGURATIONS debrelease RENAME ${PROJECT_NAME})

