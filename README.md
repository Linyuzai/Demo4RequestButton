# Demo4RequestButton
follow pay button of alipay

![request_button.gif](http://upload-images.jianshu.io/upload_images/2113387-2118d439d2c97bea.gif?imageMogr2/auto-orient/strip)

```
<dependency>
  <groupId>com.linyuzai</groupId>
  <artifactId>requestbutton</artifactId>
  <version>1.0</version>
  <type>pom</type>
</dependency>

or

compile 'com.linyuzai:requestbutton:1.0'
```

```
<com.linyuzai.requestbutton.RequestButton    
  android:id="@+id/end2"
  android:layout_width="match_parent"
  android:layout_height="50dp"
  android:layout_marginBottom="5dp"
  android:background="@drawable/bg" 
  icon:request_icon_size="2dp"
  icon:request_icon_spacing="25dp"
  icon:request_icon_style="tick_end_circle"
  icon:request_speed_multiplier="1.8"
  icon:text_color="@android:color/white"   
  icon:text_default="default"    
  icon:text_failure="failure"    
  icon:text_progress="progress"    
  icon:text_success="success" />
```
>下面是所有的属性

```
<declare-styleable name="RequestButton">    
  <attr name="request_icon_spacing" format="dimension" />   
  <attr name="request_icon_color" format="color" />
  <attr name="request_icon_size" format="dimension" />  
  <attr name="request_icon_style" format="enum">
    <enum name="tick_start_circle" value="0" />   
    <enum name="tick_half_circle" value="1" />   
    <enum name="tick_end_circle" value="2" />   
  </attr> 
  <attr name="request_speed_multiplier" format="float" /> 
  <attr name="text_default" format="string" />  
  <attr name="text_progress" format="string" /> 
  <attr name="text_success" format="string" />    
  <attr name="text_failure" format="string" />    
  <attr name="text_color" format="color" />    
  <attr name="text_size" format="dimension" />    
  <attr name="text_width" format="dimension" />
</declare-styleable>
```
>然后简单说明一下，嫌我啰嗦的请跳过

request_icon_spacing，icon和文本之间的间隔默认0px

request_icon_color，icon的颜色默认白色

request_icon_size，画笔Paint的width默认5px

request_icon_style：

1.tick_start_circle，最后的画圈和打钩一起进行
    
2.tick_half_circle，最后的画圈画到一半开始打钩
    
3.tick_end_circle，最后的画圈画完之后再打钩，默认tick_start_circle
    
request_speed_multiplier，动画画圈和打钩的速度乘数0.5-2.0，默认1.8

text_default，按钮默认文本，默认“default”

text_progress，请求时文本，默认“progress”

text_success，成功时文本，默认“success”

text_failure，失败时文本，默认“failure”

text_color,文本字体颜色，默认黑色

text_size，文本字体尺寸，默认20px

text_width，文本宽度，默认wrap_content


```
end2.setOnRequestCallback(new OnRequestCallback() {    
    @Override    
    public void onRequest() {        
        Toast.makeText(MainActivity.this, "request", Toast.LENGTH_SHORT).show();    
    }    
    @Override    
    public void onFinish() {
        Toast.makeText(MainActivity.this, "finish", Toast.LENGTH_SHORT).show();    
    }
});
```
>不需要自己设置OnClickListener，在onRequest()里进行请求，成功或失败后手动调用requestSuccess()或者requestFailure()，等到icon画完就会回调到onFinish()

```
end2.requestSuccess();
end2.requestFailure();
```
