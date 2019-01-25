# 仿今日头条IOS版新闻点赞动画 #
## 感谢##
https://blog.csdn.net/z82367825/article/details/51599245

https://www.jianshu.com/p/3efa5341abcc
## 效果图##
![效果图](https://github.com/workertao/acode_emoji_example/raw/master/images/GIF.gif)

## 使用方法 ##
在XML布局中声明

    <com.acode.emoji.AcodeEmojiView xmlns:android="http://schemas.android.com/apk/res/an****droid"
        android:id="@+id/view_point"
        android:layout_width="match_parent"
        android:layout_height="match_parent">****
    
        <Button
            android:id="@+id/btn3"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_centerInParent="true"
            android:text="动画" />
    </com.acode.emoji.AcodeEmojiView>

在Activity中使用

    public class MainActivity extends AppCompatActivity {
        private Button btn3;
        private AcodeEmojiView view_point;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            view_point = (AcodeEmojiView) findViewById(R.id.view_point);
            btn3 = (Button) findViewById(R.id.btn3);
            btn3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    view_point.addEmoji(v);
                }
            });
        }
    }


## 0.1版本 ##
1. 参照今日头条ios版实现基本效果


