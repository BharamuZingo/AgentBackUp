package app.zingo.com.agentapp.Utils;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import app.zingo.com.agentapp.R;


/**
 * Created by Benayah on 29/6/2017.
 */

public class CommonAppMember {

    public static void hideSoftKeyboard(Activity activity, View view) {

        InputMethodManager imm = (InputMethodManager) (activity.getSystemService(Context.INPUT_METHOD_SERVICE));
        // View view = getView();
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    public static void setTitle(Activity context, String title, ActionBar actionBar)
    {
        View viewActionBar = context.getLayoutInflater().inflate(R.layout.actionbar_title, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                android.support.v7.app.ActionBar.LayoutParams.WRAP_CONTENT,
                android.support.v7.app.ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER_VERTICAL);
        TextView textviewTitle = (TextView) viewActionBar.findViewById(R.id.actionbar_textview);
        textviewTitle.setText(title);
        actionBar.setCustomView(viewActionBar, params);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
    }

    public static String saveToInternalStorage(Context context,Bitmap bitmapImage){
        System.out.println("save");
        ContextWrapper cw = new ContextWrapper(context);
        // path to /data/data/yourapp/app_data/imageDir
        File path = Environment.getExternalStorageDirectory();
        File directory = new File(path,"Zingo");//cw.getDir(path.getAbsolutePath()+"/imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        if(!directory.exists())
        {
            directory.mkdir();
        }
        File mypath = new File(directory,"profileImage.jpg");


        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Path = "+mypath.getAbsolutePath());
        return directory.getAbsolutePath();
    }


    public static Bitmap loadImageFromStorage()
    {
        //System.out.println("loadImageFromStorage = "+path);

        try {
            String fileName = "profileImage.jpg";
            File sdCard = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
            File directory = new File (sdCard.getAbsolutePath() + "/Zingo");

            File imageFile = new File(directory, fileName);

            /*File file = new File(imageFile);*/
            /*Uri imageUri = Uri.fromFile(file);*/
            //File f = new File(path, "InsuranceCard.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(imageFile));
            /*ImageView img=(ImageView)findViewById(R.id.imgPicker);
            img.setImageBitmap(b);*/
            return b;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static void deleteImageFromInternalStorage(String path)
    {
        System.out.println("loadImageFromStorage = "+path);

        File f = new File(path, "InsuranceCard.jpg");
        if(f.exists())
        {
            f.delete();
        }


    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context)
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>= Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();

                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

}
