package br.com.alecindro.face;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.AgeRange;
import com.amazonaws.services.rekognition.model.Attribute;
import com.amazonaws.services.rekognition.model.BoundingBox;
import com.amazonaws.services.rekognition.model.CompareFacesMatch;
import com.amazonaws.services.rekognition.model.CompareFacesRequest;
import com.amazonaws.services.rekognition.model.CompareFacesResult;
import com.amazonaws.services.rekognition.model.ComparedFace;
import com.amazonaws.services.rekognition.model.DetectFacesRequest;
import com.amazonaws.services.rekognition.model.DetectFacesResult;
import com.amazonaws.services.rekognition.model.FaceDetail;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.util.IOUtils;

public class FaceApi {
	
	private static AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.standard().withRegion(Regions.US_EAST_2).build();
	
	public boolean compare(InputStream source, InputStream target, Float similarity) throws IOException {
		ByteBuffer sourceImageBytes= ByteBuffer.wrap(IOUtils.toByteArray(source));
		ByteBuffer targetImageBytes= ByteBuffer.wrap(IOUtils.toByteArray(target));; 
		Image sourceImage = new Image().withBytes(sourceImageBytes);
		Image targetImage = new Image().withBytes(targetImageBytes);
		CompareFacesRequest request= getRequest(sourceImage, targetImage, similarity);
		return compare(request);
	}
	
	public AgeRange findAge(InputStream source) throws IOException {
		ByteBuffer sourceImageBytes= ByteBuffer.wrap(IOUtils.toByteArray(source));
		Image sourceImage = new Image().withBytes(sourceImageBytes);
		DetectFacesRequest request = new DetectFacesRequest().withImage(sourceImage).withAttributes(Attribute.ALL); 
		for(String attribute : request.getAttributes()) {
			System.out.println("Attribute: "+ attribute);
		}
		DetectFacesResult result = rekognitionClient.detectFaces(request); 
		List < FaceDetail > faceDetails = result.getFaceDetails(); 
		for (FaceDetail face: faceDetails) {
			 if (request.getAttributes().contains("ALL")) {
			 return face.getAgeRange(); 
		}
		}
		return null;
	}
	
	private CompareFacesRequest getRequest(Image sourceImage, Image targetImage, Float similarity) {
		return new CompareFacesRequest()
				 .withSourceImage(sourceImage)
				 .withTargetImage(targetImage)
				 .withSimilarityThreshold(similarity); 
	}	
	
	private boolean compare(CompareFacesRequest request) {
		boolean result = true;
		CompareFacesResult compareFacesResult=rekognitionClient.compareFaces(request); 
		 List <CompareFacesMatch> faceDetails = compareFacesResult.getFaceMatches();
		 for (CompareFacesMatch match: faceDetails){
		 ComparedFace face= match.getFace();
		 BoundingBox position = face.getBoundingBox();
		 System.out.println("Face at " + position.getLeft().toString()
		 + " " + position.getTop()
		 + " matches with " + match.getSimilarity().toString()
		 + "% confidence.");
		 }
		 
		 List<ComparedFace> uncompared = compareFacesResult.getUnmatchedFaces();
		 System.out.println("There was " + uncompared.size()
		 + " face(s) that did not match"); 
		 if(uncompared.size() >0) {
			 result = false;
		 }
		 return result;
	}

}
