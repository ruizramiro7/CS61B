package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents gitlet version history as a tree.
 * Adding files copies the files specified in the java gitlet.Main add []
 * command to the staging area directory. Each staged file is named using
 * SHA-1 while the workspace file name (e.g. file.txt) is stored in a map.
 * Committing files moves all files in the staging area directory to the
 * commits directory. The references that map the filename (e.g. file.txt) to the
 * SHA-1 id (e.g. f9324c...) are passed to a new commit node. That commit node is
 * then added to the end of the current head node and the head pointer is moved
 * forward.
 */
public class CommitTree implements Serializable {

    static final File COMMIT_LOG = Utils.join(Main.METADATA_DIR, "commit.dat");
    static final File STAGING_AREA = Utils.join(Main.METADATA_DIR, "stage");
    static final File COMMITS = Utils.join(Main.METADATA_DIR, "commits");
    static final int SEARCH_PRECISION = 7;

    /**
     * Represents a single commit in the version history.
     */
    public class CommitNode implements Serializable {
        private String message;
        private String merges = "";
        private Date timestamp;
        private String id;
        private HashMap<String, String> references;
        private int distance = 0;
        public CommitNode parent;
        public CommitNode secondParent;
        public List<CommitNode> children;

        /**
         * @param message the commit message that displays when using gitet log
         * @param timestamp the current date and time
         */
        public CommitNode(String message, Date timestamp,
                          HashMap<String, String> references) {

            this.message = message;
            this.timestamp = timestamp;
            this.children = new LinkedList<>();
            if (references == null) {
               this.references = new HashMap<>();
            }
            else {
                this.references = references;
            }

            String uniqueId = Arrays.stream(this.references.values().toArray())
                    .map(e -> e.toString()).reduce("", String::concat);
            id = Utils.sha1(uniqueId + timestamp.toString());
        }

        /**
         * Returns the string representation of a commit as per the specifications of
         * the gitlet log command. Log should look like:
         * ===
         * commit e05e0c60f7945913cdfa6692855c3ba7aec706c6
         * Date: Wed Dec 31 16:00:00 1969 -0800
         * initial commit
         * @return the string representation of the commit
         */
        public String toString() {
            SimpleDateFormat format
                    = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z");
            String date = format.format(timestamp);
            String mergeText = "";
            if (!merges.equals("")) {
                mergeText = "Merge: " + merges + "\n";
            }
            String s = "";
            s += "===\n"
              + "commit " + id + "\n"
              + mergeText
              + "Date: " + date + "\n"
              + message + "\n\n";
            return s;
        }

        /**
         * Prints all commits mades.
         */
        public void printAll() {
            for (CommitNode n: children) {
                n.printAll();
            }
            System.out.println(this);
        }

        /**
         * Returns whether or not the current commit has saved a version of a file.
         * @param name the filename as it appears in the local workspace.
         * @param id the id of the file as determine by a SHA-1 conversion of the
         *           file name + file contents.
         * @return true if it contains the file, false if it doesn't
         */
        public boolean contains(String name, String id) {
            if (references.containsKey(name)) {
                return references.get(name).equals(id);
            }
            return false;
        }

        public void addChild(CommitNode node) {
            node.parent = this;
            node.distance = this.distance + 1;
            children.add(node);
        }

        public CommitNode duplicate() {
            HashMap<String, String> copyRefs = new HashMap<String, String>();
            copyRefs.putAll(this.references);
            return new CommitNode(this.message, new Date(System.currentTimeMillis()), copyRefs);
        }

        public CommitNode mergeChild(CommitNode node) {
            HashMap<String, String> copyRefs = new HashMap<>();
            copyRefs.putAll(this.references);
            copyRefs.putAll(node.references);
            node.references = copyRefs;
            addChild(node);
            return node;
        }

        /**
         * Finds the commit by looking up the message.
         * @param message String of the message
         * @param ids takes a linkedlist composed of strings used as commit messages.
         */
        public void findCommitsWithMessage(String message, LinkedList<String> ids) {
            if (this.message.equals(message)) {
                ids.push(this.id);
            }
            for (CommitNode n: children) {
                n.findCommitsWithMessage(message, ids);
            }
        }

