#!/bin/bash
show_usage() {
	echo "Script creating a CHANGELOG.md file for the next release"
	echo "--------------------------------------------------------"
	echo "Usage: $0 -h/--help"
	echo "Usage: $0 --tag <tag>"
	echo "Usage: $0 --tag <tag> --preset <preset>"
	echo ""
	echo "Example: $0 --tag 2.1.17"
	echo "Example: $0 --tag 2.1.17 --preset conventionalcommits"
	echo ""
	echo "** requires nodejs >= 14.0.0"
}
# show usage if asked for help
if [[ ($1 == "--help") || $1 == "-h" ]]; then
	show_usage
	exit 0
fi
# default named parameters
preset=${preset:-conventionalcommits}
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
# remove previous CHANGELOG.md file
rm -f CHANGELOG.md
# create a temporary release context file
echo "{\"version\": \"$tag\"}" >release-context.json
# use conventional-change-log to generate the CHANGELOG.md file
npx conventional-changelog -p $preset -t "" -c release-context.json -o CHANGELOG.md
# delete the temporary release context file
rm -f release-context.json
