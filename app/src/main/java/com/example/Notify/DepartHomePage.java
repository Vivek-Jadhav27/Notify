package com.example.Notify;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class DepartHomePage extends AppCompatActivity {

    Button btnInsert;
    Button btnUpload;
    EditText noticeHead;
    EditText noticeBody;
    TextView selectedfile;
    Uri selectedFileUri;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    String pdfName="" , noticetitle,noticeblock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_depart_home_page);

        btnInsert = findViewById(R.id.btninsert);
        btnUpload = findViewById(R.id.btnupload);
        noticeHead = findViewById(R.id.inputnoticehead);
        noticeBody = findViewById(R.id.inputnoticebody);
        selectedfile = findViewById(R.id.selectedpdf);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectFiles();
            }
        });
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsertData();
            }
        });
    }

    private void selectFiles() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Pdf File ...."),1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data!=null && data.getData()!=null){
            selectedFileUri = data.getData();
            pdfName = getFileNameFromUri(selectedFileUri);

        }
        selectedfile.setText(pdfName);
    }

    @SuppressLint("Range")
    private String getFileNameFromUri(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private void UploadFiles(Uri data) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading ..");
        progressDialog.show();

        StorageReference reference = storageReference.child("Uploads/"+pdfName);
        reference.putFile(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                Uri url = uriTask.getResult();
                NoticeItem noticeItem = new NoticeItem(noticeHead.getText().toString(),noticeBody.getText().toString(),url.toString());
                databaseReference.child("Notice").child(databaseReference.push().getKey()).setValue(noticeItem);
                progressDialog.dismiss();

                Toast.makeText(DepartHomePage.this, "Notice Uploaded Successfully", Toast.LENGTH_SHORT).show();

                noticeHead.setText("");
                selectedfile.setText("No File is Selected");
                noticeBody.setText("");
                pdfName = "";

            }

        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = (100.0*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                progressDialog.setMessage("Uploaded :"+(int)progress +"%");
            }
        });

    }

    private void InsertData(){
        String nHead = noticeHead.getText().toString();
        String nBody = noticeBody.getText().toString();
        String id = databaseReference.push().getKey();
        NoticeItem noticeItem = new NoticeItem(nHead,nBody);
        noticetitle = noticeHead.getText().toString();
        noticeblock = noticeBody.getText().toString();
        if(noticetitle.isEmpty()){
            noticeHead.setError("Empty Field");
        }
        if(noticeblock.isEmpty()){
            noticeBody.setError("Empty Field");
        }
        if (pdfName.isEmpty()){
                Toast.makeText(this, " no file is selected", Toast.LENGTH_SHORT).show();
        }
        if(!noticeblock.isEmpty() && !noticetitle.isEmpty() && !pdfName.isEmpty()){
            UploadFiles(selectedFileUri);
        }
        else {
            Toast.makeText(this, "Fill Complete Information", Toast.LENGTH_SHORT).show();
        }

    }
}