        /**
         * @source https://stackoverflow.com/questions/2261697/compare-first-three-characters-of-two-strings
         * @param id
         * @param precision
         * @return
         */
        public CommitNode findCommit(String id, int precision) {
            if (id.length() < precision) {
                Main.exitWithError("You must supply an id of length " + SEARCH_PRECISION + "or longer.");
            }
            if (id.length() > precision) {
                precision = id.length();
            }
            if (this.id.startsWith(id.substring(0, precision - 1))) {
                return this;
            }
            for (var n: children) {
                CommitNode candidate = n.findCommit(id, precision);
                if (candidate != null) {
                    return candidate;
                }
            }
            return null;
        }

        /**
         * Keep track of all Commit nodes that are parent of the other.
         * @param splitPoint Commit in branch.
         * @param list List of all related commit nodes
         */
        public void getHistory(CommitNode splitPoint, LinkedList<CommitNode> list) {
            if (splitPoint == this || this == null) {
                return;
            }
            list.push(this);
            parent.getHistory(splitPoint, list);
        }

    }

    private String currentBranch;
    private HashMap<String, CommitNode> branches = new HashMap<>();
    // Holds the references to blobs containing 1) the file name 2) the unique id
    private HashMap<String, String> staged = new HashMap<>();
    private LinkedList<String> toRemove = new LinkedList<String>();
    private CommitNode root;

    public CommitTree() {
        root = new CommitNode("initial commit", new Date(0), null);
        branches.put("master", root);
        currentBranch = "master";
    }

    /**
     * The head points to the current branch.
     * @return What the current Commit the head is at.
     */
    private CommitNode head() {
        return branches.get(currentBranch);
    }

    private void resetToCommit(CommitNode node) {
        File workingFile;
        File commitFile;

        // Copy and overwrite files from new branch into working directory
        for (String fileName: node.references.keySet()) {
            String fileID = node.references.get(fileName);
            commitFile = Utils.join(COMMITS, fileID);
            workingFile = Utils.join(Main.CWD, fileName);
            String contents = Utils.readContentsAsString(commitFile);
            Utils.writeContents(workingFile, contents);
        }

        // Delete file that are not tracked by the new branch
        deleteUntrackedFiles(node);
        //for (String fileName: Main.CWD.list()) {
        //    workingFile = Utils.join(Main.CWD, fileName);
        //    if (!workingFile.isDirectory()
        //            && !branch.references.containsKey(fileName)) {
        //        workingFile.delete();
        //    }
        //}
        clearStagingArea();
        branches.replace(currentBranch, node);
    }

    public void removeBranch(String branchName) {
        if (!branches.containsKey(branchName)) {
            Main.exitWithError("A branch with that name does not exist.");
        }
        else if (branchName.equals(currentBranch)) {
            Main.exitWithError("Cannot remove the current branch.");
        }
        else {
            branches.remove(branchName);
        }
        save();
    }

    /**
     * Deletes any untracked files
     * @param node Takes node and deletes any untracked file
     */
    public void deleteUntrackedFiles(CommitNode node) {
        File workingFile;
        for (String fileName: Main.CWD.list()) {
            workingFile = Utils.join(Main.CWD, fileName);
            if (!workingFile.isDirectory()
                    && !node.references.containsKey(fileName)) {
                workingFile.delete();
            }
        }
    }

    /**
     * Resets to the commit by taking in the ID of
     * commit they want to reset to.
     * @param id String of a commit's id
     */
    public void reset(String id) {
        if (getUntracked().size() > 0) {
            Main.exitWithError("There is an untracked file in the way; delete it, or add and commit it first.");
        }
       resetToCommit(findCommit(id));
       save();
    }

    /**
     * Helper to findCommit, finds the first 7 digits if the SHA1 of
     * the commit
     * @param id string of the commit's ID
     * @return Finds the commit with the matching ID
     */
    private CommitNode findCommit(String id) {
        CommitNode node = root.findCommit(id, SEARCH_PRECISION);
        if (node == null) {
            Main.exitWithError("No commit with that id exists.");
        }
        return node;
    }

