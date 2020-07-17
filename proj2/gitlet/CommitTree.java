package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

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

    /**
     * Represents a single commit in the version history.
     */
    public class CommitNode implements Serializable {
        private String message;
        private Date timestamp;
        private String id;
        private HashMap<String, String> references;
        public CommitNode parent;

        /**
         * @param message the commit message that displays when using gitet log
         * @param timestamp the current date and time
         */
        public CommitNode(String message, Date timestamp,
                          HashMap<String, String> references) {

            this.message = message;
            this.timestamp = timestamp;
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
            String s = "";
            s += "===\n"
              + "commit " + id + "\n"
              + "Date: " + date + "\n"
              + message + "\n\n";
            return s;
        }

        /**
         * Returns whether or not the current commit has saved a version of a file.
         * @param name the filename as it appears in the local workspace.
         * @param id the id of the file as determine by a SHA-1 conversion of the
         *           file name + file contents.
         * @return
         */
        public boolean contains(String name, String id) {
            if (references.containsKey(name)) {
                return references.get(name).equals(id);
            }
            return false;
        }

    }

    //!// Should probably make head a method head() that uses currentBranch
    private CommitNode head;
    private String currentBranch;
    // Branches should probably be a HashMap<BRANCH_NAME, COMMIT_NODE>
    private HashMap<String, CommitNode> branches = new HashMap<>();
    // Holds the references to blobs containing 1) the file name 2) the unique id
    private HashMap<String, String> staged = new HashMap<>();

    public CommitTree() {
        CommitNode newHead = new CommitNode("initial commit", new Date(0), null);
        branches.put("master", newHead);
        currentBranch = "master";
    }

    private CommitNode head() {
        return branches.get(currentBranch);
    }

    public void checkout() {
        File workingFile;
        File commitFile;
        for (String fileName: head().references.keySet()) {
            String fileID = head().references.get(fileName);
            commitFile = Utils.join(COMMITS, fileID);
            workingFile = Utils.join(Main.CWD, fileName);
            String contents = Utils.readContentsAsString(commitFile);
            Utils.writeContents(workingFile, contents);
        }
    }

    public void find() {

    }

    public void status() {
        System.out.println("=== Branches ===");
        for (String b: branches.keySet()) {
            if (b == currentBranch) {
                System.out.print("*");
            }
            System.out.println(b);
        }
        System.out.println("\n=== Staged Files ===");
        for (String r: staged.keySet()) {
            System.out.println(r);
        }
        System.out.println("\n=== Removed Files ===");
        System.out.println("\n=== Modifications Not Staged for Commit ===");
        System.out.println("\n=== Untracked Files ===");
        for (String fileName: Main.CWD.list()) {
            File f = Utils.join(Main.CWD, fileName);
            if (f.isDirectory()) {
                continue;
            }
            else if (!head().references.containsKey(fileName)){
                System.out.println(fileName);
            }
        }
    }

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
     * Creates a new commit node by overwriting parent commit with new changes.
     * @param message The commit message that should be displayed with the
     *                gitlet log command.
     */
    public void commit(String message) {

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
        newReferences.putAll(staged);
        staged.clear();
        newReferences.putAll(head().references);

        // Create commit node with new data and update head().
        CommitNode newCommit = new CommitNode(
                                    message,
                                    new Date(System.currentTimeMillis()),
                                    newReferences);
        // Red line in IntelliJ but still compiles?
        newCommit.parent = head();
        //head = newCommit;
        branches.replace(currentBranch, newCommit);
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
