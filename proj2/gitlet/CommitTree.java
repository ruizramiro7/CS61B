package gitlet;

import java.io.File;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;

public class CommitTree implements Serializable {

    static final File COMMIT_LOG = Utils.join(Main.METADATA_DIR, "commits");

    public class CommitNode {
        private String message;
        private Timestamp timestamp;
        private HashMap<Integer, String> references;
        public CommitNode[] next;
        public CommitNode prev;

    }

    private CommitNode head;
    private CommitNode staged;

    public void stage() {
    }

    public static CommitTree load() {
       return Utils.readObject(COMMIT_LOG, CommitTree.class) ;
    }

    public void save() {
        Utils.writeContents(COMMIT_LOG, Utils.serialize(this));
    }
}
