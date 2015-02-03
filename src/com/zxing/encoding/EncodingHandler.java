package com.zxing.encoding;


import java.util.Hashtable;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;



public class EncodingHandler {
	// 图片大小,注意图片不要设置太大,否则会影响二维码的识别
	private static final int IMAGE_WIDTH = 30;

	/**
	 * 生成普通二维码方法
	 * 
	 * @param 传入二维码的内容
	 * @return Bitmap 插入到二维码中间的位图对象,可以传入Null,传入NULL后,生成的二维码中间不带图片
	 */
	public Bitmap cretaeBitmap(String str, Bitmap mBitmap) throws Exception {
		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

		// 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
		BitMatrix matrix = new MultiFormatWriter().encode(str,
				BarcodeFormat.QR_CODE, 400, 400, hints);//original size 350*350
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		// 二维矩阵转为一维像素数组,也就是一直横着排了
		int halfW = width / 2;
		int halfH = height / 2;
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				// 如果mBitmap不为空，则添加中间小图片
				if (mBitmap != null && x > halfW - IMAGE_WIDTH
						&& x < halfW + IMAGE_WIDTH && y > halfH - IMAGE_WIDTH
						&& y < halfH + IMAGE_WIDTH) {
					pixels[y * width + x] = mBitmap.getPixel(x - halfW
							+ IMAGE_WIDTH, y - halfH + IMAGE_WIDTH);
				} else {
					if (matrix.get(x, y)) {
						pixels[y * width + x] =0xff000000;//黑色
					}
					else{
						pixels[y * width + x] = 0xffffffff;//填充白色
					}
				}
			}
		}
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		// 通过像素数组生成bitmap位图
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

	/**
	 * 生成中间带图片的二维码
	 * 
	 * @param image
	 *            设置需要放在二维码中间的图片
	 * @content 传入二维码的内容
	 * @resources Resources获得对象
	 */
	public Bitmap createImageViewQRBitmap(Bitmap mBitmap, String content) throws Exception {
		// 构造需要插入的位图对象
		//Bitmap mBitmap = ((BitmapDrawable) resources.getDrawable(image)).getBitmap();
		// 进行缩放图片
		Matrix m = new Matrix();
		float sx = (float) 2 * IMAGE_WIDTH / mBitmap.getWidth();
		float sy = (float) 2 * IMAGE_WIDTH / mBitmap.getHeight();
		m.setScale(sx, sy);
		// 重新构造位图对象
		mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(),
				mBitmap.getHeight(), m, true);
		mBitmap = cretaeBitmap(content, mBitmap);
		return mBitmap;
	}
}







/*ex
package com.zxing.encoding;
 

import java.util.Hashtable;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
/**
 * @author Tianyin Zheng
 *
 */
/*
public final class EncodingHandler {
	private static final int BLACK = 0xff000000;
	
	public static Bitmap createQRCode(String str,int widthAndHeight) throws WriterException {
		//These are a set of hints that you may pass to Writers to specify their behavior.
		Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8"); 
		//BitMatrix
        //width - The preferred width in pixels,
        //height - The preferred height in pixels
        BitMatrix matrix = new MultiFormatWriter().encode(str,
				BarcodeFormat.QR_CODE, widthAndHeight, widthAndHeight);
		//pixels
        int width = matrix.getWidth();
		int height = matrix.getHeight();
		Log.i("tag", "height=" + height);
		Log.i("tag", "weight=" + width);
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (matrix.get(x, y)) {
					pixels[y * width + x] = BLACK;
				}
			}
		}
		//Bitmap
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}
}*/

