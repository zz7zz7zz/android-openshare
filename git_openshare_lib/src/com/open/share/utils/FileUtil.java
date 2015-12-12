package com.open.share.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.os.Environment;

import com.open.share.ContextMgr;


/**
 * 
 * 与文件相关的类,主要负责文件的读写
 * 
 * @author 杨龙辉 2012.04.07
 * 
 */
public final class FileUtil
{

	// ------------------------------ 手机系统相关 ------------------------------
	public static final String NEWLINE = System.getProperty("line.separator");// 系统的换行符
	public static final String APPROOT = "www.name.com";// 程序的根目录
	
	//----------------------------------存放文件的路径后缀------------------------------------
	public static final String CACHE_IMAGE_SUFFIX=File.separator + APPROOT + File.separator + "cache" + File.separator + "images" + File.separator;
	public static final String CACHE_XML_SUFFIX=File.separator + APPROOT + File.separator + "cache" + File.separator + "xmls" + File.separator;
	public static final String CACHE_VOICE_SUFFIX=File.separator + APPROOT + File.separator + "cache" + File.separator + "voice" + File.separator;
	public static final String CACHE_VIDEO_SUFFIX=File.separator + APPROOT + File.separator + "cache" + File.separator + "video" + File.separator;
	public static final String CACHE_LOCATION_SUFFIX=File.separator + APPROOT + File.separator + "cache" + File.separator + "loc" + File.separator;
	public static final String COMPRESS_OUTPUT_SUFFIX=File.separator + APPROOT + File.separator + "compress" + File.separator;
	public static final String UPGRADE_OUTPUT_SUFFIX=File.separator + APPROOT + File.separator + "upgrade" + File.separator;

	// ------------------------------------数据的缓存目录-------------------------------------------------------
	public static String SDCARD_PAHT ;// SD卡路径
	public static String LOCAL_PATH ;// 本地路径,即/data/data/目录下的程序私有目录
	public static String CURRENT_PATH = "";// 当前的路径,如果有SD卡的时候当前路径为SD卡，如果没有的话则为程序的私有目录
	
	public static String CACHE_IMAGE;
	public static String CACHE_XML;
	public static String CACHE_VOICE;
	public static String CACHE_VIDEO;
	public static String CACHE_LOCATION;
	
	public static String COMPRESS_OUTPUT;// 压缩后的图片存放地址
	public static String UPGRADE_OUTPUT;// 升级APK后的存放地址
	
	static
	{
		init(ContextMgr.getContext());
	}

