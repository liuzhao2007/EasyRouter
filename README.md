# EasyRouter
简单、稳定、高性能的组件化路由框架。


# 一、功能特性
1. 通过url打开Activity，实现界面间解耦；
2. 通过服务实现方法调用，实现Module间方法调用解耦；
3. 通过拦截器实现界面跳转的处理：条件拦截、埋点统计等；
4. 界面、服务、拦截器均支持多Module；
5. 基于Apt，界面、服务、拦截器等均可自动注册；
6. 可传递Bundle支持的所有数据类型；
7. 支持获取Fragment；
8. 支持全局、局部过程监听：降级、打开后等；
9. Api简单、实现高性能；


# 二、应用场景
1. Module内、跨Module界面跳转，界面解耦；
2. 界面跳转过程拦截：条件拦截（eg：未登录）、重定向等；
3. 跨Module方法调用，Module间解耦；

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
        compile 'com.easyrouter:router-api:1.0.0'
        compile 'com.easyrouter:router-annotation:1.0.0'
        annotationProcessor 'com.easyrouter:router-compiler:1.0.0'
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

**EasyRouter.init().setScheme()必调，别的设置选调；**

```
    EasyRouter.init(EasyRouterApp.this).setScheme("easyrouter").setDefaultRouterCallBack(new IRouterCallBack() {
        @Override
        public void onFound() {
        }

        @Override
        public void onLost() {
        }

        @Override
        public void onOpenSuccess() {
        }

        @Override
        public void onOpenFailed() {
        }
    });
```

### 3、添加注解
- 在app里任意一个类中添加注解@DispatcherModules，里面写上所有使用此框架的Module的name；

```
    例如：@DispatcherModules({"app","moduleinteract"})；
```

- 在任意需要路由打开的Activity加上注解@DisPatcher，里面写上其对应的url；

```
    @DisPatcher({"easyrouter://main", "easyrouter://maintwo"})
    public class MainActivity extends Activity 
```

### 4、发起路由

```
    1. EasyRouter.open("easyrouter://main");
    2. EasyRouter.with("easyrouter://main").open();
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

- 通过url传参；
    - 非必须参数；
```
    EasyRouter.open("easyrouter://main?name=liuzhao&sex=man");
    这样传递了两个参数：name与sex；在目标Activity中可以通过getIntent.getString("name")方式来获取；
```
    - 必须参数；

```
    注解声明：
    @DisPatcher({"easyrouter://main/i:tab"}) // 注解声明需要一个必备参数tab，并且声明其类型为int；
    public class MainActivity extends Activity 
    EasyRouter.open("easyrouter://main/3");
    这样传递了一个参数：tab；在目标Activity中可以通过getIntent.getInt("tab",0)方式来获取；
```
**备注：必须参数与非必须参数可搭配使用，区别在于必须参数参与url匹配过程；**

**备注：通过url传参与不通过url传参可搭配使用；**

### 2、Module间通信（方法调用）

配置稍微复杂，但使用极其简单；
- 在项目的Library中创建继承IBaseModuleService的接口文件com.easyrouter.service.BaseModuleService；（包名及类名不可变）
- 各Module需要向外提供的方法在BaseModuleService中新建接口类并暴露接口；
```
    public interface ModuleInteractService extends BaseModuleService {
        void runModuleInteract();
    }
```

- 在Module中创建Module的接口实现类，类名需要和接口名一样；
- 打上注解@ModuleService；
- 在别的Module中直接以方法调用；
```
    EasyRouter.getModuleService(BaseModuleService.ModuleInteractService.class).runModuleInteract();
```

**备注：配置复杂带来的优势是方法的直接调用，无需强转也不限定调用方法的方法签名；**

### 3、拦截器
- 实现IInterceptor接口；
- 打上注解@Interceptor；

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

**备注：可以对每一次路由做监听，也可以设置全局默认的监听；**

```
EasyRouter.init(EasyRouterApp.this).setScheme("easyrouter").setDefaultRouterCallBack();
```

# 五、其它设置
### 1、打开Log；
```
EasyRouter.init(EasyRouterApp.this).setDebug(true);
```

### 2、详细Api；
- EasyRouter.open("url");
- EasyRouter.open("url",IRouterCallBack);
- EasyRouter.open(Activity,"url");
- EasyRouter.open(Activity,"url",IRouterCallBack);
- EasyRouter.with("url").with("","").open();  传递参数
- EasyRouter.with("url").with("","").open(IRouterCallBack);  传递参数
- EasyRouter.with("url").with("","").open(Activity,requestCode);  传递参数

















