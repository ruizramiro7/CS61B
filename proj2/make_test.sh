#!/bin/bash

#d="gitlet/"
#javac ${d}Main.java ${d}CommitTree.java ${d}Utils.java ${d}GitletException.java;

path="gitlet"
files=""

for f in ${path}/*.java; do 
	if [ $f != ${path}/UnitTest.java ]; then 
		echo "Compiling $f";
		files="$files $f"
	fi; 
done

javac $files

testdir="test-gitlet"
rm -r $testdir
mkdir $testdir
mkdir $testdir/gitlet
cp gitlet/*.class ./$testdir/gitlet
touch ./$testdir/test.txt
echo "version 1" >> ./$testdir/test.txt
cd $testdir
java gitlet.Main init
java gitlet.Main add "test.txt"
java gitlet.Main log
java gitlet.Main commit "test commit"
echo "Looking in staging area and found..."
ls .gitlet/stage
echo "Looking in commits and found..."
ls .gitlet/commits
echo "Printing commit log"
java gitlet.Main log
echo "done"
cd ..
