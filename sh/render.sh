#!/bin/bash

main_server_screen="server2"

function runcmd {
 if [ -n "$STY" ]; then
  screen -x $1 -p $2 -X stuff "$3$(echo -ne '\r')"
 else
  screen -p $2 -X stuff "$3$(echo -ne '\r')"
 fi
}
#runcmd $main_server_screen 1 "echo test"
CUR=$($(dirname $0)/curserverpath.sh)

runcmd $main_server_screen 4 "python Minecraft-Overviewer/overviewer.py --config overviewer.cfg"

