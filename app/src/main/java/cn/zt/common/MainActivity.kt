package cn.zt.common

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import cn.zt.common.dialog.custom.TextDialogBuilder
import cn.zt.common.dialog.custom.Utils
import cn.zt.common.utils.RegexUtils
import com.blankj.utilcode.util.ToastUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        text.setOnClickListener {
            val dialogBuilder = TextDialogBuilder(this)
                    .message("TextDialogBuilder 测试")
                    .title("设置IP")
//                    .addButton("确定")
//                    .show()
            dialogBuilder.builderParams.builder
                    .hideInput(true)
                    .clickButtonCancel(false)
                    .canceledOnTouchOutside(false)
            val value = Utils.setIpView(dialogBuilder, "192.168.1.123")
            dialogBuilder.addButton("设置", { _, dialog ->
                val ip = value.get()
                if (RegexUtils.isIP(ip)) {
                    dialog.cancel()
                    ToastUtils.showShort(ip)
                } else {
                    ToastUtils.showShort("请输入正确的IP地址")
                }
            }).show()
        }
        TextDialogBuilder(this).title("标题").message("提示提示").show()
        imageView.setOnClickListener {
            ToastUtils.showShort("click")
        }
    }
}