package gitlet;

import java.io.File;
import java.io.IOException;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author Ramiro Ruiz
 *  @author Brandon Bizzarro
 */
public class Main {
    static final File CWD = new File(".");
    static final File METADATA_DIR = Utils.join(CWD, ".gitlet");

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */
    public static void main(String... args) {
        if (args.length == 0) {
            exitWithError("Please enter a command.");
        }
        // Exit if the user tries to use a command other than init before
        // initializing the directory.
        if (!args[0].equals("init") && !checkInit()) {
            exitWithError("Not in an initialized Gitlet directory.");
        }
        switch (args[0]) {
            case "init":
                if (checkInit()) {
                    break;
                }
                initialize(args);
                break;
            case "add":
                add(args);
                break;
            case "commit":
                commit(args);
                break;
            case "rm":
                break;
            case "log":
                log(args);
                break;
            case "global-log":
                break;
            case "find":
                find(args);
                break;
            case "status":
                status(args);
                break;
            case "checkout":
                checkout(args);
                break;
            case "branch":
                branch(args);
                break;
            case "rm-branch":
                break;
            case "reset":
                break;
            case "merge":
                break;
            case "rebase":
                break;
            default:
                exitWithError("No command with that name exists.");
                break;
        }
    }

    public static boolean checkInit() {
        return METADATA_DIR.exists() && CommitTree.COMMIT_LOG.exists();
    }

    public static void initialize(String... args) {
        validateNumArgs(args, 1);

        if (!METADATA_DIR.exists()) {
            METADATA_DIR.mkdir();
        }
        if (!CommitTree.COMMITS.exists()) {
            CommitTree.COMMITS.mkdir();
        }
        if (!CommitTree.STAGING_AREA.exists()) {
            CommitTree.STAGING_AREA.mkdir();
        }
        try {
            CommitTree.COMMIT_LOG.createNewFile();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        CommitTree initCommit = new CommitTree();
        initCommit.save();
    }

    /**
     * Collects all the files specified in the add command and passes
     * them to the CommitTree.
     * @param args Command line arguments containing the add command
     *             and the names of the files to add.
     */
    public static void add(String... args) {
        validateNumArgs(args, 2);

        CommitTree tree = CommitTree.load();
        File[] files = new File[args.length - 1];
        File fileToAdd;
        for (int i = 1; i < args.length; ++i) {
            fileToAdd = new File(args[i]);
            if (!fileToAdd.exists()) {
                exitWithError("File does not exist.");
            }
            else {
                files[i - 1] = fileToAdd;
            }
        }
        tree.stage(files);
    }

    public static void commit(String... args) {
        validateNumArgs(args, 2);
        CommitTree tree = CommitTree.load();
        tree.commit(args[1]);
    }

    public static void log(String... args) {
        validateNumArgs(args, 1);
        CommitTree tree = CommitTree.load();
        tree.printLog();
    }

    public static void find(String... args) {
        validateNumArgs(args, 2);
    }

    public static void status(String... args) {
        validateNumArgs(args, 1);
        CommitTree tree = CommitTree.load();
        tree.status();
    }

    public static void checkout(String... args) {
        validateNumArgs(args, 1);
        CommitTree tree = CommitTree.load();
        tree.checkout();
    }

    public static void branch(String... args) {
        validateNumArgs(args, 2);
        CommitTree tree = CommitTree.load();
        try {
            tree.branch(args[1]);
        } catch (Exception e) {
            exitWithError(e.getMessage());
            //e.printStackTrace();
        }
    }

    /**
     * @source CS61BL Lab09
     * @param message
     */
    public static void exitWithError(String message) {
        if (message != null && !message.equals("")) {
            System.out.println(message);
        }
        System.exit(0);
    }

    /**
     * Checks the number of arguments versus the expected number,
     * throws a RuntimeException if they do not match.
     *
     * @param args Argument array from command line
     * @param n    Number of expected arguments
     */
    public static void validateNumArgs(String[] args, int n) {
        if (args.length != n) {
            throw new RuntimeException(
                    String.format("Incorrect operands."));
        }
    }



}
