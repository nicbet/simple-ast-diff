# Semantic (AST) Diff for Java

## Quickstart

1. Clone this repository
2. Run the sample `gradle run --args="samples/A.java samples/B.java"`

Will print out a structural diff as follows:
```
* Computing Structural Differences between samples/A.java and samples/B.java
* Creating List of Deltas...
* Traversing Syntax Trees...
* Found the following differences
---------------------------------
[A] Line 13 : System.out.println(files.size());
        In Class FileListing (samples/B.java:83-1001)
        In method File[] listFilesAsArray(File directory, FilenameFilter filter, boolean recurse) (samples/B.java:181-630)

[D] Line 26 : // If the file is a directory and the recurse flag is set, recurse into the directory
        In Class FileListing (samples/A.java:83-1153)
        In method Collection<File> listFiles(File directory, FilenameFilter filter, boolean recurse) (samples/A.java:416-1484)
        In EnhancedForStatement for (File entry : entries) { (samples/A.java:605-1622)

[D] Line 27 : if (recurse && entry.isDirectory()) {
        In Class FileListing (samples/A.java:83-1153)
        In method Collection<File> listFiles(File directory, FilenameFilter filter, boolean recurse) (samples/A.java:416-1484)
        In EnhancedForStatement for (File entry : entries) { (samples/A.java:605-1622)

[D] Line 28 : files.addAll(listFiles(entry, filter, recurse));
        In Class FileListing (samples/A.java:83-1153)
        In method Collection<File> listFiles(File directory, FilenameFilter filter, boolean recurse) (samples/A.java:416-1484)
        In EnhancedForStatement for (File entry : entries) { (samples/A.java:605-1622)
        In IfStatement if (recurse && entry.isDirectory()) { (samples/A.java:918-1931)

[D] Line 29 : }
        In Class FileListing (samples/A.java:83-1153)
        In method Collection<File> listFiles(File directory, FilenameFilter filter, boolean recurse) (samples/A.java:416-1484)
        In EnhancedForStatement for (File entry : entries) { (samples/A.java:605-1622)

[M] Line 23 : if (filter != null || filter.accept(directory, entry.getName())) {
        In Class FileListing (samples/B.java:83-1001)
        In method Collection<File> listFiles(File directory, FilenameFilter filter, boolean recurse) (samples/B.java:452-1368)
        In EnhancedForStatement for (File entry : entries) { (samples/B.java:641-1506)
```