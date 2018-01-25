package cn.zt.common.dialog.custom;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.TextView;

import cn.zt.common.R;
import cn.zt.common.dialog.simple.SimpleDialogBuilder;

/**
 * 自定义提醒弹窗
 *
 * @author : ZhangTao on 2018/1/24 16:33
 */
public class TextDialogBuilder extends SimpleDialogBuilder<TextDialogBuilder> {
    @Override
    public TextDialogBuilder getThis() {
        return this;
    }

    public TextDialogBuilder(@NonNull Context context) {
        super(context, R.style.Dialog_Default);
        params(Utils.getDefaultDialogParamsBuilder().params());
        hideCloseView(true);
        addView(R.layout.dialog_view_message);
    }

    public TextDialogBuilder message(CharSequence msg) {
        TextView text_message = findView(R.id.text_message);
        text_message.setText(msg);
        return this;
    }
}