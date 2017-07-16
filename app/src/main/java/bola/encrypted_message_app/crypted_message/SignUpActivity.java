package bola.encrypted_message_app.crypted_message;

import bola.encrypted_message_app.R;
import bola.encrypted_message_app.crypt.Crypt;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends Activity {
    Button btnSignUp;
    EditText etPass, etUserName, etPassConfirm, etName;
    Database userDatabase;
    Crypt cryptObject = new Crypt();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signupactivity);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        etPass = (EditText) findViewById(R.id.etPass);
        etPassConfirm = (EditText) findViewById(R.id.etPassConfirm);
        etUserName = (EditText) findViewById(R.id.etUserName);
        etName = (EditText) findViewById(R.id.etName);
        userDatabase = new Database(this);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etPass.getText().toString().trim().length() == 0
                        || etUserName.getText().toString().trim().length() == 0
                        || etName.getText().toString().trim().length() == 0
                        ||etPassConfirm.getText().toString().trim().length() == 0)
                    Toast.makeText(getBaseContext(), R.string.empty,Toast.LENGTH_LONG).show();
                else if (!(etPass.getText().toString().equals(etPassConfirm.getText().toString())))
                    Toast.makeText(getBaseContext(), R.string.passwordConfirmProblem,Toast.LENGTH_LONG).show();
                else {
                    try {
                        int count = userDatabase.tableCount();

                        if (count != 0) {
                            Toast.makeText(getBaseContext(),R.string.reRegister,Toast.LENGTH_LONG).show();
                        } else {
                            String passwordHasH = cryptObject.md5Coder(etPass.getText().toString().trim());
                            userDatabase.addPerson(etName.getText().toString().trim(),etUserName.getText().toString(),passwordHasH);
                            etName.setText("");
                            etUserName.setText("");
                            etPass.setText("");
                            etPassConfirm.setText("");
                            Toast.makeText(getBaseContext(),R.string.UserRegisrationMessage,Toast.LENGTH_LONG).show();
                            onBackPressed();
                        }
                    } catch (Exception e) {

                    }
                }
            }
        });
    }
}
