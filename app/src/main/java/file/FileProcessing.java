package file;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import setting.AppEnvironment;

public class FileProcessing {

    public void createWriteFile(String rawFileName, boolean isUniqueFile, Context pContext, String data) {

        String fileName;

        if (isUniqueFile) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
            Date now = new Date();
            String uniqueName = formatter.format(now);
            fileName = rawFileName + uniqueName + AppEnvironment.FILE_EXTENSION;

        } else {
            fileName = rawFileName + AppEnvironment.FILE_EXTENSION;
        }

        File file = new File(pContext.getExternalFilesDir(null), fileName);
        try {

            FileOutputStream os = new FileOutputStream(file, true);
            OutputStreamWriter out = new OutputStreamWriter(os);
            out.write(data);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
