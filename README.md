# Android-Skin-Loader 

[Android换肤原理和Android-Skin-Loader框架解析](http://dandanlove.com/2017/11/27/android-skin-changed/)



## 目录介绍

```
Android-Skin-Loader
├── android-skin-loader-lib      // 皮肤加载库
├── android-skin-loader-sample   // 皮肤库应用实例
├── android-skin-loader-skin     // 皮肤包生成demo
├── skinloader                   // skinloader的换肤sdk
└── skin-package                 // 皮肤包生成demo生成的BlackFantacy.skin资源包
```

## skinloader

为什么我要自己写 `skinloader` ，在我看了**GitHub**上的大部分的**Android换肤SDK**之后再运用到项目中的时候基本上都需要进行改动，所以有了`skinloader`。

- `Android-Skin-Loader` 可以不用写换肤的类，但如果在代码中通过`new`创建出来的`View`那么换肤的时候很复杂，对业务的入侵程度很高；
- `Android-skin-support` 需要些换肤的类，貌似没有考虑在代码中通过`new`创建出来的`View`换肤的情况。

综合比较所以`Android-skin-support` 的使用者更多一些，因为它提供了更多的功能。

`skinloader` 是在 `Android-Skin-Loader` 的基础上进行的开发，因为 `Android-skin-support`  的好多功能都用不到。`skinloader` 使用换肤类，来解决代码中通过`new`创建出来的`View`换肤的情况。

[skinloader的详细介绍请点击这^_^](https://github.com/stven0king/Android-Skin-Loader/tree/master/skinloader)



=================原项目介绍====================



皮肤在` android-skin-loader-skin `皮肤demo的同级目录`skin-package`文件夹中，`BlackFantacy.skin`放在SD卡根目录。


## 用法

#### 1. 在`Application`中进行初始化
```java
public class SkinApplication extends Application {
	public void onCreate() {
		super.onCreate();
		// Must call init first 
		SkinManager.getInstance().init(this);
		SkinManager.getInstance().load();
	}
}
```

#### 2. 在布局文件中标识需要换肤的View

```xml
...
xmlns:skin="http://schemas.android.com/android/skin"
...
  <TextView
     ...
     skin:enable="true" 
     ... />
```

#### 3. 继承`BaseActivity`或者`BaseFragmentActivity`作为BaseActivity进行开发


#### 4. 从`.skin`文件中设置皮肤
```java
String SKIN_NAME = "BlackFantacy.skin";
String SKIN_DIR = Environment.getExternalStorageDirectory() + File.separator + SKIN_NAME;
File skin = new File(SKIN_DIR);
SkinManager.getInstance().load(skin.getAbsolutePath(),
				new ILoaderListener() {
					@Override
					public void onStart() {
					}

					@Override
					public void onSuccess() {
					}

					@Override
					public void onFailed() {
					}
				});
```

#### 5. 重设默认皮肤
```java
SkinManager.getInstance().restoreDefaultTheme();
```

#### 6. 对代码中创建的View的换肤支持
主要由`IDynamicNewView`接口实现该功能，在`BaseActivity`，`BaseFragmentActivity`和`BaseFragment`中已经实现该接口.

```java
public interface IDynamicNewView {
	void dynamicAddView(View view, List<DynamicAttr> pDAttrs);
}
```
**用法：**动态创建View后，调用`dynamicAddView`方法注册该View至皮肤映射表即可(如下).详见sample工程

```java
	private void dynamicAddTitleView() {
		TextView textView = new TextView(getActivity());
		textView.setText("Small Article (动态new的View)");
		RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		param.addRule(RelativeLayout.CENTER_IN_PARENT);
		textView.setLayoutParams(param);
		textView.setTextColor(getActivity().getResources().getColor(R.color.color_title_bar_text));
		textView.setTextSize(20);
		titleBarLayout.addView(textView);
		
		List<DynamicAttr> mDynamicAttr = new ArrayList<DynamicAttr>();
		mDynamicAttr.add(new DynamicAttr(AttrFactory.TEXT_COLOR, R.color.color_title_bar_text));
		dynamicAddView(textView, mDynamicAttr);
	}
```

#### 7. 皮肤包是什么？如何生成？
- 皮肤包（后缀名为`.skin`）的本质是一个apk文件，该apk文件不包含代码，只包含资源文件
- 在皮肤包工程中（示例工程为`skin/BlackFantacy`）添加需要换肤的同名的资源文件，直接编译生成apk文件，再更改后缀名为`.skin`j即可（防止用户点击安装）
- 使用gradle的同学，执行`android-skin-loader-skin`工程的```assembleDebug```后即可在`skin-package`目录下取皮肤包（修改脚本中`def skinName = "BlackFantacy.skin"`换成自己想要的皮肤名）

---


## License

    Copyright [2015] [FENGJUN]
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
       http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

