package com.com2027.group03;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Ryva on 23/03/2017.
 */

public class ContactPage extends AppCompatActivity {

    Context context = this;
    private TextView fullName;
    private EditText name;
    private TextView emailAdd;
    private EditText email;
    private TextView messageLabel;
    private EditText message;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout linearLayout = new LinearLayout(getApplicationContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        // NEED TO BASICALLY PAD THE TEXTVIEWS

        // TEXTVIEW FOR THE WELCOMING MESSAGE
        // textview welcoming them to the contact page
        TextView contactInfo = new TextView(this);

        // set the text for the textview
        contactInfo.setText(R.string.welcome_contact_message);

        // add the textview to the layout
        linearLayout.addView(contactInfo);

        // TEXTVIEW AND EDITEXT FOR THE NAME
        fullName = new TextView(this);
        name = new EditText(this);

        fullName.setText(R.string.name_label);
        name.setHint(R.string.full_name);

        linearLayout.addView(fullName);
        linearLayout.addView(name);

        // radiobutton for the nature of the email
        final TextView natureLabel = new TextView(this);
        natureLabel.setText(R.string.nature);
        // add the textview to the layout
        linearLayout.addView(natureLabel);

        // array to populate the spinner
        ArrayList<String> natureOptions = new ArrayList<String>();
        natureOptions.add("Complaint");
        natureOptions.add("Compliment");
        natureOptions.add("Recommendation");
        // array adapter to populate the spinner
        final Spinner natureOfEmail = new Spinner(this);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, natureOptions);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        natureOfEmail.setAdapter(spinnerAdapter);
        // add the spinner to the layout
        linearLayout.addView(natureOfEmail);

        // TEXTVIEW AND EDITTEXT FOR THE EMAIL
        emailAdd = new TextView(this);
        email = new EditText(this);

        emailAdd.setText(R.string.email_label);
        email.setHint(R.string.email);

        linearLayout.addView(emailAdd);
        linearLayout.addView(email);


        // TEXTVIEW AND EDITTEXT FOR THE MESSAGE
        messageLabel = new TextView(this);
        message = new EditText(this);

        messageLabel.setText(R.string.message_label);
        message.setHint(R.string.message);

        linearLayout.addView(messageLabel);
        linearLayout.addView(message);

        // BUTTON TO SEND
        Button sendButton = new Button(this);
        sendButton.setText(R.string.send_button);

        linearLayout.addView(sendButton);


        // THIS SEEMS TO WORK - BUT I NEED TO SOURCE IT - http://tekeye.biz/2012/email-contact-form-in-app

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameOfPerson = name.getText().toString();
                String recipient = "rc00389@surrey.ac.uk";
                String emailOfPerson = email.getText().toString();
                String messageOfPerson = message.getText().toString();
                String emailNature = natureOfEmail.getSelectedItem().toString();

                Intent sendMail = new Intent(Intent.ACTION_SEND);

                // put this in a method
                sendMail.putExtra(Intent.EXTRA_EMAIL, new String[]{recipient});
                sendMail.putExtra(Intent.EXTRA_SUBJECT, emailNature);
                sendMail.putExtra(Intent.EXTRA_TEXT, "Dear Valram, \n \n" + messageOfPerson + "\n \n Kind Regards,\n" + nameOfPerson + "\n" + emailOfPerson);


                // forces the app to open a mail app in order to send the email - I THINK
                // changes it from plain text, to getting the values from the field - I THINK
                sendMail.setType("message/rfc822");
                // user can then choose which app to open to send the email - http://stackoverflow.com/questions/2197741/how-can-i-send-emails-from-my-android-application
                try {
                    startActivity(Intent.createChooser(sendMail, "Send email using:"));
                } catch (android.content.ActivityNotFoundException a) {
                    // display a pop up window to the user telling them that there is not an app that they can use
                    AlertDialog.Builder confirmation = new AlertDialog.Builder(context);
                    confirmation.setMessage(R.string.alert_message);
                    confirmation.setCancelable(true);
                    confirmation.setPositiveButton(R.string.okay_button, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.cancel();
                        }
                    });
                    AlertDialog ad = confirmation.create();
                    ad.show();
                }
            }
        });

        setContentView(linearLayout);

    }





}
