#!/bin/sh
# Before executing this script, generate the javadoc files into pcdoc

# find out the current branch so we know where to switch back
OLD_BRANCH=`git branch --no-color | sed -e '/^[^*]/d' -e 's/* \(.*\)/\1/'`

git checkout gh-pages || exit $?

# Clear out the old files: (files which will be served)
echo "Clean old docs ..."
rm -rf docs/javadoc/* 

# Replace them with new files and commit them:
echo "Copying new files and comitting ..."
cp -R -p docs/pcdoc/* docs/javadoc \
&& git add docs/javadoc \
&& git commit -a -m "Generated Javadoc"

#Remove the generated doc
echo "Clean generated docs ..."
rm -rf docs/pcdoc/*

echo "Pushing to GitHub gh-pages branch ..."
git push origin gh-pages || exit $?

# Switch back to the old branch
git checkout $OLD_BRANCH || exit $?