    /**
     *
     * @param fileName
     * @param node
     */
    private void checkoutFile(String fileName, CommitNode node) {
        if (!node.references.containsKey(fileName)) {
            Main.exitWithError("File does not exist in that commit.");
        }
        File workingFile = Utils.join(Main.CWD, fileName);
        File commitFile = Utils.join(COMMITS, node.references.get(fileName));
        String contents = Utils.readContentsAsString(commitFile);
        Utils.writeContents(workingFile, contents);
    }

    // Methods for checkout command
    // ============================
    public void checkoutByFileName(String fileName) {
        checkoutFile(fileName, head());
    }

    public void checkoutByCommitID(String id, String fileName) {
        CommitNode node = findCommit(id);
        checkoutFile(fileName, node);

    }

    public void checkoutBranch(String branchName) {
        if (!branches.containsKey(branchName)) {
            Main.exitWithError("No such branch exists.");
        }
        else if (currentBranch.equals(branchName)) {
            Main.exitWithError("No need to checkout the current branch.");
        }
        else if (getUntracked().size() > 0) {
            Main.exitWithError("There is an untracked file in the way; " +
                    "delete it, or add and commit it first.");
        }
        else {
            currentBranch = branchName;
            resetToCommit(branches.get(branchName));
        }
        save();
    }

    // Methods for comparing working directory to head commit
    // ======================================================
    public LinkedList<String> getDeleted() {
        LinkedList<String> deletedFiles = new LinkedList<>();
        File file;
        for (String fileName: head().references.keySet()) {
            file = Utils.join(Main.CWD, fileName);
            if (!file.exists() && !toRemove.contains(fileName)) {
                deletedFiles.push(fileName);
            }
        }
        return deletedFiles;
    }

    public LinkedList<String> getChanged() {
        LinkedList<String> modifiedFiles = new LinkedList<>();
        File file;
        for (String fileName: Main.CWD.list()) {
            file = Utils.join(Main.CWD, fileName);
            if (file.isDirectory()) {
                continue;
            }
            String contents = Utils.readContentsAsString(file);
            if (head().references.containsKey(fileName)
                    && !head().references.get(fileName).equals(Utils.sha1(fileName + contents))) {
                modifiedFiles.push(fileName);
            }
        }
        return modifiedFiles;
    }

    public LinkedList<String> getUntracked() {
        LinkedList<String> untrackedFiles = new LinkedList<>();
        File file;
        for (String fileName: Main.CWD.list()) {
            file = Utils.join(Main.CWD, fileName);
            if (file.isDirectory()) {
                continue;
            }
            if (!head().references.containsKey(fileName) &&
                !staged.containsKey(fileName)) {
                untrackedFiles.push(fileName);
            }
        }
        return untrackedFiles;
    }

    public LinkedList<String> getModified() {
        LinkedList modified = new LinkedList<>();
        for (String fileName: getDeleted()) {
            modified.add(fileName + " (deleted)");
        }
        for (String fileName: getChanged()) {
            modified.add(fileName + " (modified)");
        }
        return modified;
    }

    public void printBranches() {
        for (String b: branches.keySet().stream().sorted().collect(Collectors.toList())) {
            if (b.equals(currentBranch)) {
                System.out.print("*");
            }
            System.out.println(b);
        }
    }

    public void printCommitsWithMessage(String message)  {
        LinkedList<String> commitIds = new LinkedList<>();
        root.findCommitsWithMessage(message, commitIds);

        if (commitIds.size() == 0) {
            Main.exitWithError("Found no commit with that message.");
        }
        for (var id: commitIds) {
            System.out.println(id);
        }
    }

    // ======================================================


    public void status() {
        System.out.println("=== Branches ===");
        printBranches();
        System.out.println("\n=== Staged Files ===");
        printStrings(staged.keySet().stream().sorted().collect(Collectors.toList()));
        System.out.println("\n=== Removed Files ===");
        printStrings(toRemove.stream().sorted().collect(Collectors.toList()));
        System.out.println("\n=== Modifications Not Staged For Commit ===");
        printStrings(getModified().stream().sorted().collect(Collectors.toList()));
        System.out.println("\n=== Untracked Files ===");
        printStrings(getUntracked().stream().sorted().collect(Collectors.toList()));
        System.out.println("");
    }

