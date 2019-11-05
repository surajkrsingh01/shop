package com.shoppursshop.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.shoppursshop.R;
import com.shoppursshop.adapters.BrowseImageAdapter;
import com.shoppursshop.fragments.BottomSearchFragment;
import com.shoppursshop.interfaces.MyItemClickListener;
import com.shoppursshop.services.FirebaseImageUploadService;
import com.shoppursshop.utilities.Utility;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BaseImageActivity extends NetworkBaseActivity implements MyItemClickListener {

    private final float DEST_WIDTH = 600f;
    private final float DEST_HEIGHT = 800f;

    private int PICK_IMAGE_REQUEST=1;
    private int REQUEST_CAMERA = 0;
    private Uri mHighQualityImageUri;
    protected String imagePath="";
    private String fileName="";
    private Uri filePath;
    private Bitmap bitmap;
    private String userChoosenTask,flag;
    private boolean successfullyUpdated,isNewRider;
    protected String imageBase64;
    protected AlertDialog alertDialog;
    protected RequestOptions requestOptions;

    private List<String> itemList;
    private RecyclerView recyclerView;
    private BrowseImageAdapter itemAdapter;
    private String cat;
    private ProgressBar progress_bar;
    private TextView text_no_files;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestOptions = new RequestOptions();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        // requestOptions.override(Utility.dpToPx(150, context), Utility.dpToPx(150, context));
        requestOptions.centerCrop();
        requestOptions.skipMemoryCache(false);

    }

    protected void selectImage(){
        int view=R.layout.select_image_dialog_layout;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setCancelable(false)
                .setView(view);

        // create alert dialog
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();


        // final TextView textHeader=(TextView) alertDialog.findViewById(R.id.text_header);
        final ImageView imageCancel=(ImageView) alertDialog.findViewById(R.id.image_close);
        final Button btnGallery=(Button) alertDialog.findViewById(R.id.btn_gallery);
        final Button btnCamera=(Button) alertDialog.findViewById(R.id.btn_camera);
        imageCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userChoosenTask = "Camera";
                boolean result = Utility.verifyCameraPermissions(BaseImageActivity.this);
                if (result)
                    cameraIntent();
            }
        });

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userChoosenTask = "Gallery";
                boolean result = Utility.verifyStorageOnlyPermissions(BaseImageActivity.this);
                if (result)
                    galleryIntent();
            }
        });

        alertDialog.show();
    }

    protected void selectChatImage(){
        int view=R.layout.select_chat_attach_dialog;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setCancelable(false)
                .setView(view);

        // create alert dialog
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();


        // final TextView textHeader=(TextView) alertDialog.findViewById(R.id.text_header);
        final ImageView imageCancel=(ImageView) alertDialog.findViewById(R.id.image_close);
        final Button btnGallery=(Button) alertDialog.findViewById(R.id.btn_gallery);
        final Button btnCamera=(Button) alertDialog.findViewById(R.id.btn_camera);
        final Button btn_product = alertDialog.findViewById(R.id.btn_product);
        imageCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userChoosenTask = "Camera";
                boolean result = Utility.verifyCameraPermissions(BaseImageActivity.this);
                if (result)
                    cameraIntent();
            }
        });

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userChoosenTask = "Gallery";
                boolean result = Utility.verifyStorageOnlyPermissions(BaseImageActivity.this);
                if (result)
                    galleryIntent();
            }
        });

        btn_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectProduct();
            }
        });

        alertDialog.show();
    }

    protected void selectProductImage(String cat){

        if(this.cat == null || !this.cat.equals(cat)){
            if(itemList == null)
                itemList = new ArrayList<>();
            else
                itemList.clear();
        }



        int view=R.layout.select_product_image_dialog;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setCancelable(false)
                .setView(view);

        // create alert dialog
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();

      //  itemList.add("https://firebasestorage.googleapis.com/v0/b/shoppurs-2a1ac.appspot.com/o/images%2Fcat%2Fgrc%2Fgrocery.jpg?alt=media&token=3f72a1a9-fc00-4fb5-863b-c148ba0a2975");
        // final TextView textHeader=(TextView) alertDialog.findViewById(R.id.text_header);
        final ImageView imageCancel=(ImageView) alertDialog.findViewById(R.id.image_close);
        final Button btnGallery=(Button) alertDialog.findViewById(R.id.btn_gallery);
        final Button btnCamera=(Button) alertDialog.findViewById(R.id.btn_camera);
        text_no_files = alertDialog.findViewById(R.id.text_no_files);
        progress_bar = alertDialog.findViewById(R.id.progress_bar);
        recyclerView = alertDialog.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        if(itemAdapter == null){
            itemAdapter=new BrowseImageAdapter(this,itemList);
            itemAdapter.setMyItemClickListener(this);
        }

        recyclerView.setAdapter(itemAdapter);

        imageCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userChoosenTask = "Camera";
                boolean result = Utility.verifyCameraPermissions(BaseImageActivity.this);
                if (result)
                    cameraIntent();
            }
        });

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userChoosenTask = "Gallery";
                boolean result = Utility.verifyStorageOnlyPermissions(BaseImageActivity.this);
                if (result)
                    galleryIntent();
            }
        });

        if(this.cat == null || !this.cat.equals(cat)){
            progress_bar.setVisibility(View.VISIBLE);
            browseCategoryImages(cat);
            this.cat = cat;
        }


        alertDialog.show();
    }

    public void selectProduct(){

    }

    protected void browseCategoryImages(String cat){
        listFiles("cat/"+cat,null);
    }

    private void cameraIntent(){
        mHighQualityImageUri = FileProvider.getUriForFile(this,
                getApplicationContext().getPackageName() + ".provider", getFile());
        //filePath = Ur;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mHighQualityImageUri);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public void onSelectFromGalleryResult(Intent data){
        filePath = data.getData();
        Bitmap bmp = null;
        try {
            bmp = BitmapFactory.decodeStream(getContentResolver().openInputStream(filePath));
            saveBitmap(bmp);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // imagePath = filePath.getPath();
        // textViewImageStatus.setVisibility(View.VISIBLE);
        //  textViewImageStatus.setText("Pan card image added.");
        //  imagePath=getPath(filePath);
        try {
            //   bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
            if(bmp != null){
                bitmap = compressImage(imagePath);
                File file = new File(imagePath);
                if(file.exists()){
                    file.delete();
                }
            }else{
                bitmap = compressImage(getRealPathFromURI(filePath.toString()));
            }

           // bitmap=getBitmapFromUri(filePath);
            saveBitmap(bitmap);
            //convertToBase64(new File(imagePath));
            imageAdded();
            //galleryAddPic();
            //  imageByte=AppHelper.getFileDataFromDrawable(this,bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onCaptureImageResult(){
        //convertToBase64(new File(imagePath));
        try {
           // File file = new File(imagePath);
            //Bitmap b = BitmapFactory.decodeFile(file.getAbsolutePath());
            Bitmap compressedBitmap = compressImage(imagePath);
            saveBitmap(compressedBitmap);
            imageAdded();

        }catch (Exception e){
            e.printStackTrace();
        }


        //galleryAddPic();
    }

    public void saveBitmap(Bitmap bitmap){

        Log.i(TAG,"bitmap width "+bitmap.getWidth()+
                " bitmap height "+bitmap.getHeight());

        FileOutputStream out = null;
        File filename = getFile();

        try {
            out = new FileOutputStream(filename);
//          write the compressed bitmap at the destination specified by filename.
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
            out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public File getFile() {
        String date=new SimpleDateFormat("yyyy_MM_dd").format(new Date());
        String time=new SimpleDateFormat("HH:mm:ss").format(new Date());

        String root="";
        if(isExternalStorageAvailable()){
            root = Environment.getExternalStoragePublicDirectory("").toString();
        }
        File myDir = new File(root+"/Shoppurs/Shoppurs photos");
        myDir.mkdirs();
        String fname="";
        fname = date+time+".jpg";

        imagePath=root+"/Shoppurs/Shoppurs photos/"+fname;
        File file = new File (myDir, fname);
        return file;

    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    public String convertToBase64(File file){
        String base64 = "";
        try {
            FileInputStream inputStream = new FileInputStream(file);
            byte[] bytes;
            byte[] buffer = new byte[8192];
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            try {
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            bytes = output.toByteArray();
            base64 = Base64.encodeToString(bytes, Base64.NO_WRAP);
            // imageBase64 = imageBase64.replaceAll("\n","");
            Log.i("PanCard","Image converted into base64");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            base64 = "";
            Log.i("PanCard","Image conversion not successful");
        }

        return base64;
    }

    public static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    public static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.i(TAG,"Request Code "+requestCode+" "+userChoosenTask);
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Camera"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Gallery"))
                        galleryIntent();

                } else {
                    //code for deny
                }
                break;
            case Utility.MY_PERMISSIONS_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Camera"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Gallery"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(alertDialog != null)
            alertDialog.dismiss();

        //  Log.i(TAG,"requestCode code "+requestCode+" "+resultCode+" "+RESULT_OK);

        if (requestCode == PICK_IMAGE_REQUEST){
            if(data != null){
                onSelectFromGalleryResult(data);
            }
        }else if (requestCode == REQUEST_CAMERA){
            Log.i(TAG,"data "+data);
            Log.i(TAG,"image captured "+imagePath);
            onCaptureImageResult();
        }
    }

    protected void imageAdded(){

    }


    private Bitmap compressImage(String path){

        BitmapFactory.Options options = new BitmapFactory.Options();
//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(path, options);

        int origHeight = options.outHeight;
        int origWidth = options.outWidth;

        Log.i(TAG,"width "+origWidth+" height "+origHeight);

        Bitmap scaledBitmap = null;

        float imgRatio = origWidth / origHeight;
        float maxRatio = DEST_WIDTH / DEST_HEIGHT;

        Log.i(TAG,"imgRatio "+imgRatio+" maxRatio "+maxRatio);

        //      width and height values are set maintaining the aspect ratio of the image
        if (origHeight > DEST_HEIGHT || origWidth > DEST_WIDTH) {
            if (imgRatio < maxRatio) {
                imgRatio = DEST_HEIGHT / origHeight;
                origWidth = (int) (imgRatio * origWidth);
                origHeight = (int) DEST_HEIGHT;
            } else if (imgRatio > maxRatio) {
                imgRatio = DEST_WIDTH / origWidth;
                origHeight = (int) (imgRatio * origHeight);
                origWidth = (int) DEST_WIDTH;
            } else {
                origHeight = (int) DEST_HEIGHT;
                origWidth = (int) DEST_WIDTH;
            }
        }

        Log.i(TAG,"width "+origWidth+" height "+origHeight);

        //      setting inSampleSize value allows to load a scaled down version of the original image
        options.inSampleSize = calculateInSampleSize(options, origWidth, origHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(path, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }

        try {
            scaledBitmap = Bitmap.createBitmap(origWidth, origHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = origWidth / (float) options.outWidth;
        float ratioY = origHeight / (float) options.outHeight;
        float middleX = origWidth / 2.0f;
        float middleY = origHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

        //      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(path);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);

            Log.i(TAG,"compressed width "+scaledBitmap.getWidth()+" compressed height "+scaledBitmap.getHeight());
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*if(origWidth > DEST_WIDTH){
            // picture is wider than we want it, we calculate its target height
            int destHeight = origHeight/( origWidth / DEST_WIDTH ) ;
            // we create an scaled bitmap so it reduces the image, not just trim it
            returnBitmap = Bitmap.createScaledBitmap(bitmap, DEST_WIDTH, destHeight, false);
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            // compress to the format you want, JPEG, PNG...
            // 70 is the 0-100 quality percentage
            returnBitmap.compress(Bitmap.CompressFormat.JPEG,70 , outStream);
            Log.i(TAG,"image compressed");

        }else{
            returnBitmap = bitmap;
        }*/

        return scaledBitmap;
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public void listFiles(final String dir,String pageToken){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference listRef = storageRef.child("images/"+dir);
// Fetch the next page of results, using the pageToken if we have one.
        Task<ListResult> listPageTask = pageToken != null
                ? listRef.list(100, pageToken)
                : listRef.list(100);

        listPageTask
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        List<StorageReference> prefixes = listResult.getPrefixes();
                        List<StorageReference> items = listResult.getItems();

                        for(StorageReference storageReference : items){
                            Log.i(TAG,"storage "+storageReference.getBucket());
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Log.i(TAG,"Downloaded "+uri.toString());
                                    itemList.add(uri.toString());
                                    Log.i(TAG,"list size "+itemList.size());
                                    itemAdapter.notifyItemInserted(itemList.size()-1);
                                    if(progress_bar.getVisibility() == View.VISIBLE){
                                        progress_bar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }
                        if(items == null || items.size() == 0){
                            showError();
                        }

                        // Recurse onto next page
                        if (listResult.getPageToken() != null) {
                            Log.i(TAG,"Loading next itmes...");
                            listFiles(dir,listResult.getPageToken());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG,"failed..."+e.getMessage());
                showError();
                // Uh-oh, an error occurred.
            }
        });


    }

    private void showError(){
        text_no_files.setVisibility(View.VISIBLE);
        progress_bar.setVisibility(View.GONE);
    }

    @Override
    public void onItemClicked(int position) {
      imagePath = itemList.get(position);
      browseImageSelected();
      alertDialog.dismiss();
    }

    protected void browseImageSelected(){

    }
}
