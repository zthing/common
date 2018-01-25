package cn.zt.common.dialog.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayout;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import cn.zt.common.R;
import cn.zt.common.utils.ViewUtils;

/**
 * @author : ZhangTao on 2018/1/5 11:20
 */
public class GridLayoutKeyBoard extends GridLayout {
    private View inputView;

    public static GridLayoutKeyBoard getGridLayoutKeyBoard(Context context, @LayoutRes int layoutRes) {
        return (GridLayoutKeyBoard) ViewUtils.getView(context,layoutRes);
    }

    public GridLayoutKeyBoard(Context context) {
        this(context, null);
    }

    public GridLayoutKeyBoard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GridLayoutKeyBoard(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        int min = ViewUtils.dp2px(80);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if (getMinimumWidth() == 0) {
                setMinimumWidth(getColumnCount() * min);
            }
            if (getMinimumHeight() == 0) {
                setMinimumHeight(getRowCount() * min);
            }
        }
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.GridLayoutKeyBoard);
        final int inputViewId = ta.getResourceId(R.styleable.GridLayoutKeyBoard_inputView, -1);
        ta.recycle();
        post(new Runnable() {
            @Override
            public void run() {
                if (inputViewId != -1) {
                    inputView = getRootView().findViewById(inputViewId);
                }
                OnClickListener keyClickListener = new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        View v;
                        if (inputView == null) {
                            v = ((ViewGroup) getParent()).findFocus();
                        } else {
                            if (inputView instanceof EditText) {
                                v = inputView;
                            } else {
                                v = inputView.findFocus();
                            }
                        }
                        if (v instanceof TextView) {
                            TextView textView = (TextView) v;
                            Editable editable = (Editable) textView.getText();
                            int start = textView.getSelectionStart();
                            LayoutParams params = (LayoutParams) view.getLayoutParams();
                            CharSequence keyStr = params.inputText == null ? (view instanceof TextView ? ((TextView) view).getText() : "") : params.inputText;
                            switch (params.keyType) {
                                case Key.TYPE_NONE:
                                    break;
                                case Key.TYPE_TEXT:
                                    if (textView.getSelectionEnd() - start > 0) {
                                        editable.replace(start, textView.getSelectionEnd(), keyStr);
                                        if (textView instanceof EditText) {
                                            ((EditText) textView).setSelection(textView.getText().length());
                                        }
                                    } else {
                                        editable.insert(start, keyStr);
                                    }
                                    break;
                                case Key.TYPE_DELETE:
                                    if (editable.length() > 0) {
                                        if (textView.getSelectionEnd() - start > 0) {
                                            editable.delete(start, textView.getSelectionEnd());
                                        } else if (start > 0) {
                                            editable.delete(start - 1, start);
                                        }
                                    }
                                    break;
                                case Key.TYPE_CLEAR:
                                    editable.clear();
                                    break;
                                case Key.TYPE_REPLACE:
                                    editable.replace(0, editable.length(), keyStr);
                                    break;
                                case Key.TYPE_CUSTOM:
                                    break;
                            }
                        }
                    }
                };
                for (int i = 0; i < getChildCount(); i++) {
                    View childView = getChildAt(i);
                    LayoutParams params = (LayoutParams) childView.getLayoutParams();
                    if (params.keyType != Key.TYPE_CUSTOM) {
                        childView.setOnClickListener(keyClickListener);
                    } else if (customOnClickListener != null) {
                        childView.setOnClickListener(customOnClickListener);
                    }
                }
            }
        });
    }

    public void setCustomOnClickListener(View.OnClickListener customOnClickListener) {
        this.customOnClickListener = customOnClickListener;
    }

    private View.OnClickListener customOnClickListener;

    public View getInputView() {
        return inputView;
    }

    public void setInputView(View inputView) {
        this.inputView = inputView;
    }

    @Override
    public GridLayout.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    public static class LayoutParams extends GridLayout.LayoutParams {
        public int keyType;
        public String inputText;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.GridLayoutKeyBoard_Layout);
            keyType = a.getInt(R.styleable.GridLayoutKeyBoard_Layout_keyType, Key.TYPE_TEXT);
            inputText = a.getString(R.styleable.GridLayoutKeyBoard_Layout_inputText);
            a.recycle();
        }

        public LayoutParams(LayoutParams param) {
            super(param);
            initKeyBoard(keyType, inputText);
        }

        public LayoutParams() {
            super();
        }

        public LayoutParams(int keyType, String inputText) {
            super();
            initKeyBoard(keyType, inputText);
        }

        public LayoutParams(Spec rowSpec, Spec columnSpec) {
            super(rowSpec, columnSpec);
        }

        public LayoutParams(Spec rowSpec, Spec columnSpec, int keyType, String inputText) {
            super(rowSpec, columnSpec);
            initKeyBoard(keyType, inputText);
        }

        public LayoutParams(GridLayout.LayoutParams lp) {
            super(lp);
        }

        public void initKeyBoard(int keyType, String inputText) {
            this.inputText = inputText;
            this.keyType = keyType;
        }
    }

    /**
     * 自定义键盘View按钮
     *
     * @author : ZhangTao on 2018/1/4 10:37
     */
    public static class Key {
        //操作类型
        public static final int TYPE_NONE = 0;      // 什么都不干
        public static final int TYPE_TEXT = 1;      // 文本末尾添加文字
        public static final int TYPE_DELETE = 2;    // 删除
        public static final int TYPE_CLEAR = 3;     // 清除
        public static final int TYPE_REPLACE = 4;   // 替换整个输入框
        public static final int TYPE_CUSTOM = 5;    // 返回点击事件自定义操作
    }
}