    private void printStrings(List<String> list) {
        for (String s: list) {
            System.out.println(s);
        }
    }

    /**
     * Deletes everything in the staging area.
     */
    private void clearStagingArea() {
        File f;
        for (String fileName: staged.values()) {
            f = Utils.join(STAGING_AREA, fileName);
            f.delete();
        }
        staged.clear();
    }

    /**
     * Creates a branch and stores its name into branches.
     * @param branchName String of branchname
     * @throws Exception if branch name already exists.
     */
    public void branch(String branchName) throws Exception {
        if (branches.containsKey(branchName)) {
            throw new Exception("A branch with that name already exists");
        }
        branches.put(branchName, head());
        save();
    }

    /**
     * Load the current commit tree in .gitlet/commit.dat from bytes.
     * @return The current commit tree object tracking the working initialized
     * directory.
     */
    public static CommitTree load() {
        return Utils.readObject(COMMIT_LOG, CommitTree.class) ;
    }

    /**
     * Serialize and save the CommitTree object to a file.
     */
    public void save() {
        Utils.writeContents(COMMIT_LOG, Utils.serialize(this));
    }

    /**
     * Prints out the string representation of each commit from the current
     * head to the initial commit.
     */
    public void printLog() {
        CommitNode curr = head();
        while (curr != null) {
            System.out.print(curr.toString());
            // Red line in IntelliJ but still compiles?
            curr = curr.parent;
        }
    }

    /**
     * Finds the split point of two commit nodes in different branches.
     * @param A
     * @param B
     * @return
     */
    public CommitNode findSplitPoint(CommitNode A, CommitNode B) {

       while (A != null && B != null) {
           int dateDiff = A.timestamp.compareTo(B.timestamp);
           if (A == B) {
               return A;
           }

           if (dateDiff < 0) {
               if (B.secondParent != null) {
                   CommitNode pathA = findSplitPoint(A, B.parent);
                   CommitNode pathB = findSplitPoint(A, B.secondParent);
                   if (pathA == null && pathB == null) {
                       return null;
                   }
                   else if (pathA == null) {
                       return pathB;
                   }
                   else if (pathB == null) {
                       return pathA;
                   }
                   else {
                       if (pathA.distance > pathB.distance) {
                           return pathA;
                       }
                       return pathB;
                   }
               }
               B = B.parent;
           }
           else {
               if (A.secondParent != null) {
                   CommitNode pathA = findSplitPoint(A.parent, B);
                   CommitNode pathB = findSplitPoint(A.secondParent, B);
                   if (pathA == null && pathB == null) {
                       return null;
                   }
                   else if (pathA == null) {
                       return pathB;
                   }
                   else if (pathB == null) {
                       return pathA;
                   }
                   else {
                       if (pathA.distance > pathB.distance) {
                           return pathA;
                       }
                       return pathB;
                   }
               }
               A = A.parent;
           }

       }
       return null;

    }

    public void printGlobalLog() {
        root.printAll();
    }

    public void rebase(String branchName) {
        if (!branches.containsKey(branchName)) {
            Main.exitWithError("A branch with that name does not exist.");
        }
        else if (currentBranch.equals(branchName)) {
            Main.exitWithError("Cannot rebase a branch onto itself.");
        }

        LinkedList<CommitNode> history = new LinkedList<>();
        CommitNode anchorPoint = branches.get(branchName);
        CommitNode splitPoint = findSplitPoint(head(), anchorPoint);
        if (splitPoint == anchorPoint) {
            Main.exitWithError("Already up-to-date.");
        }
        head().getHistory(splitPoint, history);
        CommitNode current = branches.get(branchName);
        while (history.size() > 0) {
            current = current.mergeChild(history.pop().duplicate());
        }
        branches.replace(branchName, current);
        save();
    }

    private boolean checkRefs(HashMap<String, String> A, HashMap<String, String> B, String v) {
        if (A.get(v) == null || B.get(v) == null) {
            return A.get(v) == null && B.get(v) == null;
        }
        return A.get(v).equals(B.get(v));
    }

