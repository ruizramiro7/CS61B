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

public class CommitTree implements Serializable {

    static final File COMMIT_LOG = Utils.join(Main.METADATA_DIR, "commit.dat");
    static final File STAGING_AREA = Utils.join(Main.METADATA_DIR, "stage");
    static final File COMMITS = Utils.join(Main.METADATA_DIR, "commits");

    public class CommitNode implements Serializable {
        private String message;
        private Date timestamp;
        private String id;
        //private HashMap<String, String> references = new HashMap<String, String>();
        private HashMap<String, String> references;
        public CommitNode parent;
        //public CommitNode[] next;
        //public CommitNode prev;

        /**
         * @source https://mkyong.com/java/how-to-get-current-timestamps-in-java/
         * @param message
         */
        public CommitNode(String message, Date timestamp) {

            this.message = message;
            this.timestamp = timestamp;
            references = new HashMap<String, String>();

            String uniqueId = Arrays.stream(references.values().toArray())
                    .map(e -> e.toString()).reduce("", String::concat);
            id = Utils.sha1(uniqueId + timestamp.toString());
        }

        public String toString() {
            SimpleDateFormat format
                    = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z");
            String date = format.format(timestamp);
            String s = "";
            s += "===\n"
              + "commit " + id + "\n"
              + "Date: " + date + "\n"
              + message + "\n";
            return s;
        }

        public boolean contains(String name, String id) {
            if (references.containsKey(name)) {
                return references.get(name).equals(id);
            }
            return false;
        }

    }

    private CommitNode head;
    private CommitNode[] branches;
    private HashMap<String, String> staged;

    public CommitTree() {

        head = new CommitNode("initial commit", new Date(0));
        staged = new HashMap<String, String>();
    }

    public static CommitTree load() {
       return Utils.readObject(COMMIT_LOG, CommitTree.class) ;
    }

    public void save() {
        Utils.writeContents(COMMIT_LOG, Utils.serialize(this));
    }

    public void printLog() {
        CommitNode curr = head;
        while (curr != null) {
            System.out.print(curr.toString());
            curr = curr.parent;
        }
    }

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

        // Create commit node with new data and update head.
        CommitNode newCommit
                = new CommitNode(message, new Date(System.currentTimeMillis()));
        newCommit.parent = head;
        head = newCommit;
        save();
    }

    public void stage(File[] files) {

        File fileAsCopy;
        for (File f: files) {
            String contents = Utils.readContentsAsString(f);
            String id = Utils.sha1(f.getName() + contents);

            if (head.contains(f.getName(), id)) {
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

    //public boolean contains(File file) {

    //}
}
