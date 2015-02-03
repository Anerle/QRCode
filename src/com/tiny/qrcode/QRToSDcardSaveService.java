package com.tiny.qrcode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

/**
 * 将生成的二维码保存在SD卡
 */
public class QRToSDcardSaveService {

	/**
	 * 将位图对象转换为字节数组
	 * 
	 * @param bm
	 * @return
	 */
	public byte[] Bitmap2Bytes(Bitmap bitmap) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
		return outputStream.toByteArray();
	}

	/**
	 * 保存二维码至SD卡
	 * 
	 * @param filename
	 * @param bitmap
	 */
	public void saveToSDCard(String path, String filename, Bitmap bitmap) throws Exception {
		// 获取SD卡的路径：Environment.getExternalStorageDirectory()
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(path, filename);
		FileOutputStream outStream = new FileOutputStream(file);
		outStream.write(Bitmap2Bytes(bitmap));
		outStream.close();
	}
}