    private void handleMergeConflicts(HashSet<String> conflicts, HashMap<String, String> to,
                                      HashMap<String, String> from, HashMap<String, String> nw) {

        File toFile;
        File fromFile;
        File mergeFile;
        File workingFile;
        String mergeFileID;
        String toContents = "";
        String fromContents = "";
        String mergeContents = "";
        for (var s: conflicts) {
            if (to.get(s) != null) {
                toFile = Utils.join(COMMITS, to.get(s));
                toContents = Utils.readContentsAsString(toFile);
            }
            if (from.get(s) != null) {
                fromFile = Utils.join(COMMITS, from.get(s));
                fromContents = Utils.readContentsAsString(fromFile);
            }
            mergeContents = "<<<<<<< HEAD\n" + toContents + "=======\n" + fromContents + ">>>>>>>\n";
            mergeFileID = Utils.sha1(s, mergeContents);
            mergeFile = Utils.join(COMMITS, mergeFileID);
            workingFile = Utils.join(Main.CWD, s);
            Utils.writeContents(mergeFile, mergeContents);
            Utils.writeContents(workingFile, mergeContents);
            nw.put(s, mergeFileID);
        }
    }

    public void merge(String branchName) {
        // same, modified, removed, new w/ respect to split point for A then compare with B
        CommitNode splitPoint = findSplitPoint(head(), branches.get(branchName));
        if (getUntracked().size() > 0) {
            Main.exitWithError("There is an untracked file in the way; delete it, or add and commit it first.");
        }
        else if (staged.size() > 0 || toRemove.size() > 0) {
            Main.exitWithError("You have uncommitted changes.");
        }
        else if (!branches.containsKey(currentBranch)) {
            Main.exitWithError("A branch with that name does not exist.");
        }
        else if (currentBranch.equals(branchName)) {
            Main.exitWithError("Cannot merge a branch with itself.");
        }
        else if (splitPoint == head()) {
            checkoutBranch(branchName);
            Main.exitWithError("Current branch fast-forwarded.");
        }
        else if (splitPoint == branches.get(branchName)) {
            Main.exitWithError("Given branch is an ancestor of the current branch.");
        }

        HashMap<String, String> toRefs = head().references;
        HashMap<String, String> frRefs = branches.get(branchName).references;
        HashMap<String, String> spRefs = splitPoint.references;

        HashSet<String> files = new HashSet<>();
        HashSet<String> filesToDelete = new HashSet<>();
        files.addAll(toRefs.keySet());
        files.addAll(frRefs.keySet());
        files.addAll(spRefs.keySet());

        HashMap<String, String> nwRefs = new HashMap<>();
        HashSet<String> mergeConflicts = new HashSet<>();
        for (var f: files) {
            // Cases when to and from have same file
            if (checkRefs(toRefs, frRefs, f)) {
                if (toRefs.get(f) != null) {
                    nwRefs.put(f, toRefs.get(f));
                }
                else {
                    filesToDelete.add(f);
                }
            }
            else {
                if (checkRefs(spRefs, toRefs, f)) {
                    if (frRefs.get(f) != null) {
                        nwRefs.put(f, frRefs.get(f));
                    }
                    else {
                        filesToDelete.add(f);
                    }
                }
                else if (checkRefs(spRefs, frRefs, f)) {
                    if (toRefs.get(f) != null) {
                        nwRefs.put(f, toRefs.get(f));
                    }
                    else {
                        filesToDelete.add(f);
                    }
                }
                else {
                    mergeConflicts.add(f);
                }
            }
        }
        handleMergeConflicts(mergeConflicts, toRefs, frRefs, nwRefs);
        //File workingFile;
        //for (var f: filesToDelete) {
        //    workingFile = Utils.join(Main.CWD, f);
        //    workingFile.delete();
        //}

        String message = "Merged " + branchName + " into " + currentBranch + ".";
        CommitNode mergeNode = new CommitNode(message, new Date(System.currentTimeMillis()), nwRefs);
        mergeNode.secondParent = branches.get(branchName);
        mergeNode.merges = head().id.substring(0, 7) + " " + branches.get(branchName).id.substring(0, 7);
        //if (sameCommit(mergeNode, head())) {
        //    Main.exitWithError("No changes added to the commit.");
        //}

        head().addChild(mergeNode);
        branches.replace(currentBranch, mergeNode);
        if (mergeConflicts.size() > 0) {
           System.out.println("Encountered a merge conflict.");
        }
        resetToCommit(mergeNode);
        save();
    }

