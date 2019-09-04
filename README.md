## 首先什么是KOIN？
适用于 Kotlin 开发人员的实用轻量级依赖注入框架。
用纯 Kotlin 编写，仅使用功能分辨率：无代理，无代码生成，无反射。

**PS：KOIN 支持 Kotion 和 Java** 

## Koin 怎么配置？

本文主要讲解 Koin (2.0.1) 在AndroidX中的使用，所以直接添加 koin-android 依赖

首先添加 Koin Android 基本依赖
> // Koin for Android
>
> implementation "org.koin:koin-android:$koin_version"

如果需要使用到 Scope(范围) 控制，则依赖  koin-androidx-scope
> // Koin AndroidX Scope features
>
> implementation "org.koin:koin-androidx-scope:$koin_version"

如果项目中使用到ViewModel，那么毫不犹豫依赖 koin-androidx-viewmodel
> // Koin AndroidX ViewModel features
>
> implementation "org.koin:koin-androidx-viewmodel:$koin_version"

既然使用Kotlin，扩展功能怎么能少呢？添加 koin-androidx-ext
> // Koin AndroidX Experimental features
>
> implementation "org.koin:koin-androidx-ext:$koin_version"

## Koin 怎么使用？

Koin 入门使用相当容易，学会下面几个关键词就 OK 了，跟着来看看哦～

### factory
今天 Activity 需要一个 Girl（嗯～，可能是几个），那我们就创建个依赖对象使用 Koin 注入给它，这时我需要每次都给它个新的（使用factory）：

```Kotlin
val girlModule = module {
    factory {
        Girl()
    }
}
```

依赖对象有了，我得让 Koin 知道，所以需要在我们的 Application 初始化：
```Kotlin
override fun onCreate() {
    super.onCreate()
    startKoin {
        androidLogger(Level.DEBUG)
        androidContext(this@App)
        androidFileProperties()
        modules(listOf(girlModule))
    }
}
```

现在好像什么都有，接下来当 Activity 需要时该怎么给它呢？看 Activity 代码：

```Kotlin
// 注释掉的是另一种写法
class Simple1Activity : AppCompatActivity() {

    private val girl by inject<Girl>()
    //private lateinit var girl: Girl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample1)

        //girl = get()
        ivImage.setImageResource(girl.getGirl())
    }
}
```

![Activity 有了 Girl](https://user-gold-cdn.xitu.io/2019/9/4/16cfb9fffd93d7bf?w=209&h=357&f=png&s=48053)

看脸都是一个人啊！怎么知道每次都是个新 Gril 呢？直接检查 Gril 的身份证：
>System.out: ---->com.joker.koin.model.Girl@a5b751b
>
>System.out: ---->com.joker.koin.model.Girl@727e094
>
>System.out: ---->com.joker.koin.model.Girl@e005b30

K，原来是3胞胎...

### single

现在都什么时代了，不能给它享受多个 Gril 啊，那就把 factory 换成 single，动手试试检查 Gril 证件：
```Kotlin
val girlModule = module {
    single {
        Girl()
    }
}
```
>System.out: ---->com.joker.koin.model.Girl@48146b8
>
>System.out: ---->com.joker.koin.model.Girl@48146b8
>
>System.out: ---->com.joker.koin.model.Girl@48146b8

如此简单的我们就实现了单例。什么？同时要2个 Girl，还要不同类型且要是固定的。没问题我们有 Qualifier 和 Definition
```Kotlin
val girlModule = module {
    single(named("girl1")) { (type: String) ->
        Girl().apply {
            this.type = type
        }
    }

    single(named("girl2")) { (type: String) ->
        Girl().apply {
            this.type = type
        }
    }
}
```

看看 Activity 怎么样了：

```Kotlin
class Simple2Activity : AppCompatActivity() {

    private val girl1 by inject<Girl>(named("girl1")) { parametersOf("可爱") }
    private val girl2 by inject<Girl>(named("girl2")) { parametersOf("性感") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample1)

        ivImage.setImageResource(girl1.getGirl())

        ivImage.postDelayed({
            ivImage.setImageResource(girl2.getGirl())
        }, 3000)
    }
}
```

![Activity 的2个妹子](https://user-gold-cdn.xitu.io/2019/9/4/16cfbb55d918f367?w=400&h=683&f=gif&s=336710)

### scope

scope 是个什么东西呢？我理解是使用范围，类似于生命周期，我们可以控制它的存活范围。
来个其他的栗子换个口味，先奉上效果图：

![scope 栗子](https://user-gold-cdn.xitu.io/2019/9/4/16cfbf60b8cf6cf0?w=398&h=684&f=gif&s=190436)

```Kotlin
val girlModule = module {
    scope(named("scope")) {
        scoped {
            ScopeEntity()
        }
    }
}
```

scope 必须得指定 Qualifier，创建了 scope 依赖。接下来就是 createScope 和 bindScope，在这里设置了默认值  "Scope1Activity"：

```Kotlin
class Scope1Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scope1)
        
        //创建 scope 需要指定 id 和 qualifier，getScope 需要id
        val scope1 = getKoin().createScope("scope1", named("scope"))
        //默认绑定onDestory
        bindScope(scope1)

        scope1.get<ScopeEntity>().text = "Scope1Activity"

        tvText.text = scope1.get<ScopeEntity>().text

        btn.setOnClickListener {
            startActivity(Intent(this, Scope2Activity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()

        tvText.text = getKoin().getScope("scope1").get<ScopeEntity>().text
    }
}
```
在 Scope2Activity 使用 getScope 获取并修改新值 "Scope2Activity"：

```Kotlin
class Scope2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scope1)

        val scope1 = getKoin().getScope("scope1")

        scope1.get<ScopeEntity>().text = "Scope2Activity"

        tvText.text = scope1.get<ScopeEntity>().text

    }
}
```
在 Scope1Activity 执行 onDestory 时，帮定在其中的 scope 就已经 close 了，这时再 getScope 将抛出异常：

```Kotlin
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //验证 scope销毁 延时是为了保证 Scope1Activity 已经执行 onDestory
        scope.postDelayed({
            scope.text = try {
                getKoin().getScope("scope1").get<ScopeEntity>().text
            } catch (e: Exception) {
                "scope back"
            }
        }, 3000)
    }
```

描述下流程：
 1. 在 Scope1Activity 创建了 scope1 设置值，并且绑定（bindScope）了范围。
 2. 跳转到 Scope2Activity 先显示原有值，修改后返回。由于 Scope1Activity 没有销毁，所以在 onResume 显示了新值。
 3. 返回 MainActivity ，由于 scope 特性，此时已经获取不到相应的值了。
 
