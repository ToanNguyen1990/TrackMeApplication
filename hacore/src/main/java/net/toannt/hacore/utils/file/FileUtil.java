package net.toannt.hacore.utils.file;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.util.Log;

import com.homa.app.demo.hacore.R;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.utils.IOUtils;

import timber.log.Timber;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class FileUtil {

    private static final int BUFFER_SIZE = 2048;

    public static void writeToFile(String data, String fileName, Context context) {
        try {
            OutputStreamWriter streamWriter = new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE));
            streamWriter.write(data);
            streamWriter.close();

            Timber.i("path: %s", fileName);
        } catch (IOException e) {
            Timber.e("File write failed: %s", e.toString());
        }
    }

    public static boolean zipFile(String sourceFile, String nameFileZip) {
        try {
            BufferedInputStream origin;
            FileOutputStream dest = new FileOutputStream(nameFileZip);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
            byte data[] = new byte[BUFFER_SIZE];

            FileInputStream fi = new FileInputStream(sourceFile);
            origin = new BufferedInputStream(fi, BUFFER_SIZE);

            ZipEntry entry = new ZipEntry(sourceFile.substring(sourceFile.lastIndexOf("/") + 1));
            out.putNextEntry(entry);
            int count;

            while ((count = origin.read(data, 0, BUFFER_SIZE)) != -1) {
                out.write(data, 0, count);
            }
            origin.close();

            out.close();
            return true;
        } catch (Exception e) {
            Timber.e("Zip file failed: " + e.toString());
            e.printStackTrace();
            return false;
        }
    }

    public static boolean zipFolder(String sourceFolder, String outputZipPath) {
        try {
            FileOutputStream fos = new FileOutputStream(outputZipPath);
            ZipOutputStream zos = new ZipOutputStream(fos);
            File srcFile = new File(sourceFolder);
            File[] files = srcFile.listFiles();
            Log.d("", "Zip directory: " + srcFile.getName());
            for (int i = 0; i < files.length; i++) {
                Log.d("", "Adding file: " + files[i].getName());
                byte[] buffer = new byte[1024];
                FileInputStream fis = new FileInputStream(files[i]);
                zos.putNextEntry(new ZipEntry(files[i].getName()));
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, length);
                }
                zos.closeEntry();
                fis.close();
            }
            zos.close();
            return true;
        } catch (IOException ioe) {
            Log.e("", ioe.getMessage());
            return false;
        }
    }

    public static void unzip(String _zipFile, String pathFolder) {

        //create target location folder if not exist
        checkDisk(pathFolder);

        try {
            FileInputStream fin = new FileInputStream(_zipFile);
            ZipInputStream zin = new ZipInputStream(fin);
            ZipEntry ze;
            while ((ze = zin.getNextEntry()) != null) {

                //create dir if required while unzipping
                if (ze.isDirectory()) {
                    checkDisk(ze.getName());
                } else {
                    FileOutputStream fileOutPutStream = new FileOutputStream(pathFolder + ze.getName());
                    for (int c = zin.read(); c != -1; c = zin.read()) {
                        fileOutPutStream.write(c);
                    }

                    zin.closeEntry();
                    fileOutPutStream.close();
                }

            }
            zin.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static String createFileTxt(String path, String fileName, String content) throws IOException {
        String file = path + "/" + fileName;
        BufferedWriter writer = new BufferedWriter(new FileWriter(path + "/" + fileName));
        Timber.i("file: " + file);
        writer.write(content);
        writer.close();
        return file;
    }

    private static void checkDisk(String fileName) {
        File file = new File(fileName);
        if (file.exists() && file.isDirectory()) {
            return;
        }

        file.mkdir();
    }

    public static String createTarGZ(String dirPath, String tarGzPath) throws FileNotFoundException, IOException {
        FileOutputStream fOut = null;
        BufferedOutputStream bOut = null;
        GzipCompressorOutputStream gzOut = null;
        TarArchiveOutputStream tOut = null;
        try {

            fOut = new FileOutputStream(new File(tarGzPath));
            bOut = new BufferedOutputStream(fOut);
            gzOut = new GzipCompressorOutputStream(bOut);
            tOut = new TarArchiveOutputStream(gzOut);
            addFileToTarGz(tOut, dirPath, "");
        } finally {
            if (tOut != null) {
                tOut.finish();
            }

            if (tOut != null) {
                tOut.close();
            }

            if (gzOut != null) {
                gzOut.close();
            }

            if (bOut != null) {
                bOut.close();
            }

            if (fOut != null) {
                fOut.close();
            }
        }

        return tarGzPath;
    }

    private static void addFileToTarGz(TarArchiveOutputStream tOut, String path, String base) throws IOException {
        File f = new File(path);
        System.out.println(f.exists());
        String entryName = base + f.getName();
        TarArchiveEntry tarEntry = new TarArchiveEntry(f, entryName);
        tOut.putArchiveEntry(tarEntry);

        if (f.isFile()) {
            IOUtils.copy(new FileInputStream(f), tOut);
            tOut.closeArchiveEntry();
        } else {
            tOut.closeArchiveEntry();
            File[] children = f.listFiles();
            if (children != null) {
                for (File child : children) {
                    System.out.println(child.getName());
                    addFileToTarGz(tOut, child.getAbsolutePath(), entryName + "/");
                }
            }
        }
    }

    public static String getRecordFileName(String appName) {
        String directory = getDownloadDirectory(appName);
        return directory + File.separator + getDownloadFileName();
    }

    private static String getDownloadFileName() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US);
        return formatter.format(date) + ".flv";
    }

    private static String getDownloadDirectory(String folderName) {
        File directory = null;
        boolean isSuccess = false;
        //if there is no SD card, create new directory objects to make directory on device
        if (Environment.getExternalStorageState() == null) {
            //create new file directory object
            directory = new File(Environment.getDataDirectory() + "/" + folderName + "/");
            if (!directory.exists()) {
                isSuccess = directory.mkdir();
                Timber.d("Success 1: %s", isSuccess);
            }
            // if phone DOES have SD card
        } else if (Environment.getExternalStorageState() != null) {
            // search for directory on SD card
            directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + folderName + "/");
            if (!directory.exists()) {
                isSuccess = directory.mkdir();
                Timber.d("Success 2: %s", isSuccess);
            }
        }

        String path = Objects.requireNonNull(directory).getPath();
        Timber.d("Path: %s, success: %s", path, isSuccess);
        return path;
    }

    public static String insertImageToGallery(Context context, Bitmap source){
        SimpleDateFormat sdf = new SimpleDateFormat("MM_dd_yyyy_HHmmss", Locale.getDefault());
        return insertImageToGallery(context, source, sdf.format(new Date()), context.getString(R.string.app_brand_name));
    }

    /**
     * A copy of the Android internals  insertImageToGallery method, this method populates the
     * meta data with DATE_ADDED and DATE_TAKEN. This fixes a common problem where media
     * that is inserted manually gets saved at the end of the gallery (because date is not populated).
     *
     * @see [android.provider.MediaStore.Images.Media.insertImage( ContentResolver , Bitmap, String, String)]
     */
    public static String insertImageToGallery(Context context, Bitmap source, String title, String description) {
        String stringUrl = null;
        try {
            File pictureFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File imagesFolder = new File(pictureFolder, description + "" + context.getString(R.string.common_photos));

            if (!imagesFolder.exists()) {
                imagesFolder.mkdirs();
            }

            File image = new File(imagesFolder.getPath() + File.separator + title + ".png");

            if (source != null) {
                FileOutputStream fos = new FileOutputStream(image);
                source.compress(Bitmap.CompressFormat.PNG, 90, fos);
                fos.close();
                stringUrl = image.getPath();
                MediaScannerConnection.scanFile(context, new String[]{image.getPath()}, null, null);
            }
        } catch (Exception exception) {
            Timber.e(exception);
        }

        return stringUrl;
    }
}
