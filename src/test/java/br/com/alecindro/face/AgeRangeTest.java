package br.com.alecindro.face;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import com.amazonaws.services.rekognition.model.AgeRange;

public class AgeRangeTest {
	
	@Test
	public void testeFaceApi() throws IOException {
		InputStream source = new FileInputStream(new File("src/test/resources/examples/crianca10_5.jfif"));
		FaceApi faceApi = new FaceApi();
		AgeRange ageRange = faceApi.findAge(source);
		if(ageRange != null) {
			System.out.println("The detected face is estimated to be between "
					 + ageRange.getLow().toString() + " and " +
					 ageRange.getHigh().toString()
					 + " years old."); 
		}
	}

}
