#!/usr/bin/env sh

export AWS_PROFILE="jutul"
export AWS_REGION="eu-west-1"
java -cp target/hockey-smash.jar no.meikle.hockey_smash.utils.TableAnalysis "$@"