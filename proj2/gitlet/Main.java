package gitlet;

import java.io.File;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author Ramiro Ruiz
 *  @author Brandon Bizzarro
 */
public class Main {
    static final File CWD = new File(".");
    static final File METADATA_DIR = Utils.join(CWD, ".gitlet");
    static final File COMMIT_LOG = Utils.join(METADATA_DIR, "commits");

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */
    public static void main(String... args) {
        if (args.length == 0) {
            exitWithError("Must have at least one argument");
        }
        switch (args[0]) {
            case "init":
                break;
            case "add":
                break;
            case "commit":
                break;
            case "rm":
                break;
            case "log":
                break;
            case "global-log":
                break;
            case "find":
                break;
            case "status":
                break;
            case "checkout":
                break;
            case "branch":
                break;
            case "rm-branch":
                break;
            case "reset":
                break;
            case "merge":
                break;
            case "rebase":
                break;
        }
    }

    public static void initialize() {

    }

    /**
     * @source CS61BL Lab09
     * @param message
     */
    public static void exitWithError(String message) {
        if (message != null && !message.equals("")) {
            System.out.println(message);
        }
        System.exit(-1);
    }


}
