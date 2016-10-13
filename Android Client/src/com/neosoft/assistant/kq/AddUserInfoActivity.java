package com.neosoft.assistant.kq;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;

import com.neosoft.assistant.kq.model.UserInfo;

public class AddUserInfoActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_userinfo);
    }

    public void onButtonClicked(View clicked) {
        switch (clicked.getId()) {
        case R.id.buttonCreateAccount:
            this.addUserInfo();
            this.finish();
            break;
        default:
            break;
        }
    }

    private void addUserInfo() {
        String username = this.getInputText(R.id.editTextUserName);
        String password = this.getInputText(R.id.editTextPassword);
        String email = this.getInputText(R.id.editTextEMail);

        SqliteDBHelper.getInstance().addUserInfo(new UserInfo(0, username, password, email));
    }

    private String getInputText(int id) {
        EditText editText = (EditText) this.findViewById(id);
        return editText.getText().toString().trim();
    }
}
