# EasyRouter

简单、稳定、高性能的组件化路由框架。

# 一、功能特性

1. 通过url打开Activity，实现界面间解耦；

2. 通过服务实现方法调用，实现Module间方法调用解耦；

3. 通过拦截器实现界面跳转的处理：条件拦截、埋点统计等；

4. 界面、服务、拦截器均支持多Module；

5. 基于Apt，界面、服务、拦截器等均可自动注册；

6. 可传递Bundle支持的所有数据类型；

7. 支持自动注入参数到目标界面；

8. 支持获取Fragment；

9. 支持全局、局部过程监听：降级、打开后等；

10. Api简单、实现高性能；

![image](https://github.com/liuzhao2007/EasyRouter/blob/master/app/gif/EasyRouter.gif)

# 二、应用场景

1. Module内、跨Module界面跳转，界面解耦；

2. 界面跳转过程拦截：条件拦截（eg：未登录）、重定向等；

3. 跨Module方法调用，Module间解耦；

4. 外部Url跳转应用内界面；

# 三、集成使用

### 1. 添加依赖与配置

```
    android {
        defaultConfig {
            javaCompileOptions {
                annotationProcessorOptions {
                    arguments = [ moduleName : project.getName() ]
                }
            }
        }
    }

    dependencies {
        compile 'com.easyrouter:router-api:1.2.3'
        compile 'com.easyrouter:router-annotation:1.2.3'
        annotationProcessor 'com.easyrouter:router-compiler:1.2.3'
    }
```

在Project级别的build.gradle中添加：

```
    allprojects {
        repositories {
            jcenter()
            maven { url "https://dl.bintray.com/liuzhaowy2007/maven" }
        }
    }
```

### 2、初始化

**EasyRouterConfig.getInstance().setScheme()必调，别的设置选调；**

```
    EasyRouterConfig.getInstance()
            .setDebug(true)
            .setScheme("easyrouter")
            .setDefaultRouterCallBack(new IRouterCallBack() {
                @Override
                public void onFound() {
                    LogUtil.i("default onFound");
                }

                @Override
                public void onLost() {
                    LogUtil.i("default onLost");
                }

                @Override
                public void onOpenSuccess() {
                    LogUtil.i("default onOpenSuccess");
                }

                @Override
                public void onOpenFailed() {
                    LogUtil.i("default onOpenFailed");
                }
            })
            .init(EasyRouterApp.this);
```

### 3、添加注解

1. 在app里任意一个类中添加注解@DispatcherModules，里面写上所有使用此框架的Module的name；

```
    例如：@DispatcherModules({"app","moduleinteract"})；
```

2. 在任意需要路由打开的Activity加上注解@DisPatcher，里面写上其对应的url；

```
    @DisPatcher({"easyrouter://main", "easyrouter://maintwo"})
    public class MainActivity extends Activity
```

### 4、发起路由

```
    1. EasyRouter.open("easyrouter://main");//方式一
    2. EasyRouter.with("easyrouter://main").open();//方式二
```

# 四、进阶使用

### 1、传递参数

- 不通过url传参；

```
    EasyRouter.with("easyrouter://main").withString("stringparams","")// 传递基本数据类型；
                        .withParcelable("parcelable",null)// 传递系列化对象；
                        .withFlags(Intent.FLAG_ACTIVITY_NEW_TASK)// 设置Flag；
                        .withTransition(0,0)// 设置动画；
                        .open(Activity,requestCode);// 设置RequestCode
```

- 通过url传参：非必须参数；

```
    EasyRouter.open("easyrouter://main?name=liuzhao&sex=man");
    这样传递了两个参数：name与sex；在目标Activity中可以通过getIntent.getString("name")方式来获取；
```

- 通过url传参：必须参数；

```
    注解声明：
    @DisPatcher({"easyrouter://main/i:tab"}) // 注解声明需要一个必备参数tab，并且声明其类型为int；
    public class MainActivity extends Activity 

    调用：
    EasyRouter.open("easyrouter://main/3");
    这样传递了一个参数：tab；在目标Activity中可以通过getIntent.getInt("tab",0)方式来获取；
```

**备注：必须参数与非必须参数可搭配使用，区别在于必须参数参与url匹配过程；通过url传参与不通过url传参两种方式可搭配使用。**

### 2、Module间通信（方法调用）

配置稍微复杂，但使用极其简单；可参考modulelib中的BaseModuleService。

1. 在项目的Library中创建继承IBaseModuleService的接口文件com.android.easyrouter.service.BaseModuleService；（包名、类名及继承关系不可变）

2. 各Module需要向外提供的方法在BaseModuleService中新建接口类并暴露接口；

```
  public interface ModuleInteractService extends BaseModuleService {
      void runModuleInteract(Context context);
  }
```

3. 在Module中创建Module的接口实现类，类名需要和接口名一样；

4. 打上注解@ModuleService、并编译；

5. 在别的Module中直接以方法调用；

```
  EasyRouter.getModuleService(BaseModuleService.ModuleInteractService.class).runModuleInteract(context);
```

**备注：配置复杂带来的优势是方法的直接调用，无需强转也不限定调用方法的方法签名；**

### 3、拦截器

1. 实现IInterceptor接口；

2. 打上注解@Interceptor；

```
    @Interceptor
    public class RouterTestInterceptor implements IInterceptor{

        @Override
        public boolean intercept() {
            LogUtil.i("intercept by me");
            return true;// if true intercept; false go on;
        }

        @Override
        public void onIntercepted() {
        }
    }
```

**备注：在intercept方法中进行拦截与否的判断，例如登录态、重定向等；**

### 4、过程监听

```
  EasyRouter.open("easyrouter://routertest",new IRouterCallBack(){
        @Override
        public void onFound() {
            //匹配到
        }

        @Override
        public void onLost() {
            //匹配不到，可做降级；
        }

        @Override
        public void onOpenSuccess() {
            //界面打开成功
        }

        @Override
        public void onOpenFailed() {
            //界面打开失败，可做降级；
        }
    });
```

**备注：可以对每一次路由做监听，也可以设置全局默认的监听；**

```
    EasyRouter.init(EasyRouterApp.this).setScheme("easyrouter").setDefaultRouterCallBack();
```

### 5、获取Fragment；

```
    // 传入Fragment继承的类，android.app.Fragment或者android.support.v4.app.Fragment
    Fragment fragment = EasyRouter.with("easyrouter://fragmenttest").getFragment(Fragment.class);
```

### 6、外部Url跳转应用内界面；

**AndroidManifest.xml中注册**

```
    <activity
        android:name="com.android.easyrouter.url.EasyRouterUrlActivity"
        android:theme="@android:style/Theme.Translucent.NoTitleBar">
        <intent-filter>
            <action android:name="android.intent.action.VIEW" />
            <category android:name="android.intent.category.DEFAULT" />
            <category android:name="android.intent.category.BROWSABLE" />
            <data android:scheme="easyrouter" /><!--改成自己的Scheme-->
        </intent-filter>
    </activity>
```

**备注：也可以使用自己的Activity：**

- ```只需要调用EasyRouter.open(Uri.toString());即可```

### 7、自动注入参数到界面；

- 在目标Activity中加上EasyRouter.inject(this);

- 在Activity中需要自动传参的参数上加上注解@AutoAssign，则会自动通过Intent赋值。例如：

```
    @AutoAssign
    long time;
    @AutoAssign
    int age;
    @AutoAssign
    String url;
```

**备注：自动注入参数功能目前仅支持基本数据类型和String；**

# 五、其它设置

### 1、打开Log；

```
    EasyRouterConfig.getInstance().setDebug(true)
```

### 2、详细Api；

```
    EasyRouter.open("url");
    EasyRouter.open("url",IRouterCallBack);
    EasyRouter.open(Activity,"url");
    EasyRouter.open(Activity,"url",IRouterCallBack);
    EasyRouter.with("url").with("","").open();  传递参数
    EasyRouter.with("url").with("","").open(IRouterCallBack);  传递参数
    EasyRouter.with("url").with("","").open(Activity,requestCode);  传递参数
```

### 3、问题；

如果有不生效的情况，例如：界面跳转、服务调用、编译失败等，可以尝试以下解决思路：

- **确认配置是否完备，特备注意：在每个使用EasyRouter的build.gradle中添加javaCompileOptions（参见上面的具体配置）以及在app里任意一个类中添加注解@DispatcherModules，里面写上所有使用此框架的Module的name；**

- **查看Log输出信息，Tag为easyrouter**；

### 4、混淆；

如果使用了Proguard，则需要添加以下混淆规则；

```
    -keep public class com.android.easyrouter.**{*;}
    -keep class * implements com.android.easyrouter.service.BaseModuleService{*;}
```