    private boolean sameCommit(CommitNode A, CommitNode B) {
        HashSet<String> files = new HashSet<>();
        files.addAll(A.references.keySet());
        files.addAll(B.references.keySet());
        for (var f: files) {
            if (A.references.get(f) == null || B.references.get(f) == null) {
                if (A.references.get(f) != B.references.get(f)) {
                    return false;
                }
            }

            if (A.references.get(f).equals(B.references.get(f))) {
                return false;
            }
        }
        return true;
    }


    /**
     * Creates a new commit node by overwriting parent commit with new changes.
     * @param message The commit message that should be displayed with the
     *                gitlet log command.
     */
    public void commit(String message) {

        if (staged.isEmpty() && toRemove.isEmpty()) {
            Main.exitWithError("No changes added to the commit.");
        }
        else if (message.equals("")) {
            Main.exitWithError("Please enter a commit message.");
        }

        // Loop over all staged docs
        for (String filename: staged.keySet()) {
            // Get unique SHA-1 associated with filename.
            String fileID = staged.get(filename);
            // Copy staged file to committed file.
            File oldFile = Utils.join(STAGING_AREA, fileID);
            File newFile = Utils.join(COMMITS, fileID);
            String contents = Utils.readContentsAsString(oldFile);
            try {
                newFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Utils.writeContents(newFile, contents);
            //!// Delete staged file after copying
            oldFile.delete();
        }

        HashMap<String, String> newReferences = new HashMap<String, String>();
        newReferences.putAll(head().references);
        // Apply staged removals
        for (String r: toRemove) {
            newReferences.remove(r);
        }
        toRemove.clear();
        // Apply staged additions/modifications;
        newReferences.putAll(staged);
        staged.clear();

        // Create commit node with new data and update head().
        CommitNode newCommit = new CommitNode(
                                    message,
                                    new Date(System.currentTimeMillis()),
                                    newReferences);
        // Red line in IntelliJ but still compiles?
        head().addChild(newCommit);
        //head = newCommit;
        branches.replace(currentBranch, newCommit);
        save();
    }

    /**
     * Removes the file from the directory completely.
     * @param fileName Takes in a string of the file name.
     */
    public void remove(String fileName) {
        if (!staged.containsKey(fileName) && !head().references.containsKey(fileName)) {
            Main.exitWithError("No reason to remove the file.");
        }
        if (head().references.containsKey(fileName)) {
            File fileToRemove = Utils.join(Main.CWD, fileName);
            fileToRemove.delete();
            toRemove.push(fileName);
        }
        if (staged.containsKey(fileName)) {
            File stagedFile = Utils.join(STAGING_AREA, staged.get(fileName));
            stagedFile.delete();
            staged.remove(fileName);
        }
        save();
    }

    /**
     * Adds files to .gitlet/staged and gives them unique ids.
     * @param files the files in the current workspace to be added to the staging
     *              area.
     */
    public void stage(File[] files) {

        File fileAsCopy;
        for (File f: files) {
            toRemove.remove(f.getName());
            String contents = Utils.readContentsAsString(f);
            String id = Utils.sha1(f.getName() + contents);

            if (head().contains(f.getName(), id)) {
                continue;
            }

            fileAsCopy = Utils.join(STAGING_AREA, id);
            if (fileAsCopy.exists()) {
                fileAsCopy.delete();
            }
            try {
                fileAsCopy.createNewFile();
                Utils.writeContents(fileAsCopy,contents);
                // We probably don't need the if statement
                // since put just replaces the key anyways
                if (staged.containsKey(f.getName())) {
                    staged.replace(f.getName(), id);
                }
                else {
                    staged.put(f.getName(), id);
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        save();
    }
}
