#!/bin/bash
#note: ##($RECONFIG) edit the whole file... lost hope of simplicity for now

#get nowtime
now=$(date +%F_%T)
bak=/home/admalledd/minecraft/old_baks/server2/$now

#steps:
#1, move current rdiff-backup dir to archive
#2, extract the current newest backup
#3, compress to a snapshot
#4, remove un-compressed backup snapshot

mv /home/$USER/minecraft/backup.server2/ $bak.rdiff

rdiff-backup -r 0B $bak.rdiff $bak.raw

tar cvfz $bak.snap.tar.gz $bak.raw

rm -rf $bak.raw
