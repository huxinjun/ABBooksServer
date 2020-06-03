package com.accountbook.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class QrUtil {

	public static void generateQRCodeImage(String text, int width, int height, OutputStream out, String logoPath) {

		try {
			QRCodeWriter qrCodeWriter = new QRCodeWriter();

			BitMatrix bitMatrix;
			bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

			MatrixToImageWriter.writeToStream(bitMatrix, "PNG", out, logoPath);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void generateQRCodeImage(String text, int width, int height, OutputStream out,
			InputStream logoStream) {

		try {
			QRCodeWriter qrCodeWriter = new QRCodeWriter();

			BitMatrix bitMatrix;
			bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

			MatrixToImageWriter.writeToStream(bitMatrix, "PNG", out, logoStream);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		try {
			String outFilePath = "C:/Users/Administrator/Desktop/qrcode.png";
			FileOutputStream fos = new FileOutputStream(new File(outFilePath));

			// 本地logo file
			String logo = "C:/Users/Administrator/Java/AccountBook/WebContent/WEB-INF/static/images/unknow_file.png";
//			String logo2 = "C:/Users/Administrator/Java/AccountBook/WebContent/WEB-INF/static/images/app_icon.png";
			generateQRCodeImage("This is my first QR Code", 1000, 1000, fos, logo);

			// logo流
			// String testLogo =
			// "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAJAAAACQCAMAAADQmBKKAAABR1BMVEUAAACmpqa1tbW1tbW1tbW1tbWRkZG1tbW1tbW1tbW1tbW1tbW1tbW1tbWvr6+qqqr///+1tbWRkZH///+1tbX///+RkZH///////+RkZG1tbWRkZH///+1tbW1tbX///+RkZG1tbW1tbX///+1tbX///+RkZG1tbX///+RkZG1tbWRkZH///+RkZH///////+RkZH///////+RkZG1tbX///////+1tbWRkZG1tbX///+RkZGRkZH///////+1tbX///+RkZHa2tqRkZH///+1tbWRkZH///+RkZGRkZH///+RkZGRkZGRkZGRkZG1tbWRkZH///+RkZH///+1tbWRkZH////R0dGjo6OXl5fb29vLy8ulpaWurq6np6erq6upqam0tLSysrLNzc3Gxsa4uLixsbGwsLDPz8+/v7/BwcG9vb3AwMDibWukAAAAVHRSTlMACuvhFPTh1VrbuXdIHwwH8/Hs2pIL9Ozg1Me6Wk4mFRTkp52cd3dwbm5sSB8eCOTb0cfHvr24nZyYkllQTkhFJib5+aioqKenmZiUk5BdUk1HRkWGJAPkAAAEAklEQVR42u3W13bTQBCA4ZErLnEnhRAINY3Qe++9M2MJZLkmTgK8/zWKS+RgSbaEdzXn4P8JvjM7Ky2MKPJovZSMh6L4rz2dK165cXf120nwn7JUiuOkav0od7ty98FR8FP6dBQnmK6aol65V1/AY5mVME42XTVFVvOrXsakpEI46bbUjshq7txFGLMjYZx8v9WeyGr+OIzTTBJFtK0Oi8o3L4wxnjwKqanaiXKjhpS9hYKqqbai8hnXTYocQ1FVdQfR1ZMu65NAYdX3VAdR0XGRTpxCcdXbqpPo0uMAPFir7HoURRIoMkP7qTqKijZ7lBW0zxZoR3UWXR2+a2dRbG1N011EZ4a+hyi4hqZtqy6i439d+DyKrqJVWi6i3OHLn0ThVa0R2Ypuyjkwq5qmVfQhkf2hKWEUn6FpWlN1Ec1bT7YUSqihmW25ic5Br0wIZVQ1QfWWi2iuP6J1lFJNM2uoLqJV6JZAKTW1/fZcRPPQKY2SqnREWy6ihx3QaZSU0QFVdWfRczBToiipRndEdWdRbn+tl1BahjZK9AAA7qC8qtqIU3sr745ZF82s4rTZRYAIymxH6/XLQXQS0ig1oy9qtmxFD0X+x9xnVLf9r52Dsyi5ZvVgSPqw6AxcR+kZlf5ubw+9IW9AHOXXGCDph0VFCGEgtWv9k9v5uTsgmoM8BlXDqFUrnTG1f+l9UQ4w2Bptw6jV69Vae3tvS2/t/vAKKo8b+swjiMr+RSJARGXfIhEgMkF+RSJA1AN9pxHZiESAyDPIEokAkWeQJRIBIs8gSyQCRJ5BlkgEiJiBiBmImIHIE8jquyAQMQMRMxAxAxEzEDEDETMQMQMRMxAxAxEzEDEDETMQMQMRMxAxAxEzEDEDETMQMQMRMxAxAxEzEDEDETMQMQMRMxAxAxEzEDEDETMQiQGRXxAxAxEzEDED0RQ0BU1BU9AUNAVNQYGDxm4K+j9Az0ho6DUo0EDBe6IQI6vgPRiCBTqIgQfjsEj9OHjwOtymXiw8eBbOUzceHkzBBnVi4sE0LNN+XDwYAbhMRGw8CQB4Q8TGg3cA4D4jDy4BgDLLxxPNgNlLNh58AfttsPFgGjrFuHjC0G2NiQdXoJtS4OEJZaDXeRYeTEE/JcbBE1bgoHsMPHgEBloM3pOEwTZng/bkZ+BQHwP1WAdmdTtYzy34u+y1ID3HsjDU8uXgPIkI2PQkFpTn1AkAZ5F8T/gEOLS5EIQnPgOOLV8LYJ8j4FL2vWxPKQvu3S/I9OQ/wcg2F+V5kjMwTvdicjzhIzBmyvmCeE8opcD4ZdZiYj3hlQx4bOP1rChP9HQa/KR8/rAweU+8tKSA/5a/rr1bXCjM/rsnGoonS+uPIuDeH23u8r83rF7yAAAAAElFTkSuQmCC";
			// ByteArrayInputStream logoInputStream = new ByteArrayInputStream(
			// Base64.getDecoder().decode(testLogo.split(",")[1]));
			// generateQRCodeImage("This is my first QR Code", 1000, 1000, fos,
			// logoInputStream);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	/*
	 * 二维码的生成需要借助MatrixToImageWriter类，该类是由Google提供的，可以将该类直接拷贝到源码中使用，当然你也可以自己写个
	 * 生产条形码的基类
	 */
	public static class MatrixToImageWriter {
		private static final int BLACK = 0xFF000000;// 用于设置图案的颜色
		private static final int WHITE = 0xFFFFFFFF; // 用于背景色

		private MatrixToImageWriter() {
		}

		public static BufferedImage toBufferedImage(BitMatrix matrix) {
			int width = matrix.getWidth();
			int height = matrix.getHeight();
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					image.setRGB(x, y, (matrix.get(x, y) ? BLACK : WHITE));
					// image.setRGB(x, y, (matrix.get(x, y) ?
					// Color.YELLOW.getRGB() : Color.CYAN.getRGB()));
				}
			}
			return image;
		}

		public static void writeToFile(BitMatrix matrix, String format, File file, String logUri) throws IOException {

			System.out.println("write to file");
			BufferedImage image = toBufferedImage(matrix);
			// 设置logo图标
			QRCodeFactory logoConfig = new QRCodeFactory();
			image = logoConfig.setMatrixLogo(image, logUri);

			if (!ImageIO.write(image, format, file)) {
				System.out.println("生成图片失败");
				throw new IOException("Could not write an image of format " + format + " to " + file);
			} else {
				System.out.println("图片生成成功！");
			}
		}

		public static void writeToStream(BitMatrix matrix, String format, OutputStream stream, String logUri)
				throws IOException {

			BufferedImage image = toBufferedImage(matrix);

			// 设置logo图标
			QRCodeFactory logoConfig = new QRCodeFactory();
			image = logoConfig.setMatrixLogo(image, logUri);

			if (!ImageIO.write(image, format, stream)) {
				throw new IOException("Could not write an image of format " + format);
			}
		}

		public static void writeToStream(BitMatrix matrix, String format, OutputStream stream, InputStream logStream)
				throws IOException {

			BufferedImage image = toBufferedImage(matrix);

			// 设置logo图标
			QRCodeFactory logoConfig = new QRCodeFactory();
			image = logoConfig.setMatrixLogo(image, logStream);

			if (!ImageIO.write(image, format, stream)) {
				throw new IOException("Could not write an image of format " + format);
			}
		}
	}

	/**
	 * 本类用于对我们二维码进行参数的设定,生成我们的二维码：
	 * 
	 * @author kingwen
	 */
	public static class QRCodeFactory {

		public BufferedImage setMatrixLogo(BufferedImage matrixImage, String logUri) throws IOException {
			return setMatrixLogo(matrixImage, new FileInputStream(new File(logUri)));
		}

		/**
		 * 给生成的二维码添加中间的logo
		 * 
		 * @param matrixImage
		 *            生成的二维码
		 * @param logUri
		 *            logo地址
		 * @return 带有logo的二维码
		 * @throws IOException
		 *             logo地址找不到会有io异常
		 */
		public BufferedImage setMatrixLogo(BufferedImage matrixImage, InputStream logoInputStream) throws IOException {
			/**
			 * 读取二维码图片，并构建绘图对象
			 */
			Graphics2D g2 = matrixImage.createGraphics();
			// 抗锯齿
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);// 去文字锯齿
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);// 去图片锯齿

			int matrixWidth = matrixImage.getWidth();
			int matrixHeigh = matrixImage.getHeight();

			System.out.println("matrixWidth=" + matrixWidth);
			System.out.println("matrixHeigh=" + matrixHeigh);

			/**
			 * 读取Logo图片
			 */
			BufferedImage logo = ImageIO.read(logoInputStream);

			int logoW = matrixWidth / 7;
			int logoH = matrixHeigh / 7;
			int logoX = (matrixWidth - logoW) / 2;
			int logoY = (matrixHeigh - logoH) / 2;

			int rectBorderWidth = logoW / 15;// 图片白色外框宽度
			int rectBorderRadius = matrixWidth / 10;// 外框圆角

			System.out.println("logoW=" + logoW);
			System.out.println("logoH=" + logoH);
			System.out.println("logoX=" + logoX);
			System.out.println("logoY=" + logoY);
			System.out.println("rectBorderWidth=" + rectBorderWidth);
			System.out.println("rectBorderRadius=" + rectBorderRadius);

			g2.setColor(Color.WHITE);
			// 绘制圆弧矩形
			g2.fillRoundRect(logoX - rectBorderWidth, logoY - rectBorderWidth, logoW + rectBorderWidth * 2,
					logoH + rectBorderWidth * 2, rectBorderRadius, rectBorderRadius);

			// 图片圆角
			g2.setClip(new RoundRectangle2D.Double(logoX, logoY, logoW, logoH, rectBorderRadius, rectBorderRadius));
			g2.drawImage(logo, logoX, logoY, logoW, logoH, null);

			g2.dispose();
			matrixImage.flush();
			return matrixImage;

		}

		/**
		 * 创建我们的二维码图片
		 * 
		 * @param content
		 *            二维码内容
		 * @param format
		 *            生成二维码的格式
		 * @param outFileUri
		 *            二维码的生成地址
		 * @param logUri
		 *            二维码中间logo的地址
		 * @param size
		 *            用于设定图片大小（可变参数，宽，高）
		 * @throws IOException
		 *             抛出io异常
		 * @throws WriterException
		 *             抛出书写异常
		 */
		public void CreatQrImage(String content, String format, String outFileUri, String logUri, int... size)
				throws IOException, WriterException {

			int width = 430; // 二维码图片宽度 430
			int height = 430; // 二维码图片高度430

			// 如果存储大小的不为空，那么对我们图片的大小进行设定
			if (size.length == 2) {
				width = size[0];
				height = size[1];
			} else if (size.length == 1) {
				width = height = size[0];
			}

			Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
			// 指定纠错等级,纠错级别（L 7%、M 15%、Q 25%、H 30%）
			hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
			// 内容所使用字符集编码
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			// hints.put(EncodeHintType.MAX_SIZE, 350);//设置图片的最大值
			// hints.put(EncodeHintType.MIN_SIZE, 100);//设置图片的最小值
			hints.put(EncodeHintType.MARGIN, 1);// 设置二维码边的空度，非负数

			BitMatrix bitMatrix = new MultiFormatWriter().encode(content, // 要编码的内容
					// 编码类型，目前zxing支持：Aztec 2D,CODABAR 1D format,Code 39 1D,Code
					// 93 1D ,Code 128 1D,
					// Data Matrix 2D , EAN-8 1D,EAN-13 1D,ITF (Interleaved Two
					// of Five) 1D,
					// MaxiCode 2D barcode,PDF417,QR Code 2D,RSS 14,RSS
					// EXPANDED,UPC-A 1D,UPC-E 1D,UPC/EAN
					// extension,UPC_EAN_EXTENSION
					BarcodeFormat.QR_CODE, width, // 条形码的宽度
					height, // 条形码的高度
					hints);// 生成条形码时的一些配置,此项可选

			// 生成二维码图片文件
			File outputFile = new File(outFileUri);

			// 指定输出路径
			System.out.println("输出文件路径为" + outputFile.getPath());

			MatrixToImageWriter.writeToFile(bitMatrix, format, outputFile, logUri);
		}
	}

}
