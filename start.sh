#!/bin/sh

runmode=$(yq e '.runmode' /config/config.yaml)
if [ "${runmode}" == "" ] || [ "${runmode}" == "once" ] ; then
  echo "Collecting..."
  collect.sh
  echo "Collect finished. Exiting..."
  exit 0
elif [ "${runmode}" == "once_and_standby" ] ; then
  echo "Collecting..."
  collect.sh
  echo "Collect finished. Standing by..."
  tail -f /dev/null
elif [ "${runmode}" == "standby" ] ; then
  echo "Standing by..."
  tail -f /dev/null
fi

echo "Unkown run mode. Exiting."
