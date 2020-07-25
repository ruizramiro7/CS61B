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
cd $testdir
echo "java gitlet.Main init"
java gitlet.Main init
touch same.txt
echo "same stuff" >> same.txt
touch rem.txt
echo "rem stuff" >> rem.txt
touch mod.txt
echo "mod stuff" >> mod.txt
java gitlet.Main add same.txt
java gitlet.Main add rem.txt
java gitlet.Main add mod.txt
java gitlet.Main commit "First commit"
java gitlet.Main branch other

touch master.txt
java gitlet.Main add master.txt
echo "Edit from master" >> mod.txt
java gitlet.Main add mod.txt
java gitlet.Main commit "Master pointer"

java gitlet.Main checkout other
touch other.txt
java gitlet.Main add other.txt
echo "Edit from other" >> mod.txt
java gitlet.Main rm rem.txt
java gitlet.Main add mod.txt
java gitlet.Main commit "Removed rem.txt and modified mod.txt"

java gitlet.Main checkout master
java gitlet.Main merge other
java gitlet.Main log

echo "DONE"
cd ..
