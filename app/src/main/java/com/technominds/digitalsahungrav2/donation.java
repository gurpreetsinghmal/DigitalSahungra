package com.technominds.digitalsahungrav2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class donation extends AppCompatActivity  {
    public static final String PAYTM_PACKAGE_NAME = "net.one97.paytm";
    EditText name, amount, note;
    TextView msg;
    Button pay;
    public static String payerName, vpa, msgNote, status, sendAmount;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation);
        name = findViewById(R.id.name);
        amount = findViewById(R.id.amount_et);
        note = findViewById(R.id.note);
        msg = findViewById(R.id.details);
        pay = findViewById(R.id.send);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // getting the text view data

                payerName = name.getText().toString().trim();
                vpa = "9805144775@okbizaxis";
                sendAmount = amount.getText().toString().trim();
                msgNote = note.getText().toString().trim();

                if(payerName.length()>0 && sendAmount.length()>0 && msgNote.length()>0){

                    uri = getPayTmUri(payerName, vpa, msgNote, sendAmount);
                    payWithPayTm(PAYTM_PACKAGE_NAME);

                } else {
                    Toast.makeText(donation.this, "Please fill all the details.", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private static Uri getPayTmUri(String name, String upiId, String note, String amount) {
        return new Uri.Builder()
                .scheme("upi")
                .authority("pay")
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("tn", note)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                .build();
    }

    private void payWithPayTm(String packageName) {
       try{

            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(uri);
            i.setPackage(packageName);
            startActivityForResult(i, 0);

        } catch(Exception e) {
            Toast.makeText(this, "Something went wrong. Please try later", Toast.LENGTH_SHORT).show();
        }


    }

    // Hi all today we are going to integrate paytm payment with android app.
    // without using any sdk.
    // so lets have the demo first.
    // so lets get started.
    // Please like share and dont forget to subscribe.
    // so transaction successful and we also got the transac ref no
    // Thanks for watching see you in the next video


    //checking paytm app is install or not

    public static boolean isAppInstalled(Context context, String packageName) {
        try {
          context.getPackageManager().getApplicationInfo(packageName, 0);
            return false;
        } catch (Exception e) {
            Log.d("isAppInstalled", "isAppInstalled: "+e.toString());
            return false;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            status = data.getStringExtra("Status").toLowerCase();
        }
        if ((RESULT_OK == resultCode) && status.equals("success")) {
            Toast.makeText(donation.this, "Transaction successful." + data.getStringExtra("ApprovalRefNo"), Toast.LENGTH_SHORT).show();
            msg.setText("Transaction successful of ₹" + sendAmount);
            msg.setTextColor(Color.GREEN);

        } else {
            Toast.makeText(donation.this, "Transaction cancelled or failed please try again.", Toast.LENGTH_SHORT).show();
            msg.setText("Transaction Failed of ₹" + sendAmount);
            msg.setTextColor(Color.RED);
        }

    }
}











