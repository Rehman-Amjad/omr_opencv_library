package com.example.omr.SL_STUDENT;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.omr.R;
import com.example.omr.DATABASE.todatabase;
import com.example.omr.SHAREDPREFRENCES.SHAREDPREF;
import com.example.omr.SL_TEACHER.SIGNUP;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp_Fragment extends Fragment implements OnClickListener {
	private static View view;
	private static EditText fullName, emailId, mobileNumber, location,
			password, confirmPassword, classname;
	private static TextView login;
	private static Button signUpButton;
	private static CheckBox terms_conditions;
	String getFullName, getEmailId, getMobileNumber, getRollno, getPassword, getclassname;
	private todatabase db;
	Boolean valid;

	DatabaseReference ref;
	private SHAREDPREF sharedpref;

	public SignUp_Fragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.signup_layout, container, false);
		ref = FirebaseDatabase.getInstance().getReference("STUDENT");
		initViews();
		setListeners();


		return view;
	}

	// Initialize all views
	private void initViews() {
		fullName = (EditText) view.findViewById(R.id.fullName);
		emailId = (EditText) view.findViewById(R.id.userEmailId);
		mobileNumber = (EditText) view.findViewById(R.id.mobileNumber);
		location = (EditText) view.findViewById(R.id.Rollno_student);
		password = (EditText) view.findViewById(R.id.password);
		confirmPassword = (EditText) view.findViewById(R.id.confirmPassword);
		signUpButton = (Button) view.findViewById(R.id.signUpBtn);
		login = (TextView) view.findViewById(R.id.already_user);
		terms_conditions = (CheckBox) view.findViewById(R.id.terms_conditions);
		classname = view.findViewById(R.id.Classname_student);

		// Setting text selector over textviews
		@SuppressLint("ResourceType") XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
		try {
			ColorStateList csl = ColorStateList.createFromXml(getResources(),
					xrp);

			login.setTextColor(csl);
			terms_conditions.setTextColor(csl);
		} catch (Exception e) {
		}
	}

	// Set Listeners
	private void setListeners() {
		signUpButton.setOnClickListener(this);
		login.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.signUpBtn:
//				Log.d("RETURN", "onClick: " + checkValidation());
				signUpButton.setClickable(false);
				// Call checkValidation method
				getFullName = fullName.getText().toString().trim().toUpperCase();
				getEmailId = emailId.getText().toString().trim().toUpperCase();
				getMobileNumber = mobileNumber.getText().toString().trim().toUpperCase();
				getRollno = location.getText().toString().trim().toUpperCase();
				getclassname = classname.getText().toString().toUpperCase();
				getPassword = password.getText().toString();

				alreadyexist();


				break;

			case R.id.already_user:

				// Replace login fragment
				new MainActivity().replaceLoginFragment();
				break;
		}

	}

	// Check Validation Method
	private Boolean checkValidation() {

		// Get all edittext texts
		valid = true;
		String getConfirmPassword = confirmPassword.getText().toString();
		// Pattern match for email id
		Pattern p = Pattern.compile(Utils.regEx);
		Matcher m = p.matcher(getEmailId);

		// Check if all strings are null or not
		if (getFullName.equals("") || getFullName.length() == 0
				|| getEmailId.equals("") || getEmailId.length() == 0
				|| getMobileNumber.equals("") || getMobileNumber.length() == 0
				|| getRollno.equals("") || getRollno.length() == 0
				|| getPassword.equals("") || getPassword.length() == 0
				|| getConfirmPassword.equals("")
				|| getConfirmPassword.length() == 0 || getclassname.equals("") || getclassname.length() == 0) {

			new CustomToast().Show_Toast(getActivity(), view,
					"All fields are required.");

			valid = false;
		}

		// Check if email id valid or not
		else if (!m.find()) {
			new CustomToast().Show_Toast(getActivity(), view,
					"Your Email Id is Invalid.");
			valid = false;
		}

		// Check if both password should be equal
		else if (!getConfirmPassword.equals(getPassword)) {
			new CustomToast().Show_Toast(getActivity(), view,
					"Both password doesn't match.");
			valid = false;
		}

		// Make sure user should check Terms and Conditions checkbox
		else if (!terms_conditions.isChecked()) {
			new CustomToast().Show_Toast(getActivity(), view,
					"Please select Terms and Conditions.");
			valid = false;
		}
		return valid;
	}

	public void alreadyexist() {
		Log.d("STUDENT_VALUES", "alreadyexist: " + getRollno);
		DatabaseReference ref;
		ref = FirebaseDatabase.getInstance().getReference("STUDENT");
		ref.orderByChild("rollno").equalTo(getRollno).addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

				if (dataSnapshot.hasChildren()) {
					signUpButton.setClickable(true);
					Log.d("STUDENT_ROLL", "onDataChange: " + dataSnapshot.hasChildren());
					new CustomToast().Show_Toast(getActivity(), view,
							"Rollno Already Exist");
				} else {
					Log.d("STUDENT_ROLL", "onDataChange3: " + dataSnapshot.hasChildren());
					create_account();
					signUpButton.setClickable(true);
				}
			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {

			}
		});

	}

	public void create_account() {
		if (checkValidation()) {
			sharedpref = new SHAREDPREF(getContext());
			db = new todatabase(ref, getContext());
			Log.d("VALUES_CHECK", "onClick: " + getRollno + " : " + getclassname);
			db.validate_rollno(getRollno, getclassname);
			String validate = sharedpref.getRollno();
			//Log.d("VALUES", "onCreateView: " + validate);
			if (validate.equals("true")) {
				db.signup_student(getFullName, getEmailId, getRollno, getPassword, getclassname);
				new CustomToast().Show_Toast(getActivity(), view,
						"ACCOUNT CREATED SUCCESSFULLY");

				getFragmentManager().beginTransaction()
						.setCustomAnimations(R.anim.right_enter, R.anim.left_out)
						.replace(R.id.frameContainer, new Login_Fragment(),
								Utils.Login_Fragment).commit();

			} else {
				signUpButton.setClickable(true);
				new CustomToast().Show_Toast(getActivity(), view,
						"Roll Not Exist In Record");
			}
		} else {
			signUpButton.setClickable(true);
		}
	}
}
