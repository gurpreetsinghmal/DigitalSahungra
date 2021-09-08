package com.sahungra.digitalsahungra;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.FileUtils;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.util.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sahungra.digitalsahungra.adapter.attachment_adapter;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComplaintActivity extends AppCompatActivity {
    private static final int PICKFILE_REQUEST_CODE = 123;
    EditText name,mobile,msg,title;
    ImageView send,attachment;
    FirebaseAuth auth;
    Toolbar toolbar;
    List<attachment_list> attachment_items;
    String download_url;
    LinearLayout attachment_container;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    LinearLayout progressbar;
    RecyclerView rv;
    attachment_adapter attachment_adapter;
    String key=null;
    attachment_list attachmentfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_complaints);
        name =  findViewById(R.id.etname);
        rv=findViewById(R.id.attachment_rv);
        mobile = findViewById(R.id.ettitle);
        attachment_container=findViewById(R.id.attachment_container);
        msg = findViewById(R.id.etmsg);
        attachment=findViewById(R.id.attachment);
        auth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference("complaints");
        storageReference=FirebaseStorage.getInstance().getReference("complaints/");
        toolbar=findViewById(R.id.complaints_toolbar);
        progressbar=findViewById(R.id.progressbar);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(ComplaintActivity.this,RecyclerView.VERTICAL,false);
        rv.setLayoutManager(linearLayoutManager);

        key=databaseReference.push().getKey();
        setSupportActionBar(toolbar);
        attachment_items=new ArrayList<>();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.getNavigationIcon().setTint(getResources().getColor(R.color.color2));
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        send = findViewById(R.id.sendcomplaint);

        title=findViewById(R.id.ettitle);



        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(name.getText().toString())){
                    Toast.makeText(ComplaintActivity.this, "Name is required", Toast.LENGTH_SHORT).show();
                }else  if(TextUtils.isEmpty(title.getText().toString())){
                    Toast.makeText(ComplaintActivity.this, "title is required", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(msg.getText().toString())){
                    Toast.makeText(ComplaintActivity.this, "Some Message is required", Toast.LENGTH_SHORT).show();
                }else {
                    upload();


                }
                     }
        });

        attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, PICKFILE_REQUEST_CODE);

            }
        });

    }









    private void getPDF() {
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + getPackageName()));
            startActivity(intent);
            return;
        }

        //creating an intent for file chooser
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Media"), PICKFILE_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //when the user choses the file
        if (requestCode == PICKFILE_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //if a file is selected
            if (data.getData() != null) {
                //uploading the file
                uploadFile(data.getData());
            }else{
                Toast.makeText(this, "No file chosen", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = this.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }



    private void uploadFile(final Uri data) {


        attachment_items.clear();

        progressbar.setVisibility(View.VISIBLE);

        final StorageReference sRef = storageReference.child(key );

        sRef.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                      sRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                          @Override
                          public void onComplete(@NonNull Task<Uri> task) {
                               download_url=task.getResult().toString();
                               progressbar.setVisibility(View.GONE);

                              attachmentfile=new attachment_list(key,getFileExtension(data),download_url);
                               attachment_items.add(attachmentfile);

                               attachment_adapter=new attachment_adapter(ComplaintActivity.this,attachment_items);
                               rv.setAdapter(attachment_adapter);
                               attachment_container.setVisibility(View.VISIBLE);

                          }
                      });
                           }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

    }

    private  void upload(){
        complaint_list complaint_list = null;
        if(download_url!=null){

        complaint_list=new complaint_list(name.getText().toString(),title.getText().toString(),auth.getCurrentUser().getPhoneNumber(),msg.getText().toString(),attachmentfile,Calendar.getInstance().getTime(),key);
        }else{
            complaint_list=new complaint_list(name.getText().toString(),title.getText().toString(),auth.getCurrentUser().getPhoneNumber(),msg.getText().toString(),null,Calendar.getInstance().getTime(),key);
        }
        databaseReference.child(key).setValue(complaint_list);
        name.setText("");
        title.setText("");
        msg.setText("");
        attachment_items.clear();
        attachment_container.setVisibility(View.GONE);
        Toast.makeText(this, "Complaint Sent Successfully", Toast.LENGTH_SHORT).show();
    }

}

