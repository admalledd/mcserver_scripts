#!/bin/bash

#A script to create a mirror of the main server, this is mostly used in de-greifing to check out what was what long ago.

PDIR=~/minecraft/para/
echo Cleaning old server files...

rm -rf $PDIR

echo Decompressing delta files...
rdiff-backup -r $1 -v6 ~/minecraft/backup/ $PDIR


echo removing problem plugins...
rm $PDIR/plugins/SimpleCronClone.jar
rm $PDIR/plugins/PyPluginLoader-0.3.4.jar
rm $PDIR/plugins/LogBlock.jar


echo editing server.properties...

sed 's/server-ip.*/server-ip=0.0.0.0/' $PDIR/server.properties >$PDIR/server.properties

echo server should be ready.
