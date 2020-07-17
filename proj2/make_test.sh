#!/bin/bash

#d="gitlet/"
#javac ${d}Main.java ${d}CommitTree.java ${d}Utils.java ${d}GitletException.java;

# Run me with: bash make_test.sh

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
echo "Creating file called test.txt with text 'version 1'"
touch ./$testdir/test.txt
echo "version 1" >> ./$testdir/test.txt
cd $testdir
echo "java gitlet.Main init"
java gitlet.Main init
echo "java gitlet.Main add test.txt"
java gitlet.Main add "test.txt"
echo "gitlet.Main log"
java gitlet.Main log
echo "gitlet.Main commit 'test commit'"
java gitlet.Main commit "test commit"
echo "ls .gitlet/stage"
ls .gitlet/stage
echo "ls .gitlet/commits"
ls .gitlet/commits
echo "java gitlet.Main log"
java gitlet.Main log
echo "java gitlet.Main branch testBranch"
java gitlet.Main branch testBranch
echo "java gitlet.Main status"
java gitlet.Main status
echo "Done"
cd ..
