package cn.zt.common.dialog.custom;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import cn.zt.common.R;
import cn.zt.common.dialog.simple.SimpleDialogBuilder;
import cn.zt.common.dialog.simple.SimpleDialogParams;
import cn.zt.common.simple.Value;
import cn.zt.common.utils.RegexUtils;
import cn.zt.common.utils.ViewUtils;

/**
 * @author : ZhangTao on 2018/1/25 10:32
 */
public class Utils {
    public static SimpleDialogParams.Builder getDefaultDialogParamsBuilder() {
        return new SimpleDialogParams.Builder()
                .layout(R.layout.dialog_default)
                .titleId(R.id.text_dialog_title)
                .closeId(R.id.btn_dialog_close)
                .contentId(R.id.linear_dialog_content)
                .buttonId(R.id.linear_dialog_button)
                .btnLayout(R.layout.dialog_button_layout)
                .btnLineLayout(R.layout.dialog_button_layout_line)
                .btnDrawables(new int[]{R.drawable.selector_dialog_bottom_start
                        , R.drawable.selector_dialog_bottom, R.drawable.selector_dialog_bottom_end})
                .themeResId(R.style.Dialog_Default)
                .cancelable(false)
                .canceledOnTouchOutside(true)
                .clickButtonCancel(true)
                .maxWidth(0.8f)
                .maxHeight(0.8f);
    }

    public static Value<String> setIpView(SimpleDialogBuilder dialogBuilder, String ip) {
        return setIpView(dialogBuilder, ip, R.layout.dialog_set_ip);
    }

    public static Value<String> setIpView(SimpleDialogBuilder dialogBuilder, String ip, @LayoutRes int setIpLayout) {
        final View view = ViewUtils.getView(dialogBuilder.getContext(), setIpLayout);
        dialogBuilder.addView(view);
        final EditText edit_ip1 = view.findViewById(R.id.edit_ip1);
        final EditText edit_ip2 = view.findViewById(R.id.edit_ip2);
        final EditText edit_ip3 = view.findViewById(R.id.edit_ip3);
        final EditText edit_ip4 = view.findViewById(R.id.edit_ip4);
        GridLayoutKeyBoard gridLayout = view.findViewById(R.id.gl_key_ip);
        if (RegexUtils.isIP(ip)) {
            String[] ips = ip.split("\\.");
            edit_ip1.getText().replace(0, 0, ips[0]);
            edit_ip2.getText().replace(0, 0, ips[1]);
            edit_ip3.getText().replace(0, 0, ips[2]);
            edit_ip4.getText().replace(0, 0, ips[3]);
            edit_ip4.requestFocus();
        } else {
            edit_ip1.requestFocus();
        }
        gridLayout.setCustomOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View e = view.findFocus();
                if (e instanceof EditText) {
                    EditText editText = (EditText) e;
                    if (v instanceof TextView) {
                        TextView textView = (TextView) v;
                        switch (textView.getText().toString()) {
                            case ".":
                                if (editText.getText().length() != 0) {
                                    int i = editText.getId();
                                    if (i == R.id.edit_ip1) {
                                        edit_ip2.requestFocus();
                                    } else if (i == R.id.edit_ip2) {
                                        edit_ip3.requestFocus();
                                    } else if (i == R.id.edit_ip3) {
                                        edit_ip4.requestFocus();
                                    }
                                }
                                break;
                            case "退格":
                                if (editText.getText().length() == 0) {
                                    int i = editText.getId();
                                    if (i == R.id.edit_ip2) {
                                        edit_ip1.requestFocus();
                                    } else if (i == R.id.edit_ip3) {
                                        edit_ip2.requestFocus();
                                    } else if (i == R.id.edit_ip4) {
                                        edit_ip3.requestFocus();
                                    }
                                } else {
                                    int start = editText.getSelectionStart();
                                    if (editText.getSelectionEnd() - start > 0) {
                                        editText.getText().delete(start, editText.getSelectionEnd());
                                    } else if (start > 0) {
                                        editText.getText().delete(start - 1, start);
                                    }
                                }
                                break;
                            default:
                                int start = editText.getSelectionStart();
                                int end = editText.getSelectionEnd();
                                if (end - start > 0) {
                                    editText.getText().replace(start, end, textView.getText());
                                    editText.setSelection(editText.getText().length());
                                } else if (editText.getText().length() < 3) {
                                    editText.getText().insert(start, textView.getText());
                                }
                                if (editText.getText().length() >= 3) {
                                    int i = editText.getId();
                                    if (i == R.id.edit_ip1) {
                                        edit_ip2.requestFocus();
                                    } else if (i == R.id.edit_ip2) {
                                        edit_ip3.requestFocus();
                                    } else if (i == R.id.edit_ip3) {
                                        edit_ip4.requestFocus();
                                    }
                                }
                                break;
                        }
                    }
                }
            }
        });
        return new Value<String>() {
            @Override
            public String get() {
                return String.valueOf(edit_ip1.getText()) + "."
                        + edit_ip2.getText() + "."
                        + edit_ip3.getText() + "."
                        + edit_ip4.getText();
            }
        };
    }
}
