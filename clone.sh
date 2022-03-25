#!/bin/bash

clone-into() {
  task=${1}
  dest=${2}
  if  [ ! -d .tasks/satori/${task} ]; then
    echo "Task not found"
    exit 0
  fi
  mkdir -p ${dest}/${task}
  mkdir -p ${dest}/${task}/src/test/java

  cp -r ./.tasks/satori/${task} ${dest}/
  cp -r ./satori/${task}/* ${dest}/${task}/src/test/java
}



if [ -n "${1}" ]; then
  if [ -n "${2}" ]; then
    clone-into ${1} ${2}
  else
    echo "Destination directory required."
  fi
else
  echo "Task name required."
fi
