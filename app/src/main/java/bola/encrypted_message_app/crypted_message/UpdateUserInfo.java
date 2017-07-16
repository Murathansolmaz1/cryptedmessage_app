package bola.encrypted_message_app.crypted_message;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import bola.encrypted_message_app.R;
import bola.encrypted_message_app.crypt.Crypt;

public class UpdateUserInfo extends Activity {

    Database userDatabase;
    Crypt cryptObject = new Crypt();
    EditText etPass, etUserName, etName, etPassConfirm, etOldPass;
    Button update;
    String[] records;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.updateuserinfo);
        etPass = (EditText) findViewById(R.id.etPass);
        etOldPass = (EditText) findViewById(R.id.etOldPass);
        etPassConfirm = (EditText) findViewById(R.id.etPassConfirm);
        etUserName = (EditText) findViewById(R.id.etUserName);
        etName = (EditText) findViewById(R.id.etName);
        update = (Button) findViewById(R.id.btnUpdate);

        userDatabase = new Database(this);

        records = userDatabase.records();
        etName.setText(records[0]);

        update.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (etPass.getText().toString().trim().length()== 0
                        || etUserName.toString().trim().length()==0
                        || etName.toString().trim().length()==0
                        || etPassConfirm.toString().trim().length()==0
                        || etOldPass.toString().trim().length()==0)
                Toast.makeText(getBaseContext(), R.string.empty, Toast.LENGTH_LONG).show();

                else if (!cryptObject.md5Coder(etOldPass.getText().toString()).equals(records[2]))
                    Toast.makeText(getBaseContext(), R.string.checkPassword, Toast.LENGTH_LONG).show();

                else if(!(etPass.getText().toString().equals(etPassConfirm.getText().toString())))
                    Toast.makeText(getBaseContext(), R.string.passwordConfirmProblem, Toast.LENGTH_LONG).show();

                else {
                    try {
                        String passwordHash = cryptObject.md5Coder(etPass.getText().toString().trim());
                        userDatabase.update(etName.getText().toString().trim(), etUserName.getText().toString(), passwordHash);
                        Toast.makeText(getBaseContext(), R.string.updateInformation, Toast.LENGTH_LONG).show();
                        onBackPressed();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), R.string.tryAgain, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}
