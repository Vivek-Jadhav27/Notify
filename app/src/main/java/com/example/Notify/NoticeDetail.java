package com.example.Notify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class NoticeDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_detail);

        String noticeHead = getIntent().getStringExtra("noticeHead");
        String noticeBody = getIntent().getStringExtra("noticeBody");
        String pdfUrl = getIntent().getStringExtra("pdfUrl");
        TextView noticeHeadText = findViewById(R.id.noticeTitle);
        TextView noticeBodyText = findViewById(R.id.noticeDescription);
        CardView openPdfButton = (CardView) findViewById(R.id.noticepdfcard);
        TextView noticepdfText = findViewById(R.id.filename);
        String fileName = pdfUrl.substring(pdfUrl.lastIndexOf("%2F") + 3, pdfUrl.indexOf("?alt="));
        fileName = fileName.replace("%2F", "/");
        noticeHeadText.setText(noticeHead);
        noticeBodyText.setText(noticeBody);
        noticepdfText.setText(fileName);
        System.out.println("File Name: " + fileName);
        openPdfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the PDF file using an Intent
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(pdfUrl));
                startActivity(intent);
            }
        });
    }
}