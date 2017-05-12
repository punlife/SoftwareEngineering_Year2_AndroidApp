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
    // field for full name text
    private TextView fullName;
    // field for the person's full name
    private EditText name;
    // field for email address text
    private TextView emailAdd;
    // field for the person's email
    private EditText email;
    // field for the message label
    private TextView messageLabel;
    // field for the person's message
    private EditText message;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // declare a linear layout for the page with a vertical (portrait) orientation
        LinearLayout linearLayout = new LinearLayout(getApplicationContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        // TextView welcoming them to the contact page
        TextView contactInfo = new TextView(this);

        // set the text for the TextView
        contactInfo.setText(R.string.welcome_contact_message);

        // add the TextView to the layout
        linearLayout.addView(contactInfo);

        // TextView and EditText for the full name
        fullName = new TextView(this);
        name = new EditText(this);

        // set the label for the name field
        fullName.setText(R.string.name_label);
        // add a hint to the name field
        name.setHint(R.string.full_name);

        // add the TextView and EditText to the layout so that they display
        linearLayout.addView(fullName);
        linearLayout.addView(name);

        // RadioButton for the nature of the email
        final TextView natureLabel = new TextView(this);
        natureLabel.setText(R.string.nature);
        // add the TextView to the layout
        linearLayout.addView(natureLabel);

        // array to populate the spinner for the nature of the email
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

        // TextView and EditText for the email
        emailAdd = new TextView(this);
        email = new EditText(this);

        // set the text for the email label and hint
        emailAdd.setText(R.string.email_label);
        email.setHint(R.string.email);

        // add the email label and field to the layout
        linearLayout.addView(emailAdd);
        linearLayout.addView(email);

        // TextView and EditText for the message
        messageLabel = new TextView(this);
        message = new EditText(this);

        // set the text for the message label and hint
        messageLabel.setText(R.string.message_label);
        message.setHint(R.string.message);

        // add the message label and field to the layout
        linearLayout.addView(messageLabel);
        linearLayout.addView(message);

        // declare the send button and add text to it
        Button sendButton = new Button(this);
        sendButton.setText(R.string.send_button);

        // add the send button to the layout
        linearLayout.addView(sendButton);


        // SRC: http://tekeye.biz/2012/email-contact-form-in-app

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // convert the text entered into the fields into a string
                String nameOfPerson = name.getText().toString();
                // declare the email receiving the email being sent by the user
                String recipient = "rc00389@surrey.ac.uk";
                String emailOfPerson = email.getText().toString();
                String messageOfPerson = message.getText().toString();
                String emailNature = natureOfEmail.getSelectedItem().toString();

                // start a new intent when the send mail button is clicked
                // the user can select which application will send the email
                Intent sendMail = new Intent(Intent.ACTION_SEND);

                // put the text retrieved from the fields into the relevant places in an email
                sendMail.putExtra(Intent.EXTRA_EMAIL, new String[]{recipient});
                sendMail.putExtra(Intent.EXTRA_SUBJECT, emailNature);
                // write the body of the email
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
                    // if no app to send the email with is found, print a message telling the user
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
