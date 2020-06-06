package com.accountbook.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import com.accountbook.model.TestingAppInfo;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class QrUtil {

	public static void generateQRCodeImage(String text, int size, OutputStream out, String logoPath,
			TestingAppInfo appInfo) {

		try {
			// QRCodeWriter qrCodeWriter = new QRCodeWriter();
			// bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE,
			// width, height);

			BitMatrix bitMatrix = createBitMatrix(text, size);

			MatrixToImageWriter.writeToStream(bitMatrix, "PNG", out, new FileInputStream(logoPath), size, appInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void generateQRCodeImage(String text, int size, OutputStream out, InputStream logoStream,
			TestingAppInfo appInfo) {

		try {
			BitMatrix bitMatrix = createBitMatrix(text, size);
			MatrixToImageWriter.writeToStream(bitMatrix, "PNG", out, logoStream, size, appInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		try {
			String outFilePath = "C:/Users/XINJUN/Desktop/qrcode.png";
			FileOutputStream fos = new FileOutputStream(new File(outFilePath));

			// 本地logo file
			String logo = "C:\\Users\\XINJUN\\WorkSpace\\WEB\\AccountBookServer\\WebContent\\WEB-INF\\static\\testing\\images\\app_icon.png";
			// String logo2 =
			// "C:/Users/Administrator/Java/AccountBook/WebContent/WEB-INF/static/images/app_icon.png";
			TestingAppInfo appInfo = new TestingAppInfo();
			appInfo.device = "ios";
			appInfo.developType = "debug";
			generateQRCodeImage("http://xzbenben.cn/AccountBook/testing/detail.html?id=34", 2000, fos, logo, appInfo);

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

		public static void writeToStream(BitMatrix matrix, String format, OutputStream stream, InputStream logStream,
				int size, TestingAppInfo appInfo) throws IOException {

			BufferedImage image = toBufferedImage(matrix);

			// 设置logo图标
			QRCodeFactory logoConfig = new QRCodeFactory();
			image = logoConfig.setMatrixLogo(image, logStream);
			if (appInfo != null)
				image = logoConfig.drawAppInfo(image, appInfo);

			if (!ImageIO.write(image, format, stream)) {
				throw new IOException("Could not write an image of format " + format);
			}
			stream.close();
			logStream.close();
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

			int logoW = matrixWidth / 5;
			int logoH = matrixHeigh / 5;
			int logoX = (matrixWidth - logoW) / 2;
			int logoY = (matrixHeigh - logoH) / 2;

			int rectBorderWidth = logoW / 15;// 图片白色外框宽度
			int rectBorderRadius = (int) (logoW / 1.3f);// 外框圆角

			// System.out.println("logoW=" + logoW);
			// System.out.println("logoH=" + logoH);
			// System.out.println("logoX=" + logoX);
			// System.out.println("logoY=" + logoY);
			// System.out.println("rectBorderWidth=" + rectBorderWidth);
			// System.out.println("rectBorderRadius=" + rectBorderRadius);

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
		 * 画上app相关信息
		 */
		public BufferedImage drawAppInfo(BufferedImage matrixImage, TestingAppInfo appInfo) {

			SizeInfo sizeInfo = SizeInfo.get(matrixImage.getWidth(), matrixImage.getHeight());
			System.out.println("sizeinfo=" + sizeInfo);

			BufferedImage bimage = new BufferedImage(sizeInfo.finalW, sizeInfo.finalH, BufferedImage.TYPE_INT_RGB);

			Graphics2D g2 = bimage.createGraphics();
			g2.setColor(Color.WHITE);
			g2.fillRect(0, 0, bimage.getWidth(), bimage.getHeight());
			g2.drawImage(matrixImage, sizeInfo.qrX, sizeInfo.qrY, matrixImage.getWidth(), matrixImage.getHeight(),
					null);
			
			if("ios".equalsIgnoreCase(appInfo.device))
				sizeInfo.correctionIosDeviceImage();

			String icon = "android".equalsIgnoreCase(appInfo.device) ? androidIcon
					: "ios".equalsIgnoreCase(appInfo.device) ? iosIcon : otherIcon;
			BufferedImage deviceIcon = getBufferImageByBase64(icon);
			g2.drawImage(deviceIcon, sizeInfo.deviceX, sizeInfo.deviceY, sizeInfo.deviceSize, sizeInfo.deviceSize,
					null);

			// g2.setColor(Color.RED);
			// g2.drawLine(0, sizeInfo.deviceY, sizeInfo.finalW,
			// sizeInfo.deviceY);
			// g2.drawLine(0, sizeInfo.deviceY + sizeInfo.deviceSize / 2,
			// sizeInfo.finalW,
			// sizeInfo.deviceY + sizeInfo.deviceSize / 2);
			// g2.drawLine(0, sizeInfo.deviceY + sizeInfo.deviceSize,
			// sizeInfo.finalW,
			// sizeInfo.deviceY + sizeInfo.deviceSize);

			String developText = "debug".equalsIgnoreCase(appInfo.developType) ? "测试版"
					: "release".equalsIgnoreCase(appInfo.developType) ? "正式版" : "未知";
			g2.setFont(sizeInfo.developFont);
			g2.setColor(new Color(85, 85, 85));
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			g2.drawString(developText, sizeInfo.developX, sizeInfo.developY);

			g2.setFont(sizeInfo.tipFont);
			g2.setColor(new Color(204, 204, 204));
			g2.drawString("温馨提示 : 请扫描二维码下载安装包", sizeInfo.tipX, sizeInfo.tipY);

			g2.dispose();
			bimage.flush();
			return bimage;
		}

		private static class SizeInfo {
			// 二维码
			int qrX;
			int qrY;
			int qrW;
			int qrH;
			int qrMargin;

			// 全图
			int finalW;
			int finalH;

			// 信息栏
			int infoH;

			// 设备
			int deviceX;
			int deviceY;
			int deviceSize;

			// 开发类型
			int developX;
			int developY;
			int developFontSize;
			Font developFont;

			// 提示
			int tipX;
			int tipY;
			int tipFontSize;
			Font tipFont;

			public static SizeInfo get(int matrixW, int matrixH) {
				SizeInfo info = new SizeInfo();
				info.qrW = matrixW;
				info.qrH = matrixH;
				info.qrMargin = info.qrW / 10;
				info.qrX = info.qrMargin;
				info.qrY = info.qrMargin;

				info.infoH = info.qrH / 5;

				info.finalW = info.qrW + info.qrMargin * 2;
				info.finalH = info.qrH + info.qrMargin * 2 + info.infoH;

				info.deviceX = info.qrMargin;
				info.deviceY = info.qrMargin + info.qrH + info.infoH / 5;
				info.deviceSize = (int) (info.infoH / 1.8);

				info.developFontSize = (int) (info.infoH / 2.5);
				info.developX = info.deviceX + info.deviceSize + info.qrMargin / 5;
				info.developY = info.deviceY + (int) (info.infoH * 0.4f);
				info.developFont = new Font("微软雅黑", Font.BOLD, info.developFontSize);

				info.tipFontSize = (int) (info.infoH / 6);
				info.tipX = info.deviceX;
				info.tipY = info.deviceY + info.deviceSize + info.tipFontSize + info.infoH / 8;
				info.tipFont = new Font("微软雅黑", Font.PLAIN, info.tipFontSize);

				return info;
			}

			public void correctionIosDeviceImage() {
				deviceY -= infoH / 15;
			}

			@Override
			public String toString() {
				return "SizeInfo [qrX=" + qrX + ", qrY=" + qrY + ", qrW=" + qrW + ", qrH=" + qrH + ", qrMargin="
						+ qrMargin + ", finalW=" + finalW + ", finalH=" + finalH + ", infoH=" + infoH + ", deviceX="
						+ deviceX + ", deviceY=" + deviceY + ", deviceSize=" + deviceSize + ", developX=" + developX
						+ ", developY=" + developY + ", developFontSize=" + developFontSize + ", developFont="
						+ developFont + ", tipX=" + tipX + ", tipY=" + tipY + ", tipFontSize=" + tipFontSize
						+ ", tipFont=" + tipFont + "]";
			}

		}

	}

	/**
	 * 创建我们的二维码图片
	 */
	public static BitMatrix createBitMatrix(String content, int size) throws IOException, WriterException {

		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
		// 指定纠错等级,纠错级别（L 7%、M 15%、Q 25%、H 30%）
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		// 内容所使用字符集编码
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		// hints.put(EncodeHintType.MAX_SIZE, 350);//设置图片的最大值
		// hints.put(EncodeHintType.MIN_SIZE, 100);//设置图片的最小值
		hints.put(EncodeHintType.MARGIN, 0);// 设置二维码边的空度，非负数

		BitMatrix bitMatrix = new MultiFormatWriter().encode(content, // 要编码的内容
				// 编码类型，目前zxing支持：Aztec 2D,CODABAR 1D format,Code 39 1D,Code
				// 93 1D ,Code 128 1D,
				// Data Matrix 2D , EAN-8 1D,EAN-13 1D,ITF (Interleaved Two
				// of Five) 1D,
				// MaxiCode 2D barcode,PDF417,QR Code 2D,RSS 14,RSS
				// EXPANDED,UPC-A 1D,UPC-E 1D,UPC/EAN
				// extension,UPC_EAN_EXTENSION
				BarcodeFormat.QR_CODE, size, // 条形码的宽度
				size, // 条形码的高度
				hints);// 生成条形码时的一些配置,此项可选

		return bitMatrix;
	}

	public static InputStream getImageStreamByBase64(String iconBase64) {
		if (TextUtils.isEmpty(iconBase64))
			return null;

		String[] split = iconBase64.split(",");
		if (split == null || split.length == 0)
			return null;

		return new ByteArrayInputStream(Base64.getDecoder().decode(split[1]));

	}

	static String androidIcon = "data:image/png;base64,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";
	static String iosIcon = "data:image/png;base64,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";
	static String otherIcon = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAGQAAABkCAMAAABHPGVmAAAAkFBMVEUAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADDIYgjAAAAL3RSTlMA8PyABtA7WbkSx6r3wBcJ6tnDou1PA969mpBlDuXh1LKIe8t1ViuVhXBIRCJfMco6uF0AAAGhSURBVGje7dnJcoJAEIDhdgQCyCargIL7Ek3m/d8uFClMVQ4MMy0mVvV/4/QV3c0JoChKNe2WTMNJHATBalVVVZbNm+q61nV9t9vZth2Goeu6C7dYKhvvusMHZlpqxLJgfHjmUslwuVR5qoAUXLK1Jb8PJgMETGUvWs1l2u7bvWhyyI3JITBTuLGESzUFv1UWUspUFgFtw5tcGcWWRZoi2RubKCD+TPQueKSbWGiNiXQTi7RRke7GvFER8KN2Yt4YyAnufV9yOgJS+dB2n9jMezzCS/il2N7jkdiAe9q5R8EgPEisnz1E7cTkEXGxbXaFrHl28Ii4CSGEEEIIIYQQ8v8RJyoPb71dFmhkC8LSBRJxjiDu8gxkjx3X2gNRnxkW4dlm2l/uvMYJE0IIIYMQ1t8DkHh7NfpLcoZFPkBchEQcD8SVSIQZIO6MHZdt+Fp/y0OARTjL9P5Wr/OdEEIIIX+BzJ6BlM9ADIZG5kLEctDIGoSd0EgiRqw50sjRfxrFBd4QJN3HmK0fYFhHU5VghQVDS8vcjieyrcziChRFqfUFUAxZjMfqVF8AAAAASUVORK5CYII=";

	public static BufferedImage getBufferImageByBase64(String iconBase64) {

		InputStream imageStreamByBase64 = getImageStreamByBase64(iconBase64);
		try {
			return ImageIO.read(imageStreamByBase64);
		} catch (IOException e) {
			return null;
		}
	}

}
