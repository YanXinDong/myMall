package com.BFMe.BFMBuyer.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.base.IBaseActivity;

/**
 * Description:注册协议明细页面
 * Copyright  : Copyright (c) 2016
 * Company    : 白富美(北京)网络科技有限公司
 * Author     : 王  可
 * Date       : 2016/7/15 9:33
 */

public class UserRegistAgreeActivity extends IBaseActivity {

    private ImageButton vArrow;
    private String info = "您注册Buy For Me网站时，即表示您同意本用户协议。当本协议有变更时，如若您没有明确明示不接受新协议，" +
            "Buy For Me网站将默认您同意新协议。\n一、注册主体：\n        您注册或使用Buy For Me网站时，您必须确保您已经具有完全" +
            "民事行为能力，如若您不具备此主体条件，则由您和您的监护人承担因此产生的后果及经济损失，并且Buy For Me网站有权暂停或" +
            "终止您继续使用Buy For Me网站的权限。\n 二、用户信息\n        您在注册或使用Buy For Me网站时，请您确保您所提供的信" +
            "息正确性、准确性，并请您在资料或信息有变更时及时更新。\n三、账户安全\n        1.您的Buy For Me网站账户、密码、邮箱" +
            "及其他信息不得以任何方式或途径进行买卖、转让、赠与、继承。\n        2.您必须对您的Buy For Me网站账户、密码、邮箱及其他信息承" +
            "担保密责任，并且您需要对您在Buy For Me网站账户、密码、邮箱下发生的所有行为承担责任。\n四、Buy For Me网站\n        1.您" +
            "可以通过Buy For Me网站查询或购买商品，对卖家进行如实的评价。\n        2.当您在Buy For Me网站进行的交易，与卖家发生交易纠纷" +
            "时，双方无法协商一致可申请Buy For Me网站作为第三方进行处理，Buy For Me网站有权根据单方面判断作出处理结果，同时也默认您同意此处" +
            "理结果。\n         3.Buy For Me网站有权应政府部门的要求，向其提供您在Buy For Me网站的用户信息和交易信息等资料。\n五、使用" +
            "Buy For Me网站的规范 \n         1.您必须确保您在Buy For Me网站上的交易与行为都符合国家法律法规。如若您违反相关法律法规或本协议，导致" +
            "Buy For Me网站遭受任何损失或索赔或行政部门的处罚，您应当赔偿Buy For Me网站因此造成的所有损失或费用。\n         2.在Buy For Me网站" +
            "进行交易时，您必须遵守诚实守信的原则，不能以任何形式或行为（如：恶意购买商品、辱骂等）扰乱平台的正常交易环境和秩序。\n六、责任" +
            "范围和限制\n         1.Buy For Me网站将按现有的状态向您提供平台服务，但Buy For Me网站不保证平台服务与技术信息的适用性、准确性" +
            "、正确性、可靠性等。\n         2.Buy For Me网站上的商品都是由卖家自行发布，可能存在风险或瑕疵，Buy For Me网站仅作为交易" +
            "平台，应当由您。\n         3.Buy For Me网站没有义务对所有用户的信息、商品、交易等其他事项进行事先核实，除非法律法规有明确地" +
            "要求。\n         4.任何情况下，Buy For Me网站均不承担因以下原因产生的不能服务或延迟服务的责任，包括但不限于：由信息网络正常" +
            "的设备维护，信息网络连接故障，电脑，通讯，其他系统故障，火灾、洪水和其他不可抗力造成。\n七、 终止协议\n         1.Buy For Me" +
            "网站有权以任何理由限制或暂停或终止您使用平台，并有权不事先通知您，也无须向您承担任何责任或损失。\n         2.当您被限制或暂停" +
            "或终止使用Buy For Me网站时，Buy For Me网站将继续享有您在使用期间存在的违法违规行为的权利主张。\n         3.当您被限制或暂停" +
            "或终止使用Buy For Me网站时，您在使用Buy For Me网站期间产生的投诉、纠纷等行为，仍应当由您来承担相关的责任与损失。\n八、风险订单说明" +
            "\n         平台仅允许一位公民拥有一个平台账号，当2个以上平台账号的收货人或收货地址相同时，均被认定为风险订单，平台会冻结订单，" +
            "且对订单参与者酌情处理。情节严重者将追究其法律责任。\n        最终解释权归平台所有";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBackButtonVisibility(View.VISIBLE);
        tv_title.setText(getString(R.string.register_agree2));
        TextView tvRegistAgree = (TextView) findViewById(R.id.tv_regist_agree_info);
        tvRegistAgree.setText(info);
    }

    @Override
    public int initLayout() {
        return R.layout.activity_user_regist_agree;
    }
}
