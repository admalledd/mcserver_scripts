#!/bin/sh
#echo the current server base directory
echo $(dirname $(readlink -f $(dirname $0)/../start.sh))
