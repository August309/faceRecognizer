package edu.uj.faceRecognizer.faceRecognition;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class PersonActivity extends Activity {
	
    private int counter;
    private static String personName;
    private static final int CAMERA_PIC_REQUEST = 1337;
    private static final int CROP_REQ = 1333;
    private File storageDir;
    private static File[] takenPhotoFiles;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_activity2);
        counter = 0;
        takenPhotoFiles = new File[3];
	}
	
    @Override
    protected void onStart() {
        super.onResume();
        //setContentView(R.layout.person_activity2);
       
        
        storageDir = new File(
        	    Environment.getExternalStorageDirectory(), "faceRecognizer"
        );	
      
    }
    
    public void smile() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
       
	        try {
	        	
	        	System.out.println("B: " + takenPhotoFiles[counter]);
	        	takenPhotoFiles[counter] = createImageFile();
	        	System.out.println("A: " + takenPhotoFiles[counter]);
	        	
				System.out.println(takenPhotoFiles[counter].getAbsolutePath());
				cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(takenPhotoFiles[counter]));
				
				startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
				
				System.out.println("Photo Taken!!!");
				
			} catch (IOException e) {
				System.out.println("Epic fail!");
				e.printStackTrace();
			}
        
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_PIC_REQUEST) {
            if(resultCode == Activity.RESULT_OK) {
                System.out.println("CHECKED!");
                counter += 1;
	                
	             if (counter < 2) {
                	CharSequence text = "Fine, take another!";
                    Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
                    toast.show();
                    
  
                } else {
                	CharSequence text = "Photos captured & saved!";
                    Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
                    toast.show();
                    
                    Intent intent = new Intent("com.android.camera.action.CROP");
   	             
                    for (File f : takenPhotoFiles) {
			             // this will open all images in the Galery
			               System.out.println(f.getAbsolutePath());
			             intent.setDataAndType(Uri.fromFile(f), "image/*");
			             intent.putExtra("crop", "true");
			             // this defines the aspect ration
			             intent.putExtra("aspectX", 4);
			             intent.putExtra("aspectY", 3);
			             // this defines the output bitmap size
			             intent.putExtra("outputX", 200);
			             intent.putExtra("outputY", 150);
			             // true to return a Bitmap, false to directly save the cropped iamge
			             intent.putExtra("return-data", false);
			             //save output image in uri
			             intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
			             startActivity(intent);
                    }
                    
                	Intent intentEnd = new Intent(this, MainActivity.class);
                	startActivity(intentEnd);
                	
                }
                
            } else {
            	takenPhotoFiles[counter].delete();
            }
        }
    }
    
    public void savePerson(View view) {
    	EditText editText = (EditText) findViewById(R.id.personEditText);
    	String message = editText.getText().toString();
    	personName = message;

    	for (int i=0; i<3; i++) {
    		smile();
    	}
    }
    
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = personName + "_" + timeStamp;
        File image = File.createTempFile(
            imageFileName, 
            ".jpg",
            storageDir
        );
        String mCurrentPhotoPath = image.getAbsolutePath();
        System.out.println("path: " + mCurrentPhotoPath);
        return image;
    }
}
