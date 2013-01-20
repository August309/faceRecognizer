package edu.uj.faceRecognizer.faceRecognition.email;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import edu.uj.faceRecognizer.faceRecognition.AppPreferences;

/**
 * User: piotrplaneta
 * Date: 20.01.2013
 * Time: 22:01
 */
public class MailSender {
    public static boolean emailSent = true;

    public static void sendEmail(Context context) {
        MailSender.emailSent = false;
        Mail m = new Mail("f.detection@gmail.com", "detection123");
        AppPreferences preferences = new AppPreferences(context);
        String email = preferences.getEmail();
        Toast.makeText(context, email, Toast.LENGTH_LONG);
        if(email == "none") {
            Toast.makeText(context, "Set email first!", Toast.LENGTH_LONG).show();
            return;
        }

        String[] toArr = {email};
        m.setTo(toArr);
        m.setSubject("Security breach");
        m.setBody("We have recognized one of saved faces");
        try {
            m.addAttachment(Environment.getExternalStorageDirectory().getPath() + "/faceRecognizerTest/test.png");
        }
        catch(Exception e) {
            Log.e("faceRecognizer", e.getMessage(), e);
        }
        (new SendEmailTask(context)).execute(m);
    }
}
