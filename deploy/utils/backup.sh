#!/usr/bin/env sh

export JAVA_TOOL_OPTIONS="-agentlib:jdwp=transport=dt_socket,address=5000,server=y,suspend=y"
java -cp target/hockey-smash.jar no.meikle.hockey_smash.utils.HockeySmashBackup "$@"