#!/bin/bash

runmode=$(yq e '.runmode' /config/config.yaml)
if [ "${runmode}" == "once" ] ; then
  ./collect.sh
  exit 0
elif [ "${runmode}" == "once_and_standby" ] ; then
  ./collect.sh
  tail -f /dev/null
elif [ "${runmode}" == "standby" ] ; then
  tail -f /dev/null
fi

echo "Unkown run mode. Exiting."