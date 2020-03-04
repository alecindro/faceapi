package br.com.alecindro.face;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class FaceApiTest {

	@Test
	public void testeFaceApi() throws IOException {
		InputStream source = new FileInputStream(new File("src/test/resources/examples/ivete4.jfif"));
		InputStream target = new FileInputStream(new File("src/test/resources/examples/ivete6.jfif"));
		FaceApi faceApi = new FaceApi();
		faceApi.compare(source, target, 90F);
	}
}
