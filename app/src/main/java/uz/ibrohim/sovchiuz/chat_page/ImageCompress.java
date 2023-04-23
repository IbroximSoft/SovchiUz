package uz.ibrohim.sovchiuz.chat_page;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.File;
import java.io.IOException;

import id.zelory.compressor.Compressor;

public class ImageCompress {

    public static File compress(Context context,File file){
        File file1 = null;

        try {
            file1 = new Compressor(context)
                    .setMaxWidth(640)
                    .setMaxHeight(640)
                    .setQuality(60)
                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                    .compressToFile(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return file1;
    }

}