	public static void init(Context context)
	{
		SDCARD_PAHT = Environment.getExternalStorageDirectory().getPath();// SD卡路径
		LOCAL_PATH =context.getApplicationContext().getFilesDir().getAbsolutePath();// 本地路径,即/data/data/目录下的程序私有目录
		
		if(android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
		{
			CURRENT_PATH = SDCARD_PAHT;
		} 
		else
		{
			CURRENT_PATH = LOCAL_PATH;
		}

		CACHE_IMAGE = CURRENT_PATH + CACHE_IMAGE_SUFFIX;
		CACHE_XML = CURRENT_PATH + CACHE_XML_SUFFIX;
		CACHE_VOICE = CURRENT_PATH + CACHE_VOICE_SUFFIX;
		CACHE_VIDEO = CURRENT_PATH + CACHE_VIDEO_SUFFIX;
		CACHE_LOCATION = CURRENT_PATH + CACHE_LOCATION_SUFFIX;

		COMPRESS_OUTPUT = CURRENT_PATH + COMPRESS_OUTPUT_SUFFIX;
		UPGRADE_OUTPUT = CURRENT_PATH + UPGRADE_OUTPUT_SUFFIX;
	}

	/**
	 * 得到与当前存储路径相反的路径(当前为/data/data目录，则返回/sdcard目录;当前为/sdcard，则返回/data/data目录)
	 * @return
	 */
	public static String getDiffPath()
	{
		if(CURRENT_PATH.equals(SDCARD_PAHT))
		{
			return LOCAL_PATH;
		}
		return SDCARD_PAHT;
	}
	
	/**
	 * 
	 * @param pathIn 
	 * @return
	 */
	public static String getDiffPath(String pathIn)
	{
		return pathIn.replace(CURRENT_PATH, getDiffPath());
	}

	// ------------------------------------文件的相关方法--------------------------------------------
	/**
	 * 将数据写入一个文件
	 * 
	 * @param destFilePath
	 *            要创建的文件的路径
	 * @param data
	 *            待写入的文件数据
	 * @param startPos
	 *            起始偏移量
	 * @param length
	 *            要写入的数据长度
	 * @return 成功写入文件返回true,失败返回false
	 */
	public static boolean writeFile(String destFilePath, byte[] data, int startPos, int length)
	{
		try
		{
			if (!createFile(destFilePath))
			{
				return false;
			}
			FileOutputStream fos = new FileOutputStream(destFilePath);
			fos.write(data, startPos, length);
			fos.flush();
			if (null != fos)
			{
				fos.close();
				fos = null;
			}
			return true;

		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 从一个输入流里写文件,该类还需要优化
	 * 
	 * @param destFilePath
	 *            要创建的文件的路径
	 * @param in
	 *            要读取的输入流
	 * @return 写入成功返回true,写入失败返回false
	 */
	public static boolean writeFile(String destFilePath, InputStream in)
	{
		try
		{
			if (!createFile(destFilePath))
			{
				return false;
			}
			FileOutputStream fos = new FileOutputStream(destFilePath);
			int readCount = 0;
			int len = 1024;
			byte[] buffer = new byte[len];
			while ((readCount = in.read(buffer)) != -1)
			{
				fos.write(buffer, 0, readCount);
			}
			fos.flush();
			if (null != fos)
			{
				fos.close();
				fos = null;
			}
			if (null != in)
			{
				in.close();
				in = null;
			}
			return true;
		} catch (IOException e)
		{
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 读取文件，返回以byte数组形式的数据
	 * 
	 * @param filePath
	 *            要读取的文件路径名
	 * @return
	 */
	public static byte[] readFile(String filePath)
	{
		if (isFileExist(filePath))
		{
			try
			{
				FileInputStream fi = new FileInputStream(filePath);
				return readInputStream(fi);
			} catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 从一个数量流里读取数据,返回以byte数组形式的数据。
	 * </br></br>
	 * 需要注意的是，如果这个方法用在从本地文件读取数据时，一般不会遇到问题，但如果是用于网络操作，就经常会遇到一些麻烦(available()方法的问题)。所以如果是网络流不应该使用这个方法。
	 * @param in
	 *            要读取的输入流
	 * @return
	 * @throws IOException
	 */
	public static byte[] readInputStream(InputStream in)
	{
		try
		{
			ByteArrayOutputStream os = new ByteArrayOutputStream();

			byte[] b = new byte[in.available()];
			int length = 0;
			while ((length = in.read(b)) != -1)
			{
				os.write(b, 0, length);
			}

			b = os.toByteArray();

			in.close();
			in = null;

			os.close();
			os = null;

			return b;

		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 读取网络流 
	 * @param in
	 * @return
	 */
	public static byte[] readNetWorkInputStream(InputStream in)
	{
		ByteArrayOutputStream os=null;
		try
		{
			os = new ByteArrayOutputStream();
			
			int readCount = 0;
			int len = 1024;
			byte[] buffer = new byte[len];
			while ((readCount = in.read(buffer)) != -1)
			{
				os.write(buffer, 0, readCount);
			}

			in.close();
			in = null;

			return os.toByteArray();

		} catch (IOException e)
		{
			e.printStackTrace();
		}finally{
			if(null!=os)
			{
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				os = null;
			}
		}
		return null;
	}
	
	
	/**
	 * 读取网络流
	 * @param in
	 * @return
	 */
	public static byte[] readNetWorkInputStream(InputStream in,int inLength)
	{
		try
		{
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			
			byte[] b=null;
			if(inLength>0)
			{
				b = new byte[inLength];
				int length = 0;
				
				while ((length = in.read(b)) != -1)
				{
					os.write(b, 0, length);
				}
				b = os.toByteArray();
			}

			in.close();
			in = null;

			os.close();
			os = null;

			return b;

		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将一个文件拷贝到另外一个地方
	 * @param sourceFile 源文件地址
	 * @param destFile 目的地址
	 * @param shouldOverlay 是否覆盖
	 * @return
	 */
	public static boolean copyFiles(String sourceFile, String destFile,boolean shouldOverlay)
	{
		try
		{
			if(shouldOverlay)
			{
				deleteFile(destFile);
			}
			FileInputStream fi = new FileInputStream(sourceFile);
			writeFile(destFile, fi);
			return true;
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 判断文件是否存在
	 * 
	 * @param filePath
	 *            路径名
	 * @return
	 */
	public static boolean isFileExist(String filePath)
	{
		File file = new File(filePath);
		return file.exists();
	}

	/**
	 * 创建一个文件，创建成功返回true
	 * 
	 * @param filePath
	 * @return
	 */
	public static boolean createFile(String filePath)
	{
		try
		{
			File file = new File(filePath);
			if (!file.exists())
			{
				if (!file.getParentFile().exists())
				{
					file.getParentFile().mkdirs();
				}

				return file.createNewFile();
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 删除一个文件
	 * 
	 * @param filePath
	 *            要删除的文件路径名
	 * @return true if this file was deleted, false otherwise
	 */
	public static boolean deleteFile(String filePath)
	{
		File file = new File(filePath);
		if (file.exists())
		{
			return file.delete();
		}
		return false;
	}

	/**
	 * 字符串转流
	 * @param str
	 * @return
	 */
	public static InputStream String2InputStream(String str)
	{
		ByteArrayInputStream stream = new ByteArrayInputStream(str.getBytes());
		return stream;
	}

	/**
	 * 流转字符串
	 * @param is
	 * @return
	 */
	public static String inputStream2String(InputStream is)
	{
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		StringBuffer buffer = new StringBuffer();
		String line = "";

		try
		{
			while ((line = in.readLine()) != null)
			{
				buffer.append(line);
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return buffer.toString();
	}
}
