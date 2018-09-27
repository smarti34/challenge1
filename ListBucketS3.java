import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.UUID;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

/**
 * This application show a Listing over all S3 buckets in a Amazon account using the
 * AWS SDK for Java.
 * <p>
 * <b>Prerequisites:</b> You must have a valid Amazon Web Services developer
 * account
 * <p>
 * Fill in arguments acces_key and acces_secret_key mandatory and region is optional
 * because Default region is us-east-1
 */
public class ListBucketS3 {

    public static void main(String[] args) throws IOException {

       
    	String region = "us-east-1";
    	if (args.length < 2) {
    		System.out.println("ListBucketS3 acces_key acces_secret_key [region]");
    		System.out.println("Default region: us-east-1");
    		return ;
    	}
    	if (args.length == 3)
    		region = args[2];
        AWSCredentials credentials = null;
        try {
            credentials = new BasicAWSCredentials(args[0],args[1]);
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot valid the credentials." +
                    "Please make sure that your credentials are correct", 
                    e);
        }

        AmazonS3 s3 =  AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(region)
                .build();
        

        System.out.println("==================================================");
        System.out.println("Listing over all S3 buckets in this Amazon account");
        System.out.println("==================================================");

        try {
          
            System.out.println();
            int count;
			int totalSize = count = 0;
            for (Bucket bucket : s3.listBuckets()) {
                
                ObjectListing objectListing = s3.listObjects(new ListObjectsRequest()
                        .withBucketName(bucket.getName()));
                totalSize = 0;
                count = 0;
                for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                	totalSize +=  objectSummary.getSize();
                	count ++;
                }
                System.out.println("Name Bucket: " + bucket.getName());
                System.out.println("Creation: " +bucket.getCreationDate());
                System.out.println("Number of Files: "+ count); 
                System.out.println("Total Size: " + totalSize + " (bytes)");
                System.out.println();
            }
            
        } catch (AmazonServiceException ase) {
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Error Message: " + ace.getMessage());
        }
    }

   

}
