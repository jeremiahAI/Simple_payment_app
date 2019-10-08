package com.jeremiahvaris.simplepaymentapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.flutterwave.raveandroid.RaveConstants;
import com.flutterwave.raveandroid.RavePayActivity;
import com.flutterwave.raveandroid.RavePayManager;

public class MainActivity extends AppCompatActivity {

    Button payButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        payButton = findViewById(R.id.pay_button); //initialize pay button

        payButton.setOnClickListener(new View.OnClickListener() { // Set Onclicklistener.
            @Override
            public void onClick(View view) {
                pay();
            }
        });
    }

    /**
     * This function calls the Rave SDK to show the payment UI and begin the payment.
     * All the functions called inside this function are from the SDK
     */
    private void pay() {
        double amount = 10.0; //It can be gotten from a customer input into an Edittext, or any other place, but for now, let's set it here


        new RavePayManager(this) // Create a new instance of the RavePayManager object. This is from the SDK and it is what handles the payments for us.
                .setAmount(amount) //Set the amount to charge.
                .setCountry("NG") // Set the country
                .setCurrency("NGN")// Set the currency
                .setEmail("briggsdeborahokorite@gmail.com")// set the email. Note that these values I'm setting that are within quotes are strings. That's because that's how they are meant to be according to the readme.""
                .setfName("Debby") // Set the first name. Also a string
                .setlName("Finest") // Set the last name. These strings can also be passed in as a variable just like with the amount.
                .setNarration("Money for Benz")// Set the narration
                .setTxRef("12345") // A reference for the transaction. It can be any string.

                //THis section addresses what payment methods we want to include. For now, just know that there are different payment methods, and passing true (boolean value) means we want that payment method enabled. PAssing false means otherwise. Play around with these a little bit and see how it affects the UI that's shown when you click Pay
                .acceptMpesaPayments(false)
                .acceptAccountPayments(true)
                .acceptCardPayments(true)
                .acceptAchPayments(false)
                .acceptGHMobileMoneyPayments(true)
                .acceptUgMobileMoneyPayments(true)
                .acceptBankTransferPayments(true)

                .setPublicKey("") // This is the public kay of your RAve account. Get it from your Rave dashboard and put it in here. I'm leaving this one for you to do
                .setEncryptionKey("") // Also get the encryption key from your rave dashboard
                .onStagingEnv(true)// This tells the SDK whether you're in test mode or live mode. If you used live public and encryption keys, then this should be set to false. If you used test keys, it should be set to true.
//                .setSubAccounts(subAccounts)//We can skip this for now
//                .isPreAuth(isPreAuthSwitch.isChecked())// WE can skip this also
//                .showStagingLabel() // This as well
//                .shouldDisplayFee(true) //This too
                //                    .setMeta(meta)
                //                    .withTheme(R.style.TestNewTheme)

                //This guy here is the koko. Without him, nothing will happen. All the former functions just set up the details for the payment. This guy is the one that actually starts the something.
                .initialize();
    }

    /**
     * The reason for overriding this function is because the Rave kini opens it's own activity to handle the payment
     * We need to get a result from that activity to know how our payment went.
     * So that activity sends a result back to this activity, and it's in this onActivityResult
     * function that we handle that response.
     *
     * In this case, we'll just be simply showing the result as a toast
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

            if (requestCode == RaveConstants.RAVE_REQUEST_CODE && data != null) { // That is, if the result is from Rave

                String message = data.getStringExtra("response"); //Extract the message from the response. Further explanation later.

                // The resultCode shows whether the payment was successful, there was an error, or it was cancelled.
                //These if else blocks handle for each case
                if (resultCode == RavePayActivity.RESULT_SUCCESS) {
                    // If it's successful, show a toast saying success and the response from the server (The transaction details)
                    Toast.makeText(this, "SUCCESS \n" + message, Toast.LENGTH_SHORT).show();
                }
                else if (resultCode == RavePayActivity.RESULT_ERROR) {
                    // If there's an error, show a toast saying "Error" and the response from the server (The transaction details)
                    Toast.makeText(this, "ERROR \n" + message, Toast.LENGTH_SHORT).show();
                }
                else if (resultCode == RavePayActivity.RESULT_CANCELLED) {
                    // If it was cancelled, show a toast saying "Payment cancelled"
                    Toast.makeText(this, "PAYMENT CANCELLED", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                super.onActivityResult(requestCode, resultCode, data);
            }
    }
}
