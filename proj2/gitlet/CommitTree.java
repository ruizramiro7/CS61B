package gitlet;

import java.io.File;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;

public class CommitTree implements Serializable {

    static final File COMMIT_LOG = Utils.join(Main.METADATA_DIR, "commit.dat");
    static final File STAGING_AREA = Utils.join(Main.METADATA_DIR, "stage");
    static final File COMMITS = Utils.join(Main.METADATA_DIR, "commits");

    public class CommitNode implements Serializable {
        private String message;
        private Timestamp timestamp;
        private String id;
        private HashMap<String, String> references;
        public CommitNode parent;
        //public CommitNode[] next;
        //public CommitNode prev;

        /**
         * @source https://mkyong.com/java/how-to-get-current-timestamps-in-java/
         * @param message
         */
        public CommitNode(String message, Timestamp timestamp) {
            this.message = message;
            this.timestamp = timestamp;
            if (references == null) {
                id = Utils.sha1(timestamp.toString());
            }
            else {
                id = Utils.sha1(references.values().toArray(), timestamp.toString());
            }
            System.out.println("New commit with id: " + id);
        }

        public String toString() {
            String s = "";
            s += "===\n";
            s += "commit " + id + "\n";
            s += "Date: " + timestamp.toString() + "\n";
            s += message + "\n";
            return s;
        }

    }

    private CommitNode head;
    private CommitNode[] branches;
    private CommitNode staged;

    public CommitTree() {
        head = new CommitNode("initial commit",
                             new Timestamp(1970, 1, 1, 0, 0, 0, 0));

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
}
