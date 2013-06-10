#!/bin/bash
#note: ##($RECONFIG) change to correct server number blah blah blah
log=/tmp/rdiff-log.server2
echo ---------------->>$log
echo $(date) >>$log


##backup entire server:

#current server path:
CUR=$($(dirname $0)/curserverpath.sh)
#note: ##($RECONFIG) change to backup-dir needed for this server number wrt multihosting
rdiff-backup -v6 $CUR /home/$USER/minecraft/backup.server2/ >> $log
