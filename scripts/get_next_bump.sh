#!/bin/bash
show_usage() {
	echo "Script returing the next recommended semantic version based on git tags and commits"
	echo "-----------------------------------------------------------------------------------"
	echo "Usage: $0 -h/--help"
	echo "Usage: $0"
	echo ""
	echo "Example output: 2.1.17"
	echo ""
	echo "** requires nodejs >= 14.0.0"
}
# show usage if asked for help
if [[ ($# == "--help") || $# == "-h" ]]; then
	show_usage
	exit 0
fi
# utility function for incrementing the bump number
increment() { echo $(("$1" + 1)); }
# use git-semver-tags to get the latest semver tag and read its parts seperately
last_semver=$(npx git-semver-tags | head -n 1)
read last_major last_minor last_patch <<<$(sed "s/\./ /g" <<<$last_semver)
# use conventional-recommended-bump to get the next bump recommendation
rec_bump=$(npx conventional-recommended-bump -p conventionalcommits -t "")
# create the next semver release based on the bump recomendation
if [ $rec_bump = "major" ]; then
	# bump major
	new_version=$(increment $last_major).0.0
elif [ $rec_bump = "minor" ]; then
	# bump minor
	new_version=$last_major.$(increment $last_minor).0
else
	# bump patch
	new_version=$last_major.$last_minor.$(increment $last_patch)
fi
# return new recommended semver
echo $new_version
