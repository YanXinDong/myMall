package com.BFMe.BFMBuyer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.base.IBaseActivity;

/**
 * 填写快递信息
 */
public class AddExpressInformationActivity extends IBaseActivity implements View.OnClickListener {

    private EditText et_company_name;
    private EditText et_company_number;
    private Button btn_finish;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setBackButtonVisibility(View.VISIBLE);
        tv_title.setText("填写快递信息");

        et_company_name = (EditText)findViewById(R.id.et_company_name);
        et_company_number = (EditText)findViewById(R.id.et_company_number);
        btn_finish = (Button) findViewById(R.id.btn_finish);
        btn_finish.setOnClickListener(this);

    }

    @Override
    public int initLayout() {
        return R.layout.activity_add_express_information;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_finish:
                Intent intent = new Intent(this, RefundDetailsActivity.class);
                intent.putExtra("companyName",et_company_name.getText().toString());
                intent.putExtra("companyNumber",et_company_number.getText().toString());
                setResult(1002,intent);
                finish();
                exitAnim();
                break;
        }
    }
}
