#!/bin/bash

CURRENT_DIR=`pwd`
BUILD_DIR="$CURRENT_DIR/spigot-buildtools"

echo "Current directory is $CURRENT_DIR"

if [ ! -e "$BUILD_DIR" ]; then
    mkdir "$BUILD_DIR"
fi

cd "$BUILD_DIR"

BUILDTOOLS="BuildTools.jar"

if [ ! -e "$BUILDTOOLS" ]; then
    echo "Download $BUILDTOOLS"

    wget "https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/$BUILDTOOLS"
fi

echo "Execute $BUILDTOOLS"

chmod +x "$BUILDTOOLS"
java -jar "$BUILDTOOLS"

cd "$CURRENT_DIR"

LIB_DIR="$CURRENT_DIR/lib"

if [ ! -e "$LIB_DIR" ]; then
    mkdir "$LIB_DIR"
fi

mvn install:install-file -Dfile="$BUILD_DIR"/spigot-*.jar -DgroupId=org.spigotmc -DartifactId=spigot -Dversion=1.12.2-R0.1-SNAPSHOT -Dpackaging=jar