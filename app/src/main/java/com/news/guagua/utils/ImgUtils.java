package com.news.guagua.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ImgUtils {

	private static ImgUtils utils;

	private ImgUtils() {
		// TODO Auto-generated constructor stub
	}

	public static ImgUtils newInstance() {
		if (utils == null) {
			utils = new ImgUtils();
		}

		return utils;
	}

	public Bitmap getBitmapFromResouceId(int id, int max, Context context) {
		Bitmap bitmap = null;
		InputStream inputStream = context.getResources().openRawResource(id);
		bitmap = getBitmapFromInputStream(max, inputStream);
		return bitmap;
	}

	public Bitmap getBitmapFromFile(File file, int maxNumOfPixels) throws Exception {
		Bitmap bitmap=null;
		byte[] data=readStream(new FileInputStream(file));
		bitmap=getBitmapFromByte(data, maxNumOfPixels);
		return bitmap;
	}
	/*
	 * 得到图片字节流 数组大小
	 * */
	public static byte[] readStream(InputStream inStream) throws Exception{      
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();      
        byte[] buffer = new byte[1024];      
        int len = 0;      
        while( (len=inStream.read(buffer)) != -1){      
            outStream.write(buffer, 0, len);      
        }      
        outStream.close();      
        inStream.close();      
        return outStream.toByteArray();      
    }
	public Bitmap getBitmapFromFile(int maxNumOfPixels, File file) {
		Bitmap bitmap = null; 
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		try {
			BitmapFactory.decodeFileDescriptor(inputStream.getFD(),null, opts);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		opts.inSampleSize = computeSampleSize(opts, -1, maxNumOfPixels);
		// end
		opts.inJustDecodeBounds = false;
		try {
				bitmap = BitmapFactory.decodeFileDescriptor(inputStream.getFD(), new Rect(), opts);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bitmap;
	}

	// 使用Bitmap加Matrix来缩放
	public Bitmap resizeImage(Bitmap bitmap, int w, int h) {

		Bitmap bitmapOrg = bitmap;
		int width = 0;
		int height = 0;
		try {
			width = bitmapOrg.getWidth();
			height = bitmapOrg.getHeight();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} finally {
		}

		int newWidth = w;
		int newHeight = h;

		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;

		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// if you want to rotate the Bitmap
		// matrix.postRotate(45);
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0, width, height, matrix, true);
		return resizedBitmap;
		// return new BitmapDrawable(resizedBitmap);
	}

	public Bitmap getBitmapFromInputStream(int maxNumOfPixels, InputStream inputStream) {
		Bitmap bitmap;
		byte[] bytes = getBytes(inputStream);
		bitmap = getBitmapFromByte(bytes, maxNumOfPixels);
		return bitmap;
	}

	public Bitmap getBitmapFromByte(byte[] bytes, int maxNumOfPixels) {
		Bitmap bitmap = null;
		// 锟斤拷3锟斤拷锟角达拷锟斤拷图片锟斤拷锟斤拷锟絙egin( 锟斤拷锟斤拷锟揭拷锟斤拷锟斤拷锟斤拷直锟斤拷
		// opts.inSampleSize=1;)
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
		opts.inSampleSize = computeSampleSize(opts, -1, maxNumOfPixels);
		// end
		opts.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
		return bitmap;
	}

	/**
	 * 锟斤拷锟斤拷锟阶拷锟絙tyle[]锟斤拷锟斤拷
	 */
	private byte[] getBytes(InputStream is) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] b = new byte[1024];
		int len = 0;
		try {
			while ((len = is.read(b, 0, 1024)) != -1) {
				baos.write(b, 0, len);
				baos.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				baos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		byte[] bytes = baos.toByteArray();
		return bytes;
	}

	/****
	 * 锟斤拷锟斤拷图片bitmap size exceeds VM budget 锟斤拷Out Of Memory 锟节达拷锟斤拷锟斤拷锟�
	 */
	private int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}
	/**
     * 把bitmap转成圆形
     * */
    public Bitmap toRoundBitmap(Bitmap bitmap){
            int width=bitmap.getWidth();
            int height=bitmap.getHeight();
            int r=0;
            //取最短边做边长
            if(width<height){
                    r=width;
            }else{
                    r=height;
            }
            //构建一个bitmap
            Bitmap backgroundBm=Bitmap.createBitmap(width,height,Config.ARGB_8888);
            //new一个Canvas，在backgroundBmp上画图 
            Canvas canvas=new Canvas(backgroundBm);
            Paint p=new Paint();
            //设置边缘光滑，去掉锯齿 
            p.setAntiAlias(true);
            RectF rect=new RectF(0, 0, r, r);
            //通过制定的rect画一个圆角矩形，当圆角X轴方向的半径等于Y轴方向的半径时，  
            //且都等于r/2时，画出来的圆角矩形就是圆形 
            canvas.drawRoundRect(rect, r/2, r/2, p);
            //设置当两个图形相交时的模式，SRC_IN为取SRC图形相交的部分，多余的将被去掉
            p.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            //canvas将bitmap画在backgroundBmp上
            canvas.drawBitmap(bitmap, null, rect, p);
            return backgroundBm;
    }
    
	private int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;
		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128
				: (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));
		if (upperBound < lowerBound) {
			return lowerBound;
		}
		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}
}
