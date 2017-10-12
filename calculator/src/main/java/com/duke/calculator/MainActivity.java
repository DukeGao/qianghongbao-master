package com.duke.calculator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.math.BigDecimal;

public class MainActivity extends AppCompatActivity {

    //2017年度住房公积金月缴存额设上、下限，月缴存额上限为2732元，下限为306元。
    private  final double PERCENTAGE_FUND_PERSON = 0.07;//住房公积金缴存比例:职工7%
    private  final double PERCENTAGE_FUND_COMPANY = 0.07;//住房公积金缴存比例:单位7%
    private  final double PERCENTAGE_INSURANCE_PERSON = 0.08;//养老保险缴存比例:职工8%
    private  final double PERCENTAGE_INSURANCE_COMPANY = 0.2;//养老保险缴存比例:单位20%
    private  final double PERCENTAGE_MEDICAL_PERSON = 0.02;//医疗保险缴存比例:职工2%
    private  final double PERCENTAGE_MEDICAL_COMPANY = 0.095;//医疗保险缴存比例:单位9.5%
    private  final double PERCENTAGE_UNEMPLOYMENT_PERSON = 0.005;//失业保险缴存比例:职工0.5%
    private  final double PERCENTAGE_UNEMPLOYMENT_COMPANY = 0.005;//失业保险缴存比例:单位0.5%
    private  final double PERCENTAGE_BIRTH_PERSON = 0;//生育保险缴存比例:个人不缴费
    private  final double PERCENTAGE_BIRTH_COMPANY = 0.01;//生育保险缴存比例:单位1%
    private  final double PERCENTAGE_INJURY_PERSON = 0;//工伤保险缴存比例:个人不缴费
    private  final double PERCENTAGE_INJURY_COMPANY = 0.002;//工伤保险缴存比例:单位0.2%

    private  final double LIMIT_FUND_MAX = 2732;//2017年度住房公积金月 缴存额 上限
    private  final double LIMIT_FUND_MIN = 306;//2017年度住房公积金月 缴存额 下限
    private  final double LIMIT_SOCIAL_SECURITY_MAX = 19512;//2017年上海社保 缴费基数 上限
    private  final double LIMIT_SOCIAL_SECURITY_MIN = 3902;//2017年上海社保 缴费基数 下限

    private EditText et_fund, et_social_security, et_money;
    private boolean isEditable_et_fund = false;//公积金缴费基数是否可改动
    private boolean isEditable_et_social_security = false;//社保缴费基数是否可改动
    private double money, fund, socialSecurity, tax, moneyLeft;//收入，个人公积金缴费总额，五险缴费总额,缴税总额，收入余额

    private Double percentageFund = PERCENTAGE_FUND_PERSON;
    private Double percentageSocial = PERCENTAGE_INSURANCE_PERSON + PERCENTAGE_MEDICAL_PERSON
            + PERCENTAGE_UNEMPLOYMENT_PERSON + PERCENTAGE_BIRTH_PERSON + PERCENTAGE_INJURY_PERSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Button btn_change1 = (Button) findViewById(R.id.content_main_btn_change1);//修改公积金缴费基数
        Button btn_change2 = (Button) findViewById(R.id.content_main_btn_change2);//修改社保缴费基数
        et_fund = (EditText) findViewById(R.id.content_main_et1);// 公积金缴费基数
        et_social_security = (EditText) findViewById(R.id.content_main_et2);// 社保缴费基数
        et_money = (EditText) findViewById(R.id.content_main_et_money);//税前月收入
        Button btn_calculate = (Button) findViewById(R.id.content_main_btn_calculate);//开始计算

        //将五险一金缴费基数设为默认自动添加
        et_fund.setEnabled(isEditable_et_fund);
        et_social_security.setEnabled(isEditable_et_social_security);

