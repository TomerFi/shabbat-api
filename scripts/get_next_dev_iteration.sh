#!/bin/bash
show_usage() {
	echo "Script returing the next java development iteration for a semantic version"
	echo "--------------------------------------------------------------------------"
	echo "Usage: $0 -h/--help"
	echo "Usage: $0 --tag <tag>"
	echo ""
	echo "Example: $0 --tag 2.1.17"
	echo "Will output: 2.1.18-SNAPSHOT"
	echo ""
	echo "** requires nodejs >= 14.0.0"
}
if [[ ($# == "--help") || $# == "-h" ]]; then
	show_usage
	exit 0
fi
if [ -z "$1" ]; then
	show_usage
	exit 1
fi
# iterate over arguments and create named parameters
while [ $# -gt 0 ]; do
	if [[ $1 == *"--"* ]]; then
		param="${1/--/}"
		declare $param="$2"
	fi
	shift
done
# if no --tag provided show usage and exit
if [ -z "$tag" ]; then
	show_usage
	exit 1
fi
# utility function for incrementing the bump number
increment() { echo $(("$1" + 1)); }
# extract the major_minor and patch parts from the version
new_major_minor=$(cut -f1,2 -d"." <<<$tag)
new_patch=$(cut -d"." -f3- <<<$tag)
# increment the patch part
next_patch=$(increment $new_patch)
# concatenate the new major, minor, and next patch parts with the -SNAPSHOT suffix
next_iteration=$new_major_minor.$next_patch-SNAPSHOT
# return the next development iteration
echo $next_iteration
