package com.shoppursshop.print;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Environment;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Created by suraj kumar singh on 21-10-2016.
 */
public class Activity_To_Pdf {
   static File pdfFile;

    public static Bitmap takeScreenshot(AppCompatActivity activity) { //this is ok with cut windos actionbar , spacing on all sided
        View view = activity.getWindow().getDecorView();     // Log.d("Activity 1",""+activity.getWindow().getDecorView());
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int statusBarHeight = rect.top;
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay().getHeight();

        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        Bitmap bitmap2 = Bitmap.createBitmap(bitmap, 0, statusBarHeight, width,
                height - statusBarHeight);
        view.destroyDrawingCache();
        return bitmap2;
    }

    public static File createDirectory(String dirName, String fileName){
        String state = Environment.getExternalStorageState(); //First Check if the external storage is writable
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
        }else {
            File pdfDir = new File(Environment.getExternalStorageDirectory() +dirName); //Create a directory for your PDF
            if (!pdfDir.exists()) {
                pdfDir.mkdir();
            }
            pdfFile = new File(pdfDir, fileName); //Now create the name of your PDF file that you will generate
        }
        return pdfFile;
    }

    public static void createPDF(Bitmap screen, File pdfFile){
        //create Pdf from image in android
        try {
            Document document = new Document();              //Log.d("Size Document","W "+document.getPageSize().getWidth() +"H "+document.getPageSize().getHeight());
            PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            document.open();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            screen.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            addImage(document,byteArray);
            document.close();            // Log.d("File Created ",""+pdfFile);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    private static void addImage(Document document, byte[] byteArray)
    {
       float documentWidth = document.getPageSize().getWidth()- document.leftMargin() - document.rightMargin();
        float documentHeight = document.getPageSize().getHeight() - document.topMargin() - document.bottomMargin();

        Image image = null;
        try
        {
            image = Image.getInstance(byteArray);
            image.scaleToFit(documentWidth, documentHeight);
            image.setAlignment(Element.ALIGN_CENTER);
        }
        catch (BadElementException e)
        {
            e.printStackTrace();
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        // image.scaleAbsolute(150f, 150f);
        try
        {
            document.add(image);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

}