        btn_change1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isEditable_et_fund = !isEditable_et_fund;
                et_fund.setEnabled(isEditable_et_fund);
            }
        });

        btn_change2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isEditable_et_social_security = !isEditable_et_social_security;
                et_social_security.setEnabled(isEditable_et_social_security);
            }
        });

        et_money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= 1) {

                    money = Double.valueOf(s.toString());//获取content_main_et_money中的

                    //注意：公积金的上下限制为缴费额
                    if (!isEditable_et_fund) {
                        if (money * percentageFund >= LIMIT_FUND_MAX) {
                            et_fund.setText(doubleToString(2, LIMIT_FUND_MAX / 2 / PERCENTAGE_FUND_PERSON));
                        } else if (money * percentageFund <= LIMIT_FUND_MIN) {
                            et_fund.setText(doubleToString(2, LIMIT_FUND_MIN / 2 / PERCENTAGE_FUND_PERSON));
                        } else {
                            et_fund.setText(doubleToString(2, money));
                        }
                    }

                    //注意；社保的上下限制为缴存基数
                    if (!isEditable_et_social_security) {
                        if (money >= LIMIT_SOCIAL_SECURITY_MAX) {
                            et_social_security.setText(doubleToString(2, LIMIT_SOCIAL_SECURITY_MAX));
                        } else if (money <= LIMIT_SOCIAL_SECURITY_MIN) {
                            et_social_security.setText(doubleToString(2, LIMIT_SOCIAL_SECURITY_MIN));
                        } else {
                            et_social_security.setText(doubleToString(2, money));
                        }
                    }

                } else {
                    et_money.setHint(R.string.content_main_hint1);
                    et_fund.setText("");
                    et_fund.setHint(R.string.content_main_hint2);
                    et_social_security.setText("");
                    et_social_security.setHint(R.string.content_main_hint3);
                }

            }
        });

        btn_calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                money = Double.valueOf(et_money.getText().toString());
                fund = (Double.valueOf(et_fund.getText().toString())) * percentageFund;
                socialSecurity = (Double.valueOf(et_social_security.getText().toString())) * percentageSocial;
                tax = calculatePersonalTax(3500, (money - fund - socialSecurity));
                moneyLeft = money - fund - socialSecurity - tax;

                Intent intent = new Intent(MainActivity.this, Result.class);
                Bundle bundle = new Bundle();
                bundle.putString("moneyLeft", doubleToString(2, moneyLeft));
                bundle.putString("fund", doubleToString(2, fund));
                bundle.putString("socialSecurity", doubleToString(2, socialSecurity));
                bundle.putString("tax", doubleToString(2, tax));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    /**
     * @param digits 需要保存的小数位数
     * @param num    原double类型的数
     * @return 按小数位数取的String值
     */
    private static String doubleToString(int digits, double num) {
        BigDecimal b = new BigDecimal(num);
        double f = b.setScale(digits, BigDecimal.ROUND_HALF_UP).doubleValue();
        return Double.toString(f);
    }


    /**
     * @param threshold 个税起征点
     * @param money     已缴纳五险一金后的收入
     * @return
     */
    private static Double calculatePersonalTax(int threshold, Double money) {

        if (money > 0) {
            money = money - threshold;//以每月收入额减除费用门槛值以及附加减除费用后的余额
            if (money <= 0) {
                return 0.00;
            } else if (money <= 1500) {
                return money * 0.03;
            } else if (money <= 4500) {
                return 1500 * 0.03 + (money - 1500) * 0.1;
            } else if (money <= 9000) {
                return 1500 * 0.03 + (4500 - 1500) * 0.1 + (money - 4500) * 0.2;
            } else if (money <= 35000) {
                return 1500 * 0.03 + (4500 - 1500) * 0.1 + (9000 - 4500) * 0.2 + (money - 9000) * 0.25;
            } else if (money <= 55000) {
                return 1500 * 0.03 + (4500 - 1500) * 0.1 + (9000 - 4500) * 0.2 + (35000 - 9000) * 0.25 + (money - 35000) * 0.3;
            } else if (money <= 80000) {
                return 1500 * 0.03 + (4500 - 1500) * 0.1 + (9000 - 4500) * 0.2 + (35000 - 9000) * 0.25 + (55000 - 35000) * 0.3 + (money - 55000) * 0.35;
            } else {
                return 1500 * 0.03 + (4500 - 1500) * 0.1 + (9000 - 4500) * 0.2 + (35000 - 9000) * 0.25 + (55000 - 35000) * 0.3 + (80000 - 55000) * 0.35 + (money - 80000) * 0.45;
            }
        } else {
            return -1.0;
        }
    }

}
