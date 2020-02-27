# changeSkin

## 功能：

- 可实现`layout.xml` 中声明的`View` 的换肤，`layout.xml` 的布局文件中的`View` 可以实现动态替换为换肤`View`;
- 可以实现自定义解析`layout.xml`中的`View`的`layoutInflater` ，自定义的 `layoutInflater` 的优先级高于默认的;
- 可以实现换肤`View`的动态注册，以及`layout.xml` 中的`view` 和换肤`View`的映射关系的动态注册；
- 可实现代码中创建的`View` 的换肤（代码中创建的`View`必须是可以换肤的`View`）；
- 可实现皮肤从`sdcard`的动态加载；
- 可实现启动默认加载上次换肤资源；
- 可实现页面是否换肤的的控制，根据 `Context` 上线做区分；
- 支持替换`color` 和 `drawable` 类型的资源;
- 支持替换 `style` 和 `theme` 中的皮肤属性；

## 依赖：

- 可以换肤的`View` 必须是继承和换肤的接口、类；

```java
public class IMTextView extends TextView implements SkinViewInterface {
    private SkinBackgroundAttr skinBackgroundAttr;
    private SkinTextColorAttr skinTextColorAttr;
    private SkinDrawableLeftAttr skinDrawableLeftAttr;
    public IMTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }
    public IMTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }
    public IMTextView(Context context) {
        super(context);
        init(null);
    }
    private void init(AttributeSet attrs) {
        skinBackgroundAttr = new SkinBackgroundAttr(this, attrs);
        skinTextColorAttr = new SkinTextColorAttr(this, attrs);
    }
    @Override
    public void setBackgroundResource(int resid) {
        skinBackgroundAttr.setBackgroundResource(resid);
    }
    @Override
    public void applySkin() {
        SkinViewAttr.applySkin(skinBackgroundAttr,
                skinTextColorAttr,
                skinDrawableLeftAttr);
    }
}
```

招才猫项目中已经有了一套基础的UI组件库，直接在这套组件库上实现换肤接口就可以。

## 换肤SDK

### 初始化

```java
 SkinManager.getInstance().init(this.getApplicationContext())//初始化
                .setMarkSkinObserverFlag(false)//标记注册的页面是否可以换肤
                .registerMarkSkinObserver(SkinActivity.class)//注册的页面
                .registSkinViewConvertMap(View.class, SkinView.class)//注册的换肤的View
                .registSkinViewConvertMap(Button.class, SkinButton.class)
                .registSkinViewConvertMap(TextView.class, SkinTextView.class)
                .registSkinViewConvertMap(RelativeLayout.class, SkinRelativeLayout.class)
   							.setDefaultSkinLoaderListener(new SkinLoaderListener() {//设置换肤资源的监听
                        @Override
                        public void onStart() {
                            Logger.d("SkinManager", "onStart");
                        }

                        @Override
                        public void onSuccess() {
                            Logger.d("SkinManager", "onSuccess");
                        }

                        @Override
                        public void onFailed() {
                            Logger.d("SkinManager", "onFailed");
                        }
                    })
                    .load();//默认加载上次的换肤资源
```

### 加载皮肤

```java
String s = Environment.getExternalStorageDirectory() + File.separator + "dark.skin";
//s:皮肤资源的路径
//“dark_”:皮肤资源的前缀
SkinManager.getInstance().load(s, "dark_");
```

### 相关类介绍

#### 资源获取

```java
//皮肤资源获取
public class SkinResource {
    public static final String ATTR_COLOR = "color";
    //获取resId对应的资源类型，color或者drawable等
    public static String getResourceEntryType(int resId){……}
    //获取resId对应的资源
    public static int getColor(int resId){……}
    public static Drawable getDrawable(int resId){……}
    //更新当前View以及子view的皮肤
    public static void updateSkinResource(View view){……}
}
```

建议在换肤sdk和项目之间搞一个代理，这样项目的业务代码和换肤sdk可以实现隔离，嫌少换肤sdk对业务代码的影响；

#### 换肤属性

```java
//实现换肤的接口
public interface SkinViewInterface {
    void applySkin();
}
```

目前实现：

- `SkinBackgroundAttr` :视图的背景；
- `SkinTextColorAttr` ：文本的字体颜色；
- `SkinDrawableLeftAttr` ：文本左侧的图片；

#### 自定义layout解析

```java
public interface InflaterInterface {
    View onCreateView(View parent, String name, Context context, AttributeSet attrs);
}
```

```java
SkinManager.getInstance().registSkinInflater(inflater);
```

#### 换肤页面基类

```java
public class SkinBaseActivity extends Activity implements SkinUpdateLinstener {
    public SkinInflaterFactory skinInflaterFactory;
    protected boolean isSupportSkinupdate = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //如果支持当前页面进行换肤，那么注册SkinInflaterFactory
        if (SkinManager.getInstance().isSupportSkinUpdateObserver(this)) {
            skinInflaterFactory = new SkinInflaterFactory();
            LayoutInflater.from(this).setFactory2(skinInflaterFactory);
            isSupportSkinupdate = true;
        }
        super.onCreate(savedInstanceState);
    }
    @Override
    protected void onResume() {
        super.onResume();
        //如果支持当前页面进行换肤，那么注册页面监听换肤
        if (isSupportSkinupdate) {
            SkinManager.getInstance().attach(this);
						getWindow().getDecorView().post(new Runnable() {
                @Override
                public void run() {
                    SkinResource.updateSkinResource(getWindow().getDecorView());
                }
            });
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isSupportSkinupdate) {
            SkinManager.getInstance().detach(this);
        }
    }
    @Override
    public void onSkinChanged() {
        //如果支持当前页面进行换肤，那么回调换肤
        if (isSupportSkinupdate) {
            SkinResource.updateSkinResource(getWindow().getDecorView());
          	skinInflaterFactory.applySkin();
        }
    }
}
```
