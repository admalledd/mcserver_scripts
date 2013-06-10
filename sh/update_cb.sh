#!/bin/sh
out="$($(dirname $0)/curserverpath.sh)/craftbukkit.jar"
if [ -n "$1" ]; then
    if [ "$1" = "dev" ]; then
        wget "http://dl.bukkit.org/downloads/craftbukkit/get/latest-dev/craftbukkit-dev.jar" -O $out
        echo 'using latest dev build'
    elif [ "$1" = "beta" ]; then
        wget "http://dl.bukkit.org/downloads/craftbukkit/get/latest-beta/craftbukkit-beta.jar" -O $out
        echo 'using latest beta build'
    elif [ "$1" = "rb" ]; then
        wget "http://dl.bukkit.org/downloads/craftbukkit/get/latest-rb/craftbukkit-rb.jar" -O $out
        echo 'using latest recomended build'
    else
        echo 'no build chosen, use ./CMD (dev|beta|rb)'
    fi
fi
