package com.example.stevens.vibcomm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.http.util.EncodingUtils;

import android.os.Environment;

public class FileStoreTools {

	public static String getSDPath() {
		boolean hasSDCard = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
		if (hasSDCard) {
			return Environment.getExternalStorageDirectory().toString();
		} else
			return Environment.getDownloadCacheDirectory().toString();
	}

	public static void saveFile(String str, String filePath) {

		FileOutputStream fos = null;
		try {
			File file = new File(filePath);
			
			if (!file.exists())
				{file.createNewFile();
				}
			fos = new FileOutputStream(file, true);
			
			fos.write(str.getBytes());
			fos.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != fos)
					fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static String readFile(String filePath) {
		FileInputStream fis = null;
		String res = "";
		////////////////////////

		//////////////////////////
		try {
			
			fis = new FileInputStream(new File(filePath));
			//Log.e("  DisplayMetrics", Integer.toString(fis.available()));
			byte[] buffer = new byte[fis.available()];
			//byte[] line = new byte[5];
			fis.read(buffer,0,buffer.length);
			
			res = EncodingUtils.getString(buffer, "UTF-8");
			


			
			//a[1] = Double.parseDouble(res.substring(16+51,27+51));
			//arr = res.toCharArray();
			//Log.e("  DisplayMetrics", res);

			//Log.e("  DisplayMetrics", Double.toString(a[1]));
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != fis)
					fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return res;
	}
}