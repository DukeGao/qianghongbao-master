package com.duke.calculator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class Result extends AppCompatActivity {

    private String moneyLeft,fund, socialSecurity,tax;
    private EditText et_moneyLeft,et_fund, et_socialSecurity,et_tax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        et_moneyLeft = (EditText) findViewById(R.id.activity_result_et_money);
        et_fund = (EditText) findViewById(R.id.activity_result_et1);
        et_socialSecurity = (EditText) findViewById(R.id.activity_result_et2);
        et_tax = (EditText) findViewById(R.id.activity_result_tv_tax);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();


        et_moneyLeft.setEnabled(false);
        et_fund.setEnabled(false);
        et_socialSecurity.setEnabled(false);
        et_tax.setEnabled(false);

        et_moneyLeft.setText(bundle.getString("moneyLeft"));
        et_fund.setText(bundle.getString("fund"));
        et_socialSecurity.setText(bundle.getString("socialSecurity"));
        et_tax.setText(bundle.getString("tax"));


    }
}
