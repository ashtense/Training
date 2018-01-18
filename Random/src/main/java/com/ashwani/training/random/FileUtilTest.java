package com.ashwani.training.random;

import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;

public class FileUtilTest {

	public static void main(String[] args) throws InterruptedException, IOException {
		File localFile = new File("D://abc.jpg");
		java.awt.Image awtEditAsset = ImageIO.read(localFile);
		PixelGrabber pixelGrabberEdit = new PixelGrabber(awtEditAsset, 0, 0, -1, -1, false);
		pixelGrabberEdit.grabPixels();
		System.err.println(pixelGrabberEdit.getWidth());
		
		URL url = new URL("http://digistyle.bonprix.net/images/9/9/8/0/prev_15029980.jpg");
		File tempFile = new File("D://def.jpg");
		FileUtils.copyURLToFile(url, tempFile);
		
		java.awt.Image awtEditAsset2 = ImageIO.read(tempFile);
		PixelGrabber pixelGrabberEdit2 = new PixelGrabber(awtEditAsset2, 0, 0, -1, -1, false);
		pixelGrabberEdit2.grabPixels();
		System.err.println(pixelGrabberEdit2.getWidth());
	}
}