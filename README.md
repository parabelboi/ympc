jmpd
====

Standalone Web Service GUI written in C, utilizing Websockets and Bootstrap/JS

http://www.ympd.org

![ScreenShot](http://www.ympd.org/assets/ympd_github.png)

Dependencies
------------
 - libmpdclient 2: http://www.musicpd.org/libs/libmpdclient/
 - cmake 2.6: http://cmake.org/
 - maven 3

Unix Build Instructions
-----------------------

1. install dependencies, cmake and libmpdclient are available from all major distributions.
2. create build directory ```cd /path/to/src; mkdir build; cd build```
3. create makefile ```cmake ..  -DCMAKE_INSTALL_PREFIX:PATH=/usr```
4. build ```make```
5. install ```sudo make install``` or just run with ```./ympc```

Run flags
---------
```
Usage: ./jmpd [OPTION]...

 -c, --url <url>            use alternative url to bootstrap services
 -l, --listen [ip:]<port>   listen on address, defaults to 127.0.0.1:8000
 -k, --cert ssl_cert.pem	path to certificate
 -u, --user <username>      drop priviliges to user after socket bind
 -V, --version              get version
 --help                     this help
```


Copyright
---------

2013-2014 <andy@ndyk.de>
2014 <parabelboi@gmail.com>
