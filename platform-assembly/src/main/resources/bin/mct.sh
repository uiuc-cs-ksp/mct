#!/bin/bash

_REALPATH="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
_MCT_BASE="$_REALPATH/.."
_MCT_LIB="$_MCT_BASE/lib"
_MCT_LAUNCHER_JAR="$_MCT_LIB/startup-${project.version}.jar"
_JAVA_EXE=java

cd $_MCT_BASE

$_JAVA_EXE -jar $_MCT_LAUNCHER_JAR