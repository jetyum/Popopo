#!/bin/bash

BUILD_DIR=`pwd`"/spigot-buildtools"

echo "[Preprocess] Start preprocess"

if [ ! -e "$BUILD_DIR" ]; then
    mkdir "$BUILD_DIR"
fi

cd "$BUILD_DIR"

BUILDTOOLS="BuildTools.jar"

echo "[Preprocess] Prepare $BUILDTOOLS"

if [ ! -e "$BUILDTOOLS" ]; then
    URL="https://hub.spigotmc.org"
    PREFIX="$URL/jenkins/job"
    ROOT="BuildTools/lastStableBuild"
    SUFFIX="artifact/target/$BUILDTOOLS"

    wget "$PREFIX/$ROOT/$SUFFIX"
fi

echo "[Preprocess] Execute $BUILDTOOLS"

chmod +x "$BUILDTOOLS"
# java -jar "$BUILDTOOLS"

LATEST_JAR=$(ls -lt spigot-*.jar \
    | head -n 1 \
    | grep -o spigot-*.jar)

echo "[Preprocess] Install $LATEST_JAR"

mvn install:install-file \
    -Dfile="$LATEST_JAR" \
    -DgroupId=org.spigotmc \
    -DartifactId=spigot \
    -Dversion=1.12.2-R0.1-SNAPSHOT \
    -Dpackaging=jar

echo "[Preprocess] Finish preprocess"