package gov.nasa.arc.mct.importExport.provider;

import java.io.File;
import java.io.IOException;

public class TemporaryFolder {

    private File folder;

    /**
     * Create a temporary folder for a unit test.
     */
    public TemporaryFolder() {
    }

    public void create() throws IOException {
        folder = File.createTempFile("testng", "", null);
        folder.delete();
        folder.mkdir();
    }

    public void destroy() {
        if (folder != null) {
            recursiveDelete(folder);
        }
    }

    public File getRoot() {
        if (folder == null) {
            throw new IllegalStateException(
                    "The temporary folder has not yet been created. Did you forget to call the create() method?");
        }
        return folder;
    }

    private void recursiveDelete(File file) {
        File[] files = file.listFiles();
        if (files != null) {
            for (File each : files) {
                recursiveDelete(each);
            }
        }
        file.delete();
    }

    /**
     * Return a new fresh file with the given name under the temporary folder.
     * 
     * @param fileName the file name
     * @return new file
     */
    public File newFile(String fileName) throws IOException {
        File file = new File(getRoot(), fileName);
        if (!file.createNewFile()) {
            new IOException("A file with the name '" + fileName
                    + "' already exists in the test folder");
        }

        return file;
    }

}
