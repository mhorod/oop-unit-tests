init-test() {
  task=${1}
  echo "Copying tests into .tasks/${task}/src/test/java/"
  echo -n "Found tests: "
  tests=$(ls ${task})
  echo ${tests}
  mkdir -p .tasks/${task}/src/test/java
  cp -r ${task}/* .tasks/${task}/src/test/java/
}

execute-test() {
  task=${1}
  echo "Compiling tests for task ${task}"
  mvn test-compile -f .tasks/${task}/pom.xml
  if [ $? -ne 0 ]; then
    return 1
  fi
}

cleanup-test() {
  task=${1}
  echo "Cleaning up after testing task ${task}"
  rm -r .tasks/${task}/src/test
  rm -r .tasks/${task}/target
}

check-task() {
  task=${1}
  if [ ! -d .tasks/${task} ]; then
    echo "Task ${task} not found. Ignoring. "
    exit 0
  fi

  if [ ! -d ${task} ]; then 
    echo "No tests for ${task} found. Skipping."
    exit 0
  else
    echo "Running tests for task ${task}"
  fi
  
  init-test ${task}
  execute-test ${task}
  exec_exit_code=$?
  if [ ${exec_exit_code} -ne 0 ]; then
    echo "Test build for task ${task} failed."
  else
    echo "Test build for task ${task} succeeded."
  fi
  cleanup-test ${task}
  echo ""

  return ${exec_exit_code}
}

check-all()  {
  exit_code=0;
  for task in $(ls .tasks/satori) 
  do
    check-task satori/${task}
    if [ $? -ne 0 ]; then
      exit_code=1
    fi
  done

  if [ ${exit_code} -ne 0 ]; then
    echo "Project build failed."
  else
    echo "Project build succeeded";
  fi

  exit ${exit_code}
}


if [ -n "${1}" ]; then
  check-task satori/${1}
else
  check-all
fi

